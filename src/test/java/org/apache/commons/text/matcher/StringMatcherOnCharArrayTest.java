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
public class StringMatcherOnCharArrayTest {

    private static final char[] INPUT1 = "0,1\t2 3\n\r\f\u0000'\"".toCharArray();

    private static final char[] INPUT2 = "abcdef".toCharArray();

    private static final int INPUT2_LENGTH = INPUT2.length;

    private void checkAndMatcher_char(final StringMatcher matcher) {
        assertThat(matcher.isMatch(INPUT2, 0, 0, INPUT2_LENGTH)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 1, 0, INPUT2_LENGTH)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 2, 0, INPUT2_LENGTH)).isEqualTo(3);
        assertThat(matcher.isMatch(INPUT2, 3, 0, INPUT2_LENGTH)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 4, 0, INPUT2_LENGTH)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 5, 0, INPUT2_LENGTH)).isEqualTo(0);
        //
        assertThat(matcher.isMatch(INPUT2, 0)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 1)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 2)).isEqualTo(3);
        assertThat(matcher.isMatch(INPUT2, 3)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 4)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 5)).isEqualTo(0);
    }

    @Test
    public void testAndMatcher_char() {
        final StringMatcher matcher1 = StringMatcherFactory.INSTANCE.andMatcher(
            StringMatcherFactory.INSTANCE.charMatcher('c'), StringMatcherFactory.INSTANCE.stringMatcher("de"));
        assertEquals(3, matcher1.size());
        checkAndMatcher_char(matcher1);
        //
        final StringMatcher matcher2 = StringMatcherFactory.INSTANCE.andMatcher(null,
            StringMatcherFactory.INSTANCE.charMatcher('c'), null, StringMatcherFactory.INSTANCE.stringMatcher("de"),
            null);
        assertEquals(3, matcher2.size());
        checkAndMatcher_char(matcher2);
    }

    @Test
    public void testCharMatcher_char() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.charMatcher('c');
        assertEquals(1, matcher.size());
        //
        assertThat(matcher.isMatch(INPUT2, 0, 0, INPUT2_LENGTH)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 1, 0, INPUT2_LENGTH)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 2, 0, INPUT2_LENGTH)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT2, 3, 0, INPUT2_LENGTH)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 4, 0, INPUT2_LENGTH)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 5, 0, INPUT2_LENGTH)).isEqualTo(0);
        //
        assertThat(matcher.isMatch(INPUT2, 0)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 1)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 2)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT2, 3)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 4)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 5)).isEqualTo(0);
    }

    @Test
    public void testCharSetMatcher_charArray() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.charSetMatcher("ace".toCharArray());
        assertEquals(1, matcher.size());
        //
        assertThat(matcher.isMatch(INPUT2, 0, 0, INPUT2_LENGTH)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT2, 1, 0, INPUT2_LENGTH)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 2, 0, INPUT2_LENGTH)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT2, 3, 0, INPUT2_LENGTH)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 4, 0, INPUT2_LENGTH)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT2, 5, 0, INPUT2_LENGTH)).isEqualTo(0);
        //
        assertThat(matcher.isMatch(INPUT2, 0)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT2, 1)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 2)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT2, 3)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 4)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT2, 5)).isEqualTo(0);
        //
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
        //
        assertThat(matcher.isMatch(INPUT2, 0, 0, INPUT2_LENGTH)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT2, 1, 0, INPUT2_LENGTH)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 2, 0, INPUT2_LENGTH)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT2, 3, 0, INPUT2_LENGTH)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 4, 0, INPUT2_LENGTH)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT2, 5, 0, INPUT2_LENGTH)).isEqualTo(0);
        //
        assertThat(matcher.isMatch(INPUT2, 0)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT2, 1)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 2)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT2, 3)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 4)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT2, 5)).isEqualTo(0);
        //
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
        //
        assertThat(matcher.isMatch(INPUT1, 0, 0, INPUT1.length)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 1, 0, INPUT1.length)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT1, 2, 0, INPUT1.length)).isEqualTo(0);
        //
        assertThat(matcher.isMatch(INPUT1, 0)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 1)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT1, 2)).isEqualTo(0);
    }

    @Test
    public void testDoubleQuoteMatcher() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.doubleQuoteMatcher();
        assertEquals(1, matcher.size());
        assertThat(StringMatcherFactory.INSTANCE.doubleQuoteMatcher()).isSameAs(matcher);
        //
        assertThat(matcher.isMatch(INPUT1, 11, 0, INPUT1.length)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 12, 0, INPUT1.length)).isEqualTo(1);
        //
        assertThat(matcher.isMatch(INPUT1, 11)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 12)).isEqualTo(1);
    }

    @Test
    public void testMatcherIndices() {
        // remember that the API contract is tight for the isMatch() method
        // all the onus is on the caller, so invalid inputs are not
        // the concern of StringMatcher, and are not bugs
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.stringMatcher("bc");
        assertEquals(2, matcher.size());
        assertThat(matcher.isMatch(INPUT2, 1, 1, INPUT2_LENGTH)).isEqualTo(2);
        assertThat(matcher.isMatch(INPUT2, 1, 0, 3)).isEqualTo(2);
        assertThat(matcher.isMatch(INPUT2, 1, 0, 2)).isEqualTo(0);
    }

    @Test
    public void testNoneMatcher() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.noneMatcher();
        assertEquals(0, matcher.size());
        assertThat(StringMatcherFactory.INSTANCE.noneMatcher()).isSameAs(matcher);
        //
        assertThat(matcher.isMatch(INPUT1, 0, 0, INPUT1.length)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 1, 0, INPUT1.length)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 2, 0, INPUT1.length)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 3, 0, INPUT1.length)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 4, 0, INPUT1.length)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 5, 0, INPUT1.length)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 6, 0, INPUT1.length)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 7, 0, INPUT1.length)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 8, 0, INPUT1.length)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 9, 0, INPUT1.length)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 10, 0, INPUT1.length)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 11, 0, INPUT1.length)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 12, 0, INPUT1.length)).isEqualTo(0);
        //
        assertThat(matcher.isMatch(INPUT1, 0)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 1)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 2)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 3)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 4)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 5)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 6)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 7)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 8)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 9)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 10)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 11)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 12)).isEqualTo(0);
    }

    @Test
    public void testQuoteMatcher() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.quoteMatcher();
        assertEquals(1, matcher.size());
        assertThat(StringMatcherFactory.INSTANCE.quoteMatcher()).isSameAs(matcher);
        //
        assertThat(matcher.isMatch(INPUT1, 10, 0, INPUT1.length)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 11, 0, INPUT1.length)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT1, 12, 0, INPUT1.length)).isEqualTo(1);
        //
        assertThat(matcher.isMatch(INPUT1, 10)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 11)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT1, 12)).isEqualTo(1);
    }

    @Test
    public void testSingleQuoteMatcher() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.singleQuoteMatcher();
        assertEquals(1, matcher.size());
        assertThat(StringMatcherFactory.INSTANCE.singleQuoteMatcher()).isSameAs(matcher);
        //
        assertThat(matcher.isMatch(INPUT1, 10, 0, INPUT1.length)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 11, 0, INPUT1.length)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT1, 12, 0, INPUT1.length)).isEqualTo(0);
        //
        assertThat(matcher.isMatch(INPUT1, 10)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 11)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT1, 12)).isEqualTo(0);
    }

    @Test
    public void testSpaceMatcher() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.spaceMatcher();
        assertEquals(1, matcher.size());
        assertThat(StringMatcherFactory.INSTANCE.spaceMatcher()).isSameAs(matcher);
        //
        assertThat(matcher.isMatch(INPUT1, 4, 0, INPUT1.length)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 5, 0, INPUT1.length)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT1, 6, 0, INPUT1.length)).isEqualTo(0);
        //
        assertThat(matcher.isMatch(INPUT1, 4)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 5)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT1, 6)).isEqualTo(0);
    }

    @Test
    public void testSplitMatcher() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.splitMatcher();
        assertEquals(1, matcher.size());
        assertThat(StringMatcherFactory.INSTANCE.splitMatcher()).isSameAs(matcher);
        //
        assertThat(matcher.isMatch(INPUT1, 2, 0, INPUT1.length)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 3, 0, INPUT1.length)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT1, 4, 0, INPUT1.length)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 5, 0, INPUT1.length)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT1, 6, 0, INPUT1.length)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 7, 0, INPUT1.length)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT1, 8, 0, INPUT1.length)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT1, 9, 0, INPUT1.length)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT1, 10, 0, INPUT1.length)).isEqualTo(0);
        //
        assertThat(matcher.isMatch(INPUT1, 2)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 3)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT1, 4)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 5)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT1, 6)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 7)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT1, 8)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT1, 9)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT1, 10)).isEqualTo(0);
    }

    private void testStringMatcher_String(final StringMatcher matcher) {
        assertEquals(2, matcher.size());
        //
        assertThat(matcher.isMatch(INPUT2, 0, 0, INPUT2_LENGTH)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 1, 0, INPUT2_LENGTH)).isEqualTo(2);
        assertThat(matcher.isMatch(INPUT2, 2, 0, INPUT2_LENGTH)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 3, 0, INPUT2_LENGTH)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 4, 0, INPUT2_LENGTH)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 5, 0, INPUT2_LENGTH)).isEqualTo(0);
        //
        assertThat(matcher.isMatch(INPUT2, 0)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 1)).isEqualTo(2);
        assertThat(matcher.isMatch(INPUT2, 2)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 3)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 4)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT2, 5)).isEqualTo(0);
        //
        assertThat(StringMatcherFactory.INSTANCE.stringMatcher(""))
            .isSameAs(StringMatcherFactory.INSTANCE.noneMatcher());
        assertThat(StringMatcherFactory.INSTANCE.stringMatcher((String) null))
            .isSameAs(StringMatcherFactory.INSTANCE.noneMatcher());
    }

    @Test
    public void testStringMatcher_String_fromChars() {
        testStringMatcher_String(StringMatcherFactory.INSTANCE.stringMatcher('b', 'c'));
        testStringMatcher_String(StringMatcherFactory.INSTANCE.stringMatcher(new char[] {'b', 'c'}));
    }

    @Test
    public void testStringMatcher_String_fromString() {
        testStringMatcher_String(StringMatcherFactory.INSTANCE.stringMatcher("bc"));
    }

    // -----------------------------------------------------------------------
    @Test
    public void testTabMatcher() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.tabMatcher();
        assertEquals(1, matcher.size());
        assertThat(StringMatcherFactory.INSTANCE.tabMatcher()).isSameAs(matcher);
        //
        assertThat(matcher.isMatch(INPUT1, 2, 0, INPUT1.length)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 3, 0, INPUT1.length)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT1, 4, 0, INPUT1.length)).isEqualTo(0);
        //
        assertThat(matcher.isMatch(INPUT1, 2)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 3)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT1, 4)).isEqualTo(0);
    }

    @Test
    public void testTrimMatcher() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.trimMatcher();
        assertEquals(1, matcher.size());
        assertThat(StringMatcherFactory.INSTANCE.trimMatcher()).isSameAs(matcher);
        //
        assertThat(matcher.isMatch(INPUT1, 2, 0, INPUT1.length)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 3, 0, INPUT1.length)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT1, 4, 0, INPUT1.length)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 5, 0, INPUT1.length)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT1, 6, 0, INPUT1.length)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 7, 0, INPUT1.length)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT1, 8, 0, INPUT1.length)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT1, 9, 0, INPUT1.length)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT1, 10, 0, INPUT1.length)).isEqualTo(1);
        //
        assertThat(matcher.isMatch(INPUT1, 2)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 3)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT1, 4)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 5)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT1, 6)).isEqualTo(0);
        assertThat(matcher.isMatch(INPUT1, 7)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT1, 8)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT1, 9)).isEqualTo(1);
        assertThat(matcher.isMatch(INPUT1, 10)).isEqualTo(1);
    }

}
