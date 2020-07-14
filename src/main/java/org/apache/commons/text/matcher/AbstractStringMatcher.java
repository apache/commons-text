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

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * A matcher that determines if a character array portion matches.
 * <p>
 * Thread=safe.
 * </p>
 *
 * @since 1.3
 */
abstract class AbstractStringMatcher implements StringMatcher {

    /**
     * Matches all of the given matchers in order.
     *
     * @since 1.9
     */
    static final class AndStringMatcher extends AbstractStringMatcher {

        /**
         * Matchers in order.
         */
        private final StringMatcher[] stringMatchers;

        /**
         * Constructs a new initialized instance.
         *
         * @param stringMatchers Matchers in order.
         */
        AndStringMatcher(final StringMatcher... stringMatchers) {
            this.stringMatchers = stringMatchers;
        }

        @Override
        public int isMatch(final char[] buffer, final int start, final int bufferStart, final int bufferEnd) {
            int total = 0;
            if (stringMatchers != null) {
                int curStart = start;
                for (final StringMatcher stringMatcher : stringMatchers) {
                    if (stringMatcher != null) {
                        final int len = stringMatcher.isMatch(buffer, curStart, bufferStart, bufferEnd);
                        if (len == 0) {
                            return 0;
                        }
                        total += len;
                        curStart += len;
                    }
                }
            }
            return total;
        }

        @Override
        public int isMatch(final CharSequence buffer, final int start, final int bufferStart, final int bufferEnd) {
            int total = 0;
            if (stringMatchers != null) {
                int curStart = start;
                for (final StringMatcher stringMatcher : stringMatchers) {
                    if (stringMatcher != null) {
                        final int len = stringMatcher.isMatch(buffer, curStart, bufferStart, bufferEnd);
                        if (len == 0) {
                            return 0;
                        }
                        total += len;
                        curStart += len;
                    }
                }
            }
            return total;
        }

        @Override
        public int size() {
            int total = 0;
            if (stringMatchers != null) {
                for (final StringMatcher stringMatcher : stringMatchers) {
                    if (stringMatcher != null) {
                        total += stringMatcher.size();
                    }
                }
            }
            return total;
        }
    }

    /**
     * Matches out of a set of characters.
     * <p>
     * Thread=safe.
     * </p>
     */
    static final class CharArrayMatcher extends AbstractStringMatcher {

        /** The string to match, as a character array, implementation treats as immutable. */
        private final char[] chars;

        /** The string to match. */
        private final String string;

        /**
         * Constructs a matcher from a String.
         *
         * @param str the string to match, must not be null
         */
        CharArrayMatcher(final char... chars) {
            super();
            this.string = String.valueOf(chars);
            this.chars = chars.clone();
        }

        /**
         * Returns the number of matching characters, {@code 0} if there is no match.
         *
         * @param buffer the text content to match against, do not change
         * @param start the starting position for the match, valid for buffer
         * @param bufferStart unused
         * @param bufferEnd the end index of the active buffer, valid for buffer
         * @return The number of matching characters, zero for no match
         */
        @Override
        public int isMatch(final char[] buffer, final int start, final int bufferStart, final int bufferEnd) {
            final int len = size();
            if (start + len > bufferEnd) {
                return 0;
            }
            int j = start;
            for (int i = 0; i < len; i++, j++) {
                if (chars[i] != buffer[j]) {
                    return 0;
                }
            }
            return len;
        }

        /**
         * Returns the number of matching characters, {@code 0} if there is no match.
         *
         * @param buffer the text content to match against, do not change
         * @param start the starting position for the match, valid for buffer
         * @param bufferStart unused
         * @param bufferEnd the end index of the active buffer, valid for buffer
         * @return The number of matching characters, zero for no match
         */
        @Override
        public int isMatch(final CharSequence buffer, final int start, final int bufferStart, final int bufferEnd) {
            final int len = size();
            if (start + len > bufferEnd) {
                return 0;
            }
            int j = start;
            for (int i = 0; i < len; i++, j++) {
                if (chars[i] != buffer.charAt(j)) {
                    return 0;
                }
            }
            return len;
        }

