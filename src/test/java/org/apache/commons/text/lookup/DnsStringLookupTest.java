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

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link DnsStringLookup}.
 */
public class DnsStringLookupTest {

    @Test
    public void testAddressFromHostAddress() throws UnknownHostException {
        final InetAddress localHost = InetAddress.getLocalHost();
        Assertions.assertEquals(localHost.getHostAddress(),
                DnsStringLookup.INSTANCE.lookup("address|" + localHost.getHostAddress()));
    }

    @Test
    public void testAddressFromHostName() throws UnknownHostException {
        final InetAddress localHost = InetAddress.getLocalHost();
        Assertions.assertEquals(localHost.getHostAddress(),
                DnsStringLookup.INSTANCE.lookup("address|" + localHost.getHostName()));
    }

    @Test
    public void testCanonicalNameFromHostAddress() throws UnknownHostException {
        final InetAddress localHost = InetAddress.getLocalHost();
        Assertions.assertEquals(localHost.getCanonicalHostName(),
                DnsStringLookup.INSTANCE.lookup("canonical-name|" + localHost.getHostAddress()));
    }

    @Test
    public void testCanonicalNameFromHostName() throws UnknownHostException {
        final InetAddress localHost = InetAddress.getLocalHost();
        Assertions.assertEquals(localHost.getCanonicalHostName(),
                DnsStringLookup.INSTANCE.lookup("canonical-name|" + localHost.getHostName()));
    }

    @Test
    public void testName() throws UnknownHostException {
        final InetAddress localHost = InetAddress.getByName("127.0.0.1");
        Assertions.assertEquals(localHost.getHostName(), DnsStringLookup.INSTANCE.lookup("name|127.0.0.1"));
    }

    @Test
    public void testNull() {
        Assertions.assertNull(DnsStringLookup.INSTANCE.lookup(null));
    }

}
