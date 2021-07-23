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

import java.text.DecimalFormatSymbols;
import java.util.Objects;
import java.util.function.DoubleFunction;
import java.util.function.Function;

/**
 * Enum containing standard double format types with methods to produce
 * configured formatter instances. This type is intended to provide a
 * quick and convenient way to create lightweight, thread-safe double format functions
 * for common format types using a builder pattern. Output can be localized by
 * passing a {@link DecimalFormatSymbols} instance to the
 * {@link Builder#formatSymbols(DecimalFormatSymbols) formatSymbols} method or by
 * directly calling the various other builder configuration methods, such as
 * {@link Builder#digits(String) digits}.
 *
 * <p><strong>Comparison with DecimalFormat</strong>
 * <p>This type provides some of the same functionality as Java's own
 * {@link java.text.DecimalFormat}. However, unlike {@code DecimalFormat}, the format
 * functions produced by this type are lightweight and thread-safe, making them
 * much easier to work with in multi-threaded environments. They also provide performance
 * comparable to, and in many cases faster than, {@code DecimalFormat}.
 *
 * <p><strong>Examples</strong>
 * <pre>
 * // construct a formatter equivalent to Double.toString()
 * DoubleFunction&lt;String&gt; fmt = DoubleFormat.MIXED.builder().build();
 *
 * // construct a formatter equivalent to Double.toString() but using
 * // format symbols for a specific locale
 * DoubleFunction&lt;String&gt; fmt = DoubleFormat.MIXED.builder()
 *      .formatSymbols(DecimalFormatSymbols.getInstance(locale))
 *      .build();
 *
 * // construct a formatter equivalent to the DecimalFormat pattern "0.0##"
 * DoubleFunction&lt;String&gt; fmt = DoubleFormat.PLAIN.builder()
 *      .minDecimalExponent(-3)
 *      .build();
 *
 * // construct a formatter equivalent to the DecimalFormat pattern "#,##0.0##",
 * // where whole number groups of thousands are separated
 * DoubleFunction&lt;String&gt; fmt = DoubleFormat.PLAIN.builder()
 *      .minDecimalExponent(-3)
 *      .groupThousands(true)
 *      .build();
 *
 * // construct a formatter equivalent to the DecimalFormat pattern "0.0##E0"
 * DoubleFunction&lt;String&gt; fmt = DoubleFormat.SCIENTIFIC.builder()
 *      .maxPrecision(4)
 *      .alwaysIncludeExponent(true)
 *      .build()
 *
 * // construct a formatter equivalent to the DecimalFormat pattern "##0.0##E0",
 * // i.e. "engineering format"
 * DoubleFunction&lt;String&gt; fmt = DoubleFormat.ENGINEERING.builder()
 *      .maxPrecision(6)
 *      .alwaysIncludeExponent(true)
 *      .build()
 * </pre>
 *
 * <p><strong>Implementation Notes</strong>
 * <p>{@link java.math.RoundingMode#HALF_EVEN Half-even} rounding is used in cases where the
 * decimal value must be rounded in order to meet the configuration requirements of the formatter
 * instance.
 */
public enum DoubleFormat {

    /**
     * Number format without exponents.
     * Ex:
     * <pre>
     * 0.0
     * 12.401
     * 100000.0
     * 1450000000.0
     * 0.0000000000123
     * </pre>
     */
    PLAIN(PlainDoubleFormat::new),

    /**
     * Number format that uses exponents and contains a single digit
     * to the left of the decimal point.
     * Ex:
     * <pre>
     * 0.0
     * 1.2401E1
     * 1.0E5
     * 1.45E9
     * 1.23E-11
     * </pre>
     */
    SCIENTIFIC(ScientificDoubleFormat::new),

    /**
     * Number format similar to {@link #SCIENTIFIC scientific format} but adjusted
     * so that the exponent value is always a multiple of 3, allowing easier alignment
     * with SI prefixes.
     * Ex:
     * <pre>
     * 0.0
     * 12.401
     * 100.0E3
     * 1.45E9
     * 12.3E-12
     * </pre>
     */
    ENGINEERING(EngineeringDoubleFormat::new),

