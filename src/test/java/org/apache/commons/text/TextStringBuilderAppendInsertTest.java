/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link TextStringBuilder}.
 */
class TextStringBuilderAppendInsertTest {

    /** The system line separator. */
    private static final String SEP = System.lineSeparator();

    /** Test subclass of Object, with a toString method. */
    private static final Object FOO = new Object() {
        @Override
        public String toString() {
            return "foo";
        }
    };

    @Test
    void testAppend_Boolean() {
        final TextStringBuilder sb = new TextStringBuilder();
        sb.append(true);
        assertEquals("true", sb.toString());

        sb.append(false);
        assertEquals("truefalse", sb.toString());

        sb.append('!');
        assertEquals("truefalse!", sb.toString());
    }

    @Test
    void testAppend_CharArray() {
        TextStringBuilder sb = new TextStringBuilder();
        sb.setNullText("NULL").append((char[]) null);
        assertEquals("NULL", sb.toString());

        sb = new TextStringBuilder();
        sb.append(ArrayUtils.EMPTY_CHAR_ARRAY);
        assertEquals("", sb.toString());

        sb.append(new char[] {'f', 'o', 'o'});
        assertEquals("foo", sb.toString());
    }

    @Test
    void testAppend_CharArray_int_int() {
        final TextStringBuilder sb0 = new TextStringBuilder();
        sb0.setNullText("NULL").append((char[]) null, 0, 1);
        assertEquals("NULL", sb0.toString());

        final TextStringBuilder sb = new TextStringBuilder();
        sb.append(new char[] {'f', 'o', 'o'}, 0, 3);
        assertEquals("foo", sb.toString());

        assertThrows(IndexOutOfBoundsException.class, () -> sb.append(new char[] {'b', 'a', 'r'}, -1, 1),
            "append(char[], -1,) expected IndexOutOfBoundsException");

        assertThrows(IndexOutOfBoundsException.class, () -> sb.append(new char[] {'b', 'a', 'r'}, 3, 1),
            "append(char[], 3,) expected IndexOutOfBoundsException");

        assertThrows(IndexOutOfBoundsException.class, () -> sb.append(new char[] {'b', 'a', 'r'}, 1, -1),
            "append(char[],, -1) expected IndexOutOfBoundsException");

        assertThrows(IndexOutOfBoundsException.class, () -> sb.append(new char[] {'b', 'a', 'r'}, 1, 3),
            "append(char[], 1, 3) expected IndexOutOfBoundsException");

        assertThrows(IndexOutOfBoundsException.class, () -> sb.append(new char[] {'b', 'a', 'r'}, -1, 3),
            "append(char[], -1, 3) expected IndexOutOfBoundsException");

        assertThrows(IndexOutOfBoundsException.class, () -> sb.append(new char[] {'b', 'a', 'r'}, 4, 0),
            "append(char[], 4, 0) expected IndexOutOfBoundsException");

        sb.append(new char[] {'b', 'a', 'r'}, 3, 0);
        assertEquals("foo", sb.toString());

        sb.append(new char[] {'a', 'b', 'c', 'b', 'a', 'r', 'd', 'e', 'f'}, 3, 3);
        assertEquals("foobar", sb.toString());
    }

    @Test
    void testAppend_FormattedString() {
        TextStringBuilder sb;

        sb = new TextStringBuilder();
        sb.append("Hi", (Object[]) null);
        assertEquals("Hi", sb.toString());

        sb = new TextStringBuilder();
        sb.append("Hi", "Alice");
        assertEquals("Hi", sb.toString());

        sb = new TextStringBuilder();
        sb.append("Hi %s", "Alice");
        assertEquals("Hi Alice", sb.toString());

        sb = new TextStringBuilder();
        sb.append("Hi %s %,d", "Alice", 5000);
        // group separator depends on system locale
        final char groupingSeparator = DecimalFormatSymbols.getInstance().getGroupingSeparator();
        final String expected = "Hi Alice 5" + groupingSeparator + "000";
        assertEquals(expected, sb.toString());
    }

    @Test
    void testAppend_Object() {
        final TextStringBuilder sb = new TextStringBuilder();
        sb.appendNull();
        assertEquals("", sb.toString());

        sb.append((Object) null);
        assertEquals("", sb.toString());

        sb.append(FOO);
        assertEquals("foo", sb.toString());

        sb.append((StringBuffer) null);
        assertEquals("foo", sb.toString());

        sb.append(new StringBuffer("baz"));
        assertEquals("foobaz", sb.toString());

        sb.append(new TextStringBuilder("yes"));
        assertEquals("foobazyes", sb.toString());

        sb.append((CharSequence) "Seq");
        assertEquals("foobazyesSeq", sb.toString());

        sb.append(new StringBuilder("bld")); // Check it supports StringBuilder
        assertEquals("foobazyesSeqbld", sb.toString());
    }

    @Test
    void testAppend_PrimitiveNumber() {
        final TextStringBuilder sb = new TextStringBuilder();
        sb.append(0);
        assertEquals("0", sb.toString());

        sb.append(1L);
        assertEquals("01", sb.toString());

        sb.append(2.3f);
        assertEquals("012.3", sb.toString());

        sb.append(4.5d);
        assertEquals("012.34.5", sb.toString());
    }

