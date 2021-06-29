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
import java.text.DecimalFormatSymbols;
import java.util.function.Function;

/** Enum containing standard double format types with methods to produce
 * configured {@link DoubleFormat} instances.
 */
public enum StandardDoubleFormat {
    PLAIN(PlainDoubleFormat::new),
    SCIENTIFIC(ScientificDoubleFormat::new),
    ENGINEERING(EngineeringDoubleFormat::new),
    MIXED(MixedDoubleFormat::new);

    /** Function used to construct {@link DoubleFormat} instances of this format type. */
    private final Function<Builder, DoubleFormat> factory;

    /** Construct a new instance.
     * @param factory function used to construct format instances
     */
    StandardDoubleFormat(final Function<Builder, DoubleFormat> factory) {
        this.factory = factory;
    }

    /** Return a {@link Builder} for configuring and constructing
     * {@link DoubleFormat} instances for this format type.
     * @return builder instance
     */
    public Builder builder() {
        return new Builder(factory);
    }

    /** Class for constructing configured {@link DoubleFormat} instances for standard format types.
     */
    public static final class Builder {

        /** Default value for the plain format max decimal exponent. */
        private static final int DEFAULT_PLAIN_FORMAT_MAX_DECIMAL_EXPONENT = 6;

        /** Default value for the plain format min decimal exponent. */
        private static final int DEFAULT_PLAIN_FORMAT_MIN_DECIMAL_EXPONENT = -3;

        /** Function used to construct {@link DoubleFormat} instances. */
        private final Function<Builder, DoubleFormat> factory;

        /** Maximum number of significant decimal digits in formatted strings. */
        private int maxPrecision = 0;

        /** Minimum decimal exponent. */
        private int minDecimalExponent = Integer.MIN_VALUE;

        /** Max decimal exponent to use to plain formatting with the mixed format type. */
        private int plainFormatMaxDecimalExponent = DEFAULT_PLAIN_FORMAT_MAX_DECIMAL_EXPONENT;

        /** Min decimal exponent to use to plain formatting with the mixed format type. */
        private int plainFormatMinDecimalExponent = DEFAULT_PLAIN_FORMAT_MIN_DECIMAL_EXPONENT;

        /** String representing infinity. */
        private String infinity = "Infinity";

        /** String representing NaN. */
        private String nan = "NaN";

        /** Flag determining if fraction placeholders should be used. */
        private boolean fractionPlaceholder = true;

        /** Flag determining if signed zero strings are allowed. */
        private boolean signedZero = true;

        /** Character used to represent zero. */
        private char zeroDigit = '0';

        /** Decimal separator character. */
        private char decimalSeparator = '.';

        /** Minus sign character. */
        private char minusSign = '-';

        /** Exponent separator character. */
        private String exponentSeparator = "E";

        /** Construct a new instance that delegates {@link DoubleFormat} construction
         * to the given factory function.
         * @param factory factory function
         */
        private Builder(final Function<Builder, DoubleFormat> factory) {
            this.factory = factory;
        }

        /** Set the maximum number of significant decimal digits used in format
         * results. A value of {@code 0} indicates no limit. The default value is {@code 0}.
         * @param maxPrecision maximum precision
         * @return this instance
         */
        public Builder withMaxPrecision(final int maxPrecision) {
            this.maxPrecision = maxPrecision;
            return this;
        }

        /** Set the minimum decimal exponent for formatted strings. No digits with an
         * absolute value of less than {@code 10^minDecimalExponent} will be included
         * in format results. If the number being formatted does not contain any such
         * digits, then zero is returned. For example, if {@code minDecimalExponent}
         * is set to {@code -2} and the number {@code 3.14159} is formatted, the plain
         * format result will be {@code "3.14"}. If {@code 0.001} is formatted, then the
         * result is the zero string.
         * @param minDecimalExponent minimum decimal exponent
         * @return this instance
         */
        public Builder withMinDecimalExponent(final int minDecimalExponent) {
            this.minDecimalExponent = minDecimalExponent;
            return this;
        }

