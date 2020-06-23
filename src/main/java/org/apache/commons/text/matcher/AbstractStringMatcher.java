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

/**
 * A matcher class that can be queried to determine if a character array portion matches.
 * <p>
 * This class comes complete with various factory methods. If these do not suffice, you can subclass and implement your
 * own matcher.
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
         * Constructor that creates a matcher that matches a single character.
         *
         * @param ch
         *            the character to match
         */
        CharMatcher(final char ch) {
            super();
            this.ch = ch;
        }

        /**
         * Returns whether or not the given character matches.
         *
         * @param buffer
         *            the text content to match against, do not change
         * @param start
         *            the starting position for the match, valid for buffer
         * @param bufferStart
         *            the first active index in the buffer, valid for buffer
         * @param bufferEnd
         *            the end index of the active buffer, valid for buffer
         * @return The number of matching characters, zero for no match
         */
        @Override
        public int isMatch(final char[] buffer, final int start, final int bufferStart, final int bufferEnd) {
            return ch == buffer[start] ? 1 : 0;
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
         * Constructor that creates a matcher from a character array.
         *
         * @param chars
         *            the characters to match, must not be null
         */
        CharSetMatcher(final char[] chars) {
            super();
            this.chars = chars.clone();
            Arrays.sort(this.chars);
        }

        /**
         * Returns whether or not the given character matches.
         *
         * @param buffer
         *            the text content to match against, do not change
         * @param start
         *            the starting position for the match, valid for buffer
         * @param bufferStart
         *            the first active index in the buffer, valid for buffer
         * @param bufferEnd
         *            the end index of the active buffer, valid for buffer
         * @return The number of matching characters, zero for no match
         */
        @Override
        public int isMatch(final char[] buffer, final int start, final int bufferStart, final int bufferEnd) {
            return Arrays.binarySearch(chars, buffer[start]) >= 0 ? 1 : 0;
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
         * Always returns {@code false}.
         *
         * @param buffer
         *            the text content to match against, do not change
         * @param start
         *            the starting position for the match, valid for buffer
         * @param bufferStart
         *            the first active index in the buffer, valid for buffer
         * @param bufferEnd
         *            the end index of the active buffer, valid for buffer
         * @return The number of matching characters, zero for no match
         */
        @Override
        public int isMatch(final char[] buffer, final int start, final int bufferStart, final int bufferEnd) {
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

        /** The string to match, as a character array. */
        private final char[] chars;

        /**
         * Constructor that creates a matcher from a String.
         *
         * @param str
         *            the string to match, must not be null
         */
        StringMatcher(final String str) {
            super();
            chars = str.toCharArray();
        }

        /**
         * Returns whether or not the given text matches the stored string.
         *
         * @param buffer
         *            the text content to match against, do not change
         * @param start
         *            the starting position for the match, valid for buffer
         * @param bufferStart
         *            the first active index in the buffer, valid for buffer
         * @param bufferEnd
         *            the end index of the active buffer, valid for buffer
         * @return The number of matching characters, zero for no match
         */
        @Override
        public int isMatch(final char[] buffer, int start, final int bufferStart, final int bufferEnd) {
            final int len = size();
            if (start + len > bufferEnd) {
                return 0;
            }
            for (int i = 0; i < len; i++, start++) {
                if (chars[i] != buffer[start]) {
                    return 0;
                }
            }
            return len;
        }

        @Override
        public String toString() {
            return super.toString() + ' ' + Arrays.toString(chars);
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
         * Returns whether or not the given character matches.
         *
         * @param buffer
         *            the text content to match against, do not change
         * @param start
         *            the starting position for the match, valid for buffer
         * @param bufferStart
         *            the first active index in the buffer, valid for buffer
         * @param bufferEnd
         *            the end index of the active buffer, valid for buffer
         * @return The number of matching characters, zero for no match
         */
        @Override
        public int isMatch(final char[] buffer, final int start, final int bufferStart, final int bufferEnd) {
            return buffer[start] <= SPACE_INT ? 1 : 0;
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
     * Constructor.
     */
    protected AbstractStringMatcher() {
        super();
    }

    /**
     * Returns the number of matching characters, zero for no match.
     * <p>
     * This method is called to check for a match. The parameter {@code pos} represents the current position to be
     * checked in the string {@code buffer} (a character array which must not be changed). The API guarantees that
     * {@code pos} is a valid index for {@code buffer}.
     * <p>
     * The matching code may check one character or many. It may check characters preceding {@code pos} as well as
     * those after.
     * <p>
     * It must return zero for no match, or a positive number if a match was found. The number indicates the number of
     * characters that matched.
     *
     * @param buffer
     *            the text content to match against, do not change
     * @param pos
     *            the starting position for the match, valid for buffer
     * @return The number of matching characters, zero for no match
     */
    public int isMatch(final char[] buffer, final int pos) {
        return isMatch(buffer, pos, 0, buffer.length);
    }

}
