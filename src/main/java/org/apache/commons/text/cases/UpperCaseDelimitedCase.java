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
 * Case implementation which parses and formats strings where tokens are delimited by upper case characters.
 */
public class UpperCaseDelimitedCase implements Case {

    /** Flag to indicate whether the first character of the first token should be upper case. */
    private boolean lowerCaseFirstCharacter = false;

    /**
     * Constructs a new UpperCaseDelimitedCase instance.
     */
    UpperCaseDelimitedCase(boolean lowerCaseFirstCharacter) {
        this.lowerCaseFirstCharacter = lowerCaseFirstCharacter;
    }

    /**
     * Parses a string into tokens.
     * <p>
     * String characters are iterated over and when an upper case Unicode character is
     * encountered, that character starts a new token, with the character
     * itself included in the token. This method never returns empty tokens.
     * </p>
     *
     * @param string the string to parse
     * @return the list of tokens found in the string
     */
    @Override
    public List<String> parse(String string) {
        List<String> tokens = new ArrayList<>();
        if (string == null || string.isEmpty()) {
            return tokens;
        }
        if (lowerCaseFirstCharacter) {
            toLowerCase(string.codePointAt(0));
        } else {
            toUpperCase(string.codePointAt(0));
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
     * Formats string tokens into a single string where each token begins with an upper case
     * character, followed by lower case or non cased characters.
     * <p>
     * Iterates the tokens and formats each one into a token where the first character of the token
     * is forced upper case in the output. The remaining characters of the token will be lower case
     * or non cased. Conversions to lower case are attempted and any conversion that is not possible
     * throws an exception. Any other characters in the token are returned as-is. Empty tokens are
     * not supported and will cause an exception to be thrown.
     * </p>
     *
     * @param tokens the string tokens to be formatted
     * @return the formatted string
     * @throws IllegalArgumentException if 1) any token is empty 2) any token begins with a
     * character that cannot be mapped to upper case, or 3) any token contains an upper or title case
     * character that cannot be mapped to lower case.
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
                if (i == 0 && tokenIndex == 0 && lowerCaseFirstCharacter) {
                    codePointFormatted = toLowerCase(codePoint);
                } else if (i == 0) {
                    codePointFormatted = toUpperCase(codePoint);
                } else if (Character.isUpperCase(codePointFormatted) || Character.isTitleCase(codePointFormatted)) {
                    //if character is title or upper case, it must be converted to lower
                    codePointFormatted = toLowerCase(codePoint);
                }
                formattedString.appendCodePoint(codePointFormatted);
                i += Character.charCount(codePoint);
            }
            tokenIndex++;
        }
        return formattedString.toString();
    }

    /**
     * Transforms a Unicode code point into upper case using {@link java.lang.Character#toUpperCase} and confirms the
     * result is upper case.
     *
     * @param codePoint the code point to upper case
     * @return the transformed code point
     * @throws IllegalArgumentException if the converted code point cannot be mapped into an upper case character
     */
    private static int toUpperCase(int codePoint) {
        int codePointFormatted = Character.toUpperCase(codePoint);
        if (!Character.isUpperCase(codePointFormatted)) {
            throw new IllegalArgumentException(createExceptionMessage(codePoint, " cannot be mapped to upper case"));
        }
        return codePointFormatted;
    }

    /**
     * Transforms a Unicode code point into lower case using {@link java.lang.Character#toLowerCase} and confirms the
     * result is lower case.
     *
     * @param codePoint the code point to lower case
     * @return the lower case code point that corresponds to the input parameter
     * @throws IllegalArgumentException if the converted code point cannot be mapped into a lower case character
     */
    private static int toLowerCase(int codePoint) {
        int codePointFormatted = Character.toLowerCase(codePoint);
        if (!Character.isLowerCase(codePointFormatted)) {
            throw new IllegalArgumentException(createExceptionMessage(codePoint, " cannot be mapped to lower case"));
        }
        return codePointFormatted;
    }

    /**
     * Creates an exception message that displays the Unicode character as well as the hex value for clarity.
     *
     * @param codePoint the Unicode code point to transform
     * @param suffix a string suffix for the message
     * @return the message
     */
    private static String createExceptionMessage(int codePoint, String suffix) {
        return "Character '" + new String(new int[] { codePoint }, 0, 1) + "' with value 0x" + Integer.toHexString(codePoint) + suffix;
    }

}
