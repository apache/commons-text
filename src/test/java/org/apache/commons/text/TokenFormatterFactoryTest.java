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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.text.TokenFormatterFactory.ConstantTokenFormatter;
import org.apache.commons.text.TokenFormatterFactory.NoOpFormatter;
import org.junit.jupiter.api.Test;

public class TokenFormatterFactoryTest {

    @Test
    public void testConstantTokenFormatterFailOnConstant() {
        ConstantTokenFormatter formatter = TokenFormatterFactory.constantFormatter("abc".toCharArray(), true);
        assertThrows(IllegalArgumentException.class, () -> formatter.format(null, 0, "dabc".toCharArray()));
        assertThrows(IllegalArgumentException.class, () -> formatter.format(null, 0, "abc".toCharArray()));
        assertThrows(IllegalArgumentException.class, () -> formatter.format(null, 0, "abcd".toCharArray()));

        ConstantTokenFormatter unicode = TokenFormatterFactory.constantFormatter("\uD801\uDC00".toCharArray(), true);
        assertThrows(IllegalArgumentException.class, () -> unicode.format(null, 0, "\uD801\uDC00".toCharArray()));
        assertThrows(IllegalArgumentException.class, () -> unicode.format(null, 0, "a\uD801\uDC00".toCharArray()));
        assertThrows(IllegalArgumentException.class, () -> unicode.format(null, 0, "\uD801\uDC00b".toCharArray()));
    }

    @Test
    public void testConstantTokenFormatter() {
        ConstantTokenFormatter formatter = TokenFormatterFactory.constantFormatter("abc".toCharArray(), false);
        assertEquals("abc", formatter.format(null, 0, new char[0]));
        assertEquals("abc", formatter.format(null, 0, "abc".toCharArray()));
        assertEquals("abc", formatter.format(null, 0, "abdc".toCharArray()));
        assertEquals("abc", formatter.format(null, 0, "".toCharArray()));

        formatter = TokenFormatterFactory.constantFormatter("\uD801\uDC00".toCharArray(), true);
        assertEquals("\uD801\uDC00", formatter.format(null, 0, new char[0]));
        assertEquals("\uD801\uDC00", formatter.format(null, 0, "abc".toCharArray()));
    }

    @Test
    public void testNoOpFormatter() {
        NoOpFormatter formatter = TokenFormatterFactory.noOpFormatter();
        assertEquals("\uD801\uDC00", formatter.format(null, 0, "\uD801\uDC00".toCharArray()));
        assertEquals("\uD801\uDC00a", formatter.format(null, 0, "\uD801\uDC00a".toCharArray()));
        assertEquals("abc", formatter.format(null, 0, "abc".toCharArray()));
        assertEquals("", formatter.format(null, 0, "".toCharArray()));
    }

    @Test
    public void testEmptyFormatter() {
        ConstantTokenFormatter formatter = TokenFormatterFactory.emptyFormatter();
        assertEquals("", formatter.format(null, 0, "\uD801\uDC00".toCharArray()));
        assertEquals("", formatter.format(null, 0, "\uD801\uDC00a".toCharArray()));
        assertEquals("", formatter.format(null, 0, "abc".toCharArray()));
        assertEquals("", formatter.format(null, 0, "".toCharArray()));
    }
}
