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

/** Simple class representing a double value parsed into separate decimal components. Each double
 * is represented with
 * <ul>
 *  <li>a boolean flag for the sign,</li>
 *  <li> a sequence of the digits '0' - '10' representing an unsigned integer with leading and trailing zeros
 *      removed, and</li>
 *  <li>an exponent value that when applied to the base 10 digits produces a floating point value with the
 *      correct magnitude.</li>
 * </ul>
 * <p><strong>Examples</strong></p>
 * <table>
 *  <tr><th>Double</th><th>Negative</th><th>Digits</th><th>Exponent</th></tr>
 *  <tr><td>0.0</td><td>false</td><td>"0"</td><td>0</td></tr>
 *  <tr><td>1.2</td><td>false</td><td>"12"</td><td>-1</td></tr>
 *  <tr><td>-0.00971</td><td>true</td><td>"971"</td><td>-5</td></tr>
 *  <tr><td>56300</td><td>true</td><td>"563"</td><td>2</td></tr>
 * </table>
 */
final class SimpleDecimal {

    /** Interface containing values used during string formatting.
     */
    interface FormatOptions {

        /** Return true if fraction placeholders (e.g., {@code ".0"} in {@code "1.0"})
         * should be included.
         * @return true if fraction placeholders should be included
         */
        boolean getIncludeFractionPlaceholder();

        /** Return true if the string zero should be prefixed with the minus sign
         * for negative zero values.
         * @return true if the minus zero string should be allowed
         */
        boolean getSignedZero();

        /** Get the string containing the localized digit characters 0-9 in that order.
         * This string <em>must</em> be non-null and have a length of 10.
         * @return string containing the digit characters 0-9
         */
        String getDigits();

        /** Get the decimal separator character.
         * @return decimal separator character
         */
        char getDecimalSeparator();

        /** Get the character used to separate thousands groupings.
         * @return character used to separate thousands groupings
         */
        char getThousandsGroupingSeparator();

        /** Return true if thousands should be grouped.
         * @return true if thousand should be grouped
         */
        boolean getGroupThousands();

        /** Get the minus sign character.
         * @return minus sign character
         */
        char getMinusSign();

        /** Get the exponent separator string.
         * @return exponent separator string
         */
        String getExponentSeparator();

        /** Return true if exponent values should always be included in
         * formatted output, even if the value is zero.
         * @return true if exponent values should always be included
         */
        boolean getAlwaysIncludeExponent();
    }

    /** Minus sign character. */
    private static final char MINUS_CHAR = '-';

    /** Plus sign character. */
    private static final char PLUS_CHAR = '+';

    /** Decimal separator character. */
    private static final char DECIMAL_SEP_CHAR = '.';

    /** Exponent character. */
    private static final char EXPONENT_CHAR = 'E';

    /** Zero digit character. */
    private static final char ZERO_CHAR = '0';

    /** Number of characters in a thousands grouping. */
    private static final int THOUSANDS_GROUP_SIZE = 3;

    /** Radix for decimal numbers. */
    private static final int DECIMAL_RADIX = 10;

    /** Center value used when rounding. */
    private static final int ROUND_CENTER = DECIMAL_RADIX / 2;

    /** True if the value is negative. */
    final boolean negative;

    /** Array containing the significant decimal digits for the value. */
    final int[] digits;

    /** Number of digits used in the digits array; not necessarily equal to the length. */
    int digitCount;

    /** Exponent for the value. */
    int exponent;

    /** Construct a new instance from its parts.
     * @param negative true if the value is negative
     * @param digits array containing significant digits
     * @param digitCount number of digits used from the {@code digits} array
     * @param exponent exponent value
     */
    SimpleDecimal(final boolean negative, final int[] digits, final int digitCount,
            final int exponent) {
        this.negative = negative;
        this.digits = digits;
        this.digitCount = digitCount;
        this.exponent = exponent;
    }

    /** Get the exponent value. This exponent produces a floating point value with the
     * correct magnitude when applied to the unsigned integer represented by the {@link #getDigits() digit}
     * string.
     * @return exponent value
     */
    public int getExponent() {
        return exponent;
    }

