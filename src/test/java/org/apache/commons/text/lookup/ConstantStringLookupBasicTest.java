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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link ConstantStringLookup}.
 */
public class ConstantStringLookupBasicTest {

    /**
     * Test fixture.
     */
    public static final String NULL_STRING_FIXTURE = null;

    /**
     * Test fixture.
     */
    public static final String STRING_FIXTURE = "Hello World!";

    /**
     * Clears the test environment. Here the static cache of the constant lookup class is wiped out.
     */
    @AfterEach
    public void afterEach() {
        ConstantStringLookup.clear();
    }

    /**
     * Clears the test environment. Here the static cache of the constant lookup class is wiped out.
     */
    @BeforeEach
    public void beforeEach() {
        ConstantStringLookup.clear();
    }

    @Test
    public void testNull() {
        Assertions.assertNull(ConstantStringLookup.INSTANCE.lookup(null));
    }

    @Test
    public void testNullClassFetch() {
        Assertions.assertNull(new ConstantStringLookup() {
            @Override
            protected Class<?> fetchClass(final String className) throws ClassNotFoundException {
                return null;
            }
        }.lookup(ConstantStringLookupBasicTest.class.getName() + ".STRING_FIXTURE"));
    }

    @Test
    public void testNullValue() {
        Assertions.assertEquals(NULL_STRING_FIXTURE, ConstantStringLookup.INSTANCE
            .lookup(ConstantStringLookupBasicTest.class.getName() + ".NULL_STRING_FIXTURE"));
    }

    @Test
    public void testOne() {
        Assertions.assertEquals(STRING_FIXTURE,
            ConstantStringLookup.INSTANCE.lookup(ConstantStringLookupBasicTest.class.getName() + ".STRING_FIXTURE"));
    }

    @Test
    public void testToString() {
        // does not blow up and gives some kind of string.
        Assertions.assertFalse(ConstantStringLookup.INSTANCE.toString().isEmpty());
    }

}
