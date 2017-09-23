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

/**
 * Measures the Jaccard similarity (aka Jaccard index) of two sets of character
 * sequence. Jaccard similarity is the size of the intersection divided by the
 * size of the union of the two sets.
 *
 * <p>
 * For further explanation about Jaccard Similarity, refer
 * https://en.wikipedia.org/wiki/Jaccard_index
 * </p>
 *
 * @since 1.0
 */
public class JaccardSimilarity implements SimilarityScore<Double> {

    /**
     * Calculates Jaccard Similarity of two set character sequence passed as
     * input.
     *
     * @param left first character sequence
     * @param right second character sequence
     * @return index
     * @throws IllegalArgumentException
     *             if either String input {@code null}
     */
    @Override
    public Double apply(final CharSequence left, final CharSequence right) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }
        return Math.round(calculateJaccardSimilarity(left, right) * 100d) / 100d;
    }

    /**
     * Calculates Jaccard Similarity of two character sequences passed as
     * input. Does the calculation by identifying the union (characters in at
     * least one of the two sets) of the two sets and intersection (characters
     * which are present in set one which are present in set two)
     *
     * @param left first character sequence
     * @param right second character sequence
     * @return index
     */
    private Double calculateJaccardSimilarity(final CharSequence left, final CharSequence right) {
        final Set<String> intersectionSet = new HashSet<>();
        final Set<String> unionSet = new HashSet<>();
        boolean unionFilled = false;
        final int leftLength = left.length();
        final int rightLength = right.length();
        if (leftLength == 0 || rightLength == 0) {
            return 0d;
        }

        for (int leftIndex = 0; leftIndex < leftLength; leftIndex++) {
            unionSet.add(String.valueOf(left.charAt(leftIndex)));
            for (int rightIndex = 0; rightIndex < rightLength; rightIndex++) {
                if (!unionFilled) {
                    unionSet.add(String.valueOf(right.charAt(rightIndex)));
                }
                if (left.charAt(leftIndex) == right.charAt(rightIndex)) {
                    intersectionSet.add(String.valueOf(left.charAt(leftIndex)));
                }
            }
            unionFilled = true;
        }
        return Double.valueOf(intersectionSet.size()) / Double.valueOf(unionSet.size());
    }
}
