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

/**
 * A matcher that determines if a character array portion matches.
 *
 * @since 1.3
 */
abstract class AbstractStringMatcher implements StringMatcher {

    /**
     * Class used to define a character for matching purposes.
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
    }

    /**
     * Class used to define a set of characters for matching purposes.
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

    }

    /**
     * Class used to match no characters.
     */
    static final class NoMatcher extends AbstractStringMatcher {

        /**
         * Constructs a new instance of {@code NoMatcher}.
         */
        NoMatcher() {
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
     * Class used to define a set of characters for matching purposes.
     */
    static final class StringMatcher extends AbstractStringMatcher {

        /** The string to match, as a character array, implementation treats as immutable. */
        private final char[] chars;

        /** The string to match. */
        private final String string;

        /**
         * Constructs a matcher from a String.
         *
         * @param str the string to match, must not be null
         */
        StringMatcher(final char... chars) {
            super();
            this.string = String.valueOf(chars);
            this.chars = chars.clone();
        }

        /**
         * Constructs a matcher from a String.
         *
         * @param str the string to match, must not be null
         */
        StringMatcher(final String string) {
            super();
            this.string = string;
            this.chars = string.toCharArray();
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
     * Class used to match whitespace as per trim().
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
     * Green implementation of toCharArray. TODO Reuse Apache Commons Lang 3.11 when released.
     *
     * @param source the {@code CharSequence} to be processed
     * @return the resulting char array
     */
    static char[] toCharArray(final CharSequence source) {
        final int len = source.length();
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
