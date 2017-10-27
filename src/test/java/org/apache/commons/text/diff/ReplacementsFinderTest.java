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
import static org.junit.Assert.assertArrayEquals;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
/**
 * Tests for the ReplacementsFinder.
 */
@RunWith(Parameterized.class)
public class ReplacementsFinderTest {
    private SimpleHandler handler = null;
    private final String left;
    private final String right;
    private final int skipped;
    private final Character[] from;
    private final Character[] to;

    @Before
    public void setUp() {
        handler = new SimpleHandler();
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            {
                "branco",
                "blanco",
                1,
                new Character[] {'r'},
                new Character[] {'l'}},
            {
                "test the blocks before you use it",
                "try the blocks before you put it",
                25,
                new Character[] {'e', 's', 't', 's', 'e'},
                new Character[] {'r', 'y', 'p', 't'}
            }
        });
    }

    public ReplacementsFinderTest(final String left, final String right, final int skipped,
            final Character[] from, final Character[] to) {
        this.left = left;
        this.right = right;
        this.skipped = skipped;
        this.from = from;
        this.to = to;
    }

    @Test
    public void testReplacementsHandler() {
        final StringsComparator sc = new StringsComparator(left, right);
        final ReplacementsFinder<Character> replacementFinder = new ReplacementsFinder<>(handler);
        sc.getScript().visit(replacementFinder);
        assertThat(handler.getSkipped()).as("Skipped characters do not match").isEqualTo(skipped);
        assertArrayEquals("From characters do not match", from,
                handler.getFrom().toArray(new Character[0]));
        assertArrayEquals("To characters do not match", to,
                handler.getTo().toArray(new Character[0]));
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
