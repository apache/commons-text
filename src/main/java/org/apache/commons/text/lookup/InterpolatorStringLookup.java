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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Proxies other {@link StringLookup}s using a keys within ${} markers using the format "${StringLookup:Key}".
 * <p>
 * The following lookups are used by default:
 * </p>
 * <ul>
 * <li>"sys" for the {@link SystemPropertyStringLookup}.</li>
 * <li>"env" for the {@link EnvironmentVariableStringLookup}.</li>
 * <li>"java" for the {@link JavaPlatformStringLookup}.</li>
 * <li>"date" for the {@link DateStringLookup}.</li>
 * <li>"localhost" for the {@link LocalHostStringLookup}.</li>
 * <li>"xml" for the {@link XmlStringLookup}.</li>
 * <li>"properties" for the {@link PropertiesStringLookup}.</li>
 * <li>"file" for the {@link FileStringLookup}.</li>
 * <li>"url" for the {@link UrlStringLookup}.</li>
 * </ul>
 */
class InterpolatorStringLookup extends AbstractStringLookup {

    /** Constant for the prefix separator. */
    private static final char PREFIX_SEPARATOR = ':';

    /** The default string lookup. */
    private final StringLookup defaultStringLookup;

    /** The map of String lookups keyed by prefix. */
    private final Map<String, StringLookup> stringLookupMap;

    /**
     * Creates an instance using only lookups that work without initial properties and are stateless.
     * <p>
     * The following lookups are installed:
     * </p>
     * <ul>
     * <li>"sys" for the {@link SystemPropertyStringLookup}.</li>
     * <li>"env" for the {@link EnvironmentVariableStringLookup}.</li>
     * <li>"java" for the {@link JavaPlatformStringLookup}.</li>
     * <li>"date" for the {@link DateStringLookup}.</li>
     * <li>"localhost" for the {@link LocalHostStringLookup}.</li>
     * <li>"xml" for the {@link XmlStringLookup}.</li>
     * <li>"properties" for the {@link PropertiesStringLookup}.</li>
     * <li>"script" for the {@link ScriptStringLookup}.</li>
     * <li>"file" for the {@link FileStringLookup}.</li>
     * <li>"url" for the {@link UrlStringLookup}.</li>
     * </ul>
     */
    InterpolatorStringLookup() {
        this((Map<String, String>) null);
    }

    /**
     * Creates an instance using only lookups that work without initial properties and are stateless.
     * <p>
     * The following lookups are installed:
     * </p>
     * <ul>
     * <li>"sys" for the {@link SystemPropertyStringLookup}.</li>
     * <li>"env" for the {@link EnvironmentVariableStringLookup}.</li>
     * <li>"java" for the {@link JavaPlatformStringLookup}.</li>
     * <li>"date" for the {@link DateStringLookup}.</li>
     * <li>"localhost" for the {@link LocalHostStringLookup}.</li>
     * <li>"xml" for the {@link XmlStringLookup}.</li>
     * <li>"properties" for the {@link PropertiesStringLookup}.</li>
     * <li>"script" for the {@link ScriptStringLookup}.</li>
     * <li>"file" for the {@link FileStringLookup}.</li>
     * <li>"url" for the {@link UrlStringLookup}.</li>
     * </ul>
     *
     * @param <V>
     *            the map's value type.
     * @param defaultMap
     *            the default map for string lookups.
     */
    <V> InterpolatorStringLookup(final Map<String, V> defaultMap) {
        this(MapStringLookup.on(defaultMap == null ? new HashMap<String, V>() : defaultMap));
    }

    /**
     * Creates an instance with the given lookup.
     *
     * @param defaultStringLookup
     *            the default lookup.
     */
    InterpolatorStringLookup(final StringLookup defaultStringLookup) {
        this(new HashMap<>(), defaultStringLookup, true);
    }

    /**
     * Creates a fully customized instance.
     *
     * @param stringLookupMap
     *            the map of string lookups.
     * @param defaultStringLookup
     *            the default string lookup.
     * @param addDefaultLookups
     *            whether the default lookups should be used.
     */
    InterpolatorStringLookup(final Map<String, StringLookup> stringLookupMap, final StringLookup defaultStringLookup,
            final boolean addDefaultLookups) {
        super();
        this.defaultStringLookup = defaultStringLookup;
        this.stringLookupMap = new HashMap<>(stringLookupMap.size());
        for (final Entry<String, StringLookup> entry : stringLookupMap.entrySet()) {
            this.stringLookupMap.put(entry.getKey().toLowerCase(Locale.ROOT), entry.getValue());
        }
        if (addDefaultLookups) {
            this.stringLookupMap.put("sys", SystemPropertyStringLookup.INSTANCE);
            this.stringLookupMap.put("env", EnvironmentVariableStringLookup.INSTANCE);
            this.stringLookupMap.put("java", JavaPlatformStringLookup.INSTANCE);
            this.stringLookupMap.put("date", DateStringLookup.INSTANCE);
            this.stringLookupMap.put("localhost", LocalHostStringLookup.INSTANCE);
            this.stringLookupMap.put("xml", XmlStringLookup.INSTANCE);
            this.stringLookupMap.put("properties", PropertiesStringLookup.INSTANCE);
            this.stringLookupMap.put("script", ScriptStringLookup.INSTANCE);
            this.stringLookupMap.put("file", FileStringLookup.INSTANCE);
            this.stringLookupMap.put("url", UrlStringLookup.INSTANCE);
        }
    }

    /**
     * Gets the lookup map.
     *
     * @return the lookup map.
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
     * @param var
     *            the name of the variable whose value is to be looked up
     * @return the value of this variable or <b>null</b> if it cannot be resolved
     */
    @Override
    public String lookup(String var) {
        if (var == null) {
            return null;
        }

        final int prefixPos = var.indexOf(PREFIX_SEPARATOR);
        if (prefixPos >= 0) {
            final String prefix = var.substring(0, prefixPos).toLowerCase(Locale.ROOT);
            final String name = var.substring(prefixPos + 1);
            final StringLookup lookup = stringLookupMap.get(prefix);
            String value = null;
            if (lookup != null) {
                value = lookup.lookup(name);
            }

            if (value != null) {
                return value;
            }
            var = var.substring(prefixPos + 1);
        }
        if (defaultStringLookup != null) {
            return defaultStringLookup.lookup(var);
        }
        return null;
    }

    @Override
    public String toString() {
        return getClass().getName() + " [stringLookupMap=" + stringLookupMap + ", defaultStringLookup="
                + defaultStringLookup + "]";
    }
}
