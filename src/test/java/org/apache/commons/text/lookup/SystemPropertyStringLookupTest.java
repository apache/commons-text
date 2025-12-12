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

import org.junit.jupiter.api.Test;

/**
 * Tests {@link StringLookupFactory#INSTANCE_SYSTEM_PROPERTIES}.
 */
class SystemPropertyStringLookupTest {

    @Test
    void testNull() {
        assertNull(StringLookupFactory.INSTANCE_SYSTEM_PROPERTIES.apply(null));
    }

    @Test
    void testToString() {
        // does not blow up and gives some kind of string.
        assertFalse(StringLookupFactory.INSTANCE_SYSTEM_PROPERTIES.toString().isEmpty());
    }

    @Test
    void testUserName() {
        final String key = "user.name";
        assertEquals(System.getProperty(key), StringLookupFactory.INSTANCE_SYSTEM_PROPERTIES.apply(key));
    }

}
