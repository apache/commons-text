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

import java.util.Locale;

/**
 * A matching algorithm that is similar to the searching algorithms implemented in editors such
 * as Sublime Text, TextMate, Atom and others.
 *
 * <p>
 * One point is given for every matched character. Subsequent matches yield two bonus points. A higher score
 * indicates a higher similarity.
 * </p>
 *
 * <p>
 * This code has been adapted from Apache Commons Lang 3.3.
 * </p>
 */
public class FuzzyDistance implements StringMetric<Integer> {

    /**
     * <p>
     * Find the Fuzzy Distance which indicates the similarity score between two
     * Strings. This method uses the default locale.
     * </p>
     *
     * @param term a full term that should be matched against, must not be null
     * @param query the query that will be matched against a term, must not be
     *            null
     * @return result score
     * @throws IllegalArgumentException if either String input {@code null}
     */
    @Override
    public Integer compare(CharSequence term, CharSequence query) {
        return compare(term, query, Locale.getDefault());
    }

    /**
     * <p>
     * Find the Fuzzy Distance which indicates the similarity score between two
     * Strings.
     * </p>
     *
     * <pre>
     * distance.getFuzzyDistance(null, null, null)                                    = IllegalArgumentException
     * distance.getFuzzyDistance("", "", Locale.ENGLISH)                              = 0
     * distance.getFuzzyDistance("Workshop", "b", Locale.ENGLISH)                     = 0
     * distance.getFuzzyDistance("Room", "o", Locale.ENGLISH)                         = 1
     * distance.getFuzzyDistance("Workshop", "w", Locale.ENGLISH)                     = 1
     * distance.getFuzzyDistance("Workshop", "ws", Locale.ENGLISH)                    = 2
     * distance.getFuzzyDistance("Workshop", "wo", Locale.ENGLISH)                    = 4
     * distance.getFuzzyDistance("Apache Software Foundation", "asf", Locale.ENGLISH) = 3
     * </pre>
     *
     * @param term a full term that should be matched against, must not be null
     * @param query the query that will be matched against a term, must not be
     *            null
     * @param locale This string matching logic is case insensitive. A locale is
     *            necessary to normalize both Strings to lower case.
     * @return result score
     * @throws IllegalArgumentException if either String input {@code null} or
     *             Locale input {@code null}
     */
    public Integer compare(CharSequence term, CharSequence query, Locale locale) {
        if (term == null || query == null) {
            throw new IllegalArgumentException("Strings must not be null");
        } else if (locale == null) {
            throw new IllegalArgumentException("Locale must not be null");
        }

        // fuzzy logic is case insensitive. We normalize the Strings to lower
        // case right from the start. Turning characters to lower case
        // via Character.toLowerCase(char) is unfortunately insufficient
        // as it does not accept a locale.
        final String termLowerCase = term.toString().toLowerCase(locale);
        final String queryLowerCase = query.toString().toLowerCase(locale);

        // the resulting score
        int score = 0;

        // the position in the term which will be scanned next for potential
        // query character matches
        int termIndex = 0;

        // index of the previously matched character in the term
        int previousMatchingCharacterIndex = Integer.MIN_VALUE;

        for (int queryIndex = 0; queryIndex < queryLowerCase.length(); queryIndex++) {
            final char queryChar = queryLowerCase.charAt(queryIndex);

            boolean termCharacterMatchFound = false;
            for (; termIndex < termLowerCase.length()
                    && !termCharacterMatchFound; termIndex++) {
                final char termChar = termLowerCase.charAt(termIndex);

                if (queryChar == termChar) {
                    // simple character matches result in one point
                    score++;

                    // subsequent character matches further improve
                    // the score.
                    if (previousMatchingCharacterIndex + 1 == termIndex) {
                        score += 2;
                    }

                    previousMatchingCharacterIndex = termIndex;

                    // we can leave the nested loop. Every character in the
                    // query can match at most one character in the term.
                    termCharacterMatchFound = true;
                }
            }
        }

        return score;
    }

}
