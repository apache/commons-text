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
 * Tests {@link JaroWinklerSimilarity}.
 */
class JaroWinklerSimilarityTest {

    private static JaroWinklerSimilarity similarity;

    @BeforeAll
    public static void setUp() {
        similarity = new JaroWinklerSimilarity();
    }

    /**
     * Wraps the string in a custom {@link CharSequence}. This ensures that using the {@link Object#equals(Object)} method on the input CharSequence to test for
     * equality will fail.
     *
     * @param string the string
     * @return the char sequence
     */
    private static CharSequence wrap(final String string) {
        return new CharSequence() {

            @Override
            public char charAt(final int index) {
                return string.charAt(index);
            }

            @Override
            public boolean equals(final Object obj) {
                return string.equals(obj);
            }

            @Override
            public int hashCode() {
                return string.hashCode();
            }

            @Override
            public int length() {
                return string.length();
            }

            @Override
            public CharSequence subSequence(final int start, final int end) {
                return string.subSequence(start, end);
            }

            @Override
            public String toString() {
                return string;
            }
        };
    }

    @ParameterizedTest
    @MethodSource("org.apache.commons.text.similarity.SimilarityInputTest#similarityInputsEquals()")
    void testGetJaroWinklerSimilarity(final Class<?> cls) {
        assertEquals(1d, similarity.apply(SimilarityInputTest.build(cls, ""), SimilarityInputTest.build(cls, "")), 0.00001d);
        assertEquals(1d, similarity.apply(SimilarityInputTest.build(cls, "foo"), SimilarityInputTest.build(cls, "foo")), 0.00001d);
        assertEquals(0.94166d, similarity.apply(SimilarityInputTest.build(cls, "foo"), SimilarityInputTest.build(cls, "foo ")), 0.00001d);
        assertEquals(0.90666d, similarity.apply(SimilarityInputTest.build(cls, "foo"), SimilarityInputTest.build(cls, "foo  ")), 0.00001d);
        assertEquals(0.86666d, similarity.apply(SimilarityInputTest.build(cls, "foo"), SimilarityInputTest.build(cls, " foo ")), 0.00001d);
        assertEquals(0.51111d, similarity.apply(SimilarityInputTest.build(cls, "foo"), SimilarityInputTest.build(cls, "  foo")), 0.00001d);
        assertEquals(0.92499d, similarity.apply(SimilarityInputTest.build(cls, "frog"), SimilarityInputTest.build(cls, "fog")), 0.00001d);
        assertEquals(0.0d, similarity.apply(SimilarityInputTest.build(cls, "fly"), SimilarityInputTest.build(cls, "ant")), 0.00000000000000000001d);
        assertEquals(0.44166d, similarity.apply(SimilarityInputTest.build(cls, "elephant"), SimilarityInputTest.build(cls, "hippo")), 0.00001d);
        assertEquals(0.90666d, similarity.apply(SimilarityInputTest.build(cls, "ABC Corporation"), SimilarityInputTest.build(cls, "ABC Corp")), 0.00001d);
        assertEquals(0.95251d,
                similarity.apply(SimilarityInputTest.build(cls, "D N H Enterprises Inc"), SimilarityInputTest.build(cls, "D & H Enterprises, Inc.")), 0.00001d);
        assertEquals(0.942d, similarity.apply(SimilarityInputTest.build(cls, "My Gym Children's Fitness Center"),
                SimilarityInputTest.build(cls, "My Gym. Childrens Fitness")), 0.00001d);
        assertEquals(0.898018d, similarity.apply(SimilarityInputTest.build(cls, "PENNSYLVANIA"), SimilarityInputTest.build(cls, "PENNCISYLVNIA")), 0.00001d);
        assertEquals(0.971428d, similarity.apply(SimilarityInputTest.build(cls, "/opt/software1"), SimilarityInputTest.build(cls, "/opt/software2")), 0.00001d);
        assertEquals(0.941666d, similarity.apply(SimilarityInputTest.build(cls, "aaabcd"), SimilarityInputTest.build(cls, "aaacdb")), 0.00001d);
        assertEquals(0.911111d, similarity.apply(SimilarityInputTest.build(cls, "John Horn"), SimilarityInputTest.build(cls, "John Hopkins")), 0.00001d);
    }

    @Test
    void testGetJaroWinklerSimilarity_NullNull() {
        assertThrows(IllegalArgumentException.class, () -> similarity.apply((String) null, null));
    }

    @Test
    void testGetJaroWinklerSimilarity_NullString() {
        assertThrows(IllegalArgumentException.class, () -> similarity.apply(null, "clear"));
    }

    @Test
    void testGetJaroWinklerSimilarity_StringNull() {
        assertThrows(IllegalArgumentException.class, () -> similarity.apply(" ", null));
    }

    @Test
    void testGetJaroWinklerSimilarity_StringString() {
        assertEquals(1d, similarity.apply(wrap(""), ""), 0.00001d);
        assertEquals(1d, similarity.apply(wrap("foo"), "foo"), 0.00001d);
        assertEquals(0.94166d, similarity.apply(wrap("foo"), "foo "), 0.00001d);
        assertEquals(0.90666d, similarity.apply(wrap("foo"), "foo  "), 0.00001d);
        assertEquals(0.86666d, similarity.apply(wrap("foo"), " foo "), 0.00001d);
        assertEquals(0.51111d, similarity.apply(wrap("foo"), "  foo"), 0.00001d);
        assertEquals(0.92499d, similarity.apply(wrap("frog"), "fog"), 0.00001d);
        assertEquals(0.0d, similarity.apply(wrap("fly"), "ant"), 0.00000000000000000001d);
        assertEquals(0.44166d, similarity.apply(wrap("elephant"), "hippo"), 0.00001d);
        assertEquals(0.90666d, similarity.apply(wrap("ABC Corporation"), "ABC Corp"), 0.00001d);
        assertEquals(0.95251d, similarity.apply(wrap("D N H Enterprises Inc"), "D & H Enterprises, Inc."), 0.00001d);
        assertEquals(0.942d, similarity.apply(wrap("My Gym Children's Fitness Center"), "My Gym. Childrens Fitness"), 0.00001d);
        assertEquals(0.898018d, similarity.apply(wrap("PENNSYLVANIA"), "PENNCISYLVNIA"), 0.00001d);
        assertEquals(0.971428d, similarity.apply(wrap("/opt/software1"), "/opt/software2"), 0.00001d);
        assertEquals(0.941666d, similarity.apply(wrap("aaabcd"), "aaacdb"), 0.00001d);
        assertEquals(0.911111d, similarity.apply(wrap("John Horn"), "John Hopkins"), 0.00001d);
    }

}
