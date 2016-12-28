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
 * Unit tests for {@link JaroWinklerDistance}.
 */
public class JaroWinklerDistanceTest {

    private static JaroWinklerDistance distance;
    
    @BeforeClass
    public static void setUp() {
        distance = new JaroWinklerDistance();
    }
    
    @Test
    public void testGetJaroWinklerDistance_StringString() {
        assertEquals(0.93d, (double) distance.apply("frog", "fog"), 0.0d);
        assertEquals(0.0d, (double) distance.apply("fly", "ant"), 0.0d);
        assertEquals(0.44d, (double) distance.apply("elephant", "hippo"), 0.0d);
        assertEquals(0.93d, (double) distance.apply("ABC Corporation", "ABC Corp"), 0.0d);
        assertEquals(0.95d, (double) distance.apply("D N H Enterprises Inc", "D & H Enterprises, Inc."), 0.0d);
        assertEquals(0.92d, (double) distance.apply("My Gym Children's Fitness Center", "My Gym. Childrens Fitness"), 0.0d);
        assertEquals(0.88d, (double) distance.apply("PENNSYLVANIA", "PENNCISYLVNIA"), 0.0d);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetJaroWinklerDistance_NullNull() throws Exception {
        distance.apply(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetJaroWinklerDistance_StringNull() throws Exception {
        distance.apply(" ", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetJaroWinklerDistance_NullString() throws Exception {
        distance.apply(null, "clear");
    }
    
}
