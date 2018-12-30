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
/**
 * <p>Provides algorithms for string similarity.</p>
 *
 * <p>The algorithms that implement the EditDistance interface follow the same
 * simple principle: the more similar (closer) strings are, lower is the distance.
 * For example, the words house and hose are closer than house and trousers.</p>
 *
 * <p>The following algorithms are available at the moment:</p>
 *
 * <ul>
 * <li>{@link org.apache.commons.text.similarity.CosineDistance Cosine Distance}</li>
 * <li>{@link org.apache.commons.text.similarity.CosineSimilarity Cosine Similarity}</li>
 * <li>{@link org.apache.commons.text.similarity.FuzzyScore Fuzzy Score}</li>
 * <li>{@link org.apache.commons.text.similarity.HammingDistance Hamming Distance}</li>
 * <li>{@link org.apache.commons.text.similarity.JaroWinklerDistance Jaro-Winkler Distance}</li>
 * <li>{@link org.apache.commons.text.similarity.JaroWinklerSimilarity Jaro-Winkler Similarity}</li>
 * <li>{@link org.apache.commons.text.similarity.LevenshteinDistance Levenshtein Distance}</li>
 * <li>{@link org.apache.commons.text.similarity.LongestCommonSubsequenceDistance
 * Longest Commons Subsequence Distance}</li>
 * </ul>
 *
 * <p>The {@link org.apache.commons.text.similarity.CosineDistance Cosine Distance}
 * utilises a {@link org.apache.commons.text.similarity.RegexTokenizer regular expression tokenizer (\w+)}.
 * And the {@link org.apache.commons.text.similarity.LevenshteinDistance Levenshtein Distance}'s
 * behaviour can be changed to take into consideration a maximum throughput.</p>
 *
 * @since 1.0
 */
package org.apache.commons.text.similarity;
