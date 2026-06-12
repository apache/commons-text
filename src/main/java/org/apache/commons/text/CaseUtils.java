/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
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
        str = str.toLowerCase();
        final int strLen = str.length();
        final int[] newCodePoints = new int[str.codePointCount(0, strLen)];
        int outOffset = 0;
        final Set<Integer> delimiterSet = toDelimiterSet(delimiters);
        boolean capitalizeNext = capitalizeFirstLetter;
        for (int index = 0; index < strLen;) {
            final int codePoint = str.codePointAt(index);

            if (delimiterSet.contains(codePoint)) {
                capitalizeNext = outOffset != 0;
                index += Character.charCount(codePoint);
            } else if (capitalizeNext || outOffset == 0 && capitalizeFirstLetter) {
                final int titleCaseCodePoint = Character.toTitleCase(codePoint);
                newCodePoints[outOffset++] = titleCaseCodePoint;
                index += Character.charCount(titleCaseCodePoint);
                capitalizeNext = false;
            } else {
                newCodePoints[outOffset++] = codePoint;
                index += Character.charCount(codePoint);
            }
        }

        return new String(newCodePoints, 0, outOffset);
    }

    private static String toDelimiterCase(String str, final char newDelimiter, final char... delimiters) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        str = str.toLowerCase();
        final int strLen = str.length();
        final int[] newCodePoints = new int[str.codePointCount(0, strLen)];
        int outOffset = 0;
        final Set<Integer> delimiterSet = toDelimiterSet(delimiters);
        boolean toAddDelimiter = false;
        for (int index = 0; index < strLen;) {
            final int codePoint = str.codePointAt(index);

            if (delimiterSet.contains(codePoint)) {
                toAddDelimiter = outOffset != 0;
                index += Character.charCount(codePoint);
            } else {
                if (toAddDelimiter) {
                    newCodePoints[outOffset++] = newDelimiter;
                    toAddDelimiter = false;
                }
                newCodePoints[outOffset++] = codePoint;
                index += Character.charCount(codePoint);
            }
        }

        return new String(newCodePoints, 0, outOffset);
    }

    /**
     * Converts all the delimiter separated words in a String into snake_case,
     * that is each word is separated by an underscore (_) character and the String
     * is converted to lower case.
     *
     * <p>The delimiters represent a set of characters understood to separate words.</p>
     *
     * <p>A {@code null} input String returns {@code null}.</p>
     *
     * <p>A input string with only delimiter characters returns {@code ""}.</p>
     *
     * <pre>
     * CaseUtils.toSnakeCase(null)                                 = null
     * CaseUtils.toSnakeCase("", *)                                = ""
     * CaseUtils.toSnakeCase(*, null)                              = *
     * CaseUtils.toSnakeCase(*, new char[0])                       = *
     * CaseUtils.toSnakeCase("To.Snake.Case", new char[]{'.'})     = "to_snake_case"
     * CaseUtils.toSnakeCase(" to @ Snake case", new char[]{'@'})  = "to_snake_case"
     * CaseUtils.toSnakeCase(" @to @ Snake case", new char[]{'@'}) = "to_snake_case"
     * CaseUtils.toSnakeCase(" @", new char[]{'@'})                = ""
     * </pre>
     *
     * @param str  the String to be converted to snake_case, may be null
     * @param delimiters  set of characters to determine a new word, null and/or empty array means whitespace
     * @return snake_case of String, {@code null} if null String input
     */
    public static String toSnakeCase(String str, final char... delimiters) {
        return toDelimiterCase(str, '_', delimiters);
    }

    /**
     * Converts all the delimiter separated words in a String into kebab-case,
     * that is each word is separated by aa dash (-) character and the String
     * is converted to lower case.
     *
     * <p>The delimiters represent a set of characters understood to separate words.</p>
     *
     * <p>A {@code null} input String returns {@code null}.</p>
     *
     * <p>A input string with only delimiter characters returns {@code ""}.</p>
     *
     * <pre>
     * CaseUtils.toKebabCase(null)                                 = null
     * CaseUtils.toKebabCase("", *)                                = ""
     * CaseUtils.toKebabCase(*, null)                              = *
     * CaseUtils.toKebabCase(*, new char[0])                       = *
     * CaseUtils.toKebabCase("To.Kebab.Case", new char[]{'.'})     = "to-kebab-case"
     * CaseUtils.toKebabCase(" to @ Kebab case", new char[]{'@'})  = "to-kebab-case"
     * CaseUtils.toKebabCase(" @to @ Kebab case", new char[]{'@'}) = "to-kebab-case"
     * CaseUtils.toKebabCase(" @", new char[]{'@'})                = ""
     * </pre>
     *
     * @param str  the String to be converted to kebab-case, may be null
     * @param delimiters  set of characters to determine a new word, null and/or empty array means whitespace
     * @return kebab-case of String, {@code null} if null String input
     */
    public static String toKebabCase(String str, final char... delimiters) {
        return toDelimiterCase(str, '-', delimiters);
    }

    /**
     * Converts an array of delimiters to a hash set of code points. Code point of space(32) is added
     * as the default value. The generated hash set provides O(1) lookup time.
     *
     * @param delimiters  set of characters to determine words, null means whitespace
     * @return Set<Integer>
     */
    private static Set<Integer> toDelimiterSet(final char[] delimiters) {
        final Set<Integer> delimiterHashSet = new HashSet<>();
        delimiterHashSet.add(Character.codePointAt(new char[]{' '}, 0));
        if (ArrayUtils.isEmpty(delimiters)) {
            return delimiterHashSet;
        }

        for (int index = 0; index < delimiters.length; index++) {
            delimiterHashSet.add(Character.codePointAt(delimiters, index));
        }
        return delimiterHashSet;
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

