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

import java.util.Arrays;

/**
 * An algorithm for measuring the difference between two character sequences using the
 * <a href="https://en.wikipedia.org/wiki/Levenshtein_distance">Levenshtein Distance</a>.
 *
 * <p>
 * This is the number of changes needed to change one sequence into another, where each change is a
 * single character modification (deletion, insertion or substitution).
 * </p>
 *
 * <p>
 * This implementation supports configurable costs for insertion, deletion, and substitution
 * operations. By default, all costs are set to 1 for backward compatibility.
 * </p>
 *
 * <p>
 * Use {@link Builder} to construct instances with custom thresholds and operation costs:
 * </p>
 *
 * <pre>
 * LevenshteinDistance dist = LevenshteinDistance.builder()
 *     .setThreshold(10)
 *     .setInsertCost(1)
 *     .setDeleteCost(2)
 *     .setReplaceCost(3)
 *     .build();
 * </pre>
 *
 * <p>
 * This code has been adapted from Apache Commons Lang 3.3.
 * </p>
 *
 * @since 1.0
 * @see <a href="https://en.wikipedia.org/wiki/Levenshtein_distance">Levenshtein Distance on Wikipedia</a>
 * @see <a href="https://xlinux.nist.gov/dads/HTML/Levenshtein.html">Levenshtein Distance on NIST</a>
 */
public class LevenshteinDistance implements EditDistance<Integer> {

    /**
     * Builds {@link LevenshteinDistance} instances.
     *
     * <p>
     * All costs default to 1. The threshold defaults to {@code null} (unlimited).
     * </p>
     *
     * <pre>
     * LevenshteinDistance dist = LevenshteinDistance.builder()
     *     .setThreshold(5)
     *     .setInsertCost(1)
     *     .setDeleteCost(1)
     *     .setReplaceCost(2)
     *     .build();
     * </pre>
     *
     * @since 1.16.0
     */
    public static final class Builder {

        /**
         * Default cost for any single edit operation.
         */
        private static final int DEFAULT_COST = 1;

        /** Threshold for limited compare, or {@code null} for unlimited. */
        private Integer threshold;

        /** Cost of inserting a character. */
        private int insertCost = DEFAULT_COST;

        /** Cost of deleting a character. */
        private int deleteCost = DEFAULT_COST;

        /** Cost of substituting one character for another. */
        private int replaceCost = DEFAULT_COST;

        /**
         * Constructs a new builder with default values.
         */
        private Builder() {
            // use LevenshteinDistance.builder() factory method
        }

        /**
         * Builds a new {@link LevenshteinDistance} from the current state of this builder.
         *
         * @return a new {@link LevenshteinDistance}.
         * @throws IllegalArgumentException if the threshold is negative, or any cost is negative.
         */
        public LevenshteinDistance build() {
            return new LevenshteinDistance(this);
        }

        /**
         * Sets the cost of a deletion operation.
         *
         * @param deleteCost the cost of deleting a character; must not be negative.
         * @return {@code this} builder.
         */
        public Builder setDeleteCost(final int deleteCost) {
            this.deleteCost = deleteCost;
            return this;
        }

        /**
         * Sets the cost of an insertion operation.
         *
         * @param insertCost the cost of inserting a character; must not be negative.
         * @return {@code this} builder.
         */
        public Builder setInsertCost(final int insertCost) {
            this.insertCost = insertCost;
            return this;
        }

        /**
         * Sets the cost of a substitution (replace) operation.
         *
         * @param replaceCost the cost of replacing a character; must not be negative.
         * @return {@code this} builder.
         */
        public Builder setReplaceCost(final int replaceCost) {
            this.replaceCost = replaceCost;
            return this;
        }

        /**
         * Sets the threshold for limited distance calculation.
         *
         * <p>
         * When set, {@link LevenshteinDistance#apply} returns {@code -1} if the computed
         * distance exceeds this value. When {@code null}, the unlimited algorithm is used.
         * </p>
         *
         * @param threshold the maximum distance to report; must not be negative, or {@code null}
         *                  for no limit.
         * @return {@code this} builder.
         */
        public Builder setThreshold(final Integer threshold) {
            this.threshold = threshold;
            return this;
        }
    }

