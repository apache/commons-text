/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.text.similarity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@link JaccardDistance}.
 */
public class JaccardDistanceTest {

    private static JaccardDistance classBeingTested;

    @BeforeAll
    public static void setUp() {
        classBeingTested = new JaccardDistance();
    }

    @ParameterizedTest
    @MethodSource("org.apache.commons.text.similarity.SimilarityInputTest#similarityInputsEquals()")
    public void testGettingJaccardDistance(final Class<?> cls) {
        // Expected Jaccard distance = 1.0 - (intersect / union)
        assertEquals(0.0, classBeingTested.apply(SimilarityInputTest.build(cls, ""), SimilarityInputTest.build(cls, "")));
        assertEquals(1.0, classBeingTested.apply(SimilarityInputTest.build(cls, "left"), SimilarityInputTest.build(cls, "")));
        assertEquals(1.0, classBeingTested.apply(SimilarityInputTest.build(cls, ""), SimilarityInputTest.build(cls, "right")));
        assertEquals(1.0 - 3.0 / 4, classBeingTested.apply(SimilarityInputTest.build(cls, "frog"), SimilarityInputTest.build(cls, "fog")));
        assertEquals(1.0, classBeingTested.apply(SimilarityInputTest.build(cls, "fly"), SimilarityInputTest.build(cls, "ant")));
        assertEquals(1.0 - 2.0 / 9, classBeingTested.apply(SimilarityInputTest.build(cls, "elephant"), SimilarityInputTest.build(cls, "hippo")));
        assertEquals(1.0 - 7.0 / 11, classBeingTested.apply(SimilarityInputTest.build(cls, "ABC Corporation"), SimilarityInputTest.build(cls, "ABC Corp")));
        assertEquals(1.0 - 13.0 / 17,
                classBeingTested.apply(SimilarityInputTest.build(cls, "D N H Enterprises Inc"), SimilarityInputTest.build(cls, "D & H Enterprises, Inc.")));
        assertEquals(1.0 - 16.0 / 18, classBeingTested.apply(SimilarityInputTest.build(cls, "My Gym Children's Fitness Center"),
                SimilarityInputTest.build(cls, "My Gym. Childrens Fitness")));
        assertEquals(1.0 - 9.0 / 10, classBeingTested.apply(SimilarityInputTest.build(cls, "PENNSYLVANIA"), SimilarityInputTest.build(cls, "PENNCISYLVNIA")));
        assertEquals(1.0 - 1.0 / 8, classBeingTested.apply(SimilarityInputTest.build(cls, "left"), SimilarityInputTest.build(cls, "right")));
        assertEquals(1.0 - 1.0 / 8, classBeingTested.apply(SimilarityInputTest.build(cls, "leettteft"), SimilarityInputTest.build(cls, "ritttght")));
        assertEquals(0.0, classBeingTested.apply(SimilarityInputTest.build(cls, "the same string"), SimilarityInputTest.build(cls, "the same string")));
    }

    @Test
    public void testGettingJaccardDistanceCharSequence() {
        // Expected Jaccard distance = 1.0 - (intersect / union)
        assertEquals(0.0, classBeingTested.apply("", ""));
        assertEquals(1.0, classBeingTested.apply("left", ""));
        assertEquals(1.0, classBeingTested.apply("", "right"));
        assertEquals(1.0 - 3.0 / 4, classBeingTested.apply("frog", "fog"));
        assertEquals(1.0, classBeingTested.apply("fly", "ant"));
        assertEquals(1.0 - 2.0 / 9, classBeingTested.apply("elephant", "hippo"));
        assertEquals(1.0 - 7.0 / 11, classBeingTested.apply("ABC Corporation", "ABC Corp"));
        assertEquals(1.0 - 13.0 / 17, classBeingTested.apply("D N H Enterprises Inc", "D & H Enterprises, Inc."));
        assertEquals(1.0 - 16.0 / 18, classBeingTested.apply("My Gym Children's Fitness Center", "My Gym. Childrens Fitness"));
        assertEquals(1.0 - 9.0 / 10, classBeingTested.apply("PENNSYLVANIA", "PENNCISYLVNIA"));
        assertEquals(1.0 - 1.0 / 8, classBeingTested.apply("left", "right"));
        assertEquals(1.0 - 1.0 / 8, classBeingTested.apply("leettteft", "ritttght"));
        assertEquals(0.0, classBeingTested.apply("the same string", "the same string"));
    }

    @Test
    public void testGettingJaccardDistanceNullNull() {
        assertThrows(IllegalArgumentException.class, () -> classBeingTested.apply((String) null, null));
    }

    @Test
    public void testGettingJaccardDistanceNullString() {
        assertThrows(IllegalArgumentException.class, () -> classBeingTested.apply(null, "right"));
    }

    @Test
    public void testGettingJaccardDistanceStringNull() {
        assertThrows(IllegalArgumentException.class, () -> classBeingTested.apply(" ", null));
    }
}
