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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link IntersectionResult}.
 */
class IntersectionResultTest {
    @Test
    void testEquals() {
        final IntersectionResult[] results = {
                new IntersectionResult(0, 0, 0),
                new IntersectionResult(10, 0, 0),
                new IntersectionResult(10, 10, 0),
                new IntersectionResult(10, 10, 10),
        };

        // Test a different instance with same values
        assertEquals(new IntersectionResult(0, 0, 0), results[0]);

        final Object something = new Object();
        for (int i = 0; i < results.length; i++) {
            assertNotEquals(results[i], something);
            assertNotEquals(null, results[i]);
            for (int j = 0; j < results.length; j++) {
                assertEquals(results[i].equals(results[j]), i == j);
            }

            assertFalse(results[i].equals(null), "Should not be Equal to null");
            assertNotEquals("Test", results[i], "Should not be Equal to a different type of object");
        }
    }

    @Test
    void testHashCode() {
        final IntersectionResult[] results = {
                new IntersectionResult(10, 0, 0),
                new IntersectionResult(10, 10, 0),
                new IntersectionResult(10, 10, 10),
        };
        final HashMap<IntersectionResult, Integer> map = new HashMap<>();
        final int offset = 123;
        for (int i = 0; i < results.length; i++) {
            map.put(results[i], i + offset);
        }
        for (int i = 0; i < results.length; i++) {
            assertEquals(i + offset, map.get(results[i]));
        }
    }

    @Test
    void testNewIntersectionResult_WithIntersectionAboveSizeAorB() {
        final int sizeA = 1;
        final int sizeB = 2;
        final int intersection = Math.max(sizeA, sizeB) + 1;
        assertThrows(IllegalArgumentException.class, () -> new IntersectionResult(sizeA, sizeB, intersection));
        assertThrows(IllegalArgumentException.class, () -> new IntersectionResult(sizeB, sizeA, intersection));
    }

    @Test
    void testNewIntersectionResult_WithNegativeIntersection() {
        final int sizeA = 0;
        final int sizeB = 0;
        final int intersection = -1;
        assertThrows(IllegalArgumentException.class, () -> new IntersectionResult(sizeA, sizeB, intersection));
    }

    @Test
    void testNewIntersectionResult_WithNegativeSizeA() {
        final int sizeA = -1;
        final int sizeB = 0;
        final int intersection = 0;
        assertThrows(IllegalArgumentException.class, () -> new IntersectionResult(sizeA, sizeB, intersection));
    }

    @Test
    void testNewIntersectionResult_WithNegativeSizeB() {
        final int sizeA = 0;
        final int sizeB = -1;
        final int intersection = 0;
        assertThrows(IllegalArgumentException.class, () -> new IntersectionResult(sizeA, sizeB, intersection));
    }

    @Test
    void testNewIntersectionResult_WithZeros() {
        final int sizeA = 0;
        final int sizeB = 0;
        final int intersection = 0;
        new IntersectionResult(sizeA, sizeB, intersection);
    }

    @Test
    void testProperties() {
        final ThreadLocalRandom rand = ThreadLocalRandom.current();
        final int max = 1024;
        for (int i = 0; i < 5; i++) {
            // Ensure the min is above 0
            final int sizeA = rand.nextInt(max) + 1;
            final int sizeB = rand.nextInt(max) + 1;
            final int intersection = rand.nextInt(Math.min(sizeA, sizeB));
            final IntersectionResult result = new IntersectionResult(sizeA, sizeB, intersection);
            assertEquals(sizeA, result.getSizeA());
            assertEquals(sizeB, result.getSizeB());
            assertEquals(intersection, result.getIntersection());
        }
    }

    @Test
    void testToString() {
        final ThreadLocalRandom rand = ThreadLocalRandom.current();
        final int max = 9;
        for (int i = 0; i < 5; i++) {
            // Ensure the min is above 0
            final int sizeA = rand.nextInt(max) + 1;
            final int sizeB = rand.nextInt(max) + 1;
            final int intersection = rand.nextInt(Math.min(sizeA, sizeB));
            final IntersectionResult result = new IntersectionResult(sizeA, sizeB, intersection);
            final String string = result.toString();
            // Not perfect as this will match substrings too. The chance of error
            // is limited by restricting the numbers to a max of 10.
            assertTrue(string.contains(String.valueOf(sizeA)));
            assertTrue(string.contains(String.valueOf(sizeB)));
            assertTrue(string.contains(String.valueOf(intersection)));
        }
    }
}
