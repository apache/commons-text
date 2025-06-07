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
package org.apache.commons.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link CaseUtils} class.
 */
public class CaseUtilsTest {

    @Test
    void testConstructor() {
        assertNotNull(new CaseUtils());
        final Constructor<?>[] cons = CaseUtils.class.getDeclaredConstructors();
        assertEquals(1, cons.length);
        assertTrue(Modifier.isPublic(cons[0].getModifiers()));
        assertTrue(Modifier.isPublic(CaseUtils.class.getModifiers()));
        assertFalse(Modifier.isFinal(CaseUtils.class.getModifiers()));
    }

    @Test
    void testToCamelCase() {
        assertNull(CaseUtils.toCamelCase(null, false, null));
        assertEquals("", CaseUtils.toCamelCase("", true, null));
        assertEquals("", CaseUtils.toCamelCase("  ", false, null));
        assertEquals("aBC@def", CaseUtils.toCamelCase("a  b  c  @def", false, null));
        assertEquals("ABC@def", CaseUtils.toCamelCase("a b c @def", true));
        assertEquals("ABC@def", CaseUtils.toCamelCase("a b c @def", true, '-'));
        assertEquals("ABC@def", CaseUtils.toCamelCase("a b c @def", true, '-'));

        final char[] chars = { '-', '+', ' ', '@' };
        assertEquals("", CaseUtils.toCamelCase("-+@ ", true, chars));
        assertEquals("toCamelCase", CaseUtils.toCamelCase("   to-CAMEL-cASE", false, chars));
        assertEquals("ToCamelCase", CaseUtils.toCamelCase("@@@@   to+CAMEL@cASE ", true, chars));
        assertEquals("ToCaMeLCase", CaseUtils.toCamelCase("To+CA+ME L@cASE", true, chars));

        assertEquals("toCamelCase", CaseUtils.toCamelCase("To.Camel.Case", false, '.'));
        assertEquals("toCamelCase", CaseUtils.toCamelCase("To.Camel-Case", false, '-', '.'));
        assertEquals("toCamelCase", CaseUtils.toCamelCase(" to @ Camel case", false, '-', '@'));
        assertEquals("ToCamelCase", CaseUtils.toCamelCase(" @to @ Camel case", true, '-', '@'));

        assertEquals("ToCamelCase", CaseUtils.toCamelCase("TO CAMEL CASE", true, null));
        assertEquals("toCamelCase", CaseUtils.toCamelCase("TO CAMEL CASE", false, null));
        assertEquals("toCamelCase", CaseUtils.toCamelCase("TO CAMEL CASE", false, null));
        assertEquals("tocamelcase", CaseUtils.toCamelCase("tocamelcase", false, null));
        assertEquals("Tocamelcase", CaseUtils.toCamelCase("tocamelcase", true, null));
        assertEquals("tocamelcase", CaseUtils.toCamelCase("Tocamelcase", false, null));

        assertEquals("Tocamelcase", CaseUtils.toCamelCase("tocamelcase", true));
        assertEquals("tocamelcase", CaseUtils.toCamelCase("tocamelcase", false));

        assertEquals("\uD800\uDF00\uD800\uDF02", CaseUtils.toCamelCase("\uD800\uDF00 \uD800\uDF02", true));
        assertEquals("\uD800\uDF00\uD800\uDF01\uD800\uDF02\uD800\uDF03",
                CaseUtils.toCamelCase("\uD800\uDF00\uD800\uDF01\uD800\uDF14\uD800\uDF02\uD800\uDF03", true, '\uD800', '\uDF14'));
    }
}
