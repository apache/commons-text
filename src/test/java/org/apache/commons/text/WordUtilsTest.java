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
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@link WordUtils} class.
 */
public class WordUtilsTest {

    private final static String systemNewLine = System.lineSeparator();

    @Test
    public void testAbbreviateForLowerThanMinusOneValues() {
        assertThatIllegalArgumentException().isThrownBy(() -> assertThat(WordUtils.abbreviate("01 23 45 67 89", 9, -10, null)).isEqualTo("01 23 45 67"));
    }

     @Test
    public void testAbbreviateForLowerValue() {
        assertThat(WordUtils.abbreviate("012 3456789", 0, 5, null)).isEqualTo("012");
        assertThat(WordUtils.abbreviate("01234 56789", 5, 10, null)).isEqualTo("01234");
        assertThat(WordUtils.abbreviate("01 23 45 67 89", 9, -1, null)).isEqualTo("01 23 45 67");
        assertThat(WordUtils.abbreviate("01 23 45 67 89", 9, 10, null)).isEqualTo("01 23 45 6");
        assertThat(WordUtils.abbreviate("0123456789", 15, 20, null)).isEqualTo("0123456789");
    }

     @Test
    public void testAbbreviateForLowerValueAndAppendedString() {
        assertThat(WordUtils.abbreviate("012 3456789", 0, 5, null)).isEqualTo("012");
        assertThat(WordUtils.abbreviate("01234 56789", 5, 10, "-")).isEqualTo("01234-");
        assertThat(WordUtils.abbreviate("01 23 45 67 89", 9, -1, "abc")).isEqualTo("01 23 45 67abc");
        assertThat(WordUtils.abbreviate("01 23 45 67 89", 9, 10, "")).isEqualTo("01 23 45 6");
    }

     @Test
    public void testAbbreviateForNullAndEmptyString() {
        assertThat(WordUtils.abbreviate(null, 1, -1, "")).isNull();
        assertThat(WordUtils.abbreviate("", 1, -1, "")).isEqualTo(StringUtils.EMPTY);
        assertThat(WordUtils.abbreviate("0123456790", 0, 0, "")).isEqualTo("");
        assertThat(WordUtils.abbreviate(" 0123456790", 0, -1, "")).isEqualTo("");
    }

     @Test
    public void testAbbreviateForUpperLimit() {
        assertThat(WordUtils.abbreviate("0123456789", 0, 5, "")).isEqualTo("01234");
        assertThat(WordUtils.abbreviate("012 3456789", 2, 5, "")).isEqualTo("012");
        assertThat(WordUtils.abbreviate("0123456789", 0, -1, "")).isEqualTo("0123456789");
    }

     @Test
    public void testAbbreviateForUpperLimitAndAppendedString() {
        assertThat(WordUtils.abbreviate("0123456789", 0, 5, "-")).isEqualTo("01234-");
        assertThat(WordUtils.abbreviate("012 3456789", 2, 5, null)).isEqualTo("012");
        assertThat(WordUtils.abbreviate("0123456789", 0, -1, "")).isEqualTo("0123456789");
    }

    @Test
    public void testAbbreviateUpperLessThanLowerValues() {
        assertThatIllegalArgumentException().isThrownBy(() -> assertThat(WordUtils.abbreviate("0123456789", 5, 2, "")).isEqualTo("01234"));
    }

     @Test
    public void testCapitalize_String() {
        assertThat(WordUtils.capitalize(null)).isNull();
        assertThat(WordUtils.capitalize("")).isEqualTo("");
        assertThat(WordUtils.capitalize("  ")).isEqualTo("  ");

        assertThat(WordUtils.capitalize("I")).isEqualTo("I");
        assertThat(WordUtils.capitalize("i")).isEqualTo("I");
        assertThat(WordUtils.capitalize("i am here 123")).isEqualTo("I Am Here 123");
        assertThat(WordUtils.capitalize("I Am Here 123")).isEqualTo("I Am Here 123");
        assertThat(WordUtils.capitalize("i am HERE 123")).isEqualTo("I Am HERE 123");
        assertThat(WordUtils.capitalize("I AM HERE 123")).isEqualTo("I AM HERE 123");
    }

