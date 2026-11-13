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
package org.apache.commons.text.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests skipping characters in the substituted stream.
 */
class StringSubstitutorReaderSkipTest {

    private Reader createReader(final String template, final String value) {
        return new StringSubstitutorReader(new StringReader(template),
                new StringSubstitutor(Collections.singletonMap("v", value)));
    }

    private Reader createReader(final Reader input) {
        return new StringSubstitutorReader(input,
                new StringSubstitutor(Collections.singletonMap("v", "abc")));
    }

    private Reader createNoSkipInput() {
        return new Reader() {
            private final StringReader input = new StringReader("${v}Z");

            @Override
            public int read(final char[] target, final int offset, final int length) throws IOException {
                return input.read(target, offset, length);
            }

            @Override
            public long skip(final long count) throws IOException {
                throw new IOException("skip unsupported");
            }

            @Override
            public void close() throws IOException {
                input.close();
            }
        };
    }

    private Reader createInheritedSkipInput() {
        return new Reader() {
            private final StringReader input = new StringReader("${v}Z");

            @Override
            public int read(final char[] target, final int offset, final int length) throws IOException {
                return input.read(target, offset, length);
            }

            @Override
            public void close() throws IOException {
                input.close();
            }
        };
    }

    @ParameterizedTest
    @CsvSource({
        "${v}Z, abc, 1, 1, bcZ",
        "A${v}Z, bc, 2, 2, cZ",
        "${v}Z, abcdef, 5, 5, fZ",
        "${v}Z, x, 5, 2, ''",
        "${v}Z, '', 1, 1, ''",
        "$${v}Z, abc, 2, 2, v}Z",
        "plain, abc, 2, 2, ain"
    })
    void testSkip(final String template, final String value, final long count, final long expectedCount,
            final String remaining) throws IOException {
        try (Reader reader = createReader(template, value)) {
            assertEquals(expectedCount, reader.skip(count));
            assertEquals(remaining, IOUtils.toString(reader));
            assertEquals(0, reader.skip(1));
        }
    }

    @Test
    void testSkipAfterRead() throws IOException {
        try (Reader reader = createReader("${v}Z", "abcd")) {
            assertEquals('a', reader.read());
            assertEquals(2, reader.skip(2));
            assertEquals("dZ", IOUtils.toString(reader));
        }
    }

    @Test
    void testSkipDoesNotCallWrappedSkip() throws IOException {
        try (Reader reader = createReader(createNoSkipInput())) {
            assertEquals('a', reader.read());
            assertEquals(1, reader.skip(1));
            assertEquals("cZ", IOUtils.toString(reader));
        }
    }

    @Test
    void testSkipZeroDoesNotCallWrappedSkip() throws IOException {
        try (Reader reader = createReader(createNoSkipInput())) {
            assertEquals(0, reader.skip(0));
            assertEquals("abcZ", IOUtils.toString(reader));
        }
    }

    @ParameterizedTest
    @ValueSource(longs = {0, 1})
    void testSkipClosedWithInheritedSkip(final long count) throws IOException {
        final Reader reader = createReader(createInheritedSkipInput());
        assertEquals("abcZ", IOUtils.toString(reader));
        reader.close();
        assertThrows(IOException.class, () -> reader.skip(count));
    }

    @ParameterizedTest
    @ValueSource(longs = {0, 1})
    void testSkipClosed(final long count) throws IOException {
        final Reader reader = createReader("${v}Z", "abc");
        try {
            assertEquals("abcZ", IOUtils.toString(reader));
        } finally {
            reader.close();
        }
        assertThrows(IOException.class, () -> reader.skip(count));
    }

    @Test
    void testSkipLargeReplacement() throws IOException {
        try (Reader reader = createReader("${v}Z", StringUtils.repeat('a', 4096))) {
            assertEquals(4096, reader.skip(4096));
            assertEquals("Z", IOUtils.toString(reader));
        }
    }

    @Test
    void testSkipLongMaxValue() throws IOException {
        try (Reader reader = createReader("${v}Z", "x")) {
            assertEquals(2, reader.skip(Long.MAX_VALUE));
            assertEquals(-1, reader.read());
        }
    }

    @Test
    void testSkipNegative() throws IOException {
        try (Reader reader = createReader("${v}Z", "abc")) {
            assertThrows(IllegalArgumentException.class, () -> reader.skip(-1));
            assertEquals("abcZ", IOUtils.toString(reader));
        }
    }

    @Test
    void testSkipZero() throws IOException {
        try (Reader reader = createReader("${v}Z", "abc")) {
            assertEquals(0, reader.skip(0));
            assertEquals("abcZ", IOUtils.toString(reader));
        }
    }
}
