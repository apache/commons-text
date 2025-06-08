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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.NullReader;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.StringSubstitutorTest;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link StringSubstitutorReader}.
 */
class StringSubstitutorFilterReaderTest extends StringSubstitutorTest {

    private StringSubstitutorReader createReader(final StringSubstitutor substitutor, final String template) {
        return new StringSubstitutorReader(new StringReader(template), substitutor);
    }

    @Override
    protected void doTestNoReplace(final StringSubstitutor substitutor, final String replaceTemplate)
        throws IOException {
        super.doTestNoReplace(substitutor, replaceTemplate);
        doTestNoReplaceInSteps(replaceTemplate, substitutor);
    }

    private void doTestNoReplaceInSteps(final String replaceTemplate, final StringSubstitutor substitutor)
        throws IOException {
        doTestReplaceInCharSteps(substitutor, replaceTemplate, replaceTemplate, false);
        final int minTargetSize = 1;
        int maxTargetSize = 1024 * 8;
        for (int targetSize = minTargetSize; targetSize <= maxTargetSize; targetSize++) {
            doTestReplaceInCharArraySteps(substitutor, replaceTemplate, replaceTemplate, false, targetSize);
        }
        maxTargetSize = 400;
        for (int targetSize = minTargetSize; targetSize <= maxTargetSize; targetSize++) {
            for (int targetIndex = 0; targetIndex < targetSize; targetIndex++) {
                doTestReplaceInCharArrayAtSteps(substitutor, replaceTemplate, replaceTemplate, false, targetIndex,
                    targetSize);
            }
        }
    }

    @Override
    protected void doTestReplace(final StringSubstitutor sub, final String expectedResult, final String replaceTemplate,
        final boolean substring) throws IOException {
        doTestReplaceInCharSteps(sub, expectedResult, replaceTemplate, substring);
        super.doTestReplace(sub, expectedResult, replaceTemplate, substring);
    }

    private void doTestReplaceInCharArrayAtSteps(final StringSubstitutor substitutor, final String expectedResult,
        final String replaceTemplate, final boolean substring, final int targetIndex, final int targetSize)
        throws IOException {
        final StringWriter actualResultWriter = new StringWriter();
        final StringWriter expectedResultWriter = new StringWriter();
        final AtomicInteger index = new AtomicInteger();
        final int expectedResultLen = StringUtils.length(expectedResult);
        try (Reader expectedResultReader = toReader(expectedResult);
            Reader actualReader = new StringSubstitutorReader(toReader(replaceTemplate), substitutor)) {
            final char[] actualCh = new char[targetSize];
            final char[] expectedCh = new char[targetSize];
            int actualCount;
            while ((actualCount = actualReader.read(actualCh, targetIndex, targetSize - targetIndex)) != -1) {
                final int expectedCount = expectedResultReader.read(expectedCh, targetIndex, targetSize - targetIndex);
                if (expectedCount != -1) {
                    expectedResultWriter.write(expectedCh, targetIndex, expectedCount);
                }
                // stream can chunk in smaller sizes
                if (expectedCount == actualCount) {
                    assertEquals(expectedCount, actualCount, () -> String.format("Step size %,d", targetSize));
                    assertArrayEquals(expectedCh, actualCh,
                        () -> String.format("[%,d] '%s' != '%s', result so far: \"%s\"", index.get(),
                            String.valueOf(expectedCh), String.valueOf(actualCh), actualResultWriter.toString()));
                } else if (actualCount < expectedCount) {
                    assertTrue(expectedResultWriter.toString().startsWith(actualResultWriter.toString()));
                }
                if (actualCount != -1) {
                    actualResultWriter.write(actualCh, targetIndex, actualCount);
                } else {
                    // fails
                    assertEquals(expectedCount, actualCount, () -> String.format("Step size %,d", targetSize));
                }
                index.incrementAndGet();
                assertFalse(index.get() > expectedResultLen, () -> "Index: " + index.get());
                // simpler to debug if we zero out the buffers.
                Arrays.fill(actualCh, (char) 0);
                Arrays.fill(expectedCh, (char) 0);
            }
        }
        assertEquals(Objects.toString(expectedResult, StringUtils.EMPTY), actualResultWriter.toString());
    }

