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
 * A similarity algorithm indicating the length of the longest common subsequence between two strings.
 *
 * <p>
 * The Longest common subsequence algorithm returns the length of the longest subsequence that two strings have in
 * common. Two strings that are entirely different, return a value of 0, and two strings that return a value
 * of the commonly shared length implies that the strings are completely the same in value and position.
 * <em>Note.</em>  Generally this algorithm is fairly inefficient, as for length <em>m</em>, <em>n</em> of the input
 * {@code CharSequence}'s {@code left} and {@code right} respectively, the runtime of the
 * algorithm is <em>O(m*n)</em>.
 * </p>
 *
 * <p>
 * As of version 1.10, a more space-efficient of the algorithm is implemented. The new algorithm has linear space
 * complexity instead of quadratic. However, time complexity is still quadratic in the size of input strings.
 * </p>
 *
 * <p>
 * The implementation is based on Hirschberg's Longest Commons Substring algorithm (cited below).
 * </p>
 *
 * <p>For further reading see:</p>
 * <ul>
 * <li>
 * Lothaire, M. <em>Applied combinatorics on words</em>. New York: Cambridge U Press, 2005. <b>12-13</b>
 * </li>
 * <li>
 * D. S. Hirschberg, "A linear space algorithm for computing maximal common subsequences," CACM, 1975, pp. 341--343.
 * </li>
 * </ul>
 *
 * @since 1.0
 */
public class LongestCommonSubsequence implements SimilarityScore<Integer> {

    /**
     * Singleton instance.
     */
    static final LongestCommonSubsequence INSTANCE = new LongestCommonSubsequence();

    /**
     * An implementation of "ALG B" from Hirschberg's CACM '71 paper.
     * Assuming the first input sequence is of size {@code m} and the second input sequence is of size
     * {@code n}, this method returns the last row of the dynamic programming (DP) table when calculating
     * the LCS of the two sequences in <em>O(m*n)</em> time and <em>O(n)</em> space.
     * The last element of the returned array, is the size of the LCS of the two input sequences.
     *
     * @param left first input sequence.
     * @param right second input sequence.
     * @return last row of the dynamic-programming (DP) table for calculating the LCS of {@code left} and {@code right}
     * @since 1.10.0
     */
    private static int[] algorithmB(final CharSequence left, final CharSequence right) {
        final int m = left.length();
        final int n = right.length();

        // Creating an array for storing two rows of DP table
        final int[][] dpRows = new int[2][1 + n];

        for (int i = 1; i <= m; i++) {
            // K(0, j) <- K(1, j) [j = 0...n], as per the paper:
            // Since we have references in Java, we can swap references instead of literal copying.
            // We could also use a "binary index" using modulus operator, but directly swapping the
            // two rows helps readability and keeps the code consistent with the algorithm description
            // in the paper.
            final int[] temp = dpRows[0];
            dpRows[0] = dpRows[1];
            dpRows[1] = temp;

            for (int j = 1; j <= n; j++) {
                if (left.charAt(i - 1) == right.charAt(j - 1)) {
                    dpRows[1][j] = dpRows[0][j - 1] + 1;
                } else {
                    dpRows[1][j] = Math.max(dpRows[1][j - 1], dpRows[0][j]);
                }
            }
        }

        // LL(j) <- K(1, j) [j=0...n], as per the paper:
        // We don't need literal copying of the array, we can just return the reference
        return dpRows[1];
    }

    /**
     * An implementation of "ALG C" from Hirschberg's CACM '71 paper.
     * Assuming the first input sequence is of size {@code m} and the second input sequence is of size
     * {@code n}, this method returns the Longest Common Subsequence (LCS) of the two sequences in
     * <em>O(m*n)</em> time and <em>O(m+n)</em> space.
     *
     * @param left first input sequence.
     * @param right second input sequence.
     * @return the LCS of {@code left} and {@code right}
     * @since 1.10.0
     */
    private static String algorithmC(final CharSequence left, final CharSequence right) {
        final int m = left.length();
        final int n = right.length();

        final StringBuilder out = new StringBuilder();

        if (m == 1) { // Handle trivial cases, as per the paper
            final char leftCh = left.charAt(0);
            for (int j = 0; j < n; j++) {
                if (leftCh == right.charAt(j)) {
                    out.append(leftCh);
                    break;
                }
            }
        } else if (n > 0 && m > 1) {
            final int mid = m / 2; // Find the middle point

            final CharSequence leftFirstPart = left.subSequence(0, mid);
            final CharSequence leftSecondPart = left.subSequence(mid, m);

            // Step 3 of the algorithm: two calls to Algorithm B
            final int[] l1 = algorithmB(leftFirstPart, right);
            final int[] l2 = algorithmB(reverse(leftSecondPart), reverse(right));

            // Find k, as per the Step 4 of the algorithm
            int k = 0;
            int t = 0;
            for (int j = 0; j <= n; j++) {
                final int s = l1[j] + l2[n - j];
                if (t < s) {
                    t = s;
                    k = j;
                }
            }

            // Step 5: solve simpler problems, recursively
            out.append(algorithmC(leftFirstPart, right.subSequence(0, k)));
            out.append(algorithmC(leftSecondPart, right.subSequence(k, n)));
        }

        return out.toString();
    }

    // An auxiliary method for CharSequence reversal
    private static String reverse(final CharSequence s) {
        return new StringBuilder(s).reverse().toString();
    }

