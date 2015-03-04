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
 * <p>
 * This stores a {@link StringMetric} implementation and a {@link CharSequence} "left" string.
 * The {@link #apply(CharSequence right)} method accepts the "right" string and invokes the
 * comparison function for the pair of strings.
 * </p>
 *
 * <p>
 * The following is an example which finds the most similar string:
 * </p>
 * <pre>
 * StringMetric&lt;Integer&gt; metric = new LevenshteinDistance();
 * String target = "Apache";
 * StringMetricFrom&lt;Integer&gt; metricFrom =
 *     new StringMetricFrom&lt;Integer&gt;(metric, target);
 * String mostSimilar = null;
 * Integer shortestDistance = null;
 *
 * for (String test : new String[] { "Appaloosa", "a patchy", "apple" }) {
 *     Integer distance = metricFrom.apply(test);
 *     if (shortestDistance == null || distance &lt; shortestDistance) {
 *         shortestDistance = distance;
 *         mostSimilar = test;
 *     }
 * }
 *
 * System.out.println("The string most similar to \"" + target + "\" "
 *     + "is \"" + mostSimilar + "\" because "
 *     + "its distance is only " + shortestDistance + ".");
 * </pre>
 *
 * @param <R> This is the type of similarity score used by the StringMetric function.
 */
public class StringMetricFrom<R> {

    /**
     * String metric.
     */
    private final StringMetric<R> metric;
    /**
     * Left parameter used in distance function.
     */
    private final CharSequence left;

    /**
     * <p>This accepts the metric implementation and the "left" string.</p>
     *
     * @param metric This may not be null.
     * @param left This may be null here,
     *             but the StringMetric#compare(CharSequence left, CharSequence right)
     *             implementation may not accept nulls.
     */
    public StringMetricFrom(final StringMetric<R> metric, final CharSequence left) {
        if (metric == null) {
            throw new IllegalArgumentException("The metric may not be null.");
        }

        this.metric = metric;
        this.left = left;
    }

    /**
     * <p>
     * This compares "left" field against the "right" parameter
     * using the "metric" implementation.
     * </p>
     *
     * @param right the second CharSequence
     * @return the similarity score between two CharSequences
     */
    public R apply(CharSequence right) {
        return metric.apply(left, right);
    }

    /**
     * Gets the left parameter.
     *
     * @return the left parameter
     */
    public CharSequence getLeft() {
        return left;
    }

    /**
     * Gets the right parameter.
     *
     * @return the right parameter
     */
    public StringMetric<R> getMetric() {
        return metric;
    }

}
