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
    static interface FormatOptions {

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

    /** One digit character. */
    private static final char ONE_CHAR = '1';

    /** String containing the decimal digits '0' - '9' in sequence. */
    private static final String DECIMAL_DIGITS = "0123456789";

    /** Shared instance representing the positive zero double value. */
    private static final SimpleDecimal POS_ZERO = new SimpleDecimal(false, String.valueOf(ZERO_CHAR), 0);

    /** Shared instance representing the negative zero double value. */
    private static final SimpleDecimal NEG_ZERO = new SimpleDecimal(true, String.valueOf(ZERO_CHAR), 0);

    /** True if the value is negative. */
    private final boolean negative;

    /** String containing the significant base-10 digits for the value. */
    private final String digits;

    /** Exponent for the value. */
    private final int exponent;

    /** Construct a new instance from its parts.
     * @param negative true if the value is negative
     * @param digits string containing significant digits
     * @param exponent exponent value
     */
    SimpleDecimal(final boolean negative, final String digits, final int exponent) {
        this.negative = negative;
        this.digits = digits;
        this.exponent = exponent;
    }

    /** Return true if the value is negative.
     * @return true if the value is negative
     */
    public boolean isNegative() {
        return negative;
    }

    /** Get a string containing the significant digits of the value. If the value is
     * {@code 0}, then the returned string is {@code "0"}. Otherwise, the string contains
     * one or more characters with the first and last characters not equal to {@code '0'}.
     * @return string containing the significant digits of the value
     */
    public String getDigits() {
        return digits;
    }

    /** Get the exponent value. This exponent produces a floating point value with the
     * correct magnitude when applied to the unsigned integer represented by the {@link #getDigits() digit}
     * string.
     * @return exponent value
     */
    public int getExponent() {
        return exponent;
    }

    /** Return true if the value is equal to zero. The sign field is ignored,
     * meaning that this method will return true for both {@code +0} and {@code -0}.
     * @return true if the value is equal to zero
     */
    public boolean isZero() {
        return getPrecision() == 1 && digits.charAt(0) == ZERO_CHAR;
    }

    /** Return the precision of this instance, meaning the number of significant decimal
     * digits in the representation.
     * @return the precision of this instance
     */
    public int getPrecision() {
        return digits.length();
    }

    /** Get the exponent that would be used when representing this number in scientific
     * notation (i.e., with a single non-zero digit in front of the decimal point.
     * @return the exponent that would be used when representing this number in scientific
     *      notation
     */
    public int getScientificExponent() {
        return getPrecision() + exponent - 1;
    }

    /** Round the instance to the given decimal exponent position using
     * {@link java.math.RoundingMode#HALF_EVEN half-even rounding}. For example, a value of {@code -2}
     * will round the instance to the digit at the position 10<sup>-2</sup> (i.e. to the closest multiple of 0.01).
     * A new instance is returned if the rounding operation results in a new value.
     * @param roundExponent exponent defining the decimal place to round to
     * @return result of the rounding operation
     */
    public SimpleDecimal round(final int roundExponent) {
        if (roundExponent > exponent) {
            final int precision = getPrecision();
            final int max = precision + exponent;

            if (roundExponent < max) {
                return maxPrecision(max - roundExponent);
            } else if (roundExponent == max && shouldRoundUp(0)) {
                return new SimpleDecimal(negative, "1", roundExponent);
            }

            return isNegative() ? NEG_ZERO : POS_ZERO;
        }

        return this;
    }

    /** Return the value as close as possible to this instance with <em>at most</em> the given number
     * of significant digits (i.e. precision). If this instance already has a precision less than or equal
     * to the argument, it is returned directly. If the given precision requires a reduction in the number
     * of digits, then the value is rounded using {@link java.math.RoundingMode#HALF_EVEN half-even rounding}
     * and a new instance is returned with the rounded value.
     * @param precision maximum number of significant digits to include
     * @return the instance as close as possible to this value with at most the given number of
     *      significant digits
     * @throws IllegalArgumentException if {@code precision} is less than 1
     */
    public SimpleDecimal maxPrecision(final int precision) {
        if (precision < 1) {
            throw new IllegalArgumentException("Precision must be greater than zero; was " + precision);
        }

        final int currentPrecision = getPrecision();
        if (currentPrecision > precision) {
            // we need to round to reduce the number of digits
            String resultDigits = shouldRoundUp(precision) ?
                    addOne(digits, precision) :
                    digits.substring(0, precision);

            // compute the initial result exponent
            int resultExponent = exponent + (currentPrecision - precision);

            // remove zeros from the end of the integer if present, adjusting the
            // exponent as needed
            final int lastNonZeroIdx = findLastNonZero(resultDigits);
            if (lastNonZeroIdx < resultDigits.length() - 1) {
                resultExponent += resultDigits.length() - 1 - lastNonZeroIdx;
                resultDigits = resultDigits.substring(0, lastNonZeroIdx + 1);
            }

            return new SimpleDecimal(negative, resultDigits, resultExponent);
        }

        return this; // no change needed
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
        final int wholeDigitCount = getPrecision() + exponent;

        final int fractionStartIdx = opts.getGroupThousands() ?
                appendWholeGrouped(wholeDigitCount, dst, opts) :
                appendWhole(wholeDigitCount, dst, opts);

        final int fractionZeroCount = wholeDigitCount < 0 ?
                Math.abs(wholeDigitCount) :
                0;

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
        final int wholeDigitCount = 1 + Math.floorMod(getPrecision() + exponent - 1, 3);
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
        final int precision = getPrecision();
        final String localizedDigits = opts.getDigits();

        final int fractionStartIdx = appendWhole(wholeDigitCount, dst, opts);
        appendFraction(0, fractionStartIdx, dst, opts);

        // add the exponent but only if non-zero or explicitly requested
        final int resultExponent = exponent + precision - wholeDigitCount;
        if (resultExponent != 0 || opts.getAlwaysIncludeExponent()) {
            dst.append(opts.getExponentSeparator());

            if (resultExponent < 0) {
                dst.append(opts.getMinusSign());
            }

            final String exponentStr = Integer.toString(Math.abs(resultExponent));
            appendLocalizedDigits(exponentStr, 0, exponentStr.length(), localizedDigits, dst);
        }
    }

    /** Append {@code count} characters from the beginning of {@code digits} as the whole number
     * portion of the string representation. The index of the next character from {@code digits}
     * is returned if any characters remain. Otherwise, {@code digits.length()} is returned.
     * @param count number of digits to append
     * @param dst destination to append to
     * @param opts format options
     * @return index of the next character from {@code digits} or the {@code digits.length()}
     *      if all character have been appended
     * @throws IOException in an I/O error occurs
     */
    private int appendWhole(final int count, final Appendable dst, final FormatOptions opts)
            throws IOException {
        if (shouldIncludeMinus(opts)) {
            dst.append(opts.getMinusSign());
        }

        final String localizedDigits = opts.getDigits();
        final char localizedZero = localizedDigits.charAt(0);

        final int available = digits.length();
        final int significantDigitCount = Math.max(0, Math.min(count, available));

        if (significantDigitCount > 0) {
            int i;
            for (i = 0; i < significantDigitCount; ++i) {
                appendLocalizedDigit(digits.charAt(i), localizedDigits, dst);
            }

            for (;i < count; ++i) {
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
    private int appendWholeGrouped(final int count, final Appendable dst, final FormatOptions opts)
            throws IOException {
        if (shouldIncludeMinus(opts)) {
            dst.append(opts.getMinusSign());
        }

        final String localizedDigits = opts.getDigits();
        final char localizedZero = localizedDigits.charAt(0);
        final char groupingChar = opts.getThousandsGroupingSeparator();

        final int available = digits.length();
        final int significantDigitCount = Math.max(0, Math.min(count, available));

        if (significantDigitCount > 0) {
            int i;
            int pos = count;
            for (i = 0; i < significantDigitCount; ++i, --pos) {
                appendLocalizedDigit(digits.charAt(i), localizedDigits, dst);
                if (pos > 1 && (pos % 3) == 1) {
                    dst.append(groupingChar);
                }
            }

            for (;i < count; ++i, --pos) {
                dst.append(localizedZero);
                if (pos > 1 && (pos % 3) == 1) {
                    dst.append(groupingChar);
                }
            }
        } else {
            dst.append(localizedZero);
        }

        return significantDigitCount;
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

        final int len = digits.length();
        if (startIdx < len) {
            dst.append(opts.getDecimalSeparator());

            // add the zero prefix
            for (int i = 0; i < zeroCount; ++i) {
                dst.append(localizedZero);
            }

            // add the fraction digits
            appendLocalizedDigits(digits, startIdx, len, localizedDigits, dst);
        } else if (opts.getIncludeFractionPlaceholder()){
            dst.append(opts.getDecimalSeparator());
            dst.append(localizedZero);
        }
    }

    /** Append a localized digit character to {@code dst}.
     * @param digit standard decimal digit
     * @param digitChars string containing the localized digit characters 0-9
     * @param dst destination to append to
     * @throws IOException if an I/O error occurs
     */
    private void appendLocalizedDigit(final char digit, final String digitChars, final Appendable dst)
            throws IOException {
        dst.append(digitChars.charAt(digitValue(digit)));
    }

    /** Append localized digit characters from {@code seq} to {@code dst}.
     * @param seq sequence to get characters from
     * @param startIdx start index, inclusive
     * @param endIdx end index, exclusive
     * @param digitChars string containing the localized digit characters 0-9
     * @param dst destination to append to
     * @throws IOException if an I/O error occurs
     */
    private void appendLocalizedDigits(final CharSequence seq, final int startIdx, final int endIdx,
            final String digitChars, final Appendable dst) throws IOException {
        for (int i = startIdx; i < endIdx; ++i) {
            appendLocalizedDigit(seq.charAt(i), digitChars, dst);
        }
    }

    /** Return true if formatted strings should include the minus sign, considering
     * the value of this instance and the given format options.
     * @param opts format options
     * @return true if a minus sign should be included in the output
     */
    private boolean shouldIncludeMinus(final FormatOptions opts) {
        return negative && (opts.getSignedZero() || !isZero());
    }

    /** Return true if a rounding operation at the given index should round up.
     * @param idx index of the digit to round; must be a valid index into {@code digits}
     * @return true if a rounding operation at the given index should round up
     */
    private boolean shouldRoundUp(final int idx) {
        // Round up in the following cases:
        // 1. The digit at the index is greater than 5.
        // 2. The digit at the index is 5 and there are additional (non-zero)
        //      digits after it.
        // 3. The digit is 5, there are no additional digits afterward,
        //      and the digit before it is odd (half-even rounding).
        final int precision = getPrecision();
        final int roundValue = digitValue(digits.charAt(idx));

        return roundValue > 5 || (roundValue == 5 &&
                (idx < precision - 1 || (idx > 0 && digitValue(digits.charAt(idx - 1)) % 2 != 0)));
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

        final char[] digitChars = new char[strChars.length];

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
                if (ch != ZERO_CHAR) {
                    if (firstNonZeroDigitIdx < 0) {
                        firstNonZeroDigitIdx = digitCount;
                    }
                    lastNonZeroDigitIdx = digitCount;
                }

                digitChars[digitCount++] = ch;
            }
        }

        if (firstNonZeroDigitIdx > -1) {
            // determine the exponent
            final int explicitExponent = exponentIdx > -1 ?
                    parseExponent(str, exponentIdx + 1) :
                    0;
            final int exponent = explicitExponent + decimalSepIdx - digitStartIdx - lastNonZeroDigitIdx - 1;

            // get the digit string without any leading or trailing zeros
            final String digits = String.valueOf(
                    digitChars,
                    firstNonZeroDigitIdx,
                    lastNonZeroDigitIdx - firstNonZeroDigitIdx + 1);

            return new SimpleDecimal(negative, digits, exponent);
        }

        // no non-zero digits, so value is zero
        return negative ?
                NEG_ZERO :
                POS_ZERO;
    }

    /** Parse a double exponent value from {@code seq}, starting at the {@code start}
     * index and continuing through the end of the sequence.
     * @param seq sequence to part a double exponent value from
     * @param start start index
     * @return parsed exponent value
     */
    private static int parseExponent(final CharSequence seq, final int start) {
        int exp = 0;
        boolean neg = false;

        final int len = seq.length();
        for (int i = start; i < len; ++i) {
            final char ch = seq.charAt(i);
            if (ch == MINUS_CHAR) {
                neg = !neg;
            } else if (ch != PLUS_CHAR) {
                exp = (exp * 10) + digitValue(ch);
            }
        }

        return neg ? -exp : exp;
    }

    /** Return the index of the last character in the argument not equal
     * to {@code '0'} or {@code -1} if no such character can be found.
     * @param seq sequence to search
     * @return the index of the last non-zero character or {@code -1} if not found
     */
    private static int findLastNonZero(final CharSequence seq) {
        int i;
        char ch;
        for (i = seq.length() - 1; i >= 0; --i) {
            ch = seq.charAt(i);
            if (ch != ZERO_CHAR) {
                break;
            }
        }

        return i;
    }

    /** Get the numeric value of the given digit character. No validation of the
     * character type is performed.
     * @param ch digit character
     * @return numeric value of the digit character, ex: '1' = 1
     */
    private static int digitValue(final char ch) {
        return ch - ZERO_CHAR;
    }

    /** Add one to the value of the integer represented by the substring of length {@code len}
     * starting at index {@code 0}, returning the result as another string. The input is assumed
     * to contain only digit characters
     * (i.e. '0' - '9'). No validation is performed.
     * @param digitStr string containing a representation of an integer
     * @param len number of characters to use from {@code str}
     * @return string representation of the result of adding 1 to the integer represented
     *      by the input substring
     */
    private static String addOne(final String digitStr, final int len) {
        final char[] resultChars = new char[len + 1];

        boolean carrying = true;
        for (int i = len - 1; i >= 0; --i) {
            final char inChar = digitStr.charAt(i);
            final char outChar = carrying ?
                    DECIMAL_DIGITS.charAt((digitValue(inChar) + 1) % DECIMAL_DIGITS.length()) :
                    inChar;
            resultChars[i + 1] = outChar;

            if (carrying && outChar != ZERO_CHAR) {
                carrying = false;
            }
        }

        if (carrying) {
            resultChars[0] = ONE_CHAR;
            return String.valueOf(resultChars);
        }

        return String.valueOf(resultChars, 1, len);
    }
}
