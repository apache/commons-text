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

class ParsedDoubleTest {

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
        assertThrowsWithMessage(() -> ParsedDouble.from(Double.NaN),
                IllegalArgumentException.class, msg);
        assertThrowsWithMessage(() -> ParsedDouble.from(Double.NEGATIVE_INFINITY),
                IllegalArgumentException.class, msg);
        assertThrowsWithMessage(() -> ParsedDouble.from(Double.POSITIVE_INFINITY),
                IllegalArgumentException.class, msg);
    }

    @Test
    void testIsZero() {
        // act/assert
        Assertions.assertTrue(ParsedDouble.from(0.0).isZero());
        Assertions.assertTrue(ParsedDouble.from(-0.0).isZero());

        Assertions.assertFalse(ParsedDouble.from(1.0).isZero());
        Assertions.assertFalse(ParsedDouble.from(-1.0).isZero());

        Assertions.assertFalse(ParsedDouble.from(Double.MIN_NORMAL).isZero());
        Assertions.assertFalse(ParsedDouble.from(-Double.MIN_NORMAL).isZero());

        Assertions.assertFalse(ParsedDouble.from(Double.MAX_VALUE).isZero());
        Assertions.assertFalse(ParsedDouble.from(-Double.MIN_VALUE).isZero());
    }

    @Test
    void testRound_one() {
        // arrange
        final ParsedDouble a = ParsedDouble.from(1e-10);
        final ParsedDouble b = ParsedDouble.from(-1);
        final ParsedDouble c = ParsedDouble.from(1e10);

        // act/assert
        assertParsedDouble(a.round(-11), false, "1", -10);
        assertParsedDouble(a.round(-10), false, "1", -10);
        assertParsedDouble(a.round(-9), false, "0", 0);

        assertParsedDouble(b.round(-1), true, "1", 0);
        assertParsedDouble(b.round(0), true, "1", 0);
        assertParsedDouble(b.round(1), false, "0", 0);

        assertParsedDouble(c.round(9), false, "1", 10);
        assertParsedDouble(c.round(10), false, "1", 10);
        assertParsedDouble(c.round(11), false, "0", 0);
    }

    @Test
    void testRound_nine() {
        // arrange
        final ParsedDouble a = ParsedDouble.from(9e-10);
        final ParsedDouble b = ParsedDouble.from(-9);
        final ParsedDouble c = ParsedDouble.from(9e10);

        // act/assert
        assertParsedDouble(a.round(-11), false, "9", -10);
        assertParsedDouble(a.round(-10), false, "9", -10);
        assertParsedDouble(a.round(-9), false, "1", -9);

        assertParsedDouble(b.round(-1), true, "9", 0);
        assertParsedDouble(b.round(0), true, "9", 0);
        assertParsedDouble(b.round(1), true, "1", 1);

        assertParsedDouble(c.round(9), false, "9", 10);
        assertParsedDouble(c.round(10), false, "9", 10);
        assertParsedDouble(c.round(11), false, "1", 11);
    }

    @Test
    void testRound_mixed() {
        // arrange
        final ParsedDouble a = ParsedDouble.from(9.94e-10);
        final ParsedDouble b = ParsedDouble.from(-3.1415);
        final ParsedDouble c = ParsedDouble.from(5.55e10);

        // act/assert
        assertParsedDouble(a.round(-13), false, "994", -12);
        assertParsedDouble(a.round(-12), false, "994", -12);
        assertParsedDouble(a.round(-11), false, "99", -11);
        assertParsedDouble(a.round(-10), false, "1", -9);
        assertParsedDouble(a.round(-9), false, "1", -9);
        assertParsedDouble(a.round(-8), false, "0", 0);

        assertParsedDouble(b.round(-5), true, "31415", -4);
        assertParsedDouble(b.round(-4), true, "31415", -4);
        assertParsedDouble(b.round(-3), true, "3142", -3);
        assertParsedDouble(b.round(-2), true, "314", -2);
        assertParsedDouble(b.round(-1), true, "31", -1);
        assertParsedDouble(b.round(0), true, "3", 0);
        assertParsedDouble(b.round(1), false, "0", 0);
        assertParsedDouble(b.round(2), false, "0", 0);

        assertParsedDouble(c.round(7), false, "555", 8);
        assertParsedDouble(c.round(8), false, "555", 8);
        assertParsedDouble(c.round(9), false, "56", 9);
        assertParsedDouble(c.round(10), false, "6", 10);
        assertParsedDouble(c.round(11), false, "1", 11);
        assertParsedDouble(c.round(12), false, "0", 0);
    }

    @Test
    void testMaxPrecision() {
        // arrange
        final ParsedDouble d = ParsedDouble.from(1.02576552);

        // act
        assertParsedDouble(d.maxPrecision(10), false, "102576552", -8);
        assertParsedDouble(d.maxPrecision(9), false, "102576552", -8);
        assertParsedDouble(d.maxPrecision(8), false, "10257655", -7);
        assertParsedDouble(d.maxPrecision(7), false, "1025766", -6);
        assertParsedDouble(d.maxPrecision(6), false, "102577", -5);
        assertParsedDouble(d.maxPrecision(5), false, "10258", -4);
        assertParsedDouble(d.maxPrecision(4), false, "1026", -3);
        assertParsedDouble(d.maxPrecision(3), false, "103", -2);
        assertParsedDouble(d.maxPrecision(2), false, "1", 0);
        assertParsedDouble(d.maxPrecision(1), false, "1", 0);
    }

    @Test
    void testMaxPrecision_carry() {
        // arrange
        final ParsedDouble d = ParsedDouble.from(-999.0999e50);

        // act
        assertParsedDouble(d.maxPrecision(8), true, "9990999", 46);
        assertParsedDouble(d.maxPrecision(7), true, "9990999", 46);
        assertParsedDouble(d.maxPrecision(6), true, "9991", 49);
        assertParsedDouble(d.maxPrecision(5), true, "9991", 49);
        assertParsedDouble(d.maxPrecision(4), true, "9991", 49);
        assertParsedDouble(d.maxPrecision(3), true, "999", 50);
        assertParsedDouble(d.maxPrecision(2), true, "1", 53);
        assertParsedDouble(d.maxPrecision(1), true, "1", 53);
    }

    @Test
    void testMaxPrecision_halfEvenRounding() {
        // act/assert
        // Test values taken from RoundingMode.HALF_EVEN javadocs
        assertParsedDouble(ParsedDouble.from(5.5).maxPrecision(1), false, "6", 0);
        assertParsedDouble(ParsedDouble.from(2.5).maxPrecision(1), false, "2", 0);
        assertParsedDouble(ParsedDouble.from(1.6).maxPrecision(1), false, "2", 0);
        assertParsedDouble(ParsedDouble.from(1.1).maxPrecision(1), false, "1", 0);
        assertParsedDouble(ParsedDouble.from(1.0).maxPrecision(1), false, "1", 0);

        assertParsedDouble(ParsedDouble.from(-1.0).maxPrecision(1), true, "1", 0);
        assertParsedDouble(ParsedDouble.from(-1.1).maxPrecision(1), true, "1", 0);
        assertParsedDouble(ParsedDouble.from(-1.6).maxPrecision(1), true, "2", 0);
        assertParsedDouble(ParsedDouble.from(-2.5).maxPrecision(1), true, "2", 0);
        assertParsedDouble(ParsedDouble.from(-5.5).maxPrecision(1), true, "6", 0);
    }

    @Test
    void testMaxPrecision_singleDigits() {
        // act
        assertParsedDouble(ParsedDouble.from(9.0).maxPrecision(1), false, "9", 0);
        assertParsedDouble(ParsedDouble.from(1.0).maxPrecision(1), false, "1", 0);
        assertParsedDouble(ParsedDouble.from(0.0).maxPrecision(1), false, "0", 0);
        assertParsedDouble(ParsedDouble.from(-0.0).maxPrecision(1), true, "0", 0);
        assertParsedDouble(ParsedDouble.from(-1.0).maxPrecision(1), true, "1", 0);
        assertParsedDouble(ParsedDouble.from(-9.0).maxPrecision(1), true, "9", 0);
    }

    @Test
    void testMaxPrecision_random() {
        // arrange
        final UniformRandomProvider rand = RandomSource.create(RandomSource.XO_RO_SHI_RO_128_PP, 0L);

        double d;
        int precision;
        ParsedDouble result;
        MathContext ctx;
        for (int i = 0; i < 10_000; ++i) {
            d = createRandomDouble(rand);
            precision = rand.nextInt(20) + 1;
            ctx = new MathContext(precision, RoundingMode.HALF_EVEN);

            // act
            result = ParsedDouble.from(d).maxPrecision(precision);

            // assert
            Assertions.assertEquals(new BigDecimal(Double.toString(d), ctx).doubleValue(),
                    Double.parseDouble(result.toScientificString(true)));
        }
    }

    @Test
    void testMaxPrecision_invalidArg() {
        // arrange
        final ParsedDouble d = ParsedDouble.from(10);
        final String baseMsg = "Precision must be greater than zero; was ";

        // act/assert
        assertThrowsWithMessage(() -> d.maxPrecision(0), IllegalArgumentException.class, baseMsg + "0");
        assertThrowsWithMessage(() -> d.maxPrecision(-1), IllegalArgumentException.class, baseMsg + "-1");
    }

    @Test
    void testToPlainString() {
        // act/assert
        checkToPlainString(0.0, "0.0", "0");
        checkToPlainString(1.0, "1.0", "1");
        checkToPlainString(1.5, "1.5");

        checkToPlainString(0.000123, "0.000123");
        checkToPlainString(12300, "12300.0", "12300");

        checkToPlainString(Math.PI, "3.141592653589793");
        checkToPlainString(Math.E, "2.718281828459045");

        checkToPlainString(12345.6789, "12345.6789");
        checkToPlainString(1.23e12, "1230000000000.0", "1230000000000");
        checkToPlainString(1.23e-12, "0.00000000000123");
    }

    @Test
    void testToScientificString() {
        // act/assert
        checkToScientificString(0.0, "0.0", "0");
        checkToScientificString(1.0, "1.0", "1");
        checkToScientificString(1.5, "1.5");

        checkToScientificString(0.000123, "1.23E-4");
        checkToScientificString(12300, "1.23E4");

        checkToScientificString(Math.PI, "3.141592653589793");
        checkToScientificString(Math.E, "2.718281828459045");

        checkToScientificString(Double.MAX_VALUE, "1.7976931348623157E308");
        checkToScientificString(Double.MIN_VALUE, "4.9E-324");
        checkToScientificString(Double.MIN_NORMAL, "2.2250738585072014E-308");
    }

    @Test
    void testToEngineeringString() {
        // act/assert
        checkToEngineeringString(0.0, "0.0", "0");
        checkToEngineeringString(1.0, "1.0", "1");
        checkToEngineeringString(1.5, "1.5");

        checkToEngineeringString(10, "10.0", "10");

        checkToEngineeringString(0.000000123, "123.0E-9", "123E-9");
        checkToEngineeringString(12300000, "12.3E6");

        checkToEngineeringString(Math.PI, "3.141592653589793");
        checkToEngineeringString(Math.E, "2.718281828459045");

        checkToEngineeringString(Double.MAX_VALUE, "179.76931348623157E306");
        checkToEngineeringString(Double.MIN_VALUE, "4.9E-324");
        checkToEngineeringString(Double.MIN_NORMAL, "22.250738585072014E-309");
    }

    @Test
    void testStringMethodAccuracy_sequence() {
        // arrange
        final double min = -1000;
        final double max = 1000;
        final double delta = 0.1;

        Assertions.assertEquals(10.0, Double.parseDouble(ParsedDouble.from(10.0).toEngineeringString(true)));

        for (double d = min; d <= max; d += delta) {
            // act/assert
            Assertions.assertEquals(d, Double.parseDouble(ParsedDouble.from(d).toScientificString(true)));
            Assertions.assertEquals(d, Double.parseDouble(ParsedDouble.from(d).toScientificString(false)));

            Assertions.assertEquals(d, Double.parseDouble(ParsedDouble.from(d).toEngineeringString(true)));
            Assertions.assertEquals(d, Double.parseDouble(ParsedDouble.from(d).toEngineeringString(false)));

            Assertions.assertEquals(d, Double.parseDouble(ParsedDouble.from(d).toPlainString(true)));
            Assertions.assertEquals(d, Double.parseDouble(ParsedDouble.from(d).toPlainString(false)));
        }
    }

    @Test
    void testStringMethodAccuracy_random() {
        // arrange
        final UniformRandomProvider rand = RandomSource.create(RandomSource.XO_RO_SHI_RO_128_PP, 0L);

        double d;
        for (int i = 0; i < 10_000; ++i) {
            d = createRandomDouble(rand);

            // act/assert
            Assertions.assertEquals(d, Double.parseDouble(ParsedDouble.from(d).toScientificString(true)));
            Assertions.assertEquals(d, Double.parseDouble(ParsedDouble.from(d).toScientificString(false)));

            Assertions.assertEquals(d, Double.parseDouble(ParsedDouble.from(d).toEngineeringString(true)));
            Assertions.assertEquals(d, Double.parseDouble(ParsedDouble.from(d).toEngineeringString(false)));

            Assertions.assertEquals(d, Double.parseDouble(ParsedDouble.from(d).toPlainString(true)));
            Assertions.assertEquals(d, Double.parseDouble(ParsedDouble.from(d).toPlainString(false)));
        }
    }

    private static void checkFrom(final double d, final String digits, final int exponent) {
        final boolean negative = Math.signum(d) < 0;

        assertParsedDouble(ParsedDouble.from(d), negative, digits, exponent);
        assertParsedDouble(ParsedDouble.from(-d), !negative, digits, exponent);
    }

    private static void checkToPlainString(final double d, final String expected) {
        checkToPlainString(d, expected, expected);
    }

    private static void checkToPlainString(final double d, final String withPlaceholder,
            final String withoutPlaceholder) {
        checkToStringMethod(d, withPlaceholder, withoutPlaceholder, ParsedDouble::toPlainString);
    }

    private static void checkToScientificString(final double d, final String expected) {
        checkToScientificString(d, expected, expected);
    }

    private static void checkToScientificString(final double d, final String withPlaceholder,
            final String withoutPlaceholder) {
        checkToStringMethod(d, withPlaceholder, withoutPlaceholder, ParsedDouble::toScientificString);
    }

    private static void checkToEngineeringString(final double d, final String expected) {
        checkToEngineeringString(d, expected, expected);
    }

    private static void checkToEngineeringString(final double d, final String withPlaceholder,
            final String withoutPlaceholder) {
        checkToStringMethod(d, withPlaceholder, withoutPlaceholder, ParsedDouble::toEngineeringString);

        // check the exponent value to make sure it is a multiple of 3
        final String pos = ParsedDouble.from(d).toEngineeringString(true);
        final int posEIdx = pos.indexOf('E');
        if (posEIdx > -1) {
            Assertions.assertEquals(0, Integer.parseInt(pos.substring(posEIdx + 1)) % 3);
        }

        final String neg = ParsedDouble.from(-d).toEngineeringString(true);
        final int negEIdx = neg.indexOf('E');
        if (negEIdx > -1) {
            Assertions.assertEquals(0, Integer.parseInt(neg.substring(negEIdx + 1)) % 3);
        }
    }

    private static void checkToStringMethod(final double d, final String withPlaceholder,
            final String withoutPlaceholder, final BiFunction<ParsedDouble, Boolean, String> fn) {
        final ParsedDouble pos = ParsedDouble.from(d);

        final String posWith = fn.apply(pos, true);
        Assertions.assertEquals(withPlaceholder, posWith);
        assertDoubleString(d, posWith);

        final String posWithout = fn.apply(pos, false);
        Assertions.assertEquals(withoutPlaceholder, posWithout);
        assertDoubleString(d, posWithout);

        final ParsedDouble neg = ParsedDouble.from(-d);

        final String negWith = fn.apply(neg, true);
        Assertions.assertEquals("-" + withPlaceholder, negWith);
        assertDoubleString(-d, negWith);

        final String negWithout = fn.apply(neg, false);
        Assertions.assertEquals("-" + withoutPlaceholder, negWithout);
        assertDoubleString(-d, negWithout);
    }

    private static void assertDoubleString(final double d, final String str) {
        Assertions.assertEquals(d, Double.parseDouble(str), 0.0);
    }

    private static void assertParsedDouble(final ParsedDouble parsed, final boolean negative, final String digits,
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
}