    @Test
    public void testCapitalizeFully_String() {
        assertThat(WordUtils.capitalizeFully(null)).isNull();
        assertThat(WordUtils.capitalizeFully("")).isEqualTo("");
        assertThat(WordUtils.capitalizeFully("  ")).isEqualTo("  ");

        assertThat(WordUtils.capitalizeFully("I")).isEqualTo("I");
        assertThat(WordUtils.capitalizeFully("i")).isEqualTo("I");
        assertThat(WordUtils.capitalizeFully("i am here 123")).isEqualTo("I Am Here 123");
        assertThat(WordUtils.capitalizeFully("I Am Here 123")).isEqualTo("I Am Here 123");
        assertThat(WordUtils.capitalizeFully("i am HERE 123")).isEqualTo("I Am Here 123");
        assertThat(WordUtils.capitalizeFully("I AM HERE 123")).isEqualTo("I Am Here 123");
        assertThat(WordUtils.capitalizeFully("alphabet")).isEqualTo("Alphabet"); // single word
    }

    @Test
    public void testCapitalizeFully_Text88() {
        assertThat(WordUtils.capitalizeFully("i am fine now", new char[] {})).isEqualTo("I am fine now");
    }

    @Test
    public void testCapitalizeFullyWithDelimiters_String() {
        assertThat(WordUtils.capitalizeFully(null, null)).isNull();
        assertThat(WordUtils.capitalizeFully("", ArrayUtils.EMPTY_CHAR_ARRAY)).isEqualTo("");
        assertThat(WordUtils.capitalizeFully("  ", ArrayUtils.EMPTY_CHAR_ARRAY)).isEqualTo("  ");

        char[] chars = {'-', '+', ' ', '@'};
        assertThat(WordUtils.capitalizeFully("I", chars)).isEqualTo("I");
        assertThat(WordUtils.capitalizeFully("i", chars)).isEqualTo("I");
        assertThat(WordUtils.capitalizeFully("i-am here+123", chars)).isEqualTo("I-Am Here+123");
        assertThat(WordUtils.capitalizeFully("I Am+Here-123", chars)).isEqualTo("I Am+Here-123");
        assertThat(WordUtils.capitalizeFully("i+am-HERE 123", chars)).isEqualTo("I+Am-Here 123");
        assertThat(WordUtils.capitalizeFully("I-AM HERE+123", chars)).isEqualTo("I-Am Here+123");
        chars = new char[] {'.'};
        assertThat(WordUtils.capitalizeFully("i aM.fine", chars)).isEqualTo("I am.Fine");
        assertThat(WordUtils.capitalizeFully("i am.fine", null)).isEqualTo("I Am.fine");
        assertThat(WordUtils.capitalizeFully("alphabet", null)).isEqualTo("Alphabet"); // single word
        assertThat(WordUtils.capitalizeFully("alphabet", new char[] {'!'})).isEqualTo("Alphabet"); // no matching delim
    }

    @Test
    public void testCapitalizeWithDelimiters_String() {
        assertThat(WordUtils.capitalize(null, null)).isNull();
        assertThat(WordUtils.capitalize("", ArrayUtils.EMPTY_CHAR_ARRAY)).isEqualTo("");
        assertThat(WordUtils.capitalize("  ", ArrayUtils.EMPTY_CHAR_ARRAY)).isEqualTo("  ");

        char[] chars = {'-', '+', ' ', '@'};
        assertThat(WordUtils.capitalize("I", chars)).isEqualTo("I");
        assertThat(WordUtils.capitalize("i", chars)).isEqualTo("I");
        assertThat(WordUtils.capitalize("i-am here+123", chars)).isEqualTo("I-Am Here+123");
        assertThat(WordUtils.capitalize("I Am+Here-123", chars)).isEqualTo("I Am+Here-123");
        assertThat(WordUtils.capitalize("i+am-HERE 123", chars)).isEqualTo("I+Am-HERE 123");
        assertThat(WordUtils.capitalize("I-AM HERE+123", chars)).isEqualTo("I-AM HERE+123");
        chars = new char[] {'.'};
        assertThat(WordUtils.capitalize("i aM.fine", chars)).isEqualTo("I aM.Fine");
        assertThat(WordUtils.capitalize("i am.fine", null)).isEqualTo("I Am.fine");
    }

     @Test
    public void testConstructor() {
        assertThat(new WordUtils()).isNotNull();
        final Constructor<?>[] cons = WordUtils.class.getDeclaredConstructors();
        assertThat(cons.length).isEqualTo(1);
        assertThat(Modifier.isPublic(cons[0].getModifiers())).isTrue();
        assertThat(Modifier.isPublic(WordUtils.class.getModifiers())).isTrue();
        assertThat(Modifier.isFinal(WordUtils.class.getModifiers())).isFalse();
    }