    /**
     * Calculates the longest common subsequence similarity score of two {@code CharSequence}'s passed as
     * input.
     *
     * <p>
     * This method implements a more efficient version of LCS algorithm which has quadratic time and
     * linear space complexity.
     * </p>
     *
     * <p>
     * This method is based on newly implemented {@link #algorithmB(CharSequence, CharSequence)}.
     * An evaluation using JMH revealed that this method is almost two times faster than its previous version.
     * </p>
     *
     * @param left first character sequence
     * @param right second character sequence
     * @return length of the longest common subsequence of {@code left} and {@code right}
     * @throws IllegalArgumentException if either String input {@code null}
     */
    @Override
    public Integer apply(final CharSequence left, final CharSequence right) {
        // Quick return for invalid inputs
        if (left == null || right == null) {
            throw new IllegalArgumentException("Inputs must not be null");
        }
        // Find lengths of two strings
        final int leftSz = left.length();
        final int rightSz = right.length();

        // Check if we can avoid calling algorithmB which involves heap space allocation
        if (leftSz == 0 || rightSz == 0) {
            return 0;
        }

        // Check if we can save even more space
        if (leftSz < rightSz) {
            return algorithmB(right, left)[leftSz];
        }
        return algorithmB(left, right)[rightSz];
    }

    /**
     * Computes the longest common subsequence between the two {@code CharSequence}'s passed as input.
     *
     * <p>
     * Note, a substring and subsequence are not necessarily the same thing. Indeed, {@code abcxyzqrs} and
     * {@code xyzghfm} have both the same common substring and subsequence, namely {@code xyz}. However,
     * {@code axbyczqrs} and {@code abcxyzqtv} have the longest common subsequence {@code xyzq} because a
     * subsequence need not have adjacent characters.
     * </p>
     *
     * <p>
     * For reference, we give the definition of a subsequence for the reader: a <em>subsequence</em> is a sequence that
     * can be derived from another sequence by deleting some elements without changing the order of the remaining
     * elements.
     * </p>
     *
     * @param left first character sequence
     * @param right second character sequence
     * @return the longest common subsequence found
     * @throws IllegalArgumentException if either String input {@code null}
     * @deprecated Deprecated as of 1.2 due to a typo in the method name.
     * Use {@link #longestCommonSubsequence(CharSequence, CharSequence)} instead.
     * This method will be removed in 2.0.
     */
    @Deprecated
    public CharSequence logestCommonSubsequence(final CharSequence left, final CharSequence right) {
        return longestCommonSubsequence(left, right);
    }

    /**
     * Computes the longest common subsequence between the two {@code CharSequence}'s passed as
     * input.
     *
     * <p>
     * This method implements a more efficient version of LCS algorithm which although has quadratic time, it
     * has linear space complexity.
     * </p>
     *
     *
     * <p>
     * Note, a substring and subsequence are not necessarily the same thing. Indeed, {@code abcxyzqrs} and
     * {@code xyzghfm} have both the same common substring and subsequence, namely {@code xyz}. However,
     * {@code axbyczqrs} and {@code abcxyzqtv} have the longest common subsequence {@code xyzq} because a
     * subsequence need not have adjacent characters.
     * </p>
     *
     * <p>
     * For reference, we give the definition of a subsequence for the reader: a <em>subsequence</em> is a sequence that
     * can be derived from another sequence by deleting some elements without changing the order of the remaining
     * elements.
     * </p>
     *
     * @param left first character sequence
     * @param right second character sequence
     * @return the longest common subsequence found
     * @throws IllegalArgumentException if either String input {@code null}
     * @since 1.2
     */
    public CharSequence longestCommonSubsequence(final CharSequence left, final CharSequence right) {
        // Quick return
        if (left == null || right == null) {
            throw new IllegalArgumentException("Inputs must not be null");
        }
        // Find lengths of two strings
        final int leftSz = left.length();
        final int rightSz = right.length();

        // Check if we can avoid calling algorithmC which involves heap space allocation
        if (leftSz == 0 || rightSz == 0) {
            return "";
        }

        // Check if we can save even more space
        if (leftSz < rightSz) {
            return algorithmC(right, left);
        }
        return algorithmC(left, right);
    }

    /**
     * Computes the lcsLengthArray for the sake of doing the actual lcs calculation. This is the
     * dynamic programming portion of the algorithm, and is the reason for the runtime complexity being
     * O(m*n), where m=left.length() and n=right.length().
     *
     * @param left first character sequence
     * @param right second character sequence
     * @return lcsLengthArray
     * @deprecated Deprecated as of 1.10. A more efficient implementation for calculating LCS is now available.
     * Use {@link #longestCommonSubsequence(CharSequence, CharSequence)} instead to directly calculate the LCS.
     * This method will be removed in 2.0.
     */
    @Deprecated
    public int[][] longestCommonSubstringLengthArray(final CharSequence left, final CharSequence right) {
        final int[][] lcsLengthArray = new int[left.length() + 1][right.length() + 1];
        for (int i = 0; i < left.length(); i++) {
            for (int j = 0; j < right.length(); j++) {
                if (i == 0) {
                    lcsLengthArray[i][j] = 0;
                }
                if (j == 0) {
                    lcsLengthArray[i][j] = 0;
                }
                if (left.charAt(i) == right.charAt(j)) {
                    lcsLengthArray[i + 1][j + 1] = lcsLengthArray[i][j] + 1;
                } else {
                    lcsLengthArray[i + 1][j + 1] = Math.max(lcsLengthArray[i + 1][j], lcsLengthArray[i][j + 1]);
                }
            }
        }
        return lcsLengthArray;
    }

}
