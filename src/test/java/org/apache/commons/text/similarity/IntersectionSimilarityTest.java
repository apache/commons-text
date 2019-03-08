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
 * Unit tests for {@link IntersectionSimilarity}.
 */
public class IntersectionSimilarityTest {
    @Test
    public void testJaccardIndexUsingSetCharacter() {
        // Match the functionality of the JaccardSimilarity class by dividing
        // the sequence into single characters
        final Function<CharSequence, Collection<Character>> converter = cs -> {
            final int length = cs.length();
            final Set<Character> set = new HashSet<>(length);
            for (int i = 0; i < length; i++) {
                set.add(cs.charAt(i));
            }
            return set;
        };
        final IntersectionSimilarity<Character> similarity = new IntersectionSimilarity<>(converter);

        // Expected Jaccard index = (intersect / union)
        // intersection = count of unique matching characters (exclude duplicates)
        // union = count of unique characters
        assertEquals(0.0, similarity.apply("", "").getJaccardIndex());
        assertEquals(0.0, similarity.apply("left", "").getJaccardIndex());
        assertEquals(0.0, similarity.apply("", "right").getJaccardIndex());
        assertEquals(3.0 / 4, similarity.apply("frog", "fog").getJaccardIndex());
        assertEquals(0.0, similarity.apply("fly", "ant").getJaccardIndex());
        assertEquals(2.0 / 9, similarity.apply("elephant", "hippo").getJaccardIndex());
        assertEquals(7.0 / 11, similarity.apply("ABC Corporation", "ABC Corp").getJaccardIndex());
        assertEquals(13.0 / 17, similarity.apply("D N H Enterprises Inc", "D & H Enterprises, Inc.").getJaccardIndex());
        assertEquals(16.0 / 18,
            similarity.apply("My Gym Children's Fitness Center", "My Gym. Childrens Fitness").getJaccardIndex());
        assertEquals(9.0 / 10, similarity.apply("PENNSYLVANIA", "PENNCISYLVNIA").getJaccardIndex());
        assertEquals(1.0 / 8, similarity.apply("left", "right").getJaccardIndex());
        assertEquals(1.0 / 8, similarity.apply("leettteft", "ritttght").getJaccardIndex());
        assertEquals(1.0, similarity.apply("the same string", "the same string").getJaccardIndex());
    }

    @Test
    public void testJaccardIndexUsingListCharacter() {
        // This test uses a list and so duplicates should be matched
        final Function<CharSequence, Collection<Character>> converter = cs -> {
            final int length = cs.length();
            final List<Character> list = new ArrayList<>(length);
            for (int i = 0; i < length; i++) {
                list.add(cs.charAt(i));
            }
            return list;
        };
        final IntersectionSimilarity<Character> similarity = new IntersectionSimilarity<>(converter);

        // Expected Jaccard index = (intersect / union)
        // intersection = count of matching characters including duplicates
        // union = left.length() + right.length() - intersection
        assertEquals(0.0, similarity.apply("", "").getJaccardIndex());
        assertEquals(0.0, similarity.apply("left", "").getJaccardIndex());
        assertEquals(0.0, similarity.apply("", "right").getJaccardIndex());
        assertEquals(3.0 / (4 + 3 - 3), similarity.apply("frog", "fog").getJaccardIndex());
        assertEquals(0.0, similarity.apply("fly", "ant").getJaccardIndex());
        assertEquals(2.0 / (8 + 5 - 2), similarity.apply("elephant", "hippo").getJaccardIndex());
        assertEquals(8.0 / (15 + 8 - 8), similarity.apply("ABC Corporation", "ABC Corp").getJaccardIndex());
        assertEquals(20.0 / (21 + 23 - 20),
            similarity.apply("D N H Enterprises Inc", "D & H Enterprises, Inc.").getJaccardIndex());
        assertEquals(24.0 / (32 + 25 - 24),
            similarity.apply("My Gym Children's Fitness Center", "My Gym. Childrens Fitness").getJaccardIndex());
        assertEquals(11.0 / (12 + 13 - 11), similarity.apply("PENNSYLVANIA", "PENNCISYLVNIA").getJaccardIndex());
        assertEquals(1.0 / (4 + 5 - 1), similarity.apply("left", "right").getJaccardIndex());
        assertEquals(4.0 / (9 + 8 - 4), similarity.apply("leettteft", "ritttght").getJaccardIndex());
        assertEquals(1.0, similarity.apply("the same string", "the same string").getJaccardIndex());
    }

