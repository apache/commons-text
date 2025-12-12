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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link InetAddressStringLookup#LOCAL_HOST}.
 */
class InetAddressStringLookupLocalHostTest {

    @Test
    void testAddress() throws UnknownHostException {
        assertEquals(InetAddress.getLocalHost().getHostAddress(), InetAddressStringLookup.LOCAL_HOST.apply("address"));
    }

    @Test
    void testBadKey() {
        assertThrows(IllegalArgumentException.class, () -> InetAddressStringLookup.LOCAL_HOST.apply("FOO"));
    }

    @Test
    void testCanonicalName() throws UnknownHostException {
        assertEquals(InetAddress.getLocalHost().getCanonicalHostName(), InetAddressStringLookup.LOCAL_HOST.apply("canonical-name"));
    }

    @Test
    void testName() throws UnknownHostException {
        assertEquals(InetAddress.getLocalHost().getHostName(), InetAddressStringLookup.LOCAL_HOST.apply("name"));
    }

    @Test
    void testNull() {
        assertNull(InetAddressStringLookup.LOCAL_HOST.apply(null));
    }

    @Test
    void testToString() {
        // does not blow up and gives some kind of string.
        assertFalse(InetAddressStringLookup.LOCAL_HOST.toString().isEmpty());
    }

}
