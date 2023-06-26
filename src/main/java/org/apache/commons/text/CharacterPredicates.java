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

/**
 * Commonly used implementations of {@link CharacterPredicate}. Per the interface
 * requirements, all implementations are thread safe.
 *
 * @since 1.0
 */
public enum CharacterPredicates implements CharacterPredicate {

    /**
     * Tests code points against {@link Character#isLetter(int)}.
     *
     * @since 1.0
     */
    LETTERS {
        @Override
        public boolean test(final int codePoint) {
            return Character.isLetter(codePoint);
        }
    },

    /**
     * Tests code points against {@link Character#isDigit(int)}.
     *
     * @since 1.0
     */
    DIGITS {
        @Override
        public boolean test(final int codePoint) {
            return Character.isDigit(codePoint);
        }
    },

    /**
     * Tests code points against {@link Character#isLetterOrDigit(int)}.
     *
     * @since 1.0
     */
    LETTERS_OR_DIGITS {
        @Override
        public boolean test(final int codePoint) {
            return Character.isLetterOrDigit(codePoint);
        }
    },

    /**
     * Tests if the code points represents a number between 0 and 9.
     *
     * @since 1.2
     */
    ARABIC_NUMERALS {
        @Override
        public boolean test(final int codePoint) {
            return codePoint >= '0' && codePoint <= '9';
        }
    },

    /**
     * Tests if the code points represents a letter between a and z.
     *
     * @since 1.2
     */
    ASCII_LOWERCASE_LETTERS {
        @Override
        public boolean test(final int codePoint) {
            return codePoint >= 'a' && codePoint <= 'z';
        }
    },

    /**
     * Tests if the code points represents a letter between A and Z.
     *
     * @since 1.2
     */
    ASCII_UPPERCASE_LETTERS {
        @Override
        public boolean test(final int codePoint) {
            return codePoint >= 'A' && codePoint <= 'Z';
        }
    },

    /**
     * Tests if the code points represents a letter between a and Z.
     *
     * @since 1.2
     */
    ASCII_LETTERS {
        @Override
        public boolean test(final int codePoint) {
            return ASCII_LOWERCASE_LETTERS.test(codePoint) || ASCII_UPPERCASE_LETTERS.test(codePoint);
        }
    },

    /**
     * Tests if the code points represents a letter between a and Z or a number between 0 and 9.
     *
     * @since 1.2
     */
    ASCII_ALPHA_NUMERALS {
        @Override
        public boolean test(final int codePoint) {
            return ASCII_LOWERCASE_LETTERS.test(codePoint) || ASCII_UPPERCASE_LETTERS.test(codePoint)
                    || ARABIC_NUMERALS.test(codePoint);
        }
    }
}