        /** Set the maximum decimal exponent for numbers formatted as plain decimal strings when
         * using the {@link StandardDoubleFormat#MIXED MIXED} format type. If the number being formatted
         * has an absolute value less than {@code 10^(plainFormatMaxDecimalExponent + 1)} and
         * greater than or equal to {@code 10^(plainFormatMinDecimalExponent)} after any necessary rounding,
         * then the formatted result will use the {@link StandardDoubleFormat#PLAIN PLAIN} format type.
         * Otherwise, {@link StandardDoubleFormat#SCIENTIFIC SCIENTIFIC} format will be used. For example,
         * if this value is set to {@code 2}, the number {@code 999} will be formatted as {@code "999.0"}
         * while {@code 1000} will be formatted as {@code "1.0E3"}.
         *
         * <p>The default value is {@value #DEFAULT_PLAIN_FORMAT_MAX_DECIMAL_EXPONENT}.
         *
         * <p>This value is ignored for formats other than {@link StandardDoubleFormat#MIXED}.
         * @param plainFormatMaxDecimalExponent maximum decimal exponent for values formatted as plain
         *      strings when using the {@link StandardDoubleFormat#MIXED MIXED} format type.
         * @return this instance
         * @see #withPlainFormatMinDecimalExponent(int)
         */
        public Builder withPlainFormatMaxDecimalExponent(final int plainFormatMaxDecimalExponent) {
            this.plainFormatMaxDecimalExponent = plainFormatMaxDecimalExponent;
            return this;
        }

        /** Set the minimum decimal exponent for numbers formatted as plain decimal strings when
         * using the {@link StandardDoubleFormat#MIXED MIXED} format type. If the number being formatted
         * has an absolute value less than {@code 10^(plainFormatMaxDecimalExponent + 1)} and
         * greater than or equal to {@code 10^(plainFormatMinDecimalExponent)} after any necessary rounding,
         * then the formatted result will use the {@link StandardDoubleFormat#PLAIN PLAIN} format type.
         * Otherwise, {@link StandardDoubleFormat#SCIENTIFIC SCIENTIFIC} format will be used. For example,
         * if this value is set to {@code -2}, the number {@code 0.01} will be formatted as {@code "0.01"}
         * while {@code 0.0099} will be formatted as {@code "9.9E-3"}.
         *
         * <p>The default value is {@value #DEFAULT_PLAIN_FORMAT_MIN_DECIMAL_EXPONENT}.
         *
         * <p>This value is ignored for formats other than {@link StandardDoubleFormat#MIXED}.
         * @param plainFormatMaxDecimalExponent maximum decimal exponent for values formatted as plain
         *      strings when using the {@link StandardDoubleFormat#MIXED MIXED} format type.
         * @return this instance
         * @see #withPlainFormatMinDecimalExponent(int)
         */
        public Builder withPlainFormatMinDecimalExponent(final int plainFormatMinDecimalExponent) {
            this.plainFormatMinDecimalExponent = plainFormatMinDecimalExponent;
            return this;
        }

        /** Set the flag determining whether or not the zero string may be returned with the minus
         * sign or if it will always be returned in the positive form. For example, if set to true,
         * the string {@code "-0.0"} may be returned for some input numbers. If false, only {@code "0.0"}
         * will be returned, regardless of the sign of the input number.
         * @param signedZero if true, the zero string may be returned with a preceding minus sign; if false,
         *      the zero string will only be returned in its positive form
         * @return this instance
         */
        public Builder withSignedZero(final boolean signedZero) {
            this.signedZero = signedZero;
            return this;
        }

        /** Set the character used to represent zero.
         * @param zeroDigit character used to represent zero
         * @return this instance
         */
        public Builder withZeroDigit(final char zeroDigit) {
            this.zeroDigit = zeroDigit;
            return this;
        }

        /** Set the flag determining whether or not a zero character is added in the fraction position
         * when no fractional value is present. For example, if set to true, the number {@code 1} would
         * be formatted as {@code "1.0"}. If false, it would be formatted as {@code "1"}. The default
         * value is {@code true}.
         * @param fractionPlaceholder if true, a zero character is placed in the fraction position when
         *      no fractional value is present; if false, fractional digits are only included when needed
         * @return this instance
         */
        public Builder withFractionPlaceholder(final boolean fractionPlaceholder) {
            this.fractionPlaceholder = fractionPlaceholder;
            return this;
        }

        /** Set the character used as the minus sign.
         * @param minusSign character to use as the minus sign
         * @return this instance
         */
        public Builder withMinusSign(final char minusSign) {
            this.minusSign = minusSign;
            return this;
        }

        /** Set the decimal separator character, i.e., the character placed between the
         * whole number and fractional portions of the formatted strings. The default value
         * is {@code '.'}.
         * @param decimalSeparator decimal separator character
         * @return this instance
         */
        public Builder withDecimalSeparator(final char decimalSeparator) {
            this.decimalSeparator = decimalSeparator;
            return this;
        }

        /** Set the exponent separator character, i.e., the string placed between
         * the mantissa and the exponent. The default value is {@code "E"}, as in
         * {@code "1.2E6"}.
         * @param exponentSeparator exponent separator string
         * @return this instance
         */
        public Builder withExponentSeparator(final String exponentSeparator) {
            this.exponentSeparator = exponentSeparator;
            return this;
        }

