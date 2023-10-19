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

/**
 * Util class for {@link org.apache.commons.text.cases.Case} implementations to force case
 * conversion and throw consistent and clear exceptions.
 */
public class CasesUtils {

    /**
     * Transforms a unicode code point into upper case using {@link java.lang.Character#toUpperCase} and confirms the
     * result is upper case.
     * @param codePoint
     * @return the transformed code point
     * @throws IllegalArgumentException if the converted code point cannot be mapped into an upper case character
     */
    protected static int toUpperCase(int codePoint) {
        int codePointFormatted = Character.toUpperCase(codePoint);
        if (!Character.isUpperCase(codePointFormatted)) {
            throw new IllegalArgumentException(createExceptionMessage(codePoint, " cannot be mapped to upper case"));
        }
        return codePointFormatted;
    }

    /**
     * Transforms a unicode code point into lower case using {@link java.lang.Character#toLowerCase} and confirms the
     * result is lower case.
     * @param codePoint the code point to transform
     * @return the lower case code point that corresponds to the input parameter
     * @throws IllegalArgumentException if the converted code point cannot be mapped into a lower case character
     */
    protected static int toLowerCase(int codePoint) {
        int codePointFormatted = Character.toLowerCase(codePoint);
        if (!Character.isLowerCase(codePointFormatted)) {
            throw new IllegalArgumentException(createExceptionMessage(codePoint, " cannot be mapped to lower case"));
        }
        return codePointFormatted;
    }

    /**
     * Creates an exception message that displays the unicode character as well as the hex value for clarity.
     * @param codePoint the unicode code point
     * @param suffix a string suffix for the message
     * @return the message
     */
    protected static String createExceptionMessage(int codePoint, String suffix) {
        return "Character '" + new String(new int[] { codePoint }, 0, 1) + "' with value 0x" + Integer.toHexString(codePoint) + suffix;
    }

}
