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
 * Unit tests for {@link JaroWinklerSimilarity}.
 */
public class JaroWinklerSimilarityTest {

    private static JaroWinklerSimilarity similarity;

    @BeforeAll
    public static void setUp() {
        similarity = new JaroWinklerSimilarity();
    }

    @Test
    public void testGetJaroWinklerSimilarity_StringString() {
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
        assertEquals(0.942d,
                similarity.apply(wrap("My Gym Children's Fitness Center"), "My Gym. Childrens Fitness"), 0.00001d);
        assertEquals(0.898018d, similarity.apply(wrap("PENNSYLVANIA"), "PENNCISYLVNIA"), 0.00001d);
        assertEquals(0.971428d, similarity.apply(wrap("/opt/software1"), "/opt/software2"), 0.00001d);
        assertEquals(0.941666d, similarity.apply(wrap("aaabcd"), "aaacdb"), 0.00001d);
        assertEquals(0.911111d, similarity.apply(wrap("John Horn"), "John Hopkins"), 0.00001d);
    }

    /**
     * Wrap the string to a {@link CharSequence}. This ensures that using the
     * {@link Object#equals(Object)} method on the input CharSequence to test for
     * equality will fail.
     *
     * @param string the string
     * @return the char sequence
     */
    private static CharSequence wrap(String string) {
        return new CharSequence() {
            @Override
            public int length() {
                return string.length();
            }
            @Override
            public char charAt(int index) {
                return string.charAt(index);
            }
            @Override
            public CharSequence subSequence(int start, int end) {
                return string.subSequence(start, end);
            }
        };
    }

    @Test
    public void testGetJaroWinklerSimilarity_NullNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            similarity.apply(null, null);
        });
    }

    @Test
    public void testGetJaroWinklerSimilarity_StringNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            similarity.apply(" ", null);
        });
    }

    @Test
    public void testGetJaroWinklerSimilarity_NullString() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            similarity.apply(null, "clear");
        });
    }

}
