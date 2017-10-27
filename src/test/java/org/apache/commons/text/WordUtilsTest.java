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

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * Unit tests for {@link WordUtils} class.
 */
public class WordUtilsTest {

    // -----------------------------------------------------------------------
    @Test
    public void testConstructor() {
        assertThat(new WordUtils()).isNotNull();
        final Constructor<?>[] cons = WordUtils.class.getDeclaredConstructors();
        assertThat(cons.length).isEqualTo(1);
        assertThat(Modifier.isPublic(cons[0].getModifiers())).isTrue();
        assertThat(Modifier.isPublic(WordUtils.class.getModifiers())).isTrue();
        assertThat(Modifier.isFinal(WordUtils.class.getModifiers())).isFalse();
    }

    // -----------------------------------------------------------------------
    @Test
    public void testWrap_StringInt() {
        assertThat(WordUtils.wrap(null, 20)).isNull();
        assertThat(WordUtils.wrap(null, -1)).isNull();

        assertThat(WordUtils.wrap("", 20)).isEqualTo("");
        assertThat(WordUtils.wrap("", -1)).isEqualTo("");

        // normal
        final String systemNewLine = System.lineSeparator();
        String input = "Here is one line of text that is going to be wrapped after 20 columns.";
        String expected = "Here is one line of" + systemNewLine + "text that is going" + systemNewLine
                + "to be wrapped after" + systemNewLine + "20 columns.";
        assertThat(WordUtils.wrap(input, 20)).isEqualTo(expected);

        // long word at end
        input = "Click here to jump to the commons website - http://commons.apache.org";
        expected = "Click here to jump" + systemNewLine + "to the commons" + systemNewLine + "website -" + systemNewLine
                + "http://commons.apache.org";
        assertThat(WordUtils.wrap(input, 20)).isEqualTo(expected);

        // long word in middle
        input = "Click here, http://commons.apache.org, to jump to the commons website";
        expected = "Click here," + systemNewLine + "http://commons.apache.org," + systemNewLine + "to jump to the"
                + systemNewLine + "commons website";
        assertThat(WordUtils.wrap(input, 20)).isEqualTo(expected);

        // leading spaces on a new line are stripped
        // trailing spaces are not stripped
        input = "word1             word2                        word3";
        expected = "word1  " + systemNewLine + "word2  " + systemNewLine + "word3";
        assertThat(WordUtils.wrap(input, 7)).isEqualTo(expected);
    }

