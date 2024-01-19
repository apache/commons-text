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

import java.util.Map;

/**
 * Measures the cosine distance between two character sequences.
 *
 * <p>It utilizes the {@link CosineSimilarity} to compute the distance. Character sequences
 * are converted into vectors through a simple tokenizer that works with a regular expression
 * to split words in a sentence.</p>
 *
 * <p>
 * For further explanation about Cosine Similarity and Cosine Distance, refer to
 * https://en.wikipedia.org/wiki/Cosine_similarity.
 * </p>
 *
 * @since 1.0
 * @see CosineSimilarity
 */
public class CosineDistance implements EditDistance<Double> {

    @Override
    public Double apply(final CharSequence left, final CharSequence right) {
        final CharSequence[] leftTokens = RegexTokenizer.INSTANCE.apply(left);
        final CharSequence[] rightTokens = RegexTokenizer.INSTANCE.apply(right);

        final Map<CharSequence, Integer> leftVector = Counter.of(leftTokens);
        final Map<CharSequence, Integer> rightVector = Counter.of(rightTokens);
        final double similarity = CosineSimilarity.INSTANCE.cosineSimilarity(leftVector, rightVector);
        return 1.0 - similarity;
    }

}
