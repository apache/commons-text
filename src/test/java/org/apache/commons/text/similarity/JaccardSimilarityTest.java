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
 * Unit tests for {@link org.apache.commons.text.similarity.JaccardSimilarity}.
 */
public class JaccardSimilarityTest {

    private static JaccardSimilarity classBeingTested;
    
    @BeforeClass
    public static void setUp() {
        classBeingTested = new JaccardSimilarity();
    }

    @Test
    public void testGettingJaccardSimilarity() {
        assertEquals(0.00d, classBeingTested.apply("", ""), 0.0d);
        assertEquals(0.00d, classBeingTested.apply("left", ""), 0.0d);
        assertEquals(0.00d, classBeingTested.apply("", "right"), 0.0d);
        assertEquals(0.75d, classBeingTested.apply("frog", "fog"), 0.0d);
        assertEquals(0.00d, classBeingTested.apply("fly", "ant"), 0.0d);
        assertEquals(0.22d, classBeingTested.apply("elephant", "hippo"), 0.0d);
        assertEquals(0.64d, classBeingTested.apply("ABC Corporation", "ABC Corp"), 0.0d);
        assertEquals(0.76d, classBeingTested.apply("D N H Enterprises Inc", "D & H Enterprises, Inc."), 0.0d);
        assertEquals(0.89d, classBeingTested.apply("My Gym Children's Fitness Center", "My Gym. Childrens Fitness"), 0.0d);
        assertEquals(0.9d, classBeingTested.apply("PENNSYLVANIA", "PENNCISYLVNIA"), 0.0d);
        assertEquals(0.13d, classBeingTested.apply("left", "right"), 0.0d);
        assertEquals(0.13d, classBeingTested.apply("leettteft", "ritttght"), 0.0d);
        assertEquals(1.0d, classBeingTested.apply("the same string", "the same string"), 0.0d);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGettingJaccardSimilarityNullNull() throws Exception {
        classBeingTested.apply(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGettingJaccardSimilarityStringNull() throws Exception {
        classBeingTested.apply(" ", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGettingJaccardSimilarityNullString() throws Exception {
        classBeingTested.apply(null, "right");
    }
}