    @Test
    void testAppend_String() {
        TextStringBuilder sb = new TextStringBuilder();
        sb.setNullText("NULL").append((String) null);
        assertEquals("NULL", sb.toString());

        sb = new TextStringBuilder();
        sb.append("foo");
        assertEquals("foo", sb.toString());

        sb.append("");
        assertEquals("foo", sb.toString());

        sb.append("bar");
        assertEquals("foobar", sb.toString());
    }

    @Test
    void testAppend_String_int_int() {
        final TextStringBuilder sb0 = new TextStringBuilder();
        sb0.setNullText("NULL").append((String) null, 0, 1);
        assertEquals("NULL", sb0.toString());

        final TextStringBuilder sb = new TextStringBuilder();
        sb.append("foo", 0, 3);
        assertEquals("foo", sb.toString());
        assertThrows(IndexOutOfBoundsException.class, () -> sb.append("bar", -1, 1), "append(char[], -1,) expected IndexOutOfBoundsException");
        assertThrows(IndexOutOfBoundsException.class, () -> sb.append("bar", 3, 1), "append(char[], 3,) expected IndexOutOfBoundsException");
        assertThrows(IndexOutOfBoundsException.class, () -> sb.append("bar", 1, -1), "append(char[],, -1) expected IndexOutOfBoundsException");
        assertThrows(IndexOutOfBoundsException.class, () -> sb.append("bar", 1, 3), "append(char[], 1, 3) expected IndexOutOfBoundsException");
        assertThrows(IndexOutOfBoundsException.class, () -> sb.append("bar", -1, 3), "append(char[], -1, 3) expected IndexOutOfBoundsException");
        assertThrows(IndexOutOfBoundsException.class, () -> sb.append("bar", 4, 0), "append(char[], 4, 0) expected IndexOutOfBoundsException");
        assertThrows(IndexOutOfBoundsException.class, () -> sb.append((CharSequence) "bar", 2, 1), "append(char[], 2, 1) expected IndexOutOfBoundsException");
        assertThrows(IndexOutOfBoundsException.class, () -> sb.append((CharSequence) "bar", 2, 2), "append(char[], 2, 2) expected IndexOutOfBoundsException");
        assertThrows(IndexOutOfBoundsException.class, () -> sb.append((CharSequence) "bar", 2, -2), "append(char[], 2, -2) expected IndexOutOfBoundsException");
        assertThrows(IndexOutOfBoundsException.class, () -> sb.append((CharSequence) "bar", 2, 0), "append(char[], 2, 0) expected IndexOutOfBoundsException");

        sb.append("bar", 3, 0);
        assertEquals("foo", sb.toString());

        sb.append("abcbardef", 3, 3);
        assertEquals("foobar", sb.toString());

        sb.append((CharSequence) "abcbardef", 4, 7);
        assertEquals("foobarard", sb.toString());
    }

    @Test
    void testAppend_StringBuffer() {
        TextStringBuilder sb = new TextStringBuilder();
        sb.setNullText("NULL").append((StringBuffer) null);
        assertEquals("NULL", sb.toString());

        sb = new TextStringBuilder();
        sb.append(new StringBuffer("foo"));
        assertEquals("foo", sb.toString());

        sb.append(new StringBuffer(""));
        assertEquals("foo", sb.toString());

        sb.append(new StringBuffer("bar"));
        assertEquals("foobar", sb.toString());
    }

    @Test
    void testAppend_StringBuffer_int_int() {
        final TextStringBuilder sb0 = new TextStringBuilder();
        sb0.setNullText("NULL").append((StringBuffer) null, 0, 1);
        assertEquals("NULL", sb0.toString());

        final TextStringBuilder sb = new TextStringBuilder();
        sb.append(new StringBuffer("foo"), 0, 3);
        assertEquals("foo", sb.toString());

        assertThrows(IndexOutOfBoundsException.class, () -> sb.append(new StringBuffer("bar"), -1, 1),
            "append(char[], -1,) expected IndexOutOfBoundsException");

        assertThrows(IndexOutOfBoundsException.class, () -> sb.append(new StringBuffer("bar"), 3, 1), "append(char[], 3,) expected IndexOutOfBoundsException");

        assertThrows(IndexOutOfBoundsException.class, () -> sb.append(new StringBuffer("bar"), 1, -1),
            "append(char[],, -1) expected IndexOutOfBoundsException");

        assertThrows(IndexOutOfBoundsException.class, () -> sb.append(new StringBuffer("bar"), 1, 3),
            "append(char[], 1, 3) expected IndexOutOfBoundsException");

        assertThrows(IndexOutOfBoundsException.class, () -> sb.append(new StringBuffer("bar"), -1, 3),
            "append(char[], -1, 3) expected IndexOutOfBoundsException");

        assertThrows(IndexOutOfBoundsException.class, () -> sb.append(new StringBuffer("bar"), 4, 0),
            "append(char[], 4, 0) expected IndexOutOfBoundsException");

        sb.append(new StringBuffer("bar"), 3, 0);
        assertEquals("foo", sb.toString());

        sb.append(new StringBuffer("abcbardef"), 3, 3);
        assertEquals("foobar", sb.toString());
    }

