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
package org.apache.commons.text.translate;

import org.junit.jupiter.api.Test;

import java.io.CharArrayWriter;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link UnicodeUnpairedSurrogateRemover}.
 */
public class UnicodeUnpairedSurrogateRemoverTest {
    final UnicodeUnpairedSurrogateRemover subject = new UnicodeUnpairedSurrogateRemover();
    final CharArrayWriter writer = new CharArrayWriter(); // nothing is ever written to it

    @Test
    public void testValidCharacters() throws IOException {
        assertThat(subject.translate(0xd7ff, writer)).isFalse();
        assertThat(subject.translate(0xe000, writer)).isFalse();
        assertThat(writer.size()).isZero();
    }

    @Test
    public void testInvalidCharacters() throws IOException {
        assertThat(subject.translate(0xd800, writer)).isTrue();
        assertThat(subject.translate(0xdfff, writer)).isTrue();
        assertThat(writer.size()).isZero();
    }
}

