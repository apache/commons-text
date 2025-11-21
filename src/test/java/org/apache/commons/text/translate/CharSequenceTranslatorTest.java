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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.Writer;

import org.junit.jupiter.api.Test;


class CharSequenceTranslatorTest {

    //Used to count translate invocations
    private int translateInvocationCounter;

    @Test
    void testWith() throws IOException {
        CharSequenceTranslator charSequenceTranslatorOne = new TestCharSequenceTranslator();
        CharSequenceTranslator charSequenceTranslatorTwo = new TestCharSequenceTranslator();
        CharSequenceTranslator charSequenceTranslatorThree = new TestCharSequenceTranslator();
        CharSequenceTranslator aggregatedTranslator = charSequenceTranslatorOne.with(charSequenceTranslatorTwo, charSequenceTranslatorThree);
        aggregatedTranslator.translate("", 0, null);
        assertTrue(aggregatedTranslator instanceof AggregateTranslator);
        assertEquals(3, translateInvocationCounter);
    }

    private final class TestCharSequenceTranslator extends CharSequenceTranslator {
        @Override
        public int translate(final CharSequence input, final int index, final Writer writer) {
            translateInvocationCounter++;
            return 0;
        }

    }

}
