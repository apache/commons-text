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

import org.apache.commons.text.StringSubstitutor;

/**
 * Looks up keys related to DNS entries like host name, canonical host name, host address.
 * <p>
 * The lookup keys are:
 * </p>
 * <ul>
 * <li><strong>name|<em>address</em></strong>: for the host name, for example {@code "name|93.184.216.34"} ->
 * {@code "example.com"}.</li>
 * <li><strong>canonical-name|<em>address</em></strong>: for the canonical host name, for example {@code "name|93.184.216.34"} ->
 * {@code "example.com"}.</li>
 * <li><strong>address|<em>hostname</em></strong>: for the host address, for example {@code "address|example.com"} ->
 * {@code "93.184.216.34"}.</li>
 * <li><strong><em>address</em></strong>: same as {@code address|hostname}.</li>
 * </ul>
 *
 * <p>
 * Using a {@link StringLookup} from the {@link StringLookupFactory}:
 * </p>
 *
 * <pre>
 * StringLookupFactory.INSTANCE.dnsStringLookup().lookup("address|apache.org");
 * </pre>
 * <p>
 * Using a {@link StringSubstitutor}:
 * </p>
 *
 * <pre>
 * StringSubstitutor.createInterpolator().replace("... ${dns:address|apache.org} ..."));
 * </pre>
 * <p>
 * The above examples convert {@code "address|apache.org"} to {@code "95.216.24.32} (or {@code "40.79.78.1"}).
 * </p>
 * <p>
 * Public access is through {@link StringLookupFactory}.
 * </p>
 *
 * @see StringLookupFactory
 * @since 1.8
 */
final class DnsStringLookup extends AbstractStringLookup {

    /**
     * Defines the singleton for this class.
     */
    static final DnsStringLookup INSTANCE = new DnsStringLookup();

    /**
     * No need to build instances for now.
     */
    private DnsStringLookup() {
        // empty
    }

    /**
     * Looks up the DNS value of the key.
     *
     * @param key the key to be looked up, may be null
     * @return The DNS value.
     */
    @Override
    public String lookup(final String key) {
        if (key == null) {
            return null;
        }
        final String[] keys = key.trim().split("\\|");
        final int keyLen = keys.length;
        final String subKey = keys[0].trim();
        final String subValue = keyLen < 2 ? key : keys[1].trim();
        try {
            final InetAddress inetAddress = InetAddress.getByName(subValue);
            switch (subKey) {
            case InetAddressKeys.KEY_NAME:
                return inetAddress.getHostName();
            case InetAddressKeys.KEY_CANONICAL_NAME:
                return inetAddress.getCanonicalHostName();
            case InetAddressKeys.KEY_ADDRESS:
                return inetAddress.getHostAddress();
            default:
                return inetAddress.getHostAddress();
            }
        } catch (final UnknownHostException e) {
            return null;
        }
    }

}
