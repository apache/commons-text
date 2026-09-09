/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */

package org.apache.commons.text.lookup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.xml.XMLConstants;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.text.StringSubstitutor;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link XmlStringLookup}.
 */
class XmlStringLookupTest {

    static final String DATA = "Hello World!";
    static final Map<String, Boolean> EMPTY_MAP = Collections.emptyMap();
    static final String DOC_DIR = "src/test/resources/org/apache/commons/text/";
    private static final Path CURRENT_PATH = Paths.get(StringUtils.EMPTY); // NOT "."
    private static final Path ABSENT_PATH = Paths.get("does not exist at all");
    private static final String DOC_RELATIVE = DOC_DIR + "document.xml";
    private static final String DOC_ROOT = "/document.xml";
    private static final String DTD_DATA = "This is an external entity.";
    /** Holds the files the fixture documents reference. */
    private static final String FENCE_DIR = "src/test/resources/XmlStringLookup/";
    static final Path FENCE_ROOT = Paths.get(FENCE_DIR);
    /** Holds the documents themselves, one directory below the files they reference. */
    static final String FENCE_DOCS = FENCE_DIR + "documents/";
    private static final Path FENCE_DOCS_PATH = Paths.get(FENCE_DOCS);

    /**
     * Asserts external content does not leak
     */
    static void assertDoesNotLeak(final Supplier<String> lookup, final String external) {
        final String result = lookup.get();
        assertNotNull(result, "lookup returned null");
        assertFalse(result.contains(external), () -> "external content leaked: " + result);
    }

    /**
     * Builds a substitutor whose {@code xml} lookup is fenced. {@link StringSubstitutor#createInterpolator()} shares one
     * {@link InterpolatorStringLookup} instance JVM-wide, so its lookup map must not be mutated here.
     */
    private static StringSubstitutor fencedInterpolator(final Path... fences) {
        final Map<String, StringLookup> stringLookupMap = new HashMap<>(1);
        stringLookupMap.put(StringLookupFactory.KEY_XML, StringLookupFactory.INSTANCE.xmlStringLookup(EMPTY_MAP, fences));
        return new StringSubstitutor(StringLookupFactory.INSTANCE.interpolatorStringLookup(stringLookupMap, null, false));
    }

    /**
     * Asserts the lookup is refused because {@code fileName}, not the document itself, resolves outside the fence.
     */
    static void assertRefusesOutsideFence(final Supplier<String> lookup, final String fileName) {
        final IllegalArgumentException e = assertThrows(IllegalArgumentException.class, lookup::get);
        final String message = ExceptionUtils.getRootCauseMessage(e);
        assertTrue(message.contains(fileName) && message.contains("not in the fence"), () -> "unexpected refusal: " + message);
    }

    static void assertLookup(final StringLookup xmlStringLookup) {
        assertNotNull(xmlStringLookup);
        assertInstanceOf(XmlStringLookup.class, xmlStringLookup);
        assertEquals(DATA, xmlStringLookup.apply(DOC_RELATIVE + ":/root/path/to/node"));
        assertNull(xmlStringLookup.apply(null));
    }

    @Test
    void testBadXPath() {
        assertThrows(IllegalArgumentException.class, () -> XmlStringLookup.INSTANCE.apply("docName"));
    }

    @Test
    void testExternalDtdOff() {
        assertDoesNotLeak(
                () -> new XmlStringLookup(EMPTY_MAP, EMPTY_MAP).apply(FENCE_DOCS + "document-external-dtd.xml:/document/content"), DTD_DATA);
    }

    @Test
    void testExternalEntityOff() {
        assertDoesNotLeak(
                () -> new XmlStringLookup(EMPTY_MAP, EMPTY_MAP).apply(FENCE_DOCS + "document-entity-ref.xml:/document/content"), DATA);
    }

    @Test
    void testFenceAllowsExternalDtd() {
        // The fence covers the document and the DTD it references in the parent directory.
        assertEquals(DTD_DATA,
                new XmlStringLookup(EMPTY_MAP, EMPTY_MAP, FENCE_ROOT).apply(FENCE_DOCS + "document-external-dtd.xml:/document/content").trim());
    }

    @Test
    void testFenceAllowsExternalEntity() {
        // The fence covers the document and the entity it references in the parent directory.
        assertEquals(DATA, new XmlStringLookup(EMPTY_MAP, EMPTY_MAP, FENCE_ROOT).apply(FENCE_DOCS + "document-entity-ref.xml:/document/content").trim());
    }

    @Test
    void testFenceBlocksExternalDtdOutsideFence() {
        // The fence covers the document only, so its DTD in the parent directory is out of reach.
        final XmlStringLookup lookup = new XmlStringLookup(EMPTY_MAP, EMPTY_MAP, FENCE_DOCS_PATH);
        assertRefusesOutsideFence(() -> lookup.apply(FENCE_DOCS + "document-external-dtd.xml:/document/content"), "document.dtd");
    }

