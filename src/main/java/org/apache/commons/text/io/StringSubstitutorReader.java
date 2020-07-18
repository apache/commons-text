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

package org.apache.commons.text.io;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Objects;

import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.TextStringBuilder;
import org.apache.commons.text.matcher.StringMatcher;
import org.apache.commons.text.matcher.StringMatcherFactory;

/**
 * A {@link Reader} that performs string substitution on a source {@code Reader} using a {@link StringSubstitutor}.
 *
 * <p>
 * Using this Reader avoids reading a whole file into memory as a {@code String} to perform string substitution, for
 * example, when a Servlet filters a file to a client.
 * </p>
 * <p>
 * This class is not thread-safe.
 * </p>
 *
 * @since 1.9
 */
public class StringSubstitutorReader extends FilterReader {

    /** The end-of-stream character marker. */
    private static final int EOS = -1;

    /** Our internal buffer. */
    private final TextStringBuilder buffer = new TextStringBuilder();

    /** End-of-Stream flag. */
    private boolean eos;

    /** Matches escaped variable starts. */
    private final StringMatcher prefixEscapeMatcher;

    /** Internal buffer for {@link #read()} method. */
    private final char[] read1CharBuffer = {0};

    /** We don't always want to drain the whole buffer. */
    private int toDrain;

    /** The underlying StringSubstitutor. */
    private final StringSubstitutor stringSubstitutor;

    /**
     * Constructs a new instance.
     *
     * @param reader the underlying reader containing the template text known to the given {@code StringSubstitutor}.
     * @param stringSubstitutor How to replace as we read.
     * @throws NullPointerException if {@code reader} is {@code null}.
     * @throws NullPointerException if {@code stringSubstitutor} is {@code null}.
     */
    public StringSubstitutorReader(final Reader reader, final StringSubstitutor stringSubstitutor) {
        super(reader);
        this.stringSubstitutor = Objects.requireNonNull(stringSubstitutor);
        this.prefixEscapeMatcher = StringMatcherFactory.INSTANCE.charMatcher(stringSubstitutor.getEscapeChar())
            .andThen(stringSubstitutor.getVariablePrefixMatcher());
    }

    /**
     * Buffers the requested number of characters if available.
     */
    private int buffer(final int requestReadCount) throws IOException {
        final int actualReadCount = buffer.readFrom(super.in, requestReadCount);
        eos = actualReadCount == EOS;
        return actualReadCount;
    }

    /**
     * Reads a requested number of chars from the underlying reader into the buffer. On EOS, set the state is DRANING,
     * drain, and return a drain count, otherwise, returns the actual read count.
     */
    private int bufferOrDrainOnEos(final int requestReadCount, final char[] target, final int targetIndex,
        final int targetLength) throws IOException {
        final int actualReadCount = buffer(requestReadCount);
        return drainOnEos(actualReadCount, target, targetIndex, targetLength);
    }

    /**
     * Drains characters from our buffer to the given {@code target}.
     */
    private int drain(final char[] target, final int targetIndex, final int targetLength) {
        final int actualLen = Math.min(buffer.length(), targetLength);
        final int drainCount = buffer.drainChars(0, actualLen, target, targetIndex);
        toDrain -= drainCount;
        if (buffer.isEmpty() || toDrain == 0) {
            // nothing or everything drained.
            toDrain = 0;
        }
        return drainCount;
    }

    /**
     * Drains from the buffer to the target only if we are at EOS per the input count. If input count is EOS, drain and
     * returns the drain count, otherwise return the input count. If draining, the state is set to DRAINING.
     */
    private int drainOnEos(final int readCountOrEos, final char[] target, final int targetIndex,
        final int targetLength) {
        if (readCountOrEos == EOS) {
            // At EOS, drain.
            if (buffer.isNotEmpty()) {
                toDrain = buffer.size();
                return drain(target, targetIndex, targetLength);
            }
            return EOS;
        }
        return readCountOrEos;
    }

    /**
     * Tests if our buffer matches the given string matcher at the given position in the buffer.
     */
    private boolean isBufferMatchAt(final StringMatcher stringMatcher, final int pos) {
        return stringMatcher.isMatch(buffer, pos) == stringMatcher.size();
    }

    /**
     * Tests if we are draining.
     */
    private boolean isDraining() {
        return toDrain > 0;
    }

    /**
     * Reads a single character.
     *
     * @return a character as an {@code int} or {@code -1} for end-of-stream.
     * @throws IOException If an I/O error occurs
     */
    @Override
    public int read() throws IOException {
        int count = 0;
        // ask until we get a char or EOS
        do {
            count = read(read1CharBuffer, 0, 1);
            if (count == EOS) {
                return EOS;
            }
            // keep on buffering
        } while (count < 1);
        return read1CharBuffer[0];
    }

