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
package org.apache.commons.text.beta.similarity;

import java.util.HashMap;
import java.util.Map;

/**
 * Java implementation of Python's collections Counter module.
 *
 * <p>It counts how many times each element provided occurred in an array and
 * returns a dict with the element as key and the count as value.</p>
 *
 * @see <a href="https://docs.python.org/dev/library/collections.html#collections.Counter">
 * https://docs.python.org/dev/library/collections.html#collections.Counter</a>
 *
 * @since 1.0
 */
final class Counter {

    /**
     * Hidden constructor.
     */
    private Counter() {
        super();
    }

    /**
     * It counts how many times each element provided occurred in an array and
     * returns a dict with the element as key and the count as value.
     *
     * @param tokens array of tokens
     * @return dict, where the elements are key, and the count the value
     */
    public static Map<CharSequence, Integer> of(final CharSequence[] tokens) {
        final Map<CharSequence, Integer> innerCounter = new HashMap<>();
        for (final CharSequence token : tokens) {
            if (innerCounter.containsKey(token)) {
                int value = innerCounter.get(token);
                innerCounter.put(token, ++value);
            } else {
                innerCounter.put(token, 1);
            }
        }
        return innerCounter;
    }

}