        /** Set the string used to represent infinity. For negative infinity, this string
         * is prefixed with the {@link #withMinusSign(char) minus sign}.
         * @param infinity string used to represent infinity
         * @return this instance
         */
        public Builder withInfinity(final String infinity) {
            this.infinity = infinity;
            return this;
        }

        /** Set the string used to represent {@link Double#NaN}.
         * @param nan string used to represent {@link Double#NaN}
         * @return this instance
         */
        public Builder withNaN(final String nan) {
            this.nan = nan;
            return this;
        }

        /** Configure this instance with the given format symbols. The following values
         * are set:
         * <ul>
         *  <li>{@link #withDecimalSeparator(char) decimal separator}</li>
         *  <li>{@link #withMinusSign(char) minus sign}</li>
         *  <li>{@link #withZeroDigit(char) zero digit}</li>
         *  <li>{@link #withExponentSeparator(String) exponent separator}</li>
         *  <li>{@link #withInfinity(String) infinity}</li>
         *  <li>{@link #withNaN(String) NaN}</li>
         * </ul>
         * @param symbols format symbols
         * @return this instance
         */
        public Builder withFormatSymbols(final DecimalFormatSymbols symbols) {
            return withDecimalSeparator(symbols.getDecimalSeparator())
                    .withMinusSign(symbols.getMinusSign())
                    .withZeroDigit(symbols.getZeroDigit())
                    .withExponentSeparator(symbols.getExponentSeparator())
                    .withInfinity(symbols.getInfinity())
                    .withNaN(symbols.getNaN());
        }

        /** Construct a new {@link DoubleFormat} instance.
         * @return format instance
         */
        public DoubleFormat build() {
            return factory.apply(this);
        }
    }

    /** Base class for standard double formatting classes.
     */
    private abstract static class AbstractDoubleFormat implements DoubleFormat, SimpleDecimal.FormatOptions {

        /** Initial size to use for string builder instances. */
        private static final int INITIAL_STR_BUILDER_SIZE = 32;

        /** Maximum precision; 0 indicates no limit. */
        private final int maxPrecision;

        /** Minimum decimal exponent. */
        private final int minDecimalExponent;

        /** String representing positive infinity. */
        private final String postiveInfinity;

        /** String representing negative infinity. */
        private final String negativeInfinity;

        /** String representing NaN. */
        private final String nan;

        /** Flag determining if fraction placeholders should be used. */
        private final boolean fractionPlaceholder;

        /** Flag determining if signed zero strings are allowed. */
        private final boolean signedZero;

        /** Difference between the localized character used to represent zero and '0'. */
        private final int zeroDelta;

        /** Decimal separator character. */
        private final char decimalSeparator;

        /** Minus sign character. */
        private final char minusSign;

        /** Exponent separator character. */
        private final String exponentSeparator;

        /** Construct a new instance configured with the values from the builder.
         * @param builder builder instance containing configuration values
         */
        AbstractDoubleFormat(final Builder builder) {
            this.maxPrecision = builder.maxPrecision;
            this.minDecimalExponent = builder.minDecimalExponent;

            this.postiveInfinity = builder.infinity;
            this.negativeInfinity = builder.minusSign + builder.infinity;
            this.nan = builder.nan;

            this.fractionPlaceholder = builder.fractionPlaceholder;
            this.signedZero = builder.signedZero;
            this.zeroDelta = builder.zeroDigit - '0';
            this.decimalSeparator = builder.decimalSeparator;
            this.minusSign = builder.minusSign;
            this.exponentSeparator = builder.exponentSeparator;
        }

        /** {@inheritDoc} */
        @Override
        public boolean getIncludeFractionPlaceholder() {
            return fractionPlaceholder;
        }

        /** {@inheritDoc} */
        @Override
        public boolean getSignedZero() {
            return signedZero;
        }

        /** {@inheritDoc} */
        @Override
        public int getZeroDelta() {
            return zeroDelta;
        }

        /** {@inheritDoc} */
        @Override
        public char getDecimalSeparator() {
            return decimalSeparator;
        }

        /** {@inheritDoc} */
        @Override
        public char getMinusSign() {
            return minusSign;
        }

        /** {@inheritDoc} */
        @Override
        public String getExponentSeparator() {
            return exponentSeparator;
        }

        /** {@inheritDoc} */
        @Override
        public String apply(final double d) {
            final StringBuilder sb = new StringBuilder(INITIAL_STR_BUILDER_SIZE);
            appendTo(sb, d);
            return sb.toString();
        }

        /** {@inheritDoc} */
        @Override
        public void appendTo(final Appendable dst, final double d) throws IOException {
            if (Double.isFinite(d)) {
                appendFinite(dst, d);
            } else if (Double.isInfinite(d)) {
                dst.append(d > 0.0 ? postiveInfinity : negativeInfinity);
            } else {
                dst.append(nan);
            }
        }

