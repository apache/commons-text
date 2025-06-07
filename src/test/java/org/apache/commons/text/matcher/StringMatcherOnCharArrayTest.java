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
package org.apache.commons.text.matcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link StringMatcher}.
 */
public class StringMatcherOnCharArrayTest {

    private static final char[] INPUT1 = "0,1\t2 3\n\r\f\u0000'\"".toCharArray();

    private static final char[] INPUT2 = "abcdef".toCharArray();

    private static final int INPUT2_LENGTH = INPUT2.length;

    private void checkAndMatcher_char(final StringMatcher matcher) {
        assertEquals(0, matcher.isMatch(INPUT2, 0, 0, INPUT2_LENGTH));
        assertEquals(0, matcher.isMatch(INPUT2, 1, 0, INPUT2_LENGTH));
        assertEquals(3, matcher.isMatch(INPUT2, 2, 0, INPUT2_LENGTH));
        assertEquals(0, matcher.isMatch(INPUT2, 3, 0, INPUT2_LENGTH));
        assertEquals(0, matcher.isMatch(INPUT2, 4, 0, INPUT2_LENGTH));
        assertEquals(0, matcher.isMatch(INPUT2, 5, 0, INPUT2_LENGTH));
        //
        assertEquals(0, matcher.isMatch(INPUT2, 0));
        assertEquals(0, matcher.isMatch(INPUT2, 1));
        assertEquals(3, matcher.isMatch(INPUT2, 2));
        assertEquals(0, matcher.isMatch(INPUT2, 3));
        assertEquals(0, matcher.isMatch(INPUT2, 4));
        assertEquals(0, matcher.isMatch(INPUT2, 5));
    }

    @Test
    void testAndMatcher_char() {
        final StringMatcher matcher1 = StringMatcherFactory.INSTANCE.andMatcher(StringMatcherFactory.INSTANCE.charMatcher('c'),
                StringMatcherFactory.INSTANCE.stringMatcher("de"));
        assertEquals(3, matcher1.size());
        checkAndMatcher_char(matcher1);
        //
        final StringMatcher matcher2 = StringMatcherFactory.INSTANCE.andMatcher(null, StringMatcherFactory.INSTANCE.charMatcher('c'), null,
                StringMatcherFactory.INSTANCE.stringMatcher("de"), null);
        assertEquals(3, matcher2.size());
        checkAndMatcher_char(matcher2);
    }

