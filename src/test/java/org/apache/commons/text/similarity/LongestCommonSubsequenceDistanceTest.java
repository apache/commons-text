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
 * Unit tests for {@link LongestCommonSubsequenceDistance}.
 */
public class LongestCommonSubsequenceDistanceTest {

    private static LongestCommonSubsequenceDistance subject;

    @BeforeClass
    public static void setup() {
        subject = new LongestCommonSubsequenceDistance();
    }

    @Test
    public void testGettingLogestCommonSubsequenceDistacne() {
        assertEquals(Integer.valueOf(0), subject.apply("", ""));
        assertEquals(Integer.valueOf(4), subject.apply("left", ""));
        assertEquals(Integer.valueOf(5), subject.apply("", "right"));
        assertEquals(Integer.valueOf(1), subject.apply("frog", "fog"));
        assertEquals(Integer.valueOf(6), subject.apply("fly", "ant"));
        assertEquals(Integer.valueOf(11), subject.apply("elephant", "hippo"));
        assertEquals(Integer.valueOf(7), subject.apply("ABC Corporation", "ABC Corp"));
        assertEquals(Integer.valueOf(4), subject.apply("D N H Enterprises Inc", "D & H Enterprises, Inc."));
        assertEquals(Integer.valueOf(9),
                subject.apply("My Gym Children's Fitness Center", "My Gym. Childrens Fitness"));
        assertEquals(Integer.valueOf(3), subject.apply("PENNSYLVANIA", "PENNCISYLVNIA"));
        assertEquals(Integer.valueOf(7), subject.apply("left", "right"));
        assertEquals(Integer.valueOf(9), subject.apply("leettteft", "ritttght"));
        assertEquals(Integer.valueOf(0), subject.apply("the same string", "the same string"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGettingLongestCommonSubsequenceDistanceNullNull() throws Exception {
        subject.apply(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGettingLongestCommonSubsequenceDistanceStringNull() throws Exception {
        subject.apply(" ", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGettingLongestCommonSubsequenceDistanceNullString() throws Exception {
        subject.apply(null, "right");
    }

}
