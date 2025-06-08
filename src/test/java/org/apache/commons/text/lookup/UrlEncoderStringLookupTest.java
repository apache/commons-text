/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */

package org.apache.commons.text.lookup;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link UrlEncoderStringLookup}.
 */
class UrlEncoderStringLookupTest {

    private static final String DATA = "Hello+World%21";

    @Test
    void test() {
        Assertions.assertEquals(DATA, UrlEncoderStringLookup.INSTANCE.apply("Hello World!"));
    }

    @Test
    void testExceptionGettingString() throws UnsupportedEncodingException {
        final UrlEncoderStringLookup mockLookup = spy(UrlEncoderStringLookup.class);
        when(mockLookup.encode(DATA, StandardCharsets.UTF_8.displayName()))
            .thenThrow(UnsupportedEncodingException.class);
        assertThrows(IllegalArgumentException.class, () -> mockLookup.apply(DATA));
    }

    @Test
    void testNull() {
        Assertions.assertNull(UrlEncoderStringLookup.INSTANCE.apply(null));
    }

    @Test
    void testToString() {
        // does not blow up and gives some kind of string.
        Assertions.assertFalse(UrlEncoderStringLookup.INSTANCE.toString().isEmpty());
    }

}
