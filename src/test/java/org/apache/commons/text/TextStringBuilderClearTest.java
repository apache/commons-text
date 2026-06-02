/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Test;

/**
 * Tests that {@link TextStringBuilder} shrink and clear paths do not leave stale chars in the backing buffer.
 * <p>
 * readFrom(Reader) reads directly into the internal char[] buffer, so a Reader that is also an attacker can observe stale chars in that buffer beyond the
 * logical content. The buffer is non-transient, so stale chars also survive serialization.
 * </p>
 * <p>
 * Post-patch: clear(), delete(), replace() and setLength() NUL out the freed region so stale chars are not exposed.
 * </p>
 */
public class TextStringBuilderClearTest {

    /**
     * A Reader that, upon reading, inspects the char array it has been given access to (positions beyond offset+len that may contain stale data), records them,
     * then supplies its normal data.
     */
    static class SpyReader extends Reader {

        private boolean done;
        private char[] observedExtra;
        private final char[] supply;

        SpyReader(final String supply) {
            this.supply = supply.toCharArray();
        }

        @Override
        public void close() {
            // empty
        }

        boolean observedStaleChars(final String marker) {
            if (observedExtra == null) {
                return false;
            }
            return new String(observedExtra).contains(marker);
        }

        @Override
        public int read(final char[] cbuf, final int off, final int len) {
            if (done) {
                return -1;
            }
            done = true;
            // Record chars in the buffer beyond where we will write
            final int toWrite = Math.min(supply.length, len);
            final int staleStart = off + toWrite;
            final int staleLen = cbuf.length - staleStart;
            if (staleLen > 0) {
                observedExtra = new char[staleLen];
                System.arraycopy(cbuf, staleStart, observedExtra, 0, staleLen);
            }
            System.arraycopy(supply, 0, cbuf, off, toWrite);
            return toWrite;
        }
    }

    /** Search for a string encoded as UTF-16BE (2 bytes per char) in a byte array. */
    private static boolean containsUtf16Be(final byte[] haystack, final String needle) {
        final byte[] needleBytes = needle.getBytes(StandardCharsets.UTF_16BE);
        outer: for (int i = 0; i <= haystack.length - needleBytes.length; i++) {
            for (int j = 0; j < needleBytes.length; j++) {
                if (haystack[i + j] != needleBytes[j]) {
                    continue outer;
                }
            }
            return true;
        }
        return false;
    }

    @Test
    public void testDeserializedBuilderHasNoStaleBufferContent() throws Exception {
        final TextStringBuilder sb = new TextStringBuilder("secret_password_xyzzy");
        sb.clear();
        sb.append("safe");
        final byte[] serialized = SerializationUtils.serialize(sb);
        final TextStringBuilder sb2;
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(serialized))) {
            sb2 = (TextStringBuilder) ois.readObject();
        }
        final String bufContent = new String(sb2.getBuffer());
        assertFalse(bufContent.contains("secret_password"), "Deserialized buffer must not contain stale chars: " + bufContent);
    }

    @Test
    void testDeleteShrinkLeavesNoResidue() {
        final String string = "SECRET_PASSWORD_DATA";
        final int len = string.length();
        final TextStringBuilder sb = new TextStringBuilder(string);
        assertEquals(len, sb.length());
        sb.delete(6, len);
        assertEquals(6, sb.length());
        assertEquals("SECRET", sb.toString());
        final char[] buf = sb.getBuffer();
        for (int i = 6; i < len; i++) {
            assertEquals(CharUtils.NUL, buf[i]);
        }
    }

    @Test
    public void testReadFromReaderDoesNotExposeStaleInternalBuffer() throws IOException {
        final TextStringBuilder sb = new TextStringBuilder();
        sb.append("SECRET_DATA_SHOULD_NOT_LEAK_ABCDEFGHIJ");
        sb.clear();
        try (SpyReader spy = new SpyReader("hi")) {
            sb.readFrom(spy);
            assertFalse(spy.observedStaleChars("_DATA_SHOULD_NOT_LEAK"));
        }
    }

    @Test
    void testReplaceShrinkLeavesNoResidue() {
        final String string = "SECRET_PASSWORD_DATA";
        final TextStringBuilder sb = new TextStringBuilder(string);
        assertEquals(20, sb.length());
        // Shrink: replace [0,20) with "X" => removeLen=20, insertLen=1, newSize=1.
        sb.replace(0, 20, "X");
        assertEquals(1, sb.length());
        assertEquals("X", sb.toString());
        final char[] buf = sb.getBuffer();
        assertTrue(buf.length >= 20);
        for (int i = 1; i < 20; i++) {
            assertEquals(CharUtils.NUL, buf[i]);
        }
    }

    @Test
    void testSetLengthShrinkLeavesNoResidue() {
        final String string = "CONFIDENTIAL_TOKEN_VALUE";
        final int len = string.length();
        final TextStringBuilder sb = new TextStringBuilder(string);
        assertEquals(len, sb.length());
        sb.setLength(5);
        assertEquals(5, sb.length());
        assertEquals("CONFI", sb.toString());
        final char[] buf = sb.getBuffer();
        assertTrue(buf.length >= len);
        for (int i = 5; i < len; i++) {
            assertEquals(CharUtils.NUL, buf[i]);
        }
    }

    @Test
    public void testStaleCharsNotLeakedAfterClear() {
        final TextStringBuilder sb = new TextStringBuilder("secret_password_xyzzy_leak");
        sb.clear();
        sb.append("ok");
        assertFalse(containsUtf16Be(SerializationUtils.serialize(sb), "xyzzy_leak"));
    }

    @Test
    public void testStaleCharsNotLeakedAfterTruncate() {
        final TextStringBuilder sb = new TextStringBuilder("top_secret_key_material");
        sb.delete(6, sb.length());
        assertFalse(containsUtf16Be(SerializationUtils.serialize(sb), "secret_key_material"));
    }
}
