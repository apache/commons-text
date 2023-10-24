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
    public void testCharacterDelimitedCase() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> KebabCase.INSTANCE.format(Arrays.asList("a", "-")));
        Assertions.assertThrows(IllegalArgumentException.class, () -> SnakeCase.INSTANCE.format(Arrays.asList("a", "_")));
        CharacterDelimitedCase nullDelimiters = new CharacterDelimitedCase();
        assertFormat(nullDelimiters, "abc", Arrays.asList("a", "b", "c"));
        assertParse(nullDelimiters, "abc", Arrays.asList("abc"));
    }

    @Test
    public void testKebabCase() {
        assertFormatAndParse(KebabCase.INSTANCE, "", Arrays.asList());
        assertParse(KebabCase.INSTANCE, null, Arrays.asList());
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
        assertParse(SnakeCase.INSTANCE, null, Arrays.asList());
        assertFormatAndParse(SnakeCase.INSTANCE, "my_Tokens_123_a1", Arrays.asList("my", "Tokens", "123", "a1"));
        assertFormatAndParse(SnakeCase.INSTANCE, "blank__token", Arrays.asList("blank", "", "token"));
    }

    @Test
    public void testPascalCase() {

        assertFormatAndParse(PascalCase.INSTANCE, "MyVarName", Arrays.asList("My", "Var", "Name"));
        assertFormatAndParse(PascalCase.INSTANCE, "MyTokensA1D", Arrays.asList("My", "Tokens", "A1", "D"));
        assertFormatAndParse(PascalCase.INSTANCE, "", Arrays.asList());
        assertParse(PascalCase.INSTANCE, "lowerFirst", Arrays.asList("lower", "First"));
        assertFormat(PascalCase.INSTANCE, "LowerFirst", Arrays.asList("lower", "First"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> PascalCase.INSTANCE.format(Arrays.asList("1")));
        Assertions.assertThrows(IllegalArgumentException.class, () -> PascalCase.INSTANCE.format(Arrays.asList("a1", "2c")));
        Assertions.assertThrows(IllegalArgumentException.class, () -> PascalCase.INSTANCE.format(Arrays.asList("1a")));
        Assertions.assertThrows(IllegalArgumentException.class, () -> PascalCase.INSTANCE.format(Arrays.asList("")));
    }

    @Test
    public void testNumberLetters() {

        // roman numerals - have an upper/lower case but are numbers

        assertFormatAndParse(PascalCase.INSTANCE, "A\u2170\u2160c", Arrays.asList("A\u2170", "\u2160c"));

        assertFormat(PascalCase.INSTANCE, "A\u2170Bc", Arrays.asList("a\u2160", "bc"));
        assertParse(PascalCase.INSTANCE, "A\u2170Bc", Arrays.asList("A\u2170", "Bc"));
        assertFormat(PascalCase.INSTANCE, "A\u2170", Arrays.asList("a\u2170"));
        assertParse(PascalCase.INSTANCE, "A\u2170Bc", Arrays.asList("A\u2170", "Bc"));

        assertFormat(CamelCase.INSTANCE, "a\u2170Bc", Arrays.asList("a\u2160", "bc"));
        assertParse(CamelCase.INSTANCE, "\u2160Bc", Arrays.asList("\u2160", "Bc"));

    }

    @Test
    public void testCamelCase() {

        assertFormatAndParse(CamelCase.INSTANCE, "", Arrays.asList());
        assertFormatAndParse(CamelCase.INSTANCE, "myTokensAbc123", Arrays.asList("my", "Tokens", "Abc123"));
        assertFormatAndParse(CamelCase.INSTANCE, "specChar-Token+", Arrays.asList("spec", "Char-", "Token+"));

        assertParse(CamelCase.INSTANCE, "MyTokens", Arrays.asList("My", "Tokens"));
        assertFormat(CamelCase.INSTANCE, "myTokens", Arrays.asList("My", "Tokens"));

        // empty token not supported
        Assertions.assertThrows(IllegalArgumentException.class, () -> CamelCase.INSTANCE.format(Arrays.asList("a", "b", "")));
        // must begin with character that can be uppercased
        Assertions.assertThrows(IllegalArgumentException.class, () -> CamelCase.INSTANCE.format(Arrays.asList("a", "1b")));
        Assertions.assertThrows(IllegalArgumentException.class, () -> CamelCase.INSTANCE.format(Arrays.asList("1a")));
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
    public void testUnicodeCases() {

        // LATIN SMALL LETTER SHARP S - lower case, no upper case
        Assertions.assertThrows(IllegalArgumentException.class, () -> CamelCase.INSTANCE.format(Arrays.asList("a", "\u00DFabc")));
        Assertions.assertThrows(IllegalArgumentException.class, () -> PascalCase.INSTANCE.format(Arrays.asList("\u00DFabc")));

        // LATIN CAPITAL LETTER L WITH SMALL LETTER J - title case, has upper and lower
        assertFormatAndParse(CamelCase.INSTANCE, "\u01CCbc", Arrays.asList("\u01CBbc"), true);
        assertFormatAndParse(CamelCase.INSTANCE, "a\u01CAbc", Arrays.asList("a", "\u01CBbc"), true);

        // GREEK CAPITAL LETTER ALPHA WITH PSILI AND PROSGEGRAMMENI - title case , no upper case
        assertFormatAndParse(PascalCase.INSTANCE, "A\u1f80", Arrays.asList("a\u1f88"), true);
        assertFormatAndParse(CamelCase.INSTANCE, "\u1f80", Arrays.asList("\u1f88"), true);
        Assertions.assertThrows(IllegalArgumentException.class, () -> PascalCase.INSTANCE.format(Arrays.asList("\u1f88")));

        //scan all titlecase characters
        for (int i = 0; i < Character.MAX_CODE_POINT; i++) {
            if (Character.isTitleCase(i)) {
                String codeString = new String(new int[] { i }, 0, 1);

                int upperCode = Character.toUpperCase(i);
                int lowerCode = Character.toLowerCase(i);

                //if upper exists, ensure it gets upper cased to it
                if (upperCode != i) {
                    String upperCodeString = new String(new int[] { upperCode }, 0, 1);
                    Assertions.assertEquals(PascalCase.INSTANCE.format(Arrays.asList(codeString + "bc")), upperCodeString + "bc");
                } else {
                    // if there is no uppercase value
                    Assertions.assertThrows(IllegalArgumentException.class, () -> PascalCase.INSTANCE.format(Arrays.asList(codeString)));
                }

                //if lower exists, ensure it gets lower cased to it
                if (lowerCode != i) {
                    String lowerCodeString = new String(new int[] { lowerCode }, 0, 1);
                    Assertions.assertEquals(CamelCase.INSTANCE.format(Arrays.asList(codeString + "bc")), lowerCodeString + "bc");
                } else {
                    Assertions.assertThrows(IllegalArgumentException.class, () -> CamelCase.INSTANCE.format(Arrays.asList("a" + codeString)));
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
        assertFormat(caseInstance, string, tokens, caseInsensitive);
        assertParse(caseInstance, string, tokens, caseInsensitive);
    }

    private void assertFormat(Case caseInstance, String string, List<String> tokens) {
        assertFormat(caseInstance, string, tokens, false);
    }

    private void assertFormat(Case caseInstance, String string, List<String> tokens, boolean caseInsensitive) {
        String formatted = caseInstance.format(tokens);
        if (caseInsensitive) {
            Assertions.assertEquals(string.toLowerCase(), formatted.toLowerCase());
        } else {
            Assertions.assertEquals(string, formatted);
        }
    }

    private void assertParse(Case caseInstance, String string, List<String> tokens) {
        assertParse(caseInstance, string, tokens, false);
    }

    /**
     * Asserts that string parses into the expected tokens, ignoring case if the caseInsensitive parameter is true
     */
    private void assertParse(Case caseInstance, String string, List<String> tokens, Boolean caseInsensitive) {
        List<String> parsedTokens = caseInstance.parse(string);
        if (caseInsensitive) {
            assertEqualsIgnoreCase(tokens, parsedTokens);
        } else {
            Assertions.assertEquals(tokens, parsedTokens);
        }
    }

    private void assertEqualsIgnoreCase(List<String> expected, List<String> actual) {
        Assertions.assertEquals(expected.size(), actual.size());
        Iterator<String> itAc = actual.iterator();
        for (Iterator<String> itEx = expected.iterator(); itEx.hasNext();) {
            Assertions.assertEquals(itEx.next().toLowerCase(), itAc.next().toLowerCase());
        }
    }

}
