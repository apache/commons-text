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
import org.checkerframework.checker.signedness.qual.Signed;

/**
 * Measures the Cosine similarity of two vectors of an inner product space and
 * compares the angle between them.
 *
 * <p>
 * For further explanation about the Cosine Similarity, refer to
 * http://en.wikipedia.org/wiki/Cosine_similarity.
 * </p>
 *
 * @since 1.0
 */
public class CosineSimilarity {

    /**
     * Calculates the cosine similarity for two given vectors.
     *
     * @param leftVector left vector
     * @param rightVector right vector
     * @return cosine similarity between the two vectors
     */
    /* On line #58 and #62, `value` should ideally be converted to `double` and then sent to `pow()`
     * but even if not done as said, the code is safe as Java takes care of the conversion internally */
    @SuppressWarnings("argument.type.incompatible")
    public @Signed Double cosineSimilarity(final Map<CharSequence, Integer> leftVector,
                                   final Map<CharSequence, Integer> rightVector) {
        if (leftVector == null || rightVector == null) {
            throw new IllegalArgumentException("Vectors must not be null");
        }

        final Set<CharSequence> intersection = getIntersection(leftVector, rightVector);

        final double dotProduct = dot(leftVector, rightVector, intersection);
        double d1 = 0.0d;
        for (final Integer value : leftVector.values()) {
            d1 += Math.pow(value, 2);
        }
        double d2 = 0.0d;
        for (final Integer value : rightVector.values()) {
            d2 += Math.pow(value, 2);
        }
        double cosineSimilarity;
        if (d1 <= 0.0 || d2 <= 0.0) {
            cosineSimilarity = 0.0;
        } else {
            cosineSimilarity = dotProduct / (Math.sqrt(d1) * Math.sqrt(d2));
        }
        return cosineSimilarity;
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
    /* Code is safe as `rightVector.get()` returns a value which is converted to `long` explicitly 
     * and due to this `leftVector.get()` is also converted to `long` implicitly,
     * the final value which is assigned to `dotProduct` is indeed a `@Signed long`
     * */
    @SuppressWarnings("compound.assignment.type.incompatible")
    private double dot(final Map<CharSequence, Integer> leftVector, final Map<CharSequence, Integer> rightVector,
            final Set<CharSequence> intersection) {
        long dotProduct = 0;
        for (final CharSequence key : intersection) {
            dotProduct += leftVector.get(key) * (long) rightVector.get(key);
        }
        return dotProduct;
    }

}
