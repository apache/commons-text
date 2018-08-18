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
 * Unit tests for {@link JaroWinklerDistance}.
 */
public class JaroWinklerDistanceTest {

    private static JaroWinklerDistance distance;

    @BeforeAll
    public static void setUp() {
        distance = new JaroWinklerDistance();
    }

    @Test
    public void testGetJaroWinklerDistance_StringString() {
        assertEquals(0.92499d, distance.apply("frog", "fog"), 0.00001d);
        assertEquals(0.0d, distance.apply("fly", "ant"), 0.00000000000000000001d);
        assertEquals(0.44166d, distance.apply("elephant", "hippo"), 0.00001d);
        assertEquals(0.90666d, distance.apply("ABC Corporation", "ABC Corp"), 0.00001d);
        assertEquals(0.95251d, distance.apply("D N H Enterprises Inc", "D & H Enterprises, Inc."), 0.00001d);
        assertEquals(0.942d,
                distance.apply("My Gym Children's Fitness Center", "My Gym. Childrens Fitness"), 0.00001d);
        assertEquals(0.898018d, distance.apply("PENNSYLVANIA", "PENNCISYLVNIA"), 0.00001d);
        assertEquals(0.971428d, distance.apply("/opt/software1", "/opt/software2"), 0.00001d);
        assertEquals(0.941666d, distance.apply("aaabcd", "aaacdb"), 0.00001d);
        assertEquals(0.911111d, distance.apply("John Horn", "John Hopkins"), 0.00001d);
    }

    @Test
    public void testGetJaroWinklerDistance_NullNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            distance.apply(null, null);
        });
    }

    @Test
    public void testGetJaroWinklerDistance_StringNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            distance.apply(" ", null);
        });
    }

    @Test
    public void testGetJaroWinklerDistance_NullString() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            distance.apply(null, "clear");
        });
    }

}
