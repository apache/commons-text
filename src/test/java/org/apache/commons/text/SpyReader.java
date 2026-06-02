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

import java.io.Reader;

/**
 * A Reader that, upon reading, inspects the char array it has been given access to (positions beyond offset+len that may contain stale data), records them,
 * then supplies its normal data.
 */
class SpyReader extends Reader {

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
