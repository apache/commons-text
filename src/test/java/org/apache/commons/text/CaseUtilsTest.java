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
        assertThat(CaseUtils.toCamelCase("a  b  c  @def", false, null))
                .isEqualTo("aBC@def");
        assertThat(CaseUtils.toCamelCase("a b c @def", true)).isEqualTo("ABC@def");
        assertThat(CaseUtils.toCamelCase("a b c @def", true, '-'))
                .isEqualTo("ABC@def");
        assertThat(CaseUtils.toCamelCase("a b c @def", true, '-'))
                .isEqualTo("ABC@def");

        final char[] chars = {'-', '+', ' ', '@'};
        assertThat(CaseUtils.toCamelCase("-+@ ", true, chars)).isEqualTo("");
        assertThat(CaseUtils.toCamelCase("   to-CAMEL-cASE", false, chars))
                .isEqualTo("toCamelCase");
        assertThat(CaseUtils.toCamelCase("@@@@   to+CAMEL@cASE ", true, chars))
                .isEqualTo("ToCamelCase");
        assertThat(CaseUtils.toCamelCase("To+CA+ME L@cASE", true, chars))
                .isEqualTo("ToCaMeLCase");

        assertThat(CaseUtils.toCamelCase("To.Camel.Case", false, '.'))
                .isEqualTo("toCamelCase");
        assertThat(CaseUtils.toCamelCase("To.Camel-Case", false, '-', '.'))
                .isEqualTo("toCamelCase");
        assertThat(CaseUtils.toCamelCase(" to @ Camel case", false, '-', '@'))
                .isEqualTo("toCamelCase");
        assertThat(CaseUtils.toCamelCase(" @to @ Camel case", true, '-', '@'))
                .isEqualTo("ToCamelCase");

        assertThat(CaseUtils.toCamelCase("TO CAMEL CASE", true, null))
                .isEqualTo("ToCamelCase");
        assertThat(CaseUtils.toCamelCase("TO CAMEL CASE", false, null))
                .isEqualTo("toCamelCase");
        assertThat(CaseUtils.toCamelCase("TO CAMEL CASE", false, null))
                .isEqualTo("toCamelCase");
        assertThat(CaseUtils.toCamelCase("tocamelcase", false, null))
                .isEqualTo("tocamelcase");
        assertThat(CaseUtils.toCamelCase("tocamelcase", true, null))
                .isEqualTo("Tocamelcase");
        assertThat(CaseUtils.toCamelCase("Tocamelcase", false, null))
                .isEqualTo("tocamelcase");

        assertThat(CaseUtils.toCamelCase("tocamelcase", true)).isEqualTo("Tocamelcase");
        assertThat(CaseUtils.toCamelCase("tocamelcase", false)).isEqualTo("tocamelcase");

