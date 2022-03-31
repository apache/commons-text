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

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.matcher.StringMatcher;

/**
 * Builds a string from constituent parts providing a more flexible and powerful API than StringBuffer.
 * <p>
 * The main differences from StringBuffer/StringBuilder are:
 * </p>
 * <ul>
 * <li>Not synchronized</li>
 * <li>Not final</li>
 * <li>Subclasses have direct access to character array</li>
 * <li>Additional methods
 * <ul>
 * <li>appendWithSeparators - adds an array of values, with a separator</li>
 * <li>appendPadding - adds a length padding characters</li>
 * <li>appendFixedLength - adds a fixed width field to the builder</li>
 * <li>toCharArray/getChars - simpler ways to get a range of the character array</li>
 * <li>delete - delete char or string</li>
 * <li>replace - search and replace for a char or string</li>
 * <li>leftString/rightString/midString - substring without exceptions</li>
 * <li>contains - whether the builder contains a char or string</li>
 * <li>size/clear/isEmpty - collections style API methods</li>
 * </ul>
 * </li>
 * <li>Views
 * <ul>
 * <li>asTokenizer - uses the internal buffer as the source of a StrTokenizer</li>
 * <li>asReader - uses the internal buffer as the source of a Reader</li>
 * <li>asWriter - allows a Writer to write directly to the internal buffer</li>
 * </ul>
 * </li>
 * </ul>
 * <p>
 * The aim has been to provide an API that mimics very closely what StringBuffer provides, but with additional methods.
 * It should be noted that some edge cases, with invalid indices or null input, have been altered - see individual
 * methods. The biggest of these changes is that by default, null will not output the text 'null'. This can be
 * controlled by a property, {@link #setNullText(String)}.
 * </p>
 * <p>
 * This class is called {@code TextStringBuilder} instead of {@code StringBuilder} to avoid clashing with
 * {@link java.lang.StringBuilder}.
 * </p>
 *
 * @since 1.3
 */
public class TextStringBuilder implements CharSequence, Appendable, Serializable, Builder<String> {

    /**
     * Inner class to allow StrBuilder to operate as a reader.
     */
    class TextStringBuilderReader extends Reader {

        /** The last mark position. */
        private int mark;

        /** The current stream position. */
        private int pos;

        /**
         * Default constructor.
         */
        TextStringBuilderReader() {
        }

        /** {@inheritDoc} */
        @Override
        public void close() {
            // do nothing
        }

        /** {@inheritDoc} */
        @Override
        public void mark(final int readAheadLimit) {
            mark = pos;
        }

        /** {@inheritDoc} */
        @Override
        public boolean markSupported() {
            return true;
        }

        /** {@inheritDoc} */
        @Override
        public int read() {
            if (!ready()) {
                return -1;
            }
            return TextStringBuilder.this.charAt(pos++);
        }

        /** {@inheritDoc} */
        @Override
        public int read(final char[] b, final int off, int len) {
            if (off < 0 || len < 0 || off > b.length || off + len > b.length || off + len < 0) {
                throw new IndexOutOfBoundsException();
            }
            if (len == 0) {
                return 0;
            }
            if (pos >= TextStringBuilder.this.size()) {
                return -1;
            }
            if (pos + len > size()) {
                len = TextStringBuilder.this.size() - pos;
            }
            TextStringBuilder.this.getChars(pos, pos + len, b, off);
            pos += len;
            return len;
        }

        /** {@inheritDoc} */
        @Override
        public boolean ready() {
            return pos < TextStringBuilder.this.size();
        }

        /** {@inheritDoc} */
        @Override
        public void reset() {
            pos = mark;
        }

        /** {@inheritDoc} */
        @Override
        public long skip(long n) {
            if (pos + n > TextStringBuilder.this.size()) {
                n = TextStringBuilder.this.size() - pos;
            }
            if (n < 0) {
                return 0;
            }
            pos = Math.addExact(pos, Math.toIntExact(n));
            return n;
        }
    }

    /**
     * Inner class to allow StrBuilder to operate as a tokenizer.
     */
    class TextStringBuilderTokenizer extends StringTokenizer {

        /**
         * Default constructor.
         */
        TextStringBuilderTokenizer() {
        }

        /** {@inheritDoc} */
        @Override
        public String getContent() {
            final String str = super.getContent();
            if (str == null) {
                return TextStringBuilder.this.toString();
            }
            return str;
        }

        /** {@inheritDoc} */
        @Override
        protected List<String> tokenize(final char[] chars, final int offset, final int count) {
            if (chars == null) {
                return super.tokenize(TextStringBuilder.this.getBuffer(), 0, TextStringBuilder.this.size());
            }
            return super.tokenize(chars, offset, count);
        }
    }

    /**
     * Inner class to allow StrBuilder to operate as a writer.
     */
    class TextStringBuilderWriter extends Writer {

        /**
         * Default constructor.
         */
        TextStringBuilderWriter() {
        }

        /** {@inheritDoc} */
        @Override
        public void close() {
            // do nothing
        }

        /** {@inheritDoc} */
        @Override
        public void flush() {
            // do nothing
        }

        /** {@inheritDoc} */
        @Override
        public void write(final char[] cbuf) {
            TextStringBuilder.this.append(cbuf);
        }

        /** {@inheritDoc} */
        @Override
        public void write(final char[] cbuf, final int off, final int len) {
            TextStringBuilder.this.append(cbuf, off, len);
        }

        /** {@inheritDoc} */
        @Override
        public void write(final int c) {
            TextStringBuilder.this.append((char) c);
        }

        /** {@inheritDoc} */
        @Override
        public void write(final String str) {
            TextStringBuilder.this.append(str);
        }

        /** {@inheritDoc} */
        @Override
        public void write(final String str, final int off, final int len) {
            TextStringBuilder.this.append(str, off, len);
        }
    }

    /** The space character. */
    private static final char SPACE = ' ';

    /**
     * The extra capacity for new builders.
     */
    static final int CAPACITY = 32;

    /**
     * End-Of-Stream.
     */
    private static final int EOS = -1;

    /**
     * The size of the string {@code "false"}.
     */
    private static final int FALSE_STRING_SIZE = Boolean.FALSE.toString().length();

    /**
     * Required for serialization support.
     *
     * @see java.io.Serializable
     */
    private static final long serialVersionUID = 1L;

    /**
     * The size of the string {@code "true"}.
     */
    private static final int TRUE_STRING_SIZE = Boolean.TRUE.toString().length();

    /**
     * Constructs an instance from a reference to a character array. Changes to the input chars are reflected in this
     * instance until the internal buffer needs to be reallocated. Using a reference to an array allows the instance to
     * be initialized without copying the input array.
     *
     * @param initialBuffer The initial array that will back the new builder.
     * @return A new instance.
     * @since 1.9
     */
    public static TextStringBuilder wrap(final char[] initialBuffer) {
        Objects.requireNonNull(initialBuffer, "initialBuffer");
        return new TextStringBuilder(initialBuffer, initialBuffer.length);
    }

    /**
     * Constructs an instance from a reference to a character array. Changes to the input chars are reflected in this
     * instance until the internal buffer needs to be reallocated. Using a reference to an array allows the instance to
     * be initialized without copying the input array.
     *
     * @param initialBuffer The initial array that will back the new builder.
     * @param length The length of the subarray to be used; must be non-negative and no larger than
     *        {@code initialBuffer.length}. The new builder's size will be set to {@code length}.
     * @return A new instance.
     * @since 1.9
     */
    public static TextStringBuilder wrap(final char[] initialBuffer, final int length) {
        return new TextStringBuilder(initialBuffer, length);
    }

    /** Internal data storage. */
    private char[] buffer;

    /** The new line. */
    private String newLine;

    /** The null text. */
    private String nullText;

    /** Incremented when the buffer is reallocated. */
    private int reallocations;

    /** Current size of the buffer. */
    private int size;

    /**
     * Constructs an empty builder initial capacity 32 characters.
     */
    public TextStringBuilder() {
        this(CAPACITY);
    }

    /**
     * Constructs an instance from a reference to a character array.
     *
     * @param initialBuffer a reference to a character array, must not be null.
     * @param length The length of the subarray to be used; must be non-negative and no larger than
     *        {@code initialBuffer.length}. The new builder's size will be set to {@code length}.
     * @throws NullPointerException If {@code initialBuffer} is null.
     * @throws IllegalArgumentException if {@code length} is bad.
     */
    private TextStringBuilder(final char[] initialBuffer, final int length) {
        this.buffer = Objects.requireNonNull(initialBuffer, "initialBuffer");
        if (length < 0 || length > initialBuffer.length) {
            throw new IllegalArgumentException("initialBuffer.length=" + initialBuffer.length + ", length=" + length);
        }
        this.size = length;
    }

    /**
     * Constructs an instance from a character sequence, allocating 32 extra characters for growth.
     *
     * @param seq the string to copy, null treated as blank string
     * @since 1.9
     */
    public TextStringBuilder(final CharSequence seq) {
        this(StringUtils.length(seq) + CAPACITY);
        if (seq != null) {
            append(seq);
        }
    }

    /**
     * Constructs an instance with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity, zero or less will be converted to 32
     */
    public TextStringBuilder(final int initialCapacity) {
        buffer = new char[initialCapacity <= 0 ? CAPACITY : initialCapacity];
    }

    /**
     * Constructs an instance from a string, allocating 32 extra characters for growth.
     *
     * @param str the string to copy, null treated as blank string
     */
    public TextStringBuilder(final String str) {
        this(StringUtils.length(str) + CAPACITY);
        if (str != null) {
            append(str);
        }
    }

    /**
     * Appends a boolean value to the string builder.
     *
     * @param value the value to append
     * @return this, to enable chaining
     */
    public TextStringBuilder append(final boolean value) {
        if (value) {
            ensureCapacity(size + TRUE_STRING_SIZE);
            appendTrue(size);
        } else {
            ensureCapacity(size + FALSE_STRING_SIZE);
            appendFalse(size);
        }
        return this;
    }

    /**
     * Appends a char value to the string builder.
     *
     * @param ch the value to append
     * @return this, to enable chaining
     */
    @Override
    public TextStringBuilder append(final char ch) {
        final int len = length();
        ensureCapacity(len + 1);
        buffer[size++] = ch;
        return this;
    }

    /**
     * Appends a char array to the string builder. Appending null will call {@link #appendNull()}.
     *
     * @param chars the char array to append
     * @return this, to enable chaining
     */
    public TextStringBuilder append(final char[] chars) {
        if (chars == null) {
            return appendNull();
        }
        final int strLen = chars.length;
        if (strLen > 0) {
            final int len = length();
            ensureCapacity(len + strLen);
            System.arraycopy(chars, 0, buffer, len, strLen);
            size += strLen;
        }
        return this;
    }

