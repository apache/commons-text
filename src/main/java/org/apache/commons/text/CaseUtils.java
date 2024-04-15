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
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Case manipulation operations on Strings that contain words.
 *
 * <p>This class tries to handle {@code null} input gracefully.
 * An exception will not be thrown for a {@code null} input.
 * Each method documents its behavior in more detail.</p>
 * Examples:
 * <pre>
 *                                                           "Two words" "foo bar" "Piñata Café"
 * camelCase        toCamelCase(str)                         "twoWords"  "fooBar"  "pinataCafe"
 * camelCase        toCamelCase(str, false, " ")             "twoWords"  "fooBar"  "piñataCafé"
 * camel_Snake      toDelimitedCase(str, false, '_')         "two_Words" "foo_Bar" "pinata_Cafe"
 * flatcase         toPascalCase(str).toLowerCase()          "twowords"  "foobar"  "pinatacafe"
 * kebab-case       toKebabCase(str)                         "two-words" "foo-bar" "pinata-cafe"
 * PascalCase       toPascalCase(str)                        "TwoWords"  "FooBar"  "PinataCafe"
 * PascalCase       toCamelCase(str, true, " ")              "TwoWords"  "FooBar"  "PiñataCafé"
 * SCREAMINGCASE    toPascalCase(str).toUpperCase()          "TWOWORDS"  "FOOBAR"  "PINATACAFE"
 * SCREAMING-KEBAB  toDelimitedCase(str, '-').toUpperCase()  "TWO-WORDS" "FOO-BAR" "PINATA-CAFE"
 * SCREAMING_SNAKE  toDelimitedCase(str, '_').toUpperCase()  "TWO_WORDS" "FOO_BAR" "PINATA_CAFE"
 * snake_case       toSnakeCase(str)                         "two_words" "foo_bar" "pinata_cafe"
 * Title_Case       toDelimitedCase(str, '_')                "Two_Words" "Foo_Bar" "Pinata_Cafe"
 * Train-Case       toDelimitedCase(str, '-')                "Two-Words" "Foo-Bar" "Pinata-Cafe"
 * </pre>
 *
 * @since 1.2
 */
public class CaseUtils {

    /**
     * Converts all the delimiter-separated words in a String into camelCase,
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
     * <p>
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
     * @param str                   the String to be converted to camelCase, may be null
     * @param capitalizeFirstLetter boolean. If true, set the first character of the first word to title case.
     * @param delimiters            set of characters to determine capitalization, null and/or empty array means whitespace
     * @return camelCase of String, {@code null} if null String input
     */
    public static String toCamelCase(String str, final boolean capitalizeFirstLetter, final char... delimiters) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        str = str.toLowerCase();
        final int strLen = str.length();
        final int[] newCodePoints = new int[strLen];
        int outOffset = 0;
        final Set<Integer> delimiterSet = toDelimiterSet(delimiters);
        boolean capitalizeNext = capitalizeFirstLetter;
        for (int index = 0; index < strLen; ) {
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

    /**
     * Converts an array of delimiters to a hash set of code points. Code point of space(32) is added
     * as the default value. The generated hash set provides O(1) lookup time.
     *
     * @param delimiters set of characters to determine capitalization, null means whitespace
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
     * Uses {@code toDelimitedCase()} to convert a string to camelCase. <br>
     * This method has different behavior from {@link #toCamelCase(String, boolean, char[])}
     * because all accented characters are normalized (accents removed). <br>
     * For example, {@code toCamelCase("Piñata Café")} will return {@code "pinataCafe"}, where
     * {@code toCamelCase("Piñata Café", false, " ")} will return {@code "piñataCafé"}. <br>
     * Converts the first alphanumeric character of the string to lower case.
     * Capitalizes first character of all other alphanumeric sequences.
     * Converts all other characters in the sequence to lower case. <br>
     * Strips all non-alphanumeric characters or sequences of non-alphanumeric characters
     * from the beginning and end of the string. <br>
     * Deletes all other non-alphanumeric characters or sequences of non-alphanumeric characters. <br>
     *
     * @param str The text to convert.
     * @return The convertedText.
     * @see #toCamelCase(String, boolean, char[])
     * @see #toDelimitedCase(String, Boolean, Character)
     */
    public static String toCamelCase(String str) {
        return StringUtils.deleteWhitespace(toDelimitedCase(str, false, ' '));
    }

    /**
     * Converts a string to Delimited Case. <br>
     * This is identical to using {@code toDelimitedCase(str, true, separator);} <br>
     * Normalizes accented characters (removes accents). <br>
     * Capitalizes the first character of any alphanumeric sequence.
     * Converts the rest of the characters in the sequence to lower case<br>
     * Strips all non-alphanumeric characters or sequences of non-alphanumeric characters
     * from the beginning and end of the string. <br>
     * Converts all other non-alphanumeric characters or sequences of non-alphanumeric characters
     * to the separator delimiter. <br>
     *
     * @param str       String: the text to convert.
     * @param separator char: The separator to use as a delimiter.
     * @return The Converted_Text.
     */
    public static String toDelimitedCase(String str, Character separator) {
        return toDelimitedCase(str, true, separator);
    }

    // todo reduce cyclomatic complexity (17) to < 10 if possible. Create a private method for sanitization?
    /**
     * Converts a string to Delimited Case. <br>
     * Normalizes accented characters (removes accents). <br>
     * If {@code capitalizeFirstLetter} is {@code true}, capitalizes the first character of the string.
     * Otherwise, converts the first character of the string to lower case. <br>
     * Capitalizes the first character of any other alphanumeric sequence.
     * Converts the rest of the characters in the sequence to lower case<br>
     * Strips all non-alphanumeric characters or sequences of non-alphanumeric characters
     * from the beginning and end of the string. <br>
     * Converts all other non-alphanumeric characters or sequences of non-alphanumeric characters
     * to the separator delimiter. <br>
     *
     * @param str                   String: the text to convert.
     * @param capitalizeFirstLetter boolean: If false, converts the first character of the string to lower case.
     * @param separator             char: The separator to use as a delimiter.
     * @return The Converted_Text.
     */
    public static String toDelimitedCase(String str, final Boolean capitalizeFirstLetter, Character separator) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        boolean capitalizeFirst = BooleanUtils.isNotFalse(capitalizeFirstLetter);
        if (separator == null) {
            return toPascalCase(str);
        }
        // todo precompile regex patterns to handle these and other regex patterns.
        String normalized = StringUtils.stripAccents(str)
                .replace("O'", "O ")
                .replace("O\u2019", "O ").trim();
        if (Pattern.matches("^[^0-9A-Za-z]*$", normalized)) {
            return "";
        }
        int startIndex = 0;
        for (int i = 0; i < normalized.length(); i++) {
            if (Pattern.matches("[0-9A-Za-z]", Character.toString(normalized.charAt(i)))) {
                startIndex = i;
                break;
            }
        }
        // todo see if TextStringBuilder is better than StringBuilder for this application.
        StringBuilder delimited = new StringBuilder();
        for (int i = startIndex; i < normalized.length(); i++) {
            if (i > startIndex && !Pattern.matches("[0-9A-Za-z'\u2019]",
                    Character.toString(normalized.charAt(i)))) {
                if (delimited.charAt(delimited.length() - 1) != separator) {
                    delimited.append(separator);
                }
            } else if (normalized.charAt(i) != '\'' && normalized.charAt(i) != '\u2019') {
                if (i == startIndex && capitalizeFirst) {
                    delimited.append(Character.toUpperCase(normalized.charAt(i)));
                } else if (i != startIndex && delimited.charAt(delimited.length() - 1) == separator) {
                    delimited.append(Character.toUpperCase(normalized.charAt(i)));
                } else {
                    delimited.append(Character.toLowerCase(normalized.charAt(i)));
                }
            }
        }
        if (delimited.charAt(delimited.length() - 1) == separator) {
            delimited.deleteCharAt(delimited.length() - 1);
        }

        return delimited.toString();
    }

