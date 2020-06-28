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
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * A function-based lookup where the request for a lookup is answered by applying that function with a key.
 *
 * @param <R> A function's return type
 * @param <P> A function's second input type
 *
 * @since 1.9
 */
final class BiFunctionStringLookup<P, R> implements BiStringLookup<P> {

    /**
     * Creates a new instance backed by a Function.
     *
     * @param <T> the function's input type
     * @param biFunction the function, may be null.
     * @return a new instance backed by the given function.
     */
    static <U, T> BiFunctionStringLookup<U, T> on(final BiFunction<String, U, T> biFunction) {
        return new BiFunctionStringLookup<>(biFunction);
    }

    /**
     * Creates a new instance backed by a Map. Used by the default lookup.
     *
     * @param <T> the map's value type.
     * @param map the map of keys to values, may be null.
     * @return a new instance backed by the given map.
     */
    static <U, T> BiFunctionStringLookup<U, T> on(final Map<String, T> map) {
        return on((key, u) -> map.get(key));
    }

    /**
     * Function.
     */
    private final BiFunction<String, P, R> biFunction;

    /**
     * Creates a new instance backed by a Function.
     *
     * @param biFunction the function, may be null.
     */
    private BiFunctionStringLookup(final BiFunction<String, P, R> biFunction) {
        this.biFunction = biFunction;
    }

    /**
     * Gets the function used in lookups.
     *
     * @return The function used in lookups.
     */
    BiFunction<String, P, R> getBiFunction() {
        return biFunction;
    }

    /**
     * Looks up a String key by applying the function.
     * <p>
     * If the function is null, then null is returned. The function result object is converted to a string using
     * toString().
     * </p>
     *
     * @param key the key to be looked up, may be null.
     * @return The function result as a string, may be null.
     */
    @Override
    public String lookup(final String key, final P object) {
        if (biFunction == null) {
            return null;
        }
        final R obj;
        try {
            obj = biFunction.apply(key, object);
        } catch (final SecurityException | NullPointerException | IllegalArgumentException e) {
            // Squelched. All lookup(String) will return null.
            // Could be a ConcurrentHashMap and a null key request
            return null;
        }
        return Objects.toString(obj, null);
    }

    @Override
    public String toString() {
        return super.toString() + " [function=" + biFunction + "]";
    }

    @Override
    public String lookup(final String key) {
        return lookup(key, null);
    }

}
