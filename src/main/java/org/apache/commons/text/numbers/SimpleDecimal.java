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

/** Internal class representing a decimal value parsed into separate components. Each number
 * is represented with
 * <ul>
 *  <li>a boolean flag for the sign,</li>
 *  <li> a sequence of the digits {@code 0 - 10} representing an unsigned integer with leading and trailing zeros
 *      removed, and</li>
 *  <li>an exponent value that when applied to the base 10 digits produces a floating point value with the
 *      correct magnitude.</li>
 * </ul>
 * <p><strong>Examples</strong></p>
 * <table>
 *  <tr><th>Double</th><th>Negative</th><th>Digits</th><th>Exponent</th></tr>
 *  <tr><td>0.0</td><td>false</td><td>[0]</td><td>0</td></tr>
 *  <tr><td>1.2</td><td>false</td><td>[1, 2]</td><td>-1</td></tr>
 *  <tr><td>-0.00971</td><td>true</td><td>[9, 7, 1]</td><td>-5</td></tr>
 *  <tr><td>56300</td><td>true</td><td>[5, 6, 3]</td><td>2</td></tr>
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

        /** Get an array containing the localized digit characters 0-9 in that order.
         * This string <em>must</em> be non-null and have a length of 10.
         * @return array containing the digit characters 0-9
         */
        char[] getDigits();

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

        /** Get the exponent separator as an array of characters.
         * @return exponent separator as an array of characters
         */
        char[] getExponentSeparatorChars();

        /** Return true if exponent values should always be included in
         * formatted output, even if the value is zero.
         * @return true if exponent values should always be included
         */
        boolean getAlwaysIncludeExponent();
    }

    /** Minus sign character. */
    private static final char MINUS_CHAR = '-';

    /** Decimal separator character. */
    private static final char DECIMAL_SEP_CHAR = '.';

    /** Exponent character. */
    private static final char EXPONENT_CHAR = 'E';

    /** Zero digit character. */
    private static final char ZERO_CHAR = '0';

    /** Number of characters in thousands groupings. */
    private static final int THOUSANDS_GROUP_SIZE = 3;

    /** Radix for decimal numbers. */
    private static final int DECIMAL_RADIX = 10;

    /** Center value used when rounding. */
    private static final int ROUND_CENTER = DECIMAL_RADIX / 2;

    /** Number that exponents in engineering format must be a multiple of. */
    private static final int ENG_EXPONENT_MOD = 3;

    /** True if the value is negative. */
    final boolean negative;

    /** Array containing the significant decimal digits for the value. */
    final int[] digits;

    /** Number of digits used in the digits array; not necessarily equal to the length. */
    int digitCount;

    /** Exponent for the value. */
    int exponent;

    /** Output buffer for use in creating string representations. */
    private char[] outputChars;

    /** Output buffer index. */
    private int outputIdx;

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

    /** Return a string representation of this value with no exponent field. Ex:
     * <pre>
     * 10 = "10.0"
     * 1e-6 = "0.000001"
     * 1e11 = "100000000000.0"
     * </pre>
     * @param opts format options
     * @return value in plain format
     */
    public String toPlainString(final FormatOptions opts) {
        final int decimalPos = digitCount + exponent;
        final int fractionZeroCount = decimalPos < 1
                ? Math.abs(decimalPos)
                : 0;

        prepareOutput(getPlainStringSize(decimalPos, opts));

        final int fractionStartIdx = opts.getGroupThousands()
                ? appendWholeGrouped(decimalPos, opts)
                : appendWhole(decimalPos, opts);

        appendFraction(fractionZeroCount, fractionStartIdx, opts);

        return outputString();
    }

    /** Return a string representation of this value in scientific notation. Ex:
     * <pre>
     * 0 = "0.0"
     * 10 = "1.0E1"
     * 1e-6 = "1.0E-6"
     * 1e11 = "1.0E11"
     * </pre>
     * @param opts format options
     * @return value in scientific format
     */
    public String toScientificString(final FormatOptions opts) {
        return toScientificString(1, opts);
    }

    /** Return a string representation of this value in engineering notation. This
     * is similar to {@link #toScientificString(FormatOptions) scientific notation}
     * but with the exponent forced to be a multiple of 3, allowing easier alignment with SI prefixes.
     * <pre>
     * 0 = "0.0"
     * 10 = "10.0"
     * 1e-6 = "1.0E-6"
     * 1e11 = "100.0E9"
     * </pre>
     * @param opts format options
     * @return value in engineering format
     */
    public String toEngineeringString(final FormatOptions opts) {
        final int decimalPos = 1 + Math.floorMod(getScientificExponent(), ENG_EXPONENT_MOD);
        return toScientificString(decimalPos, opts);
    }

    /** Return a string representation of the value in scientific notation using the
     * given decimal point position.
     * @param decimalPos decimal position relative to the {@code digits} array; this value
     *      is expected to be greater than 0
     * @param opts format options
     * @return value in scientific format
     */
    private String toScientificString(final int decimalPos, final FormatOptions opts) {
        final int resultExponent = digitCount + exponent - decimalPos;

        // only include the exponent string if it is non-zero or it is requested to be present
        final char[] resultExponentChars = resultExponent != 0 || opts.getAlwaysIncludeExponent()
                ? Integer.toString(Math.abs(resultExponent)).toCharArray()
                : null;

        prepareOutput(getScientificStringSize(decimalPos, resultExponent, resultExponentChars, opts));

        final int fractionStartIdx = appendWhole(decimalPos, opts);
        appendFraction(0, fractionStartIdx, opts);

        if (resultExponentChars != null) {
            append(opts.getExponentSeparatorChars());

            if (resultExponent < 0) {
                append(opts.getMinusSign());
            }

            final char[] localizedDigits = opts.getDigits();
            for (int i = 0; i < resultExponentChars.length; ++i) {
                final int val = digitValue(resultExponentChars[i]);
                appendLocalizedDigit(val, localizedDigits);
            }
        }

        return outputString();
    }

    /** Prepare the output buffer for a string of the given size.
     * @param size buffer size
     */
    private void prepareOutput(final int size) {
        outputChars = new char[size];
        outputIdx = 0;
    }

    /** Get the output buffer as a string.
     * @return output buffer as a string
     */
    private String outputString() {
        final String str = String.valueOf(outputChars);
        outputChars = null;
        return str;
    }

    /** Append the given character to the output buffer.
     * @param ch character to append
     */
    private void append(final char ch) {
        outputChars[outputIdx++] = ch;
    }

    /** Append the given character array directly to the output buffer.
     * @param chars characters to append
     */
    private void append(final char[] chars) {
        for (int i = 0; i < chars.length; ++i) {
            append(chars[i]);
        }
    }

    /** Append the localized representation of the digit {@code n} to the output buffer.
     * @param n digit to append
     * @param digitChars character array containing localized versions of the digits {@code 0-9}
     *      in that order
     */
    private void appendLocalizedDigit(final int n, final char[] digitChars) {
        append(digitChars[n]);
    }

    /** Append the whole number portion of this value to the output buffer. No thousands
     * separators are added.
     * @param wholeCount total number of digits required to the left of the decimal point
     * @param opts format options
     * @return number of digits from {@code digits} appended to the output buffer
     * @see #appendWholeGrouped(int, FormatOptions)
     */
    private int appendWhole(final int wholeCount, final FormatOptions opts) {
        if (shouldIncludeMinus(opts)) {
            append(opts.getMinusSign());
        }

        final char[] localizedDigits = opts.getDigits();
        final char localizedZero = localizedDigits[0];

        final int significantDigitCount = Math.max(0, Math.min(wholeCount, digitCount));

        if (significantDigitCount > 0) {
            int i;
            for (i = 0; i < significantDigitCount; ++i) {
                appendLocalizedDigit(digits[i], localizedDigits);
            }

            for (; i < wholeCount; ++i) {
                append(localizedZero);
            }
        } else {
            append(localizedZero);
        }

        return significantDigitCount;
    }

    /** Append the whole number portion of this value to the output buffer, adding thousands
     * separators as needed.
     * @param wholeCount total number of digits required to the right of the decimal point
     * @param opts format options
     * @return number of digits from {@code digits} appended to the output buffer
     * @see #appendWhole(int, FormatOptions)
     */
    private int appendWholeGrouped(final int wholeCount, final FormatOptions opts) {
        if (shouldIncludeMinus(opts)) {
            append(opts.getMinusSign());
        }

        final char[] localizedDigits = opts.getDigits();
        final char localizedZero = localizedDigits[0];
        final char groupingChar = opts.getThousandsGroupingSeparator();

        final int appendCount = Math.max(0, Math.min(wholeCount, digitCount));

        if (appendCount > 0) {
            int i;
            int pos = wholeCount;
            for (i = 0; i < appendCount; ++i, --pos) {
                appendLocalizedDigit(digits[i], localizedDigits);
                if (requiresGroupingSeparatorAfterPosition(pos)) {
                    append(groupingChar);
                }
            }

            for (; i < wholeCount; ++i, --pos) {
                append(localizedZero);
                if (requiresGroupingSeparatorAfterPosition(pos)) {
                    append(groupingChar);
                }
            }
        } else {
            append(localizedZero);
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

    /** Append the fractional component of the number to the current output buffer.
     * @param zeroCount number of zeros to add after the decimal point and before the
     *      first significant digit
     * @param fractionStartIdx significant digit start index
     * @param opts format options
     */
    private void appendFraction(final int zeroCount, final int startIdx, final FormatOptions opts) {
        final char[] localizedDigits = opts.getDigits();
        final char localizedZero = localizedDigits[0];

        if (startIdx < digitCount) {
            append(opts.getDecimalSeparator());

            // add the zero prefix
            for (int i = 0; i < zeroCount; ++i) {
                append(localizedZero);
            }

            // add the fraction digits
            for (int i = startIdx; i < digitCount; ++i) {
                appendLocalizedDigit(digits[i], localizedDigits);
            }
        } else if (opts.getIncludeFractionPlaceholder()) {
            append(opts.getDecimalSeparator());
            append(localizedZero);
        }
    }

    /** Get the number of characters required to create a plain format representation
     * of this value.
     * @param decimalPos decimal position relative to the {@code digits} array
     * @param opts format options
     * @return number of characters in the plain string representation of this value,
     *      created using the given parameters
     */
    private int getPlainStringSize(final int decimalPos, final FormatOptions opts) {
        int size = getDigitStringSize(decimalPos, opts);

        // adjust for groupings if needed
        if (opts.getGroupThousands() && decimalPos > 0) {
            size += (decimalPos - 1) / THOUSANDS_GROUP_SIZE;
        }

        return size;
    }

    /** Get the number of characters required to create a scientific format representation
     * of this value.
     * @param decimalPos decimal point position relative to the {@code digits} array
     * @param resultExponent target exponent
     * @param resultExponentChars target exponent characters; if null, no explicit exponent is to be included
     * @param opts format options
     * @return number of characters in the scientific representation of this string, using the
     *      given parameters
     */
    private int getScientificStringSize(final int decimalPos, final int resultExponent,
            final char[] resultExponentChars, final FormatOptions opts) {
        int size = getDigitStringSize(decimalPos, opts);
        if (resultExponentChars != null) {
            size += opts.getExponentSeparatorChars().length + resultExponentChars.length;
            if (resultExponent < 0) {
                // adjust for the exponent minus sign
                ++size;
            }
        }
        return size;
    }

    /** Get the number of characters required for the digit portion of a string representation of
     * this value. This excludes any exponent or thousands groupings characters.
     * @param decimalPos decimal point position relative to the {@code digits} array
     * @param opts format options
     * @return number of characters required for the digit portion of a string representation of
     *      this value
     */
    private int getDigitStringSize(final int decimalPos, final FormatOptions opts) {
        int size = digitCount;
        if (shouldIncludeMinus(opts)) {
            ++size;
        }
        if (decimalPos < 1) {
            // no whole component;
            // add decimal point and leading zeros
            size += 2 + Math.abs(decimalPos);
        } else if (decimalPos >= digitCount) {
            // no fraction component;
            // add trailing zeros
            size += decimalPos - digitCount;
            if (opts.getIncludeFractionPlaceholder()) {
                size += 2;
            }
        } else {
            // whole and fraction components;
            // add decimal point
            size += 1;
        }

        return size;
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

        // Get the canonical string representation of the double value and parse
        // it to extract the components of the decimal value. From the documentation
        // of Double.toString() and the fact that d is finite, we are guaranteed the
        // following:
        // - the string will not be empty
        // - it will contain exactly one decimal point character
        // - all digit characters are in the ASCII range
        final char[] strChars = Double.toString(d).toCharArray();

        final boolean negative = strChars[0] == MINUS_CHAR;
        final int digitStartIdx = negative ? 1 : 0;

        final int[] digits = new int[strChars.length];

        boolean foundDecimalPoint = false;
        int digitCount = 0;
        int significantDigitCount = 0;
        int decimalPos = 0;

        int i;
        for (i = digitStartIdx; i < strChars.length; ++i) {
            final char ch = strChars[i];

            if (ch == DECIMAL_SEP_CHAR) {
                foundDecimalPoint = true;
                decimalPos = digitCount;
            } else if (ch == EXPONENT_CHAR) {
                // no more mantissa digits
                break;
            } else if (ch != ZERO_CHAR || digitCount > 0) {
                // this is either the first non-zero digit or one after it
                final int val = digitValue(ch);
                digits[digitCount++] = val;

                if (val > 0) {
                    significantDigitCount = digitCount;
                }
            } else if (foundDecimalPoint) {
                // leading zero in a fraction; adjust the decimal position
                --decimalPos;
            }
        }

        if (digitCount > 0) {
            // determine the exponent
            final int explicitExponent = i < strChars.length
                    ? parseExponent(strChars, i + 1)
                    : 0;
            final int exponent = explicitExponent + decimalPos - significantDigitCount;

            return new SimpleDecimal(negative, digits, significantDigitCount, exponent);
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
        int i = start;
        boolean neg = chars[i] == MINUS_CHAR;
        if (neg) {
            ++i;
        }

        int exp = 0;
        for (; i < chars.length; ++i) {
            exp = (exp * DECIMAL_RADIX) + digitValue(chars[i]);
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
