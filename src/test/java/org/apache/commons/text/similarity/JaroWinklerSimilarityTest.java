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
        assertEquals(1d, similarity.apply("", ""), 0.00001d);
        assertEquals(1d, similarity.apply("foo", "foo"), 0.00001d);
        assertEquals(0.94166d, similarity.apply("foo", "foo "), 0.00001d);
        assertEquals(0.90666d, similarity.apply("foo", "foo  "), 0.00001d);
        assertEquals(0.86666d, similarity.apply("foo", " foo "), 0.00001d);
        assertEquals(0.51111d, similarity.apply("foo", "  foo"), 0.00001d);
        assertEquals(0.92499d, similarity.apply("frog", "fog"), 0.00001d);
        assertEquals(0.0d, similarity.apply("fly", "ant"), 0.00000000000000000001d);
        assertEquals(0.44166d, similarity.apply("elephant", "hippo"), 0.00001d);
        assertEquals(0.90666d, similarity.apply("ABC Corporation", "ABC Corp"), 0.00001d);
        assertEquals(0.95251d, similarity.apply("D N H Enterprises Inc", "D & H Enterprises, Inc."), 0.00001d);
        assertEquals(0.942d,
                similarity.apply("My Gym Children's Fitness Center", "My Gym. Childrens Fitness"), 0.00001d);
        assertEquals(0.898018d, similarity.apply("PENNSYLVANIA", "PENNCISYLVNIA"), 0.00001d);
        assertEquals(0.971428d, similarity.apply("/opt/software1", "/opt/software2"), 0.00001d);
        assertEquals(0.941666d, similarity.apply("aaabcd", "aaacdb"), 0.00001d);
        assertEquals(0.911111d, similarity.apply("John Horn", "John Hopkins"), 0.00001d);
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
