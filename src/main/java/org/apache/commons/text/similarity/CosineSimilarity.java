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

import java.util.Map;

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

        double dotProduct = 0.0d;
        double normA = 0.0d;
        double normB = 0.0d;

        for (Map.Entry<CharSequence, Integer> leftEntry : leftVector.entrySet()) {
            normA += Math.pow(leftEntry.getValue(), 2);
            if (rightVector.containsKey(leftEntry.getKey())) {
                dotProduct += leftEntry.getValue() * rightVector.get(leftEntry.getKey());
            }
        }

        for (Integer rightValue : rightVector.values()) {
            normB += Math.pow(rightValue, 2);
        }

        double denominator = Math.sqrt(normA) * Math.sqrt(normB);
        return (denominator != 0) ? dotProduct / denominator : 0.0;
    }
}
