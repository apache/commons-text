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
package org.apache.commons.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link AlphabetConverter}.
 */
public class AlphabetConverterTest {

    private static final Character[] LOWER_CASE_ENGLISH = { ' ', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
            't', 'u', 'v', 'w', 'x', 'y', 'z' };

    private static final Character[] ENGLISH_AND_NUMBERS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', ' ' };

    private static final Character[] LOWER_CASE_ENGLISH_AND_NUMBERS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', ' ' };

    private static final Character[] NUMBERS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    private static final Character[] BINARY = { '0', '1' };

    private static final Character[] HEBREW = { '_', ' ', '\u05e7', '\u05e8', '\u05d0', '\u05d8', '\u05d5', '\u05df', '\u05dd', '\u05e4', '\u05e9', '\u05d3',
            '\u05d2', '\u05db', '\u05e2', '\u05d9', '\u05d7', '\u05dc', '\u05da', '\u05e3', '\u05d6', '\u05e1', '\u05d1', '\u05d4', '\u05e0', '\u05de',
            '\u05e6', '\u05ea', '\u05e5' };

    private static final Integer[] UNICODE = { 32, 35395, 35397, 36302, 36291, 35203, 35201, 35215, 35219, 35268, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106,
            107, 108, 109, 110, 1001, 1002, 1003, 1004, 1005 };

    private static final Integer[] LOWER_CASE_ENGLISH_CODEPOINTS = { 32, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114,
            115, 116, 117, 118, 119, 120, 121, 122 };

    private static final Integer[] DO_NOT_ENCODE_CODEPOINTS = { 32, 97, 98, 99 }; // space, a, b, c

    private AlphabetConverter createJavadocExample() {
        final Character[] original = { 'a', 'b', 'c', 'd' };
        final Character[] encoding = { '0', '1', 'd' };
        final Character[] doNotEncode = { 'd' };

        return AlphabetConverter.createConverterFromChars(original, encoding, doNotEncode);
    }

    private void test(final Character[] originalChars, final Character[] encodingChars, final Character[] doNotEncodeChars, final String... strings)
            throws UnsupportedEncodingException {

        final AlphabetConverter ac = AlphabetConverter.createConverterFromChars(originalChars, encodingChars, doNotEncodeChars);

        final AlphabetConverter reconstructedAlphabetConverter = AlphabetConverter.createConverterFromMap(ac.getOriginalToEncoded());

        assertEquals(ac, reconstructedAlphabetConverter);
        assertEquals(ac.hashCode(), reconstructedAlphabetConverter.hashCode());
        assertEquals(ac.toString(), reconstructedAlphabetConverter.toString());
        assertNull(ac.encode(null)); // test null conversions
        assertEquals("", ac.encode("")); // test empty conversion

        // test all the trial strings
        for (final String s : strings) {
            final String encoded = ac.encode(s);

            // test that only encoding chars are used
            final List<Character> originalEncodingChars = Arrays.asList(encodingChars);
            for (int i = 0; i < encoded.length(); i++) {
                assertTrue(originalEncodingChars.contains(encoded.charAt(i)));
            }

            final String decoded = ac.decode(encoded);

            // test that only the original alphabet is used after decoding
            final List<Character> originalCharsList = Arrays.asList(originalChars);
            for (int i = 0; i < decoded.length(); i++) {
                assertTrue(originalCharsList.contains(decoded.charAt(i)));
            }

            assertEquals(s, decoded, () -> "Encoded '" + s + "' into '" + encoded + "', but decoded into '" + decoded + "'");
        }
    }

    @Test
    public void testBinaryTest() throws UnsupportedEncodingException {
        test(BINARY, NUMBERS, ArrayUtils.EMPTY_CHARACTER_OBJECT_ARRAY, "0", "1", "10", "11");
        test(NUMBERS, BINARY, ArrayUtils.EMPTY_CHARACTER_OBJECT_ARRAY, "12345", "0");
        test(LOWER_CASE_ENGLISH, BINARY, ArrayUtils.EMPTY_CHARACTER_OBJECT_ARRAY, "abc", "a");
    }

    @Test
    public void testCreateConverterFromCharsAndEquals() {
        final Character[] characterArray = new Character[2];
        final char charOne = '+';
        final char character = '+';
        characterArray[0] = character;
        characterArray[1] = characterArray[0];
        final AlphabetConverter alphabetConverter = AlphabetConverter.createConverterFromChars(characterArray, characterArray, characterArray);

        assertFalse(alphabetConverter.equals(charOne));
    }

