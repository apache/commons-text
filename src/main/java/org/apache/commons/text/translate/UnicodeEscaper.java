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

import java.io.IOException;
import java.io.Writer;

/**
 * Translates code points to their Unicode escaped value.
 *
 * @since 1.0
 */
public class UnicodeEscaper extends CodePointTranslator {

    /**
     * Constructs a {@code UnicodeEscaper} above the specified value (exclusive).
     *
     * @param codePoint above which to escape
     * @return The newly created {@code UnicodeEscaper} instance
     */
    public static UnicodeEscaper above(final int codePoint) {
        return outsideOf(0, codePoint);
    }
    /**
     * Constructs a {@code UnicodeEscaper} below the specified value (exclusive).
     *
     * @param codePoint below which to escape
     * @return The newly created {@code UnicodeEscaper} instance
     */
    public static UnicodeEscaper below(final int codePoint) {
        return outsideOf(codePoint, Integer.MAX_VALUE);
    }
    /**
     * Constructs a {@code UnicodeEscaper} between the specified values (inclusive).
     *
     * @param codePointLow above which to escape
     * @param codePointHigh below which to escape
     * @return The newly created {@code UnicodeEscaper} instance
     */
    public static UnicodeEscaper between(final int codePointLow, final int codePointHigh) {
        return new UnicodeEscaper(codePointLow, codePointHigh, true);
    }

    /**
     * Constructs a {@code UnicodeEscaper} outside of the specified values (exclusive).
     *
     * @param codePointLow below which to escape
     * @param codePointHigh above which to escape
     * @return The newly created {@code UnicodeEscaper} instance
     */
    public static UnicodeEscaper outsideOf(final int codePointLow, final int codePointHigh) {
        return new UnicodeEscaper(codePointLow, codePointHigh, false);
    }

    /** The lowest code point boundary. */
    private final int below;

    /** The highest code point boundary. */
    private final int above;

    /** Whether to escape between the boundaries or outside them. */
    private final boolean between;

    /**
     * Constructs a {@code UnicodeEscaper} for all characters.
     *
     */
    public UnicodeEscaper() {
        this(0, Integer.MAX_VALUE, true);
    }

    /**
     * Constructs a {@code UnicodeEscaper} for the specified range. This is
     * the underlying method for the other constructors/builders. The {@code below}
     * and {@code above} boundaries are inclusive when {@code between} is
     * {@code true} and exclusive when it is {@code false}.
     *
     * @param below int value representing the lowest code point boundary
     * @param above int value representing the highest code point boundary
     * @param between whether to escape between the boundaries or outside them
     */
    protected UnicodeEscaper(final int below, final int above, final boolean between) {
        this.below = below;
        this.above = above;
        this.between = between;
    }

    /**
     * Converts the given code point to a hexadecimal string of the form {@code "\\uXXXX"}.
     *
     * @param codePoint
     *            a Unicode code point
     * @return The hexadecimal string for the given code point
     *
     */
    protected String toUtf16Escape(final int codePoint) {
        return "\\u" + hex(codePoint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean translate(final int codePoint, final Writer writer) throws IOException {
        if (between) {
            if (codePoint < below || codePoint > above) {
                return false;
            }
        } else if (codePoint >= below && codePoint <= above) {
            return false;
        }

        if (codePoint > 0xffff) {
            writer.write(toUtf16Escape(codePoint));
        } else {
          writer.write("\\u");
          writer.write(HEX_DIGITS[codePoint >> 12 & 15]);
          writer.write(HEX_DIGITS[codePoint >> 8 & 15]);
          writer.write(HEX_DIGITS[codePoint >> 4 & 15]);
          writer.write(HEX_DIGITS[codePoint & 15]);
        }
        return true;
    }
}
