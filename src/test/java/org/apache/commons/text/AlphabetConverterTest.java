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
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit tests for {@link AlphabetConverter}.
 */
public class AlphabetConverterTest {

    private static Character[] lower_case_english = {' ','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
    private static Character[] english_and_numbers = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',' ' };
    private static Character[] lower_case_english_and_numbers = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',' ' };
    private static Character[] numbers = {'0','1','2','3','4','5','6','7','8','9'};
    private static Character[] binary = {'0','1'};
    private static Character[] hebrew = {'_', ' ', '\u05e7','\u05e8','\u05d0','\u05d8','\u05d5','\u05df','\u05dd','\u05e4','\u05e9','\u05d3','\u05d2','\u05db','\u05e2','\u05d9','\u05d7','\u05dc','\u05da','\u05e3','\u05d6','\u05e1','\u05d1','\u05d4','\u05e0','\u05de','\u05e6','\u05ea','\u05e5'};
    private static Character[] empty = {};

    private static Integer[] unicode = {32,35395,35397,36302,36291,35203,35201,35215,35219,35268,97,98,99,100,101,102,103,104,105,106,107,108,109,110,1001,1002,1003,1004,1005};
    private static Integer[] lower_case_english_codepoints = {32,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122};
    private static Integer[] doNotEncodePoints = {32,97,98,99}; // space, a, b, c
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Test
    public void encodeFailureTest() throws UnsupportedEncodingException {
        thrown.expect(UnsupportedEncodingException.class);
        thrown.expectMessage("Couldn't find encoding for '3'");
        test(binary, numbers, empty, "3");
    }

    @Test
    public void binaryTest() throws UnsupportedEncodingException {
        test(binary, numbers, empty, "0", "1", "10", "11");
        test(numbers, binary, empty, "12345", "0");
        test(lower_case_english, binary, empty, "abc", "a");
    }

    @Test
    public void hebrewTest() throws UnsupportedEncodingException {
        test(hebrew, binary, empty, "\u05d0", "\u05e2", "\u05d0\u05dc\u05e3_\u05d0\u05d5\u05d4\u05d1\u05dc_\u05d1\u05d9\u05ea_\u05d6\u05d4_\u05d1\u05d9\u05ea_\u05d2\u05d9\u05de\u05dc_\u05d6\u05d4_\u05db\u05de\u05dc_\u05d2\u05d3\u05d5\u05dc");
        test(hebrew, numbers, empty, "\u05d0", "\u05e2", "\u05d0\u05dc\u05e3_\u05d0\u05d5\u05d4\u05d1\u05dc_\u05d1\u05d9\u05ea_\u05d6\u05d4_\u05d1\u05d9\u05ea_\u05d2\u05d9\u05de\u05dc_\u05d6\u05d4_\u05db\u05de\u05dc_\u05d2\u05d3\u05d5\u05dc");
        test(numbers, hebrew, empty, "123456789", "1", "5");
        test(lower_case_english, hebrew, empty, "this is a test");
    }

    @Test
    public void doNotEncodeTest() throws UnsupportedEncodingException {
        test(english_and_numbers, lower_case_english_and_numbers, lower_case_english, "1", "456", "abc", "ABC", "this will not be converted but THIS WILL");
        test(english_and_numbers, lower_case_english_and_numbers, numbers, "1", "456", "abc", "ABC", "this will be converted but 12345 and this will be");
    }

    private AlphabetConverter createJavadocExample() {
        final Character[] original = {'a','b','c','d'};
        final Character[] encoding = {'0','1','d'};
        final Character[] doNotEncode = {'d'};
        
        return AlphabetConverter.createConverterFromChars(original, encoding, doNotEncode);
    }
    
    /*
     * Test example in javadocs for consistency
     */
    @Test
    public void javadocExampleTest() throws UnsupportedEncodingException {
        final AlphabetConverter ac = createJavadocExample();
        
        Assert.assertEquals("00", ac.encode("a"));
        Assert.assertEquals("01", ac.encode("b"));
        Assert.assertEquals("0d", ac.encode("c"));
        Assert.assertEquals("d", ac.encode("d"));
        Assert.assertEquals("00010dd", ac.encode("abcd"));
    }

    @Test
    public void unexpectedEndwhileDecodingTest() throws UnsupportedEncodingException {
        final String toDecode = "00d01d0";
        
        thrown.expect(UnsupportedEncodingException.class);
        thrown.expectMessage("Unexpected end of string while decoding " + toDecode);

        final AlphabetConverter ac = createJavadocExample();
        ac.decode(toDecode);
    }

    @Test
    public void unexpectedStringWhileDecodingTest() throws UnsupportedEncodingException {
        final String toDecode = "00XX";
        
        thrown.expect(UnsupportedEncodingException.class);
        thrown.expectMessage("Unexpected string without decoding (XX) in " + toDecode);

        final AlphabetConverter ac = createJavadocExample();
        ac.decode(toDecode);
    }

    /*
     * Test constructor from code points
     */
    @Test
    public void unicodeTest() throws UnsupportedEncodingException {
        final AlphabetConverter ac = AlphabetConverter.createConverter(unicode, lower_case_english_codepoints, doNotEncodePoints);
        
        Assert.assertEquals(2, ac.getEncodedCharLength());
        
        final String original = "\u8a43\u8a45 \u8dce ab \u8dc3 c \u8983";
        final String encoded = ac.encode(original);
        final String decoded = ac.decode(encoded);
        
        Assert.assertEquals("Encoded '" + original + "' into '" + encoded + "', but decoded into '" + decoded + "'", original, decoded);
    }

    @Test
    public void noEncodingLettersTest() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Must have at least two encoding characters (excluding those in the 'do not encode' list), but has 0");

        AlphabetConverter.createConverterFromChars(english_and_numbers, numbers, numbers);
    }

