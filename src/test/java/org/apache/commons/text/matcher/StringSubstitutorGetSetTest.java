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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.text.StringSubstitutor;
import org.junit.Test;

/**
 * Test class for {@link StringSubstitutor}.
 */
public class StringSubstitutorGetSetTest {

    /**
     * Tests get set.
     */
    @Test
    public void testGetSetPrefix() {
        final StringSubstitutor sub = new StringSubstitutor();
        assertTrue(sub.getVariablePrefixMatcher() instanceof AbstractStringMatcher.StringMatcher);
        sub.setVariablePrefix('<');
        assertTrue(sub.getVariablePrefixMatcher() instanceof AbstractStringMatcher.CharMatcher);

        sub.setVariablePrefix("<<");
        assertTrue(sub.getVariablePrefixMatcher() instanceof AbstractStringMatcher.StringMatcher);
        try {
            sub.setVariablePrefix((String) null);
            fail();
        } catch (final IllegalArgumentException ex) {
            // expected
        }
        assertTrue(sub.getVariablePrefixMatcher() instanceof AbstractStringMatcher.StringMatcher);

        final StringMatcher matcher = StringMatcherFactory.INSTANCE.commaMatcher();
        sub.setVariablePrefixMatcher(matcher);
        assertSame(matcher, sub.getVariablePrefixMatcher());
        try {
            sub.setVariablePrefixMatcher((StringMatcher) null);
            fail();
        } catch (final IllegalArgumentException ex) {
            // expected
        }
        assertSame(matcher, sub.getVariablePrefixMatcher());
    }

    /**
     * Tests get set.
     */
    @Test
    public void testGetSetSuffix() {
        final StringSubstitutor sub = new StringSubstitutor();
        assertTrue(sub.getVariableSuffixMatcher() instanceof AbstractStringMatcher.StringMatcher);
        sub.setVariableSuffix('<');
        assertTrue(sub.getVariableSuffixMatcher() instanceof AbstractStringMatcher.CharMatcher);

        sub.setVariableSuffix("<<");
        assertTrue(sub.getVariableSuffixMatcher() instanceof AbstractStringMatcher.StringMatcher);
        try {
            sub.setVariableSuffix((String) null);
            fail();
        } catch (final IllegalArgumentException ex) {
            // expected
        }
        assertTrue(sub.getVariableSuffixMatcher() instanceof AbstractStringMatcher.StringMatcher);

        final StringMatcher matcher = StringMatcherFactory.INSTANCE.commaMatcher();
        sub.setVariableSuffixMatcher(matcher);
        assertSame(matcher, sub.getVariableSuffixMatcher());
        try {
            sub.setVariableSuffixMatcher((StringMatcher) null);
            fail();
        } catch (final IllegalArgumentException ex) {
            // expected
        }
        assertSame(matcher, sub.getVariableSuffixMatcher());
    }

    /**
     * Tests get set.
     */
    @Test
    public void testGetSetValueDelimiter() {
        final StringSubstitutor sub = new StringSubstitutor();
        assertTrue(sub.getValueDelimiterMatcher() instanceof AbstractStringMatcher.StringMatcher);
        sub.setValueDelimiter(':');
        assertTrue(sub.getValueDelimiterMatcher() instanceof AbstractStringMatcher.CharMatcher);

        sub.setValueDelimiter("||");
        assertTrue(sub.getValueDelimiterMatcher() instanceof AbstractStringMatcher.StringMatcher);
        sub.setValueDelimiter((String) null);
        assertNull(sub.getValueDelimiterMatcher());

        final StringMatcher matcher = StringMatcherFactory.INSTANCE.commaMatcher();
        sub.setValueDelimiterMatcher(matcher);
        assertSame(matcher, sub.getValueDelimiterMatcher());
        sub.setValueDelimiterMatcher((StringMatcher) null);
        assertNull(sub.getValueDelimiterMatcher());
    }

}
