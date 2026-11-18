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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests {@link CosineSimilarity}.
 */
class CosineDistanceTest {

    /**
     * Cosine distance under test.
     */
    private static CosineDistance cosineDistance;

    /**
     * Creates the cosine distance object used throughout the tests.
     */
    @BeforeAll
    public static void setUp() {
        cosineDistance = new CosineDistance();
    }

    /**
     * Rounds up a value.
     *
     * @param value A value
     * @return rounded up value
     */
    private Double roundValue(final Double value) {
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * Tests the cosine distance with several inputs.
     */
    @Test
    void testCosineDistance() {
        assertEquals(Double.valueOf(0.5d), roundValue(cosineDistance.apply("the house", "da house")));
        assertEquals(Double.valueOf(0.0d), roundValue(cosineDistance.apply("AB", "AB")));
        assertEquals(Double.valueOf(1.0d), roundValue(cosineDistance.apply("AB", "BA")));
        assertEquals(Double.valueOf(0.08d), roundValue(cosineDistance.apply(
                "the boy was from tamana shi, kumamoto ken, and the girl was from rio de janeiro, rio",
                "the boy was from tamana shi, kumamoto, and the boy was from rio de janeiro, rio de janeiro")));
    }

    /**
     * Tests that the cosine distance stays within [0.0, 1.0].
     */
    @Test
    void testCosineDistanceIsInRange() {
        final String[] words = { "a", "b", "c", "d", "e", "f", "g" };
        for (int leftSize = 1; leftSize <= 30; leftSize++) {
            for (int shift = 0; shift < words.length; shift++) {
                final StringBuilder left = new StringBuilder();
                final StringBuilder right = new StringBuilder();
                for (int i = 0; i < leftSize; i++) {
                    left.append(words[i % words.length]).append(' ');
                    right.append(words[(i * 3 + shift) % words.length]).append(' ');
                }
                final double identical = cosineDistance.apply(left, left.toString());
                final double other = cosineDistance.apply(left, right);
                assertTrue(identical >= 0.0 && identical <= 1.0, () -> "identical: " + identical + " for " + left);
                assertTrue(other >= 0.0 && other <= 1.0, () -> "other: " + other + " for " + left + "/" + right);
            }
        }
    }

    /**
     * Tests that identical inputs have a cosine distance of exactly 0.0.
     *
     * @param input the input compared to itself.
     */
    @ParameterizedTest
    @ValueSource(strings = { "a", "a b", "a b c", "a b c d", "a b c d e f", "a b c d e f g h i j k l" })
    void testCosineDistanceOfIdenticalInputsIsZero(final String input) {
        assertEquals(0.0, cosineDistance.apply(input, input));
    }

}
