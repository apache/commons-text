/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.text.similarity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@link LevenshteinDistance}.
 */
class LevenshteinDistanceTest {

    private static final LevenshteinDistance UNLIMITED_DISTANCE = LevenshteinDistance.getDefaultInstance();

    @Test
    void testApplyThrowsIllegalArgumentExceptionSimilarityInput() {
        assertThrows(IllegalArgumentException.class, () -> new LevenshteinDistance(0).apply((SimilarityInput<Object>) null, (SimilarityInput<Object>) null));
        assertThrows(IllegalArgumentException.class, () -> new LevenshteinDistance(0).apply(new SimilarityCharacterInput("asdf"),
                (SimilarityCharacterInput) null));
        assertThrows(IllegalArgumentException.class, () -> new LevenshteinDistance(0).apply((SimilarityCharacterInput) null,
                new SimilarityCharacterInput("asdf")));
    }

    @Test
    void testApplyThrowsIllegalArgumentExceptionString() {
        assertThrows(IllegalArgumentException.class, () -> new LevenshteinDistance(0).apply((String) null, (String) null));
    }

    @Test
    void testConstructorWithNegativeThreshold() {
        assertThrows(IllegalArgumentException.class, () -> new LevenshteinDistance(-1));
    }

    @ParameterizedTest
    @MethodSource("org.apache.commons.text.similarity.SimilarityInputTest#similarityInputs()")
    void testGetLevenshteinDistance(final Class<?> cls) {
        assertEquals(0, UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, ""), SimilarityInputTest.build(cls, "")));
        assertEquals(1, UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, ""), SimilarityInputTest.build(cls, "a")));
        assertEquals(7, UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, "aaapppp"), SimilarityInputTest.build(cls, "")));
        assertEquals(1, UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, "frog"), SimilarityInputTest.build(cls, "fog")));
        assertEquals(3, UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, "fly"), SimilarityInputTest.build(cls, "ant")));
        assertEquals(7, UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, "elephant"), SimilarityInputTest.build(cls, "hippo")));
        assertEquals(7, UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, "hippo"), SimilarityInputTest.build(cls, "elephant")));
        assertEquals(8, UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, "hippo"), SimilarityInputTest.build(cls, "zzzzzzzz")));
        assertEquals(8, UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, "zzzzzzzz"), SimilarityInputTest.build(cls, "hippo")));
        assertEquals(1, UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, "hello"), SimilarityInputTest.build(cls, "hallo")));
    }

    @Test
    void testGetLevenshteinDistance_EmptyStringString() {
        assertEquals(-1, new LevenshteinDistance(0).apply(new SimilarityCharacterInput(""),
                new SimilarityCharacterInput("asdf")));
    }

    @ParameterizedTest
    @MethodSource("org.apache.commons.text.similarity.SimilarityInputTest#similarityInputs()")
    void testGetLevenshteinDistance_NullString(final Class<?> cls) {
        assertThrows(IllegalArgumentException.class, () -> UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, "a"), SimilarityInputTest.build(cls, null)));
    }

    @ParameterizedTest
    @MethodSource("org.apache.commons.text.similarity.SimilarityInputTest#similarityInputs()")
    void testGetLevenshteinDistance_NullStringInt(final Class<?> cls) {
        assertThrows(IllegalArgumentException.class, () -> UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, null), SimilarityInputTest.build(cls, "a")));
    }

    @ParameterizedTest
    @MethodSource("org.apache.commons.text.similarity.SimilarityInputTest#similarityInputs()")
    void testGetLevenshteinDistance_StringNull(final Class<?> cls) {
        assertThrows(IllegalArgumentException.class, () -> UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, null), SimilarityInputTest.build(cls, "a")));
    }

    @ParameterizedTest
    @MethodSource("org.apache.commons.text.similarity.SimilarityInputTest#similarityInputs()")
    void testGetLevenshteinDistance_StringNullInt(final Class<?> cls) {
        assertThrows(IllegalArgumentException.class, () -> UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, "a"), SimilarityInputTest.build(cls, null)));
    }

    @ParameterizedTest
    @MethodSource("org.apache.commons.text.similarity.SimilarityInputTest#similarityInputs()")
    void testGetLevenshteinDistance_StringString(final Class<?> cls) {
        assertEquals(0, UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, ""), SimilarityInputTest.build(cls, "")));
        assertEquals(1, UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, ""), SimilarityInputTest.build(cls, "a")));
        assertEquals(7, UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, "aaapppp"), SimilarityInputTest.build(cls, "")));
        assertEquals(1, UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, "frog"), SimilarityInputTest.build(cls, "fog")));
        assertEquals(3, UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, "fly"), SimilarityInputTest.build(cls, "ant")));
        assertEquals(7, UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, "elephant"), SimilarityInputTest.build(cls, "hippo")));
        assertEquals(7, UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, "hippo"), SimilarityInputTest.build(cls, "elephant")));
        assertEquals(8, UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, "hippo"), SimilarityInputTest.build(cls, "zzzzzzzz")));
        assertEquals(8, UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, "zzzzzzzz"), SimilarityInputTest.build(cls, "hippo")));
        assertEquals(1, UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, "hello"), SimilarityInputTest.build(cls, "hallo")));
    }

    @ParameterizedTest
    @MethodSource("org.apache.commons.text.similarity.SimilarityInputTest#similarityInputs()")
    void testGetLevenshteinDistance_StringStringInt(final Class<?> cls) {
        // empty strings
        assertEquals(0, new LevenshteinDistance(0).apply(SimilarityInputTest.build(cls, ""), SimilarityInputTest.build(cls, "")));
        assertEquals(7, new LevenshteinDistance(8).apply(SimilarityInputTest.build(cls, "aaapppp"), SimilarityInputTest.build(cls, "")));
        assertEquals(7, new LevenshteinDistance(7).apply(SimilarityInputTest.build(cls, "aaapppp"), SimilarityInputTest.build(cls, "")));
        assertEquals(-1, new LevenshteinDistance(6).apply(SimilarityInputTest.build(cls, "aaapppp"), SimilarityInputTest.build(cls, "")));

        // unequal strings, zero threshold
        assertEquals(-1, new LevenshteinDistance(0).apply(SimilarityInputTest.build(cls, "b"), SimilarityInputTest.build(cls, "a")));
        assertEquals(-1, new LevenshteinDistance(0).apply(SimilarityInputTest.build(cls, "a"), SimilarityInputTest.build(cls, "b")));

        // equal strings
        assertEquals(0, new LevenshteinDistance(0).apply(SimilarityInputTest.build(cls, "aa"), SimilarityInputTest.build(cls, "aa")));
        assertEquals(0, new LevenshteinDistance(2).apply(SimilarityInputTest.build(cls, "aa"), SimilarityInputTest.build(cls, "aa")));

        // same length
        assertEquals(-1, new LevenshteinDistance(2).apply(SimilarityInputTest.build(cls, "aaa"), SimilarityInputTest.build(cls, "bbb")));
        assertEquals(3, new LevenshteinDistance(3).apply(SimilarityInputTest.build(cls, "aaa"), SimilarityInputTest.build(cls, "bbb")));

        // big stripe
        assertEquals(6, new LevenshteinDistance(10).apply(SimilarityInputTest.build(cls, "aaaaaa"), SimilarityInputTest.build(cls, "b")));

        // distance less than threshold
        assertEquals(7, new LevenshteinDistance(8).apply(SimilarityInputTest.build(cls, "aaapppp"), SimilarityInputTest.build(cls, "b")));
        assertEquals(3, new LevenshteinDistance(4).apply(SimilarityInputTest.build(cls, "a"), SimilarityInputTest.build(cls, "bbb")));

        // distance equal to threshold
        assertEquals(7, new LevenshteinDistance(7).apply(SimilarityInputTest.build(cls, "aaapppp"), SimilarityInputTest.build(cls, "b")));
        assertEquals(3, new LevenshteinDistance(3).apply(SimilarityInputTest.build(cls, "a"), SimilarityInputTest.build(cls, "bbb")));

        // distance greater than threshold
        assertEquals(-1, new LevenshteinDistance(2).apply(SimilarityInputTest.build(cls, "a"), SimilarityInputTest.build(cls, "bbb")));
        assertEquals(-1, new LevenshteinDistance(2).apply(SimilarityInputTest.build(cls, "bbb"), SimilarityInputTest.build(cls, "a")));
        assertEquals(-1, new LevenshteinDistance(6).apply(SimilarityInputTest.build(cls, "aaapppp"), SimilarityInputTest.build(cls, "b")));

        // stripe runs off array, strings not similar
        assertEquals(-1, new LevenshteinDistance(1).apply(SimilarityInputTest.build(cls, "a"), SimilarityInputTest.build(cls, "bbb")));
        assertEquals(-1, new LevenshteinDistance(1).apply(SimilarityInputTest.build(cls, "bbb"), SimilarityInputTest.build(cls, "a")));

        // stripe runs off array, strings are similar
        assertEquals(-1, new LevenshteinDistance(1).apply(SimilarityInputTest.build(cls, "12345"), SimilarityInputTest.build(cls, "1234567")));
        assertEquals(-1, new LevenshteinDistance(1).apply(SimilarityInputTest.build(cls, "1234567"), SimilarityInputTest.build(cls, "12345")));

        // old getLevenshteinDistance test cases
        assertEquals(1, new LevenshteinDistance(1).apply(SimilarityInputTest.build(cls, "frog"), SimilarityInputTest.build(cls, "fog")));
        assertEquals(3, new LevenshteinDistance(3).apply(SimilarityInputTest.build(cls, "fly"), SimilarityInputTest.build(cls, "ant")));
        assertEquals(7, new LevenshteinDistance(7).apply(SimilarityInputTest.build(cls, "elephant"), SimilarityInputTest.build(cls, "hippo")));
        assertEquals(-1, new LevenshteinDistance(6).apply(SimilarityInputTest.build(cls, "elephant"), SimilarityInputTest.build(cls, "hippo")));
        assertEquals(7, new LevenshteinDistance(7).apply(SimilarityInputTest.build(cls, "hippo"), SimilarityInputTest.build(cls, "elephant")));
        assertEquals(-1, new LevenshteinDistance(6).apply(SimilarityInputTest.build(cls, "hippo"), SimilarityInputTest.build(cls, "elephant")));
        assertEquals(8, new LevenshteinDistance(8).apply(SimilarityInputTest.build(cls, "hippo"), SimilarityInputTest.build(cls, "zzzzzzzz")));
        assertEquals(8, new LevenshteinDistance(8).apply(SimilarityInputTest.build(cls, "zzzzzzzz"), SimilarityInputTest.build(cls, "hippo")));
        assertEquals(1, new LevenshteinDistance(1).apply(SimilarityInputTest.build(cls, "hello"), SimilarityInputTest.build(cls, "hallo")));

        assertEquals(1, new LevenshteinDistance(Integer.MAX_VALUE).apply(SimilarityInputTest.build(cls, "frog"), SimilarityInputTest.build(cls, "fog")));
        assertEquals(3, new LevenshteinDistance(Integer.MAX_VALUE).apply(SimilarityInputTest.build(cls, "fly"), SimilarityInputTest.build(cls, "ant")));
        assertEquals(7, new LevenshteinDistance(Integer.MAX_VALUE).apply(SimilarityInputTest.build(cls, "elephant"), SimilarityInputTest.build(cls, "hippo")));
        assertEquals(7, new LevenshteinDistance(Integer.MAX_VALUE).apply(SimilarityInputTest.build(cls, "hippo"), SimilarityInputTest.build(cls, "elephant")));
        assertEquals(8, new LevenshteinDistance(Integer.MAX_VALUE).apply(SimilarityInputTest.build(cls, "hippo"), SimilarityInputTest.build(cls, "zzzzzzzz")));
        assertEquals(8, new LevenshteinDistance(Integer.MAX_VALUE).apply(SimilarityInputTest.build(cls, "zzzzzzzz"), SimilarityInputTest.build(cls, "hippo")));
        assertEquals(1, new LevenshteinDistance(Integer.MAX_VALUE).apply(SimilarityInputTest.build(cls, "hello"), SimilarityInputTest.build(cls, "hallo")));
        assertEquals(-1, new LevenshteinDistance(1).apply(SimilarityInputTest.build(cls, "abc"), SimilarityInputTest.build(cls, "acb")));
    }

    @Test
    void testGetThresholdDirectlyAfterObjectInstantiation() {
        assertNull(LevenshteinDistance.getDefaultInstance().getThreshold());
    }

    @Test
    void testConstructorWithNegativeCosts() {
        assertThrows(IllegalArgumentException.class, () -> LevenshteinDistance.builder().setInsertCost(-1).build());
        assertThrows(IllegalArgumentException.class, () -> LevenshteinDistance.builder().setDeleteCost(-1).build());
        assertThrows(IllegalArgumentException.class, () -> LevenshteinDistance.builder().setReplaceCost(-1).build());
    }

    @Test
    void testGetLevenshteinDistance_WeightedUnlimited() {
        // Substitution is very expensive (10) vs Insert/Delete (1 each)
        final LevenshteinDistance dist = LevenshteinDistance.builder().setInsertCost(1).setDeleteCost(1).setReplaceCost(10).build();
        // 'a' -> 'b' should choose delete 'a' (1) and insert 'b' (1) = distance 2,
        // instead of replace (10).
        assertEquals(2, dist.apply("a", "b"));

        // All operations are free (0)
        final LevenshteinDistance freeDist = LevenshteinDistance.builder().setInsertCost(0).setDeleteCost(0).setReplaceCost(0).build();
        assertEquals(0, freeDist.apply("abc", "def"));

        // Asymmetric costs: Insert=10, Delete=1, Replace=100
        final LevenshteinDistance asymmetric = LevenshteinDistance.builder().setInsertCost(10).setDeleteCost(1).setReplaceCost(100).build();
        assertEquals(1, asymmetric.apply("a", ""));   // Delete 'a' = 1
        assertEquals(10, asymmetric.apply("", "a"));  // Insert 'a' = 10
    }

    @Test
    void testGetLevenshteinDistance_WeightedThreshold() {
        // Distance is 2 (via delete/insert), threshold is 5 -> result 2
        final LevenshteinDistance weighted = LevenshteinDistance.builder().setThreshold(5).setInsertCost(1).setDeleteCost(1).setReplaceCost(10).build();
        assertEquals(2, weighted.apply("a", "b"));

        // Distance is 2, threshold is 1 -> result -1
        final LevenshteinDistance strict = LevenshteinDistance.builder().setThreshold(1).setInsertCost(1).setDeleteCost(1).setReplaceCost(10).build();
        assertEquals(-1, strict.apply("a", "b"));

        // Empty strings with weighted threshold
        assertEquals(0, LevenshteinDistance.builder().setThreshold(5).setInsertCost(2).setDeleteCost(2).setReplaceCost(2).build().apply("", ""));
        assertEquals(4, LevenshteinDistance.builder().setThreshold(5).setInsertCost(2).setDeleteCost(2).setReplaceCost(2).build().apply("aa", ""));
        assertEquals(-1, LevenshteinDistance.builder().setThreshold(1).setInsertCost(2).setDeleteCost(2).setReplaceCost(2).build().apply("aa", ""));
    }

    @Test
    void testWeightedAccessors() {
        final LevenshteinDistance dist = LevenshteinDistance.builder().setThreshold(10).setInsertCost(2).setDeleteCost(3).setReplaceCost(4).build();
        assertEquals(10, dist.getThreshold());
        assertEquals(2, dist.getInsertCost());
        assertEquals(3, dist.getDeleteCost());
        assertEquals(4, dist.getReplaceCost());
    }
}
