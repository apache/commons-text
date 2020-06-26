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
package org.apache.commons.text.matcher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link StringMatcher}.
 */
public class StringMatcherOnCharSequenceStringTest {

    private static final String BUFFER1 = "0,1\t2 3\n\r\f\u0000'\"";

    private static final String BUFFER2 = "abcdef";

    @Test
    public void testCharMatcher_char() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.charMatcher('c');
        assertEquals(1, matcher.size());
        assertThat(matcher.isMatch(BUFFER2, 0, 0, BUFFER2.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER2, 1, 0, BUFFER2.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER2, 2, 0, BUFFER2.length())).isEqualTo(1);
        assertThat(matcher.isMatch(BUFFER2, 3, 0, BUFFER2.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER2, 4, 0, BUFFER2.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER2, 5, 0, BUFFER2.length())).isEqualTo(0);
        //
        assertThat(matcher.isMatch(BUFFER2, 0)).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER2, 1)).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER2, 2)).isEqualTo(1);
        assertThat(matcher.isMatch(BUFFER2, 3)).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER2, 4)).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER2, 5)).isEqualTo(0);
    }

    @Test
    public void testCharSetMatcher_charArray() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.charSetMatcher("ace".toCharArray());
        assertEquals(1, matcher.size());
        assertThat(matcher.isMatch(BUFFER2, 0, 0, BUFFER2.length())).isEqualTo(1);
        assertThat(matcher.isMatch(BUFFER2, 1, 0, BUFFER2.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER2, 2, 0, BUFFER2.length())).isEqualTo(1);
        assertThat(matcher.isMatch(BUFFER2, 3, 0, BUFFER2.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER2, 4, 0, BUFFER2.length())).isEqualTo(1);
        assertThat(matcher.isMatch(BUFFER2, 5, 0, BUFFER2.length())).isEqualTo(0);
        assertThat(StringMatcherFactory.INSTANCE.charSetMatcher())
                .isSameAs(StringMatcherFactory.INSTANCE.noneMatcher());
        assertThat(StringMatcherFactory.INSTANCE.charSetMatcher((char[]) null))
                .isSameAs(StringMatcherFactory.INSTANCE.noneMatcher());
        assertThat(StringMatcherFactory.INSTANCE
                .charSetMatcher("a".toCharArray()) instanceof AbstractStringMatcher.CharMatcher).isTrue();
    }

    @Test
    public void testCharSetMatcher_String() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.charSetMatcher("ace");
        assertEquals(1, matcher.size());
        assertThat(matcher.isMatch(BUFFER2, 0, 0, BUFFER2.length())).isEqualTo(1);
        assertThat(matcher.isMatch(BUFFER2, 1, 0, BUFFER2.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER2, 2, 0, BUFFER2.length())).isEqualTo(1);
        assertThat(matcher.isMatch(BUFFER2, 3, 0, BUFFER2.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER2, 4, 0, BUFFER2.length())).isEqualTo(1);
        assertThat(matcher.isMatch(BUFFER2, 5, 0, BUFFER2.length())).isEqualTo(0);
        assertThat(StringMatcherFactory.INSTANCE.charSetMatcher(""))
                .isSameAs(StringMatcherFactory.INSTANCE.noneMatcher());
        assertThat(StringMatcherFactory.INSTANCE.charSetMatcher((String) null))
                .isSameAs(StringMatcherFactory.INSTANCE.noneMatcher());
        assertThat(StringMatcherFactory.INSTANCE.charSetMatcher("a") instanceof AbstractStringMatcher.CharMatcher)
                .isTrue();
    }

    @Test
    public void testCommaMatcher() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.commaMatcher();
        assertEquals(1, matcher.size());
        assertThat(StringMatcherFactory.INSTANCE.commaMatcher()).isSameAs(matcher);
        assertThat(matcher.isMatch(BUFFER1, 0, 0, BUFFER1.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER1, 1, 0, BUFFER1.length())).isEqualTo(1);
        assertThat(matcher.isMatch(BUFFER1, 2, 0, BUFFER1.length())).isEqualTo(0);
    }

    @Test
    public void testDoubleQuoteMatcher() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.doubleQuoteMatcher();
        assertEquals(1, matcher.size());
        assertThat(StringMatcherFactory.INSTANCE.doubleQuoteMatcher()).isSameAs(matcher);
        assertThat(matcher.isMatch(BUFFER1, 11, 0, BUFFER1.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER1, 12, 0, BUFFER1.length())).isEqualTo(1);
    }

    @Test
    public void testMatcherIndices() {
        // remember that the API contract is tight for the isMatch() method
        // all the onus is on the caller, so invalid inputs are not
        // the concern of StringMatcher, and are not bugs
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.stringMatcher("bc");
        assertEquals(2, matcher.size());
        assertThat(matcher.isMatch(BUFFER2, 1, 1, BUFFER2.length())).isEqualTo(2);
        assertThat(matcher.isMatch(BUFFER2, 1, 0, 3)).isEqualTo(2);
        assertThat(matcher.isMatch(BUFFER2, 1, 0, 2)).isEqualTo(0);
    }

    @Test
    public void testNoneMatcher() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.noneMatcher();
        assertEquals(0, matcher.size());
        assertThat(StringMatcherFactory.INSTANCE.noneMatcher()).isSameAs(matcher);
        assertThat(matcher.isMatch(BUFFER1, 0, 0, BUFFER1.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER1, 1, 0, BUFFER1.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER1, 2, 0, BUFFER1.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER1, 3, 0, BUFFER1.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER1, 4, 0, BUFFER1.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER1, 5, 0, BUFFER1.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER1, 6, 0, BUFFER1.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER1, 7, 0, BUFFER1.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER1, 8, 0, BUFFER1.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER1, 9, 0, BUFFER1.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER1, 10, 0, BUFFER1.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER1, 11, 0, BUFFER1.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER1, 12, 0, BUFFER1.length())).isEqualTo(0);
    }

    @Test
    public void testQuoteMatcher() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.quoteMatcher();
        assertEquals(1, matcher.size());
        assertThat(StringMatcherFactory.INSTANCE.quoteMatcher()).isSameAs(matcher);
        assertThat(matcher.isMatch(BUFFER1, 10, 0, BUFFER1.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER1, 11, 0, BUFFER1.length())).isEqualTo(1);
        assertThat(matcher.isMatch(BUFFER1, 12, 0, BUFFER1.length())).isEqualTo(1);
    }

    @Test
    public void testSingleQuoteMatcher() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.singleQuoteMatcher();
        assertEquals(1, matcher.size());
        assertThat(StringMatcherFactory.INSTANCE.singleQuoteMatcher()).isSameAs(matcher);
        assertThat(matcher.isMatch(BUFFER1, 10, 0, BUFFER1.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER1, 11, 0, BUFFER1.length())).isEqualTo(1);
        assertThat(matcher.isMatch(BUFFER1, 12, 0, BUFFER1.length())).isEqualTo(0);
    }

    @Test
    public void testSpaceMatcher() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.spaceMatcher();
        assertEquals(1, matcher.size());
        assertThat(StringMatcherFactory.INSTANCE.spaceMatcher()).isSameAs(matcher);
        assertThat(matcher.isMatch(BUFFER1, 4, 0, BUFFER1.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER1, 5, 0, BUFFER1.length())).isEqualTo(1);
        assertThat(matcher.isMatch(BUFFER1, 6, 0, BUFFER1.length())).isEqualTo(0);
    }

    @Test
    public void testSplitMatcher() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.splitMatcher();
        assertEquals(1, matcher.size());
        assertThat(StringMatcherFactory.INSTANCE.splitMatcher()).isSameAs(matcher);
        assertThat(matcher.isMatch(BUFFER1, 2, 0, BUFFER1.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER1, 3, 0, BUFFER1.length())).isEqualTo(1);
        assertThat(matcher.isMatch(BUFFER1, 4, 0, BUFFER1.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER1, 5, 0, BUFFER1.length())).isEqualTo(1);
        assertThat(matcher.isMatch(BUFFER1, 6, 0, BUFFER1.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER1, 7, 0, BUFFER1.length())).isEqualTo(1);
        assertThat(matcher.isMatch(BUFFER1, 8, 0, BUFFER1.length())).isEqualTo(1);
        assertThat(matcher.isMatch(BUFFER1, 9, 0, BUFFER1.length())).isEqualTo(1);
        assertThat(matcher.isMatch(BUFFER1, 10, 0, BUFFER1.length())).isEqualTo(0);
    }

    @Test
    public void testStringMatcher_String() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.stringMatcher("bc");
        assertEquals(2, matcher.size());
        assertThat(matcher.isMatch(BUFFER2, 0, 0, BUFFER2.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER2, 1, 0, BUFFER2.length())).isEqualTo(2);
        assertThat(matcher.isMatch(BUFFER2, 2, 0, BUFFER2.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER2, 3, 0, BUFFER2.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER2, 4, 0, BUFFER2.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER2, 5, 0, BUFFER2.length())).isEqualTo(0);
        assertThat(StringMatcherFactory.INSTANCE.stringMatcher(""))
                .isSameAs(StringMatcherFactory.INSTANCE.noneMatcher());
        assertThat(StringMatcherFactory.INSTANCE.stringMatcher((String) null))
                .isSameAs(StringMatcherFactory.INSTANCE.noneMatcher());
    }

    // -----------------------------------------------------------------------
    @Test
    public void testTabMatcher() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.tabMatcher();
        assertEquals(1, matcher.size());
        assertThat(StringMatcherFactory.INSTANCE.tabMatcher()).isSameAs(matcher);
        assertThat(matcher.isMatch(BUFFER1, 2, 0, BUFFER1.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER1, 3, 0, BUFFER1.length())).isEqualTo(1);
        assertThat(matcher.isMatch(BUFFER1, 4, 0, BUFFER1.length())).isEqualTo(0);
    }

    @Test
    public void testTrimMatcher() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.trimMatcher();
        assertEquals(1, matcher.size());
        assertThat(StringMatcherFactory.INSTANCE.trimMatcher()).isSameAs(matcher);
        assertThat(matcher.isMatch(BUFFER1, 2, 0, BUFFER1.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER1, 3, 0, BUFFER1.length())).isEqualTo(1);
        assertThat(matcher.isMatch(BUFFER1, 4, 0, BUFFER1.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER1, 5, 0, BUFFER1.length())).isEqualTo(1);
        assertThat(matcher.isMatch(BUFFER1, 6, 0, BUFFER1.length())).isEqualTo(0);
        assertThat(matcher.isMatch(BUFFER1, 7, 0, BUFFER1.length())).isEqualTo(1);
        assertThat(matcher.isMatch(BUFFER1, 8, 0, BUFFER1.length())).isEqualTo(1);
        assertThat(matcher.isMatch(BUFFER1, 9, 0, BUFFER1.length())).isEqualTo(1);
        assertThat(matcher.isMatch(BUFFER1, 10, 0, BUFFER1.length())).isEqualTo(1);
    }

}
