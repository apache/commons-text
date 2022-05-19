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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.io.IOException;
import java.io.StringWriter;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link LookupTranslator}.
 */
public class LookupTranslatorTest  {

    @Test
    public void testBasicLookup() throws IOException {
        final Map<CharSequence, CharSequence> translatorMap = new HashMap<>();
        translatorMap.put("one", "two");
        final LookupTranslator lt = new LookupTranslator(translatorMap);
        final StringWriter out = new StringWriter();
        final int result = lt.translate("one", 0, out);
        assertThat(result).as("Incorrect codepoint consumption").isEqualTo(3);
        assertThat(out.toString()).as("Incorrect value").isEqualTo("two");
    }

    // Tests: https://issues.apache.org/jira/browse/LANG-882
    @Test
    public void testLang882() throws IOException {
        final Map<CharSequence, CharSequence> translatorMap = new HashMap<>();
        translatorMap.put(new StringBuffer("one"), new StringBuffer("two"));
        final LookupTranslator lt = new LookupTranslator(translatorMap);
        final StringWriter out = new StringWriter();
        final int result = lt.translate(new StringBuffer("one"), 0, out);
        assertThat(result).as("Incorrect codepoint consumption").isEqualTo(3);
        assertThat(out.toString()).as("Incorrect value").isEqualTo("two");
    }

    @Test
    public void testFailsToCreateLookupTranslatorThrowsInvalidParameterException() {
        assertThatExceptionOfType(InvalidParameterException.class).isThrownBy(() -> new LookupTranslator(null));
    }

    @Test
    public void testTranslateSupplementaryCharacter() {
        /* Key: string with Mathematical double-struck capital A (U+1D538) */
        String symbol = new StringBuilder().appendCodePoint(0x1D538).toString();
        /* Map U+1D538 to "A" */
        Map<CharSequence, CharSequence> map = new HashMap<>();
        map.put(symbol, "A");
        LookupTranslator translator = new LookupTranslator(map);
        String translated = translator.translate(symbol + "=A");
        /* we should get "A=A". */
        assertThat(translated).as("Incorrect value").isEqualTo("A=A");
    }

}
