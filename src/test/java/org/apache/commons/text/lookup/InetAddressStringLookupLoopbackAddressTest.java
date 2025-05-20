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

import java.net.InetAddress;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link InetAddressStringLookup#LOOPACK_ADDRESS}.
 */
public class InetAddressStringLookupLoopbackAddressTest {

    @Test
    public void testAddress() {
        Assertions.assertEquals(InetAddress.getLoopbackAddress().getHostAddress(), InetAddressStringLookup.LOOPACK_ADDRESS.apply("address"));
    }

    @Test
    public void testBadKey() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> InetAddressStringLookup.LOOPACK_ADDRESS.apply("FOO"));
    }

    @Test
    public void testCanonicalName() {
        Assertions.assertEquals(InetAddress.getLoopbackAddress().getCanonicalHostName(), InetAddressStringLookup.LOOPACK_ADDRESS.apply("canonical-name"));
    }

    @Test
    public void testName() {
        Assertions.assertEquals(InetAddress.getLoopbackAddress().getHostName(), InetAddressStringLookup.LOOPACK_ADDRESS.apply("name"));
    }

    @Test
    public void testNull() {
        Assertions.assertNull(InetAddressStringLookup.LOOPACK_ADDRESS.apply(null));
    }

    @Test
    public void testToString() {
        // does not blow up and gives some kind of string.
        Assertions.assertFalse(InetAddressStringLookup.LOOPACK_ADDRESS.toString().isEmpty());
    }

}