    @Test
    void testAppend_StringBuilder() {
        TextStringBuilder sb = new TextStringBuilder();
        sb.setNullText("NULL").append((String) null);
        assertEquals("NULL", sb.toString());

        sb = new TextStringBuilder();
        sb.append(new StringBuilder("foo"));
        assertEquals("foo", sb.toString());

        sb.append(new StringBuilder(""));
        assertEquals("foo", sb.toString());

        sb.append(new StringBuilder("bar"));
        assertEquals("foobar", sb.toString());
    }

    @Test
    void testAppend_StringBuilder_int_int() {
        final TextStringBuilder sb0 = new TextStringBuilder();
        sb0.setNullText("NULL").append((String) null, 0, 1);
        assertEquals("NULL", sb0.toString());

        final TextStringBuilder sb = new TextStringBuilder();
        sb.append(new StringBuilder("foo"), 0, 3);
        assertEquals("foo", sb.toString());

        assertThrows(IndexOutOfBoundsException.class, () -> sb.append(new StringBuilder("bar"), -1, 1),
            "append(StringBuilder, -1,) expected IndexOutOfBoundsException");

        assertThrows(IndexOutOfBoundsException.class, () -> sb.append(new StringBuilder("bar"), 3, 1),
            "append(StringBuilder, 3,) expected IndexOutOfBoundsException");

        assertThrows(IndexOutOfBoundsException.class, () -> sb.append(new StringBuilder("bar"), 1, -1),
            "append(StringBuilder,, -1) expected IndexOutOfBoundsException");

        assertThrows(IndexOutOfBoundsException.class, () -> sb.append(new StringBuilder("bar"), 1, 3),
            "append(StringBuilder, 1, 3) expected IndexOutOfBoundsException");

        assertThrows(IndexOutOfBoundsException.class, () -> sb.append(new StringBuilder("bar"), -1, 3),
            "append(StringBuilder, -1, 3) expected IndexOutOfBoundsException");

        assertThrows(IndexOutOfBoundsException.class, () -> sb.append(new StringBuilder("bar"), 4, 0),
            "append(StringBuilder, 4, 0) expected IndexOutOfBoundsException");

        sb.append(new StringBuilder("bar"), 3, 0);
        assertEquals("foo", sb.toString());

        sb.append(new StringBuilder("abcbardef"), 3, 3);
        assertEquals("foobar", sb.toString());

        sb.append(new StringBuilder("abcbardef"), 4, 3);
        assertEquals("foobarard", sb.toString());
    }

    @Test
    void testAppend_TextStringBuilder() {
        TextStringBuilder sb = new TextStringBuilder();
        sb.setNullText("NULL").append((TextStringBuilder) null);
        assertEquals("NULL", sb.toString());

        sb = new TextStringBuilder();
        sb.append(new TextStringBuilder("foo"));
        assertEquals("foo", sb.toString());

        sb.append(new TextStringBuilder(""));
        assertEquals("foo", sb.toString());

        sb.append(new TextStringBuilder("bar"));
        assertEquals("foobar", sb.toString());
    }

    @Test
    void testAppend_TextStringBuilder_int_int() {
        final TextStringBuilder sb0 = new TextStringBuilder();
        sb0.setNullText("NULL").append((TextStringBuilder) null, 0, 1);
        assertEquals("NULL", sb0.toString());

        final TextStringBuilder sb = new TextStringBuilder();
        sb.append(new TextStringBuilder("foo"), 0, 3);
        assertEquals("foo", sb.toString());

        assertThrows(IndexOutOfBoundsException.class, () -> sb.append(new TextStringBuilder("bar"), -1, 1),
            "append(char[], -1,) expected IndexOutOfBoundsException");

        assertThrows(IndexOutOfBoundsException.class, () -> sb.append(new TextStringBuilder("bar"), 3, 1),
            "append(char[], 3,) expected IndexOutOfBoundsException");

        assertThrows(IndexOutOfBoundsException.class, () -> sb.append(new TextStringBuilder("bar"), 1, -1),
            "append(char[],, -1) expected IndexOutOfBoundsException");

        assertThrows(IndexOutOfBoundsException.class, () -> sb.append(new TextStringBuilder("bar"), 1, 3),
            "append(char[], 1, 3) expected IndexOutOfBoundsException");

        assertThrows(IndexOutOfBoundsException.class, () -> sb.append(new TextStringBuilder("bar"), -1, 3),
            "append(char[], -1, 3) expected IndexOutOfBoundsException");

        assertThrows(IndexOutOfBoundsException.class, () -> sb.append(new TextStringBuilder("bar"), 4, 0),
            "append(char[], 4, 0) expected IndexOutOfBoundsException");

        sb.append(new TextStringBuilder("bar"), 3, 0);
        assertEquals("foo", sb.toString());

        sb.append(new TextStringBuilder("abcbardef"), 3, 3);
        assertEquals("foobar", sb.toString());
    }

