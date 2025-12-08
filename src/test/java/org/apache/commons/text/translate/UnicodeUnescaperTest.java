/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.text.translate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link UnicodeEscaper}.
 */
class UnicodeUnescaperTest {

    @Test
    void testLessThanFour() {
        final UnicodeUnescaper escaper = new UnicodeUnescaper();
        final String input = "\\0047\\u006";
        assertThrows(IllegalArgumentException.class, () -> escaper.translate(input));
    }

    // Requested in LANG-507
    @Test
    void testUPlus() {
        final UnicodeUnescaper escaper = new UnicodeUnescaper();
        final String input = "\\u+0047";
        assertEquals("G", escaper.translate(input), "Failed to unescape Unicode characters with 'u+' notation");
    }

    @Test
    void testUuuuu() {
        final UnicodeUnescaper escaper = new UnicodeUnescaper();
        final String input = "\\uuuuuuuu0047";
        final String result = escaper.translate(input);
        assertEquals("G", result, "Failed to unescape Unicode characters with many 'u' characters");
    }

    @Test
    void testTooShort() {
        final UnicodeUnescaper escaper = new UnicodeUnescaper();
        final String input = "\\u";
        assertThrows(IllegalArgumentException.class, () -> escaper.translate(input));
    }
}
