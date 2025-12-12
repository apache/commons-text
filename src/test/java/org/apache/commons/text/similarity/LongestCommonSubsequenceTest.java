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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link LongestCommonSubsequence}.
 */
class LongestCommonSubsequenceTest {

    private static LongestCommonSubsequence subject;

    @BeforeAll
    public static void setup() {
        subject = new LongestCommonSubsequence();
    }

    @Test
    @SuppressWarnings("deprecation")
    void testGettingLogestCommonSubsequenceNullNull() {
        assertThrows(IllegalArgumentException.class, () -> subject.logestCommonSubsequence(null, null));
    }

    @Test
    @SuppressWarnings("deprecation")
    void testGettingLogestCommonSubsequenceNullString() {
        assertThrows(IllegalArgumentException.class, () -> subject.logestCommonSubsequence(null, "right"));
    }

    @Test
    @SuppressWarnings("deprecation")
    void testGettingLogestCommonSubsequenceStringNull() {
        assertThrows(IllegalArgumentException.class, () -> subject.logestCommonSubsequence(" ", null));
    }

    @Test
    void testGettingLongestCommonSubsequenceApplyNullNull() {
        assertThrows(IllegalArgumentException.class, () -> subject.apply(null, null));
    }

    @Test
    void testGettingLongestCommonSubsequenceApplyNullString() {
        assertThrows(IllegalArgumentException.class, () -> subject.apply(null, "right"));
    }

    @Test
    void testGettingLongestCommonSubsequenceApplyStringNull() {
        assertThrows(IllegalArgumentException.class, () -> subject.apply(" ", null));
    }

    @Test
    void testGettingLongestCommonSubsequenceNullNull() {
        assertThrows(IllegalArgumentException.class, () -> subject.longestCommonSubsequence(null, null));
    }

    @Test
    void testGettingLongestCommonSubsequenceNullString() {
        assertThrows(IllegalArgumentException.class, () -> subject.longestCommonSubsequence(null, "right"));
    }

    @Test
    void testGettingLongestCommonSubsequenceStringNull() {
        assertThrows(IllegalArgumentException.class, () -> subject.longestCommonSubsequence(" ", null));
    }

    @Test
    @Deprecated
    void testLogestCommonSubsequence() {
        assertEquals("", subject.logestCommonSubsequence("", ""));
        assertEquals("", subject.logestCommonSubsequence("left", ""));
        assertEquals("", subject.logestCommonSubsequence("", "right"));
        assertEquals("", subject.logestCommonSubsequence("l", "a"));
        assertEquals("", subject.logestCommonSubsequence("left", "a"));
        assertEquals("fog", subject.logestCommonSubsequence("frog", "fog"));
        assertEquals("", subject.logestCommonSubsequence("fly", "ant"));
        assertEquals("h", subject.logestCommonSubsequence("elephant", "hippo"));
        assertEquals("ABC Corp", subject.logestCommonSubsequence("ABC Corporation", "ABC Corp"));
        assertEquals("D  H Enterprises Inc", subject.logestCommonSubsequence("D N H Enterprises Inc", "D & H Enterprises, Inc."));
        assertEquals("My Gym Childrens Fitness", subject.logestCommonSubsequence("My Gym Children's Fitness Center", "My Gym. Childrens Fitness"));
        assertEquals("PENNSYLVNIA", subject.logestCommonSubsequence("PENNSYLVANIA", "PENNCISYLVNIA"));
        assertEquals("t", subject.logestCommonSubsequence("left", "right"));
        assertEquals("tttt", subject.logestCommonSubsequence("leettteft", "ritttght"));
        assertEquals("the same string", subject.logestCommonSubsequence("the same string", "the same string"));
    }

    @Test
    void testLongestCommonSubsequence() {
        assertEquals("", subject.longestCommonSubsequence("", ""));
        assertEquals("", subject.longestCommonSubsequence("left", ""));
        assertEquals("", subject.longestCommonSubsequence("", "right"));
        assertEquals("", subject.longestCommonSubsequence("l", "a"));
        assertEquals("", subject.longestCommonSubsequence("left", "a"));
        assertEquals("fog", subject.longestCommonSubsequence("frog", "fog"));
        assertEquals("", subject.longestCommonSubsequence("fly", "ant"));
        assertEquals("h", subject.longestCommonSubsequence("elephant", "hippo"));
        assertEquals("ABC Corp", subject.longestCommonSubsequence("ABC Corporation", "ABC Corp"));
        assertEquals("D  H Enterprises Inc", subject.longestCommonSubsequence("D N H Enterprises Inc", "D & H Enterprises, Inc."));
        assertEquals("My Gym Childrens Fitness", subject.longestCommonSubsequence("My Gym Children's Fitness Center", "My Gym. Childrens Fitness"));
        assertEquals("PENNSYLVNIA", subject.longestCommonSubsequence("PENNSYLVANIA", "PENNCISYLVNIA"));
        assertEquals("t", subject.longestCommonSubsequence("left", "right"));
        assertEquals("tttt", subject.longestCommonSubsequence("leettteft", "ritttght"));
        assertEquals("the same string", subject.longestCommonSubsequence("the same string", "the same string"));
    }

    @Test
    void testLongestCommonSubsequenceApply() {
        assertEquals(0, subject.apply("", ""));
        assertEquals(0, subject.apply("left", ""));
        assertEquals(0, subject.apply("", "right"));
        assertEquals(3, subject.apply("frog", "fog"));
        assertEquals(0, subject.apply("fly", "ant"));
        assertEquals(1, subject.apply("elephant", "hippo"));
        assertEquals(8, subject.apply("ABC Corporation", "ABC Corp"));
        assertEquals(20, subject.apply("D N H Enterprises Inc", "D & H Enterprises, Inc."));
        assertEquals(24, subject.apply("My Gym Children's Fitness Center", "My Gym. Childrens Fitness"));
        assertEquals(11, subject.apply("PENNSYLVANIA", "PENNCISYLVNIA"));
        assertEquals(1, subject.apply("left", "right"));
        assertEquals(4, subject.apply("leettteft", "ritttght"));
        assertEquals(15, subject.apply("the same string", "the same string"));
    }

    @Test
    void testLongestCommonSubstringLengthArray() {
        assertArrayEquals(new int[][]{ {0, 0, 0, 0}, {0, 1, 1, 1}, {0, 1, 2, 2}}, subject.longestCommonSubstringLengthArray("ab", "abc"));
    }

}
