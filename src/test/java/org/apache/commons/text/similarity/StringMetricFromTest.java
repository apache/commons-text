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

import org.junit.jupiter.api.Test;

/**
 * Tests {@link EditDistanceFrom}.
 */
class StringMetricFromTest {

    @Test
    void testEquivalence() {
        final EditDistance<Integer> metric = LevenshteinDistance.getDefaultInstance();
        final String left = "Apache";
        final String right = "a patchy";
        final Integer distance = 4;
        final EditDistanceFrom<Integer> metricFrom = new EditDistanceFrom<>(metric, left);

        assertEquals(distance, metricFrom.apply(right));
        assertEquals(metric.apply(left, right), metricFrom.apply(right));
    }

    @Test
    void testJavadocExample() {
        final EditDistance<Integer> metric = LevenshteinDistance.getDefaultInstance();
        final String target = "Apache";
        final EditDistanceFrom<Integer> metricFrom = new EditDistanceFrom<>(metric, target);
        String mostSimilar = null;
        Integer shortestDistance = null;

        for (final String test : new String[] { "Appaloosa", "a patchy", "apple" }) {
            final Integer distance = metricFrom.apply(test);
            if (shortestDistance == null || distance < shortestDistance) {
                shortestDistance = distance;
                mostSimilar = test;
            }
        }
        assertEquals("a patchy", mostSimilar);
        assertEquals(4, shortestDistance);
    }

    @Test
    void testMissingMetric() {
        assertThrows(IllegalArgumentException.class, () -> new EditDistanceFrom<Number>(null, "no go"));
    }

}
