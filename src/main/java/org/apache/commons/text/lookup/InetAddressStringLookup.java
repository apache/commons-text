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
import java.util.Objects;

import org.apache.commons.lang3.function.FailableSupplier;

/**
 * Looks up keys related to an {@link InetAddresse}.
 * <ul>
 *   <li>local host: host name, canonical host name, host address.</li>
 * </ul>
 * <p>
 * The lookup keys are:
 * </p>
 * <ul>
 * <li><strong>name</strong>: for the local host name, for example {@code EXAMPLE}.</li>
 * <li><strong>canonical-name</strong>: for the local canonical host name, for example {@code EXAMPLE.apache.org}.</li>
 * <li><strong>address</strong>: for the local host address, for example {@code 192.168.56.1}.</li>
 * </ul>
 * <p>
 * Public access is through {@link StringLookupFactory}.
 * </p>
 *
 * @see StringLookupFactory
 * @since 1.3
 */
final class InetAddressStringLookup extends AbstractStringLookup {

    /**
     * Defines the LOCAL_HOST constant.
     */
    static final InetAddressStringLookup LOCAL_HOST = new InetAddressStringLookup(InetAddress::getLocalHost);

    /**
     * Defines the LOCAL_HOST constant.
     */
    static final InetAddressStringLookup LOOPACK_ADDRESS = new InetAddressStringLookup(InetAddress::getLoopbackAddress);

    /**
     * Supplies the InetAddress.
     */
    private final FailableSupplier<InetAddress, UnknownHostException> inetAddressSupplier;

    /**
     * No need to build instances for now.
     */
    private InetAddressStringLookup(final FailableSupplier<InetAddress, UnknownHostException> inetAddressSupplier) {
        this.inetAddressSupplier = Objects.requireNonNull(inetAddressSupplier, "inetAddressSupplier");
    }

    private InetAddress getInetAddress() throws UnknownHostException {
        // Don't cache result, methods, like InetAddress::getLocalHost do their own cacheing.
        return inetAddressSupplier.get();
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
            switch (key) {
            case InetAddressKeys.KEY_NAME:
                return getInetAddress().getHostName();
            case InetAddressKeys.KEY_CANONICAL_NAME:
                return getInetAddress().getCanonicalHostName();
            case InetAddressKeys.KEY_ADDRESS:
                return getInetAddress().getHostAddress();
            default:
                throw new IllegalArgumentException(key);
            }
        } catch (final UnknownHostException e) {
            return null;
        }
    }
}
