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

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.text.FuzzyScore}.
 */
public class FuzzyScoreTest {

    private static FuzzyScore score;

    @BeforeClass
    public static void setUp() {
        score = new FuzzyScore();
    }

    @Test
    public void testGetFuzzyScore() throws Exception {
        assertEquals(0, (int) score.compare("", "", Locale.ENGLISH));
        assertEquals(0,
                (int) score.compare("Workshop", "b", Locale.ENGLISH));
        assertEquals(1,
                (int) score.compare("Room", "o", Locale.ENGLISH));
        assertEquals(1,
                (int) score.compare("Workshop", "w", Locale.ENGLISH));
        assertEquals(2,
                (int) score.compare("Workshop", "ws", Locale.ENGLISH));
        assertEquals(4,
                (int) score.compare("Workshop", "wo", Locale.ENGLISH));
        assertEquals(3, (int) score.compare(
                "Apache Software Foundation", "asf", Locale.ENGLISH));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFuzzyScore_NullNullNull() throws Exception {
        score.compare(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFuzzyScore_StringNullLoclae() throws Exception {
        score.compare(" ", null, Locale.ENGLISH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFuzzyScore_NullStringLocale() throws Exception {
        score.compare(null, "clear", Locale.ENGLISH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFuzzyScore_StringStringNull() throws Exception {
        score.compare(" ", "clear", null);
    }

}