    /** Get the exponent that would be used when representing this number in scientific
     * notation (i.e., with a single non-zero digit in front of the decimal point.
     * @return the exponent that would be used when representing this number in scientific
     *      notation
     */
    public int getScientificExponent() {
        return digitCount + exponent - 1;
    }

    /** Round the instance to the given decimal exponent position using
     * {@link java.math.RoundingMode#HALF_EVEN half-even rounding}. For example, a value of {@code -2}
     * will round the instance to the digit at the position 10<sup>-2</sup> (i.e. to the closest multiple of 0.01).
     * @param roundExponent exponent defining the decimal place to round to
     */
    public void round(final int roundExponent) {
        if (roundExponent > exponent) {
            final int max = digitCount + exponent;

            if (roundExponent < max) {
                // rounding to a decimal place less than the max; set max precision
                maxPrecision(max - roundExponent);
            } else if (roundExponent == max && shouldRoundUp(0)) {
                // rounding up directly on the max decimal place
                setSingleDigitValue(1, roundExponent);
            } else {
                // change to zero
                setSingleDigitValue(0, 0);
            }
        }
    }

    /** Ensure that this instance has <em>at most</em> the given number of significant digits
     * (i.e. precision). If this instance already has a precision less than or equal
     * to the argument, nothing is done. If the given precision requires a reduction in the number
     * of digits, then the value is rounded using {@link java.math.RoundingMode#HALF_EVEN half-even rounding}.
     * @param precision maximum number of significant digits to include
     */
    public void maxPrecision(final int precision) {
        if (precision > 0 && precision < digitCount) {
            if (shouldRoundUp(precision)) {
                roundUp(precision);
            } else {
                truncate(precision);
            }
        }
    }

    /** Append a string representation of this value with no exponent field to {@code dst}. Ex:
     * <pre>
     * 10 = "10.0"
     * 1e-6 = "0.000001"
     * 1e11 = "100000000000.0"
     * </pre>
     * @param dst destination to append to
     * @param opts format options
     * @throws IOException if an I/O error occurs
     */
    public void toPlainString(final Appendable dst, final FormatOptions opts)
            throws IOException {
        final int wholeDigitCount = digitCount + exponent;

        final int fractionStartIdx = opts.getGroupThousands()
                ? appendWholeGrouped(wholeDigitCount, dst, opts)
                : appendWhole(wholeDigitCount, dst, opts);

        final int fractionZeroCount = wholeDigitCount < 0
                ? Math.abs(wholeDigitCount)
                : 0;

        appendFraction(fractionZeroCount, fractionStartIdx, dst, opts);
    }

    /** Append a string representation of this value in scientific notation to {@code dst}.
     * If the exponent field is equal to zero, it is not included in the result. Ex:
     * <pre>
     * 0 = "0.0"
     * 10 = "1.0E1"
     * 1e-6 = "1.0E-6"
     * 1e11 = "1.0E11"
     * </pre>
     * @param dst destination to append to
     * @param opts format options
     * @throws IOException if an I/O error occurs
     */
    public void toScientificString(final Appendable dst, final FormatOptions opts)
            throws IOException {
        toScientificString(1, dst, opts);
    }

    /** Append a string representation of the value in engineering notation to {@code dst}. This
     * is similar to {@link #toScientificString(Appendable, FormatOptions) scientific notation}
     * but with the exponent forced to be a multiple of 3, allowing easier alignment with SI prefixes.
     * If the exponent field is equal to zero, it is not included in the result.
     * <pre>
     * 0 = "0.0"
     * 10 = "10.0"
     * 1e-6 = "1.0E-6"
     * 1e11 = "100.0E9"
     * </pre>
     * @param dst destination to append to
     * @param opts format options
     * @throws IOException if an I/O error occurs
     */
    public void toEngineeringString(final Appendable dst, final FormatOptions opts)
            throws IOException {
        final int wholeDigitCount = 1 + Math.floorMod(digitCount + exponent - 1, 3);
        toScientificString(wholeDigitCount, dst, opts);
    }

