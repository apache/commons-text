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
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Translate XML numeric entities of the form &amp;#[xX]?\d+;? to
 * the specific codepoint.
 *
 * Note that the semi-colon is optional.
 *
 * @since 1.0
 */
public class NumericEntityUnescaper extends CharSequenceTranslator {

    /** Enumerates {@code NumericEntityUnescaper} options for unescaping. */
    public enum OPTION {

        /**
         * Require a semicolon.
         */
        semiColonRequired,

        /**
         * Do not require a semicolon.
         */
        semiColonOptional,

        /**
         * Throw an exception if a semi-colon is missing.
         */
        errorIfNoSemiColon
    }

    /** EnumSet of OPTIONS, given from the constructor. */
    private final EnumSet<OPTION> options;

    /** Code points which are invalid Windows-1252 points. */
    private static final Set<Integer> INVALID_CP1252_POINTS =
            Collections.unmodifiableSet(new HashSet<>(Arrays.asList(129, 141, 143, 144, 157)));

    /** Decoder for Windows-1252 characters */
    // Windows-1252 is supported. See https://docs.oracle.com/javase/8/docs/technotes/guides/intl/encoding.doc.html.
    private static final CharsetDecoder CP_1252_DECODER = Charset.forName("Windows-1252").newDecoder()
            .onMalformedInput(CodingErrorAction.REPORT)
            .onUnmappableCharacter(CodingErrorAction.REPORT);

    /**
     * Create a {@code NumericEntityUnescaper}. The constructor takes a list of options, only one type of which is
     * currently available (whether to allow, error or ignore the semi-colon on the end of a
     * numeric entity to being missing).
     *
     * <br />
     * <p>For example, to support numeric entities without a ';':</p>
     *    <ul><code>new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional)</code></ul>
     *
     * <p>and to throw an IllegalArgumentException when they're missing:</p>
     *    <ul><code>new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon)</code></ul>
     *
     * <p>Note that the default behavior is to ignore them.</p>
     *
     * @param options to apply to this unescaper
     */
    public NumericEntityUnescaper(final OPTION... options) {
        if (options.length > 0) {
            this.options = EnumSet.copyOf(Arrays.asList(options));
        } else {
            this.options = EnumSet.copyOf(Arrays.asList(OPTION.semiColonRequired));
        }
    }

    /**
     * Whether the passed in option is currently set.
     *
     * @param option to check state of
     * @return whether the option is set
     */
    public boolean isSet(final OPTION option) {
        return options != null && options.contains(option);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int translate(final CharSequence input, final int index, final Writer out) throws IOException {
        final int seqEnd = input.length();
        // Uses -2 to ensure there is something after the &#
        if (input.charAt(index) == '&' && index < seqEnd - 2 && input.charAt(index + 1) == '#') {
            int start = index + 2;
            boolean isHex = false;

            final char firstChar = input.charAt(start);
            if (firstChar == 'x' || firstChar == 'X') {
                start++;
                isHex = true;

                // Check there's more than just an x after the &#
                if (start == seqEnd) {
                    return 0;
                }
            }

            int end = start;
            // Note that this supports character codes without a ; on the end
            while (end < seqEnd && (input.charAt(end) >= '0' && input.charAt(end) <= '9'
                                    || input.charAt(end) >= 'a' && input.charAt(end) <= 'f'
                                    || input.charAt(end) >= 'A' && input.charAt(end) <= 'F')) {
                end++;
            }

            final boolean semiNext = end != seqEnd && input.charAt(end) == ';';

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
                if (isHex) {
                    entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 16);
                } else {
                    entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 10);
                }
            } catch (final NumberFormatException nfe) {
                return 0;
            }

            if (entityValue > 0xFFFF) {
                final char[] chrs = Character.toChars(entityValue);
                out.write(chrs[0]);
                out.write(chrs[1]);

            } else if (128 <= entityValue && entityValue <= 159  // must be within the cp-1252 extension range
                    && !isHex  // must be a NUMERIC entity, not hex entity (see StringEscapeUtilsTest for hex)
                    && !INVALID_CP1252_POINTS.contains(entityValue)  // must not be an invalid code point for cp-1252
            ) {
                System.err.println(entityValue);
                try {
                    final String newChar = CP_1252_DECODER
                            .decode(ByteBuffer.wrap(new byte[] {(byte) entityValue}))
                            .toString();
                    out.write(newChar);

                } catch (final IllegalArgumentException e) {
                    /*
                     * Rethrow exception with causal input, as throw from Charset.decode does not include it.
                     *
                     * That said, the input should always be a valid byte due to the restrictions that are imposed by
                     * the if statement; all characters should be mappable as well, as entity values that are not so are
                     * excluded by the if statement. If something happens to violate the restrictions meant to ensure
                     * that translation is valid and should always work, user ought to know.
                     */
                    throw new IllegalArgumentException(String.format("input %s is malformed input", e));
                }

            } else {
                out.write(entityValue);

            }

            return 2 + end - start + (isHex ? 1 : 0) + (semiNext ? 1 : 0);
        }
        return 0;
    }
}
