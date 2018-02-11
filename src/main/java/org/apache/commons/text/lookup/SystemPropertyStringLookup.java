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

/**
 * Looks up keys from system properties.
 *
 * @since 1.3
 */
final class SystemPropertyStringLookup extends AbstractStringLookup {

    /**
     * Defines the singleton for this class.
     */
    public static final StringLookup INSTANCE = new SystemPropertyStringLookup();

    /**
     * No need to build instances for now.
     */
    private SystemPropertyStringLookup() {
        // empty
    }

    /**
     * Looks up the value for the key from system properties.
     *
     * @param key
     *            the key to be looked up, may be null
     * @return The value associated with the key.
     * @see System#getProperty(String)
     */
    @Override
    public String lookup(final String key) {
        try {
            return System.getProperty(key);
        } catch (final SecurityException | NullPointerException | IllegalArgumentException e) {
            // Squelched. All lookup(String) will return null.
            return null;
        }
    }
}
