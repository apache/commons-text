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
 * The hamming distance between two strings of equal length is the number of positions at which the corresponding symbols are different.
 *
 * <p>
 * For further explanation about the Hamming Distance, take a look at its Wikipedia page at https://en.wikipedia.org/wiki/Hamming_distance.
 * </p>
 *
 * @since 1.0
 */
public class HammingDistance implements EditDistance<Integer> {

    /**
     * Computes the Hamming Distance between two strings with the same length.
     *
     * <p>
     * The distance starts with zero, and for each occurrence of a different character in either String, it increments the distance by 1, and finally return its
     * value.
     * </p>
     *
     * <p>
     * Since the Hamming Distance can only be calculated between strings of equal length, input of different lengths will throw IllegalArgumentException
     * </p>
     *
     * <pre>
     * distance.apply("", "")               = 0
     * distance.apply("pappa", "pappa")     = 0
     * distance.apply("1011101", "1011111") = 1
     * distance.apply("ATCG", "ACCC")       = 2
     * distance.apply("karolin", "kerstin"  = 3
     * </pre>
     *
     * @param left  the first input, must not be null.
     * @param right the second input, must not be null.
     * @return distance.
     * @throws IllegalArgumentException if either input is {@code null} or if they do not have the same length.
     */
    @Override
    public Integer apply(final CharSequence left, final CharSequence right) {
        return apply(SimilarityInput.input(left), SimilarityInput.input(right));
    }

    /**
     * Computes the Hamming Distance between two strings with the same length.
     *
     * <p>
     * The distance starts with zero, and for each occurrence of a different character in either String, it increments the distance by 1, and finally return its
     * value.
     * </p>
     * <p>
     * Since the Hamming Distance can only be calculated between strings of equal length, input of different lengths will throw IllegalArgumentException
     * </p>
     *
     * <pre>
     * distance.apply("", "")               = 0
     * distance.apply("pappa", "pappa")     = 0
     * distance.apply("1011101", "1011111") = 1
     * distance.apply("ATCG", "ACCC")       = 2
     * distance.apply("karolin", "kerstin"  = 3
     * </pre>
     *
     * @param <E> The type of similarity score unit.
     * @param left  the first input, must not be null.
     * @param right the second input, must not be null.
     * @return distance.
     * @throws IllegalArgumentException if either input is {@code null} or if they do not have the same length.
     * @since 1.13.0
     */
    public <E> Integer apply(final SimilarityInput<E> left, final SimilarityInput<E> right) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("SimilarityInput must not be null");
        }
        if (left.length() != right.length()) {
            throw new IllegalArgumentException("SimilarityInput must have the same length");
        }
        int distance = 0;
        for (int i = 0; i < left.length(); i++) {
            if (!left.at(i).equals(right.at(i))) {
                distance++;
            }
        }
        return distance;
    }

}