    @Test
    void testAppendAll_Array() {
        final TextStringBuilder sb = new TextStringBuilder();
        sb.appendAll((Object[]) null);
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendAll();
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendAll("foo", "bar", "baz");
        assertEquals("foobarbaz", sb.toString());

        sb.clear();
        sb.appendAll("foo", "bar", "baz");
        assertEquals("foobarbaz", sb.toString());
    }

    @Test
    void testAppendAll_Collection() {
        final TextStringBuilder sb = new TextStringBuilder();
        sb.appendAll((Collection<?>) null);
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendAll(Collections.EMPTY_LIST);
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendAll(Arrays.asList("foo", "bar", "baz"));
        assertEquals("foobarbaz", sb.toString());
    }

    @Test
    void testAppendAll_Iterator() {
        final TextStringBuilder sb = new TextStringBuilder();
        sb.appendAll((Iterator<?>) null);
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendAll(Collections.EMPTY_LIST.iterator());
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendAll(Arrays.asList("foo", "bar", "baz").iterator());
        assertEquals("foobarbaz", sb.toString());
    }

    @Test
    void testAppendFixedWidthPadLeft() {
        final TextStringBuilder sb = new TextStringBuilder();
        sb.appendFixedWidthPadLeft("foo", -1, '-');
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadLeft("foo", 0, '-');
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadLeft("foo", 1, '-');
        assertEquals("o", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadLeft("foo", 2, '-');
        assertEquals("oo", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadLeft("foo", 3, '-');
        assertEquals("foo", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadLeft("foo", 4, '-');
        assertEquals("-foo", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadLeft("foo", 10, '-');
        assertEquals(10, sb.length());
        // 1234567890
        assertEquals("-------foo", sb.toString());

        sb.clear();
        sb.setNullText("null");
        sb.appendFixedWidthPadLeft(null, 5, '-');
        assertEquals("-null", sb.toString());
    }

    @Test
    void testAppendFixedWidthPadLeft_int() {
        final TextStringBuilder sb = new TextStringBuilder();
        sb.appendFixedWidthPadLeft(123, -1, '-');
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadLeft(123, 0, '-');
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadLeft(123, 1, '-');
        assertEquals("3", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadLeft(123, 2, '-');
        assertEquals("23", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadLeft(123, 3, '-');
        assertEquals("123", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadLeft(123, 4, '-');
        assertEquals("-123", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadLeft(123, 10, '-');
        assertEquals(10, sb.length());
        // 1234567890
        assertEquals("-------123", sb.toString());
    }

    @Test
    void testAppendFixedWidthPadRight() {
        final TextStringBuilder sb = new TextStringBuilder();
        sb.appendFixedWidthPadRight("foo", -1, '-');
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadRight("foo", 0, '-');
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadRight("foo", 1, '-');
        assertEquals("f", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadRight("foo", 2, '-');
        assertEquals("fo", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadRight("foo", 3, '-');
        assertEquals("foo", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadRight("foo", 4, '-');
        assertEquals("foo-", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadRight("foo", 10, '-');
        assertEquals(10, sb.length());
        // 1234567890
        assertEquals("foo-------", sb.toString());

        sb.clear();
        sb.setNullText("null");
        sb.appendFixedWidthPadRight(null, 5, '-');
        assertEquals("null-", sb.toString());
    }

    @Test
    void testAppendFixedWidthPadRight_int() {
        final TextStringBuilder sb = new TextStringBuilder();
        sb.appendFixedWidthPadRight(123, -1, '-');
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadRight(123, 0, '-');
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadRight(123, 1, '-');
        assertEquals("1", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadRight(123, 2, '-');
        assertEquals("12", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadRight(123, 3, '-');
        assertEquals("123", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadRight(123, 4, '-');
        assertEquals("123-", sb.toString());

        sb.clear();
        sb.appendFixedWidthPadRight(123, 10, '-');
        assertEquals(10, sb.length());
        // 1234567890
        assertEquals("123-------", sb.toString());
    }

    @Test
    void testAppendln_Boolean() {
        final TextStringBuilder sb = new TextStringBuilder();
        sb.appendln(true);
        assertEquals("true" + SEP, sb.toString());

        sb.clear();
        sb.appendln(false);
        assertEquals("false" + SEP, sb.toString());
    }

    @Test
    void testAppendln_CharArray() {
        final TextStringBuilder sb = spy(new TextStringBuilder());
        final char[] input = "foo".toCharArray();
        sb.appendln(input);

        assertEquals("foo" + SEP, sb.toString());

        verify(sb, times(1)).append(input);
        verify(sb, times(1)).appendNewLine();
    }

    @Test
    void testAppendln_CharArray_int_int() {
        final TextStringBuilder sb = spy(new TextStringBuilder());
        final char[] input = "foo".toCharArray();
        sb.appendln(input, 0, 3);

        assertEquals("foo" + SEP, sb.toString());

        verify(sb, times(1)).append(input, 0, 3);
        verify(sb, times(1)).appendNewLine();
    }

    @Test
    void testAppendln_FormattedString() {
        final TextStringBuilder sb = spy(new TextStringBuilder());
        sb.appendln("Hello %s", "Alice");

        assertEquals("Hello Alice" + SEP, sb.toString());

        verify(sb, times(2)).append(anyString()); // appendNewLine() calls append(String)
        verify(sb, times(1)).appendNewLine();
    }

    @Test
    void testAppendln_Object() {
        final TextStringBuilder sb = new TextStringBuilder();
        sb.appendln((Object) null);
        assertEquals("" + SEP, sb.toString());

        sb.appendln(FOO);
        assertEquals(SEP + "foo" + SEP, sb.toString());

        sb.appendln(Integer.valueOf(6));
        assertEquals(SEP + "foo" + SEP + "6" + SEP, sb.toString());
    }

    @Test
    void testAppendln_PrimitiveNumber() {
        final TextStringBuilder sb = new TextStringBuilder();
        sb.appendln(0);
        assertEquals("0" + SEP, sb.toString());

        sb.clear();
        sb.appendln(1L);
        assertEquals("1" + SEP, sb.toString());

        sb.clear();
        sb.appendln(2.3f);
        assertEquals("2.3" + SEP, sb.toString());

        sb.clear();
        sb.appendln(4.5d);
        assertEquals("4.5" + SEP, sb.toString());
    }

    @Test
    void testAppendln_String() {
        final TextStringBuilder sb = spy(new TextStringBuilder());
        sb.appendln("foo");

        assertEquals("foo" + SEP, sb.toString());

        verify(sb, times(2)).append(anyString()); // appendNewLine() calls append(String)
        verify(sb, times(1)).appendNewLine();
    }

    @Test
    void testAppendln_String_int_int() {
        final TextStringBuilder sb = spy(new TextStringBuilder());
        sb.appendln("foo", 0, 3);

        assertEquals("foo" + SEP, sb.toString());

        verify(sb, times(2)).append(anyString(), anyInt(), anyInt()); // appendNewLine() calls append(String)
        verify(sb, times(1)).appendNewLine();
    }

    @Test
    void testAppendln_StringBuffer() {
        final TextStringBuilder sb = spy(new TextStringBuilder());
        sb.appendln(new StringBuffer("foo"));

        assertEquals("foo" + SEP, sb.toString());

        verify(sb, times(1)).append(any(StringBuffer.class));
        verify(sb, times(1)).appendNewLine();
    }

    @Test
    void testAppendln_StringBuffer_int_int() {
        final TextStringBuilder sb = spy(new TextStringBuilder());
        sb.appendln(new StringBuffer("foo"), 0, 3);

        assertEquals("foo" + SEP, sb.toString());

        verify(sb, times(1)).append(any(StringBuffer.class), anyInt(), anyInt());
        verify(sb, times(1)).appendNewLine();
    }

    @Test
    void testAppendln_StringBuilder() {
        final TextStringBuilder sb = spy(new TextStringBuilder());
        sb.appendln(new StringBuilder("foo"));

        assertEquals("foo" + SEP, sb.toString());

        verify(sb, times(1)).append(any(StringBuilder.class));
        verify(sb, times(1)).appendNewLine();
    }

    @Test
    void testAppendln_StringBuilder_int_int() {
        final TextStringBuilder sb = spy(new TextStringBuilder());
        sb.appendln(new StringBuilder("foo"), 0, 3);

        assertEquals("foo" + SEP, sb.toString());

        verify(sb, times(1)).append(any(StringBuilder.class), anyInt(), anyInt());
        verify(sb, times(1)).appendNewLine();
    }

    @Test
    void testAppendln_TextStringBuilder() {
        final TextStringBuilder sb = spy(new TextStringBuilder());
        sb.appendln(new TextStringBuilder("foo"));

        assertEquals("foo" + SEP, sb.toString());

        verify(sb, times(1)).append(any(TextStringBuilder.class));
        verify(sb, times(1)).appendNewLine();
    }

    @Test
    void testAppendln_TextStringBuilder_int_int() {
        final TextStringBuilder sb = spy(new TextStringBuilder());
        sb.appendln(new TextStringBuilder("foo"), 0, 3);

        assertEquals("foo" + SEP, sb.toString());

        verify(sb, times(1)).append(any(TextStringBuilder.class), anyInt(), anyInt());
        verify(sb, times(1)).appendNewLine();
    }

    @Test
    void testAppendNewLine() {
        TextStringBuilder sb = new TextStringBuilder("---");
        sb.appendNewLine().append("+++");
        assertEquals("---" + SEP + "+++", sb.toString());

        sb = new TextStringBuilder("---");
        sb.setNewLineText("#").appendNewLine().setNewLineText(null).appendNewLine();
        assertEquals("---#" + SEP, sb.toString());
    }

    @Test
    void testAppendPadding() {
        final TextStringBuilder sb = new TextStringBuilder();
        sb.append("foo");
        assertEquals("foo", sb.toString());

        sb.appendPadding(-1, '-');
        assertEquals("foo", sb.toString());

        sb.appendPadding(0, '-');
        assertEquals("foo", sb.toString());

        sb.appendPadding(1, '-');
        assertEquals("foo-", sb.toString());

        sb.appendPadding(16, '-');
        assertEquals(20, sb.length());
        // 12345678901234567890
        assertEquals("foo-----------------", sb.toString());
    }

    @Test
    void testAppendSeparator_char() {
        final TextStringBuilder sb = new TextStringBuilder();
        sb.appendSeparator(','); // no effect
        assertEquals("", sb.toString());
        sb.append("foo");
        assertEquals("foo", sb.toString());
        sb.appendSeparator(',');
        assertEquals("foo,", sb.toString());
    }

    @Test
    void testAppendSeparator_char_char() {
        final TextStringBuilder sb = new TextStringBuilder();
        final char startSeparator = ':';
        final char standardSeparator = ',';
        final String foo = "foo";
        sb.appendSeparator(standardSeparator, startSeparator); // no effect
        assertEquals(String.valueOf(startSeparator), sb.toString());
        sb.append(foo);
        assertEquals(String.valueOf(startSeparator) + foo, sb.toString());
        sb.appendSeparator(standardSeparator, startSeparator);
        assertEquals(String.valueOf(startSeparator) + foo + standardSeparator, sb.toString());
    }

    @Test
    void testAppendSeparator_char_int() {
        final TextStringBuilder sb = new TextStringBuilder();
        sb.appendSeparator(',', 0); // no effect
        assertEquals("", sb.toString());
        sb.append("foo");
        assertEquals("foo", sb.toString());
        sb.appendSeparator(',', 1);
        assertEquals("foo,", sb.toString());

        sb.appendSeparator(',', -1); // no effect
        assertEquals("foo,", sb.toString());
    }

    @Test
    void testAppendSeparator_String() {
        final TextStringBuilder sb = new TextStringBuilder();
        sb.appendSeparator(","); // no effect
        assertEquals("", sb.toString());
        sb.append("foo");
        assertEquals("foo", sb.toString());
        sb.appendSeparator(",");
        assertEquals("foo,", sb.toString());
    }

    @Test
    void testAppendSeparator_String_int() {
        final TextStringBuilder sb = new TextStringBuilder();
        sb.appendSeparator(null, -1); // no effect
        assertEquals("", sb.toString());
        sb.appendSeparator(null, 0); // no effect
        assertEquals("", sb.toString());
        sb.appendSeparator(null, 1); // no effect
        assertEquals("", sb.toString());
        sb.appendSeparator(",", -1); // no effect
        assertEquals("", sb.toString());
        sb.appendSeparator(",", 0); // no effect
        assertEquals("", sb.toString());
        sb.append("foo");
        assertEquals("foo", sb.toString());
        sb.appendSeparator(",", 1);
        assertEquals("foo,", sb.toString());

        sb.appendSeparator(",", -1); // no effect
        assertEquals("foo,", sb.toString());
    }

    @Test
    void testAppendSeparator_String_String() {
        final TextStringBuilder sb = new TextStringBuilder();
        final String startSeparator = "order by ";
        final String standardSeparator = ",";
        final String foo = "foo";
        sb.appendSeparator(null, null);
        assertEquals("", sb.toString());
        sb.appendSeparator(standardSeparator, null);
        assertEquals("", sb.toString());
        sb.appendSeparator(standardSeparator, startSeparator);
        assertEquals(startSeparator, sb.toString());
        sb.appendSeparator(null, null);
        assertEquals(startSeparator, sb.toString());
        sb.appendSeparator(null, startSeparator);
        assertEquals(startSeparator, sb.toString());
        sb.append(foo);
        assertEquals(startSeparator + foo, sb.toString());
        sb.appendSeparator(standardSeparator, startSeparator);
        assertEquals(startSeparator + foo + standardSeparator, sb.toString());
    }

    @Test
    void testAppendWithNullText() {
        final TextStringBuilder sb = new TextStringBuilder();
        sb.setNullText("NULL");
        assertEquals("", sb.toString());

        sb.appendNull();
        assertEquals("NULL", sb.toString());

        sb.append((Object) null);
        assertEquals("NULLNULL", sb.toString());

        sb.append(FOO);
        assertEquals("NULLNULLfoo", sb.toString());

        sb.append((String) null);
        assertEquals("NULLNULLfooNULL", sb.toString());

        sb.append("");
        assertEquals("NULLNULLfooNULL", sb.toString());

        sb.append("bar");
        assertEquals("NULLNULLfooNULLbar", sb.toString());

        sb.append((StringBuffer) null);
        assertEquals("NULLNULLfooNULLbarNULL", sb.toString());

        sb.append(new StringBuffer("baz"));
        assertEquals("NULLNULLfooNULLbarNULLbaz", sb.toString());
    }

    @Test
    void testAppendWithSeparators_Array() {
        final TextStringBuilder sb = new TextStringBuilder();
        sb.appendWithSeparators((Object[]) null, ",");
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendWithSeparators(ArrayUtils.EMPTY_OBJECT_ARRAY, ",");
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendWithSeparators(new Object[] {"foo", "bar", "baz"}, ",");
        assertEquals("foo,bar,baz", sb.toString());

        sb.clear();
        sb.appendWithSeparators(new Object[] {"foo", "bar", "baz"}, null);
        assertEquals("foobarbaz", sb.toString());

        sb.clear();
        sb.appendWithSeparators(new Object[] {"foo", null, "baz"}, ",");
        assertEquals("foo,,baz", sb.toString());
    }

    @Test
    void testAppendWithSeparators_Collection() {
        final TextStringBuilder sb = new TextStringBuilder();
        sb.appendWithSeparators((Collection<?>) null, ",");
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendWithSeparators(Collections.EMPTY_LIST, ",");
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendWithSeparators(Arrays.asList("foo", "bar", "baz"), ",");
        assertEquals("foo,bar,baz", sb.toString());

        sb.clear();
        sb.appendWithSeparators(Arrays.asList("foo", "bar", "baz"), null);
        assertEquals("foobarbaz", sb.toString());

        sb.clear();
        sb.appendWithSeparators(Arrays.asList("foo", null, "baz"), ",");
        assertEquals("foo,,baz", sb.toString());
    }

    @Test
    void testAppendWithSeparators_Iterator() {
        final TextStringBuilder sb = new TextStringBuilder();
        sb.appendWithSeparators((Iterator<?>) null, ",");
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendWithSeparators(Collections.EMPTY_LIST.iterator(), ",");
        assertEquals("", sb.toString());

        sb.clear();
        sb.appendWithSeparators(Arrays.asList("foo", "bar", "baz").iterator(), ",");
        assertEquals("foo,bar,baz", sb.toString());

        sb.clear();
        sb.appendWithSeparators(Arrays.asList("foo", "bar", "baz").iterator(), null);
        assertEquals("foobarbaz", sb.toString());

        sb.clear();
        sb.appendWithSeparators(Arrays.asList("foo", null, "baz").iterator(), ",");
        assertEquals("foo,,baz", sb.toString());
    }

    @Test
    void testAppendWithSeparatorsWithNullText() {
        final TextStringBuilder sb = new TextStringBuilder();
        sb.setNullText("null");
        sb.appendWithSeparators(new Object[] {"foo", null, "baz"}, ",");
        assertEquals("foo,null,baz", sb.toString());

        sb.clear();
        sb.appendWithSeparators(Arrays.asList("foo", null, "baz"), ",");
        assertEquals("foo,null,baz", sb.toString());
    }

    @Test
    void testInsert() {

        final TextStringBuilder sb = new TextStringBuilder();
        sb.append("barbaz");
        assertEquals("barbaz", sb.toString());

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(-1, FOO));

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(7, FOO));

        sb.insert(0, (Object) null);
        assertEquals("barbaz", sb.toString());

        sb.insert(0, FOO);
        assertEquals("foobarbaz", sb.toString());

        sb.clear();
        sb.append("barbaz");
        assertEquals("barbaz", sb.toString());

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(-1, "foo"));

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(7, "foo"));

        sb.insert(0, (String) null);
        assertEquals("barbaz", sb.toString());

        sb.insert(0, "foo");
        assertEquals("foobarbaz", sb.toString());

        sb.clear();
        sb.append("barbaz");
        assertEquals("barbaz", sb.toString());

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(-1, new char[] {'f', 'o', 'o'}));

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(7, new char[] {'f', 'o', 'o'}));

        sb.insert(0, (char[]) null);
        assertEquals("barbaz", sb.toString());

        sb.insert(0, ArrayUtils.EMPTY_CHAR_ARRAY);
        assertEquals("barbaz", sb.toString());

        sb.insert(0, new char[] {'f', 'o', 'o'});
        assertEquals("foobarbaz", sb.toString());

        sb.clear();
        sb.append("barbaz");
        assertEquals("barbaz", sb.toString());

        assertThrows(IndexOutOfBoundsException.class,
            () -> sb.insert(-1, new char[] {'a', 'b', 'c', 'f', 'o', 'o', 'd', 'e', 'f'}, 3, 3));

        assertThrows(IndexOutOfBoundsException.class,
            () -> sb.insert(7, new char[] {'a', 'b', 'c', 'f', 'o', 'o', 'd', 'e', 'f'}, 3, 3));

        sb.insert(0, (char[]) null, 0, 0);
        assertEquals("barbaz", sb.toString());

        sb.insert(0, ArrayUtils.EMPTY_CHAR_ARRAY, 0, 0);
        assertEquals("barbaz", sb.toString());

        assertThrows(IndexOutOfBoundsException.class,
            () -> sb.insert(0, new char[] {'a', 'b', 'c', 'f', 'o', 'o', 'd', 'e', 'f'}, -1, 3));

        assertThrows(IndexOutOfBoundsException.class,
            () -> sb.insert(0, new char[] {'a', 'b', 'c', 'f', 'o', 'o', 'd', 'e', 'f'}, 10, 3));

        assertThrows(IndexOutOfBoundsException.class,
            () -> sb.insert(0, new char[] {'a', 'b', 'c', 'f', 'o', 'o', 'd', 'e', 'f'}, 0, -1));

        assertThrows(IndexOutOfBoundsException.class,
            () -> sb.insert(0, new char[] {'a', 'b', 'c', 'f', 'o', 'o', 'd', 'e', 'f'}, 0, 10));

        sb.insert(0, new char[] {'a', 'b', 'c', 'f', 'o', 'o', 'd', 'e', 'f'}, 0, 0);
        assertEquals("barbaz", sb.toString());

        sb.insert(0, new char[] {'a', 'b', 'c', 'f', 'o', 'o', 'd', 'e', 'f'}, 3, 3);
        assertEquals("foobarbaz", sb.toString());

        sb.clear();
        sb.append("barbaz");
        assertEquals("barbaz", sb.toString());

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(-1, true));

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(7, true));

        sb.insert(0, true);
        assertEquals("truebarbaz", sb.toString());

        sb.insert(0, false);
        assertEquals("falsetruebarbaz", sb.toString());

        sb.clear();
        sb.append("barbaz");
        assertEquals("barbaz", sb.toString());

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(-1, '!'));

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(7, '!'));

        sb.insert(0, '!');
        assertEquals("!barbaz", sb.toString());

        sb.clear();
        sb.append("barbaz");
        assertEquals("barbaz", sb.toString());

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(-1, 0));

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(7, 0));

        sb.insert(0, '0');
        assertEquals("0barbaz", sb.toString());

        sb.clear();
        sb.append("barbaz");
        assertEquals("barbaz", sb.toString());

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(-1, 1L));

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(7, 1L));

        sb.insert(0, 1L);
        assertEquals("1barbaz", sb.toString());

        sb.clear();
        sb.append("barbaz");
        assertEquals("barbaz", sb.toString());

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(-1, 2.3F));

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(7, 2.3F));

