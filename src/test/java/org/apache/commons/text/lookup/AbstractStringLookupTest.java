/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */

package org.apache.commons.text.lookup;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

/**
 * Tests forwarding methods provided by {@link AbstractStringLookup}.
 */
public class AbstractStringLookupTest {

    private static class TestStringLookup extends AbstractStringLookup {

        @Override
        public String lookup(final String key) {
            // noop
            return null;
        }

    }

    @SuppressWarnings("deprecation")
    @Test
    public void testForwarding_substringAfter() {
        assertEquals(StringUtils.substringAfterLast("abc", 'a'), new TestStringLookup().substringAfterLast("abc", 'a'));
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testForwarding_substringAfterChar() {
        assertEquals(StringUtils.substringAfter("abc", 'a'), new TestStringLookup().substringAfter("abc", 'a'));
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testForwarding_substringAfterString() {
        assertEquals(StringUtils.substringAfter("abc", "a"), new TestStringLookup().substringAfter("abc", "a"));
    }
}
