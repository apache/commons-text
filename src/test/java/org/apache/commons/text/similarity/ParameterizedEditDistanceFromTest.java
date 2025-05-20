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

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@link EditDistanceFrom}.
 *
 * @param <R> The {@link EditDistance} return type.
 */
public class ParameterizedEditDistanceFromTest<R> {

    public static Stream<Arguments> parameters() {
        return Stream.of(
                Arguments.of(new HammingDistance(), "Sam I am.", "Ham I am.", 1),
                Arguments.of(new HammingDistance(), "Japtheth, Ham, Shem", "Japtheth, HAM, Shem", 2),
                Arguments.of(new HammingDistance(), "Hamming", "Hamming", 0),

                Arguments.of(LevenshteinDistance.getDefaultInstance(), "Apache", "a patchy", 4),
                Arguments.of(LevenshteinDistance.getDefaultInstance(), "go", "no go", 3),
                Arguments.of(LevenshteinDistance.getDefaultInstance(), "go", "go", 0),

                Arguments.of(new LevenshteinDistance(4), "Apache", "a patchy", 4),
                Arguments.of(new LevenshteinDistance(4), "go", "no go", 3),
                Arguments.of(new LevenshteinDistance(0), "go", "go", 0),

                Arguments.of(
                    (EditDistance<Boolean>) (left, right) -> left == right || left != null && left.equals(right),
                    "Bob's your uncle.",
                    "Every good boy does fine.",
                    false));
    }

    @ParameterizedTest
    @MethodSource("parameters")
    public void test(final EditDistance<R> editDistance, final CharSequence left, final CharSequence right, final R distance) {
        final EditDistanceFrom<R> editDistanceFrom = new EditDistanceFrom<>(editDistance, left);
        assertEquals(distance, editDistanceFrom.apply(right));
    }

}
