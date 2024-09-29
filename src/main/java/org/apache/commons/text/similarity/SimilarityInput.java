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

package org.apache.commons.text.similarity;

import java.util.Objects;

/**
 * An ordered input of elements used to compute a similarity score.
 * <p>
 * You can implement a SimilarityInput on a domain object instead of CharSequence where implementing CharSequence does not make sense.
 * </p>
 *
 * @param <E> the type of elements in this input.
 * @since 1.13.0
 */
public interface SimilarityInput<E> {

    /**
     * Creates a new input for a {@link CharSequence}.
     *
     * @param cs input character sequence.
     * @return a new input.
     */
    static SimilarityInput<Character> input(final CharSequence cs) {
        return new SimilarityCharacterInput(cs);
    }

    /**
     * Creates a new input for a {@link CharSequence} or {@link SimilarityInput}.
     *
     * @param <T> The type of similarity score unit.
     * @param input character sequence or similarity input.
     * @return a new input.
     * @throws IllegalArgumentException when the input type is neither {@link CharSequence} or {@link SimilarityInput}.
     */
    @SuppressWarnings("unchecked")
    static <T> SimilarityInput<T> input(final Object input) {
        if (input instanceof SimilarityInput) {
            return (SimilarityInput<T>) input;
        }
        if (input instanceof CharSequence) {
            return (SimilarityInput<T>) input((CharSequence) input);
        }
        throw new IllegalArgumentException(Objects.requireNonNull(input, "input").getClass().getName());
    }

    /**
     * Gets the element in the input at the given 0-based index.
     *
     * @param index a 0-based index.
     * @return the element in the input at the given 0-based index.
     */
    E at(int index);

    /**
     * Gets the length of the input.
     *
     * @return the length of the input.
     */
    int length();

}
