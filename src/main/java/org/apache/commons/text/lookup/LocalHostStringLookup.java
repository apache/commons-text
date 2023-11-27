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

/**
 * Looks up keys related to the local host: host name, canonical host name, host address.
 * <p>
 * The lookup keys are:
 * </p>
 * <ul>
 * <li><b>name</b>: for the local host name, for example {@code EXAMPLE}.</li>
 * <li><b>canonical-name</b>: for the local canonical host name, for example {@code EXAMPLE.apache.org}.</li>
 * <li><b>address</b>: for the local host address, for example {@code 192.168.56.1}.</li>
 * </ul>
 *
 * @since 1.3
 */
final class LocalHostStringLookup extends AbstractStringLookup {

    /**
     * Defines the singleton for this class.
     */
    static final LocalHostStringLookup INSTANCE = new LocalHostStringLookup();

    /**
     * No need to build instances for now.
     */
    private LocalHostStringLookup() {
        // empty
    }

    /**
     * Looks up the value of a local host key.
     *
     * @param key the key to be looked up, may be null.
     * @return The value of the environment variable.
     */
    @Override
    public String lookup(final String key) {
        if (key == null) {
            return null;
        }
        try {
            InetAddressLookupStrategy strategy = InetAddressLookupStrategyFactory.createStrategy(key);
            return strategy.lookup();
        } catch (final UnknownHostException e) {
            return null;
        }
    }

    /**
     * Interface for InetAddress lookup strategies.
     */
    private interface InetAddressLookupStrategy {
        String lookup() throws UnknownHostException;
    }

    /**
     * Strategy for InetAddress lookup by host name.
     */
    private static class HostNameLookupStrategy implements InetAddressLookupStrategy {
        @Override
        public String lookup() throws UnknownHostException {
            return InetAddress.getLocalHost().getHostName();
        }
    }

    /**
     * Strategy for InetAddress lookup by canonical host name.
     */
    private static class CanonicalNameLookupStrategy implements InetAddressLookupStrategy {
        @Override
        public String lookup() throws UnknownHostException {
            return InetAddress.getLocalHost().getCanonicalHostName();
        }
    }

    /**
     * Strategy for InetAddress lookup by host address.
     */
    private static class AddressLookupStrategy implements InetAddressLookupStrategy {
        @Override
        public String lookup() throws UnknownHostException {
            return InetAddress.getLocalHost().getHostAddress();
        }
    }

    /**
     * Factory for creating the appropriate InetAddress lookup strategy.
     */
    private static class InetAddressLookupStrategyFactory {
        static InetAddressLookupStrategy createStrategy(String key) {
            switch (key) {
                case InetAddressKeys.KEY_NAME:
                    return new HostNameLookupStrategy();
                case InetAddressKeys.KEY_CANONICAL_NAME:
                    return new CanonicalNameLookupStrategy();
                case InetAddressKeys.KEY_ADDRESS:
                    return new AddressLookupStrategy();
                default:
                    throw new IllegalArgumentException(key);
            }
        }
    }
}
