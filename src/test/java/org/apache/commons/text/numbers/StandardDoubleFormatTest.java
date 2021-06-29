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
package org.apache.commons.text.numbers;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Random;
import java.util.function.Function;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StandardDoubleFormatTest {

    @Test
    void testBuilder_illegalArgs() {
        // arrange
        final StandardDoubleFormat.Builder builder = StandardDoubleFormat.PLAIN.builder();

        // act/assert
        Assertions.assertThrows(NullPointerException.class, () -> builder.withDigits(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> builder.withDigits("a"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> builder.withDigits("0123456789a"));

        Assertions.assertThrows(NullPointerException.class, () -> builder.withExponentSeparator(null));
        Assertions.assertThrows(NullPointerException.class, () -> builder.withInfinity(null));
        Assertions.assertThrows(NullPointerException.class, () -> builder.withNaN(null));
        Assertions.assertThrows(NullPointerException.class, () -> builder.withFormatSymbols(null));
    }

    @Test
    void testFormatAccuracy() {
        // act/assert
        checkFormatAccuracyWithDefaults(StandardDoubleFormat.PLAIN);
        checkFormatAccuracyWithDefaults(StandardDoubleFormat.MIXED);
        checkFormatAccuracyWithDefaults(StandardDoubleFormat.SCIENTIFIC);
        checkFormatAccuracyWithDefaults(StandardDoubleFormat.ENGINEERING);
    }

    @Test
    void testPlain_defaults() {
        // arrange
        DoubleFormat fmt = StandardDoubleFormat.PLAIN.builder()
            .build();

        // act/assert
        checkFormat(fmt, 0.00001, "0.00001");
        checkFormat(fmt, -0.0001, "-0.0001");
        checkFormat(fmt, 0.001, "0.001");
        checkFormat(fmt, -0.01, "-0.01");
        checkFormat(fmt, 0.1, "0.1");
        checkFormat(fmt, -0.0, "-0.0");
        checkFormat(fmt, 0.0, "0.0");
        checkFormat(fmt, -1.0, "-1.0");
        checkFormat(fmt, 10.0, "10.0");
        checkFormat(fmt, -100.0, "-100.0");
        checkFormat(fmt, 1000.0, "1000.0");
        checkFormat(fmt, -10000.0, "-10000.0");
        checkFormat(fmt, 100000.0, "100000.0");
        checkFormat(fmt, -1000000.0, "-1000000.0");
        checkFormat(fmt, 10000000.0, "10000000.0");
        checkFormat(fmt, -100000000.0, "-100000000.0");

        checkFormat(fmt, 1.25e-3, "0.00125");
        checkFormat(fmt, -9.975e-4, "-0.0009975");
        checkFormat(fmt, 12345, "12345.0");
        checkFormat(fmt, -9_999_999, "-9999999.0");
        checkFormat(fmt, 1.00001e7, "10000100.0");

        checkFormat(fmt, Float.MAX_VALUE, "340282346638528860000000000000000000000.0");
        checkFormat(fmt, -Float.MIN_VALUE, "-0.000000000000000000000000000000000000000000001401298464324817");
        checkFormat(fmt, Float.MIN_NORMAL, "0.000000000000000000000000000000000000011754943508222875");
        checkFormat(fmt, Math.PI, "3.141592653589793");
        checkFormat(fmt, Math.E, "2.718281828459045");
    }

    @Test
    void testPlain_custom() {
        // arrange
        DoubleFormat fmt = StandardDoubleFormat.PLAIN.builder()
            .withMaxPrecision(3)
            .withMinDecimalExponent(-3)
            .withSignedZero(false)
            .withFractionPlaceholder(false)
            .withDecimalSeparator(',')
            .withExponentSeparator("e")
            .withInfinity("inf")
            .withNaN("nan")
            .withMinusSign('!')
            .build();

        // act/assert
        checkFormat(fmt, Double.NaN, "nan");
        checkFormat(fmt, Double.POSITIVE_INFINITY, "inf");
        checkFormat(fmt, Double.NEGATIVE_INFINITY, "!inf");

        checkFormat(fmt, 0.00001, "0");
        checkFormat(fmt, -0.0001, "0");
        checkFormat(fmt, 0.001, "0,001");
        checkFormat(fmt, -0.01, "!0,01");
        checkFormat(fmt, 0.1, "0,1");
        checkFormat(fmt, -0.0, "0");
        checkFormat(fmt, 0.0, "0");
        checkFormat(fmt, -1.0, "!1");
        checkFormat(fmt, 10.0, "10");
        checkFormat(fmt, -100.0, "!100");
        checkFormat(fmt, 1000.0, "1000");
        checkFormat(fmt, -10000.0, "!10000");
        checkFormat(fmt, 100000.0, "100000");
        checkFormat(fmt, -1000000.0, "!1000000");
        checkFormat(fmt, 10000000.0, "10000000");
        checkFormat(fmt, -100000000.0, "!100000000");

        checkFormat(fmt, 1.25e-3, "0,001");
        checkFormat(fmt, -9.975e-4, "!0,001");
        checkFormat(fmt, 12345, "12300");
        checkFormat(fmt, -9_999_999, "!10000000");
        checkFormat(fmt, 1.00001e7, "10000000");

        checkFormat(fmt, Float.MAX_VALUE, "340000000000000000000000000000000000000");
        checkFormat(fmt, -Float.MIN_VALUE, "0");
        checkFormat(fmt, Float.MIN_NORMAL, "0");
        checkFormat(fmt, Math.PI, "3,14");
        checkFormat(fmt, Math.E, "2,72");
    }

    @Test
    void testPlain_localeFormatComparison() {
        // arrange
        final String pattern = "0.0##";
        final Function<Locale, DoubleFormat> factory = loc -> StandardDoubleFormat.PLAIN.builder()
                .withMinDecimalExponent(-3)
                .withFormatSymbols(DecimalFormatSymbols.getInstance(loc))
                .build();

        // act/assert
        checkLocalizedFormats(pattern, factory);
    }

    @Test
    void testScientific_defaults() {
        // arrange
        final DoubleFormat fmt = StandardDoubleFormat.SCIENTIFIC.builder().build();

        // act/assert
        checkDefaultFormatSpecial(fmt);

        checkFormat(fmt, 0.00001, "1.0E-5");
        checkFormat(fmt, -0.0001, "-1.0E-4");
        checkFormat(fmt, 0.001, "1.0E-3");
        checkFormat(fmt, -0.01, "-1.0E-2");
        checkFormat(fmt, 0.1, "1.0E-1");
        checkFormat(fmt, -0.0, "-0.0");
        checkFormat(fmt, 0.0, "0.0");
        checkFormat(fmt, -1.0, "-1.0");
        checkFormat(fmt, 10.0, "1.0E1");
        checkFormat(fmt, -100.0, "-1.0E2");
        checkFormat(fmt, 1000.0, "1.0E3");
        checkFormat(fmt, -10000.0, "-1.0E4");
        checkFormat(fmt, 100000.0, "1.0E5");
        checkFormat(fmt, -1000000.0, "-1.0E6");
        checkFormat(fmt, 10000000.0, "1.0E7");
        checkFormat(fmt, -100000000.0, "-1.0E8");

        checkFormat(fmt, 1.25e-3, "1.25E-3");
        checkFormat(fmt, -9.975e-4, "-9.975E-4");
        checkFormat(fmt, 12345, "1.2345E4");
        checkFormat(fmt, -9_999_999, "-9.999999E6");
        checkFormat(fmt, 1.00001e7, "1.00001E7");

        checkFormat(fmt, Double.MAX_VALUE, "1.7976931348623157E308");
        checkFormat(fmt, Double.MIN_VALUE, "4.9E-324");
        checkFormat(fmt, Double.MIN_NORMAL, "2.2250738585072014E-308");
        checkFormat(fmt, Math.PI, "3.141592653589793");
        checkFormat(fmt, Math.E, "2.718281828459045");
    }

    @Test
    void testScientific_custom() {
        // arrange
        final DoubleFormat fmt = StandardDoubleFormat.SCIENTIFIC.builder()
                .withMaxPrecision(3)
                .withMinDecimalExponent(-3)
                .withSignedZero(false)
                .withFractionPlaceholder(false)
                .withDecimalSeparator(',')
                .withExponentSeparator("e")
                .withInfinity("inf")
                .withNaN("nan")
                .withMinusSign('!')
                .build();

        // act/assert
        checkFormat(fmt, Double.NaN, "nan");
        checkFormat(fmt, Double.POSITIVE_INFINITY, "inf");
        checkFormat(fmt, Double.NEGATIVE_INFINITY, "!inf");

        checkFormat(fmt, 0.00001, "0");
        checkFormat(fmt, -0.0001, "0");
        checkFormat(fmt, 0.001, "1e!3");
        checkFormat(fmt, -0.01, "!1e!2");
        checkFormat(fmt, 0.1, "1e!1");
        checkFormat(fmt, -0.0, "0");
        checkFormat(fmt, 0.0, "0");
        checkFormat(fmt, -1.0, "!1");
        checkFormat(fmt, 10.0, "1e1");
        checkFormat(fmt, -100.0, "!1e2");
        checkFormat(fmt, 1000.0, "1e3");
        checkFormat(fmt, -10000.0, "!1e4");
        checkFormat(fmt, 100000.0, "1e5");
        checkFormat(fmt, -1000000.0, "!1e6");
        checkFormat(fmt, 10000000.0, "1e7");
        checkFormat(fmt, -100000000.0, "!1e8");

        checkFormat(fmt, 1.25e-3, "1e!3");
        checkFormat(fmt, -9.975e-4, "!1e!3");
        checkFormat(fmt, 12345, "1,23e4");
        checkFormat(fmt, -9_999_999, "!1e7");
        checkFormat(fmt, 1.00001e7, "1e7");

        checkFormat(fmt, Double.MAX_VALUE, "1,8e308");
        checkFormat(fmt, Double.MIN_VALUE, "0");
        checkFormat(fmt, Double.MIN_NORMAL, "0");
        checkFormat(fmt, Math.PI, "3,14");
        checkFormat(fmt, Math.E, "2,72");
    }

    @Test
    void testEngineering_defaults() {
        // act
        final DoubleFormat fmt = StandardDoubleFormat.ENGINEERING.builder()
                .build();

        // act/assert
        checkDefaultFormatSpecial(fmt);

        checkFormat(fmt, 0.00001, "10.0E-6");
        checkFormat(fmt, -0.0001, "-100.0E-6");
        checkFormat(fmt, 0.001, "1.0E-3");
        checkFormat(fmt, -0.01, "-10.0E-3");
        checkFormat(fmt, 0.1, "100.0E-3");
        checkFormat(fmt, -0.0, "-0.0");
        checkFormat(fmt, 0.0, "0.0");
        checkFormat(fmt, -1.0, "-1.0");
        checkFormat(fmt, 10.0, "10.0");
        checkFormat(fmt, -100.0, "-100.0");
        checkFormat(fmt, 1000.0, "1.0E3");
        checkFormat(fmt, -10000.0, "-10.0E3");
        checkFormat(fmt, 100000.0, "100.0E3");
        checkFormat(fmt, -1000000.0, "-1.0E6");
        checkFormat(fmt, 10000000.0, "10.0E6");
        checkFormat(fmt, -100000000.0, "-100.0E6");

        checkFormat(fmt, 1.25e-3, "1.25E-3");
        checkFormat(fmt, -9.975e-4, "-997.5E-6");
        checkFormat(fmt, 12345, "12.345E3");
        checkFormat(fmt, -9_999_999, "-9.999999E6");
        checkFormat(fmt, 1.00001e7, "10.0001E6");

        checkFormat(fmt, Double.MAX_VALUE, "179.76931348623157E306");
        checkFormat(fmt, Double.MIN_VALUE, "4.9E-324");
        checkFormat(fmt, Double.MIN_NORMAL, "22.250738585072014E-309");
        checkFormat(fmt, Math.PI, "3.141592653589793");
        checkFormat(fmt, Math.E, "2.718281828459045");
    }

    @Test
    void testEngineering_custom() {
        // act
        final DoubleFormat fmt = StandardDoubleFormat.ENGINEERING.builder()
                .withMaxPrecision(3)
                .withMinDecimalExponent(-3)
                .withSignedZero(false)
                .withFractionPlaceholder(false)
                .withDecimalSeparator(',')
                .withExponentSeparator("e")
                .withInfinity("inf")
                .withNaN("nan")
                .withMinusSign('!')
                .build();

        // act/assert
        checkFormat(fmt, Double.NaN, "nan");
        checkFormat(fmt, Double.POSITIVE_INFINITY, "inf");
        checkFormat(fmt, Double.NEGATIVE_INFINITY, "!inf");

        checkFormat(fmt, 0.00001, "0");
        checkFormat(fmt, -0.0001, "0");
        checkFormat(fmt, 0.001, "1e!3");
        checkFormat(fmt, -0.01, "!10e!3");
        checkFormat(fmt, 0.1, "100e!3");
        checkFormat(fmt, -0.0, "0");
        checkFormat(fmt, 0.0, "0");
        checkFormat(fmt, -1.0, "!1");
        checkFormat(fmt, 10.0, "10");
        checkFormat(fmt, -100.0, "!100");
        checkFormat(fmt, 1000.0, "1e3");
        checkFormat(fmt, -10000.0, "!10e3");
        checkFormat(fmt, 100000.0, "100e3");
        checkFormat(fmt, -1000000.0, "!1e6");
        checkFormat(fmt, 10000000.0, "10e6");
        checkFormat(fmt, -100000000.0, "!100e6");

        checkFormat(fmt, 1.25e-3, "1e!3");
        checkFormat(fmt, -9.975e-4, "!1e!3");
        checkFormat(fmt, 12345, "12,3e3");
        checkFormat(fmt, -9_999_999, "!10e6");
        checkFormat(fmt, 1.00001e7, "10e6");

        checkFormat(fmt, Double.MAX_VALUE, "180e306");
        checkFormat(fmt, Double.MIN_VALUE, "0");
        checkFormat(fmt, Double.MIN_NORMAL, "0");
        checkFormat(fmt, Math.PI, "3,14");
        checkFormat(fmt, Math.E, "2,72");
    }

    @Test
    void testMixed_defaults() {
        // arrange
        final DoubleFormat fmt = StandardDoubleFormat.MIXED.builder().build();

        // act/assert
        checkDefaultFormatSpecial(fmt);

        checkFormat(fmt, 0.00001, "1.0E-5");
        checkFormat(fmt, -0.0001, "-1.0E-4");
        checkFormat(fmt, 0.001, "0.001");
        checkFormat(fmt, -0.01, "-0.01");
        checkFormat(fmt, 0.1, "0.1");
        checkFormat(fmt, -0.0, "-0.0");
        checkFormat(fmt, 0.0, "0.0");
        checkFormat(fmt, -1.0, "-1.0");
        checkFormat(fmt, 10.0, "10.0");
        checkFormat(fmt, -100.0, "-100.0");
        checkFormat(fmt, 1000.0, "1000.0");
        checkFormat(fmt, -10000.0, "-10000.0");
        checkFormat(fmt, 100000.0, "100000.0");
        checkFormat(fmt, -1000000.0, "-1000000.0");
        checkFormat(fmt, 10000000.0, "1.0E7");
        checkFormat(fmt, -100000000.0, "-1.0E8");

        checkFormat(fmt, 1.25e-3, "0.00125");
        checkFormat(fmt, -9.975e-4, "-9.975E-4");
        checkFormat(fmt, 12345, "12345.0");
        checkFormat(fmt, -9_999_999, "-9999999.0");
        checkFormat(fmt, 1.00001e7, "1.00001E7");

        checkFormat(fmt, Double.MAX_VALUE, "1.7976931348623157E308");
        checkFormat(fmt, Double.MIN_VALUE, "4.9E-324");
        checkFormat(fmt, Double.MIN_NORMAL, "2.2250738585072014E-308");
        checkFormat(fmt, Math.PI, "3.141592653589793");
        checkFormat(fmt, Math.E, "2.718281828459045");
    }

    @Test
    void testMixed_custom() {
        // arrange
        final DoubleFormat fmt = StandardDoubleFormat.MIXED.builder()
                .withMaxPrecision(3)
                .withMinDecimalExponent(-3)
                .withSignedZero(false)
                .withFractionPlaceholder(false)
                .withDecimalSeparator(',')
                .withPlainFormatMaxDecimalExponent(4)
                .withPlainFormatMinDecimalExponent(-1)
                .withExponentSeparator("e")
                .withInfinity("inf")
                .withNaN("nan")
                .withMinusSign('!')
                .build();

        // act/assert
        checkFormat(fmt, Double.NaN, "nan");
        checkFormat(fmt, Double.POSITIVE_INFINITY, "inf");
        checkFormat(fmt, Double.NEGATIVE_INFINITY, "!inf");

        checkFormat(fmt, 0.00001, "0");
        checkFormat(fmt, -0.0001, "0");
        checkFormat(fmt, 0.001, "1e!3");
        checkFormat(fmt, -0.01, "!1e!2");
        checkFormat(fmt, 0.1, "0,1");
        checkFormat(fmt, -0.0, "0");
        checkFormat(fmt, 0.0, "0");
        checkFormat(fmt, -1.0, "!1");
        checkFormat(fmt, 10.0, "10");
        checkFormat(fmt, -100.0, "!100");
        checkFormat(fmt, 1000.0, "1000");
        checkFormat(fmt, -10000.0, "!10000");
        checkFormat(fmt, 100000.0, "1e5");
        checkFormat(fmt, -1000000.0, "!1e6");
        checkFormat(fmt, 10000000.0, "1e7");
        checkFormat(fmt, -100000000.0, "!1e8");

        checkFormat(fmt, 1.25e-3, "1e!3");
        checkFormat(fmt, -9.975e-4, "!1e!3");
        checkFormat(fmt, 12345, "12300");
        checkFormat(fmt, -9_999_999, "!1e7");
        checkFormat(fmt, 1.00001e7, "1e7");

        checkFormat(fmt, Double.MAX_VALUE, "1,8e308");
        checkFormat(fmt, Double.MIN_VALUE, "0");
        checkFormat(fmt, Double.MIN_NORMAL, "0");
        checkFormat(fmt, Math.PI, "3,14");
        checkFormat(fmt, Math.E, "2,72");
    }

    @Test
    void testCustomDigitString() {
        // arrange
        final String digits = "abcdefghij";
        final DoubleFormat plain = StandardDoubleFormat.PLAIN.builder().withDigits(digits).build();
        final DoubleFormat sci = StandardDoubleFormat.SCIENTIFIC.builder().withDigits(digits).build();
        final DoubleFormat eng = StandardDoubleFormat.ENGINEERING.builder().withDigits(digits).build();
        final DoubleFormat mixed = StandardDoubleFormat.MIXED.builder().withDigits(digits).build();

        // act/assert
        checkFormat(plain, 9876543210.0, "jihgfedcba.a");
        checkFormat(sci, 9876543210.0, "j.ihgfedcbEj");
        checkFormat(eng, 9876543210.0, "j.ihgfedcbEj");
        checkFormat(mixed, 9876543210.0, "j.ihgfedcbEj");
    }

    /** Check that the given format type correctly formats doubles when using the
     * default configuration options. The format itself is not checked; only the
     * fact that the input double can be successfully recovered using {@link Double#parseDouble(String)}
     * is asserted.
     * @param type
     */
    private static void checkFormatAccuracyWithDefaults(final StandardDoubleFormat type) {
        final DoubleFormat fmt = type.builder().build();

        checkDefaultFormatSpecial(fmt);

        checkFormatAccuracy(fmt, Double.MIN_VALUE);
        checkFormatAccuracy(fmt, -Double.MIN_VALUE);

        checkFormatAccuracy(fmt, Double.MIN_NORMAL);
        checkFormatAccuracy(fmt, -Double.MIN_NORMAL);

        checkFormatAccuracy(fmt, Double.MAX_VALUE);
        checkFormatAccuracy(fmt, -Double.MAX_VALUE);

        checkFormatAccuracy(fmt, Math.PI);
        checkFormatAccuracy(fmt, Math.E);

        final Random rnd = new Random(10L);
        final int cnt = 1000;
        for (int i = 0; i < cnt; ++i) {
            checkFormatAccuracy(fmt, randomDouble(rnd));
        }
    }

    /** Check that the given double value can be exactly recovered from formatted string representation
     * produced by the format instance.
     * @param fmt format instance
     * @param d input double value
     */
    private static void checkFormatAccuracy(final DoubleFormat fmt, final double d) {
        final String str = fmt.apply(d);
        final double parsed = Double.parseDouble(str);
        Assertions.assertEquals(d, parsed, () -> "Formatted double string [" + str + "] did not match input value");
    }

    private static void checkFormat(final DoubleFormat fmt, final double d, final String str) {
        Assertions.assertEquals(str, fmt.apply(d));

        final StringBuilder sb = new StringBuilder();
        fmt.appendTo(sb, d);
        Assertions.assertEquals(str, sb.toString());

        final StringWriter appendable = new StringWriter();
        try {
            fmt.appendTo(appendable, d);
        } catch (IOException exc) {
            throw new UncheckedIOException(exc);
        }
        Assertions.assertEquals(str, appendable.toString());
    }

    private static void checkDefaultFormatSpecial(final DoubleFormat fmt) {
        checkFormat(fmt, 0.0, "0.0");
        checkFormat(fmt, -0.0, "-0.0");
        checkFormat(fmt, Double.NaN, "NaN");
        checkFormat(fmt, Double.POSITIVE_INFINITY, "Infinity");
        checkFormat(fmt, Double.NEGATIVE_INFINITY, "-Infinity");
    }

    private static void checkLocalizedFormats(final String pattern, final Function<Locale, DoubleFormat> factory) {
        for (final Locale loc : Locale.getAvailableLocales()) {
            checkLocalizedFormat(loc, pattern, factory);
        }
    }

    private static void checkLocalizedFormat(final Locale loc, final String pattern,
            final Function<Locale, DoubleFormat> factory) {
        // arrange
        final DecimalFormat df = new DecimalFormat(pattern, DecimalFormatSymbols.getInstance(loc));
        final DoubleFormat fmt = factory.apply(loc);

        // act/assert
        assertLocalizedFormatsAreEqual(0.0, df, fmt, loc);
        assertLocalizedFormatsAreEqual(-0.0, df, fmt, loc);
        assertLocalizedFormatsAreEqual(Double.POSITIVE_INFINITY, df, fmt, loc);
        assertLocalizedFormatsAreEqual(Double.NEGATIVE_INFINITY, df, fmt, loc);
        assertLocalizedFormatsAreEqual(Double.NaN, df, fmt, loc);

        assertLocalizedFormatsAreEqual(1.0, df, fmt, loc);
        assertLocalizedFormatsAreEqual(-1.0, df, fmt, loc);
        assertLocalizedFormatsAreEqual(Math.PI, df, fmt, loc);
        assertLocalizedFormatsAreEqual(Math.E, df, fmt, loc);

        final Random rnd = new Random(11L);
        final int cnt = 1000;
        for (int i = 0; i < cnt; ++i) {
            assertLocalizedFormatsAreEqual(randomDouble(rnd), df, fmt, loc);
        }
    }

    private static void assertLocalizedFormatsAreEqual(final double d, final DecimalFormat df, final DoubleFormat fmt,
            final Locale loc) {
        Assertions.assertEquals(df.format(d), fmt.apply(d),
                () -> "Unexpected output for locale [" + loc.toLanguageTag() + "] and double value " + d);
    }

    /** Create a random double value using the full range of exponent values.
     * @param rnd random number generator
     * @return random double
     */
    private static double randomDouble(final Random rnd) {
        return randomDouble(Double.MIN_EXPONENT, Double.MAX_EXPONENT, rnd);
    }

    /** Create a random double value with exponent in the range {@code [minExp, maxExp]}.
     * @param minExp minimum exponent; must be less than {@code maxExp}
     * @param maxExp maximum exponent; must be greater than {@code minExp}
     * @param rnd random number generator
     * @return random double
     */
    private static double randomDouble(final int minExp, final int maxExp, final Random rnd) {
        // Create random doubles using random bits in the sign bit and the mantissa.
        final long mask = ((1L << 52) - 1) | 1L << 63;
        final long bits = rnd.nextLong() & mask;
        // The exponent must be unsigned so + 1023 to the signed exponent
        final long exp = rnd.nextInt(maxExp - minExp + 1) + minExp + 1023;
        return Double.longBitsToDouble(bits | (exp << 52));
    }
}
