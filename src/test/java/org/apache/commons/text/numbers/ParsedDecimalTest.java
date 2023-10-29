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

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.function.BiFunction;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class ParsedDecimalTest {

    private static final class FormatOptionsImpl implements ParsedDecimal.FormatOptions {

        private boolean includeFractionPlaceholder = true;

        private boolean signedZero = true;

        private char[] digits = "0123456789".toCharArray();

        private char decimalSeparator = '.';

        private char thousandsGroupingSeparator = ',';

        private boolean groupThousands;

        private char minusSign = '-';

        private String exponentSeparator = "E";

        private boolean alwaysIncludeExponent;

        @Override
        public char getDecimalSeparator() {
            return decimalSeparator;
        }

        @Override
        public char[] getDigits() {
            return digits;
        }

        @Override
        public char[] getExponentSeparatorChars() {
            return exponentSeparator.toCharArray();
        }

        @Override
        public char getGroupingSeparator() {
            return thousandsGroupingSeparator;
        }

        @Override
        public char getMinusSign() {
            return minusSign;
        }

        @Override
        public boolean isAlwaysIncludeExponent() {
            return alwaysIncludeExponent;
        }

        @Override
        public boolean isGroupThousands() {
            return groupThousands;
        }

        @Override
        public boolean isIncludeFractionPlaceholder() {
            return includeFractionPlaceholder;
        }

        @Override
        public boolean isSignedZero() {
            return signedZero;
        }

        public void setAlwaysIncludeExponent(final boolean alwaysIncludeExponent) {
            this.alwaysIncludeExponent = alwaysIncludeExponent;
        }

        public void setDecimalSeparator(final char decimalSeparator) {
            this.decimalSeparator = decimalSeparator;
        }

        public void setDigitsFromString(final String digits) {
            this.digits = digits.toCharArray();
        }

        public void setExponentSeparator(final String exponentSeparator) {
            this.exponentSeparator = exponentSeparator;
        }

        public void setGroupThousands(final boolean groupThousands) {
            this.groupThousands = groupThousands;
        }

        public void setIncludeFractionPlaceholder(final boolean includeFractionPlaceholder) {
            this.includeFractionPlaceholder = includeFractionPlaceholder;
        }

        public void setMinusSign(final char minusSign) {
            this.minusSign = minusSign;
        }

        public void setSignedZero(final boolean signedZero) {
            this.signedZero = signedZero;
        }

        public void setThousandsGroupingSeparator(final char thousandsGroupingSeparator) {
            this.thousandsGroupingSeparator = thousandsGroupingSeparator;
        }
    }

    private static void assertMaxPrecision(final double d, final int maxPrecision,
            final boolean negative, final String digits, final int exponent) {
        final ParsedDecimal dec = ParsedDecimal.from(d);
        dec.maxPrecision(maxPrecision);

        assertSimpleDecimal(dec, negative, digits, exponent);
    }

    private static void assertRound(final double d, final int roundExponent,
            final boolean negative, final String digits, final int exponent) {
        final ParsedDecimal dec = ParsedDecimal.from(d);
        dec.round(roundExponent);

        assertSimpleDecimal(dec, negative, digits, exponent);
    }

    private static void assertSimpleDecimal(final ParsedDecimal parsed, final boolean negative, final String digits,
            final int exponent) {
        Assertions.assertEquals(negative, parsed.negative);
        Assertions.assertEquals(digits, digitString(parsed));
        Assertions.assertEquals(exponent, parsed.getExponent());
        Assertions.assertEquals(digits.length(), parsed.digitCount);
        Assertions.assertEquals(exponent, parsed.getScientificExponent() - digits.length() + 1);
    }

    private static void assertThrowsWithMessage(final Executable fn, final Class<? extends Throwable> type,
            final String msg) {
        final Throwable exc = Assertions.assertThrows(type, fn);
        Assertions.assertEquals(msg, exc.getMessage());
    }

    private static void checkFrom(final double d, final String digits, final int exponent) {
        final boolean negative = Math.signum(d) < 0;

        assertSimpleDecimal(ParsedDecimal.from(d), negative, digits, exponent);
        assertSimpleDecimal(ParsedDecimal.from(-d), !negative, digits, exponent);
    }

    private static void checkToEngineeringString(final double d, final String expected,
            final ParsedDecimal.FormatOptions opts) {
        checkToStringMethod(d, expected, ParsedDecimal::toEngineeringString, opts);

        // check the exponent value to make sure it is a multiple of 3
        final String pos = ParsedDecimal.from(d).toEngineeringString(opts);
        Assertions.assertEquals(0, parseExponent(pos, opts) % 3);

        final String neg = ParsedDecimal.from(-d).toEngineeringString(opts);
        Assertions.assertEquals(0, parseExponent(neg, opts) % 3);
    }

    private static void checkToPlainString(final double d, final String expected,
            final ParsedDecimal.FormatOptions opts) {
        checkToStringMethod(d, expected, ParsedDecimal::toPlainString, opts);
    }

    private static void checkToScientificString(final double d, final String expected,
            final ParsedDecimal.FormatOptions opts) {
        checkToStringMethod(d, expected, ParsedDecimal::toScientificString, opts);
    }

    private static void checkToStringMethod(final double d, final String expected,
            final BiFunction<ParsedDecimal, ParsedDecimal.FormatOptions, String> fn,
            final ParsedDecimal.FormatOptions opts) {

        final ParsedDecimal pos = ParsedDecimal.from(d);
        final String actual = fn.apply(pos, opts);

        Assertions.assertEquals(expected, actual);
    }

    private static double createRandomDouble(final UniformRandomProvider rng) {
        final long mask = (1L << 52) - 1 | 1L << 63;
        final long bits = rng.nextLong() & mask;
        final long exp = rng.nextInt(2045) + 1;
        return Double.longBitsToDouble(bits | exp << 52);
    }

    /** Get the raw digits in the given decimal as a string.
     * @param dec decimal instancE
     * @return decimal digits as a string
     */
    private static String digitString(final ParsedDecimal dec) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dec.digitCount; ++i) {
            sb.append(dec.digits[i]);
        }
        return sb.toString();
    }

    private static int parseExponent(final String str, final ParsedDecimal.FormatOptions opts) {
        final char[] expSep = opts.getExponentSeparatorChars();

        final int expStartIdx = str.indexOf(String.valueOf(expSep));
        if (expStartIdx > -1) {
            int expIdx = expStartIdx + expSep.length;

            boolean neg = false;
            if (str.charAt(expIdx) == opts.getMinusSign()) {
                ++expIdx;
                neg = true;
            }

            final String expStr = str.substring(expIdx);
            final int val = Integer.parseInt(expStr);
            return neg
                    ? -val
                    : val;
        }

        return 0;
    }

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
    void testFromNotFinite() {
        // arrange
        final String msg = "Double is not finite";

        // act/assert
        assertThrowsWithMessage(() -> ParsedDecimal.from(Double.NaN),
                IllegalArgumentException.class, msg);
        assertThrowsWithMessage(() -> ParsedDecimal.from(Double.NEGATIVE_INFINITY),
                IllegalArgumentException.class, msg);
        assertThrowsWithMessage(() -> ParsedDecimal.from(Double.POSITIVE_INFINITY),
                IllegalArgumentException.class, msg);
    }

    @Test
    void testIsZero() {
        // act/assert
        Assertions.assertTrue(ParsedDecimal.from(0.0).isZero());
        Assertions.assertTrue(ParsedDecimal.from(-0.0).isZero());

        Assertions.assertFalse(ParsedDecimal.from(1.0).isZero());
        Assertions.assertFalse(ParsedDecimal.from(-1.0).isZero());

        Assertions.assertFalse(ParsedDecimal.from(Double.MIN_NORMAL).isZero());
        Assertions.assertFalse(ParsedDecimal.from(-Double.MIN_NORMAL).isZero());

        Assertions.assertFalse(ParsedDecimal.from(Double.MAX_VALUE).isZero());
        Assertions.assertFalse(ParsedDecimal.from(-Double.MIN_VALUE).isZero());
    }

    @Test
    void testMaxPrecision() {
        // arrange
        final double d = 1.02576552;

        // act
        assertMaxPrecision(d, 10, false, "102576552", -8);
        assertMaxPrecision(d, 9, false, "102576552", -8);
        assertMaxPrecision(d, 8, false, "10257655", -7);
        assertMaxPrecision(d, 7, false, "1025766", -6);
        assertMaxPrecision(d, 6, false, "102577", -5);
        assertMaxPrecision(d, 5, false, "10258", -4);
        assertMaxPrecision(d, 4, false, "1026", -3);
        assertMaxPrecision(d, 3, false, "103", -2);
        assertMaxPrecision(d, 2, false, "1", 0);
        assertMaxPrecision(d, 1, false, "1", 0);

        assertMaxPrecision(d, 0, false, "102576552", -8);
    }

    @Test
    void testMaxPrecision_carry() {
        // arrange
        final double d = -999.0999e50;

        // act
        assertMaxPrecision(d, 8, true, "9990999", 46);
        assertMaxPrecision(d, 7, true, "9990999", 46);
        assertMaxPrecision(d, 6, true, "9991", 49);
        assertMaxPrecision(d, 5, true, "9991", 49);
        assertMaxPrecision(d, 4, true, "9991", 49);
        assertMaxPrecision(d, 3, true, "999", 50);
        assertMaxPrecision(d, 2, true, "1", 53);
        assertMaxPrecision(d, 1, true, "1", 53);

        assertMaxPrecision(d, 0, true, "9990999", 46);
    }

    @Test
    void testMaxPrecision_halfEvenRounding() {
        // act/assert
        // Test values taken from RoundingMode.HALF_EVEN javadocs
        assertMaxPrecision(5.5, 1, false, "6", 0);
        assertMaxPrecision(2.5, 1, false, "2", 0);
        assertMaxPrecision(1.6, 1, false, "2", 0);
        assertMaxPrecision(1.1, 1, false, "1", 0);
        assertMaxPrecision(1.0, 1, false, "1", 0);

        assertMaxPrecision(-1.0, 1, true, "1", 0);
        assertMaxPrecision(-1.1, 1, true, "1", 0);
        assertMaxPrecision(-1.6, 1, true, "2", 0);
        assertMaxPrecision(-2.5, 1, true, "2", 0);
        assertMaxPrecision(-5.5, 1, true, "6", 0);
    }

    @Test
    void testMaxPrecision_random() {
        // arrange
        final UniformRandomProvider rand = RandomSource.create(RandomSource.XO_RO_SHI_RO_128_PP, 0L);
        final ParsedDecimal.FormatOptions opts = new FormatOptionsImpl();

        for (int i = 0; i < 10_000; ++i) {
            final double d = createRandomDouble(rand);
            final int precision = rand.nextInt(20) + 1;
            final MathContext ctx = new MathContext(precision, RoundingMode.HALF_EVEN);

            final ParsedDecimal dec = ParsedDecimal.from(d);

            // act
            dec.maxPrecision(precision);

            // assert
            Assertions.assertEquals(new BigDecimal(Double.toString(d), ctx).doubleValue(),
                    Double.parseDouble(dec.toScientificString(opts)));
        }
    }

    @Test
    void testMaxPrecision_singleDigits() {
        // act
        assertMaxPrecision(9.0, 1, false, "9", 0);
        assertMaxPrecision(1.0, 1, false, "1", 0);
        assertMaxPrecision(0.0, 1, false, "0", 0);
        assertMaxPrecision(-0.0, 1, true, "0", 0);
        assertMaxPrecision(-1.0, 1, true, "1", 0);
        assertMaxPrecision(-9.0, 1, true, "9", 0);
    }

    @Test
    void testRound_mixed() {
        // arrange
        final double a = 9.94e-10;
        final double b = -3.1415;
        final double c = 5.55e10;

        // act/assert
        assertRound(a, -13, false, "994", -12);
        assertRound(a, -12, false, "994", -12);
        assertRound(a, -11, false, "99", -11);
        assertRound(a, -10, false, "1", -9);
        assertRound(a, -9, false, "1", -9);
        assertRound(a, -8, false, "0", 0);

        assertRound(b, -5, true, "31415", -4);
        assertRound(b, -4, true, "31415", -4);
        assertRound(b, -3, true, "3142", -3);
        assertRound(b, -2, true, "314", -2);
        assertRound(b, -1, true, "31", -1);
        assertRound(b, 0, true, "3", 0);
        assertRound(b, 1, true, "0", 0);
        assertRound(b, 2, true, "0", 0);

        assertRound(c, 7, false, "555", 8);
        assertRound(c, 8, false, "555", 8);
        assertRound(c, 9, false, "56", 9);
        assertRound(c, 10, false, "6", 10);
        assertRound(c, 11, false, "1", 11);
        assertRound(c, 12, false, "0", 0);
    }

    @Test
    void testRound_nine() {
        // arrange
        final double a = 9e-10;
        final double b = -9;
        final double c = 9e10;

        // act/assert
        assertRound(a, -11, false, "9", -10);
        assertRound(a, -10, false, "9", -10);
        assertRound(a, -9, false, "1", -9);

        assertRound(b, -1, true, "9", 0);
        assertRound(b, 0, true, "9", 0);
        assertRound(b, 1, true, "1", 1);

        assertRound(c, 9, false, "9", 10);
        assertRound(c, 10, false, "9", 10);
        assertRound(c, 11, false, "1", 11);
    }

    @Test
    void testRound_one() {
        // arrange
        final double a = 1e-10;
        final double b = -1;
        final double c = 1e10;

        // act/assert
        assertRound(a, -11, false, "1", -10);
        assertRound(a, -10, false, "1", -10);
        assertRound(a, -9, false, "0", 0);

        assertRound(b, -1, true, "1", 0);
        assertRound(b, 0, true, "1", 0);
        assertRound(b, 1, true, "0", 0);

        assertRound(c, 9, false, "1", 10);
        assertRound(c, 10, false, "1", 10);
        assertRound(c, 11, false, "0", 0);
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
            Assertions.assertEquals(d, Double.parseDouble(ParsedDecimal.from(d).toScientificString(stdOpts)));
            Assertions.assertEquals(d, Double.parseDouble(ParsedDecimal.from(d).toScientificString(altOpts)));

            Assertions.assertEquals(d, Double.parseDouble(ParsedDecimal.from(d).toEngineeringString(stdOpts)));
            Assertions.assertEquals(d, Double.parseDouble(ParsedDecimal.from(d).toEngineeringString(altOpts)));

            Assertions.assertEquals(d, Double.parseDouble(ParsedDecimal.from(d).toPlainString(stdOpts)));
            Assertions.assertEquals(d, Double.parseDouble(ParsedDecimal.from(d).toPlainString(altOpts)));
        }
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

        Assertions.assertEquals(10.0, Double.parseDouble(ParsedDecimal.from(10.0).toScientificString(stdOpts)));

        for (double d = min; d <= max; d += delta) {
            // act/assert
            Assertions.assertEquals(d, Double.parseDouble(ParsedDecimal.from(d).toScientificString(stdOpts)));
            Assertions.assertEquals(d, Double.parseDouble(ParsedDecimal.from(d).toScientificString(altOpts)));

            Assertions.assertEquals(d, Double.parseDouble(ParsedDecimal.from(d).toEngineeringString(stdOpts)));
            Assertions.assertEquals(d, Double.parseDouble(ParsedDecimal.from(d).toEngineeringString(altOpts)));

            Assertions.assertEquals(d, Double.parseDouble(ParsedDecimal.from(d).toPlainString(stdOpts)));
            Assertions.assertEquals(d, Double.parseDouble(ParsedDecimal.from(d).toPlainString(altOpts)));
        }
    }

    @Test
    void testStringMethods_customDigits() {
        // arrange
        final FormatOptionsImpl opts = new FormatOptionsImpl();
        opts.setDigitsFromString("abcdefghij");

        // act/assert
        Assertions.assertEquals("b.a", ParsedDecimal.from(1.0).toPlainString(opts));
        Assertions.assertEquals("-a.abcd", ParsedDecimal.from(-0.0123).toPlainString(opts));
        Assertions.assertEquals("bc.de", ParsedDecimal.from(12.34).toPlainString(opts));
        Assertions.assertEquals("baaaa.a", ParsedDecimal.from(10000).toPlainString(opts));
        Assertions.assertEquals("jihgfedcba.a", ParsedDecimal.from(9876543210d).toPlainString(opts));

        Assertions.assertEquals("b.a", ParsedDecimal.from(1.0).toScientificString(opts));
        Assertions.assertEquals("-b.cdE-c", ParsedDecimal.from(-0.0123).toScientificString(opts));
        Assertions.assertEquals("b.cdeEb", ParsedDecimal.from(12.34).toScientificString(opts));
        Assertions.assertEquals("b.aEe", ParsedDecimal.from(10000).toScientificString(opts));
        Assertions.assertEquals("j.ihgfedcbEj", ParsedDecimal.from(9876543210d).toScientificString(opts));

        Assertions.assertEquals("b.a", ParsedDecimal.from(1.0).toEngineeringString(opts));
        Assertions.assertEquals("-bc.dE-d", ParsedDecimal.from(-0.0123).toEngineeringString(opts));
        Assertions.assertEquals("bc.de", ParsedDecimal.from(12.34).toEngineeringString(opts));
        Assertions.assertEquals("ba.aEd", ParsedDecimal.from(10000).toEngineeringString(opts));
        Assertions.assertEquals("j.ihgfedcbEj", ParsedDecimal.from(9876543210d).toEngineeringString(opts));
    }

    @Test
    void testToEngineeringString_altFormat() {
        // arrange
        final FormatOptionsImpl opts = new FormatOptionsImpl();
        opts.setIncludeFractionPlaceholder(false);
        opts.setSignedZero(false);
        opts.setDecimalSeparator(',');
        opts.setMinusSign('!');
        opts.setExponentSeparator("x10^");
        opts.setAlwaysIncludeExponent(true);

        // act/assert
        checkToEngineeringString(0.0, "0x10^0", opts);
        checkToEngineeringString(-0.0, "0x10^0", opts);
        checkToEngineeringString(1.0, "1x10^0", opts);
        checkToEngineeringString(1.5, "1,5x10^0", opts);

        checkToEngineeringString(10, "10x10^0", opts);

        checkToEngineeringString(-0.000000123, "!123x10^!9", opts);
        checkToEngineeringString(12300000, "12,3x10^6", opts);

        checkToEngineeringString(Math.PI, "3,141592653589793x10^0", opts);
        checkToEngineeringString(Math.E, "2,718281828459045x10^0", opts);

        checkToEngineeringString(-Double.MAX_VALUE, "!179,76931348623157x10^306", opts);
        checkToEngineeringString(Double.MIN_VALUE, "4,9x10^!324", opts);
        checkToEngineeringString(Double.MIN_NORMAL, "22,250738585072014x10^!309", opts);
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
    void testToPlainString_altFormat() {
        // arrange
        final FormatOptionsImpl opts = new FormatOptionsImpl();
        opts.setIncludeFractionPlaceholder(false);
        opts.setSignedZero(false);
        opts.setDecimalSeparator(',');
        opts.setMinusSign('!');
        opts.setThousandsGroupingSeparator('_');
        opts.setGroupThousands(true);

        // act/assert
        checkToPlainString(0.0, "0", opts);
        checkToPlainString(-0.0, "0", opts);
        checkToPlainString(1.0, "1", opts);
        checkToPlainString(1.5, "1,5", opts);

        checkToPlainString(12, "12", opts);
        checkToPlainString(123, "123", opts);
        checkToPlainString(1234, "1_234", opts);
        checkToPlainString(12345, "12_345", opts);
        checkToPlainString(123456, "123_456", opts);
        checkToPlainString(1234567, "1_234_567", opts);
        checkToPlainString(12345678, "12_345_678", opts);
        checkToPlainString(123456789, "123_456_789", opts);
        checkToPlainString(1234567890, "1_234_567_890", opts);

        checkToPlainString(-0.000123, "!0,000123", opts);
        checkToPlainString(12301, "12_301", opts);

        checkToPlainString(Math.PI, "3,141592653589793", opts);
        checkToPlainString(Math.E, "2,718281828459045", opts);

        checkToPlainString(-12345.6789, "!12_345,6789", opts);
        checkToPlainString(1.23e12, "1_230_000_000_000", opts);
        checkToPlainString(1.23e-12, "0,00000000000123", opts);
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

        checkToPlainString(12, "12.0", opts);
        checkToPlainString(123, "123.0", opts);
        checkToPlainString(1234, "1234.0", opts);
        checkToPlainString(12345, "12345.0", opts);
        checkToPlainString(123456, "123456.0", opts);
        checkToPlainString(1234567, "1234567.0", opts);
        checkToPlainString(12345678, "12345678.0", opts);
        checkToPlainString(123456789, "123456789.0", opts);
        checkToPlainString(1234567890, "1234567890.0", opts);

        checkToPlainString(-0.000123, "-0.000123", opts);
        checkToPlainString(12301, "12301.0", opts);

        checkToPlainString(Math.PI, "3.141592653589793", opts);
        checkToPlainString(Math.E, "2.718281828459045", opts);

        checkToPlainString(-12345.6789, "-12345.6789", opts);
        checkToPlainString(1.23e12, "1230000000000.0", opts);
        checkToPlainString(1.23e-12, "0.00000000000123", opts);
    }

    @Test
    void testToScientificString_altFormats() {
        // arrange
        final FormatOptionsImpl opts = new FormatOptionsImpl();
        opts.setIncludeFractionPlaceholder(false);
        opts.setSignedZero(false);
        opts.setDecimalSeparator(',');
        opts.setMinusSign('!');
        opts.setExponentSeparator("x10^");
        opts.setAlwaysIncludeExponent(true);

        // act/assert
        checkToScientificString(0.0, "0x10^0", opts);
        checkToScientificString(-0.0, "0x10^0", opts);
        checkToScientificString(1.0, "1x10^0", opts);
        checkToScientificString(1.5, "1,5x10^0", opts);

        checkToScientificString(-0.000123, "!1,23x10^!4", opts);
        checkToScientificString(12301, "1,2301x10^4", opts);

        checkToScientificString(Math.PI, "3,141592653589793x10^0", opts);
        checkToScientificString(Math.E, "2,718281828459045x10^0", opts);

        checkToScientificString(-Double.MAX_VALUE, "!1,7976931348623157x10^308", opts);
        checkToScientificString(Double.MIN_VALUE, "4,9x10^!324", opts);
        checkToScientificString(Double.MIN_NORMAL, "2,2250738585072014x10^!308", opts);
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
}
