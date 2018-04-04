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

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for {@link SimilarityScoreFrom}.
 *
 * @param <R> The {@link SimilarityScore} return type.
 */
public class ParameterizedSimilarityScoreFromTest<R> {

    public static Stream<Arguments> parameters() {
        return Stream.of(
                Arguments.of(new LevenshteinDistance(), "elephant", "hippo", 7),
                Arguments.of(new LevenshteinDistance(), "hippo", "elephant", 7),
                Arguments.of(new LevenshteinDistance(), "hippo", "zzzzzzzz", 8),

                Arguments.of(
                        new SimilarityScore<Boolean>() {
                            @Override
                            public Boolean apply(final CharSequence left, final CharSequence right) {
                                return left == right || (left != null && left.equals(right));
                            }
                        },
                        "Bob's your uncle.",
                        "Every good boy does fine.",
                        false
                ));
    }

    @ParameterizedTest
    @MethodSource("parameters")
    public void test(SimilarityScore<R> similarityScore, CharSequence left, CharSequence right, R distance) {
        final SimilarityScoreFrom<R> similarityScoreFrom = new SimilarityScoreFrom<>(similarityScore, left);
        assertThat(similarityScoreFrom.apply(right)).isEqualTo(distance);
    }
}
