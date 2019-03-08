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
 * <p>Stores the size of set A, set B and the intersection of A and B (<code>|A &#8745; B|</code>).
 * The result can be used to produce various similarity metrics, for example the Jaccard index or
 * S&#248;rensen-Dice coefficient (F1 score).</p>
 *
 * <p>This class is immutable.</p>
 *
 * @since 1.7
 * @see <a href="https://en.wikipedia.org/wiki/Jaccard_index">Jaccard index</a>
 * @see <a href="http://en.wikipedia.org/wiki/S%C3%B8rensen%E2%80%93Dice_coefficient">S&#248;rensen Dice coefficient</a>
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
     * @param intersection the size of the intersection of A and B (<code>|A &#8745; B|</code>)
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
     * Get the size of set A.
     *
     * @return |A|
     */
    public int getSizeA() {
        return sizeA;
    }

    /**
     * Get the size of set B.
     *
     * @return |B|
     */
    public int getSizeB() {
        return sizeB;
    }

    /**
     * Get the size of the intersection between set A and B.
     *
     * @return <code>|A &#8745; B|</code>
     */
    public int getIntersection() {
        return intersection;
    }
    /**
     * Get the size of the union between set A and B.
     *
     * @return <code>|A &#8746; B|</code>
     */
    public long getUnion() {
        return (long) sizeA + sizeB - intersection;
    }

    /**
     * Gets the Jaccard index. The Jaccard is the intersection divided by the union.
     *
     * <pre><code>|A &#8745; B| / |A &#8746; B| </code></pre>
     *
     * <p>This implementation defines the result as zero if there is no intersection,
     * even when the union is zero to avoid a {@link Double#NaN} result.</p>
     *
     * @return the Jaccard index
     * @see <a href="https://en.wikipedia.org/wiki/Jaccard_index">Jaccard index</a>
     */
    public double getJaccardIndex() {
        return intersection == 0 ? 0.0 : (double) intersection / getUnion();
    }

    /**
     * Gets the S&#248;rensen-Dice coefficient. The coefficient is twice the size of the intersection
     * divided by the size of both sets.
     *
     * <pre>
     * <code>2|A &#8745; B| / (|A| + |B|) </code>
     * </pre>
     *
     * <p>This is also known as the F1 score.
     *
     * <p>This implementation defines the result as zero if there is no intersection, even when the size
     * of both sets is zero to avoid a {@link Double#NaN} result.</p>
     *
     * @return the S&#248;rensen-Dice coefficient
     * @see <a href="http://en.wikipedia.org/wiki/S%C3%B8rensen%E2%80%93Dice_coefficient">S&#248;rensen
     * Dice coefficient</a>
     * @see <a href="https://en.wikipedia.org/wiki/F1_score">F1 score</a>
     */
    public double getSorensenDiceCoefficient() {
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
