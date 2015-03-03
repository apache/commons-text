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

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.text.FuzzyScore}.
 */
public class FuzzyScoreTest {

    @Test
    public void testGetFuzzyScore() throws Exception {
        FuzzyScore score = new FuzzyScore(Locale.ENGLISH);

        assertEquals(0, (int) score.compare("", ""));
        assertEquals(0,
                (int) score.compare("Workshop", "b"));
        assertEquals(1,
                (int) score.compare("Room", "o"));
        assertEquals(1,
                (int) score.compare("Workshop", "w"));
        assertEquals(2,
                (int) score.compare("Workshop", "ws"));
        assertEquals(4,
                (int) score.compare("Workshop", "wo"));
        assertEquals(3, (int) score.compare(
                "Apache Software Foundation", "asf"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFuzzyScore_StringNullLocale() throws Exception {
        FuzzyScore score = new FuzzyScore(Locale.ENGLISH);

        score.compare("not null", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFuzzyScore_NullStringLocale() throws Exception {
        FuzzyScore score = new FuzzyScore(Locale.ENGLISH);

        score.compare(null, "not null");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFuzzyScore_NullNullLocale() throws Exception {
        FuzzyScore score = new FuzzyScore(Locale.ENGLISH);

        score.compare(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingLocale() throws Exception {
        FuzzyScore score = new FuzzyScore((Locale) null);
    }

}
