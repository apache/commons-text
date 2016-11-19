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
 * Container class to store Levenshtein distance between two character sequences.
 *
 * <p>Stores the count of insert, deletion and substitute operations needed to
 * change one character sequence into another.</p>
 *
 * <p>This class is immutable.</p>
 *
 * @since 1.0
 */
public class LevenshteinResults {
    /**
     * Edit distance.
     */
    private final Integer distance;
    /**
     * Insert character count.
     */
    private final Integer insertCount;
    /**
     * Delete character count.
     */
    private final Integer deleteCount;
    /**
     * Substitute character count.
     */
    private final Integer substituteCount;

    /**
     * Create the results for a detailed Levenshtein distance.
     *
     * @param distance distance between two character sequences.
     * @param insertCount insert character count
     * @param deleteCount delete character count
     * @param substituteCount substitute character count
     */
    public LevenshteinResults(final Integer distance, final Integer insertCount, final Integer deleteCount,
            final Integer substituteCount) {
        this.distance = distance;
        this.insertCount = insertCount;
        this.deleteCount = deleteCount;
        this.substituteCount = substituteCount;
    }

    /**
     * Get the distance between two character sequences.
     *
     * @return distance between two character sequence
     */
    public Integer getDistance() {
        return distance;
    }

    /**
     * Get the number of insertion needed to change one character sequence into another.
     *
     * @return insert character count
     */
    public Integer getInsertCount() {
        return insertCount;
    }

    /**
     * Get the number of character deletion needed to change one character sequence to other.
     *
     * @return delete character count
     */
    public Integer getDeleteCount() {
        return deleteCount;
    }

    /**
     * Get the number of character substitution needed to change one character sequence into another.
     *
     * @return substitute character count
     */
    public Integer getSubstituteCount() {
        return substituteCount;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LevenshteinResults result = (LevenshteinResults) o;
        return Objects.equals(distance, result.distance) && Objects.equals(insertCount, result.insertCount)
                && Objects.equals(deleteCount, result.deleteCount)
                && Objects.equals(substituteCount, result.substituteCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance, insertCount, deleteCount, substituteCount);
    }

    @Override
    public String toString() {
        return "Distance: " + distance + ", Insert: " + insertCount + ", Delete: " + deleteCount + ", Substitute: "
                + substituteCount;
    }
}
