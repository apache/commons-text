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
    public void testGettingJaccardSimilarity() {
        // Match the functionality of the JaccardSimilarity class
        final Function<CharSequence, Set<Character>> converter = (cs) -> {
            final Set<Character> set = new HashSet<>();
            for (int i = 0; i < cs.length(); i++) {
                set.add(cs.charAt(i));
            }
            return set;
        };
        final IntersectionSimilarity<Character> similarity = new IntersectionSimilarity<>(converter);

        assertEquals(0.00d, round(similarity.apply("", "").getJaccard()), 0.00000000000000000001d);
        assertEquals(0.00d, round(similarity.apply("left", "").getJaccard()), 0.00000000000000000001d);
        assertEquals(0.00d, round(similarity.apply("", "right").getJaccard()), 0.00000000000000000001d);
        assertEquals(0.75d, round(similarity.apply("frog", "fog").getJaccard()), 0.00000000000000000001d);
        assertEquals(0.00d, round(similarity.apply("fly", "ant").getJaccard()), 0.00000000000000000001d);
        assertEquals(0.22d, round(similarity.apply("elephant", "hippo").getJaccard()), 0.00000000000000000001d);
        assertEquals(0.64d, round(similarity.apply("ABC Corporation", "ABC Corp").getJaccard()),
                0.00000000000000000001d);
        assertEquals(0.76d, round(similarity.apply("D N H Enterprises Inc", "D & H Enterprises, Inc.").getJaccard()),
                0.00000000000000000001d);
        assertEquals(0.89d,
                round(similarity.apply("My Gym Children's Fitness Center", "My Gym. Childrens Fitness").getJaccard()),
                0.00000000000000000001d);
        assertEquals(0.9d, round(similarity.apply("PENNSYLVANIA", "PENNCISYLVNIA").getJaccard()),
                0.00000000000000000001d);
        assertEquals(0.13d, round(similarity.apply("left", "right").getJaccard()), 0.00000000000000000001d);
        assertEquals(0.13d, round(similarity.apply("leettteft", "ritttght").getJaccard()), 0.00000000000000000001d);
        assertEquals(1.0d, round(similarity.apply("the same string", "the same string").getJaccard()),
                0.00000000000000000001d);
    }

    private static double round(double value) {
        // For some undocumented reason the JaccardSimilarity rounds to 2 D.P.
        return Math.round(value * 100d) / 100d;
    }

    @Test
    public void testGettingF1ScoreUsingBigrams() {
        // Compute the F1-score using pairs of characters (bigrams)
        final Function<CharSequence, Set<String>> converter = (cs) -> {
            final Set<String> set = new HashSet<>();
            final int length = cs.length();
            if (length > 1) {
                final char[] bigram = new char[2];
                bigram[1] = cs.charAt(0);
                for (int i = 1; i < cs.length(); i++) {
                    bigram[0] = bigram[1];
                    bigram[1] = cs.charAt(i);
                    set.add(new String(bigram));
                }
            }
            return set;
        };
        final IntersectionSimilarity<String> similarity = new IntersectionSimilarity<>(converter);

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
    public void testGettingSetSimilarityNullNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            new IntersectionSimilarity<>((cs) -> new HashSet<>(Arrays.asList(cs))).apply(null, null);
        });
    }

    @Test
    public void testGettingSetSimilarityStringNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            new IntersectionSimilarity<>((cs) -> new HashSet<>(Arrays.asList(cs))).apply("left", null);
        });
    }

    @Test
    public void testGettingSetSimilarityNullString() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            new IntersectionSimilarity<>((cs) -> new HashSet<>(Arrays.asList(cs))).apply(null, "right");
        });
    }
}
