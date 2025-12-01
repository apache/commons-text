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
 * An algorithm for measuring the difference between two character sequences using the
 * <a href="https://en.wikipedia.org/wiki/Damerau%E2%80%93Levenshtein_distance">Damerau-Levenshtein Distance</a>.
 *
 * <p>
 * This is the number of changes needed to change one sequence into another, where each change is a single character
 * modification (deletion, insertion, substitution, or transposition of two adjacent characters).
 * </p>
 *
 * @see <a href="https://en.wikipedia.org/wiki/Damerau%E2%80%93Levenshtein_distance">Damerau-Levenshtein Distance on Wikipedia</a>
 * @since 1.15.0
 */
public class DamerauLevenshteinDistance implements EditDistance<Integer> {

    /**
     * Utility function to ensure distance is valid according to threshold.
     *
     * @param distance  The distance value
     * @param threshold The threshold value
     * @return The distance value, or {@code -1} if distance is greater than threshold
     */
    private static int clampDistance(final int distance, final int threshold) {
        return distance > threshold ? -1 : distance;
    }

    /**
     * Finds the Damerau-Levenshtein distance between two CharSequences if it's less than or equal to a given threshold.
     *
     * @param left      the first SimilarityInput, must not be null.
     * @param right     the second SimilarityInput, must not be null.
     * @param threshold the target threshold, must not be negative.
     * @return result distance, or -1 if distance exceeds threshold
     */
    private static <E> int limitedCompare(SimilarityInput<E> left, SimilarityInput<E> right, final int threshold) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Left/right inputs must not be null");
        }

        // Implementation based on https://en.wikipedia.org/wiki/Damerau%E2%80%93Levenshtein_distance#Optimal_string_alignment_distance

        int leftLength = left.length();
        int rightLength = right.length();

        if (leftLength == 0) {
            return clampDistance(rightLength, threshold);
        }

        if (rightLength == 0) {
            return clampDistance(leftLength, threshold);
        }

        // Inspired by LevenshteinDistance impl; swap the input strings to consume less memory
        if (rightLength > leftLength) {
            final SimilarityInput<E> tmp = left;
            left = right;
            right = tmp;
            leftLength = rightLength;
            rightLength = right.length();
        }

        // If the difference between the lengths of the strings is greater than the threshold, we must at least do
        // threshold operations so we can return early
        if (leftLength - rightLength > threshold) {
            return -1;
        }

        // Use three arrays of minimum possible size to reduce memory usage. This avoids having to create a 2D
        // array of size leftLength * rightLength
        int[] curr = new int[rightLength + 1];
        int[] prev = new int[rightLength + 1];
        int[] prevPrev = new int[rightLength + 1];
        int[] temp; // Temp variable use to shuffle arrays at the end of each iteration

        int rightIndex, leftIndex, cost, minCost;

        // Changing empty sequence to [0..i] requires i insertions
        for (rightIndex = 0; rightIndex <= rightLength; rightIndex++) {
            prev[rightIndex] = rightIndex;
        }

        // Calculate how many operations it takes to change right[0..rightIndex] into left[0..leftIndex]
        // For each iteration
        //  - curr[i] contains the cost of changing right[0..i] into left[0..leftIndex]
        //          (computed in current iteration)
        //  - prev[i] contains the cost of changing right[0..i] into left[0..leftIndex - 1]
        //          (computed in previous iteration)
        //  - prevPrev[i] contains the cost of changing right[0..i] into left[0..leftIndex - 2]
        //          (computed in iteration before previous)
        for (leftIndex = 1; leftIndex <= leftLength; leftIndex++) {
            // For right[0..0] we must insert leftIndex characters, which means the cost is always leftIndex
            curr[0] = leftIndex;

            minCost = Integer.MAX_VALUE;

            for (rightIndex = 1; rightIndex <= rightLength; rightIndex++) {
                cost = left.at(leftIndex - 1) == right.at(rightIndex - 1) ? 0 : 1;

                // Select cheapest operation
                curr[rightIndex] = Math.min(
                        Math.min(
                                prev[rightIndex] + 1, // Delete current character
                                curr[rightIndex - 1] + 1 // Insert current character
                        ),
                        prev[rightIndex - 1] + cost // Replace (or no cost if same character)
                );

                // Check if adjacent characters are the same -> transpose if cheaper
                if (leftIndex > 1
                        && rightIndex > 1
                        && left.at(leftIndex - 1) == right.at(rightIndex - 2)
                        && left.at(leftIndex - 2) == right.at(rightIndex - 1)) {
                    // Use cost here, to properly handle two subsequent equal letters
                    curr[rightIndex] = Math.min(curr[rightIndex], prevPrev[rightIndex - 2] + cost);
                }

                minCost = Math.min(curr[rightIndex], minCost);
            }

            // If there was no total cost for this entire iteration to transform right to left[0..leftIndex], there
            // can not be a way to do it below threshold. This is because we have no way to reduce the overall cost
            // in later operations.
            if (minCost > threshold) {
                return -1;
            }

            // Rotate arrays for next iteration
            temp = prevPrev;
            prevPrev = prev;
            prev = curr;
            curr = temp;
        }

        // Prev contains the value computed in the latest iteration
        return clampDistance(prev[rightLength], threshold);
    }

    /**
     * Finds the Damerau-Levenshtein distance between two inputs using optimal string alignment.
     *
     * @param left  the first CharSequence, must not be null.
     * @param right the second CharSequence, must not be null.
     * @return result distance.
     * @throws IllegalArgumentException if either CharSequence input is {@code null}.
     */
    private static <E> int unlimitedCompare(SimilarityInput<E> left, SimilarityInput<E> right) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Left/right inputs must not be null");
        }

        /*
         * Implementation based on https://en.wikipedia.org/wiki/Damerau%E2%80%93Levenshtein_distance#Optimal_string_alignment_distance
         */

        int leftLength = left.length();
        int rightLength = right.length();

        if (leftLength == 0) {
            return rightLength;
        }

        if (rightLength == 0) {
            return leftLength;
        }

        // Inspired by LevenshteinDistance impl; swap the input strings to consume less memory
        if (rightLength > leftLength) {
            final SimilarityInput<E> tmp = left;
            left = right;
            right = tmp;
            leftLength = rightLength;
            rightLength = right.length();
        }

        // Use three arrays of minimum possible size to reduce memory usage. This avoids having to create a 2D
        // array of size leftLength * rightLength
        int[] curr = new int[rightLength + 1];
        int[] prev = new int[rightLength + 1];
        int[] prevPrev = new int[rightLength + 1];
        int[] temp; // Temp variable use to shuffle arrays at the end of each iteration

        int rightIndex, leftIndex, cost;

        // Changing empty sequence to [0..i] requires i insertions
        for (rightIndex = 0; rightIndex <= rightLength; rightIndex++) {
            prev[rightIndex] = rightIndex;
        }

        // Calculate how many operations it takes to change right[0..rightIndex] into left[0..leftIndex]
        // For each iteration
        //  - curr[i] contains the cost of changing right[0..i] into left[0..leftIndex]
        //          (computed in current iteration)
        //  - prev[i] contains the cost of changing right[0..i] into left[0..leftIndex - 1]
        //          (computed in previous iteration)
        //  - prevPrev[i] contains the cost of changing right[0..i] into left[0..leftIndex - 2]
        //          (computed in iteration before previous)
        for (leftIndex = 1; leftIndex <= leftLength; leftIndex++) {
            // For right[0..0] we must insert leftIndex characters, which means the cost is always leftIndex
            curr[0] = leftIndex;

            for (rightIndex = 1; rightIndex <= rightLength; rightIndex++) {
                cost = left.at(leftIndex - 1) == right.at(rightIndex - 1) ? 0 : 1;

                // Select cheapest operation
                curr[rightIndex] = Math.min(
                        Math.min(
                                prev[rightIndex] + 1, // Delete current character
                                curr[rightIndex - 1] + 1 // Insert current character
                        ),
                        prev[rightIndex - 1] + cost // Replace (or no cost if same character)
                );

                // Check if adjacent characters are the same -> transpose if cheaper
                if (leftIndex > 1
                        && rightIndex > 1
                        && left.at(leftIndex - 1) == right.at(rightIndex - 2)
                        && left.at(leftIndex - 2) == right.at(rightIndex - 1)) {
                    // Use cost here, to properly handle two subsequent equal letters
                    curr[rightIndex] = Math.min(curr[rightIndex], prevPrev[rightIndex - 2] + cost);
                }
            }

            // Rotate arrays for next iteration
            temp = prevPrev;
            prevPrev = prev;
            prev = curr;
            curr = temp;
        }

        // Prev contains the value computed in the latest iteration
        return prev[rightLength];
    }

    /**
     * Threshold.
     */
    private final Integer threshold;

    /**
     * Constructs a default instance that uses a version of the algorithm that does not use a threshold parameter.
     */
    public DamerauLevenshteinDistance() {
        this(null);
    }

    /**
     * Constructs a new instance. If the threshold is not null, distance calculations will be limited to a maximum length.
     * If the threshold is null, the unlimited version of the algorithm will be used.
     *
     * @param threshold If this is null then distances calculations will not be limited. This may not be negative.
     */
    public DamerauLevenshteinDistance(final Integer threshold) {
        if (threshold != null && threshold < 0) {
            throw new IllegalArgumentException("Threshold must not be negative");
        }
        this.threshold = threshold;
    }

    /**
     * Computes the Damerau-Levenshtein distance between two Strings.
     *
     * <p>
     * A higher score indicates a greater distance.
     * </p>
     *
     * @param left  the first input, must not be null.
     * @param right the second input, must not be null.
     * @return result distance, or -1 if threshold is exceeded.
     * @throws IllegalArgumentException if either String input {@code null}.
     */
    @Override
    public Integer apply(final CharSequence left, final CharSequence right) {
        return apply(SimilarityInput.input(left), SimilarityInput.input(right));
    }

    /**
     * Computes the Damerau-Levenshtein distance between two inputs.
     *
     * <p>
     * A higher score indicates a greater distance.
     * </p>
     *
     * @param <E>   The type of similarity score unit.
     * @param left  the first input, must not be null.
     * @param right the second input, must not be null.
     * @return result distance, or -1 if threshold is exceeded.
     * @throws IllegalArgumentException if either String input {@code null}.
     * @since 1.13.0
     */
    public <E> Integer apply(final SimilarityInput<E> left, final SimilarityInput<E> right) {
        if (threshold != null) {
            return limitedCompare(left, right, threshold);
        }
        return unlimitedCompare(left, right);
    }

    /**
     * Gets the distance threshold.
     *
     * @return The distance threshold.
     */
    public Integer getThreshold() {
        return threshold;
    }
}
