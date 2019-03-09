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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Unit tests for {@link OverlapResult}.
 */
public class OverlapResultTest {
    @Test
    public void testNewOverlapResult_WithZeros() {
        final int sizeA = 0;
        final int sizeB = 0;
        final int intersection = 0;
        new OverlapResult(sizeA, sizeB, intersection);
    }

    @Test
    public void testNewOverlapResult_WithNegativeSizeA() {
        final int sizeA = -1;
        final int sizeB = 0;
        final int intersection = 0;
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new OverlapResult(sizeA, sizeB, intersection);
        });
    }

    @Test
    public void testNewOverlapResult_WithNegativeSizeB() {
        final int sizeA = 0;
        final int sizeB = -1;
        final int intersection = 0;
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new OverlapResult(sizeA, sizeB, intersection);
        });
    }

    @Test
    public void testNewOverlapResult_WithNegativeIntersection() {
        final int sizeA = 0;
        final int sizeB = 0;
        final int intersection = -1;
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new OverlapResult(sizeA, sizeB, intersection);
        });
    }

    @Test
    public void testNewOverlapResult_WithIntersectionAboveSizeAorB() {
        final int sizeA = 1;
        final int sizeB = 2;
        final int intersection = Math.max(sizeA, sizeB) + 1;
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new OverlapResult(sizeA, sizeB, intersection);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new OverlapResult(sizeB, sizeA, intersection);
        });
    }

    @Test
    public void testUnion() {
        // Union is the combined size minus the intersection
        Assertions.assertEquals(0, getUnion(0, 0, 0));

        Assertions.assertEquals(1, getUnion(1, 0, 0));
        Assertions.assertEquals(2, getUnion(1, 1, 0));
        Assertions.assertEquals(1, getUnion(1, 1, 1));

        Assertions.assertEquals(2, getUnion(2, 0, 0));
        Assertions.assertEquals(3, getUnion(2, 1, 0));
        Assertions.assertEquals(2, getUnion(2, 1, 1));
        Assertions.assertEquals(2, getUnion(2, 2, 2));

        // Test overflow of int addition
        Assertions.assertEquals((long) Integer.MAX_VALUE + 1, getUnion(Integer.MAX_VALUE, 1, 0));
        Assertions.assertEquals((long) Integer.MAX_VALUE + 1,
                getUnion(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE - 1));
        Assertions.assertEquals((long) Integer.MAX_VALUE + Integer.MAX_VALUE,
                getUnion(Integer.MAX_VALUE, Integer.MAX_VALUE, 0));
    }

    private static long getUnion(int sizeA, int sizeB, int intersection) {
        return new OverlapResult(sizeA, sizeB, intersection).getUnion();
    }

    @Test
    public void testProperties() {
        final ThreadLocalRandom rand = ThreadLocalRandom.current();
        final int max = 1024;
        for (int i = 0; i < 5; i++) {
            // Ensure the min is above 0
            final int sizeA = rand.nextInt(max) + 1;
            final int sizeB = rand.nextInt(max) + 1;
            final int intersection = rand.nextInt(Math.min(sizeA, sizeB));
            final OverlapResult result = new OverlapResult(sizeA, sizeB, intersection);
            Assertions.assertEquals(sizeA, result.getSizeA());
            Assertions.assertEquals(sizeB, result.getSizeB());
            Assertions.assertEquals(intersection, result.getIntersection());
        }
    }

    @Test
    public void testEquals() {
        final OverlapResult[] results = new OverlapResult[] {
                new OverlapResult(0, 0, 0),
                new OverlapResult(10, 0, 0),
                new OverlapResult(10, 10, 0),
                new OverlapResult(10, 10, 10),
        };

        // Test difference instance with same values
        Assertions.assertTrue(results[0].equals(new OverlapResult(0, 0, 0)));

        final Object something = new Object();
        for (int i = 0; i < results.length; i++) {
            Assertions.assertFalse(results[i].equals(something));
            Assertions.assertFalse(results[i].equals(null));
            for (int j = 0; j < results.length; j++) {
                Assertions.assertTrue(results[i].equals(results[j]) == (i == j));
            }
        }
    }

    @Test
    public void testHashCode() {
        final OverlapResult[] results = new OverlapResult[] {
                new OverlapResult(10, 0, 0),
                new OverlapResult(10, 10, 0),
                new OverlapResult(10, 10, 10),
        };
        final HashMap<OverlapResult, Integer> map = new HashMap<>();
        final int offset = 123;
        for (int i = 0; i < results.length; i++) {
            map.put(results[i], i + offset);
        }
        for (int i = 0; i < results.length; i++) {
            Assertions.assertEquals(i + offset, map.get(results[i]));
        }
    }

    @Test
    public void testToString() {
        final ThreadLocalRandom rand = ThreadLocalRandom.current();
        final int max = 9;
        for (int i = 0; i < 5; i++) {
            // Ensure the min is above 0
            final int sizeA = rand.nextInt(max) + 1;
            final int sizeB = rand.nextInt(max) + 1;
            final int intersection = rand.nextInt(Math.min(sizeA, sizeB));
            final OverlapResult result = new OverlapResult(sizeA, sizeB, intersection);
            final String string = result.toString();
            // Not perfect as this will match substrings too. The chance of error
            // is limited by restricting the numbers to a max of 10.
            Assertions.assertTrue(string.contains(String.valueOf(sizeA)));
            Assertions.assertTrue(string.contains(String.valueOf(sizeB)));
            Assertions.assertTrue(string.contains(String.valueOf(intersection)));
        }
    }
}
