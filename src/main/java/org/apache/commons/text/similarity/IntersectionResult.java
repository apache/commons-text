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
 * Container class to store the intersection results between two sets.
 *
 * <p>Stores the size of set A, set B and the intersection of A and B (<code>A &#8745; B</code>).
 * The result can be used to produce various similarity metrics, for example the Jaccard or F1-score.</p>
 *
 * <p>This class is immutable.</p>
 *
 * @since 1.7
 * @see <a href="https://en.wikipedia.org/wiki/Jaccard_index">Jaccard index</a>
 * @see <a href="https://en.wikipedia.org/wiki/F1_score">F1 score</a>
 */
public class IntersectionResult {
    /**
     * The size of set A.
     */
    private final int sizeA;
    /**
     * The size of set B.
     */
    private final int sizeB;
    /**
     * The size of the intersection between set A and B.
     */
    private final int intersection;

    /**
     * Create the results for an intersection between two sets.
     *
     * @param sizeA the size of set A ({@code |A|})
     * @param sizeB the size of set B ({@code |B|})
     * @param intersection the size of the intersection of A and B (<code>A &#8745; B</code>)
     * @throws IllegalArgumentException if the sizes are negative or the intersection is greater
     * than the minimum of the two set sizes
     */
    public IntersectionResult(final int sizeA, final int sizeB, final int intersection) {
        if (sizeA < 0) {
            throw new IllegalArgumentException("Set size |A| is not positive: " + sizeA);
        }
        if (sizeB < 0) {
            throw new IllegalArgumentException("Set size |B| is not positive: " + sizeB);
        }
        if (intersection < 0 || intersection > Math.min(sizeA, sizeB)) {
            throw new IllegalArgumentException("Invalid intersection of |A| and |B|: " + intersection);
        }
        this.sizeA = sizeA;
        this.sizeB = sizeB;
        this.intersection = intersection;
    }

    /**
     * Get the size of set A (|A|).
     *
     * @return |A|
     */
    public int getSizeA() {
        return sizeA;
    }

    /**
     * Get the size of set B (|B|).
     *
     * @return |B|
     */
    public int getSizeB() {
        return sizeB;
    }

    /**
     * Get the size of the intersection between set A and B.
     *
     * @return <code>A &#8745; B</code>
     */
    public int getIntersection() {
        return intersection;
    }
    /**
     * Get the size of the union between set A and B.
     *
     * @return <code>A &#8745; B</code>
     */
    public long getUnion() {
        return (long) sizeA + sizeB - intersection;
    }

    /**
     * Gets the Jaccard.
     *
     * <p>This implementation defines the result as zero if there is no intersection,
     * even when the size of both sets is zero.</p>
     *
     * @return the Jaccard
     * @see <a href="https://en.wikipedia.org/wiki/Jaccard_index">Jaccard index</a>
     */
    public double getJaccard() {
        return intersection == 0 ? 0.0 : (double) intersection / getUnion();
    }

    /**
     * Gets the F1 score.
     *
     * <p>This implementation defines the result as zero if there is no intersection,
     * even when the size of both sets is zero.</p>
     *
     * @return the F1 score
     * @see <a href="https://en.wikipedia.org/wiki/F1_score">F1 score</a>
     */
    public double getF1Score() {
        return intersection == 0 ? 0.0 : 2.0 * intersection / ((long) sizeA + sizeB);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final IntersectionResult result = (IntersectionResult) o;
        return sizeA == result.sizeA && sizeB == result.sizeB && intersection == result.intersection;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sizeA, sizeB, intersection);
    }

    @Override
    public String toString() {
        return "Size A: " + sizeA + ", Size B: " + sizeB + ", Intersection: " + intersection;
    }
}