    /**
     * Appends a char array to the string builder. Appending null will call {@link #appendNull()}.
     *
     * @param chars the char array to append
     * @param startIndex the start index, inclusive, must be valid
     * @param length the length to append, must be valid
     * @return this, to enable chaining
     * @throws StringIndexOutOfBoundsException if {@code startIndex} is not in the
     *  range {@code 0 <= startIndex <= chars.length}
     * @throws StringIndexOutOfBoundsException if {@code length < 0}
     * @throws StringIndexOutOfBoundsException if {@code startIndex + length > chars.length}
     */
    public TextStringBuilder append(final char[] chars, final int startIndex, final int length) {
        if (chars == null) {
            return appendNull();
        }
        if (startIndex < 0 || startIndex > chars.length) {
            throw new StringIndexOutOfBoundsException("Invalid startIndex: " + length);
        }
        if (length < 0 || startIndex + length > chars.length) {
            throw new StringIndexOutOfBoundsException("Invalid length: " + length);
        }
        if (length > 0) {
            final int len = length();
            ensureCapacity(len + length);
            System.arraycopy(chars, startIndex, buffer, len, length);
            size += length;
        }
        return this;
    }

    /**
     * Appends the contents of a char buffer to this string builder. Appending null will call {@link #appendNull()}.
     *
     * @param str the char buffer to append
     * @return this, to enable chaining
     */
    public TextStringBuilder append(final CharBuffer str) {
        return append(str, 0, StringUtils.length(str));
    }

    /**
     * Appends the contents of a char buffer to this string builder. Appending null will call {@link #appendNull()}.
     *
     * @param buf the char buffer to append
     * @param startIndex the start index, inclusive, must be valid
     * @param length the length to append, must be valid
     * @return this, to enable chaining
     */
    public TextStringBuilder append(final CharBuffer buf, final int startIndex, final int length) {
        if (buf == null) {
            return appendNull();
        }
        if (buf.hasArray()) {
            final int totalLength = buf.remaining();
            if (startIndex < 0 || startIndex > totalLength) {
                throw new StringIndexOutOfBoundsException("startIndex must be valid");
            }
            if (length < 0 || startIndex + length > totalLength) {
                throw new StringIndexOutOfBoundsException("length must be valid");
            }
            final int len = length();
            ensureCapacity(len + length);
            System.arraycopy(buf.array(), buf.arrayOffset() + buf.position() + startIndex, buffer, len, length);
            size += length;
        } else {
            append(buf.toString(), startIndex, length);
        }
        return this;
    }

    /**
     * Appends a CharSequence to this string builder. Appending null will call {@link #appendNull()}.
     *
     * @param seq the CharSequence to append
     * @return this, to enable chaining
     */
    @Override
    public TextStringBuilder append(final CharSequence seq) {
        if (seq == null) {
            return appendNull();
        }
        if (seq instanceof TextStringBuilder) {
            return append((TextStringBuilder) seq);
        }
        if (seq instanceof StringBuilder) {
            return append((StringBuilder) seq);
        }
        if (seq instanceof StringBuffer) {
            return append((StringBuffer) seq);
        }
        if (seq instanceof CharBuffer) {
            return append((CharBuffer) seq);
        }
        return append(seq.toString());
    }

    /**
     * Appends part of a CharSequence to this string builder. Appending null will call {@link #appendNull()}.
     *
     * @param seq the CharSequence to append
     * @param startIndex the start index, inclusive, must be valid
     * @param endIndex the end index, exclusive, must be valid
     * @return this, to enable chaining
     */
    @Override
    public TextStringBuilder append(final CharSequence seq, final int startIndex, final int endIndex) {
        if (seq == null) {
            return appendNull();
        }
        if (endIndex <= 0) {
            throw new StringIndexOutOfBoundsException("endIndex must be valid");
        }
        if (startIndex >= endIndex) {
            throw new StringIndexOutOfBoundsException("endIndex must be greater than startIndex");
        }
        return append(seq.toString(), startIndex, endIndex - startIndex);
    }

    /**
     * Appends a double value to the string builder using {@code String.valueOf}.
     *
     * @param value the value to append
     * @return this, to enable chaining
     */
    public TextStringBuilder append(final double value) {
        return append(String.valueOf(value));
    }

    /**
     * Appends a float value to the string builder using {@code String.valueOf}.
     *
     * @param value the value to append
     * @return this, to enable chaining
     */
    public TextStringBuilder append(final float value) {
        return append(String.valueOf(value));
    }

    /**
     * Appends an int value to the string builder using {@code String.valueOf}.
     *
     * @param value the value to append
     * @return this, to enable chaining
     */
    public TextStringBuilder append(final int value) {
        return append(String.valueOf(value));
    }

    /**
     * Appends a long value to the string builder using {@code String.valueOf}.
     *
     * @param value the value to append
     * @return this, to enable chaining
     */
    public TextStringBuilder append(final long value) {
        return append(String.valueOf(value));
    }

    /**
     * Appends an object to this string builder. Appending null will call {@link #appendNull()}.
     *
     * @param obj the object to append
     * @return this, to enable chaining
     */
    public TextStringBuilder append(final Object obj) {
        if (obj == null) {
            return appendNull();
        }
        if (obj instanceof CharSequence) {
            return append((CharSequence) obj);
        }
        return append(obj.toString());
    }

    /**
     * Appends a string to this string builder. Appending null will call {@link #appendNull()}.
     *
     * @param str the string to append
     * @return this, to enable chaining
     */
    public TextStringBuilder append(final String str) {
        return append(str, 0, StringUtils.length(str));
    }

    /**
     * Appends part of a string to this string builder. Appending null will call {@link #appendNull()}.
     *
     * @param str the string to append
     * @param startIndex the start index, inclusive, must be valid
     * @param length the length to append, must be valid
     * @return this, to enable chaining
     * @throws StringIndexOutOfBoundsException if {@code startIndex} is not in the
     *  range {@code 0 <= startIndex <= str.length()}
     * @throws StringIndexOutOfBoundsException if {@code length < 0}
     * @throws StringIndexOutOfBoundsException if {@code startIndex + length > str.length()}
     */
    public TextStringBuilder append(final String str, final int startIndex, final int length) {
        if (str == null) {
            return appendNull();
        }
        if (startIndex < 0 || startIndex > str.length()) {
            throw new StringIndexOutOfBoundsException("startIndex must be valid");
        }
        if (length < 0 || startIndex + length > str.length()) {
            throw new StringIndexOutOfBoundsException("length must be valid");
        }
        if (length > 0) {
            final int len = length();
            ensureCapacity(len + length);
            str.getChars(startIndex, startIndex + length, buffer, len);
            size += length;
        }
        return this;
    }

    /**
     * Calls {@link String#format(String, Object...)} and appends the result.
     *
     * @param format the format string
     * @param objs the objects to use in the format string
     * @return {@code this} to enable chaining
     * @see String#format(String, Object...)
     */
    public TextStringBuilder append(final String format, final Object... objs) {
        return append(String.format(format, objs));
    }

    /**
     * Appends a string buffer to this string builder. Appending null will call {@link #appendNull()}.
     *
     * @param str the string buffer to append
     * @return this, to enable chaining
     */
    public TextStringBuilder append(final StringBuffer str) {
        return append(str, 0, StringUtils.length(str));
    }

    /**
     * Appends part of a string buffer to this string builder. Appending null will call {@link #appendNull()}.
     *
     * @param str the string to append
     * @param startIndex the start index, inclusive, must be valid
     * @param length the length to append, must be valid
     * @return this, to enable chaining
     */
    public TextStringBuilder append(final StringBuffer str, final int startIndex, final int length) {
        if (str == null) {
            return appendNull();
        }
        if (startIndex < 0 || startIndex > str.length()) {
            throw new StringIndexOutOfBoundsException("startIndex must be valid");
        }
        if (length < 0 || startIndex + length > str.length()) {
            throw new StringIndexOutOfBoundsException("length must be valid");
        }
        if (length > 0) {
            final int len = length();
            ensureCapacity(len + length);
            str.getChars(startIndex, startIndex + length, buffer, len);
            size += length;
        }
        return this;
    }

    /**
     * Appends a StringBuilder to this string builder. Appending null will call {@link #appendNull()}.
     *
     * @param str the StringBuilder to append
     * @return this, to enable chaining
     */
    public TextStringBuilder append(final StringBuilder str) {
        return append(str, 0, StringUtils.length(str));
    }

    /**
     * Appends part of a StringBuilder to this string builder. Appending null will call {@link #appendNull()}.
     *
     * @param str the StringBuilder to append
     * @param startIndex the start index, inclusive, must be valid
     * @param length the length to append, must be valid
     * @return this, to enable chaining
     */
    public TextStringBuilder append(final StringBuilder str, final int startIndex, final int length) {
        if (str == null) {
            return appendNull();
        }
        if (startIndex < 0 || startIndex > str.length()) {
            throw new StringIndexOutOfBoundsException("startIndex must be valid");
        }
        if (length < 0 || startIndex + length > str.length()) {
            throw new StringIndexOutOfBoundsException("length must be valid");
        }
        if (length > 0) {
            final int len = length();
            ensureCapacity(len + length);
            str.getChars(startIndex, startIndex + length, buffer, len);
            size += length;
        }
        return this;
    }

    /**
     * Appends another string builder to this string builder. Appending null will call {@link #appendNull()}.
     *
     * @param str the string builder to append
     * @return this, to enable chaining
     */
    public TextStringBuilder append(final TextStringBuilder str) {
        return append(str, 0, StringUtils.length(str));
    }

    /**
     * Appends part of a string builder to this string builder. Appending null will call {@link #appendNull()}.
     *
     * @param str the string to append
     * @param startIndex the start index, inclusive, must be valid
     * @param length the length to append, must be valid
     * @return this, to enable chaining
     */
    public TextStringBuilder append(final TextStringBuilder str, final int startIndex, final int length) {
        if (str == null) {
            return appendNull();
        }
        if (startIndex < 0 || startIndex > str.length()) {
            throw new StringIndexOutOfBoundsException("startIndex must be valid");
        }
        if (length < 0 || startIndex + length > str.length()) {
            throw new StringIndexOutOfBoundsException("length must be valid");
        }
        if (length > 0) {
            final int len = length();
            ensureCapacity(len + length);
            str.getChars(startIndex, startIndex + length, buffer, len);
            size += length;
        }
        return this;
    }

    /**
     * Appends each item in an iterable to the builder without any separators. Appending a null iterable will have no
     * effect. Each object is appended using {@link #append(Object)}.
     *
     * @param iterable the iterable to append
     * @return this, to enable chaining
     */
    public TextStringBuilder appendAll(final Iterable<?> iterable) {
        if (iterable != null) {
            for (final Object o : iterable) {
                append(o);
            }
        }
        return this;
    }

    /**
     * Appends each item in an iterator to the builder without any separators. Appending a null iterator will have no
     * effect. Each object is appended using {@link #append(Object)}.
     *
     * @param it the iterator to append
     * @return this, to enable chaining
     */
    public TextStringBuilder appendAll(final Iterator<?> it) {
        if (it != null) {
            while (it.hasNext()) {
                append(it.next());
            }
        }
        return this;
    }

