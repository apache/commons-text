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

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link CosineSimilarity}.
 */
public class CosineDistanceTest {

    /**
     * Cosine distance under test.
     */
    private static CosineDistance cosineDistance;

    /**
     * Creates the cosine distance object used throughout the tests.
     */
    @BeforeAll
    public static void setUp() {
        cosineDistance = new CosineDistance();
    }

    /**
     * Rounds up a value.
     *
     * @param value a value
     * @return rounded up value
     */
    private Double roundValue(final Double value) {
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    // --- Utility methods

    /**
     * Tests the cosine distance with several inputs.
     */
    @Test
    public void testCosineDistance() {
        assertThat(roundValue(cosineDistance.apply("the house", "da house"))).isEqualTo(Double.valueOf(0.5d));
        assertThat(roundValue(cosineDistance.apply("AB", "AB"))).isEqualTo(Double.valueOf(0.0d));
        assertThat(roundValue(cosineDistance.apply("AB", "BA"))).isEqualTo(Double.valueOf(1.0d));
        assertThat(roundValue(cosineDistance.apply(
                "the boy was from tamana shi, kumamoto ken, and the girl was from rio de janeiro, rio",
                "the boy was from tamana shi, kumamoto, and the boy was from rio de janeiro, rio de janeiro")))
                .isEqualTo(Double.valueOf(0.08d));
    }

}
