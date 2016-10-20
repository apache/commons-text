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

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Unit tests for {@link org.apache.commons.text.similarity.EditDistanceFrom}.
 *
 * @param <R> The {@link EditDistance} return type.
 */
@RunWith(Parameterized.class)
public class ParameterizedEditDistanceFromTest<R> {

    private final EditDistance<R> editDistance;
    private final CharSequence left;
    private final CharSequence right;
    private final R distance;

    public ParameterizedEditDistanceFromTest(
        final EditDistance<R> editDistance,
        final CharSequence left, final CharSequence right,
        final R distance) {

        this.editDistance = editDistance;
        this.left = left;
        this.right = right;
        this.distance = distance;
    }

    @Parameters
    public static Iterable<Object[]> parameters() {
        return Arrays.asList( new Object[][] {

            { new HammingDistance(), "Sam I am.", "Ham I am.", 1 },
            { new HammingDistance(), "Japtheth, Ham, Shem", "Japtheth, HAM, Shem", 2 },
            { new HammingDistance(), "Hamming", "Hamming", 0 },

            { new JaroWrinklerSimilarity(), "elephant", "hippo", 0.44 },
            { new JaroWrinklerSimilarity(), "hippo", "elephant",  0.44 },
            { new JaroWrinklerSimilarity(), "hippo", "zzzzzzzz", 0.0 },

            { new LevenshteinDistance(), "Apache", "a patchy", 4 },
            { new LevenshteinDistance(), "go", "no go", 3 },
            { new LevenshteinDistance(), "go", "go", 0 },

            { new LevenshteinDistance(4), "Apache", "a patchy", 4 },
            { new LevenshteinDistance(4), "go", "no go", 3 },
            { new LevenshteinDistance(0), "go", "go", 0 },

            {
                new EditDistance<Boolean>() {
                    public Boolean apply(CharSequence left, CharSequence right) {
                        return left == right || (left != null && left.equals(right));
                    }
                },
                "Bob's your uncle.",
                "Every good boy does fine.",
                false
            }

        } );
    }

    @Test
    public void test() {
        EditDistanceFrom<R> editDistanceFrom = new EditDistanceFrom<R>(editDistance, left);
        assertThat(editDistanceFrom.apply(right), equalTo(distance));
    }

}
