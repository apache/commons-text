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
    MIXED(MixedDoubleFormat::new),
    SCIENTIFIC(ScientificDoubleFormat::new),
    ENGINEERING(EngineeringDoubleFormat::new);

    private final Function<Builder, DoubleFormat> factory;

    StandardDoubleFormat(final Function<Builder, DoubleFormat> factory) {
        this.factory = factory;
    }

    public Builder builder() {
        return new Builder(factory);
    }

    public static final class Builder {

        private final Function<Builder, DoubleFormat> factory;

        private int maxPrecision = -1;

        private int minExponent = Integer.MIN_VALUE;

        private int upperThresholdExponent = 7;

        private int lowerThresholdExponent = -4;

        private ParsedDouble.FormatOptions formatOptions = new ParsedDouble.FormatOptions();

        private Builder(final Function<Builder, DoubleFormat> factory) {
            this.factory = factory;
        }

        public Builder withMaxPrecision(final int maxPrecision) {
            this.maxPrecision = maxPrecision;
            return this;
        }

        public Builder withMinExponent(final int minExponent) {
            this.minExponent = minExponent;
            return this;
        }

        public Builder withUpperThresholdExponent(final int upperThresholdExponent) {
            this.upperThresholdExponent = upperThresholdExponent;
            return this;
        }

        public Builder withLowerThresholdExponent(final int lowerThresholdExponent) {
            this.lowerThresholdExponent = lowerThresholdExponent;
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

        public Builder withFormatSymbols(final DecimalFormatSymbols symbols) {
            return withDecimalSeparator(symbols.getDecimalSeparator())
                    .withMinusSign(symbols.getMinusSign())
                    .withExponentSeparator(symbols.getExponentSeparator());
        }

        public DoubleFormat build() {
            return factory.apply(this);
        }
    }

    /** Base class for standard double formatting classes.
     */
    private abstract static class AbstractDoubleFormat implements DoubleFormat {

        /** Maximum precision to use when formatting values. */
        private final int maxPrecision;

        /** The minimum exponent to allow in the result. Value with exponents less than this are
         * rounded to positive zero.
         */
        private final int minExponent;

        private final ParsedDouble.FormatOptions formatOptions;

        AbstractDoubleFormat(final Builder builder) {
            if (builder.maxPrecision < 0) {
                throw new IllegalArgumentException(
                        "Max precision must be greater than or equal to zero; was " + builder.maxPrecision);
            }

            this.maxPrecision = builder.maxPrecision;
            this.minExponent = builder.minExponent;
            this.formatOptions = builder.formatOptions;
        }

        /** {@inheritDoc} */
        @Override
        public String apply(final double d) {
            if (Double.isFinite(d)) {
                final ParsedDouble n = ParsedDouble.from(d);

                int roundExponent = Math.max(n.getExponent(), minExponent);
                if (maxPrecision > 0) {
                    roundExponent = Math.max(n.getScientificExponent() - maxPrecision + 1, roundExponent);
                }

                final ParsedDouble rounded = n.round(roundExponent);

                return formatInternal(rounded, formatOptions);
            }

            return Double.toString(d); // NaN or infinite; use default Double toString() method
        }

        /** Format the given parsed double value.
         * @param val value to format
         * @return formatted double value
         */
        protected abstract String formatInternal(ParsedDouble val, ParsedDouble.FormatOptions formatOptions);
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
        protected String formatInternal(final ParsedDouble val, final ParsedDouble.FormatOptions formatOptions) {
            return val.toPlainString(formatOptions);
        }
    }

    /** Format class producing results similar to {@link Double#toString()}, with
     * plain decimal notation for small numbers relatively close to zero and scientific
     * notation otherwise.
     */
    private static class MixedDoubleFormat extends AbstractDoubleFormat {

        /** Decimal exponent upper bound for use of plain formatted strings. */
        private static final int UPPER_PLAIN_EXP = 7;

        /** Decimal exponent lower bound for use of plain formatted strings. */
        private static final int LOWER_PLAIN_EXP = -4;

        private final int plainUpperThresholdExponent;

        private final int plainLowerThresholdExponent;

        /** Construct a new instance with the given maximum precision and minimum exponent.
         * @param maxPrecision maximum number of significant decimal digits
         * @param minExponent minimum decimal exponent; values less than this that do not round up
         *      are considered to be zero
         * @throws IllegalArgumentException if {@code maxPrecision} is less than zero
         */
        MixedDoubleFormat(final Builder builder) {
            super(builder);

            this.plainUpperThresholdExponent = builder.upperThresholdExponent;
            this.plainLowerThresholdExponent = builder.lowerThresholdExponent;
        }

        /** {@inheritDoc} */
        @Override
        protected String formatInternal(final ParsedDouble val, final ParsedDouble.FormatOptions formatOptions) {
            final int sciExp = val.getScientificExponent();
            return sciExp < plainUpperThresholdExponent && sciExp > plainLowerThresholdExponent ?
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
        public String formatInternal(final ParsedDouble val, final ParsedDouble.FormatOptions formatOptions) {
            return val.toScientificString(formatOptions);
        }
    }

    /** Format class that uses engineering notation for all values.
     */
    private static class EngineeringDoubleFormat extends AbstractDoubleFormat {

        /** Construct a new instance with the given maximum precision and minimum exponent.
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
        public String formatInternal(final ParsedDouble val, final ParsedDouble.FormatOptions formatOptions) {
            return val.toEngineeringString(formatOptions);
        }
    }
}
