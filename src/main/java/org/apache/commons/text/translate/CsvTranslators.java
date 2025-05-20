/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
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

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * This class holds inner classes for escaping/unescaping Comma Separated Values.
 * <p>
 * In general the use a high level API like <a href="https://commons.apache.org/proper/commons-csv/">Apache Commons
 * CSV</a> should be preferred over these low level classes.
 * </p>
 *
 * @see <a href="https://commons.apache.org/proper/commons-csv/apidocs/index.html">Apache Commons CSV</a>
 */
public final class CsvTranslators {

    /**
     * Translator for escaping Comma Separated Values.
     */
    public static class CsvEscaper extends SinglePassTranslator {

        /**
         * Construct a new instance.
         */
        public CsvEscaper() {
            // empty
        }

        @Override
        void translateWhole(final CharSequence input, final Writer writer) throws IOException {
            final String inputSting = input.toString();
            if (StringUtils.containsNone(inputSting, CSV_SEARCH_CHARS)) {
                writer.write(inputSting);
            } else {
                // input needs quoting
                writer.write(CSV_QUOTE);
                writer.write(StringUtils.replace(inputSting, CSV_QUOTE_STR, CSV_ESCAPED_QUOTE_STR));
                writer.write(CSV_QUOTE);
            }
        }
    }
    /**
     * Translator for unescaping escaped Comma Separated Value entries.
     */
    public static class CsvUnescaper extends SinglePassTranslator {

        /**
         * Construct a new instance.
         */
        public CsvUnescaper() {
            // empty
        }

        @Override
        void translateWhole(final CharSequence input, final Writer writer) throws IOException {
            // is input not quoted?
            if (input.charAt(0) != CSV_QUOTE || input.charAt(input.length() - 1) != CSV_QUOTE) {
                writer.write(input.toString());
                return;
            }

            // strip quotes
            final String quoteless = input.subSequence(1, input.length() - 1).toString();

            if (StringUtils.containsAny(quoteless, CSV_SEARCH_CHARS)) {
                // deal with escaped quotes; ie) ""
                writer.write(StringUtils.replace(quoteless, CSV_ESCAPED_QUOTE_STR, CSV_QUOTE_STR));
            } else {
                writer.write(quoteless);
            }
        }
    }
    /** Comma character. */
    private static final char CSV_DELIMITER = ',';
    /** Quote character. */
    private static final char CSV_QUOTE = '"';
    /** Quote character converted to string. */
    private static final String CSV_QUOTE_STR = String.valueOf(CSV_QUOTE);

    /** Escaped quote string. */
    private static final String CSV_ESCAPED_QUOTE_STR = CSV_QUOTE_STR + CSV_QUOTE_STR;

    /** CSV key characters in an array. */
    private static final char[] CSV_SEARCH_CHARS = { CSV_DELIMITER, CSV_QUOTE, CharUtils.CR, CharUtils.LF };

    /** Hidden constructor. */
    private CsvTranslators() {
        // empty
    }
}
