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
 * Tests {@link JaroWinklerDistance}.
 */
class JaroWinklerDistanceTest {

    private static JaroWinklerDistance distance;

    @BeforeAll
    public static void setUp() {
        distance = new JaroWinklerDistance();
    }

    @ParameterizedTest
    @MethodSource("org.apache.commons.text.similarity.SimilarityInputTest#similarityInputsEquals()")
    void testGetJaroWinklerDistance(final Class<?> cls) {
        assertEquals(0.07501d, distance.apply(SimilarityInputTest.build(cls, "frog"), SimilarityInputTest.build(cls, "fog")), 0.00001d);
        assertEquals(1.0d, distance.apply(SimilarityInputTest.build(cls, "fly"), SimilarityInputTest.build(cls, "ant")), 0.00000000000000000001d);
        assertEquals(0.55834d, distance.apply(SimilarityInputTest.build(cls, "elephant"), SimilarityInputTest.build(cls, "hippo")), 0.00001d);
        assertEquals(0.09334d, distance.apply(SimilarityInputTest.build(cls, "ABC Corporation"), SimilarityInputTest.build(cls, "ABC Corp")), 0.00001d);
        assertEquals(0.04749d,
                distance.apply(SimilarityInputTest.build(cls, "D N H Enterprises Inc"), SimilarityInputTest.build(cls, "D & H Enterprises, Inc.")), 0.00001d);
        assertEquals(0.058d,
                distance.apply(SimilarityInputTest.build(cls, "My Gym Children's Fitness Center"), SimilarityInputTest.build(cls, "My Gym. Childrens Fitness")),
                0.00001d);
        assertEquals(0.101982d, distance.apply(SimilarityInputTest.build(cls, "PENNSYLVANIA"), SimilarityInputTest.build(cls, "PENNCISYLVNIA")), 0.00001d);
        assertEquals(0.028572d, distance.apply(SimilarityInputTest.build(cls, "/opt/software1"), SimilarityInputTest.build(cls, "/opt/software2")), 0.00001d);
        assertEquals(0.058334d, distance.apply(SimilarityInputTest.build(cls, "aaabcd"), SimilarityInputTest.build(cls, "aaacdb")), 0.00001d);
        assertEquals(0.088889d, distance.apply(SimilarityInputTest.build(cls, "John Horn"), SimilarityInputTest.build(cls, "John Hopkins")), 0.00001d);
        assertEquals(0d, distance.apply(SimilarityInputTest.build(cls, ""), SimilarityInputTest.build(cls, "")), 0.00001d);
        assertEquals(0d, distance.apply(SimilarityInputTest.build(cls, "foo"), SimilarityInputTest.build(cls, "foo")), 0.00001d);
        assertEquals(1 - 0.94166d, distance.apply(SimilarityInputTest.build(cls, "foo"), SimilarityInputTest.build(cls, "foo ")), 0.00001d);
        assertEquals(1 - 0.90666d, distance.apply(SimilarityInputTest.build(cls, "foo"), SimilarityInputTest.build(cls, "foo  ")), 0.00001d);
        assertEquals(1 - 0.86666d, distance.apply(SimilarityInputTest.build(cls, "foo"), SimilarityInputTest.build(cls, " foo ")), 0.00001d);
        assertEquals(1 - 0.51111d, distance.apply(SimilarityInputTest.build(cls, "foo"), SimilarityInputTest.build(cls, "  foo")), 0.00001d);
    }

    @Test
    void testGetJaroWinklerDistance_NullNull() {
        assertThrows(IllegalArgumentException.class, () -> distance.apply((String) null, null));
    }

    @Test
    void testGetJaroWinklerDistance_NullString() {
        assertThrows(IllegalArgumentException.class, () -> distance.apply(null, "clear"));
    }

    @Test
    void testGetJaroWinklerDistance_StringNull() {
        assertThrows(IllegalArgumentException.class, () -> distance.apply(" ", null));
    }

    @Test
    void testGetJaroWinklerDistance_StringString() {
        assertEquals(0.07501d, distance.apply("frog", "fog"), 0.00001d);
        assertEquals(1.0d, distance.apply("fly", "ant"), 0.00000000000000000001d);
        assertEquals(0.55834d, distance.apply("elephant", "hippo"), 0.00001d);
        assertEquals(0.09334d, distance.apply("ABC Corporation", "ABC Corp"), 0.00001d);
        assertEquals(0.04749d, distance.apply("D N H Enterprises Inc", "D & H Enterprises, Inc."), 0.00001d);
        assertEquals(0.058d, distance.apply("My Gym Children's Fitness Center", "My Gym. Childrens Fitness"), 0.00001d);
        assertEquals(0.101982d, distance.apply("PENNSYLVANIA", "PENNCISYLVNIA"), 0.00001d);
        assertEquals(0.028572d, distance.apply("/opt/software1", "/opt/software2"), 0.00001d);
        assertEquals(0.058334d, distance.apply("aaabcd", "aaacdb"), 0.00001d);
        assertEquals(0.088889d, distance.apply("John Horn", "John Hopkins"), 0.00001d);
        assertEquals(0d, distance.apply("", ""), 0.00001d);
        assertEquals(0d, distance.apply("foo", "foo"), 0.00001d);
        assertEquals(1 - 0.94166d, distance.apply("foo", "foo "), 0.00001d);
        assertEquals(1 - 0.90666d, distance.apply("foo", "foo  "), 0.00001d);
        assertEquals(1 - 0.86666d, distance.apply("foo", " foo "), 0.00001d);
        assertEquals(1 - 0.51111d, distance.apply("foo", "  foo"), 0.00001d);
    }

}
