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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the WordWiseStringsComparator.
 */
public class WordWiseStringsComparatorTest {
    private List<String[]> before;
    private List<String[]> after;
    private int[] length;
    private int[] lcs;

    @Test
    public void testLength() {
        for (int i = 0; i < before.size(); ++i) {
            final WordWiseStringsComparator comparator = new WordWiseStringsComparator(before.get(i), after.get(i));
            assertThat(comparator.getScript().getModifications()).isEqualTo(length[i]);
        }
    }

    @Test
    public void testLongestCommonSubsequence() {
        for (int i = 0; i < before.size(); ++i) {
            final WordWiseStringsComparator comparator = new WordWiseStringsComparator(before.get(i), after.get(i));
            assertThat(comparator.getScript().getLCSLength()).isEqualTo(lcs[i]);
        }
    }

    @Test
    public void testExecution() {
        for (int i = 0; i < before.size(); ++i) {
            final ExecutionVisitor ev = new ExecutionVisitor();
            new WordWiseStringsComparator(before.get(i), after.get(i)).getScript().visit(ev);
            assertThat(ev.getSequence()).isEqualTo(after.get(i));
        }
    }

    private static class ExecutionVisitor implements CommandVisitor<String> {

        private final ArrayList<String> v;

        ExecutionVisitor() {
            v = new ArrayList<>();
        }

        @Override
        public void visitInsertCommand(final String object) {
            v.add(object);
        }

        @Override
        public void visitKeepCommand(final String object) {
            v.add(object);
        }

        @Override
        public void visitDeleteCommand(final String object) {
            // noop
        }

        public String[] getSequence() {
            return v.toArray(new String[0]);
        }
    }

    @BeforeEach
    public void setUp() {
        before = Arrays.asList(
                new String[]{"there", "is", "a", "bottle", "and", "a", "glass"},
                new String[]{"nematode", "empty", "knowledge"},
                new String[]{"empty", "bottle"},
                new String[]{""},
                new String[]{"different", "string"},
                new String[]{"ABCABBA"},
                new String[]{"glop", "glop"});
        after = Arrays.asList(
                new String[]{"there", "are", "noodles", "and", "chopsticks"},
                new String[]{"empty", "bottle"},
                new String[]{"the", "bottle", "is", "empty"},
                new String[]{""},
                new String[]{"stranger", "stuff"},
                new String[]{"CBABAC"},
                new String[]{"pas", "glop", "pas", "glop"});
        length = new int[]{
                8,
                3,
                4,
                0,
                4,
                2,
                2
        };
        lcs = new int[]{
                2,
                1,
                1,
                1,
                0,
                0,
                2
        };
    }

    @AfterEach
    public void tearDown() {
        before = null;
        after = null;
        length = null;
    }

}
