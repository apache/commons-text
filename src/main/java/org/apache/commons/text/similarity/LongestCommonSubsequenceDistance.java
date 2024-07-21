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
 * An edit distance algorithm based on the length of the longest common subsequence between two strings.
 *
 * <p>
 * This code is directly based upon the implementation in {@link LongestCommonSubsequence}.
 * </p>
 *
 * <p>
 * For reference see: <a href="https://en.wikipedia.org/wiki/Longest_common_subsequence_problem">
 * https://en.wikipedia.org/wiki/Longest_common_subsequence_problem</a>.
 * </p>
 *
 * <p>For further reading see:</p>
 *
 * <p>Lothaire, M. <em>Applied combinatorics on words</em>. New York: Cambridge U Press, 2005. <b>12-13</b></p>
 *
 * @since 1.0
 */
public class LongestCommonSubsequenceDistance implements EditDistance<Integer> {

    /**
     * Calculates an edit distance between two {@code CharSequence}'s {@code left} and
     * {@code right} as: {@code left.length() + right.length() - 2 * LCS(left, right)}, where
     * {@code LCS} is given in {@link LongestCommonSubsequence#apply(CharSequence, CharSequence)}.
     *
     * @param left first character sequence
     * @param right second character sequence
     * @return distance
     * @throws IllegalArgumentException
     *             if either String input {@code null}
     */
    @Override
    public Integer apply(final CharSequence left, final CharSequence right) {
        // Quick return for invalid inputs
        if (left == null || right == null) {
            throw new IllegalArgumentException("Inputs must not be null");
        }
        return left.length() + right.length() - 2 * LongestCommonSubsequence.INSTANCE.apply(left, right);
    }

}
