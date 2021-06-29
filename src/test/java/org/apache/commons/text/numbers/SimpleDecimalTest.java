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
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.function.BiFunction;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class SimpleDecimalTest {

    @Test
    void testFrom() {
        // act/assert
        checkFrom(0.0, "0", 0);

        checkFrom(1.0, "1", 0);
        checkFrom(10.0, "1", 1);
        checkFrom(100.0, "1", 2);
        checkFrom(1000.0, "1", 3);
        checkFrom(10000.0, "1", 4);

        checkFrom(0.1, "1", -1);
        checkFrom(0.01, "1", -2);
        checkFrom(0.001, "1", -3);
        checkFrom(0.0001, "1", -4);
        checkFrom(0.00001, "1", -5);

        checkFrom(1.2, "12", -1);
        checkFrom(0.00971, "971", -5);
        checkFrom(56300, "563", 2);

        checkFrom(123.0, "123", 0);
        checkFrom(1230.0, "123", 1);
        checkFrom(12300.0, "123", 2);
        checkFrom(123000.0, "123", 3);

        checkFrom(12.3, "123", -1);
        checkFrom(1.23, "123", -2);
        checkFrom(0.123, "123", -3);
        checkFrom(0.0123, "123", -4);

        checkFrom(1.987654321e270, "1987654321", 261);
        checkFrom(1.987654321e-270, "1987654321", -279);

        checkFrom(Math.PI, "3141592653589793", -15);
        checkFrom(Math.E, "2718281828459045", -15);

        checkFrom(Double.MAX_VALUE, "17976931348623157", 292);
        checkFrom(Double.MIN_VALUE, "49", -325);
        checkFrom(Double.MIN_NORMAL, "22250738585072014", -324);
    }

    @Test
    void testFrom_notFinite() {
        // arrange
        final String msg = "Double is not finite";

        // act/assert
        assertThrowsWithMessage(() -> SimpleDecimal.from(Double.NaN),
                IllegalArgumentException.class, msg);
        assertThrowsWithMessage(() -> SimpleDecimal.from(Double.NEGATIVE_INFINITY),
                IllegalArgumentException.class, msg);
        assertThrowsWithMessage(() -> SimpleDecimal.from(Double.POSITIVE_INFINITY),
                IllegalArgumentException.class, msg);
    }

    @Test
    void testIsZero() {
        // act/assert
        Assertions.assertTrue(SimpleDecimal.from(0.0).isZero());
        Assertions.assertTrue(SimpleDecimal.from(-0.0).isZero());

        Assertions.assertFalse(SimpleDecimal.from(1.0).isZero());
        Assertions.assertFalse(SimpleDecimal.from(-1.0).isZero());

        Assertions.assertFalse(SimpleDecimal.from(Double.MIN_NORMAL).isZero());
        Assertions.assertFalse(SimpleDecimal.from(-Double.MIN_NORMAL).isZero());

        Assertions.assertFalse(SimpleDecimal.from(Double.MAX_VALUE).isZero());
        Assertions.assertFalse(SimpleDecimal.from(-Double.MIN_VALUE).isZero());
    }

    @Test
    void testRound_one() {
        // arrange
        final SimpleDecimal a = SimpleDecimal.from(1e-10);
        final SimpleDecimal b = SimpleDecimal.from(-1);
        final SimpleDecimal c = SimpleDecimal.from(1e10);

        // act/assert
        assertSimpleDecimal(a.round(-11), false, "1", -10);
        assertSimpleDecimal(a.round(-10), false, "1", -10);
        assertSimpleDecimal(a.round(-9), false, "0", 0);

        assertSimpleDecimal(b.round(-1), true, "1", 0);
        assertSimpleDecimal(b.round(0), true, "1", 0);
        assertSimpleDecimal(b.round(1), true, "0", 0);

        assertSimpleDecimal(c.round(9), false, "1", 10);
        assertSimpleDecimal(c.round(10), false, "1", 10);
        assertSimpleDecimal(c.round(11), false, "0", 0);
    }

    @Test
    void testRound_nine() {
        // arrange
        final SimpleDecimal a = SimpleDecimal.from(9e-10);
        final SimpleDecimal b = SimpleDecimal.from(-9);
        final SimpleDecimal c = SimpleDecimal.from(9e10);

        // act/assert
        assertSimpleDecimal(a.round(-11), false, "9", -10);
        assertSimpleDecimal(a.round(-10), false, "9", -10);
        assertSimpleDecimal(a.round(-9), false, "1", -9);

        assertSimpleDecimal(b.round(-1), true, "9", 0);
        assertSimpleDecimal(b.round(0), true, "9", 0);
        assertSimpleDecimal(b.round(1), true, "1", 1);

        assertSimpleDecimal(c.round(9), false, "9", 10);
        assertSimpleDecimal(c.round(10), false, "9", 10);
        assertSimpleDecimal(c.round(11), false, "1", 11);
    }

    @Test
    void testRound_mixed() {
        // arrange
        final SimpleDecimal a = SimpleDecimal.from(9.94e-10);
        final SimpleDecimal b = SimpleDecimal.from(-3.1415);
        final SimpleDecimal c = SimpleDecimal.from(5.55e10);

        // act/assert
        assertSimpleDecimal(a.round(-13), false, "994", -12);
        assertSimpleDecimal(a.round(-12), false, "994", -12);
        assertSimpleDecimal(a.round(-11), false, "99", -11);
        assertSimpleDecimal(a.round(-10), false, "1", -9);
        assertSimpleDecimal(a.round(-9), false, "1", -9);
        assertSimpleDecimal(a.round(-8), false, "0", 0);

        assertSimpleDecimal(b.round(-5), true, "31415", -4);
        assertSimpleDecimal(b.round(-4), true, "31415", -4);
        assertSimpleDecimal(b.round(-3), true, "3142", -3);
        assertSimpleDecimal(b.round(-2), true, "314", -2);
        assertSimpleDecimal(b.round(-1), true, "31", -1);
        assertSimpleDecimal(b.round(0), true, "3", 0);
        assertSimpleDecimal(b.round(1), true, "0", 0);
        assertSimpleDecimal(b.round(2), true, "0", 0);

        assertSimpleDecimal(c.round(7), false, "555", 8);
        assertSimpleDecimal(c.round(8), false, "555", 8);
        assertSimpleDecimal(c.round(9), false, "56", 9);
        assertSimpleDecimal(c.round(10), false, "6", 10);
        assertSimpleDecimal(c.round(11), false, "1", 11);
        assertSimpleDecimal(c.round(12), false, "0", 0);
    }

    @Test
    void testMaxPrecision() {
        // arrange
        final SimpleDecimal d = SimpleDecimal.from(1.02576552);

        // act
        assertSimpleDecimal(d.maxPrecision(10), false, "102576552", -8);
        assertSimpleDecimal(d.maxPrecision(9), false, "102576552", -8);
        assertSimpleDecimal(d.maxPrecision(8), false, "10257655", -7);
        assertSimpleDecimal(d.maxPrecision(7), false, "1025766", -6);
        assertSimpleDecimal(d.maxPrecision(6), false, "102577", -5);
        assertSimpleDecimal(d.maxPrecision(5), false, "10258", -4);
        assertSimpleDecimal(d.maxPrecision(4), false, "1026", -3);
        assertSimpleDecimal(d.maxPrecision(3), false, "103", -2);
        assertSimpleDecimal(d.maxPrecision(2), false, "1", 0);
        assertSimpleDecimal(d.maxPrecision(1), false, "1", 0);
    }

    @Test
    void testMaxPrecision_carry() {
        // arrange
        final SimpleDecimal d = SimpleDecimal.from(-999.0999e50);

        // act
        assertSimpleDecimal(d.maxPrecision(8), true, "9990999", 46);
        assertSimpleDecimal(d.maxPrecision(7), true, "9990999", 46);
        assertSimpleDecimal(d.maxPrecision(6), true, "9991", 49);
        assertSimpleDecimal(d.maxPrecision(5), true, "9991", 49);
        assertSimpleDecimal(d.maxPrecision(4), true, "9991", 49);
        assertSimpleDecimal(d.maxPrecision(3), true, "999", 50);
        assertSimpleDecimal(d.maxPrecision(2), true, "1", 53);
        assertSimpleDecimal(d.maxPrecision(1), true, "1", 53);
    }

    @Test
    void testMaxPrecision_halfEvenRounding() {
        // act/assert
        // Test values taken from RoundingMode.HALF_EVEN javadocs
        assertSimpleDecimal(SimpleDecimal.from(5.5).maxPrecision(1), false, "6", 0);
        assertSimpleDecimal(SimpleDecimal.from(2.5).maxPrecision(1), false, "2", 0);
        assertSimpleDecimal(SimpleDecimal.from(1.6).maxPrecision(1), false, "2", 0);
        assertSimpleDecimal(SimpleDecimal.from(1.1).maxPrecision(1), false, "1", 0);
        assertSimpleDecimal(SimpleDecimal.from(1.0).maxPrecision(1), false, "1", 0);

        assertSimpleDecimal(SimpleDecimal.from(-1.0).maxPrecision(1), true, "1", 0);
        assertSimpleDecimal(SimpleDecimal.from(-1.1).maxPrecision(1), true, "1", 0);
        assertSimpleDecimal(SimpleDecimal.from(-1.6).maxPrecision(1), true, "2", 0);
        assertSimpleDecimal(SimpleDecimal.from(-2.5).maxPrecision(1), true, "2", 0);
        assertSimpleDecimal(SimpleDecimal.from(-5.5).maxPrecision(1), true, "6", 0);
    }

    @Test
    void testMaxPrecision_singleDigits() {
        // act
        assertSimpleDecimal(SimpleDecimal.from(9.0).maxPrecision(1), false, "9", 0);
        assertSimpleDecimal(SimpleDecimal.from(1.0).maxPrecision(1), false, "1", 0);
        assertSimpleDecimal(SimpleDecimal.from(0.0).maxPrecision(1), false, "0", 0);
        assertSimpleDecimal(SimpleDecimal.from(-0.0).maxPrecision(1), true, "0", 0);
        assertSimpleDecimal(SimpleDecimal.from(-1.0).maxPrecision(1), true, "1", 0);
        assertSimpleDecimal(SimpleDecimal.from(-9.0).maxPrecision(1), true, "9", 0);
    }

    @Test
    void testMaxPrecision_random() {
        // arrange
        final UniformRandomProvider rand = RandomSource.create(RandomSource.XO_RO_SHI_RO_128_PP, 0L);

        double d;
        int precision;
        SimpleDecimal result;
        MathContext ctx;
        for (int i = 0; i < 10_000; ++i) {
            d = createRandomDouble(rand);
            precision = rand.nextInt(20) + 1;
            ctx = new MathContext(precision, RoundingMode.HALF_EVEN);

            // act
            result = SimpleDecimal.from(d).maxPrecision(precision);

            // assert
            Assertions.assertEquals(new BigDecimal(Double.toString(d), ctx).doubleValue(),
                    Double.parseDouble(scientificString(result, new FormatOptionsImpl())));
        }
    }

    @Test
    void testMaxPrecision_invalidArg() {
        // arrange
        final SimpleDecimal d = SimpleDecimal.from(10);
        final String baseMsg = "Precision must be greater than zero; was ";

        // act/assert
        assertThrowsWithMessage(() -> d.maxPrecision(0), IllegalArgumentException.class, baseMsg + "0");
        assertThrowsWithMessage(() -> d.maxPrecision(-1), IllegalArgumentException.class, baseMsg + "-1");
    }

    @Test
    void testToPlainString_defaults() {
        // arrange
        final FormatOptionsImpl opts = new FormatOptionsImpl();

        // act/assert
        checkToPlainString(0.0, "0.0", opts);
        checkToPlainString(-0.0, "-0.0", opts);
        checkToPlainString(1.0, "1.0", opts);
        checkToPlainString(1.5, "1.5", opts);

        checkToPlainString(-0.000123, "-0.000123", opts);
        checkToPlainString(12301, "12301.0", opts);

        checkToPlainString(Math.PI, "3.141592653589793", opts);
        checkToPlainString(Math.E, "2.718281828459045", opts);

        checkToPlainString(-12345.6789, "-12345.6789", opts);
        checkToPlainString(1.23e12, "1230000000000.0", opts);
        checkToPlainString(1.23e-12, "0.00000000000123", opts);
    }

    @Test
    void testToPlainString_altFormat() {
        // arrange
        final FormatOptionsImpl opts = new FormatOptionsImpl();
        opts.setIncludeFractionPlaceholder(false);
        opts.setSignedZero(false);
//        opts.setZeroDigit('#');
        opts.setDecimalSeparator(',');
        opts.setMinusSign('!');
        opts.setExponentSeparator("10^");

        // act/assert
        checkToPlainString(0.0, "#", opts);
        checkToPlainString(-0.0, "#", opts);
        checkToPlainString(1.0, "1", opts);
        checkToPlainString(1.5, "1,5", opts);

        checkToPlainString(-0.000123, "!#,###123", opts);
        checkToPlainString(12301, "123#1", opts);

        checkToPlainString(Math.PI, "3,141592653589793", opts);
        checkToPlainString(Math.E, "2,718281828459#45", opts);

        checkToPlainString(-12345.6789, "!12345,6789", opts);
        checkToPlainString(1.23e12, "123##########", opts);
        checkToPlainString(1.23e-12, "#,###########123", opts);
    }

    @Test
    void testToScientificString_defaults() {
        // arrange
        final FormatOptionsImpl opts = new FormatOptionsImpl();

        // act/assert
        checkToScientificString(0.0, "0.0", opts);
        checkToScientificString(-0.0, "-0.0", opts);
        checkToScientificString(1.0, "1.0", opts);
        checkToScientificString(1.5, "1.5", opts);

        checkToScientificString(-0.000123, "-1.23E-4", opts);
        checkToScientificString(12301, "1.2301E4", opts);

        checkToScientificString(Math.PI, "3.141592653589793", opts);
        checkToScientificString(Math.E, "2.718281828459045", opts);

        checkToScientificString(-Double.MAX_VALUE, "-1.7976931348623157E308", opts);
        checkToScientificString(Double.MIN_VALUE, "4.9E-324", opts);
        checkToScientificString(Double.MIN_NORMAL, "2.2250738585072014E-308", opts);
    }

    @Test
    void testToScientificString_altFormats() {
        // arrange
        final FormatOptionsImpl opts = new FormatOptionsImpl();
        opts.setIncludeFractionPlaceholder(false);
        opts.setSignedZero(false);
//        opts.setZeroDigit('#');
        opts.setDecimalSeparator(',');
        opts.setMinusSign('!');
        opts.setExponentSeparator("x10^");

        // act/assert
        checkToScientificString(0.0, "#", opts);
        checkToScientificString(-0.0, "#", opts);
        checkToScientificString(1.0, "1", opts);
        checkToScientificString(1.5, "1,5", opts);

        checkToScientificString(-0.000123, "!1,23x10^!4", opts);
        checkToScientificString(12301, "1,23#1x10^4", opts);

        checkToScientificString(Math.PI, "3,141592653589793", opts);
        checkToScientificString(Math.E, "2,718281828459#45", opts);

        checkToScientificString(-Double.MAX_VALUE, "!1,7976931348623157x10^3#8", opts);
        checkToScientificString(Double.MIN_VALUE, "4,9x10^!324", opts);
        checkToScientificString(Double.MIN_NORMAL, "2,225#738585#72#14x10^!3#8", opts);
    }

    @Test
    void testToEngineeringString_defaults() {
        // arrange
        final FormatOptionsImpl opts = new FormatOptionsImpl();

        // act/assert
        checkToEngineeringString(0.0, "0.0", opts);
        checkToEngineeringString(-0.0, "-0.0", opts);
        checkToEngineeringString(1.0, "1.0", opts);
        checkToEngineeringString(1.5, "1.5", opts);

        checkToEngineeringString(10, "10.0", opts);

        checkToEngineeringString(-0.000000123, "-123.0E-9", opts);
        checkToEngineeringString(12300000, "12.3E6", opts);

        checkToEngineeringString(Math.PI, "3.141592653589793", opts);
        checkToEngineeringString(Math.E, "2.718281828459045", opts);

        checkToEngineeringString(-Double.MAX_VALUE, "-179.76931348623157E306", opts);
        checkToEngineeringString(Double.MIN_VALUE, "4.9E-324", opts);
        checkToEngineeringString(Double.MIN_NORMAL, "22.250738585072014E-309", opts);
    }

    @Test
    void testToEngineeringString_altFormat() {
        // arrange
        final FormatOptionsImpl opts = new FormatOptionsImpl();
        opts.setIncludeFractionPlaceholder(false);
        opts.setSignedZero(false);
//        opts.setZeroDigit('#');
        opts.setDecimalSeparator(',');
        opts.setMinusSign('!');
        opts.setExponentSeparator("x10^");

        // act/assert
        checkToEngineeringString(0.0, "#", opts);
        checkToEngineeringString(-0.0, "#", opts);
        checkToEngineeringString(1.0, "1", opts);
        checkToEngineeringString(1.5, "1,5", opts);

        checkToEngineeringString(10, "1#", opts);

        checkToEngineeringString(-0.000000123, "!123x10^!9", opts);
        checkToEngineeringString(12300000, "12,3x10^6", opts);

        checkToEngineeringString(Math.PI, "3,141592653589793", opts);
        checkToEngineeringString(Math.E, "2,718281828459#45", opts);

        checkToEngineeringString(-Double.MAX_VALUE, "!179,76931348623157x10^3#6", opts);
        checkToEngineeringString(Double.MIN_VALUE, "4,9x10^!324", opts);
        checkToEngineeringString(Double.MIN_NORMAL, "22,25#738585#72#14x10^!3#9", opts);
    }

    @Test
    void testStringMethodAccuracy_sequence() {
        // arrange
        final double min = -1000;
        final double max = 1000;
        final double delta = 0.1;

        final FormatOptionsImpl stdOpts = new FormatOptionsImpl();
        final FormatOptionsImpl altOpts = new FormatOptionsImpl();
        altOpts.setExponentSeparator("e");
        altOpts.setIncludeFractionPlaceholder(false);

        Assertions.assertEquals(10.0, Double.parseDouble(scientificString(SimpleDecimal.from(10.0), stdOpts)));

        for (double d = min; d <= max; d += delta) {
            // act/assert
            Assertions.assertEquals(d, Double.parseDouble(scientificString(SimpleDecimal.from(d), stdOpts)));
            Assertions.assertEquals(d, Double.parseDouble(scientificString(SimpleDecimal.from(d), altOpts)));

            Assertions.assertEquals(d, Double.parseDouble(engineeringString(SimpleDecimal.from(d), stdOpts)));
            Assertions.assertEquals(d, Double.parseDouble(engineeringString(SimpleDecimal.from(d), altOpts)));

            Assertions.assertEquals(d, Double.parseDouble(plainString(SimpleDecimal.from(d), stdOpts)));
            Assertions.assertEquals(d, Double.parseDouble(plainString(SimpleDecimal.from(d), altOpts)));
        }
    }

    @Test
    void testStringMethodAccuracy_random() {
        // arrange
        final UniformRandomProvider rand = RandomSource.create(RandomSource.XO_RO_SHI_RO_128_PP, 0L);

        final FormatOptionsImpl stdOpts = new FormatOptionsImpl();
        final FormatOptionsImpl altOpts = new FormatOptionsImpl();
        altOpts.setExponentSeparator("e");
        altOpts.setIncludeFractionPlaceholder(false);

        double d;
        for (int i = 0; i < 10_000; ++i) {
            d = createRandomDouble(rand);

            // act/assert
            Assertions.assertEquals(d, Double.parseDouble(scientificString(SimpleDecimal.from(d), stdOpts)));
            Assertions.assertEquals(d, Double.parseDouble(scientificString(SimpleDecimal.from(d), altOpts)));

            Assertions.assertEquals(d, Double.parseDouble(engineeringString(SimpleDecimal.from(d), stdOpts)));
            Assertions.assertEquals(d, Double.parseDouble(engineeringString(SimpleDecimal.from(d), altOpts)));

            Assertions.assertEquals(d, Double.parseDouble(plainString(SimpleDecimal.from(d), stdOpts)));
            Assertions.assertEquals(d, Double.parseDouble(plainString(SimpleDecimal.from(d), altOpts)));
        }
    }

    private static void checkFrom(final double d, final String digits, final int exponent) {
        final boolean negative = Math.signum(d) < 0;

        assertSimpleDecimal(SimpleDecimal.from(d), negative, digits, exponent);
        assertSimpleDecimal(SimpleDecimal.from(-d), !negative, digits, exponent);
    }

    private static void checkToPlainString(final double d, final String expected,
            final SimpleDecimal.FormatOptions opts) {
        checkToStringMethod(d, expected, SimpleDecimalTest::plainString, opts);
    }

    private static void checkToScientificString(final double d, final String expected,
            final SimpleDecimal.FormatOptions opts) {
        checkToStringMethod(d, expected, SimpleDecimalTest::scientificString, opts);
    }

    private static void checkToEngineeringString(final double d, final String expected,
            final SimpleDecimal.FormatOptions opts) {
        checkToStringMethod(d, expected, SimpleDecimalTest::engineeringString, opts);

        // check the exponent value to make sure it is a multiple of 3
        final String pos = engineeringString(SimpleDecimal.from(d), opts);
        Assertions.assertEquals(0, parseExponent(pos, opts) % 3);

        final String neg = engineeringString(SimpleDecimal.from(-d), opts);
        Assertions.assertEquals(0, parseExponent(neg, opts) % 3);
    }

    private static int parseExponent(final String str, final SimpleDecimal.FormatOptions opts) {
        final String expSep = opts.getExponentSeparator();

        final int expStartIdx = str.indexOf(expSep);
        if (expStartIdx > -1) {
            int expIdx = expStartIdx + expSep.length();

            boolean neg = false;
            if (str.charAt(expIdx) == opts.getMinusSign()) {
                ++expIdx;
            }

            final String expStr = str.substring(expIdx);
            final int val = Integer.parseInt(expStr);
            return neg ?
                    -val :
                    val;
        }

        return 0;
    }

    private static void checkToStringMethod(final double d, final String expected,
            final BiFunction<SimpleDecimal, SimpleDecimal.FormatOptions, String> fn,
            final SimpleDecimal.FormatOptions opts) {

        final SimpleDecimal pos = SimpleDecimal.from(d);
        final String actual = fn.apply(pos, opts);

        Assertions.assertEquals(expected, actual);
    }

    private static void assertSimpleDecimal(final SimpleDecimal parsed, final boolean negative, final String digits,
            final int exponent) {
        Assertions.assertEquals(negative, parsed.isNegative());
        Assertions.assertEquals(digits, parsed.getDigits());
        Assertions.assertEquals(exponent, parsed.getExponent());
        Assertions.assertEquals(digits.length(), parsed.getPrecision());
        Assertions.assertEquals(exponent, parsed.getScientificExponent() - digits.length() + 1);
    }

    private static void assertThrowsWithMessage(final Executable fn, final Class<? extends Throwable> type,
            final String msg) {
        Throwable exc = Assertions.assertThrows(type, fn);
        Assertions.assertEquals(msg, exc.getMessage());
    }

    private static double createRandomDouble(final UniformRandomProvider rng) {
        final long mask = ((1L << 52) - 1) | 1L << 63;
        final long bits = rng.nextLong() & mask;
        final long exp = rng.nextInt(2045) + 1;
        return Double.longBitsToDouble(bits | (exp << 52));
    }

    private static String plainString(final SimpleDecimal dec, final SimpleDecimal.FormatOptions opts) {
        try {
            StringBuilder sb = new StringBuilder();
            dec.toPlainString(sb, opts);
            return sb.toString();
        } catch (IOException exc) {
            throw new UncheckedIOException(exc);
        }
    }

    private static String scientificString(final SimpleDecimal dec, final SimpleDecimal.FormatOptions opts) {
        try {
            StringBuilder sb = new StringBuilder();
            dec.toScientificString(sb, opts);
            return sb.toString();
        } catch (IOException exc) {
            throw new UncheckedIOException(exc);
        }
    }

    private static String engineeringString(final SimpleDecimal dec, final SimpleDecimal.FormatOptions opts) {
        try {
            StringBuilder sb = new StringBuilder();
            dec.toEngineeringString(sb, opts);
            return sb.toString();
        } catch (IOException exc) {
            throw new UncheckedIOException(exc);
        }
    }

    private static final class FormatOptionsImpl implements SimpleDecimal.FormatOptions {

        private boolean includeFractionPlaceholder = true;

        private boolean signedZero = true;

        private int zeroDelta = 0;

        private char decimalSeparator = '.';

        private char minusSign = '-';

        private String exponentSeparator = "E";

        @Override
        public boolean getIncludeFractionPlaceholder() {
            return includeFractionPlaceholder;
        }

        public void setIncludeFractionPlaceholder(final boolean includeFractionPlaceholder) {
            this.includeFractionPlaceholder = includeFractionPlaceholder;
        }

        @Override
        public boolean getSignedZero() {
            return signedZero;
        }

        public void setSignedZero(final boolean signedZero) {
            this.signedZero = signedZero;
        }

        @Override
        public int getZeroDelta() {
            return zeroDelta;
        }

        public void setZeroDelta(final int zeroDelta) {
            this.zeroDelta = zeroDelta;
        }

        @Override
        public char getDecimalSeparator() {
            return decimalSeparator;
        }

        public void setDecimalSeparator(final char decimalSeparator) {
            this.decimalSeparator = decimalSeparator;
        }

        @Override
        public char getMinusSign() {
            return minusSign;
        }

        public void setMinusSign(final char minusSign) {
            this.minusSign = minusSign;
        }

        @Override
        public String getExponentSeparator() {
            return exponentSeparator;
        }

        public void setExponentSeparator(final String exponentSeparator) {
            this.exponentSeparator = exponentSeparator;
        }
    }
}