    /**
     * Reads characters into a portion of an array.
     *
     * @param target Target buffer.
     * @param targetIndexIn Index in the target at which to start storing characters.
     * @param targetLengthIn Maximum number of characters to read.
     *
     * @return The number of characters read, or -1 on end of stream.
     * @throws IOException If an I/O error occurs
     */
    @Override
    public int read(final char[] target, final int targetIndexIn, final int targetLengthIn) throws IOException {
        // The whole thing is inefficient because we must look for a balanced suffix to match the starting prefix
        // Trying to substitute an incomplete expression can perform replacements when it should not.
        // At a high level:
        // - if draining, drain until empty or target length hit
        // - copy to target until we find a variable start
        // - buffer until a balanced suffix is read, then substitute.
        if (eos && buffer.isEmpty()) {
            return EOS;
        }
        if (targetLengthIn <= 0) {
            // short-circuit: ask nothing, give nothing
            return 0;
        }
        // drain check
        int targetIndex = targetIndexIn;
        int targetLength = targetLengthIn;
        if (isDraining()) {
            // drain as much as possible
            final int drainCount = drain(target, targetIndex, Math.min(toDrain, targetLength));
            if (drainCount == targetLength) {
                // drained length requested, target is full, can only do more in the next invocation
                return targetLength;
            }
            // drained less than requested, target not full.
            targetIndex += drainCount;
            targetLength -= drainCount;
        }
        // BUFFER from the underlying reader
        final int minReadLenPrefix = prefixEscapeMatcher.size();
        // READ enough to test for an [optionally escaped] variable start
        int readCount = buffer(readCount(minReadLenPrefix, 0));
        if (buffer.length() < minReadLenPrefix && targetLength < minReadLenPrefix) {
            // read less than minReadLenPrefix, no variable possible
            return drain(target, targetIndex, targetLength);
        }
        if (eos) {
            // EOS
            stringSubstitutor.replaceIn(buffer);
            toDrain = buffer.size();
            return drain(target, targetIndex, targetLength);
        }
        // PREFIX
        // buffer and drain until we find a variable start, escaped or plain.
        int balance = 0;
        final StringMatcher prefixMatcher = stringSubstitutor.getVariablePrefixMatcher();
        int pos = 0;
        while (targetLength > 0) {
            if (isBufferMatchAt(prefixMatcher, 0)) {
                balance = 1;
                pos = prefixMatcher.size();
                break;
            } else if (isBufferMatchAt(prefixEscapeMatcher, 0)) {
                balance = 1;
                pos = prefixEscapeMatcher.size();
                break;
            }
            // drain first char
            final int drainCount = drain(target, targetIndex, 1);
            targetIndex += drainCount;
            targetLength -= drainCount;
            if (buffer.size() < minReadLenPrefix) {
                readCount = bufferOrDrainOnEos(minReadLenPrefix, target, targetIndex, targetLength);
                if (eos || isDraining()) {
                    // if draining, readCount is a drain count
                    if (readCount != EOS) {
                        targetIndex += readCount;
                        targetLength -= readCount;
                    }
                    final int actual = targetIndex - targetIndexIn;
                    return actual > 0 ? actual : EOS;
                }
            }
        }
        // we found a variable start
        if (targetLength <= 0) {
            // no more room in target
            return targetLengthIn;
        }
        // SUFFIX
        // buffer more to find a balanced suffix
        final StringMatcher suffixMatcher = stringSubstitutor.getVariableSuffixMatcher();
        final int minReadLenSuffix = Math.max(minReadLenPrefix, suffixMatcher.size());
        readCount = buffer(readCount(minReadLenSuffix, pos));
        if (eos) {
            // EOS
            stringSubstitutor.replaceIn(buffer);
            toDrain = buffer.size();
            final int drainCount = drain(target, targetIndex, targetLength);
            return targetIndex + drainCount - targetIndexIn;
        }
        // buffer and break out when we find the end or a balanced suffix
        while (true) {
            if (isBufferMatchAt(suffixMatcher, pos)) {
                balance--;
                pos++;
                if (balance == 0) {
                    break;
                }
            } else if (isBufferMatchAt(prefixMatcher, pos)) {
                balance++;
                pos += prefixMatcher.size();
            } else if (isBufferMatchAt(prefixEscapeMatcher, pos)) {
                balance++;
                pos += prefixEscapeMatcher.size();
            } else {
                pos++;
            }
            readCount = buffer(readCount(minReadLenSuffix, pos));
            if (readCount == EOS && pos >= buffer.size()) {
                break;
            }
        }
        // substitute
        final int endPos = pos + 1;
        final int leftover = Math.max(0, buffer.size() - pos);
        stringSubstitutor.replaceIn(buffer, 0, Math.min(buffer.size(), endPos));
        pos = buffer.size() - leftover;
        final int drainLen = Math.min(targetLength, pos);
        // only drain up to what we've substituted
        toDrain = pos;
        drain(target, targetIndex, drainLen);
        return targetIndex + drainLen;
    }

    /**
     * Returns how many chars to attempt reading to have room in the buffer for {@code count} chars starting at position
     * {@code pos}.
     */
    private int readCount(final int count, final int pos) {
        final int avail = buffer.size() - pos;
        return avail >= count ? 0 : count - avail;
    }

}
