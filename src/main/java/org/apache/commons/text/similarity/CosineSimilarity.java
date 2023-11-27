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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Measures the Cosine similarity of two vectors of an inner product space and compares the angle between them.
 * <p>
 * For further explanation about the Cosine Similarity, refer to http://en.wikipedia.org/wiki/Cosine_similarity.
 * </p>
 * <p>
 * Instances of this class are immutable and are safe for use by multiple concurrent threads.
 * </p>
 *
 * @since 1.0
 */
public class CosineSimilarity {

    /**
     * Singleton instance.
     */
    static final CosineSimilarity INSTANCE = new CosineSimilarity();

    /**
     * Calculates the cosine similarity for two given vectors.
     *
     * @param leftVector left vector
     * @param rightVector right vector
     * @return cosine similarity between the two vectors
     */
    public Double cosineSimilarity(final Map<CharSequence, Integer> leftVector,
                                   final Map<CharSequence, Integer> rightVector) {
        if (leftVector == null || rightVector == null) {
            throw new IllegalArgumentException("Vectors must not be null");
        }

        final Set<CharSequence> intersection = getIntersection(leftVector, rightVector);

        final double dotProduct = dot(leftVector, rightVector, intersection);

        double d1 = calculateMagnitude(leftVector);
        double d2 = calculateMagnitude(rightVector);

        final double cosineSimilarity;
        if (d1 <= 0.0 || d2 <= 0.0) {
            cosineSimilarity = 0.0;
        } else {
            cosineSimilarity = dotProduct / (Math.sqrt(d1) * Math.sqrt(d2));
        }
        return cosineSimilarity;
    }

    /**
     * Calculates the magnitude of a vector represented by the given map of character sequences and their corresponding integer values.
     *
     * The magnitude of a vector is computed as the square root of the sum of the squares of its components.
     *
     * @param vector A map representing a vector, where keys are character sequences and values are corresponding integer components.
     * @return The magnitude of the vector.
     */
    private double calculateMagnitude(Map<CharSequence, Integer> vector) {
        double magnitude = 0.0d;
        for (Integer value : vector.values()) {
            magnitude += Math.pow(value, 2);
        }
        return magnitude;
    }

    /**
     * Computes the dot product of two vectors. It ignores remaining elements. It means
     * that if a vector is longer than other, then a smaller part of it will be used to compute
     * the dot product.
     *
     * @param leftVector left vector
     * @param rightVector right vector
     * @param intersection common elements
     * @return The dot product
     */
    private double dot(final Map<CharSequence, Integer> leftVector, final Map<CharSequence, Integer> rightVector,
            final Set<CharSequence> intersection) {
        long dotProduct = 0;
        for (final CharSequence key : intersection) {
            dotProduct += leftVector.get(key) * (long) rightVector.get(key);
        }
        return dotProduct;
    }

    /**
     * Returns a set with strings common to the two given maps.
     *
     * @param leftVector left vector map
     * @param rightVector right vector map
     * @return common strings
     */
    private Set<CharSequence> getIntersection(final Map<CharSequence, Integer> leftVector,
            final Map<CharSequence, Integer> rightVector) {
        final Set<CharSequence> intersection = new HashSet<>(leftVector.keySet());
        intersection.retainAll(rightVector.keySet());
        return intersection;
    }

}
