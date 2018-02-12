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

package org.apache.commons.text.matcher;

/**
 * Provides access to matchers defined in this package.
 *
 * @since 1.3
 */
public final class StringMatcherFactory {

    /**
     * Defines the singleton for this class.
     */
    public static final StringMatcherFactory INSTANCE = new StringMatcherFactory();

    /**
     * Matches the same characters as StringTokenizer, namely space, tab, newline, form feed.
     */
    private static final AbstractStringMatcher.CharSetMatcher SPLIT_MATCHER = new AbstractStringMatcher.CharSetMatcher(
            " \t\n\r\f".toCharArray());

    /**
     * Matches the comma character.
     */
    private static final AbstractStringMatcher.CharMatcher COMMA_MATCHER = new AbstractStringMatcher.CharMatcher(',');

    /**
     * Matches the tab character.
     */
    private static final AbstractStringMatcher.CharMatcher TAB_MATCHER = new AbstractStringMatcher.CharMatcher('\t');

    /**
     * Matches the space character.
     */
    private static final AbstractStringMatcher.CharMatcher SPACE_MATCHER = new AbstractStringMatcher.CharMatcher(' ');

    /**
     * Matches the String trim() whitespace characters.
     */
    private static final AbstractStringMatcher.TrimMatcher TRIM_MATCHER = new AbstractStringMatcher.TrimMatcher();

    /**
     * Matches the double quote character.
     */
    private static final AbstractStringMatcher.CharMatcher SINGLE_QUOTE_MATCHER = new AbstractStringMatcher.CharMatcher(
            '\'');

    /**
     * Matches the double quote character.
     */
    private static final AbstractStringMatcher.CharMatcher DOUBLE_QUOTE_MATCHER = new AbstractStringMatcher.CharMatcher(
            '"');

    /**
     * Matches the single or double quote character.
     */
    private static final AbstractStringMatcher.CharSetMatcher QUOTE_MATCHER = new AbstractStringMatcher.CharSetMatcher(
            "'\"".toCharArray());

    /**
     * Matches no characters.
     */
    private static final AbstractStringMatcher.NoMatcher NONE_MATCHER = new AbstractStringMatcher.NoMatcher();

    /**
     * No need to build instances for now.
     */
    private StringMatcherFactory() {
        // empty
    }

    /**
     * Constructor that creates a matcher from a character.
     *
     * @param ch
     *            the character to match, must not be null
     * @return a new Matcher for the given char
     */
    public StringMatcher charMatcher(final char ch) {
        return new AbstractStringMatcher.CharMatcher(ch);
    }

    /**
     * Constructor that creates a matcher from a set of characters.
     *
     * @param chars
     *            the characters to match, null or empty matches nothing
     * @return a new matcher for the given char[]
     */
    public StringMatcher charSetMatcher(final char... chars) {
        if (chars == null || chars.length == 0) {
            return NONE_MATCHER;
        }
        if (chars.length == 1) {
            return new AbstractStringMatcher.CharMatcher(chars[0]);
        }
        return new AbstractStringMatcher.CharSetMatcher(chars);
    }

    /**
     * Creates a matcher from a string representing a set of characters.
     *
     * @param chars
     *            the characters to match, null or empty matches nothing
     * @return a new Matcher for the given characters
     */
    public StringMatcher charSetMatcher(final String chars) {
        if (chars == null || chars.length() == 0) {
            return NONE_MATCHER;
        }
        if (chars.length() == 1) {
            return new AbstractStringMatcher.CharMatcher(chars.charAt(0));
        }
        return new AbstractStringMatcher.CharSetMatcher(chars.toCharArray());
    }

    /**
     * Returns a matcher which matches the comma character.
     *
     * @return a matcher for a comma
     */
    public StringMatcher commaMatcher() {
        return COMMA_MATCHER;
    }

    /**
     * Returns a matcher which matches the double quote character.
     *
     * @return a matcher for a double quote
     */
    public StringMatcher doubleQuoteMatcher() {
        return DOUBLE_QUOTE_MATCHER;
    }

    /**
     * Matches no characters.
     *
     * @return a matcher that matches nothing
     */
    public StringMatcher noneMatcher() {
        return NONE_MATCHER;
    }

    /**
     * Returns a matcher which matches the single or double quote character.
     *
     * @return a matcher for a single or double quote
     */
    public StringMatcher quoteMatcher() {
        return QUOTE_MATCHER;
    }

    /**
     * Returns a matcher which matches the single quote character.
     *
     * @return a matcher for a single quote
     */
    public StringMatcher singleQuoteMatcher() {
        return SINGLE_QUOTE_MATCHER;
    }

    /**
     * Returns a matcher which matches the space character.
     *
     * @return a matcher for a space
     */
    public StringMatcher spaceMatcher() {
        return SPACE_MATCHER;
    }

    /**
     * Matches the same characters as StringTokenizer, namely space, tab, newline and form feed.
     *
     * @return the split matcher
     */
    public StringMatcher splitMatcher() {
        return SPLIT_MATCHER;
    }

    /**
     * Creates a matcher from a string.
     *
     * @param str
     *            the string to match, null or empty matches nothing
     * @return a new Matcher for the given String
     */
    public StringMatcher stringMatcher(final String str) {
        if (str == null || str.length() == 0) {
            return NONE_MATCHER;
        }
        return new AbstractStringMatcher.StringMatcher(str);
    }

    /**
     * Returns a matcher which matches the tab character.
     *
     * @return a matcher for a tab
     */
    public StringMatcher tabMatcher() {
        return TAB_MATCHER;
    }

    /**
     * Matches the String trim() whitespace characters.
     *
     * @return the trim matcher
     */
    public StringMatcher trimMatcher() {
        return TRIM_MATCHER;
    }

}