    private void doTestReplaceInCharArraySteps(final StringSubstitutor substitutor, final String expectedResult,
        final String replaceTemplate, final boolean substring, final int targetSize) throws IOException {
        final StringWriter actualResultWriter = new StringWriter();
        final StringWriter expectedResultWriter = new StringWriter();
        final AtomicInteger index = new AtomicInteger();
        final int expectedResultLen = StringUtils.length(expectedResult);
        try (Reader expectedResultReader = toReader(expectedResult);
            Reader actualReader = new StringSubstitutorReader(toReader(replaceTemplate), substitutor)) {
            final char[] actualCh = new char[targetSize];
            final char[] expectedCh = new char[targetSize];
            int actualCount;
            while ((actualCount = actualReader.read(actualCh)) != -1) {
                final int expectedCount = expectedResultReader.read(expectedCh);
                if (expectedCount != -1) {
                    expectedResultWriter.write(expectedCh, 0, expectedCount);
                }
                // stream can chunk in smaller sizes
                if (expectedCount == actualCount) {
                    assertEquals(expectedCount, actualCount, () -> String.format("Step size %,d", targetSize));
                    assertArrayEquals(expectedCh, actualCh,
                        () -> String.format("[%,d] '%s' != '%s', result so far: \"%s\"", index.get(),
                            String.valueOf(expectedCh), String.valueOf(actualCh), actualResultWriter.toString()));
                } else if (actualCount < expectedCount) {
                    assertTrue(expectedResultWriter.toString().startsWith(actualResultWriter.toString()));
                }
                if (actualCount != -1) {
                    actualResultWriter.write(actualCh, 0, actualCount);
                } else {
                    // fails
                    assertEquals(expectedCount, actualCount, () -> String.format("Step size %,d", targetSize));
                }
                index.incrementAndGet();
                assertFalse(index.get() > expectedResultLen, () -> "Index: " + index.get());
                // simpler to debug if we zero out the buffers.
                Arrays.fill(actualCh, (char) 0);
                Arrays.fill(expectedCh, (char) 0);
            }
        }
        assertEquals(Objects.toString(expectedResult, StringUtils.EMPTY), actualResultWriter.toString());
    }

    private void doTestReplaceInCharSteps(final StringSubstitutor substitutor, final String expectedResult,
        final String replaceTemplate, final boolean substring) throws IOException {
        final StringWriter actualResultWriter = new StringWriter();
        final AtomicInteger index = new AtomicInteger();
        final int expectedResultLen = StringUtils.length(expectedResult);
        try (Reader expectedResultReader = toReader(expectedResult);
            Reader actualReader = new StringSubstitutorReader(toReader(replaceTemplate), substitutor)) {
            int actualCh;
            while ((actualCh = actualReader.read()) != -1) {
                final int expectedCh = expectedResultReader.read();
                final int actualCh2 = actualCh;
                assertEquals(expectedCh, actualCh, () -> String.format("[%,d] '%s' != '%s', result so far: \"%s\"",
                    index.get(), toStringChar(expectedCh), toStringChar(actualCh2), actualResultWriter.toString()));
                if (actualCh != -1) {
                    actualResultWriter.write(actualCh);
                }
                index.incrementAndGet();
                assertFalse(index.get() > expectedResultLen, () -> "Index: " + index.get());
            }
        }
        assertEquals(Objects.toString(expectedResult, StringUtils.EMPTY), actualResultWriter.toString());
    }

    private int getMinExpressionLength(final StringSubstitutor substitutor) {
        return substitutor.getVariablePrefixMatcher().size() + 1 + substitutor.getVariableSuffixMatcher().size();
    }

    @Override
    protected String replace(final StringSubstitutor substitutor, final String source) throws IOException {
        if (source == null) {
            return null;
        }
        try (Reader reader = createReader(substitutor, source)) {
            return IOUtils.toString(reader);
        }
    }

    @Test
    void testReadMixedBufferLengths1ToVarLenPlusNoReplace() throws IOException {
        final StringSubstitutor substitutor = new StringSubstitutor(values);
        final String template = "123456";
        assertTrue(template.length() > getMinExpressionLength(substitutor) + 1);
        try (Reader reader = createReader(substitutor, template)) {
            assertEquals('1', reader.read());
            final char[] cbuf = new char[template.length() - 1];
            reader.read(cbuf);
            final String result = String.valueOf(cbuf);
            assertEquals(template.substring(1), result);
        }
    }

    @Test
    void testReadMixedBufferLengthsReplace() throws IOException {
        final String template = "${aa}${bb}";
        final StringSubstitutor substitutor = new StringSubstitutor(values);
        try (Reader reader = createReader(substitutor, template)) {
            assertEquals('1', reader.read());
            final char[] cbuf = new char[3];
            assertEquals(0, reader.read(cbuf, 0, 0));
            reader.read(cbuf);
            final String result = String.valueOf(cbuf);
            assertEquals("122", result, () -> String.format("length %,d", result.length()));
        }
    }

    @Test
    void testReadMixedBufferLengthsVarLenPlusToNoReplace() throws IOException {
        final StringSubstitutor substitutor = new StringSubstitutor(values);
        final String template = "123456";
        assertTrue(template.length() > getMinExpressionLength(substitutor) + 1);
        try (Reader reader = createReader(substitutor, template)) {
            final int endIndex = template.length() - 1;
            final char[] cbuf = new char[endIndex];
            reader.read(cbuf);
            final String result = String.valueOf(cbuf);
            assertEquals(template.substring(0, endIndex), result);
            assertEquals('6', reader.read());
        }
    }

    private Reader toReader(final String expectedResult) {
        return expectedResult != null ? new StringReader(expectedResult) : new NullReader();
    }

    private String toStringChar(final int ch) {
        switch (ch) {
        case -1:
            return "EOS";
        case 0:
            return "NUL";
        default:
            return String.valueOf((char) ch);
        }
    }
}
