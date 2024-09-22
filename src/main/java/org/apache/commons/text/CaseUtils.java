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
 * Note: Examples with {@code toUpperCase()} and {@code toLowerCase()} may be replaced with
 * {@code StringUtils.upperCase(str)} or {@code StringUtils.lowerCase(str)} to be null-safe.
 *
 * @since 1.2
 */
public class CaseUtils {

    /**
     * All lower ASCII alphanumeric characters.
     */
    private static final Pattern ALPHANUMERIC = Pattern.compile("[0-9A-Za-z]");

    /**
     * All lower ASCII alphanumeric characters, single quote, and right single "curly" quote (\u2019).
     */
    private static final Pattern ALPHANUMERIC_WITH_APOSTROPHE = Pattern.compile("[0-9A-Za-z'\u2019]");

    /**
     * All characters not included in ALPHANUMERIC
     */
    private static final Pattern NON_ALPHANUMERIC = Pattern.compile("^[^0-9A-Za-z]*$");

    private static final Pattern O_IRISH = Pattern.compile("(O')|(O\u2019)");


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
    public static String toCamelCase(String str, final Boolean capitalizeFirstLetter, char... delimiters) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        boolean capitalizeFirst = BooleanUtils.isTrue(capitalizeFirstLetter);
        if (ArrayUtils.isEmpty(delimiters)) {
            delimiters = new char[]{' '};
        }
        // The delimiter array in text.WordUtils.capitalize(String, char[]) is not working properly
        // in the current (1.12) build.
        // The following loop is a temporary fix.
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != ' ' && ArrayUtils.contains(delimiters, str.charAt(i))) {
                sb.append(' ');
            } else {
                int codepoint = str.codePointAt(i);
                sb.append(Character.toChars(Character.toLowerCase(codepoint)));
            }
        }
        str = sb.toString();
        delimiters = new char[]{' '};
        // End temporary fix.
        if (capitalizeFirst) {
            return StringUtils.deleteWhitespace(WordUtils.capitalize(str, delimiters));
        } else {
            return WordUtils.uncapitalize(StringUtils.deleteWhitespace(WordUtils.capitalize(str, delimiters)));
        }
    }

    /**
     * Uses {@code toDelimitedCase()} to convert a string to camelCase. <br>
     * This method has different behavior from {@link #toCamelCase(String, Boolean, char[])}
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
     * @see #toCamelCase(String, Boolean, char[])
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
        // This method sanitizes the input to run through toDelimitedEngine().
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        boolean capitalizeFirst = BooleanUtils.isNotFalse(capitalizeFirstLetter);
        if (separator == null) {
            if (capitalizeFirst) {
                return toPascalCase(str);
            } else {
                return toCamelCase(str);
            }
        }
        // return STRIP_ACCENTS_PATTERN.matcher(decomposed).replaceAll(EMPTY);
        String normalized = O_IRISH.matcher(StringUtils.stripAccents(str).trim()).replaceAll("O ");
        if (NON_ALPHANUMERIC.matcher(normalized).matches()) {
            return "";
        }
        int startIndex = 0;
        for (int i = 0; i < normalized.length(); i++) {
            if (ALPHANUMERIC.matcher(Character.toString(normalized.charAt(i))).matches()) {
                startIndex = i;
                break;
            }
        }

        return toDelimitedEngine(normalized, capitalizeFirst, separator, startIndex);
    }

    /**
     * This is the engine that generates the return value of {@link #toDelimitedCase(String, Boolean, Character)}
     *
     * @param normalized      String: the sanitized and normalized text to convert.
     * @param capitalizeFirst boolean: If false, converts the first character of the string to lower case.
     * @param separator       char: The separator to use as a delimiter.
     * @param startIndex      int: The index of the first alphanumeric character.
     * @return The Converted_Text.
     */
    private static String toDelimitedEngine(String normalized, boolean capitalizeFirst, char separator, int startIndex) {
        StringBuilder delimited = new StringBuilder();
        for (int i = startIndex; i < normalized.length(); i++) {
            if (i > startIndex &&
                !ALPHANUMERIC_WITH_APOSTROPHE.matcher(Character.toString(normalized.charAt(i))).matches()) {
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
