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

import java.util.Objects;

/**
 * Scores the similarity between two {@link CharSequence}s.
 */
final class SimilarityCharacterInput implements SimilarityInput<Character> {

    /**
     * Source.
     */
    private final CharSequence cs;

    SimilarityCharacterInput(final CharSequence cs) {
        if (cs == null) {
            throw new IllegalArgumentException("CharSequence");
        }
        this.cs = cs;
    }

    @Override
    public Character at(final int index) {
        // Character.valueOf caches character <= 127.
        return Character.valueOf(cs.charAt(index));
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SimilarityCharacterInput other = (SimilarityCharacterInput) obj;
        return Objects.equals(cs, other.cs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cs);
    }

    @Override
    public int length() {
        return cs.length();
    }

    @Override
    public String toString() {
        return cs.toString();
    }
}
