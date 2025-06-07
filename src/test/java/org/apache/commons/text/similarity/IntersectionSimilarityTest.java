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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link IntersectionSimilarity}.
 */
public class IntersectionSimilarityTest {
    private static <T> void assertIntersection(final IntersectionSimilarity<T> similarity, final CharSequence cs1, final CharSequence cs2, final int sizeA,
            final int sizeB, final int intersection) {
        final IntersectionResult result = similarity.apply(cs1, cs2);
        assertEquals(sizeA, result.getSizeA(), "Size A error");
        assertEquals(sizeB, result.getSizeB(), "Size B error");
        assertEquals(intersection, result.getIntersection(), "Intersection error");
    }

    /**
     * Convert the {@link CharSequence} to a {@link List} of bigrams (pairs of characters). These are represented using 2 16-bit chars packed into a 32-bit int.
     *
     * @param sequence the sequence
     * @return the list
     */
    private static List<Integer> toBigramList(final CharSequence sequence) {
        final int length = sequence.length();
        final List<Integer> list = new ArrayList<>(length);
        if (length > 1) {
            char ch2 = sequence.charAt(0);
            for (int i = 1; i < length; i++) {
                final char ch1 = ch2;
                ch2 = sequence.charAt(i);
                list.add(Integer.valueOf(ch1 << 16 | ch2));
            }
        }
        return list;
    }

    /**
     * Convert the {@link CharSequence} to a {@link Set} of bigrams (pairs of characters). These are represented using 2 16-bit chars packed into a 32-bit int.
     *
     * @param sequence the sequence
     * @return the set
     */
    private static Set<Integer> toBigramSet(final CharSequence sequence) {
        final int length = sequence.length();
        final Set<Integer> set = new HashSet<>(length);
        if (length > 1) {
            char ch2 = sequence.charAt(0);
            for (int i = 1; i < length; i++) {
                final char ch1 = ch2;
                ch2 = sequence.charAt(i);
                set.add(Integer.valueOf(ch1 << 16 | ch2));
            }
        }
        return set;
    }