    @Test
    public void testContainsAllWords_StringString() {
        assertThat(WordUtils.containsAllWords(null, (String) null)).isFalse();
        assertThat(WordUtils.containsAllWords(null, "")).isFalse();
        assertThat(WordUtils.containsAllWords(null, "ab")).isFalse();

        assertThat(WordUtils.containsAllWords("", (String) null)).isFalse();
        assertThat(WordUtils.containsAllWords("", "")).isFalse();
        assertThat(WordUtils.containsAllWords("", "ab")).isFalse();

        assertThat(WordUtils.containsAllWords("foo", (String) null)).isFalse();
        assertThat(WordUtils.containsAllWords("bar", "")).isFalse();
        assertThat(WordUtils.containsAllWords("zzabyycdxx", "by")).isFalse();
        assertThat(WordUtils.containsAllWords("lorem ipsum dolor sit amet", "ipsum", "lorem", "dolor")).isTrue();
        assertThat(WordUtils.containsAllWords("lorem ipsum dolor sit amet", "ipsum", null, "lorem", "dolor")).isFalse();
        assertThat(WordUtils.containsAllWords("lorem ipsum null dolor sit amet", "ipsum", null, "lorem", "dolor"))
            .isFalse();
        assertThat(WordUtils.containsAllWords("ab", "b")).isFalse();
        assertThat(WordUtils.containsAllWords("ab", "z")).isFalse();
    }

    @Test
    public void testContainsAllWordsWithNull() {
        assertThat(WordUtils.containsAllWords("M", (CharSequence) null)).isFalse();
    }

     @Test
    public void testInitials_String() {
        assertThat(WordUtils.initials(null)).isNull();
        assertThat(WordUtils.initials("")).isEqualTo("");
        assertThat(WordUtils.initials("  ")).isEqualTo("");

        assertThat(WordUtils.initials("I")).isEqualTo("I");
        assertThat(WordUtils.initials("i")).isEqualTo("i");
        assertThat(WordUtils.initials("Ben John Lee")).isEqualTo("BJL");
        assertThat(WordUtils.initials("   Ben \n   John\tLee\t")).isEqualTo("BJL");
        assertThat(WordUtils.initials("Ben J.Lee")).isEqualTo("BJ");
        assertThat(WordUtils.initials(" Ben   John  . Lee")).isEqualTo("BJ.L");
        assertThat(WordUtils.initials("i am here 123")).isEqualTo("iah1");
    }

