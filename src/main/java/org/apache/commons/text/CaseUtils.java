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
import org.apache.commons.lang3.StringUtils;

/**
 * Case manipulation operations on Strings that contain words.
 *
 * <p>This class tries to handle {@code null} input gracefully.
 * An exception will not be thrown for a {@code null} input.
 * Each method documents its behavior in more detail.</p>
 * Examples:
 * <pre>
 *                              "Two words" "foo bar" "Pi�ata Caf�"
 * toCamelCase(str)             "twoWords"  "fooBar"  "pinataCafe"
 * toCamelCase(str, false, " ") "twoWords"  "fooBar"  "pi�ataCaf�"
 * toCamelCase(str, true, " ")  "TwoWords"  "FooBar"  "Pi�ataCaf�"
 * ToCamelSnakeCase             "two_Words" "foo_Bar" "pinata_Cafe"
 * toFlatcase(str)              "twowords"  "foobar"  "pinatacafe"
 * toKebabCase(str)             "two-words" "foo-bar" "pinata-cafe"
 * toScreamingCase(str)         "TWOWORDS"  "FOOBAR"  "PINATACAFE"
 * toScreamingKebabCase(str)    "TWO-WORDS" "FOO-BAR" "PINATA-CAFE"
 * toScreamingSnakeCase(str)    "TWO_WORDS" "FOO_BAR" "PINATA_CAFE"
 * toSnakeCase(str)             "two_words" "foo_bar" "pinata_cafe"
 * toTitleCase(str)             "Two_Words" "Foo_Bar" "Pinata_Cafe"
 * toTrainCase(str)             "Two-Words" "Foo-Bar" "Pinata-Cafe"
 * toUpperCamelCase(str)        "TwoWords"  "FooBar"  "PinataCafe"
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
     * @param capitalizeFirstLetter boolean. If true, set the first character of the first word to title case.
     * @param delimiters  set of characters to determine capitalization, null and/or empty array means whitespace
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

    /**
     * Converts an array of delimiters to a hash set of code points. Code point of space(32) is added
     * as the default value. The generated hash set provides O(1) lookup time.
     *
     * @param delimiters  set of characters to determine capitalization, null means whitespace
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
     * Uses {@code toTitleCase()} to convert a string to camelCase. <br>
     * This method has different behavior from {@link #toCamelCase(String, boolean, char[])}
     * because all accented characters are normalized (accents removed). <br>
     * For example, {@code toCamelCase("Pi�ata Caf�")} will return {@code "pinataCafe"}, where
     * {@code toCamelCase("Pi�ata Caf�", false, " ")} will return {@code "pi�ataCaf�"}. <br>
     * The first alphanumeric character of the string is converted to lower case.
     * The first character of all other alphanumeric sequences capitalized.
     * The rest of the characters in the sequence are converted to lower case. <br>
     * Strips all non-alphanumeric characters or sequences of non-alphanumeric characters
     * from the beginning and end of the string. <br>
     * All other non-alphanumeric characters or sequences of non-alphanumeric characters are deleted. <br>
     *
     * @see #toCamelCase(String, boolean, char[])
     * @see #toTitleCase(String)
     * @param str The text to convert.
     * @return The convertedText.
     */
    public static String toCamelCase(String str) {
        if (StringUtils.isEmpty(str)) return str;
        String titleCase = toTitleCase(str).replace("_", "");
        if (titleCase.length() <= 1) return titleCase.toLowerCase();
        return titleCase.substring(0,1).toLowerCase() + titleCase.substring(1);
    }

    /**
     * Uses {@code toTitleCase()} to convert a string to camel_Snake_Case. <br>
     * All accented characters are normalized (accents removed). <br>
     * The first alphanumeric character of the string is converted to lower case.
     * The first character of all other alphanumeric sequences capitalized.
     * The rest of the characters in the sequence are converted to lower case. <br>
     * Strips all non-alphanumeric characters or sequences of non-alphanumeric characters
     * from the beginning and end of the string. <br>
     * All other non-alphanumeric characters or sequences of non-alphanumeric characters
     * are converted to a single underscore ('_'). <br>
     *
     * @see #toTitleCase(String)
     * @param str The text to convert.
     * @return The converted_Text.
     */
    public static String toCamelSnakeCase(String str) {
        if (StringUtils.isEmpty(str)) return str;
        String titleCase = toTitleCase(str);
        if (titleCase.length() <= 1) return titleCase.toLowerCase();
        return titleCase.substring(0,1).toLowerCase() + titleCase.substring(1);
    }

    /**
     * Uses {@code toTitleCase()} to convert a string to flatcase. <br>
     * All accented characters are normalized (accents removed). <br>
     * All alphanumeric characters are converted to lower case. <br>
     * All non-alphanumeric characters or sequences of non-alphanumeric characters are deleted. <br>
     *
     * @see #toTitleCase(String)
     * @param str The text to convert.
     * @return The convertedtext.
     */
    public static String toFlatCase(String str) {
        if (StringUtils.isEmpty(str)) return str;
        return toSnakeCase(str).replace("_", "");
    }

    /**
     * Uses {@code toTitleCase()} to convert a string to kebab-case. <br>
     * All accented characters are normalized (accents removed). <br>
     * All alphanumeric characters are converted to lower case. <br>
     * Strips all non-alphanumeric characters or sequences of non-alphanumeric characters
     * from the beginning and end of the string. <br>
     * All other non-alphanumeric characters or sequences of non-alphanumeric characters
     * are converted to a single hyphen ('-'). <br>
     *
     * @see #toTitleCase(String)
     * @param str The text to convert.
     * @return The converted-text.
     */
    public static String toKebabCase(String str){
        if (StringUtils.isEmpty(str)) return str;
        return toSnakeCase(str).replace("_", "-");
    }

    /**
     * Uses {@code toTitleCase()} to convert a string to SCREAMINGCASE. <br>
     * All accented characters are normalized (accents removed). <br>
     * All alphanumeric characters are converted to upper case. <br>
     * All non-alphanumeric characters or sequences of non-alphanumeric characters are deleted. <br>
     *
     * @see #toTitleCase(String)
     * @param str The text to convert.
     * @return The CONVERTEDTEXT.
     */
    public static String toScreamingCase(String str) {
        if (StringUtils.isEmpty(str)) return str;
        return toScreamingSnakeCase(str).replace("_", "");
    }

    /**
     * Uses {@code toTitleCase()} to convert a string to SCREAMING-KEBAB-CASE. <br>
     * All accented characters are normalized (accents removed). <br>
     * All alphanumeric characters are converted to upper case. <br>
     * Strips all non-alphanumeric characters or sequences of non-alphanumeric characters
     * from the beginning and end of the string. <br>
     * All other non-alphanumeric characters or sequences of non-alphanumeric characters
     * are converted to a single hyphen ('-'). <br>
     *
     * @see #toTitleCase(String)
     * @param str The text to convert.
     * @return The CONVERTED-TEXT.
     */
    public static String toScreamingKebabCase(String str){
        if (StringUtils.isEmpty(str)) return str;
        return toScreamingSnakeCase(str).replace("_", "-");
    }

    /**
     * Uses {@code toTitleCase()} to convert a string to SCREAMING_SNAKE_CASE. <br>
     * All accented characters are normalized (accents removed). <br>
     * All alphanumeric characters are converted to upper case. <br>
     * Strips all non-alphanumeric characters or sequences of non-alphanumeric characters
     * from the beginning and end of the string. <br>
     * All other non-alphanumeric characters or sequences of non-alphanumeric characters
     * are converted to a single underscore ('_'). <br>
     *
     * @see #toTitleCase(String)
     * @param str The text to convert.
     * @return The CONVERTED_TEXT.
     */
    public static String toScreamingSnakeCase(String str){
        if (StringUtils.isEmpty(str)) return str;
        return toTitleCase(str).toUpperCase();
    }

    /**
     * Uses {@code toTitleCase()} to convert a string to snake_case. <br>
     * All accented characters are normalized (accents removed). <br>
     * All alphanumeric characters are converted to lower case. <br>
     * Strips all non-alphanumeric characters or sequences of non-alphanumeric characters
     * from the beginning and end of the string. <br>
     * All other non-alphanumeric characters or sequences of non-alphanumeric characters
     * are converted to a single underscore ('_'). <br>
     *
     * @see #toTitleCase(String)
     * @param str The text to convert.
     * @return The converted_text.
     */
    public static String toSnakeCase(String str){
        if (StringUtils.isEmpty(str)) return str;
        return toTitleCase(str).toLowerCase();
    }

    /**
     * Converts a string to Title_Case. <br>
     * All accented characters are normalized (accents removed). <br>
     * The first character of any alphanumeric sequence is capitalized.
     * The rest of the characters in the sequence are converted to lower case<br>
     * Strips all non-alphanumeric characters or sequences of non-alphanumeric characters
     * from the beginning and end of the string. <br>
     * All other non-alphanumeric characters or sequences of non-alphanumeric characters
     * are converted to a single underscore ('_'). <br>
     *
     * @see #toTitleCase(String)
     * @param str The text to convert.
     * @return The Converted_Text.
     */
    public static String toTitleCase(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        String normalized = StringUtils.stripAccents(str).trim();
        if (Pattern.matches("^[^a-zA-Z0-9]*$", normalized)) {
            return "";
        }
        int startIndex = 0;
        for (int i = 0; i < normalized.length(); i++){
            if (Pattern.matches("[0-9A-Za-z]", Character.toString(normalized.charAt(i)))){
                startIndex = i;
                break;
            }
        }
        StringBuilder snake = new StringBuilder();
        for (int i = startIndex; i < normalized.length(); i++) {
            if (i > 0 && !Pattern.matches("[0-9A-Za-z'\u2019]", Character.toString(normalized.charAt(i)))) {
                if (snake.charAt(snake.length() - 1) != '_' && i != normalized.length() - 1) {
                    snake.append("_");
                }
            } else if (i == startIndex || snake.charAt(snake.length() - 1) == '_') {
                snake.append(Character.toUpperCase(normalized.charAt(i)));
            }
            else if (normalized.charAt(i) != '\'' && normalized.charAt(i) != '\u2019') {
                snake.append(Character.toLowerCase(normalized.charAt(i)));
            }
        }
        return snake.toString();
    }

    /**
     * Uses {@code toTitleCase()} to convert a string to Train-Case. <br>
     * All accented characters are normalized (accents removed). <br>
     * The first character of any alphanumeric sequence is capitalized.
     * The rest of the characters in the sequence are converted to lower case<br>
     * Strips all non-alphanumeric characters or sequences of non-alphanumeric characters
     * from the beginning and end of the string. <br>
     * All other non-alphanumeric characters or sequences of non-alphanumeric characters
     * are converted to a single hyphen ('-'). <br>
     *
     * @see #toTitleCase(String)
     * @param str The text to convert.
     * @return The Converted-Text.
     */
    public static String toTrainCase(String str) {
        if (StringUtils.isEmpty(str)) return str;
        return toTitleCase(str).replace("_", "-");
    }

    /**
     * Uses {@code toTitleCase()} to convert a string to UpperCamelCase. <br>
     * All accented characters are normalized (accents removed). <br>
     * The first character of any alphanumeric sequence is capitalized.
     * The rest of the characters in the sequence are converted to lower case <br>
     * Strips all non-alphanumeric characters or sequences of non-alphanumeric characters
     * from the beginning and end of the string. <br>
     * All other non-alphanumeric characters or sequences of non-alphanumeric characters are deleted. <br>
     *
     * @see #toTitleCase(String)
     * @param str The text to convert.
     * @return The ConvertedText.
     */
    public static String toUpperCamelCase(String str) {
        if (StringUtils.isEmpty(str)) return str;
        return toTitleCase(str).replace("_", "");
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

