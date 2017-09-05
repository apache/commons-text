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

import org.junit.Test;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

import static org.junit.Assert.*;

/**
 * Tests for {@link RandomStringGenerator}
 */
public class RandomStringGeneratorTest {

    private static final CharacterPredicate A_FILTER = new CharacterPredicate() {
        @Override
        public boolean test(int codePoint) {
            return codePoint == 'a';
        }
    };

    private static final CharacterPredicate B_FILTER = new CharacterPredicate() {
        @Override
        public boolean test(int codePoint) {
            return codePoint == 'b';
        }
    };

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLength() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().build();
        generator.generate(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerateMinMaxLengthInvalidLength() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().build();
        generator.generate(-1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerateMinMaxLengthMinGreaterThanMax() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().build();
        generator.generate(1, 0);
    }

    private static int codePointLength(String s) {
        return s.codePointCount(0, s.length());
    }

    @Test
    public void testSetLength() {
        final int length = 99;
        RandomStringGenerator generator = new RandomStringGenerator.Builder().build();
        String str = generator.generate(length);
        assertEquals(length, codePointLength(str));
    }

    @Test
    public void testGenerateMinMaxLength() {
        final int minLength = 0;
        final int maxLength = 3;
        RandomStringGenerator generator = new RandomStringGenerator.Builder().build();
        String str = generator.generate(minLength, maxLength);
        assertThat(codePointLength(str), allOf(greaterThanOrEqualTo(0), lessThanOrEqualTo(3)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadMinimumCodePoint() {
        new RandomStringGenerator.Builder().withinRange(-1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadMaximumCodePoint() {
        new RandomStringGenerator.Builder().withinRange(0, Character.MAX_CODE_POINT + 1);
    }

    @Test
    public void testWithinRange() {
        final int length = 5000;
        final int minimumCodePoint = 'a';
        final int maximumCodePoint = 'z';
        RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange(minimumCodePoint,maximumCodePoint).build();
        String str = generator.generate(length);

        int i = 0;
        do {
            int codePoint = str.codePointAt(i);
            assertTrue(codePoint >= minimumCodePoint && codePoint <= maximumCodePoint);
            i += Character.charCount(codePoint);
        } while (i < str.length());
    }

    @Test
    public void testWithinMultipleRanges() {
        final int length = 5000;
        final char [][] pairs = {{'a','z'},{'0','9'}};
        RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange(pairs).build();
        String str = generator.generate(length);

        int minimumCodePoint = 0, maximumCodePoint = 0;

        for (char [] pair : pairs ) {
            minimumCodePoint = Math.min(minimumCodePoint, pair[0]);
            maximumCodePoint = Math.max(maximumCodePoint, pair[1]);
        }

        for (char [] pair : pairs ) {
            int i = 0;
            do {
                int codePoint = str.codePointAt(i);
                assertTrue(codePoint >= minimumCodePoint && codePoint <= maximumCodePoint);
                i += Character.charCount(codePoint);
            } while (i < str.length());
        }
    }



    @Test
    public void testNoLoneSurrogates() {
        final int length = 5000;
        String str = new RandomStringGenerator.Builder().build().generate(length);

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
    public void testUsingRandom() {
        final char testChar = 'a';
        final TextRandomProvider testRandom = new TextRandomProvider() {

            @Override
            public int nextInt(int n) {
                return testChar;
            }
        };

        String str = new RandomStringGenerator.Builder().usingRandom(testRandom).build().generate(10);
        for (char c : str.toCharArray()) {
            assertEquals(testChar, c);
        }
    }

    @Test
    public void testMultipleFilters() {
        String str = new RandomStringGenerator.Builder().withinRange('a','d')
                .filteredBy(A_FILTER, B_FILTER).build().generate(5000);

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
    public void testNoPrivateCharacters() {
        final int startOfPrivateBMPChars = 0xE000;

        // Request a string in an area of the Basic Multilingual Plane that is
        // largely occupied by private characters
        String str = new RandomStringGenerator.Builder().withinRange(startOfPrivateBMPChars, 
                Character.MIN_SUPPLEMENTARY_CODE_POINT - 1).build().generate(5000);

        int i = 0;
        do {
            int codePoint = str.codePointAt(i);
            assertFalse(Character.getType(codePoint) == Character.PRIVATE_USE);
            i += Character.charCount(codePoint);
        } while (i < str.length());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadMinAndMax() throws Exception {
        new RandomStringGenerator.Builder().withinRange(2, 1);
    }

    @Test
    public void testRemoveFilters() {
        RandomStringGenerator.Builder builder = new RandomStringGenerator.Builder().withinRange('a', 'z')
                .filteredBy(A_FILTER);

        builder.filteredBy();

        String str = builder.build().generate(100);
        for (char c : str.toCharArray()) {
            if (c != 'a') {
                // filter was successfully removed
                return;
            }
        }

        fail("Filter appears to have remained in place");
    }

    @Test
    public void testChangeOfFilter() {
        RandomStringGenerator.Builder builder = new RandomStringGenerator.Builder().withinRange('a', 'z')
                .filteredBy(A_FILTER);
        String str = builder.filteredBy(B_FILTER).build().generate(100);

        for (char c : str.toCharArray()) {
            assertTrue(c == 'b');
        }
    }

    @Test
    public void testZeroLength() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().build();
        assertEquals("", generator.generate(0));
    }

    @Test
    public void testSelectFromCharArray() {
        String str = "abc";
        char[] charArray = str.toCharArray();
        RandomStringGenerator generator = new RandomStringGenerator.Builder().selectFrom(charArray).build();

        String randomText = generator.generate(5);

        for (char c : randomText.toCharArray()) {
            assertTrue(str.indexOf(c) != -1);
        }
    }

    @Test
    public void testSelectFromCharVarargs() {
        String str="abc";
        RandomStringGenerator generator = new RandomStringGenerator.Builder().selectFrom('a','b','c').build();
        String randomText = generator.generate(5);
        for (char c : randomText.toCharArray()) {
            assertTrue(str.indexOf(c) != -1);
        }
    }

    @Test(expected=NullPointerException.class)
    public void testGenerateTakingIntThrowsNullPointerException() {
        RandomStringGenerator.Builder randomStringGeneratorBuilder = new RandomStringGenerator.Builder();
        CharacterPredicate[] characterPredicateArray = new CharacterPredicate[2];
        randomStringGeneratorBuilder.filteredBy(characterPredicateArray);
        RandomStringGenerator randomStringGenerator = randomStringGeneratorBuilder.build();

        randomStringGenerator.generate(18);
    }
}
