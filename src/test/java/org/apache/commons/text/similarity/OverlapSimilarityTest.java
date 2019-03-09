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

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * Unit tests for {@link OverlapSimilarity}.
 */
public class OverlapSimilarityTest {
    @Test
    public void testOverlapUsingSetCharacter() {
        // Compute using single characters.
        // This test uses a set and so should not allow duplicates.
        final Function<CharSequence, Collection<Character>> converter = cs -> {
            final int length = cs.length();
            final Set<Character> set = new HashSet<>(length);
            for (int i = 0; i < length; i++) {
                set.add(cs.charAt(i));
            }
            return set;
        };
        final OverlapSimilarity<Character> similarity = new OverlapSimilarity<>(converter);

        // Expected:
        // size A or B = count of unique characters (exclude duplicates)
        // intersection = count of unique matching characters (exclude duplicates)
        // union = count of unique characters in total (exclude duplicates)
        assertOverlap(similarity, "", "", 0, 0, 0, 0);
        assertOverlap(similarity, "a", "", 1, 0, 0, 1);
        assertOverlap(similarity, "a", "a", 1, 1, 1, 1);
        assertOverlap(similarity, "a", "b", 1, 1, 0, 2);
        assertOverlap(similarity, "aa", "ab", 1, 2, 1, 2);
        assertOverlap(similarity, "ab", "ab", 2, 2, 2, 2);
        assertOverlap(similarity, "aaba", "abaa", 2, 2, 2, 2);
        assertOverlap(similarity, "aaaa", "aa", 1, 1, 1, 1);
        assertOverlap(similarity, "aaaa", "aaa", 1, 1, 1, 1);
        assertOverlap(similarity, "aabab", "ababa", 2, 2, 2, 2);
        assertOverlap(similarity, "the same", "the same", 7, 7, 7, 7);
        assertOverlap(similarity, "abcdefghijklm", "ab_defg ijklm", 13, 13, 11, 15);
    }

    @Test
    public void testOverlapUsingListCharacter() {
        // Compute using single characters.
        // This test uses a list and so duplicates should be matched.
        final Function<CharSequence, Collection<Character>> converter = cs -> {
            final int length = cs.length();
            final List<Character> list = new ArrayList<>(length);
            for (int i = 0; i < length; i++) {
                list.add(cs.charAt(i));
            }
            return list;
        };
        final OverlapSimilarity<Character> similarity = new OverlapSimilarity<>(converter);

        // Expected:
        // size A or B = sequence length
        // intersection = count of matching characters (include duplicates)
        // union = count of matching characters (include duplicates) plus unmatched
        //       = size A + size B - intersection
        assertOverlap(similarity, "", "", 0, 0, 0, 0);
        assertOverlap(similarity, "a", "", 1, 0, 0, 1);
        assertOverlap(similarity, "a", "a", 1, 1, 1, 1);
        assertOverlap(similarity, "a", "b", 1, 1, 0, 2);
        assertOverlap(similarity, "aa", "ab", 2, 2, 1, 3);
        assertOverlap(similarity, "ab", "ab", 2, 2, 2, 2);
        assertOverlap(similarity, "aaba", "abaa", 4, 4, 4, 4);
        assertOverlap(similarity, "aaaa", "aa", 4, 2, 2, 4);
        assertOverlap(similarity, "aaaa", "aaa", 4, 3, 3, 4);
        assertOverlap(similarity, "aabab", "ababa", 5, 5, 5, 5);
        assertOverlap(similarity, "the same", "the same", 8, 8, 8, 8);
        assertOverlap(similarity, "abcdefghijklm", "ab_defg ijklm", 13, 13, 11, 15);
    }

    @Test
    public void testOverlapUsingSetBigrams() {
        // Compute using pairs of characters (bigrams).
        // This can be done using a 32-bit int to store two 16-bit characters.
        // This test uses a set and so should not allow duplicates.
        final Function<CharSequence, Collection<Integer>> converter = cs -> {
            final int length = cs.length();
            final Set<Integer> set = new HashSet<>(length);
            if (length > 1) {
                char ch2 = cs.charAt(0);
                for (int i = 1; i < length; i++) {
                    final char ch1 = ch2;
                    ch2 = cs.charAt(i);
                    set.add(Integer.valueOf((ch1 << 16) | ch2));
                }
            }
            return set;
        };
        final OverlapSimilarity<Integer> similarity = new OverlapSimilarity<>(converter);

        // Expected:
        // size A or B = count of unique bigrams (exclude duplicates)
        // intersection = count of unique matching bigrams (exclude duplicates)
        // union = count of unique bigrams in total (exclude duplicates)
        assertOverlap(similarity, "", "", 0, 0, 0, 0);
        assertOverlap(similarity, "a", "", 0, 0, 0, 0);
        assertOverlap(similarity, "a", "a", 0, 0, 0, 0);
        assertOverlap(similarity, "a", "b", 0, 0, 0, 0);
        assertOverlap(similarity, "aa", "ab", 1, 1, 0, 2);
        assertOverlap(similarity, "ab", "ab", 1, 1, 1, 1);
        assertOverlap(similarity, "aaba", "abaa", 3, 3, 3, 3);
        assertOverlap(similarity, "aaaa", "aa", 1, 1, 1, 1);
        assertOverlap(similarity, "aaaa", "aaa", 1, 1, 1, 1);
        assertOverlap(similarity, "aabab", "ababa", 3, 2, 2, 3);
        assertOverlap(similarity, "the same", "the same", 7, 7, 7, 7);
        assertOverlap(similarity, "abcdefghijklm", "ab_defg ijklm", 12, 12, 8, 16);
    }