    @Test
    public void testSorensenDiceCoefficientUsingSetBigrams() {
        // Compute using pairs of characters (bigrams).
        // This can be done using a 32-bit int to store two 16-bit characters
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
        final IntersectionSimilarity<Integer> similarity = new IntersectionSimilarity<>(converter);

        // Expected Sorensen-Dice = 2 * intersection / (|A| + |B|)
        // intersection = count of unique matching bigrams (exclude duplicates)
        // |A| = count of unique bigrams in A
        // |B| = count of unique bigrams in B
        assertEquals(2.0 * 1 / (1 + 1), similarity.apply("aa", "aa").getSorensenDiceCoefficient());
        assertEquals(2.0 * 1 / (1 + 1), similarity.apply("aaa", "aa").getSorensenDiceCoefficient());
        assertEquals(2.0 * 1 / (1 + 1), similarity.apply("aaaa", "aa").getSorensenDiceCoefficient());
        assertEquals(2.0 * 1 / (1 + 1), similarity.apply("aaaa", "aaa").getSorensenDiceCoefficient());
        assertEquals(2.0 * 2 / (3 + 2), similarity.apply("abaa", "aba").getSorensenDiceCoefficient());
        assertEquals(2.0 * 2 / (2 + 2), similarity.apply("ababa", "aba").getSorensenDiceCoefficient());
        assertEquals(2.0 * 2 / (2 + 2), similarity.apply("ababa", "abab").getSorensenDiceCoefficient());

        assertEquals(0.0, similarity.apply("", "").getSorensenDiceCoefficient());
        assertEquals(0.0, similarity.apply("", "a").getSorensenDiceCoefficient());
        assertEquals(0.0, similarity.apply("a", "").getSorensenDiceCoefficient());
        assertEquals(0.0, similarity.apply("a", "a").getSorensenDiceCoefficient());
        assertEquals(0.0, similarity.apply("a", "b").getSorensenDiceCoefficient());
        assertEquals(1.0, similarity.apply("foo", "foo").getSorensenDiceCoefficient());
        assertEquals(2.0 * 2 / (2 + 3), similarity.apply("foo", "foo ").getSorensenDiceCoefficient());
        assertEquals(2.0 * 1 / (3 + 2), similarity.apply("frog", "fog").getSorensenDiceCoefficient());
        assertEquals(0.0, similarity.apply("fly", "ant").getSorensenDiceCoefficient());
        assertEquals(0.0, similarity.apply("elephant", "hippo").getSorensenDiceCoefficient());
        assertEquals(0.0, similarity.apply("hippo", "elephant").getSorensenDiceCoefficient());
        assertEquals(0.0, similarity.apply("hippo", "zzzzzzzz").getSorensenDiceCoefficient());
        assertEquals(2.0 * 2 / (4 + 4), similarity.apply("hello", "hallo").getSorensenDiceCoefficient());
        assertEquals(2.0 * 7 / (13 + 7), similarity.apply("ABC Corporation", "ABC Corp").getSorensenDiceCoefficient());
        assertEquals(2.0 * 17 / (20 + 26),
            similarity.apply("D N H Enterprises Inc", "D &amp; H Enterprises, Inc.").getSorensenDiceCoefficient());
        assertEquals(2.0 * 21 / (28 + 24), similarity
            .apply("My Gym Children's Fitness Center", "My Gym. Childrens Fitness").getSorensenDiceCoefficient());
        assertEquals(2.0 * 8 / (11 + 12),
            similarity.apply("PENNSYLVANIA", "PENNCISYLVNIA").getSorensenDiceCoefficient());
        assertEquals(2.0 * 12 / (13 + 13),
            similarity.apply("/opt/software1", "/opt/software2").getSorensenDiceCoefficient());
        assertEquals(2.0 * 2 / (4 + 4), similarity.apply("aaabcd", "aaacdb").getSorensenDiceCoefficient());
        assertEquals(2.0 * 6 / (8 + 11), similarity.apply("John Horn", "John Hopkins").getSorensenDiceCoefficient());
    }

