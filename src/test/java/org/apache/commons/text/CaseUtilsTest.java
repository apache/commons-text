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

        assertThat(CaseUtils.toCamelCase("\uD800\uDF00 \uD800\uDF02", true, null)).isEqualTo("\uD800\uDF00\uD800\uDF02");
        assertThat(CaseUtils.toCamelCase("\uD800\uDF00\uD800\uDF01\uD800\uDF14\uD800\uDF02\uD800\uDF03", true, '\uD800',
            '\uDF14')).isEqualTo("\uD800\uDF00\uD800\uDF01\uD800\uDF02\uD800\uDF03");

        assertThat(CaseUtils.toCamelCase("The café\u2019s piñata gave me déjà vu.", true, '.'))
                .isEqualTo("TheCafé\u2019sPiñataGaveMeDéjàVu");

        assertThat(CaseUtils.toCamelCase(null)).isNull();
        assertThat(CaseUtils.toCamelCase("")).isEqualTo("");
        assertThat(CaseUtils.toCamelCase("          ")).isEqualTo("");
        assertThat(CaseUtils.toCamelCase("a  b  c  @def")).isEqualTo("aBCDef");
        assertThat(CaseUtils.toCamelCase("a b c @def")).isEqualTo("aBCDef");

        assertThat(CaseUtils.toCamelCase("-+@ ")).isEqualTo("");
        assertThat(CaseUtils.toCamelCase("  to-CAMEL-cASE")).isEqualTo("toCamelCase");
        assertThat(CaseUtils.toCamelCase("@@@@   to+CAMEL@cASE ")).isEqualTo("toCamelCase");
        assertThat(CaseUtils.toCamelCase("To+CA+ME L@cASE")).isEqualTo("toCaMeLCase");

        assertThat(CaseUtils.toCamelCase("To.Camel.Case")).isEqualTo("toCamelCase");
        assertThat(CaseUtils.toCamelCase("To.Camel-Case")).isEqualTo("toCamelCase");
        assertThat(CaseUtils.toCamelCase(" to @ Camel case")).isEqualTo("toCamelCase");
        assertThat(CaseUtils.toCamelCase(" @to @ Camel case_")).isEqualTo("toCamelCase");

        assertThat(CaseUtils.toCamelCase("TO CAMEL CASE")).isEqualTo("toCamelCase");
        assertThat(CaseUtils.toCamelCase("tocamelcase")).isEqualTo("tocamelcase");

        assertThat(CaseUtils.toCamelCase("\uD800\uDF00\uD800\uDF01\uD800\uDF14\uD800\uDF02\uD800\uDF03"))
                .isEqualTo("");

        assertThat(CaseUtils.toCamelCase("The café\u2019s piñata gave me déjà vu."))
                .isEqualTo("theCafesPinataGaveMeDejaVu");
        assertThat(CaseUtils.toCamelCase("\u1E70\u01EB \u010C\u0227\u1E41\u0113\u0142 \u010D\u1E01\u0219\u1E1B"))
                .isEqualTo("toCamelCase");
    }

    @Test
    public void testToCamelSnakeCase() {
        assertThat(CaseUtils.toCamelSnakeCase(null)).isNull();
        assertThat(CaseUtils.toCamelSnakeCase("")).isEqualTo("");
        assertThat(CaseUtils.toCamelSnakeCase("          ")).isEqualTo("");
        assertThat(CaseUtils.toCamelSnakeCase("a  b  c  @def")).isEqualTo("a_B_C_Def");
        assertThat(CaseUtils.toCamelSnakeCase("a b c @def")).isEqualTo("a_B_C_Def");

        assertThat(CaseUtils.toCamelSnakeCase("-+@ ")).isEqualTo("");
        assertThat(CaseUtils.toCamelSnakeCase("  to-CAMELSNAKE-cASE")).isEqualTo("to_Camelsnake_Case");
        assertThat(CaseUtils.toCamelSnakeCase("@@@@   to+CAMELSNAKE@cASE ")).isEqualTo("to_Camelsnake_Case");
        assertThat(CaseUtils.toCamelSnakeCase("To+CAMEL+SNAK E@cASE")).isEqualTo("to_Camel_Snak_E_Case");

        assertThat(CaseUtils.toCamelSnakeCase("To.CamelSnake.Case")).isEqualTo("to_Camelsnake_Case");
        assertThat(CaseUtils.toCamelSnakeCase("To.CamelSnake-Case")).isEqualTo("to_Camelsnake_Case");
        assertThat(CaseUtils.toCamelSnakeCase(" to @ CamelSnake case")).isEqualTo("to_Camelsnake_Case");
        assertThat(CaseUtils.toCamelSnakeCase(" @to @ CamelSnake case_")).isEqualTo("to_Camelsnake_Case");

        assertThat(CaseUtils.toCamelSnakeCase("TO CAMELSNAKE CASE")).isEqualTo("to_Camelsnake_Case");
        assertThat(CaseUtils.toCamelSnakeCase("tocamelsnakecase")).isEqualTo("tocamelsnakecase");

        assertThat(CaseUtils.toCamelSnakeCase("\uD800\uDF00\uD800\uDF01\uD800\uDF14\uD800\uDF02\uD800\uDF03"))
                .isEqualTo("");

        assertThat(CaseUtils.toCamelSnakeCase("The café\u2019s piñata gave me déjà vu."))
                .isEqualTo("the_Cafes_Pinata_Gave_Me_Deja_Vu");
        assertThat(CaseUtils.toCamelSnakeCase("\u010C\u0227\u1E41\u0113\u0142 \u1E61\u1E47\u1EA3\u1E31\u1EB9"))
                .isEqualTo("camel_Snake");
    }

    @Test
    public void testToFlatCase() {
        assertThat(CaseUtils.toFlatCase(null)).isNull();
        assertThat(CaseUtils.toFlatCase("")).isEqualTo("");
        assertThat(CaseUtils.toFlatCase("          ")).isEqualTo("");
        assertThat(CaseUtils.toFlatCase("a  b  c  @def")).isEqualTo("abcdef");
        assertThat(CaseUtils.toFlatCase("a b c @def")).isEqualTo("abcdef");

        assertThat(CaseUtils.toFlatCase("-+@ ")).isEqualTo("");
        assertThat(CaseUtils.toFlatCase("  to-FLAT-cASE")).isEqualTo("toflatcase");
        assertThat(CaseUtils.toFlatCase("@@@@   to+FLAT@cASE ")).isEqualTo("toflatcase");
        assertThat(CaseUtils.toFlatCase("To+FL+A T@cASE")).isEqualTo("toflatcase");

        assertThat(CaseUtils.toFlatCase("To.Flat.Case")).isEqualTo("toflatcase");
        assertThat(CaseUtils.toFlatCase("To.Flat-Case")).isEqualTo("toflatcase");
        assertThat(CaseUtils.toFlatCase(" to @ Flat case")).isEqualTo("toflatcase");
        assertThat(CaseUtils.toFlatCase(" @to @ Flat case_")).isEqualTo("toflatcase");

        assertThat(CaseUtils.toFlatCase("TO FLAT CASE")).isEqualTo("toflatcase");
        assertThat(CaseUtils.toFlatCase("toflatcase")).isEqualTo("toflatcase");

        assertThat(CaseUtils.toFlatCase("\uD800\uDF00\uD800\uDF01\uD800\uDF14\uD800\uDF02\uD800\uDF03"))
                .isEqualTo("");

        assertThat(CaseUtils.toFlatCase("The café\u2019s piñata gave me déjà vu."))
                .isEqualTo("thecafespinatagavemedejavu");
        assertThat(CaseUtils.toFlatCase("\u1E70\u01EB \u1E1E\u0142\u1EA3\u1E6F \u010D\u1E01\u0219\u1E1B"))
                .isEqualTo("toflatcase");
    }

    @Test
    public void testToKebabCase() {
        assertThat(CaseUtils.toKebabCase(null)).isNull();
        assertThat(CaseUtils.toKebabCase("")).isEqualTo("");
        assertThat(CaseUtils.toKebabCase("          ")).isEqualTo("");
        assertThat(CaseUtils.toKebabCase("a  b  c  @def")).isEqualTo("a-b-c-def");
        assertThat(CaseUtils.toKebabCase("a b c @def")).isEqualTo("a-b-c-def");

        assertThat(CaseUtils.toKebabCase("-+@ ")).isEqualTo("");
        assertThat(CaseUtils.toKebabCase("  to-KEBAB-cASE")).isEqualTo("to-kebab-case");
        assertThat(CaseUtils.toKebabCase("@@@@   to+KEBAB@cASE ")).isEqualTo("to-kebab-case");
        assertThat(CaseUtils.toKebabCase("To+KE+BA B@cASE")).isEqualTo("to-ke-ba-b-case");

        assertThat(CaseUtils.toKebabCase("To.Kebab.Case")).isEqualTo("to-kebab-case");
        assertThat(CaseUtils.toKebabCase("To.Kebab-Case")).isEqualTo("to-kebab-case");
        assertThat(CaseUtils.toKebabCase(" to @ Kebab case")).isEqualTo("to-kebab-case");
        assertThat(CaseUtils.toKebabCase(" @to @ Kebab case_")).isEqualTo("to-kebab-case");

        assertThat(CaseUtils.toKebabCase("TO KEBAB CASE")).isEqualTo("to-kebab-case");
        assertThat(CaseUtils.toKebabCase("tokebabcase")).isEqualTo("tokebabcase");

        assertThat(CaseUtils.toKebabCase("\uD800\uDF00\uD800\uDF01\uD800\uDF14\uD800\uDF02\uD800\uDF03"))
                .isEqualTo("");

        assertThat(CaseUtils.toKebabCase("The café\u2019s piñata gave me déjà vu."))
                .isEqualTo("the-cafes-pinata-gave-me-deja-vu");
        assertThat(CaseUtils.toKebabCase("\u1E70\u01EB \u1E31\u0115\u1E07\u0227\u1E05 \u010D\u1E01\u0219\u1E1B"))
                .isEqualTo("to-kebab-case");
    }

    @Test
    public void testToScreamingCase() {
        assertThat(CaseUtils.toScreamingCase(null)).isNull();
        assertThat(CaseUtils.toScreamingCase("")).isEqualTo("");
        assertThat(CaseUtils.toScreamingCase("          ")).isEqualTo("");
        assertThat(CaseUtils.toScreamingCase("a  b  c  @def")).isEqualTo("ABCDEF");
        assertThat(CaseUtils.toScreamingCase("a b c @def")).isEqualTo("ABCDEF");

        assertThat(CaseUtils.toScreamingCase("-+@ ")).isEqualTo("");
        assertThat(CaseUtils.toScreamingCase("  to-SCREAMING-cASE")).isEqualTo("TOSCREAMINGCASE");
        assertThat(CaseUtils.toScreamingCase("@@@@   to+SCREAMING@cASE ")).isEqualTo("TOSCREAMINGCASE");
        assertThat(CaseUtils.toScreamingCase("To+SCR+EAM ING@cASE")).isEqualTo("TOSCREAMINGCASE");

        assertThat(CaseUtils.toScreamingCase("To.SCREAMING.Case")).isEqualTo("TOSCREAMINGCASE");
        assertThat(CaseUtils.toScreamingCase("To.SCREAMING-Case")).isEqualTo("TOSCREAMINGCASE");
        assertThat(CaseUtils.toScreamingCase(" to @ SCREAMING case")).isEqualTo("TOSCREAMINGCASE");
        assertThat(CaseUtils.toScreamingCase(" @to @ SCREAMING case_")).isEqualTo("TOSCREAMINGCASE");

        assertThat(CaseUtils.toScreamingCase("TO SCREAMING CASE")).isEqualTo("TOSCREAMINGCASE");
        assertThat(CaseUtils.toScreamingCase("toscreamingcase")).isEqualTo("TOSCREAMINGCASE");

        assertThat(CaseUtils.toScreamingCase("\uD800\uDF00\uD800\uDF01\uD800\uDF14\uD800\uDF02\uD800\uDF03"))
                .isEqualTo("");

        assertThat(CaseUtils.toScreamingCase("The café\u2019s piñata gave me déjà vu."))
                .isEqualTo("THECAFESPINATAGAVEMEDEJAVU");

        assertThat(CaseUtils.toScreamingCase("\u0218\u0107\u1E59\u0113\u1E01\u1E43\u1EC9\u1F09\u0148\u1E21 " +
                                             "\u010D\u1E01\u0219\u1E1B")).isEqualTo("SCREAMINGCASE");
    }

    @Test
    public void testToScreamingKebabCase() {
        assertThat(CaseUtils.toScreamingKebabCase(null)).isNull();
        assertThat(CaseUtils.toScreamingKebabCase("")).isEqualTo("");
        assertThat(CaseUtils.toScreamingKebabCase("          ")).isEqualTo("");
        assertThat(CaseUtils.toScreamingKebabCase("a  b  c  @def")).isEqualTo("A-B-C-DEF");
        assertThat(CaseUtils.toScreamingKebabCase("a b c @def")).isEqualTo("A-B-C-DEF");

        assertThat(CaseUtils.toScreamingKebabCase("-+@ ")).isEqualTo("");
        assertThat(CaseUtils.toScreamingKebabCase("  to-KEBAB-cASE")).isEqualTo("TO-KEBAB-CASE");
        assertThat(CaseUtils.toScreamingKebabCase("@@@@   to+KEBAB@cASE ")).isEqualTo("TO-KEBAB-CASE");
        assertThat(CaseUtils.toScreamingKebabCase("To+KE+BA B@cASE")).isEqualTo("TO-KE-BA-B-CASE");

        assertThat(CaseUtils.toScreamingKebabCase("To.Kebab.Case")).isEqualTo("TO-KEBAB-CASE");
        assertThat(CaseUtils.toScreamingKebabCase("To.Kebab-Case")).isEqualTo("TO-KEBAB-CASE");
        assertThat(CaseUtils.toScreamingKebabCase(" to @ Kebab case")).isEqualTo("TO-KEBAB-CASE");
        assertThat(CaseUtils.toScreamingKebabCase(" @to @ Kebab case_")).isEqualTo("TO-KEBAB-CASE");

        assertThat(CaseUtils.toScreamingKebabCase("TO KEBAB CASE")).isEqualTo("TO-KEBAB-CASE");
        assertThat(CaseUtils.toScreamingKebabCase("tokebabcase")).isEqualTo("TOKEBABCASE");

        assertThat(CaseUtils.toScreamingKebabCase("\uD800\uDF00\uD800\uDF01\uD800\uDF14\uD800\uDF02\uD800\uDF03"))
                .isEqualTo("");

        assertThat(CaseUtils.toScreamingKebabCase("The café\u2019s piñata gave me déjà vu."))
                .isEqualTo("THE-CAFES-PINATA-GAVE-ME-DEJA-VU");

        assertThat(CaseUtils.toScreamingKebabCase("\u1E70\u01EB \u1E31\u0115\u1E07\u0227\u1E05 \u010D\u1E01\u0219\u1E1B"))
                .isEqualTo("TO-KEBAB-CASE");
    }

    @Test
    public void testToScreamingSnakeCase() {
        assertThat(CaseUtils.toScreamingSnakeCase(null)).isNull();
        assertThat(CaseUtils.toScreamingSnakeCase("")).isEqualTo("");
        assertThat(CaseUtils.toScreamingSnakeCase("          ")).isEqualTo("");
        assertThat(CaseUtils.toScreamingSnakeCase("a  b  c  @def")).isEqualTo("A_B_C_DEF");
        assertThat(CaseUtils.toScreamingSnakeCase("a b c @def")).isEqualTo("A_B_C_DEF");

        assertThat(CaseUtils.toScreamingSnakeCase("-+@ ")).isEqualTo("");
        assertThat(CaseUtils.toScreamingSnakeCase("  to-SNAKE-cASE")).isEqualTo("TO_SNAKE_CASE");
        assertThat(CaseUtils.toScreamingSnakeCase("@@@@   to+SNAKE@cASE ")).isEqualTo("TO_SNAKE_CASE");
        assertThat(CaseUtils.toScreamingSnakeCase("To+SN+AK E@cASE")).isEqualTo("TO_SN_AK_E_CASE");

        assertThat(CaseUtils.toScreamingSnakeCase("To.Snake.Case")).isEqualTo("TO_SNAKE_CASE");
        assertThat(CaseUtils.toScreamingSnakeCase("To.Snake-Case")).isEqualTo("TO_SNAKE_CASE");
        assertThat(CaseUtils.toScreamingSnakeCase(" to @ Snake case")).isEqualTo("TO_SNAKE_CASE");
        assertThat(CaseUtils.toScreamingSnakeCase(" @to @ Snake case_")).isEqualTo("TO_SNAKE_CASE");

        assertThat(CaseUtils.toScreamingSnakeCase("TO SNAKE CASE")).isEqualTo("TO_SNAKE_CASE");
        assertThat(CaseUtils.toScreamingSnakeCase("tosnakecase")).isEqualTo("TOSNAKECASE");

        assertThat(CaseUtils.toScreamingSnakeCase("\uD800\uDF00\uD800\uDF01\uD800\uDF14\uD800\uDF02\uD800\uDF03"))
                .isEqualTo("");

        assertThat(CaseUtils.toScreamingSnakeCase("The café\u2019s piñata gave me déjà vu."))
                .isEqualTo("THE_CAFES_PINATA_GAVE_ME_DEJA_VU");

        assertThat(CaseUtils.toScreamingSnakeCase("\u1E70\u01EB \u1E61\u1E47\u1EA3\u1E31\u1EB9 " +
                                                  "\u010D\u1E01\u0219\u1E1B")).isEqualTo("TO_SNAKE_CASE");
    }

    @Test
    public void testToSnakeCase() {
        assertThat(CaseUtils.toSnakeCase(null)).isNull();
        assertThat(CaseUtils.toSnakeCase("")).isEqualTo("");
        assertThat(CaseUtils.toSnakeCase("          ")).isEqualTo("");
        assertThat(CaseUtils.toSnakeCase("a  b  c  @def")).isEqualTo("a_b_c_def");
        assertThat(CaseUtils.toSnakeCase("a b c @def")).isEqualTo("a_b_c_def");

        assertThat(CaseUtils.toSnakeCase("-+@ ")).isEqualTo("");
        assertThat(CaseUtils.toSnakeCase("  to-SNAKE-cASE")).isEqualTo("to_snake_case");
        assertThat(CaseUtils.toSnakeCase("@@@@   to+SNAKE@cASE ")).isEqualTo("to_snake_case");
        assertThat(CaseUtils.toSnakeCase("To+SN+AK E@cASE")).isEqualTo("to_sn_ak_e_case");

        assertThat(CaseUtils.toSnakeCase("To.Snake.Case")).isEqualTo("to_snake_case");
        assertThat(CaseUtils.toSnakeCase("To.Snake-Case")).isEqualTo("to_snake_case");
        assertThat(CaseUtils.toSnakeCase(" to @ Snake case")).isEqualTo("to_snake_case");
        assertThat(CaseUtils.toSnakeCase(" @to @ Snake case_")).isEqualTo("to_snake_case");

        assertThat(CaseUtils.toSnakeCase("TO SNAKE CASE")).isEqualTo("to_snake_case");
        assertThat(CaseUtils.toSnakeCase("tosnakecase")).isEqualTo("tosnakecase");

        assertThat(CaseUtils.toSnakeCase("\uD800\uDF00\uD800\uDF01\uD800\uDF14\uD800\uDF02\uD800\uDF03"))
                .isEqualTo("");

        assertThat(CaseUtils.toSnakeCase("The café\u2019s piñata gave me déjà vu."))
                .isEqualTo("the_cafes_pinata_gave_me_deja_vu");
        assertThat(CaseUtils.toSnakeCase("\u1E70\u01EB \u1E61\u1E47\u1EA3\u1E31\u1EB9 \u010D\u1E01\u0219\u1E1B"))
                .isEqualTo("to_snake_case");
    }

    @Test
    public void testToTitleCase() {
        assertThat(CaseUtils.toTitleCase(null)).isNull();
        assertThat(CaseUtils.toTitleCase("")).isEqualTo("");
        assertThat(CaseUtils.toTitleCase("          ")).isEqualTo("");
        assertThat(CaseUtils.toTitleCase("a  b  c  @def")).isEqualTo("A_B_C_Def");
        assertThat(CaseUtils.toTitleCase("a b c @def")).isEqualTo("A_B_C_Def");

        assertThat(CaseUtils.toTitleCase("-+@ ")).isEqualTo("");
        assertThat(CaseUtils.toTitleCase("  to-TITLE-cASE")).isEqualTo("To_Title_Case");
        assertThat(CaseUtils.toTitleCase("@@@@   to+TITLE@cASE ")).isEqualTo("To_Title_Case");
        assertThat(CaseUtils.toTitleCase("To+TI+TL E@cASE")).isEqualTo("To_Ti_Tl_E_Case");

        assertThat(CaseUtils.toTitleCase("To.Title.Case")).isEqualTo("To_Title_Case");
        assertThat(CaseUtils.toTitleCase("To.Title-Case")).isEqualTo("To_Title_Case");
        assertThat(CaseUtils.toTitleCase(" to @ Title case")).isEqualTo("To_Title_Case");
        assertThat(CaseUtils.toTitleCase(" @to @ Title case_")).isEqualTo("To_Title_Case");

        assertThat(CaseUtils.toTitleCase("TO TITLE CASE")).isEqualTo("To_Title_Case");
        assertThat(CaseUtils.toTitleCase("totitlecase")).isEqualTo("Totitlecase");

        assertThat(CaseUtils.toTitleCase("\uD800\uDF00\uD800\uDF01\uD800\uDF14\uD800\uDF02\uD800\uDF03"))
                .isEqualTo("");

        assertThat(CaseUtils.toTitleCase("The café\u2019s piñata gave me déjà vu."))
                .isEqualTo("The_Cafes_Pinata_Gave_Me_Deja_Vu");
        assertThat(CaseUtils.toTitleCase("\u1E70\u01EB \u1E6A\u1ECB\u1E71\u0142\u1EB9 \u010D\u1E01\u0219\u1E1B"))
                .isEqualTo("To_Title_Case");
    }

    @Test
    public void testToTrainCase() {
        assertThat(CaseUtils.toTrainCase(null)).isNull();
        assertThat(CaseUtils.toTrainCase("")).isEqualTo("");
        assertThat(CaseUtils.toTrainCase("          ")).isEqualTo("");
        assertThat(CaseUtils.toTrainCase("a  b  c  @def")).isEqualTo("A-B-C-Def");
        assertThat(CaseUtils.toTrainCase("a b c @def")).isEqualTo("A-B-C-Def");

        assertThat(CaseUtils.toTrainCase("-+@ ")).isEqualTo("");
        assertThat(CaseUtils.toTrainCase("  to-TRAIN-cASE")).isEqualTo("To-Train-Case");
        assertThat(CaseUtils.toTrainCase("@@@@   to+TRAIN@cASE ")).isEqualTo("To-Train-Case");
        assertThat(CaseUtils.toTrainCase("To+TR+AI N@cASE")).isEqualTo("To-Tr-Ai-N-Case");

        assertThat(CaseUtils.toTrainCase("To.Train.Case")).isEqualTo("To-Train-Case");
        assertThat(CaseUtils.toTrainCase("To.Train-Case")).isEqualTo("To-Train-Case");
        assertThat(CaseUtils.toTrainCase(" to @ Train case")).isEqualTo("To-Train-Case");
        assertThat(CaseUtils.toTrainCase(" @to @ Train case_")).isEqualTo("To-Train-Case");

        assertThat(CaseUtils.toTrainCase("TO TRAIN CASE")).isEqualTo("To-Train-Case");
        assertThat(CaseUtils.toTrainCase("totraincase")).isEqualTo("Totraincase");

        assertThat(CaseUtils.toTrainCase("\uD800\uDF00\uD800\uDF01\uD800\uDF14\uD800\uDF02\uD800\uDF03"))
                .isEqualTo("");

        assertThat(CaseUtils.toTrainCase("The café\u2019s piñata gave me déjà vu."))
                .isEqualTo("The-Cafes-Pinata-Gave-Me-Deja-Vu");
        assertThat(CaseUtils.toTrainCase("\u1E70\u01EB \u1E6A\u0211\u0101\u012F\u1E49 \u010D\u1E01\u0219\u1E1B"))
                .isEqualTo("To-Train-Case");
    }

    @Test
    public void testToUpperCamelCase() {
        assertThat(CaseUtils.toUpperCamelCase(null)).isNull();
        assertThat(CaseUtils.toUpperCamelCase("")).isEqualTo("");
        assertThat(CaseUtils.toUpperCamelCase("          ")).isEqualTo("");
        assertThat(CaseUtils.toUpperCamelCase("a  b  c  @def")).isEqualTo("ABCDef");
        assertThat(CaseUtils.toUpperCamelCase("a b c @def")).isEqualTo("ABCDef");

        assertThat(CaseUtils.toUpperCamelCase("-+@ ")).isEqualTo("");
        assertThat(CaseUtils.toUpperCamelCase("  to-CAMEL-cASE")).isEqualTo("ToCamelCase");
        assertThat(CaseUtils.toUpperCamelCase("@@@@   to+CAMEL@cASE ")).isEqualTo("ToCamelCase");
        assertThat(CaseUtils.toUpperCamelCase("To+CA+ME L@cASE")).isEqualTo("ToCaMeLCase");

        assertThat(CaseUtils.toUpperCamelCase("To.Camel.Case")).isEqualTo("ToCamelCase");
        assertThat(CaseUtils.toUpperCamelCase("To.Camel-Case")).isEqualTo("ToCamelCase");
        assertThat(CaseUtils.toUpperCamelCase(" to @ Camel case")).isEqualTo("ToCamelCase");
        assertThat(CaseUtils.toUpperCamelCase(" @to @ Camel case_")).isEqualTo("ToCamelCase");

        assertThat(CaseUtils.toUpperCamelCase("TO CAMEL CASE")).isEqualTo("ToCamelCase");
        assertThat(CaseUtils.toUpperCamelCase("tocamelcase")).isEqualTo("Tocamelcase");

        assertThat(CaseUtils.toUpperCamelCase("\uD800\uDF00\uD800\uDF01\uD800\uDF14\uD800\uDF02\uD800\uDF03"))
                .isEqualTo("");

        assertThat(CaseUtils.toUpperCamelCase("The café\u2019s piñata gave me déjà vu."))
                .isEqualTo("TheCafesPinataGaveMeDejaVu");
        assertThat(CaseUtils.toUpperCamelCase("\u1E70\u01EB \u010C\u0227\u1E41\u0113\u0142 \u010D\u1E01\u0219\u1E1B"))
                .isEqualTo("ToCamelCase");
    }

}
