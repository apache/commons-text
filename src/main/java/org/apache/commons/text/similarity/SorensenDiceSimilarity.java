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

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * A similarity algorithm indicating the percentage of matched characters
 * between two character sequences.
 *
 * <p>The Sørensen-Dice coefficient is a statistic used for comparing the
 * similarity of two samples. It was independently developed by the botanists
 * Thorvald Sørensen and Lee Raymond Dice, who published in 1948 and 1945
 * respectively. The index is known by several other names, especially
 * Sørensen-Dice index, Sørensen index and Dice's coefficient. Other
 * variations include the "similarity coefficient" or "index", such as Dice
 * similarity coefficient (DSC).</p>
 *
 * <p>This implementation is based on the Sørensen-Dice similarity algorithm
 * from <a href=
 * "https://en.wikipedia.org/wiki/Dice-S%C3%B8rensen_coefficient">
 * https://en.wikipedia.org/wiki/Dice-S%C3%B8rensen_coefficient</a>.</p>
 *
 * @since 1.13
 */
public class SorensenDiceSimilarity implements SimilarityScore<Double> {

    /**
     * For shifting bigrams to fit in single integer.
     */
    private static final int SHIFT_NUMBER = 16;

    /**
     * Converter function for conversion of string to bigrams.
     */
    private final Function<CharSequence, Collection<Integer>> converter = new SorensenDiceConverter();

    /**
     * Measures the overlap of two sets created from a pair of character sequences.
     * {@link IntersectionSimilarity}}
     */
    private final IntersectionSimilarity<Integer> similarity = new IntersectionSimilarity<>(this.converter);

    /**
     * Calculates Sorensen-Dice Similarity of two character sequences passed as
     * input.
     *
     * <pre>
     * similarity.apply(null, null)                 = IllegalArgumentException
     * similarity.apply("foo", null)                = IllegalArgumentException
     * similarity.apply(null, "foo")                = IllegalArgumentException
     * similarity.apply("night", "nacht")           = 0.25
     * similarity.apply("", "")                     = 1.0
     * similarity.apply("foo", "foo")               = 1.0
     * similarity.apply("foo", "foo ")              = 0.8
     * similarity.apply("foo", "foo ")              = 0.66
     * similarity.apply("foo", " foo ")             = 0.66
     * similarity.apply("foo", " foo")              = 0.66
     * similarity.apply("", "a")                    = 0.0
     * similarity.apply("aaapppp", "")              = 0.0
     * similarity.apply("frog", "fog")              = 0.4
     * similarity.apply("fly", "ant")               = 0.0
     * similarity.apply("elephant", "hippo")        = 0.0
     * similarity.apply("hippo", "elephant")        = 0.0
     * similarity.apply("hippo", "zzzzzzzz")        = 0.0
     * similarity.apply("hello", "hallo")           = 0.5
     * similarity.apply("ABC Corporation", "ABC Corp") = 0.7
     * similarity.apply("D N H Enterprises Inc", "D &amp; H Enterprises, Inc.") = 0.74
     * similarity.apply("My Gym Children's Fitness Center", "My Gym. Childrens Fitness") = 0.81
     * similarity.apply("PENNSYLVANIA", "PENNCISYLVNIA") = 0.69
     * </pre>
     *
     * @param left  the first CharSequence, must not be null
     * @param right the second CharSequence, must not be null
     * @return result similarity
     * @throws IllegalArgumentException if either CharSequence input is {@code null}
     */
    @Override
    public Double apply(final CharSequence left, final CharSequence right) {

        if (left == null || right == null) {
            throw new IllegalArgumentException("CharSequences must not be null");
        }

        if (StringUtils.equals(left, right)) {
            return 1d;
        }

        // if bigram is not formed out of any given string, clearly both are not similar.
        if (left.length() < 2 || right.length() < 2) {
            return 0d;
        }

        IntersectionResult overlap = similarity.apply(left, right);

        final int total = overlap.getSizeA() + overlap.getSizeB();
        final long intersection = overlap.getIntersection();

        return (2.0d * intersection) / total;
    }

    /**
     * Converter class for creating Bigrams for SorensenDice similarity.
     */
    private static class SorensenDiceConverter implements Function<CharSequence, Collection<Integer>> {
        @Override
        public Collection<Integer> apply(CharSequence cs) {
            final int length = cs.length();
            final List<Integer> list = new ArrayList<>(length);
            if (length > 1) {
                char ch2 = cs.charAt(0);
                for (int i = 1; i < length; i++) {
                    final char ch1 = ch2;
                    ch2 = cs.charAt(i);
                    list.add((ch1 << SHIFT_NUMBER) | ch2);
                }
            }
            return list;
        }
    }
}
