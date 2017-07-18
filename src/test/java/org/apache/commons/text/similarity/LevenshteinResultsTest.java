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

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LevenshteinResultsTest {

    @Test
    public void testEqualsReturningFalse() {
        Integer integerOne = new Integer(1662);
        Integer integerTwo = new Integer(1164);
        LevenshteinResults levenshteinResults = new LevenshteinResults(integerOne, integerOne, integerOne, integerOne);
        LevenshteinResults levenshteinResultsTwo = new LevenshteinResults(integerOne, integerOne, integerTwo, integerTwo);

        assertFalse(levenshteinResults.equals(levenshteinResultsTwo));
    }

    @Test
    public void testEqualsWithNonNull() {
        Integer integer = new Integer(1);
        LevenshteinResults levenshteinResults = new LevenshteinResults(null, integer, integer, null);
        LevenshteinResults levenshteinResultsTwo = new LevenshteinResults(null, null, null, null);

        assertFalse(levenshteinResults.equals(levenshteinResultsTwo));
    }

    @Test
    public void testEqualsWithNull() {
        Integer integer = new Integer((-647));
        LevenshteinResults levenshteinResults = new LevenshteinResults(integer, null, null, integer);

        assertFalse(levenshteinResults.equals(null));
    }

    @Test
    public void testEqualsDifferenceInSubstitutionCount() {
        Integer integer = new Integer(1662);
        LevenshteinResults levenshteinResults = new LevenshteinResults(integer, integer, integer, integer);
        LevenshteinResults levenshteinResultsTwo = new LevenshteinResults(integer, integer, integer, null);

        assertFalse(levenshteinResults.equals(levenshteinResultsTwo));
    }

    @Test
    public void testEqualsSameObject() {
        Integer integer = new Integer(1662);
        LevenshteinResults levenshteinResults = new LevenshteinResults(integer, integer, integer, null);

        assertTrue(levenshteinResults.equals(levenshteinResults));
    }

}