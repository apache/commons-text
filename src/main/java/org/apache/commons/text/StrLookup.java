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

/**
 * Lookup a String key to a String value.
 * <p>
 * This class represents the simplest form of a string to string map.
 * It has a benefit over a map in that it can create the result on
 * demand based on the key.
 * <p>
 * This class comes complete with various factory methods.
 * If these do not suffice, you can subclass and implement your own matcher.
 * <p>
 * For example, it would be possible to implement a lookup that used the
 * key as a primary key, and looked up the value on demand from the database
 *
 * @since 1.0
 */
public abstract class StrLookup {

    /**
     * Lookup that always returns null.
     */
    private static final StrLookup NONE_LOOKUP = new MapStrLookup(null);

    /**
     * Lookup based on system properties.
     */
    private static final StrLookup SYSTEM_PROPERTIES_LOOKUP = new SystemPropertiesStrLookup();

    //-----------------------------------------------------------------------
    /**
     * Returns a lookup which always returns null.
     *
     * @return a lookup that always returns null, not null
     */
    public static StrLookup noneLookup() {
        return NONE_LOOKUP;
    }

    /**
     * Returns a new lookup which uses a copy of the current
     * {@link System#getProperties() System properties}.
     * <p>
     * If a security manager blocked access to system properties, then null will
     * be returned from every lookup.
     * <p>
     * If a null key is used, this lookup will throw a NullPointerException.
     *
     * @return a lookup using system properties, not null
     */
    public static StrLookup systemPropertiesLookup() {
        return SYSTEM_PROPERTIES_LOOKUP;
    }

    /**
     * Returns a lookup which looks up values using a map.
     * <p>
     * If the map is null, then null will be returned from every lookup.
     * The map result object is converted to a string using toString().
     *
     * @param map  the map of keys to values, may be null
     * @return a lookup using the map, not null
     */
    public static StrLookup mapLookup(final Map<String, ?> map) {
        return new MapStrLookup(map);
    }

    //-----------------------------------------------------------------------
    /**
     * Constructor.
     */
    protected StrLookup() {
        super();
    }

    /**
     * Looks up a String key to a String value.
     * <p>
     * The internal implementation may use any mechanism to return the value.
     * The simplest implementation is to use a Map. However, virtually any
     * implementation is possible.
     * <p>
     * For example, it would be possible to implement a lookup that used the
     * key as a primary key, and looked up the value on demand from the database
     * Or, a numeric based implementation could be created that treats the key
     * as an integer, increments the value and return the result as a string -
     * converting 1 to 2, 15 to 16 etc.
     * <p>
     * The {@link #lookup(String)} method always returns a String, regardless of
     * the underlying data, by converting it as necessary. For example:
     * <pre>
     * Map&lt;String, Object&gt; map = new HashMap&lt;String, Object&gt;();
     * map.put("number", Integer.valueOf(2));
     * assertEquals("2", StrLookup.mapLookup(map).lookup("number"));
     * </pre>
     * @param key  the key to be looked up, may be null
     * @return the matching value, null if no match
     */
    public abstract String lookup(String key);

    //-----------------------------------------------------------------------
    /**
     * Lookup implementation that uses a Map.
     */
    static class MapStrLookup extends StrLookup {

        /** Map keys are variable names and value. */
        private final Map<String, ?> map;

        /**
         * Creates a new instance backed by a Map.
         *
         * @param map  the map of keys to values, may be null
         */
        MapStrLookup(final Map<String, ?> map) {
            this.map = map;
        }

        /**
         * Looks up a String key to a String value using the map.
         * <p>
         * If the map is null, then null is returned.
         * The map result object is converted to a string using toString().
         *
         * @param key  the key to be looked up, may be null
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
    }

    //-----------------------------------------------------------------------
    /**
     * Lookup implementation based on system properties.
     */
    private static class SystemPropertiesStrLookup extends StrLookup {
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
