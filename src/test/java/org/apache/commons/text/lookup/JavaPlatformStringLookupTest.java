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
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link JavaPlatformStringLookup}.
 */
class JavaPlatformStringLookupTest {

    @Test
    void testBadKey() {
        assertThrows(IllegalArgumentException.class, () -> JavaPlatformStringLookup.INSTANCE.apply("BADKEY"));
    }

    @Test
    void testMain() {
        JavaPlatformStringLookup.main(ArrayUtils.EMPTY_STRING_ARRAY);
    }

    @Test
    void testNull() {
        Assertions.assertNull(JavaPlatformStringLookup.INSTANCE.apply(null));
    }

    @Test
    void testToString() {
        // does not blow up and gives some kind of string.
        Assertions.assertFalse(JavaPlatformStringLookup.INSTANCE.toString().isEmpty());
    }

    @Test
    void testVm() {
        final String key = "vm";
        assertTrue(JavaPlatformStringLookup.INSTANCE.apply(key).contains(System.getProperty("java.vm.name")));
    }

}
