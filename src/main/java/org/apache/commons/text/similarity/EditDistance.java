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
 * Interface for <a href="http://en.wikipedia.org/wiki/Edit_distance">Edit Distances</a>.
 *
 * <p>
 * An edit distance is a formal metric on the Kleene closure (<code>X<sup>*</sup></code>) over an
 * alphabet (<code>X</code>). Note, that a <a href="https://en.wikipedia.org/wiki/Metric_(mathematics)">metric</a>
 * on a set <code>S</code> is a function <code>d: [S * S] -&gt; [0, INFINITY)</code> such
 * that the following hold for <code>x,y,z</code> in
 * the set <code>S</code>:
 * </p>
 * <ul>
 *     <li><code>d(x,y) &gt;= 0</code>, non-negativity or separation axiom</li>
 *     <li><code>d(x,y) == 0</code>, if and only if, <code>x == y</code></li>
 *     <li><code>d(x,y) == d(y,x)</code>, symmetry, and</li>
 *     <li><code>d(x,z) &lt;=  d(x,y) + d(y,z)</code>, the triangle inequality</li>
 * </ul>
 *
 *
 * <p>
 * This is a BiFunction&lt;CharSequence, CharSequence, R&gt;.
 * The <code>apply</code> method
 * accepts a pair of {@link CharSequence} parameters
 * and returns an <code>R</code> type similarity score.
 * </p>
 *
 * @param <R> The type of similarity score unit used by this EditDistance.
 * @since 1.0
 */
public interface EditDistance<R> extends SimilarityScore<R> {

    /**
     * Compares two CharSequences.
     *
     * @param left the first CharSequence
     * @param right the second CharSequence
     * @return the similarity score between two CharSequences
     */
    @Override
    R apply(CharSequence left, CharSequence right);

}