    @Test
    public void testWrap_StringIntStringBoolean() {
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

        // normal
        String input = "Here is one line of text that is going to be wrapped after 20 columns.";
        String expected = "Here is one line of\ntext that is going\nto be wrapped after\n20 columns.";
        assertThat(WordUtils.wrap(input, 20, "\n", false)).isEqualTo(expected);
        assertThat(WordUtils.wrap(input, 20, "\n", true)).isEqualTo(expected);

        // unusual newline char
        input = "Here is one line of text that is going to be wrapped after 20 columns.";
        expected = "Here is one line of<br />text that is going<br />to be wrapped after<br />20 columns.";
        assertThat(WordUtils.wrap(input, 20, "<br />", false)).isEqualTo(expected);
        assertThat(WordUtils.wrap(input, 20, "<br />", true)).isEqualTo(expected);

        // short line length
        input = "Here is one line";
        expected = "Here\nis one\nline";
        assertThat(WordUtils.wrap(input, 6, "\n", false)).isEqualTo(expected);
        expected = "Here\nis\none\nline";
        assertThat(WordUtils.wrap(input, 2, "\n", false)).isEqualTo(expected);
        assertThat(WordUtils.wrap(input, -1, "\n", false)).isEqualTo(expected);

        // system newline char
        final String systemNewLine = System.lineSeparator();
        input = "Here is one line of text that is going to be wrapped after 20 columns.";
        expected = "Here is one line of" + systemNewLine + "text that is going" + systemNewLine + "to be wrapped after"
                + systemNewLine + "20 columns.";
        assertThat(WordUtils.wrap(input, 20, null, false)).isEqualTo(expected);
        assertThat(WordUtils.wrap(input, 20, null, true)).isEqualTo(expected);

        // with extra spaces
        input = " Here:  is  one  line  of  text  that  is  going  to  be  wrapped  after  20  columns.";
        expected = "Here:  is  one  line\nof  text  that  is \ngoing  to  be \nwrapped  after  20 \ncolumns.";
        assertThat(WordUtils.wrap(input, 20, "\n", false)).isEqualTo(expected);
        assertThat(WordUtils.wrap(input, 20, "\n", true)).isEqualTo(expected);

        // with tab
        input = "Here is\tone line of text that is going to be wrapped after 20 columns.";
        expected = "Here is\tone line of\ntext that is going\nto be wrapped after\n20 columns.";
        assertThat(WordUtils.wrap(input, 20, "\n", false)).isEqualTo(expected);
        assertThat(WordUtils.wrap(input, 20, "\n", true)).isEqualTo(expected);

        // with tab at wrapColumn
        input = "Here is one line of\ttext that is going to be wrapped after 20 columns.";
        expected = "Here is one line\nof\ttext that is\ngoing to be wrapped\nafter 20 columns.";
        assertThat(WordUtils.wrap(input, 20, "\n", false)).isEqualTo(expected);
        assertThat(WordUtils.wrap(input, 20, "\n", true)).isEqualTo(expected);

        // difference because of long word
        input = "Click here to jump to the commons website - http://commons.apache.org";
        expected = "Click here to jump\nto the commons\nwebsite -\nhttp://commons.apache.org";
        assertThat(WordUtils.wrap(input, 20, "\n", false)).isEqualTo(expected);
        expected = "Click here to jump\nto the commons\nwebsite -\nhttp://commons.apach\ne.org";
        assertThat(WordUtils.wrap(input, 20, "\n", true)).isEqualTo(expected);

        // difference because of long word in middle
        input = "Click here, http://commons.apache.org, to jump to the commons website";
        expected = "Click here,\nhttp://commons.apache.org,\nto jump to the\ncommons website";
        assertThat(WordUtils.wrap(input, 20, "\n", false)).isEqualTo(expected);
        expected = "Click here,\nhttp://commons.apach\ne.org, to jump to\nthe commons website";
        assertThat(WordUtils.wrap(input, 20, "\n", true)).isEqualTo(expected);
    }

    @Test
    public void testWrap_StringIntStringBooleanString() {

        // no changes test
        String input = "flammable/inflammable";
        String expected = "flammable/inflammable";
        assertThat(WordUtils.wrap(input, 30, "\n", false, "/")).isEqualTo(expected);

        // wrap on / and small width
        expected = "flammable\ninflammable";
        assertThat(WordUtils.wrap(input, 2, "\n", false, "/")).isEqualTo(expected);

        // wrap long words on / 1
        expected = "flammable\ninflammab\nle";
        assertThat(WordUtils.wrap(input, 9, "\n", true, "/")).isEqualTo(expected);

        // wrap long words on / 2
        expected = "flammable\ninflammable";
        assertThat(WordUtils.wrap(input, 15, "\n", true, "/")).isEqualTo(expected);

        // wrap long words on / 3
        input = "flammableinflammable";
        expected = "flammableinflam\nmable";
        assertThat(WordUtils.wrap(input, 15, "\n", true, "/")).isEqualTo(expected);
    }

