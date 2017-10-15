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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit tests for {@link HammingDistance}.
 */
public class HammingDistanceTest {

    private static HammingDistance distance;

    @BeforeClass
    public static void setUp() {
        distance = new HammingDistance();
    }

    @Test
    public void testHammingDistance() {
        assertThat(distance.apply("", "")).isEqualTo(0);
        assertThat(distance.apply("pappa", "pappa")).isEqualTo(0);
        assertThat(distance.apply("papaa", "pappa")).isEqualTo(1);
        assertThat(distance.apply("karolin", "kathrin")).isEqualTo(3);
        assertThat(distance.apply("karolin", "kerstin")).isEqualTo(3);
        assertThat(distance.apply("1011101", "1001001")).isEqualTo(2);
        assertThat(distance.apply("2173896", "2233796")).isEqualTo(3);
        assertThat(distance.apply("ATCG", "ACCC")).isEqualTo(2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHammingDistance_nullLeftValue() {
        distance.apply(null, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHammingDistance_nullRightValue() {
        distance.apply("", null);
    }

}
