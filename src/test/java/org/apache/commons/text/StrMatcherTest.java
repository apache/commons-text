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
package org.apache.commons.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link StrMatcher}.
 *
 * @deprecated This class will be removed in 2.0.
 */
@Deprecated
class StrMatcherTest  {

    private static final char[] BUFFER1 = "0,1\t2 3\n\r\f\u0000'\"".toCharArray();

    private static final char[] BUFFER2 = "abcdef".toCharArray();

    static void assertStrMatcherImpl(final String internalSimpleName, final StrMatcher obj) {
        assertEquals(StrMatcher.class.getName() + "$" + internalSimpleName, obj.getClass().getName());
    }

    static void assertStrMatcherPrefixImpl(final String internalSimpleName, final StrSubstitutor obj) {
        assertStrMatcherImpl(internalSimpleName, obj.getVariablePrefixMatcher());
    }

    static void assertStrMatcherSuffixImpl(final String internalSimpleName, final StrSubstitutor obj) {
        assertStrMatcherImpl(internalSimpleName, obj.getVariableSuffixMatcher());
    }

    @Test
    void testCharMatcher_char() {
        final StrMatcher matcher = StrMatcher.charMatcher('c');
        assertEquals(0, matcher.isMatch(BUFFER2, 0));
        assertEquals(0, matcher.isMatch(BUFFER2, 1));
        assertEquals(1, matcher.isMatch(BUFFER2, 2));
        assertEquals(0, matcher.isMatch(BUFFER2, 3));
        assertEquals(0, matcher.isMatch(BUFFER2, 4));
        assertEquals(0, matcher.isMatch(BUFFER2, 5));
    }

    @Test
    void testCharSetMatcher_charArray() {
        final StrMatcher matcher = StrMatcher.charSetMatcher("ace".toCharArray());
        assertEquals(1, matcher.isMatch(BUFFER2, 0));
        assertEquals(0, matcher.isMatch(BUFFER2, 1));
        assertEquals(1, matcher.isMatch(BUFFER2, 2));
        assertEquals(0, matcher.isMatch(BUFFER2, 3));
        assertEquals(1, matcher.isMatch(BUFFER2, 4));
        assertEquals(0, matcher.isMatch(BUFFER2, 5));
        assertSame(StrMatcher.charSetMatcher(), StrMatcher.noneMatcher());
        assertSame(StrMatcher.charSetMatcher((char[]) null), StrMatcher.noneMatcher());
        StrMatcherTest.assertStrMatcherImpl("CharMatcher", StrMatcher.charSetMatcher("a".toCharArray()));
    }

    @Test
    void testCharSetMatcher_String() {
        final StrMatcher matcher = StrMatcher.charSetMatcher("ace");
        assertEquals(1, matcher.isMatch(BUFFER2, 0));
        assertEquals(0, matcher.isMatch(BUFFER2, 1));
        assertEquals(1, matcher.isMatch(BUFFER2, 2));
        assertEquals(0, matcher.isMatch(BUFFER2, 3));
        assertEquals(1, matcher.isMatch(BUFFER2, 4));
        assertEquals(0, matcher.isMatch(BUFFER2, 5));
        assertSame(StrMatcher.charSetMatcher(""), StrMatcher.noneMatcher());
        assertSame(StrMatcher.charSetMatcher((String) null), StrMatcher.noneMatcher());
        StrMatcherTest.assertStrMatcherImpl("CharMatcher", StrMatcher.charSetMatcher("a"));
    }

    @Test
    void testCommaMatcher() {
        final StrMatcher matcher = StrMatcher.commaMatcher();
        assertSame(StrMatcher.commaMatcher(), matcher);
        assertEquals(0, matcher.isMatch(BUFFER1, 0));
        assertEquals(1, matcher.isMatch(BUFFER1, 1));
        assertEquals(0, matcher.isMatch(BUFFER1, 2));
    }

    @Test
    void testDoubleQuoteMatcher() {
        final StrMatcher matcher = StrMatcher.doubleQuoteMatcher();
        assertSame(StrMatcher.doubleQuoteMatcher(), matcher);
        assertEquals(0, matcher.isMatch(BUFFER1, 11));
        assertEquals(1, matcher.isMatch(BUFFER1, 12));
    }

    @Test
    void testMatcherIndices() {
        // remember that the API contract is tight for the isMatch() method
        // all the onus is on the caller, so invalid inputs are not
        // the concern of StrMatcher, and are not bugs
        final StrMatcher matcher = StrMatcher.stringMatcher("bc");
        assertEquals(2, matcher.isMatch(BUFFER2, 1, 1, BUFFER2.length));
        assertEquals(2, matcher.isMatch(BUFFER2, 1, 0, 3));
        assertEquals(0, matcher.isMatch(BUFFER2, 1, 0, 2));
    }