    // -----------------------------------------------------------------------
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
    public void testCapitalizeWithDelimiters_String() {
        assertThat(WordUtils.capitalize(null, null)).isNull();
        assertThat(WordUtils.capitalize("", new char[0])).isEqualTo("");
        assertThat(WordUtils.capitalize("  ", new char[0])).isEqualTo("  ");

        char[] chars = new char[] {'-', '+', ' ', '@'};
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
    public void testUnCapitalize_Text88() {
        assertThat(WordUtils.uncapitalize("I am fine now", new char[] {})).isEqualTo("i am fine now");
    }

    @Test
    public void testCapitalizeFullyWithDelimiters_String() {
        assertThat(WordUtils.capitalizeFully(null, null)).isNull();
        assertThat(WordUtils.capitalizeFully("", new char[0])).isEqualTo("");
        assertThat(WordUtils.capitalizeFully("  ", new char[0])).isEqualTo("  ");

        char[] chars = new char[] {'-', '+', ' ', '@'};
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
    public void testUncapitalizeWithDelimiters_String() {
        assertThat(WordUtils.uncapitalize(null, null)).isNull();
        assertThat(WordUtils.uncapitalize("", new char[0])).isEqualTo("");
        assertThat(WordUtils.uncapitalize("  ", new char[0])).isEqualTo("  ");

        char[] chars = new char[] {'-', '+', ' ', '@'};
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

    // -----------------------------------------------------------------------
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

    // -----------------------------------------------------------------------
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

        array = new char[0];
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

    // -----------------------------------------------------------------------
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

    // -----------------------------------------------------------------------
    @Test
    public void testAbbreviateForNullAndEmptyString() {
        assertThat((WordUtils.abbreviate(null, 1, -1, ""))).isNull();
        assertThat(WordUtils.abbreviate("", 1, -1, "")).isEqualTo(StringUtils.EMPTY);
        assertThat(WordUtils.abbreviate("0123456790", 0, 0, "")).isEqualTo("");
        assertThat(WordUtils.abbreviate(" 0123456790", 0, -1, "")).isEqualTo("");
    }

    // -----------------------------------------------------------------------
    @Test
    public void testAbbreviateForUpperLimit() {
        assertThat(WordUtils.abbreviate("0123456789", 0, 5, "")).isEqualTo("01234");
        assertThat(WordUtils.abbreviate("012 3456789", 2, 5, "")).isEqualTo("012");
        assertThat(WordUtils.abbreviate("0123456789", 0, -1, "")).isEqualTo("0123456789");
    }

    // -----------------------------------------------------------------------
    @Test
    public void testAbbreviateForUpperLimitAndAppendedString() {
        assertThat(WordUtils.abbreviate("0123456789", 0, 5, "-")).isEqualTo("01234-");
        assertThat(WordUtils.abbreviate("012 3456789", 2, 5, null)).isEqualTo("012");
        assertThat(WordUtils.abbreviate("0123456789", 0, -1, "")).isEqualTo("0123456789");
    }

    // -----------------------------------------------------------------------
    @Test
    public void testAbbreviateForLowerValue() {
        assertThat(WordUtils.abbreviate("012 3456789", 0, 5, null)).isEqualTo("012");
        assertThat(WordUtils.abbreviate("01234 56789", 5, 10, null)).isEqualTo("01234");
        assertThat(WordUtils.abbreviate("01 23 45 67 89", 9, -1, null)).isEqualTo("01 23 45 67");
        assertThat(WordUtils.abbreviate("01 23 45 67 89", 9, 10, null)).isEqualTo("01 23 45 6");
        assertThat(WordUtils.abbreviate("0123456789", 15, 20, null)).isEqualTo("0123456789");
    }

    // -----------------------------------------------------------------------
    @Test
    public void testAbbreviateForLowerValueAndAppendedString() {
        assertThat(WordUtils.abbreviate("012 3456789", 0, 5, null)).isEqualTo("012");
        assertThat(WordUtils.abbreviate("01234 56789", 5, 10, "-")).isEqualTo("01234-");
        assertThat(WordUtils.abbreviate("01 23 45 67 89", 9, -1, "abc")).isEqualTo("01 23 45 67abc");
        assertThat(WordUtils.abbreviate("01 23 45 67 89", 9, 10, "")).isEqualTo("01 23 45 6");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbbreviateForLowerThanMinusOneValues() {
        assertThat(WordUtils.abbreviate("01 23 45 67 89", 9, -10, null)).isEqualTo("01 23 45 67");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbbreviateUpperLessThanLowerValues() {
        assertThat(WordUtils.abbreviate("0123456789", 5, 2, "")).isEqualTo("01234");
    }

    @Test
    public void testLANG673() throws Exception {
        assertThat(WordUtils.abbreviate("01 23 45 67 89", 0, 40, "")).isEqualTo("01");
        assertThat(WordUtils.abbreviate("01 23 45 67 89", 10, 40, "")).isEqualTo("01 23 45 67");
        assertThat(WordUtils.abbreviate("01 23 45 67 89", 40, 40, "")).isEqualTo("01 23 45 67 89");
    }

    @Test
    public void testLANG1292() throws Exception {
        // Prior to fix, this was throwing StringIndexOutOfBoundsException
        WordUtils.wrap("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa "
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa "
                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 70);
    }

    @Test
    public void testContainsAllWordsWithNull() {
        assertThat(WordUtils.containsAllWords("M", null)).isFalse();
    }

}
