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
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.text.TextStringBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class LevenshteinDetailedDistanceTest {

    private static final LevenshteinDetailedDistance UNLIMITED_DISTANCE = LevenshteinDetailedDistance.getDefaultInstance();

    @Test
    public void testApplyThrowsIllegalArgumentExceptionAndCreatesLevenshteinDetailedDistanceTakingInteger() {
        assertThrows(IllegalArgumentException.class, () -> {
            final LevenshteinDetailedDistance levenshteinDetailedDistance = new LevenshteinDetailedDistance(0);
            final CharSequence charSequence = new TextStringBuilder();
            levenshteinDetailedDistance.apply(charSequence, null);
        });
    }

    @Test
    public void testApplyWithNullSimilarityInput() {
        assertThrows(IllegalArgumentException.class,
                () -> new LevenshteinDetailedDistance(0).apply((SimilarityInput<Object>) null, (SimilarityInput<Object>) null));
    }

    @Test
    public void testApplyWithNullString() {
        assertThrows(IllegalArgumentException.class, () -> new LevenshteinDetailedDistance(0).apply((String) null, (String) null));
    }

    @Test
    public void testConstructorWithNegativeThreshold() {
        assertThrows(IllegalArgumentException.class, () -> new LevenshteinDetailedDistance(-1));
    }

    @ParameterizedTest
    @MethodSource("org.apache.commons.text.similarity.SimilarityInputTest#similarityInputs()")
    public void testCreatesLevenshteinDetailedDistanceTakingInteger6(final Class<?> cls) {
        final LevenshteinDetailedDistance levenshteinDetailedDistance = new LevenshteinDetailedDistance(0);
        final LevenshteinResults levenshteinResults = levenshteinDetailedDistance.apply("", "Distance: 38, Insert: 0, Delete: 0, Substitute: 0");
        assertEquals(0, levenshteinResults.getSubstituteCount());
        assertEquals(0, levenshteinResults.getDeleteCount());
        assertEquals(0, levenshteinResults.getInsertCount());
        assertEquals(-1, levenshteinResults.getDistance());
        assertEquals(levenshteinResults, levenshteinDetailedDistance.apply(SimilarityInputTest.build(cls, ""),
                SimilarityInputTest.build(cls, "Distance: 38, Insert: 0, Delete: 0, Substitute: 0")));
    }

    @ParameterizedTest
    @MethodSource("org.apache.commons.text.similarity.SimilarityInputTest#similarityInputs()")
    public void testEquals(final Class<?> cls) {
        final LevenshteinDetailedDistance classBeingTested = LevenshteinDetailedDistance.getDefaultInstance();
        LevenshteinResults actualResult = classBeingTested.apply(SimilarityInputTest.build(cls, "hello"), SimilarityInputTest.build(cls, "hallo"));
        LevenshteinResults expectedResult = new LevenshteinResults(1, 0, 0, 1);
        assertEquals(expectedResult, actualResult);

        assertEquals(classBeingTested.apply("zzzzzzzz", "hippo"),
                classBeingTested.apply(SimilarityInputTest.build(cls, "zzzzzzzz"), SimilarityInputTest.build(cls, "hippo")));
        actualResult = classBeingTested.apply(SimilarityInputTest.build(cls, "zzzzzzzz"), SimilarityInputTest.build(cls, "hippo"));
        expectedResult = new LevenshteinResults(8, 0, 3, 5);
        assertEquals(expectedResult, actualResult);
        assertEquals(actualResult, actualResult); // intentionally added

        actualResult = classBeingTested.apply(SimilarityInputTest.build(cls, ""), SimilarityInputTest.build(cls, ""));
        expectedResult = new LevenshteinResults(0, 0, 0, 0);
        assertEquals(expectedResult, actualResult);
    }

    @ParameterizedTest
    @MethodSource("org.apache.commons.text.similarity.SimilarityInputTest#similarityInputs()")
    public void testGetDefaultInstanceOne(final Class<?> cls) {
        final LevenshteinDetailedDistance levenshteinDetailedDistance = LevenshteinDetailedDistance.getDefaultInstance();
        final LevenshteinResults levenshteinResults = levenshteinDetailedDistance.apply(
                SimilarityInputTest.build(cls, "Distance: -2147483643, Insert: 0, Delete: 0, Substitute: 0"),
                SimilarityInputTest.build(cls, "Distance: 0, Insert: 2147483536, Delete: 0, Substitute: 0"));

        assertEquals(21, levenshteinResults.getDistance());
    }

    @ParameterizedTest
    @MethodSource("org.apache.commons.text.similarity.SimilarityInputTest#similarityInputs()")
    public void testGetDefaultInstanceTwo(final Class<?> cls) {
        final LevenshteinDetailedDistance levenshteinDetailedDistance = LevenshteinDetailedDistance.getDefaultInstance();
        final LevenshteinResults levenshteinResults = levenshteinDetailedDistance.apply("Distance: 2147483647, Insert: 0, Delete: 0, Substitute: 0",
                "Distance: 0, Insert: 2147483647, Delete: 0, Substitute: 0");
        assertEquals(20, levenshteinResults.getDistance());
        assertEquals(levenshteinResults,
                levenshteinDetailedDistance.apply(SimilarityInputTest.build(cls, "Distance: 2147483647, Insert: 0, Delete: 0, Substitute: 0"),
                        SimilarityInputTest.build(cls, "Distance: 0, Insert: 2147483647, Delete: 0, Substitute: 0")));
    }

    @Test
    public void testGetLevenshteinDetailedDistance_NullString() {
        assertThrows(IllegalArgumentException.class, () -> UNLIMITED_DISTANCE.apply("a", null));
    }

    @Test
    public void testGetLevenshteinDetailedDistance_NullStringInt() {
        assertThrows(IllegalArgumentException.class, () -> UNLIMITED_DISTANCE.apply(null, "a"));
    }

    @Test
    public void testGetLevenshteinDetailedDistance_StringNull() {
        assertThrows(IllegalArgumentException.class, () -> UNLIMITED_DISTANCE.apply(null, "a"));
    }

    @Test
    public void testGetLevenshteinDetailedDistance_StringNullInt() {
        assertThrows(IllegalArgumentException.class, () -> UNLIMITED_DISTANCE.apply("a", null));
    }

    @ParameterizedTest
    @MethodSource("org.apache.commons.text.similarity.SimilarityInputTest#similarityInputs()")
    public void testGetLevenshteinDetailedDistance_StringString(final Class<?> cls) {
        LevenshteinResults result = UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, ""), SimilarityInputTest.build(cls, ""));
        assertEquals(0, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(0, result.getSubstituteCount());

        result = UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, ""), SimilarityInputTest.build(cls, "a"));
        assertEquals(1, result.getDistance());
        assertEquals(1, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(0, result.getSubstituteCount());

        result = UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, "aaapppp"), SimilarityInputTest.build(cls, ""));
        assertEquals(7, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(7, result.getDeleteCount());
        assertEquals(0, result.getSubstituteCount());

        result = UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, "frog"), SimilarityInputTest.build(cls, "fog"));
        assertEquals(1, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(1, result.getDeleteCount());
        assertEquals(0, result.getSubstituteCount());

        result = UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, "fly"), SimilarityInputTest.build(cls, "ant"));
        assertEquals(3, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(3, result.getSubstituteCount());

        result = UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, "elephant"), SimilarityInputTest.build(cls, "hippo"));
        assertEquals(7, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(3, result.getDeleteCount());
        assertEquals(4, result.getSubstituteCount());

        result = UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, "hippo"), SimilarityInputTest.build(cls, "elephant"));
        assertEquals(7, result.getDistance());
        assertEquals(3, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(4, result.getSubstituteCount());

        result = UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, "hippo"), SimilarityInputTest.build(cls, "zzzzzzzz"));
        assertEquals(8, result.getDistance());
        assertEquals(3, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(5, result.getSubstituteCount());

        result = UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, "zzzzzzzz"), SimilarityInputTest.build(cls, "hippo"));
        assertEquals(8, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(3, result.getDeleteCount());
        assertEquals(5, result.getSubstituteCount());

        result = UNLIMITED_DISTANCE.apply(SimilarityInputTest.build(cls, "hello"), SimilarityInputTest.build(cls, "hallo"));
        assertEquals(1, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(1, result.getSubstituteCount());
    }

    @Test
    public void testGetLevenshteinDetailedDistance_StringStringInt() {

        LevenshteinResults result = new LevenshteinDetailedDistance(0).apply("", "");

        assertEquals(0, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(0, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(8).apply("aaapppp", "");
        assertEquals(7, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(7, result.getDeleteCount());
        assertEquals(0, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(7).apply("aaapppp", "");
        assertEquals(7, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(7, result.getDeleteCount());
        assertEquals(0, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(6).apply("aaapppp", "");
        assertEquals(-1, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(0, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(0).apply("b", "a");
        assertEquals(-1, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(0, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(0).apply("a", "b");
        assertEquals(-1, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(0, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(0).apply("aa", "aa");
        assertEquals(0, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(0, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(2).apply("aa", "aa");
        assertEquals(0, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(0, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(2).apply("aaa", "bbb");
        assertEquals(-1, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(0, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(3).apply("aaa", "bbb");
        assertEquals(3, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(3, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(10).apply("aaaaaa", "b");
        assertEquals(6, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(5, result.getDeleteCount());
        assertEquals(1, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(8).apply("aaapppp", "b");
        assertEquals(7, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(6, result.getDeleteCount());
        assertEquals(1, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(4).apply("a", "bbb");
        assertEquals(3, result.getDistance());
        assertEquals(2, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(1, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(7).apply("aaapppp", "b");
        assertEquals(7, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(6, result.getDeleteCount());
        assertEquals(1, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(3).apply("a", "bbb");
        assertEquals(3, result.getDistance());
        assertEquals(2, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(1, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(2).apply("a", "bbb");
        assertEquals(-1, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(0, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(2).apply("bbb", "a");
        assertEquals(-1, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(0, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(6).apply("aaapppp", "b");
        assertEquals(-1, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(0, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(1).apply("a", "bbb");
        assertEquals(-1, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(0, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(1).apply("bbb", "a");
        assertEquals(-1, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(0, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(1).apply("12345", "1234567");
        assertEquals(-1, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(0, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(1).apply("1234567", "12345");
        assertEquals(-1, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(0, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(1).apply("frog", "fog");
        assertEquals(1, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(1, result.getDeleteCount());
        assertEquals(0, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(3).apply("fly", "ant");
        assertEquals(3, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(3, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(7).apply("elephant", "hippo");
        assertEquals(7, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(3, result.getDeleteCount());
        assertEquals(4, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(6).apply("elephant", "hippo");
        assertEquals(-1, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(0, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(7).apply("hippo", "elephant");
        assertEquals(7, result.getDistance());
        assertEquals(3, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(4, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(7).apply("hippo", "elephant");
        assertEquals(7, result.getDistance());
        assertEquals(3, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(4, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(6).apply("hippo", "elephant");
        assertEquals(-1, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(0, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(8).apply("hippo", "zzzzzzzz");
        assertEquals(8, result.getDistance());
        assertEquals(3, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(5, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(8).apply("zzzzzzzz", "hippo");
        assertEquals(8, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(3, result.getDeleteCount());
        assertEquals(5, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(1).apply("hello", "hallo");
        assertEquals(1, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(1, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(Integer.MAX_VALUE).apply("frog", "fog");
        assertEquals(1, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(1, result.getDeleteCount());
        assertEquals(0, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(Integer.MAX_VALUE).apply("fly", "ant");
        assertEquals(3, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(3, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(Integer.MAX_VALUE).apply("elephant", "hippo");
        assertEquals(7, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(3, result.getDeleteCount());
        assertEquals(4, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(Integer.MAX_VALUE).apply("hippo", "elephant");
        assertEquals(7, result.getDistance());
        assertEquals(3, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(4, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(Integer.MAX_VALUE).apply("hippo", "zzzzzzzz");
        assertEquals(8, result.getDistance());
        assertEquals(3, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(5, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(Integer.MAX_VALUE).apply("zzzzzzzz", "hippo");
        assertEquals(8, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(3, result.getDeleteCount());
        assertEquals(5, result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(Integer.MAX_VALUE).apply("hello", "hallo");
        assertEquals(1, result.getDistance());
        assertEquals(0, result.getInsertCount());
        assertEquals(0, result.getDeleteCount());
        assertEquals(1, result.getSubstituteCount());
    }

    @Test
    public void testGetThreshold() {
        final LevenshteinDetailedDistance levenshteinDetailedDistance = new LevenshteinDetailedDistance(0);

        assertEquals(0, levenshteinDetailedDistance.getThreshold());
    }

    @Test
    public void testHashCode() {
        final LevenshteinDetailedDistance classBeingTested = LevenshteinDetailedDistance.getDefaultInstance();
        LevenshteinResults actualResult = classBeingTested.apply("aaapppp", "");
        LevenshteinResults expectedResult = new LevenshteinResults(7, 0, 7, 0);
        assertEquals(expectedResult.hashCode(), actualResult.hashCode());

        actualResult = classBeingTested.apply("frog", "fog");
        expectedResult = new LevenshteinResults(1, 0, 1, 0);
        assertEquals(expectedResult.hashCode(), actualResult.hashCode());

        actualResult = classBeingTested.apply("elephant", "hippo");
        expectedResult = new LevenshteinResults(7, 0, 3, 4);
        assertEquals(expectedResult.hashCode(), actualResult.hashCode());
    }

    @Test
    public void testToString() {
        final LevenshteinDetailedDistance classBeingTested = LevenshteinDetailedDistance.getDefaultInstance();
        LevenshteinResults actualResult = classBeingTested.apply("fly", "ant");
        LevenshteinResults expectedResult = new LevenshteinResults(3, 0, 0, 3);
        assertEquals(expectedResult.toString(), actualResult.toString());

        actualResult = classBeingTested.apply("hippo", "elephant");
        expectedResult = new LevenshteinResults(7, 3, 0, 4);
        assertEquals(expectedResult.toString(), actualResult.toString());

        actualResult = classBeingTested.apply("", "a");
        expectedResult = new LevenshteinResults(1, 1, 0, 0);
        assertEquals(expectedResult.toString(), actualResult.toString());
    }

}
