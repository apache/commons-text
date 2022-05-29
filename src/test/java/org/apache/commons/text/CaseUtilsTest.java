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
package org.apache.commons.text;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link CaseUtils} class.
 */
public class CaseUtilsTest {

    @Test
    public void testConstructor() {
        assertThat(new CaseUtils()).isNotNull();
        final Constructor<?>[] cons = CaseUtils.class.getDeclaredConstructors();
        assertThat(cons.length).isEqualTo(1);
        assertThat(Modifier.isPublic(cons[0].getModifiers())).isTrue();
        assertThat(Modifier.isPublic(CaseUtils.class.getModifiers())).isTrue();
        assertThat(Modifier.isFinal(CaseUtils.class.getModifiers())).isFalse();
    }

    @Test
    public void testToCamelCase() {
        assertThat(CaseUtils.toCamelCase(null, false, null)).isNull();
        assertThat(CaseUtils.toCamelCase("", true, null)).isEqualTo("");
        assertThat(CaseUtils.toCamelCase("  ", false, null)).isEqualTo("");
        assertThat(CaseUtils.toCamelCase("a  b  c  @def", false, null)).isEqualTo("aBC@def");
        assertThat(CaseUtils.toCamelCase("a b c @def", true)).isEqualTo("ABC@def");
        assertThat(CaseUtils.toCamelCase("a b c @def", true, '-')).isEqualTo("ABC@def");
        assertThat(CaseUtils.toCamelCase("a b c @def", true, '-')).isEqualTo("ABC@def");

        final char[] chars = {'-', '+', ' ', '@'};
        assertThat(CaseUtils.toCamelCase("-+@ ", true, chars)).isEqualTo("");
        assertThat(CaseUtils.toCamelCase("   to-CAMEL-cASE", false, chars)).isEqualTo("toCamelCase");
        assertThat(CaseUtils.toCamelCase("@@@@   to+CAMEL@cASE ", true, chars)).isEqualTo("ToCamelCase");
        assertThat(CaseUtils.toCamelCase("To+CA+ME L@cASE", true, chars)).isEqualTo("ToCaMeLCase");

        assertThat(CaseUtils.toCamelCase("To.Camel.Case", false, '.')).isEqualTo("toCamelCase");
        assertThat(CaseUtils.toCamelCase("To.Camel-Case", false, '-', '.')).isEqualTo("toCamelCase");
        assertThat(CaseUtils.toCamelCase(" to @ Camel case", false, '-', '@')).isEqualTo("toCamelCase");
        assertThat(CaseUtils.toCamelCase(" @to @ Camel case", true, '-', '@')).isEqualTo("ToCamelCase");

        assertThat(CaseUtils.toCamelCase("TO CAMEL CASE", true, null)).isEqualTo("ToCamelCase");
        assertThat(CaseUtils.toCamelCase("TO CAMEL CASE", false, null)).isEqualTo("toCamelCase");
        assertThat(CaseUtils.toCamelCase("TO CAMEL CASE", false, null)).isEqualTo("toCamelCase");
        assertThat(CaseUtils.toCamelCase("tocamelcase", false, null)).isEqualTo("tocamelcase");
        assertThat(CaseUtils.toCamelCase("tocamelcase", true, null)).isEqualTo("Tocamelcase");
        assertThat(CaseUtils.toCamelCase("Tocamelcase", false, null)).isEqualTo("tocamelcase");

        assertThat(CaseUtils.toCamelCase("tocamelcase", true)).isEqualTo("Tocamelcase");
        assertThat(CaseUtils.toCamelCase("tocamelcase", false)).isEqualTo("tocamelcase");

        assertThat(CaseUtils.toCamelCase("\uD800\uDF00 \uD800\uDF02", true)).isEqualTo("\uD800\uDF00\uD800\uDF02");
        assertThat(CaseUtils.toCamelCase("\uD800\uDF00\uD800\uDF01\uD800\uDF14\uD800\uDF02\uD800\uDF03", true, '\uD800',
            '\uDF14')).isEqualTo("\uD800\uDF00\uD800\uDF01\uD800\uDF02\uD800\uDF03");
    }
}
