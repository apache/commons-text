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
package org.apache.commons.text.jmh;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.text.similarity.LongestCommonSubsequence;
import org.apache.commons.text.similarity.SimilarityScore;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

/**
 * Performance analysis for LongestCommonSubsequence
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(value = 1, jvmArgs = {"-server", "-Xms512M", "-Xmx512M"})
public class LongestCommonSubsequencePerformance {
    /**
     * Older implementation of LongestCommonSubsequence.
     * Code is copied from Apache Commons Text version 1.10.0-SNAPSHOT
     */
    private static final class BaselineLongestCommonSubsequence implements SimilarityScore<Integer> {
        @Override
        public Integer apply(final CharSequence left, final CharSequence right) {
            if (left == null || right == null) {
                throw new IllegalArgumentException("Inputs must not be null");
            }
            return longestCommonSubsequence(left, right).length();
        }

        public CharSequence longestCommonSubsequence(final CharSequence left, final CharSequence right) {
            if (left == null || right == null) {
                throw new IllegalArgumentException("Inputs must not be null");
            }
            final StringBuilder longestCommonSubstringArray = new StringBuilder(Math.max(left.length(), right.length()));
            final int[][] lcsLengthArray = longestCommonSubstringLengthArray(left, right);
            int i = left.length() - 1;
            int j = right.length() - 1;
            int k = lcsLengthArray[left.length()][right.length()] - 1;
            while (k >= 0) {
                if (left.charAt(i) == right.charAt(j)) {
                    longestCommonSubstringArray.append(left.charAt(i));
                    i = i - 1;
                    j = j - 1;
                    k = k - 1;
                } else if (lcsLengthArray[i + 1][j] < lcsLengthArray[i][j + 1]) {
                    i = i - 1;
                } else {
                    j = j - 1;
                }
            }
            return longestCommonSubstringArray.reverse().toString();
        }

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

    @State(Scope.Benchmark)
    public static class InputData {
        final List<Pair<CharSequence, CharSequence>> inputs = new ArrayList<>();

        @Setup(Level.Trial)
        public void setup() throws IOException {
            final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            CSVFormat.DEFAULT.builder().setCommentMarker('#').setTrim(true).get()
                    .parse(new InputStreamReader(
                            Objects.requireNonNull(classLoader.getResourceAsStream("org/apache/commons/text/lcs-perf-analysis-inputs.csv"))))
                    .forEach(record -> {
                        final String line = record.get(0);
                        final int indexOfComma = line.indexOf(',');
                        if (indexOfComma < 0) {
                            throw new IllegalStateException("Invalid input line: " + line);
                        }
                        final String inputA = line.substring(0, indexOfComma);
                        final String inputB = line.substring(1 + indexOfComma);
                        this.inputs.add(ImmutablePair.of(inputA, inputB));
                    });
        }
    }

    @Benchmark
    public void testLCS(final InputData data) {
        final LongestCommonSubsequence lcs = new LongestCommonSubsequence();
        for (final Pair<CharSequence, CharSequence> input : data.inputs) {
            lcs.longestCommonSubsequence(input.getLeft(), input.getRight());
        }
    }

    @Benchmark
    public void testLCSBaseline(final InputData data) {
        final BaselineLongestCommonSubsequence lcs = new BaselineLongestCommonSubsequence();
        for (final Pair<CharSequence, CharSequence> input : data.inputs) {
            lcs.longestCommonSubsequence(input.getLeft(), input.getRight());
        }
    }

    @Benchmark
    public void testLCSLen(final InputData data) {
        final LongestCommonSubsequence lcs = new LongestCommonSubsequence();
        for (final Pair<CharSequence, CharSequence> input : data.inputs) {
            lcs.apply(input.getLeft(), input.getRight());
        }
    }

    @Benchmark
    public void testLCSLenBaseline(final InputData data) {
        final BaselineLongestCommonSubsequence lcs = new BaselineLongestCommonSubsequence();
        for (final Pair<CharSequence, CharSequence> input : data.inputs) {
            lcs.apply(input.getLeft(), input.getRight());
        }
    }
}