    /**
     * The singleton instance (uses default costs and no threshold).
     */
    private static final LevenshteinDistance INSTANCE = new LevenshteinDistance();

    /**
     * Returns a new {@link Builder} for constructing {@link LevenshteinDistance} instances.
     *
     * @return a new {@link Builder}.
     * @since 1.16.0
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Gets the default instance, which uses no threshold and all operation costs set to 1.
     *
     * @return the default instance.
     */
    public static LevenshteinDistance getDefaultInstance() {
        return INSTANCE;
    }

    /**
     * Finds the Levenshtein distance between two CharSequences if it is less than or equal to a
     * given threshold, using configurable costs for insert, delete, and replace operations.
     *
     * <p>
     * This implementation follows from <em>Algorithms on Strings, Trees and Sequences</em> by
     * Dan Gusfield and Chas Emerick's implementation of the Levenshtein distance algorithm.
     * </p>
     *
     * <p>
     * Note: The stripe-width optimisation used in the unit-cost case relies on the assumption that
     * each operation costs exactly 1. When custom costs are supplied the stripe cannot be reliably
     * bounded to {@code 2*threshold+1}, so the full O(nm) DP table is used instead, returning
     * {@code -1} only when the final distance exceeds the threshold.
     * </p>
     *
     * <pre>
     * limitedCompare(null, *, *, *, *, *)             = throws {@link IllegalArgumentException}
     * limitedCompare(*, null, *, *, *, *)             = throws {@link IllegalArgumentException}
     * limitedCompare(*, *, -1, *, *, *)               = throws {@link IllegalArgumentException}
     * limitedCompare("","", 0, 1, 1, 1)               = 0
     * limitedCompare("aaapppp", "", 8, 1, 1, 1)       = 7
     * limitedCompare("aaapppp", "", 7, 1, 1, 1)       = 7
     * limitedCompare("aaapppp", "", 6, 1, 1, 1)       = -1
     * limitedCompare("elephant", "hippo", 7, 1, 1, 1) = 7
     * limitedCompare("elephant", "hippo", 6, 1, 1, 1) = -1
     * limitedCompare("hippo", "elephant", 7, 1, 1, 1) = 7
     * limitedCompare("hippo", "elephant", 6, 1, 1, 1) = -1
     * </pre>
     *
     * @param <E>         the element type of the {@link SimilarityInput}.
     * @param left        the first SimilarityInput, must not be null.
     * @param right       the second SimilarityInput, must not be null.
     * @param threshold   the target threshold, must not be negative.
     * @param insertCost  the cost of an insertion operation, must not be negative.
     * @param deleteCost  the cost of a deletion operation, must not be negative.
     * @param replaceCost the cost of a substitution operation, must not be negative.
     * @return result distance, or {@code -1} if the distance exceeds the threshold.
     */
    private static <E> int limitedCompare(SimilarityInput<E> left, SimilarityInput<E> right, // NOPMD
                                          final int threshold, final int insertCost, final int deleteCost, final int replaceCost) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("CharSequences must not be null");
        }
        if (threshold < 0) {
            throw new IllegalArgumentException("Threshold must not be negative");
        }

        final int n = left.length();
        final int m = right.length();

        if (n == 0) {
            final int dist = m * insertCost;
            return dist <= threshold ? dist : -1;
        }
        if (m == 0) {
            final int dist = n * deleteCost;
            return dist <= threshold ? dist : -1;
        }

        if (insertCost == 1 && deleteCost == 1 && replaceCost == 1) {
            return limitedCompareUniformCost(left, right, threshold, n, m);
        }
        return limitedCompareCustomCost(left, right, threshold, n, m,
                new int[] {insertCost, deleteCost, replaceCost});
    }

    /**
     * Full O(nm) limited compare for custom (non-uniform) operation costs.
     *
     * <p>
     * Uses two rolling arrays to keep memory at O(min(n, m)).
     * </p>
     *
     * <p>
     * When {@code deleteCost != insertCost} swapping the strings would change the semantics
     * (delete on the original becomes insert on the swapped copy), so the orientation is always
     * kept as-is and the correct directional cost is applied.
     * </p>
     *
     * @param <E>       the element type of the {@link SimilarityInput}.
     * @param left      the first SimilarityInput, must not be null.
     * @param right     the second SimilarityInput, must not be null.
     * @param threshold the target threshold.
     * @param n         the length of {@code left}.
     * @param m         the length of {@code right}.
     * @param costs     int array of length 3: {@code {insertCost, deleteCost, replaceCost}}.
     * @return result distance, or {@code -1} if the distance exceeds the threshold.
     */
    private static <E> int limitedCompareCustomCost(final SimilarityInput<E> left,
                                                    final SimilarityInput<E> right, final int threshold, final int n, final int m,
                                                    final int[] costs) {
        final int insertCost = costs[0];
        final int deleteCost = costs[1];
        final int replaceCost = costs[2];

        int[] p = new int[n + 1];
        int[] d = new int[n + 1];

        for (int i = 0; i <= n; i++) {
            p[i] = i * deleteCost;
        }

        for (int j = 1; j <= m; j++) {
            final E rightJ = right.at(j - 1);
            d[0] = j * insertCost;

            for (int i = 1; i <= n; i++) {
                if (left.at(i - 1).equals(rightJ)) {
                    d[i] = p[i - 1];
                } else {
                    d[i] = Math.min(
                            Math.min(d[i - 1] + insertCost, p[i] + deleteCost),
                            p[i - 1] + replaceCost);
                }
            }

            final int[] tempD = p;
            p = d;
            d = tempD;
        }

        return p[n] <= threshold ? p[n] : -1;
    }

    /**
     * Classic stripe-optimised O(km) limited compare for uniform unit costs.
     *
     * <p>
     * This preserves the original algorithm exactly.
     * </p>
     *
     * @param <E>       the element type of the {@link SimilarityInput}.
     * @param left      the first SimilarityInput, must not be null.
     * @param right     the second SimilarityInput, must not be null.
     * @param threshold the target threshold.
     * @param n         the length of {@code left} (after optional swap).
     * @param m         the length of {@code right} (after optional swap).
     * @return result distance, or {@code -1} if the distance exceeds the threshold.
     */
    private static <E> int limitedCompareUniformCost(SimilarityInput<E> left,
                                                     SimilarityInput<E> right, final int threshold, int n, int m) {

        if (n > m) {
            final SimilarityInput<E> tmp = left;
            left = right;
            right = tmp;
            n = m;
            m = right.length();
        }

        if (m - n > threshold) {
            return -1;
        }

        int[] p = new int[n + 1];
        int[] d = new int[n + 1];
        int[] tempD;

        final int boundary = Math.min(n, threshold) + 1;
        for (int i = 0; i < boundary; i++) {
            p[i] = i;
        }
        Arrays.fill(p, boundary, p.length, Integer.MAX_VALUE);
        Arrays.fill(d, Integer.MAX_VALUE);

        for (int j = 1; j <= m; j++) {
            final E rightJ = right.at(j - 1);
            d[0] = j;

            final int min = Math.max(1, j - threshold);
            final int max = j > Integer.MAX_VALUE - threshold ? n : Math.min(n, j + threshold);

            if (min > 1) {
                d[min - 1] = Integer.MAX_VALUE;
            }

            int lowerBound = Integer.MAX_VALUE;
            for (int i = min; i <= max; i++) {
                if (left.at(i - 1).equals(rightJ)) {
                    d[i] = p[i - 1];
                } else {
                    d[i] = 1 + Math.min(Math.min(d[i - 1], p[i]), p[i - 1]);
                }
                lowerBound = Math.min(lowerBound, d[i]);
            }
            if (lowerBound > threshold) {
                return -1;
            }

            tempD = p;
            p = d;
            d = tempD;
        }

        return p[n] <= threshold ? p[n] : -1;
    }

    /**
     * Finds the Levenshtein distance between two Strings using configurable insert, delete, and
     * replace costs.
     *
     * <p>
     * A higher score indicates a greater distance.
     * </p>
     *
     * <pre>
     * unlimitedCompare(null, *, *, *, *)             = throws {@link IllegalArgumentException}
     * unlimitedCompare(*, null, *, *, *)             = throws {@link IllegalArgumentException}
     * unlimitedCompare("","", 1, 1, 1)               = 0
     * unlimitedCompare("","a", 1, 1, 1)              = 1
     * unlimitedCompare("aaapppp", "", 1, 1, 1)       = 7
     * unlimitedCompare("frog", "fog", 1, 1, 1)       = 1
     * unlimitedCompare("fly", "ant", 1, 1, 1)        = 3
     * unlimitedCompare("elephant", "hippo", 1, 1, 1) = 7
     * unlimitedCompare("hippo", "elephant", 1, 1, 1) = 7
     * unlimitedCompare("hippo", "zzzzzzzz", 1, 1, 1) = 8
     * unlimitedCompare("hello", "hallo", 1, 1, 1)    = 1
     * </pre>
     *
     * @param <E>         the element type of the {@link SimilarityInput}.
     * @param left        the first SimilarityInput, must not be null.
     * @param right       the second SimilarityInput, must not be null.
     * @param insertCost  the cost of an insertion operation, must not be negative.
     * @param deleteCost  the cost of a deletion operation, must not be negative.
     * @param replaceCost the cost of a substitution operation, must not be negative.
     * @return result distance.
     * @throws IllegalArgumentException if either input is {@code null}.
     */
    private static <E> int unlimitedCompare(SimilarityInput<E> left, SimilarityInput<E> right,
                                            final int insertCost, final int deleteCost, final int replaceCost) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("CharSequences must not be null");
        }

        int n = left.length();
        int m = right.length();

        if (n == 0) {
            return m * insertCost;
        }
        if (m == 0) {
            return n * deleteCost;
        }

        // When insert == delete costs are symmetric; swapping the shorter string into
        // 'left' minimises working-array size without changing semantics.
        // When insert != delete, swapping reverses their roles, so we keep the original order.
        final boolean canSwap = insertCost == deleteCost;
        if (canSwap && n > m) {
            final SimilarityInput<E> tmp = left;
            left = right;
            right = tmp;
            n = m;
            m = right.length();
        }

        final int[] p = new int[n + 1];

        for (int i = 0; i <= n; i++) {
            p[i] = i * deleteCost;
        }

        int upperLeft;
        int upper;

        for (int j = 1; j <= m; j++) {
            upperLeft = p[0];
            final E rightJ = right.at(j - 1);
            p[0] = j * insertCost;

            for (int i = 1; i <= n; i++) {
                upper = p[i];
                if (left.at(i - 1).equals(rightJ)) {
                    p[i] = upperLeft;
                } else {
                    p[i] = Math.min(
                            Math.min(p[i - 1] + insertCost, p[i] + deleteCost),
                            upperLeft + replaceCost);
                }
                upperLeft = upper;
            }
        }
        return p[n];
    }

    /**
     * Threshold (nullable). When non-null, {@link #limitedCompare} is used instead of
     * {@link #unlimitedCompare}.
     */
    private final Integer threshold;

    /** Cost of inserting a character into the left sequence. */
    private final int insertCost;

    /** Cost of deleting a character from the left sequence. */
    private final int deleteCost;

    /** Cost of substituting one character for another. */
    private final int replaceCost;

    /**
     * Constructs a default instance that uses the unlimited algorithm with all operation costs
     * set to 1.
     *
     * @see LevenshteinDistance#getDefaultInstance()
     * @deprecated Use {@link #getDefaultInstance()} or {@link #builder()}.
     */
    @Deprecated
    public LevenshteinDistance() {
        this(builder());
    }

    /**
     * Constructs a new instance with the given threshold and all operation costs set to 1.
     *
     * <p>
     * If the threshold is not null, distance calculations will be limited to that maximum value.
     * If the threshold is null, the unlimited version of the algorithm will be used.
     * </p>
     *
     * @param threshold if this is null then distance calculations will not be limited;
     *                  otherwise it must not be negative.
     * @deprecated Use {@link #builder()}.
     */
    @Deprecated
    public LevenshteinDistance(final Integer threshold) {
        this(builder().setThreshold(threshold));
    }

    /**
     * Constructs a new {@link LevenshteinDistance} from a {@link Builder}.
     *
     * @param builder the builder; must not be null.
     * @throws IllegalArgumentException if the threshold is negative, or any cost is negative.
     */
    private LevenshteinDistance(final Builder builder) {
        if (builder.threshold != null && builder.threshold < 0) {
            throw new IllegalArgumentException("Threshold must not be negative");
        }
        if (builder.insertCost < 0) {
            throw new IllegalArgumentException("Insert cost must not be negative");
        }
        if (builder.deleteCost < 0) {
            throw new IllegalArgumentException("Delete cost must not be negative");
        }
        if (builder.replaceCost < 0) {
            throw new IllegalArgumentException("Replace cost must not be negative");
        }
        this.threshold = builder.threshold;
        this.insertCost = builder.insertCost;
        this.deleteCost = builder.deleteCost;
        this.replaceCost = builder.replaceCost;
    }

    /**
     * Computes the Levenshtein distance between two Strings.
     *
     * <p>
     * A higher score indicates a greater distance.
     * </p>
     *
     * <pre>
     * distance.apply(null, *)             = throws {@link IllegalArgumentException}
     * distance.apply(*, null)             = throws {@link IllegalArgumentException}
     * distance.apply("","")               = 0
     * distance.apply("","a")              = insertCost
     * distance.apply("aaapppp", "")       = 7 * deleteCost
     * distance.apply("frog", "fog")       = 1 * deleteCost
     * distance.apply("fly", "ant")        = 3 * replaceCost
     * distance.apply("elephant", "hippo") = 7  (with default costs)
     * distance.apply("hippo", "elephant") = 7  (with default costs)
     * distance.apply("hello", "hallo")    = 1  (with default costs)
     * </pre>
     *
     * @param left  the first input, must not be null.
     * @param right the second input, must not be null.
     * @return result distance, or {@code -1} if a threshold is set and the distance exceeds it.
     * @throws IllegalArgumentException if either String input is {@code null}.
     */
    @Override
    public Integer apply(final CharSequence left, final CharSequence right) {
        return apply(SimilarityInput.input(left), SimilarityInput.input(right));
    }

    /**
     * Computes the Levenshtein distance between two {@link SimilarityInput} instances.
     *
     * <p>
     * A higher score indicates a greater distance.
     * </p>
     *
     * @param <E>   the type of element compared by the similarity score.
     * @param left  the first input, must not be null.
     * @param right the second input, must not be null.
     * @return result distance, or {@code -1} if a threshold is set and the distance exceeds it.
     * @throws IllegalArgumentException if either input is {@code null}.
     * @since 1.16.0
     */
    public <E> Integer apply(final SimilarityInput<E> left, final SimilarityInput<E> right) {
        if (threshold != null) {
            return limitedCompare(left, right, threshold, insertCost, deleteCost, replaceCost);
        }
        return unlimitedCompare(left, right, insertCost, deleteCost, replaceCost);
    }

    /**
     * Gets the cost of a deletion operation.
     *
     * @return the deletion cost.
     * @since 1.16.0
     */
    public int getDeleteCost() {
        return deleteCost;
    }

    /**
     * Gets the cost of an insertion operation.
     *
     * @return the insertion cost.
     * @since 1.16.0
     */
    public int getInsertCost() {
        return insertCost;
    }

    /**
     * Gets the cost of a substitution (replace) operation.
     *
     * @return the replacement cost.
     * @since 1.16.0
     */
    public int getReplaceCost() {
        return replaceCost;
    }

    /**
     * Gets the distance threshold.
     *
     * @return the distance threshold, or {@code null} if no threshold is set.
     */
    public Integer getThreshold() {
        return threshold;
    }
}
