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

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.apache.commons.text.beta.similarity.LevenshteinDistance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Unit tests for {@link org.apache.commons.text.beta.similarity.LevenshteinDistance}.
 */
@RunWith(Parameterized.class)
public class ParameterizedLevenshteinDistanceTest {

    private final Integer distance;
    private final CharSequence left;
    private final CharSequence right;
    private final Integer threshold;

    public ParameterizedLevenshteinDistanceTest(
        final Integer threshold,
        final CharSequence left, final CharSequence right,
        final Integer distance) {

        this.threshold = threshold;
        this.left = left;
        this.right = right;
        this.distance = distance;
    }

    @Parameters
    public static Iterable<Object[]> parameters() {
        return Arrays.asList( new Object[][] {

            /* empty strings */
            { 0, "", "", 0 },
            { 8, "aaapppp", "", 7 },
            { 7, "aaapppp", "", 7 },
            { 6, "aaapppp", "", -1 },

            /* unequal strings, zero threshold */
            { 0, "b", "a", -1 },
            { 0, "a", "b", -1 },

            /* equal strings */
            { 0, "aa", "aa", 0 },
            { 2, "aa", "aa", 0 },

            /* same length */
            { 2, "aaa", "bbb", -1 },
            { 3, "aaa", "bbb", 3 },

            /* big stripe */
            { 10, "aaaaaa", "b", 6 },

            /* distance less than threshold */
            { 8, "aaapppp", "b", 7 },
            { 4, "a", "bbb", 3 },

            /* distance equal to threshold */
            { 7, "aaapppp", "b", 7 },
            { 3, "a", "bbb", 3 },

            /* distance greater than threshold */
            { 2, "a", "bbb", -1 },
            { 2, "bbb", "a", -1 },
            { 6, "aaapppp", "b", -1 },

            /* stripe runs off array, strings not similar */
            { 1, "a", "bbb", -1 },
            { 1, "bbb", "a", -1 },

            /* stripe runs off array, strings are similar */
            { 1, "12345", "1234567", -1 },
            { 1, "1234567", "12345", -1 },

           /* old getLevenshteinDistance test cases */
            { 1, "frog", "fog", 1 },
            { 3, "fly", "ant", 3 },
            { 7, "elephant", "hippo", 7 },
            { 6, "elephant", "hippo", -1 },
            { 7, "hippo", "elephant", 7 },
            { 6, "hippo", "elephant", -1 },
            { 8, "hippo", "zzzzzzzz", 8 },
            { 8, "zzzzzzzz", "hippo", 8 },
            { 1, "hello", "hallo", 1 },

            { Integer.MAX_VALUE, "frog", "fog", 1 },
            { Integer.MAX_VALUE, "fly", "ant", 3 },
            { Integer.MAX_VALUE, "elephant", "hippo", 7 },
            { Integer.MAX_VALUE, "hippo", "elephant", 7 },
            { Integer.MAX_VALUE, "hippo", "zzzzzzzz", 8 },
            { Integer.MAX_VALUE, "zzzzzzzz", "hippo", 8 },
            { Integer.MAX_VALUE, "hello", "hallo", 1 }

        } );
    }

    @Test
    public void test() {
        final LevenshteinDistance metric = new LevenshteinDistance(threshold);
        assertThat(metric.apply(left, right), equalTo(distance));
    }

}
