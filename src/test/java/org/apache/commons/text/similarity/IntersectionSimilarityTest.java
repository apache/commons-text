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

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * Unit tests for {@link IntersectionSimilarity}.
 */
public class IntersectionSimilarityTest {
    @Test
    public void testGetJaccardSimilarityUsingChars() {
        // Match the functionality of the JaccardSimilarity class by dividing
        // the sequence into single characters
        final Function<CharSequence, Set<Character>> converter = (cs) -> {
            final int length = cs.length();
            final Set<Character> set = new HashSet<>(length);
            for (int i = 0; i < length; i++) {
                set.add(cs.charAt(i));
            }
            return set;
        };
        final IntersectionSimilarity<Character> similarity = new IntersectionSimilarity<>(converter);

        // This is explicitly implemented instead of using the JaccardSimilarity
        // since that class does rounding to 2 D.P.
        // Results generated using the python distance library using: 1 - distance.jaccard(seq1, seq2)
        assertEquals(0.0, similarity.apply("", "").getJaccard());
        assertEquals(0.0, similarity.apply("left", "").getJaccard());
        assertEquals(0.0, similarity.apply("", "right").getJaccard());
        assertEquals(0.75, similarity.apply("frog", "fog").getJaccard());
        assertEquals(0.0, similarity.apply("fly", "ant").getJaccard());
        assertEquals(0.2222222222222222, similarity.apply("elephant", "hippo").getJaccard());
        assertEquals(0.6363636363636364, similarity.apply("ABC Corporation", "ABC Corp").getJaccard());
        assertEquals(0.7647058823529411,
                similarity.apply("D N H Enterprises Inc", "D & H Enterprises, Inc.").getJaccard());
        assertEquals(0.8888888888888888,
                similarity.apply("My Gym Children's Fitness Center", "My Gym. Childrens Fitness").getJaccard());
        assertEquals(0.9, similarity.apply("PENNSYLVANIA", "PENNCISYLVNIA").getJaccard());
        assertEquals(0.125, similarity.apply("left", "right").getJaccard());
        assertEquals(0.125, similarity.apply("leettteft", "ritttght").getJaccard());
        assertEquals(1.0, similarity.apply("the same string", "the same string").getJaccard());
    }

    @Test
    public void testGetF1ScoreUsingBigrams() {
        // Compute the F1-score using pairs of characters (bigrams).
        // This can be done using a 32-bit int to store two 16-bit characters
        final Function<CharSequence, Set<Integer>> converter = (cs) -> {
            final int length = cs.length();
            final Set<Integer> set = new HashSet<>(length);
            if (length > 1) {
                char ch2 = cs.charAt(0);
                for (int i = 1; i < length; i++) {
                    final char ch1 = ch2;
                    ch2 = cs.charAt(i);
                    set.add(Integer.valueOf((ch1 << 16) | ch2));
                }
            }
            return set;
        };
        final IntersectionSimilarity<Integer> similarity = new IntersectionSimilarity<>(converter);

        // Note that when there are no bigrams then the similarity is zero.

        assertEquals(0d, similarity.apply("", "").getF1Score());
        assertEquals(0d, similarity.apply("", "a").getF1Score());
        assertEquals(0d, similarity.apply("a", "").getF1Score());
        assertEquals(0d, similarity.apply("a", "a").getF1Score());
        assertEquals(0d, similarity.apply("a", "b").getF1Score());
        assertEquals(1.0d, similarity.apply("foo", "foo").getF1Score());
        assertEquals(0.8d, similarity.apply("foo", "foo ").getF1Score());
        assertEquals(0.4d, similarity.apply("frog", "fog").getF1Score());
        assertEquals(0.0d, similarity.apply("fly", "ant").getF1Score());
        assertEquals(0.0d, similarity.apply("elephant", "hippo").getF1Score());
        assertEquals(0.0d, similarity.apply("hippo", "elephant").getF1Score());
        assertEquals(0.0d, similarity.apply("hippo", "zzzzzzzz").getF1Score());
        assertEquals(0.5d, similarity.apply("hello", "hallo").getF1Score());
        assertEquals(0.7d, similarity.apply("ABC Corporation", "ABC Corp").getF1Score());
        assertEquals(0.7391304347826086d,
                similarity.apply("D N H Enterprises Inc", "D &amp; H Enterprises, Inc.").getF1Score());
        assertEquals(0.8076923076923077d,
                similarity.apply("My Gym Children's Fitness Center", "My Gym. Childrens Fitness").getF1Score());
        assertEquals(0.6956521739130435, similarity.apply("PENNSYLVANIA", "PENNCISYLVNIA").getF1Score());
        assertEquals(0.9230769230769231, similarity.apply("/opt/software1", "/opt/software2").getF1Score());
        assertEquals(0.5d, similarity.apply("aaabcd", "aaacdb").getF1Score());
        assertEquals(0.631578947368421, similarity.apply("John Horn", "John Hopkins").getF1Score());
    }

    @Test
    public void testConstructorWithNullConverterThrows() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            new IntersectionSimilarity<>(null);
        });
    }

    @Test
    public void testApplyNullNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            new IntersectionSimilarity<>((cs) -> new HashSet<>(Arrays.asList(cs))).apply(null, null);
        });
    }

    @Test
    public void testApplyStringNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            new IntersectionSimilarity<>((cs) -> new HashSet<>(Arrays.asList(cs))).apply("left", null);
        });
    }

    @Test
    public void testApplyNullString() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            new IntersectionSimilarity<>((cs) -> new HashSet<>(Arrays.asList(cs))).apply(null, "right");
        });
    }
}
