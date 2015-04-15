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

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.text.similarity.EditDistanceFrom}.
 */
public class StringMetricFromTest {

    @Test
    public void testEquivalence() {
        EditDistance<Integer> metric = new LevenshteinDistance();
        String left = "Apache";
        String right = "a patchy";
        Integer distance = 4;
        EditDistanceFrom<Integer> metricFrom = new EditDistanceFrom<Integer>(metric, left);

        assertThat(metricFrom.apply(right), equalTo(distance));
        assertThat(metricFrom.apply(right), equalTo(metric.apply(left, right)));
    }

    @Test
    public void testJavadocExample() {
        EditDistance<Integer> metric = new LevenshteinDistance();
        String target = "Apache";
        EditDistanceFrom<Integer> metricFrom =
            new EditDistanceFrom<Integer>(metric, target);
        String mostSimilar = null;
        Integer shortestDistance = null;
        
        for (String test : new String[] { "Appaloosa", "a patchy", "apple" }) {
            Integer distance = metricFrom.apply(test);
            if (shortestDistance == null || distance < shortestDistance) {
                shortestDistance = distance;
                mostSimilar = test;
            }
        }
       
        System.out.println("The string most similar to \"" + target + "\" "
            + "is \"" + mostSimilar + "\" because "
            + "its distance is only " + shortestDistance + ".");

        assertThat(mostSimilar, equalTo("a patchy"));
        assertThat(shortestDistance, equalTo(4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingMetric() {
        new EditDistanceFrom<Number>(null, "no go");
    }

}
