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

    public LevenshteinResults (final Integer distance, final Integer insertCount, final Integer deleteCount, final Integer substituteCount) {
        this.distance = distance;
        this.insertCount = insertCount;
        this.deleteCount = deleteCount;
        this.substituteCount = substituteCount;
    }

    /**
     * gets the distance between two character sequence.
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
}
