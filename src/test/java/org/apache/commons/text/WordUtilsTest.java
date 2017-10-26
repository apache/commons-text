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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        assertNotNull(new WordUtils());
        final Constructor<?>[] cons = WordUtils.class.getDeclaredConstructors();
        assertEquals(1, cons.length);
        assertTrue(Modifier.isPublic(cons[0].getModifiers()));
        assertTrue(Modifier.isPublic(WordUtils.class.getModifiers()));
        assertFalse(Modifier.isFinal(WordUtils.class.getModifiers()));
    }

    // -----------------------------------------------------------------------
    @Test
    public void testWrap_StringInt() {
        assertNull(WordUtils.wrap(null, 20));
        assertNull(WordUtils.wrap(null, -1));

        assertEquals("", WordUtils.wrap("", 20));
        assertEquals("", WordUtils.wrap("", -1));

        // normal
        final String systemNewLine = System.lineSeparator();
        String input = "Here is one line of text that is going to be wrapped after 20 columns.";
        String expected = "Here is one line of" + systemNewLine + "text that is going" + systemNewLine
                + "to be wrapped after" + systemNewLine + "20 columns.";
        assertEquals(expected, WordUtils.wrap(input, 20));

        // long word at end
        input = "Click here to jump to the commons website - http://commons.apache.org";
        expected = "Click here to jump" + systemNewLine + "to the commons" + systemNewLine + "website -" + systemNewLine
                + "http://commons.apache.org";
        assertEquals(expected, WordUtils.wrap(input, 20));

        // long word in middle
        input = "Click here, http://commons.apache.org, to jump to the commons website";
        expected = "Click here," + systemNewLine + "http://commons.apache.org," + systemNewLine + "to jump to the"
                + systemNewLine + "commons website";
        assertEquals(expected, WordUtils.wrap(input, 20));

        // leading spaces on a new line are stripped
        // trailing spaces are not stripped
        input = "word1             word2                        word3";
        expected = "word1  " + systemNewLine + "word2  " + systemNewLine + "word3";
        assertEquals(expected, WordUtils.wrap(input, 7));
    }

    @Test
    public void testWrap_StringIntStringBoolean() {
        assertNull(WordUtils.wrap(null, 20, "\n", false));
        assertNull(WordUtils.wrap(null, 20, "\n", true));
        assertNull(WordUtils.wrap(null, 20, null, true));
        assertNull(WordUtils.wrap(null, 20, null, false));
        assertNull(WordUtils.wrap(null, -1, null, true));
        assertNull(WordUtils.wrap(null, -1, null, false));

        assertEquals("", WordUtils.wrap("", 20, "\n", false));
        assertEquals("", WordUtils.wrap("", 20, "\n", true));
        assertEquals("", WordUtils.wrap("", 20, null, false));
        assertEquals("", WordUtils.wrap("", 20, null, true));
        assertEquals("", WordUtils.wrap("", -1, null, false));
        assertEquals("", WordUtils.wrap("", -1, null, true));

        // normal
        String input = "Here is one line of text that is going to be wrapped after 20 columns.";
        String expected = "Here is one line of\ntext that is going\nto be wrapped after\n20 columns.";
        assertEquals(expected, WordUtils.wrap(input, 20, "\n", false));
        assertEquals(expected, WordUtils.wrap(input, 20, "\n", true));

        // unusual newline char
        input = "Here is one line of text that is going to be wrapped after 20 columns.";
        expected = "Here is one line of<br />text that is going<br />to be wrapped after<br />20 columns.";
        assertEquals(expected, WordUtils.wrap(input, 20, "<br />", false));
        assertEquals(expected, WordUtils.wrap(input, 20, "<br />", true));

        // short line length
        input = "Here is one line";
        expected = "Here\nis one\nline";
        assertEquals(expected, WordUtils.wrap(input, 6, "\n", false));
        expected = "Here\nis\none\nline";
        assertEquals(expected, WordUtils.wrap(input, 2, "\n", false));
        assertEquals(expected, WordUtils.wrap(input, -1, "\n", false));

        // system newline char
        final String systemNewLine = System.lineSeparator();
        input = "Here is one line of text that is going to be wrapped after 20 columns.";
        expected = "Here is one line of" + systemNewLine + "text that is going" + systemNewLine + "to be wrapped after"
                + systemNewLine + "20 columns.";
        assertEquals(expected, WordUtils.wrap(input, 20, null, false));
        assertEquals(expected, WordUtils.wrap(input, 20, null, true));

        // with extra spaces
        input = " Here:  is  one  line  of  text  that  is  going  to  be  wrapped  after  20  columns.";
        expected = "Here:  is  one  line\nof  text  that  is \ngoing  to  be \nwrapped  after  20 \ncolumns.";
        assertEquals(expected, WordUtils.wrap(input, 20, "\n", false));
        assertEquals(expected, WordUtils.wrap(input, 20, "\n", true));

        // with tab
        input = "Here is\tone line of text that is going to be wrapped after 20 columns.";
        expected = "Here is\tone line of\ntext that is going\nto be wrapped after\n20 columns.";
        assertEquals(expected, WordUtils.wrap(input, 20, "\n", false));
        assertEquals(expected, WordUtils.wrap(input, 20, "\n", true));

        // with tab at wrapColumn
        input = "Here is one line of\ttext that is going to be wrapped after 20 columns.";
        expected = "Here is one line\nof\ttext that is\ngoing to be wrapped\nafter 20 columns.";
        assertEquals(expected, WordUtils.wrap(input, 20, "\n", false));
        assertEquals(expected, WordUtils.wrap(input, 20, "\n", true));

        // difference because of long word
        input = "Click here to jump to the commons website - http://commons.apache.org";
        expected = "Click here to jump\nto the commons\nwebsite -\nhttp://commons.apache.org";
        assertEquals(expected, WordUtils.wrap(input, 20, "\n", false));
        expected = "Click here to jump\nto the commons\nwebsite -\nhttp://commons.apach\ne.org";
        assertEquals(expected, WordUtils.wrap(input, 20, "\n", true));

        // difference because of long word in middle
        input = "Click here, http://commons.apache.org, to jump to the commons website";
        expected = "Click here,\nhttp://commons.apache.org,\nto jump to the\ncommons website";
        assertEquals(expected, WordUtils.wrap(input, 20, "\n", false));
        expected = "Click here,\nhttp://commons.apach\ne.org, to jump to\nthe commons website";
        assertEquals(expected, WordUtils.wrap(input, 20, "\n", true));
    }

    @Test
    public void testWrap_StringIntStringBooleanString() {

        // no changes test
        String input = "flammable/inflammable";
        String expected = "flammable/inflammable";
        assertEquals(expected, WordUtils.wrap(input, 30, "\n", false, "/"));

        // wrap on / and small width
        expected = "flammable\ninflammable";
        assertEquals(expected, WordUtils.wrap(input, 2, "\n", false, "/"));

        // wrap long words on / 1
        expected = "flammable\ninflammab\nle";
        assertEquals(expected, WordUtils.wrap(input, 9, "\n", true, "/"));

        // wrap long words on / 2
        expected = "flammable\ninflammable";
        assertEquals(expected, WordUtils.wrap(input, 15, "\n", true, "/"));

        // wrap long words on / 3
        input = "flammableinflammable";
        expected = "flammableinflam\nmable";
        assertEquals(expected, WordUtils.wrap(input, 15, "\n", true, "/"));
    }

    // -----------------------------------------------------------------------
    @Test
    public void testCapitalize_String() {
        assertNull(WordUtils.capitalize(null));
        assertEquals("", WordUtils.capitalize(""));
        assertEquals("  ", WordUtils.capitalize("  "));

        assertEquals("I", WordUtils.capitalize("I"));
        assertEquals("I", WordUtils.capitalize("i"));
        assertEquals("I Am Here 123", WordUtils.capitalize("i am here 123"));
        assertEquals("I Am Here 123", WordUtils.capitalize("I Am Here 123"));
        assertEquals("I Am HERE 123", WordUtils.capitalize("i am HERE 123"));
        assertEquals("I AM HERE 123", WordUtils.capitalize("I AM HERE 123"));
    }

    @Test
    public void testCapitalizeWithDelimiters_String() {
        assertNull(WordUtils.capitalize(null, null));
        assertEquals("", WordUtils.capitalize("", new char[0]));
        assertEquals("  ", WordUtils.capitalize("  ", new char[0]));

        char[] chars = new char[] {'-', '+', ' ', '@'};
        assertEquals("I", WordUtils.capitalize("I", chars));
        assertEquals("I", WordUtils.capitalize("i", chars));
        assertEquals("I-Am Here+123", WordUtils.capitalize("i-am here+123", chars));
        assertEquals("I Am+Here-123", WordUtils.capitalize("I Am+Here-123", chars));
        assertEquals("I+Am-HERE 123", WordUtils.capitalize("i+am-HERE 123", chars));
        assertEquals("I-AM HERE+123", WordUtils.capitalize("I-AM HERE+123", chars));
        chars = new char[] {'.'};
        assertEquals("I aM.Fine", WordUtils.capitalize("i aM.fine", chars));
        assertEquals("I Am.fine", WordUtils.capitalize("i am.fine", null));
    }

    @Test
    public void testCapitalizeFully_String() {
        assertNull(WordUtils.capitalizeFully(null));
        assertEquals("", WordUtils.capitalizeFully(""));
        assertEquals("  ", WordUtils.capitalizeFully("  "));

        assertEquals("I", WordUtils.capitalizeFully("I"));
        assertEquals("I", WordUtils.capitalizeFully("i"));
        assertEquals("I Am Here 123", WordUtils.capitalizeFully("i am here 123"));
        assertEquals("I Am Here 123", WordUtils.capitalizeFully("I Am Here 123"));
        assertEquals("I Am Here 123", WordUtils.capitalizeFully("i am HERE 123"));
        assertEquals("I Am Here 123", WordUtils.capitalizeFully("I AM HERE 123"));
        assertEquals("Alphabet", WordUtils.capitalizeFully("alphabet")); // single word
    }

    @Test
    public void testCapitalizeFully_Text88() {
        assertEquals("I am fine now", WordUtils.capitalizeFully("i am fine now", new char[] {}));
    }

    @Test
    public void testUnCapitalize_Text88() {
        assertEquals("i am fine now", WordUtils.uncapitalize("I am fine now", new char[] {}));
    }

    @Test
    public void testCapitalizeFullyWithDelimiters_String() {
        assertNull(WordUtils.capitalizeFully(null, null));
        assertEquals("", WordUtils.capitalizeFully("", new char[0]));
        assertEquals("  ", WordUtils.capitalizeFully("  ", new char[0]));

        char[] chars = new char[] {'-', '+', ' ', '@'};
        assertEquals("I", WordUtils.capitalizeFully("I", chars));
        assertEquals("I", WordUtils.capitalizeFully("i", chars));
        assertEquals("I-Am Here+123", WordUtils.capitalizeFully("i-am here+123", chars));
        assertEquals("I Am+Here-123", WordUtils.capitalizeFully("I Am+Here-123", chars));
        assertEquals("I+Am-Here 123", WordUtils.capitalizeFully("i+am-HERE 123", chars));
        assertEquals("I-Am Here+123", WordUtils.capitalizeFully("I-AM HERE+123", chars));
        chars = new char[] {'.'};
        assertEquals("I am.Fine", WordUtils.capitalizeFully("i aM.fine", chars));
        assertEquals("I Am.fine", WordUtils.capitalizeFully("i am.fine", null));
        assertEquals("Alphabet", WordUtils.capitalizeFully("alphabet", null)); // single word
        assertEquals("Alphabet", WordUtils.capitalizeFully("alphabet", new char[] {'!'})); // no matching delim
    }

    @Test
    public void testContainsAllWords_StringString() {
        assertFalse(WordUtils.containsAllWords(null, (String) null));
        assertFalse(WordUtils.containsAllWords(null, ""));
        assertFalse(WordUtils.containsAllWords(null, "ab"));

        assertFalse(WordUtils.containsAllWords("", (String) null));
        assertFalse(WordUtils.containsAllWords("", ""));
        assertFalse(WordUtils.containsAllWords("", "ab"));

        assertFalse(WordUtils.containsAllWords("foo", (String) null));
        assertFalse(WordUtils.containsAllWords("bar", ""));
        assertFalse(WordUtils.containsAllWords("zzabyycdxx", "by"));
        assertTrue(WordUtils.containsAllWords("lorem ipsum dolor sit amet", "ipsum", "lorem", "dolor"));
        assertFalse(WordUtils.containsAllWords("lorem ipsum dolor sit amet", "ipsum", null, "lorem", "dolor"));
        assertFalse(WordUtils.containsAllWords("lorem ipsum null dolor sit amet", "ipsum", null, "lorem", "dolor"));
        assertFalse(WordUtils.containsAllWords("ab", "b"));
        assertFalse(WordUtils.containsAllWords("ab", "z"));
    }

    @Test
    public void testUncapitalize_String() {
        assertNull(WordUtils.uncapitalize(null));
        assertEquals("", WordUtils.uncapitalize(""));
        assertEquals("  ", WordUtils.uncapitalize("  "));

        assertEquals("i", WordUtils.uncapitalize("I"));
        assertEquals("i", WordUtils.uncapitalize("i"));
        assertEquals("i am here 123", WordUtils.uncapitalize("i am here 123"));
        assertEquals("i am here 123", WordUtils.uncapitalize("I Am Here 123"));
        assertEquals("i am hERE 123", WordUtils.uncapitalize("i am HERE 123"));
        assertEquals("i aM hERE 123", WordUtils.uncapitalize("I AM HERE 123"));
    }

    @Test
    public void testUncapitalizeWithDelimiters_String() {
        assertNull(WordUtils.uncapitalize(null, null));
        assertEquals("", WordUtils.uncapitalize("", new char[0]));
        assertEquals("  ", WordUtils.uncapitalize("  ", new char[0]));

        char[] chars = new char[] {'-', '+', ' ', '@'};
        assertEquals("i", WordUtils.uncapitalize("I", chars));
        assertEquals("i", WordUtils.uncapitalize("i", chars));
        assertEquals("i am-here+123", WordUtils.uncapitalize("i am-here+123", chars));
        assertEquals("i+am here-123", WordUtils.uncapitalize("I+Am Here-123", chars));
        assertEquals("i-am+hERE 123", WordUtils.uncapitalize("i-am+HERE 123", chars));
        assertEquals("i aM-hERE+123", WordUtils.uncapitalize("I AM-HERE+123", chars));
        chars = new char[] {'.'};
        assertEquals("i AM.fINE", WordUtils.uncapitalize("I AM.FINE", chars));
        assertEquals("i aM.FINE", WordUtils.uncapitalize("I AM.FINE", null));
    }

    // -----------------------------------------------------------------------
    @Test
    public void testInitials_String() {
        assertNull(WordUtils.initials(null));
        assertEquals("", WordUtils.initials(""));
        assertEquals("", WordUtils.initials("  "));

        assertEquals("I", WordUtils.initials("I"));
        assertEquals("i", WordUtils.initials("i"));
        assertEquals("BJL", WordUtils.initials("Ben John Lee"));
        assertEquals("BJL", WordUtils.initials("   Ben \n   John\tLee\t"));
        assertEquals("BJ", WordUtils.initials("Ben J.Lee"));
        assertEquals("BJ.L", WordUtils.initials(" Ben   John  . Lee"));
        assertEquals("iah1", WordUtils.initials("i am here 123"));
    }

    // -----------------------------------------------------------------------
    @Test
    public void testInitials_String_charArray() {
        char[] array = null;
        assertNull(WordUtils.initials(null, array));
        assertEquals("", WordUtils.initials("", array));
        assertEquals("", WordUtils.initials("  ", array));
        assertEquals("I", WordUtils.initials("I", array));
        assertEquals("i", WordUtils.initials("i", array));
        assertEquals("S", WordUtils.initials("SJC", array));
        assertEquals("BJL", WordUtils.initials("Ben John Lee", array));
        assertEquals("BJL", WordUtils.initials("   Ben \n   John\tLee\t", array));
        assertEquals("BJ", WordUtils.initials("Ben J.Lee", array));
        assertEquals("BJ.L", WordUtils.initials(" Ben   John  . Lee", array));
        assertEquals("KO", WordUtils.initials("Kay O'Murphy", array));
        assertEquals("iah1", WordUtils.initials("i am here 123", array));

        array = new char[0];
        assertNull(WordUtils.initials(null, array));
        assertEquals("", WordUtils.initials("", array));
        assertEquals("", WordUtils.initials("  ", array));
        assertEquals("", WordUtils.initials("I", array));
        assertEquals("", WordUtils.initials("i", array));
        assertEquals("", WordUtils.initials("SJC", array));
        assertEquals("", WordUtils.initials("Ben John Lee", array));
        assertEquals("", WordUtils.initials("   Ben \n   John\tLee\t", array));
        assertEquals("", WordUtils.initials("Ben J.Lee", array));
        assertEquals("", WordUtils.initials(" Ben   John  . Lee", array));
        assertEquals("", WordUtils.initials("Kay O'Murphy", array));
        assertEquals("", WordUtils.initials("i am here 123", array));

        array = " ".toCharArray();
        assertNull(WordUtils.initials(null, array));
        assertEquals("", WordUtils.initials("", array));
        assertEquals("", WordUtils.initials("  ", array));
        assertEquals("I", WordUtils.initials("I", array));
        assertEquals("i", WordUtils.initials("i", array));
        assertEquals("S", WordUtils.initials("SJC", array));
        assertEquals("BJL", WordUtils.initials("Ben John Lee", array));
        assertEquals("BJ", WordUtils.initials("Ben J.Lee", array));
        assertEquals("B\nJ", WordUtils.initials("   Ben \n   John\tLee\t", array));
        assertEquals("BJ.L", WordUtils.initials(" Ben   John  . Lee", array));
        assertEquals("KO", WordUtils.initials("Kay O'Murphy", array));
        assertEquals("iah1", WordUtils.initials("i am here 123", array));

        array = " .".toCharArray();
        assertNull(WordUtils.initials(null, array));
        assertEquals("", WordUtils.initials("", array));
        assertEquals("", WordUtils.initials("  ", array));
        assertEquals("I", WordUtils.initials("I", array));
        assertEquals("i", WordUtils.initials("i", array));
        assertEquals("S", WordUtils.initials("SJC", array));
        assertEquals("BJL", WordUtils.initials("Ben John Lee", array));
        assertEquals("BJL", WordUtils.initials("Ben J.Lee", array));
        assertEquals("BJL", WordUtils.initials(" Ben   John  . Lee", array));
        assertEquals("KO", WordUtils.initials("Kay O'Murphy", array));
        assertEquals("iah1", WordUtils.initials("i am here 123", array));

        array = " .'".toCharArray();
        assertNull(WordUtils.initials(null, array));
        assertEquals("", WordUtils.initials("", array));
        assertEquals("", WordUtils.initials("  ", array));
        assertEquals("I", WordUtils.initials("I", array));
        assertEquals("i", WordUtils.initials("i", array));
        assertEquals("S", WordUtils.initials("SJC", array));
        assertEquals("BJL", WordUtils.initials("Ben John Lee", array));
        assertEquals("BJL", WordUtils.initials("Ben J.Lee", array));
        assertEquals("BJL", WordUtils.initials(" Ben   John  . Lee", array));
        assertEquals("KOM", WordUtils.initials("Kay O'Murphy", array));
        assertEquals("iah1", WordUtils.initials("i am here 123", array));

        array = "SIJo1".toCharArray();
        assertNull(WordUtils.initials(null, array));
        assertEquals("", WordUtils.initials("", array));
        assertEquals(" ", WordUtils.initials("  ", array));
        assertEquals("", WordUtils.initials("I", array));
        assertEquals("i", WordUtils.initials("i", array));
        assertEquals("C", WordUtils.initials("SJC", array));
        assertEquals("Bh", WordUtils.initials("Ben John Lee", array));
        assertEquals("B.", WordUtils.initials("Ben J.Lee", array));
        assertEquals(" h", WordUtils.initials(" Ben   John  . Lee", array));
        assertEquals("K", WordUtils.initials("Kay O'Murphy", array));
        assertEquals("i2", WordUtils.initials("i am here 123", array));
    }

    @Test
    public void testInitialsSurrogatePairs() {
        // Tests with space as default delimiter
        assertEquals("\uD800\uDF00\uD800\uDF02",
                WordUtils.initials("\uD800\uDF00\uD800\uDF01 \uD800\uDF02\uD800\uDF03"));
        assertEquals("\uD800\uDF00\uD800\uDF02",
                WordUtils.initials("\uD800\uDF00\uD800\uDF01 \uD800\uDF02\uD800\uDF03", null));
        assertEquals("\uD800\uDF00\uD800\uDF02", WordUtils.initials("\uD800\uDF00 \uD800\uDF02 ", null));

        // Tests with UTF-16 as delimiters
        assertEquals("\uD800\uDF00\uD800\uDF02",
                WordUtils.initials("\uD800\uDF00\uD800\uDF01.\uD800\uDF02\uD800\uDF03", new char[] {'.'}));
        assertEquals("\uD800\uDF00\uD800\uDF02",
                WordUtils.initials("\uD800\uDF00\uD800\uDF01A\uD800\uDF02\uD800\uDF03", new char[] {'A'}));

        // Tests with UTF-32 as delimiters
        assertEquals("\uD800\uDF00\uD800\uDF02", WordUtils.initials(
                "\uD800\uDF00\uD800\uDF01\uD800\uDF14\uD800\uDF02\uD800\uDF03", new char[] {'\uD800', '\uDF14'}));
        assertEquals("\uD800\uDF00\uD800\uDF02",
                WordUtils.initials("\uD800\uDF00\uD800\uDF01\uD800\uDF14\uD800\uDF18\uD800\uDF02\uD800\uDF03",
                        new char[] {'\uD800', '\uDF14', '\uD800', '\uDF18'}));
    }

    // -----------------------------------------------------------------------
    @Test
    public void testSwapCase_String() {
        assertNull(WordUtils.swapCase(null));
        assertEquals("", WordUtils.swapCase(""));
        assertEquals("  ", WordUtils.swapCase("  "));

        assertEquals("i", WordUtils.swapCase("I"));
        assertEquals("I", WordUtils.swapCase("i"));
        assertEquals("I AM HERE 123", WordUtils.swapCase("i am here 123"));
        assertEquals("i aM hERE 123", WordUtils.swapCase("I Am Here 123"));
        assertEquals("I AM here 123", WordUtils.swapCase("i am HERE 123"));
        assertEquals("i am here 123", WordUtils.swapCase("I AM HERE 123"));

        final String test = "This String contains a TitleCase character: \u01C8";
        final String expect = "tHIS sTRING CONTAINS A tITLEcASE CHARACTER: \u01C9";
        assertEquals(expect, WordUtils.swapCase(test));
    }

    // -----------------------------------------------------------------------
    @Test
    public void testAbbreviateForNullAndEmptyString() {
        assertNull((WordUtils.abbreviate(null, 1, -1, "")));
        assertEquals(StringUtils.EMPTY, WordUtils.abbreviate("", 1, -1, ""));
        assertEquals("", WordUtils.abbreviate("0123456790", 0, 0, ""));
        assertEquals("", WordUtils.abbreviate(" 0123456790", 0, -1, ""));
    }

    // -----------------------------------------------------------------------
    @Test
    public void testAbbreviateForUpperLimit() {
        assertEquals("01234", WordUtils.abbreviate("0123456789", 0, 5, ""));
        assertEquals("012", WordUtils.abbreviate("012 3456789", 2, 5, ""));
        assertEquals("0123456789", WordUtils.abbreviate("0123456789", 0, -1, ""));
    }

    // -----------------------------------------------------------------------
    @Test
    public void testAbbreviateForUpperLimitAndAppendedString() {
        assertEquals("01234-", WordUtils.abbreviate("0123456789", 0, 5, "-"));
        assertEquals("012", WordUtils.abbreviate("012 3456789", 2, 5, null));
        assertEquals("0123456789", WordUtils.abbreviate("0123456789", 0, -1, ""));
    }

    // -----------------------------------------------------------------------
    @Test
    public void testAbbreviateForLowerValue() {
        assertEquals("012", WordUtils.abbreviate("012 3456789", 0, 5, null));
        assertEquals("01234", WordUtils.abbreviate("01234 56789", 5, 10, null));
        assertEquals("01 23 45 67", WordUtils.abbreviate("01 23 45 67 89", 9, -1, null));
        assertEquals("01 23 45 6", WordUtils.abbreviate("01 23 45 67 89", 9, 10, null));
        assertEquals("0123456789", WordUtils.abbreviate("0123456789", 15, 20, null));
    }

    // -----------------------------------------------------------------------
    @Test
    public void testAbbreviateForLowerValueAndAppendedString() {
        assertEquals("012", WordUtils.abbreviate("012 3456789", 0, 5, null));
        assertEquals("01234-", WordUtils.abbreviate("01234 56789", 5, 10, "-"));
        assertEquals("01 23 45 67abc", WordUtils.abbreviate("01 23 45 67 89", 9, -1, "abc"));
        assertEquals("01 23 45 6", WordUtils.abbreviate("01 23 45 67 89", 9, 10, ""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbbreviateForLowerThanMinusOneValues() {
        assertEquals("01 23 45 67", WordUtils.abbreviate("01 23 45 67 89", 9, -10, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbbreviateUpperLessThanLowerValues() {
        assertEquals("01234", WordUtils.abbreviate("0123456789", 5, 2, ""));
    }

    @Test
    public void testLANG673() throws Exception {
        assertEquals("01", WordUtils.abbreviate("01 23 45 67 89", 0, 40, ""));
        assertEquals("01 23 45 67", WordUtils.abbreviate("01 23 45 67 89", 10, 40, ""));
        assertEquals("01 23 45 67 89", WordUtils.abbreviate("01 23 45 67 89", 40, 40, ""));
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
        assertFalse(WordUtils.containsAllWords("M", null));
    }

}
