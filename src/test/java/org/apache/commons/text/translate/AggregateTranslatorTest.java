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

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link AggregateTranslator}.
 */
public class AggregateTranslatorTest {

    @Test
    public void testNullConstructor() throws Exception {
        final String testString = "foo";
        final AggregateTranslator subject = new AggregateTranslator((CharSequenceTranslator[]) null);
        assertThat(subject.translate(testString)).isEqualTo(testString);
    }

    @Test
    public void testNullVarargConstructor() throws Exception {
        final String testString = "foo";
        final AggregateTranslator subject = new AggregateTranslator((CharSequenceTranslator) null);
        assertThat(subject.translate(testString)).isEqualTo(testString);
    }

    @Test
    public void testNonNull() throws IOException {
        final Map<CharSequence, CharSequence> oneTwoMap = new HashMap<>();
        oneTwoMap.put("one", "two");
        final Map<CharSequence, CharSequence> threeFourMap = new HashMap<>();
        threeFourMap.put("three", "four");
        final CharSequenceTranslator translator1 = new LookupTranslator(oneTwoMap);
        final CharSequenceTranslator translator2 = new LookupTranslator(threeFourMap);
        final AggregateTranslator subject = new AggregateTranslator(translator1, translator2);
        final StringWriter out1 = new StringWriter();
        final int result1 = subject.translate(new StringBuffer("one"), 0, out1);
        assertThat(result1).as("Incorrect codepoint consumption").isEqualTo(3);
        assertThat(out1.toString()).as("Incorrect value").isEqualTo("two");
        final StringWriter out2 = new StringWriter();
        final int result2 = subject.translate(new StringBuffer("three"), 0, out2);
        assertThat(result2).as("Incorrect codepoint consumption").isEqualTo(5);
        assertThat(out2.toString()).as("Incorrect value").isEqualTo("four");
    }

}
