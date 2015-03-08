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
 * Unit tests for {@link org.apache.commons.text.similarity.HammingDistance}.
 */
public class HammingDistanceTest {

    private static HammingDistance distance;

    @BeforeClass
    public static void setUp() {
        distance = new HammingDistance();
    }

    @Test
    public void testHammingDistance() {
        assertEquals(Integer.valueOf(0), distance.apply("", ""));
        assertEquals(Integer.valueOf(0), distance.apply("pappa", "pappa"));
        assertEquals(Integer.valueOf(1), distance.apply("papaa", "pappa"));
        assertEquals(Integer.valueOf(3), distance.apply("karolin", "kathrin"));
        assertEquals(Integer.valueOf(3), distance.apply("karolin", "kerstin"));
        assertEquals(Integer.valueOf(2), distance.apply("1011101", "1001001"));
        assertEquals(Integer.valueOf(3), distance.apply("2173896", "2233796"));
        assertEquals(Integer.valueOf(2), distance.apply("ATCG", "ACCC"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testHammingDistance_nullLeftValue() {
        distance.apply(null, "");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testHammingDistance_nullRightValue() {
        distance.apply("", null);
    }

}
