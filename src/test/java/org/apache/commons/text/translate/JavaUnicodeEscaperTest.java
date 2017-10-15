/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.text.translate;

import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link JavaUnicodeEscaper}.
 */
public class JavaUnicodeEscaperTest {

    @Test
    public void testBelow() {
        final JavaUnicodeEscaper jue = JavaUnicodeEscaper.below('F');

        final String input = "ADFGZ";
        final String result = jue.translate(input);
        assertEquals("Failed to escape Unicode characters via the below method", "\\u0041\\u0044FGZ", result);
    }

    @Test
    public void testBetween() {
        final JavaUnicodeEscaper jue = JavaUnicodeEscaper.between('F', 'L');

        final String input = "ADFGZ";
        final String result = jue.translate(input);
        assertEquals("Failed to escape Unicode characters via the between method", "AD\\u0046\\u0047Z", result);
    }

    @Test
    public void testAbove() {
        final JavaUnicodeEscaper jue = JavaUnicodeEscaper.above('F');

        final String input = "ADFGZ";
        final String result = jue.translate(input);
        assertEquals("Failed to escape Unicode characters via the above method", "ADF\\u0047\\u005A", result);
    }

    @Test
    public void testToUtf16Escape() throws UnsupportedEncodingException {
        final JavaUnicodeEscaper jue = JavaUnicodeEscaper.below('F');
        // According to https://en.wikipedia.org/wiki/UTF-16#Code_points_U.2B10000..U.2B10FFFF,
        // Character ?, U+24B62, Binary Code Point 0010 0100 1011 0110 0010,
        // Binary UTF-167 1101 1000 0101 0010 1101 1111 0110 0010, UTF-16 Hex Code Units D852 DF62
        final String encoding = jue.toUtf16Escape(Integer.parseInt("024B62", 16));
        assertEquals("\\uD852\\uDF62", encoding);
    }
}
