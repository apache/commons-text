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

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.commons.text.matcher.StringMatcher;
import org.apache.commons.text.matcher.StringMatcherFactory;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link TextStringBuilder}.
 */
public class TextStringBuilderTest {

    private static class MockReadable implements Readable {

        private final CharBuffer src;

        MockReadable(final String src) {
            this.src = CharBuffer.wrap(src);
        }

        @Override
        public int read(final CharBuffer cb) throws IOException {
            return src.read(cb);
        }
    }

    static final StringMatcher A_NUMBER_MATCHER = (buffer, start, bufferStart, bufferEnd) -> {
        if (buffer[start] == 'A') {
            start++;
            if (start < bufferEnd && buffer[start] >= '0' && buffer[start] <= '9') {
                return 2;
            }
        }
        return 0;
    };

    @Test
    public void test_LANG_1131_EqualsWithNullTextStringBuilder() throws Exception {
        final TextStringBuilder sb = new TextStringBuilder();
        final TextStringBuilder other = null;
        assertFalse(sb.equals(other));
    }

    // -----------------------------------------------------------------------
    @Test
    public void testAppendCharBuffer() {
        final TextStringBuilder sb1 = new TextStringBuilder();
        final CharBuffer buf = CharBuffer.allocate(10);
        buf.append("0123456789");
        buf.flip();
        sb1.append(buf);
        assertEquals("0123456789", sb1.toString());

        final TextStringBuilder sb2 = new TextStringBuilder();
        sb2.append(buf, 1, 8);
        assertEquals("12345678", sb2.toString());
    }

    @Test
    public void testAppendCharBufferException() throws Exception {
        final TextStringBuilder sb = new TextStringBuilder("1234567890");
        final String text = "Test";
        final CharBuffer buffer = CharBuffer.allocate(sb.size() + text.length());
        buffer.put(text);
        buffer.flip();
        try {
            sb.append(buffer, -1, 12);
        } catch (final StringIndexOutOfBoundsException e) {
            assertEquals("startIndex must be valid", e.getMessage());
        }

        try {
            sb.append(buffer, 0, -1);
        } catch (final StringIndexOutOfBoundsException e) {
            assertEquals("length must be valid", e.getMessage());
        }

        sb.append(buffer);
        assertEquals("1234567890Test", sb.toString());
    }

    @Test
    public void testAppendCharBufferNull() throws Exception {
        final TextStringBuilder sb = new TextStringBuilder("1234567890");
        final CharBuffer buffer = null;
        sb.append(buffer);
        assertEquals("1234567890", sb.toString());

        final TextStringBuilder sb1 = new TextStringBuilder("1234567890");
        final CharBuffer buffer1 = null;
        sb.append(buffer1, 0, 0);
        assertEquals("1234567890", sb1.toString());
    }

    @Test
    public void testAppendCharSequence() {
        final CharSequence obj0 = null;
        final CharSequence obj1 = new TextStringBuilder("test1");
        final CharSequence obj2 = new StringBuilder("test2");
        final CharSequence obj3 = new StringBuffer("test3");
        final CharBuffer obj4 = CharBuffer.wrap("test4".toCharArray());

        final TextStringBuilder sb0 = new TextStringBuilder();
        assertEquals("", sb0.append(obj0).toString());

        final TextStringBuilder sb1 = new TextStringBuilder();
        assertEquals("test1", sb1.append(obj1).toString());

        final TextStringBuilder sb2 = new TextStringBuilder();
        assertEquals("test2", sb2.append(obj2).toString());

        final TextStringBuilder sb3 = new TextStringBuilder();
        assertEquals("test3", sb3.append(obj3).toString());

        final TextStringBuilder sb4 = new TextStringBuilder();
        assertEquals("test4", sb4.append(obj4).toString());

        final TextStringBuilder sb5 = new TextStringBuilder();
        assertEquals("", sb5.append(obj0, 0, 0).toString());
    }

    @Test
    public void testAppendln() {
        final TextStringBuilder sb1 = new TextStringBuilder();
        final char ch = 'c';
        assertEquals("c" + System.lineSeparator(), sb1.appendln(ch).toString());
    }

    @Test
    public void testAppendStringBuilderNull() {
        final TextStringBuilder sb1 = new TextStringBuilder();
        final StringBuilder b = null;
        assertEquals("", sb1.append(b).toString());

        final TextStringBuilder sb2 = new TextStringBuilder();
        assertEquals("", sb2.append(b, 0, 0).toString());
    }

    @Test
    public void testAppendTakingTwoIntsWithIndexOutOfBoundsThrowsStringIndexOutOfBoundsExceptionTwo() {
        assertThatExceptionOfType(StringIndexOutOfBoundsException.class).isThrownBy(() -> {
            final Charset charset = Charset.defaultCharset();
            final ByteBuffer byteBuffer = charset.encode("asdf");
            final CharBuffer charBuffer = charset.decode(byteBuffer);

            new TextStringBuilder().append(charBuffer, 933, 654);
        });
    }

    @Test
    public void testAppendTakingTwoIntsWithZeroThrowsStringIndexOutOfBoundsException() {
        assertThatExceptionOfType(StringIndexOutOfBoundsException.class).isThrownBy(() -> {
            final Charset charset = Charset.defaultCharset();
            final ByteBuffer byteBuffer = charset.encode("end < start");
            final CharBuffer charBuffer = charset.decode(byteBuffer);

            new TextStringBuilder(630).append(charBuffer, 0, 630);
        });
    }

    @Test
    public void testAppendToCharBuffer() throws Exception {
        final TextStringBuilder sb = new TextStringBuilder("1234567890");
        final String text = "Test ";
        final CharBuffer buffer = CharBuffer.allocate(sb.size() + text.length());
        buffer.put(text);

        sb.appendTo(buffer);

        buffer.flip();
        assertEquals("Test 1234567890", buffer.toString());
    }

    @Test
    public void testAppendToStringBuffer() throws Exception {
        final TextStringBuilder sb = new TextStringBuilder("1234567890");
        final StringBuffer buffer = new StringBuffer("Test ");

        sb.appendTo(buffer);

        assertEquals("Test 1234567890", buffer.toString());
    }

    @Test
    public void testAppendToStringBuilder() throws Exception {
        final TextStringBuilder sb = new TextStringBuilder("1234567890");
        final StringBuilder builder = new StringBuilder("Test ");

        sb.appendTo(builder);

        assertEquals("Test 1234567890", builder.toString());
    }

    // -----------------------------------------------------------------------
    @Test
    public void testAppendToWriter() throws Exception {
        final TextStringBuilder sb = new TextStringBuilder("1234567890");
        final StringWriter writer = new StringWriter();
        writer.append("Test ");

        sb.appendTo(writer);

        assertEquals("Test 1234567890", writer.toString());
    }

    @Test
    public void testAsBuilder() {
        final TextStringBuilder sb = new TextStringBuilder().appendAll("Lorem", " ", "ipsum", " ", "dolor");
        assertEquals(sb.toString(), sb.build());
    }

    // -----------------------------------------------------------------------
    @Test
    public void testAsReader() throws Exception {
        final TextStringBuilder sb = new TextStringBuilder("some text");
        try (Reader reader = sb.asReader()) {
            assertTrue(reader.ready());
            final char[] buf = new char[40];
            assertEquals(9, reader.read(buf));
            assertEquals("some text", new String(buf, 0, 9));

            assertEquals(-1, reader.read());
            assertFalse(reader.ready());
            assertEquals(0, reader.skip(2));
            assertEquals(0, reader.skip(-1));

            assertTrue(reader.markSupported());
        }
        try (Reader reader = sb.asReader()) {
            assertEquals('s', reader.read());
            reader.mark(-1);
            final char[] array = new char[3];
            assertEquals(3, reader.read(array, 0, 3));
            assertEquals('o', array[0]);
            assertEquals('m', array[1]);
            assertEquals('e', array[2]);
            reader.reset();
            assertEquals(1, reader.read(array, 1, 1));
            assertEquals('o', array[0]);
            assertEquals('o', array[1]);
            assertEquals('e', array[2]);
            assertEquals(2, reader.skip(2));
            assertEquals(' ', reader.read());

            assertTrue(reader.ready());
            reader.close();
            assertTrue(reader.ready());
        }
        try (Reader reader = sb.asReader()) {
            char[] array = new char[3];
            try {
                reader.read(array, -1, 0);
                fail("Exception expected!");
            } catch (final IndexOutOfBoundsException ex) {
                // expected
            }
            try {
                reader.read(array, 0, -1);
                fail("Exception expected!");
            } catch (final IndexOutOfBoundsException ex) {
                // expected
            }
            try {
                reader.read(array, 100, 1);
                fail("Exception expected!");
            } catch (final IndexOutOfBoundsException ex) {
                // expected
            }
            try {
                reader.read(array, 0, 100);
                fail("Exception expected!");
            } catch (final IndexOutOfBoundsException ex) {
                // expected
            }
            try {
                reader.read(array, Integer.MAX_VALUE, Integer.MAX_VALUE);
                fail("Exception expected!");
            } catch (final IndexOutOfBoundsException ex) {
                // expected
            }

            assertEquals(0, reader.read(array, 0, 0));
            assertEquals(0, array[0]);
            assertEquals(0, array[1]);
            assertEquals(0, array[2]);

            reader.skip(9);
            assertEquals(-1, reader.read(array, 0, 1));

            reader.reset();
            array = new char[30];
            assertEquals(9, reader.read(array, 0, 30));
        }
    }

    // -----------------------------------------------------------------------
    @Test
    public void testAsTokenizer() throws Exception {
        // from Javadoc
        final TextStringBuilder b = new TextStringBuilder();
        b.append("a b ");
        final StringTokenizer t = b.asTokenizer();

        final String[] tokens1 = t.getTokenArray();
        assertEquals(2, tokens1.length);
        assertEquals("a", tokens1[0]);
        assertEquals("b", tokens1[1]);
        assertEquals(2, t.size());

        b.append("c d ");
        final String[] tokens2 = t.getTokenArray();
        assertEquals(2, tokens2.length);
        assertEquals("a", tokens2[0]);
        assertEquals("b", tokens2[1]);
        assertEquals(2, t.size());
        assertEquals("a", t.next());
        assertEquals("b", t.next());

        t.reset();
        final String[] tokens3 = t.getTokenArray();
        assertEquals(4, tokens3.length);
        assertEquals("a", tokens3[0]);
        assertEquals("b", tokens3[1]);
        assertEquals("c", tokens3[2]);
        assertEquals("d", tokens3[3]);
        assertEquals(4, t.size());
        assertEquals("a", t.next());
        assertEquals("b", t.next());
        assertEquals("c", t.next());
        assertEquals("d", t.next());

        assertEquals("a b c d ", t.getContent());
    }

