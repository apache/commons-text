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
package org.apache.commons.text.cases;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.CharUtils;

/**
 * DelimitedCase is a case in which the true alphabetic case of the characters is ignored by default
 * and tokens themselves are determined by the presence of a delimiter between each token.
 */
public class DelimitedCase implements Case {

    /** delimiters to be used when parsing. */
    private Set<Integer> parseDelimiters;

    /** delimited to be used when formatting. */
    private String formatDelimiter;

    /**
     * Constructs a new Delimited Case.
     * @param delimiter the character to use as both the parse and format delimiter
     */
    public DelimitedCase(char delimiter) {
        this(new char[] { delimiter }, CharUtils.toString(delimiter));
    }

    /**
     * Constructs a new delimited case.
     * @param parseDelimiters The array of delimiters to use when parsing
     * @param formatDelimiter The delimiter to use when formatting
     */
    public DelimitedCase(char[] parseDelimiters, String formatDelimiter) {
        super();
        if (parseDelimiters == null || parseDelimiters.length == 0) {
            throw new IllegalArgumentException("Parse Delimiters cannot be null or empty");
        }
        if (formatDelimiter == null || formatDelimiter.length() == 0) {
            throw new IllegalArgumentException("Format Delimiters cannot be null or empty");
        }
        this.parseDelimiters = generateDelimiterSet(parseDelimiters);
        this.formatDelimiter = formatDelimiter;
    }

    /**
     * Formats tokens into Delimited Case.
     * <p>
     * Tokens are iterated on and appended to an output stream, with an instance of a
     * delimiter character between them. This method validates that the delimiter character is not
     * part of the token. If it is found within the token an exception is thrown.<br>
     * No other restrictions are placed on the contents of the tokens.
     * Note: This Case does support empty tokens.<br>
     * </p>
     * @param tokens the tokens to be formatted into a delimited string
     * @return The delimited string
     */
    @Override
    public String format(Iterable<String> tokens) {
        StringBuilder formattedString = new StringBuilder();
        int i = 0;
        for (String token : tokens) {
            int delimiterFoundIndex = token.indexOf(formatDelimiter);
            if (delimiterFoundIndex > -1) {
                throw new IllegalArgumentException("Token " + i + " contains delimiter character '" + formatDelimiter + "' at index " + delimiterFoundIndex);
            }
            if (i > 0) {
                formattedString.append(formatDelimiter);
            }
            i++;
            formattedString.append(token);
        }
        return formattedString.toString();
    }

    /**
     * Parses delimited string into tokens.
     * <p>
     * Input string is parsed one character at a time until a delimiter character is reached.
     * When a delimiter character is reached a new token begins. The delimiter character is
     * considered reserved, and is omitted from the returned parsed tokens.<br>
     * No other restrictions are placed on the contents of the input string. <br>
     * </p>
     * @param string The delimited string to be parsed
     * @return The list of tokens found in the string
     */
    @Override
    public List<String> parse(String string) {
        List<String> tokens = new LinkedList<>();
        if (string.length() == 0) {
            return tokens;
        }
        int strLen = string.length();
        int[] tokenCodePoints = new int[strLen];
        int tokenCodePointsOffset = 0;
        for (int i = 0; i < string.length();) {
            final int codePoint = string.codePointAt(i);
            if (parseDelimiters.contains(codePoint)) {
                tokens.add(new String(tokenCodePoints, 0, tokenCodePointsOffset));
                tokenCodePoints = new int[strLen];
                tokenCodePointsOffset = 0;
                i++;
            } else {
                tokenCodePoints[tokenCodePointsOffset++] = codePoint;
                i += Character.charCount(codePoint);
            }
        }
        tokens.add(new String(tokenCodePoints, 0, tokenCodePointsOffset));
        return tokens;
    }

    /**
     * Converts an array of delimiters to a hash set of code points. The generated hash set provides O(1) lookup time.
     *
     * @param delimiters set of characters to determine capitalization, null means whitespace
     * @return Set<Integer>
     */
    private static Set<Integer> generateDelimiterSet(final char[] delimiters) {
        final Set<Integer> delimiterHashSet = new HashSet<>();
        for (int index = 0; index < delimiters.length; index++) {
            delimiterHashSet.add(Character.codePointAt(delimiters, index));
        }
        return delimiterHashSet;
    }

}