     @Test
    public void testInitials_String_charArray() {
        char[] array = null;
        assertThat(WordUtils.initials(null, array)).isNull();
        assertThat(WordUtils.initials("", array)).isEqualTo("");
        assertThat(WordUtils.initials("  ", array)).isEqualTo("");
        assertThat(WordUtils.initials("I", array)).isEqualTo("I");
        assertThat(WordUtils.initials("i", array)).isEqualTo("i");
        assertThat(WordUtils.initials("SJC", array)).isEqualTo("S");
        assertThat(WordUtils.initials("Ben John Lee", array)).isEqualTo("BJL");
        assertThat(WordUtils.initials("   Ben \n   John\tLee\t", array)).isEqualTo("BJL");
        assertThat(WordUtils.initials("Ben J.Lee", array)).isEqualTo("BJ");
        assertThat(WordUtils.initials(" Ben   John  . Lee", array)).isEqualTo("BJ.L");
        assertThat(WordUtils.initials("Kay O'Murphy", array)).isEqualTo("KO");
        assertThat(WordUtils.initials("i am here 123", array)).isEqualTo("iah1");

        array = ArrayUtils.EMPTY_CHAR_ARRAY;
        assertThat(WordUtils.initials(null, array)).isNull();
        assertThat(WordUtils.initials("", array)).isEqualTo("");
        assertThat(WordUtils.initials("  ", array)).isEqualTo("");
        assertThat(WordUtils.initials("I", array)).isEqualTo("");
        assertThat(WordUtils.initials("i", array)).isEqualTo("");
        assertThat(WordUtils.initials("SJC", array)).isEqualTo("");
        assertThat(WordUtils.initials("Ben John Lee", array)).isEqualTo("");
        assertThat(WordUtils.initials("   Ben \n   John\tLee\t", array)).isEqualTo("");
        assertThat(WordUtils.initials("Ben J.Lee", array)).isEqualTo("");
        assertThat(WordUtils.initials(" Ben   John  . Lee", array)).isEqualTo("");
        assertThat(WordUtils.initials("Kay O'Murphy", array)).isEqualTo("");
        assertThat(WordUtils.initials("i am here 123", array)).isEqualTo("");

        array = " ".toCharArray();
        assertThat(WordUtils.initials(null, array)).isNull();
        assertThat(WordUtils.initials("", array)).isEqualTo("");
        assertThat(WordUtils.initials("  ", array)).isEqualTo("");
        assertThat(WordUtils.initials("I", array)).isEqualTo("I");
        assertThat(WordUtils.initials("i", array)).isEqualTo("i");
        assertThat(WordUtils.initials("SJC", array)).isEqualTo("S");
        assertThat(WordUtils.initials("Ben John Lee", array)).isEqualTo("BJL");
        assertThat(WordUtils.initials("Ben J.Lee", array)).isEqualTo("BJ");
        assertThat(WordUtils.initials("   Ben \n   John\tLee\t", array)).isEqualTo("B\nJ");
        assertThat(WordUtils.initials(" Ben   John  . Lee", array)).isEqualTo("BJ.L");
        assertThat(WordUtils.initials("Kay O'Murphy", array)).isEqualTo("KO");
        assertThat(WordUtils.initials("i am here 123", array)).isEqualTo("iah1");

        array = " .".toCharArray();
        assertThat(WordUtils.initials(null, array)).isNull();
        assertThat(WordUtils.initials("", array)).isEqualTo("");
        assertThat(WordUtils.initials("  ", array)).isEqualTo("");
        assertThat(WordUtils.initials("I", array)).isEqualTo("I");
        assertThat(WordUtils.initials("i", array)).isEqualTo("i");
        assertThat(WordUtils.initials("SJC", array)).isEqualTo("S");
        assertThat(WordUtils.initials("Ben John Lee", array)).isEqualTo("BJL");
        assertThat(WordUtils.initials("Ben J.Lee", array)).isEqualTo("BJL");
        assertThat(WordUtils.initials(" Ben   John  . Lee", array)).isEqualTo("BJL");
        assertThat(WordUtils.initials("Kay O'Murphy", array)).isEqualTo("KO");
        assertThat(WordUtils.initials("i am here 123", array)).isEqualTo("iah1");

        array = " .'".toCharArray();
        assertThat(WordUtils.initials(null, array)).isNull();
        assertThat(WordUtils.initials("", array)).isEqualTo("");
        assertThat(WordUtils.initials("  ", array)).isEqualTo("");
        assertThat(WordUtils.initials("I", array)).isEqualTo("I");
        assertThat(WordUtils.initials("i", array)).isEqualTo("i");
        assertThat(WordUtils.initials("SJC", array)).isEqualTo("S");
        assertThat(WordUtils.initials("Ben John Lee", array)).isEqualTo("BJL");
        assertThat(WordUtils.initials("Ben J.Lee", array)).isEqualTo("BJL");
        assertThat(WordUtils.initials(" Ben   John  . Lee", array)).isEqualTo("BJL");
        assertThat(WordUtils.initials("Kay O'Murphy", array)).isEqualTo("KOM");
        assertThat(WordUtils.initials("i am here 123", array)).isEqualTo("iah1");

        array = "SIJo1".toCharArray();
        assertThat(WordUtils.initials(null, array)).isNull();
        assertThat(WordUtils.initials("", array)).isEqualTo("");
        assertThat(WordUtils.initials("  ", array)).isEqualTo(" ");
        assertThat(WordUtils.initials("I", array)).isEqualTo("");
        assertThat(WordUtils.initials("i", array)).isEqualTo("i");
        assertThat(WordUtils.initials("SJC", array)).isEqualTo("C");
        assertThat(WordUtils.initials("Ben John Lee", array)).isEqualTo("Bh");
        assertThat(WordUtils.initials("Ben J.Lee", array)).isEqualTo("B.");
        assertThat(WordUtils.initials(" Ben   John  . Lee", array)).isEqualTo(" h");
        assertThat(WordUtils.initials("Kay O'Murphy", array)).isEqualTo("K");
        assertThat(WordUtils.initials("i am here 123", array)).isEqualTo("i2");
    }

    @Test
    public void testInitialsSurrogatePairs() {
        // Tests with space as default delimiter
        assertThat(WordUtils.initials("\uD800\uDF00\uD800\uDF01 \uD800\uDF02\uD800\uDF03"))
            .isEqualTo("\uD800\uDF00\uD800\uDF02");
        assertThat(WordUtils.initials("\uD800\uDF00\uD800\uDF01 \uD800\uDF02\uD800\uDF03", null))
            .isEqualTo("\uD800\uDF00\uD800\uDF02");
        assertThat(WordUtils.initials("\uD800\uDF00 \uD800\uDF02 ", null)).isEqualTo("\uD800\uDF00\uD800\uDF02");

        // Tests with UTF-16 as delimiters
        assertThat(WordUtils.initials("\uD800\uDF00\uD800\uDF01.\uD800\uDF02\uD800\uDF03", new char[] {'.'}))
            .isEqualTo("\uD800\uDF00\uD800\uDF02");
        assertThat(WordUtils.initials("\uD800\uDF00\uD800\uDF01A\uD800\uDF02\uD800\uDF03", new char[] {'A'}))
            .isEqualTo("\uD800\uDF00\uD800\uDF02");

        // Tests with UTF-32 as delimiters
        assertThat(WordUtils.initials(
                "\uD800\uDF00\uD800\uDF01\uD800\uDF14\uD800\uDF02\uD800\uDF03", new char[] {'\uD800', '\uDF14'}))
            .isEqualTo("\uD800\uDF00\uD800\uDF02");
        assertThat(WordUtils.initials("\uD800\uDF00\uD800\uDF01\uD800\uDF14\uD800\uDF18\uD800\uDF02\uD800\uDF03",
                        new char[] {'\uD800', '\uDF14', '\uD800', '\uDF18'}))
            .isEqualTo("\uD800\uDF00\uD800\uDF02");
    }

