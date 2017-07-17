package org.apache.commons.text.similarity;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for class {@link LevenshteinResults}.
 *
 * @date 2017-07-17
 * @see LevenshteinResults
 **/
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