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
 * An algorithm for measuring the difference between two character sequences using the <a href="https://en.wikipedia.org/wiki/Levenshtein_distance">Levenshtein
 * Distance</a>.
 *
 * <p>
 * This is the number of changes needed to change one sequence into another, where each change is a single character modification (deletion, insertion or
 * substitution).
 * </p>
 *
 * <p>
 * This implementation supports configurable costs for insertion, deletion, and substitution operations. By default, all costs are set to 1 for
 * backward compatibility.
 * </p>
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
     * Default cost for an insertion operation.
     */
    private static final int DEFAULT_INSERT_COST = 1;

    /**
     * Default cost for a deletion operation.
     */
    private static final int DEFAULT_DELETE_COST = 1;

    /**
     * Default cost for a substitution (replace) operation.
     */
    private static final int DEFAULT_REPLACE_COST = 1;

    /**
     * The singleton instance (uses default costs and no threshold).
     */
    private static final LevenshteinDistance INSTANCE = new LevenshteinDistance();

    /**
     * Gets the default instance.
     *
     * @return The default instance.
     */
    public static LevenshteinDistance getDefaultInstance() {
        return INSTANCE;
    }

    /**
     * Finds the Levenshtein distance between two CharSequences if it's less than or equal to a given
     * threshold, using configurable costs for insert, delete, and replace operations.
     *
     * <p>
     * This implementation follows from Algorithms on Strings, Trees and Sequences by Dan Gusfield and
     * Chas Emerick's implementation of the Levenshtein distance algorithm.
     * </p>
     *
     * <p>
     * Note: The stripe-width optimisation used in the default (all-costs-1) case relies on the
     * assumption that each operation costs exactly 1. When custom costs are supplied the stripe
     * cannot be reliably bounded to {@code 2*threshold+1}, so the full O(nm) DP table is used
     * instead, returning -1 only when the final distance exceeds the threshold.
     * </p>
     *
     * <pre>
     * limitedCompare(null, *, *, *, *, *)             = Throws {@link IllegalArgumentException}
     * limitedCompare(*, null, *, *, *, *)             = Throws {@link IllegalArgumentException}
     * limitedCompare(*, *, -1, *, *, *)               = Throws {@link IllegalArgumentException}
     * limitedCompare("","", 0, 1, 1, 1)               = 0
     * limitedCompare("aaapppp", "", 8, 1, 1, 1)       = 7
     * limitedCompare("aaapppp", "", 7, 1, 1, 1)       = 7
     * limitedCompare("aaapppp", "", 6, 1, 1, 1))      = -1
     * limitedCompare("elephant", "hippo", 7, 1, 1, 1) = 7
     * limitedCompare("elephant", "hippo", 6, 1, 1, 1) = -1
     * limitedCompare("hippo", "elephant", 7, 1, 1, 1) = 7
     * limitedCompare("hippo", "elephant", 6, 1, 1, 1) = -1
     * </pre>
     *
     * @param left        the first SimilarityInput, must not be null.
     * @param right       the second SimilarityInput, must not be null.
     * @param threshold   the target threshold, must not be negative.
     * @param insertCost  the cost of an insertion operation, must not be negative.
     * @param deleteCost  the cost of a deletion operation, must not be negative.
     * @param replaceCost the cost of a substitution operation, must not be negative.
     * @return result distance, or -1 if the distance exceeds the threshold.
     */
    private static <E> int limitedCompare(SimilarityInput<E> left, SimilarityInput<E> right, // NOPMD
                                          final int threshold, final int insertCost, final int deleteCost, final int replaceCost) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("CharSequences must not be null");
        }
        if (threshold < 0) {
            throw new IllegalArgumentException("Threshold must not be negative");
        }

        int n = left.length(); // length of left
        int m = right.length(); // length of right

        // If one string is empty, the edit distance is the cost of inserting/deleting
        // all characters of the other string.
        if (n == 0) {
            final int dist = m * insertCost;
            return dist <= threshold ? dist : -1;
        }
        if (m == 0) {
            final int dist = n * deleteCost;
            return dist <= threshold ? dist : -1;
        }

        // When all costs equal 1, use the classic diagonal-stripe optimisation.
        // For asymmetric costs the stripe width is not reliably bounded, so fall
        // back to the full O(nm) table and threshold-check only at the end.
        if (insertCost == 1 && deleteCost == 1 && replaceCost == 1) {
            return limitedCompareUniformCost(left, right, threshold, n, m);
        }
        return limitedCompareCustomCost(left, right, threshold, insertCost, deleteCost, replaceCost, n, m);
    }

    /**
     * Classic stripe-optimised O(km) limited compare for uniform unit costs.
     * This preserves the original algorithm exactly.
     */
    private static <E> int limitedCompareUniformCost(SimilarityInput<E> left, SimilarityInput<E> right,
                                                     final int threshold, int n, int m) {

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

        if (p[n] <= threshold) {
            return p[n];
        }
        return -1;
    }

    /**
     * Full O(nm) limited compare for custom (non-uniform) operation costs.
     * Uses two rolling arrays to keep memory at O(min(n,m)).
     *
     * <p>
     * When {@code deleteCost != insertCost} swapping the strings would change the
     * semantics (delete on the original becomes insert on the swapped copy), so
     * we always keep left as-is and pay the correct directional cost.
     * </p>
     */
    private static <E> int limitedCompareCustomCost(final SimilarityInput<E> left, final SimilarityInput<E> right,
                                                    final int threshold, final int insertCost, final int deleteCost, final int replaceCost,
                                                    final int n, final int m) {

        // p[i] = cost to convert left[0..i-1] to right[0..j-1] (previous row)
        int[] p = new int[n + 1];
        int[] d = new int[n + 1];

        // Base case: convert left[0..i-1] to empty string via i deletions.
        for (int i = 0; i <= n; i++) {
            p[i] = i * deleteCost;
        }

        for (int j = 1; j <= m; j++) {
            final E rightJ = right.at(j - 1);
            // Base case: convert empty string to right[0..j-1] via j insertions.
            d[0] = j * insertCost;

            for (int i = 1; i <= n; i++) {
                if (left.at(i - 1).equals(rightJ)) {
                    // Characters match ? no operation needed (cost 0).
                    d[i] = p[i - 1];
                } else {
                    // Minimum of: delete left[i-1], insert right[j-1], or replace.
                    d[i] = Math.min(
                            Math.min(d[i - 1] + insertCost,  // insert right[j-1]
                                    p[i]     + deleteCost), // delete left[i-1]
                            p[i - 1]           + replaceCost // replace
                    );
                }
            }

            // Swap rows.
            final int[] tempD = p;
            p = d;
            d = tempD;
        }

        if (p[n] <= threshold) {
            return p[n];
        }
        return -1;
    }

    /**
     * Finds the Levenshtein distance between two Strings using configurable
     * insert, delete, and replace costs.
     *
     * <p>
     * A higher score indicates a greater distance.
     * </p>
     *
     * <pre>
     * unlimitedCompare(null, *, *, *, *)             = Throws {@link IllegalArgumentException}
     * unlimitedCompare(*, null, *, *, *)             = Throws {@link IllegalArgumentException}
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
     * @param left        the first CharSequence, must not be null.
     * @param right       the second CharSequence, must not be null.
     * @param insertCost  the cost of an insertion operation, must not be negative.
     * @param deleteCost  the cost of a deletion operation, must not be negative.
     * @param replaceCost the cost of a substitution operation, must not be negative.
     * @return result distance.
     * @throws IllegalArgumentException if either CharSequence input is {@code null}.
     */
    private static <E> int unlimitedCompare(SimilarityInput<E> left, SimilarityInput<E> right,
                                            final int insertCost, final int deleteCost, final int replaceCost) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("CharSequences must not be null");
        }

        int n = left.length(); // length of left
        int m = right.length(); // length of right

        if (n == 0) {
            return m * insertCost;
        }
        if (m == 0) {
            return n * deleteCost;
        }

        // When costs are symmetric (insert == delete) we can safely swap the
        // shorter string into 'left' to minimise the working array size.
        // When insert != delete, swapping reverses the semantics of those two
        // operations, so we must keep the original orientation.
        final boolean canSwap = insertCost == deleteCost;
        if (canSwap && n > m) {
            final SimilarityInput<E> tmp = left;
            left = right;
            right = tmp;
            n = m;
            m = right.length();
        }

        // Single rolling array of length n+1.
        final int[] p = new int[n + 1];

        // Base case: converting left[0..i-1] ? "" costs i deletions.
        for (int i = 0; i <= n; i++) {
            p[i] = i * deleteCost;
        }

        int upperLeft;
        int upper;

        for (int j = 1; j <= m; j++) {
            upperLeft = p[0];
            final E rightJ = right.at(j - 1);
            // Base case: converting "" ? right[0..j-1] costs j insertions.
            p[0] = j * insertCost;

            for (int i = 1; i <= n; i++) {
                upper = p[i];
                if (left.at(i - 1).equals(rightJ)) {
                    // Characters match ? carry diagonal (no cost).
                    p[i] = upperLeft;
                } else {
                    // Minimum of insert, delete, or replace.
                    p[i] = Math.min(
                            Math.min(p[i - 1] + insertCost,  // insert right[j-1]
                                    p[i]     + deleteCost), // delete left[i-1]
                            upperLeft          + replaceCost  // replace
                    );
                }
                upperLeft = upper;
            }
        }
        return p[n];
    }

    // -------------------------------------------------------------------------
    // Instance state
    // -------------------------------------------------------------------------

    /**
     * Threshold (nullable). When non-null, {@link #limitedCompare} is used
     * instead of {@link #unlimitedCompare}.
     */
    private final Integer threshold;

    /**
     * Cost of inserting a character into the left sequence.
     */
    private final int insertCost;

    /**
     * Cost of deleting a character from the left sequence.
     */
    private final int deleteCost;

    /**
     * Cost of substituting one character for another.
     */
    private final int replaceCost;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Constructs a default instance that uses a version of the algorithm that does not use a
     * threshold parameter, with all operation costs set to 1.
     *
     * @see LevenshteinDistance#getDefaultInstance()
     * @deprecated Use {@link #getDefaultInstance()}.
     */
    @Deprecated
    public LevenshteinDistance() {
        this(null);
    }

    /**
     * Constructs a new instance with the given threshold and all operation costs set to 1.
     *
     * <p>
     * If the threshold is not null, distance calculations will be limited to a maximum length. If
     * the threshold is null, the unlimited version of the algorithm will be used.
     * </p>
     *
     * @param threshold If this is null then distances calculations will not be limited.
     *                  This may not be negative.
     */
    public LevenshteinDistance(final Integer threshold) {
        this(threshold, DEFAULT_INSERT_COST, DEFAULT_DELETE_COST, DEFAULT_REPLACE_COST);
    }

    /**
     * Constructs a new instance with the given threshold and custom operation costs.
     *
     * <p>
     * If the threshold is not null, distance calculations will be limited to a maximum value.
     * If the threshold is null, the unlimited version of the algorithm will be used.
     * </p>
     *
     * <p>
     * All cost parameters must be non-negative integers. Passing 0 for a cost makes that
     * operation free; passing values greater than 1 makes it more expensive relative to
     * the other operations.
     * </p>
     *
     * @param threshold   If this is null then distance calculations will not be limited.
     *                    This may not be negative.
     * @param insertCost  the cost of inserting a character, must not be negative.
     * @param deleteCost  the cost of deleting a character, must not be negative.
     * @param replaceCost the cost of replacing (substituting) a character, must not be negative.
     * @throws IllegalArgumentException if threshold is negative, or any cost is negative.
     * @since 1.13.0
     */
    public LevenshteinDistance(final Integer threshold, final int insertCost, final int deleteCost,
                               final int replaceCost) {
        if (threshold != null && threshold < 0) {
            throw new IllegalArgumentException("Threshold must not be negative");
        }
        if (insertCost < 0) {
            throw new IllegalArgumentException("Insert cost must not be negative");
        }
        if (deleteCost < 0) {
            throw new IllegalArgumentException("Delete cost must not be negative");
        }
        if (replaceCost < 0) {
            throw new IllegalArgumentException("Replace cost must not be negative");
        }
        this.threshold    = threshold;
        this.insertCost   = insertCost;
        this.deleteCost   = deleteCost;
        this.replaceCost  = replaceCost;
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Computes the Levenshtein distance between two Strings.
     *
     * <p>
     * A higher score indicates a greater distance.
     * </p>
     *
     * <pre>
     * distance.apply(null, *)             = Throws {@link IllegalArgumentException}
     * distance.apply(*, null)             = Throws {@link IllegalArgumentException}
     * distance.apply("","")               = 0
     * distance.apply("","a")              = insertCost
     * distance.apply("aaapppp", "")       = 7 * deleteCost
     * distance.apply("frog", "fog")       = 1 * deleteCost (one deletion)
     * distance.apply("fly", "ant")        = replaceCost + replaceCost + replaceCost
     * distance.apply("elephant", "hippo") = 7  (with default costs)
     * distance.apply("hippo", "elephant") = 7  (with default costs)
     * distance.apply("hello", "hallo")    = 1  (with default costs)
     * </pre>
     *
     * @param left  the first input, must not be null.
     * @param right the second input, must not be null.
     * @return result distance, or -1 if a threshold is set and the distance exceeds it.
     * @throws IllegalArgumentException if either String input is {@code null}.
     */
    @Override
    public Integer apply(final CharSequence left, final CharSequence right) {
        return apply(SimilarityInput.input(left), SimilarityInput.input(right));
    }

    /**
     * Computes the Levenshtein distance between two inputs.
     *
     * <p>
     * A higher score indicates a greater distance.
     * </p>
     *
     * @param <E>   The type of similarity score unit.
     * @param left  the first input, must not be null.
     * @param right the second input, must not be null.
     * @return result distance, or -1 if a threshold is set and the distance exceeds it.
     * @throws IllegalArgumentException if either input is {@code null}.
     * @since 1.13.0
     */
    public <E> Integer apply(final SimilarityInput<E> left, final SimilarityInput<E> right) {
        if (threshold != null) {
            return limitedCompare(left, right, threshold, insertCost, deleteCost, replaceCost);
        }
        return unlimitedCompare(left, right, insertCost, deleteCost, replaceCost);
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    /**
     * Gets the distance threshold.
     *
     * @return The distance threshold, or {@code null} if no threshold is set.
     */
    public Integer getThreshold() {
        return threshold;
    }

    /**
     * Gets the cost of an insertion operation.
     *
     * @return The insertion cost.
     * @since 1.13.0
     */
    public int getInsertCost() {
        return insertCost;
    }

    /**
     * Gets the cost of a deletion operation.
     *
     * @return The deletion cost.
     * @since 1.13.0
     */
    public int getDeleteCost() {
        return deleteCost;
    }

    /**
     * Gets the cost of a substitution (replace) operation.
     *
     * @return The replacement cost.
     * @since 1.13.0
     */
    public int getReplaceCost() {
        return replaceCost;
    }
}