    @Test
    public void testLANG1292() {
        // Prior to fix, this was throwing StringIndexOutOfBoundsException
        WordUtils.wrap("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa "
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa "
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 70);
    }

    @Test
    public void testLANG673() {
        assertThat(WordUtils.abbreviate("01 23 45 67 89", 0, 40, "")).isEqualTo("01");
        assertThat(WordUtils.abbreviate("01 23 45 67 89", 10, 40, "")).isEqualTo("01 23 45 67");
        assertThat(WordUtils.abbreviate("01 23 45 67 89", 40, 40, "")).isEqualTo("01 23 45 67 89");
    }

     @Test
    public void testSwapCase_String() {
        assertThat(WordUtils.swapCase(null)).isNull();
        assertThat(WordUtils.swapCase("")).isEqualTo("");
        assertThat(WordUtils.swapCase("  ")).isEqualTo("  ");

        assertThat(WordUtils.swapCase("I")).isEqualTo("i");
        assertThat(WordUtils.swapCase("i")).isEqualTo("I");
        assertThat(WordUtils.swapCase("i am here 123")).isEqualTo("I AM HERE 123");
        assertThat(WordUtils.swapCase("I Am Here 123")).isEqualTo("i aM hERE 123");
        assertThat(WordUtils.swapCase("i am HERE 123")).isEqualTo("I AM here 123");
        assertThat(WordUtils.swapCase("I AM HERE 123")).isEqualTo("i am here 123");

        final String test = "This String contains a TitleCase character: \u01C8";
        final String expect = "tHIS sTRING CONTAINS A tITLEcASE CHARACTER: \u01C9";
        assertThat(WordUtils.swapCase(test)).isEqualTo(expect);
    }