//        // These tests fail the new toCamelCase(String, Boolean, char[]) method because
//        // surrogate pairs \uD800 to \uDFFF have been removed from the Unicode Character Set.
//        assertThat(CaseUtils.toCamelCase("\uD800\uDF00 \uD800\uDF02", true)).isEqualTo("\uD800\uDF00\uD800\uDF02");
//        assertThat(CaseUtils.toCamelCase("\uD800\uDF00\uD800\uDF01\uD800\uDF14\uD800\uDF02\uD800\uDF03", true, '\uD800',
//                '\uDF14')).isEqualTo("\uD800\uDF00\uD800\uDF01\uD800\uDF02\uD800\uDF03");

        /* **** NEW TESTS **** */

        assertThat(CaseUtils.toCamelCase("The café\u2019s piñata gave me déjà vu.",
                false, ' ', '.')).isEqualTo("theCafé\u2019sPiñataGaveMeDéjàVu");

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
    public void testToDelimitedCase() {
        assertThat(CaseUtils.toDelimitedCase(null, null)).isNull();
        assertThat(CaseUtils.toDelimitedCase(null, null, null)).isNull();
        assertThat(CaseUtils.toDelimitedCase("", ' ')).isEqualTo("");
        assertThat(CaseUtils.toDelimitedCase("          ", ' ')).isEqualTo("");
        assertThat(CaseUtils.toDelimitedCase("a  b  c  @def", '_')).isEqualTo("A_B_C_Def");
        assertThat(CaseUtils.toDelimitedCase("a b c @def", '-')).isEqualTo("A-B-C-Def");
        assertThat(CaseUtils.toDelimitedCase("a  b  c  @def", true, null))
                .isEqualTo("ABCDef");
        assertThat(CaseUtils.toDelimitedCase("a  b  c  @def", false, null))
                .isEqualTo("aBCDef");
        assertThat(CaseUtils.toDelimitedCase("a  b  c  @def", null, null))
                .isEqualTo("ABCDef");
        assertThat(CaseUtils.toDelimitedCase("a  b  c  @def", null, '_'))
                .isEqualTo("A_B_C_Def");

        assertThat(CaseUtils.toDelimitedCase("-+@ ", '@')).isEqualTo("");

        assertThat(CaseUtils.toDelimitedCase("  to-Delimited-cASE", '_'))
                .isEqualTo("To_Delimited_Case");
        assertThat(CaseUtils.toDelimitedCase("@@@@   to+DELIMITED@cASE ", '_'))
                .isEqualTo("To_Delimited_Case");
        assertThat(CaseUtils.toDelimitedCase("To+DELIM+IT ED@cASE", '+'))
                .isEqualTo("To+Delim+It+Ed+Case");

        assertThat(CaseUtils.toDelimitedCase("  to-Delimited-cASE", false, '_'))
                .isEqualTo("to_Delimited_Case");
        assertThat(CaseUtils.toDelimitedCase("@@@@   to+DELIMITED@cASE ", false, '_'))
                .isEqualTo("to_Delimited_Case");
        assertThat(CaseUtils.toDelimitedCase("To+DELIM+IT ED@cASE", false, '+'))
                .isEqualTo("to+Delim+It+Ed+Case");

        assertThat(CaseUtils.toDelimitedCase("To.Delimited.Case", '.'))
                .isEqualTo("To.Delimited.Case");
        assertThat(CaseUtils.toDelimitedCase("To.Delimited-Case", '\u2250'))
                .isEqualTo("To\u2250Delimited\u2250Case");
        assertThat(CaseUtils.toDelimitedCase(" to @ Delimited case", ' '))
                .isEqualTo("To Delimited Case");
        assertThat(CaseUtils.toDelimitedCase(" @to @ Delimited case_", '@'))
                .isEqualTo("To@Delimited@Case");

        assertThat(CaseUtils.toDelimitedCase("To.Delimited.Case", false, '.'))
                .isEqualTo("to.Delimited.Case");
        assertThat(CaseUtils.toDelimitedCase("To.Delimited-Case", false, '\u2250'))
                .isEqualTo("to\u2250Delimited\u2250Case");
        assertThat(CaseUtils.toDelimitedCase(" to @ Delimited case", false, ' '))
                .isEqualTo("to Delimited Case");
        assertThat(CaseUtils.toDelimitedCase(" @to @ Delimited case_", false, '@'))
                .isEqualTo("to@Delimited@Case");

        assertThat(CaseUtils.toDelimitedCase("TO DELIMITED CASE", '_'))
                .isEqualTo("To_Delimited_Case");
        assertThat(CaseUtils.toDelimitedCase("todelimitedcase", '_'))
                .isEqualTo("Todelimitedcase");

        assertThat(CaseUtils.toDelimitedCase("\uD800\uDF00\uD800\uDF01\uD800\uDF14\uD800\uDF02\uD800\uDF03",
                '\uD800')).isEqualTo("");

        assertThat(CaseUtils.toDelimitedCase("The café\u2019s piñata gave me déjà vu.", '_'))
                .isEqualTo("The_Cafes_Pinata_Gave_Me_Deja_Vu");
        assertThat(CaseUtils.toDelimitedCase("The café\u2019s piñata gave me déjà vu.",
                false, '_')).isEqualTo("the_Cafes_Pinata_Gave_Me_Deja_Vu");
        assertThat(CaseUtils.toDelimitedCase("\u1E12\u0205\u0142\u012B\u1E43\u01D0\u1E6B\u0119\u1E0B " +
                                             "\u010D\u1E01\u0219\u1E1B", '_'))
                .isEqualTo("Delimited_Case");

        assertThat(CaseUtils.toDelimitedCase("Will 'O The Wisp", '-')).isEqualTo("Will-O-The-Wisp");

        assertThat(CaseUtils.toDelimitedCase("\u2026with boughs of holly\u2026\n\u2019Tis the season\u2026",
                '\u272F'))
                .isEqualTo("With\u272FBoughs\u272FOf\u272FHolly\u272FTis\u272FThe\u272FSeason");
        assertThat(CaseUtils.toDelimitedCase("\u2026with boughs of holly\u2026\n\u2019Tis the season\u2026",
                false,'\u272F'))
                .isEqualTo("with\u272FBoughs\u272FOf\u272FHolly\u272FTis\u272FThe\u272FSeason");

        assertThat(CaseUtils.toDelimitedCase("\"Officer O'Malley and Peart O'Niel " +
                                             "walk into the Protestant's Bar.\"", '_'))
                .isEqualTo("Officer_O_Malley_And_Peart_O_Niel_Walk_Into_The_Protestants_Bar");
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
    public void testToPascalCase() {
        assertThat(CaseUtils.toPascalCase(null)).isNull();
        assertThat(CaseUtils.toPascalCase("")).isEqualTo("");
        assertThat(CaseUtils.toPascalCase("          ")).isEqualTo("");
        assertThat(CaseUtils.toPascalCase("a  b  c  @def")).isEqualTo("ABCDef");
        assertThat(CaseUtils.toPascalCase("a b c @def")).isEqualTo("ABCDef");

        assertThat(CaseUtils.toPascalCase("-+@ ")).isEqualTo("");
        assertThat(CaseUtils.toPascalCase("  to-CAMEL-cASE")).isEqualTo("ToCamelCase");
        assertThat(CaseUtils.toPascalCase("@@@@   to+CAMEL@cASE ")).isEqualTo("ToCamelCase");
        assertThat(CaseUtils.toPascalCase("To+CA+ME L@cASE")).isEqualTo("ToCaMeLCase");

        assertThat(CaseUtils.toPascalCase("To.Camel.Case")).isEqualTo("ToCamelCase");
        assertThat(CaseUtils.toPascalCase("To.Camel-Case")).isEqualTo("ToCamelCase");
        assertThat(CaseUtils.toPascalCase(" to @ Camel case")).isEqualTo("ToCamelCase");
        assertThat(CaseUtils.toPascalCase(" @to @ Camel case_")).isEqualTo("ToCamelCase");

        assertThat(CaseUtils.toPascalCase("TO CAMEL CASE")).isEqualTo("ToCamelCase");
        assertThat(CaseUtils.toPascalCase("tocamelcase")).isEqualTo("Tocamelcase");

        assertThat(CaseUtils.toPascalCase("\uD800\uDF00\uD800\uDF01\uD800\uDF14\uD800\uDF02\uD800\uDF03"))
                .isEqualTo("");

        assertThat(CaseUtils.toPascalCase("The café\u2019s piñata gave me déjà vu."))
                .isEqualTo("TheCafesPinataGaveMeDejaVu");
        assertThat(CaseUtils.toPascalCase("\u1E70\u01EB \u010C\u0227\u1E41\u0113\u0142 \u010D\u1E01\u0219\u1E1B"))
                .isEqualTo("ToCamelCase");
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
    public void testSeparatedWordFormats() {
        assertThat(CaseUtils.toCamelCase("Çàmè\u0142 çásé")).isEqualTo("camelCase");
        assertThat(CaseUtils.toCamelCase("Çàmè\u0142 çásé", false, ' '))
                .isEqualTo("çàmè\u0142Çásé");
        assertThat(CaseUtils.toDelimitedCase("Camel snake", false, '_'))
                .isEqualTo("camel_Snake");
        assertThat(CaseUtils.toPascalCase("FLAT CASE").toLowerCase()).isEqualTo("flatcase");
        assertThat(CaseUtils.toKebabCase("Kebab Case")).isEqualTo("kebab-case");
        assertThat(CaseUtils.toPascalCase("pâsçã\u0142 çäsê")).isEqualTo("PascalCase");
        assertThat(CaseUtils.toCamelCase("pâsçã\u0142 çäsê", true, ' '))
                .isEqualTo("Pâsçã\u0142Çäsê");
        assertThat(CaseUtils.toPascalCase("screaming case").toUpperCase()).isEqualTo("SCREAMINGCASE");
        assertThat(CaseUtils.toDelimitedCase("screaming KEBAB-CASE", '-').toUpperCase())
                .isEqualTo("SCREAMING-KEBAB-CASE");
        assertThat(CaseUtils.toDelimitedCase("SCREAMING snake_case", '_').toUpperCase())
                .isEqualTo("SCREAMING_SNAKE_CASE");
        assertThat(CaseUtils.toDelimitedCase("Snake Case", '_').toLowerCase())
                .isEqualTo("snake_case");
        assertThat(CaseUtils.toDelimitedCase("title case", true, '_'))
                .isEqualTo("Title_Case");
        assertThat(CaseUtils.toDelimitedCase("train case", true, '-'))
                .isEqualTo("Train-Case");
    }

}
