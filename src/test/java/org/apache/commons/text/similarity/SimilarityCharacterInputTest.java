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
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@link SimilarityCharacterInput}.
 */
public class SimilarityCharacterInputTest {

    @ParameterizedTest
    @MethodSource("org.apache.commons.text.similarity.SimilarityInputTest#similarityInputsEquals()")
    void testEquals(final Class<?> cls) {
        final SimilarityInput<Object> similarityInput1 = SimilarityInputTest.build(cls, "");
        final SimilarityInput<Object> similarityInput2 = SimilarityInputTest.build(cls, "");
        assertEquals(similarityInput1, similarityInput2);
        assertEquals(similarityInput1, similarityInput1);
        assertEquals(similarityInput2, similarityInput1);
        assertEquals(similarityInput2, similarityInput2);
        final SimilarityInput<Object> similarityInput3 = SimilarityInputTest.build(cls, "3");
        final SimilarityInput<Object> similarityInput4 = SimilarityInputTest.build(cls, "4");
        assertNotEquals(similarityInput1, similarityInput4);
        assertNotEquals(similarityInput2, similarityInput4);
        assertNotEquals(similarityInput3, similarityInput4);
    }

    @ParameterizedTest
    @MethodSource("org.apache.commons.text.similarity.SimilarityInputTest#similarityInputsEquals()")
    void testHashCode(final Class<?> cls) {
        final SimilarityInput<Object> similarityInput1 = SimilarityInputTest.build(cls, "");
        final SimilarityInput<Object> similarityInput2 = SimilarityInputTest.build(cls, "");
        assertEquals(similarityInput1.hashCode(), similarityInput2.hashCode());
        assertEquals(similarityInput1.hashCode(), similarityInput1.hashCode());
        assertEquals(similarityInput2.hashCode(), similarityInput1.hashCode());
        assertEquals(similarityInput2.hashCode(), similarityInput2.hashCode());
        final SimilarityInput<Object> similarityInput3 = SimilarityInputTest.build(cls, "3");
        final SimilarityInput<Object> similarityInput4 = SimilarityInputTest.build(cls, "4");
        assertNotEquals(similarityInput1.hashCode(), similarityInput4.hashCode());
        assertNotEquals(similarityInput2.hashCode(), similarityInput4.hashCode());
        assertNotEquals(similarityInput3.hashCode(), similarityInput4.hashCode());
    }

    @ParameterizedTest
    @MethodSource("org.apache.commons.text.similarity.SimilarityInputTest#similarityInputsEquals()")
    void testToString(final Class<?> cls) {
        final SimilarityInput<Object> similarityInput1 = SimilarityInputTest.build(cls, "");
        final SimilarityInput<Object> similarityInput2 = SimilarityInputTest.build(cls, "");
        assertEquals(similarityInput1.toString(), similarityInput2.toString());
        assertEquals(similarityInput1.toString(), similarityInput1.toString());
        assertEquals(similarityInput2.toString(), similarityInput1.toString());
        assertEquals(similarityInput2.toString(), similarityInput2.toString());
        final SimilarityInput<Object> similarityInput3 = SimilarityInputTest.build(cls, "3");
        final SimilarityInput<Object> similarityInput4 = SimilarityInputTest.build(cls, "4");
        assertNotEquals(similarityInput1.toString(), similarityInput4.toString());
        assertNotEquals(similarityInput2.toString(), similarityInput4.toString());
        assertNotEquals(similarityInput3.toString(), similarityInput4.toString());
    }
}