    /**
     * Appends each item in an array to the builder without any separators. Appending a null array will have no effect.
     * Each object is appended using {@link #append(Object)}.
     *
     * @param <T> the element type
     * @param array the array to append
     * @return this, to enable chaining
     */
    public <T> TextStringBuilder appendAll(@SuppressWarnings("unchecked") final T... array) {
        /*
         * @SuppressWarnings used to hide warning about vararg usage. We cannot use @SafeVarargs, since this method is
         * not final. Using @SuppressWarnings is fine, because it isn't inherited by subclasses, so each subclass must
         * vouch for itself whether its use of 'array' is safe.
         */
        if (array != null && array.length > 0) {
            for (final Object element : array) {
                append(element);
            }
        }
        return this;
    }

    /** Appends {@code "false"}. */
    private void appendFalse(int index) {
        buffer[index++] = 'f';
        buffer[index++] = 'a';
        buffer[index++] = 'l';
        buffer[index++] = 's';
        buffer[index] = 'e';
        size += FALSE_STRING_SIZE;
    }

    /**
     * Appends an object to the builder padding on the left to a fixed width. The {@code String.valueOf} of the
     * {@code int} value is used. If the formatted value is larger than the length, the left hand side is lost.
     *
     * @param value the value to append
     * @param width the fixed field width, zero or negative has no effect
     * @param padChar the pad character to use
     * @return this, to enable chaining
     */
    public TextStringBuilder appendFixedWidthPadLeft(final int value, final int width, final char padChar) {
        return appendFixedWidthPadLeft(String.valueOf(value), width, padChar);
    }

    /**
     * Appends an object to the builder padding on the left to a fixed width. The {@code toString} of the object is
     * used. If the object is larger than the length, the left hand side is lost. If the object is null, the null text
     * value is used.
     *
     * @param obj the object to append, null uses null text
     * @param width the fixed field width, zero or negative has no effect
     * @param padChar the pad character to use
     * @return this, to enable chaining
     */
    public TextStringBuilder appendFixedWidthPadLeft(final Object obj, final int width, final char padChar) {
        if (width > 0) {
            ensureCapacity(size + width);
            String str = obj == null ? getNullText() : obj.toString();
            if (str == null) {
                str = StringUtils.EMPTY;
            }
            final int strLen = str.length();
            if (strLen >= width) {
                str.getChars(strLen - width, strLen, buffer, size);
            } else {
                final int padLen = width - strLen;
                for (int i = 0; i < padLen; i++) {
                    buffer[size + i] = padChar;
                }
                str.getChars(0, strLen, buffer, size + padLen);
            }
            size += width;
        }
        return this;
    }

    /**
     * Appends an object to the builder padding on the right to a fixed length. The {@code String.valueOf} of the
     * {@code int} value is used. If the object is larger than the length, the right hand side is lost.
     *
     * @param value the value to append
     * @param width the fixed field width, zero or negative has no effect
     * @param padChar the pad character to use
     * @return this, to enable chaining
     */
    public TextStringBuilder appendFixedWidthPadRight(final int value, final int width, final char padChar) {
        return appendFixedWidthPadRight(String.valueOf(value), width, padChar);
    }

    /**
     * Appends an object to the builder padding on the right to a fixed length. The {@code toString} of the object is
     * used. If the object is larger than the length, the right hand side is lost. If the object is null, null text
     * value is used.
     *
     * @param obj the object to append, null uses null text
     * @param width the fixed field width, zero or negative has no effect
     * @param padChar the pad character to use
     * @return this, to enable chaining
     */
    public TextStringBuilder appendFixedWidthPadRight(final Object obj, final int width, final char padChar) {
        if (width > 0) {
            ensureCapacity(size + width);
            String str = obj == null ? getNullText() : obj.toString();
            if (str == null) {
                str = StringUtils.EMPTY;
            }
            final int strLen = str.length();
            if (strLen >= width) {
                str.getChars(0, width, buffer, size);
            } else {
                final int padLen = width - strLen;
                str.getChars(0, strLen, buffer, size);
                for (int i = 0; i < padLen; i++) {
                    buffer[size + strLen + i] = padChar;
                }
            }
            size += width;
        }
        return this;
    }

    /**
     * Appends a boolean value followed by a new line to the string builder.
     *
     * @param value the value to append
     * @return this, to enable chaining
     */
    public TextStringBuilder appendln(final boolean value) {
        return append(value).appendNewLine();
    }

    /**
     * Appends a char value followed by a new line to the string builder.
     *
     * @param ch the value to append
     * @return this, to enable chaining
     */
    public TextStringBuilder appendln(final char ch) {
        return append(ch).appendNewLine();
    }

    /**
     * Appends a char array followed by a new line to the string builder. Appending null will call
     * {@link #appendNull()}.
     *
     * @param chars the char array to append
     * @return this, to enable chaining
     */
    public TextStringBuilder appendln(final char[] chars) {
        return append(chars).appendNewLine();
    }

    /**
     * Appends a char array followed by a new line to the string builder. Appending null will call
     * {@link #appendNull()}.
     *
     * @param chars the char array to append
     * @param startIndex the start index, inclusive, must be valid
     * @param length the length to append, must be valid
     * @return this, to enable chaining
     */
    public TextStringBuilder appendln(final char[] chars, final int startIndex, final int length) {
        return append(chars, startIndex, length).appendNewLine();
    }

    /**
     * Appends a double value followed by a new line to the string builder using {@code String.valueOf}.
     *
     * @param value the value to append
     * @return this, to enable chaining
     */
    public TextStringBuilder appendln(final double value) {
        return append(value).appendNewLine();
    }

    /**
     * Appends a float value followed by a new line to the string builder using {@code String.valueOf}.
     *
     * @param value the value to append
     * @return this, to enable chaining
     */
    public TextStringBuilder appendln(final float value) {
        return append(value).appendNewLine();
    }

    /**
     * Appends an int value followed by a new line to the string builder using {@code String.valueOf}.
     *
     * @param value the value to append
     * @return this, to enable chaining
     */
    public TextStringBuilder appendln(final int value) {
        return append(value).appendNewLine();
    }

    /**
     * Appends a long value followed by a new line to the string builder using {@code String.valueOf}.
     *
     * @param value the value to append
     * @return this, to enable chaining
     */
    public TextStringBuilder appendln(final long value) {
        return append(value).appendNewLine();
    }

    /**
     * Appends an object followed by a new line to this string builder. Appending null will call {@link #appendNull()}.
     *
     * @param obj the object to append
     * @return this, to enable chaining
     */
    public TextStringBuilder appendln(final Object obj) {
        return append(obj).appendNewLine();
    }

    /**
     * Appends a string followed by a new line to this string builder. Appending null will call {@link #appendNull()}.
     *
     * @param str the string to append
     * @return this, to enable chaining
     */
    public TextStringBuilder appendln(final String str) {
        return append(str).appendNewLine();
    }

    /**
     * Appends part of a string followed by a new line to this string builder. Appending null will call
     * {@link #appendNull()}.
     *
     * @param str the string to append
     * @param startIndex the start index, inclusive, must be valid
     * @param length the length to append, must be valid
     * @return this, to enable chaining
     */
    public TextStringBuilder appendln(final String str, final int startIndex, final int length) {
        return append(str, startIndex, length).appendNewLine();
    }

    /**
     * Calls {@link String#format(String, Object...)} and appends the result.
     *
     * @param format the format string
     * @param objs the objects to use in the format string
     * @return {@code this} to enable chaining
     * @see String#format(String, Object...)
     */
    public TextStringBuilder appendln(final String format, final Object... objs) {
        return append(format, objs).appendNewLine();
    }

    /**
     * Appends a string buffer followed by a new line to this string builder. Appending null will call
     * {@link #appendNull()}.
     *
     * @param str the string buffer to append
     * @return this, to enable chaining
     */
    public TextStringBuilder appendln(final StringBuffer str) {
        return append(str).appendNewLine();
    }

    /**
     * Appends part of a string buffer followed by a new line to this string builder. Appending null will call
     * {@link #appendNull()}.
     *
     * @param str the string to append
     * @param startIndex the start index, inclusive, must be valid
     * @param length the length to append, must be valid
     * @return this, to enable chaining
     */
    public TextStringBuilder appendln(final StringBuffer str, final int startIndex, final int length) {
        return append(str, startIndex, length).appendNewLine();
    }

    /**
     * Appends a string builder followed by a new line to this string builder. Appending null will call
     * {@link #appendNull()}.
     *
     * @param str the string builder to append
     * @return this, to enable chaining
     */
    public TextStringBuilder appendln(final StringBuilder str) {
        return append(str).appendNewLine();
    }

    /**
     * Appends part of a string builder followed by a new line to this string builder. Appending null will call
     * {@link #appendNull()}.
     *
     * @param str the string builder to append
     * @param startIndex the start index, inclusive, must be valid
     * @param length the length to append, must be valid
     * @return this, to enable chaining
     */
    public TextStringBuilder appendln(final StringBuilder str, final int startIndex, final int length) {
        return append(str, startIndex, length).appendNewLine();
    }

    /**
     * Appends another string builder followed by a new line to this string builder. Appending null will call
     * {@link #appendNull()}.
     *
     * @param str the string builder to append
     * @return this, to enable chaining
     */
    public TextStringBuilder appendln(final TextStringBuilder str) {
        return append(str).appendNewLine();
    }

    /**
     * Appends part of a string builder followed by a new line to this string builder. Appending null will call
     * {@link #appendNull()}.
     *
     * @param str the string to append
     * @param startIndex the start index, inclusive, must be valid
     * @param length the length to append, must be valid
     * @return this, to enable chaining
     */
    public TextStringBuilder appendln(final TextStringBuilder str, final int startIndex, final int length) {
        return append(str, startIndex, length).appendNewLine();
    }

    /**
     * Appends the new line string to this string builder.
     * <p>
     * The new line string can be altered using {@link #setNewLineText(String)}. This might be used to force the output
     * to always use Unix line endings even when on Windows.
     * </p>
     *
     * @return this, to enable chaining
     */
    public TextStringBuilder appendNewLine() {
        if (newLine == null) {
            append(System.lineSeparator());
            return this;
        }
        return append(newLine);
    }

    /**
     * Appends the text representing {@code null} to this string builder.
     *
     * @return this, to enable chaining
     */
    public TextStringBuilder appendNull() {
        if (nullText == null) {
            return this;
        }
        return append(nullText);
    }

    /**
     * Appends the pad character to the builder the specified number of times.
     *
     * @param length the length to append, negative means no append
     * @param padChar the character to append
     * @return this, to enable chaining
     */
    public TextStringBuilder appendPadding(final int length, final char padChar) {
        if (length >= 0) {
            ensureCapacity(size + length);
            for (int i = 0; i < length; i++) {
                buffer[size++] = padChar;
            }
        }
        return this;
    }

    /**
     * Appends a separator if the builder is currently non-empty. The separator is appended using {@link #append(char)}.
     * <p>
     * This method is useful for adding a separator each time around the loop except the first.
     * </p>
     *
     * <pre>
     * for (Iterator it = list.iterator(); it.hasNext();) {
     *     appendSeparator(',');
     *     append(it.next());
     * }
     * </pre>
     *
     * <p>
     * Note that for this simple example, you should use {@link #appendWithSeparators(Iterable, String)}.
     * </p>
     *
     * @param separator the separator to use
     * @return this, to enable chaining
     */
    public TextStringBuilder appendSeparator(final char separator) {
        if (isNotEmpty()) {
            append(separator);
        }
        return this;
    }

