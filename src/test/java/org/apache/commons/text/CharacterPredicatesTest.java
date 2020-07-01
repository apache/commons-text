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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CharacterPredicates}.
 */
public class CharacterPredicatesTest {

    @Test
    public void testArabicNumerals() {
        assertThat(CharacterPredicates.ARABIC_NUMERALS.test('0')).isTrue();
        assertThat(CharacterPredicates.ARABIC_NUMERALS.test('1')).isTrue();
        assertThat(CharacterPredicates.ARABIC_NUMERALS.test('9')).isTrue();

        assertThat(CharacterPredicates.ARABIC_NUMERALS.test('/')).isFalse();
        assertThat(CharacterPredicates.ARABIC_NUMERALS.test(':')).isFalse();
        assertThat(CharacterPredicates.ARABIC_NUMERALS.test('a')).isFalse();
    }

    @Test
    public void testAsciiAlphaNumerals() {
        assertThat(CharacterPredicates.ASCII_ALPHA_NUMERALS.test('a')).isTrue();
        assertThat(CharacterPredicates.ASCII_ALPHA_NUMERALS.test('z')).isTrue();
        assertThat(CharacterPredicates.ASCII_ALPHA_NUMERALS.test('A')).isTrue();
        assertThat(CharacterPredicates.ASCII_ALPHA_NUMERALS.test('Z')).isTrue();
        assertThat(CharacterPredicates.ASCII_ALPHA_NUMERALS.test('0')).isTrue();
        assertThat(CharacterPredicates.ASCII_ALPHA_NUMERALS.test('9')).isTrue();

        assertThat(CharacterPredicates.ASCII_ALPHA_NUMERALS.test('`')).isFalse();
        assertThat(CharacterPredicates.ASCII_ALPHA_NUMERALS.test('{')).isFalse();
        assertThat(CharacterPredicates.ASCII_ALPHA_NUMERALS.test('@')).isFalse();
        assertThat(CharacterPredicates.ASCII_ALPHA_NUMERALS.test('[')).isFalse();
        assertThat(CharacterPredicates.ASCII_ALPHA_NUMERALS.test('/')).isFalse();
        assertThat(CharacterPredicates.ASCII_ALPHA_NUMERALS.test(':')).isFalse();
    }

    @Test
    public void testAsciiLetters() {
        assertThat(CharacterPredicates.ASCII_LETTERS.test('a')).isTrue();
        assertThat(CharacterPredicates.ASCII_LETTERS.test('z')).isTrue();
        assertThat(CharacterPredicates.ASCII_LETTERS.test('A')).isTrue();
        assertThat(CharacterPredicates.ASCII_LETTERS.test('Z')).isTrue();

        assertThat(CharacterPredicates.ASCII_LETTERS.test('9')).isFalse();
        assertThat(CharacterPredicates.ASCII_LETTERS.test('`')).isFalse();
        assertThat(CharacterPredicates.ASCII_LETTERS.test('{')).isFalse();
        assertThat(CharacterPredicates.ASCII_LETTERS.test('@')).isFalse();
        assertThat(CharacterPredicates.ASCII_LETTERS.test('[')).isFalse();
    }

    @Test
    public void testAsciiLowercaseLetters() {
        assertThat(CharacterPredicates.ASCII_LOWERCASE_LETTERS.test('a')).isTrue();
        assertThat(CharacterPredicates.ASCII_LOWERCASE_LETTERS.test('z')).isTrue();

        assertThat(CharacterPredicates.ASCII_LOWERCASE_LETTERS.test('9')).isFalse();
        assertThat(CharacterPredicates.ASCII_LOWERCASE_LETTERS.test('A')).isFalse();
        assertThat(CharacterPredicates.ASCII_LOWERCASE_LETTERS.test('Z')).isFalse();
        assertThat(CharacterPredicates.ASCII_LOWERCASE_LETTERS.test('`')).isFalse();
        assertThat(CharacterPredicates.ASCII_LOWERCASE_LETTERS.test('{')).isFalse();
    }

    @Test
    public void testAsciiUppercaseLetters() {
        assertThat(CharacterPredicates.ASCII_UPPERCASE_LETTERS.test('A')).isTrue();
        assertThat(CharacterPredicates.ASCII_UPPERCASE_LETTERS.test('Z')).isTrue();

        assertThat(CharacterPredicates.ASCII_UPPERCASE_LETTERS.test('9')).isFalse();
        assertThat(CharacterPredicates.ASCII_UPPERCASE_LETTERS.test('@')).isFalse();
        assertThat(CharacterPredicates.ASCII_UPPERCASE_LETTERS.test('[')).isFalse();
        assertThat(CharacterPredicates.ASCII_UPPERCASE_LETTERS.test('a')).isFalse();
        assertThat(CharacterPredicates.ASCII_UPPERCASE_LETTERS.test('z')).isFalse();
    }

    @Test
    public void testDigits() {
        assertThat(CharacterPredicates.DIGITS.test('0')).isTrue();
        assertThat(CharacterPredicates.DIGITS.test('9')).isTrue();

        assertThat(CharacterPredicates.DIGITS.test('-')).isFalse();
        assertThat(CharacterPredicates.DIGITS.test('.')).isFalse();
        assertThat(CharacterPredicates.DIGITS.test('L')).isFalse();
    }

    @Test
    public void testLetters() {
        assertThat(CharacterPredicates.LETTERS.test('a')).isTrue();
        assertThat(CharacterPredicates.LETTERS.test('Z')).isTrue();

        assertThat(CharacterPredicates.LETTERS.test('1')).isFalse();
        assertThat(CharacterPredicates.LETTERS.test('?')).isFalse();
        assertThat(CharacterPredicates.LETTERS.test('@')).isFalse();
    }
}
