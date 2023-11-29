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

/**
 * Translates code points to their Unicode escaped value suitable for Java source.
 *
 * @since 1.0
 */
public class JavaUnicodeEscaper extends UnicodeEscaper {

    /**
     * Constructs a {@code JavaUnicodeEscaper} above the specified value (exclusive).
     *
     * @param codePoint
     *            above which to escape
     * @return The newly created {@code UnicodeEscaper} instance
     */
    public static JavaUnicodeEscaper above(final int codePoint) {
        return outsideOf(0, codePoint);
    }

    /**
     * Constructs a {@code JavaUnicodeEscaper} below the specified value (exclusive).
     *
     * @param codePoint
     *            below which to escape
     * @return The newly created {@code UnicodeEscaper} instance
     */
    public static JavaUnicodeEscaper below(final int codePoint) {
        return outsideOf(codePoint, Integer.MAX_VALUE);
    }

    /**
     * Constructs a {@code JavaUnicodeEscaper} between the specified values (inclusive).
     *
     * @param codePointLow
     *            above which to escape
     * @param codePointHigh
     *            below which to escape
     * @return The newly created {@code UnicodeEscaper} instance
     */
    public static JavaUnicodeEscaper between(final int codePointLow, final int codePointHigh) {
        return new JavaUnicodeEscaper(codePointLow, codePointHigh, true);
    }

    /**
     * Constructs a {@code JavaUnicodeEscaper} outside of the specified values (exclusive).
     *
     * @param codePointLow
     *            below which to escape
     * @param codePointHigh
     *            above which to escape
     * @return The newly created {@code UnicodeEscaper} instance
     */
    public static JavaUnicodeEscaper outsideOf(final int codePointLow, final int codePointHigh) {
        return new JavaUnicodeEscaper(codePointLow, codePointHigh, false);
    }

    /**
     * Constructs a {@code JavaUnicodeEscaper} for the specified range. This is the underlying method for the
     * other constructors/builders. The {@code below} and {@code above} boundaries are inclusive when
     * {@code between} is {@code true} and exclusive when it is {@code false}.
     *
     * @param below
     *            int value representing the lowest code point boundary
     * @param above
     *            int value representing the highest code point boundary
     * @param between
     *            whether to escape between the boundaries or outside them
     */
    public JavaUnicodeEscaper(final int below, final int above, final boolean between) {
        super(below, above, between);
    }

    /**
     * Converts the given code point to a hexadecimal string of the form {@code "\\uXXXX\\uXXXX"}.
     *
     * @param codePoint
     *            a Unicode code point
     * @return The hexadecimal string for the given code point
     */
    @Override
    protected String toUtf16Escape(final int codePoint) {
        final char[] surrogatePair = Character.toChars(codePoint);
        return "\\u" + hex(surrogatePair[0]) + "\\u" + hex(surrogatePair[1]);
    }

}