        /**
         * Returns the size of the string to match given in the constructor.
         *
         * @since 1.9
         */
        @Override
        public int size() {
            return chars.length;
        }

        @Override
        public String toString() {
            return super.toString() + "[\"" + string + "\"]";
        }

    }

    /**
     * Matches a character.
     * <p>
     * Thread=safe.
     * </p>
     */
    static final class CharMatcher extends AbstractStringMatcher {

        /** The character to match. */
        private final char ch;

        /**
         * Constructs a matcher for a single character.
         *
         * @param ch the character to match
         */
        CharMatcher(final char ch) {
            super();
            this.ch = ch;
        }

        /**
         * Returns {@code 1} if there is a match, or {@code 0} if there is no match.
         *
         * @param buffer the text content to match against, do not change
         * @param start the starting position for the match, valid for buffer
         * @param bufferStart unused
         * @param bufferEnd unused
         * @return The number of matching characters, zero for no match
         */
        @Override
        public int isMatch(final char[] buffer, final int start, final int bufferStart, final int bufferEnd) {
            return ch == buffer[start] ? 1 : 0;
        }

        /**
         * Returns {@code 1} if there is a match, or {@code 0} if there is no match.
         *
         * @param buffer the text content to match against, do not change
         * @param start the starting position for the match, valid for buffer
         * @param bufferStart unused
         * @param bufferEnd unused
         * @return The number of matching characters, zero for no match
         */
        @Override
        public int isMatch(final CharSequence buffer, final int start, final int bufferStart, final int bufferEnd) {
            return ch == buffer.charAt(start) ? 1 : 0;
        }

