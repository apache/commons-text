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

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Case manipulation operations on Strings that contain words.
 *
 * <p>This class tries to handle {@code null} input gracefully.
 * An exception will not be thrown for a {@code null} input.
 * Each method documents its behavior in more detail.</p>
 *
 * @since 1.2
 */
public class CaseUtils {
    private static final int SPACE_CODEPOINT = 32;
    private static final Set<Integer> DEFAULT_DELIMITER_SET = Set.of(SPACE_CODEPOINT);

    /**
     * Converts all the delimiter separated words in a String into camelCase,
     * that is each word is made up of a title case character and then a series of
     * lowercase characters.
     *
     * <p>The delimiters represent a set of characters understood to separate words.
     * The first non-delimiter character after a delimiter will be capitalized. The first String
     * character may or may not be capitalized and it's determined by the user input for capitalizeFirstLetter
     * variable.</p>
     *
     * <p>A {@code null} input String returns {@code null}.</p>
     *
     * <p>A input string with only delimiter characters returns {@code ""}.</p>
     *
     * Capitalization uses the Unicode title case, normally equivalent to
     * upper case and cannot perform locale-sensitive mappings.
     *
     * <pre>
     * CaseUtils.toCamelCase(null, false)                                 = null
     * CaseUtils.toCamelCase("", false, *)                                = ""
     * CaseUtils.toCamelCase(*, false, null)                              = *
     * CaseUtils.toCamelCase(*, true, new char[0])                        = *
     * CaseUtils.toCamelCase("To.Camel.Case", false, new char[]{'.'})     = "toCamelCase"
     * CaseUtils.toCamelCase(" to @ Camel case", true, new char[]{'@'})   = "ToCamelCase"
     * CaseUtils.toCamelCase(" @to @ Camel case", false, new char[]{'@'}) = "toCamelCase"
     * CaseUtils.toCamelCase(" @", false, new char[]{'@'})                = ""
     * </pre>
     *
     * @param str  the String to be converted to camelCase, may be null
     * @param capitalizeFirstLetter boolean that determines if the first character of first word should be title case.
     * @param delimiters  set of characters to determine capitalization, null and/or empty array means whitespace
     * @return camelCase of String, {@code null} if null String input
     */
    public static String toCamelCase(String str, final boolean capitalizeFirstLetter, final char... delimiters) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }

        final int strLen = str.length();
        final int[] newCodePoints = new int[strLen];
        final Set<Integer> delimiterSet = toDelimiterSet(delimiters);

        int outOffset = 0;
        boolean capitalizeNext = capitalizeFirstLetter;
        for (int index = 0; index < strLen;) {
            final int codePoint = str.codePointAt(index);

            if (delimiterSet.contains(codePoint)) {
                capitalizeNext = outOffset != 0 || capitalizeFirstLetter;
            } else if (capitalizeNext) {
                newCodePoints[outOffset++] = Character.toTitleCase(codePoint);
                capitalizeNext = false;
            } else {
                newCodePoints[outOffset++] = Character.toLowerCase(codePoint);
            }
            index += Character.charCount(codePoint);
        }

        return new String(newCodePoints, 0, outOffset);
    }

    /**
     * Converts an array of delimiters to a hash set of code points. Code point of space(32) is added
     * as the default value. The generated hash set provides O(1) lookup time.
     *
     * @param delimiters  set of characters to determine capitalization, null means whitespace
     * @return Set<Integer>
     */
    private static Set<Integer> toDelimiterSet(final char[] delimiters) {
        if (ArrayUtils.isEmpty(delimiters)) {
            return DEFAULT_DELIMITER_SET;
        } else if (delimiters.length == 1) {
            final int delimiter = delimiters[0];  // With length == 1 there cannot be a surrogate pair!
            return delimiter == SPACE_CODEPOINT ? DEFAULT_DELIMITER_SET : Set.of(SPACE_CODEPOINT, delimiter);
        } else {
            final Set<Integer> delimiterHashSet = new HashSet<>(1 + delimiters.length);
            delimiterHashSet.add(SPACE_CODEPOINT);

            for (int index = 0; index < delimiters.length;) {
                final int codePoint = Character.codePointAt(delimiters, index);
                delimiterHashSet.add(codePoint);
                index += Character.charCount(codePoint);
            }
            return delimiterHashSet;
        }
    }

    /**
     * {@code CaseUtils} instances should NOT be constructed in
     * standard programming. Instead, the class should be used as
     * {@code CaseUtils.toCamelCase("foo bar", true, new char[]{'-'});}.
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public CaseUtils() {
    }
}
