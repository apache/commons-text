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
 * A similarity algorithm indicating the percentage of matched characters between two character sequences.
 *
 * <p>
 * The Jaro measure is the weighted sum of percentage of matched characters
 * from each file and transposed characters. Winkler increased this measure
 * for matching initial characters.
 * </p>
 *
 * <p>
 * This implementation is based on the Jaro Winkler similarity algorithm
 * from <a href="http://en.wikipedia.org/wiki/Jaro%E2%80%93Winkler_distance">
 * http://en.wikipedia.org/wiki/Jaro%E2%80%93Winkler_distance</a>.
 * </p>
 *
 * <p>
 * This code has been adapted from Apache Commons Lang 3.3.
 * </p>
 */
public class JaroWinklerDistance implements SimilarityScore<Double> {

    /**
     * The default prefix length limit set to four.
     */
    private static final int PREFIX_LENGTH_LIMIT = 4;
    /**
     * Represents a failed index search.
     */
    public static final int INDEX_NOT_FOUND = -1;

    /**
     * Find the Jaro Winkler Distance which indicates the similarity score
     * between two CharSequences.
     *
     * <pre>
     * distance.apply(null, null)          = IllegalArgumentException
     * distance.apply("","")               = 0.0
     * distance.apply("","a")              = 0.0
     * distance.apply("aaapppp", "")       = 0.0
     * distance.apply("frog", "fog")       = 0.93
     * distance.apply("fly", "ant")        = 0.0
     * distance.apply("elephant", "hippo") = 0.44
     * distance.apply("hippo", "elephant") = 0.44
     * distance.apply("hippo", "zzzzzzzz") = 0.0
     * distance.apply("hello", "hallo")    = 0.88
     * distance.apply("ABC Corporation", "ABC Corp") = 0.91
     * distance.apply("D N H Enterprises Inc", "D &amp; H Enterprises, Inc.") = 0.93
     * distance.apply("My Gym Children's Fitness Center", "My Gym. Childrens Fitness") = 0.94
     * distance.apply("PENNSYLVANIA", "PENNCISYLVNIA")    = 0.9
     * </pre>
     *
     * @param left the first String, must not be null
     * @param right the second String, must not be null
     * @return result distance
     * @throws IllegalArgumentException if either String input {@code null}
     */
    @Override
    public Double apply(final CharSequence left, final CharSequence right) {
        final double defaultScalingFactor = 0.1;
        final double percentageRoundValue = 100.0;

        if (left == null || right == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }

        final double jaro = score(left, right);
        final int cl = commonPrefixLength(left, right);
        final double matchScore = Math.round((jaro + defaultScalingFactor
                * cl * (1.0 - jaro)) * percentageRoundValue) / percentageRoundValue;

        return matchScore;
    }

    /**
     * Calculates the number of characters from the beginning of the strings
     * that match exactly one-to-one, up to a maximum of four (4) characters.
     *
     * @param first The first string.
     * @param second The second string.
     * @return A number between 0 and 4.
     */
    private static int commonPrefixLength(final CharSequence first,
            final CharSequence second) {
        final int result = getCommonPrefix(first.toString(), second.toString())
                .length();

        // Limit the result to 4.
        return result > PREFIX_LENGTH_LIMIT ? PREFIX_LENGTH_LIMIT : result;
    }