        /** {@inheritDoc} */
        @Override
        public void appendTo(final StringBuilder sb, final double d) {
            try {
                appendTo((Appendable) sb, d);
            } catch (IOException exc) {
                // cannot end up here since StringBuilder does not throw IOExceptions
                throw new UncheckedIOException("StringBuilder threw IOException: " + exc.getMessage(), exc);
            }
        }

        /** Append a formatted string representation of the given finite value to {@code dst}.
         * @param dst destination to append to
         * @param d double value
         * @throws IOException if an I/O error occurs
         */
        private void appendFinite(final Appendable dst, final double d) throws IOException {
            final SimpleDecimal n = SimpleDecimal.from(d);

            int roundExponent = Math.max(n.getExponent(), minDecimalExponent);
            if (maxPrecision > 0) {
                roundExponent = Math.max(n.getScientificExponent() - maxPrecision + 1, roundExponent);
            }

            final SimpleDecimal rounded = n.round(roundExponent);

            appendFiniteInternal(dst, rounded);
        }

        /** Append a formatted representation of the given rounded decimal value to {@code dst}.
         * @param dst destination to write to
         * @param val value to format
         * @throws IOException if an I/O error occurs
         */
        protected abstract void appendFiniteInternal(Appendable dst, SimpleDecimal val) throws IOException;
    }

    /** Format class that produces plain decimal strings that do not use
     * scientific notation.
     */
    private static class PlainDoubleFormat extends AbstractDoubleFormat {

        /** Construct a new instance with the given maximum precision and minimum exponent.
         * @param maxPrecision maximum number of significant decimal digits
         * @param minExponent minimum decimal exponent; values less than this that do not round up
         *      are considered to be zero
         * @throws IllegalArgumentException if {@code maxPrecision} is less than zero
         */
        PlainDoubleFormat(final Builder builder) {
            super(builder);
        }

        /** {@inheritDoc} */
        @Override
        protected void appendFiniteInternal(final Appendable dst, final SimpleDecimal val) throws IOException {
            val.toPlainString(dst, this);
        }
    }

    /** Format class producing results similar to {@link Double#toString()}, with
     * plain decimal notation for small numbers relatively close to zero and scientific
     * notation otherwise.
     */
    private static final class MixedDoubleFormat extends AbstractDoubleFormat {

        private final int plainMaxExponent;

        private final int plainMinExponent;

        /** Construct a new instance with the given maximum precision and minimum exponent.
         * @param maxPrecision maximum number of significant decimal digits
         * @param minExponent minimum decimal exponent; values less than this that do not round up
         *      are considered to be zero
         * @throws IllegalArgumentException if {@code maxPrecision} is less than zero
         */
        MixedDoubleFormat(final Builder builder) {
            super(builder);

            this.plainMaxExponent = builder.plainFormatMaxDecimalExponent;
            this.plainMinExponent = builder.plainFormatMinDecimalExponent;
        }

        /** {@inheritDoc} */
        @Override
        protected void appendFiniteInternal(final Appendable dst, final SimpleDecimal val) throws IOException {
            final int sciExp = val.getScientificExponent();
            if (sciExp <= plainMaxExponent && sciExp >= plainMinExponent) {
                val.toPlainString(dst, this);
            } else {
                val.toScientificString(dst, this);
            }
        }
    }

    /** Format class that uses scientific notation for all values.
     */
    private static class ScientificDoubleFormat extends AbstractDoubleFormat {

        /** Construct a new instance with the given maximum precision and minimum exponent.
         * @param maxPrecision maximum number of significant decimal digits
         * @param minExponent minimum decimal exponent; values less than this that do not round up
         *      are considered to be zero
         * @throws IllegalArgumentException if {@code maxPrecision} is less than zero
         */
        ScientificDoubleFormat(final Builder builder) {
            super(builder);
        }

        /** {@inheritDoc} */
        @Override
        public void appendFiniteInternal(final Appendable dst, final SimpleDecimal val) throws IOException {
            val.toScientificString(dst, this);
        }
    }

    /** Format class that uses engineering notation for all values.
     */
    private static class EngineeringDoubleFormat extends AbstractDoubleFormat {

        /** Construct a new instance with the given maximum) precision and minimum exponent.
         * @param maxPrecision maximum number of significant decimal digits
         * @param minExponent minimum decimal exponent; values less than this that do not round up
         *      are considered to be zero
         * @throws IllegalArgumentException if {@code maxPrecision} is less than zero
         */
        EngineeringDoubleFormat(final Builder builder) {
            super(builder);
        }

        /** {@inheritDoc} */
        @Override
        public void appendFiniteInternal(final Appendable dst, final SimpleDecimal val) throws IOException {
            val.toEngineeringString(dst, this);
        }
    }
}
