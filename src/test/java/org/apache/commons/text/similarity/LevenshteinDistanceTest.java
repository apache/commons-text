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

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.text.LevenshteinDistance}.
 */
public class LevenshteinDistanceTest {

    private static LevenshteinDistance distance;

    @BeforeClass
    public static void setUp() {
        distance = new LevenshteinDistance();
    }

    @Test
    public void testGetLevenshteinDistance_StringString() {
        assertEquals(0, (int) distance.apply("", ""));
        assertEquals(1, (int) distance.apply("", "a"));
        assertEquals(7, (int) distance.apply("aaapppp", ""));
        assertEquals(1, (int) distance.apply("frog", "fog"));
        assertEquals(3, (int) distance.apply("fly", "ant"));
        assertEquals(7, (int) distance.apply("elephant", "hippo"));
        assertEquals(7, (int) distance.apply("hippo", "elephant"));
        assertEquals(8, (int) distance.apply("hippo", "zzzzzzzz"));
        assertEquals(8, (int) distance.apply("zzzzzzzz", "hippo"));
        assertEquals(1, (int) distance.apply("hello", "hallo"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLevenshteinDistance_NullString() throws Exception {
        distance.apply("a", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLevenshteinDistance_StringNull() throws Exception {
        distance.apply(null, "a");
    }

    @Test
    public void testGetLevenshteinDistance_StringStringInt() {
        // empty strings
        assertEquals(0, (int) distance.apply("", "", 0));
        assertEquals(7, (int) distance.apply("aaapppp", "", 8));
        assertEquals(7, (int) distance.apply("aaapppp", "", 7));
        assertEquals(-1, (int) distance.apply("aaapppp", "", 6));

        // unequal strings, zero threshold
        assertEquals(-1, (int) distance.apply("b", "a", 0));
        assertEquals(-1, (int) distance.apply("a", "b", 0));

        // equal strings
        assertEquals(0, (int) distance.apply("aa", "aa", 0));
        assertEquals(0, (int) distance.apply("aa", "aa", 2));

        // same length
        assertEquals(-1, (int) distance.apply("aaa", "bbb", 2));
        assertEquals(3, (int) distance.apply("aaa", "bbb", 3));

        // big stripe
        assertEquals(6, (int) distance.apply("aaaaaa", "b", 10));

        // distance less than threshold
        assertEquals(7, (int) distance.apply("aaapppp", "b", 8));
        assertEquals(3, (int) distance.apply("a", "bbb", 4));

        // distance equal to threshold
        assertEquals(7, (int) distance.apply("aaapppp", "b", 7));
        assertEquals(3, (int) distance.apply("a", "bbb", 3));

        // distance greater than threshold
        assertEquals(-1, (int) distance.apply("a", "bbb", 2));
        assertEquals(-1, (int) distance.apply("bbb", "a", 2));
        assertEquals(-1, (int) distance.apply("aaapppp", "b", 6));

        // stripe runs off array, strings not similar
        assertEquals(-1, (int) distance.apply("a", "bbb", 1));
        assertEquals(-1, (int) distance.apply("bbb", "a", 1));

        // stripe runs off array, strings are similar
        assertEquals(-1, (int) distance.apply("12345", "1234567", 1));
        assertEquals(-1, (int) distance.apply("1234567", "12345", 1));

        // old getLevenshteinDistance test cases
        assertEquals(1, (int) distance.apply("frog", "fog", 1));
        assertEquals(3, (int) distance.apply("fly", "ant", 3));
        assertEquals(7, (int) distance.apply("elephant", "hippo", 7));
        assertEquals(-1, (int) distance.apply("elephant", "hippo", 6));
        assertEquals(7, (int) distance.apply("hippo", "elephant", 7));
        assertEquals(-1, (int) distance.apply("hippo", "elephant", 6));
        assertEquals(8, (int) distance.apply("hippo", "zzzzzzzz", 8));
        assertEquals(8, (int) distance.apply("zzzzzzzz", "hippo", 8));
        assertEquals(1, (int) distance.apply("hello", "hallo", 1));

        assertEquals(1,
                (int) distance.apply("frog", "fog", Integer.MAX_VALUE));
        assertEquals(3, (int) distance.apply("fly", "ant", Integer.MAX_VALUE));
        assertEquals(7,
                (int) distance.apply("elephant", "hippo", Integer.MAX_VALUE));
        assertEquals(7,
                (int) distance.apply("hippo", "elephant", Integer.MAX_VALUE));
        assertEquals(8,
                (int) distance.apply("hippo", "zzzzzzzz", Integer.MAX_VALUE));
        assertEquals(8,
                (int) distance.apply("zzzzzzzz", "hippo", Integer.MAX_VALUE));
        assertEquals(1,
                (int) distance.apply("hello", "hallo", Integer.MAX_VALUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLevenshteinDistance_NullStringInt() throws Exception {
        distance.apply(null, "a", 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLevenshteinDistance_StringNullInt() throws Exception {
        distance.apply("a", null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLevenshteinDistance_StringStringNegativeInt()
            throws Exception {
        distance.apply("a", "a", -1);
    }

}
