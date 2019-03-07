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

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Measures the intersection of two sets created from a pair of character
 * sequences.
 *
 * <p>It is assumed that the type {@code T} correctly conforms to the
 * requirements for storage within a {@link Set}, ideally the type is
 * immutable and implements {@link Object#equals(Object)}.</p>
 *
 * @param <T> the type of the set extracted from the character sequence
 * @since 1.7
 * @see Set
 */
public class IntersectionSimilarity<T> implements SimilarityScore<IntersectionResult> {
    /** The converter used to create the set elements. */
    private final Function<CharSequence, Set<T>> converter;

    /**
     * Create a new set similarity using the provided converter.
     *
     * @param converter the converter used to create the set
     * @throws IllegalArgumentException if the converter is null
     */
    public IntersectionSimilarity(Function<CharSequence, Set<T>> converter) {
        if (converter == null) {
            throw new IllegalArgumentException("Converter must not be null");
        }
        this.converter = converter;
    }

    /**
     * Calculates the intersection of two character sequences passed as input.
     *
     * @param left first character sequence
     * @param right second character sequence
     * @return the intersection result
     * @throws IllegalArgumentException if either input sequence is {@code null}
     */
    @Override
    public IntersectionResult apply(final CharSequence left, final CharSequence right) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }
        final Set<T> setA = converter.apply(left);
        final Set<T> setB = converter.apply(right);
        final int sizeA = setA.size();
        final int sizeB = setB.size();
        // Short-cut if either set is empty
        if (Math.min(sizeA, sizeB) == 0) {
            // No intersection
            return new IntersectionResult(sizeA, sizeB, 0);
        }
        // We can use intValue() to convert the Long output from the
        // collector as the intersection cannot be bigger than either set.
        final int intersection = setA.stream().filter(setB::contains).collect(Collectors.counting()).intValue();
        return new IntersectionResult(sizeA, sizeB, intersection);
    }
}
