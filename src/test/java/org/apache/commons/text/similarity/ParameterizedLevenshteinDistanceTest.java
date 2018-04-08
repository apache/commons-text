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
 * Unit tests for {@link LevenshteinDistance}.
 */
public class ParameterizedLevenshteinDistanceTest {

    public static Stream<Arguments> parameters() {
        return Stream.of(
            /* empty strings */
            Arguments.of(0, "", "", 0),
            Arguments.of(8, "aaapppp", "", 7),
            Arguments.of(7, "aaapppp", "", 7),
            Arguments.of(6, "aaapppp", "", -1),

            /* unequal strings, zero threshold */
            Arguments.of(0, "b", "a", -1),
            Arguments.of(0, "a", "b", -1),

            /* equal strings */
            Arguments.of(0, "aa", "aa", 0),
            Arguments.of(2, "aa", "aa", 0),

            /* same length */
            Arguments.of(2, "aaa", "bbb", -1),
            Arguments.of(3, "aaa", "bbb", 3),

            /* big stripe */
            Arguments.of(10, "aaaaaa", "b", 6),

            /* distance less than threshold */
            Arguments.of(8, "aaapppp", "b", 7),
            Arguments.of(4, "a", "bbb", 3),

            /* distance equal to threshold */
            Arguments.of(7, "aaapppp", "b", 7),
            Arguments.of(3, "a", "bbb", 3),

            /* distance greater than threshold */
            Arguments.of(2, "a", "bbb", -1),
            Arguments.of(2, "bbb", "a", -1),
            Arguments.of(6, "aaapppp", "b", -1),

            /* stripe runs off array, strings not similar */
            Arguments.of(1, "a", "bbb", -1),
            Arguments.of(1, "bbb", "a", -1),

            /* stripe runs off array, strings are similar */
            Arguments.of(1, "12345", "1234567", -1),
            Arguments.of(1, "1234567", "12345", -1),

            /* old getLevenshteinDistance test cases */
            Arguments.of(1, "frog", "fog", 1),
            Arguments.of(3, "fly", "ant", 3),
            Arguments.of(7, "elephant", "hippo", 7),
            Arguments.of(6, "elephant", "hippo", -1),
            Arguments.of(7, "hippo", "elephant", 7),
            Arguments.of(6, "hippo", "elephant", -1),
            Arguments.of(8, "hippo", "zzzzzzzz", 8),
            Arguments.of(8, "zzzzzzzz", "hippo", 8),
            Arguments.of(1, "hello", "hallo", 1),

            Arguments.of(Integer.MAX_VALUE, "frog", "fog", 1),
            Arguments.of(Integer.MAX_VALUE, "fly", "ant", 3),
            Arguments.of(Integer.MAX_VALUE, "elephant", "hippo", 7),
            Arguments.of(Integer.MAX_VALUE, "hippo", "elephant", 7),
            Arguments.of(Integer.MAX_VALUE, "hippo", "zzzzzzzz", 8),
            Arguments.of(Integer.MAX_VALUE, "zzzzzzzz", "hippo", 8),
            Arguments.of(Integer.MAX_VALUE, "hello", "hallo", 1));
    }

    @ParameterizedTest
    @MethodSource("parameters")
    public void test(Integer threshold, CharSequence left, CharSequence right, Integer distance) {
        final LevenshteinDistance metric = new LevenshteinDistance(threshold);
        assertThat(metric.apply(left, right)).isEqualTo(distance);
    }

}
