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
package org.apache.commons.text.cases;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CasesTest {

    @Test
    public void testDelimiterCharacterException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> KebabCase.INSTANCE.format(Arrays.asList("a", "-")));
        Assertions.assertThrows(IllegalArgumentException.class, () -> SnakeCase.INSTANCE.format(Arrays.asList("a", "_")));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new DelimitedCase(null, ","));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new DelimitedCase(new char[1], null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new DelimitedCase(new char[0], ","));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new DelimitedCase(new char[0], ""));
    }

    @Test
    public void testKebabCase() {
        assertFormatAndParse(KebabCase.INSTANCE, "", Arrays.asList());
        assertFormatAndParse(KebabCase.INSTANCE, "my-Tokens-123-a1", Arrays.asList("my", "Tokens", "123", "a1"));
        assertFormatAndParse(KebabCase.INSTANCE, "blank--token", Arrays.asList("blank", "", "token"));
    }

    @Test
    public void testUtf32() {
        assertFormatAndParse(KebabCase.INSTANCE, "\uD800\uDF00-\uD800\uDF01\uD800\uDF14-\uD800\uDF02\uD800\uDF03",
                Arrays.asList("\uD800\uDF00", "\uD800\uDF01\uD800\uDF14", "\uD800\uDF02\uD800\uDF03"));
        assertFormatAndParse(SnakeCase.INSTANCE, "\uD800\uDF00_\uD800\uDF01\uD800\uDF14_\uD800\uDF02\uD800\uDF03",
                Arrays.asList("\uD800\uDF00", "\uD800\uDF01\uD800\uDF14", "\uD800\uDF02\uD800\uDF03"));
        assertFormatAndParse(PascalCase.INSTANCE, "A\uD800\uDF00B\uD800\uDF01\uD800\uDF14C\uD800\uDF02\uD800\uDF03",
                Arrays.asList("A\uD800\uDF00", "B\uD800\uDF01\uD800\uDF14", "C\uD800\uDF02\uD800\uDF03"));
        assertFormatAndParse(CamelCase.INSTANCE, "a\uD800\uDF00B\uD800\uDF01\uD800\uDF14C\uD800\uDF02\uD800\uDF03",
                Arrays.asList("a\uD800\uDF00", "B\uD800\uDF01\uD800\uDF14", "C\uD800\uDF02\uD800\uDF03"));
    }

    @Test
    public void testSnakeCase() {
        assertFormatAndParse(SnakeCase.INSTANCE, "", Arrays.asList());
        assertFormatAndParse(SnakeCase.INSTANCE, "my_Tokens_123_a1", Arrays.asList("my", "Tokens", "123", "a1"));
        assertFormatAndParse(SnakeCase.INSTANCE, "blank__token", Arrays.asList("blank", "", "token"));
    }

    @Test
    public void testPascalCase() {

        assertFormatAndParse(PascalCase.INSTANCE, "MyVarName", Arrays.asList("My", "Var", "Name"));
        assertFormatAndParse(PascalCase.INSTANCE, "MyTokensA1D", Arrays.asList("My", "Tokens", "A1", "D"));
        assertFormatAndParse(PascalCase.INSTANCE, "", Arrays.asList());

        // first character must be ASCII alpha upper
        Assertions.assertThrows(IllegalArgumentException.class, () -> PascalCase.INSTANCE.parse("lowerFirst"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> PascalCase.INSTANCE.format(Arrays.asList("1")));
        Assertions.assertThrows(IllegalArgumentException.class, () -> PascalCase.INSTANCE.format(Arrays.asList("")));
    }

    @Test
    public void testCamelCase() {

        assertFormatAndParse(CamelCase.INSTANCE, "", Arrays.asList());
        assertFormatAndParse(CamelCase.INSTANCE, "myTokensAbc123", Arrays.asList("my", "Tokens", "Abc123"));
        assertFormatAndParse(CamelCase.INSTANCE, "specChar-Token+", Arrays.asList("spec", "Char-", "Token+"));

        // empty token not supported
        Assertions.assertThrows(IllegalArgumentException.class, () -> CamelCase.INSTANCE.format(Arrays.asList("a", "b", "")));
        // must begin with ASCII alpha
        Assertions.assertThrows(IllegalArgumentException.class, () -> CamelCase.INSTANCE.format(Arrays.asList("a", "1b")));
        // must begin with ASCII alpha lower
        Assertions.assertThrows(IllegalArgumentException.class, () -> CamelCase.INSTANCE.parse("MyTokens"));
    }

    @Test
    public void testConversionsDelimited() {

        List<String> tokens = Arrays.asList("My", "var", "NAME", "mIXED", "a1", "12", "");

        String kebabString = "My-var-NAME-mIXED-a1-12-";
        assertFormatAndParse(KebabCase.INSTANCE, kebabString, tokens);

        String snakeString = "My_var_NAME_mIXED_a1_12_";
        assertFormatAndParse(SnakeCase.INSTANCE, snakeString, tokens);
    }

    @Test
    public void testConversions() {

        List<String> tokens = Arrays.asList("My", "var", "NAME", "mIXED", "a1", "c|=+");

        String kebabString = "My-var-NAME-mIXED-a1-c|=+";
        assertFormatAndParse(KebabCase.INSTANCE, kebabString, tokens);

        String snakeString = "My_var_NAME_mIXED_a1_c|=+";
        assertFormatAndParse(SnakeCase.INSTANCE, snakeString, tokens);

        String camelString = "myVarNameMixedA1C|=+";
        assertFormatAndParse(CamelCase.INSTANCE, camelString, tokens, true);

        String pascalString = "MyVarNameMixedA1C|=+";
        assertFormatAndParse(PascalCase.INSTANCE, pascalString, tokens, true);

    }

    @Test
    public void testEmptyTokens() {
        List<String> tokens = Arrays.asList("HAS", "", "empty", "Tokens", "");

        String snakeString = "HAS__empty_Tokens_";
        assertFormatAndParse(SnakeCase.INSTANCE, snakeString, tokens);

        String kebabString = "HAS--empty-Tokens-";
        assertFormatAndParse(KebabCase.INSTANCE, kebabString, tokens);

        Assertions.assertThrows(IllegalArgumentException.class, () -> PascalCase.INSTANCE.format(tokens));
        Assertions.assertThrows(IllegalArgumentException.class, () -> CamelCase.INSTANCE.format(tokens));
    }

    @Test
    public void testUnicodeUncaseableLetter() {

        // LATIN SMALL LETTER SHARP S
        Assertions.assertThrows(IllegalArgumentException.class, () -> CamelCase.INSTANCE.format(Arrays.asList("uncaseable", "\u00DFabc", "token")));
        Assertions.assertThrows(IllegalArgumentException.class, () -> PascalCase.INSTANCE.format(Arrays.asList("uncaseable", "\u00DFabc", "token")));

        // LATIN SMALL LETTER KRA
        Assertions.assertThrows(IllegalArgumentException.class, () -> CamelCase.INSTANCE.format(Arrays.asList("uncaseable", "\u0138abc", "token")));
        Assertions.assertThrows(IllegalArgumentException.class, () -> PascalCase.INSTANCE.format(Arrays.asList("uncaseable", "\u0138abc", "token")));

    }

    /**
     * This method iterates through all unicode characters and confirms: 1. If the character is
     * uppercase or lowercase, that it can be parsed successfully 2. If the character can be
     * converted to the opposite case, that this conversion can be parsed successfully 3. If the
     * opposite case character can be converted BACK to the initial character, that this conversion
     * can be parsed successfully
     */
    @Test
    public void testUnicodeCasing() {

        for (int i = 0; i < Character.MAX_CODE_POINT; i++) {

            // this if block weeds out titlecase characters
            if (Character.isLowerCase(i) || Character.isUpperCase(i)) {

                // if char is lowercase
                boolean lower = Character.isLowerCase(i);

                // find the lowercase code point, if it exists
                int lowerCode = lower ? i : Character.toLowerCase(i);
                if (!Character.isLowerCase(lowerCode)) {
                    lowerCode = i;
                }

                // find the uppercase code point, if it exists
                int upperCode = !lower ? i : Character.toUpperCase(i);
                if (!Character.isUpperCase(upperCode)) {
                    upperCode = i;
                }

                // if char has a valid conversion
                boolean canConvert = lowerCode != upperCode;

                // if opposite case converts back to original char
                // this is sometimes false when an uppercase character has a many-to-one
                // relationship with lower cases, and thus there is no single upper-to-lower
                // conversion that be selected
                boolean canReverse = false;
                if (canConvert) {
                    if (lower) {
                        canReverse = Character.toLowerCase(upperCode) == lowerCode;
                    } else {
                        canReverse = Character.toUpperCase(lowerCode) == upperCode;
                    }
                }

                String lowerCodeString = new String(new int[] { lowerCode }, 0, 1);
                String upperCodeString = new String(new int[] { upperCode }, 0, 1);

                // confirm the token can be handled by format and parse methods
                String camelCaseString = lower ? lowerCodeString : "a" + upperCodeString;
                List<String> camelTokens = lower ? Arrays.asList(lowerCodeString) : Arrays.asList("a", upperCodeString);
                assertFormatAndParse(CamelCase.INSTANCE, camelCaseString, camelTokens);

                String pascalCaseString = lower ? "A" + lowerCodeString : upperCodeString;
                List<String> pascalTokens = lower ? Arrays.asList("A" + lowerCodeString) : Arrays.asList(upperCodeString);
                assertFormatAndParse(PascalCase.INSTANCE, pascalCaseString, pascalTokens);

                if (canConvert) {

                    if (lower || canReverse) {
                        // confirm we can convert lower to upper or reverse
                        Assertions.assertEquals(upperCodeString, PascalCase.INSTANCE.format(Arrays.asList(lowerCodeString)));
                        Assertions.assertEquals("a" + upperCodeString, CamelCase.INSTANCE.format(Arrays.asList("a", lowerCodeString)));
                    }
                    if (!lower || canReverse) {
                        // confirm we can convert upper to lower or reverse
                        Assertions.assertEquals("A" + lowerCodeString, PascalCase.INSTANCE.format(Arrays.asList("A" + upperCodeString)));
                        Assertions.assertEquals(lowerCodeString, CamelCase.INSTANCE.format(Arrays.asList(upperCodeString)));
                    }

                }

            }

        }

    }

    private void assertFormatAndParse(Case caseInstance, String string, List<String> tokens) {
        assertFormatAndParse(caseInstance, string, tokens, false);
    }

    /**
     * Test Util method for ensuring that a case instance parses and formats the expected string and tokens
     * to one another
     *
     * @param case Instance the case instance to use
     * @param string the expected formatted string
     * @param tokens the expected tokens
     * @param caseInsensitive whether to not to validate tokens case insensitively
     */
    private void assertFormatAndParse(Case caseInstance, String string, List<String> tokens, Boolean caseInsensitive) {
        List<String> parsedTokens = caseInstance.parse(string);
        if (caseInsensitive) {
            assertEqualsIgnoreCase(tokens, parsedTokens);
        } else {
            Assertions.assertEquals(tokens, parsedTokens);
        }
        String formatted = caseInstance.format(tokens);
        Assertions.assertEquals(string, formatted);
    }

    private void assertEqualsIgnoreCase(List<String> expected, List<String> actual) {
        Assertions.assertEquals(expected.size(), actual.size());
        Iterator<String> itAc = actual.iterator();
        for (Iterator<String> itEx = expected.iterator(); itEx.hasNext();) {
            Assertions.assertEquals(itEx.next().toLowerCase(), itAc.next().toLowerCase());
        }
    }

}
