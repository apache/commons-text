/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.commons.text;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.text.CasedString.StringCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class CasedStringTest {

    private static String helloWorldValue(StringCase stringCase) {
        switch (stringCase) {
            case CAMEL:
                return "helloWorld";
            case KEBAB:
                return "hello-World";
            case PHRASE:
                return "hello World";
            case SNAKE:
                return "hello_World";
            case DOT:
                return "hello.World";
            default:
                fail("Unsupported StringCase: " + stringCase);
        }
        return null; // keeps compiler happy
    }

    private static final CasedString CAMEL = new CasedString(StringCase.CAMEL, "aCamelString");
    private static final CasedString PHRASE = new CasedString(StringCase.PHRASE, "A test PhrAse");
    private static final CasedString KEBAB = new CasedString(StringCase.KEBAB, "A-kebAb-string");
    private static final CasedString SNAKE = new CasedString(StringCase.SNAKE, "A_snaKE_string");
    private static final CasedString DOT = new CasedString(StringCase.DOT, "A.dOt.string");
    private static final CasedString ABCDEF = new CasedString(StringCase.PHRASE, "a  b  c  @def");
    /**
     * tests the conversion from each Cased string type to every other type.
     * @param underTest the CasedString being tested.
     */
    @ParameterizedTest
    @MethodSource("conversionProvider")
    public void testCrossProductConversions(CasedString underTest) {
        for (StringCase stringCase : StringCase.values()) {
            assertEquals(helloWorldValue(stringCase), underTest.toCase(stringCase), () -> "failed converting to " + stringCase);
        }
    }
    /* generates the hello world Cased String for every StringCase */
    private static Stream<Arguments> conversionProvider() {
        List<Arguments> lst = new ArrayList<>();
        for (StringCase stringCase : StringCase.values()) {
            lst.add(Arguments.of(new CasedString(stringCase, helloWorldValue(stringCase))));
        }
        return lst.stream();
    }

    @Test
    public void testNullConstructor() {
        for (StringCase stringCase : StringCase.values()) {
            CasedString underTest = new CasedString(stringCase, null);
            assertThat(underTest.toString()).isNull();
            assertThat(underTest.getSegments()).isEmpty();
            // test a null underTest can convert to all others types.
            for (CasedString.StringCase otherCase : StringCase.values()) {
                assertThat(underTest.toCase(otherCase)).isNull();
            }
        }
    }

    @Test
    public void testToCamelCase() {
        assertThat(new CasedString(StringCase.CAMEL, "").toString()).isEqualTo("");
        assertThat(new CasedString(StringCase.CAMEL, "  ").toString()).isEqualTo("");
        assertThat(new CasedString(StringCase.CAMEL, "Tocamelcase").toString()).isEqualTo("tocamelcase");
        assertThat(new CasedString(StringCase.PHRASE, "\uD800\uDF00 \uD800\uDF02").toCase(StringCase.CAMEL)).isEqualTo("\uD800\uDF00\uD800\uDF02");
        assertThat(ABCDEF.toCase(StringCase.CAMEL)).isEqualTo("aBC@def");
        assertThat(CAMEL.toCase(StringCase.CAMEL)).isEqualTo("aCamelString");
        assertThat(PHRASE.toCase(StringCase.CAMEL)).isEqualTo("aTestPhrase");
        assertThat(KEBAB.toCase(StringCase.CAMEL)).isEqualTo("aKebabString");
        assertThat(SNAKE.toCase(StringCase.CAMEL)).isEqualTo("aSnakeString");
        assertThat(DOT.toCase(StringCase.CAMEL)).isEqualTo("aDotString");
        assertThat(new CasedString(StringCase.PHRASE, "TO  CAMEL CASE").toCase(StringCase.CAMEL)).isEqualTo("toCamelCase");
        assertThat(new CasedString(StringCase.KEBAB, "   to-CAMEL-cASE").toCase(StringCase.CAMEL)).isEqualTo("toCamelCase");
        assertThat(new CasedString(StringCase.DOT, "To.Camel.Case").toCase(StringCase.CAMEL)).isEqualTo("toCamelCase");
    }

    @Test
    public void testToPhraseTest() {
        assertThat(new CasedString(StringCase.PHRASE, "").toString()).isEqualTo("");
        assertThat(new CasedString(StringCase.PHRASE, "  ").toString()).isEqualTo("");
        assertThat(ABCDEF.toCase(StringCase.PHRASE)).isEqualTo("a b c @def");
        assertThat(CAMEL.toCase(StringCase.PHRASE)).isEqualTo("a Camel String");
        assertThat(PHRASE.toCase(StringCase.PHRASE)).isEqualTo("A test PhrAse");
        assertThat(KEBAB.toCase(StringCase.PHRASE)).isEqualTo("A kebAb string");
        assertThat(SNAKE.toCase(StringCase.PHRASE)).isEqualTo("A snaKE string");
        assertThat(DOT.toCase(StringCase.PHRASE)).isEqualTo("A dOt string");
    }

    @Test
    public void testToKebabTest() {
        assertThat(new CasedString(StringCase.KEBAB, "").toString()).isEqualTo("");
        assertThat(new CasedString(StringCase.KEBAB, "  ").toString()).isEqualTo("");
        assertThat(ABCDEF.toCase(StringCase.KEBAB)).isEqualTo("a-b-c-@def");
        assertThat(CAMEL.toCase(StringCase.KEBAB)).isEqualTo("a-Camel-String");
        assertThat(PHRASE.toCase(StringCase.KEBAB)).isEqualTo("A-test-PhrAse");
        assertThat(KEBAB.toCase(StringCase.KEBAB)).isEqualTo("A-kebAb-string");
        assertThat(SNAKE.toCase(StringCase.KEBAB)).isEqualTo("A-snaKE-string");
        assertThat(DOT.toCase(StringCase.KEBAB)).isEqualTo("A-dOt-string");
    }

    @Test
    public void testToSnakeTest() {
        assertThat(new CasedString(StringCase.SNAKE, "").toString()).isEqualTo("");
        assertThat(new CasedString(StringCase.SNAKE, "  ").toString()).isEqualTo("");
        assertThat(ABCDEF.toCase(StringCase.SNAKE)).isEqualTo("a_b_c_@def");
        assertThat(CAMEL.toCase(StringCase.SNAKE)).isEqualTo("a_Camel_String");
        assertThat(PHRASE.toCase(StringCase.SNAKE)).isEqualTo("A_test_PhrAse");
        assertThat(KEBAB.toCase(StringCase.SNAKE)).isEqualTo("A_kebAb_string");
        assertThat(SNAKE.toCase(StringCase.SNAKE)).isEqualTo("A_snaKE_string");
        assertThat(DOT.toCase(StringCase.SNAKE)).isEqualTo("A_dOt_string");
    }

    @Test
    public void testToDotTest() {
        assertThat(new CasedString(StringCase.DOT, "").toString()).isEqualTo("");
        assertThat(new CasedString(StringCase.DOT, "  ").toString()).isEqualTo("");
        assertThat(ABCDEF.toCase(StringCase.DOT)).isEqualTo("a.b.c.@def");
        assertThat(CAMEL.toCase(StringCase.DOT)).isEqualTo("a.Camel.String");
        assertThat(PHRASE.toCase(StringCase.DOT)).isEqualTo("A.test.PhrAse");
        assertThat(KEBAB.toCase(StringCase.DOT)).isEqualTo("A.kebAb.string");
        assertThat(SNAKE.toCase(StringCase.DOT)).isEqualTo("A.snaKE.string");
        assertThat(DOT.toCase(StringCase.DOT)).isEqualTo("A.dOt.string");
    }
}
