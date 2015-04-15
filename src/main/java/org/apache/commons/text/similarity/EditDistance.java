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
 * A edit distance measures the similarity between two character sequences. Closer strings
 * have shorter distances, and vice-versa.
 * </p>
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
public interface EditDistance<R> {

    /**
     * Compares two CharSequences.
     *
     * @param left the first CharSequence
     * @param right the second CharSequence
     * @return the similarity score between two CharSequences
     */
    R apply(CharSequence left, CharSequence right);

}
