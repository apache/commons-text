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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

/**
 * Measures the intersection of two sets created from a pair of character sequences.
 *
 * <p>It is assumed that the type {@code T} correctly conforms to the requirements for storage
 * within a {@link Set} or {@link HashMap}. Ideally the type is immutable and implements
 * {@link Object#equals(Object)} and {@link Object#hashCode()}.</p>
 *
 * @param <T> the type of the elements extracted from the character sequence
 * @since 1.7
 * @see Set
 * @see HashMap
 */
public class IntersectionSimilarity<T> implements SimilarityScore<IntersectionResult> {
    /** The converter used to create the elements from the characters. */
    private final Function<CharSequence, Collection<T>> converter;

    // The following is adapted from commons-collections for a Bag.
    // A Bag is a collection that can store the count of the number
    // of copies of each element.

    /**
     * Mutable counter class for storing the count of elements.
     */
    private static class BagCount {
        /** The count. This is initialised to 1 upon construction. */
        int count = 1;
    }

    /**
     * A minimal implementation of a Bag that can store elements and a count.
     *
     * <p>For the intended purpose the Bag does not have to be a {@link Collection}. It does not
     * even have to know its own size.
     */
    private class TinyBag {
        /** The backing map. */
        private final Map<T, BagCount> map;

        /**
         * Create a new tiny bag.
         *
         * @param initialCapacity the initial capacity
         */
        TinyBag(int initialCapacity) {
            map = new HashMap<>(initialCapacity);
        }

        /**
         * Adds a new element to the bag, incrementing its count in the underlying map.
         *
         * @param object the object to add
         */
        void add(T object) {
            final BagCount mut = map.get(object);
            if (mut == null) {
                map.put(object, new BagCount());
            } else {
                mut.count++;
            }
        }

        /**
         * Returns the number of occurrence of the given element in this bag by
         * looking up its count in the underlying map.
         *
         * @param object the object to search for
         * @return the number of occurrences of the object, zero if not found
         */
        int getCount(final Object object) {
            final BagCount count = map.get(object);
            if (count != null) {
                return count.count;
            }
            return 0;
        }

        /**
         * Returns a Set view of the mappings contained in this bag.
         *
         * @return the Set view
         */
        Set<Entry<T, BagCount>> entrySet() {
            return map.entrySet();
        }

        /**
         * Get the number of unique elements in the bag.
         *
         * @return the unique element size
         */
        int uniqueElementSize() {
            return map.size();
        }
    }

    /**
     * Create a new intersection similarity using the provided converter.
     *
     * <p>If the converter returns a {@link Set} then the intersection result will
     * not include duplicates. Any other {@link Collection} is used to produce a result
     * that will include duplicates in the intersect and union.
     *
     * @param converter the converter used to create the elements from the characters
     * @throws IllegalArgumentException if the converter is null
     */
    public IntersectionSimilarity(Function<CharSequence, Collection<T>> converter) {
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

        // Create the elements from the sequences
        final Collection<T> objectsA = converter.apply(left);
        final Collection<T> objectsB = converter.apply(right);
        final int sizeA = objectsA.size();
        final int sizeB = objectsB.size();

        // Short-cut if either collection is empty
        if (Math.min(sizeA, sizeB) == 0) {
            // No intersection
            return new IntersectionResult(sizeA, sizeB, 0);
        }

        // Intersection = count the number of shared elements
        int intersection;
        if (objectsA instanceof Set && objectsB instanceof Set) {
            // If a Set then the elements will only have a count of 1.
            // Iterate over the smaller set.
            intersection = (sizeA < sizeB)
                    ? getIntersection((Set<T>) objectsA, (Set<T>) objectsB)
                    : getIntersection((Set<T>) objectsB, (Set<T>) objectsA);
        } else  {
            // Create a bag for each collection
            final TinyBag bagA = toBag(objectsA);
            final TinyBag bagB = toBag(objectsB);
            // Iterate over the smaller number of unique elements
            intersection = (bagA.uniqueElementSize() < bagB.uniqueElementSize())
                    ? getIntersection(bagA, bagB)
                    : getIntersection(bagB, bagA);
        }

        return new IntersectionResult(sizeA, sizeB, intersection);
    }

    /**
     * Convert the collection to a bag. The bag will contain the count of each element
     * in the collection.
     *
     * @param objects the objects
     * @return the bag
     */
    private TinyBag toBag(Collection<T> objects) {
        final TinyBag bag = new TinyBag(objects.size());
        for (T t : objects) {
            bag.add(t);
        }
        return bag;
    }

    /**
     * Compute the intersection between two sets. This is the count of all the elements
     * that are within both sets.
     *
     * @param <T> the type of the elements in the set
     * @param setA the set A
     * @param setB the set B
     * @return the intersection
     */
    private static <T> int getIntersection(Set<T> setA, Set<T> setB) {
        int intersection = 0;
        for (T element : setA) {
            if (setB.contains(element)) {
                intersection++;
            }
        }
        return intersection;
    }

    /**
     * Compute the intersection between two bags. This is the sum of the minimum
     * count of each element that is within both sets.
     *
     * @param bagA the bag A
     * @param bagB the bag B
     * @return the intersection
     */
    private int getIntersection(TinyBag bagA, TinyBag bagB) {
        int intersection = 0;
        for (Entry<T, BagCount> entry : bagA.entrySet()) {
            final T element = entry.getKey();
            final int count = entry.getValue().count;
            // The intersection of this entry in both bags is the minimum count
            intersection += Math.min(count, bagB.getCount(element));
        }
        return intersection;
    }
}
