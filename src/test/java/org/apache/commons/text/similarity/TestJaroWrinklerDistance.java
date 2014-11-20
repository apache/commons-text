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

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.text.JaroWrinklerDistance}.
 */
public class TestJaroWrinklerDistance {

    private static JaroWrinklerDistance distance;
    
    @BeforeClass
    public static void setUp() {
        distance = new JaroWrinklerDistance();
    }
    
    @Test
    public void testGetJaroWinklerDistance_StringString() {
        assertEquals(0.93d, (double) distance.compare("frog", "fog"), 0.0d);
        assertEquals(0.0d, (double) distance.compare("fly", "ant"), 0.0d);
        assertEquals(0.44d, (double) distance.compare("elephant", "hippo"), 0.0d);
        assertEquals(0.91d, (double) distance.compare("ABC Corporation", "ABC Corp"), 0.0d);
        assertEquals(0.93d, (double) distance.compare("D N H Enterprises Inc", "D & H Enterprises, Inc."), 0.0d);
        assertEquals(0.94d, (double) distance.compare("My Gym Children's Fitness Center", "My Gym. Childrens Fitness"), 0.0d);
        assertEquals(0.9d, (double) distance.compare("PENNSYLVANIA", "PENNCISYLVNIA"), 0.0d);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetJaroWinklerDistance_NullNull() throws Exception {
        distance.compare(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetJaroWinklerDistance_StringNull() throws Exception {
        distance.compare(" ", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetJaroWinklerDistance_NullString() throws Exception {
        distance.compare(null, "clear");
    }
    
}
