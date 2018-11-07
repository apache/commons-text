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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link UnicodeEscaper}.
 */
public class UnicodeUnescaperTest {

    // Requested in LANG-507
    @Test
    public void testUPlus() {
        final UnicodeUnescaper uu = new UnicodeUnescaper();

        final String input = "\\u+0047";
        assertThat(uu.translate(input)).as("Failed to unescape Unicode characters with 'u+' notation").isEqualTo("G");
    }

    @Test
    public void testUuuuu() {
        final UnicodeUnescaper uu = new UnicodeUnescaper();

        final String input = "\\uuuuuuuu0047";
        final String result = uu.translate(input);
        assertThat(result).as("Failed to unescape Unicode characters with many 'u' characters").isEqualTo("G");
    }

    @Test
    public void testLessThanFour() {
        final UnicodeUnescaper uu = new UnicodeUnescaper();

        final String input = "\\0047\\u006";
        assertThrows(IllegalArgumentException.class, () -> uu.translate(input));
    }
}
