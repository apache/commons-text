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
class CaseUtilsTest {

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

    @Test
    public void testToSnakeCase() {
        assertThat(CaseUtils.toSnakeCase(null, null)).isNull();
        assertThat(CaseUtils.toSnakeCase("", null)).isEqualTo("");
        assertThat(CaseUtils.toSnakeCase("  ", null)).isEqualTo("");
        assertThat(CaseUtils.toSnakeCase("a  b  c  @def", null)).isEqualTo("a_b_c_@def");
        assertThat(CaseUtils.toSnakeCase("a b c @def")).isEqualTo("a_b_c_@def");
        assertThat(CaseUtils.toSnakeCase("a b c @def", '_')).isEqualTo("a_b_c_@def");
        assertThat(CaseUtils.toSnakeCase("a_b_c_@def", '_')).isEqualTo("a_b_c_@def");
        assertThat(CaseUtils.toSnakeCase("_a___b__c_@def", '_')).isEqualTo("a_b_c_@def");

        final char[] chars = {'-', '+', ' ', '@'};
        assertThat(CaseUtils.toSnakeCase("-+@ ", chars)).isEqualTo("");
        assertThat(CaseUtils.toSnakeCase("   to-SNAKE-cASE", chars)).isEqualTo("to_snake_case");
        assertThat(CaseUtils.toSnakeCase("@@@@   to+SNAKE@cASE ", chars)).isEqualTo("to_snake_case");
        assertThat(CaseUtils.toSnakeCase("To+SN+AK E@cASE", chars)).isEqualTo("to_sn_ak_e_case");

        assertThat(CaseUtils.toSnakeCase("To.Snake.Case", '.')).isEqualTo("to_snake_case");
        assertThat(CaseUtils.toSnakeCase("To.Snake-Case", '-', '.')).isEqualTo("to_snake_case");
        assertThat(CaseUtils.toSnakeCase(" to @ Snake case", '-', '@')).isEqualTo("to_snake_case");
        assertThat(CaseUtils.toSnakeCase(" @to @ Snake case", '-', '@')).isEqualTo("to_snake_case");

        assertThat(CaseUtils.toSnakeCase("tosnakecase")).isEqualTo("tosnakecase");

        assertThat(CaseUtils.toSnakeCase("\uD800\uDF00 \uD800\uDF02")).isEqualTo("\uD800\uDF00_\uD800\uDF02");
        assertThat(CaseUtils.toSnakeCase("\uD800\uDF00\uD800\uDF01\uD800\uDF14\uD800\uDF02\uD800\uDF03", '\uD800',
            '\uDF14')).isEqualTo("\uD800\uDF00\uD800\uDF01_\uD800\uDF02\uD800\uDF03");
    }

    @Test
    public void testToKebabCase() {
        assertThat(CaseUtils.toKebabCase(null, null)).isNull();
        assertThat(CaseUtils.toKebabCase("", null)).isEqualTo("");
        assertThat(CaseUtils.toKebabCase("  ", null)).isEqualTo("");
        assertThat(CaseUtils.toKebabCase("a  b  c  @def", null)).isEqualTo("a-b-c-@def");
        assertThat(CaseUtils.toKebabCase("a b c @def")).isEqualTo("a-b-c-@def");
        assertThat(CaseUtils.toKebabCase("a b c @def", '-')).isEqualTo("a-b-c-@def");
        assertThat(CaseUtils.toKebabCase("a-b-c-@def", '-')).isEqualTo("a-b-c-@def");
        assertThat(CaseUtils.toKebabCase("-a---b--c-@def", '-')).isEqualTo("a-b-c-@def");

        final char[] chars = {'-', '+', ' ', '@'};
        assertThat(CaseUtils.toKebabCase("-+@ ", chars)).isEqualTo("");
        assertThat(CaseUtils.toKebabCase("   to-KEBAB-cASE", chars)).isEqualTo("to-kebab-case");
        assertThat(CaseUtils.toKebabCase("@@@@   to+KEBAB@cASE ", chars)).isEqualTo("to-kebab-case");
        assertThat(CaseUtils.toKebabCase("To+KE+BA B@cASE", chars)).isEqualTo("to-ke-ba-b-case");

        assertThat(CaseUtils.toKebabCase("To.Kebab.Case", '.')).isEqualTo("to-kebab-case");
        assertThat(CaseUtils.toKebabCase("To.Kebab-Case", '-', '.')).isEqualTo("to-kebab-case");
        assertThat(CaseUtils.toKebabCase(" to @ Kebab case", '-', '@')).isEqualTo("to-kebab-case");
        assertThat(CaseUtils.toKebabCase(" @to @ Kebab case", '-', '@')).isEqualTo("to-kebab-case");

        assertThat(CaseUtils.toKebabCase("tokebabcase")).isEqualTo("tokebabcase");

        assertThat(CaseUtils.toKebabCase("\uD800\uDF00 \uD800\uDF02")).isEqualTo("\uD800\uDF00-\uD800\uDF02");
        assertThat(CaseUtils.toKebabCase("\uD800\uDF00\uD800\uDF01\uD800\uDF14\uD800\uDF02\uD800\uDF03", '\uD800',
            '\uDF14')).isEqualTo("\uD800\uDF00\uD800\uDF01-\uD800\uDF02\uD800\uDF03");
    }
}
