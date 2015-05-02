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
package org.apache.commons.text.names;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@code ParserOptions}.
 */
public class ParserOptionsTest {

    private ParserOptions p1;

    @Before
    public void setUp() {
        p1 = new ParserOptions();
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testParserOptionsIsImmutableForPrefixes() {
        p1.getPrefixes().add("Invalid entry");
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testParserOptionsIsImmutableForSuffixes() {
        p1.getSuffixes().add("Invalid entry");
    }

    @Test
    public void testParserOptionsUsesDefaultOptions() {
        assertTrue(p1.getPrefixes().contains("van"));
        assertTrue(p1.getPrefixes().contains("san"));
        assertTrue(p1.getPrefixes().contains("bin"));
        assertTrue(p1.getSuffixes().contains("esq"));
        assertTrue(p1.getSuffixes().contains("esquire"));
        assertTrue(p1.getSuffixes().contains("iv"));
    }

    @Test
    public void testParseOptionsShouldBeConfigurable() {
        Set<String> prefixes = new HashSet<String>(Arrays.asList("sr", "nho", "sinho"));
        p1 = new ParserOptions(prefixes, Collections.<String>emptySet());
        assertTrue(p1.getPrefixes().contains("nho"));

        Set<String> suffixes = new HashSet<String>(Arrays.asList("primeiro", "segundo", "neto"));
        p1 = new ParserOptions(Collections.<String>emptySet(), suffixes);
        assertTrue(p1.getSuffixes().contains("neto"));
    }

}