    @Test
    public void onlyOneEncodingLettersTest() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Must have at least two encoding characters (excluding those in the 'do not encode' list), but has 1");

        final Character[] numbersPlusUnderscore = Arrays.copyOf(numbers, numbers.length + 1);
        numbersPlusUnderscore[numbersPlusUnderscore.length -1] = '_';

        AlphabetConverter.createConverterFromChars(english_and_numbers, numbersPlusUnderscore, numbers);
    }

    @Test
    public void missingDoNotEncodeLettersFromEncodingTest() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Can not use 'do not encode' list because encoding alphabet does not contain");

        AlphabetConverter.createConverterFromChars(english_and_numbers, lower_case_english, numbers);
    }

    @Test
    public void missingDoNotEncodeLettersFromOriginalTest() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Can not use 'do not encode' list because original alphabet does not contain");

        AlphabetConverter.createConverterFromChars(lower_case_english, english_and_numbers, numbers);
    }

    private void test(final Character[] originalChars, final Character[] encodingChars, final Character[] doNotEncodeChars, final String... strings) throws UnsupportedEncodingException {
        
        final AlphabetConverter ac = AlphabetConverter.createConverterFromChars(originalChars, encodingChars, doNotEncodeChars);
        
        final AlphabetConverter reconstructedAlphabetConverter = AlphabetConverter.createConverterFromMap(ac.getOriginalToEncoded());
        
        Assert.assertEquals(ac, reconstructedAlphabetConverter);
        Assert.assertEquals(ac.hashCode(), reconstructedAlphabetConverter.hashCode());
        Assert.assertEquals(ac.toString(), reconstructedAlphabetConverter.toString());
        Assert.assertEquals(null, ac.encode(null)); // test null conversions
        Assert.assertEquals("", ac.encode("")); // test empty conversion

        // test all the trial strings
        for (final String s : strings) {
            final String encoded = ac.encode(s);

            // test that only encoding chars are used
            final List<Character> originalEncodingChars = Arrays.asList(encodingChars);
            for (int i = 0; i < encoded.length(); i++) {
                Assert.assertTrue(originalEncodingChars.contains(encoded.charAt(i)));
            }

            final String decoded = ac.decode(encoded);

            // test that only the original alphabet is used after decoding
            final List<Character> originalCharsList = Arrays.asList(originalChars);
            for (int i = 0; i < decoded.length(); i++) {
                Assert.assertTrue(originalCharsList.contains(decoded.charAt(i)));
            }
            
            Assert.assertEquals("Encoded '" + s + "' into '" + encoded + "', but decoded into '" + decoded + "'", s, decoded);
        }
    }
}
