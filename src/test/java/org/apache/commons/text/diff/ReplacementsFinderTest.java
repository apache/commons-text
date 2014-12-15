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
import static org.junit.Assert.assertEquals;
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
    private String left;
    private String right;
    private int skipped;
    private Character[] from;
    private Character[] to;
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
    public ReplacementsFinderTest(String left, String right, int skipped,
            Character[] from, Character[] to) {
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
        assertEquals("Skipped characters do not match", skipped, handler.getSkipped());
        assertArrayEquals("From characters do not match", from,
                handler.getFrom().toArray(new Character[0]));
        assertArrayEquals("To characters do not match", to,
                handler.getTo().toArray(new Character[0]));
    }
    // Helper RecplacementsHandler implementation for testing
    private class SimpleHandler implements ReplacementsHandler<Character> {
        private int skipped;
        private List<Character> from;
        private List<Character> to;
        public SimpleHandler() {
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
        public void handleReplacement(int skipped, List<Character> from, List<Character> to) {
            this.skipped += skipped;
            this.from.addAll(from);
            this.to.addAll(to);
        }
    }
}
