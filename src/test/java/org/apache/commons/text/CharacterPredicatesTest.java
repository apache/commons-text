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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for {@link CharacterPredicates}.
 */
public class CharacterPredicatesTest {

    @Test
    public void testLetters() throws Exception {
        assertTrue(CharacterPredicates.LETTERS.test('a'));
        assertTrue(CharacterPredicates.LETTERS.test('Z'));

        assertFalse(CharacterPredicates.LETTERS.test('1'));
        assertFalse(CharacterPredicates.LETTERS.test('?'));
        assertFalse(CharacterPredicates.LETTERS.test('@'));
    }

    @Test
    public void testDigits() {
        assertTrue(CharacterPredicates.DIGITS.test('0'));
        assertTrue(CharacterPredicates.DIGITS.test('9'));

        assertFalse(CharacterPredicates.DIGITS.test('-'));
        assertFalse(CharacterPredicates.DIGITS.test('.'));
        assertFalse(CharacterPredicates.DIGITS.test('L'));
    }

    @Test
    public void testArabicNumerals() {
        assertTrue(CharacterPredicates.ARABIC_NUMERALS.test('0'));
        assertTrue(CharacterPredicates.ARABIC_NUMERALS.test('1'));
        assertTrue(CharacterPredicates.ARABIC_NUMERALS.test('9'));

        assertFalse(CharacterPredicates.ARABIC_NUMERALS.test('/'));
        assertFalse(CharacterPredicates.ARABIC_NUMERALS.test(':'));
        assertFalse(CharacterPredicates.ARABIC_NUMERALS.test('a'));
    }

    @Test
    public void testAsciiLowercaseLetters() {
        assertTrue(CharacterPredicates.ASCII_LOWERCASE_LETTERS.test('a'));
        assertTrue(CharacterPredicates.ASCII_LOWERCASE_LETTERS.test('z'));

        assertFalse(CharacterPredicates.ASCII_LOWERCASE_LETTERS.test('9'));
        assertFalse(CharacterPredicates.ASCII_LOWERCASE_LETTERS.test('A'));
        assertFalse(CharacterPredicates.ASCII_LOWERCASE_LETTERS.test('Z'));
        assertFalse(CharacterPredicates.ASCII_LOWERCASE_LETTERS.test('`'));
        assertFalse(CharacterPredicates.ASCII_LOWERCASE_LETTERS.test('{'));
    }

    @Test
    public void testAsciiUppercaseLetters() {
        assertTrue(CharacterPredicates.ASCII_UPPERCASE_LETTERS.test('A'));
        assertTrue(CharacterPredicates.ASCII_UPPERCASE_LETTERS.test('Z'));

        assertFalse(CharacterPredicates.ASCII_UPPERCASE_LETTERS.test('9'));
        assertFalse(CharacterPredicates.ASCII_UPPERCASE_LETTERS.test('@'));
        assertFalse(CharacterPredicates.ASCII_UPPERCASE_LETTERS.test('['));
        assertFalse(CharacterPredicates.ASCII_UPPERCASE_LETTERS.test('a'));
        assertFalse(CharacterPredicates.ASCII_UPPERCASE_LETTERS.test('z'));
    }

    @Test
    public void testAsciiLetters() {
        assertTrue(CharacterPredicates.ASCII_LETTERS.test('a'));
        assertTrue(CharacterPredicates.ASCII_LETTERS.test('z'));
        assertTrue(CharacterPredicates.ASCII_LETTERS.test('A'));
        assertTrue(CharacterPredicates.ASCII_LETTERS.test('Z'));

        assertFalse(CharacterPredicates.ASCII_LETTERS.test('9'));
        assertFalse(CharacterPredicates.ASCII_LETTERS.test('`'));
        assertFalse(CharacterPredicates.ASCII_LETTERS.test('{'));
        assertFalse(CharacterPredicates.ASCII_LETTERS.test('@'));
        assertFalse(CharacterPredicates.ASCII_LETTERS.test('['));
    }

    @Test
    public void testAsciiAlphaNumerals() {
        assertTrue(CharacterPredicates.ASCII_ALPHA_NUMERALS.test('a'));
        assertTrue(CharacterPredicates.ASCII_ALPHA_NUMERALS.test('z'));
        assertTrue(CharacterPredicates.ASCII_ALPHA_NUMERALS.test('A'));
        assertTrue(CharacterPredicates.ASCII_ALPHA_NUMERALS.test('Z'));
        assertTrue(CharacterPredicates.ASCII_ALPHA_NUMERALS.test('0'));
        assertTrue(CharacterPredicates.ASCII_ALPHA_NUMERALS.test('9'));

        assertFalse(CharacterPredicates.ASCII_ALPHA_NUMERALS.test('`'));
        assertFalse(CharacterPredicates.ASCII_ALPHA_NUMERALS.test('{'));
        assertFalse(CharacterPredicates.ASCII_ALPHA_NUMERALS.test('@'));
        assertFalse(CharacterPredicates.ASCII_ALPHA_NUMERALS.test('['));
        assertFalse(CharacterPredicates.ASCII_ALPHA_NUMERALS.test('/'));
        assertFalse(CharacterPredicates.ASCII_ALPHA_NUMERALS.test(':'));
    }
}
