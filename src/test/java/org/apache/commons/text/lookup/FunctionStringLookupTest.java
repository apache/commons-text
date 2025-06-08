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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link FunctionStringLookup}.
 */
class FunctionStringLookupTest {

    @Test
    void testConcurrentHashMapNull() {
        Assertions.assertNull(FunctionStringLookup.on(new ConcurrentHashMap<>()).apply(null));
    }

    @Test
    void testHashMapNull() {
        Assertions.assertNull(FunctionStringLookup.on(new HashMap<>()).apply(null));
    }

    @Test
    void testNullFunction() {
        Assertions.assertNull(FunctionStringLookup.on((Function<String, Object>) null).apply(null));
    }

    @Test
    void testOne() {
        final String key = "key";
        final String value = "value";
        final Map<String, String> map = new HashMap<>();
        map.put(key, value);
        Assertions.assertEquals(value, FunctionStringLookup.on(map).apply(key));
    }

    @Test
    void testToString() {
        // does not blow up and gives some kind of string.
        Assertions.assertFalse(FunctionStringLookup.on(new HashMap<>()).toString().isEmpty());
    }

}
