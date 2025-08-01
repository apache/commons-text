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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link XmlEncoderStringLookup}.
 */
class XmlEncoderStringLookupTest {

    private static final String DATA = "&lt;element&gt;";

    @Test
    void testDecode() {
        Assertions.assertEquals(DATA, XmlEncoderStringLookup.INSTANCE.apply("<element>"));
    }

    @Test
    void testNull() {
        Assertions.assertNull(XmlEncoderStringLookup.INSTANCE.apply(null));
    }

    @Test
    void testToString() {
        // does not blow up and gives some kind of string.
        Assertions.assertFalse(XmlEncoderStringLookup.INSTANCE.toString().isEmpty());
    }

}
