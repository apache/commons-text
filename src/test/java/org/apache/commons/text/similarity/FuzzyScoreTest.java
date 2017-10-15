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

import static junit.framework.TestCase.assertSame;
import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.junit.Test;

/**
 * Unit tests for {@link FuzzyScore}.
 */
public class FuzzyScoreTest {

    private static final FuzzyScore ENGLISH_SCORE = new FuzzyScore(Locale.ENGLISH);

    @Test
    public void testGetFuzzyScore() throws Exception {
        assertEquals(0, (int) ENGLISH_SCORE.fuzzyScore("", ""));
        assertEquals(0, (int) ENGLISH_SCORE.fuzzyScore("Workshop", "b"));
        assertEquals(1, (int) ENGLISH_SCORE.fuzzyScore("Room", "o"));
        assertEquals(1, (int) ENGLISH_SCORE.fuzzyScore("Workshop", "w"));
        assertEquals(2, (int) ENGLISH_SCORE.fuzzyScore("Workshop", "ws"));
        assertEquals(4, (int) ENGLISH_SCORE.fuzzyScore("Workshop", "wo"));
        assertEquals(3, (int) ENGLISH_SCORE.fuzzyScore(
            "Apache Software Foundation", "asf"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFuzzyScore_StringNullLocale() throws Exception {
        ENGLISH_SCORE.fuzzyScore("not null", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFuzzyScore_NullStringLocale() throws Exception {
        ENGLISH_SCORE.fuzzyScore(null, "not null");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFuzzyScore_NullNullLocale() throws Exception {
        ENGLISH_SCORE.fuzzyScore(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingLocale() throws Exception {
        new FuzzyScore((Locale) null);
    }

    @Test
    public void testGetLocale() {
        final Locale locale = Locale.CANADA_FRENCH;
        final FuzzyScore fuzzyScore = new FuzzyScore(locale);
        final Locale localeTwo = fuzzyScore.getLocale();

        assertSame(locale, localeTwo);
    }

}
