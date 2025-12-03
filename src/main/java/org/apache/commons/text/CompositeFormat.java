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
package org.apache.commons.text;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;

/**
 * Formats using one formatter and parses using a different formatter. An
 * example of use for this would be a webapp where data is taken in one way and
 * stored in a database another way.
 *
 * @since 1.0
 */
public class CompositeFormat extends Format {

    /**
     * Required for serialization support.
     *
     * @see java.io.Serializable
     */
    private static final long serialVersionUID = -4329119827877627683L;

    /** The parser to use. */
    private final Format parser;

    /** The formatter to use. */
    private final Format formatter;

    /**
     * Constructs a format that points its parseObject method to one implementation
     * and its format method to another.
     *
     * @param parser implementation.
     * @param formatter implementation.
     */
    public CompositeFormat(final Format parser, final Format formatter) {
        this.parser = parser;
        this.formatter = formatter;
    }

    /**
     * Formats the input.
     *
     * @param obj        the object to format.
     * @param toAppendTo the {@link StringBuffer} to append to.
     * @param pos        the FieldPosition to use (or ignore).
     * @return {@code toAppendTo}.
     * @see Format#format(Object, StringBuffer, FieldPosition)
     */
    @Override // Therefore has to use StringBuffer
    public StringBuffer format(final Object obj, final StringBuffer toAppendTo,
            final FieldPosition pos) {
        return formatter.format(obj, toAppendTo, pos);
    }

    /**
     * Gets the parser Format implementation.
     *
     * @return formatter Format implementation
     */
    public Format getFormatter() {
        return this.formatter;
    }

    /**
     * Gets the parser Format implementation.
     *
     * @return parser Format implementation
     */
    public Format getParser() {
        return this.parser;
    }

    /**
     * Parses the input.
     *
     * @param source the String source.
     * @param pos    the ParsePosition containing the position to parse from, will be updated according to parsing success (index) or failure (error index).
     * @return The parsed Object.
     * @see Format#parseObject(String, ParsePosition)
     */
    @Override
    public Object parseObject(final String source, final ParsePosition pos) {
        return parser.parseObject(source, pos);
    }

    /**
     * Parses and then reformats a String.
     *
     * @param input String to reformat.
     * @return A reformatted String.
     * @throws ParseException thrown by parseObject(String) call.
     */
    public String reformat(final String input) throws ParseException {
        return format(parseObject(input));
    }

}
