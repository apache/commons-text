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

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link SorensenDicesSimilarity}.
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
    public void testGetSorensenDicesSimilarity_StringString() {

        assertEquals(1d, similarity.apply("", ""));
        assertEquals(0d, similarity.apply("", "a"));
        assertEquals(0d, similarity.apply("a", ""));
        assertEquals(1d, similarity.apply("a", "a"));
        assertEquals(0d, similarity.apply("a", "b"));
        assertEquals(1.0d, similarity.apply("foo", "foo"));
        assertEquals(0.8d, similarity.apply("foo", "foo "));
        assertEquals(0.4d, similarity.apply("frog", "fog"));
        assertEquals(0.0d, similarity.apply("fly", "ant"));
        assertEquals(0.0d, similarity.apply("elephant", "hippo"));
        assertEquals(0.0d, similarity.apply("hippo", "elephant"));
        assertEquals(0.0d, similarity.apply("hippo", "zzzzzzzz"));
        assertEquals(0.5d, similarity.apply("hello", "hallo"));
        assertEquals(0.7d, similarity.apply("ABC Corporation", "ABC Corp"));
        assertEquals(0.7391304347826086d, similarity.apply("D N H Enterprises Inc", "D &amp; H Enterprises, Inc."));
        assertEquals(0.8076923076923077d,
                similarity.apply("My Gym Children's Fitness Center", "My Gym. Childrens Fitness"));
        assertEquals(0.6956521739130435, similarity.apply("PENNSYLVANIA", "PENNCISYLVNIA"));
        assertEquals(0.9230769230769231, similarity.apply("/opt/software1", "/opt/software2"));
        assertEquals(0.5d, similarity.apply("aaabcd", "aaacdb"));
        assertEquals(0.631578947368421, similarity.apply("John Horn", "John Hopkins"));

    }

    @Test
    public void testGetSorensenDicesSimilarity_NullNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            similarity.apply(null, null);
        });
    }

    @Test
    public void testGetSorensenDicesSimilarity_StringNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            similarity.apply(" ", null);
        });
    }

    @Test
    public void testGetSorensenDicesSimilarity_NullString() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            similarity.apply(null, "clear");
        });
    }
}
