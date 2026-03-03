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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link SorensenDiceSimilarity}.
 */
public class SorensenDiceSimilarityTest {

    private static SorensenDiceSimilarity similarity;

    @BeforeAll
    public static void setUp() {
        similarity = new SorensenDiceSimilarity();
    }

    @Test
    public void test() {
        assertEquals(0.25d, similarity.apply("night", "nacht"));
    }

    @Test
    public void testGetSorensenDiceSimilarity_StringString() {
        assertEquals(1d, similarity.apply("", ""));
        assertEquals(0d, similarity.apply("", "a"));
        assertEquals(0d, similarity.apply("a", ""));
        assertEquals(1d, similarity.apply("a", "a"));
        assertEquals(0d, similarity.apply("a", "b"));
        assertEquals(1.0d, similarity.apply("foo", "foo"));
        assertEquals(0.8d, similarity.apply("foo", "foo "));
        assertEquals(0.8d, similarity.apply("foo", " foo"));
        assertEquals(0.66d, printTwoDecimals(similarity.apply("foo", " foo ")));
        assertEquals(0.4d, similarity.apply("frog", "fog"));
        assertEquals(0.0d, similarity.apply("fly", "ant"));
        assertEquals(0.0d, similarity.apply("elephant", "hippo"));
        assertEquals(0.0d, similarity.apply("hippo", "elephant"));
        assertEquals(0.0d, similarity.apply("hippo", "zzzzzzzz"));
        assertEquals(0.25d, similarity.apply("night", "nacht"));
        assertEquals(0.5d, similarity.apply("hello", "hallo"));
        assertEquals(0.0d, similarity.apply("aaapppp", ""));
        assertEquals(0.66d, printTwoDecimals(similarity.apply("ABC Corporation", "ABC Corp")));
        assertEquals(0.73d, printTwoDecimals(similarity.apply("D N H Enterprises Inc", "D &amp; H Enterprises, Inc.")));
        assertEquals(0.76d, printTwoDecimals(similarity.apply("My Gym Children's Fitness Center", "My Gym. Childrens Fitness")));
        assertEquals(0.69d, printTwoDecimals(similarity.apply("PENNSYLVANIA", "PENNCISYLVNIA")));
        assertEquals(0.92d, printTwoDecimals(similarity.apply("/opt/software1", "/opt/software2")));
        assertEquals(0.60d, printTwoDecimals(similarity.apply("aaabcd", "aaacdb")));
        assertEquals(0.63d, printTwoDecimals(similarity.apply("John Horn", "John Hopkins")));
    }

    @Test
    public void testGetSorensenDicesSimilarity_NullNull() {
        assertThrows(IllegalArgumentException.class, () -> similarity.apply(null, null));
    }

    @Test
    public void testGetSorensenDicesSimilarity_StringNull() {
        assertThrows(IllegalArgumentException.class, () -> similarity.apply(" ", null));
    }

    @Test
    public void testGetSorensenDicesSimilarity_NullString() {
        assertThrows(IllegalArgumentException.class, () -> similarity.apply(null, "clear"));
    }

    /**
     * Format the double to two decimal places rounding it down.
     *
     * @param value the double value
     * @return double formatted to two places rounding it down
     */
    public static double printTwoDecimals(double value) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.DOWN);
        return Double.parseDouble(df.format(value));
    }
}
