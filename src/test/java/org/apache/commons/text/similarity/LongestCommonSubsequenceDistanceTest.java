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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link LongestCommonSubsequenceDistance}.
 */
public class LongestCommonSubsequenceDistanceTest {

    private static LongestCommonSubsequenceDistance subject;

    @BeforeAll
    public static void setup() {
        subject = new LongestCommonSubsequenceDistance();
    }

    @Test
    public void testGettingLongestCommonSubsequenceDistance() {
        assertEquals(0, subject.apply("", ""));
        assertEquals(4, subject.apply("left", ""));
        assertEquals(5, subject.apply("", "right"));
        assertEquals(1, subject.apply("frog", "fog"));
        assertEquals(6, subject.apply("fly", "ant"));
        assertEquals(11, subject.apply("elephant", "hippo"));
        assertEquals(7, subject.apply("ABC Corporation", "ABC Corp"));
        assertEquals(4, subject.apply("D N H Enterprises Inc", "D & H Enterprises, Inc."));
        assertEquals(9, subject.apply("My Gym Children's Fitness Center", "My Gym. Childrens Fitness"));
        assertEquals(3, subject.apply("PENNSYLVANIA", "PENNCISYLVNIA"));
        assertEquals(7, subject.apply("left", "right"));
        assertEquals(9, subject.apply("leettteft", "ritttght"));
        assertEquals(0, subject.apply("the same string", "the same string"));
    }

    @Test
    public void testGettingLongestCommonSubsequenceDistanceNullNull() {
        assertThrows(IllegalArgumentException.class, () -> subject.apply(null, null));
    }

    @Test
    public void testGettingLongestCommonSubsequenceDistanceNullString() {
        assertThrows(IllegalArgumentException.class, () -> subject.apply(null, "right"));
    }

    @Test
    public void testGettingLongestCommonSubsequenceDistanceStringNull() {
        assertThrows(IllegalArgumentException.class, () -> subject.apply(" ", null));
    }

}
