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

import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.text.LevenshteinDistance}.
 */
public class LevenshteinDistanceTest {

    @Test
    public void testGetLevenshteinDistance_StringString() {
        LevenshteinDistance distance = new LevenshteinDistance();

        assertEquals(0, (int) distance.compare("", ""));
        assertEquals(1, (int) distance.compare("", "a"));
        assertEquals(7, (int) distance.compare("aaapppp", ""));
        assertEquals(1, (int) distance.compare("frog", "fog"));
        assertEquals(3, (int) distance.compare("fly", "ant"));
        assertEquals(7, (int) distance.compare("elephant", "hippo"));
        assertEquals(7, (int) distance.compare("hippo", "elephant"));
        assertEquals(8, (int) distance.compare("hippo", "zzzzzzzz"));
        assertEquals(8, (int) distance.compare("zzzzzzzz", "hippo"));
        assertEquals(1, (int) distance.compare("hello", "hallo"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLevenshteinDistance_NullString() throws Exception {
        LevenshteinDistance distance = new LevenshteinDistance();

        distance.compare("a", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLevenshteinDistance_StringNull() throws Exception {
        LevenshteinDistance distance = new LevenshteinDistance();

        distance.compare(null, "a");
    }

    @Test
    public void testGetLevenshteinDistance_StringStringInt() {
        Map<Integer, LevenshteinDistance> distances = new TreeMap<Integer, LevenshteinDistance>();
        for (int threshold : new int[] { 0, 1, 2, 3, 4, 6, 7, 8, 10, Integer.MAX_VALUE }) {
            distances.put(threshold, new LevenshteinDistance(threshold));
        }

        // empty strings
        assertEquals(0, (int) distances.get(0).compare("", ""));
        assertEquals(7, (int) distances.get(8).compare("aaapppp", ""));
        assertEquals(7, (int) distances.get(7).compare("aaapppp", ""));
        assertEquals(-1, (int) distances.get(6).compare("aaapppp", ""));

        // unequal strings, zero threshold
        assertEquals(-1, (int) distances.get(0).compare("b", "a"));
        assertEquals(-1, (int) distances.get(0).compare("a", "b"));

        // equal strings
        assertEquals(0, (int) distances.get(0).compare("aa", "aa"));
        assertEquals(0, (int) distances.get(2).compare("aa", "aa"));

        // same length
        assertEquals(-1, (int) distances.get(2).compare("aaa", "bbb"));
        assertEquals(3, (int) distances.get(3).compare("aaa", "bbb"));

        // big stripe
        assertEquals(6, (int) distances.get(10).compare("aaaaaa", "b"));

        // distance less than threshold
        assertEquals(7, (int) distances.get(8).compare("aaapppp", "b"));
        assertEquals(3, (int) distances.get(4).compare("a", "bbb"));

        // distance equal to threshold
        assertEquals(7, (int) distances.get(7).compare("aaapppp", "b"));
        assertEquals(3, (int) distances.get(3).compare("a", "bbb"));

        // distance greater than threshold
        assertEquals(-1, (int) distances.get(2).compare("a", "bbb"));
        assertEquals(-1, (int) distances.get(2).compare("bbb", "a"));
        assertEquals(-1, (int) distances.get(6).compare("aaapppp", "b"));

        // stripe runs off array, strings not similar
        assertEquals(-1, (int) distances.get(1).compare("a", "bbb"));
        assertEquals(-1, (int) distances.get(1).compare("bbb", "a"));

        // stripe runs off array, strings are similar
        assertEquals(-1, (int) distances.get(1).compare("12345", "1234567"));
        assertEquals(-1, (int) distances.get(1).compare("1234567", "12345"));

        // old getLevenshteinDistance test cases
        assertEquals(1, (int) distances.get(1).compare("frog", "fog"));
        assertEquals(3, (int) distances.get(3).compare("fly", "ant"));
        assertEquals(7, (int) distances.get(7).compare("elephant", "hippo"));
        assertEquals(-1, (int) distances.get(6).compare("elephant", "hippo"));
        assertEquals(7, (int) distances.get(7).compare("hippo", "elephant"));
        assertEquals(-1, (int) distances.get(6).compare("hippo", "elephant"));
        assertEquals(8, (int) distances.get(8).compare("hippo", "zzzzzzzz"));
        assertEquals(8, (int) distances.get(8).compare("zzzzzzzz", "hippo"));
        assertEquals(1, (int) distances.get(1).compare("hello", "hallo"));

        assertEquals(1,
                (int) distances.get(Integer.MAX_VALUE).compare("frog", "fog"));
        assertEquals(3, (int) distances.get(Integer.MAX_VALUE).compare("fly", "ant"));
        assertEquals(7,
                (int) distances.get(Integer.MAX_VALUE).compare("elephant", "hippo"));
        assertEquals(7,
                (int) distances.get(Integer.MAX_VALUE).compare("hippo", "elephant"));
        assertEquals(8,
                (int) distances.get(Integer.MAX_VALUE).compare("hippo", "zzzzzzzz"));
        assertEquals(8,
                (int) distances.get(Integer.MAX_VALUE).compare("zzzzzzzz", "hippo"));
        assertEquals(1,
                (int) distances.get(Integer.MAX_VALUE).compare("hello", "hallo"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLevenshteinDistance_NullStringInt() throws Exception {
        LevenshteinDistance distance = new LevenshteinDistance(0);

        distance.compare(null, "a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLevenshteinDistance_StringNullInt() throws Exception {
        LevenshteinDistance distance = new LevenshteinDistance(0);

        distance.compare("a", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeThreshold()
            throws Exception {

        LevenshteinDistance distance = new LevenshteinDistance(-1);
    }

}
