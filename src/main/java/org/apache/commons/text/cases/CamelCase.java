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

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Case implementation that parses and formats strings of the form 'myCamelCase'
 * <p>
 * This case separates tokens on uppercase ASCII alpha characters. Each token begins with an
 * uppercase ASCII alpha character, except the first token, which begins with a lowercase ASCII
 * alpha character.
 * </p>
 */
public class CamelCase implements Case {

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
     * Parses each character of the string parameter and creates new tokens when uppercase ASCII
     * letters are encountered. The uppercase letter is considered part of the new token. The very
     * first character of the string is an exception to this rule and must be a lowercase ASCII
     * character. This method places no other restrictions on the content of the string. <br>
     * Note: This method should never produce empty tokens.
     * </p>
     * @param string Camel Case formatted string to parse
     * @return list of tokens parsed from the string
     */
    @Override
    public List<String> parse(String string) {
        List<String> tokens = new LinkedList<>();
        if (string.length() == 0) {
            return tokens;
        }
        if (!CharUtils.isAsciiAlphaLower(string.charAt(0))) {
            throw new IllegalArgumentException("Character '" + string.charAt(0) + "' at index 0 must be an ascii lowercase letter");
        }
        int strLen = string.length();
        int[] tokenCodePoints = new int[strLen];
        int tokenCodePointsOffset = 0;
        for (int i = 0; i < string.length();) {
            final int codePoint = string.codePointAt(i);
            if (CharUtils.isAsciiAlphaUpper((char) codePoint)) {
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
     * Iterates over tokens and creates a camel case formatted string. Each token must begin with an
     * ASCII letter, which will be forced uppercase in the output, except for the very first token,
     * which will have a lowercase first character. The remaining characters in all tokens will be
     * forced lowercase. This Case does not support empty tokens.<br>
     * No other restrictions are placed on token contents.
     * </p>
     * @param tokens String tokens to format into camel case
     * @return camel case formatted string
     */
    @Override
    public String format(Iterable<String> tokens) {
        StringBuilder formattedString = new StringBuilder();
        int i = 0;
        for (String token : tokens) {
            if (token.length() == 0) {
                throw new IllegalArgumentException("Unsupported empty token at index " + i);
            }
            if (!CharUtils.isAsciiAlpha(token.charAt(0))) {
                throw new IllegalArgumentException("First character '" + token.charAt(0) + "' in token " + i + " must be an ascii letter");
            }
            String formattedToken = (i == 0 ? token.substring(0, 1).toLowerCase() : token.substring(0, 1).toUpperCase())
                    + (token.length() > 1 ? token.substring(1).toLowerCase() : StringUtils.EMPTY);
            i++;
            formattedString.append(formattedToken);
        }
        return formattedString.toString();
    }

}
