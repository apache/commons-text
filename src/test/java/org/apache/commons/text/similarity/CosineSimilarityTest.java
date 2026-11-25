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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CosineSimilarityTest {

    private static Map<CharSequence, Integer> vector(final int size, final int seed) {
        final Map<CharSequence, Integer> map = new HashMap<>();
        for (int i = 0; i < size; i++) {
            map.put("t" + (i + seed), 1 + (i * 7 + seed) % 5);
        }
        return map;
    }

    private static Map<CharSequence, Integer> negate(final Map<CharSequence, Integer> vector) {
        final Map<CharSequence, Integer> map = new HashMap<>();
        vector.forEach((key, value) -> map.put(key, -value));
        return map;
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 6, 12 })
    void testCosineSimilarityOfIdenticalVectorsIsOne(final int size) {
        final Map<CharSequence, Integer> map = new HashMap<>();
        for (int i = 0; i < size; i++) {
            map.put("t" + i, 1);
        }
        assertEquals(1.0, CosineSimilarity.INSTANCE.cosineSimilarity(map, new HashMap<>(map)));
    }

    /**
     * Tests that non-negative vectors, such as the token counts built by {@link CosineDistance}, have a similarity within [0.0, 1.0].
     */
    @Test
    void testCosineSimilarityOfNonNegativeVectorsIsInRange() {
        for (int leftSize = 1; leftSize <= 20; leftSize++) {
            for (int rightSize = 1; rightSize <= 20; rightSize++) {
                for (int seed = 0; seed < 5; seed++) {
                    final Map<CharSequence, Integer> left = vector(leftSize, seed);
                    final double identical = CosineSimilarity.INSTANCE.cosineSimilarity(left, new HashMap<>(left));
                    final double overlapping = CosineSimilarity.INSTANCE.cosineSimilarity(left, vector(rightSize, seed + 1));
                    assertTrue(identical >= 0.0 && identical <= 1.0, () -> "identical: " + identical + " for " + left);
                    assertTrue(overlapping >= 0.0 && overlapping <= 1.0, () -> "overlapping: " + overlapping + " for " + left);
                }
            }
        }
    }

    /**
     * Tests that signed vectors keep negative similarities, within [-1.0, 1.0].
     */
    @Test
    void testCosineSimilarityOfSignedVectors() {
        final Map<CharSequence, Integer> positive = new HashMap<>();
        positive.put("x", 1);
        assertEquals(-1.0, CosineSimilarity.INSTANCE.cosineSimilarity(positive, negate(positive)));
        for (int leftSize = 1; leftSize <= 20; leftSize++) {
            for (int rightSize = 1; rightSize <= 20; rightSize++) {
                for (int seed = 0; seed < 5; seed++) {
                    final Map<CharSequence, Integer> left = vector(leftSize, seed);
                    final Map<CharSequence, Integer> right = negate(vector(rightSize, seed + 1));
                    assertEquals(-1.0, CosineSimilarity.INSTANCE.cosineSimilarity(left, negate(left)), () -> "negated: " + left);
                    final double opposite = CosineSimilarity.INSTANCE.cosineSimilarity(left, right);
                    assertTrue(opposite >= -1.0 && opposite <= 0.0, () -> "opposite: " + opposite + " for " + left + "/" + right);
                }
            }
        }
    }

    @Test
    void testCosineSimilarityReturningDoubleWhereByteValueIsZero() {
        final Map<CharSequence, Integer> hashMap = new HashMap<>();
        assertEquals(0.0, CosineSimilarity.INSTANCE.cosineSimilarity(hashMap, hashMap), 0.01);
    }

    @Test
    void testCosineSimilarityThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> CosineSimilarity.INSTANCE.cosineSimilarity(new HashMap<>(), null));
    }

    @Test
    void testCosineSimilarityWithNonEmptyMap() {
        final Map<CharSequence, Integer> hashMap = new HashMap<>();
        final Integer integer = -397;
        hashMap.put("3J/$3.L", integer);
        final Map<CharSequence, Integer> hashMapTwo = new HashMap<>();
        assertEquals(0.0, CosineSimilarity.INSTANCE.cosineSimilarity(hashMap, hashMapTwo), 0.01);
    }

    @Test
    void testCosineSimilarityWithNull() {
        assertThrows(IllegalArgumentException.class, () -> CosineSimilarity.INSTANCE.cosineSimilarity(null, null));
    }

}
