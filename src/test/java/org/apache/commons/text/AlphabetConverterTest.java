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

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link AlphabetConverter}.
 */
public class AlphabetConverterTest {

    private static Character[] lowerCaseEnglish = {' ', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private static Character[] englishAndNumbers = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c',
            'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
            'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
            'T', 'U', 'V', 'W', 'X', 'Y', 'Z', ' '};
    private static Character[] lowerCaseEnglishAndNumbers = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
            'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', ' '};
    private static Character[] numbers = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static Character[] binary = {'0', '1'};
    private static Character[] hebrew = {'_', ' ', '\u05e7', '\u05e8', '\u05d0', '\u05d8', '\u05d5', '\u05df', '\u05dd',
            '\u05e4', '\u05e9', '\u05d3', '\u05d2', '\u05db', '\u05e2', '\u05d9', '\u05d7', '\u05dc', '\u05da',
            '\u05e3', '\u05d6', '\u05e1', '\u05d1', '\u05d4', '\u05e0', '\u05de', '\u05e6', '\u05ea', '\u05e5'};
    private static Character[] empty = {};

    private static Integer[] unicode = {32, 35395, 35397, 36302, 36291, 35203, 35201, 35215, 35219, 35268, 97, 98, 99,
            100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 1001, 1002, 1003, 1004, 1005};
    private static Integer[] lowerCaseEnglishCodepoints = {32, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107,
            108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122};
    private static Integer[] doNotEncodeCodepoints = {32, 97, 98, 99}; // space, a, b, c

    @Test
    public void encodeFailureTest() {
        assertThatThrownBy(() -> {
            test(binary, numbers, empty, "3");
        }).isInstanceOf(UnsupportedEncodingException.class).hasMessage("Couldn't find encoding for '3' in 3");
    }

    @Test
    public void binaryTest() throws UnsupportedEncodingException {
        test(binary, numbers, empty, "0", "1", "10", "11");
        test(numbers, binary, empty, "12345", "0");
        test(lowerCaseEnglish, binary, empty, "abc", "a");
    }

    @Test
    public void hebrewTest() throws UnsupportedEncodingException {
        test(hebrew, binary, empty, "\u05d0", "\u05e2",
                "\u05d0\u05dc\u05e3_\u05d0\u05d5\u05d4\u05d1\u05dc_\u05d1\u05d9\u05ea_\u05d6\u05d4_\u05d1\u05d9\u05ea_"
                + "\u05d2\u05d9\u05de\u05dc_\u05d6\u05d4_\u05db\u05de\u05dc_\u05d2\u05d3\u05d5\u05dc");
        test(hebrew, numbers, empty, "\u05d0", "\u05e2",
                "\u05d0\u05dc\u05e3_\u05d0\u05d5\u05d4\u05d1\u05dc_\u05d1\u05d9\u05ea_\u05d6\u05d4_\u05d1\u05d9\u05ea_"
                + "\u05d2\u05d9\u05de\u05dc_\u05d6\u05d4_\u05db\u05de\u05dc_\u05d2\u05d3\u05d5\u05dc");
        test(numbers, hebrew, empty, "123456789", "1", "5");
        test(lowerCaseEnglish, hebrew, empty, "this is a test");
    }

    @Test
    public void doNotEncodeTest() throws UnsupportedEncodingException {
        test(englishAndNumbers, lowerCaseEnglishAndNumbers, lowerCaseEnglish, "1", "456", "abc", "ABC",
                "this will not be converted but THIS WILL");
        test(englishAndNumbers, lowerCaseEnglishAndNumbers, numbers, "1", "456", "abc", "ABC",
                "this will be converted but 12345 and this will be");
    }

    private AlphabetConverter createJavadocExample() {
        final Character[] original = {'a', 'b', 'c', 'd'};
        final Character[] encoding = {'0', '1', 'd'};
        final Character[] doNotEncode = {'d'};

        return AlphabetConverter.createConverterFromChars(original, encoding, doNotEncode);
    }

    /*
     * Test example in javadocs for consistency
     */
    @Test
    public void javadocExampleTest() throws UnsupportedEncodingException {
        final AlphabetConverter ac = createJavadocExample();

        assertThat(ac.encode("a")).isEqualTo("00");
        assertThat(ac.encode("b")).isEqualTo("01");
        assertThat(ac.encode("c")).isEqualTo("0d");
        assertThat(ac.encode("d")).isEqualTo("d");
        assertThat(ac.encode("abcd")).isEqualTo("00010dd");
    }

    @Test
    public void unexpectedEndwhileDecodingTest() throws UnsupportedEncodingException {
        final String toDecode = "00d01d0";
        assertThatThrownBy(() -> {
            final AlphabetConverter ac = createJavadocExample();
            ac.decode(toDecode);
        }).isInstanceOf(UnsupportedEncodingException.class).hasMessage(
                "Unexpected end of string while decoding " + toDecode);
    }

    @Test
    public void unexpectedStringWhileDecodingTest() throws UnsupportedEncodingException {
        final String toDecode = "00XX";
        assertThatThrownBy(() -> {
            final AlphabetConverter ac = createJavadocExample();
            ac.decode(toDecode);
        }).isInstanceOf(UnsupportedEncodingException.class).hasMessage(
                "Unexpected string without decoding (XX) in " + toDecode);
    }

    /*
     * Test constructor from code points
     */
    @Test
    public void unicodeTest() throws UnsupportedEncodingException {
        final AlphabetConverter ac = AlphabetConverter.createConverter(unicode, lowerCaseEnglishCodepoints,
                doNotEncodeCodepoints);

        assertThat(ac.getEncodedCharLength()).isEqualTo(2);

        final String original = "\u8a43\u8a45 \u8dce ab \u8dc3 c \u8983";
        final String encoded = ac.encode(original);
        final String decoded = ac.decode(encoded);

        assertThat(decoded).as("Encoded '" + original + "' into '" + encoded + "', but decoded into '" + decoded + "'")
            .isEqualTo(original);
    }

    @Test
    public void noEncodingLettersTest() {
        assertThatThrownBy(() -> {
            AlphabetConverter.createConverterFromChars(englishAndNumbers, numbers, numbers);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(
                "Must have at least two encoding characters (excluding those in the 'do not encode' list), but has 0");
    }

    @Test
    public void onlyOneEncodingLettersTest() {
        assertThatThrownBy(() -> {
            final Character[] numbersPlusUnderscore = Arrays.copyOf(numbers, numbers.length + 1);
            numbersPlusUnderscore[numbersPlusUnderscore.length - 1] = '_';

            AlphabetConverter.createConverterFromChars(englishAndNumbers, numbersPlusUnderscore, numbers);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(
                "Must have at least two encoding characters (excluding those in the 'do not encode' list), but has 1");
    }

    @Test
    public void missingDoNotEncodeLettersFromEncodingTest() {
        assertThatThrownBy(() -> {
            AlphabetConverter.createConverterFromChars(englishAndNumbers, lowerCaseEnglish, numbers);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(
                "Can not use 'do not encode' list because encoding alphabet does not contain '0'");
    }

    @Test
    public void missingDoNotEncodeLettersFromOriginalTest() {
        assertThatThrownBy(() -> {
            AlphabetConverter.createConverterFromChars(lowerCaseEnglish, englishAndNumbers, numbers);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(
                "Can not use 'do not encode' list because original alphabet does not contain '0'");
    }

    private void test(final Character[] originalChars, final Character[] encodingChars,
            final Character[] doNotEncodeChars, final String... strings) throws UnsupportedEncodingException {

        final AlphabetConverter ac = AlphabetConverter.createConverterFromChars(originalChars, encodingChars,
                doNotEncodeChars);

        final AlphabetConverter reconstructedAlphabetConverter = AlphabetConverter
                .createConverterFromMap(ac.getOriginalToEncoded());

        assertThat(reconstructedAlphabetConverter).isEqualTo(ac);
        assertThat(reconstructedAlphabetConverter.hashCode()).isEqualTo(ac.hashCode());
        assertThat(reconstructedAlphabetConverter.toString()).isEqualTo(ac.toString());
        assertThat(ac.encode(null)).isNull(); // test null conversions
        assertThat(ac.encode("")).isEqualTo(""); // test empty conversion

        // test all the trial strings
        for (final String s : strings) {
            final String encoded = ac.encode(s);

            // test that only encoding chars are used
            final List<Character> originalEncodingChars = Arrays.asList(encodingChars);
            for (int i = 0; i < encoded.length(); i++) {
                assertThat(originalEncodingChars.contains(encoded.charAt(i))).isTrue();
            }

            final String decoded = ac.decode(encoded);

            // test that only the original alphabet is used after decoding
            final List<Character> originalCharsList = Arrays.asList(originalChars);
            for (int i = 0; i < decoded.length(); i++) {
                assertThat(originalCharsList.contains(decoded.charAt(i))).isTrue();
            }

            assertThat(decoded).as("Encoded '" + s + "' into '" + encoded + "', but decoded into '" + decoded + "'")
                .isEqualTo(s);
        }
    }

    @Test
    public void testCreateConverterFromCharsWithNullAndNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            final Character[] characterArray = new Character[2];
            characterArray[0] = '$';
            characterArray[1] = characterArray[0];

            AlphabetConverter.createConverterFromChars(characterArray, null, null);
        });
    }

    @Test
    public void testCreateConverterFromCharsOne() {
        final Character[] characterArray = new Character[2];
        characterArray[0] = '5';
        characterArray[1] = characterArray[0];
        final AlphabetConverter alphabetConverter = AlphabetConverter.createConverterFromChars(characterArray,
                characterArray, characterArray);

        assertThat(alphabetConverter.getEncodedCharLength()).isEqualTo(1);
    }

    @Test
    public void testCreateConverterFromMapAndEquals() {
        final Map<Integer, String> hashMap = new HashMap<>();
        final AlphabetConverter alphabetConverter = AlphabetConverter.createConverterFromMap(hashMap);
        hashMap.put(0, "CtDs");
        final AlphabetConverter alphabetConverterTwo = AlphabetConverter.createConverterFromMap(hashMap);

        assertThat(alphabetConverter.equals(alphabetConverterTwo)).isFalse();
        assertThat(alphabetConverter.getEncodedCharLength()).isEqualTo(1);
    }

    @Test
    public void testEquals() {
        final Character[] characterArray = new Character[2];
        final Character character = 'R';
        characterArray[0] = character;
        characterArray[1] = character;
        final AlphabetConverter alphabetConverter = AlphabetConverter.createConverterFromChars(characterArray,
                characterArray, characterArray);
        final Map<Integer, String> map = new HashMap<>();
        final AlphabetConverter alphabetConverterTwo = AlphabetConverter.createConverterFromMap(map);

        assertThat(alphabetConverterTwo.getEncodedCharLength()).isEqualTo(1);
        assertThat(alphabetConverter.equals(alphabetConverterTwo)).isFalse();
    }

    @Test
    public void testEqualsWithSameObject() {
        final Character[] characterArray = new Character[2];
        final Character character = 'R';
        characterArray[0] = character;
        characterArray[1] = character;
        final AlphabetConverter alphabetConverter = AlphabetConverter.createConverterFromChars(characterArray,
                characterArray, characterArray);

        assertThat(alphabetConverter.equals(alphabetConverter)).isTrue();
    }

    @Test
    public void testEqualsWithNull() {
        final Character[] characterArray = new Character[0];
        final AlphabetConverter alphabetConverter = AlphabetConverter.createConverterFromChars(characterArray, null,
                null);

        assertThat(alphabetConverter.equals(null)).isFalse();
    }

    @Test
    public void testCreateConverterFromCharsAndEquals() {
        final Character[] characterArray = new Character[2];
        final char charOne = '+';
        final Character character = '+';
        characterArray[0] = character;
        characterArray[1] = characterArray[0];
        final AlphabetConverter alphabetConverter = AlphabetConverter.createConverterFromChars(characterArray,
                characterArray, characterArray);

        assertThat(alphabetConverter.equals(charOne)).isFalse();
    }

    @Test
    public void testDecodeReturningNull() throws UnsupportedEncodingException {
        final Map<Integer, String> map = new HashMap<>();
        final AlphabetConverter alphabetConverter = AlphabetConverter.createConverterFromMap(map);
        alphabetConverter.decode(null);

        assertThat(alphabetConverter.getEncodedCharLength()).isEqualTo(1);
    }

}
