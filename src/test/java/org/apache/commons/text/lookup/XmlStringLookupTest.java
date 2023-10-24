/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import javax.xml.XMLConstants;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link XmlStringLookup}.
 */
public class XmlStringLookupTest {

    private static final String DOC_PATH = "src/test/resources/org/apache/commons/text/document.xml";

    static void assertLookup(final StringLookup xmlStringLookup) {
        assertNotNull(xmlStringLookup);
        assertTrue(xmlStringLookup instanceof XmlStringLookup);
        assertEquals("Hello World!", xmlStringLookup.lookup(DOC_PATH + ":/root/path/to/node"));
        assertNull(xmlStringLookup.lookup(null));
    }

    @Test
    public void testBadXPath() {
        assertThrows(IllegalArgumentException.class, () -> XmlStringLookup.INSTANCE.lookup("docName"));
    }

    @Test
    public void testMissingXPath() {
        assertThrows(IllegalArgumentException.class, () -> XmlStringLookup.INSTANCE.lookup(DOC_PATH + ":" + "!JUNK!"));
    }

    @Test
    public void testNoFeatures() {
        final String xpath = "/root/path/to/node";
        assertEquals("Hello World!", new XmlStringLookup(new HashMap<>()).lookup(DOC_PATH + ":" + xpath));
    }

    @Test
    public void testNoFeaturesDefault() {
        final HashMap<String, Boolean> features = new HashMap<>(1);
        features.put(XMLConstants.FEATURE_SECURE_PROCESSING, Boolean.TRUE);
        assertLookup(new XmlStringLookup(features));
    }

    @Test
    public void testNull() {
        assertNull(XmlStringLookup.INSTANCE.lookup(null));
    }

    @Test
    public void testOne() {
        assertLookup(XmlStringLookup.INSTANCE);
    }

    @Test
    public void testToString() {
        // does not blow up and gives some kind of string.
        assertFalse(XmlStringLookup.INSTANCE.toString().isEmpty());
    }

}
