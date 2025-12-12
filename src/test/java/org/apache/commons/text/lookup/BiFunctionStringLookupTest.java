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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link FunctionStringLookup}.
 */
class BiFunctionStringLookupTest {

    private static final String SEPARATOR = ".";

    @SuppressWarnings("unchecked")
    private final BiFunction<String, Map<String, Object>, Object> nestedMapBiFunction = (k, m) -> {
        final String keyCandidate = StringUtils.substringBefore(k, SEPARATOR);
        if (keyCandidate.isEmpty()) {
            return m.get(k);
        }
        final Object value = m.get(keyCandidate);
        if (value instanceof Map) {
            return this.nestedMapBiFunction.apply(StringUtils.substringAfter(k, SEPARATOR),
                (Map<String, Object>) value);
        }
        return value;
    };

    @Test
    void testBiFunctionForNestedMap() {
        // Build map
        final String subSubKey = "subsubkeyMap";
        final String subSubValue = "subsubvalue";
        final Map<String, String> subSubMap = new HashMap<>();
        subSubMap.put(subSubKey, subSubValue);
        //
        final String subKey = "subkey";
        final String subKeyMap = "subkeyMap";
        final String subValue = "subvalue";
        final Map<String, Object> rootSubMap = new HashMap<>();
        rootSubMap.put(subKey, subValue);
        rootSubMap.put(subKeyMap, subSubMap);
        //
        final String rootKey = "keyMap";
        final String rootKey2 = "key2";
        final String rootValue2 = "value2";
        final Map<String, Object> rootMap = new HashMap<>();
        rootMap.put(rootKey, rootSubMap);
        rootMap.put(rootKey2, rootValue2);
        // Use map
        final BiStringLookup<Map<String, Object>> stringLookup = StringLookupFactory.INSTANCE
            .biFunctionStringLookup(nestedMapBiFunction);
        assertEquals(rootValue2, stringLookup.lookup(rootKey2, rootMap));
        assertEquals(subValue, stringLookup.lookup(rootKey + SEPARATOR + subKey, rootMap));
        assertEquals(subSubValue,
            stringLookup.lookup(rootKey + SEPARATOR + subKeyMap + SEPARATOR + subSubKey, rootMap));
    }

    @Test
    void testConcurrentHashMapNull() {
        assertNull(BiFunctionStringLookup.on(new ConcurrentHashMap<>()).apply(null));
    }

    @Test
    void testHashMapNull() {
        assertNull(BiFunctionStringLookup.on(new HashMap<>()).apply(null));
    }

    @Test
    void testNullBiFunction() {
        assertNull(BiFunctionStringLookup.on((BiFunction<String, Object, Object>) null).apply(null));
    }

    @Test
    void testOne() {
        final String key = "key";
        final String value = "value";
        final Map<String, String> map = new HashMap<>();
        map.put(key, value);
        assertEquals(value, FunctionStringLookup.on(map).apply(key));
    }

    @Test
    void testToString() {
        // does not blow up and gives some kind of string.
        assertFalse(BiFunctionStringLookup.on(new HashMap<>()).toString().isEmpty());
    }

}
