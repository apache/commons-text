/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.commons.text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Handles converting from one string case to another (e.g. camel case to snake case).
 * @since 1.13.0
 */
public class CasedString {
    /** the string the the cased format. */
    private final String string;
    /** the case of the string. */
    private final StringCase stringCase;

    /**
     * A method to join camel string fragments together.
     */
    private static final Function<String[], String> CAMEL_JOINER = a -> {
        StringBuilder sb = new StringBuilder(a[0].toLowerCase(Locale.ROOT));

        for (int i = 1; i < a.length; i++) {
            sb.append(WordUtils.capitalize(a[i].toLowerCase(Locale.ROOT)));
        }
        return sb.toString();
    };

    /**
     * An enumeration of supported string cases.  These cases tag strings as having a specific format.
     */
    public enum StringCase {
        /**
         * Camel case tags strings like 'CamelCase' or 'camelCase'. This conversion forces the first character to
         * lower case. If specific capitalization rules are required use {@code WordUtils.capitalize()} to set the first
         * character of the string.
         */
        CAMEL(Character::isUpperCase, true, CAMEL_JOINER),
        /**
         * Snake case tags strings like 'Snake_Case'.  This conversion does not change the capitalization of any characters
         * in the string.  If specific capitalization is required use {@code String.upperCase}, {@code String.upperCase},
         * {@code WordUtils.capitalize()}, or {@code WordUtils.uncapitalize()} as required.
         */
        SNAKE(c -> c == '_', false, a -> String.join("_", a)),
        /**
         * Kebab case tags strings like 'kebab-case'.  This conversion does not change the capitalization of any characters
         * in the string.  If specific capitalization is required use {@code String.upperCase}, {@code String.upperCase},
         * {@code WordUtils.capitalize()}, or {@code WordUtils.uncapitalize()} as required.
         */
        KEBAB(c -> c == '-', false, a -> String.join("-", a)),

        /**
         * Phrase case tags phrases of words like 'phrase case'. This conversion does not change the capitalization of any characters
         * in the string.  If specific capitalization is required use {@code String.upperCase}, {@code String.upperCase},
         * {@code WordUtils.capitalize()}, or {@code WordUtils.uncapitalize()} as required.
         */
        PHRASE(Character::isWhitespace, false, a -> String.join(" ", a)),

        /**
         * Dot case tags phrases of words like 'phrase.case'. This conversion does not change the capitalization of any characters
         * in the string.  If specific capitalization is required use {@code String.upperCase}, {@code String.upperCase},
         * {@code WordUtils.capitalize()}, or {@code WordUtils.uncapitalize()} as required.
         */
        DOT(c -> c == '.', false, a -> String.join(".", a));

        /** test for split position character. */
        private final Predicate<Character> splitter;
        /** if {@code true} split position character will be preserved in following segment. */
        private final boolean preserveSplit;
        /** a function to joining the segments into this case type. */
        private final Function<String[], String> joiner;

        /**
         * Defines a String Case.
         * @param splitter The predicate that determines when a new word in the cased string begins.
         * @param preserveSplit if {@code true} the character that the splitter detected is preserved as the first character of the new word.
         * @param joiner The function to merge a list of strings into the cased String.
         */
        StringCase(final Predicate<Character> splitter, final boolean preserveSplit, final Function<String[], String> joiner) {
            this.splitter = splitter;
            this.preserveSplit = preserveSplit;
            this.joiner = joiner;
        }

        /**
         * Creates a cased string from a collection of segments.
         * @param segments the segments to create the CasedString from.
         * @return a CasedString
         */
        public String assemble(String[] segments) {
            return segments.length == 0 ? null : this.joiner.apply(segments);
        }

        /**
         * Returns an array of each of the segments in this CasedString.  Segments are defined as the strings between
         * the separators in the CasedString.  for the CAMEL case the segments are determined by the presence of a capital letter.
         * @return the array of Strings that are segments of the cased string.
         */
        public String[] getSegments(String string) {
            if (string == null) {
                return new String[0];
            }
            if (string.isEmpty()) {
                return new String[]{""};
            }
            List<String> lst = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            for (char c : string.toCharArray()) {
                if (splitter.test(c)) {
                    if (sb.length() > 0) {
                        lst.add(sb.toString());
                        sb.setLength(0);
                    }
                    if (preserveSplit) {
                        sb.append(c);
                    }
                } else {
                    sb.append(c);
                }
            }
            if (sb.length() > 0) {
                lst.add(sb.toString());
            }
            return lst.toArray(new String[0]);
        }
    }

    /**
     * A representation of a cased string and the identified case of that string.
     * @param stringCase The {@code StringCase} that the {@code string} argument is in.
     * @param string The string.
     */
    public CasedString(StringCase stringCase, String string) {
        this.string = string == null ? null : stringCase.assemble(stringCase.getSegments(string.trim()));
        this.stringCase = stringCase;
    }

    /**
     * Returns an array of each of the segments in this CasedString.  Segments are defined as the strings between
     * the separators in the CasedString.  for the CAMEL case the segments are determined by the presence of a capital letter.
     * @return the array of Strings that are segments of the cased string.
     */
    public String[] getSegments() {
        return stringCase.getSegments(string);
    }

    /**
     * Converts this cased string into a {@code String} of another format.
     * The upper/lower case of the characters within the string are not modified.
     * @param stringCase THe fomrat to convert to.
     * @return the String current string represented in the new format.
     */
    public String toCase(StringCase stringCase) {
        if (stringCase == this.stringCase) {
            return string;
        }
        return string == null ? null : stringCase.joiner.apply(getSegments());
    }

    /**
     * Returns the string representation provided in the constructor.
     * @return the string representation.
     */
    @Override
    public String toString() {
        return string;
    }
}
