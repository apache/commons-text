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
import java.net.UnknownHostException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link DnsStringLookup}.
 */
class DnsStringLookupTest {

    @Test
    void testAddressFromHostAddress() throws UnknownHostException {
        final InetAddress localHost = InetAddress.getLocalHost();
        Assertions.assertEquals(localHost.getHostAddress(),
            DnsStringLookup.INSTANCE.apply("address|" + localHost.getHostAddress()));
    }

    @Test
    void testAddressFromHostName() throws UnknownHostException {
        final InetAddress localHost = InetAddress.getLocalHost();
        Assertions.assertEquals(localHost.getHostAddress(),
            DnsStringLookup.INSTANCE.apply("address|" + localHost.getHostName()));
    }

    @Test
    void testCanonicalNameFromHostAddress() throws UnknownHostException {
        final InetAddress localHost = InetAddress.getLocalHost();
        Assertions.assertEquals(localHost.getCanonicalHostName(),
            DnsStringLookup.INSTANCE.apply("canonical-name|" + localHost.getHostAddress()));
    }

    @Test
    void testCanonicalNameFromHostName() throws UnknownHostException {
        final InetAddress localHost = InetAddress.getLocalHost();
        Assertions.assertEquals(localHost.getCanonicalHostName(),
            DnsStringLookup.INSTANCE.apply("canonical-name|" + localHost.getHostName()));
    }

    @Test
    void testName() throws UnknownHostException {
        final String address = InetAddress.getLocalHost().getHostAddress();
        final InetAddress[] localHostAll = InetAddress.getAllByName(address);
        boolean matched = false;
        for (final InetAddress localHost : localHostAll) {
            if (localHost.getHostName().equals(DnsStringLookup.INSTANCE.apply("name|" + address + ""))) {
                matched = true;
            }
        }
        Assertions.assertTrue(matched);
    }

    @Test
    void testNull() {
        Assertions.assertNull(DnsStringLookup.INSTANCE.apply(null));
    }

    @Test
    void testToString() {
        // does not blow up and gives some kind of string.
        Assertions.assertFalse(DnsStringLookup.INSTANCE.toString().isEmpty());
    }

}
