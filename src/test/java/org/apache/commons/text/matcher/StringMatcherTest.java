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

package org.apache.commons.text.matcher;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;
import org.junit.jupiter.api.Test;

/**
 * Tests the default methods of {@link StringMatcher}.
 */
public class StringMatcherTest {

    /**
     * Implements only the abstract overload, records the window it is handed and matches only inside it.
     */
    private static final class RecordingMatcher implements StringMatcher {

        private final char[] chars;
        private int lastBufferStart = -1;
        private int lastBufferEnd = -1;

        RecordingMatcher(final String str) {
            this.chars = str.toCharArray();
        }

        @Override
        public int isMatch(final char[] buffer, final int start, final int bufferStart, final int bufferEnd) {
            lastBufferStart = bufferStart;
            lastBufferEnd = bufferEnd;
            if (start < bufferStart || start + chars.length > bufferEnd) {
                return 0;
            }
            for (int i = 0; i < chars.length; i++) {
                if (buffer[start + i] != chars[i]) {
                    return 0;
                }
            }
            return chars.length;
        }

        @Override
        public int size() {
            return chars.length;
        }
    }

    @Test
    public void testIsMatchCharSequencePassesBufferStartThrough() {
        final RecordingMatcher viaChars = new RecordingMatcher("c");
        final RecordingMatcher viaCharSequence = new RecordingMatcher("c");
        viaChars.isMatch("abcdef".toCharArray(), 2, 1, 5);
        viaCharSequence.isMatch("abcdef", 2, 1, 5);
        assertEquals(1, viaChars.lastBufferStart);
        assertEquals(1, viaCharSequence.lastBufferStart);
        assertEquals(5, viaChars.lastBufferEnd);
        assertEquals(5, viaCharSequence.lastBufferEnd);
    }

    @Test
    public void testIsMatchCharSequenceStartDelegatesThroughTheCorrectedOverload() {
        assertEquals(2, new RecordingMatcher("ab").isMatch("xabz", 1));
        assertEquals(0, new RecordingMatcher("ab").isMatch("xabz", 2));
    }

    @Test
    public void testIsMatchOverloadsAgreeOnTheSameWindow() {
        // The window covers the whole pattern.
        assertEquals(2, new RecordingMatcher("ab").isMatch("xabz".toCharArray(), 1, 0, 4));
        assertEquals(2, new RecordingMatcher("ab").isMatch("xabz", 1, 0, 4));
        // A valid window that ends before the pattern is complete.
        assertEquals(0, new RecordingMatcher("ab").isMatch("xabz".toCharArray(), 1, 0, 2));
        assertEquals(0, new RecordingMatcher("ab").isMatch("xabz", 1, 0, 2));
    }

    /**
     * StringSubstitutor searches a TextStringBuilder, so a custom matcher reaches the CharSequence default.
     */
    @Test
    public void testStringSubstitutorHandsACustomMatcherTheRealBufferStart() {
        final Map<String, String> values = new HashMap<>();
        values.put("key", "value");
        final StringSubstitutor substitutor = new StringSubstitutor(values);
        substitutor.setVariablePrefixMatcher(new RecordingMatcher("${"));
        assertEquals("a value b", substitutor.replace("a ${key} b"));
    }
}
