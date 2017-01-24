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
package org.apache.commons.text.beta;

/**
 * <p>
 * Commonly used implementations of {@link CharacterPredicate}. Per the interface
 * requirements, all implementations are thread safe.
 * </p>
 * 
 * @since 1.0
 */
public enum CharacterPredicates implements CharacterPredicate {

    /**
     * Tests code points against {@link Character#isLetter(int)}
     * 
     * @since 1.0
     */
    LETTERS {
        @Override
        public boolean test(int codePoint) {
            return Character.isLetter(codePoint);
        }
    },

    /**
     * Tests code points against {@link Character#isDigit(int)}.
     * 
     * @since 1.0
     */
    DIGITS {
        @Override
        public boolean test(int codePoint) {
            return Character.isDigit(codePoint);
        }
    }
}