    @Test
    void testCharMatcher_char() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.charMatcher('c');
        assertEquals(1, matcher.size());
        //
        assertEquals(0, matcher.isMatch(INPUT2, 0, 0, INPUT2_LENGTH));
        assertEquals(0, matcher.isMatch(INPUT2, 1, 0, INPUT2_LENGTH));
        assertEquals(1, matcher.isMatch(INPUT2, 2, 0, INPUT2_LENGTH));
        assertEquals(0, matcher.isMatch(INPUT2, 3, 0, INPUT2_LENGTH));
        assertEquals(0, matcher.isMatch(INPUT2, 4, 0, INPUT2_LENGTH));
        assertEquals(0, matcher.isMatch(INPUT2, 5, 0, INPUT2_LENGTH));
        //
        assertEquals(0, matcher.isMatch(INPUT2, 0));
        assertEquals(0, matcher.isMatch(INPUT2, 1));
        assertEquals(1, matcher.isMatch(INPUT2, 2));
        assertEquals(0, matcher.isMatch(INPUT2, 3));
        assertEquals(0, matcher.isMatch(INPUT2, 4));
        assertEquals(0, matcher.isMatch(INPUT2, 5));
    }

    @Test
    void testCharSetMatcher_charArray() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.charSetMatcher("ace".toCharArray());
        assertEquals(1, matcher.size());
        //
        assertEquals(1, matcher.isMatch(INPUT2, 0, 0, INPUT2_LENGTH));
        assertEquals(0, matcher.isMatch(INPUT2, 1, 0, INPUT2_LENGTH));
        assertEquals(1, matcher.isMatch(INPUT2, 2, 0, INPUT2_LENGTH));
        assertEquals(0, matcher.isMatch(INPUT2, 3, 0, INPUT2_LENGTH));
        assertEquals(1, matcher.isMatch(INPUT2, 4, 0, INPUT2_LENGTH));
        assertEquals(0, matcher.isMatch(INPUT2, 5, 0, INPUT2_LENGTH));
        //
        assertEquals(1, matcher.isMatch(INPUT2, 0));
        assertEquals(0, matcher.isMatch(INPUT2, 1));
        assertEquals(1, matcher.isMatch(INPUT2, 2));
        assertEquals(0, matcher.isMatch(INPUT2, 3));
        assertEquals(1, matcher.isMatch(INPUT2, 4));
        assertEquals(0, matcher.isMatch(INPUT2, 5));
        //
        assertSame(StringMatcherFactory.INSTANCE.charSetMatcher(), StringMatcherFactory.INSTANCE.noneMatcher());
        assertSame(StringMatcherFactory.INSTANCE.charSetMatcher((char[]) null), StringMatcherFactory.INSTANCE.noneMatcher());
        assertInstanceOf(AbstractStringMatcher.CharMatcher.class, StringMatcherFactory.INSTANCE.charSetMatcher("a".toCharArray()));
    }

    @Test
    void testCharSetMatcher_String() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.charSetMatcher("ace");
        assertEquals(1, matcher.size());
        //
        assertEquals(1, matcher.isMatch(INPUT2, 0, 0, INPUT2_LENGTH));
        assertEquals(0, matcher.isMatch(INPUT2, 1, 0, INPUT2_LENGTH));
        assertEquals(1, matcher.isMatch(INPUT2, 2, 0, INPUT2_LENGTH));
        assertEquals(0, matcher.isMatch(INPUT2, 3, 0, INPUT2_LENGTH));
        assertEquals(1, matcher.isMatch(INPUT2, 4, 0, INPUT2_LENGTH));
        assertEquals(0, matcher.isMatch(INPUT2, 5, 0, INPUT2_LENGTH));
        //
        assertEquals(1, matcher.isMatch(INPUT2, 0));
        assertEquals(0, matcher.isMatch(INPUT2, 1));
        assertEquals(1, matcher.isMatch(INPUT2, 2));
        assertEquals(0, matcher.isMatch(INPUT2, 3));
        assertEquals(1, matcher.isMatch(INPUT2, 4));
        assertEquals(0, matcher.isMatch(INPUT2, 5));
        //
        assertSame(StringMatcherFactory.INSTANCE.charSetMatcher(""), StringMatcherFactory.INSTANCE.noneMatcher());
        assertSame(StringMatcherFactory.INSTANCE.charSetMatcher((String) null), StringMatcherFactory.INSTANCE.noneMatcher());
        assertInstanceOf(AbstractStringMatcher.CharMatcher.class, StringMatcherFactory.INSTANCE.charSetMatcher("a"));
    }

    @Test
    void testCommaMatcher() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.commaMatcher();
        assertEquals(1, matcher.size());
        assertSame(StringMatcherFactory.INSTANCE.commaMatcher(), matcher);
        //
        assertEquals(0, matcher.isMatch(INPUT1, 0, 0, INPUT1.length));
        assertEquals(1, matcher.isMatch(INPUT1, 1, 0, INPUT1.length));
        assertEquals(0, matcher.isMatch(INPUT1, 2, 0, INPUT1.length));
        //
        assertEquals(0, matcher.isMatch(INPUT1, 0));
        assertEquals(1, matcher.isMatch(INPUT1, 1));
        assertEquals(0, matcher.isMatch(INPUT1, 2));
    }

    @Test
    void testDoubleQuoteMatcher() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.doubleQuoteMatcher();
        assertEquals(1, matcher.size());
        assertSame(StringMatcherFactory.INSTANCE.doubleQuoteMatcher(), matcher);
        //
        assertEquals(0, matcher.isMatch(INPUT1, 11, 0, INPUT1.length));
        assertEquals(1, matcher.isMatch(INPUT1, 12, 0, INPUT1.length));
        //
        assertEquals(0, matcher.isMatch(INPUT1, 11));
        assertEquals(1, matcher.isMatch(INPUT1, 12));
    }

    @Test
    void testMatcherIndices() {
        // remember that the API contract is tight for the isMatch() method
        // all the onus is on the caller, so invalid inputs are not
        // the concern of StringMatcher, and are not bugs
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.stringMatcher("bc");
        assertEquals(2, matcher.size());
        assertEquals(2, matcher.isMatch(INPUT2, 1, 1, INPUT2_LENGTH));
        assertEquals(2, matcher.isMatch(INPUT2, 1, 0, 3));
        assertEquals(0, matcher.isMatch(INPUT2, 1, 0, 2));
    }

    @Test
    void testNoneMatcher() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.noneMatcher();
        assertEquals(0, matcher.size());
        assertSame(StringMatcherFactory.INSTANCE.noneMatcher(), matcher);
        //
        assertEquals(0, matcher.isMatch(INPUT1, 0, 0, INPUT1.length));
        assertEquals(0, matcher.isMatch(INPUT1, 1, 0, INPUT1.length));
        assertEquals(0, matcher.isMatch(INPUT1, 2, 0, INPUT1.length));
        assertEquals(0, matcher.isMatch(INPUT1, 3, 0, INPUT1.length));
        assertEquals(0, matcher.isMatch(INPUT1, 4, 0, INPUT1.length));
        assertEquals(0, matcher.isMatch(INPUT1, 5, 0, INPUT1.length));
        assertEquals(0, matcher.isMatch(INPUT1, 6, 0, INPUT1.length));
        assertEquals(0, matcher.isMatch(INPUT1, 7, 0, INPUT1.length));
        assertEquals(0, matcher.isMatch(INPUT1, 8, 0, INPUT1.length));
        assertEquals(0, matcher.isMatch(INPUT1, 9, 0, INPUT1.length));
        assertEquals(0, matcher.isMatch(INPUT1, 10, 0, INPUT1.length));
        assertEquals(0, matcher.isMatch(INPUT1, 11, 0, INPUT1.length));
        assertEquals(0, matcher.isMatch(INPUT1, 12, 0, INPUT1.length));
        //
        assertEquals(0, matcher.isMatch(INPUT1, 0));
        assertEquals(0, matcher.isMatch(INPUT1, 1));
        assertEquals(0, matcher.isMatch(INPUT1, 2));
        assertEquals(0, matcher.isMatch(INPUT1, 3));
        assertEquals(0, matcher.isMatch(INPUT1, 4));
        assertEquals(0, matcher.isMatch(INPUT1, 5));
        assertEquals(0, matcher.isMatch(INPUT1, 6));
        assertEquals(0, matcher.isMatch(INPUT1, 7));
        assertEquals(0, matcher.isMatch(INPUT1, 8));
        assertEquals(0, matcher.isMatch(INPUT1, 9));
        assertEquals(0, matcher.isMatch(INPUT1, 10));
        assertEquals(0, matcher.isMatch(INPUT1, 11));
        assertEquals(0, matcher.isMatch(INPUT1, 12));
    }

    @Test
    void testQuoteMatcher() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.quoteMatcher();
        assertEquals(1, matcher.size());
        assertSame(StringMatcherFactory.INSTANCE.quoteMatcher(), matcher);
        //
        assertEquals(0, matcher.isMatch(INPUT1, 10, 0, INPUT1.length));
        assertEquals(1, matcher.isMatch(INPUT1, 11, 0, INPUT1.length));
        assertEquals(1, matcher.isMatch(INPUT1, 12, 0, INPUT1.length));
        //
        assertEquals(0, matcher.isMatch(INPUT1, 10));
        assertEquals(1, matcher.isMatch(INPUT1, 11));
        assertEquals(1, matcher.isMatch(INPUT1, 12));
    }

    @Test
    void testSingleQuoteMatcher() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.singleQuoteMatcher();
        assertEquals(1, matcher.size());
        assertSame(StringMatcherFactory.INSTANCE.singleQuoteMatcher(), matcher);
        //
        assertEquals(0, matcher.isMatch(INPUT1, 10, 0, INPUT1.length));
        assertEquals(1, matcher.isMatch(INPUT1, 11, 0, INPUT1.length));
        assertEquals(0, matcher.isMatch(INPUT1, 12, 0, INPUT1.length));
        //
        assertEquals(0, matcher.isMatch(INPUT1, 10));
        assertEquals(1, matcher.isMatch(INPUT1, 11));
        assertEquals(0, matcher.isMatch(INPUT1, 12));
    }

    @Test
    void testSpaceMatcher() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.spaceMatcher();
        assertEquals(1, matcher.size());
        assertSame(StringMatcherFactory.INSTANCE.spaceMatcher(), matcher);
        //
        assertEquals(0, matcher.isMatch(INPUT1, 4, 0, INPUT1.length));
        assertEquals(1, matcher.isMatch(INPUT1, 5, 0, INPUT1.length));
        assertEquals(0, matcher.isMatch(INPUT1, 6, 0, INPUT1.length));
        //
        assertEquals(0, matcher.isMatch(INPUT1, 4));
        assertEquals(1, matcher.isMatch(INPUT1, 5));
        assertEquals(0, matcher.isMatch(INPUT1, 6));
    }

    @Test
    void testSplitMatcher() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.splitMatcher();
        assertEquals(1, matcher.size());
        assertSame(StringMatcherFactory.INSTANCE.splitMatcher(), matcher);
        //
        assertEquals(0, matcher.isMatch(INPUT1, 2, 0, INPUT1.length));
        assertEquals(1, matcher.isMatch(INPUT1, 3, 0, INPUT1.length));
        assertEquals(0, matcher.isMatch(INPUT1, 4, 0, INPUT1.length));
        assertEquals(1, matcher.isMatch(INPUT1, 5, 0, INPUT1.length));
        assertEquals(0, matcher.isMatch(INPUT1, 6, 0, INPUT1.length));
        assertEquals(1, matcher.isMatch(INPUT1, 7, 0, INPUT1.length));
        assertEquals(1, matcher.isMatch(INPUT1, 8, 0, INPUT1.length));
        assertEquals(1, matcher.isMatch(INPUT1, 9, 0, INPUT1.length));
        assertEquals(0, matcher.isMatch(INPUT1, 10, 0, INPUT1.length));
        //
        assertEquals(0, matcher.isMatch(INPUT1, 2));
        assertEquals(1, matcher.isMatch(INPUT1, 3));
        assertEquals(0, matcher.isMatch(INPUT1, 4));
        assertEquals(1, matcher.isMatch(INPUT1, 5));
        assertEquals(0, matcher.isMatch(INPUT1, 6));
        assertEquals(1, matcher.isMatch(INPUT1, 7));
        assertEquals(1, matcher.isMatch(INPUT1, 8));
        assertEquals(1, matcher.isMatch(INPUT1, 9));
        assertEquals(0, matcher.isMatch(INPUT1, 10));
    }

    private void testStringMatcher_String(final StringMatcher matcher) {
        assertEquals(2, matcher.size());
        //
        assertEquals(0, matcher.isMatch(INPUT2, 0, 0, INPUT2_LENGTH));
        assertEquals(2, matcher.isMatch(INPUT2, 1, 0, INPUT2_LENGTH));
        assertEquals(0, matcher.isMatch(INPUT2, 2, 0, INPUT2_LENGTH));
        assertEquals(0, matcher.isMatch(INPUT2, 3, 0, INPUT2_LENGTH));
        assertEquals(0, matcher.isMatch(INPUT2, 4, 0, INPUT2_LENGTH));
        assertEquals(0, matcher.isMatch(INPUT2, 5, 0, INPUT2_LENGTH));
        //
        assertEquals(0, matcher.isMatch(INPUT2, 0));
        assertEquals(2, matcher.isMatch(INPUT2, 1));
        assertEquals(0, matcher.isMatch(INPUT2, 2));
        assertEquals(0, matcher.isMatch(INPUT2, 3));
        assertEquals(0, matcher.isMatch(INPUT2, 4));
        assertEquals(0, matcher.isMatch(INPUT2, 5));
        //
        assertSame(StringMatcherFactory.INSTANCE.stringMatcher(""), StringMatcherFactory.INSTANCE.noneMatcher());
        assertSame(StringMatcherFactory.INSTANCE.stringMatcher((String) null), StringMatcherFactory.INSTANCE.noneMatcher());
    }

    @Test
    void testStringMatcher_String_fromChars() {
        testStringMatcher_String(StringMatcherFactory.INSTANCE.stringMatcher('b', 'c'));
        testStringMatcher_String(StringMatcherFactory.INSTANCE.stringMatcher(new char[] { 'b', 'c' }));
    }

    @Test
    void testStringMatcher_String_fromString() {
        testStringMatcher_String(StringMatcherFactory.INSTANCE.stringMatcher("bc"));
    }

    @Test
    void testTabMatcher() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.tabMatcher();
        assertEquals(1, matcher.size());
        assertSame(StringMatcherFactory.INSTANCE.tabMatcher(), matcher);
        //
        assertEquals(0, matcher.isMatch(INPUT1, 2, 0, INPUT1.length));
        assertEquals(1, matcher.isMatch(INPUT1, 3, 0, INPUT1.length));
        assertEquals(0, matcher.isMatch(INPUT1, 4, 0, INPUT1.length));
        //
        assertEquals(0, matcher.isMatch(INPUT1, 2));
        assertEquals(1, matcher.isMatch(INPUT1, 3));
        assertEquals(0, matcher.isMatch(INPUT1, 4));
    }

    @Test
    void testTrimMatcher() {
        final StringMatcher matcher = StringMatcherFactory.INSTANCE.trimMatcher();
        assertEquals(1, matcher.size());
        assertSame(StringMatcherFactory.INSTANCE.trimMatcher(), matcher);
        //
        assertEquals(0, matcher.isMatch(INPUT1, 2, 0, INPUT1.length));
        assertEquals(1, matcher.isMatch(INPUT1, 3, 0, INPUT1.length));
        assertEquals(0, matcher.isMatch(INPUT1, 4, 0, INPUT1.length));
        assertEquals(1, matcher.isMatch(INPUT1, 5, 0, INPUT1.length));
        assertEquals(0, matcher.isMatch(INPUT1, 6, 0, INPUT1.length));
        assertEquals(1, matcher.isMatch(INPUT1, 7, 0, INPUT1.length));
        assertEquals(1, matcher.isMatch(INPUT1, 8, 0, INPUT1.length));
        assertEquals(1, matcher.isMatch(INPUT1, 9, 0, INPUT1.length));
        assertEquals(1, matcher.isMatch(INPUT1, 10, 0, INPUT1.length));
        //
        assertEquals(0, matcher.isMatch(INPUT1, 2));
        assertEquals(1, matcher.isMatch(INPUT1, 3));
        assertEquals(0, matcher.isMatch(INPUT1, 4));
        assertEquals(1, matcher.isMatch(INPUT1, 5));
        assertEquals(0, matcher.isMatch(INPUT1, 6));
        assertEquals(1, matcher.isMatch(INPUT1, 7));
        assertEquals(1, matcher.isMatch(INPUT1, 8));
        assertEquals(1, matcher.isMatch(INPUT1, 9));
        assertEquals(1, matcher.isMatch(INPUT1, 10));
    }

}