    /**
     * Uses {@code toDelimitedCase()} to convert a string to kebab-case. <br>
     * Normalizes accented characters (removes accents). <br>
     * Converts all alphanumeric characters to lower case. <br>
     * Strips all non-alphanumeric characters or sequences of non-alphanumeric characters
     * from the beginning and end of the string. <br>
     * Converts all other non-alphanumeric characters or sequences of non-alphanumeric characters
     * to a single hyphen ('-'). <br>
     *
     * @param str The text to convert.
     * @return The converted-text.
     * @see #toDelimitedCase(String, Character)
     * @see StringUtils#lowerCase(String)
     */
    public static String toKebabCase(String str) {
        return StringUtils.lowerCase(toDelimitedCase(str, '-'));
    }

    /**
     * Uses {@code toDelimitedCase()} to convert a string to UpperCamelCase. <br>
     * Normalizes accented characters (removes accents). <br>
     * Capitalizes The first character of any alphanumeric sequence.
     * Converts the rest of the characters in the sequence to lower case <br>
     * Strips all non-alphanumeric characters or sequences of non-alphanumeric characters
     * from the beginning and end of the string. <br>
     * Deletes all other non-alphanumeric characters or sequences of non-alphanumeric characters. <br>
     *
     * @param str The text to convert.
     * @return The ConvertedText.
     * @see #toDelimitedCase(String, Character)
     * @see StringUtils#deleteWhitespace(String)
     */
    public static String toPascalCase(String str) {
        return StringUtils.deleteWhitespace(toDelimitedCase(str, ' '));
    }

    /**
     * Uses {@code toDelimitedCase()} to convert a string to snake_case. <br>
     * Normalizes accented characters (removes accents). <br>
     * Converts all alphanumeric characters to lower case. <br>
     * Strips all non-alphanumeric characters or sequences of non-alphanumeric characters
     * from the beginning and end of the string. <br>
     * Converts all other non-alphanumeric characters or sequences of non-alphanumeric characters
     * to a single underscore ('_'). <br>
     *
     * @param str The text to convert.
     * @return The converted_text.
     * @see #toDelimitedCase(String, Character)
     * @see StringUtils#lowerCase(String)
     */
    public static String toSnakeCase(String str) {
        return StringUtils.lowerCase(toDelimitedCase(str, '_'));
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

