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
package org.apache.commons.text.names;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A wrapper around a String representing a Name to parse. Contains the logic
 * for handling executing Regexes on the wrapped name string.
 *
 * <p>This class is not thread-safe.</p>
 */
final class NameString {

    /**
     * Encapsulated string. Not immutable!
     */
    private String str;

    /**
     * Creates a new Name object.
     *
     * @param str encapsulated string.
     */
    NameString(String str) {
        this.str = str;
    }

    /**
     * Gets the wrapped string.
     *
     * @return wrapped string
     */
    String getWrappedString() {
        return str;
    }

    /**
     * Uses a regex to chop off and return part of the namestring.
     * There are two parts: first, it returns the matched substring,
     * and then it removes that substring from the encapsulated
     * string and normalizes it.
     *
     * @param regex matches the part of the namestring to chop off
     * @param submatchIndex which of the parenthesized submatches to use
     * @return the part of the namestring that got chopped off
     */
    String chopWithRegex(String regex, int submatchIndex) {
        String chopped = "";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(this.str);

        // workdaround for numReplacements in Java
        int numReplacements = 0;
        while (matcher.find()) {
            numReplacements++;
        }

        // recreate or the groups are gone
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(this.str);
        if (matcher.find()) {
            boolean subset = matcher.groupCount() > submatchIndex;
            if (subset) {
                this.str = this.str.replaceAll(regex, " ");
                if (numReplacements > 1) {
                    throw new NameParseException("The regex being used to find the name has multiple matches.");
                }
                this.norm();
                return matcher.group(submatchIndex).trim();
            }
        }
        return chopped;
    }

    /**
     * Flips the front and back parts of a name with one another.
     * Front and back are determined by a specified character somewhere in the
     * middle of the string.
     *
     * @param flipAroundChar the character(s) demarcating the two halves you want to flip.
     * @throws NameParseException if a regex fails or a condition is not expected
     */
    void flip(String flipAroundChar) {
        String[] parts = this.str.split(flipAroundChar);
        if (parts != null) {
            if (parts.length == 2) {
                this.str = String.format("%s %s", parts[1], parts[0]);
                this.norm();
            } else if (parts.length > 2) {
                throw new NameParseException(
                        "Can't flip around multiple '" + flipAroundChar + "' characters in namestring.");
            }
        }
    }

    /**
     * <p>Removes extra whitespace and punctuation from {@code this.str}.</p>
     *
     * <p>Strips whitespace chars from ends, strips redundant whitespace, converts
     * whitespace chars to " ".</p>
     */
    private void norm() {
        this.str = this.str.trim();
        this.str = this.str.replaceAll("\\s+", " ");
        this.str = this.str.replaceAll(",$", " ");
    }

}
