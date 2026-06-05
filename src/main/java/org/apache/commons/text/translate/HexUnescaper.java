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
 * Translates escaped ASCII values of the form \\x\[0-9A-Fa-f][0-9A-Fa-f] back to ASCII.
 */
public class HexUnescaper extends CharSequenceTranslator {

    /**
     * {@inheritDoc}
     */
    @Override
    public int translate(final CharSequence input, final int index, final Writer writer) throws IOException {
        final int prefixLength = 2; // "\\x".length()
        final int escapeLength = 4; // "\\xHH".length()
        if (input.charAt(index) == '\\' && index + 1 < input.length() && input.charAt(index + 1) == 'x') {
            if (index + escapeLength <= input.length()) {
                // Get 2 hex digits
                final CharSequence hex = input.subSequence(index + prefixLength, index + escapeLength);

                try {
                    final int value = Integer.parseInt(hex.toString(), 16);
                    writer.write((char) value);
                } catch (final NumberFormatException nfe) {
                    throw new IllegalArgumentException("Unable to parse ASCII value: " + hex, nfe);
                }
                return escapeLength;
            }
            throw new IllegalArgumentException("Less than 2 hex digits in ASCII value: '"
                    + input.subSequence(index, input.length())
                    + "' due to end of CharSequence");
        }
        return 0;
    }
}
