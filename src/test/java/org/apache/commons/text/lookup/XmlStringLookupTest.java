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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link XmlStringLookup}.
 */
class XmlStringLookupTest {

    static final String DATA = "Hello World!";
    static final Map<String, Boolean> EMPTY_MAP = Collections.emptyMap();
    private static final Path CURRENT_PATH = Paths.get(StringUtils.EMPTY); // NOT "."
    private static final Path ABSENT_PATH = Paths.get("does not exist at all");
    static final String DOC_DIR = "src/test/resources/org/apache/commons/text/";
    private static final String DOC_RELATIVE = DOC_DIR + "document.xml";
    private static final String DOC_ROOT = "/document.xml";

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
    void testExternalEntityOff() {
        assertThrows(IllegalArgumentException.class,
                () -> new XmlStringLookup(XmlStringLookup.DEFAULT_XML_FEATURES, EMPTY_MAP).apply(DOC_DIR + "document-entity-ref.xml:/document/content"));
    }

    @Test
    void testExternalEntityOn() {
        final String key = DOC_DIR + "document-entity-ref.xml:/document/content";
        assertEquals(DATA, new XmlStringLookup(EMPTY_MAP, EMPTY_MAP).apply(key).trim());
        assertEquals(DATA, new XmlStringLookup(EMPTY_MAP, XmlStringLookup.DEFAULT_XPATH_FEATURES).apply(key).trim());
    }

    @Test
    void testInterpolatorExternalEntityOff() {
        final StringSubstitutor stringSubstitutor = StringSubstitutor.createInterpolator();
        assertThrows(IllegalArgumentException.class, () -> stringSubstitutor.replace("${xml:" + DOC_DIR + "document-entity-ref.xml:/document/content}"));
    }

    @Test
    void testInterpolatorExternalEntityOffOverride() {
        final StringSubstitutor stringSubstitutor = StringSubstitutor.createInterpolator();
        assertEquals(DATA, stringSubstitutor.replace("${xml:secure=false:" + DOC_DIR + "document-entity-ref.xml:/document/content}").trim());
    }

    @Test
    void testInterpolatorExternalEntityOn() {
        final StringSubstitutor stringSubstitutor = StringSubstitutor.createInterpolator();
        assertThrows(IllegalArgumentException.class, () -> stringSubstitutor.replace("${xml:" + DOC_DIR + "document-entity-ref.xml:/document/content}"));
    }

    @Test
    void testInterpolatorExternalEntityOnOverride() {
        final StringSubstitutor stringSubstitutor = StringSubstitutor.createInterpolator();
        assertThrows(IllegalArgumentException.class,
                () -> stringSubstitutor.replace("${xml:secure=true:" + DOC_DIR + "document-entity-ref.xml:/document/content}"));
    }

    @Test
    void testMissingXPath() {
        assertThrows(IllegalArgumentException.class, () -> XmlStringLookup.INSTANCE.apply(DOC_RELATIVE + ":!JUNK!"));
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