    /**
     * Compares all Strings in an array and returns the initial sequence of
     * characters that is common to all of them.
     *
     * <p>
     * For example,
     * <code>getCommonPrefix(new String[] {"i am a machine", "i am a robot"}) -&gt; "i am a "</code>
     * </p>
     *
     * <pre>
     * getCommonPrefix(null) = ""
     * getCommonPrefix(new String[] {}) = ""
     * getCommonPrefix(new String[] {"abc"}) = "abc"
     * getCommonPrefix(new String[] {null, null}) = ""
     * getCommonPrefix(new String[] {"", ""}) = ""
     * getCommonPrefix(new String[] {"", null}) = ""
     * getCommonPrefix(new String[] {"abc", null, null}) = ""
     * getCommonPrefix(new String[] {null, null, "abc"}) = ""
     * getCommonPrefix(new String[] {"", "abc"}) = ""
     * getCommonPrefix(new String[] {"abc", ""}) = ""
     * getCommonPrefix(new String[] {"abc", "abc"}) = "abc"
     * getCommonPrefix(new String[] {"abc", "a"}) = "a"
     * getCommonPrefix(new String[] {"ab", "abxyz"}) = "ab"
     * getCommonPrefix(new String[] {"abcde", "abxyz"}) = "ab"
     * getCommonPrefix(new String[] {"abcde", "xyz"}) = ""
     * getCommonPrefix(new String[] {"xyz", "abcde"}) = ""
     * getCommonPrefix(new String[] {"i am a machine", "i am a robot"}) = "i am a "
     * </pre>
     *
     * @param strs array of String objects, entries may be null
     * @return the initial sequence of characters that are common to all Strings
     *         in the array; empty String if the array is null, the elements are
     *         all null or if there is no common prefix.
     */
    public static String getCommonPrefix(final String... strs) {
        if (strs == null || strs.length == 0) {
            return "";
        }
        final int smallestIndexOfDiff = indexOfDifference(strs);
        if (smallestIndexOfDiff == INDEX_NOT_FOUND) {
            // all strings were identical
            if (strs[0] == null) {
                return "";
            }
            return strs[0];
        } else if (smallestIndexOfDiff == 0) {
            // there were no common initial characters
            return "";
        } else {
            // we found a common initial character sequence
            return strs[0].substring(0, smallestIndexOfDiff);
        }
    }

    /**
     * This method returns the Jaro-Winkler score for string matching.
     *
     * @param first the first string to be matched
     * @param second the second string to be machted
     * @return matching score without scaling factor impact
     */
    protected static double score(final CharSequence first,
            final CharSequence second) {
        String shorter;
        String longer;

        // Determine which String is longer.
        if (first.length() > second.length()) {
            longer = first.toString().toLowerCase();
            shorter = second.toString().toLowerCase();
        } else {
            longer = second.toString().toLowerCase();
            shorter = first.toString().toLowerCase();
        }

        // Calculate the half length() distance of the shorter String.
        final int halflength = shorter.length() / 2 + 1;

        // Find the set of matching characters between the shorter and longer
        // strings. Note that
        // the set of matching characters may be different depending on the
        // order of the strings.
        final String m1 = getSetOfMatchingCharacterWithin(shorter, longer,
                halflength);
        final String m2 = getSetOfMatchingCharacterWithin(longer, shorter,
                halflength);

        // If one or both of the sets of common characters is empty, then
        // there is no similarity between the two strings.
        if (m1.length() == 0 || m2.length() == 0) {
            return 0.0;
        }

        // If the set of common characters is not the same size, then
        // there is no similarity between the two strings, either.
        if (m1.length() != m2.length()) {
            return 0.0;
        }

        // Calculate the number of transposition between the two sets
        // of common characters.
        final int transpositions = transpositions(m1, m2);

        final double defaultDenominator = 3.0;

        // Calculate the distance.
        final double dist = (m1.length() / ((double) shorter.length())
                + m2.length() / ((double) longer.length()) + (m1.length() - transpositions)
                / ((double) m1.length())) / defaultDenominator;
        return dist;
    }

    /**
     * Calculates the number of transposition between two strings.
     *
     * @param first The first string.
     * @param second The second string.
     * @return The number of transposition between the two strings.
     */
    protected static int transpositions(final CharSequence first,
            final CharSequence second) {
        int transpositions = 0;
        for (int i = 0; i < first.length(); i++) {
            if (first.charAt(i) != second.charAt(i)) {
                transpositions++;
            }
        }
        return transpositions / 2;
    }