    @Test
    public void testOverlapUsingListBigrams() {
        // Compute using pairs of characters (bigrams).
        // This can be done using a 32-bit int to store two 16-bit characters.
        // This test uses a list and so duplicates should be matched.
        final Function<CharSequence, Collection<Integer>> converter = cs -> {
            final int length = cs.length();
            final List<Integer> list = new ArrayList<>(length);
            if (length > 1) {
                char ch2 = cs.charAt(0);
                for (int i = 1; i < length; i++) {
                    final char ch1 = ch2;
                    ch2 = cs.charAt(i);
                    list.add(Integer.valueOf((ch1 << 16) | ch2));
                }
            }
            return list;
        };
        final OverlapSimilarity<Integer> similarity = new OverlapSimilarity<>(converter);

        // Expected:
        // size A or B = sequence length - 1
        // intersection = count of matching bigrams (include duplicates)
        // union = count of matching bigrams (include duplicates)
        //       = size A + size B - intersection
        assertOverlap(similarity, "", "", 0, 0, 0, 0);
        assertOverlap(similarity, "a", "", 0, 0, 0, 0);
        assertOverlap(similarity, "a", "a", 0, 0, 0, 0);
        assertOverlap(similarity, "a", "b", 0, 0, 0, 0);
        assertOverlap(similarity, "aa", "ab", 1, 1, 0, 2);
        assertOverlap(similarity, "ab", "ab", 1, 1, 1, 1);
        assertOverlap(similarity, "aaba", "abaa", 3, 3, 3, 3);
        assertOverlap(similarity, "aaaa", "aa", 3, 1, 1, 3);
        assertOverlap(similarity, "aaaa", "aaa", 3, 2, 2, 3);
        assertOverlap(similarity, "aabab", "ababa", 4, 4, 3, 5);
        assertOverlap(similarity, "the same", "the same", 7, 7, 7, 7);
        assertOverlap(similarity, "abcdefghijklm", "ab_defg ijklm", 12, 12, 8, 16);
    }

    private static <T> void assertOverlap(OverlapSimilarity<T> similarity, CharSequence cs1, CharSequence cs2, 
            int sizeA, int sizeB, int intersection, int union) {
        OverlapResult overlap = similarity.apply(cs1, cs2);
        assertEquals(sizeA, overlap.getSizeA(), "Size A error");
        assertEquals(sizeB, overlap.getSizeB(), "Size B error");
        assertEquals(intersection, overlap.getIntersection(), "Intersection error");
        assertEquals(union, overlap.getUnion(), "Union error");
    }

    @Test
    public void testF1ScoreUsingListWordBigrams() {
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
            for (String word : pattern.split(cs)) {
                if (word.length() > 1) {
                    // The strings are converted to upper case
                    char ch2 = Character.toUpperCase(word.charAt(0));
                    for (int i = 1; i < word.length(); i++) {
                        final char ch1 = ch2;
                        ch2 = Character.toUpperCase(word.charAt(i));
                        set.add(Integer.valueOf((ch1 << 16) | ch2));
                    }
                }
            }
            return set;
        };
        final OverlapSimilarity<Integer> similarity = new OverlapSimilarity<>(converter);

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

    private static int toF1ScorePercent(OverlapResult overlap) {
        final double value = 2.0 * overlap.getIntersection() / (overlap.getSizeA() + overlap.getSizeB());
        // Convert to percentage
        return (int) Math.round(value * 100);
    }

    @Test
    public void testConstructorWithNullConverterThrows() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            new OverlapSimilarity<>(null);
        });
    }

    @Test
    public void testApplyNullNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            new OverlapSimilarity<>(cs -> new HashSet<>(Arrays.asList(cs))).apply(null, null);
        });
    }

    @Test
    public void testApplyStringNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            new OverlapSimilarity<>(cs -> new HashSet<>(Arrays.asList(cs))).apply("left", null);
        });
    }

    @Test
    public void testApplyNullString() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            new OverlapSimilarity<>(cs -> new HashSet<>(Arrays.asList(cs))).apply(null, "right");
        });
    }
}
