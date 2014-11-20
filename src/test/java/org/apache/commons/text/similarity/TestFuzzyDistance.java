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
 * Unit tests for {@link org.apache.commons.text.FuzzyDistance}.
 */
public class TestFuzzyDistance {

    private static FuzzyDistance distance;

    @BeforeClass
    public static void setUp() {
        distance = new FuzzyDistance();
    }

    @Test
    public void testGetFuzzyDistance() throws Exception {
        assertEquals(0, (int) distance.compare("", "", Locale.ENGLISH));
        assertEquals(0,
                (int) distance.compare("Workshop", "b", Locale.ENGLISH));
        assertEquals(1,
                (int) distance.compare("Room", "o", Locale.ENGLISH));
        assertEquals(1,
                (int) distance.compare("Workshop", "w", Locale.ENGLISH));
        assertEquals(2,
                (int) distance.compare("Workshop", "ws", Locale.ENGLISH));
        assertEquals(4,
                (int) distance.compare("Workshop", "wo", Locale.ENGLISH));
        assertEquals(3, (int) distance.compare(
                "Apache Software Foundation", "asf", Locale.ENGLISH));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFuzzyDistance_NullNullNull() throws Exception {
        distance.compare(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFuzzyDistance_StringNullLoclae() throws Exception {
        distance.compare(" ", null, Locale.ENGLISH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFuzzyDistance_NullStringLocale() throws Exception {
        distance.compare(null, "clear", Locale.ENGLISH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFuzzyDistance_StringStringNull() throws Exception {
        distance.compare(" ", "clear", null);
    }

}
