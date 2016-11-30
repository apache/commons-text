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
 * <p>It utilizes the CosineSimilarity to compute the distance. Character sequences
 * are converted into vectors through a simple tokenizer that works with </p>
 */
public class CosineDistance implements EditDistance<Double> {
    /**
     * Tokenizer used to convert the character sequence into a vector.
     */
    private final Tokenizer<CharSequence> tokenizer = new RegexTokenizer();
    /**
     * Cosine similarity.
     */
    private final CosineSimilarity cosineSimilarity = new CosineSimilarity();

    @Override
    public Double apply(final CharSequence left, final CharSequence right) {
        final CharSequence[] leftTokens = tokenizer.tokenize(left);
        final CharSequence[] rightTokens = tokenizer.tokenize(right);

        final Map<CharSequence, Integer> leftVector = Counter.of(leftTokens);
        final Map<CharSequence, Integer> rightVector = Counter.of(rightTokens);
        final double similarity = cosineSimilarity.cosineSimilarity(leftVector, rightVector);
        return 1.0 - similarity;
    }

}