        sb.insert(0, 2.3F);
        assertEquals("2.3barbaz", sb.toString());

        sb.clear();
        sb.append("barbaz");
        assertEquals("barbaz", sb.toString());

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(-1, 4.5D));

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(7, 4.5D));

        sb.insert(0, 4.5D);
        assertEquals("4.5barbaz", sb.toString());
    }

    @Test
    void testInsertAtEnd() {
        final TextStringBuilder sb = new TextStringBuilder();
        assertEquals("", sb.toString());

        sb.insert(0, "Hello");
        assertEquals("Hello", sb.toString());

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(-1, "World"));
        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(6, "World"));

        sb.insert(5, true);
        assertEquals("Hellotrue", sb.toString());

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(10, false));
        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(-20, false));

        sb.insert(9, 'A');
        assertEquals("HellotrueA", sb.toString());

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(11, 'B'));
        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(-2, 'B'));

        sb.insert(10, new char[] { 'B' , 'C' });
        assertEquals("HellotrueABC", sb.toString());

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(13, new char[] { 'D', 'E' }));
        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(-1, new char[] { 'D', 'E' }));

        sb.insert(12, new char[] { 'D', 'E', 'F' }, 1, 1);
        assertEquals("HellotrueABCE", sb.toString());

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(14, new char[] { 'G', 'H', 'I' }, 1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(-1, new char[] { 'G', 'H', 'I' }, 1, 1));

        sb.insert(13, 1.2d);
        assertEquals("HellotrueABCE1.2", sb.toString());

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(17, 1.3d));
        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(-1, 1.3d));

        sb.insert(16, 1f);
        assertEquals("HellotrueABCE1.21.0", sb.toString());

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(20, 1.3f));
        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(-3,  1.3f));

        sb.insert(19, 23);
        assertEquals("HellotrueABCE1.21.023", sb.toString());

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(22, 20));
        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(-5, -5));

        sb.insert(21, 99L);
        assertEquals("HellotrueABCE1.21.02399", sb.toString());

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(24, 22L));
        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(-1, -1L));

        sb.insert(23, FOO);
        assertEquals("HellotrueABCE1.21.02399foo", sb.toString());

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(27, FOO));
        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(-3, FOO));
    }

    @Test
    void testInsertWithNullText() {
        final TextStringBuilder sb = new TextStringBuilder();
        sb.setNullText("null");
        sb.append("barbaz");
        assertEquals("barbaz", sb.toString());

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(-1, FOO));

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(7, FOO));

        sb.insert(0, (Object) null);
        assertEquals("nullbarbaz", sb.toString());

        sb.insert(0, FOO);
        assertEquals("foonullbarbaz", sb.toString());

        sb.clear();
        sb.append("barbaz");
        assertEquals("barbaz", sb.toString());

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(-1, "foo"));

        assertThrows(IndexOutOfBoundsException.class, () -> sb.insert(7, "foo"));

        sb.insert(0, (String) null);
        assertEquals("nullbarbaz", sb.toString());

        sb.insert(0, "foo");
        assertEquals("foonullbarbaz", sb.toString());

        sb.insert(0, (char[]) null);
        assertEquals("nullfoonullbarbaz", sb.toString());

        sb.insert(0, (char[]) null, 0, 0);
        assertEquals("nullnullfoonullbarbaz", sb.toString());
    }

    /** See: https://issues.apache.org/jira/browse/LANG-299 */
    @Test
    void testLang299() {
        final TextStringBuilder sb = new TextStringBuilder(1);
        sb.appendFixedWidthPadRight("foo", 1, '-');
        assertEquals("f", sb.toString());
    }
}
