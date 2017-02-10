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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Unit tests for {@link SimilarityScoreFrom}.
 *
 * @param <R> The {@link SimilarityScore} return type.
 */
@RunWith(Parameterized.class)
public class ParameterizedSimilarityScoreFromTest<R> {

    private final SimilarityScore<R> similarityScore;
    private final CharSequence left;
    private final CharSequence right;
    private final R distance;

    public ParameterizedSimilarityScoreFromTest(
            final SimilarityScore<R> similarityScore,
            final CharSequence left, final CharSequence right,
            final R distance) {

        this.similarityScore = similarityScore;
        this.left = left;
        this.right = right;
        this.distance = distance;
    }

    @Parameters
    public static Iterable<Object[]> parameters() {
        return Arrays.asList( new Object[][] {

                { new JaroWinklerDistance(), "elephant", "hippo", 0.44 },
                { new JaroWinklerDistance(), "hippo", "elephant",  0.44 },
                { new JaroWinklerDistance(), "hippo", "zzzzzzzz", 0.0 },

                {
                        new SimilarityScore<Boolean>() {
                            @Override
                            public Boolean apply(final CharSequence left, final CharSequence right) {
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
        final SimilarityScoreFrom<R> similarityScoreFrom = new SimilarityScoreFrom<>(similarityScore, left);
        assertThat(similarityScoreFrom.apply(right), equalTo(distance));
    }
}
