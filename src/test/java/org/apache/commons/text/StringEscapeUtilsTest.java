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

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.apache.commons.text.StringEscapeUtils.escapeXSI;
import static org.apache.commons.text.StringEscapeUtils.unescapeXSI;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link StringEscapeUtils}.
 *
 * <p>
 * This code has been adapted from Apache Commons Lang 3.5.
 * </p>
 *
 */
public class StringEscapeUtilsTest {
    private static final String FOO = "foo";

    @Test
    public void testConstructor() {
        assertNotNull(new StringEscapeUtils());
        final Constructor<?>[] cons = StringEscapeUtils.class.getDeclaredConstructors();
        assertEquals(1, cons.length);
        assertTrue(Modifier.isPublic(cons[0].getModifiers()));
        assertTrue(Modifier.isPublic(StringEscapeUtils.class.getModifiers()));
        assertFalse(Modifier.isFinal(StringEscapeUtils.class.getModifiers()));
    }

    @Test
    public void testEscapeJava() throws IOException {
        assertNull(StringEscapeUtils.escapeJava(null));
        try {
            StringEscapeUtils.ESCAPE_JAVA.translate(null, null);
            fail();
        } catch (final IOException ex) {
            fail();
        } catch (final IllegalArgumentException ex) {
        }
        try {
            StringEscapeUtils.ESCAPE_JAVA.translate("", null);
            fail();
        } catch (final IOException ex) {
            fail();
        } catch (final IllegalArgumentException ex) {
        }

        assertEscapeJava("empty string", "", "");
        assertEscapeJava(FOO, FOO);
        assertEscapeJava("tab", "\\t", "\t");
        assertEscapeJava("backslash", "\\\\", "\\");
        assertEscapeJava("single quote should not be escaped", "'", "'");
        assertEscapeJava("\\\\\\b\\t\\r", "\\\b\t\r");
        assertEscapeJava("\\u1234", "\u1234");
        assertEscapeJava("\\u0234", "\u0234");
        assertEscapeJava("\\u00EF", "\u00ef");
        assertEscapeJava("\\u0001", "\u0001");
        assertEscapeJava("Should use capitalized Unicode hex", "\\uABCD", "\uabcd");

        assertEscapeJava("He didn't say, \\\"stop!\\\"",
                "He didn't say, \"stop!\"");
        assertEscapeJava("non-breaking space", "This space is non-breaking:" + "\\u00A0",
                "This space is non-breaking:\u00a0");
        assertEscapeJava("\\uABCD\\u1234\\u012C",
                "\uABCD\u1234\u012C");
    }

    /**
     * Tests https://issues.apache.org/jira/browse/LANG-421
     */
    @Test
    public void testEscapeJavaWithSlash() {
        final String input = "String with a slash (/) in it";

        final String expected = input;
        final String actual = StringEscapeUtils.escapeJava(input);

        /**
         * In 2.4 StringEscapeUtils.escapeJava(String) escapes '/' characters, which are not a valid character
         * to escape in a Java string.
         */
        assertEquals(expected, actual);
    }

    private void assertEscapeJava(final String escaped, final String original) throws IOException {
        assertEscapeJava(null, escaped, original);
    }