    /**
     * Number format that uses {@link #PLAIN plain format} for small numbers and
     * {@link #SCIENTIFIC scientific format} for large numbers. The number thresholds
     * can be configured through the
     * {@link Builder#plainFormatMinDecimalExponent plainFormatMinDecimalExponent}
     * and
     * {@link Builder#plainFormatMaxDecimalExponent plainFormatMaxDecimalExponent}
     * properties.
     * Ex:
     * <pre>
     * 0.0
     * 12.401
     * 100000.0
     * 1.45E9
     * 1.23E-11
     * </pre>
     */
    MIXED(MixedDoubleFormat::new);

    /** Function used to construct instances for this format type. */
    private final Function<Builder, DoubleFunction<String>> factory;

    /**
     * Construct a new instance.
     * @param factory function used to construct format instances
     */
    DoubleFormat(final Function<Builder, DoubleFunction<String>> factory) {
        this.factory = factory;
    }

    /**
     * Return a {@link Builder} for constructing formatter functions for this format type.
     * @return builder instance
     */
    public Builder builder() {
        return new Builder(factory);
    }

    /**
     * Class for constructing configured format functions for standard double format types.
     */
    public static final class Builder {

        /** Default value for the plain format max decimal exponent. */
        private static final int DEFAULT_PLAIN_FORMAT_MAX_DECIMAL_EXPONENT = 6;

        /** Default value for the plain format min decimal exponent. */
        private static final int DEFAULT_PLAIN_FORMAT_MIN_DECIMAL_EXPONENT = -3;

        /** Default decimal digit characters. */
        private static final String DEFAULT_DECIMAL_DIGITS = "0123456789";

        /** Function used to construct format instances. */
        private final Function<Builder, DoubleFunction<String>> factory;

        /** Maximum number of significant decimal digits in formatted strings. */
        private int maxPrecision = 0;

        /** Minimum decimal exponent. */
        private int minDecimalExponent = Integer.MIN_VALUE;

        /** Max decimal exponent to use with plain formatting with the mixed format type. */
        private int plainFormatMaxDecimalExponent = DEFAULT_PLAIN_FORMAT_MAX_DECIMAL_EXPONENT;

        /** Min decimal exponent to use with plain formatting with the mixed format type. */
        private int plainFormatMinDecimalExponent = DEFAULT_PLAIN_FORMAT_MIN_DECIMAL_EXPONENT;

        /** String representing infinity. */
        private String infinity = "Infinity";

        /** String representing NaN. */
        private String nan = "NaN";

        /** Flag determining if fraction placeholders should be used. */
        private boolean fractionPlaceholder = true;

        /** Flag determining if signed zero strings are allowed. */
        private boolean signedZero = true;

        /** String of digit characters 0-9. */
        private String digits = DEFAULT_DECIMAL_DIGITS;

        /** Decimal separator character. */
        private char decimalSeparator = '.';

        /** Character used to separate groups of thousands. */
        private char groupingSeparator = ',';

        /** If {@code true}, thousands groups will be separated by the grouping separator. */
        private boolean groupThousands = false;

        /** Minus sign character. */
        private char minusSign = '-';

        /** Exponent separator character. */
        private String exponentSeparator = "E";

        /** Flag indicating if the exponent value should always be included, even if zero. */
        private boolean alwaysIncludeExponent = false;

        /**
         * Construct a new instance that delegates double function construction
         * to the given factory object.
         * @param factory factory function
         */
        private Builder(final Function<Builder, DoubleFunction<String>> factory) {
            this.factory = factory;
        }

        /**
         * Set the maximum number of significant decimal digits used in format
         * results. A value of {@code 0} indicates no limit. The default value is {@code 0}.
         * @param maxPrecision maximum precision
         * @return this instance
         */
        public Builder maxPrecision(final int maxPrecision) {
            this.maxPrecision = maxPrecision;
            return this;
        }

        /**
         * Set the minimum decimal exponent for formatted strings. No digits with an
         * absolute value of less than <code>10<sup>minDecimalExponent</sup></code> will
         * be included in format results. If the number being formatted does not contain
         * any such digits, then zero is returned. For example, if {@code minDecimalExponent}
         * is set to {@code -2} and the number {@code 3.14159} is formatted, the plain
         * format result will be {@code "3.14"}. If {@code 0.001} is formatted, then the
         * result is the zero string.
         * @param minDecimalExponent minimum decimal exponent
         * @return this instance
         */
        public Builder minDecimalExponent(final int minDecimalExponent) {
            this.minDecimalExponent = minDecimalExponent;
            return this;
        }

