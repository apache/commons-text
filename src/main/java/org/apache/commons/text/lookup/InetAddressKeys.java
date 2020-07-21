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

/**
 * Constants for referring to {@link InetAddress} APIs.
 *
 * @since 1.8
 */
final class InetAddressKeys {

    /**
     * Constants for referring to {@link InetAddress#getAddress()}.
     */
    static final String KEY_ADDRESS = "address";

    /**
     * Constants for referring to {@link InetAddress#getCanonicalHostName()}.
     */
    static final String KEY_CANONICAL_NAME = "canonical-name";

    /**
     * Constants for referring to {@link InetAddress#getHostName()}.
     */
    static final String KEY_NAME = "name";

    private InetAddressKeys() {
        // noop
    }
}
