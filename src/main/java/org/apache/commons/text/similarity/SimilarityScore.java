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
 * Interface for the concept of a {@link CharSequence} (like a {@link String}) similarity score.
 *
 * <p>
 * A string similarity score is intended to have <i>some</i> of the properties of a metric, yet
 * allowing for exceptions, namely the Jaro-Winkler similarity score.
 * </p>
 * <p>
 * We Define a SimilarityScore to be a function {@code d: [X * X] -&gt; [0, INFINITY)} with the
 * following properties:
 * </p>
 * <ul>
 *     <li>{@code d(x,y) &gt;= 0}, non-negativity or separation axiom</li>
 *     <li>{@code d(x,y) == d(y,x)}, symmetry.</li>
 * </ul>
 *
 * <p>
 * Notice, these are two of the properties that contribute to d being a metric.
 * </p>
 *
 *
 * <p>
 * Further, this intended to be BiFunction&lt;CharSequence, CharSequence, R&gt;.
 * The {@code apply} method
 * accepts a pair of {@link CharSequence} parameters
 * and returns an {@code R} type similarity score. We have omitted the explicit
 * statement of extending BiFunction due to it only being implemented in Java 1.8, and we
 * wish to maintain Java 1.7 compatibility.
 * </p>
 *
 * @param <R> The type of similarity score unit used by this EditDistance.
 * @since 1.0
 */
public interface SimilarityScore<R> {

    /**
     * Compares two CharSequences.
     *
     * @param left the first CharSequence
     * @param right the second CharSequence
     * @return The similarity score between two CharSequences
     */
    R apply(CharSequence left, CharSequence right);

}