    private void assertEscapeJava(String message, final String expected, final String original) throws IOException {
        final String converted = StringEscapeUtils.escapeJava(original);
        message = "escapeJava(String) failed" + (message == null ? "" : (": " + message));
        assertEquals(message, expected, converted);

        final StringWriter writer = new StringWriter();
        StringEscapeUtils.ESCAPE_JAVA.translate(original, writer);
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testUnescapeJava() throws IOException {
        assertNull(StringEscapeUtils.unescapeJava(null));
        try {
            StringEscapeUtils.UNESCAPE_JAVA.translate(null, null);
            fail();
        } catch (final IOException ex) {
            fail();
        } catch (final IllegalArgumentException ex) {
        }
        try {
            StringEscapeUtils.UNESCAPE_JAVA.translate("", null);
            fail();
        } catch (final IOException ex) {
            fail();
        } catch (final IllegalArgumentException ex) {
        }
        try {
            StringEscapeUtils.unescapeJava("\\u02-3");
            fail();
        } catch (final RuntimeException ex) {
        }

        assertUnescapeJava("", "");
        assertUnescapeJava("test", "test");
        assertUnescapeJava("\ntest\b", "\\ntest\\b");
        assertUnescapeJava("\u123425foo\ntest\b", "\\u123425foo\\ntest\\b");
        assertUnescapeJava("'\foo\teste\r", "\\'\\foo\\teste\\r");
        assertUnescapeJava("", "\\");
        //foo
        assertUnescapeJava("lowercase Unicode", "\uABCDx", "\\uabcdx");
        assertUnescapeJava("uppercase Unicode", "\uABCDx", "\\uABCDx");
        assertUnescapeJava("Unicode as final character", "\uABCD", "\\uabcd");
    }

    private void assertUnescapeJava(final String unescaped, final String original) throws IOException {
        assertUnescapeJava(null, unescaped, original);
    }

    private void assertUnescapeJava(final String message, final String unescaped, final String original)
            throws IOException {
        final String expected = unescaped;
        final String actual = StringEscapeUtils.unescapeJava(original);

        assertEquals("unescape(String) failed"
                        + (message == null ? "" : (": " + message))
                        + ": expected '" + StringEscapeUtils.escapeJava(expected)
                        // we escape this so we can see it in the error message
                        + "' actual '" + StringEscapeUtils.escapeJava(actual) + "'",
                expected, actual);

        final StringWriter writer = new StringWriter();
        StringEscapeUtils.UNESCAPE_JAVA.translate(original, writer);
        assertEquals(unescaped, writer.toString());
    }

    @Test
    public void testEscapeEcmaScript() {
        assertNull(StringEscapeUtils.escapeEcmaScript(null));
        try {
            StringEscapeUtils.ESCAPE_ECMASCRIPT.translate(null, null);
            fail();
        } catch (final IOException ex) {
            fail();
        } catch (final IllegalArgumentException ex) {
        }
        try {
            StringEscapeUtils.ESCAPE_ECMASCRIPT.translate("", null);
            fail();
        } catch (final IOException ex) {
            fail();
        } catch (final IllegalArgumentException ex) {
        }

        assertEquals("He didn\\'t say, \\\"stop!\\\"", StringEscapeUtils.escapeEcmaScript("He didn't say, \"stop!\""));
        assertEquals("document.getElementById(\\\"test\\\").value = \\'<script>alert(\\'aaa\\');<\\/script>\\';",
                StringEscapeUtils.escapeEcmaScript(
                        "document.getElementById(\"test\").value = '<script>alert('aaa');</script>';"));
    }


    // HTML and XML
    //--------------------------------------------------------------

    private static final String[][] HTML_ESCAPES = {
            {"no escaping", "plain text", "plain text"},
            {"no escaping", "plain text", "plain text"},
            {"empty string", "", ""},
            {"null", null, null},
            {"ampersand", "bread &amp; butter", "bread & butter"},
            {"quotes", "&quot;bread&quot; &amp; butter", "\"bread\" & butter"},
            {"final character only", "greater than &gt;", "greater than >"},
            {"first character only", "&lt; less than", "< less than"},
            {"apostrophe", "Huntington's chorea", "Huntington's chorea"},
            {"languages", "English,Fran&ccedil;ais,\u65E5\u672C\u8A9E (nihongo)",
                "English,Fran\u00E7ais,\u65E5\u672C\u8A9E (nihongo)"},
            {"8-bit ascii shouldn't number-escape", "\u0080\u009F", "\u0080\u009F"},
    };

    @Test
    public void testEscapeHtml3() {
        for (final String[] element : HTML_ESCAPES) {
            final String message = element[0];
            final String expected = element[1];
            final String original = element[2];
            assertEquals(message, expected, StringEscapeUtils.escapeHtml4(original));
            final StringWriter sw = new StringWriter();
            try {
                StringEscapeUtils.ESCAPE_HTML3.translate(original, sw);
            } catch (final IOException e) {
            }
            final String actual = original == null ? null : sw.toString();
            assertEquals(message, expected, actual);
        }
    }

    @Test
    public void testUnescapeHtml3() {
        for (final String[] element : HTML_ESCAPES) {
            final String message = element[0];
            final String expected = element[2];
            final String original = element[1];
            assertEquals(message, expected, StringEscapeUtils.unescapeHtml3(original));

            final StringWriter sw = new StringWriter();
            try {
                StringEscapeUtils.UNESCAPE_HTML3.translate(original, sw);
            } catch (final IOException e) {
            }
            final String actual = original == null ? null : sw.toString();
            assertEquals(message, expected, actual);
        }
        // \u00E7 is a cedilla (c with wiggle under)
        // note that the test string must be 7-bit-clean (Unicode escaped) or else it will compile incorrectly
        // on some locales
        assertEquals("funny chars pass through OK", "Fran\u00E7ais", StringEscapeUtils.unescapeHtml3("Fran\u00E7ais"));

        assertEquals("Hello&;World", StringEscapeUtils.unescapeHtml3("Hello&;World"));
        assertEquals("Hello&#;World", StringEscapeUtils.unescapeHtml3("Hello&#;World"));
        assertEquals("Hello&# ;World", StringEscapeUtils.unescapeHtml3("Hello&# ;World"));
        assertEquals("Hello&##;World", StringEscapeUtils.unescapeHtml3("Hello&##;World"));
    }

@Test
    public void testEscapeHtml4() {
        for (final String[] element : HTML_ESCAPES) {
            final String message = element[0];
            final String expected = element[1];
            final String original = element[2];
            assertEquals(message, expected, StringEscapeUtils.escapeHtml4(original));
            final StringWriter sw = new StringWriter();
            try {
                StringEscapeUtils.ESCAPE_HTML4.translate(original, sw);
            } catch (final IOException e) {
            }
            final String actual = original == null ? null : sw.toString();
            assertEquals(message, expected, actual);
        }
    }

    @Test
    public void testUnescapeHtml4() {
        for (final String[] element : HTML_ESCAPES) {
            final String message = element[0];
            final String expected = element[2];
            final String original = element[1];
            assertEquals(message, expected, StringEscapeUtils.unescapeHtml4(original));

            final StringWriter sw = new StringWriter();
            try {
                StringEscapeUtils.UNESCAPE_HTML4.translate(original, sw);
            } catch (final IOException e) {
            }
            final String actual = original == null ? null : sw.toString();
            assertEquals(message, expected, actual);
        }
        // \u00E7 is a cedilla (c with wiggle under)
        // note that the test string must be 7-bit-clean (Unicode escaped) or else it will compile incorrectly
        // on some locales
        assertEquals("funny chars pass through OK", "Fran\u00E7ais", StringEscapeUtils.unescapeHtml4("Fran\u00E7ais"));

        assertEquals("Hello&;World", StringEscapeUtils.unescapeHtml4("Hello&;World"));
        assertEquals("Hello&#;World", StringEscapeUtils.unescapeHtml4("Hello&#;World"));
        assertEquals("Hello&# ;World", StringEscapeUtils.unescapeHtml4("Hello&# ;World"));
        assertEquals("Hello&##;World", StringEscapeUtils.unescapeHtml4("Hello&##;World"));
    }

    @Test
    public void testUnescapeHexCharsHtml() {
        // Simple easy to grok test
        assertEquals("hex number unescape", "\u0080\u009F", StringEscapeUtils.unescapeHtml4("&#x80;&#x9F;"));
        assertEquals("hex number unescape", "\u0080\u009F", StringEscapeUtils.unescapeHtml4("&#X80;&#X9F;"));
        // Test all Character values:
        for (char i = Character.MIN_VALUE; i < Character.MAX_VALUE; i++) {
            final Character c1 = i;
            final Character c2 = (char) (i + 1);
            final String expected = c1.toString() + c2.toString();
            final String escapedC1 = "&#x" + Integer.toHexString(c1) + ";";
            final String escapedC2 = "&#x" + Integer.toHexString(c2) + ";";
            assertEquals("hex number unescape index " + i, expected,
                    StringEscapeUtils.unescapeHtml4(escapedC1 + escapedC2));
        }
    }

    @Test
    public void testUnescapeUnknownEntity() throws Exception {
        assertEquals("&zzzz;", StringEscapeUtils.unescapeHtml4("&zzzz;"));
    }

    @Test
    public void testEscapeHtmlVersions() throws Exception {
        assertEquals("&Beta;", StringEscapeUtils.escapeHtml4("\u0392"));
        assertEquals("\u0392", StringEscapeUtils.unescapeHtml4("&Beta;"));

        // TODO: refine API for escaping/unescaping specific HTML versions
    }



    @Test
    public void testEscapeXml10() throws Exception {
        assertEquals("a&lt;b&gt;c&quot;d&apos;e&amp;f", StringEscapeUtils.escapeXml10("a<b>c\"d'e&f"));
        assertEquals("XML 1.0 should not escape \t \n \r",
                "a\tb\rc\nd", StringEscapeUtils.escapeXml10("a\tb\rc\nd"));
        assertEquals("XML 1.0 should omit most #x0-x8 | #xb | #xc | #xe-#x19",
                "ab", StringEscapeUtils.escapeXml10("a\u0000\u0001\u0008\u000b\u000c\u000e\u001fb"));
        assertEquals("XML 1.0 should omit #xd800-#xdfff",
                "a\ud7ff  \ue000b", StringEscapeUtils.escapeXml10("a\ud7ff\ud800 \udfff \ue000b"));
        assertEquals("XML 1.0 should omit #xfffe | #xffff",
                "a\ufffdb", StringEscapeUtils.escapeXml10("a\ufffd\ufffe\uffffb"));
        assertEquals("XML 1.0 should escape #x7f-#x84 | #x86 - #x9f, for XML 1.1 compatibility",
                "a\u007e&#127;&#132;\u0085&#134;&#159;\u00a0b",
                StringEscapeUtils.escapeXml10("a\u007e\u007f\u0084\u0085\u0086\u009f\u00a0b"));
    }

    @Test
    public void testEscapeXml11() throws Exception {
        assertEquals("a&lt;b&gt;c&quot;d&apos;e&amp;f", StringEscapeUtils.escapeXml11("a<b>c\"d'e&f"));
        assertEquals("XML 1.1 should not escape \t \n \r",
                "a\tb\rc\nd", StringEscapeUtils.escapeXml11("a\tb\rc\nd"));
        assertEquals("XML 1.1 should omit #x0",
                "ab", StringEscapeUtils.escapeXml11("a\u0000b"));
        assertEquals("XML 1.1 should escape #x1-x8 | #xb | #xc | #xe-#x19",
                "a&#1;&#8;&#11;&#12;&#14;&#31;b",
                StringEscapeUtils.escapeXml11("a\u0001\u0008\u000b\u000c\u000e\u001fb"));
        assertEquals("XML 1.1 should escape #x7F-#x84 | #x86-#x9F",
                "a\u007e&#127;&#132;\u0085&#134;&#159;\u00a0b",
                StringEscapeUtils.escapeXml11("a\u007e\u007f\u0084\u0085\u0086\u009f\u00a0b"));
        assertEquals("XML 1.1 should omit #xd800-#xdfff",
                "a\ud7ff  \ue000b", StringEscapeUtils.escapeXml11("a\ud7ff\ud800 \udfff \ue000b"));
        assertEquals("XML 1.1 should omit #xfffe | #xffff",
                "a\ufffdb", StringEscapeUtils.escapeXml11("a\ufffd\ufffe\uffffb"));
    }

    /**
     * Reverse of the above.
     *
     * @see <a href="https://issues.apache.org/jira/browse/LANG-729">LANG-729</a>
     */
    @Test
    public void testUnescapeXmlSupplementaryCharacters() {
        assertEquals("Supplementary character must be represented using a single escape", "\uD84C\uDFB4",
                StringEscapeUtils.unescapeXml("&#144308;"));

        assertEquals("Supplementary characters mixed with basic characters should be decoded correctly",
                "a b c \uD84C\uDFB4", StringEscapeUtils.unescapeXml("a b c &#144308;"));
    }

    // Tests issue #38569
    // http://issues.apache.org/bugzilla/show_bug.cgi?id=38569
    @Test
    public void testStandaloneAmphersand() {
        assertEquals("<P&O>", StringEscapeUtils.unescapeHtml4("&lt;P&O&gt;"));
        assertEquals("test & <", StringEscapeUtils.unescapeHtml4("test & &lt;"));
        assertEquals("<P&O>", StringEscapeUtils.unescapeXml("&lt;P&O&gt;"));
        assertEquals("test & <", StringEscapeUtils.unescapeXml("test & &lt;"));
    }

    @Test
    public void testLang313() {
        assertEquals("& &", StringEscapeUtils.unescapeHtml4("& &amp;"));
    }

    @Test
    public void testEscapeCsvString() throws Exception {
        assertEquals("foo.bar", StringEscapeUtils.escapeCsv("foo.bar"));
        assertEquals("\"foo,bar\"", StringEscapeUtils.escapeCsv("foo,bar"));
        assertEquals("\"foo\nbar\"", StringEscapeUtils.escapeCsv("foo\nbar"));
        assertEquals("\"foo\rbar\"", StringEscapeUtils.escapeCsv("foo\rbar"));
        assertEquals("\"foo\"\"bar\"", StringEscapeUtils.escapeCsv("foo\"bar"));
        assertEquals("foo\uD84C\uDFB4bar", StringEscapeUtils.escapeCsv("foo\uD84C\uDFB4bar"));
        assertEquals("", StringEscapeUtils.escapeCsv(""));
        assertNull(StringEscapeUtils.escapeCsv(null));
    }

    @Test
    public void testEscapeCsvWriter() throws Exception {
        checkCsvEscapeWriter("foo.bar", "foo.bar");
        checkCsvEscapeWriter("\"foo,bar\"", "foo,bar");
        checkCsvEscapeWriter("\"foo\nbar\"", "foo\nbar");
        checkCsvEscapeWriter("\"foo\rbar\"", "foo\rbar");
        checkCsvEscapeWriter("\"foo\"\"bar\"", "foo\"bar");
        checkCsvEscapeWriter("foo\uD84C\uDFB4bar", "foo\uD84C\uDFB4bar");
        checkCsvEscapeWriter("", null);
        checkCsvEscapeWriter("", "");
    }

    private void checkCsvEscapeWriter(final String expected, final String value) {
        try {
            final StringWriter writer = new StringWriter();
            StringEscapeUtils.ESCAPE_CSV.translate(value, writer);
            assertEquals(expected, writer.toString());
        } catch (final IOException e) {
            fail("Threw: " + e);
        }
    }

    @Test
    public void testUnescapeCsvString() throws Exception {
        assertEquals("foo.bar", StringEscapeUtils.unescapeCsv("foo.bar"));
        assertEquals("foo,bar", StringEscapeUtils.unescapeCsv("\"foo,bar\""));
        assertEquals("foo\nbar", StringEscapeUtils.unescapeCsv("\"foo\nbar\""));
        assertEquals("foo\rbar", StringEscapeUtils.unescapeCsv("\"foo\rbar\""));
        assertEquals("foo\"bar", StringEscapeUtils.unescapeCsv("\"foo\"\"bar\""));
        assertEquals("foo\uD84C\uDFB4bar", StringEscapeUtils.unescapeCsv("foo\uD84C\uDFB4bar"));
        assertEquals("", StringEscapeUtils.unescapeCsv(""));
        assertNull(StringEscapeUtils.unescapeCsv(null));

        assertEquals("\"foo.bar\"", StringEscapeUtils.unescapeCsv("\"foo.bar\""));
    }

    @Test
    public void testUnescapeCsvWriter() throws Exception {
        checkCsvUnescapeWriter("foo.bar", "foo.bar");
        checkCsvUnescapeWriter("foo,bar", "\"foo,bar\"");
        checkCsvUnescapeWriter("foo\nbar", "\"foo\nbar\"");
        checkCsvUnescapeWriter("foo\rbar", "\"foo\rbar\"");
        checkCsvUnescapeWriter("foo\"bar", "\"foo\"\"bar\"");
        checkCsvUnescapeWriter("foo\uD84C\uDFB4bar", "foo\uD84C\uDFB4bar");
        checkCsvUnescapeWriter("", null);
        checkCsvUnescapeWriter("", "");

        checkCsvUnescapeWriter("\"foo.bar\"", "\"foo.bar\"");
    }

    private void checkCsvUnescapeWriter(final String expected, final String value) {
        try {
            final StringWriter writer = new StringWriter();
            StringEscapeUtils.UNESCAPE_CSV.translate(value, writer);
            assertEquals(expected, writer.toString());
        } catch (final IOException e) {
            fail("Threw: " + e);
        }
    }

    /**
     * Tests // https://issues.apache.org/jira/browse/LANG-480
     */
    @Test
    public void testEscapeHtmlHighUnicode() {
        // this is the utf8 representation of the character:
        // COUNTING ROD UNIT DIGIT THREE
        // in Unicode
        // codepoint: U+1D362
        final byte[] data = {(byte) 0xF0, (byte) 0x9D, (byte) 0x8D, (byte) 0xA2};

        final String original = new String(data, Charset.forName("UTF8"));

        final String escaped = StringEscapeUtils.escapeHtml4(original);
        assertEquals("High Unicode should not have been escaped", original, escaped);

        final String unescaped = StringEscapeUtils.unescapeHtml4(escaped);
        assertEquals("High Unicode should have been unchanged", original, unescaped);

        // TODO: I think this should hold, needs further investigation
        //        String unescapedFromEntity = StringEscapeUtils.unescapeHtml4("&#119650;");
        //        assertEquals("High Unicode should have been unescaped", original, unescapedFromEntity);
    }

    /**
     * Tests https://issues.apache.org/jira/browse/LANG-339
     */
    @Test
    public void testEscapeHiragana() {
        // Some random Japanese Unicode characters
        final String original = "\u304B\u304C\u3068";
        final String escaped = StringEscapeUtils.escapeHtml4(original);
        assertEquals("Hiragana character Unicode behaviour should not be being escaped by escapeHtml4",
                original, escaped);

        final String unescaped = StringEscapeUtils.unescapeHtml4(escaped);

        assertEquals("Hiragana character Unicode behaviour has changed - expected no unescaping", escaped, unescaped);
    }

    /**
     * Tests https://issues.apache.org/jira/browse/LANG-708
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @Test
    public void testLang708() throws IOException {
        final byte[] inputBytes = Files.readAllBytes(Paths.get("src/test/resources/stringEscapeUtilsTestData.txt"));
        final String input = new String(inputBytes, StandardCharsets.UTF_8);
        final String escaped = StringEscapeUtils.escapeEcmaScript(input);
        // just the end:
        assertTrue(escaped, escaped.endsWith("}]"));
        // a little more:
        assertTrue(escaped, escaped.endsWith("\"valueCode\\\":\\\"\\\"}]"));
    }

    /**
     * Tests https://issues.apache.org/jira/browse/LANG-911
     */
    @Test
    public void testLang911() {
        final String bellsTest = "\ud83d\udc80\ud83d\udd14";
        final String value = StringEscapeUtils.escapeJava(bellsTest);
        final String valueTest = StringEscapeUtils.unescapeJava(value);
        assertEquals(bellsTest, valueTest);
    }

    @Test
    public void testEscapeJson() {
        assertNull(StringEscapeUtils.escapeJson(null));
        try {
            StringEscapeUtils.ESCAPE_JSON.translate(null, null);
            fail();
        } catch (final IOException ex) {
            fail();
        } catch (final IllegalArgumentException ex) {
        }
        try {
            StringEscapeUtils.ESCAPE_JSON.translate("", null);
            fail();
        } catch (final IOException ex) {
            fail();
        } catch (final IllegalArgumentException ex) {
        }

        assertEquals("He didn't say, \\\"stop!\\\"", StringEscapeUtils.escapeJson("He didn't say, \"stop!\""));

        final String expected = "\\\"foo\\\" isn't \\\"bar\\\". specials: \\b\\r\\n\\f\\t\\\\\\/";
        final String input = "\"foo\" isn't \"bar\". specials: \b\r\n\f\t\\/";

        assertEquals(expected, StringEscapeUtils.escapeJson(input));
    }

    @Test
    public void testBuilder() {
        final String result =
                StringEscapeUtils.builder(StringEscapeUtils.ESCAPE_XML10).escape("<").append(">").toString();
        assertEquals("&lt;>", result);
    }

    @Test
    public void testEscapeXSI() {
        assertNull(null, escapeXSI(null));
        assertEquals("He\\ didn\\'t\\ say,\\ \\\"Stop!\\\"", escapeXSI("He didn't say, \"Stop!\""));
        assertEquals("\\\\", escapeXSI("\\"));
        assertEquals("", escapeXSI("\n"));
    }

    @Test
    public void testUnscapeXSI() {
        assertNull(null, unescapeXSI(null));
        assertEquals("\"", unescapeXSI("\\\""));
        assertEquals("He didn't say, \"Stop!\"", unescapeXSI("He\\ didn\\'t\\ say,\\ \\\"Stop!\\\""));
        assertEquals("\\", unescapeXSI("\\\\"));
        assertEquals("", unescapeXSI("\\"));
    }

    @Test
    public void testUnescapeEcmaScript() {
        assertNull("Should be null.", StringEscapeUtils.unescapeEcmaScript(null));
        assertEquals("8lvc1u+6B#-I", StringEscapeUtils.unescapeEcmaScript("8lvc1u+6B#-I"));
        assertEquals("<script src=\"build/main.bundle.js\"></script>",
                StringEscapeUtils.unescapeEcmaScript("<script src=\"build/main.bundle.js\"></script>"));
        assertEquals("<script src=\"build/main.bundle.js\"></script>>",
                StringEscapeUtils.unescapeEcmaScript("<script src=\"build/main.bundle.js\"></script>>"));
    }

    @Test
    public void testEscapeHtmlThree() {
        assertNull("Should be null.", StringEscapeUtils.escapeHtml3(null));
        assertEquals("a", StringEscapeUtils.escapeHtml3("a"));
        assertEquals("&lt;b&gt;a", StringEscapeUtils.escapeHtml3("<b>a"));
    }

    @Test
    public void testUnescapeJson() {
        final String jsonString =
                "{\"age\":100,\"name\":\"kyong.com\n\",\"messages\":[\"msg 1\",\"msg 2\",\"msg 3\"]}";

        assertEquals("", StringEscapeUtils.unescapeJson(""));
        assertEquals(" ", StringEscapeUtils.unescapeJson(" "));
        assertEquals("a:b", StringEscapeUtils.unescapeJson("a:b"));
        assertEquals(jsonString, StringEscapeUtils.unescapeJson(jsonString));
    }

    @Test
    public void testUnescapeJsonFoundBug_Issue_Text_100() {
        final String jsonString =
                "{\"age\":100,\"name\":\"m\\\"kyong.com\",\"messages\":[\"msg 1\",\"msg 2\",\"msg 3\"]}";

        assertEquals(jsonString, StringEscapeUtils.unescapeJson(jsonString));
    }

    @Test
    public void testUnescapeJsonFoundBug_Issue_Text_100_Fix() {
        final String jsonString =
                "{\"age\":100,\"name\":\"n\\\"m\\\"kyong.com\",\"messages\":[\"msg 1\",\"msg 2\",\"msg 3\"]}";

        assertEquals(jsonString, StringEscapeUtils.unescapeJson(jsonString));
    }
}
