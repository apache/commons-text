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
 * This stores a {@link StringMetric} implementation
 * and a {@link CharSequence} "left" string.
 * The {@link #apply(CharSequence right)} method
 * accepts the "right" string and 
 * invokes the comparison function for the pair of strings.
 * </p>
 *
 * @param <R> This is the type of similarity score
              used by the StringMetric function.
 */
public class StringMetricFrom<R> {

    private final StringMetric<R> metric;
    private final CharSequence left;

    /**
     * <p>This accepts the metric implementation and the "left" string.</p>
     *
     * @param metric This may not be null.
     * @param left This may be null here,
     *             but the {@link StringMetric#compare(CharSequence left, CharSequence right)}
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
        // NOTE: This method name is inconsistent with StringMetric,
        //       but StringMetric will be updated by SANDBOX-493
        //       so that everything matches the Java 8 Function API.
        return metric.compare(left, right);
    }

    public CharSequence getLeft() {
        return left;
    }

    public StringMetric<R> getMetric() {
        return metric;
    }

}
