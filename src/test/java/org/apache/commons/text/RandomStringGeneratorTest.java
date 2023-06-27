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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link RandomStringGenerator}
 */
public class RandomStringGeneratorTest {

    private static final CharacterPredicate A_FILTER = codePoint -> codePoint == 'a';

    private static final CharacterPredicate B_FILTER = codePoint -> codePoint == 'b';

    private static int codePointLength(final String s) {
        return s.codePointCount(0, s.length());
    }

    @Test
    public void testBadMaximumCodePoint() {
        assertThatIllegalArgumentException().isThrownBy(() -> new RandomStringGenerator.Builder().withinRange(0, Character.MAX_CODE_POINT + 1));
    }

    @Test
    public void testBadMinAndMax() {
        assertThatIllegalArgumentException().isThrownBy(() -> new RandomStringGenerator.Builder().withinRange(2, 1));
    }

    @Test
    public void testBadMinimumCodePoint() {
        assertThatIllegalArgumentException().isThrownBy(() -> new RandomStringGenerator.Builder().withinRange(-1, 1));
    }

    @Test
    public void testChangeOfFilter() {
        final RandomStringGenerator.Builder builder = new RandomStringGenerator.Builder().withinRange('a', 'z')
                .filteredBy(A_FILTER);
        final String str = builder.filteredBy(B_FILTER).build().generate(100);

        for (final char c : str.toCharArray()) {
            assertThat(c == 'b').isTrue();
        }
    }

    @Test
    public void testGenerateMinMaxLength() {
        final int minLength = 0;
        final int maxLength = 3;
        final RandomStringGenerator generator = new RandomStringGenerator.Builder().build();
        final String str = generator.generate(minLength, maxLength);
        assertThat(codePointLength(str)).isBetween(0, 3);
    }