        /**
         * Set the maximum decimal exponent for numbers formatted as plain decimal strings when
         * using the {@link DoubleFormat#MIXED MIXED} format type. If the number being formatted
         * has an absolute value less than <code>10<sup>plainFormatMaxDecimalExponent + 1</sup></code> and
         * greater than or equal to <code>10<sup>plainFormatMinDecimalExponent</sup></code> after any
         * necessary rounding, then the formatted result will use the {@link DoubleFormat#PLAIN PLAIN} format type.
         * Otherwise, {@link DoubleFormat#SCIENTIFIC SCIENTIFIC} format will be used. For example,
         * if this value is set to {@code 2}, the number {@code 999} will be formatted as {@code "999.0"}
         * while {@code 1000} will be formatted as {@code "1.0E3"}.
         *
         * <p>The default value is {@value #DEFAULT_PLAIN_FORMAT_MAX_DECIMAL_EXPONENT}.
         *
         * <p>This value is ignored for formats other than {@link DoubleFormat#MIXED}.
         * @param plainFormatMaxDecimalExponent maximum decimal exponent for values formatted as plain
         *      strings when using the {@link DoubleFormat#MIXED MIXED} format type.
         * @return this instance
         * @see #plainFormatMinDecimalExponent(int)
         */
        public Builder plainFormatMaxDecimalExponent(final int plainFormatMaxDecimalExponent) {
            this.plainFormatMaxDecimalExponent = plainFormatMaxDecimalExponent;
            return this;
        }

        /**
         * Set the minimum decimal exponent for numbers formatted as plain decimal strings when
         * using the {@link DoubleFormat#MIXED MIXED} format type. If the number being formatted
         * has an absolute value less than <code>10<sup>plainFormatMaxDecimalExponent + 1</sup></code> and
         * greater than or equal to <code>10<sup>plainFormatMinDecimalExponent</sup></code> after any
         * necessary rounding, then the formatted result will use the {@link DoubleFormat#PLAIN PLAIN} format type.
         * Otherwise, {@link DoubleFormat#SCIENTIFIC SCIENTIFIC} format will be used. For example,
         * if this value is set to {@code -2}, the number {@code 0.01} will be formatted as {@code "0.01"}
         * while {@code 0.0099} will be formatted as {@code "9.9E-3"}.
         *
         * <p>The default value is {@value #DEFAULT_PLAIN_FORMAT_MIN_DECIMAL_EXPONENT}.
         *
         * <p>This value is ignored for formats other than {@link DoubleFormat#MIXED}.
         * @param plainFormatMinDecimalExponent maximum decimal exponent for values formatted as plain
         *      strings when using the {@link DoubleFormat#MIXED MIXED} format type.
         * @return this instance
         * @see #plainFormatMinDecimalExponent(int)
         */
        public Builder plainFormatMinDecimalExponent(final int plainFormatMinDecimalExponent) {
            this.plainFormatMinDecimalExponent = plainFormatMinDecimalExponent;
            return this;
        }

        /**
         * Set the flag determining whether or not the zero string may be returned with the minus
         * sign or if it will always be returned in the positive form. For example, if set to {@code true},
         * the string {@code "-0.0"} may be returned for some input numbers. If {@code false}, only {@code "0.0"}
         * will be returned, regardless of the sign of the input number. The default value is {@code true}.
         * @param signedZero if {@code true}, the zero string may be returned with a preceding minus sign;
         *      if {@code false}, the zero string will only be returned in its positive form
         * @return this instance
         */
        public Builder allowSignedZero(final boolean signedZero) {
            this.signedZero = signedZero;
            return this;
        }

        /**
         * Set the string containing the digit characters 0-9, in that order. The
         * default value is the string {@code "0123456789"}.
         * @param digits string containing the digit characters 0-9
         * @return this instance
         * @throws NullPointerException if the argument is {@code null}
         * @throws IllegalArgumentException if the argument does not have a length of exactly 10
         */
        public Builder digits(final String digits) {
            Objects.requireNonNull(digits, "Digits string cannot be null");
            if (digits.length() != DEFAULT_DECIMAL_DIGITS.length()) {
                throw new IllegalArgumentException("Digits string must contain exactly "
                        + DEFAULT_DECIMAL_DIGITS.length() + " characters.");
            }

            this.digits = digits;
            return this;
        }

