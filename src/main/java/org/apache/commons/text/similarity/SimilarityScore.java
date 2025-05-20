/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.text.similarity;

/**
 * Scores {@link CharSequence} similarity, like a {@link String}.
 * <p>
 * A string similarity score is intended to have <em>some</em> of the properties of a metric, yet
 * allowing for exceptions, like the Jaro-Winkler similarity score.
 * </p>
 * <p>
 * A similarity score is the function {@code d: [X * X] -&gt; [0, INFINITY)} with the
 * following properties:
 * </p>
 * <ul>
 *     <li>{@code d(x,y) &gt;= 0}, non-negativity or separation axiom</li>
 *     <li>{@code d(x,y) == d(y,x)}, symmetry.</li>
 * </ul>
 * <p>
 * Notice, these are two of the properties that contribute to {@code d} being a metric.
 * </p>
 * <p>
 * Further, this intended to be BiFunction&lt;CharSequence, CharSequence, R&gt;.
 * The {@code apply} method accepts a pair of {@link CharSequence} parameters
 * and returns an {@code R} type similarity score.
 * </p>
 *
 * @param <R> The type of similarity score unit.
 * @since 1.0
 */
public interface SimilarityScore<R> extends ObjectSimilarityScore<CharSequence, R> {

    /**
     * Compares two CharSequences.
     *
     * @param left  the "left" or "first" input.
     * @param right the "right" or "second" input.
     * @return The similarity score between two CharSequences.
     */
    @Override
    R apply(CharSequence left, CharSequence right);

}
