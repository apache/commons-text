/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.text.similarity;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Java implementation of Python's collections Counter module.
 *
 * <p>It counts how many times each element provided occurred in an array and
 * returns a map with the element as key and the count as value.</p>
 *
 * @see <a href="https://docs.python.org/dev/library/collections.html#collections.Counter">
 * https://docs.python.org/dev/library/collections.html#collections.Counter</a>
 *
 * @since 1.0
 */
final class Counter {

    /**
     * Counts how many times each element provided occurred in an array and
     * returns a map with the element as key and the count as value.
     *
     * @param tokens array of tokens.
     * @return a map, where the elements are key, and the count the value.
     */
    public static Map<CharSequence, Integer> of(final CharSequence[] tokens) {
        final Map<CharSequence, Integer> map = new HashMap<>();
        Stream.of(tokens).forEach(token -> map.compute(token, (k, v) -> v != null ? v + 1 : 1));
        return map;
    }

    /**
     * No instance needed.
     */
    private Counter() {
    }

}
