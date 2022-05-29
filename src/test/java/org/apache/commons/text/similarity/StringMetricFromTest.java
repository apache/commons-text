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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link EditDistanceFrom}.
 */
public class StringMetricFromTest {

    @Test
    public void testEquivalence() {
        final EditDistance<Integer> metric = new LevenshteinDistance();
        final String left = "Apache";
        final String right = "a patchy";
        final Integer distance = 4;
        final EditDistanceFrom<Integer> metricFrom = new EditDistanceFrom<>(metric, left);

        assertThat(metricFrom.apply(right)).isEqualTo(distance);
        assertThat(metricFrom.apply(right)).isEqualTo(metric.apply(left, right));
    }

    @Test
    public void testJavadocExample() {
        final EditDistance<Integer> metric = new LevenshteinDistance();
        final String target = "Apache";
        final EditDistanceFrom<Integer> metricFrom =
            new EditDistanceFrom<>(metric, target);
        String mostSimilar = null;
        Integer shortestDistance = null;

        for (final String test : new String[] {"Appaloosa", "a patchy", "apple" }) {
            final Integer distance = metricFrom.apply(test);
            if (shortestDistance == null || distance < shortestDistance) {
                shortestDistance = distance;
                mostSimilar = test;
            }
        }
        assertThat(mostSimilar).isEqualTo("a patchy");
        assertThat(shortestDistance).isEqualTo(4);
    }

    @Test
    public void testMissingMetric() {
        assertThatIllegalArgumentException().isThrownBy(() -> new EditDistanceFrom<Number>(null, "no go"));
    }

}
