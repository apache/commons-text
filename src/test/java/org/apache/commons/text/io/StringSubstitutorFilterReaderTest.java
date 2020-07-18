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

package org.apache.commons.text.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
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
public class StringSubstitutorFilterReaderTest extends StringSubstitutorTest {

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
        doTestReplaceInSteps(substitutor, replaceTemplate, replaceTemplate, false);
    }

    @Override
    protected void doTestReplace(final StringSubstitutor sub, final String expectedResult, final String replaceTemplate,
        final boolean substring) throws IOException {
        doTestReplaceInSteps(sub, expectedResult, replaceTemplate, substring);
        super.doTestReplace(sub, expectedResult, replaceTemplate, substring);
    }

    private void doTestReplaceInSteps(final StringSubstitutor substitutor, final String expectedResult,
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
        if (replaceTemplate == null) {
            assertEquals(StringUtils.EMPTY, actualResultWriter.toString());
        } else {
            assertEquals(expectedResult, actualResultWriter.toString());
        }
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
    public void testReadMixedBufferLengths1ToVarLenPlusNoReplace() throws IOException {
        final StringSubstitutor substitutor = new StringSubstitutor(values);
        final String template = "123456";
        assertTrue(template.length() > substitutor.getMinExpressionLength() + 1);
        try (Reader reader = createReader(substitutor, template)) {
            assertEquals('1', reader.read());
            final char[] cbuf = new char[template.length() - 1];
            reader.read(cbuf);
            final String result = String.valueOf(cbuf);
            assertEquals(template.substring(1), result);
        }
    }

    @Test
    public void testReadMixedBufferLengthsReplace() throws IOException {
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
    public void testReadMixedBufferLengthsVarLenPlusToNoReplace() throws IOException {
        final StringSubstitutor substitutor = new StringSubstitutor(values);
        final String template = "123456";
        assertTrue(template.length() > substitutor.getMinExpressionLength() + 1);
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
