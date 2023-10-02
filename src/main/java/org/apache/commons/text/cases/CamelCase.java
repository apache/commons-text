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
package org.apache.commons.text.cases;

import java.util.ArrayList;
import java.util.List;

/**
 * Case implementation that parses and formats strings of the form 'myCamelCase'
 * <p>
 * This case separates tokens on uppercase Unicode letter characters, according to the logic in {@link java.lang.Character#toUpperCase}
 * and {@link java.lang.Character#toLowerCase} which should following the mapping present in
 * the <a href="https://www.unicode.org/Public/UCD/latest/ucd/UnicodeData.txt">Unicode data file</a>.
 * Each token begins with an
 * uppercase unicode letter, except the first token, which begins with a lowercase unicode letter character.
 * </p>
 */
public final class CamelCase implements Case {

    /** constant reusable instance of this case. */
    public static final CamelCase INSTANCE = new CamelCase();

    /**
     * Constructs new CamelCase instance.
     */
    private CamelCase() {
        super();
    }

    /**
     * Parses string tokens from a Camel Case formatted string.
     * <p>
     * Parses each character of the string parameter and creates new tokens when uppercase Unicode
     * letters are encountered. The uppercase letter is considered part of the new token. The very
     * first character of the string is an exception to this rule and must be a lowercase Unicode
     * letter. This method places no other restrictions on the content of the string. <br>
     * Note: This method should never produce empty tokens.
     * </p>
     * @param string camel case formatted string to parse
     * @return list of tokens parsed from the string
     * @throws IllegalArgumentException if the string does not begin with a Unicode lowercase letter character
     */
    @Override
    public List<String> parse(String string) {
        List<String> tokens = new ArrayList<>();
        if (string.length() == 0) {
            return tokens;
        }
        if (!Character.isLowerCase(string.codePointAt(0))) {
            throw new IllegalArgumentException(createExceptionString(string.codePointAt(0), 0, "must be a Unicode lowercase letter"));
        }
        int strLen = string.length();
        int[] tokenCodePoints = new int[strLen];
        int tokenCodePointsOffset = 0;
        for (int i = 0; i < string.length();) {
            final int codePoint = string.codePointAt(i);
            if (Character.isUpperCase(codePoint)) {
                if (tokenCodePointsOffset > 0) {
                    tokens.add(new String(tokenCodePoints, 0, tokenCodePointsOffset));
                    tokenCodePoints = new int[strLen];
                    tokenCodePointsOffset = 0;
                }
                tokenCodePoints[tokenCodePointsOffset++] = codePoint;
                i += Character.charCount(codePoint);
            } else {
                tokenCodePoints[tokenCodePointsOffset++] = codePoint;
                i += Character.charCount(codePoint);
            }
        }
        tokens.add(new String(tokenCodePoints, 0, tokenCodePointsOffset));
        return tokens;
    }

    /**
     * Formats tokens into a Camel Case string.
     * <p>
     * Iterates over tokens and creates a camel case formatted string. Each token must begin with a
     * Unicode lower/upper cased letter, which will be converted to uppercase in the output, except for the very first token,
     * which will have a lowercase first character. The remaining characters in all tokens will be
     * converted to lowercase. This Case does not support empty tokens.
     * No other restrictions are placed on token contents.
     * </p>
     * @param tokens string tokens to format into camel case
     * @return camel case formatted string
     * @throws IllegalArgumentException if any tokens are empty String or do not begin with Unicode upper/lower letter characters
     */
    @Override
    public String format(Iterable<String> tokens) {
        StringBuilder formattedString = new StringBuilder();
        int tokenIndex = 0;
        for (String token : tokens) {
            if (token.length() == 0) {
                throw new IllegalArgumentException("Unsupported empty token at index " + tokenIndex);
            }
            for (int i = 0; i < token.length();) {
                final int codePoint = token.codePointAt(i);
                int codePointFormatted = codePoint;
                if (i == 0) {
                    if (tokenIndex == 0) {
                        //token must be lowercase or lowercaseable
                        if (!Character.isLowerCase(codePoint)) {
                            codePointFormatted = Character.toLowerCase(codePoint);
                            if (codePoint == codePointFormatted) {
                                throw new IllegalArgumentException(createExceptionString(codePoint, i, "cannot be mapped to lowercase"));
                            }
                        }
                    } else {
                        //token must be uppercase or uppercaseable
                        if (!Character.isUpperCase(codePoint)) {
                            codePointFormatted = Character.toUpperCase(codePoint);
                            if (codePoint == codePointFormatted || !Character.isUpperCase(codePointFormatted)) {
                                throw new IllegalArgumentException(createExceptionString(codePoint, i, "cannot be mapped to uppercase"));
                            }
                        }
                    }
                } else {
                    //only need to force lowercase if the letter is uppercase, otherwise just add it
                    if (Character.isUpperCase(codePoint)) {
                        codePointFormatted = Character.toLowerCase(codePoint);
                        if (codePoint == codePointFormatted) {
                            throw new IllegalArgumentException(createExceptionString(codePoint, i, "cannot be mapped to lowercase"));
                        }
                    }
                }
                formattedString.appendCodePoint(codePointFormatted);
                i += Character.charCount(codePoint);
            }
            tokenIndex++;
        }
        return formattedString.toString();
    }

    private static String createExceptionString(int codePoint, int index, String suffix) {
        return "Character '" + new String(new int[] {codePoint}, 0, 1) + "' with code point " + codePoint + " at index " + index + " " + suffix;
    }
}