        /**
         * Returns 1.
         *
         * @since 1.9
         */
        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return super.toString() + "['" + ch + "']";
        }
    }

    /**
     * Matches a set of characters.
     * <p>
     * Thread=safe.
     * </p>
     */
    static final class CharSetMatcher extends AbstractStringMatcher {

        /** The set of characters to match. */
        private final char[] chars;

        /**
         * Constructs a matcher from a character array.
         *
         * @param chars the characters to match, must not be null
         */
        CharSetMatcher(final char[] chars) {
            super();
            this.chars = chars.clone();
            Arrays.sort(this.chars);
        }

        /**
         * Returns {@code 1} if there is a match, or {@code 0} if there is no match.
         *
         * @param buffer the text content to match against, do not change
         * @param start the starting position for the match, valid for buffer
         * @param bufferStart unused
         * @param bufferEnd unused
         * @return The number of matching characters, zero for no match
         */
        @Override
        public int isMatch(final char[] buffer, final int start, final int bufferStart, final int bufferEnd) {
            return Arrays.binarySearch(chars, buffer[start]) >= 0 ? 1 : 0;
        }

        /**
         * Returns {@code 1} if there is a match, or {@code 0} if there is no match.
         *
         * @param buffer the text content to match against, do not change
         * @param start the starting position for the match, valid for buffer
         * @param bufferStart unused
         * @param bufferEnd unused
         * @return The number of matching characters, zero for no match
         */
        @Override
        public int isMatch(final CharSequence buffer, final int start, final int bufferStart, final int bufferEnd) {
            return Arrays.binarySearch(chars, buffer.charAt(start)) >= 0 ? 1 : 0;
        }

        /**
         * Returns 1.
         *
         * @since 1.9
         */
        @Override
        public int size() {
            return 1;
        }

        @Override
        public String toString() {
            return super.toString() + Arrays.toString(chars);
        }

    }

    /**
     * Matches nothing.
     * <p>
     * Thread=safe.
     * </p>
     */
    static final class NoneMatcher extends AbstractStringMatcher {

        /**
         * Constructs a new instance of {@code NoMatcher}.
         */
        NoneMatcher() {
            super();
        }

        /**
         * Always returns {@code 0}.
         *
         * @param buffer unused
         * @param start unused
         * @param bufferStart unused
         * @param bufferEnd unused
         * @return The number of matching characters, zero for no match
         */
        @Override
        public int isMatch(final char[] buffer, final int start, final int bufferStart, final int bufferEnd) {
            return 0;
        }

        /**
         * Always returns {@code 0}.
         *
         * @param buffer unused
         * @param start unused
         * @param bufferStart unused
         * @param bufferEnd unused
         * @return The number of matching characters, zero for no match
         */
        @Override
        public int isMatch(final CharSequence buffer, final int start, final int bufferStart, final int bufferEnd) {
            return 0;
        }

        /**
         * Returns 0.
         *
         * @since 1.9
         */
        @Override
        public int size() {
            return 0;
        }

    }

    /**
     * Matches whitespace as per trim().
     * <p>
     * Thread=safe.
     * </p>
     */
    static final class TrimMatcher extends AbstractStringMatcher {

        /**
         * The space character.
         */
        private static final int SPACE_INT = 32;

        /**
         * Constructs a new instance of {@code TrimMatcher}.
         */
        TrimMatcher() {
            super();
        }

        /**
         * Returns {@code 1} if there is a match, or {@code 0} if there is no match.
         *
         * @param buffer the text content to match against, do not change
         * @param start the starting position for the match, valid for buffer
         * @param bufferStart unused
         * @param bufferEnd unused
         * @return The number of matching characters, zero for no match
         */
        @Override
        public int isMatch(final char[] buffer, final int start, final int bufferStart, final int bufferEnd) {
            return buffer[start] <= SPACE_INT ? 1 : 0;
        }

        /**
         * Returns {@code 1} if there is a match, or {@code 0} if there is no match.
         *
         * @param buffer the text content to match against, do not change
         * @param start the starting position for the match, valid for buffer
         * @param bufferStart unused
         * @param bufferEnd unused
         * @return The number of matching characters, zero for no match
         */
        @Override
        public int isMatch(final CharSequence buffer, final int start, final int bufferStart, final int bufferEnd) {
            return buffer.charAt(start) <= SPACE_INT ? 1 : 0;
        }

        /**
         * Returns 1.
         *
         * @since 1.9
         */
        @Override
        public int size() {
            return 1;
        }
    }

    /**
     * Converts the given CharSequence to a char[].
     *
     * TODO Reuse Apache Commons Lang 3.11 when released.
     *
     * @param source the {@code CharSequence} to be processed
     * @return the resulting char array, never null.
     */
    static char[] toCharArray(final CharSequence source) {
        final int len = StringUtils.length(source);
        if (len == 0) {
            return ArrayUtils.EMPTY_CHAR_ARRAY;
        }
        if (source instanceof String) {
            return ((String) source).toCharArray();
        }
        final char[] array = new char[len];
        for (int i = 0; i < len; i++) {
            array[i] = source.charAt(i);
        }
        return array;
    }

    /**
     * Constructor.
     */
    protected AbstractStringMatcher() {
        super();
    }

//    /**
//     * Validates indices for {@code bufferStart <= start < bufferEnd}.
//     *
//     * @param start the starting position for the match, valid in {@code buffer}.
//     * @param bufferStart the first active index in the buffer, valid in {@code buffer}.
//     * @param bufferEnd the end index (exclusive) of the active buffer, valid in {@code buffer}.
//     */
//    void validate(final int start, final int bufferStart, final int bufferEnd) {
//        if (((bufferStart > start) || (start >= bufferEnd))) {
//            throw new IndexOutOfBoundsException(
//                String.format("bufferStart(%,d) <= start(%,d) < bufferEnd(%,d)", bufferStart, start, bufferEnd));
//        }
//    }

}
