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

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link StringMatcherFactory}.
 */
public class StringMatcherFactoryTest {

    @Test
    public void test_charMatcher() {
        assertNotNull(StringMatcherFactory.INSTANCE.charMatcher('1'));
    }

    @Test
    public void test_charSetMatcher_char() {
        assertNotNull(StringMatcherFactory.INSTANCE.charSetMatcher('1'));
    }

    @Test
    public void test_charSetMatcher_String() {
        assertNotNull(StringMatcherFactory.INSTANCE.charSetMatcher("1"));
    }

    @Test
    public void test_commaMatcher() {
        assertNotNull(StringMatcherFactory.INSTANCE.commaMatcher());
    }

    @Test
    public void test_doubleQuoteMatcher() {
        assertNotNull(StringMatcherFactory.INSTANCE.doubleQuoteMatcher());
    }

    @Test
    public void test_noneMatcher() {
        assertNotNull(StringMatcherFactory.INSTANCE.noneMatcher());
    }

    @Test
    public void test_quoteMatcher() {
        assertNotNull(StringMatcherFactory.INSTANCE.quoteMatcher());
    }

    @Test
    public void test_singleQuoteMatcher() {
        assertNotNull(StringMatcherFactory.INSTANCE.singleQuoteMatcher());
    }

    @Test
    public void test_spaceMatcher() {
        assertNotNull(StringMatcherFactory.INSTANCE.spaceMatcher());
    }

    @Test
    public void test_splitMatcher() {
        assertNotNull(StringMatcherFactory.INSTANCE.splitMatcher());
    }

    @Test
    public void test_stringMatcher() {
        assertNotNull(StringMatcherFactory.INSTANCE.stringMatcher("1"));
    }

    @Test
    public void test_stringMatcherChars() {
        assertNotNull(StringMatcherFactory.INSTANCE.stringMatcher('1', '2'));
    }

    @Test
    public void test_tabMatcher() {
        assertNotNull(StringMatcherFactory.INSTANCE.charMatcher('1'));
    }

    @Test
    public void test_trimMatcher() {
        assertNotNull(StringMatcherFactory.INSTANCE.charMatcher('1'));
    }

}
