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
package org.apache.commons.text.diff;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the ReplacementsFinder.
 */
public class ReplacementsFinderTest {

    private SimpleHandler handler = null;

    @BeforeEach
    public void setUp() {
        handler = new SimpleHandler();
    }

    public static Stream<Arguments> parameters() {
        return Stream.of(
            Arguments.of(
                "branco",
                "blanco",
                1,
                new Character[] {'r'},
                new Character[] {'l'}),
            Arguments.of(
                "test the blocks before you use it",
                "try the blocks before you put it",
                25,
                new Character[] {'e', 's', 't', 's', 'e'},
                new Character[] {'r', 'y', 'p', 't'}
            ));
    }

    @ParameterizedTest
    @MethodSource("parameters")
    public void testReplacementsHandler(String left, String right, int skipped, Character[] from, Character[] to) {
        final StringsComparator sc = new StringsComparator(left, right);
        final ReplacementsFinder<Character> replacementFinder = new ReplacementsFinder<>(handler);
        sc.getScript().visit(replacementFinder);
        assertThat(handler.getSkipped()).as("Skipped characters do not match").isEqualTo(skipped);
        assertArrayEquals(handler.getFrom().toArray(new Character[0]), from, "From characters do not match");
        assertArrayEquals(to, handler.getTo().toArray(new Character[0]), "To characters do not match");
    }

    // Helper RecplacementsHandler implementation for testing
    private class SimpleHandler implements ReplacementsHandler<Character> {
        private int skipped;
        private final List<Character> from;
        private final List<Character> to;
        SimpleHandler() {
            skipped = 0;
            from = new ArrayList<>();
            to = new ArrayList<>();
        }
        public int getSkipped() {
            return skipped;
        }
        public List<Character> getFrom() {
            return from;
        }
        public List<Character> getTo() {
            return to;
        }
        @Override
        public void handleReplacement(final int skipped, final List<Character> from, final List<Character> to) {
            this.skipped += skipped;
            this.from.addAll(from);
            this.to.addAll(to);
        }
    }
}
