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
package org.apache.commons.text.translate;

import java.io.IOException;
import java.io.Writer;

/**
 * Helper subclass to CharSequenceTranslator to allow for translations that
 * will replace up to one character at a time.
 *
 * @since 1.0
 */
public abstract class CodePointTranslator extends CharSequenceTranslator {

    /**
     * Construct a new instance.
     */
    public CodePointTranslator() {
        // empty
    }

    @Override
    public final int translate(final CharSequence input, final int index, final Writer writer) throws IOException {
        final int codePoint = Character.codePointAt(input, index);
        final boolean consumed = translate(codePoint, writer);
        return consumed ? 1 : 0;
    }

    /**
     * Translates the specified code point into another.
     *
     * @param codePoint int character input to translate
     * @param writer Writer to optionally push the translated output to
     * @return boolean as to whether translation occurred or not
     * @throws IOException if and only if the Writer produces an IOException
     */
    public abstract boolean translate(int codePoint, Writer writer) throws IOException;

}
