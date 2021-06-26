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
import java.util.function.Function;

/** Enum containing standard double format types with methods to produce
 * configured {@link DoubleFormat} instances.
 */
public enum StandardDoubleFormat {
    PLAIN(PlainDoubleFormat::new),
    SCIENTIFIC(ScientificDoubleFormat::new),
    ENGINEERING(EngineeringDoubleFormat::new),
    MIXED(MixedDoubleFormat::new);

    private final Function<Builder, DoubleFormat> factory;

    StandardDoubleFormat(final Function<Builder, DoubleFormat> factory) {
        this.factory = factory;
    }

    public Builder builder() {
        return new Builder(factory);
    }

    public static final class Builder {

        private final Function<Builder, DoubleFormat> factory;

        /** Maximum number of significant decimal digits in formatted strings. */
        private int maxPrecision = 0;

        /** Minimum value formatted as non-zero. */
        private double min = Double.POSITIVE_INFINITY;

        private double plainFormatMax = 1e7;

        private double plainFormatMin = 1e-4;

        private String infinity = "Infinity";

        private String nan = "NaN";

        private ParsedDouble.FormatOptions formatOptions = new ParsedDouble.FormatOptions();

        private Builder(final Function<Builder, DoubleFormat> factory) {
            this.factory = factory;
        }

        /** Set the maximum number of significant decimal digits used in format
         * results. A value of 0 indicates no limit.
         * @param maxPrecision maximum precision
         * @return this instance
         */
        public Builder withMaxPrecision(final int maxPrecision) {
            this.maxPrecision = maxPrecision;
            return this;
        }

        public Builder withMin(final double min) {
            this.min = min;
            return this;
        }

        public Builder withPlainFormatMax(final double plainFormatMax) {
            this.plainFormatMax = plainFormatMax;
            return this;
        }

        public Builder withPlainFormatMin(final double plainFormatMin) {
            this.plainFormatMin = plainFormatMin;
            return this;
        }

        public Builder withSignedZero(final boolean signedZero) {
            formatOptions.setSignedZero(signedZero);
            return this;
        }

        public Builder withMinusSign(final char minusSign) {
            formatOptions.setMinusSign(minusSign);
            return this;
        }

        public Builder withDecimalPlaceholder(final boolean decimalPlaceholder) {
            formatOptions.setIncludeDecimalPlaceholder(decimalPlaceholder);
            return this;
        }

        public Builder withDecimalSeparator(final char decimalSeparator) {
            formatOptions.setDecimalSeparator(decimalSeparator);
            return this;
        }

        public Builder withExponentSeparator(final String exponentSeparator) {
            formatOptions.setExponentSeparator(exponentSeparator);
            return this;
        }

        public Builder withInfinity(final String infinity) {
            this.infinity = infinity;
            return this;
        }

        public Builder withNaN(final String nan) {
            this.nan = nan;
            return this;
        }

        public Builder withFormatSymbols(final DecimalFormatSymbols symbols) {
            return withDecimalSeparator(symbols.getDecimalSeparator())
                    .withMinusSign(symbols.getMinusSign())
                    .withExponentSeparator(symbols.getExponentSeparator())
                    .withInfinity(symbols.getInfinity())
                    .withNaN(symbols.getNaN());
        }

        public DoubleFormat build() {
            return factory.apply(this);
        }
    }

    /** Base class for standard double formatting classes.
     */
    private abstract static class AbstractDoubleFormat implements DoubleFormat {

        /** Minimum possible decimal exponent for double values. */
        static final int MIN_DOUBLE_EXPONENT = -326;

        /** Maximum precision to use when formatting values. */
        private final int maxPrecision;

        private final int minExponent;

        private final String postiveInfinity;

        private final String negativeInfinity;

        private final String nan;

        private final ParsedDouble.FormatOptions formatOptions;

        AbstractDoubleFormat(final Builder builder) {
            this.maxPrecision = builder.maxPrecision;

            this.minExponent = getScientificExponentOrDefault(Math.abs(builder.min), MIN_DOUBLE_EXPONENT);

            this.postiveInfinity = builder.infinity;
            this.negativeInfinity = builder.formatOptions.getMinusSign() + builder.infinity;
            this.nan = builder.nan;

            this.formatOptions = builder.formatOptions;
        }

        /** {@inheritDoc} */
        @Override
        public String apply(final double d) {
            if (Double.isFinite(d)) {
                return formatFinite(d);
            } else if (Double.isInfinite(d)) {
                return d > 0.0 ?
                    postiveInfinity :
                    negativeInfinity;
            }
            return nan;
        }

        /** Return a formatted string representing the given finite value.
         * @param d double value
         * @return formatted string
         */
        private String formatFinite(final double d) {
            final ParsedDouble n = ParsedDouble.from(d);

            int roundExponent = Math.max(n.getExponent(), minExponent);
            if (maxPrecision > 0) {
                roundExponent = Math.max(n.getScientificExponent() - maxPrecision + 1, roundExponent);
            }

            final ParsedDouble rounded = n.round(roundExponent);

            return formatFiniteInternal(rounded, formatOptions);
        }

        /** Format the given parsed double value.
         * @param val value to format
         * @return formatted double value
         */
        protected abstract String formatFiniteInternal(ParsedDouble val, ParsedDouble.FormatOptions formatOptions);

        static int getScientificExponentOrDefault(final double d, final int defaultExponent) {
            return Double.isFinite(d) ?
                    getScientificExponent(d) :
                    defaultExponent;
        }

        static int getScientificExponent(final double d) {
            return ParsedDouble.from(d).getScientificExponent();
        }
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
        protected String formatFiniteInternal(final ParsedDouble val, final ParsedDouble.FormatOptions formatOptions) {
            return val.toPlainString(formatOptions);
        }
    }

    /** Format class producing results similar to {@link Double#toString()}, with
     * plain decimal notation for small numbers relatively close to zero and scientific
     * notation otherwise.
     */
    private static class MixedDoubleFormat extends AbstractDoubleFormat {

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

            this.plainMaxExponent = getScientificExponent(builder.plainFormatMax);
            this.plainMinExponent = getScientificExponent(builder.plainFormatMin);
        }

        /** {@inheritDoc} */
        @Override
        protected String formatFiniteInternal(final ParsedDouble val, final ParsedDouble.FormatOptions formatOptions) {
            final int sciExp = val.getScientificExponent();
            return sciExp < plainMaxExponent && sciExp > plainMinExponent ?
                    val.toPlainString(formatOptions) :
                    val.toScientificString(formatOptions);
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
        public String formatFiniteInternal(final ParsedDouble val, final ParsedDouble.FormatOptions formatOptions) {
            return val.toScientificString(formatOptions);
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
        public String formatFiniteInternal(final ParsedDouble val, final ParsedDouble.FormatOptions formatOptions) {
            return val.toEngineeringString(formatOptions);
        }
    }
}
