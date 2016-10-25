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
}
