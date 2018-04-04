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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.apache.commons.text.TextStringBuilder;
import org.junit.jupiter.api.Test;

public class LevenshteinDetailedDistanceTest {

    private static final LevenshteinDetailedDistance UNLIMITED_DISTANCE = new LevenshteinDetailedDistance();

    @Test
    public void testGetLevenshteinDetailedDistance_StringString() {
        LevenshteinResults result = UNLIMITED_DISTANCE.apply("", "");
        assertThat(result.getDistance()).isEqualTo(0);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(0);

        result = UNLIMITED_DISTANCE.apply("", "a");
        assertThat(result.getDistance()).isEqualTo(1);
        assertThat(result.getInsertCount()).isEqualTo(1);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(0);

        result = UNLIMITED_DISTANCE.apply("aaapppp", "");
        assertThat(result.getDistance()).isEqualTo(7);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(7);
        assertThat(result.getSubstituteCount()).isEqualTo(0);

        result = UNLIMITED_DISTANCE.apply("frog", "fog");
        assertThat(result.getDistance()).isEqualTo(1);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(1);
        assertThat(result.getSubstituteCount()).isEqualTo(0);

        result = UNLIMITED_DISTANCE.apply("fly", "ant");
        assertThat(result.getDistance()).isEqualTo(3);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(3);

        result = UNLIMITED_DISTANCE.apply("elephant", "hippo");
        assertThat(result.getDistance()).isEqualTo(7);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(3);
        assertThat(result.getSubstituteCount()).isEqualTo(4);

        result = UNLIMITED_DISTANCE.apply("hippo", "elephant");
        assertThat(result.getDistance()).isEqualTo(7);
        assertThat(result.getInsertCount()).isEqualTo(3);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(4);

        result = UNLIMITED_DISTANCE.apply("hippo", "zzzzzzzz");
        assertThat(result.getDistance()).isEqualTo(8);
        assertThat(result.getInsertCount()).isEqualTo(3);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(5);

        result = UNLIMITED_DISTANCE.apply("zzzzzzzz", "hippo");
        assertThat(result.getDistance()).isEqualTo(8);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(3);
        assertThat(result.getSubstituteCount()).isEqualTo(5);

        result = UNLIMITED_DISTANCE.apply("hello", "hallo");
        assertThat(result.getDistance()).isEqualTo(1);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(1);
    }

    @Test
    public void testEquals() {
     final LevenshteinDetailedDistance classBeingTested = new LevenshteinDetailedDistance();
     LevenshteinResults actualResult = classBeingTested.apply("hello", "hallo");
     LevenshteinResults expectedResult = new LevenshteinResults(1, 0, 0, 1);
     assertThat(expectedResult).isEqualTo(actualResult);

     actualResult = classBeingTested.apply("zzzzzzzz", "hippo");
     expectedResult = new LevenshteinResults(8, 0, 3, 5);
     assertThat(expectedResult).isEqualTo(actualResult);
     assertThat(actualResult).isEqualTo(actualResult); //intentionally added

     actualResult = classBeingTested.apply("", "");
     expectedResult = new LevenshteinResults(0, 0, 0, 0);
     assertThat(expectedResult).isEqualTo(actualResult);
    }

    @Test
    public void testHashCode() {
     final LevenshteinDetailedDistance classBeingTested = new LevenshteinDetailedDistance();
     LevenshteinResults actualResult = classBeingTested.apply("aaapppp", "");
     LevenshteinResults expectedResult = new LevenshteinResults(7, 0, 7, 0);
     assertThat(expectedResult.hashCode()).isEqualTo(actualResult.hashCode());

     actualResult = classBeingTested.apply("frog", "fog");
     expectedResult = new LevenshteinResults(1, 0, 1, 0);
     assertThat(expectedResult.hashCode()).isEqualTo(actualResult.hashCode());

     actualResult = classBeingTested.apply("elephant", "hippo");
     expectedResult = new LevenshteinResults(7, 0, 3, 4);
     assertThat(expectedResult.hashCode()).isEqualTo(actualResult.hashCode());
    }

