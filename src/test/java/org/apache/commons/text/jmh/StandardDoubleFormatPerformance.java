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
package org.apache.commons.text.jmh;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;
import java.util.function.DoubleFunction;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.apache.commons.text.numbers.DoubleFormat;
import org.apache.commons.text.numbers.StandardDoubleFormat;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

/** Benchmarks for the {@link StandardDoubleFormat} class.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 1, jvmArgs = {"-server", "-Xms512M", "-Xmx512M"})
public class StandardDoubleFormatPerformance {

    /** Decimal format pattern for plain output. */
    private static final String PLAIN_PATTERN = "0.0##";

    /** Decimal format pattern for plain output with thousands grouping. */
    private static final String PLAIN_GROUPED_PATTERN = "#,##0.0##";

    /** Decimal format pattern for scientific output. */
    private static final String SCI_PATTERN = "0.0##E0";

    /** Decimal format pattern for engineering output. */
    private static final String ENG_PATTERN = "##0.0##E0";

    /** Benchmark input providing a source of random double values. */
    @State(Scope.Thread)
    public static class DoubleInput {

        /** The number of doubles in the input array. */
        @Param({"10000"})
        private int size;

        /** Minimum base 2 exponent for random input doubles. */
        @Param("-100")
        private int minExp;

        /** Maximum base 2 exponent for random input doubles. */
        @Param("100")
        private int maxExp;

        /** Double input array. */
        private double[] input;

        /** Get the input doubles.
         * @return the input doubles
         */
        public double[] getInput() {
            return input;
        }

        /** Set up the instance for the benchmark. */
        @Setup(Level.Iteration)
        public void setup() {
            input = randomDoubleArray(size, minExp, maxExp,
                RandomSource.create(RandomSource.XO_RO_SHI_RO_128_PP));
        }
    }

    /** Create a random double value with exponent in the range {@code [minExp, maxExp]}.
     * @param minExp minimum exponent; must be less than {@code maxExp}
     * @param maxExp maximum exponent; must be greater than {@code minExp}
     * @param rng random number generator
     * @return random double
     */
    private static double randomDouble(final int minExp, final int maxExp, final UniformRandomProvider rng) {
        // Create random doubles using random bits in the sign bit and the mantissa.
        final long mask = ((1L << 52) - 1) | 1L << 63;
        final long bits = rng.nextLong() & mask;
        // The exponent must be unsigned so + 1023 to the signed exponent
        final long exp = rng.nextInt(maxExp - minExp + 1) + minExp + 1023;
        return Double.longBitsToDouble(bits | (exp << 52));
    }

    /** Create an array with the given length containing random doubles with exponents in the range
     * {@code [minExp, maxExp]}.
     * @param len array length
     * @param minExp minimum exponent; must be less than {@code maxExp}
     * @param maxExp maximum exponent; must be greater than {@code minExp}
     * @param rng random number generator
     * @return array of random doubles
     */
    private static double[] randomDoubleArray(final int len, final int minExp, final int maxExp,
            final UniformRandomProvider rng) {
        final double[] arr = new double[len];
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = randomDouble(minExp, maxExp, rng);
        }
        return arr;
    }

    /** Run a benchmark test on a function accepting a double argument.
     * @param <T> function output type
     * @param input double array
     * @param bh jmh blackhole for consuming output
     * @param fn function to call
     */
    private static <T> void runDoubleFunction(final DoubleInput input, final Blackhole bh,
            final DoubleFunction<T> fn) {
        for (final double d : input.getInput()) {
            bh.consume(fn.apply(d));
        }
    }

    /** Benchmark testing just the overhead of the benchmark harness.
     * @param input benchmark state input
     * @param bh jmh blackhole for consuming output
     */
    @Benchmark
    public void baseline(final DoubleInput input, final Blackhole bh) {
        runDoubleFunction(input, bh, d -> "0.0");
    }

    /** Benchmark testing the {@link Double#toString()} method.
     * @param input benchmark state input
     * @param bh jmh blackhole for consuming output
     */
    @Benchmark
    public void doubleToString(final DoubleInput input, final Blackhole bh) {
        runDoubleFunction(input, bh, Double::toString);
    }

    /** Benchmark testing the {@link String#format(String, Object...)} method.
     * @param input benchmark state input
     * @param bh jmh blackhole for consuming output
     */
    @Benchmark
    public void stringFormat(final DoubleInput input, final Blackhole bh) {
        runDoubleFunction(input, bh, d -> String.format("%f", d));
    }

    /** Benchmark testing the BigDecimal formatting performance.
     * @param input benchmark state input
     * @param bh jmh blackhole for consuming output
     */
    @Benchmark
    public void bigDecimal(final DoubleInput input, final Blackhole bh) {
        final DoubleFunction<String> fn = d -> BigDecimal.valueOf(d)
                .setScale(3, RoundingMode.HALF_EVEN)
                .stripTrailingZeros()
                .toString();
        runDoubleFunction(input, bh, fn);
    }

    /** Benchmark testing the {@link DecimalFormat} class.
     * @param input benchmark state input
     * @param bh jmh blackhole for consuming output
     */
    @Benchmark
    public void decimalFormatPlain(final DoubleInput input, final Blackhole bh) {
        final DecimalFormat fmt = new DecimalFormat(PLAIN_PATTERN);
        runDoubleFunction(input, bh, fmt::format);
    }

    /** Benchmark testing the {@link DecimalFormat} class with thousands grouping.
     * @param input benchmark state input
     * @param bh jmh blackhole for consuming output
     */
    @Benchmark
    public void decimalFormatPlainGrouped(final DoubleInput input, final Blackhole bh) {
        final DecimalFormat fmt = new DecimalFormat(PLAIN_GROUPED_PATTERN);
        runDoubleFunction(input, bh, fmt::format);
    }

    /** Benchmark testing the {@link DecimalFormat} class with scientific format.
     * @param input benchmark state input
     * @param bh jmh blackhole for consuming output
     */
    @Benchmark
    public void decimalFormatScientific(final DoubleInput input, final Blackhole bh) {
        final DecimalFormat fmt = new DecimalFormat(SCI_PATTERN);
        runDoubleFunction(input, bh, fmt::format);
    }

    /** Benchmark testing the {@link DecimalFormat} class with engineering format.
     * @param input benchmark state input
     * @param bh jmh blackhole for consuming output
     */
    @Benchmark
    public void decimalFormatEngineering(final DoubleInput input, final Blackhole bh) {
        final DecimalFormat fmt = new DecimalFormat(ENG_PATTERN);
        runDoubleFunction(input, bh, fmt::format);
    }

    /** Benchmark testing the {@link StandardDoubleFormat#PLAIN} format.
     * @param input benchmark state input
     * @param bh jmh blackhole for consuming output
     */
    @Benchmark
    public void standardPlain(final DoubleInput input, final Blackhole bh) {
        final DoubleFormat fmt = StandardDoubleFormat.PLAIN.builder()
                .withMinDecimalExponent(-3)
                .build();
        runDoubleFunction(input, bh, fmt);
    }

    /** Benchmark testing the {@link StandardDoubleFormat#PLAIN} format with
     * thousands grouping.
     * @param input benchmark state input
     * @param bh jmh blackhole for consuming output
     */
    @Benchmark
    public void standardPlainGrouped(final DoubleInput input, final Blackhole bh) {
        final DoubleFormat fmt = StandardDoubleFormat.PLAIN.builder()
                .withMinDecimalExponent(-3)
                .withGroupThousands(true)
                .build();
        runDoubleFunction(input, bh, fmt);
    }

    /** Benchmark testing the {@link StandardDoubleFormat#SCIENTIFIC} format.
     * @param input benchmark state input
     * @param bh jmh blackhole for consuming output
     */
    @Benchmark
    public void standardScientific(final DoubleInput input, final Blackhole bh) {
        final DoubleFormat fmt = StandardDoubleFormat.SCIENTIFIC.builder()
                .withMaxPrecision(4)
                .withAlwaysIncludeExponent(true)
                .build();
        runDoubleFunction(input, bh, fmt);
    }

    /** Benchmark testing the {@link StandardDoubleFormat#ENGINEERING} format.
     * @param input benchmark state input
     * @param bh jmh blackhole for consuming output
     */
    @Benchmark
    public void standardEngineering(final DoubleInput input, final Blackhole bh) {
        final DoubleFormat fmt = StandardDoubleFormat.ENGINEERING.builder()
                .withMaxPrecision(6)
                .withAlwaysIncludeExponent(true)
                .build();
        runDoubleFunction(input, bh, fmt);
    }
}