    @Test
    void testFenceBlocksExternalEntityOutsideFence() {
        // The fence covers the document only, so its entity in the parent directory is out of reach.
        final XmlStringLookup lookup = new XmlStringLookup(EMPTY_MAP, EMPTY_MAP, FENCE_DOCS_PATH);
        assertRefusesOutsideFence(() -> lookup.apply(FENCE_DOCS + "document-entity-ref.xml:/document/content"), "xml-entity.txt");
    }

    @Test
    void testFenceBlocksRemoteEntity() {
        // A remote reference names no path, so no fence can ever opt it in.
        final XmlStringLookup lookup = new XmlStringLookup(EMPTY_MAP, EMPTY_MAP, FENCE_ROOT);
        assertThrows(IllegalArgumentException.class, () -> lookup.apply(FENCE_DOCS + "document-remote-entity.xml:/document/content"));
    }

    @Test
    void testFencedInterpolatorExternalDtdOn() {
        final StringSubstitutor stringSubstitutor = fencedInterpolator(FENCE_ROOT);
        assertEquals(DTD_DATA, stringSubstitutor.replace("${xml:" + FENCE_DOCS + "document-external-dtd.xml:/document/content}").trim());
    }

    @Test
    void testFencedInterpolatorExternalEntityOn() {
        final StringSubstitutor stringSubstitutor = fencedInterpolator(FENCE_ROOT);
        assertEquals(DATA, stringSubstitutor.replace("${xml:" + FENCE_DOCS + "document-entity-ref.xml:/document/content}").trim());
    }

    @Test
    void testInterpolatorExternalDtdOff() {
        final StringSubstitutor stringSubstitutor = StringSubstitutor.createInterpolator();
        assertDoesNotLeak(() -> stringSubstitutor.replace("${xml:" + FENCE_DOCS + "document-external-dtd.xml:/document/content}"), DTD_DATA);
    }

    @Test
    void testInterpolatorExternalEntityOff() {
        final StringSubstitutor stringSubstitutor = StringSubstitutor.createInterpolator();
        assertDoesNotLeak(() -> stringSubstitutor.replace("${xml:" + FENCE_DOCS + "document-entity-ref.xml:/document/content}"), DATA);
    }

    @Test
    void testInterpolatorSecureOnBla() {
        final StringSubstitutor stringSubstitutor = StringSubstitutor.createInterpolator();
        assertThrows(IllegalArgumentException.class, () -> stringSubstitutor.replace("${xml:" + DOC_DIR + "bla.xml:/document/content}"));
    }

    @Test
    void testMissingXPath() {
        assertThrows(IllegalArgumentException.class, () -> XmlStringLookup.INSTANCE.apply(DOC_RELATIVE + ":!JUNK."));
    }

    @Test
    void testNoFeatures() {
        final String xpath = "/root/path/to/node";
        assertEquals(DATA, new XmlStringLookup(EMPTY_MAP, EMPTY_MAP).apply(DOC_RELATIVE + ":" + xpath));
        assertEquals(DATA, new XmlStringLookup(EMPTY_MAP, EMPTY_MAP).apply(DOC_RELATIVE + ":" + xpath));
        assertEquals(DATA, new XmlStringLookup(EMPTY_MAP, EMPTY_MAP, CURRENT_PATH, ABSENT_PATH).apply(DOC_RELATIVE + ":" + xpath));
        assertEquals(DATA, new XmlStringLookup(EMPTY_MAP, EMPTY_MAP, ABSENT_PATH, CURRENT_PATH).apply(DOC_RELATIVE + ":" + xpath));
        assertThrows(IllegalArgumentException.class, () -> new XmlStringLookup(EMPTY_MAP, EMPTY_MAP, ABSENT_PATH).apply(DOC_ROOT + ":" + xpath));
        assertThrows(IllegalArgumentException.class, () -> new XmlStringLookup(EMPTY_MAP, EMPTY_MAP, CURRENT_PATH).apply(DOC_ROOT + ":" + xpath));
        assertThrows(IllegalArgumentException.class, () -> new XmlStringLookup(EMPTY_MAP, EMPTY_MAP, ABSENT_PATH, CURRENT_PATH).apply(DOC_ROOT + ":" + xpath));
    }

    @Test
    void testNoFeaturesDefault() {
        final HashMap<String, Boolean> features = new HashMap<>(1);
        features.put(XMLConstants.FEATURE_SECURE_PROCESSING, Boolean.TRUE);
        assertLookup(new XmlStringLookup(EMPTY_MAP, features));
    }

    @Test
    void testNull() {
        assertNull(XmlStringLookup.INSTANCE.apply(null));
    }

    @Test
    void testOne() {
        assertLookup(XmlStringLookup.INSTANCE);
    }

    @Test
    void testToString() {
        // does not blow up and gives some kind of string.
        assertFalse(XmlStringLookup.INSTANCE.toString().isEmpty());
    }
}
