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

import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;


public class CosineSimilarityTest {

    @Test
    public void testCosineSimilarityWithNonEmptyMap() {
        final CosineSimilarity cosineSimilarity = new CosineSimilarity();
        final Map<CharSequence, Integer> hashMap = new HashMap<>();
        final Integer integer = new Integer((-397));
        hashMap.put("3J/$3.L", integer);
        final Map<CharSequence, Integer> hashMapTwo = new HashMap<>();

        assertThat(cosineSimilarity.cosineSimilarity(hashMap, hashMapTwo)).isEqualTo(0.0, within(0.01));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCosineSimilarityThrowsIllegalArgumentException() {
        final CosineSimilarity cosineSimilarity = new CosineSimilarity();
        final Map<CharSequence, Integer> map = new HashMap<>();
        cosineSimilarity.cosineSimilarity(map, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCosineSimilarityWithNull() {
        final CosineSimilarity cosineSimilarity = new CosineSimilarity();
        cosineSimilarity.cosineSimilarity(null, null);
    }

    @Test
    public void testCosineSimilarityReturningDoubleWhereByteValueIsZero() {
        final CosineSimilarity cosineSimilarity = new CosineSimilarity();
        final Map<CharSequence, Integer> hashMap = new HashMap<>();

        assertThat(cosineSimilarity.cosineSimilarity(hashMap, hashMap)).isEqualTo(0.0, within(0.01));
    }

}