    @Test
    public void testCreateConverterFromCharsOne() {
        final Character[] characterArray = new Character[2];
        characterArray[0] = '5';
        characterArray[1] = characterArray[0];
        final AlphabetConverter alphabetConverter = AlphabetConverter.createConverterFromChars(characterArray, characterArray, characterArray);

        assertEquals(1, alphabetConverter.getEncodedCharLength());
    }

    @Test
    public void testCreateConverterFromCharsWithNullAndNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            final Character[] characterArray = new Character[2];
            characterArray[0] = '$';
            characterArray[1] = characterArray[0];
            AlphabetConverter.createConverterFromChars(characterArray, null, null);
        });
    }

    @Test
    public void testCreateConverterFromMapAndEquals() {
        final Map<Integer, String> hashMap = new HashMap<>();
        final AlphabetConverter alphabetConverter = AlphabetConverter.createConverterFromMap(hashMap);
        hashMap.put(0, "CtDs");
        final AlphabetConverter alphabetConverterTwo = AlphabetConverter.createConverterFromMap(hashMap);
        assertFalse(alphabetConverter.equals(alphabetConverterTwo));
        assertEquals(1, alphabetConverter.getEncodedCharLength());
    }

    @Test
    public void testDecodeReturningNull() throws UnsupportedEncodingException {
        final Map<Integer, String> map = new HashMap<>();
        final AlphabetConverter alphabetConverter = AlphabetConverter.createConverterFromMap(map);
        alphabetConverter.decode(null);
        assertEquals(1, alphabetConverter.getEncodedCharLength());
    }

    @Test
    public void testDoNotEncodeTest() throws UnsupportedEncodingException {
        test(ENGLISH_AND_NUMBERS, LOWER_CASE_ENGLISH_AND_NUMBERS, LOWER_CASE_ENGLISH, "1", "456", "abc", "ABC", "this will not be converted but THIS WILL");
        test(ENGLISH_AND_NUMBERS, LOWER_CASE_ENGLISH_AND_NUMBERS, NUMBERS, "1", "456", "abc", "ABC", "this will be converted but 12345 and this will be");
    }

    @Test
    public void testEncodeFailureTest() {
        assertEquals("Couldn't find encoding for '3' in 3",
                assertThrows(UnsupportedEncodingException.class, () -> test(BINARY, NUMBERS, ArrayUtils.EMPTY_CHARACTER_OBJECT_ARRAY, "3")).getMessage());
    }

    @Test
    public void testEquals() {
        final Character[] characterArray = new Character[2];
        final char character = 'R';
        characterArray[0] = character;
        characterArray[1] = character;
        final AlphabetConverter alphabetConverter = AlphabetConverter.createConverterFromChars(characterArray, characterArray, characterArray);
        final Map<Integer, String> map = new HashMap<>();
        final AlphabetConverter alphabetConverterTwo = AlphabetConverter.createConverterFromMap(map);

        assertEquals(1, alphabetConverterTwo.getEncodedCharLength());
        assertFalse(alphabetConverter.equals(alphabetConverterTwo));
    }

    @Test
    public void testEqualsWithNull() {
        final Character[] characterArray = ArrayUtils.EMPTY_CHARACTER_OBJECT_ARRAY;
        final AlphabetConverter alphabetConverter = AlphabetConverter.createConverterFromChars(characterArray, null, null);

        assertFalse(alphabetConverter.equals(null));
    }

    @Test
    public void testEqualsWithSameObject() {
        final Character[] characterArray = new Character[2];
        final char character = 'R';
        characterArray[0] = character;
        characterArray[1] = character;
        final AlphabetConverter alphabetConverter = AlphabetConverter.createConverterFromChars(characterArray, characterArray, characterArray);

        assertTrue(alphabetConverter.equals(alphabetConverter));
    }

    @Test
    public void testHebrewTest() throws UnsupportedEncodingException {
        test(HEBREW, BINARY, ArrayUtils.EMPTY_CHARACTER_OBJECT_ARRAY, "\u05d0", "\u05e2",
                "\u05d0\u05dc\u05e3_\u05d0\u05d5\u05d4\u05d1\u05dc_\u05d1\u05d9\u05ea_\u05d6\u05d4_\u05d1\u05d9\u05ea_"
              + "\u05d2\u05d9\u05de\u05dc_\u05d6\u05d4_\u05db\u05de\u05dc_\u05d2\u05d3\u05d5\u05dc");
        test(HEBREW, NUMBERS, ArrayUtils.EMPTY_CHARACTER_OBJECT_ARRAY, "\u05d0", "\u05e2",
                "\u05d0\u05dc\u05e3_\u05d0\u05d5\u05d4\u05d1\u05dc_\u05d1\u05d9\u05ea_\u05d6\u05d4_\u05d1\u05d9\u05ea_"
              + "\u05d2\u05d9\u05de\u05dc_\u05d6\u05d4_\u05db\u05de\u05dc_\u05d2\u05d3\u05d5\u05dc");
        test(NUMBERS, HEBREW, ArrayUtils.EMPTY_CHARACTER_OBJECT_ARRAY, "123456789", "1", "5");
        test(LOWER_CASE_ENGLISH, HEBREW, ArrayUtils.EMPTY_CHARACTER_OBJECT_ARRAY, "this is a test");
    }

    /*
     * Test example in javadocs for consistency
     */
    @Test
    public void testJavadocExampleTest() throws UnsupportedEncodingException {
        final AlphabetConverter ac = createJavadocExample();

        assertEquals("00", ac.encode("a"));
        assertEquals("01", ac.encode("b"));
        assertEquals("0d", ac.encode("c"));
        assertEquals("d", ac.encode("d"));
        assertEquals("00010dd", ac.encode("abcd"));
    }

    @Test
    public void testMissingDoNotEncodeLettersFromEncodingTest() {
        assertEquals("Can not use 'do not encode' list because encoding alphabet does not contain '0'",
                assertThrows(IllegalArgumentException.class, () -> AlphabetConverter.createConverterFromChars(ENGLISH_AND_NUMBERS, LOWER_CASE_ENGLISH, NUMBERS))
                        .getMessage());
    }

    @Test
    public void testMissingDoNotEncodeLettersFromOriginalTest() {
        assertEquals("Can not use 'do not encode' list because original alphabet does not contain '0'",
                assertThrows(IllegalArgumentException.class, () -> AlphabetConverter.createConverterFromChars(LOWER_CASE_ENGLISH, ENGLISH_AND_NUMBERS, NUMBERS))
                        .getMessage());
    }

    @Test
    public void testNoEncodingLettersTest() {
        assertEquals("Must have at least two encoding characters (excluding those in the 'do not encode' list), but has 0",
                assertThrows(IllegalArgumentException.class, () -> AlphabetConverter.createConverterFromChars(ENGLISH_AND_NUMBERS, NUMBERS, NUMBERS))
                        .getMessage());
    }

    @Test
    public void testOnlyOneEncodingLettersTest() {
        assertEquals("Must have at least two encoding characters (excluding those in the 'do not encode' list), but has 1",
                assertThrows(IllegalArgumentException.class, () -> {
                    final Character[] numbersPlusUnderscore = Arrays.copyOf(NUMBERS, NUMBERS.length + 1);
                    numbersPlusUnderscore[numbersPlusUnderscore.length - 1] = '_';

                    AlphabetConverter.createConverterFromChars(ENGLISH_AND_NUMBERS, numbersPlusUnderscore, NUMBERS);
                }).getMessage());
    }

    @Test
    public void testUnexpectedEndWhileDecodingTest() {
        final String toDecode = "00d01d0";
        assertEquals("Unexpected end of string while decoding " + toDecode,
                assertThrows(UnsupportedEncodingException.class, () -> createJavadocExample().decode(toDecode)).getMessage());
    }

    @Test
    public void testUnexpectedStringWhileDecodingTest() {
        final String toDecode = "00XX";
        assertEquals("Unexpected string without decoding (XX) in " + toDecode,
                assertThrows(UnsupportedEncodingException.class, () -> createJavadocExample().decode(toDecode)).getMessage());
    }

    /**
     * Test constructor from code points
     */
    @Test
    public void testUnicodeTest() throws UnsupportedEncodingException {
        final AlphabetConverter ac = AlphabetConverter.createConverter(UNICODE, LOWER_CASE_ENGLISH_CODEPOINTS, DO_NOT_ENCODE_CODEPOINTS);
        assertEquals(2, ac.getEncodedCharLength());
        final String original = "\u8a43\u8a45 \u8dce ab \u8dc3 c \u8983";
        final String encoded = ac.encode(original);
        final String decoded = ac.decode(encoded);
        assertEquals(original, decoded, () -> "Encoded '" + original + "' into '" + encoded + "', but decoded into '" + decoded + "'");
    }

}