    @Test
    public void testSorensenDiceCoefficientUsingListBigrams() {
        // Compute using pairs of characters (bigrams).
        // This can be done using a 32-bit int to store two 16-bit characters
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
        final IntersectionSimilarity<Integer> similarity = new IntersectionSimilarity<>(converter);

        // Expected Sorensen-Dice = 2 * intersection / (|A| + |B|)
        // intersection = count of matching bigrams including duplicates
        // |A| = max(0, left.length() - 1)
        // |B| = max(0, right.length() - 1)
        assertEquals(2.0 * 1 / (1 + 1), similarity.apply("aa", "aa").getSorensenDiceCoefficient());
        assertEquals(2.0 * 1 / (2 + 1), similarity.apply("aaa", "aa").getSorensenDiceCoefficient());
        assertEquals(2.0 * 1 / (3 + 1), similarity.apply("aaaa", "aa").getSorensenDiceCoefficient());
        assertEquals(2.0 * 2 / (3 + 2), similarity.apply("aaaa", "aaa").getSorensenDiceCoefficient());
        assertEquals(2.0 * 2 / (3 + 2), similarity.apply("abaa", "aba").getSorensenDiceCoefficient());
        assertEquals(2.0 * 2 / (4 + 2), similarity.apply("ababa", "aba").getSorensenDiceCoefficient());
        assertEquals(2.0 * 3 / (4 + 3), similarity.apply("ababa", "abab").getSorensenDiceCoefficient());

        assertEquals(0.0, similarity.apply("", "").getSorensenDiceCoefficient());
        assertEquals(0.0, similarity.apply("", "a").getSorensenDiceCoefficient());
        assertEquals(0.0, similarity.apply("a", "").getSorensenDiceCoefficient());
        assertEquals(0.0, similarity.apply("a", "a").getSorensenDiceCoefficient());
        assertEquals(0.0, similarity.apply("a", "b").getSorensenDiceCoefficient());
        assertEquals(1.0, similarity.apply("foo", "foo").getSorensenDiceCoefficient());
        assertEquals(2.0 * 2 / (2 + 3), similarity.apply("foo", "foo ").getSorensenDiceCoefficient());
        assertEquals(2.0 * 1 / (3 + 2), similarity.apply("frog", "fog").getSorensenDiceCoefficient());
        assertEquals(0.0, similarity.apply("fly", "ant").getSorensenDiceCoefficient());
        assertEquals(0.0, similarity.apply("elephant", "hippo").getSorensenDiceCoefficient());
        assertEquals(0.0, similarity.apply("hippo", "elephant").getSorensenDiceCoefficient());
        assertEquals(0.0, similarity.apply("hippo", "zzzzzzzz").getSorensenDiceCoefficient());
        assertEquals(2.0 * 2 / (4 + 4), similarity.apply("hello", "hallo").getSorensenDiceCoefficient());
        assertEquals(2.0 * 7 / (14 + 7), similarity.apply("ABC Corporation", "ABC Corp").getSorensenDiceCoefficient());
        assertEquals(2.0 * 17 / (20 + 26),
            similarity.apply("D N H Enterprises Inc", "D &amp; H Enterprises, Inc.").getSorensenDiceCoefficient());
        assertEquals(2.0 * 21 / (31 + 24), similarity
            .apply("My Gym Children's Fitness Center", "My Gym. Childrens Fitness").getSorensenDiceCoefficient());
        assertEquals(2.0 * 8 / (11 + 12),
            similarity.apply("PENNSYLVANIA", "PENNCISYLVNIA").getSorensenDiceCoefficient());
        assertEquals(2.0 * 12 / (13 + 13),
            similarity.apply("/opt/software1", "/opt/software2").getSorensenDiceCoefficient());
        assertEquals(2.0 * 3 / (5 + 5), similarity.apply("aaabcd", "aaacdb").getSorensenDiceCoefficient());
        assertEquals(2.0 * 6 / (8 + 11), similarity.apply("John Horn", "John Hopkins").getSorensenDiceCoefficient());
    }

