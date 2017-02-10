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

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link LongestCommonSubsequence}.
 */
public class LongestCommonSubsequenceTest {

    private static LongestCommonSubsequence subject;

    @BeforeClass
    public static void setup() {
        subject = new LongestCommonSubsequence();
    }

    @Test
    public void testLongestCommonSubsequenceApply() {
        assertEquals(Integer.valueOf(0), subject.apply("", ""));
        assertEquals(Integer.valueOf(0), subject.apply("left", ""));
        assertEquals(Integer.valueOf(0), subject.apply("", "right"));
        assertEquals(Integer.valueOf(3), subject.apply("frog", "fog"));
        assertEquals(Integer.valueOf(0), subject.apply("fly", "ant"));
        assertEquals(Integer.valueOf(1), subject.apply("elephant", "hippo"));
        assertEquals(Integer.valueOf(8), subject.apply("ABC Corporation", "ABC Corp"));
        assertEquals(Integer.valueOf(20), subject.apply("D N H Enterprises Inc", "D & H Enterprises, Inc."));
        assertEquals(Integer.valueOf(24), subject.apply("My Gym Children's Fitness Center", "My Gym. Childrens Fitness"));
        assertEquals(Integer.valueOf(11), subject.apply("PENNSYLVANIA", "PENNCISYLVNIA"));
        assertEquals(Integer.valueOf(1), subject.apply("left", "right"));
        assertEquals(Integer.valueOf(4), subject.apply("leettteft", "ritttght"));
        assertEquals(Integer.valueOf(15), subject.apply("the same string", "the same string"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGettingLongestCommonSubsequenceApplyNullNull() throws Exception {
        subject.apply(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGettingLongestCommonSubsequenceApplyStringNull() throws Exception {
        subject.apply(" ", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGettingLongestCommonSubsequenceApplyNullString() throws Exception {
        subject.apply(null, "right");
    }

    @Test
    public void testLongestCommonSubsequence() {
        assertEquals("", subject.logestCommonSubsequence("", ""));
        assertEquals("", subject.logestCommonSubsequence("left", ""));
        assertEquals("", subject.logestCommonSubsequence("", "right"));
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

    @Test(expected = IllegalArgumentException.class)
    public void testGettingLongestCommonSubsequenceNullNull() throws Exception {
        subject.logestCommonSubsequence(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGettingLongestCommonSubsequenceStringNull() throws Exception {
        subject.logestCommonSubsequence(" ", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGettingLongestCommonSubsequenceNullString() throws Exception {
        subject.logestCommonSubsequence(null, "right");
    }
}
