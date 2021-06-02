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
 * <i>Note.</i>  Generally this algorithm is fairly inefficient, as for length <i>m</i>, <i>n</i> of the input
 * {@code CharSequence}'s {@code left} and {@code right} respectively, the runtime of the
 * algorithm is <i>O(m*n)</i>.
 * </p>
 *
 * <p>
 * This implementation is based on the Longest Commons Substring algorithm
 * from <a href="https://en.wikipedia.org/wiki/Longest_common_subsequence_problem">
 * https://en.wikipedia.org/wiki/Longest_common_subsequence_problem</a>.
 * </p>
 *
 * <p>For further reading see:</p>
 *
 * <p>Lothaire, M. <i>Applied combinatorics on words</i>. New York: Cambridge U Press, 2005. <b>12-13</b></p>
 *
 * @since 1.0
 */
public class LongestCommonSubsequence implements SimilarityScore<Integer> {

    /**
     * Calculates longest common subsequence similarity score of two {@code CharSequence}'s passed as
     * input.
     *
     * @param left first character sequence
     * @param right second character sequence
     * @return longestCommonSubsequenceLength
     * @throws IllegalArgumentException
     *             if either String input {@code null}
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

        // Check if we can save even more space
        if (leftSz < rightSz) {
            return algorithmB(right, rightSz, left, leftSz)[leftSz];
        }
        return algorithmB(left, leftSz, right, rightSz)[rightSz];
    }

    /**
     * An implementation of "ALG B" from Hirschberg's paper <a href="https://dl.acm.org/doi/10.1145/360825.360861">A linear space algorithm for computing maximal common subsequences</a>.
     * Assuming the sequence <code>left</code> is of size <code>m</code> and the sequence <code>right</code> is of size <code>n</code>,
     * this method returns the last row of the dynamic programming table when calculating LCS the two sequences.
     * Therefore, the last element of the returned array, is the size of LCS of <code>left</code> and <code>right</code>.
     * This method runs in O(m * n) time and O(n) space.
     * To save more space, it is preferable to pass the shorter sequence as <code>right</code>.
     *
     * @param left Left sequence
     * @param m Length of left sequence
     * @param right Right sequence
     * @param n Length of right sequence
     * @return Last row of DP table for calculating LCS of <code>left</code> and <code>right</code>
     */
    static int[] algorithmB(final CharSequence left, final int m,
                            final CharSequence right, final int n) {
        final int[][] dp = new int[2][1 + n];

        for (int i = 1; i <= m; i++) {
            // K(0, j) <- K(1, j) [j = 0...n], as per the paper:
            // Since we have references in Java, we can swap references instead of literal copying.
            // We could also use a "binary index" using modulus operator, but directly swapping the
            // two rows helps readability and keeps the code consistent with the algorithm description
            // in the paper.
            final int[] temp = dp[0];
            dp[0] = dp[1];
            dp[1] = temp;
            // Hoisting the virtual call out of the inner loop to help with performance.
            final int leftCh = left.charAt(i - 1);
            for (int j = 1; j <= n; j++) {
                if (leftCh == right.charAt(j - 1)) {
                    dp[1][j] = dp[0][j - 1] + 1;
                } else {
                    dp[1][j] = Math.max(dp[1][j - 1], dp[0][j]);
                }
            }
        }
        // LL(j) <- K(1, j) [j=0...n], as per the paper:
        // We don't need literal copying of the array, we can just return the reference
        return dp[1];
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
     * For reference, we give the definition of a subsequence for the reader: a <i>subsequence</i> is a sequence that
     * can be derived from another sequence by deleting some elements without changing the order of the remaining
     * elements.
     * </p>
     *
     * @param left first character sequence
     * @param right second character sequence
     * @return The longest common subsequence found
     * @throws IllegalArgumentException
     *             if either String input {@code null}
     * @deprecated Deprecated as of 1.2 due to a typo in the method name.
     *              Use {@link #longestCommonSubsequence(CharSequence, CharSequence)} instead.
     *              This method will be removed in 2.0.
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
    * Note, a substring and subsequence are not necessarily the same thing. Indeed, {@code abcxyzqrs} and
    * {@code xyzghfm} have both the same common substring and subsequence, namely {@code xyz}. However,
    * {@code axbyczqrs} and {@code abcxyzqtv} have the longest common subsequence {@code xyzq} because a
    * subsequence need not have adjacent characters.
    * </p>
    *
    * <p>
    * For reference, we give the definition of a subsequence for the reader: a <i>subsequence</i> is a sequence that
    * can be derived from another sequence by deleting some elements without changing the order of the remaining
    * elements.
    * </p>
    *
    * @param left first character sequence
    * @param right second character sequence
    * @return The longest common subsequence found
    * @throws IllegalArgumentException
    *             if either String input {@code null}
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

       // Check if we can save even more space
       if (leftSz < rightSz) {
           return algorithmC(right, rightSz, left, leftSz);
       }
       return algorithmC(left, leftSz, right, rightSz);
   }

    /**
     * An implementation of "ALG C" from Hirschberg's paper <a href="https://dl.acm.org/doi/10.1145/360825.360861">A linear space algorithm for computing maximal common subsequences</a>.
     * Assuming the sequence <code>left</code> is of size <code>m</code> and the sequence <code>right</code> is of size <code>n</code>,
     * this method returns Longest Common Subsequence (LCS) the two sequences.
     * As per the paper, this method runs in O(m * n) time and O(m + n) space.
     *
     * @param left Left sequence
     * @param m Length of left sequence
     * @param right Right sequence
     * @param n Length of right sequence
     * @return LCS of <code>left</code> and <code>right</code>
     */
   static CharSequence algorithmC(final CharSequence left, final int m,
                                  final CharSequence right, final int n) {
       final StringBuilder sb = new StringBuilder(Math.max(m, n));

       if (m == 1) { // Handle trivial cases, as per the paper
           final int leftCh = left.charAt(0);
           for (int j = 0; j < n; j++) {
               if (leftCh == right.charAt(j)) {
                   sb.appendCodePoint(leftCh);
                   break;
               }
           }
       } else if (n > 0 && m > 1) {
           final int i = m >> 1; // Find the middle point

           final CharSequence left0Toi = left.subSequence(0, i);
           final CharSequence leftiTom = left.subSequence(i, m);

           final int[] l1 = algorithmB(left0Toi, i, right, n);
           final int[] l2 = algorithmB(reverseSequence(leftiTom), m - i, reverseSequence(right), n);

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

           // Solve simpler problems
           final CharSequence c1 = algorithmC(left0Toi, i, right.subSequence(0, k), k);
           final CharSequence c2 = algorithmC(leftiTom, m - i, right.subSequence(k, n), n - k);
           sb.append(c1).append(c2);
      }
      return sb.toString();
   }

   // Auxiliary method for reversing a sequence
   static CharSequence reverseSequence(final CharSequence sequence) {
       return new StringBuilder(sequence).reverse().toString();
   }

    /**
     *
     * Computes the lcsLengthArray for the sake of doing the actual lcs calculation. This is the
     * dynamic programming portion of the algorithm, and is the reason for the runtime complexity being
     * O(m*n), where m=left.length() and n=right.length().
     *
     * @param left first character sequence
     * @param right second character sequence
     * @return lcsLengthArray
     */
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
