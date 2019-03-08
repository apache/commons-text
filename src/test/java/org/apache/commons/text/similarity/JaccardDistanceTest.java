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
        // Results generated using the python distance library using:
        // distance.jaccard(seq1, seq2)
        assertEquals(1.0, classBeingTested.apply("", ""));
        assertEquals(1.0, classBeingTested.apply("left", ""));
        assertEquals(1.0, classBeingTested.apply("", "right"));
        assertEquals(0.25, classBeingTested.apply("frog", "fog"));
        assertEquals(1.0, classBeingTested.apply("fly", "ant"));
        assertEquals(0.7777777777777778, classBeingTested.apply("elephant", "hippo"));
        assertEquals(0.36363636363636365, classBeingTested.apply("ABC Corporation", "ABC Corp"));
        assertEquals(0.23529411764705888,
                classBeingTested.apply("D N H Enterprises Inc", "D & H Enterprises, Inc."));
        assertEquals(0.11111111111111116,
                classBeingTested.apply("My Gym Children's Fitness Center", "My Gym. Childrens Fitness"));
        assertEquals(0.09999999999999998, classBeingTested.apply("PENNSYLVANIA", "PENNCISYLVNIA"));
        assertEquals(0.875, classBeingTested.apply("left", "right"));
        assertEquals(0.875, classBeingTested.apply("leettteft", "ritttght"));
        assertEquals(0.0, classBeingTested.apply("the same string", "the same string"));
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