        /**
         * Set the flag determining whether or not a zero character is added in the fraction position
         * when no fractional value is present. For example, if set to {@code true}, the number {@code 1} would
         * be formatted as {@code "1.0"}. If {@code false}, it would be formatted as {@code "1"}. The default
         * value is {@code true}.
         * @param fractionPlaceholder if {@code true}, a zero character is placed in the fraction position when
         *      no fractional value is present; if {@code false}, fractional digits are only included when needed
         * @return this instance
         */
        public Builder includeFractionPlaceholder(final boolean fractionPlaceholder) {
            this.fractionPlaceholder = fractionPlaceholder;
            return this;
        }

        /**
         * Set the character used as the minus sign.
         * @param minusSign character to use as the minus sign
         * @return this instance
         */
        public Builder minusSign(final char minusSign) {
            this.minusSign = minusSign;
            return this;
        }

        /**
         * Set the decimal separator character, i.e., the character placed between the
         * whole number and fractional portions of the formatted strings. The default value
         * is {@code '.'}.
         * @param decimalSeparator decimal separator character
         * @return this instance
         */
        public Builder decimalSeparator(final char decimalSeparator) {
            this.decimalSeparator = decimalSeparator;
            return this;
        }

        /**
         * Set the character used to separate groups of thousands. Default value is {@code ','}.
         * @param groupingSeparator character used to separate groups of thousands
         * @return this instance
         * @see #groupThousands(boolean)
         */
        public Builder groupingSeparator(final char groupingSeparator) {
            this.groupingSeparator = groupingSeparator;
            return this;
        }

        /**
         * If set to {@code true}, thousands will be grouped with the
         * {@link #groupingSeparator(char) grouping separator}. For example, if set to {@code true},
         * the number {@code 1000} could be formatted as {@code "1,000"}. This property only applies
         * to the {@link DoubleFormat#PLAIN PLAIN} format. Default value is {@code false}.
         * @param groupThousands if {@code true}, thousands will be grouped
         * @return this instance
         * @see #groupingSeparator(char)
         */
        public Builder groupThousands(final boolean groupThousands) {
            this.groupThousands = groupThousands;
            return this;
        }

        /**
         * Set the exponent separator character, i.e., the string placed between
         * the mantissa and the exponent. The default value is {@code "E"}, as in
         * {@code "1.2E6"}.
         * @param exponentSeparator exponent separator string
         * @return this instance
         * @throws NullPointerException if the argument is {@code null}
         */
        public Builder exponentSeparator(final String exponentSeparator) {
            this.exponentSeparator = Objects.requireNonNull(exponentSeparator, "Exponent separator cannot be null");
            return this;
        }

        /**
         * Set the flag indicating if an exponent value should always be included in the
         * formatted value, even if the exponent value is zero. This property only applies
         * to formats that use scientific notation, namely
         * {@link DoubleFormat#SCIENTIFIC SCIENTIFIC},
         * {@link DoubleFormat#ENGINEERING ENGINEERING}, and
         * {@link DoubleFormat#MIXED MIXED}. The default value is {@code false}.
         * @param alwaysIncludeExponent if {@code true}, exponents will always be included in formatted
         *      output even if the exponent value is zero
         * @return this instance
         */
        public Builder alwaysIncludeExponent(final boolean alwaysIncludeExponent) {
            this.alwaysIncludeExponent = alwaysIncludeExponent;
            return this;
        }

        /**
         * Set the string used to represent infinity. For negative infinity, this string
         * is prefixed with the {@link #minusSign(char) minus sign}.
         * @param infinity string used to represent infinity
         * @return this instance
         * @throws NullPointerException if the argument is {@code null}
         */
        public Builder infinity(final String infinity) {
            this.infinity = Objects.requireNonNull(infinity, "Infinity string cannot be null");
            return this;
        }

        /**
         * Set the string used to represent {@link Double#NaN}.
         * @param nan string used to represent {@link Double#NaN}
         * @return this instance
         * @throws NullPointerException if the argument is {@code null}
         */
        public Builder nan(final String nan) {
            this.nan = Objects.requireNonNull(nan, "NaN string cannot be null");
            return this;
        }

