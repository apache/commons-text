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

}
