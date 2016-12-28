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
package org.apache.commons.text;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for implementations in the {@link CharacterPredicates} enum.
 */
public class CharacterPredicatesTest {
    @Test
    public void testDigitPredicate() throws Exception {
        String str = new RandomStringGenerator.Builder().filteredBy(CharacterPredicates.DIGITS).build().generate(5000);

        int i = 0;
        do {
            int codePoint = str.codePointAt(i);
            assertTrue(Character.isDigit(codePoint));
            i += Character.charCount(codePoint);
        } while (i < str.length());
    }

    @Test
    public void testLetterPredicate() throws Exception {
        String str = new RandomStringGenerator.Builder().filteredBy(CharacterPredicates.LETTERS).build().generate(5000);

        int i = 0;
        do {
            int codePoint = str.codePointAt(i);
            assertTrue(Character.isLetter(codePoint));
            i += Character.charCount(codePoint);
        } while (i < str.length());
    }
}
