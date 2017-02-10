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

/**
 * Unit tests for {@link LevenshteinDistance}.
 */
public class LevenshteinDistanceTest {

    private static final LevenshteinDistance UNLIMITED_DISTANCE = new LevenshteinDistance();

    @Test
    public void testGetLevenshteinDistance_StringString() {
        assertEquals(0, (int) UNLIMITED_DISTANCE.apply("", ""));
        assertEquals(1, (int) UNLIMITED_DISTANCE.apply("", "a"));
        assertEquals(7, (int) UNLIMITED_DISTANCE.apply("aaapppp", ""));
        assertEquals(1, (int) UNLIMITED_DISTANCE.apply("frog", "fog"));
        assertEquals(3, (int) UNLIMITED_DISTANCE.apply("fly", "ant"));
        assertEquals(7, (int) UNLIMITED_DISTANCE.apply("elephant", "hippo"));
        assertEquals(7, (int) UNLIMITED_DISTANCE.apply("hippo", "elephant"));
        assertEquals(8, (int) UNLIMITED_DISTANCE.apply("hippo", "zzzzzzzz"));
        assertEquals(8, (int) UNLIMITED_DISTANCE.apply("zzzzzzzz", "hippo"));
        assertEquals(1, (int) UNLIMITED_DISTANCE.apply("hello", "hallo"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLevenshteinDistance_NullString() throws Exception {
        UNLIMITED_DISTANCE.apply("a", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLevenshteinDistance_StringNull() throws Exception {
        UNLIMITED_DISTANCE.apply(null, "a");
    }

    @Test
    public void testGetLevenshteinDistance_StringStringInt() {
        // empty strings
        assertEquals(0, (int) new LevenshteinDistance(0).apply("", ""));
        assertEquals(7, (int) new LevenshteinDistance(8).apply("aaapppp", ""));
        assertEquals(7, (int) new LevenshteinDistance(7).apply("aaapppp", ""));
        assertEquals(-1, (int) new LevenshteinDistance(6).apply("aaapppp", ""));

        // unequal strings, zero threshold
        assertEquals(-1, (int) new LevenshteinDistance(0).apply("b", "a"));
        assertEquals(-1, (int) new LevenshteinDistance(0).apply("a", "b"));

        // equal strings
        assertEquals(0, (int) new LevenshteinDistance(0).apply("aa", "aa"));
        assertEquals(0, (int) new LevenshteinDistance(2).apply("aa", "aa"));

        // same length
        assertEquals(-1, (int) new LevenshteinDistance(2).apply("aaa", "bbb"));
        assertEquals(3, (int) new LevenshteinDistance(3).apply("aaa", "bbb"));

        // big stripe
        assertEquals(6, (int) new LevenshteinDistance(10).apply("aaaaaa", "b"));

        // distance less than threshold
        assertEquals(7, (int) new LevenshteinDistance(8).apply("aaapppp", "b"));
        assertEquals(3, (int) new LevenshteinDistance(4).apply("a", "bbb"));

        // distance equal to threshold
        assertEquals(7, (int) new LevenshteinDistance(7).apply("aaapppp", "b"));
        assertEquals(3, (int) new LevenshteinDistance(3).apply("a", "bbb"));

        // distance greater than threshold
        assertEquals(-1, (int) new LevenshteinDistance(2).apply("a", "bbb"));
        assertEquals(-1, (int) new LevenshteinDistance(2).apply("bbb", "a"));
        assertEquals(-1, (int) new LevenshteinDistance(6).apply("aaapppp", "b"));

        // stripe runs off array, strings not similar
        assertEquals(-1, (int) new LevenshteinDistance(1).apply("a", "bbb"));
        assertEquals(-1, (int) new LevenshteinDistance(1).apply("bbb", "a"));

        // stripe runs off array, strings are similar
        assertEquals(-1, (int) new LevenshteinDistance(1).apply("12345", "1234567"));
        assertEquals(-1, (int) new LevenshteinDistance(1).apply("1234567", "12345"));

        // old getLevenshteinDistance test cases
        assertEquals(1, (int) new LevenshteinDistance(1).apply("frog", "fog"));
        assertEquals(3, (int) new LevenshteinDistance(3).apply("fly", "ant"));
        assertEquals(7, (int) new LevenshteinDistance(7).apply("elephant", "hippo"));
        assertEquals(-1, (int) new LevenshteinDistance(6).apply("elephant", "hippo"));
        assertEquals(7, (int) new LevenshteinDistance(7).apply("hippo", "elephant"));
        assertEquals(-1, (int) new LevenshteinDistance(6).apply("hippo", "elephant"));
        assertEquals(8, (int) new LevenshteinDistance(8).apply("hippo", "zzzzzzzz"));
        assertEquals(8, (int) new LevenshteinDistance(8).apply("zzzzzzzz", "hippo"));
        assertEquals(1, (int) new LevenshteinDistance(1).apply("hello", "hallo"));

        assertEquals(1,
                (int) new LevenshteinDistance(Integer.MAX_VALUE).apply("frog", "fog"));
        assertEquals(3, (int) new LevenshteinDistance(Integer.MAX_VALUE).apply("fly", "ant"));
        assertEquals(7,
                (int) new LevenshteinDistance(Integer.MAX_VALUE).apply("elephant", "hippo"));
        assertEquals(7,
                (int) new LevenshteinDistance(Integer.MAX_VALUE).apply("hippo", "elephant"));
        assertEquals(8,
                (int) new LevenshteinDistance(Integer.MAX_VALUE).apply("hippo", "zzzzzzzz"));
        assertEquals(8,
                (int) new LevenshteinDistance(Integer.MAX_VALUE).apply("zzzzzzzz", "hippo"));
        assertEquals(1,
                (int) new LevenshteinDistance(Integer.MAX_VALUE).apply("hello", "hallo"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLevenshteinDistance_NullStringInt() throws Exception {
        UNLIMITED_DISTANCE.apply(null, "a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLevenshteinDistance_StringNullInt() throws Exception {
        UNLIMITED_DISTANCE.apply("a", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeThreshold() throws Exception {
        new LevenshteinDistance(-1);
    }

}