    /**
     * Appends one of both separators to the builder If the builder is currently empty it will append the
     * defaultIfEmpty-separator Otherwise it will append the standard-separator
     *
     * The separator is appended using {@link #append(char)}.
     *
     * @param standard the separator if builder is not empty
     * @param defaultIfEmpty the separator if builder is empty
     * @return this, to enable chaining
     */
    public TextStringBuilder appendSeparator(final char standard, final char defaultIfEmpty) {
        if (isEmpty()) {
            append(defaultIfEmpty);
        } else {
            append(standard);
        }
        return this;
    }

    /**
     * Appends a separator to the builder if the loop index is greater than zero. The separator is appended using
     * {@link #append(char)}.
     * <p>
     * This method is useful for adding a separator each time around the loop except the first.
     * </p>
     *
     * <pre>
     * for (int i = 0; i &lt; list.size(); i++) {
     *     appendSeparator(",", i);
     *     append(list.get(i));
     * }
     * </pre>
     *
     * <p>
     * Note that for this simple example, you should use {@link #appendWithSeparators(Iterable, String)}.
     * </p>
     *
     * @param separator the separator to use
     * @param loopIndex the loop index
     * @return this, to enable chaining
     */
    public TextStringBuilder appendSeparator(final char separator, final int loopIndex) {
        if (loopIndex > 0) {
            append(separator);
        }
        return this;
    }

    /**
     * Appends a separator if the builder is currently non-empty. Appending a null separator will have no effect. The
     * separator is appended using {@link #append(String)}.
     * <p>
     * This method is useful for adding a separator each time around the loop except the first.
     * </p>
     *
     * <pre>
     * for (Iterator it = list.iterator(); it.hasNext();) {
     *     appendSeparator(",");
     *     append(it.next());
     * }
     * </pre>
     *
     * <p>
     * Note that for this simple example, you should use {@link #appendWithSeparators(Iterable, String)}.
     * </p>
     *
     * @param separator the separator to use, null means no separator
     * @return this, to enable chaining
     */
    public TextStringBuilder appendSeparator(final String separator) {
        return appendSeparator(separator, null);
    }

    /**
     * Appends a separator to the builder if the loop index is greater than zero. Appending a null separator will have
     * no effect. The separator is appended using {@link #append(String)}.
     * <p>
     * This method is useful for adding a separator each time around the loop except the first.
     * </p>
     *
     * <pre>
     * for (int i = 0; i &lt; list.size(); i++) {
     *     appendSeparator(",", i);
     *     append(list.get(i));
     * }
     * </pre>
     *
     * <p>
     * Note that for this simple example, you should use {@link #appendWithSeparators(Iterable, String)}.
     * </p>
     *
     * @param separator the separator to use, null means no separator
     * @param loopIndex the loop index
     * @return this, to enable chaining
     */
    public TextStringBuilder appendSeparator(final String separator, final int loopIndex) {
        if (separator != null && loopIndex > 0) {
            append(separator);
        }
        return this;
    }

    /**
     * Appends one of both separators to the StrBuilder. If the builder is currently empty it will append the
     * defaultIfEmpty-separator Otherwise it will append the standard-separator
     *
     * Appending a null separator will have no effect. The separator is appended using {@link #append(String)}.
     * <p>
     * This method is for example useful for constructing queries
     * </p>
     *
     * <pre>
     * StrBuilder whereClause = new StrBuilder();
     * if(searchCommand.getPriority() != null) {
     *  whereClause.appendSeparator(" and", " where");
     *  whereClause.append(" priority = ?")
     * }
     * if(searchCommand.getComponent() != null) {
     *  whereClause.appendSeparator(" and", " where");
     *  whereClause.append(" component = ?")
     * }
     * selectClause.append(whereClause)
     * </pre>
     *
     * @param standard the separator if builder is not empty, null means no separator
     * @param defaultIfEmpty the separator if builder is empty, null means no separator
     * @return this, to enable chaining
     */
    public TextStringBuilder appendSeparator(final String standard, final String defaultIfEmpty) {
        final String str = isEmpty() ? defaultIfEmpty : standard;
        if (str != null) {
            append(str);
        }
        return this;
    }

    /**
     * Appends current contents of this {@code StrBuilder} to the provided {@link Appendable}.
     * <p>
     * This method tries to avoid doing any extra copies of contents.
     * </p>
     *
     * @param appendable the appendable to append data to
     * @throws IOException if an I/O error occurs.
     *
     * @see #readFrom(Readable)
     */
    public void appendTo(final Appendable appendable) throws IOException {
        if (appendable instanceof Writer) {
            ((Writer) appendable).write(buffer, 0, size);
        } else if (appendable instanceof StringBuilder) {
            ((StringBuilder) appendable).append(buffer, 0, size);
        } else if (appendable instanceof StringBuffer) {
            ((StringBuffer) appendable).append(buffer, 0, size);
        } else if (appendable instanceof CharBuffer) {
            ((CharBuffer) appendable).put(buffer, 0, size);
        } else {
            appendable.append(this);
        }
    }

    /** Appends {@code "true"}. */
    private void appendTrue(int index) {
        buffer[index++] = 't';
        buffer[index++] = 'r';
        buffer[index++] = 'u';
        buffer[index] = 'e';
        size += TRUE_STRING_SIZE;
    }

    /**
     * Appends an iterable placing separators between each value, but not before the first or after the last. Appending
     * a null iterable will have no effect. Each object is appended using {@link #append(Object)}.
     *
     * @param iterable the iterable to append
     * @param separator the separator to use, null means no separator
     * @return this, to enable chaining
     */
    public TextStringBuilder appendWithSeparators(final Iterable<?> iterable, final String separator) {
        if (iterable != null) {
            final String sep = Objects.toString(separator, StringUtils.EMPTY);
            final Iterator<?> it = iterable.iterator();
            while (it.hasNext()) {
                append(it.next());
                if (it.hasNext()) {
                    append(sep);
                }
            }
        }
        return this;
    }

    /**
     * Appends an iterator placing separators between each value, but not before the first or after the last. Appending
     * a null iterator will have no effect. Each object is appended using {@link #append(Object)}.
     *
     * @param it the iterator to append
     * @param separator the separator to use, null means no separator
     * @return this, to enable chaining
     */
    public TextStringBuilder appendWithSeparators(final Iterator<?> it, final String separator) {
        if (it != null) {
            final String sep = Objects.toString(separator, StringUtils.EMPTY);
            while (it.hasNext()) {
                append(it.next());
                if (it.hasNext()) {
                    append(sep);
                }
            }
        }
        return this;
    }

    /**
     * Appends an array placing separators between each value, but not before the first or after the last. Appending a
     * null array will have no effect. Each object is appended using {@link #append(Object)}.
     *
     * @param array the array to append
     * @param separator the separator to use, null means no separator
     * @return this, to enable chaining
     */
    public TextStringBuilder appendWithSeparators(final Object[] array, final String separator) {
        if (array != null && array.length > 0) {
            final String sep = Objects.toString(separator, StringUtils.EMPTY);
            append(array[0]);
            for (int i = 1; i < array.length; i++) {
                append(sep);
                append(array[i]);
            }
        }
        return this;
    }

    /**
     * Gets the contents of this builder as a Reader.
     * <p>
     * This method allows the contents of the builder to be read using any standard method that expects a Reader.
     * </p>
     * <p>
     * To use, simply create a {@code StrBuilder}, populate it with data, call {@code asReader}, and then read away.
     * </p>
     * <p>
     * The internal character array is shared between the builder and the reader. This allows you to append to the
     * builder after creating the reader, and the changes will be picked up. Note however, that no synchronization
     * occurs, so you must perform all operations with the builder and the reader in one thread.
     * </p>
     * <p>
     * The returned reader supports marking, and ignores the flush method.
     * </p>
     *
     * @return a reader that reads from this builder
     */
    public Reader asReader() {
        return new TextStringBuilderReader();
    }

    /**
     * Creates a tokenizer that can tokenize the contents of this builder.
     * <p>
     * This method allows the contents of this builder to be tokenized. The tokenizer will be setup by default to
     * tokenize on space, tab, newline and form feed (as per StringTokenizer). These values can be changed on the
     * tokenizer class, before retrieving the tokens.
     * </p>
     * <p>
     * The returned tokenizer is linked to this builder. You may intermix calls to the builder and tokenizer within
     * certain limits, however there is no synchronization. Once the tokenizer has been used once, it must be
     * {@link StringTokenizer#reset() reset} to pickup the latest changes in the builder. For example:
     * </p>
     *
     * <pre>
     * StrBuilder b = new StrBuilder();
     * b.append("a b ");
     * StrTokenizer t = b.asTokenizer();
     * String[] tokens1 = t.getTokenArray(); // returns a,b
     * b.append("c d ");
     * String[] tokens2 = t.getTokenArray(); // returns a,b (c and d ignored)
     * t.reset(); // reset causes builder changes to be picked up
     * String[] tokens3 = t.getTokenArray(); // returns a,b,c,d
     * </pre>
     *
     * <p>
     * In addition to simply intermixing appends and tokenization, you can also call the set methods on the tokenizer to
     * alter how it tokenizes. Just remember to call reset when you want to pickup builder changes.
     * </p>
     * <p>
     * Calling {@link StringTokenizer#reset(String)} or {@link StringTokenizer#reset(char[])} with a non-null value will
     * break the link with the builder.
     * </p>
     *
     * @return a tokenizer that is linked to this builder
     */
    public StringTokenizer asTokenizer() {
        return new TextStringBuilderTokenizer();
    }

    /**
     * Gets this builder as a Writer that can be written to.
     * <p>
     * This method allows you to populate the contents of the builder using any standard method that takes a Writer.
     * </p>
     * <p>
     * To use, simply create a {@code StrBuilder}, call {@code asWriter}, and populate away. The data is available at
     * any time using the methods of the {@code StrBuilder}.
     * </p>
     * <p>
     * The internal character array is shared between the builder and the writer. This allows you to intermix calls that
     * append to the builder and write using the writer and the changes will be occur correctly. Note however, that no
     * synchronization occurs, so you must perform all operations with the builder and the writer in one thread.
     * </p>
     * <p>
     * The returned writer ignores the close and flush methods.
     * </p>
     *
     * @return a writer that populates this builder
     */
    public Writer asWriter() {
        return new TextStringBuilderWriter();
    }

    /**
     * Implement the {@link Builder} interface.
     *
     * @return The builder as a String
     * @see #toString()
     */
    @Override
    public String build() {
        return toString();
    }

    /**
     * Gets the current size of the internal character array buffer.
     *
     * @return The capacity
     */
    public int capacity() {
        return buffer.length;
    }

