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
import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for {@link org.apache.commons.text.translate.AggregateTranslator}.
 */
public class AggregateTranslatorTest {

    @Test
    public void testNullConstructor() throws NoSuchFieldException, IllegalAccessException {
        final AggregateTranslator subject = new AggregateTranslator(null);
        final Field field = AggregateTranslator.class.getDeclaredField("translators");
        field.setAccessible(Boolean.TRUE);
        assertNull(field.get(subject));
    }

    @Test
    public void testNonNull() throws IOException{
        final CharSequenceTranslator translator1 = new LookupTranslator(new CharSequence[][] { { "one", "two" } });
        final CharSequenceTranslator translator2 = new LookupTranslator(new CharSequence[][] { { "three", "four" } });
        final AggregateTranslator subject = new AggregateTranslator(translator1, translator2);
        final StringWriter out1 = new StringWriter();
        final int result1 = subject.translate(new StringBuffer("one"), 0, out1);
        assertEquals("Incorrect codepoint consumption", 3, result1);
        assertEquals("Incorrect value", "two", out1.toString());
        final StringWriter out2 = new StringWriter();
        final int result2 = subject.translate(new StringBuffer("three"), 0, out2);
        assertEquals("Incorrect codepoint consumption", 5, result2);
        assertEquals("Incorrect value", "four", out2.toString());
    }

}