    /** Append a string representation of the value in scientific notation using the
     * given number of whole digits to {@code dst}. If the exponent field of the result
     * is zero, it is not included in the returned string.
     * @param wholeDigits number of whole digits; must be greater than 0
     * @param dst destination to append to
     * @param opts format options
     * @throws IOException if an I/O error occurs
     */
    private void toScientificString(final int wholeDigitCount, final Appendable dst, final FormatOptions opts)
            throws IOException {
        final String localizedDigits = opts.getDigits();

        final int fractionStartIdx = appendWhole(wholeDigitCount, dst, opts);
        appendFraction(0, fractionStartIdx, dst, opts);

        // add the exponent but only if non-zero or explicitly requested
        final int resultExponent = digitCount + exponent - wholeDigitCount;
        if (resultExponent != 0 || opts.getAlwaysIncludeExponent()) {
            dst.append(opts.getExponentSeparator());

            if (resultExponent < 0) {
                dst.append(opts.getMinusSign());
            }

            final String exponentStr = Integer.toString(Math.abs(resultExponent));
            for (int i = 0; i < exponentStr.length(); ++i) {
                final int val = digitValue(exponentStr.charAt(i));
                appendLocalizedDigit(val, localizedDigits, dst);
            }
        }
    }

    /** Append {@code count} characters from the beginning of {@code digits} as the whole number
     * portion of the string representation. The index of the next character from {@code digits}
     * is returned if any characters remain. Otherwise, {@code digits.length()} is returned.
     * @param wholeCount number of whole digits to append
     * @param dst destination to append to
     * @param opts format options
     * @return index of the next character from {@code digits} or the {@code digits.length()}
     *      if all character have been appended
     * @throws IOException in an I/O error occurs
     */
    private int appendWhole(final int wholeCount, final Appendable dst, final FormatOptions opts)
            throws IOException {
        if (shouldIncludeMinus(opts)) {
            dst.append(opts.getMinusSign());
        }

        final String localizedDigits = opts.getDigits();
        final char localizedZero = localizedDigits.charAt(0);

        final int significantDigitCount = Math.max(0, Math.min(wholeCount, digitCount));

        if (significantDigitCount > 0) {
            int i;
            for (i = 0; i < significantDigitCount; ++i) {
                appendLocalizedDigit(digits[i], localizedDigits, dst);
            }

            for (; i < wholeCount; ++i) {
                dst.append(localizedZero);
            }
        } else {
            dst.append(localizedZero);
        }

        return significantDigitCount;
    }

    /** Same as {@link #appendWhole(int, Appendable, FormatOptions)} but includes thousands
     * grouping separators.
     * @param count number of digits to append
     * @param dst destination to append to
     * @param opts format options
     * @return index of the next character from {@code digits} or the {@code digits.length()}
     *      if all character have been appended
     * @throws IOException in an I/O error occurs
     * @see #appendWhole(int, Appendable, FormatOptions)
     */
    private int appendWholeGrouped(final int wholeCount, final Appendable dst, final FormatOptions opts)
            throws IOException {
        if (shouldIncludeMinus(opts)) {
            dst.append(opts.getMinusSign());
        }

        final String localizedDigits = opts.getDigits();
        final char localizedZero = localizedDigits.charAt(0);
        final char groupingChar = opts.getThousandsGroupingSeparator();

        final int appendCount = Math.max(0, Math.min(wholeCount, digitCount));

        if (appendCount > 0) {
            int i;
            int pos = wholeCount;
            for (i = 0; i < appendCount; ++i, --pos) {
                appendLocalizedDigit(digits[i], localizedDigits, dst);
                if (requiresGroupingSeparatorAfterPosition(pos)) {
                    dst.append(groupingChar);
                }
            }

            for (; i < wholeCount; ++i, --pos) {
                dst.append(localizedZero);
                if (requiresGroupingSeparatorAfterPosition(pos)) {
                    dst.append(groupingChar);
                }
            }
        } else {
            dst.append(localizedZero);
        }

        return appendCount;
    }

