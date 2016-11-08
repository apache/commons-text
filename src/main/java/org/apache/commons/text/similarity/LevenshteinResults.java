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
 * Container class to store Levenshtein distance between two character sequence.
 * It also stores the count of inserts, deletions and substitutions needed to
 * change one character sequence to other.
 * 
 */
public class LevenshteinResults {

    private final Integer distance;
    private final Integer insertCount;
    private final Integer deleteCount;
    private final Integer substituteCount;

    public LevenshteinResults(final Integer distance, final Integer insertCount, final Integer deleteCount,
            final Integer substituteCount) {
        this.distance = distance;
        this.insertCount = insertCount;
        this.deleteCount = deleteCount;
        this.substituteCount = substituteCount;
    }

    /**
     * gets the distance between two character sequence.
     * 
     * @return distance between two character sequence.
     */
    public Integer getDistance() {
        return distance;
    }

    /**
     * gets the number of insertion needed to change one character sequence to
     * other.
     * 
     * @return insert character count.
     */
    public Integer getInsertCount() {
        return insertCount;
    }

    /**
     * gets the number of character deletion needed to change one character
     * sequence to other.
     * 
     * @return delete character count.
     */
    public Integer getDeleteCount() {
        return deleteCount;
    }

    /**
     * get the number of character substitution needed to change one character
     * sequence to other.
     * 
     * @return substitute character count.
     */
    public Integer getSubstituteCount() {
        return substituteCount;
    }

    /**
     * indicates whether this object is equal to the object passed. It checks
     * whether each of the individual attributes of both the objects are equal.
     * 
     * @param o
     *            - the object to which this object needs to be compared.
     * 
     * @return - true if this object is same as the one passed as argument,
     *         false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        LevenshteinResults result = (LevenshteinResults) o;
        return Objects.equals(distance, result.distance) && Objects.equals(insertCount, result.insertCount)
                && Objects.equals(deleteCount, result.deleteCount)
                && Objects.equals(substituteCount, result.substituteCount);
    }

    /**
     * gets the hash code value for this object. This hash code is made from
     * individual attributes of this object.
     * 
     * @return - hash code value of this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(distance, insertCount, deleteCount, substituteCount);
    }

    /**
     * returns the textual representation of this object, its formed using
     * individual attributes of this object.
     * 
     * @return - textual representation of this object.
     */
    @Override
    public String toString() {
        return "Distance: " + distance + ", Insert: " + insertCount + ", Delete: " + deleteCount + ", Substitute: "
                + substituteCount;

    }
}
