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
 * Interface for <a href="https://en.wikipedia.org/wiki/Edit_distance">Edit Distances</a>.
 *
 * <p>
 * An edit distance is a formal metric on the Kleene closure ({@code X<sup>*</sup>}) over an
 * alphabet ({@code X}). Note, that a <a href="https://en.wikipedia.org/wiki/Metric_(mathematics)">metric</a>
 * on a set {@code S} is a function {@code d: [S * S] -&gt; [0, INFINITY)} such
 * that the following hold for {@code x,y,z} in
 * the set {@code S}:
 * </p>
 * <ul>
 *     <li>{@code d(x,y) &gt;= 0}, non-negativity or separation axiom</li>
 *     <li>{@code d(x,y) == 0}, if and only if, {@code x == y}</li>
 *     <li>{@code d(x,y) == d(y,x)}, symmetry, and</li>
 *     <li>{@code d(x,z) &lt;=  d(x,y) + d(y,z)}, the triangle inequality</li>
 * </ul>
 *
 *
 * <p>
 * This is a BiFunction&lt;CharSequence, CharSequence, R&gt;.
 * The {@code apply} method
 * accepts a pair of {@link CharSequence} parameters
 * and returns an {@code R} type similarity score.
 * </p>
 *
 * @param <R> The type of similarity score unit used by this EditDistance.
 * @since 1.0
 */
public interface EditDistance<R> extends SimilarityScore<R> {
    // empty
}
