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

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link JaccardDistance}.
 */
public class JaccardDistanceTest {

    private static JaccardDistance classBeingTested;

    @BeforeAll
    public static void setUp() {
        classBeingTested = new JaccardDistance();
    }

    @Test
    public void testGettingJaccardDistance() {
        assertEquals(1.00d, classBeingTested.apply("", ""), 0.00000000000000000001d);
        assertEquals(1.00d, classBeingTested.apply("left", ""), 0.00000000000000000001d);
        assertEquals(1.00d, classBeingTested.apply("", "right"), 0.00000000000000000001d);
        assertEquals(0.25d, classBeingTested.apply("frog", "fog"), 0.00000000000000000001d);
        assertEquals(1.00d, classBeingTested.apply("fly", "ant"), 0.00000000000000000001d);
        assertEquals(0.78d, classBeingTested.apply("elephant", "hippo"), 0.00000000000000000001d);
        assertEquals(0.36d, classBeingTested.apply("ABC Corporation", "ABC Corp"), 0.00000000000000000001d);
        assertEquals(0.24d, classBeingTested.apply("D N H Enterprises Inc", "D & H Enterprises, Inc."),
                0.00000000000000000001d);
        assertEquals(0.11d, classBeingTested.apply("My Gym Children's Fitness Center", "My Gym. Childrens Fitness"),
                0.00000000000000000001d);
        assertEquals(0.10d, classBeingTested.apply("PENNSYLVANIA", "PENNCISYLVNIA"), 0.00000000000000000001d);
        assertEquals(0.87d, classBeingTested.apply("left", "right"), 0.00000000000000000001d);
        assertEquals(0.87d, classBeingTested.apply("leettteft", "ritttght"), 0.00000000000000000001d);
        assertEquals(0.0d, classBeingTested.apply("the same string", "the same string"), 0.00000000000000000001d);
    }

    @Test
    public void testGettingJaccardDistanceNullNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            classBeingTested.apply(null, null);
        });
    }

    @Test
    public void testGettingJaccardDistanceStringNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            classBeingTested.apply(" ", null);
        });
    }

    @Test
    public void testGettingJaccardDistanceNullString() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            classBeingTested.apply(null, "right");
        });
    }
}
