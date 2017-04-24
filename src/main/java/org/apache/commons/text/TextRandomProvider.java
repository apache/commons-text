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

/**
 * <p>
 * TextRandomProvider implementations are used by {@link RandomStringGenerator}
 * as a source of randomness.  It is highly recommended that the
 * <a href="http://commons.apache.org/proper/commons-rng/">Apache Commons RNG</a>
 * library be used to provide the random number generation.
 * </p>
 *
 * <p>
 * When using Java 8 or later, TextRandomProvider is a functional interface and
 * need not be explicitly implemented.  For example:
 * </p>
 * <pre>
 * {@code
 * UniformRandomProvider rng = RandomSource.create(...);
 * RandomStringGenerator gen = new RandomStringGenerator.Builder()
 *     .usingRandom(rng::nextInt)
 *     // additional builder calls as needed
 *     .build();
 * }
 * </pre>
 * @since 1.1
 */
public interface TextRandomProvider {

    /**
     * Generates an int value between 0 (inclusive) and the specified value
     * (exclusive).
     * @param max  Bound on the random number to be returned. Must be positive.
     * @return a random int value between 0 (inclusive) and n (exclusive).
     */
    int nextInt(int max);
}