    /** Return true if a grouping separator should be added after the whole digit
     * character at the given position.
     * @param pos whole digit character position, with values starting at 1 and increasing
     *      from right to left.
     * @return true if a grouping separator should be added
     */
    private boolean requiresGroupingSeparatorAfterPosition(final int pos) {
        return pos > 1 && (pos % THOUSANDS_GROUP_SIZE) == 1;
    }

    /**
     * @param zeroCount number of zeros to add after the decimal point and before the
     *      first significant digit
     * @param fractionStartIdx significant digit start index
     * @param dst destination to append to
     * @param opts format options
     * @throws IOException if an I/O error occurs
     */
    private void appendFraction(final int zeroCount, final int startIdx, final Appendable dst,
            final FormatOptions opts) throws IOException {
        final String localizedDigits = opts.getDigits();
        final char localizedZero = localizedDigits.charAt(0);

        if (startIdx < digitCount) {
            dst.append(opts.getDecimalSeparator());

            // add the zero prefix
            for (int i = 0; i < zeroCount; ++i) {
                dst.append(localizedZero);
            }

            // add the fraction digits
            for (int i = startIdx; i < digitCount; ++i) {
                appendLocalizedDigit(digits[i], localizedDigits, dst);
            }
        } else if (opts.getIncludeFractionPlaceholder()) {
            dst.append(opts.getDecimalSeparator());
            dst.append(localizedZero);
        }
    }

    /** Append a localized digit character to {@code dst}.
     * @param n standard decimal digit
     * @param digitChars string containing the localized digit characters 0-9
     * @param dst destination to append to
     * @throws IOException if an I/O error occurs
     */
    private void appendLocalizedDigit(final int n, final String digitChars, final Appendable dst)
            throws IOException {
        dst.append(digitChars.charAt(n));
    }

    /** Return true if formatted strings should include the minus sign, considering
     * the value of this instance and the given format options.
     * @param opts format options
     * @return true if a minus sign should be included in the output
     */
    private boolean shouldIncludeMinus(final FormatOptions opts) {
        return negative && (opts.getSignedZero() || !isZero());
    }

    /** Return true if a rounding operation for the given number of digits should
     * round up.
     * @param count number of digits to round to; must be greater than zero and less
     *      than the current number of digits
     * @return true if a rounding operation for the given number of digits should
     *      round up
     */
    private boolean shouldRoundUp(final int count) {
        // Round up in the following cases:
        // 1. The digit after the last digit is greater than 5.
        // 2. The digit after the last digit is 5 and there are additional (non-zero)
        //      digits after it.
        // 3. The digit after the last digit is 5, there are no additional digits afterward,
        //      and the last digit is odd (half-even rounding).
        final int digitAfterLast = digits[count];

        return digitAfterLast > ROUND_CENTER || (digitAfterLast == ROUND_CENTER
                && (count < digitCount - 1 || (digits[count - 1] % 2) != 0));
    }

    /** Round the value up to the given number of digits.
     * @param count target number of digits; must be greater than zero and
     *      less than the current number of digits
     */
    private void roundUp(final int count) {
        int removedDigits = digitCount - count;
        int i;
        for (i = count - 1; i >= 0; --i) {
            final int d = digits[i] + 1;

            if (d < DECIMAL_RADIX) {
                // value did not carry over; done adding
                digits[i] = d;
                break;
            } else {
                // value carried over; the current position is 0
                // which we will ignore by shortening the digit count
                ++removedDigits;
            }
        }

        if (i < 0) {
            // all values carried over
            setSingleDigitValue(1, exponent + removedDigits);
        } else {
            // values were updated in-place; just need to update the length
            truncate(digitCount - removedDigits);
        }
    }

    /** Return true if this value is equal to zero. The sign field is ignored,
     * meaning that this method will return true for both {@code +0} and {@code -0}.
     * @return true if the value is equal to zero
     */
    boolean isZero() {
        return digits[0] == 0;
    }

