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
package org.apache.commons.text.similarity;

import static org.junit.Assert.assertEquals;

import org.apache.commons.text.StrBuilder;
import org.junit.Test;

public class LevenshteinDetailedDistanceTest {

    private static final LevenshteinDetailedDistance UNLIMITED_DISTANCE = new LevenshteinDetailedDistance();

    @Test
    public void testGetLevenshteinDetailedDistance_StringString() {
        LevenshteinResults result = UNLIMITED_DISTANCE.apply("", "");
        assertEquals(0, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(0, (int) result.getSubstituteCount());

        result = UNLIMITED_DISTANCE.apply("", "a");
        assertEquals(1, (int) result.getDistance());
        assertEquals(1, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(0, (int) result.getSubstituteCount());

        result = UNLIMITED_DISTANCE.apply("aaapppp", "");
        assertEquals(7, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(7, (int) result.getDeleteCount());
        assertEquals(0, (int) result.getSubstituteCount());

        result = UNLIMITED_DISTANCE.apply("frog", "fog");
        assertEquals(1, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(1, (int) result.getDeleteCount());
        assertEquals(0, (int) result.getSubstituteCount());

        result = UNLIMITED_DISTANCE.apply("fly", "ant");
        assertEquals(3, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(3, (int) result.getSubstituteCount());

        result = UNLIMITED_DISTANCE.apply("elephant", "hippo");
        assertEquals(7, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(3, (int) result.getDeleteCount());
        assertEquals(4, (int) result.getSubstituteCount());

        result = UNLIMITED_DISTANCE.apply("hippo", "elephant");
        assertEquals(7, (int) result.getDistance());
        assertEquals(3, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(4, (int) result.getSubstituteCount());

        result = UNLIMITED_DISTANCE.apply("hippo", "zzzzzzzz");
        assertEquals(8, (int) result.getDistance());
        assertEquals(3, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(5, (int) result.getSubstituteCount());

        result = UNLIMITED_DISTANCE.apply("zzzzzzzz", "hippo");
        assertEquals(8, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(3, (int) result.getDeleteCount());
        assertEquals(5, (int) result.getSubstituteCount());

        result = UNLIMITED_DISTANCE.apply("hello", "hallo");
        assertEquals(1, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(1, (int) result.getSubstituteCount());
    }

    @Test
    public void testEquals() {
     final LevenshteinDetailedDistance classBeingTested = new LevenshteinDetailedDistance();
     LevenshteinResults actualResult = classBeingTested.apply("hello", "hallo");
     LevenshteinResults expectedResult = new LevenshteinResults(1, 0, 0, 1);
     assertEquals(actualResult, expectedResult);

     actualResult = classBeingTested.apply("zzzzzzzz", "hippo");
     expectedResult = new LevenshteinResults(8, 0, 3, 5);
     assertEquals(actualResult, expectedResult);
     assertEquals(actualResult, actualResult); //intentionally added

     actualResult = classBeingTested.apply("", "");
     expectedResult = new LevenshteinResults(0, 0, 0, 0);
     assertEquals(actualResult, expectedResult);
    }

    @Test
    public void testHashCode() {
     final LevenshteinDetailedDistance classBeingTested = new LevenshteinDetailedDistance();
     LevenshteinResults actualResult = classBeingTested.apply("aaapppp", "");
     LevenshteinResults expectedResult = new LevenshteinResults(7, 0, 7, 0);
     assertEquals(actualResult.hashCode(), expectedResult.hashCode());

     actualResult = classBeingTested.apply("frog", "fog");
     expectedResult = new LevenshteinResults(1, 0, 1, 0);
     assertEquals(actualResult.hashCode(), expectedResult.hashCode());

     actualResult = classBeingTested.apply("elephant", "hippo");
     expectedResult = new LevenshteinResults(7, 0, 3, 4);
     assertEquals(actualResult.hashCode(), expectedResult.hashCode());
    }

    @Test
    public void testToString() {
     final LevenshteinDetailedDistance classBeingTested = new LevenshteinDetailedDistance();
     LevenshteinResults actualResult = classBeingTested.apply("fly", "ant");
     LevenshteinResults expectedResult = new LevenshteinResults(3, 0, 0, 3);
     assertEquals(actualResult.toString(), expectedResult.toString());

     actualResult = classBeingTested.apply("hippo", "elephant");
     expectedResult = new LevenshteinResults(7, 3, 0, 4);
     assertEquals(actualResult.toString(), expectedResult.toString());

     actualResult = classBeingTested.apply("", "a");
     expectedResult = new LevenshteinResults(1, 1, 0, 0);
     assertEquals(actualResult.toString(), expectedResult.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLevenshteinDetailedDistance_NullString() throws Exception {
        UNLIMITED_DISTANCE.apply("a", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLevenshteinDetailedDistance_StringNull() throws Exception {
        UNLIMITED_DISTANCE.apply(null, "a");
    }

    @Test
    public void testGetLevenshteinDetailedDistance_StringStringInt() {

        LevenshteinResults result = new LevenshteinDetailedDistance(0).apply("", "");

        assertEquals(0, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(0, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(8).apply("aaapppp", "");
        assertEquals(7, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(7, (int) result.getDeleteCount());
        assertEquals(0, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(7).apply("aaapppp", "");
        assertEquals(7, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(7, (int) result.getDeleteCount());
        assertEquals(0, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(6).apply("aaapppp", "");
        assertEquals(-1, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(0, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(0).apply("b", "a");
        assertEquals(-1, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(0, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(0).apply("a", "b");
        assertEquals(-1, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(0, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(0).apply("aa", "aa");
        assertEquals(0, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(0, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(2).apply("aa", "aa");
        assertEquals(0, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(0, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(2).apply("aaa", "bbb");
        assertEquals(-1, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(0, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(3).apply("aaa", "bbb");
        assertEquals(3, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(3, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(10).apply("aaaaaa", "b");
        assertEquals(6, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(5, (int) result.getDeleteCount());
        assertEquals(1, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(8).apply("aaapppp", "b");
        assertEquals(7, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(6, (int) result.getDeleteCount());
        assertEquals(1, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(4).apply("a", "bbb");
        assertEquals(3, (int) result.getDistance());
        assertEquals(2, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(1, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(7).apply("aaapppp", "b");
        assertEquals(7, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(6, (int) result.getDeleteCount());
        assertEquals(1, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(3).apply("a", "bbb");
        assertEquals(3, (int) result.getDistance());
        assertEquals(2, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(1, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(2).apply("a", "bbb");
        assertEquals(-1, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(0, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(2).apply("bbb", "a");
        assertEquals(-1, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(0, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(6).apply("aaapppp", "b");
        assertEquals(-1, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(0, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(1).apply("a", "bbb");
        assertEquals(-1, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(0, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(1).apply("bbb", "a");
        assertEquals(-1, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(0, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(1).apply("12345", "1234567");
        assertEquals(-1, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(0, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(1).apply("1234567", "12345");
        assertEquals(-1, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(0, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(1).apply("frog", "fog");
        assertEquals(1, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(1, (int) result.getDeleteCount());
        assertEquals(0, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(3).apply("fly", "ant");
        assertEquals(3, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(3, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(7).apply("elephant", "hippo");
        assertEquals(7, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(3, (int) result.getDeleteCount());
        assertEquals(4, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(6).apply("elephant", "hippo");
        assertEquals(-1, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(0, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(7).apply("hippo", "elephant");
        assertEquals(7, (int) result.getDistance());
        assertEquals(3, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(4, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(7).apply("hippo", "elephant");
        assertEquals(7, (int) result.getDistance());
        assertEquals(3, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(4, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(6).apply("hippo", "elephant");
        assertEquals(-1, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(0, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(8).apply("hippo", "zzzzzzzz");
        assertEquals(8, (int) result.getDistance());
        assertEquals(3, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(5, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(8).apply("zzzzzzzz", "hippo");
        assertEquals(8, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(3, (int) result.getDeleteCount());
        assertEquals(5, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(1).apply("hello", "hallo");
        assertEquals(1, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(1, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(Integer.MAX_VALUE).apply("frog", "fog");
        assertEquals(1, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(1, (int) result.getDeleteCount());
        assertEquals(0, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(Integer.MAX_VALUE).apply("fly", "ant");
        assertEquals(3, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(3, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(Integer.MAX_VALUE).apply("elephant", "hippo");
        assertEquals(7, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(3, (int) result.getDeleteCount());
        assertEquals(4, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(Integer.MAX_VALUE).apply("hippo", "elephant");
        assertEquals(7, (int) result.getDistance());
        assertEquals(3, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(4, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(Integer.MAX_VALUE).apply("hippo", "zzzzzzzz");
        assertEquals(8, (int) result.getDistance());
        assertEquals(3, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(5, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(Integer.MAX_VALUE).apply("zzzzzzzz", "hippo");
        assertEquals(8, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(3, (int) result.getDeleteCount());
        assertEquals(5, (int) result.getSubstituteCount());

        result = new LevenshteinDetailedDistance(Integer.MAX_VALUE).apply("hello", "hallo");
        assertEquals(1, (int) result.getDistance());
        assertEquals(0, (int) result.getInsertCount());
        assertEquals(0, (int) result.getDeleteCount());
        assertEquals(1, (int) result.getSubstituteCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLevenshteinDetailedDistance_NullStringInt() throws Exception {
        UNLIMITED_DISTANCE.apply(null, "a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLevenshteinDetailedDistance_StringNullInt() throws Exception {
        UNLIMITED_DISTANCE.apply("a", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeThreshold() throws Exception {
        new LevenshteinDetailedDistance(-1);
    }

    @Test
    public void testGetDefaultInstanceOne() {
        final LevenshteinDetailedDistance levenshteinDetailedDistance =
                LevenshteinDetailedDistance.getDefaultInstance();
        final LevenshteinResults levenshteinResults =
                levenshteinDetailedDistance.apply("Distance: -2147483643, Insert: 0, Delete: 0, Substitute: 0",
                        "Distance: 0, Insert: 2147483536, Delete: 0, Substitute: 0");

        assertEquals(21, (int) levenshteinResults.getDistance());
    }

    @Test
    public void testGetDefaultInstanceTwo() {
        final LevenshteinDetailedDistance levenshteinDetailedDistance =
                LevenshteinDetailedDistance.getDefaultInstance();
        final LevenshteinResults levenshteinResults =
                levenshteinDetailedDistance.apply("Distance: 2147483647, Insert: 0, Delete: 0, Substitute: 0",
                        "Distance: 0, Insert: 2147483647, Delete: 0, Substitute: 0");

        assertEquals(20, (int) levenshteinResults.getDistance());
    }

    @Test
    public void testCreatesLevenshteinDetailedDistanceTakingInteger6() {
        final LevenshteinDetailedDistance levenshteinDetailedDistance = new LevenshteinDetailedDistance(0);
        final LevenshteinResults levenshteinResults =
                levenshteinDetailedDistance.apply("", "Distance: 38, Insert: 0, Delete: 0, Substitute: 0");

        assertEquals(0, (int) levenshteinResults.getSubstituteCount());
        assertEquals(0, (int) levenshteinResults.getDeleteCount());

        assertEquals(0, (int) levenshteinResults.getInsertCount());
        assertEquals(-1, (int) levenshteinResults.getDistance());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyThrowsIllegalArgumentExceptionAndCreatesLevenshteinDetailedDistanceTakingInteger() {
        final LevenshteinDetailedDistance levenshteinDetailedDistance = new LevenshteinDetailedDistance(0);
        final CharSequence charSequence = new StrBuilder();

        levenshteinDetailedDistance.apply(charSequence, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyWithNull() {
        new LevenshteinDetailedDistance(0).apply(null, null);
    }

    @Test
    public void testGetThreshold() {
        final LevenshteinDetailedDistance levenshteinDetailedDistance = new LevenshteinDetailedDistance(0);

        assertEquals(0, (int) levenshteinDetailedDistance.getThreshold());
    }

}