    // -----------------------------------------------------------------------
    @Test
    public void testAsWriter() throws Exception {
        final TextStringBuilder sb = new TextStringBuilder("base");
        try (Writer writer = sb.asWriter()) {

            writer.write('l');
            assertEquals("basel", sb.toString());

            writer.write(new char[] {'i', 'n'});
            assertEquals("baselin", sb.toString());

            writer.write(new char[] {'n', 'e', 'r'}, 1, 2);
            assertEquals("baseliner", sb.toString());

            writer.write(" rout");
            assertEquals("baseliner rout", sb.toString());

            writer.write("ping that server", 1, 3);
            assertEquals("baseliner routing", sb.toString());

            writer.flush(); // no effect
            assertEquals("baseliner routing", sb.toString());

            writer.close(); // no effect
            assertEquals("baseliner routing", sb.toString());

            writer.write(" hi"); // works after close
            assertEquals("baseliner routing hi", sb.toString());

            sb.setLength(4); // mix and match
            writer.write('d');
            assertEquals("based", sb.toString());
        }
    }

    // -----------------------------------------------------------------------
    @Test
    public void testCapacity() {
        final TextStringBuilder sb = new TextStringBuilder();
        assertEquals(sb.getBuffer().length, sb.capacity());

        sb.append("HelloWorldHelloWorldHelloWorldHelloWorld");
        assertEquals(sb.getBuffer().length, sb.capacity());
    }

    // -----------------------------------------------------------------------
    @Test
    public void testCapacityAndLength() {
        final TextStringBuilder sb = new TextStringBuilder();
        assertEquals(32, sb.capacity());
        assertEquals(0, sb.length());
        assertEquals(0, sb.size());
        assertTrue(sb.isEmpty());

        sb.minimizeCapacity();
        assertEquals(0, sb.capacity());
        assertEquals(0, sb.length());
        assertEquals(0, sb.size());
        assertTrue(sb.isEmpty());

        sb.ensureCapacity(32);
        assertTrue(sb.capacity() >= 32);
        assertEquals(0, sb.length());
        assertEquals(0, sb.size());
        assertTrue(sb.isEmpty());

        sb.append("foo");
        assertTrue(sb.capacity() >= 32);
        assertEquals(3, sb.length());
        assertEquals(3, sb.size());
        assertFalse(sb.isEmpty());

        sb.clear();
        assertTrue(sb.capacity() >= 32);
        assertEquals(0, sb.length());
        assertEquals(0, sb.size());
        assertTrue(sb.isEmpty());

        sb.append("123456789012345678901234567890123");
        assertTrue(sb.capacity() > 32);
        assertEquals(33, sb.length());
        assertEquals(33, sb.size());
        assertFalse(sb.isEmpty());

        sb.ensureCapacity(16);
        assertTrue(sb.capacity() > 16);
        assertEquals(33, sb.length());
        assertEquals(33, sb.size());
        assertFalse(sb.isEmpty());

        sb.minimizeCapacity();
        assertEquals(33, sb.capacity());
        assertEquals(33, sb.length());
        assertEquals(33, sb.size());
        assertFalse(sb.isEmpty());

        assertThrows(IndexOutOfBoundsException.class, () -> sb.setLength(-1));

        sb.setLength(33);
        assertEquals(33, sb.capacity());
        assertEquals(33, sb.length());
        assertEquals(33, sb.size());
        assertFalse(sb.isEmpty());

        sb.setLength(16);
        assertTrue(sb.capacity() >= 16);
        assertEquals(16, sb.length());
        assertEquals(16, sb.size());
        assertEquals("1234567890123456", sb.toString());
        assertFalse(sb.isEmpty());

        sb.setLength(32);
        assertTrue(sb.capacity() >= 32);
        assertEquals(32, sb.length());
        assertEquals(32, sb.size());
        assertEquals("1234567890123456\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0", sb.toString());
        assertFalse(sb.isEmpty());

        sb.setLength(0);
        assertTrue(sb.capacity() >= 32);
        assertEquals(0, sb.length());
        assertEquals(0, sb.size());
        assertTrue(sb.isEmpty());
    }

    // -----------------------------------------------------------------------
    @Test
    public void testChaining() {
        final TextStringBuilder sb = new TextStringBuilder();
        assertSame(sb, sb.setNewLineText(null));
        assertSame(sb, sb.setNullText(null));
        assertSame(sb, sb.setLength(1));
        assertSame(sb, sb.setCharAt(0, 'a'));
        assertSame(sb, sb.ensureCapacity(0));
        assertSame(sb, sb.minimizeCapacity());
        assertSame(sb, sb.clear());
        assertSame(sb, sb.reverse());
        assertSame(sb, sb.trim());
    }

