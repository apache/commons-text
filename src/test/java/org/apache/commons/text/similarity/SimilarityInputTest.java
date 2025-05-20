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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.text.TextStringBuilder;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link SimilarityInput}.
 */
public class SimilarityInputTest {

    public static final class SimilarityInputFixture implements SimilarityInput<Object> {

        private final CharSequence value;

        public SimilarityInputFixture(final String value) {
            this.value = Objects.requireNonNull(value);
        }

        @Override
        public Object at(final int i) {
            return value.charAt(i);
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
            final SimilarityInputFixture other = (SimilarityInputFixture) obj;
            return Objects.equals(value, other.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public int length() {
            return value.length();
        }

    }

    static SimilarityInput<Object> build(final Class<?> fixtureClass, final String value) {
        if (value == null) {
            return null;
        }
        try {
            // Use the Class' constructor with a single String.
            return SimilarityInput.input((Object) fixtureClass.getConstructor(String.class).newInstance(value));
        } catch (final ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    static Stream<Class<?>> similarityInputs() {
        return Stream.of(SimilarityInputTest.SimilarityInputFixture.class, String.class, StringBuilder.class, StringBuffer.class, TextStringBuilder.class);
    }

    static Stream<Class<?>> similarityInputsEquals() {
        return Stream.of(SimilarityInputTest.SimilarityInputFixture.class, String.class, TextStringBuilder.class);
    }

    @Test
    void testInput() throws Exception {
        final SimilarityInput<Character> input = SimilarityInput.input("a");
        assertEquals(1, input.length());
        assertThrows(IllegalArgumentException.class, () -> SimilarityInput.input(new Object()));
    }

}
