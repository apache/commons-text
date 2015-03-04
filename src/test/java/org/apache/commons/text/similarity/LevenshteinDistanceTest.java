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

import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.text.LevenshteinDistance}.
 */
public class LevenshteinDistanceTest {

    private static final LevenshteinDistance UNLIMITED_DISTANCE = new LevenshteinDistance();

    @Test
    public void testGetLevenshteinDistance_StringString() {
        assertEquals(0, (int) UNLIMITED_DISTANCE.compare("", ""));
        assertEquals(1, (int) UNLIMITED_DISTANCE.compare("", "a"));
        assertEquals(7, (int) UNLIMITED_DISTANCE.compare("aaapppp", ""));
        assertEquals(1, (int) UNLIMITED_DISTANCE.compare("frog", "fog"));
        assertEquals(3, (int) UNLIMITED_DISTANCE.compare("fly", "ant"));
        assertEquals(7, (int) UNLIMITED_DISTANCE.compare("elephant", "hippo"));
        assertEquals(7, (int) UNLIMITED_DISTANCE.compare("hippo", "elephant"));
        assertEquals(8, (int) UNLIMITED_DISTANCE.compare("hippo", "zzzzzzzz"));
        assertEquals(8, (int) UNLIMITED_DISTANCE.compare("zzzzzzzz", "hippo"));
        assertEquals(1, (int) UNLIMITED_DISTANCE.compare("hello", "hallo"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLevenshteinDistance_NullString() throws Exception {
        UNLIMITED_DISTANCE.compare("a", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLevenshteinDistance_StringNull() throws Exception {
        UNLIMITED_DISTANCE.compare(null, "a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLevenshteinDistance_NullStringInt() throws Exception {
        UNLIMITED_DISTANCE.compare(null, "a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLevenshteinDistance_StringNullInt() throws Exception {
        UNLIMITED_DISTANCE.compare("a", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeThreshold() throws Exception {
        LevenshteinDistance distance = new LevenshteinDistance(-1);
    }

}
