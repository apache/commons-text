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

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

/**
 * A similarity algorithm indicating the percentage of matched characters between two character sequences.
 *
 * <p>
 * The Jaro measure is the weighted sum of percentage of matched characters
 * from each file and transposed characters. Winkler increased this measure
 * for matching initial characters.
 * </p>
 *
 * <p>
 * This implementation is based on the Jaro Winkler similarity algorithm
 * from <a href="http://en.wikipedia.org/wiki/Jaro%E2%80%93Winkler_distance">
 * http://en.wikipedia.org/wiki/Jaro%E2%80%93Winkler_distance</a>.
 * </p>
 *
 * <p>
 * This code has been adapted from Apache Commons Lang 3.3.
 * </p>
 *
 * @since 1.7
 */
public class JaroWinklerSimilarity implements SimilarityScore<Double> {

    /**
     * Singleton instance.
     */
    static final JaroWinklerSimilarity INSTANCE = new JaroWinklerSimilarity();

    /**
     * This method returns the Jaro-Winkler string matches, half transpositions, prefix array.
     *
     * @param first the first string to be matched
     * @param second the second string to be matched
     * @return mtp array containing: matches, half transpositions, and prefix
     */
    protected static int[] matches(final CharSequence first, final CharSequence second) {
        final CharSequence max;
        final CharSequence min;
        if (first.length() > second.length()) {
            max = first;
            min = second;
        } else {
            max = second;
            min = first;
        }
        final int range = Math.max(max.length() / 2 - 1, 0);
        final int[] matchIndexes = new int[min.length()];
        Arrays.fill(matchIndexes, -1);
        final boolean[] matchFlags = new boolean[max.length()];
        int matches = 0;
        for (int mi = 0; mi < min.length(); mi++) {
            final char c1 = min.charAt(mi);
            for (int xi = Math.max(mi - range, 0), xn = Math.min(mi + range + 1, max.length()); xi < xn; xi++) {
                if (!matchFlags[xi] && c1 == max.charAt(xi)) {
                    matchIndexes[mi] = xi;
                    matchFlags[xi] = true;
                    matches++;
                    break;
                }
            }
        }
        final char[] ms1 = new char[matches];
        final char[] ms2 = new char[matches];
        for (int i = 0, si = 0; i < min.length(); i++) {
            if (matchIndexes[i] != -1) {
                ms1[si] = min.charAt(i);
                si++;
            }
        }
        for (int i = 0, si = 0; i < max.length(); i++) {
            if (matchFlags[i]) {
                ms2[si] = max.charAt(i);
                si++;
            }
        }
        int halfTranspositions = 0;
        for (int mi = 0; mi < ms1.length; mi++) {
            if (ms1[mi] != ms2[mi]) {
                halfTranspositions++;
            }
        }
        int prefix = 0;
        for (int mi = 0; mi < Math.min(4, min.length()); mi++) {
            if (first.charAt(mi) != second.charAt(mi)) {
                break;
            }
            prefix++;
        }
        return new int[] {matches, halfTranspositions, prefix};
    }

    /**
     * Computes the Jaro Winkler Similarity between two character sequences.
     *
     * <pre>
     * sim.apply(null, null)          = IllegalArgumentException
     * sim.apply("foo", null)         = IllegalArgumentException
     * sim.apply(null, "foo")         = IllegalArgumentException
     * sim.apply("", "")              = 1.0
     * sim.apply("foo", "foo")        = 1.0
     * sim.apply("foo", "foo ")       = 0.94
     * sim.apply("foo", "foo  ")      = 0.91
     * sim.apply("foo", " foo ")      = 0.87
     * sim.apply("foo", "  foo")      = 0.51
     * sim.apply("", "a")             = 0.0
     * sim.apply("aaapppp", "")       = 0.0
     * sim.apply("frog", "fog")       = 0.93
     * sim.apply("fly", "ant")        = 0.0
     * sim.apply("elephant", "hippo") = 0.44
     * sim.apply("hippo", "elephant") = 0.44
     * sim.apply("hippo", "zzzzzzzz") = 0.0
     * sim.apply("hello", "hallo")    = 0.88
     * sim.apply("ABC Corporation", "ABC Corp") = 0.91
     * sim.apply("D N H Enterprises Inc", "D &amp; H Enterprises, Inc.") = 0.95
     * sim.apply("My Gym Children's Fitness Center", "My Gym. Childrens Fitness") = 0.92
     * sim.apply("PENNSYLVANIA", "PENNCISYLVNIA") = 0.88
     * </pre>
     *
     * @param left the first CharSequence, must not be null
     * @param right the second CharSequence, must not be null
     * @return result similarity
     * @throws IllegalArgumentException if either CharSequence input is {@code null}
     */
    @Override
    public Double apply(final CharSequence left, final CharSequence right) {
        final double defaultScalingFactor = 0.1;

        if (left == null || right == null) {
            throw new IllegalArgumentException("CharSequences must not be null");
        }

        if (StringUtils.equals(left, right)) {
            return 1d;
        }

        final int[] mtp = matches(left, right);
        final double m = mtp[0];
        if (m == 0) {
            return 0d;
        }
        final double j = (m / left.length() + m / right.length() + (m - (double) mtp[1] / 2) / m) / 3;
        return j < 0.7d ? j : j + defaultScalingFactor * mtp[2] * (1d - j);
    }

}
