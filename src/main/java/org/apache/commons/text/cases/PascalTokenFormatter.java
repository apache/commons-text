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

import org.apache.commons.text.TokenFormatter;

public class PascalTokenFormatter implements TokenFormatter {

    /**
     * Whether or not to set the first character of the first token as lower case.
     */
    private boolean lowerCaseFirstCharacter = false;

    public PascalTokenFormatter(boolean lowerCaseFirstCharacter) {
        this.lowerCaseFirstCharacter = lowerCaseFirstCharacter;
    }

    public PascalTokenFormatter() { }

    @Override
    public String format(char[] prior, int tokenIndex, char[] token) {
        if (token == null || token.length == 0) {
            throw new IllegalArgumentException("Unsupported empty token at index " + tokenIndex);
        }
        StringBuilder formattedString = new StringBuilder();

        for (int i = 0; i < token.length;) {
            final int codePoint = Character.codePointAt(token, i);
            //final int codePoint = token.codePointAt(i);
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
