
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

package org.apache.commons.text;

import org.apache.commons.lang3.StringUtils;

import java.util.Formatter;

class TruncatePadFormatter {

    private final CharSequence seq;
    private final Formatter formatter;
    private final int flags;
    private final int width;
    private final int precision;
    private final char padChar;
    private final CharSequence truncateEllipsis;

    TruncatePadFormatter(CharSequence seq, Formatter formatter, int flags, int width, int precision, char padChar, CharSequence truncateEllipsis) {
        this.seq = seq;
        this.formatter = formatter;
        this.flags = flags;
        this.width = width;
        this.precision = precision;
        this.padChar = padChar;
        this.truncateEllipsis = truncateEllipsis;
    }

    Formatter format() {
        if (!(truncateEllipsis == null || precision < 0 || truncateEllipsis.length() <= precision)) {
            throw new IllegalArgumentException(
                    String.format("Specified ellipsis '%s' exceeds precision of %s", truncateEllipsis, precision));
        }

        final StringBuilder buf = new StringBuilder(seq);
        if (precision >= 0 && precision < seq.length()) {
            final CharSequence ellipsis = (truncateEllipsis == null) ? StringUtils.EMPTY : truncateEllipsis;
            buf.replace(precision - ellipsis.length(), seq.length(), ellipsis.toString());
        }

        PadStrategy padStrategy = isLeftJustify() ? new LeftPadder() : new RightPadder();
        padStrategy.applyPadding(buf, width, padChar);

        formatter.format(buf.toString());
        return formatter;
    }

    private boolean isLeftJustify() {
        return (flags & java.util.FormattableFlags.LEFT_JUSTIFY) == java.util.FormattableFlags.LEFT_JUSTIFY;
    }

    /** Strategy Interface
     *
     */
    private interface PadStrategy {
        void applyPadding(StringBuilder buf, int width, char padChar);
    }

    private static class LeftPadder implements PadStrategy {
        public void applyPadding(StringBuilder buf, int width, char padChar) {
            for (int i = buf.length(); i < width; i++) {
                buf.insert(i, padChar);
            }
        }
    }

    private static class RightPadder implements PadStrategy {
        public void applyPadding(StringBuilder buf, int width, char padChar) {
            for (int i = buf.length(); i < width; i++) {
                buf.insert(0, padChar);
            }
        }
    }
}