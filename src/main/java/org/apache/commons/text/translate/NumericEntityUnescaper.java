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
package org.apache.commons.text.translate;

import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;

/**
 * Translates XML numeric entities of the form &amp;#[xX]?\d+;? to
 * the specific code point.
 *
 * Note that the semicolon is optional.
 *
 * @since 1.0
 */
public class NumericEntityUnescaper extends CharSequenceTranslator {

    /**
     * Enumerates NumericEntityUnescaper options for unescaping.
     */
    public enum OPTION {

        /**
         * Requires a semicolon.
         */
        semiColonRequired,

        /**
         * Does not require a semicolon.
         */
        semiColonOptional,

        /**
         * Throws an exception if a semicolon is missing.
         */
        errorIfNoSemiColon
    }

    /**
     * Default options.
     */
    private static final EnumSet<OPTION> DEFAULT_OPTIONS = EnumSet
            .copyOf(Collections.singletonList(OPTION.semiColonRequired));

    /**
     * EnumSet of OPTIONS, given from the constructor, read-only.
     */
    private final EnumSet<OPTION> options;

    /**
     * Creates a UnicodeUnescaper.
     * <p>
     * The constructor takes a list of options, only one type of which is currently
     * available (whether to allow, error or ignore the semicolon on the end of a
     * numeric entity to being missing).
     * <p>
     * For example, to support numeric entities without a ';':
     * new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional)
     * and to throw an IllegalArgumentException when they're missing:
     * new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon)
     * <p>
     * Note that the default behavior is to ignore them.
     *
     * @param options to apply to this unescaper
     */
    public NumericEntityUnescaper(final OPTION... options) {
        this.options = ArrayUtils.isEmpty(options) ? DEFAULT_OPTIONS : EnumSet.copyOf(Arrays.asList(options));
    }

    /**
     * Tests whether the passed in option is currently set.
     *
     * @param option to check state of
     * @return whether the option is set
     */
    public boolean isSet(final OPTION option) {
        return options.contains(option);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int translate(final CharSequence input, final int index, final Writer writer) throws IOException {
        final int seqEnd = input.length();

        if (!isNumericEntityStart(input, index, seqEnd)) {
            return 0;
        }

        int start = index + 2;
        boolean isHex = isHexStart(input, start);
        if (isHex) {
            start++;
            if (start == seqEnd) {
                return 0;
            }
        }

        int end = scanNumericEntityEnd(input, start, seqEnd);
        boolean semiNext = hasSemicolon(input, end, seqEnd);

        if (!semiNext) {
            if (isSet(OPTION.semiColonRequired)) {
                return 0;
            }
            if (isSet(OPTION.errorIfNoSemiColon)) {
                throw new IllegalArgumentException("Semi-colon required at end of numeric entity");
            }
        }

        final int entityValue;
        try {
            String numberStr = input.subSequence(start, end).toString();
            entityValue = Integer.parseInt(numberStr, isHex ? 16 : 10);
        } catch (final NumberFormatException nfe) {
            return 0;
        }

        writeEntity(writer, entityValue);
        return calculateConsumedLength(index, start, end, isHex, semiNext);
    }

    /**Extracted helper methods
     *
     * @param input
     * @param index
     * @param seqEnd
     * @return
     */

    private boolean isNumericEntityStart(CharSequence input, int index, int seqEnd) {
        return index < seqEnd - 2 &&
                input.charAt(index) == '&' &&
                input.charAt(index + 1) == '#';
    }

    private boolean isHexStart(CharSequence input, int start) {
        char ch = input.charAt(start);
        return ch == 'x' || ch == 'X';
    }

    private int scanNumericEntityEnd(CharSequence input, int start, int seqEnd) {
        int end = start;
        while (end < seqEnd && isHexDigit(input.charAt(end))) {
            end++;
        }
        return end;
    }

    private boolean isHexDigit(char ch) {
        return (ch >= '0' && ch <= '9') ||
                (ch >= 'a' && ch <= 'f') ||
                (ch >= 'A' && ch <= 'F');
    }

    private boolean hasSemicolon(CharSequence input, int end, int seqEnd) {
        return end != seqEnd && input.charAt(end) == ';';
    }

    private void writeEntity(Writer writer, int entityValue) throws IOException {
        if (entityValue > 0xFFFF) {
            final char[] chrs = Character.toChars(entityValue);
            writer.write(chrs[0]);
            writer.write(chrs[1]);
        } else {
            writer.write(entityValue);
        }
    }

    private int calculateConsumedLength(int index, int start, int end, boolean isHex, boolean semiNext) {
        return 2 + (end - start) + (isHex ? 1 : 0) + (semiNext ? 1 : 0);
    }
}