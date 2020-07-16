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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link StringMatcherFactory}.
 */
public class StringMatcherFactoryTest {

    @Test
    public void test_andMatcher() {
        assertNotNull(StringMatcherFactory.INSTANCE.andMatcher(StringMatcherFactory.INSTANCE.charMatcher('1'),
            StringMatcherFactory.INSTANCE.stringMatcher("2")));
        assertNotNull(StringMatcherFactory.INSTANCE.andMatcher(null, StringMatcherFactory.INSTANCE.stringMatcher("2")));
        assertNotNull(StringMatcherFactory.INSTANCE.andMatcher(null, null));
    }

    @Test
    public void test_charMatcher() {
        final StringMatcher charMatcher = StringMatcherFactory.INSTANCE.charMatcher('1');
        assertNotNull(charMatcher);
        assertNotNull(charMatcher.toString());
        assertEquals(1, charMatcher.size());
    }

    @Test
    public void test_charSetMatcher_char() {
        final StringMatcher charSetMatcher = StringMatcherFactory.INSTANCE.charSetMatcher('1');
        assertNotNull(charSetMatcher);
        assertNotNull(charSetMatcher.toString());
        assertEquals(1, charSetMatcher.size());
    }

    @Test
    public void test_charSetMatcher_String() {
        final StringMatcher charSetMatcher = StringMatcherFactory.INSTANCE.charSetMatcher("1");
        assertNotNull(charSetMatcher);
        assertNotNull(charSetMatcher.toString());
        assertEquals(1, charSetMatcher.size());
    }

    @Test
    public void test_commaMatcher() {
        final StringMatcher commaMatcher = StringMatcherFactory.INSTANCE.commaMatcher();
        assertNotNull(commaMatcher);
        assertNotNull(commaMatcher.toString());
        assertEquals(1, commaMatcher.size());
    }

    @Test
    public void test_doubleQuoteMatcher() {
        final StringMatcher doubleQuoteMatcher = StringMatcherFactory.INSTANCE.doubleQuoteMatcher();
        assertNotNull(doubleQuoteMatcher);
        assertNotNull(doubleQuoteMatcher.toString());
        assertEquals(1, doubleQuoteMatcher.size());
    }

    @Test
    public void test_noneMatcher() {
        final StringMatcher noneMatcher = StringMatcherFactory.INSTANCE.noneMatcher();
        assertNotNull(noneMatcher);
        assertNotNull(noneMatcher.toString());
        assertEquals(0, noneMatcher.size());
    }

    @Test
    public void test_quoteMatcher() {
        final StringMatcher quoteMatcher = StringMatcherFactory.INSTANCE.quoteMatcher();
        assertNotNull(quoteMatcher);
        assertNotNull(quoteMatcher.toString());
        assertEquals(1, quoteMatcher.size());
    }

    @Test
    public void test_singleQuoteMatcher() {
        final StringMatcher singleQuoteMatcher = StringMatcherFactory.INSTANCE.singleQuoteMatcher();
        assertNotNull(singleQuoteMatcher);
        assertNotNull(singleQuoteMatcher.toString());
        assertEquals(1, singleQuoteMatcher.size());
    }

    @Test
    public void test_spaceMatcher() {
        final StringMatcher spaceMatcher = StringMatcherFactory.INSTANCE.spaceMatcher();
        assertNotNull(spaceMatcher);
        assertNotNull(spaceMatcher.toString());
        assertEquals(1, spaceMatcher.size());
    }

    @Test
    public void test_splitMatcher() {
        final StringMatcher splitMatcher = StringMatcherFactory.INSTANCE.splitMatcher();
        assertNotNull(splitMatcher);
        assertNotNull(splitMatcher.toString());
        assertEquals(1, splitMatcher.size());
    }

    @Test
    public void test_stringMatcher() {
        final StringMatcher stringMatcher = StringMatcherFactory.INSTANCE.stringMatcher("1");
        assertNotNull(stringMatcher);
        assertNotNull(stringMatcher.toString());
        assertEquals(1, stringMatcher.size());
    }

    @Test
    public void test_stringMatcherChars() {
        final StringMatcher stringMatcher = StringMatcherFactory.INSTANCE.stringMatcher('1', '2');
        assertNotNull(stringMatcher);
        assertNotNull(stringMatcher.toString());
        assertEquals(2, stringMatcher.size());
    }

    @Test
    public void test_tabMatcher() {
        final StringMatcher charMatcher = StringMatcherFactory.INSTANCE.charMatcher('1');
        assertNotNull(charMatcher);
        assertNotNull(charMatcher.toString());
        assertEquals(1, charMatcher.size());
    }

    @Test
    public void test_trimMatcher() {
        final StringMatcher charMatcher = StringMatcherFactory.INSTANCE.charMatcher('1');
        assertNotNull(charMatcher);
        assertNotNull(charMatcher.toString());
        assertEquals(1, charMatcher.size());
    }

}
