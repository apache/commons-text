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

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link XmlStringLookup}.
 */
public class XmlStringLookupTest {

    private static final String DOC_PATH = "src/test/resources/document.xml";

    @Test
    public void testBadXPath() {
        assertThrows(IllegalArgumentException.class, () -> XmlStringLookup.INSTANCE.lookup("docName"));
    }

    @Test
    public void testMissingXPath() {
        assertThrows(IllegalArgumentException.class, () -> XmlStringLookup.INSTANCE.lookup(DOC_PATH + ":" + "!JUNK!"));
    }

    @Test
    public void testNull() {
        Assertions.assertNull(XmlStringLookup.INSTANCE.lookup(null));
    }

    @Test
    public void testOne() {
        final String xpath = "/root/path/to/node";
        Assertions.assertEquals("Hello World!", XmlStringLookup.INSTANCE.lookup(DOC_PATH + ":" + xpath));
    }

    @Test
    public void testToString() {
        // does not blow up and gives some kind of string.
        Assertions.assertFalse(XmlStringLookup.INSTANCE.toString().isEmpty());
    }

}
