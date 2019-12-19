/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */

package org.apache.commons.text.lookup;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link ScriptStringLookup}.
 */
public class ScriptStringLookupTest {

    @Test
    public void testBadEngineName() {
        assertThrows(IllegalArgumentException.class, () -> {
            ScriptStringLookup.INSTANCE.lookup("BAD_ENGINE_NAME:\"Hello World!\"");
        });
    }

    @Test
    public void testBadScript() {
        assertThrows(IllegalArgumentException.class, () -> {
            ScriptStringLookup.INSTANCE.lookup("javascript:X");
        });
    }

    @Test
    public void testNoScript() {
        assertThrows(IllegalArgumentException.class, () -> {
            ScriptStringLookup.INSTANCE.lookup("ENGINE_NAME:");
        });
    }

    @Test
    public void testNull() {
        Assertions.assertNull(ScriptStringLookup.INSTANCE.lookup(null));
    }

    @Test
    public void testOne() {
        Assertions.assertEquals("Hello World!", ScriptStringLookup.INSTANCE.lookup("javascript:\"Hello World!\""));
    }

    @Test
    public void testScriptWithColumn() {
        Assertions.assertEquals("It Works",
         ScriptStringLookup.INSTANCE.lookup("javascript:true ? \"It Works\" : \"It Does Not Work\" "));
    }

}