    @Test
    public void testToString() {
     final LevenshteinDetailedDistance classBeingTested = new LevenshteinDetailedDistance();
     LevenshteinResults actualResult = classBeingTested.apply("fly", "ant");
     LevenshteinResults expectedResult = new LevenshteinResults(3, 0, 0, 3);
     assertThat(expectedResult.toString()).isEqualTo(actualResult.toString());

     actualResult = classBeingTested.apply("hippo", "elephant");
     expectedResult = new LevenshteinResults(7, 3, 0, 4);
     assertThat(expectedResult.toString()).isEqualTo(actualResult.toString());

     actualResult = classBeingTested.apply("", "a");
     expectedResult = new LevenshteinResults(1, 1, 0, 0);
     assertThat(expectedResult.toString()).isEqualTo(actualResult.toString());
    }

    @Test
    public void testGetLevenshteinDetailedDistance_NullString() {
        assertThatIllegalArgumentException().isThrownBy(() -> UNLIMITED_DISTANCE.apply("a", null));
    }

    @Test
    public void testGetLevenshteinDetailedDistance_StringNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> UNLIMITED_DISTANCE.apply(null, "a"));
    }

    @Test
    public void testGetLevenshteinDetailedDistance_StringStringInt() {

        LevenshteinResults result = new LevenshteinDetailedDistance(0).apply("", "");

        assertThat(result.getDistance()).isEqualTo(0);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(0);

        result = new LevenshteinDetailedDistance(8).apply("aaapppp", "");
        assertThat(result.getDistance()).isEqualTo(7);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(7);
        assertThat(result.getSubstituteCount()).isEqualTo(0);

        result = new LevenshteinDetailedDistance(7).apply("aaapppp", "");
        assertThat(result.getDistance()).isEqualTo(7);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(7);
        assertThat(result.getSubstituteCount()).isEqualTo(0);

        result = new LevenshteinDetailedDistance(6).apply("aaapppp", "");
        assertThat(result.getDistance()).isEqualTo(-1);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(0);

        result = new LevenshteinDetailedDistance(0).apply("b", "a");
        assertThat(result.getDistance()).isEqualTo(-1);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(0);

        result = new LevenshteinDetailedDistance(0).apply("a", "b");
        assertThat(result.getDistance()).isEqualTo(-1);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(0);

        result = new LevenshteinDetailedDistance(0).apply("aa", "aa");
        assertThat(result.getDistance()).isEqualTo(0);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(0);

        result = new LevenshteinDetailedDistance(2).apply("aa", "aa");
        assertThat(result.getDistance()).isEqualTo(0);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(0);

        result = new LevenshteinDetailedDistance(2).apply("aaa", "bbb");
        assertThat(result.getDistance()).isEqualTo(-1);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(0);

        result = new LevenshteinDetailedDistance(3).apply("aaa", "bbb");
        assertThat(result.getDistance()).isEqualTo(3);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(3);

        result = new LevenshteinDetailedDistance(10).apply("aaaaaa", "b");
        assertThat(result.getDistance()).isEqualTo(6);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(5);
        assertThat(result.getSubstituteCount()).isEqualTo(1);

        result = new LevenshteinDetailedDistance(8).apply("aaapppp", "b");
        assertThat(result.getDistance()).isEqualTo(7);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(6);
        assertThat(result.getSubstituteCount()).isEqualTo(1);

        result = new LevenshteinDetailedDistance(4).apply("a", "bbb");
        assertThat(result.getDistance()).isEqualTo(3);
        assertThat(result.getInsertCount()).isEqualTo(2);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(1);

        result = new LevenshteinDetailedDistance(7).apply("aaapppp", "b");
        assertThat(result.getDistance()).isEqualTo(7);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(6);
        assertThat(result.getSubstituteCount()).isEqualTo(1);

        result = new LevenshteinDetailedDistance(3).apply("a", "bbb");
        assertThat(result.getDistance()).isEqualTo(3);
        assertThat(result.getInsertCount()).isEqualTo(2);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(1);

        result = new LevenshteinDetailedDistance(2).apply("a", "bbb");
        assertThat(result.getDistance()).isEqualTo(-1);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(0);

        result = new LevenshteinDetailedDistance(2).apply("bbb", "a");
        assertThat(result.getDistance()).isEqualTo(-1);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(0);

        result = new LevenshteinDetailedDistance(6).apply("aaapppp", "b");
        assertThat(result.getDistance()).isEqualTo(-1);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(0);

        result = new LevenshteinDetailedDistance(1).apply("a", "bbb");
        assertThat(result.getDistance()).isEqualTo(-1);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(0);

        result = new LevenshteinDetailedDistance(1).apply("bbb", "a");
        assertThat(result.getDistance()).isEqualTo(-1);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(0);

        result = new LevenshteinDetailedDistance(1).apply("12345", "1234567");
        assertThat(result.getDistance()).isEqualTo(-1);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(0);

        result = new LevenshteinDetailedDistance(1).apply("1234567", "12345");
        assertThat(result.getDistance()).isEqualTo(-1);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(0);

        result = new LevenshteinDetailedDistance(1).apply("frog", "fog");
        assertThat(result.getDistance()).isEqualTo(1);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(1);
        assertThat(result.getSubstituteCount()).isEqualTo(0);

        result = new LevenshteinDetailedDistance(3).apply("fly", "ant");
        assertThat(result.getDistance()).isEqualTo(3);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(3);

        result = new LevenshteinDetailedDistance(7).apply("elephant", "hippo");
        assertThat(result.getDistance()).isEqualTo(7);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(3);
        assertThat(result.getSubstituteCount()).isEqualTo(4);

        result = new LevenshteinDetailedDistance(6).apply("elephant", "hippo");
        assertThat(result.getDistance()).isEqualTo(-1);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(0);

        result = new LevenshteinDetailedDistance(7).apply("hippo", "elephant");
        assertThat(result.getDistance()).isEqualTo(7);
        assertThat(result.getInsertCount()).isEqualTo(3);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(4);

        result = new LevenshteinDetailedDistance(7).apply("hippo", "elephant");
        assertThat(result.getDistance()).isEqualTo(7);
        assertThat(result.getInsertCount()).isEqualTo(3);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(4);

        result = new LevenshteinDetailedDistance(6).apply("hippo", "elephant");
        assertThat(result.getDistance()).isEqualTo(-1);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(0);

        result = new LevenshteinDetailedDistance(8).apply("hippo", "zzzzzzzz");
        assertThat(result.getDistance()).isEqualTo(8);
        assertThat(result.getInsertCount()).isEqualTo(3);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(5);

        result = new LevenshteinDetailedDistance(8).apply("zzzzzzzz", "hippo");
        assertThat(result.getDistance()).isEqualTo(8);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(3);
        assertThat(result.getSubstituteCount()).isEqualTo(5);

        result = new LevenshteinDetailedDistance(1).apply("hello", "hallo");
        assertThat(result.getDistance()).isEqualTo(1);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(1);

        result = new LevenshteinDetailedDistance(Integer.MAX_VALUE).apply("frog", "fog");
        assertThat(result.getDistance()).isEqualTo(1);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(1);
        assertThat(result.getSubstituteCount()).isEqualTo(0);

        result = new LevenshteinDetailedDistance(Integer.MAX_VALUE).apply("fly", "ant");
        assertThat(result.getDistance()).isEqualTo(3);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(3);

        result = new LevenshteinDetailedDistance(Integer.MAX_VALUE).apply("elephant", "hippo");
        assertThat(result.getDistance()).isEqualTo(7);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(3);
        assertThat(result.getSubstituteCount()).isEqualTo(4);

        result = new LevenshteinDetailedDistance(Integer.MAX_VALUE).apply("hippo", "elephant");
        assertThat(result.getDistance()).isEqualTo(7);
        assertThat(result.getInsertCount()).isEqualTo(3);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(4);

        result = new LevenshteinDetailedDistance(Integer.MAX_VALUE).apply("hippo", "zzzzzzzz");
        assertThat(result.getDistance()).isEqualTo(8);
        assertThat(result.getInsertCount()).isEqualTo(3);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(5);

        result = new LevenshteinDetailedDistance(Integer.MAX_VALUE).apply("zzzzzzzz", "hippo");
        assertThat(result.getDistance()).isEqualTo(8);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(3);
        assertThat(result.getSubstituteCount()).isEqualTo(5);

        result = new LevenshteinDetailedDistance(Integer.MAX_VALUE).apply("hello", "hallo");
        assertThat(result.getDistance()).isEqualTo(1);
        assertThat(result.getInsertCount()).isEqualTo(0);
        assertThat(result.getDeleteCount()).isEqualTo(0);
        assertThat(result.getSubstituteCount()).isEqualTo(1);
    }

    @Test
    public void testGetLevenshteinDetailedDistance_NullStringInt() {
        assertThatIllegalArgumentException().isThrownBy(() -> UNLIMITED_DISTANCE.apply(null, "a"));
    }

    @Test
    public void testGetLevenshteinDetailedDistance_StringNullInt() {
        assertThatIllegalArgumentException().isThrownBy(() -> UNLIMITED_DISTANCE.apply("a", null));
    }

    @Test
    public void testConstructorWithNegativeThreshold() {
        assertThatIllegalArgumentException().isThrownBy(() -> new LevenshteinDetailedDistance(-1));
    }

    @Test
    public void testGetDefaultInstanceOne() {
        final LevenshteinDetailedDistance levenshteinDetailedDistance =
                LevenshteinDetailedDistance.getDefaultInstance();
        final LevenshteinResults levenshteinResults =
                levenshteinDetailedDistance.apply("Distance: -2147483643, Insert: 0, Delete: 0, Substitute: 0",
                        "Distance: 0, Insert: 2147483536, Delete: 0, Substitute: 0");

        assertThat(levenshteinResults.getDistance()).isEqualTo(21);
    }

    @Test
    public void testGetDefaultInstanceTwo() {
        final LevenshteinDetailedDistance levenshteinDetailedDistance =
                LevenshteinDetailedDistance.getDefaultInstance();
        final LevenshteinResults levenshteinResults =
                levenshteinDetailedDistance.apply("Distance: 2147483647, Insert: 0, Delete: 0, Substitute: 0",
                        "Distance: 0, Insert: 2147483647, Delete: 0, Substitute: 0");

        assertThat(levenshteinResults.getDistance()).isEqualTo(20);
    }

    @Test
    public void testCreatesLevenshteinDetailedDistanceTakingInteger6() {
        final LevenshteinDetailedDistance levenshteinDetailedDistance = new LevenshteinDetailedDistance(0);
        final LevenshteinResults levenshteinResults =
                levenshteinDetailedDistance.apply("", "Distance: 38, Insert: 0, Delete: 0, Substitute: 0");

        assertThat(levenshteinResults.getSubstituteCount()).isEqualTo(0);
        assertThat(levenshteinResults.getDeleteCount()).isEqualTo(0);

        assertThat(levenshteinResults.getInsertCount()).isEqualTo(0);
        assertThat(levenshteinResults.getDistance()).isEqualTo(-1);
    }

    @Test
    public void testApplyThrowsIllegalArgumentExceptionAndCreatesLevenshteinDetailedDistanceTakingInteger() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            final LevenshteinDetailedDistance levenshteinDetailedDistance = new LevenshteinDetailedDistance(0);
            final CharSequence charSequence = new TextStringBuilder();

            levenshteinDetailedDistance.apply(charSequence, null);
        });
    }

    @Test
    public void testApplyWithNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> new LevenshteinDetailedDistance(0).apply(null, null));
    }

    @Test
    public void testGetThreshold() {
        final LevenshteinDetailedDistance levenshteinDetailedDistance = new LevenshteinDetailedDistance(0);

        assertThat(levenshteinDetailedDistance.getThreshold()).isEqualTo(0);
    }

}