    @Test
    public void testGenerateMinMaxLengthInvalidLength() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            final RandomStringGenerator generator = new RandomStringGenerator.Builder().build();
            generator.generate(-1, 0);
        });
    }

    @Test
    public void testGenerateMinMaxLengthMinGreaterThanMax() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            final RandomStringGenerator generator = new RandomStringGenerator.Builder().build();
            generator.generate(1, 0);
        });
    }

    @Test
    public void testGenerateTakingIntThrowsNullPointerException() {
        assertThatNullPointerException().isThrownBy(() -> {
            final RandomStringGenerator.Builder randomStringGeneratorBuilder = new RandomStringGenerator.Builder();
            final CharacterPredicate[] characterPredicateArray = new CharacterPredicate[2];
            randomStringGeneratorBuilder.filteredBy(characterPredicateArray);
            final RandomStringGenerator randomStringGenerator = randomStringGeneratorBuilder.build();

            randomStringGenerator.generate(18);
        });
    }

    @Test
    public void testInvalidLength() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            final RandomStringGenerator generator = new RandomStringGenerator.Builder().build();
            generator.generate(-1);
        });
    }

    @Test
    public void testMultipleFilters() {
        final String str = new RandomStringGenerator.Builder().withinRange('a', 'd')
                .filteredBy(A_FILTER, B_FILTER).build().generate(5000);

        boolean aFound = false;
        boolean bFound = false;

        for (final char c : str.toCharArray()) {
            if (c == 'a') {
                aFound = true;
            } else if (c == 'b') {
                bFound = true;
            } else {
                fail("Invalid character");
            }
        }

        assertThat(aFound && bFound).isTrue();
    }

    @Test
    public void testNoLoneSurrogates() {
        final int length = 5000;
        final String str = new RandomStringGenerator.Builder().build().generate(length);

        char lastChar = str.charAt(0);
        for (int i = 1; i < str.length(); i++) {
            final char c = str.charAt(i);

            if (Character.isLowSurrogate(c)) {
                assertThat(Character.isHighSurrogate(lastChar)).isTrue();
            }

            if (Character.isHighSurrogate(lastChar)) {
                assertThat(Character.isLowSurrogate(c)).isTrue();
            }

            if (Character.isHighSurrogate(c)) {
                // test this isn't the last character in the string
                assertThat(i + 1 < str.length()).isTrue();
            }

            lastChar = c;
        }
    }

    @Test
    public void testNoPrivateCharacters() {
        final int startOfPrivateBMPChars = 0xE000;

        // Request a string in an area of the Basic Multilingual Plane that is
        // largely occupied by private characters
        final String str = new RandomStringGenerator.Builder().withinRange(startOfPrivateBMPChars,
                Character.MIN_SUPPLEMENTARY_CODE_POINT - 1).build().generate(5000);

        int i = 0;
        do {
            final int codePoint = str.codePointAt(i);
            assertThat(Character.getType(codePoint) == Character.PRIVATE_USE).isFalse();
            i += Character.charCount(codePoint);
        } while (i < str.length());
    }

    @Test
    public void testRemoveFilters() {
        final RandomStringGenerator.Builder builder = new RandomStringGenerator.Builder().withinRange('a', 'z')
                .filteredBy(A_FILTER);

        builder.filteredBy();

        final String str = builder.build().generate(100);
        for (final char c : str.toCharArray()) {
            if (c != 'a') {
                // filter was successfully removed
                return;
            }
        }

        fail("Filter appears to have remained in place");
    }

    @Test
    public void testSelectFromCharArray() {
        final String str = "abc";
        final char[] charArray = str.toCharArray();
        final RandomStringGenerator generator = new RandomStringGenerator.Builder().selectFrom(charArray).build();

        final String randomText = generator.generate(5);

        for (final char c : randomText.toCharArray()) {
            assertThat(str.indexOf(c) != -1).isTrue();
        }
    }

    @Test
    public void testSelectFromCharVarargs() {
        final String str = "abc";
        final RandomStringGenerator generator = new RandomStringGenerator.Builder().selectFrom('a', 'b', 'c').build();
        final String randomText = generator.generate(5);
        for (final char c : randomText.toCharArray()) {
            assertThat(str.indexOf(c) != -1).isTrue();
        }
    }

    @Test
    public void testSelectFromCharVarargs2() {
        final String str = "abcde";
        // @formatter:off
        final RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .selectFrom()
                .selectFrom('a', 'b')
                .selectFrom('a', 'b', 'c')
                .selectFrom('a', 'b', 'c', 'd')
                .selectFrom('a', 'b', 'c', 'd', 'e') // only this last call matters
                .build();
        // @formatter:on
        final String randomText = generator.generate(10);
        for (final char c : randomText.toCharArray()) {
            assertThat(str.indexOf(c) != -1).isTrue();
        }
    }

    @Test
    public void testSelectFromCharVarargSize1() {
        final RandomStringGenerator generator = new RandomStringGenerator.Builder().selectFrom('a').build();
        final String randomText = generator.generate(5);
        for (final char c : randomText.toCharArray()) {
            assertEquals('a', c);
        }
    }

    @Test
    public void testSelectFromEmptyCharVarargs() {
        final RandomStringGenerator generator = new RandomStringGenerator.Builder().selectFrom().build();
        final String randomText = generator.generate(5);
        for (final char c : randomText.toCharArray()) {
            assertTrue(c >= Character.MIN_CODE_POINT && c <= Character.MAX_CODE_POINT);
        }
    }

    @Test
    public void testSelectFromNullCharVarargs() {
        final int length = 5;
        final RandomStringGenerator generator = new RandomStringGenerator.Builder().selectFrom(null).build();
        final String randomText = generator.generate(length);
        assertThat(codePointLength(randomText)).isEqualTo(length);
    }

    @Test
    public void testSetLength() {
        final int length = 99;
        final RandomStringGenerator generator = new RandomStringGenerator.Builder().build();
        final String str = generator.generate(length);
        assertThat(codePointLength(str)).isEqualTo(length);
    }

    @Test
    public void testUsingRandom() {
        final char testChar = 'a';
        final TextRandomProvider testRandom = n -> testChar;

        final String str = new RandomStringGenerator.Builder().usingRandom(testRandom).build().generate(10);
        for (final char c : str.toCharArray()) {
            assertThat(c).isEqualTo(testChar);
        }
    }

    @Test
    public void testWithinMultipleRanges() {
        final int length = 5000;
        final char[][] pairs = {{'a', 'z'}, {'0', '9'}};
        final RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange(pairs).build();
        final String str = generator.generate(length);

        int minimumCodePoint = 0, maximumCodePoint = 0;

        for (final char[] pair : pairs) {
            minimumCodePoint = Math.min(minimumCodePoint, pair[0]);
            maximumCodePoint = Math.max(maximumCodePoint, pair[1]);
        }

        int i = 0;
        do {
            final int codePoint = str.codePointAt(i);
            assertThat(codePoint >= minimumCodePoint && codePoint <= maximumCodePoint).isTrue();
            i += Character.charCount(codePoint);
        } while (i < str.length());
    }

    @Test
    public void testWithinRange() {
        final int length = 5000;
        final int minimumCodePoint = 'a';
        final int maximumCodePoint = 'z';
        final RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange(minimumCodePoint, maximumCodePoint).build();
        final String str = generator.generate(length);

        int i = 0;
        do {
            final int codePoint = str.codePointAt(i);
            assertThat(codePoint >= minimumCodePoint && codePoint <= maximumCodePoint).isTrue();
            i += Character.charCount(codePoint);
        } while (i < str.length());
    }

    @Test
    public void testZeroLength() {
        final RandomStringGenerator generator = new RandomStringGenerator.Builder().build();
        assertThat(generator.generate(0)).isEqualTo("");
    }
}