    @Test
    public void testText123() throws Exception {
        // Prior to fix, this was throwing StringIndexOutOfBoundsException
        WordUtils.wrap("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa "
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", Integer.MAX_VALUE);
    }

    @Test
    public void testUncapitalize_String() {
        assertThat(WordUtils.uncapitalize(null)).isNull();
        assertThat(WordUtils.uncapitalize("")).isEqualTo("");
        assertThat(WordUtils.uncapitalize("  ")).isEqualTo("  ");

        assertThat(WordUtils.uncapitalize("I")).isEqualTo("i");
        assertThat(WordUtils.uncapitalize("i")).isEqualTo("i");
        assertThat(WordUtils.uncapitalize("i am here 123")).isEqualTo("i am here 123");
        assertThat(WordUtils.uncapitalize("I Am Here 123")).isEqualTo("i am here 123");
        assertThat(WordUtils.uncapitalize("i am HERE 123")).isEqualTo("i am hERE 123");
        assertThat(WordUtils.uncapitalize("I AM HERE 123")).isEqualTo("i aM hERE 123");
    }

    @Test
    public void testUnCapitalize_Text88() {
        assertThat(WordUtils.uncapitalize("I am fine now", new char[] {})).isEqualTo("i am fine now");
    }

    @Test
    public void testUncapitalizeWithDelimiters_String() {
        assertThat(WordUtils.uncapitalize(null, null)).isNull();
        assertThat(WordUtils.uncapitalize("", ArrayUtils.EMPTY_CHAR_ARRAY)).isEqualTo("");
        assertThat(WordUtils.uncapitalize("  ", ArrayUtils.EMPTY_CHAR_ARRAY)).isEqualTo("  ");

        char[] chars = {'-', '+', ' ', '@'};
        assertThat(WordUtils.uncapitalize("I", chars)).isEqualTo("i");
        assertThat(WordUtils.uncapitalize("i", chars)).isEqualTo("i");
        assertThat(WordUtils.uncapitalize("i am-here+123", chars)).isEqualTo("i am-here+123");
        assertThat(WordUtils.uncapitalize("I+Am Here-123", chars)).isEqualTo("i+am here-123");
        assertThat(WordUtils.uncapitalize("i-am+HERE 123", chars)).isEqualTo("i-am+hERE 123");
        assertThat(WordUtils.uncapitalize("I AM-HERE+123", chars)).isEqualTo("i aM-hERE+123");
        chars = new char[] {'.'};
        assertThat(WordUtils.uncapitalize("I AM.FINE", chars)).isEqualTo("i AM.fINE");
        assertThat(WordUtils.uncapitalize("I AM.FINE", null)).isEqualTo("i aM.FINE");
    }

    @DisplayName("Wrap, null and empty strings")
    @Test
    public void testWrap_NullAndEmpty() {
        assertThat(WordUtils.wrap(null, 20)).isNull();
        assertThat(WordUtils.wrap(null, -1)).isNull();
        assertThat(WordUtils.wrap("", 20)).isEqualTo("");
        assertThat(WordUtils.wrap("", -1)).isEqualTo("");

        assertThat(WordUtils.wrap(null, 20, "\n", false)).isNull();
        assertThat(WordUtils.wrap(null, 20, "\n", true)).isNull();
        assertThat(WordUtils.wrap(null, 20, null, true)).isNull();
        assertThat(WordUtils.wrap(null, 20, null, false)).isNull();
        assertThat(WordUtils.wrap(null, -1, null, true)).isNull();
        assertThat(WordUtils.wrap(null, -1, null, false)).isNull();
 
        assertThat(WordUtils.wrap("", 20, "\n", false)).isEqualTo("");
        assertThat(WordUtils.wrap("", 20, "\n", true)).isEqualTo("");
        assertThat(WordUtils.wrap("", 20, null, false)).isEqualTo("");
        assertThat(WordUtils.wrap("", 20, null, true)).isEqualTo("");
        assertThat(WordUtils.wrap("", -1, null, false)).isEqualTo("");
        assertThat(WordUtils.wrap("", -1, null, true)).isEqualTo("");
    }

    @DisplayName("Wrap, typical use cases, length argument")
    @ParameterizedTest(name = "{0}")
    @MethodSource("provideDataForTypicalUseCasesLengthArgument")
    public void testWrap_TypicalUseCasesLengthArgument(String name, String input, int lineLength, String expected) {
        String actual = WordUtils.wrap(input, lineLength);
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideDataForTypicalUseCasesLengthArgument() {
        return Stream.of(
            Arguments.of(
                "Normal",
                "Here is one line of text that is going to be wrapped after 20 columns.", 20,
                "Here is one line of" + systemNewLine + "text that is going" + systemNewLine +
                "to be wrapped after" + systemNewLine + "20 columns."),
            Arguments.of(
                "Long word at end",
                "Click here to jump to the commons website - https://commons.apache.org", 20,
                "Click here to jump" + systemNewLine + "to the commons" + systemNewLine + "website -" + systemNewLine
                + "https://commons.apache.org"),
            Arguments.of(
                "Long word in the middle",
                "Click here, https://commons.apache.org, to jump to the commons website", 20,
                "Click here," + systemNewLine + "https://commons.apache.org," + systemNewLine + "to jump to the"
                        + systemNewLine + "commons website"),
            Arguments.of(
                "Strip leading, keep trailing",
                "word1             word2                        word3", 7,
                "word1  " + systemNewLine + "word2  " + systemNewLine + "word3")
        );
    }

    @DisplayName("Wrap, typical use cases, default word wrap")
    @ParameterizedTest(name = "{0}")
    @MethodSource("provideDataForTypicalUseCasesDefaultWordWrap")
    public void testWrap_TypicalUseCasesDefaultWordWrap(String name, String input, int lineLength, String newLineString, boolean breakLongWords, String expected) {
        String actual = WordUtils.wrap(input, lineLength, newLineString, breakLongWords);
        assertThat(actual).isEqualTo(expected);
    }

   private static Stream<Arguments> provideDataForTypicalUseCasesDefaultWordWrap() {
       return Stream.of(
            Arguments.of(
                "Normal",
                "Here is one line of text that is going to be wrapped after 20 columns.",
                20, null, false,
                "Here is one line of" + systemNewLine + "text that is going" + systemNewLine +
                "to be wrapped after" + systemNewLine + "20 columns."),

            Arguments.of("Custom newline string, preserve long words",
                "Here is one line of text that is going to be wrapped after 20 columns.",
                20, "<br />", false,
                "Here is one line of<br />text that is going<br />to be wrapped after<br />20 columns."),

            Arguments.of("Short line, length 6", "Here is one line", 6, "\n", false, "Here\nis one\nline"),
            Arguments.of("Short line, length 2", "Here is one line", 2, "\n", false, "Here\nis\none\nline"),
            Arguments.of("Short line, length -1", "Here is one line", -1, "\n", false, "Here\nis\none\nline"),

            Arguments.of("With extra spaces, preserve long words",
                " Here:  is  one  line  of  text  that  is  going  to  be  wrapped  after  20  columns.",
                20, "\n", false,
                "Here:  is  one  line\nof  text  that  is \ngoing  to  be \nwrapped  after  20 \ncolumns."),
            Arguments.of("With extra spaces, break long words",
                " Here:  is  one  line  of  text  that  is  going  to  be  wrapped  after  20  columns.",
                20, "\n", true,
                "Here:  is  one  line\nof  text  that  is \ngoing  to  be \nwrapped  after  20 \ncolumns."),

            Arguments.of("With tabs, preserve long words",
                "Here is\tone line of text that is going to be wrapped after 20 columns.",
                20, "\n", false,
                "Here is\tone line of\ntext that is going\nto be wrapped after\n20 columns."),
            Arguments.of("With tabs, break long words",
                "Here is\tone line of text that is going to be wrapped after 20 columns.",
                20, "\n", true,
                "Here is\tone line of\ntext that is going\nto be wrapped after\n20 columns."),

            Arguments.of("With tab at wrapColumn, preserve long words",
                "Here is one line of\ttext that is going to be wrapped after 20 columns.",
                20, "\n", false,
                "Here is one line\nof\ttext that is\ngoing to be wrapped\nafter 20 columns."),
            Arguments.of("With tab at wrapColumn, break long words",
                "Here is one line of\ttext that is going to be wrapped after 20 columns.",
                20, "\n", true,
                "Here is one line\nof\ttext that is\ngoing to be wrapped\nafter 20 columns."),

            Arguments.of("Difference because of long word, preserve long words",
                "Click here to jump to the commons website - https://commons.apache.org",
                20, "\n", false,
                "Click here to jump\nto the commons\nwebsite -\nhttps://commons.apache.org"),
            Arguments.of("Difference because of long word, break long words",
                "Click here to jump to the commons website - https://commons.apache.org",
                20, "\n", true,
                "Click here to jump\nto the commons\nwebsite -\nhttps://commons.apac\nhe.org"),

            Arguments.of("Difference because of long word in middle, preserve long words",
                "Click here, https://commons.apache.org, to jump to the commons website",
                20, "\n", false,
                "Click here,\nhttps://commons.apache.org,\nto jump to the\ncommons website"),
            Arguments.of("Difference because of long word in middle, break long words",
                "Click here, https://commons.apache.org, to jump to the commons website",
                20, "\n", true,
                "Click here,\nhttps://commons.apac\nhe.org, to jump to\nthe commons website")

        );
    }

    @DisplayName("Wrap, typical use cases, custom word wrap")
    @ParameterizedTest(name = "{0}")
    @MethodSource("provideDataForTypicalUseCasesCustomWordWrap")
    public void testWrap_TypicalUseCasesCustomWordWrap(String name, String input, int lineLength, String newLineString, boolean breakLongWords, String wordWrap, String expected) {
        String actual = WordUtils.wrap(input, lineLength, newLineString, breakLongWords, wordWrap);
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideDataForTypicalUseCasesCustomWordWrap() {
        // The word wrap pattern is either a whitespace character that is stripped, or a hyphen that is kept
        // Also note that \n is both the newline string — which must force a newline — and one of the acceptable wrap characters   
        return Stream.of(
            Arguments.of("Difference because of long word in middle, preserve long words",
                "This\nhere is a\ntest of word-wrap where hyphens and whitespace-characters act as potential line-breaks along\nthe new-line character",
                9, "\n", false, "(?<=-)|\\s",
                "This\nhere is a\ntest of\nword-wrap\nwhere\nhyphens\nand\nwhitespace-\ncharacters\nact as\npotential\nline-\nbreaks\nalong\nthe new-\nline\ncharacter"),
            Arguments.of("Difference because of long word in middle, break long words",
                "This\nhere is a\ntest of word-wrap where hyphens and whitespace-characters act as potential line-breaks along\nthe new-line character",
                9, "\n", true, "(?<=-)|\\s",
                "This\nhere is a\ntest of\nword-wrap\nwhere\nhyphens\nand\nwhitespac\ne-\ncharacter\ns act as\npotential\nline-\nbreaks\nalong\nthe new-\nline\ncharacter")
        );
    }

    @DisplayName("Wrap, with break in the middle")
    @ParameterizedTest(name = "{0}")
    @MethodSource("provideDataForBreakInTheMiddle")
    public void testWrap_BreakInTheMiddle(String name, String input, int lineLength, String newLineString, boolean breakLongWords, String wrapOn, String expected) {
        String actual = WordUtils.wrap(input, lineLength, newLineString, breakLongWords, wrapOn);
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideDataForBreakInTheMiddle() {
        return Stream.of(
            Arguments.of(
                "No changes", "flammable/inflammable", 30, "\n", false, null, "flammable/inflammable"),
            Arguments.of(
                "Wrap on / and small width", "flammable/inflammable", 2, "\n", false, "/", "flammable\ninflammable"),
            Arguments.of(
                "Wrap long words on / 1", "flammable/inflammable", 9, "\n", true, "/", "flammable\ninflammab\nle"),
            Arguments.of(
                "Wrap long words on / 2", "flammable/inflammable", 15, "\n", true, "/", "flammable\ninflammable"),
            Arguments.of(
                "Wrap long words on / 3", "flammableinflammable", 15, "\n", true, "/", "flammableinflam\nmable")
            );
    }

    @DisplayName("Wrap, with wrap at start, middle, end")
    @ParameterizedTest(name = "{0}")
    @MethodSource("provideDataForWrapAtStartMiddleEnd")
    public void testWrap_WrapAtStartMiddleEnd(String name, String input, int lineLength, String newLineString, boolean breakLongWords, String wrapOn, String expected) {
        String actual = WordUtils.wrap(input, lineLength, newLineString, breakLongWords, wrapOn);
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideDataForWrapAtStartMiddleEnd() {
        return Stream.of(
            Arguments.of(
                "Wrap at middle twice, zero length match, preserve long words",
                "abcdef--abcdef", 3, "\n", false, "(?=-)", "abcdef\n-\n-abcdef"
            ),
            Arguments.of(
                "Wrap at middle twice, zero length match, break long words",
                "abcdef--abcdef", 3, "\n", true, "(?=-)", "abc\ndef\n-\n-ab\ncde\nf"
            ),
            Arguments.of(
                "Wrap at middle twice, one length match, preserve long words",
                "abcdef--abcdef", 3, "\n", false, "-", "abcdef\nabcdef"
            ),
            Arguments.of(
                "Wrap at middle twice, one length match, break long words",
                "abcdef--abcdef", 3, "\n", true, "-", "abc\ndef\nabc\ndef"
            ),
            Arguments.of(
                "Wrap at start and end, zero length match, preserve long words",
                "-abcdefabcdef-", 2, "\n", false, "(?=-)", "-abcdefabcdef\n-"
            ),
            Arguments.of(
                "Wrap at start and end, zero length match, break long words",
                "-abcdefabcdef-", 2, "\n", false, "(?<=-)", "-\nabcdefabcdef-"
            ),
            Arguments.of(
                "Wrap at start and end, one length match, preserve long words",
                "-abcdefabcdef-", 2, "\n", false, "-", "abcdefabcdef"
            ),
            Arguments.of(
                "Wrap at start and end, one length match, break long words",
                "-abcdefabcdef-", 2, "\n", true, "-", "ab\ncd\nef\nab\ncd\nef"
            )
        );
    }

    @DisplayName("Wrap, zero length regular expression")
    @ParameterizedTest(name = "{0}")
    @MethodSource("provideDataForZeroLengthRegEx")
    public void testWrap_ZeroLengthRegEx(String name, String input, int lineLength, String newLineString, boolean breakLongWords, String wrapOn, String expected) {
        String actual = WordUtils.wrap(input, lineLength, newLineString, breakLongWords, wrapOn);
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideDataForZeroLengthRegEx() {
        return Stream.of(
            Arguments.of("Single, 1", "abc-def", 2, "\n", false, "(?=-)", "abc\n-def"),
            Arguments.of("Single, 2", "abc-def", 2, "\n", false, "(?<=-)", "abc-\ndef"),
            Arguments.of("Single, 3", "abc-def", 2, "\n", true, "(?=-)", "ab\nc\n-d\nef"),
            Arguments.of("Single, 4", "abc-def", 2, "\n", true, "(?<=-)", "ab\nc-\nde\nf"),
            Arguments.of("Multiple, 1", "abc-deabc-de", 2, "\n", false, "(?=-)", "abc\n-deabc\n-de"),
            Arguments.of("Multiple, 2", "abc-deabc-de", 2, "\n", false, "(?<=-)", "abc-\ndeabc-\nde"),
            Arguments.of("Multiple, 3", "abc-deab-cde", 3, "\n", true , "(?=-)", "abc\n-de\nab\n-cd\ne"),
            Arguments.of("Multiple, 4", "abc-deab-cde", 3, "\n", true , "(?<=-)", "abc\n-\ndea\nb-\ncde")
        );
   }

}
