/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class InterpolatorStringLookupTest {

    @BeforeAll
    public static void beforeAll() throws Throwable {
        System.setProperty(TESTKEY, TESTVAL);
        System.setProperty(TESTKEY2, TESTVAL);
    }

    @AfterAll
    public static void afterAll() throws Throwable {
        System.clearProperty(TESTKEY);
        System.clearProperty(TESTKEY2);
    }

    private static final String TESTKEY = "TestKey";
    private static final String TESTKEY2 = "TestKey2";

    private static final String TESTVAL = "TestValue";

    private void assertLookupNotEmpty(final StringLookup lookup, final String key) {
        final String value = lookup.lookup(key);
        assertNotNull(value);
        assertFalse(value.isEmpty());
        System.out.println(key + " = " + value);
    }

    @Test
    public void testLookup() {
        final Map<String, String> map = new HashMap<>();
        map.put(TESTKEY, TESTVAL);
        final StringLookup lookup = new InterpolatorStringLookup(MapStringLookup.on(map));
        String value = lookup.lookup(TESTKEY);
        assertEquals(TESTVAL, value);
        value = lookup.lookup("ctx:" + TESTKEY);
        assertEquals(TESTVAL, value);
        value = lookup.lookup("sys:" + TESTKEY);
        assertEquals(TESTVAL, value);
        value = lookup.lookup("BadKey");
        assertNull(value);
        value = lookup.lookup("ctx:" + TESTKEY);
        assertEquals(TESTVAL, value);
    }

    @Test
    public void testLookupWithDefaultInterpolator() {
        final StringLookup lookup = new InterpolatorStringLookup();
        String value = lookup.lookup("sys:" + TESTKEY);
        assertEquals(TESTVAL, value);
        value = lookup.lookup("env:PATH");
        assertNotNull(value);
        value = lookup.lookup("date:yyyy-MM-dd");
        assertNotNull("No Date", value);
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final String today = format.format(new Date());
        assertEquals(value, today);
        assertLookupNotEmpty(lookup, "java:version");
        assertLookupNotEmpty(lookup, "java:runtime");
        assertLookupNotEmpty(lookup, "java:vm");
        assertLookupNotEmpty(lookup, "java:os");
        assertLookupNotEmpty(lookup, "java:locale");
        assertLookupNotEmpty(lookup, "java:hardware");
    }
}
