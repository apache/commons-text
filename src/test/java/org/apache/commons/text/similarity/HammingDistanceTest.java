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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@link HammingDistance}.
 */
public class HammingDistanceTest {

    private static HammingDistance distance;

    @BeforeAll
    public static void setUp() {
        distance = new HammingDistance();
    }

    @ParameterizedTest
    @MethodSource("org.apache.commons.text.similarity.SimilarityInputTest#similarityInputsEquals()")
    void testHammingDistance(final Class<?> cls) {
        assertEquals(0, distance.apply(SimilarityInputTest.build(cls, ""), SimilarityInputTest.build(cls, "")));
        assertEquals(0, distance.apply(SimilarityInputTest.build(cls, "pappa"), SimilarityInputTest.build(cls, "pappa")));
        assertEquals(1, distance.apply(SimilarityInputTest.build(cls, "papaa"), SimilarityInputTest.build(cls, "pappa")));
        assertEquals(3, distance.apply(SimilarityInputTest.build(cls, "karolin"), SimilarityInputTest.build(cls, "kathrin")));
        assertEquals(3, distance.apply(SimilarityInputTest.build(cls, "karolin"), SimilarityInputTest.build(cls, "kerstin")));
        assertEquals(2, distance.apply(SimilarityInputTest.build(cls, "1011101"), SimilarityInputTest.build(cls, "1001001")));
        assertEquals(3, distance.apply(SimilarityInputTest.build(cls, "2173896"), SimilarityInputTest.build(cls, "2233796")));
        assertEquals(2, distance.apply(SimilarityInputTest.build(cls, "ATCG"), SimilarityInputTest.build(cls, "ACCC")));
    }

    @Test
    void testHammingDistance_nullLeftValue() {
        assertThrows(IllegalArgumentException.class, () -> distance.apply(null, ""));
    }

    @Test
    void testHammingDistance_nullRightValue() {
        assertThrows(IllegalArgumentException.class, () -> distance.apply("", null));
    }

    @Test
    void testHammingDistanceCharSequence() {
        assertEquals(0, distance.apply("", ""));
        assertEquals(0, distance.apply("pappa", "pappa"));
        assertEquals(1, distance.apply("papaa", "pappa"));
        assertEquals(3, distance.apply("karolin", "kathrin"));
        assertEquals(3, distance.apply("karolin", "kerstin"));
        assertEquals(2, distance.apply("1011101", "1001001"));
        assertEquals(3, distance.apply("2173896", "2233796"));
        assertEquals(2, distance.apply("ATCG", "ACCC"));
    }

}
