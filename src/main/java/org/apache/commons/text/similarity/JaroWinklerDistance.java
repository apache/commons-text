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

/**
 * Measures the Jaro-Winkler distance of two character sequences.
 * It is the complementary of Jaro-Winkler similarity.
 *
 * @since 1.0
 */
public class JaroWinklerDistance implements EditDistance<Double> {

    /**
     * Computes the Jaro Winkler Distance between two character sequences.
     *
     * <pre>
     * distance.apply(null, null)          = IllegalArgumentException
     * distance.apply("foo", null)         = IllegalArgumentException
     * distance.apply(null, "foo")         = IllegalArgumentException
     * distance.apply("", "")              = 0.0
     * distance.apply("foo", "foo")        = 0.0
     * distance.apply("foo", "foo ")       = 0.06
     * distance.apply("foo", "foo  ")      = 0.09
     * distance.apply("foo", " foo ")      = 0.13
     * distance.apply("foo", "  foo")      = 0.49
     * distance.apply("", "a")             = 1.0
     * distance.apply("aaapppp", "")       = 1.0
     * distance.apply("frog", "fog")       = 0.07
     * distance.apply("fly", "ant")        = 1.0
     * distance.apply("elephant", "hippo") = 0.56
     * distance.apply("hippo", "elephant") = 0.56
     * distance.apply("hippo", "zzzzzzzz") = 1.0
     * distance.apply("hello", "hallo")    = 0.12
     * distance.apply("ABC Corporation", "ABC Corp") = 0.07
     * distance.apply("D N H Enterprises Inc", "D &amp; H Enterprises, Inc.") = 0.05
     * distance.apply("My Gym Children's Fitness Center", "My Gym. Childrens Fitness") = 0.08
     * distance.apply("PENNSYLVANIA", "PENNCISYLVNIA") = 0.12
     * </pre>
     *
     * @param left the first CharSequence, must not be null
     * @param right the second CharSequence, must not be null
     * @return result distance
     * @throws IllegalArgumentException if either CharSequence input is {@code null}
     */
    @Override
    public Double apply(CharSequence left, CharSequence right) {

        if (left == null || right == null) {
            throw new IllegalArgumentException("CharSequences must not be null");
        }

        JaroWinklerSimilarity similarity = new JaroWinklerSimilarity();
        return 1 - similarity.apply(left, right);
    }
}