        /**
         * Configure this instance with the given format symbols. The following values
         * are set:
         * <ul>
         *  <li>{@link #digits(String) digit characters}</li>
         *  <li>{@link #decimalSeparator(char) decimal separator}</li>
         *  <li>{@link #groupingSeparator(char) thousands grouping separator}</li>
         *  <li>{@link #minusSign(char) minus sign}</li>
         *  <li>{@link #exponentSeparator(String) exponent separator}</li>
         *  <li>{@link #infinity(String) infinity}</li>
         *  <li>{@link #nan(String) NaN}</li>
         * </ul>
         * The digit character string is constructed by starting at the configured
         * {@link DecimalFormatSymbols#getZeroDigit() zero digit} and adding the next
         * 9 consecutive characters.
         * @param symbols format symbols
         * @return this instance
         * @throws NullPointerException if the argument is {@code null}
         */
        public Builder formatSymbols(final DecimalFormatSymbols symbols) {
            Objects.requireNonNull(symbols, "Decimal format symbols cannot be null");

            return digits(getDigitString(symbols))
                    .decimalSeparator(symbols.getDecimalSeparator())
                    .groupingSeparator(symbols.getGroupingSeparator())
                    .minusSign(symbols.getMinusSign())
                    .exponentSeparator(symbols.getExponentSeparator())
                    .infinity(symbols.getInfinity())
                    .nan(symbols.getNaN());
        }

        /**
         * Get a string containing the localized digits 0-9 for the given symbols object. The
         * string is constructed by starting at the {@link DecimalFormatSymbols#getZeroDigit() zero digit}
         * and adding the next 9 consecutive characters.
         * @param symbols symbols object
         * @return string containing the localized digits 0-9
         */
        private String getDigitString(final DecimalFormatSymbols symbols) {
            final int zeroDelta = symbols.getZeroDigit() - DEFAULT_DECIMAL_DIGITS.charAt(0);

            final char[] digitChars = new char[DEFAULT_DECIMAL_DIGITS.length()];
            for (int i = 0; i < DEFAULT_DECIMAL_DIGITS.length(); ++i) {
                digitChars[i] = (char) (DEFAULT_DECIMAL_DIGITS.charAt(i) + zeroDelta);
            }

            return String.valueOf(digitChars);
        }

        /**
         * Construct a new double format function.
         * @return format function
         */
        public DoubleFunction<String> build() {
            return factory.apply(this);
        }
    }

    /**
     * Base class for standard double formatting classes.
     */
    private abstract static class AbstractDoubleFormat
        implements DoubleFunction<String>, ParsedDecimal.FormatOptions {

        /** Maximum precision; 0 indicates no limit. */
        private final int maxPrecision;

        /** Minimum decimal exponent. */
        private final int minDecimalExponent;

        /** String representing positive infinity. */
        private final String positiveInfinity;

        /** String representing negative infinity. */
        private final String negativeInfinity;

        /** String representing NaN. */
        private final String nan;

        /** Flag determining if fraction placeholders should be used. */
        private final boolean fractionPlaceholder;

        /** Flag determining if signed zero strings are allowed. */
        private final boolean signedZero;

        /** String containing the digits 0-9. */
        private final char[] digits;

        /** Decimal separator character. */
        private final char decimalSeparator;

        /** Thousands grouping separator. */
        private final char groupingSeparator;

        /** Flag indicating if thousands should be grouped. */
        private final boolean groupThousands;

        /** Minus sign character. */
        private final char minusSign;

        /** Exponent separator character. */
        private final char[] exponentSeparatorChars;

        /** Flag indicating if exponent values should always be included, even if zero. */
        private final boolean alwaysIncludeExponent;

        /**
         * Construct a new instance.
         * @param builder builder instance containing configuration values
         */
        AbstractDoubleFormat(final Builder builder) {
            this.maxPrecision = builder.maxPrecision;
            this.minDecimalExponent = builder.minDecimalExponent;

            this.positiveInfinity = builder.infinity;
            this.negativeInfinity = builder.minusSign + builder.infinity;
            this.nan = builder.nan;

            this.fractionPlaceholder = builder.fractionPlaceholder;
            this.signedZero = builder.signedZero;
            this.digits = builder.digits.toCharArray();
            this.decimalSeparator = builder.decimalSeparator;
            this.groupingSeparator = builder.groupingSeparator;
            this.groupThousands = builder.groupThousands;
            this.minusSign = builder.minusSign;
            this.exponentSeparatorChars = builder.exponentSeparator.toCharArray();
            this.alwaysIncludeExponent = builder.alwaysIncludeExponent;
        }

        /** {@inheritDoc} */
        @Override
        public boolean isIncludeFractionPlaceholder() {
            return fractionPlaceholder;
        }

        /** {@inheritDoc} */
        @Override
        public boolean isSignedZero() {
            return signedZero;
        }

        /** {@inheritDoc} */
        @Override
        public char[] getDigits() {
            return digits;
        }

        /** {@inheritDoc} */
        @Override
        public char getDecimalSeparator() {
            return decimalSeparator;
        }

        /** {@inheritDoc} */
        @Override
        public char getGroupingSeparator() {
            return groupingSeparator;
        }

        /** {@inheritDoc} */
        @Override
        public boolean isGroupThousands() {
            return groupThousands;
        }

        /** {@inheritDoc} */
        @Override
        public char getMinusSign() {
            return minusSign;
        }

        /** {@inheritDoc} */
        @Override
        public char[] getExponentSeparatorChars() {
            return exponentSeparatorChars;
        }

        /** {@inheritDoc} */
        @Override
        public boolean isAlwaysIncludeExponent() {
            return alwaysIncludeExponent;
        }

        /** {@inheritDoc} */
        @Override
        public String apply(final double d) {
            if (Double.isFinite(d)) {
                return applyFinite(d);
            } else if (Double.isInfinite(d)) {
                return d > 0.0
                        ? positiveInfinity
                        : negativeInfinity;
            }
            return nan;
        }

        /**
         * Return a formatted string representation of the given finite value.
         * @param d double value
         */
        private String applyFinite(final double d) {
            final ParsedDecimal n = ParsedDecimal.from(d);

            int roundExponent = Math.max(n.getExponent(), minDecimalExponent);
            if (maxPrecision > 0) {
                roundExponent = Math.max(n.getScientificExponent() - maxPrecision + 1, roundExponent);
            }
            n.round(roundExponent);

            return applyFiniteInternal(n);
        }

        /**
         * Return a formatted representation of the given rounded decimal value to {@code dst}.
         * @param val value to format
         */
        protected abstract String applyFiniteInternal(ParsedDecimal val);
    }

