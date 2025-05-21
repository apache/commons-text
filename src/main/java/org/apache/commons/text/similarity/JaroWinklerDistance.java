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

/**
 * Measures the Jaro-Winkler distance of two character sequences.
 * It is the complementary of Jaro-Winkler similarity.
 *
 * @since 1.0
 */
public class JaroWinklerDistance implements EditDistance<Double> {

    /**
     * @deprecated Deprecated as of 1.7. This constant will be removed in 2.0.
     */
    @Deprecated
    public static final int INDEX_NOT_FOUND = -1;

    /**
     * Computes the Jaro-Winkler string matches, half transpositions, prefix array.
     *
     * @param first the first string to be matched.
     * @param second the second string to be matched.
     * @return array containing: matches, half transpositions, and prefix
     * @deprecated Deprecated as of 1.7. This method will be removed in 2.0, and moved to a Jaro Winkler similarity
     *             class. TODO see TEXT-104.
     */
    @Deprecated
    protected static int[] matches(final CharSequence first, final CharSequence second) {
        return JaroWinklerSimilarity.matches(first, second);
    }

    /**
     * Creates a new instance.
     */
    public JaroWinklerDistance() {
        // empty
    }

    /**
     * Computes the Jaro Winkler Distance between two character sequences.
     *
     * <pre>
     * distance.apply(null, null)          = IllegalArgumentException
     * distance.apply("foo", null)         = IllegalArgumentException
     * distance.apply(null, "foo")         = IllegalArgumentException
     * distance.apply("", "")              = 0.0
     * distance.apply("foo", "foo")        = 0.0
     * distance.apply("foo", "foo ")       = 0.06
     * distance.apply("foo", "foo  ")      = 0.09
     * distance.apply("foo", " foo ")      = 0.13
     * distance.apply("foo", "  foo")      = 0.49
     * distance.apply("", "a")             = 1.0
     * distance.apply("aaapppp", "")       = 1.0
     * distance.apply("frog", "fog")       = 0.07
     * distance.apply("fly", "ant")        = 1.0
     * distance.apply("elephant", "hippo") = 0.56
     * distance.apply("hippo", "elephant") = 0.56
     * distance.apply("hippo", "zzzzzzzz") = 1.0
     * distance.apply("hello", "hallo")    = 0.12
     * distance.apply("ABC Corporation", "ABC Corp") = 0.09
     * distance.apply("D N H Enterprises Inc", "D &amp; H Enterprises, Inc.") = 0.05
     * distance.apply("My Gym Children's Fitness Center", "My Gym. Childrens Fitness") = 0.08
     * distance.apply("PENNSYLVANIA", "PENNCISYLVNIA") = 0.12
     * </pre>
     *
     * @param left the first input, must not be null.
     * @param right the second input, must not be null.
     * @return result distance.
     * @throws IllegalArgumentException if either CharSequence input is {@code null}
     */
    @Override
    public Double apply(final CharSequence left, final CharSequence right) {
        return apply(SimilarityInput.input(left), SimilarityInput.input(right));
    }

    /**
     * Computes the Jaro Winkler Distance between two character sequences.
     *
     * <pre>
     * distance.apply(null, null)          = IllegalArgumentException
     * distance.apply("foo", null)         = IllegalArgumentException
     * distance.apply(null, "foo")         = IllegalArgumentException
     * distance.apply("", "")              = 0.0
     * distance.apply("foo", "foo")        = 0.0
     * distance.apply("foo", "foo ")       = 0.06
     * distance.apply("foo", "foo  ")      = 0.09
     * distance.apply("foo", " foo ")      = 0.13
     * distance.apply("foo", "  foo")      = 0.49
     * distance.apply("", "a")             = 1.0
     * distance.apply("aaapppp", "")       = 1.0
     * distance.apply("frog", "fog")       = 0.07
     * distance.apply("fly", "ant")        = 1.0
     * distance.apply("elephant", "hippo") = 0.56
     * distance.apply("hippo", "elephant") = 0.56
     * distance.apply("hippo", "zzzzzzzz") = 1.0
     * distance.apply("hello", "hallo")    = 0.12
     * distance.apply("ABC Corporation", "ABC Corp") = 0.09
     * distance.apply("D N H Enterprises Inc", "D &amp; H Enterprises, Inc.") = 0.05
     * distance.apply("My Gym Children's Fitness Center", "My Gym. Childrens Fitness") = 0.08
     * distance.apply("PENNSYLVANIA", "PENNCISYLVNIA") = 0.12
     * </pre>
     *
     * @param <E> The type of similarity score unit.
     * @param left the first input, must not be null.
     * @param right the second input, must not be null.
     * @return result distance.
     * @throws IllegalArgumentException if either CharSequence input is {@code null}.
     * @since 1.13.0
     */
    public <E> Double apply(final SimilarityInput<E> left, final SimilarityInput<E> right) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("CharSequences must not be null");
        }
        return 1 - JaroWinklerSimilarity.INSTANCE.apply(left, right);
    }
}