    // -----------------------------------------------------------------------
    @Test
    public void testCharAt() {
        final TextStringBuilder sb = new TextStringBuilder();
        assertThrows(IndexOutOfBoundsException.class, () -> sb.charAt(0));
        assertThrows(IndexOutOfBoundsException.class, () -> sb.charAt(-1));
        sb.append("foo");
        assertEquals('f', sb.charAt(0));
        assertEquals('o', sb.charAt(1));
        assertEquals('o', sb.charAt(2));
        assertThrows(IndexOutOfBoundsException.class, () -> sb.charAt(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> sb.charAt(3));
    }

    @Test
    public void testCharAtDelete() {
        final String str = "abc";
        //
        final TextStringBuilder sb1 = new TextStringBuilder(str);
        assertEquals('a', sb1.charAtDelete(0));
        assertEquals("bc", sb1.toString());
        //
        final TextStringBuilder sb2 = new TextStringBuilder(str);
        assertEquals('c', sb2.charAtDelete(str.length() - 1));
        assertEquals("ab", sb2.toString());
        //
        final TextStringBuilder sb3 = new TextStringBuilder(str);
        assertThrows(IndexOutOfBoundsException.class, () -> sb3.charAtDelete(str.length()));
        assertThrows(IndexOutOfBoundsException.class, () -> sb3.charAtDelete(1000));
    }

    @Test
    public void testClear() {
        final TextStringBuilder sb = new TextStringBuilder();
        sb.append("Hello");
        sb.clear();
        assertEquals(0, sb.length());
        assertTrue(sb.getBuffer().length >= 5);
    }

    @Test
    public void testConstructorCharSequence() {
        final CharBuffer str = CharBuffer.wrap("A");
        final int length = str.length();
        final TextStringBuilder sb = new TextStringBuilder(str);
        assertEquals(TextStringBuilder.CAPACITY + length, sb.capacity());
        assertEquals(length, sb.toCharArray().length);
    }

    @Test
    public void testConstructorDefault() {
        final TextStringBuilder sb = new TextStringBuilder();
        assertEquals(TextStringBuilder.CAPACITY, sb.capacity());
        assertEquals(0, sb.toCharArray().length);
    }

    // -----------------------------------------------------------------------
    @Test
    public void testConstructors() {
        final TextStringBuilder sb0 = new TextStringBuilder();
        assertEquals(32, sb0.capacity());
        assertEquals(0, sb0.length());
        assertEquals(0, sb0.size());

        final TextStringBuilder sb1 = new TextStringBuilder(32);
        assertEquals(32, sb1.capacity());
        assertEquals(0, sb1.length());
        assertEquals(0, sb1.size());

        final TextStringBuilder sb2 = new TextStringBuilder(0);
        assertEquals(32, sb2.capacity());
        assertEquals(0, sb2.length());
        assertEquals(0, sb2.size());

        final TextStringBuilder sb3 = new TextStringBuilder(-1);
        assertEquals(32, sb3.capacity());
        assertEquals(0, sb3.length());
        assertEquals(0, sb3.size());

        final TextStringBuilder sb4 = new TextStringBuilder(1);
        assertEquals(1, sb4.capacity());
        assertEquals(0, sb4.length());
        assertEquals(0, sb4.size());

        final TextStringBuilder sb5 = new TextStringBuilder((String) null);
        assertEquals(32, sb5.capacity());
        assertEquals(0, sb5.length());
        assertEquals(0, sb5.size());

        final TextStringBuilder sb6 = new TextStringBuilder("");
        assertEquals(32, sb6.capacity());
        assertEquals(0, sb6.length());
        assertEquals(0, sb6.size());

        final TextStringBuilder sb7 = new TextStringBuilder("foo");
        assertEquals(35, sb7.capacity());
        assertEquals(3, sb7.length());
        assertEquals(3, sb7.size());
    }

    @Test
    public void testConstructorString() {
        final String str = "A";
        final int length = str.length();
        final TextStringBuilder sb = new TextStringBuilder(str);
        assertEquals(TextStringBuilder.CAPACITY + length, sb.capacity());
        assertEquals(length, sb.toCharArray().length);
    }

    // -----------------------------------------------------------------------
    @Test
    public void testContains_char() {
        final TextStringBuilder sb = new TextStringBuilder("abcdefghijklmnopqrstuvwxyz");
        assertTrue(sb.contains('a'));
        assertTrue(sb.contains('o'));
        assertTrue(sb.contains('z'));
        assertFalse(sb.contains('1'));
    }

    @Test
    public void testContains_String() {
        final TextStringBuilder sb = new TextStringBuilder("abcdefghijklmnopqrstuvwxyz");
        assertTrue(sb.contains("a"));
        assertTrue(sb.contains("pq"));
        assertTrue(sb.contains("z"));
        assertFalse(sb.contains("zyx"));
        assertFalse(sb.contains((String) null));
    }

    @Test
    public void testContains_StringMatcher() {
        TextStringBuilder sb = new TextStringBuilder("abcdefghijklmnopqrstuvwxyz");
        assertTrue(sb.contains(StringMatcherFactory.INSTANCE.charMatcher('a')));
        assertTrue(sb.contains(StringMatcherFactory.INSTANCE.stringMatcher("pq")));
        assertTrue(sb.contains(StringMatcherFactory.INSTANCE.charMatcher('z')));
        assertFalse(sb.contains(StringMatcherFactory.INSTANCE.stringMatcher("zy")));
        assertFalse(sb.contains((StringMatcher) null));

        sb = new TextStringBuilder();
        assertFalse(sb.contains(A_NUMBER_MATCHER));
        sb.append("B A1 C");
        assertTrue(sb.contains(A_NUMBER_MATCHER));
    }

    // -----------------------------------------------------------------------
    @Test
    public void testDeleteAll_char() {
        TextStringBuilder sb = new TextStringBuilder("abcbccba");
        sb.deleteAll('X');
        assertEquals("abcbccba", sb.toString());
        sb.deleteAll('a');
        assertEquals("bcbccb", sb.toString());
        sb.deleteAll('c');
        assertEquals("bbb", sb.toString());
        sb.deleteAll('b');
        assertEquals("", sb.toString());

        sb = new TextStringBuilder("");
        sb.deleteAll('b');
        assertEquals("", sb.toString());
    }

    // -----------------------------------------------------------------------
    @Test
    public void testDeleteAll_String() {
        TextStringBuilder sb = new TextStringBuilder("abcbccba");
        sb.deleteAll((String) null);
        assertEquals("abcbccba", sb.toString());
        sb.deleteAll("");
        assertEquals("abcbccba", sb.toString());

        sb.deleteAll("X");
        assertEquals("abcbccba", sb.toString());
        sb.deleteAll("a");
        assertEquals("bcbccb", sb.toString());
        sb.deleteAll("c");
        assertEquals("bbb", sb.toString());
        sb.deleteAll("b");
        assertEquals("", sb.toString());

        sb = new TextStringBuilder("abcbccba");
        sb.deleteAll("bc");
        assertEquals("acba", sb.toString());

        sb = new TextStringBuilder("");
        sb.deleteAll("bc");
        assertEquals("", sb.toString());
    }

    // -----------------------------------------------------------------------
    @Test
    public void testDeleteAll_StringMatcher() {
        TextStringBuilder sb = new TextStringBuilder("A0xA1A2yA3");
        sb.deleteAll((StringMatcher) null);
        assertEquals("A0xA1A2yA3", sb.toString());
        sb.deleteAll(A_NUMBER_MATCHER);
        assertEquals("xy", sb.toString());

        sb = new TextStringBuilder("Ax1");
        sb.deleteAll(A_NUMBER_MATCHER);
        assertEquals("Ax1", sb.toString());

        sb = new TextStringBuilder("");
        sb.deleteAll(A_NUMBER_MATCHER);
        assertEquals("", sb.toString());
    }

    @Test
    public void testDeleteCharAt() {
        final String str = "abc";
        //
        final TextStringBuilder sb1 = new TextStringBuilder(str);
        sb1.deleteCharAt(0);
        assertEquals("bc", sb1.toString());
        //
        final TextStringBuilder sb2 = new TextStringBuilder(str);
        sb2.deleteCharAt(str.length() - 1);
        assertEquals("ab", sb2.toString());
        //
        final TextStringBuilder sb3 = new TextStringBuilder(str);
        assertThrows(IndexOutOfBoundsException.class, () -> sb3.deleteCharAt(str.length()));
        assertThrows(IndexOutOfBoundsException.class, () -> sb3.deleteCharAt(1000));
    }

    @Test
    public void testDeleteCharAtWithNegative() {
        assertThatExceptionOfType(StringIndexOutOfBoundsException.class).isThrownBy(() -> {
            new TextStringBuilder().deleteCharAt((-1258));
        });
    }

    @Test
    public void testDeleteFirst_char() {
        TextStringBuilder sb = new TextStringBuilder("abcba");
        sb.deleteFirst('X');
        assertEquals("abcba", sb.toString());
        sb.deleteFirst('a');
        assertEquals("bcba", sb.toString());
        sb.deleteFirst('c');
        assertEquals("bba", sb.toString());
        sb.deleteFirst('b');
        assertEquals("ba", sb.toString());

        sb = new TextStringBuilder("");
        sb.deleteFirst('b');
        assertEquals("", sb.toString());
    }

    @Test
    public void testDeleteFirst_String() {
        TextStringBuilder sb = new TextStringBuilder("abcbccba");
        sb.deleteFirst((String) null);
        assertEquals("abcbccba", sb.toString());
        sb.deleteFirst("");
        assertEquals("abcbccba", sb.toString());

        sb.deleteFirst("X");
        assertEquals("abcbccba", sb.toString());
        sb.deleteFirst("a");
        assertEquals("bcbccba", sb.toString());
        sb.deleteFirst("c");
        assertEquals("bbccba", sb.toString());
        sb.deleteFirst("b");
        assertEquals("bccba", sb.toString());

        sb = new TextStringBuilder("abcbccba");
        sb.deleteFirst("bc");
        assertEquals("abccba", sb.toString());

        sb = new TextStringBuilder("");
        sb.deleteFirst("bc");
        assertEquals("", sb.toString());
    }

    @Test
    public void testDeleteFirst_StringMatcher() {
        TextStringBuilder sb = new TextStringBuilder("A0xA1A2yA3");
        sb.deleteFirst((StringMatcher) null);
        assertEquals("A0xA1A2yA3", sb.toString());
        sb.deleteFirst(A_NUMBER_MATCHER);
        assertEquals("xA1A2yA3", sb.toString());

        sb = new TextStringBuilder("Ax1");
        sb.deleteFirst(A_NUMBER_MATCHER);
        assertEquals("Ax1", sb.toString());

        sb = new TextStringBuilder("");
        sb.deleteFirst(A_NUMBER_MATCHER);
        assertEquals("", sb.toString());
    }

    // -----------------------------------------------------------------------
    @Test
    public void testDeleteIntInt() {
        final TextStringBuilder sb = new TextStringBuilder("abc");
        sb.delete(0, 1);
        assertEquals("bc", sb.toString());
        sb.delete(1, 2);
        assertEquals("b", sb.toString());
        sb.delete(0, 1);
        assertEquals("", sb.toString());
        sb.delete(0, 1000);
        assertEquals("", sb.toString());

        assertThrows(IndexOutOfBoundsException.class, () -> sb.delete(1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> sb.delete(-1, 1));

        assertThrows(IndexOutOfBoundsException.class, () -> new TextStringBuilder("anything").delete(2, 1));
    }

    @Test
    public void testEndsWith() {
        final TextStringBuilder sb = new TextStringBuilder();
        assertFalse(sb.endsWith("a"));
        assertFalse(sb.endsWith("c"));
        assertTrue(sb.endsWith(""));
        assertFalse(sb.endsWith(null));
        sb.append("abc");
        assertTrue(sb.endsWith("c"));
        assertTrue(sb.endsWith("bc"));
        assertTrue(sb.endsWith("abc"));
        assertFalse(sb.endsWith("cba"));
        assertFalse(sb.endsWith("abcd"));
        assertFalse(sb.endsWith(" abc"));
        assertFalse(sb.endsWith("abc "));
    }

    @Test
    public void testEnsureCapacity() {
        final TextStringBuilder sb = new TextStringBuilder();
        sb.ensureCapacity(2);
        assertTrue(sb.capacity() >= 2);

        sb.ensureCapacity(-1);
        assertTrue(sb.capacity() >= 0);

        sb.append("HelloWorld");
        sb.ensureCapacity(40);
        assertTrue(sb.capacity() >= 40);
    }

    // -----------------------------------------------------------------------
    @Test
    public void testEquals() {
        final TextStringBuilder sb1 = new TextStringBuilder();
        final TextStringBuilder sb2 = new TextStringBuilder();
        assertTrue(sb1.equals(sb2));
        assertTrue(sb1.equals(sb1));
        assertTrue(sb2.equals(sb2));
        assertTrue(sb1.equals((Object) sb2));

        sb1.append("abc");
        assertFalse(sb1.equals(sb2));
        assertFalse(sb1.equals((Object) sb2));

        sb2.append("ABC");
        assertFalse(sb1.equals(sb2));
        assertFalse(sb1.equals((Object) sb2));

        sb2.clear().append("abc");
        assertTrue(sb1.equals(sb2));
        assertTrue(sb1.equals((Object) sb2));

        assertFalse(sb1.equals(Integer.valueOf(1)));
        assertFalse(sb1.equals("abc"));
    }

    // -----------------------------------------------------------------------
    @Test
    public void testEqualsIgnoreCase() {
        final TextStringBuilder sb1 = new TextStringBuilder();
        final TextStringBuilder sb2 = new TextStringBuilder();
        assertTrue(sb1.equalsIgnoreCase(sb1));
        assertTrue(sb1.equalsIgnoreCase(sb2));
        assertTrue(sb2.equalsIgnoreCase(sb2));

        sb1.append("abc");
        assertFalse(sb1.equalsIgnoreCase(sb2));

        sb2.append("ABC");
        assertTrue(sb1.equalsIgnoreCase(sb2));

        sb2.clear().append("abc");
        assertTrue(sb1.equalsIgnoreCase(sb2));
        assertTrue(sb1.equalsIgnoreCase(sb1));
        assertTrue(sb2.equalsIgnoreCase(sb2));

        sb2.clear().append("aBc");
        assertTrue(sb1.equalsIgnoreCase(sb2));
    }

    @Test
    public void testGetChars() {
        final TextStringBuilder sb = new TextStringBuilder();

        char[] input = new char[10];
        char[] a = sb.getChars(input);
        assertSame(input, a);
        assertTrue(Arrays.equals(new char[10], a));

        sb.append("junit");
        a = sb.getChars(input);
        assertSame(input, a);
        assertTrue(Arrays.equals(new char[] {'j', 'u', 'n', 'i', 't', 0, 0, 0, 0, 0}, a));

        a = sb.getChars(null);
        assertNotSame(input, a);
        assertEquals(5, a.length);
        assertTrue(Arrays.equals("junit".toCharArray(), a));

        input = new char[5];
        a = sb.getChars(input);
        assertSame(input, a);

        input = new char[4];
        a = sb.getChars(input);
        assertNotSame(input, a);
    }

    @Test
    public void testGetCharsIntIntCharArrayInt() {
        final TextStringBuilder sb = new TextStringBuilder();

        sb.append("junit");
        char[] a = new char[5];
        sb.getChars(0, 5, a, 0);
        assertTrue(Arrays.equals(new char[] {'j', 'u', 'n', 'i', 't'}, a));

        a = new char[5];
        sb.getChars(0, 2, a, 3);
        assertTrue(Arrays.equals(new char[] {0, 0, 0, 'j', 'u'}, a));

        try {
            sb.getChars(-1, 0, a, 0);
            fail("no exception");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.getChars(0, -1, a, 0);
            fail("no exception");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.getChars(0, 20, a, 0);
            fail("no exception");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.getChars(4, 2, a, 0);
            fail("no exception");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }
    }

    // -----------------------------------------------------------------------
    @Test
    public void testGetSetNewLineText() {
        final TextStringBuilder sb = new TextStringBuilder();
        assertNull(sb.getNewLineText());

        sb.setNewLineText("#");
        assertEquals("#", sb.getNewLineText());

        sb.setNewLineText("");
        assertEquals("", sb.getNewLineText());

        sb.setNewLineText((String) null);
        assertNull(sb.getNewLineText());
    }

    // -----------------------------------------------------------------------
    @Test
    public void testGetSetNullText() {
        final TextStringBuilder sb = new TextStringBuilder();
        assertNull(sb.getNullText());

        sb.setNullText("null");
        assertEquals("null", sb.getNullText());

        sb.setNullText("");
        assertNull(sb.getNullText());

        sb.setNullText("NULL");
        assertEquals("NULL", sb.getNullText());

        sb.setNullText((String) null);
        assertNull(sb.getNullText());
    }

    // -----------------------------------------------------------------------
    @Test
    public void testHashCode() {
        final TextStringBuilder sb = new TextStringBuilder();
        final int hc1a = sb.hashCode();
        final int hc1b = sb.hashCode();
        final int emptyHc = Arrays.hashCode(sb.getBuffer());
        assertEquals(emptyHc, hc1a);
        assertEquals(hc1a, hc1b);

        sb.append("abc");
        final int hc2a = sb.hashCode();
        final int hc2b = sb.hashCode();
        assertTrue(hc2a != emptyHc);
        assertEquals(hc2a, hc2b);
    }

    // -----------------------------------------------------------------------
    @Test
    public void testIndexOf_char() {
        final TextStringBuilder sb = new TextStringBuilder("abab");
        assertEquals(0, sb.indexOf('a'));

        // should work like String#indexOf
        assertEquals("abab".indexOf('a'), sb.indexOf('a'));

        assertEquals(1, sb.indexOf('b'));
        assertEquals("abab".indexOf('b'), sb.indexOf('b'));

        assertEquals(-1, sb.indexOf('z'));
    }

    @Test
    public void testIndexOf_char_int() {
        TextStringBuilder sb = new TextStringBuilder("abab");
        assertEquals(0, sb.indexOf('a', -1));
        assertEquals(0, sb.indexOf('a', 0));
        assertEquals(2, sb.indexOf('a', 1));
        assertEquals(-1, sb.indexOf('a', 4));
        assertEquals(-1, sb.indexOf('a', 5));

        // should work like String#indexOf
        assertEquals("abab".indexOf('a', 1), sb.indexOf('a', 1));

        assertEquals(3, sb.indexOf('b', 2));
        assertEquals("abab".indexOf('b', 2), sb.indexOf('b', 2));

        assertEquals(-1, sb.indexOf('z', 2));

        sb = new TextStringBuilder("xyzabc");
        assertEquals(2, sb.indexOf('z', 0));
        assertEquals(-1, sb.indexOf('z', 3));
    }

    // -----------------------------------------------------------------------
    @Test
    public void testIndexOf_String() {
        final TextStringBuilder sb = new TextStringBuilder("abab");

        assertEquals(0, sb.indexOf("a"));
        // should work like String#indexOf
        assertEquals("abab".indexOf("a"), sb.indexOf("a"));

        assertEquals(0, sb.indexOf("ab"));
        // should work like String#indexOf
        assertEquals("abab".indexOf("ab"), sb.indexOf("ab"));

        assertEquals(1, sb.indexOf("b"));
        assertEquals("abab".indexOf("b"), sb.indexOf("b"));

        assertEquals(1, sb.indexOf("ba"));
        assertEquals("abab".indexOf("ba"), sb.indexOf("ba"));

        assertEquals(-1, sb.indexOf("z"));

        assertEquals(-1, sb.indexOf((String) null));
    }

    @Test
    public void testIndexOf_String_int() {
        TextStringBuilder sb = new TextStringBuilder("abab");
        assertEquals(0, sb.indexOf("a", -1));
        assertEquals(0, sb.indexOf("a", 0));
        assertEquals(2, sb.indexOf("a", 1));
        assertEquals(2, sb.indexOf("a", 2));
        assertEquals(-1, sb.indexOf("a", 3));
        assertEquals(-1, sb.indexOf("a", 4));
        assertEquals(-1, sb.indexOf("a", 5));

        assertEquals(-1, sb.indexOf("abcdef", 0));
        assertEquals(0, sb.indexOf("", 0));
        assertEquals(1, sb.indexOf("", 1));

        // should work like String#indexOf
        assertEquals("abab".indexOf("a", 1), sb.indexOf("a", 1));

        assertEquals(2, sb.indexOf("ab", 1));
        // should work like String#indexOf
        assertEquals("abab".indexOf("ab", 1), sb.indexOf("ab", 1));

        assertEquals(3, sb.indexOf("b", 2));
        assertEquals("abab".indexOf("b", 2), sb.indexOf("b", 2));

        assertEquals(1, sb.indexOf("ba", 1));
        assertEquals("abab".indexOf("ba", 2), sb.indexOf("ba", 2));

        assertEquals(-1, sb.indexOf("z", 2));

        sb = new TextStringBuilder("xyzabc");
        assertEquals(2, sb.indexOf("za", 0));
        assertEquals(-1, sb.indexOf("za", 3));

        assertEquals(-1, sb.indexOf((String) null, 2));
    }

    // -----------------------------------------------------------------------
    @Test
    public void testIndexOf_StringMatcher() {
        final TextStringBuilder sb = new TextStringBuilder();
        assertEquals(-1, sb.indexOf((StringMatcher) null));
        assertEquals(-1, sb.indexOf(StringMatcherFactory.INSTANCE.charMatcher('a')));

        sb.append("ab bd");
        assertEquals(0, sb.indexOf(StringMatcherFactory.INSTANCE.charMatcher('a')));
        assertEquals(1, sb.indexOf(StringMatcherFactory.INSTANCE.charMatcher('b')));
        assertEquals(2, sb.indexOf(StringMatcherFactory.INSTANCE.spaceMatcher()));
        assertEquals(4, sb.indexOf(StringMatcherFactory.INSTANCE.charMatcher('d')));
        assertEquals(-1, sb.indexOf(StringMatcherFactory.INSTANCE.noneMatcher()));
        assertEquals(-1, sb.indexOf((StringMatcher) null));

        sb.append(" A1 junction");
        assertEquals(6, sb.indexOf(A_NUMBER_MATCHER));
    }

    @Test
    public void testIndexOf_StringMatcher_int() {
        final TextStringBuilder sb = new TextStringBuilder();
        assertEquals(-1, sb.indexOf((StringMatcher) null, 2));
        assertEquals(-1, sb.indexOf(StringMatcherFactory.INSTANCE.charMatcher('a'), 2));
        assertEquals(-1, sb.indexOf(StringMatcherFactory.INSTANCE.charMatcher('a'), 0));

        sb.append("ab bd");
        assertEquals(0, sb.indexOf(StringMatcherFactory.INSTANCE.charMatcher('a'), -2));
        assertEquals(0, sb.indexOf(StringMatcherFactory.INSTANCE.charMatcher('a'), 0));
        assertEquals(-1, sb.indexOf(StringMatcherFactory.INSTANCE.charMatcher('a'), 2));
        assertEquals(-1, sb.indexOf(StringMatcherFactory.INSTANCE.charMatcher('a'), 20));

        assertEquals(1, sb.indexOf(StringMatcherFactory.INSTANCE.charMatcher('b'), -1));
        assertEquals(1, sb.indexOf(StringMatcherFactory.INSTANCE.charMatcher('b'), 0));
        assertEquals(1, sb.indexOf(StringMatcherFactory.INSTANCE.charMatcher('b'), 1));
        assertEquals(3, sb.indexOf(StringMatcherFactory.INSTANCE.charMatcher('b'), 2));
        assertEquals(3, sb.indexOf(StringMatcherFactory.INSTANCE.charMatcher('b'), 3));
        assertEquals(-1, sb.indexOf(StringMatcherFactory.INSTANCE.charMatcher('b'), 4));
        assertEquals(-1, sb.indexOf(StringMatcherFactory.INSTANCE.charMatcher('b'), 5));
        assertEquals(-1, sb.indexOf(StringMatcherFactory.INSTANCE.charMatcher('b'), 6));

        assertEquals(2, sb.indexOf(StringMatcherFactory.INSTANCE.spaceMatcher(), -2));
        assertEquals(2, sb.indexOf(StringMatcherFactory.INSTANCE.spaceMatcher(), 0));
        assertEquals(2, sb.indexOf(StringMatcherFactory.INSTANCE.spaceMatcher(), 2));
        assertEquals(-1, sb.indexOf(StringMatcherFactory.INSTANCE.spaceMatcher(), 4));
        assertEquals(-1, sb.indexOf(StringMatcherFactory.INSTANCE.spaceMatcher(), 20));

        assertEquals(-1, sb.indexOf(StringMatcherFactory.INSTANCE.noneMatcher(), 0));
        assertEquals(-1, sb.indexOf((StringMatcher) null, 0));

        sb.append(" A1 junction with A2");
        assertEquals(6, sb.indexOf(A_NUMBER_MATCHER, 5));
        assertEquals(6, sb.indexOf(A_NUMBER_MATCHER, 6));
        assertEquals(23, sb.indexOf(A_NUMBER_MATCHER, 7));
        assertEquals(23, sb.indexOf(A_NUMBER_MATCHER, 22));
        assertEquals(23, sb.indexOf(A_NUMBER_MATCHER, 23));
        assertEquals(-1, sb.indexOf(A_NUMBER_MATCHER, 24));
    }

    @Test
    public void testIndexOfLang294() {
        final TextStringBuilder sb = new TextStringBuilder("onetwothree");
        sb.deleteFirst("three");
        assertEquals(-1, sb.indexOf("three"));
    }

    @Test
    public void testIsEmpty() {
        final TextStringBuilder sb = new TextStringBuilder();
        assertTrue(sb.isEmpty());

        sb.append("Hello");
        assertFalse(sb.isEmpty());

        sb.clear();
        assertTrue(sb.isEmpty());
    }

    // -----------------------------------------------------------------------
    @Test
    public void testLang294() {
        final TextStringBuilder sb = new TextStringBuilder("\n%BLAH%\nDo more stuff\neven more stuff\n%BLAH%\n");
        sb.deleteAll("\n%BLAH%");
        assertEquals("\nDo more stuff\neven more stuff\n", sb.toString());
    }

    // -----------------------------------------------------------------------
    @Test
    public void testLang295() {
        final TextStringBuilder sb = new TextStringBuilder("onetwothree");
        sb.deleteFirst("three");
        assertFalse(sb.contains('h'), "The contains(char) method is looking beyond the end of the string");
        assertEquals(-1, sb.indexOf('h'), "The indexOf(char) method is looking beyond the end of the string");
    }

    @Test
    public void testLang412Left() {
        final TextStringBuilder sb = new TextStringBuilder();
        sb.appendFixedWidthPadLeft(null, 10, '*');
        assertEquals("**********", sb.toString(), "Failed to invoke appendFixedWidthPadLeft correctly");
    }

    // -----------------------------------------------------------------------
    @Test
    public void testLang412Right() {
        final TextStringBuilder sb = new TextStringBuilder();
        sb.appendFixedWidthPadRight(null, 10, '*');
        assertEquals("**********", sb.toString(), "Failed to invoke appendFixedWidthPadRight correctly");
    }

    @Test
    public void testLastIndexOf_char() {
        final TextStringBuilder sb = new TextStringBuilder("abab");

        assertEquals(2, sb.lastIndexOf('a'));
        // should work like String#lastIndexOf
        assertEquals("abab".lastIndexOf('a'), sb.lastIndexOf('a'));

        assertEquals(3, sb.lastIndexOf('b'));
        assertEquals("abab".lastIndexOf('b'), sb.lastIndexOf('b'));

        assertEquals(-1, sb.lastIndexOf('z'));
    }

    @Test
    public void testLastIndexOf_char_int() {
        TextStringBuilder sb = new TextStringBuilder("abab");
        assertEquals(-1, sb.lastIndexOf('a', -1));
        assertEquals(0, sb.lastIndexOf('a', 0));
        assertEquals(0, sb.lastIndexOf('a', 1));

        // should work like String#lastIndexOf
        assertEquals("abab".lastIndexOf('a', 1), sb.lastIndexOf('a', 1));

        assertEquals(1, sb.lastIndexOf('b', 2));
        assertEquals("abab".lastIndexOf('b', 2), sb.lastIndexOf('b', 2));

        assertEquals(-1, sb.lastIndexOf('z', 2));

        sb = new TextStringBuilder("xyzabc");
        assertEquals(2, sb.lastIndexOf('z', sb.length()));
        assertEquals(-1, sb.lastIndexOf('z', 1));
    }

    @Test
    public void testLastIndexOf_String() {
        final TextStringBuilder sb = new TextStringBuilder("abab");

        assertEquals(2, sb.lastIndexOf("a"));
        // should work like String#lastIndexOf
        assertEquals("abab".lastIndexOf("a"), sb.lastIndexOf("a"));

        assertEquals(2, sb.lastIndexOf("ab"));
        // should work like String#lastIndexOf
        assertEquals("abab".lastIndexOf("ab"), sb.lastIndexOf("ab"));

        assertEquals(3, sb.lastIndexOf("b"));
        assertEquals("abab".lastIndexOf("b"), sb.lastIndexOf("b"));

        assertEquals(1, sb.lastIndexOf("ba"));
        assertEquals("abab".lastIndexOf("ba"), sb.lastIndexOf("ba"));

        assertEquals(-1, sb.lastIndexOf("z"));

        assertEquals(-1, sb.lastIndexOf((String) null));
    }

    @Test
    public void testLastIndexOf_String_int() {
        TextStringBuilder sb = new TextStringBuilder("abab");
        assertEquals(-1, sb.lastIndexOf("a", -1));
        assertEquals(0, sb.lastIndexOf("a", 0));
        assertEquals(0, sb.lastIndexOf("a", 1));
        assertEquals(2, sb.lastIndexOf("a", 2));
        assertEquals(2, sb.lastIndexOf("a", 3));
        assertEquals(2, sb.lastIndexOf("a", 4));
        assertEquals(2, sb.lastIndexOf("a", 5));

        assertEquals(-1, sb.lastIndexOf("abcdef", 3));
        assertEquals("abab".lastIndexOf("", 3), sb.lastIndexOf("", 3));
        assertEquals("abab".lastIndexOf("", 1), sb.lastIndexOf("", 1));

        // should work like String#lastIndexOf
        assertEquals("abab".lastIndexOf("a", 1), sb.lastIndexOf("a", 1));

        assertEquals(0, sb.lastIndexOf("ab", 1));
        // should work like String#lastIndexOf
        assertEquals("abab".lastIndexOf("ab", 1), sb.lastIndexOf("ab", 1));

        assertEquals(1, sb.lastIndexOf("b", 2));
        assertEquals("abab".lastIndexOf("b", 2), sb.lastIndexOf("b", 2));

        assertEquals(1, sb.lastIndexOf("ba", 2));
        assertEquals("abab".lastIndexOf("ba", 2), sb.lastIndexOf("ba", 2));

        assertEquals(-1, sb.lastIndexOf("z", 2));

        sb = new TextStringBuilder("xyzabc");
        assertEquals(2, sb.lastIndexOf("za", sb.length()));
        assertEquals(-1, sb.lastIndexOf("za", 1));

        assertEquals(-1, sb.lastIndexOf((String) null, 2));
    }

    @Test
    public void testLastIndexOf_StringMatcher() {
        final TextStringBuilder sb = new TextStringBuilder();
        assertEquals(-1, sb.lastIndexOf((StringMatcher) null));
        assertEquals(-1, sb.lastIndexOf(StringMatcherFactory.INSTANCE.charMatcher('a')));

        sb.append("ab bd");
        assertEquals(0, sb.lastIndexOf(StringMatcherFactory.INSTANCE.charMatcher('a')));
        assertEquals(3, sb.lastIndexOf(StringMatcherFactory.INSTANCE.charMatcher('b')));
        assertEquals(2, sb.lastIndexOf(StringMatcherFactory.INSTANCE.spaceMatcher()));
        assertEquals(4, sb.lastIndexOf(StringMatcherFactory.INSTANCE.charMatcher('d')));
        assertEquals(-1, sb.lastIndexOf(StringMatcherFactory.INSTANCE.noneMatcher()));
        assertEquals(-1, sb.lastIndexOf((StringMatcher) null));

        sb.append(" A1 junction");
        assertEquals(6, sb.lastIndexOf(A_NUMBER_MATCHER));
    }

    @Test
    public void testLastIndexOf_StringMatcher_int() {
        final TextStringBuilder sb = new TextStringBuilder();
        assertEquals(-1, sb.lastIndexOf((StringMatcher) null, 2));
        assertEquals(-1, sb.lastIndexOf(StringMatcherFactory.INSTANCE.charMatcher('a'), 2));
        assertEquals(-1, sb.lastIndexOf(StringMatcherFactory.INSTANCE.charMatcher('a'), 0));
        assertEquals(-1, sb.lastIndexOf(StringMatcherFactory.INSTANCE.charMatcher('a'), -1));

        sb.append("ab bd");
        assertEquals(-1, sb.lastIndexOf(StringMatcherFactory.INSTANCE.charMatcher('a'), -2));
        assertEquals(0, sb.lastIndexOf(StringMatcherFactory.INSTANCE.charMatcher('a'), 0));
        assertEquals(0, sb.lastIndexOf(StringMatcherFactory.INSTANCE.charMatcher('a'), 2));
        assertEquals(0, sb.lastIndexOf(StringMatcherFactory.INSTANCE.charMatcher('a'), 20));

        assertEquals(-1, sb.lastIndexOf(StringMatcherFactory.INSTANCE.charMatcher('b'), -1));
        assertEquals(-1, sb.lastIndexOf(StringMatcherFactory.INSTANCE.charMatcher('b'), 0));
        assertEquals(1, sb.lastIndexOf(StringMatcherFactory.INSTANCE.charMatcher('b'), 1));
        assertEquals(1, sb.lastIndexOf(StringMatcherFactory.INSTANCE.charMatcher('b'), 2));
        assertEquals(3, sb.lastIndexOf(StringMatcherFactory.INSTANCE.charMatcher('b'), 3));
        assertEquals(3, sb.lastIndexOf(StringMatcherFactory.INSTANCE.charMatcher('b'), 4));
        assertEquals(3, sb.lastIndexOf(StringMatcherFactory.INSTANCE.charMatcher('b'), 5));
        assertEquals(3, sb.lastIndexOf(StringMatcherFactory.INSTANCE.charMatcher('b'), 6));

        assertEquals(-1, sb.lastIndexOf(StringMatcherFactory.INSTANCE.spaceMatcher(), -2));
        assertEquals(-1, sb.lastIndexOf(StringMatcherFactory.INSTANCE.spaceMatcher(), 0));
        assertEquals(2, sb.lastIndexOf(StringMatcherFactory.INSTANCE.spaceMatcher(), 2));
        assertEquals(2, sb.lastIndexOf(StringMatcherFactory.INSTANCE.spaceMatcher(), 4));
        assertEquals(2, sb.lastIndexOf(StringMatcherFactory.INSTANCE.spaceMatcher(), 20));

        assertEquals(-1, sb.lastIndexOf(StringMatcherFactory.INSTANCE.noneMatcher(), 0));
        assertEquals(-1, sb.lastIndexOf((StringMatcher) null, 0));

        sb.append(" A1 junction with A2");
        assertEquals(-1, sb.lastIndexOf(A_NUMBER_MATCHER, 5));
        assertEquals(-1, sb.lastIndexOf(A_NUMBER_MATCHER, 6)); // A matches, 1
                                                               // is outside
                                                               // bounds
        assertEquals(6, sb.lastIndexOf(A_NUMBER_MATCHER, 7));
        assertEquals(6, sb.lastIndexOf(A_NUMBER_MATCHER, 22));
        assertEquals(6, sb.lastIndexOf(A_NUMBER_MATCHER, 23)); // A matches, 2
                                                               // is outside
                                                               // bounds
        assertEquals(23, sb.lastIndexOf(A_NUMBER_MATCHER, 24));
    }

    @Test
    public void testLeftString() {
        final TextStringBuilder sb = new TextStringBuilder("left right");
        assertEquals("left", sb.leftString(4));
        assertEquals("", sb.leftString(0));
        assertEquals("", sb.leftString(-5));
        assertEquals("left right", sb.leftString(15));
    }

    // -----------------------------------------------------------------------
    @Test
    public void testLength() {
        final TextStringBuilder sb = new TextStringBuilder();
        assertEquals(0, sb.length());

        sb.append("Hello");
        assertEquals(5, sb.length());
    }

    // -----------------------------------------------------------------------
    @Test
    public void testMidString() {
        final TextStringBuilder sb = new TextStringBuilder("hello goodbye hello");
        assertEquals("goodbye", sb.midString(6, 7));
        assertEquals("hello", sb.midString(0, 5));
        assertEquals("hello", sb.midString(-5, 5));
        assertEquals("", sb.midString(0, -1));
        assertEquals("", sb.midString(20, 2));
        assertEquals("hello", sb.midString(14, 22));
    }

    @Test
    public void testMinimizeCapacity() {
        final TextStringBuilder sb = new TextStringBuilder();
        sb.minimizeCapacity();
        assertEquals(0, sb.capacity());

        sb.append("HelloWorld");
        sb.minimizeCapacity();
        assertEquals(10, sb.capacity());
    }

    @Test
    public void testReadFromCharBuffer() throws Exception {
        String s = "";
        for (int i = 0; i < 100; ++i) {
            final TextStringBuilder sb = new TextStringBuilder();
            final int len = sb.readFrom(CharBuffer.wrap(s));

            assertEquals(s.length(), len);
            assertEquals(s, sb.toString());

            s += Integer.toString(i);
        }
    }

    @Test
    public void testReadFromCharBufferAppendsToEnd() throws Exception {
        final TextStringBuilder sb = new TextStringBuilder("Test");
        sb.readFrom(CharBuffer.wrap(" 123"));
        assertEquals("Test 123", sb.toString());
    }

    @Test
    public void testReadFromReadable() throws Exception {
        String s = "";
        for (int i = 0; i < 100; ++i) {
            final TextStringBuilder sb = new TextStringBuilder();
            final int len = sb.readFrom(new MockReadable(s));

            assertEquals(s.length(), len);
            assertEquals(s, sb.toString());

            s += Integer.toString(i);
        }
    }

    @Test
    public void testReadFromReadableAppendsToEnd() throws Exception {
        final TextStringBuilder sb = new TextStringBuilder("Test");
        sb.readFrom(new MockReadable(" 123"));
        assertEquals("Test 123", sb.toString());
    }

    @Test
    public void testReadFromReader() throws Exception {
        String s = "";
        for (int i = 0; i < 100; ++i) {
            final TextStringBuilder sb = new TextStringBuilder();
            final int len = sb.readFrom(new StringReader(s));

            assertEquals(s.length(), len);
            assertEquals(s, sb.toString());

            s += Integer.toString(i);
        }
    }

    @Test
    public void testReadFromReaderAppendsToEnd() throws Exception {
        final TextStringBuilder sb = new TextStringBuilder("Test");
        sb.readFrom(new StringReader(" 123"));
        assertEquals("Test 123", sb.toString());
    }

    @Test
    public void testReadFromReaderInt() throws Exception {
        String s = "";
        for (int i = 0; i < 100; ++i) {
            final TextStringBuilder sb = new TextStringBuilder();
            final int len = sb.readFrom(new StringReader(s), s.length());

            assertEquals(s.length(), len);
            assertEquals(s, sb.toString());

            s += Integer.toString(i);
        }
        //
        TextStringBuilder sb;
        int count;
        int target;
        final String source = "abc";
        final int sourceLen = source.length();
        //
        target = -1;
        sb = new TextStringBuilder();
        count = sb.readFrom(new StringReader(source), target);
        assertEquals(0, count);
        assertEquals(0, sb.size());
        assertEquals(source.substring(0, 0), sb.toString());
        //
        target = 0;
        sb = new TextStringBuilder();
        count = sb.readFrom(new StringReader(source), target);
        assertEquals(target, count);
        assertEquals(target, sb.size());
        assertEquals(source.substring(0, target), sb.toString());
        //
        target = 1;
        sb = new TextStringBuilder();
        count = sb.readFrom(new StringReader(source), target);
        assertEquals(target, count);
        assertEquals(target, sb.size());
        assertEquals(source.substring(0, target), sb.toString());
        //
        target = 2;
        sb = new TextStringBuilder();
        count = sb.readFrom(new StringReader(source), target);
        assertEquals(target, count);
        assertEquals(target, sb.size());
        assertEquals(source.substring(0, target), sb.toString());
        //
        target = 3;
        sb = new TextStringBuilder();
        count = sb.readFrom(new StringReader(source), target);
        assertEquals(target, count);
        assertEquals(target, sb.size());
        assertEquals(source.substring(0, target), sb.toString());
        //
        target = 4;
        sb = new TextStringBuilder();
        count = sb.readFrom(new StringReader(source), target);
        assertEquals(sourceLen, count);
        assertEquals(sourceLen, sb.size());
        assertEquals(source.substring(0, sourceLen), sb.toString());
    }

    // -----------------------------------------------------------------------
    @Test
    public void testReplace_int_int_String() {
        TextStringBuilder sb = new TextStringBuilder("abc");
        sb.replace(0, 1, "d");
        assertEquals("dbc", sb.toString());
        sb.replace(0, 1, "aaa");
        assertEquals("aaabc", sb.toString());
        sb.replace(0, 3, "");
        assertEquals("bc", sb.toString());
        sb.replace(1, 2, (String) null);
        assertEquals("b", sb.toString());
        sb.replace(1, 1000, "text");
        assertEquals("btext", sb.toString());
        sb.replace(0, 1000, "text");
        assertEquals("text", sb.toString());

        sb = new TextStringBuilder("atext");
        sb.replace(1, 1, "ny");
        assertEquals("anytext", sb.toString());
        try {
            sb.replace(2, 1, "anything");
            fail("Expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        sb = new TextStringBuilder();
        try {
            sb.replace(1, 2, "anything");
            fail("Expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }
        try {
            sb.replace(-1, 1, "anything");
            fail("Expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testReplace_StringMatcher_String_int_int_int_VaryCount() {
        TextStringBuilder sb = new TextStringBuilder("aaxaaaayaa");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", 0, 10, -1);
        assertEquals("-x--y-", sb.toString());

        sb = new TextStringBuilder("aaxaaaayaa");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", 0, 10, 0);
        assertEquals("aaxaaaayaa", sb.toString());

        sb = new TextStringBuilder("aaxaaaayaa");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", 0, 10, 1);
        assertEquals("-xaaaayaa", sb.toString());

        sb = new TextStringBuilder("aaxaaaayaa");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", 0, 10, 2);
        assertEquals("-x-aayaa", sb.toString());

        sb = new TextStringBuilder("aaxaaaayaa");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", 0, 10, 3);
        assertEquals("-x--yaa", sb.toString());

        sb = new TextStringBuilder("aaxaaaayaa");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", 0, 10, 4);
        assertEquals("-x--y-", sb.toString());

        sb = new TextStringBuilder("aaxaaaayaa");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", 0, 10, 5);
        assertEquals("-x--y-", sb.toString());
    }

    @Test
    public void testReplace_StringMatcher_String_int_int_int_VaryEndIndex() {
        TextStringBuilder sb = new TextStringBuilder("aaxaaaayaa");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", 0, 0, -1);
        assertEquals("aaxaaaayaa", sb.toString());

        sb = new TextStringBuilder("aaxaaaayaa");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", 0, 2, -1);
        assertEquals("-xaaaayaa", sb.toString());

        sb = new TextStringBuilder("aaxaaaayaa");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", 0, 3, -1);
        assertEquals("-xaaaayaa", sb.toString());

        sb = new TextStringBuilder("aaxaaaayaa");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", 0, 4, -1);
        assertEquals("-xaaaayaa", sb.toString());

        sb = new TextStringBuilder("aaxaaaayaa");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", 0, 5, -1);
        assertEquals("-x-aayaa", sb.toString());

        sb = new TextStringBuilder("aaxaaaayaa");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", 0, 6, -1);
        assertEquals("-x-aayaa", sb.toString());

        sb = new TextStringBuilder("aaxaaaayaa");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", 0, 7, -1);
        assertEquals("-x--yaa", sb.toString());

        sb = new TextStringBuilder("aaxaaaayaa");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", 0, 8, -1);
        assertEquals("-x--yaa", sb.toString());

        sb = new TextStringBuilder("aaxaaaayaa");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", 0, 9, -1);
        assertEquals("-x--yaa", sb.toString());

        sb = new TextStringBuilder("aaxaaaayaa");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", 0, 10, -1);
        assertEquals("-x--y-", sb.toString());

        sb = new TextStringBuilder("aaxaaaayaa");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", 0, 1000, -1);
        assertEquals("-x--y-", sb.toString());

        sb = new TextStringBuilder("aaxaaaayaa");
        try {
            sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", 2, 1, -1);
            fail("Exception expected!");
        } catch (final IndexOutOfBoundsException ex) {
            // expected
        }
        assertEquals("aaxaaaayaa", sb.toString());
    }

    // -----------------------------------------------------------------------
    @Test
    public void testReplace_StringMatcher_String_int_int_int_VaryMatcher() {
        TextStringBuilder sb = new TextStringBuilder("abcbccba");
        sb.replace((StringMatcher) null, "x", 0, sb.length(), -1);
        assertEquals("abcbccba", sb.toString());

        sb.replace(StringMatcherFactory.INSTANCE.charMatcher('a'), "x", 0, sb.length(), -1);
        assertEquals("xbcbccbx", sb.toString());

        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("cb"), "x", 0, sb.length(), -1);
        assertEquals("xbxcxx", sb.toString());

        sb = new TextStringBuilder("A1-A2A3-A4");
        sb.replace(A_NUMBER_MATCHER, "***", 0, sb.length(), -1);
        assertEquals("***-******-***", sb.toString());

        sb = new TextStringBuilder();
        sb.replace(A_NUMBER_MATCHER, "***", 0, sb.length(), -1);
        assertEquals("", sb.toString());
    }

    @Test
    public void testReplace_StringMatcher_String_int_int_int_VaryReplace() {
        TextStringBuilder sb = new TextStringBuilder("abcbccba");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("cb"), "cb", 0, sb.length(), -1);
        assertEquals("abcbccba", sb.toString());

        sb = new TextStringBuilder("abcbccba");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("cb"), "-", 0, sb.length(), -1);
        assertEquals("ab-c-a", sb.toString());

        sb = new TextStringBuilder("abcbccba");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("cb"), "+++", 0, sb.length(), -1);
        assertEquals("ab+++c+++a", sb.toString());

        sb = new TextStringBuilder("abcbccba");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("cb"), "", 0, sb.length(), -1);
        assertEquals("abca", sb.toString());

        sb = new TextStringBuilder("abcbccba");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("cb"), null, 0, sb.length(), -1);
        assertEquals("abca", sb.toString());
    }

    @Test
    public void testReplace_StringMatcher_String_int_int_int_VaryStartIndex() {
        TextStringBuilder sb = new TextStringBuilder("aaxaaaayaa");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", 0, sb.length(), -1);
        assertEquals("-x--y-", sb.toString());

        sb = new TextStringBuilder("aaxaaaayaa");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", 1, sb.length(), -1);
        assertEquals("aax--y-", sb.toString());

        sb = new TextStringBuilder("aaxaaaayaa");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", 2, sb.length(), -1);
        assertEquals("aax--y-", sb.toString());

        sb = new TextStringBuilder("aaxaaaayaa");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", 3, sb.length(), -1);
        assertEquals("aax--y-", sb.toString());

        sb = new TextStringBuilder("aaxaaaayaa");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", 4, sb.length(), -1);
        assertEquals("aaxa-ay-", sb.toString());

        sb = new TextStringBuilder("aaxaaaayaa");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", 5, sb.length(), -1);
        assertEquals("aaxaa-y-", sb.toString());

        sb = new TextStringBuilder("aaxaaaayaa");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", 6, sb.length(), -1);
        assertEquals("aaxaaaay-", sb.toString());

        sb = new TextStringBuilder("aaxaaaayaa");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", 7, sb.length(), -1);
        assertEquals("aaxaaaay-", sb.toString());

        sb = new TextStringBuilder("aaxaaaayaa");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", 8, sb.length(), -1);
        assertEquals("aaxaaaay-", sb.toString());

        sb = new TextStringBuilder("aaxaaaayaa");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", 9, sb.length(), -1);
        assertEquals("aaxaaaayaa", sb.toString());

        sb = new TextStringBuilder("aaxaaaayaa");
        sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", 10, sb.length(), -1);
        assertEquals("aaxaaaayaa", sb.toString());

        sb = new TextStringBuilder("aaxaaaayaa");
        try {
            sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", 11, sb.length(), -1);
            fail("Exception expected!");
        } catch (final IndexOutOfBoundsException ex) {
            // expected
        }
        assertEquals("aaxaaaayaa", sb.toString());

        sb = new TextStringBuilder("aaxaaaayaa");
        try {
            sb.replace(StringMatcherFactory.INSTANCE.stringMatcher("aa"), "-", -1, sb.length(), -1);
            fail("Exception expected!");
        } catch (final IndexOutOfBoundsException ex) {
            // expected
        }
        assertEquals("aaxaaaayaa", sb.toString());
    }

    // -----------------------------------------------------------------------
    @Test
    public void testReplaceAll_char_char() {
        final TextStringBuilder sb = new TextStringBuilder("abcbccba");
        sb.replaceAll('x', 'y');
        assertEquals("abcbccba", sb.toString());
        sb.replaceAll('a', 'd');
        assertEquals("dbcbccbd", sb.toString());
        sb.replaceAll('b', 'e');
        assertEquals("dececced", sb.toString());
        sb.replaceAll('c', 'f');
        assertEquals("defeffed", sb.toString());
        sb.replaceAll('d', 'd');
        assertEquals("defeffed", sb.toString());
    }

    // -----------------------------------------------------------------------
    @Test
    public void testReplaceAll_String_String() {
        TextStringBuilder sb = new TextStringBuilder("abcbccba");
        sb.replaceAll((String) null, null);
        assertEquals("abcbccba", sb.toString());
        sb.replaceAll((String) null, "anything");
        assertEquals("abcbccba", sb.toString());
        sb.replaceAll("", null);
        assertEquals("abcbccba", sb.toString());
        sb.replaceAll("", "anything");
        assertEquals("abcbccba", sb.toString());

        sb.replaceAll("x", "y");
        assertEquals("abcbccba", sb.toString());
        sb.replaceAll("a", "d");
        assertEquals("dbcbccbd", sb.toString());
        sb.replaceAll("d", null);
        assertEquals("bcbccb", sb.toString());
        sb.replaceAll("cb", "-");
        assertEquals("b-c-", sb.toString());

        sb = new TextStringBuilder("abcba");
        sb.replaceAll("b", "xbx");
        assertEquals("axbxcxbxa", sb.toString());

        sb = new TextStringBuilder("bb");
        sb.replaceAll("b", "xbx");
        assertEquals("xbxxbx", sb.toString());
    }

    // -----------------------------------------------------------------------
    @Test
    public void testReplaceAll_StringMatcher_String() {
        TextStringBuilder sb = new TextStringBuilder("abcbccba");
        sb.replaceAll((StringMatcher) null, null);
        assertEquals("abcbccba", sb.toString());
        sb.replaceAll((StringMatcher) null, "anything");
        assertEquals("abcbccba", sb.toString());
        sb.replaceAll(StringMatcherFactory.INSTANCE.noneMatcher(), null);
        assertEquals("abcbccba", sb.toString());
        sb.replaceAll(StringMatcherFactory.INSTANCE.noneMatcher(), "anything");
        assertEquals("abcbccba", sb.toString());

        sb.replaceAll(StringMatcherFactory.INSTANCE.charMatcher('x'), "y");
        assertEquals("abcbccba", sb.toString());
        sb.replaceAll(StringMatcherFactory.INSTANCE.charMatcher('a'), "d");
        assertEquals("dbcbccbd", sb.toString());
        sb.replaceAll(StringMatcherFactory.INSTANCE.charMatcher('d'), null);
        assertEquals("bcbccb", sb.toString());
        sb.replaceAll(StringMatcherFactory.INSTANCE.stringMatcher("cb"), "-");
        assertEquals("b-c-", sb.toString());

        sb = new TextStringBuilder("abcba");
        sb.replaceAll(StringMatcherFactory.INSTANCE.charMatcher('b'), "xbx");
        assertEquals("axbxcxbxa", sb.toString());

        sb = new TextStringBuilder("bb");
        sb.replaceAll(StringMatcherFactory.INSTANCE.charMatcher('b'), "xbx");
        assertEquals("xbxxbx", sb.toString());

        sb = new TextStringBuilder("A1-A2A3-A4");
        sb.replaceAll(A_NUMBER_MATCHER, "***");
        assertEquals("***-******-***", sb.toString());

        sb = new TextStringBuilder("Dear X, hello X.");
        sb.replaceAll(StringMatcherFactory.INSTANCE.stringMatcher("X"), "012345678901234567");
        assertEquals("Dear 012345678901234567, hello 012345678901234567.", sb.toString());
    }

    // -----------------------------------------------------------------------
    @Test
    public void testReplaceFirst_char_char() {
        final TextStringBuilder sb = new TextStringBuilder("abcbccba");
        sb.replaceFirst('x', 'y');
        assertEquals("abcbccba", sb.toString());
        sb.replaceFirst('a', 'd');
        assertEquals("dbcbccba", sb.toString());
        sb.replaceFirst('b', 'e');
        assertEquals("decbccba", sb.toString());
        sb.replaceFirst('c', 'f');
        assertEquals("defbccba", sb.toString());
        sb.replaceFirst('d', 'd');
        assertEquals("defbccba", sb.toString());
    }

    @Test
    public void testReplaceFirst_String_String() {
        TextStringBuilder sb = new TextStringBuilder("abcbccba");
        sb.replaceFirst((String) null, null);
        assertEquals("abcbccba", sb.toString());
        sb.replaceFirst((String) null, "anything");
        assertEquals("abcbccba", sb.toString());
        sb.replaceFirst("", null);
        assertEquals("abcbccba", sb.toString());
        sb.replaceFirst("", "anything");
        assertEquals("abcbccba", sb.toString());

        sb.replaceFirst("x", "y");
        assertEquals("abcbccba", sb.toString());
        sb.replaceFirst("a", "d");
        assertEquals("dbcbccba", sb.toString());
        sb.replaceFirst("d", null);
        assertEquals("bcbccba", sb.toString());
        sb.replaceFirst("cb", "-");
        assertEquals("b-ccba", sb.toString());

        sb = new TextStringBuilder("abcba");
        sb.replaceFirst("b", "xbx");
        assertEquals("axbxcba", sb.toString());

        sb = new TextStringBuilder("bb");
        sb.replaceFirst("b", "xbx");
        assertEquals("xbxb", sb.toString());
    }

    @Test
    public void testReplaceFirst_StringMatcher_String() {
        TextStringBuilder sb = new TextStringBuilder("abcbccba");
        sb.replaceFirst((StringMatcher) null, null);
        assertEquals("abcbccba", sb.toString());
        sb.replaceFirst((StringMatcher) null, "anything");
        assertEquals("abcbccba", sb.toString());
        sb.replaceFirst(StringMatcherFactory.INSTANCE.noneMatcher(), null);
        assertEquals("abcbccba", sb.toString());
        sb.replaceFirst(StringMatcherFactory.INSTANCE.noneMatcher(), "anything");
        assertEquals("abcbccba", sb.toString());

        sb.replaceFirst(StringMatcherFactory.INSTANCE.charMatcher('x'), "y");
        assertEquals("abcbccba", sb.toString());
        sb.replaceFirst(StringMatcherFactory.INSTANCE.charMatcher('a'), "d");
        assertEquals("dbcbccba", sb.toString());
        sb.replaceFirst(StringMatcherFactory.INSTANCE.charMatcher('d'), null);
        assertEquals("bcbccba", sb.toString());
        sb.replaceFirst(StringMatcherFactory.INSTANCE.stringMatcher("cb"), "-");
        assertEquals("b-ccba", sb.toString());

        sb = new TextStringBuilder("abcba");
        sb.replaceFirst(StringMatcherFactory.INSTANCE.charMatcher('b'), "xbx");
        assertEquals("axbxcba", sb.toString());

        sb = new TextStringBuilder("bb");
        sb.replaceFirst(StringMatcherFactory.INSTANCE.charMatcher('b'), "xbx");
        assertEquals("xbxb", sb.toString());

        sb = new TextStringBuilder("A1-A2A3-A4");
        sb.replaceFirst(A_NUMBER_MATCHER, "***");
        assertEquals("***-A2A3-A4", sb.toString());
    }

    // -----------------------------------------------------------------------
    @Test
    public void testReverse() {
        final TextStringBuilder sb = new TextStringBuilder();
        assertEquals("", sb.reverse().toString());

        sb.clear().append(true);
        assertEquals("eurt", sb.reverse().toString());
        assertEquals("true", sb.reverse().toString());
    }

    @Test
    public void testRightString() {
        final TextStringBuilder sb = new TextStringBuilder("left right");
        assertEquals("right", sb.rightString(5));
        assertEquals("", sb.rightString(0));
        assertEquals("", sb.rightString(-5));
        assertEquals("left right", sb.rightString(15));
    }

    // -----------------------------------------------------------------------
    @Test
    public void testSetCharAt() {
        final TextStringBuilder sb = new TextStringBuilder();
        assertThrows(IndexOutOfBoundsException.class, () -> sb.setCharAt(0, 'f'));
        assertThrows(IndexOutOfBoundsException.class, () -> sb.setCharAt(-1, 'f'));
        sb.append("foo");
        sb.setCharAt(0, 'b');
        sb.setCharAt(1, 'a');
        sb.setCharAt(2, 'r');
        assertThrows(IndexOutOfBoundsException.class, () -> sb.setCharAt(3, '!'));
        assertEquals("bar", sb.toString());
    }

    @Test
    public void testSetLength() {
        final TextStringBuilder sb = new TextStringBuilder();
        sb.append("Hello");
        sb.setLength(2); // shorten
        assertEquals("He", sb.toString());
        sb.setLength(2); // no change
        assertEquals("He", sb.toString());
        sb.setLength(3); // lengthen
        assertEquals("He\0", sb.toString());

        assertThrows(IndexOutOfBoundsException.class, () -> sb.setLength(-1));
    }

    // -----------------------------------------------------------------------
    @Test
    public void testSize() {
        final TextStringBuilder sb = new TextStringBuilder();
        assertEquals(0, sb.size());

        sb.append("Hello");
        assertEquals(5, sb.size());
    }

    // -----------------------------------------------------------------------
    @Test
    public void testStartsWith() {
        final TextStringBuilder sb = new TextStringBuilder();
        assertFalse(sb.startsWith("a"));
        assertFalse(sb.startsWith(null));
        assertTrue(sb.startsWith(""));
        sb.append("abc");
        assertTrue(sb.startsWith("a"));
        assertTrue(sb.startsWith("ab"));
        assertTrue(sb.startsWith("abc"));
        assertFalse(sb.startsWith("cba"));
    }

    // -----------------------------------------------------------------------
    @Test
    public void testSubSequenceIntInt() {
        final TextStringBuilder sb = new TextStringBuilder("hello goodbye");
        // Start index is negative
        assertThrows(IndexOutOfBoundsException.class, () -> sb.subSequence(-1, 5));

        // End index is negative
        assertThrows(IndexOutOfBoundsException.class, () -> sb.subSequence(2, -1));

        // End index greater than length()
        assertThrows(IndexOutOfBoundsException.class, () -> sb.subSequence(2, sb.length() + 1));

        // Start index greater then end index
        assertThrows(IndexOutOfBoundsException.class, () -> sb.subSequence(3, 2));

        // Normal cases
        assertEquals("hello", sb.subSequence(0, 5));
        assertEquals("hello goodbye".subSequence(0, 6), sb.subSequence(0, 6));
        assertEquals("goodbye", sb.subSequence(6, 13));
        assertEquals("hello goodbye".subSequence(6, 13), sb.subSequence(6, 13));
    }

    @Test
    public void testSubstringInt() {
        final TextStringBuilder sb = new TextStringBuilder("hello goodbye");
        assertEquals("goodbye", sb.substring(6));
        assertEquals("hello goodbye".substring(6), sb.substring(6));
        assertEquals("hello goodbye", sb.substring(0));
        assertEquals("hello goodbye".substring(0), sb.substring(0));
        assertThrows(IndexOutOfBoundsException.class, () -> sb.substring(-1));

        assertThrows(IndexOutOfBoundsException.class, () -> sb.substring(15));

    }

    @Test
    public void testSubstringIntInt() {
        final TextStringBuilder sb = new TextStringBuilder("hello goodbye");
        assertEquals("hello", sb.substring(0, 5));
        assertEquals("hello goodbye".substring(0, 6), sb.substring(0, 6));

        assertEquals("goodbye", sb.substring(6, 13));
        assertEquals("hello goodbye".substring(6, 13), sb.substring(6, 13));

        assertEquals("goodbye", sb.substring(6, 20));

        assertThrows(IndexOutOfBoundsException.class, () -> sb.substring(-1, 5));

        assertThrows(IndexOutOfBoundsException.class, () -> sb.substring(15, 20));
    }

    // -----------------------------------------------------------------------
    @Test
    public void testToCharArray() {
        final TextStringBuilder sb = new TextStringBuilder();
        assertEquals(0, sb.toCharArray().length);

        char[] a = sb.toCharArray();
        assertNotNull(a, "toCharArray() result is null");
        assertEquals(0, a.length, "toCharArray() result is too large");

        sb.append("junit");
        a = sb.toCharArray();
        assertEquals(5, a.length, "toCharArray() result incorrect length");
        assertTrue(Arrays.equals("junit".toCharArray(), a), "toCharArray() result does not match");
    }

    @Test
    public void testToCharArrayIntInt() {
        final TextStringBuilder sb = new TextStringBuilder();
        assertEquals(0, sb.toCharArray(0, 0).length);

        sb.append("junit");
        char[] a = sb.toCharArray(0, 20); // too large test
        assertEquals(5, a.length, "toCharArray(int,int) result incorrect length");
        assertTrue(Arrays.equals("junit".toCharArray(), a), "toCharArray(int,int) result does not match");

        a = sb.toCharArray(0, 4);
        assertEquals(4, a.length, "toCharArray(int,int) result incorrect length");
        assertTrue(Arrays.equals("juni".toCharArray(), a), "toCharArray(int,int) result does not match");

        a = sb.toCharArray(0, 4);
        assertEquals(4, a.length, "toCharArray(int,int) result incorrect length");
        assertTrue(Arrays.equals("juni".toCharArray(), a), "toCharArray(int,int) result does not match");

        a = sb.toCharArray(0, 1);
        assertNotNull(a, "toCharArray(int,int) result is null");

        assertThrows(IndexOutOfBoundsException.class, () -> sb.toCharArray(-1, 5));

        assertThrows(IndexOutOfBoundsException.class, () -> sb.toCharArray(6, 5));
    }

    // -----------------------------------------------------------------------
    @Test
    public void testToString() {
        final TextStringBuilder sb = new TextStringBuilder("abc");
        assertEquals("abc", sb.toString());
    }

    // -----------------------------------------------------------------------
    @Test
    public void testToStringBuffer() {
        final TextStringBuilder sb = new TextStringBuilder();
        assertEquals(new StringBuffer().toString(), sb.toStringBuffer().toString());

        sb.append("junit");
        assertEquals(new StringBuffer("junit").toString(), sb.toStringBuffer().toString());
    }

    // -----------------------------------------------------------------------
    @Test
    public void testToStringBuilder() {
        final TextStringBuilder sb = new TextStringBuilder();
        assertEquals(new StringBuilder().toString(), sb.toStringBuilder().toString());

        sb.append("junit");
        assertEquals(new StringBuilder("junit").toString(), sb.toStringBuilder().toString());
    }

    @Test
    public void testToStringIntInt() {
        final TextStringBuilder sb = new TextStringBuilder("hello goodbye");
        assertEquals("hello", sb.substring(0, 5));
        assertEquals("hello goodbye".substring(0, 6), sb.toString(0, 6));

        assertEquals("goodbye", sb.toString(6, 7));

        assertThrows(IndexOutOfBoundsException.class, () -> sb.toString(-1, 5));

        assertThrows(IndexOutOfBoundsException.class, () -> sb.toString(15, 20));
    }

    @Test
    public void testTrim() {
        final TextStringBuilder sb = new TextStringBuilder();
        assertEquals("", sb.reverse().toString());

        sb.clear().append(" \u0000 ");
        assertEquals("", sb.trim().toString());

        sb.clear().append(" \u0000 a b c");
        assertEquals("a b c", sb.trim().toString());

        sb.clear().append("a b c \u0000 ");
        assertEquals("a b c", sb.trim().toString());

        sb.clear().append(" \u0000 a b c \u0000 ");
        assertEquals("a b c", sb.trim().toString());

        sb.clear().append("a b c");
        assertEquals("a b c", sb.trim().toString());
    }

    @Test
    public void testWrap() {
        char[] test = "abc".toCharArray();
        final TextStringBuilder sb = TextStringBuilder.wrap(test);
        assertArrayEquals(test, sb.getBuffer());
        assertEquals(test.length, sb.length());
        assertEquals(test.length, sb.size());
        sb.ensureCapacity(sb.capacity() * 2);
        assertFalse(Arrays.equals(test, sb.getBuffer()));
    }

}
