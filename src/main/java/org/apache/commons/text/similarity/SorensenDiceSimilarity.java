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

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

/**
 * A similarity algorithm indicating the percentage of matched characters
 * between two character sequences.
 *
 * <p>
 * The S&#248;rensen–Dice coefficient is a statistic used for comparing the
 * similarity of two samples. It was independently developed by the botanists
 * Thorvald S&#248;rensen and Lee Raymond Dice, who published in 1948 and 1945
 * respectively. The index is known by several other names, especially
 * S&#248;rensen–Dice index, S&#248;rensen index and Dice's coefficient. Other
 * variations include the "similarity coefficient" or "index", such as Dice
 * similarity coefficient (DSC).
 * </p>
 *
 * <p>
 * This implementation is based on the S&#248;rensen–Dice similarity algorithm
 * from <a href=
 * "http://en.wikipedia.org/wiki/S%C3%B8rensen%E2%80%93Dice_coefficient">
 * http://en.wikipedia.org/wiki/S%C3%B8rensen%E2%80%93Dice_coefficient</a>.
 *
 *
 * </p>
 *
 * @since 1.7
 */
public class SorensenDiceSimilarity implements SimilarityScore<Double> {

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

        if (left.length() < 2 || right.length() < 2) {
            return 0d;
        }

        Set<String> nLeft = createBigrams(left);
        Set<String> nRight = createBigrams(right);

        final int total = nLeft.size() + nRight.size();
        final long intersection = nLeft.stream().filter(nRight::contains).collect(Collectors.counting());

        return (2.0d * intersection) / total;
    }

    /**
     * Method for creating bigrams - two consecutive characters. Returns a set of
     * bigrams.
     *
     * @param charSequence The char sequence for which we need set of bigrams.
     * @return set of bigrams.
     */
    protected Set<String> createBigrams(CharSequence charSequence) {
        Set<String> set = new HashSet<String>();
        for (int i = 0; i < charSequence.length() - 1; i++) {
            char chr = charSequence.charAt(i);
            char nextChr = charSequence.charAt(i + 1);
            String bi = "" + chr + nextChr;
            set.add(bi);
        }
        return set;
    }
}