    @Test
    public void testSorensenDiceCoefficientUsingListWordBigrams() {
        // Example of a word letter pairs algorithm:
        // http://www.catalysoft.com/articles/StrikeAMatch.html

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
        final IntersectionSimilarity<Integer> similarity = new IntersectionSimilarity<>(converter);

        String bookTitle;
        final String search1 = "Web Database Applications";
        final String search2 = "PHP Web Applications";
        final String search3 = "Web Aplications";
        bookTitle = "Web Database Applications with PHP & MySQL";
        assertEquals(82, toPercent(similarity.apply(bookTitle, search1).getSorensenDiceCoefficient()));
        assertEquals(68, toPercent(similarity.apply(bookTitle, search2).getSorensenDiceCoefficient()));
        assertEquals(59, toPercent(similarity.apply(bookTitle, search3).getSorensenDiceCoefficient()));
        bookTitle = "Creating Database Web Applications with PHP and ASP";
        assertEquals(71, toPercent(similarity.apply(bookTitle, search1).getSorensenDiceCoefficient()));
        assertEquals(59, toPercent(similarity.apply(bookTitle, search2).getSorensenDiceCoefficient()));
        assertEquals(50, toPercent(similarity.apply(bookTitle, search3).getSorensenDiceCoefficient()));
        bookTitle = "Building Database Applications on the Web Using PHP3";
        assertEquals(70, toPercent(similarity.apply(bookTitle, search1).getSorensenDiceCoefficient()));
        assertEquals(58, toPercent(similarity.apply(bookTitle, search2).getSorensenDiceCoefficient()));
        assertEquals(49, toPercent(similarity.apply(bookTitle, search3).getSorensenDiceCoefficient()));
        bookTitle = "Building Web Database Applications with Visual Studio 6";
        assertEquals(67, toPercent(similarity.apply(bookTitle, search1).getSorensenDiceCoefficient()));
        assertEquals(47, toPercent(similarity.apply(bookTitle, search2).getSorensenDiceCoefficient()));
        assertEquals(46, toPercent(similarity.apply(bookTitle, search3).getSorensenDiceCoefficient()));
        bookTitle = "Web Application Development With PHP";
        assertEquals(51, toPercent(similarity.apply(bookTitle, search1).getSorensenDiceCoefficient()));
        assertEquals(67, toPercent(similarity.apply(bookTitle, search2).getSorensenDiceCoefficient()));
        assertEquals(56, toPercent(similarity.apply(bookTitle, search3).getSorensenDiceCoefficient()));
        bookTitle = "WebRAD: Building Database Applications on the Web with Visual FoxPro and Web Connection";
        assertEquals(49, toPercent(similarity.apply(bookTitle, search1).getSorensenDiceCoefficient()));
        assertEquals(34, toPercent(similarity.apply(bookTitle, search2).getSorensenDiceCoefficient()));
        assertEquals(32, toPercent(similarity.apply(bookTitle, search3).getSorensenDiceCoefficient()));
        bookTitle = "Structural Assessment: The Role of Large and Full-Scale Testing";
        assertEquals(12, toPercent(similarity.apply(bookTitle, search1).getSorensenDiceCoefficient()));
        assertEquals(7, toPercent(similarity.apply(bookTitle, search2).getSorensenDiceCoefficient()));
        assertEquals(7, toPercent(similarity.apply(bookTitle, search3).getSorensenDiceCoefficient()));
        bookTitle = "How to Find a Scholarship Online";
        assertEquals(10, toPercent(similarity.apply(bookTitle, search1).getSorensenDiceCoefficient()));
        assertEquals(11, toPercent(similarity.apply(bookTitle, search2).getSorensenDiceCoefficient()));
        assertEquals(12, toPercent(similarity.apply(bookTitle, search3).getSorensenDiceCoefficient()));
    }

    private static int toPercent(double value) {
        return (int) Math.round(value * 100);
    }

    @Test
    public void testConstructorWithNullConverterThrows() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            new IntersectionSimilarity<>(null);
        });
    }

    @Test
    public void testApplyNullNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            new IntersectionSimilarity<>(cs -> new HashSet<>(Arrays.asList(cs))).apply(null, null);
        });
    }

    @Test
    public void testApplyStringNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            new IntersectionSimilarity<>(cs -> new HashSet<>(Arrays.asList(cs))).apply("left", null);
        });
    }

    @Test
    public void testApplyNullString() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            new IntersectionSimilarity<>(cs -> new HashSet<>(Arrays.asList(cs))).apply(null, "right");
        });
    }
}
