/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */

package org.apache.commons.text.lookup;

import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.script.ScriptEngineManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link ScriptStringLookup}.
 */
class ScriptStringLookupTest {

    private static final String JS_NAME = "JavaScript";

    @Test
    void testBadEngineName() {
        assertThrows(IllegalArgumentException.class, () -> ScriptStringLookup.INSTANCE.apply("BAD_ENGINE_NAME:\"Hello World!\""));
    }

    @Test
    void testBadScript() {
        assertThrows(IllegalArgumentException.class, () -> ScriptStringLookup.INSTANCE.apply(JS_NAME + ":X"));
    }

    @Test
    void testNoScript() {
        assertThrows(IllegalArgumentException.class, () -> ScriptStringLookup.INSTANCE.apply("ENGINE_NAME:"));
    }

    @Test
    void testNull() {
        Assertions.assertNull(ScriptStringLookup.INSTANCE.apply(null));
    }

    @Test
    void testOne() {
        Assertions.assertEquals("Hello World!", ScriptStringLookup.INSTANCE.apply(JS_NAME + ":\"Hello World!\""));
    }

    @Test
    void testSanityCheck() {
        Assertions.assertNotNull(new ScriptEngineManager().getEngineByName(JS_NAME), JS_NAME);
    }

    @Test
    void testScriptMissingColon() {
        assertThrows(IllegalArgumentException.class, () -> ScriptStringLookup.INSTANCE.apply("JavaScript=\"test\""));
    }

    @Test
    void testScriptUsingMultipleColons() {
        Assertions.assertEquals("It Works",
            ScriptStringLookup.INSTANCE.apply(JS_NAME + ":true ? \"It Works\" : \"It Does Not Work\" "));
    }

    @Test
    void testToString() {
        // does not blow up and gives some kind of string.
        Assertions.assertFalse(ScriptStringLookup.INSTANCE.toString().isEmpty());
    }

}
