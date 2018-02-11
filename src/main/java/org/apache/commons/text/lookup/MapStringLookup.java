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

import java.util.Map;

/**
 * A map-based lookup.
 * 
 * @param <V>
 *            A map's value type
 * 
 * @since 1.3
 */
public final class MapStringLookup<V> implements StringLookup {

    /**
     * Creates a new instance backed by a Map. Used by the default lookup.
     *
     * @param map
     *            the map of keys to values, may be null.
     * @return a new instance backed by the given map.
     */
    public static <T> MapStringLookup<T> on(final Map<String, T> map) {
        return new MapStringLookup<>(map);
    }

    /**
     * Map keys are variable names and value.
     */
    private final Map<String, V> map;

    /**
     * Creates a new instance backed by a Map. Used by the default lookup.
     *
     * @param map
     *            the map of keys to values, may be null.
     */
    private MapStringLookup(final Map<String, V> map) {
        this.map = map;
    }

    protected Map<String, V> getMap() {
        return map;
    }

    /**
     * Looks up a String key to a String value using the map.
     * <p>
     * If the map is null, then null is returned. The map result object is converted to a string using toString().
     * </p>
     *
     * @param key
     *            the key to be looked up, may be null.
     * @return the matching value, null if no match
     */
    @Override
    public String lookup(final String key) {
        if (map == null) {
            return null;
        }
        final V obj;
        try {
            obj = map.get(key);
        } catch (final NullPointerException e) {
            // Could be a ConcurrentHashMap and a null key request
            return null;
        }
        return obj != null ? obj.toString() : null;
    }

    @Override
    public String toString() {
        return getClass().getName() + " [map=" + map + "]";
    }

}
