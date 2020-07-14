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

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Provides access to matchers defined in this package.
 *
 * @since 1.3
 */
public final class StringMatcherFactory {

    /**
     * Matches the comma character.
     */
    private static final AbstractStringMatcher.CharMatcher COMMA_MATCHER = new AbstractStringMatcher.CharMatcher(',');

    /**
     * Matches the double quote character.
     */
    private static final AbstractStringMatcher.CharMatcher DOUBLE_QUOTE_MATCHER = new AbstractStringMatcher.CharMatcher(
        '"');

    /**
     * Defines the singleton for this class.
     */
    public static final StringMatcherFactory INSTANCE = new StringMatcherFactory();

    /**
     * Matches no characters.
     */
    private static final AbstractStringMatcher.NoneMatcher NONE_MATCHER = new AbstractStringMatcher.NoneMatcher();

    /**
     * Matches the single or double quote character.
     */
    private static final AbstractStringMatcher.CharSetMatcher QUOTE_MATCHER = new AbstractStringMatcher.CharSetMatcher(
        "'\"".toCharArray());

    /**
     * Matches the double quote character.
     */
    private static final AbstractStringMatcher.CharMatcher SINGLE_QUOTE_MATCHER = new AbstractStringMatcher.CharMatcher(
        '\'');

    /**
     * Matches the space character.
     */
    private static final AbstractStringMatcher.CharMatcher SPACE_MATCHER = new AbstractStringMatcher.CharMatcher(' ');

    /**
     * Matches the same characters as StringTokenizer, namely space, tab, newline, form feed.
     */
    private static final AbstractStringMatcher.CharSetMatcher SPLIT_MATCHER = new AbstractStringMatcher.CharSetMatcher(
        " \t\n\r\f".toCharArray());

    /**
     * Matches the tab character.
     */
    private static final AbstractStringMatcher.CharMatcher TAB_MATCHER = new AbstractStringMatcher.CharMatcher('\t');

    /**
     * Matches the String trim() whitespace characters.
     */
    private static final AbstractStringMatcher.TrimMatcher TRIM_MATCHER = new AbstractStringMatcher.TrimMatcher();

    /**
     * No need to build instances for now.
     */
    private StringMatcherFactory() {
        // empty
    }

    /**
     * Creates a matcher that matches all of the given matchers in order.
     *
     * @param stringMatchers the matcher
     * @return a matcher that matches all of the given matchers in order.
     * @since 1.9
     */
    public StringMatcher andMatcher(final StringMatcher... stringMatchers) {
        final int len = ArrayUtils.getLength(stringMatchers);
        if (len == 0) {
            return NONE_MATCHER;
        }
        if (len == 1) {
            return stringMatchers[0];
        }
        return new AbstractStringMatcher.AndStringMatcher(stringMatchers);
    }

    /**
     * Constructor that creates a matcher from a character.
     *
     * @param ch the character to match, must not be null
     * @return a new Matcher for the given char
     */
    public StringMatcher charMatcher(final char ch) {
        return new AbstractStringMatcher.CharMatcher(ch);
    }

    /**
     * Constructor that creates a matcher from a set of characters.
     *
     * @param chars the characters to match, null or empty matches nothing
     * @return a new matcher for the given char[]
     */
    public StringMatcher charSetMatcher(final char... chars) {
        final int len = ArrayUtils.getLength(chars);
        if (len == 0) {
            return NONE_MATCHER;
        }
        if (len == 1) {
            return new AbstractStringMatcher.CharMatcher(chars[0]);
        }
        return new AbstractStringMatcher.CharSetMatcher(chars);
    }

    /**
     * Creates a matcher from a string representing a set of characters.
     *
     * @param chars the characters to match, null or empty matches nothing
     * @return a new Matcher for the given characters
     */
    public StringMatcher charSetMatcher(final String chars) {
        final int len = StringUtils.length(chars);
        if (len == 0) {
            return NONE_MATCHER;
        }
        if (len == 1) {
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
     * @return The split matcher
     */
    public StringMatcher splitMatcher() {
        return SPLIT_MATCHER;
    }

    /**
     * Creates a matcher from a string.
     *
     * @param chars the string to match, null or empty matches nothing
     * @return a new Matcher for the given String
     * @since 1.9
     */
    public StringMatcher stringMatcher(final char... chars) {
        final int length = ArrayUtils.getLength(chars);
        return length == 0 ? NONE_MATCHER
            : length == 1 ? new AbstractStringMatcher.CharMatcher(chars[0])
                : new AbstractStringMatcher.CharArrayMatcher(chars);
    }

    /**
     * Creates a matcher from a string.
     *
     * @param str the string to match, null or empty matches nothing
     * @return a new Matcher for the given String
     */
    public StringMatcher stringMatcher(final String str) {
        return StringUtils.isEmpty(str) ? NONE_MATCHER : stringMatcher(str.toCharArray());
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
     * @return The trim matcher
     */
    public StringMatcher trimMatcher() {
        return TRIM_MATCHER;
    }

}
