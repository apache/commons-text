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

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * Proxies other {@link StringLookup}s using a keys within ${} markers using the format "${StringLookup:Key}".
 * <p>
 * Uses the {@link StringLookupFactory default lookups}.
 * </p>
 */
final class InterpolatorStringLookup extends AbstractStringLookup {

    /**
     * Defines the singleton for this class.
     *
     * @since 1.6
     */
    static final AbstractStringLookup INSTANCE = new InterpolatorStringLookup();

    /** Constant for the prefix separator. */
    private static final char PREFIX_SEPARATOR = ':';

    /** The default string lookup. */
    private final StringLookup defaultStringLookup;

    /** The map of String lookups keyed by prefix. */
    private final Map<String, StringLookup> stringLookupMap;

    /**
     * Constructs an instance using only lookups that work without initial properties and are stateless.
     * <p>
     * Uses the {@link StringLookupFactory default lookups}.
     * </p>
     */
    InterpolatorStringLookup() {
        this((Map<String, String>) null);
    }

    /**
     * Constructs a fully customized instance.
     *
     * @param stringLookupMap the map of string lookups.
     * @param defaultStringLookup the default string lookup.
     * @param addDefaultLookups whether the default lookups should be used.
     */
    InterpolatorStringLookup(final Map<String, StringLookup> stringLookupMap, final StringLookup defaultStringLookup,
            final boolean addDefaultLookups) {
        this.defaultStringLookup = defaultStringLookup;
        this.stringLookupMap = stringLookupMap.entrySet().stream().collect(Collectors.toMap(e -> StringLookupFactory.toKey(e.getKey()), Entry::getValue));
        if (addDefaultLookups) {
            StringLookupFactory.INSTANCE.addDefaultStringLookups(this.stringLookupMap);
        }
    }

    /**
     * Constructs an instance using only lookups that work without initial properties and are stateless.
     * <p>
     * Uses the {@link StringLookupFactory default lookups}.
     * </p>
     *
     * @param <V> the map's value type.
     * @param defaultMap the default map for string lookups.
     */
    <V> InterpolatorStringLookup(final Map<String, V> defaultMap) {
        this(StringLookupFactory.INSTANCE.mapStringLookup(defaultMap));
    }

    /**
     * Constructs an instance with the given lookup.
     *
     * @param defaultStringLookup the default lookup.
     */
    InterpolatorStringLookup(final StringLookup defaultStringLookup) {
        this(Collections.emptyMap(), defaultStringLookup, true);
    }

    /**
     * Gets the lookup map.
     *
     * @return The lookup map.
     */
    public Map<String, StringLookup> getStringLookupMap() {
        return stringLookupMap;
    }

    /**
     * Resolves the specified variable. This implementation will try to extract a variable prefix from the given
     * variable name (the first colon (':') is used as prefix separator). It then passes the name of the variable with
     * the prefix stripped to the lookup object registered for this prefix. If no prefix can be found or if the
     * associated lookup object cannot resolve this variable, the default lookup object will be used.
     *
     * @param key the name of the variable whose value is to be looked up
     * @return The value of this variable or <strong>null</strong> if it cannot be resolved
     */
    @Override
    public String lookup(String key) {
        if (key == null) {
            return null;
        }

        final int prefixPos = key.indexOf(PREFIX_SEPARATOR);
        if (prefixPos >= 0) {
            final String prefix = StringLookupFactory.toKey(key.substring(0, prefixPos));
            final String name = key.substring(prefixPos + 1);
            final StringLookup lookup = stringLookupMap.get(prefix);
            String value = null;
            if (lookup != null) {
                value = lookup.apply(name);
            }

            if (value != null) {
                return value;
            }
            key = key.substring(prefixPos + 1);
        }
        if (defaultStringLookup != null) {
            return defaultStringLookup.apply(key);
        }
        return null;
    }

    @Override
    public String toString() {
        return super.toString() + " [stringLookupMap=" + stringLookupMap + ", defaultStringLookup="
            + defaultStringLookup + "]";
    }
}