    /**
     * Gets the character at the specified index.
     *
     * @see #setCharAt(int, char)
     * @see #deleteCharAt(int)
     * @param index the index to retrieve, must be valid
     * @return The character at the index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    @Override
    public char charAt(final int index) {
        validateIndex(index);
        return buffer[index];
    }

    /**
     * Clears the string builder (convenience Collections API style method).
     * <p>
     * This method does not reduce the size of the internal character buffer. To do that, call {@code clear()} followed
     * by {@link #minimizeCapacity()}.
     * </p>
     * <p>
     * This method is the same as {@link #setLength(int)} called with zero and is provided to match the API of
     * Collections.
     * </p>
     *
     * @return this, to enable chaining
     */
    public TextStringBuilder clear() {
        size = 0;
        return this;
    }

    /**
     * Tests if the string builder contains the specified char.
     *
     * @param ch the character to find
     * @return true if the builder contains the character
     */
    public boolean contains(final char ch) {
        final char[] thisBuf = buffer;
        for (int i = 0; i < this.size; i++) {
            if (thisBuf[i] == ch) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests if the string builder contains the specified string.
     *
     * @param str the string to find
     * @return true if the builder contains the string
     */
    public boolean contains(final String str) {
        return indexOf(str, 0) >= 0;
    }

    /**
     * Tests if the string builder contains a string matched using the specified matcher.
     * <p>
     * Matchers can be used to perform advanced searching behavior. For example you could write a matcher to search for
     * the character 'a' followed by a number.
     * </p>
     *
     * @param matcher the matcher to use, null returns -1
     * @return true if the matcher finds a match in the builder
     */
    public boolean contains(final StringMatcher matcher) {
        return indexOf(matcher, 0) >= 0;
    }

    /**
     * Deletes the characters between the two specified indices.
     *
     * @param startIndex the start index, inclusive, must be valid
     * @param endIndex the end index, exclusive, must be valid except that if too large it is treated as end of string
     * @return this, to enable chaining
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public TextStringBuilder delete(final int startIndex, final int endIndex) {
        final int actualEndIndex = validateRange(startIndex, endIndex);
        final int len = actualEndIndex - startIndex;
        if (len > 0) {
            deleteImpl(startIndex, actualEndIndex, len);
        }
        return this;
    }

    /**
     * Deletes the character wherever it occurs in the builder.
     *
     * @param ch the character to delete
     * @return this, to enable chaining
     */
    public TextStringBuilder deleteAll(final char ch) {
        for (int i = 0; i < size; i++) {
            if (buffer[i] == ch) {
                final int start = i;
                while (++i < size) {
                    if (buffer[i] != ch) {
                        break;
                    }
                }
                final int len = i - start;
                deleteImpl(start, i, len);
                i -= len;
            }
        }
        return this;
    }

    /**
     * Deletes the string wherever it occurs in the builder.
     *
     * @param str the string to delete, null causes no action
     * @return this, to enable chaining
     */
    public TextStringBuilder deleteAll(final String str) {
        final int len = str == null ? 0 : str.length();
        if (len > 0) {
            int index = indexOf(str, 0);
            while (index >= 0) {
                deleteImpl(index, index + len, len);
                index = indexOf(str, index);
            }
        }
        return this;
    }

    /**
     * Deletes all parts of the builder that the matcher matches.
     * <p>
     * Matchers can be used to perform advanced deletion behavior. For example you could write a matcher to delete all
     * occurrences where the character 'a' is followed by a number.
     * </p>
     *
     * @param matcher the matcher to use to find the deletion, null causes no action
     * @return this, to enable chaining
     */
    public TextStringBuilder deleteAll(final StringMatcher matcher) {
        return replace(matcher, null, 0, size, -1);
    }

    /**
     * Deletes the character at the specified index.
     *
     * @see #charAt(int)
     * @see #setCharAt(int, char)
     * @param index the index to delete
     * @return this, to enable chaining
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public TextStringBuilder deleteCharAt(final int index) {
        validateIndex(index);
        deleteImpl(index, index + 1, 1);
        return this;
    }

    /**
     * Deletes the character wherever it occurs in the builder.
     *
     * @param ch the character to delete
     * @return this, to enable chaining
     */
    public TextStringBuilder deleteFirst(final char ch) {
        for (int i = 0; i < size; i++) {
            if (buffer[i] == ch) {
                deleteImpl(i, i + 1, 1);
                break;
            }
        }
        return this;
    }

    /**
     * Deletes the string wherever it occurs in the builder.
     *
     * @param str the string to delete, null causes no action
     * @return this, to enable chaining
     */
    public TextStringBuilder deleteFirst(final String str) {
        final int len = str == null ? 0 : str.length();
        if (len > 0) {
            final int index = indexOf(str, 0);
            if (index >= 0) {
                deleteImpl(index, index + len, len);
            }
        }
        return this;
    }

    /**
     * Deletes the first match within the builder using the specified matcher.
     * <p>
     * Matchers can be used to perform advanced deletion behavior. For example you could write a matcher to delete where
     * the character 'a' is followed by a number.
     * </p>
     *
     * @param matcher the matcher to use to find the deletion, null causes no action
     * @return this, to enable chaining
     */
    public TextStringBuilder deleteFirst(final StringMatcher matcher) {
        return replace(matcher, null, 0, size, 1);
    }

    /**
     * Internal method to delete a range without validation.
     *
     * @param startIndex the start index, must be valid
     * @param endIndex the end index (exclusive), must be valid
     * @param len the length, must be valid
     * @throws IndexOutOfBoundsException if any index is invalid
     */
    private void deleteImpl(final int startIndex, final int endIndex, final int len) {
        System.arraycopy(buffer, endIndex, buffer, startIndex, size - endIndex);
        size -= len;
    }

    /**
     * Gets the character at the specified index before deleting it.
     *
     * @see #charAt(int)
     * @see #deleteCharAt(int)
     * @param index the index to retrieve, must be valid
     * @return The character at the index
     * @throws IndexOutOfBoundsException if the index is invalid
     * @since 1.9
     */
    public char drainChar(final int index) {
        validateIndex(index);
        final char c = buffer[index];
        deleteCharAt(index);
        return c;
    }

    /**
     * Drains (copies, then deletes) this character sequence into the specified array. This is equivalent to copying the
     * characters from this sequence into the target and then deleting those character from this sequence.
     *
     * @param startIndex first index to copy, inclusive.
     * @param endIndex last index to copy, exclusive.
     * @param target the target array, must not be {@code null}.
     * @param targetIndex the index to start copying in the target.
     * @return How many characters where copied (then deleted). If this builder is empty, return {@code 0}.
     * @since 1.9
     */
    public int drainChars(final int startIndex, final int endIndex, final char[] target, final int targetIndex) {
        final int length = endIndex - startIndex;
        if (isEmpty() || length == 0 || target.length == 0) {
            return 0;
        }
        final int actualLen = Math.min(Math.min(size, length), target.length - targetIndex);
        getChars(startIndex, actualLen, target, targetIndex);
        delete(startIndex, actualLen);
        return actualLen;
    }

    /**
     * Checks whether this builder ends with the specified string.
     * <p>
     * Note that this method handles null input quietly, unlike String.
     * </p>
     *
     * @param str the string to search for, null returns false
     * @return true if the builder ends with the string
     */
    public boolean endsWith(final String str) {
        if (str == null) {
            return false;
        }
        final int len = str.length();
        if (len == 0) {
            return true;
        }
        if (len > size) {
            return false;
        }
        int pos = size - len;
        for (int i = 0; i < len; i++, pos++) {
            if (buffer[pos] != str.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Tests the capacity and ensures that it is at least the size specified.
     *
     * @param capacity the capacity to ensure
     * @return this, to enable chaining
     */
    public TextStringBuilder ensureCapacity(final int capacity) {
        if (capacity > buffer.length) {
            reallocate(capacity * 2);
        }
        return this;
    }

    /**
     * Tests the contents of this builder against another to see if they contain the same character content.
     *
     * @param obj the object to check, null returns false
     * @return true if the builders contain the same characters in the same order
     */
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof TextStringBuilder && equals((TextStringBuilder) obj);
    }

    /**
     * Tests the contents of this builder against another to see if they contain the same character content.
     *
     * @param other the object to check, null returns false
     * @return true if the builders contain the same characters in the same order
     */
    public boolean equals(final TextStringBuilder other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (this.size != other.size) {
            return false;
        }
        // Be aware not to use Arrays.equals(buffer, other.buffer) for equals() method
        // as length of the buffers may be different (TEXT-211)
        final char[] thisBuf = this.buffer;
        final char[] otherBuf = other.buffer;
        for (int i = size - 1; i >= 0; i--) {
            if (thisBuf[i] != otherBuf[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Tests the contents of this builder against another to see if they contain the same character content ignoring
     * case.
     *
     * @param other the object to check, null returns false
     * @return true if the builders contain the same characters in the same order
     */
    public boolean equalsIgnoreCase(final TextStringBuilder other) {
        if (this == other) {
            return true;
        }
        if (this.size != other.size) {
            return false;
        }
        final char[] thisBuf = this.buffer;
        final char[] otherBuf = other.buffer;
        for (int i = size - 1; i >= 0; i--) {
            final char c1 = thisBuf[i];
            final char c2 = otherBuf[i];
            if (c1 != c2 && Character.toUpperCase(c1) != Character.toUpperCase(c2)) {
                return false;
            }
        }
        return true;
    }

    /** Gets a direct reference to internal storage, not for public consumption. */
    char[] getBuffer() {
        return buffer;
    }

    /**
     * Copies this character array into the specified array.
     *
     * @param target the target array, null will cause an array to be created
     * @return The input array, unless that was null or too small
     */
    public char[] getChars(char[] target) {
        final int len = length();
        if (target == null || target.length < len) {
            target = new char[len];
        }
        System.arraycopy(buffer, 0, target, 0, len);
        return target;
    }

    /**
     * Copies this character array into the specified array.
     *
     * @param startIndex first index to copy, inclusive, must be valid.
     * @param endIndex last index to copy, exclusive, must be valid.
     * @param target the target array, must not be null or too small.
     * @param targetIndex the index to start copying in target.
     * @throws NullPointerException if the array is null.
     * @throws IndexOutOfBoundsException if any index is invalid.
     */
    public void getChars(final int startIndex, final int endIndex, final char[] target, final int targetIndex) {
        if (startIndex < 0) {
            throw new StringIndexOutOfBoundsException(startIndex);
        }
        if (endIndex < 0 || endIndex > length()) {
            throw new StringIndexOutOfBoundsException(endIndex);
        }
        if (startIndex > endIndex) {
            throw new StringIndexOutOfBoundsException("end < start");
        }
        System.arraycopy(buffer, startIndex, target, targetIndex, endIndex - startIndex);
    }

    /**
     * Gets the text to be appended when a new line is added.
     *
     * @return The new line text, null means use system default
     */
    public String getNewLineText() {
        return newLine;
    }

    /**
     * Gets the text to be appended when null is added.
     *
     * @return The null text, null means no append
     */
    public String getNullText() {
        return nullText;
    }

    /**
     * Gets a suitable hash code for this builder.
     *
     * @return a hash code
     */
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    /**
     * Searches the string builder to find the first reference to the specified char.
     *
     * @param ch the character to find
     * @return The first index of the character, or -1 if not found
     */
    public int indexOf(final char ch) {
        return indexOf(ch, 0);
    }

    /**
     * Searches the string builder to find the first reference to the specified char.
     *
     * @param ch the character to find
     * @param startIndex the index to start at, invalid index rounded to edge
     * @return The first index of the character, or -1 if not found
     */
    public int indexOf(final char ch, int startIndex) {
        startIndex = Math.max(0, startIndex);
        if (startIndex >= size) {
            return StringUtils.INDEX_NOT_FOUND;
        }
        final char[] thisBuf = buffer;
        for (int i = startIndex; i < size; i++) {
            if (thisBuf[i] == ch) {
                return i;
            }
        }
        return StringUtils.INDEX_NOT_FOUND;
    }

    /**
     * Searches the string builder to find the first reference to the specified string.
     * <p>
     * Note that a null input string will return -1, whereas the JDK throws an exception.
     * </p>
     *
     * @param str the string to find, null returns -1
     * @return The first index of the string, or -1 if not found
     */
    public int indexOf(final String str) {
        return indexOf(str, 0);
    }

    /**
     * Searches the string builder to find the first reference to the specified string starting searching from the given
     * index.
     * <p>
     * Note that a null input string will return -1, whereas the JDK throws an exception.
     * </p>
     *
     * @param str the string to find, null returns -1
     * @param startIndex the index to start at, invalid index rounded to edge
     * @return The first index of the string, or -1 if not found
     */
    public int indexOf(final String str, int startIndex) {
        startIndex = Math.max(0, startIndex);
        if (str == null || startIndex >= size) {
            return StringUtils.INDEX_NOT_FOUND;
        }
        final int strLen = str.length();
        if (strLen == 1) {
            return indexOf(str.charAt(0), startIndex);
        }
        if (strLen == 0) {
            return startIndex;
        }
        if (strLen > size) {
            return StringUtils.INDEX_NOT_FOUND;
        }
        final char[] thisBuf = buffer;
        final int len = size - strLen + 1;
        outer: for (int i = startIndex; i < len; i++) {
            for (int j = 0; j < strLen; j++) {
                if (str.charAt(j) != thisBuf[i + j]) {
                    continue outer;
                }
            }
            return i;
        }
        return StringUtils.INDEX_NOT_FOUND;
    }

    /**
     * Searches the string builder using the matcher to find the first match.
     * <p>
     * Matchers can be used to perform advanced searching behavior. For example you could write a matcher to find the
     * character 'a' followed by a number.
     * </p>
     *
     * @param matcher the matcher to use, null returns -1
     * @return The first index matched, or -1 if not found
     */
    public int indexOf(final StringMatcher matcher) {
        return indexOf(matcher, 0);
    }

    /**
     * Searches the string builder using the matcher to find the first match searching from the given index.
     * <p>
     * Matchers can be used to perform advanced searching behavior. For example you could write a matcher to find the
     * character 'a' followed by a number.
     * </p>
     *
     * @param matcher the matcher to use, null returns -1
     * @param startIndex the index to start at, invalid index rounded to edge
     * @return The first index matched, or -1 if not found
     */
    public int indexOf(final StringMatcher matcher, int startIndex) {
        startIndex = Math.max(0, startIndex);
        if (matcher == null || startIndex >= size) {
            return StringUtils.INDEX_NOT_FOUND;
        }
        final int len = size;
        final char[] buf = buffer;
        for (int i = startIndex; i < len; i++) {
            if (matcher.isMatch(buf, i, startIndex, len) > 0) {
                return i;
            }
        }
        return StringUtils.INDEX_NOT_FOUND;
    }

    /**
     * Inserts the value into this builder.
     *
     * @param index the index to add at, must be valid
     * @param value the value to insert
     * @return this, to enable chaining
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public TextStringBuilder insert(final int index, final boolean value) {
        validateIndex(index);
        if (value) {
            ensureCapacity(size + TRUE_STRING_SIZE);
            System.arraycopy(buffer, index, buffer, index + TRUE_STRING_SIZE, size - index);
            appendTrue(index);
        } else {
            ensureCapacity(size + FALSE_STRING_SIZE);
            System.arraycopy(buffer, index, buffer, index + FALSE_STRING_SIZE, size - index);
            appendFalse(index);
        }
        return this;
    }

    /**
     * Inserts the value into this builder.
     *
     * @param index the index to add at, must be valid
     * @param value the value to insert
     * @return this, to enable chaining
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public TextStringBuilder insert(final int index, final char value) {
        validateIndex(index);
        ensureCapacity(size + 1);
        System.arraycopy(buffer, index, buffer, index + 1, size - index);
        buffer[index] = value;
        size++;
        return this;
    }

    /**
     * Inserts the character array into this builder. Inserting null will use the stored null text value.
     *
     * @param index the index to add at, must be valid
     * @param chars the char array to insert
     * @return this, to enable chaining
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public TextStringBuilder insert(final int index, final char[] chars) {
        validateIndex(index);
        if (chars == null) {
            return insert(index, nullText);
        }
        final int len = chars.length;
        if (len > 0) {
            ensureCapacity(size + len);
            System.arraycopy(buffer, index, buffer, index + len, size - index);
            System.arraycopy(chars, 0, buffer, index, len);
            size += len;
        }
        return this;
    }

    /**
     * Inserts part of the character array into this builder. Inserting null will use the stored null text value.
     *
     * @param index the index to add at, must be valid
     * @param chars the char array to insert
     * @param offset the offset into the character array to start at, must be valid
     * @param length the length of the character array part to copy, must be positive
     * @return this, to enable chaining
     * @throws IndexOutOfBoundsException if any index is invalid
     */
    public TextStringBuilder insert(final int index, final char[] chars, final int offset, final int length) {
        validateIndex(index);
        if (chars == null) {
            return insert(index, nullText);
        }
        if (offset < 0 || offset > chars.length) {
            throw new StringIndexOutOfBoundsException("Invalid offset: " + offset);
        }
        if (length < 0 || offset + length > chars.length) {
            throw new StringIndexOutOfBoundsException("Invalid length: " + length);
        }
        if (length > 0) {
            ensureCapacity(size + length);
            System.arraycopy(buffer, index, buffer, index + length, size - index);
            System.arraycopy(chars, offset, buffer, index, length);
            size += length;
        }
        return this;
    }

    /**
     * Inserts the value into this builder.
     *
     * @param index the index to add at, must be valid
     * @param value the value to insert
     * @return this, to enable chaining
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public TextStringBuilder insert(final int index, final double value) {
        return insert(index, String.valueOf(value));
    }

    /**
     * Inserts the value into this builder.
     *
     * @param index the index to add at, must be valid
     * @param value the value to insert
     * @return this, to enable chaining
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public TextStringBuilder insert(final int index, final float value) {
        return insert(index, String.valueOf(value));
    }

    /**
     * Inserts the value into this builder.
     *
     * @param index the index to add at, must be valid
     * @param value the value to insert
     * @return this, to enable chaining
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public TextStringBuilder insert(final int index, final int value) {
        return insert(index, String.valueOf(value));
    }

    /**
     * Inserts the value into this builder.
     *
     * @param index the index to add at, must be valid
     * @param value the value to insert
     * @return this, to enable chaining
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public TextStringBuilder insert(final int index, final long value) {
        return insert(index, String.valueOf(value));
    }

    /**
     * Inserts the string representation of an object into this builder. Inserting null will use the stored null text
     * value.
     *
     * @param index the index to add at, must be valid
     * @param obj the object to insert
     * @return this, to enable chaining
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public TextStringBuilder insert(final int index, final Object obj) {
        if (obj == null) {
            return insert(index, nullText);
        }
        return insert(index, obj.toString());
    }

    /**
     * Inserts the string into this builder. Inserting null will use the stored null text value.
     *
     * @param index the index to add at, must be valid
     * @param str the string to insert
     * @return this, to enable chaining
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public TextStringBuilder insert(final int index, String str) {
        validateIndex(index);
        if (str == null) {
            str = nullText;
        }
        if (str != null) {
            final int strLen = str.length();
            if (strLen > 0) {
                final int newSize = size + strLen;
                ensureCapacity(newSize);
                System.arraycopy(buffer, index, buffer, index + strLen, size - index);
                size = newSize;
                str.getChars(0, strLen, buffer, index);
            }
        }
        return this;
    }

    /**
     * Checks is the string builder is empty (convenience Collections API style method).
     * <p>
     * This method is the same as checking {@link #length()} and is provided to match the API of Collections.
     * </p>
     *
     * @return {@code true} if the size is {@code 0}.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Checks is the string builder is not empty.
     * <p>
     * This method is the same as checking {@link #length()}.
     * </p>
     *
     * @return {@code true} if the size is not {@code 0}.
     * @since 1.9
     */
    public boolean isNotEmpty() {
        return size != 0;
    }

    /**
     * Gets whether the internal buffer has been reallocated.
     *
     * @return Whether the internal buffer has been reallocated.
     * @since 1.9
     */
    public boolean isReallocated() {
        return reallocations > 0;
    }

    /**
     * Searches the string builder to find the last reference to the specified char.
     *
     * @param ch the character to find
     * @return The last index of the character, or -1 if not found
     */
    public int lastIndexOf(final char ch) {
        return lastIndexOf(ch, size - 1);
    }

    /**
     * Searches the string builder to find the last reference to the specified char.
     *
     * @param ch the character to find
     * @param startIndex the index to start at, invalid index rounded to edge
     * @return The last index of the character, or -1 if not found
     */
    public int lastIndexOf(final char ch, int startIndex) {
        startIndex = startIndex >= size ? size - 1 : startIndex;
        if (startIndex < 0) {
            return StringUtils.INDEX_NOT_FOUND;
        }
        for (int i = startIndex; i >= 0; i--) {
            if (buffer[i] == ch) {
                return i;
            }
        }
        return StringUtils.INDEX_NOT_FOUND;
    }

    /**
     * Searches the string builder to find the last reference to the specified string.
     * <p>
     * Note that a null input string will return -1, whereas the JDK throws an exception.
     * </p>
     *
     * @param str the string to find, null returns -1
     * @return The last index of the string, or -1 if not found
     */
    public int lastIndexOf(final String str) {
        return lastIndexOf(str, size - 1);
    }

    /**
     * Searches the string builder to find the last reference to the specified string starting searching from the given
     * index.
     * <p>
     * Note that a null input string will return -1, whereas the JDK throws an exception.
     * </p>
     *
     * @param str the string to find, null returns -1
     * @param startIndex the index to start at, invalid index rounded to edge
     * @return The last index of the string, or -1 if not found
     */
    public int lastIndexOf(final String str, int startIndex) {
        startIndex = startIndex >= size ? size - 1 : startIndex;
        if (str == null || startIndex < 0) {
            return StringUtils.INDEX_NOT_FOUND;
        }
        final int strLen = str.length();
        if (strLen > 0 && strLen <= size) {
            if (strLen == 1) {
                return lastIndexOf(str.charAt(0), startIndex);
            }

            outer: for (int i = startIndex - strLen + 1; i >= 0; i--) {
                for (int j = 0; j < strLen; j++) {
                    if (str.charAt(j) != buffer[i + j]) {
                        continue outer;
                    }
                }
                return i;
            }

        } else if (strLen == 0) {
            return startIndex;
        }
        return StringUtils.INDEX_NOT_FOUND;
    }

    /**
     * Searches the string builder using the matcher to find the last match.
     * <p>
     * Matchers can be used to perform advanced searching behavior. For example you could write a matcher to find the
     * character 'a' followed by a number.
     * </p>
     *
     * @param matcher the matcher to use, null returns -1
     * @return The last index matched, or -1 if not found
     */
    public int lastIndexOf(final StringMatcher matcher) {
        return lastIndexOf(matcher, size);
    }

    /**
     * Searches the string builder using the matcher to find the last match searching from the given index.
     * <p>
     * Matchers can be used to perform advanced searching behavior. For example you could write a matcher to find the
     * character 'a' followed by a number.
     * </p>
     *
     * @param matcher the matcher to use, null returns -1
     * @param startIndex the index to start at, invalid index rounded to edge
     * @return The last index matched, or -1 if not found
     */
    public int lastIndexOf(final StringMatcher matcher, int startIndex) {
        startIndex = startIndex >= size ? size - 1 : startIndex;
        if (matcher == null || startIndex < 0) {
            return StringUtils.INDEX_NOT_FOUND;
        }
        final char[] buf = buffer;
        final int endIndex = startIndex + 1;
        for (int i = startIndex; i >= 0; i--) {
            if (matcher.isMatch(buf, i, 0, endIndex) > 0) {
                return i;
            }
        }
        return StringUtils.INDEX_NOT_FOUND;
    }

    /**
     * Extracts the leftmost characters from the string builder without throwing an exception.
     * <p>
     * This method extracts the left {@code length} characters from the builder. If this many characters are not
     * available, the whole builder is returned. Thus the returned string may be shorter than the length requested.
     * </p>
     *
     * @param length the number of characters to extract, negative returns empty string
     * @return The new string
     */
    public String leftString(final int length) {
        if (length <= 0) {
            return StringUtils.EMPTY;
        }
        if (length >= size) {
            return new String(buffer, 0, size);
        }
        return new String(buffer, 0, length);
    }

    /**
     * Gets the length of the string builder.
     *
     * @return The length
     */
    @Override
    public int length() {
        return size;
    }

    /**
     * Extracts some characters from the middle of the string builder without throwing an exception.
     * <p>
     * This method extracts {@code length} characters from the builder at the specified index. If the index is negative
     * it is treated as zero. If the index is greater than the builder size, it is treated as the builder size. If the
     * length is negative, the empty string is returned. If insufficient characters are available in the builder, as
     * much as possible is returned. Thus the returned string may be shorter than the length requested.
     * </p>
     *
     * @param index the index to start at, negative means zero
     * @param length the number of characters to extract, negative returns empty string
     * @return The new string
     */
    public String midString(int index, final int length) {
        if (index < 0) {
            index = 0;
        }
        if (length <= 0 || index >= size) {
            return StringUtils.EMPTY;
        }
        if (size <= index + length) {
            return new String(buffer, index, size - index);
        }
        return new String(buffer, index, length);
    }

    /**
     * Minimizes the capacity to the actual length of the string.
     *
     * @return this, to enable chaining
     */
    public TextStringBuilder minimizeCapacity() {
        if (buffer.length > size) {
            reallocate(size);
        }
        return this;
    }

    /**
     * If possible, reads chars from the provided {@link CharBuffer} directly into underlying character buffer without
     * making extra copies.
     *
     * @param charBuffer CharBuffer to read.
     * @return The number of characters read.
     *
     * @see #appendTo(Appendable)
     * @since 1.9
     */
    public int readFrom(final CharBuffer charBuffer) {
        final int oldSize = size;
        final int remaining = charBuffer.remaining();
        ensureCapacity(size + remaining);
        charBuffer.get(buffer, size, remaining);
        size += remaining;
        return size - oldSize;
    }

    /**
     * If possible, reads all chars from the provided {@link Readable} directly into underlying character buffer without
     * making extra copies.
     *
     * @param readable object to read from
     * @return The number of characters read
     * @throws IOException if an I/O error occurs.
     *
     * @see #appendTo(Appendable)
     */
    public int readFrom(final Readable readable) throws IOException {
        if (readable instanceof Reader) {
            return readFrom((Reader) readable);
        }
        if (readable instanceof CharBuffer) {
            return readFrom((CharBuffer) readable);
        }
        final int oldSize = size;
        while (true) {
            ensureCapacity(size + 1);
            final CharBuffer buf = CharBuffer.wrap(buffer, size, buffer.length - size);
            final int read = readable.read(buf);
            if (read == EOS) {
                break;
            }
            size += read;
        }
        return size - oldSize;
    }

    /**
     * If possible, reads all chars from the provided {@link Reader} directly into underlying character buffer without
     * making extra copies.
     *
     * @param reader Reader to read.
     * @return The number of characters read or -1 if we reached the end of stream.
     * @throws IOException if an I/O error occurs.
     *
     * @see #appendTo(Appendable)
     * @since 1.9
     */
    public int readFrom(final Reader reader) throws IOException {
        final int oldSize = size;
        ensureCapacity(size + 1);
        int readCount = reader.read(buffer, size, buffer.length - size);
        if (readCount == EOS) {
            return EOS;
        }
        do {
            size += readCount;
            ensureCapacity(size + 1);
            readCount = reader.read(buffer, size, buffer.length - size);
        } while (readCount != EOS);
        return size - oldSize;
    }

    /**
     * If possible, reads {@code count} chars from the provided {@link Reader} directly into underlying character buffer
     * without making extra copies.
     *
     * @param reader Reader to read.
     * @param count The maximum characters to read, a value &lt;= 0 returns 0.
     * @return The number of characters read. If less than {@code count}, then we've reached the end-of-stream, or -1 if
     *         we reached the end of stream.
     * @throws IOException if an I/O error occurs.
     * @see #appendTo(Appendable)
     * @since 1.9
     */
    public int readFrom(final Reader reader, final int count) throws IOException {
        if (count <= 0) {
            return 0;
        }
        final int oldSize = size;
        ensureCapacity(size + count);
        int target = count;
        int readCount = reader.read(buffer, size, target);
        if (readCount == EOS) {
            return EOS;
        }
        do {
            target -= readCount;
            size += readCount;
            readCount = reader.read(buffer, size, target);
        } while (target > 0 && readCount != EOS);
        return size - oldSize;
    }

    /**
     * Reallocates the buffer to the new length.
     *
     * @param newLength the length of the copy to be returned
     */
    private void reallocate(final int newLength) {
        this.buffer = Arrays.copyOf(buffer, newLength);
        this.reallocations++;
    }

    /**
     * Replaces a portion of the string builder with another string. The length of the inserted string does not have to
     * match the removed length.
     *
     * @param startIndex the start index, inclusive, must be valid
     * @param endIndex the end index, exclusive, must be valid except that if too large it is treated as end of string
     * @param replaceStr the string to replace with, null means delete range
     * @return this, to enable chaining
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public TextStringBuilder replace(final int startIndex, int endIndex, final String replaceStr) {
        endIndex = validateRange(startIndex, endIndex);
        final int insertLen = replaceStr == null ? 0 : replaceStr.length();
        replaceImpl(startIndex, endIndex, endIndex - startIndex, replaceStr, insertLen);
        return this;
    }

    /**
     * Advanced search and replaces within the builder using a matcher.
     * <p>
     * Matchers can be used to perform advanced behavior. For example you could write a matcher to delete all
     * occurrences where the character 'a' is followed by a number.
     * </p>
     *
     * @param matcher the matcher to use to find the deletion, null causes no action
     * @param replaceStr the string to replace the match with, null is a delete
     * @param startIndex the start index, inclusive, must be valid
     * @param endIndex the end index, exclusive, must be valid except that if too large it is treated as end of string
     * @param replaceCount the number of times to replace, -1 for replace all
     * @return this, to enable chaining
     * @throws IndexOutOfBoundsException if start index is invalid
     */
    public TextStringBuilder replace(final StringMatcher matcher, final String replaceStr, final int startIndex,
        int endIndex, final int replaceCount) {
        endIndex = validateRange(startIndex, endIndex);
        return replaceImpl(matcher, replaceStr, startIndex, endIndex, replaceCount);
    }

    /**
     * Replaces the search character with the replace character throughout the builder.
     *
     * @param search the search character
     * @param replace the replace character
     * @return this, to enable chaining
     */
    public TextStringBuilder replaceAll(final char search, final char replace) {
        if (search != replace) {
            for (int i = 0; i < size; i++) {
                if (buffer[i] == search) {
                    buffer[i] = replace;
                }
            }
        }
        return this;
    }

    /**
     * Replaces the search string with the replace string throughout the builder.
     *
     * @param searchStr the search string, null causes no action to occur
     * @param replaceStr the replace string, null is equivalent to an empty string
     * @return this, to enable chaining
     */
    public TextStringBuilder replaceAll(final String searchStr, final String replaceStr) {
        final int searchLen = searchStr == null ? 0 : searchStr.length();
        if (searchLen > 0) {
            final int replaceLen = replaceStr == null ? 0 : replaceStr.length();
            int index = indexOf(searchStr, 0);
            while (index >= 0) {
                replaceImpl(index, index + searchLen, searchLen, replaceStr, replaceLen);
                index = indexOf(searchStr, index + replaceLen);
            }
        }
        return this;
    }

    /**
     * Replaces all matches within the builder with the replace string.
     * <p>
     * Matchers can be used to perform advanced replace behavior. For example you could write a matcher to replace all
     * occurrences where the character 'a' is followed by a number.
     * </p>
     *
     * @param matcher the matcher to use to find the deletion, null causes no action
     * @param replaceStr the replace string, null is equivalent to an empty string
     * @return this, to enable chaining
     */
    public TextStringBuilder replaceAll(final StringMatcher matcher, final String replaceStr) {
        return replace(matcher, replaceStr, 0, size, -1);
    }

    /**
     * Replaces the first instance of the search character with the replace character in the builder.
     *
     * @param search the search character
     * @param replace the replace character
     * @return this, to enable chaining
     */
    public TextStringBuilder replaceFirst(final char search, final char replace) {
        if (search != replace) {
            for (int i = 0; i < size; i++) {
                if (buffer[i] == search) {
                    buffer[i] = replace;
                    break;
                }
            }
        }
        return this;
    }

    /**
     * Replaces the first instance of the search string with the replace string.
     *
     * @param searchStr the search string, null causes no action to occur
     * @param replaceStr the replace string, null is equivalent to an empty string
     * @return this, to enable chaining
     */
    public TextStringBuilder replaceFirst(final String searchStr, final String replaceStr) {
        final int searchLen = searchStr == null ? 0 : searchStr.length();
        if (searchLen > 0) {
            final int index = indexOf(searchStr, 0);
            if (index >= 0) {
                final int replaceLen = replaceStr == null ? 0 : replaceStr.length();
                replaceImpl(index, index + searchLen, searchLen, replaceStr, replaceLen);
            }
        }
        return this;
    }

    /**
     * Replaces the first match within the builder with the replace string.
     * <p>
     * Matchers can be used to perform advanced replace behavior. For example you could write a matcher to replace where
     * the character 'a' is followed by a number.
     * </p>
     *
     * @param matcher the matcher to use to find the deletion, null causes no action
     * @param replaceStr the replace string, null is equivalent to an empty string
     * @return this, to enable chaining
     */
    public TextStringBuilder replaceFirst(final StringMatcher matcher, final String replaceStr) {
        return replace(matcher, replaceStr, 0, size, 1);
    }

    /**
     * Internal method to delete a range without validation.
     *
     * @param startIndex the start index, must be valid
     * @param endIndex the end index (exclusive), must be valid
     * @param removeLen the length to remove (endIndex - startIndex), must be valid
     * @param insertStr the string to replace with, null means delete range
     * @param insertLen the length of the insert string, must be valid
     * @throws IndexOutOfBoundsException if any index is invalid
     */
    private void replaceImpl(final int startIndex, final int endIndex, final int removeLen, final String insertStr,
        final int insertLen) {
        final int newSize = size - removeLen + insertLen;
        if (insertLen != removeLen) {
            ensureCapacity(newSize);
            System.arraycopy(buffer, endIndex, buffer, startIndex + insertLen, size - endIndex);
            size = newSize;
        }
        if (insertLen > 0) {
            insertStr.getChars(0, insertLen, buffer, startIndex);
        }
    }

    /**
     * Replaces within the builder using a matcher.
     * <p>
     * Matchers can be used to perform advanced behavior. For example you could write a matcher to delete all
     * occurrences where the character 'a' is followed by a number.
     * </p>
     *
     * @param matcher the matcher to use to find the deletion, null causes no action
     * @param replaceStr the string to replace the match with, null is a delete
     * @param from the start index, must be valid
     * @param to the end index (exclusive), must be valid
     * @param replaceCount the number of times to replace, -1 for replace all
     * @return this, to enable chaining
     * @throws IndexOutOfBoundsException if any index is invalid
     */
    private TextStringBuilder replaceImpl(final StringMatcher matcher, final String replaceStr, final int from, int to,
        int replaceCount) {
        if (matcher == null || size == 0) {
            return this;
        }
        final int replaceLen = replaceStr == null ? 0 : replaceStr.length();
        for (int i = from; i < to && replaceCount != 0; i++) {
            final char[] buf = buffer;
            final int removeLen = matcher.isMatch(buf, i, from, to);
            if (removeLen > 0) {
                replaceImpl(i, i + removeLen, removeLen, replaceStr, replaceLen);
                to = to - removeLen + replaceLen;
                i = i + replaceLen - 1;
                if (replaceCount > 0) {
                    replaceCount--;
                }
            }
        }
        return this;
    }

    /**
     * Reverses the string builder placing each character in the opposite index.
     *
     * @return this, to enable chaining
     */
    public TextStringBuilder reverse() {
        if (size == 0) {
            return this;
        }

        final int half = size / 2;
        final char[] buf = buffer;
        for (int leftIdx = 0, rightIdx = size - 1; leftIdx < half; leftIdx++, rightIdx--) {
            final char swap = buf[leftIdx];
            buf[leftIdx] = buf[rightIdx];
            buf[rightIdx] = swap;
        }
        return this;
    }

    /**
     * Extracts the rightmost characters from the string builder without throwing an exception.
     * <p>
     * This method extracts the right {@code length} characters from the builder. If this many characters are not
     * available, the whole builder is returned. Thus the returned string may be shorter than the length requested.
     * </p>
     *
     * @param length the number of characters to extract, negative returns empty string
     * @return The new string
     */
    public String rightString(final int length) {
        if (length <= 0) {
            return StringUtils.EMPTY;
        }
        if (length >= size) {
            return new String(buffer, 0, size);
        }
        return new String(buffer, size - length, length);
    }

    /**
     * Clears and sets this builder to the given value.
     *
     * @see #charAt(int)
     * @see #deleteCharAt(int)
     * @param str the new value.
     * @return this, to enable chaining
     * @since 1.9
     */
    public TextStringBuilder set(final CharSequence str) {
        clear();
        append(str);
        return this;
    }

    /**
     * Sets the character at the specified index.
     *
     * @see #charAt(int)
     * @see #deleteCharAt(int)
     * @param index the index to set
     * @param ch the new character
     * @return this, to enable chaining
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public TextStringBuilder setCharAt(final int index, final char ch) {
        validateIndex(index);
        buffer[index] = ch;
        return this;
    }

    /**
     * Updates the length of the builder by either dropping the last characters or adding filler of Unicode zero.
     *
     * @param length the length to set to, must be zero or positive
     * @return this, to enable chaining
     * @throws IndexOutOfBoundsException if the length is negative
     */
    public TextStringBuilder setLength(final int length) {
        if (length < 0) {
            throw new StringIndexOutOfBoundsException(length);
        }
        if (length < size) {
            size = length;
        } else if (length > size) {
            ensureCapacity(length);
            final int oldEnd = size;
            size = length;
            Arrays.fill(buffer, oldEnd, length, '\0');
        }
        return this;
    }

    /**
     * Sets the text to be appended when a new line is added.
     *
     * @param newLine the new line text, null means use system default
     * @return this, to enable chaining
     */
    public TextStringBuilder setNewLineText(final String newLine) {
        this.newLine = newLine;
        return this;
    }

    /**
     * Sets the text to be appended when null is added.
     *
     * @param nullText the null text, null means no append
     * @return this, to enable chaining
     */
    public TextStringBuilder setNullText(String nullText) {
        if (nullText != null && nullText.isEmpty()) {
            nullText = null;
        }
        this.nullText = nullText;
        return this;
    }

    /**
     * Gets the length of the string builder.
     * <p>
     * This method is the same as {@link #length()} and is provided to match the API of Collections.
     * </p>
     *
     * @return The length
     */
    public int size() {
        return size;
    }

    /**
     * Checks whether this builder starts with the specified string.
     * <p>
     * Note that this method handles null input quietly, unlike String.
     * </p>
     *
     * @param str the string to search for, null returns false
     * @return true if the builder starts with the string
     */
    public boolean startsWith(final String str) {
        if (str == null) {
            return false;
        }
        final int len = str.length();
        if (len == 0) {
            return true;
        }
        if (len > size) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            if (buffer[i] != str.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CharSequence subSequence(final int startIndex, final int endIndex) {
        if (startIndex < 0) {
            throw new StringIndexOutOfBoundsException(startIndex);
        }
        if (endIndex > size) {
            throw new StringIndexOutOfBoundsException(endIndex);
        }
        if (startIndex > endIndex) {
            throw new StringIndexOutOfBoundsException(endIndex - startIndex);
        }
        return substring(startIndex, endIndex);
    }

    /**
     * Extracts a portion of this string builder as a string.
     *
     * @param start the start index, inclusive, must be valid
     * @return The new string
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public String substring(final int start) {
        return substring(start, size);
    }

    /**
     * Extracts a portion of this string builder as a string.
     * <p>
     * Note: This method treats an endIndex greater than the length of the builder as equal to the length of the
     * builder, and continues without error, unlike StringBuffer or String.
     * </p>
     *
     * @param startIndex the start index, inclusive, must be valid
     * @param endIndex the end index, exclusive, must be valid except that if too large it is treated as end of string
     * @return The new string
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public String substring(final int startIndex, int endIndex) {
        endIndex = validateRange(startIndex, endIndex);
        return new String(buffer, startIndex, endIndex - startIndex);
    }

    /**
     * Copies the builder's character array into a new character array.
     *
     * @return a new array that represents the contents of the builder
     */
    public char[] toCharArray() {
        return size == 0 ? ArrayUtils.EMPTY_CHAR_ARRAY : Arrays.copyOf(buffer, size);
    }

    /**
     * Copies part of the builder's character array into a new character array.
     *
     * @param startIndex the start index, inclusive, must be valid
     * @param endIndex the end index, exclusive, must be valid except that if too large it is treated as end of string
     * @return a new array that holds part of the contents of the builder
     * @throws IndexOutOfBoundsException if startIndex is invalid, or if endIndex is invalid (but endIndex greater than
     *         size is valid)
     */
    public char[] toCharArray(final int startIndex, int endIndex) {
        endIndex = validateRange(startIndex, endIndex);
        final int len = endIndex - startIndex;
        return len == 0 ? ArrayUtils.EMPTY_CHAR_ARRAY : Arrays.copyOfRange(buffer, startIndex, endIndex);
    }

    /**
     * Gets a String version of the string builder, creating a new instance each time the method is called.
     * <p>
     * Note that unlike StringBuffer, the string version returned is independent of the string builder.
     * </p>
     *
     * @return The builder as a String
     */
    @Override
    public String toString() {
        return new String(buffer, 0, size);
    }

    /**
     * Gets a StringBuffer version of the string builder, creating a new instance each time the method is called.
     *
     * @return The builder as a StringBuffer
     */
    public StringBuffer toStringBuffer() {
        return new StringBuffer(size).append(buffer, 0, size);
    }

    /**
     * Gets a StringBuilder version of the string builder, creating a new instance each time the method is called.
     *
     * @return The builder as a StringBuilder
     */
    public StringBuilder toStringBuilder() {
        return new StringBuilder(size).append(buffer, 0, size);
    }

    /**
     * Trims the builder by removing characters less than or equal to a space from the beginning and end.
     *
     * @return this, to enable chaining
     */
    public TextStringBuilder trim() {
        if (size == 0) {
            return this;
        }
        int len = size;
        final char[] buf = buffer;
        int pos = 0;
        while (pos < len && buf[pos] <= SPACE) {
            pos++;
        }
        while (pos < len && buf[len - 1] <= SPACE) {
            len--;
        }
        if (len < size) {
            delete(len, size);
        }
        if (pos > 0) {
            delete(0, pos);
        }
        return this;
    }

    /**
     * Validates that an index is in the range {@code 0 <= index <= size}.
     *
     * @param index the index to test.
     * @throws IndexOutOfBoundsException Thrown when the index is not the range {@code 0 <= index <= size}.
     */
    protected void validateIndex(final int index) {
        if (index < 0 || index >= size) {
            throw new StringIndexOutOfBoundsException(index);
        }
    }

    /**
     * Validates parameters defining a range of the builder.
     *
     * @param startIndex the start index, inclusive, must be valid
     * @param endIndex the end index, exclusive, must be valid except that if too large it is treated as end of string
     * @return A valid end index.
     * @throws StringIndexOutOfBoundsException if the index is invalid
     */
    protected int validateRange(final int startIndex, int endIndex) {
        if (startIndex < 0) {
            throw new StringIndexOutOfBoundsException(startIndex);
        }
        if (endIndex > size) {
            endIndex = size;
        }
        if (startIndex > endIndex) {
            throw new StringIndexOutOfBoundsException("end < start");
        }
        return endIndex;
    }

}