    /**
     * Convert the {@link CharSequence} to a {@link List} of {@link Character}s.
     *
     * @param sequence the sequence
     * @return the list
     */
    private static List<Character> toCharacterList(final CharSequence sequence) {
        final int length = sequence.length();
        final List<Character> list = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            list.add(sequence.charAt(i));
        }
        return list;
    }

    /**
     * Convert the {@link CharSequence} to a {@link Set} of {@link Character}s.
     *
     * @param sequence the sequence
     * @return the set
     */
    private static Set<Character> toCharacterSet(final CharSequence sequence) {
        final int length = sequence.length();
        final Set<Character> set = new HashSet<>(length);
        for (int i = 0; i < length; i++) {
            set.add(sequence.charAt(i));
        }
        return set;
    }

    private static int toF1ScorePercent(final IntersectionResult result) {
        final double value = 2.0 * result.getIntersection() / (result.getSizeA() + result.getSizeB());
        // Convert to percentage
        return (int) Math.round(value * 100);
    }

    @Test
    void testApplyNullNull() {
        assertThrows(IllegalArgumentException.class, () -> new IntersectionSimilarity<>(cs -> new HashSet<>(Collections.singletonList(cs))).apply(null, null));
    }

    @Test
    void testApplyNullString() {
        assertThrows(IllegalArgumentException.class,
                () -> new IntersectionSimilarity<>(cs -> new HashSet<>(Collections.singletonList(cs))).apply(null, "right"));
    }

    @Test
    void testApplyStringNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new IntersectionSimilarity<>(cs -> new HashSet<>(Collections.singletonList(cs))).apply("left", null));
    }

    @Test
    void testConstructorWithNullConverterThrows() {
        assertThrows(IllegalArgumentException.class, () -> new IntersectionSimilarity<>(null));
    }

    @Test
    void testF1ScoreUsingListWordBigrams() {
        // Example of a word letter pairs algorithm by Simon White:
        // http://www.catalysoft.com/articles/StrikeAMatch.html
        // This splits into words using whitespace and then computes uppercase
        // bigrams for each word.

        // Split on whitespace
        final Pattern pattern = Pattern.compile("\\s+");

        // Compute using pairs of characters (bigrams) for each word.
        // This can be done using a 32-bit int to store two 16-bit characters
        final Function<CharSequence, Collection<Integer>> converter = cs -> {
            final List<Integer> set = new ArrayList<>();
            for (final String word : pattern.split(cs)) {
                if (word.length() > 1) {
                    // The strings are converted to upper case
                    char ch2 = Character.toUpperCase(word.charAt(0));
                    for (int i = 1; i < word.length(); i++) {
                        final char ch1 = ch2;
                        ch2 = Character.toUpperCase(word.charAt(i));
                        set.add(Integer.valueOf(ch1 << 16 | ch2));
                    }
                }
            }
            return set;
        };
        final IntersectionSimilarity<Integer> similarity = new IntersectionSimilarity<>(converter);

        String bookTitle;
        final String search1 = "Web Database Applications";
        final String search2 = "PHP Web Applications";
        final String search3 = "Web Aplications";
        bookTitle = "Web Database Applications with PHP & MySQL";
        assertEquals(82, toF1ScorePercent(similarity.apply(bookTitle, search1)));
        assertEquals(68, toF1ScorePercent(similarity.apply(bookTitle, search2)));
        assertEquals(59, toF1ScorePercent(similarity.apply(bookTitle, search3)));
        bookTitle = "Creating Database Web Applications with PHP and ASP";
        assertEquals(71, toF1ScorePercent(similarity.apply(bookTitle, search1)));
        assertEquals(59, toF1ScorePercent(similarity.apply(bookTitle, search2)));
        assertEquals(50, toF1ScorePercent(similarity.apply(bookTitle, search3)));
        bookTitle = "Building Database Applications on the Web Using PHP3";
        assertEquals(70, toF1ScorePercent(similarity.apply(bookTitle, search1)));
        assertEquals(58, toF1ScorePercent(similarity.apply(bookTitle, search2)));
        assertEquals(49, toF1ScorePercent(similarity.apply(bookTitle, search3)));
        bookTitle = "Building Web Database Applications with Visual Studio 6";
        assertEquals(67, toF1ScorePercent(similarity.apply(bookTitle, search1)));
        assertEquals(47, toF1ScorePercent(similarity.apply(bookTitle, search2)));
        assertEquals(46, toF1ScorePercent(similarity.apply(bookTitle, search3)));
        bookTitle = "Web Application Development With PHP";
        assertEquals(51, toF1ScorePercent(similarity.apply(bookTitle, search1)));
        assertEquals(67, toF1ScorePercent(similarity.apply(bookTitle, search2)));
        assertEquals(56, toF1ScorePercent(similarity.apply(bookTitle, search3)));
        bookTitle = "WebRAD: Building Database Applications on the Web with Visual FoxPro and Web Connection";
        assertEquals(49, toF1ScorePercent(similarity.apply(bookTitle, search1)));
        assertEquals(34, toF1ScorePercent(similarity.apply(bookTitle, search2)));
        assertEquals(32, toF1ScorePercent(similarity.apply(bookTitle, search3)));
        bookTitle = "Structural Assessment: The Role of Large and Full-Scale Testing";
        assertEquals(12, toF1ScorePercent(similarity.apply(bookTitle, search1)));
        assertEquals(7, toF1ScorePercent(similarity.apply(bookTitle, search2)));
        assertEquals(7, toF1ScorePercent(similarity.apply(bookTitle, search3)));
        bookTitle = "How to Find a Scholarship Online";
        assertEquals(10, toF1ScorePercent(similarity.apply(bookTitle, search1)));
        assertEquals(11, toF1ScorePercent(similarity.apply(bookTitle, search2)));
        assertEquals(12, toF1ScorePercent(similarity.apply(bookTitle, search3)));
    }

    @Test
    void testIntersectionUsingListBigrams() {
        // Compute using pairs of characters (bigrams).
        // This can be done using a 32-bit int to store two 16-bit characters.
        // This test uses a list and so duplicates should be matched.
        final IntersectionSimilarity<Integer> similarity = new IntersectionSimilarity<>(IntersectionSimilarityTest::toBigramList);

        // Expected:
        // size A or B = sequence length - 1
        // intersection = count of matching bigrams (include duplicates)
        assertIntersection(similarity, "", "", 0, 0, 0);
        assertIntersection(similarity, "a", "", 0, 0, 0);
        assertIntersection(similarity, "a", "a", 0, 0, 0);
        assertIntersection(similarity, "a", "b", 0, 0, 0);
        assertIntersection(similarity, "aa", "ab", 1, 1, 0);
        assertIntersection(similarity, "ab", "ab", 1, 1, 1);
        assertIntersection(similarity, "aaba", "abaa", 3, 3, 3);
        assertIntersection(similarity, "aaaa", "aa", 3, 1, 1);
        assertIntersection(similarity, "aa", "aaaa", 1, 3, 1);
        assertIntersection(similarity, "aaaa", "aaa", 3, 2, 2);
        assertIntersection(similarity, "aabab", "ababa", 4, 4, 3);
        assertIntersection(similarity, "the same", "the same", 7, 7, 7);
        assertIntersection(similarity, "abcdefghijklm", "ab_defg ijklm", 12, 12, 8);
    }

    @Test
    void testIntersectionUsingListCharacter() {
        // Compute using single characters.
        // This test uses a list and so duplicates should be matched.
        final IntersectionSimilarity<Character> similarity = new IntersectionSimilarity<>(IntersectionSimilarityTest::toCharacterList);

        // Expected:
        // size A or B = sequence length
        // intersection = count of matching characters (include duplicates)
        assertIntersection(similarity, "", "", 0, 0, 0);
        assertIntersection(similarity, "a", "", 1, 0, 0);
        assertIntersection(similarity, "a", "a", 1, 1, 1);
        assertIntersection(similarity, "a", "b", 1, 1, 0);
        assertIntersection(similarity, "aa", "ab", 2, 2, 1);
        assertIntersection(similarity, "ab", "ab", 2, 2, 2);
        assertIntersection(similarity, "aaba", "abaa", 4, 4, 4);
        assertIntersection(similarity, "aaaa", "aa", 4, 2, 2);
        assertIntersection(similarity, "aa", "aaaa", 2, 4, 2);
        assertIntersection(similarity, "aaaa", "aaa", 4, 3, 3);
        assertIntersection(similarity, "aabab", "ababa", 5, 5, 5);
        assertIntersection(similarity, "the same", "the same", 8, 8, 8);
        assertIntersection(similarity, "abcdefghijklm", "ab_defg ijklm", 13, 13, 11);
    }

    @Test
    void testIntersectionUsingSetBigrams() {
        // Compute using pairs of characters (bigrams).
        // This can be done using a 32-bit int to store two 16-bit characters.
        // This test uses a set and so should not allow duplicates.
        final IntersectionSimilarity<Integer> similarity = new IntersectionSimilarity<>(IntersectionSimilarityTest::toBigramSet);

        // Expected:
        // size A or B = count of unique bigrams (exclude duplicates)
        // intersection = count of unique matching bigrams (exclude duplicates)
        assertIntersection(similarity, "", "", 0, 0, 0);
        assertIntersection(similarity, "a", "", 0, 0, 0);
        assertIntersection(similarity, "a", "a", 0, 0, 0);
        assertIntersection(similarity, "a", "b", 0, 0, 0);
        assertIntersection(similarity, "aa", "ab", 1, 1, 0);
        assertIntersection(similarity, "ab", "ab", 1, 1, 1);
        assertIntersection(similarity, "aaba", "abaa", 3, 3, 3);
        assertIntersection(similarity, "aaaa", "aa", 1, 1, 1);
        assertIntersection(similarity, "aa", "aaaa", 1, 1, 1);
        assertIntersection(similarity, "aaaa", "aaa", 1, 1, 1);
        assertIntersection(similarity, "aabab", "ababa", 3, 2, 2);
        assertIntersection(similarity, "the same", "the same", 7, 7, 7);
        assertIntersection(similarity, "abcdefghijklm", "ab_defg ijklm", 12, 12, 8);
    }

    @Test
    void testIntersectionUsingSetCharacter() {
        // Compute using single characters.
        // This test uses a set and so should not allow duplicates.
        final IntersectionSimilarity<Character> similarity = new IntersectionSimilarity<>(IntersectionSimilarityTest::toCharacterSet);

        // Expected:
        // size A or B = count of unique characters (exclude duplicates)
        // intersection = count of unique matching characters (exclude duplicates)
        assertIntersection(similarity, "", "", 0, 0, 0);
        assertIntersection(similarity, "a", "", 1, 0, 0);
        assertIntersection(similarity, "a", "a", 1, 1, 1);
        assertIntersection(similarity, "a", "b", 1, 1, 0);
        assertIntersection(similarity, "aa", "ab", 1, 2, 1);
        assertIntersection(similarity, "ab", "ab", 2, 2, 2);
        assertIntersection(similarity, "aaba", "abaa", 2, 2, 2);
        assertIntersection(similarity, "aaaa", "aa", 1, 1, 1);
        assertIntersection(similarity, "aa", "aaaa", 1, 1, 1);
        assertIntersection(similarity, "aaaa", "aaa", 1, 1, 1);
        assertIntersection(similarity, "aabab", "ababa", 2, 2, 2);
        assertIntersection(similarity, "the same", "the same", 7, 7, 7);
        assertIntersection(similarity, "abcdefghijklm", "ab_defg ijklm", 13, 13, 11);
    }

    @Test
    void testIntersectionUsingSetCharacterListCharacter() {
        // Compute using a custom converter that returns a Set and a List.
        // This is an edge-case test.
        final HashMap<CharSequence, Collection<Character>> converter = new HashMap<>();
        final String sequence1 = "aabbccdd";
        final String sequence2 = "aaaaaabbbfffff";
        converter.put(sequence1, toCharacterSet(sequence1));
        converter.put(sequence2, toCharacterList(sequence2));
        final IntersectionSimilarity<Character> similarity = new IntersectionSimilarity<>(converter::get);

        // Expected:
        // size A = count of unique characters (exclude duplicates)
        // size B = sequence length
        // intersection = count of matching characters (exclude duplicates)
        assertIntersection(similarity, sequence1, sequence2, 4, sequence2.length(), 2);
        assertIntersection(similarity, sequence2, sequence1, sequence2.length(), 4, 2);
    }
}
