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

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @since 1.7
 */
public class SorensenDicesSimilarity implements SimilarityScore<Double> {

    /**
     * @param left  the first CharSequence, must not be null
     * @param right the second CharSequence, must not be null
     * @return result similarity
     * @throws IllegalArgumentException if either CharSequence input is {@code null}
     */

    @Override
    public Double apply(final CharSequence left, final CharSequence right) {

        if (left == null || right == null) {
            throw new IllegalArgumentException("CharSequences must not be null");
        }

        if (left.equals(right)) {
            return 1d;
        }

        if ("".equals(left) || "".equals(right)) {
            return 0d;
        }

        Set<String> nLeft = new HashSet<String>();
        Set<String> nRight = new HashSet<String>();

        for (int i = 0; i < left.length() - 1; i++) {
            char chr = left.charAt(i);
            char nextChr = left.charAt(i + 1);
            String bi = "" + chr + nextChr;
            nLeft.add(bi);
        }
        for (int j = 0; j < right.length() - 1; j++) {
            char chr = right.charAt(j);
            char nextChr = right.charAt(j + 1);
            String bi = "" + chr + nextChr;
            nRight.add(bi);
        }

        final int total = nLeft.size() + nRight.size();
        final Set<String> union = new HashSet<String>(total);
        union.addAll(nLeft);
        union.addAll(nRight);

        final int intersection = total - union.size();
        return (2.0d * intersection) / total;
    }
}
