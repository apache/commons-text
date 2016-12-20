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

import static org.apache.commons.text.RandomStringBuilder.LETTERS;
import static org.junit.Assert.*;

import java.util.Random;

import org.apache.commons.text.RandomStringBuilder.CodePointPredicate;
import org.junit.Test;

/**
 * Tests for {@link RandomStringBuilder}
 */
public class RandomStringBuilderTest {

    private static int codePointLength(String s) {
        return s.codePointCount(0, s.length());
    }

    private static final CodePointPredicate A_FILTER = new CodePointPredicate() {
        @Override
        public boolean test(int codePoint) {
            return codePoint == 'a';
        }
    };

    private static final CodePointPredicate B_FILTER = new CodePointPredicate() {
        @Override
        public boolean test(int codePoint) {
            return codePoint == 'b';
        }
    };

    @Test
    public void testDefaultLength() throws Exception {
        String str = new RandomStringBuilder().build();
        assertEquals(RandomStringBuilder.DEFAULT_LENGTH, codePointLength(str));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLength() throws Exception {
        new RandomStringBuilder().ofLength(-1);
    }

    @Test
    public void testSetLength() throws Exception {
        final int length = 99;
        String str = new RandomStringBuilder().ofLength(length).build();
        assertEquals(length, codePointLength(str));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadMinimumCodePoint() throws Exception {
        new RandomStringBuilder().withinRange(-1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadMaximumCodePoint() throws Exception {
        new RandomStringBuilder().withinRange(0, Character.MAX_CODE_POINT + 1);
    }

    @Test
        public void testWithinRange() throws Exception {
            final int length = 5000;
            final int minimumCodePoint = 'a';
            final int maximumCodePoint = 'z';
            String str = new RandomStringBuilder().ofLength(length).withinRange(minimumCodePoint,maximumCodePoint).build();
    
            int i = 0;
            do {
                int codePoint = str.codePointAt(i);
                assertTrue(codePoint >= minimumCodePoint && codePoint <= maximumCodePoint);
                i += Character.charCount(codePoint);
            } while (i < str.length());
    
        }

    @Test
    public void testNoLoneSurrogates() throws Exception {
        final int length = 5000;
        String str = new RandomStringBuilder().ofLength(length).build();

        char lastChar = str.charAt(0);
        for (int i = 1; i < str.length(); i++) {
            char c = str.charAt(i);

            if (Character.isLowSurrogate(c)) {
                assertTrue(Character.isHighSurrogate(lastChar));
            }

            if (Character.isHighSurrogate(lastChar)) {
                assertTrue(Character.isLowSurrogate(c));
            }

            if (Character.isHighSurrogate(c)) {
                // test this isn't the last character in the string
                assertTrue(i + 1 < str.length());
            }

            lastChar = c;
        }
    }

    @Test
    public void testUsingRandom() throws Exception {
        final char testChar = 'a';
        final Random testRandom = new Random() {
            private static final long serialVersionUID = 1L;

            @Override
            public int nextInt(int n) {
                return testChar;
            }
        };

        String str = new RandomStringBuilder().ofLength(100).usingRandom(testRandom).build();
        for (char c : str.toCharArray()) {
            assertEquals(testChar, c);
        }
    }

    @Test
    public void testLetterPredicate() throws Exception {
        String str = new RandomStringBuilder().ofLength(5000).filteredBy(LETTERS).build();

        int i = 0;
        do {
            int codePoint = str.codePointAt(i);
            assertTrue(Character.isLetter(codePoint));
            i += Character.charCount(codePoint);
        } while (i < str.length());
    }

    @Test
    public void testDigitPredicate() throws Exception {
        String str = new RandomStringBuilder().ofLength(5000).filteredBy(RandomStringBuilder.DIGITS).build();

        int i = 0;
        do {
            int codePoint = str.codePointAt(i);
            assertTrue(Character.isDigit(codePoint));
            i += Character.charCount(codePoint);
        } while (i < str.length());
    }

    @Test
    public void testMultipleFilters() throws Exception {
        String str = new RandomStringBuilder().ofLength(5000).withinRange('a','d')
                .filteredBy(A_FILTER, B_FILTER).build();

        boolean aFound = false;
        boolean bFound = false;

        for (char c : str.toCharArray()) {
            if (c == 'a') {
                aFound = true;
            } else if (c == 'b') {
                bFound = true;
            } else {
                fail("Invalid character");
            }
        }

        assertTrue(aFound && bFound);
    }

    @Test
    public void testNoPrivateCharacters() throws Exception {
        final int startOfPrivateBMPChars = 0xE000;

        // Request a string in an area of the Basic Multilingual Plane that is
        // largely
        // occupied by private characters
        String str = new RandomStringBuilder().ofLength(5000).withinRange(startOfPrivateBMPChars, 
                Character.MIN_SUPPLEMENTARY_CODE_POINT - 1).build();

        int i = 0;
        do {
            int codePoint = str.codePointAt(i);
            assertFalse(Character.getType(codePoint) == Character.PRIVATE_USE);
            i += Character.charCount(codePoint);
        } while (i < str.length());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadMinAndMax() throws Exception {
        new RandomStringBuilder().withinRange(2, 1);
    }

    @Test
    public void testRemoveFilters() throws Exception {

        RandomStringBuilder builder = new RandomStringBuilder().ofLength(100).withinRange('a', 'z')
                .filteredBy(A_FILTER);

        builder.filteredBy();

        String str = builder.build();
        for (char c : str.toCharArray()) {
            if (c != 'a') {
                // filter was successfully removed
                return;
            }
        }

        fail("Filter appears to have remained in place");
    }

    @Test
    public void testChangeOfFilter() throws Exception {
        RandomStringBuilder builder = new RandomStringBuilder().ofLength(100).withinRange('a', 'z')
                .filteredBy(A_FILTER);
        String str = builder.filteredBy(B_FILTER).build();

        for (char c : str.toCharArray()) {
            assertTrue(c == 'b');
        }
    }
}