    /**
     * Format class that produces plain decimal strings that do not use
     * scientific notation.
     */
    private static class PlainDoubleFormat extends AbstractDoubleFormat {

        /** Construct a new instance.
         * @param builder builder instance containing configuration values
         */
        PlainDoubleFormat(final Builder builder) {
            super(builder);
        }

        /**
         * {@inheritDoc} */
        @Override
        protected String applyFiniteInternal(final ParsedDecimal val) {
            return val.toPlainString(this);
        }
    }

    /**
     * Format class producing results similar to {@link Double#toString()}, with
     * plain decimal notation for small numbers relatively close to zero and scientific
     * notation otherwise.
     */
    private static final class MixedDoubleFormat extends AbstractDoubleFormat {

        /** Max decimal exponent for plain format. */
        private final int plainMaxExponent;

        /** Min decimal exponent for plain format. */
        private final int plainMinExponent;

        /**
         * Construct a new instance.
         * @param builder builder instance containing configuration values
         */
        MixedDoubleFormat(final Builder builder) {
            super(builder);

            this.plainMaxExponent = builder.plainFormatMaxDecimalExponent;
            this.plainMinExponent = builder.plainFormatMinDecimalExponent;
        }

        /** {@inheritDoc} */
        @Override
        protected String applyFiniteInternal(final ParsedDecimal val) {
            final int sciExp = val.getScientificExponent();
            if (sciExp <= plainMaxExponent && sciExp >= plainMinExponent) {
                return val.toPlainString(this);
            }
            return val.toScientificString(this);
        }
    }

    /**
     * Format class that uses scientific notation for all values.
     */
    private static class ScientificDoubleFormat extends AbstractDoubleFormat {

        /**
         * Construct a new instance.
         * @param builder builder instance containing configuration values
         */
        ScientificDoubleFormat(final Builder builder) {
            super(builder);
        }

        /** {@inheritDoc} */
        @Override
        public String applyFiniteInternal(final ParsedDecimal val) {
            return val.toScientificString(this);
        }
    }

    /**
     * Format class that uses engineering notation for all values.
     */
    private static class EngineeringDoubleFormat extends AbstractDoubleFormat {

        /**
         * Construct a new instance.
         * @param builder builder instance containing configuration values
         */
        EngineeringDoubleFormat(final Builder builder) {
            super(builder);
        }

        /** {@inheritDoc} */
        @Override
        public String applyFiniteInternal(final ParsedDecimal val) {
            return val.toEngineeringString(this);
        }
    }
}
