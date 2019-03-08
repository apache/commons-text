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
 * Unit tests for {@link JaccardSimilarity}.
 */
public class JaccardSimilarityTest {

    private static JaccardSimilarity classBeingTested;

    @BeforeAll
    public static void setUp() {
        classBeingTested = new JaccardSimilarity();
    }

    @Test
    public void testGettingJaccardSimilarity() {
        // Expected Jaccard similarity = (intersect / union)
        assertEquals(0.0, classBeingTested.apply("", ""));
        assertEquals(0.0, classBeingTested.apply("left", ""));
        assertEquals(0.0, classBeingTested.apply("", "right"));
        assertEquals(3.0 / 4, classBeingTested.apply("frog", "fog"));
        assertEquals(0.0, classBeingTested.apply("fly", "ant"));
        assertEquals(2.0 / 9, classBeingTested.apply("elephant", "hippo"));
        assertEquals(7.0 / 11, classBeingTested.apply("ABC Corporation", "ABC Corp"));
        assertEquals(13.0 / 17,
                classBeingTested.apply("D N H Enterprises Inc", "D & H Enterprises, Inc."));
        assertEquals(16.0 / 18,
                classBeingTested.apply("My Gym Children's Fitness Center", "My Gym. Childrens Fitness"));
        assertEquals(9.0 / 10, classBeingTested.apply("PENNSYLVANIA", "PENNCISYLVNIA"));
        assertEquals(1.0 / 8, classBeingTested.apply("left", "right"));
        assertEquals(1.0 / 8, classBeingTested.apply("leettteft", "ritttght"));
        assertEquals(1.0, classBeingTested.apply("the same string", "the same string"));
    }

    @Test
    public void testGettingJaccardSimilarityNullNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            classBeingTested.apply(null, null);
        });
    }

    @Test
    public void testGettingJaccardSimilarityStringNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            classBeingTested.apply(" ", null);
        });
    }

    @Test
    public void testGettingJaccardSimilarityNullString() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            classBeingTested.apply(null, "right");
        });
    }
}
