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

/**
 * Measures the Jaccard distance of two sets of character sequence. Jaccard
 * distance is the dissimilarity between two sets. Its the complementary of
 * Jaccard similarity.
 *
 * <p>
 * For further explanation about Jaccard Distance, refer
 * https://en.wikipedia.org/wiki/Jaccard_index
 * </p>
 */
public class JaccardDistance implements EditDistance<Double> {

    private final JaccardSimilarity jaccardSimilarity = new JaccardSimilarity();

    /**
     * Calculates Jaccard distance of two set character sequence passed as
     * input. Calculates Jaccard similarity and returns the complement of it.
     * 
     * @param left first character sequence
     * @param right second character sequence
     * @return index
     * @throws IllegalArgumentException
     *             if either String input {@code null}
     */
    @Override
    public Double apply(CharSequence left, CharSequence right) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }
        return Math.round((1 - jaccardSimilarity.apply(left, right)) * 100d) / 100d;
    }
}