    /**
     * Compares all CharSequences in an array and returns the index at which the
     * CharSequences begin to differ.
     *
     * <p>
     * For example,
     * <code>indexOfDifference(new String[] {"i am a machine", "i am a robot"}) -&gt; 7</code>
     * </p>
     *
     * <pre>
     * distance.indexOfDifference(null) = -1
     * distance.indexOfDifference(new String[] {}) = -1
     * distance.indexOfDifference(new String[] {"abc"}) = -1
     * distance.indexOfDifference(new String[] {null, null}) = -1
     * distance.indexOfDifference(new String[] {"", ""}) = -1
     * distance.indexOfDifference(new String[] {"", null}) = 0
     * distance.indexOfDifference(new String[] {"abc", null, null}) = 0
     * distance.indexOfDifference(new String[] {null, null, "abc"}) = 0
     * distance.indexOfDifference(new String[] {"", "abc"}) = 0
     * distance.indexOfDifference(new String[] {"abc", ""}) = 0
     * distance.indexOfDifference(new String[] {"abc", "abc"}) = -1
     * distance.indexOfDifference(new String[] {"abc", "a"}) = 1
     * distance.indexOfDifference(new String[] {"ab", "abxyz"}) = 2
     * distance.indexOfDifference(new String[] {"abcde", "abxyz"}) = 2
     * distance.indexOfDifference(new String[] {"abcde", "xyz"}) = 0
     * distance.indexOfDifference(new String[] {"xyz", "abcde"}) = 0
     * distance.indexOfDifference(new String[] {"i am a machine", "i am a robot"}) = 7
     * </pre>
     *
     * @param css array of CharSequences, entries may be null
     * @return the index where the strings begin to differ; -1 if they are all
     *         equal
     */
    protected static int indexOfDifference(final CharSequence... css) {
        if (css == null || css.length <= 1) {
            return INDEX_NOT_FOUND;
        }
        boolean anyStringNull = false;
        boolean allStringsNull = true;
        final int arrayLen = css.length;
        int shortestStrLen = Integer.MAX_VALUE;
        int longestStrLen = 0;

        // find the min and max string lengths; this avoids checking to make
        // sure we are not exceeding the length of the string each time through
        // the bottom loop.
        for (int i = 0; i < arrayLen; i++) {
            if (css[i] == null) {
                anyStringNull = true;
                shortestStrLen = 0;
            } else {
                allStringsNull = false;
                shortestStrLen = Math.min(css[i].length(), shortestStrLen);
                longestStrLen = Math.max(css[i].length(), longestStrLen);
            }
        }

        // handle lists containing all nulls or all empty strings
        if (allStringsNull || longestStrLen == 0 && !anyStringNull) {
            return INDEX_NOT_FOUND;
        }

        // handle lists containing some nulls or some empty strings
        if (shortestStrLen == 0) {
            return 0;
        }

        // find the position with the first difference across all strings
        int firstDiff = -1;
        for (int stringPos = 0; stringPos < shortestStrLen; stringPos++) {
            final char comparisonChar = css[0].charAt(stringPos);
            for (int arrayPos = 1; arrayPos < arrayLen; arrayPos++) {
                if (css[arrayPos].charAt(stringPos) != comparisonChar) {
                    firstDiff = stringPos;
                    break;
                }
            }
            if (firstDiff != -1) {
                break;
            }
        }

        if (firstDiff == -1 && shortestStrLen != longestStrLen) {
            // we compared all of the characters up to the length of the
            // shortest string and didn't find a match, but the string lengths
            // vary, so return the length of the shortest string.
            return shortestStrLen;
        }
        return firstDiff;
    }

    /**
     * Gets a set of matching characters between two strings.
     *
     * <p>
     * Two characters from the first string and the second string are
     * considered matching if the character's respective positions are no
     * farther than the limit value.
     * </p>
     *
     * @param first The first string.
     * @param second The second string.
     * @param limit The maximum distance to consider.
     * @return A string contain the set of common characters.
     */
    protected static String getSetOfMatchingCharacterWithin(
            final CharSequence first, final CharSequence second, final int limit) {
        final StringBuilder common = new StringBuilder();
        final StringBuilder copy = new StringBuilder(second);

        for (int i = 0; i < first.length(); i++) {
            final char ch = first.charAt(i);
            boolean found = false;

            // See if the character is within the limit positions away from the
            // original position of that character.
            for (int j = Math.max(0, i - limit); !found
                    && j < Math.min(i + limit, second.length()); j++) {
                if (copy.charAt(j) == ch) {
                    found = true;
                    common.append(ch);
                    copy.setCharAt(j, '*');
                }
            }
        }
        return common.toString();
    }

}