    /** Set the value of this instance to a single digit with the given exponent.
     * The sign of the value is retained.
     * @param digit digit value
     * @param newExponent new exponent value
     */
    private void setSingleDigitValue(final int digit, final int newExponent) {
        digits[0] = digit;
        digitCount = 1;
        exponent = newExponent;
    }

    /** Truncate the value to the given number of digits.
     * @param count number of digits; must be greater than zero and less than
     *      the current number of digits
     */
    private void truncate(final int count) {
        int nonZeroCount = count;
        for (int i = count - 1;
                i >= 0 && digits[i] == 0;
                --i) {
            --nonZeroCount;
        }
        exponent += digitCount - nonZeroCount;
        digitCount = nonZeroCount;
    }

    /** Construct a new instance from the given double value.
     * @param d double value
     * @return a new instance containing the parsed components of the given double value
     * @throws IllegalArgumentException if {@code d} is {@code NaN} or infinite
     */
    public static SimpleDecimal from(final double d) {
        if (!Double.isFinite(d)) {
            throw new IllegalArgumentException("Double is not finite");
        }

        final String str = Double.toString(d);
        final char[] strChars = str.toCharArray();

        // extract the different portions of the string representation
        // (since double is finite, str is guaranteed to not be empty and to contain a
        // single decimal point according to the Double.toString() API)
        final boolean negative = strChars[0] == MINUS_CHAR;
        final int digitStartIdx = negative ? 1 : 0;

        final int[] digits = new int[strChars.length];

        int decimalSepIdx = -1;
        int exponentIdx = -1;
        int digitCount = 0;
        int firstNonZeroDigitIdx = -1;
        int lastNonZeroDigitIdx = -1;

        for (int i = digitStartIdx; i < strChars.length; ++i) {
            final char ch = strChars[i];

            if (ch == DECIMAL_SEP_CHAR) {
                decimalSepIdx = i;
            } else if (ch == EXPONENT_CHAR) {
                exponentIdx = i;
            } else if (exponentIdx < 0) {
                // this is a significand digit
                final int val = digitValue(ch);
                if (val != 0) {
                    if (firstNonZeroDigitIdx < 0) {
                        firstNonZeroDigitIdx = digitCount;
                    }
                    lastNonZeroDigitIdx = digitCount;
                }

                digits[digitCount++] = val;
            }
        }

        if (firstNonZeroDigitIdx > -1) {
            // determine the exponent
            final int explicitExponent = exponentIdx > -1
                    ? parseExponent(strChars, exponentIdx + 1)
                    : 0;
            final int exponent = explicitExponent + decimalSepIdx - digitStartIdx - lastNonZeroDigitIdx - 1;

            // get the number of significant digits, ignoring leading and trailing zeros
            final int significantDigitCount = lastNonZeroDigitIdx - firstNonZeroDigitIdx + 1;
            final int[] significantDigits = new int[significantDigitCount];
            System.arraycopy(
                    digits, firstNonZeroDigitIdx,
                    significantDigits, 0,
                    significantDigitCount);

            return new SimpleDecimal(negative, significantDigits, significantDigitCount, exponent);
        }

        // no non-zero digits, so value is zero
        return new SimpleDecimal(negative, new int[] {0}, 1, 0);
    }

    /** Parse a double exponent value from {@code chars}, starting at the {@code start}
     * index and continuing through the end of the array.
     * @param chars character array to parse a double exponent value from
     * @param start start index
     * @return parsed exponent value
     */
    private static int parseExponent(final char[] chars, final int start) {
        int exp = 0;
        boolean neg = false;

        for (int i = start; i < chars.length; ++i) {
            final char ch = chars[i];
            if (ch == MINUS_CHAR) {
                neg = !neg;
            } else if (ch != PLUS_CHAR) {
                exp = (exp * DECIMAL_RADIX) + digitValue(ch);
            }
        }

        return neg ? -exp : exp;
    }

    /** Get the numeric value of the given digit character. No validation of the
     * character type is performed.
     * @param ch digit character
     * @return numeric value of the digit character, ex: '1' = 1
     */
    private static int digitValue(final char ch) {
        return ch - ZERO_CHAR;
    }
}