    @Test
    void testNoneMatcher() {
        final StrMatcher matcher = StrMatcher.noneMatcher();
        assertSame(StrMatcher.noneMatcher(), matcher);
        assertEquals(0, matcher.isMatch(BUFFER1, 0));
        assertEquals(0, matcher.isMatch(BUFFER1, 1));
        assertEquals(0, matcher.isMatch(BUFFER1, 2));
        assertEquals(0, matcher.isMatch(BUFFER1, 3));
        assertEquals(0, matcher.isMatch(BUFFER1, 4));
        assertEquals(0, matcher.isMatch(BUFFER1, 5));
        assertEquals(0, matcher.isMatch(BUFFER1, 6));
        assertEquals(0, matcher.isMatch(BUFFER1, 7));
        assertEquals(0, matcher.isMatch(BUFFER1, 8));
        assertEquals(0, matcher.isMatch(BUFFER1, 9));
        assertEquals(0, matcher.isMatch(BUFFER1, 10));
        assertEquals(0, matcher.isMatch(BUFFER1, 11));
        assertEquals(0, matcher.isMatch(BUFFER1, 12));
    }

    @Test
    void testQuoteMatcher() {
        final StrMatcher matcher = StrMatcher.quoteMatcher();
        assertSame(StrMatcher.quoteMatcher(), matcher);
        assertEquals(0, matcher.isMatch(BUFFER1, 10));
        assertEquals(1, matcher.isMatch(BUFFER1, 11));
        assertEquals(1, matcher.isMatch(BUFFER1, 12));
    }

    @Test
    void testSingleQuoteMatcher() {
        final StrMatcher matcher = StrMatcher.singleQuoteMatcher();
        assertSame(StrMatcher.singleQuoteMatcher(), matcher);
        assertEquals(0, matcher.isMatch(BUFFER1, 10));
        assertEquals(1, matcher.isMatch(BUFFER1, 11));
        assertEquals(0, matcher.isMatch(BUFFER1, 12));
    }

    @Test
    void testSpaceMatcher() {
        final StrMatcher matcher = StrMatcher.spaceMatcher();
        assertSame(StrMatcher.spaceMatcher(), matcher);
        assertEquals(0, matcher.isMatch(BUFFER1, 4));
        assertEquals(1, matcher.isMatch(BUFFER1, 5));
        assertEquals(0, matcher.isMatch(BUFFER1, 6));
    }

    @Test
    void testSplitMatcher() {
        final StrMatcher matcher = StrMatcher.splitMatcher();
        assertSame(StrMatcher.splitMatcher(), matcher);
        assertEquals(0, matcher.isMatch(BUFFER1, 2));
        assertEquals(1, matcher.isMatch(BUFFER1, 3));
        assertEquals(0, matcher.isMatch(BUFFER1, 4));
        assertEquals(1, matcher.isMatch(BUFFER1, 5));
        assertEquals(0, matcher.isMatch(BUFFER1, 6));
        assertEquals(1, matcher.isMatch(BUFFER1, 7));
        assertEquals(1, matcher.isMatch(BUFFER1, 8));
        assertEquals(1, matcher.isMatch(BUFFER1, 9));
        assertEquals(0, matcher.isMatch(BUFFER1, 10));
    }

    @Test
    void testStringMatcher_String() {
        final StrMatcher matcher = StrMatcher.stringMatcher("bc");
        assertEquals(0, matcher.isMatch(BUFFER2, 0));
        assertEquals(2, matcher.isMatch(BUFFER2, 1));
        assertEquals(0, matcher.isMatch(BUFFER2, 2));
        assertEquals(0, matcher.isMatch(BUFFER2, 3));
        assertEquals(0, matcher.isMatch(BUFFER2, 4));
        assertEquals(0, matcher.isMatch(BUFFER2, 5));
        assertSame(StrMatcher.stringMatcher(""), StrMatcher.noneMatcher());
        assertSame(StrMatcher.stringMatcher((String) null), StrMatcher.noneMatcher());
    }

    @Test
    void testTabMatcher() {
        final StrMatcher matcher = StrMatcher.tabMatcher();
        assertSame(StrMatcher.tabMatcher(), matcher);
        assertEquals(0, matcher.isMatch(BUFFER1, 2));
        assertEquals(1, matcher.isMatch(BUFFER1, 3));
        assertEquals(0, matcher.isMatch(BUFFER1, 4));
    }

    @Test
    void testTrimMatcher() {
        final StrMatcher matcher = StrMatcher.trimMatcher();
        assertSame(StrMatcher.trimMatcher(), matcher);
        assertEquals(0, matcher.isMatch(BUFFER1, 2));
        assertEquals(1, matcher.isMatch(BUFFER1, 3));
        assertEquals(0, matcher.isMatch(BUFFER1, 4));
        assertEquals(1, matcher.isMatch(BUFFER1, 5));
        assertEquals(0, matcher.isMatch(BUFFER1, 6));
        assertEquals(1, matcher.isMatch(BUFFER1, 7));
        assertEquals(1, matcher.isMatch(BUFFER1, 8));
        assertEquals(1, matcher.isMatch(BUFFER1, 9));
        assertEquals(1, matcher.isMatch(BUFFER1, 10));
    }

}
