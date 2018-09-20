/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.text;

import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.text.lookup.StringLookup;
import org.apache.commons.text.lookup.StringLookupFactory;

/**
 * Lookup a String key to a String value.
 * <p>
 * This class represents the simplest form of a string to string map. It has a benefit over a map in that it can create
 * the result on demand based on the key.
 * <p>
 * This class comes complete with various factory methods. If these do not suffice, you can subclass and implement your
 * own matcher.
 * <p>
 * For example, it would be possible to implement a lookup that used the key as a primary key, and looked up the value
 * on demand from the database
 *
 * @param <V> the type of the values supported by the lookup
 * @since 1.0
 * @deprecated Deprecated as of 1.3, use {@link StringLookupFactory} instead. This class will be removed in 2.0.
 */
@Deprecated
public abstract class StrLookup<V> implements StringLookup {

    /**
     * Lookup that always returns null.
     */
    private static final StrLookup<String> NONE_LOOKUP = new MapStrLookup<>(null);

    /**
     * Lookup based on system properties.
     */
    private static final StrLookup<String> SYSTEM_PROPERTIES_LOOKUP = new SystemPropertiesStrLookup();

    // -----------------------------------------------------------------------
    /**
     * Returns a lookup which always returns null.
     *
     * @return a lookup that always returns null, not null
     */
    public static StrLookup<?> noneLookup() {
        return NONE_LOOKUP;
    }

    /**
     * Returns a new lookup which uses a copy of the current {@link System#getProperties() System properties}.
     * <p>
     * If a security manager blocked access to system properties, then null will be returned from every lookup.
     * <p>
     * If a null key is used, this lookup will throw a NullPointerException.
     *
     * @return a lookup using system properties, not null
     */
    public static StrLookup<String> systemPropertiesLookup() {
        return SYSTEM_PROPERTIES_LOOKUP;
    }

    /**
     * Returns a lookup which looks up values using a map.
     * <p>
     * If the map is null, then null will be returned from every lookup. The map result object is converted to a string
     * using toString().
     *
     * @param <V> the type of the values supported by the lookup
     * @param map the map of keys to values, may be null
     * @return a lookup using the map, not null
     */
    public static <V> StrLookup<V> mapLookup(final Map<String, V> map) {
        return new MapStrLookup<>(map);
    }

    /**
     * Returns a lookup which looks up values using a ResourceBundle.
     * <p>
     * If the ResourceBundle is null, then null will be returned from every lookup. The map result object is converted
     * to a string using toString().
     *
     * @param resourceBundle the map of keys to values, may be null
     * @return a lookup using the map, not null
     * @see StringLookupFactory#resourceBundleStringLookup(String)
     */
    public static StrLookup<String> resourceBundleLookup(final ResourceBundle resourceBundle) {
        return new ResourceBundleLookup(resourceBundle);
    }

    // -----------------------------------------------------------------------
    /**
     * Constructor.
     */
    protected StrLookup() {
        super();
    }

    // -----------------------------------------------------------------------
    /**
     * Lookup implementation that uses a Map.
     *
     * @param <V> the type of the values supported by the lookup
     */
    static class MapStrLookup<V> extends StrLookup<V> {

        /** Map keys are variable names and value. */
        private final Map<String, V> map;

        /**
         * Creates a new instance backed by a Map.
         *
         * @param map the map of keys to values, may be null
         */
        MapStrLookup(final Map<String, V> map) {
            this.map = map;
        }

        /**
         * Looks up a String key to a String value using the map.
         * <p>
         * If the map is null, then null is returned. The map result object is converted to a string using toString().
         *
         * @param key the key to be looked up, may be null
         * @return the matching value, null if no match
         */
        @Override
        public String lookup(final String key) {
            if (map == null) {
                return null;
            }
            final Object obj = map.get(key);
            if (obj == null) {
                return null;
            }
            return obj.toString();
        }

        @Override
        public String toString() {
            return super.toString() + " [map=" + map + "]";
        }
    }

    // -----------------------------------------------------------------------
    /**
     * Lookup implementation based on a ResourceBundle.
     */
    private static final class ResourceBundleLookup extends StrLookup<String> {

        /** ResourceBundle keys are variable names and value. */
        private final ResourceBundle resourceBundle;

        /**
         * Creates a new instance backed by a ResourceBundle.
         *
         * @param resourceBundle the ResourceBundle of keys to values, may be null
         */
        private ResourceBundleLookup(final ResourceBundle resourceBundle) {
            this.resourceBundle = resourceBundle;
        }

        @Override
        public String lookup(final String key) {
            if (resourceBundle == null || key == null || !resourceBundle.containsKey(key)) {
                return null;
            }
            return resourceBundle.getString(key);
        }

        @Override
        public String toString() {
            return super.toString() + " [resourceBundle=" + resourceBundle + "]";
        }

    }

    // -----------------------------------------------------------------------
    /**
     * Lookup implementation based on system properties.
     */
    private static final class SystemPropertiesStrLookup extends StrLookup<String> {
        /**
         * {@inheritDoc} This implementation directly accesses system properties.
         */
        @Override
        public String lookup(final String key) {
            if (key.length() > 0) {
                try {
                    return System.getProperty(key);
                } catch (final SecurityException scex) {
                    // Squelched. All lookup(String) will return null.
                    return null;
                }
            }
            return null;
        }
    }
}
