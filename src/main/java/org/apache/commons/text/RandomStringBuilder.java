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

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * <p>
 * Generates a random Unicode string based on properties defined using a builder
 * pattern.
 * </p>
 * <p>
 * Overriding the default properties is optional, however callers will need to
 * define the length of the output string using {@link #ofLength(int)} to avoid
 * generating an empty string.
 * </p>
 * <p>
 * All the property setting methods return the {@code RandomStringBuilder}
 * instance to allow for method chaining:
 * </p>
 * 
 * <pre>
 * // Generates a 20 code point string, using only the letters a-z
 * String random = new RandomStringBuilder().ofLength(20).withinRange('a','z').build();
 * </pre>
 * 
 * <p>
 * The type of code point returned can be filtered using
 * {@link #filteredBy(CodePointPredicate...)}, which defines a collection of
 * tests that are applied to the randomly generated code points. The code points
 * will only be included in the result if they pass at least one of the tests.
 * Some commonly used predicates are provided (e.g. {@link #LETTERS} or
 * {@link #DIGITS}) and others can be created by implementing
 * {@link CodePointPredicate}.
 * </p>
 * 
 * <pre>
 * // Generates a 10 code point string containing only letters
 * 
 * import static org.apache.commons.text.RandomStringBuilder.LETTERS;
 * ...
 * String random = new RandomStringBuilder().ofLength(10).filteredBy(LETTERS).build();
 * </pre>
 * 
 * <p>
 * A {@code RandomStringBuilder} instance can be used multiple times to generate
 * different random strings, however it cannot safely be shared between threads.
 * </p>
 * 
 * @since 1.0
 */
public class RandomStringBuilder implements Builder<String> {

    /**
     * Default source of randomness
     */
    private static final Random DEFAULT_RANDOM = new Random();

    /**
     * The default string length produced by this builder: {@value}
     * 
     * @since 1.0
     */
    public static final int DEFAULT_LENGTH = 0;

    /**
     * The default minimum code point allowed: {@value}
     * 
     * @since 1.0
     */
    public static final int DEFAULT_MINIMUM_CODE_POINT = 0;

    /**
     * The default maximum code point allowed: {@link Character#MAX_CODE_POINT}
     * ({@value})
     * 
     * @since 1.0
     */
    public static final int DEFAULT_MAXIMUM_CODE_POINT = Character.MAX_CODE_POINT;

    private int length = 0;
    private int minimumCodePoint = 0;
    private int maximumCodePoint = Character.MAX_CODE_POINT;
    private Set<CodePointPredicate> inclusivePredicates = null;
    private Random random = null;

    /**
     * <p>
     * Constructs a builder with default properties:
     * </p>
     *
     * <ul>
     * <li>Length: {@value #DEFAULT_LENGTH}</li>
     * <li>Minimum code point: {@value #DEFAULT_MINIMUM_CODE_POINT}</li>
     * <li>Maximum code point: {@link Character#MAX_CODE_POINT}</li>
     * <li>Default source of randomness</li>
     * <li>No character filters</li>
     * </ul>
     * 
     * @since 1.0
     */
    public RandomStringBuilder() {
    }

    /**
     * <p>
     * Specifies how many code points to generate in the random string.
     * </p>
     * <p>
     * Note: the number of {@code char} code units generated will exceed
     * {@code length} if the string contains supplementary characters. See the
     * {@link Character} documentation to understand how Java stores Unicode
     * values.
     * </p>
     * 
     * @param length
     *            the number of code points to generate
     * @return {@code this}, to allow method chaining
     * @throws IllegalArgumentException
     *             if {@code length < 0}
     * @since 1.0
     */
    public RandomStringBuilder ofLength(final int length) {
        if (length < 0) {
            throw new IllegalArgumentException(String.format("Length %d is smaller than zero.", length));
        }

        this.length = length;
        return this;
    }

    /**
     * <p>
     * Specifies the minimum and maximum code points allowed in the generated
     * string.
     * </p>
     * 
     * @param minimumCodePoint
     *            the smallest code point allowed (inclusive)
     * @param maximumCodePoint
     *            the largest code point allowed (inclusive)
     * @return {@code this}, to allow method chaining
     * @throws IllegalArgumentException
     *             if {@code maximumCodePoint >}
     *             {@link Character#MAX_CODE_POINT}
     * @throws IllegalArgumentException
     *             if {@code minimumCodePoint < 0}
     * @throws IllegalArgumentException
     *             if {@code minimumCodePoint > maximumCodePoint}
     * @since 1.0
     */
    public RandomStringBuilder withinRange(final int minimumCodePoint, final int maximumCodePoint) {
        if (minimumCodePoint > maximumCodePoint) {
            throw new IllegalArgumentException(String.format(
                    "Minimum code point %d is larger than maximum code point %d", minimumCodePoint, maximumCodePoint));
        }
        if (minimumCodePoint < 0) {
            throw new IllegalArgumentException(String.format("Minimum code point %d is negative", minimumCodePoint));
        }
        if (maximumCodePoint > Character.MAX_CODE_POINT) {
            throw new IllegalArgumentException(
                    String.format("Value %d is larger than Character.MAX_CODE_POINT.", maximumCodePoint));
        }

        this.minimumCodePoint = minimumCodePoint;
        this.maximumCodePoint = maximumCodePoint;
        return this;
    }

    /**
     * <p>
     * Overrides the default source of randomness.
     * </p>
     * 
     * <p>
     * Passing {@code null} to this method will revert to the default source of
     * randomness.
     * </p>
     * 
     * @param random
     *            the source of randomness, may be {@code null}
     * @return {@code this}, to allow method chaining
     * @since 1.0
     */
    public RandomStringBuilder usingRandom(final Random random) {
        this.random = random;
        return this;
    }

    /**
     * <p>
     * Limits the characters in the generated string to those that match at
     * least one of the predicates supplied.
     * </p>
     * 
     * <p>
     * Passing {@code null} or an empty array to this method will revert to the
     * default behaviour of allowing any character. Multiple calls to this
     * method will replace the previously stored predicates.
     * </p>
     * 
     * @param predicates
     *            the predicates, may be {@code null} or empty
     * @return {@code this}, to allow method chaining
     * @since 1.0
     */
    public RandomStringBuilder filteredBy(final CodePointPredicate... predicates) {
        if (predicates == null || predicates.length == 0) {
            inclusivePredicates = null;
            return this;
        }

        if (inclusivePredicates == null) {
            inclusivePredicates = new HashSet<>();
        } else {
            inclusivePredicates.clear();
        }

        for (CodePointPredicate predicate : predicates) {
            inclusivePredicates.add(predicate);
        }

        return this;
    }

    /**
     * <p>
     * Generates a random string using the settings defined in this builder.
     * Code points are randomly selected between the minimum and maximum values.
     * Surrogate and private use characters are not returned, although the
     * resulting string may contain pairs of surrogates that together encode a
     * supplementary character.
     * </p>
     * 
     * <p>
     * A static {@code Random} instance is used if an alternative wasn't
     * provided via {@link #usingRandom(Random)}.
     * </p>
     * 
     * @return the randomly generated string
     * @since 1.0
     */
    @Override
    public String build() {
        if (length == 0) {
            return "";
        }

        if (random == null) {
            random = DEFAULT_RANDOM;
        }

        final StringBuilder builder = new StringBuilder(length);
        long remaining = length;

        do {
            int codePoint = random.nextInt(maximumCodePoint - minimumCodePoint + 1) + minimumCodePoint;

            switch (Character.getType(codePoint)) {
            case Character.UNASSIGNED:
            case Character.PRIVATE_USE:
            case Character.SURROGATE:
                continue;
            }

            if (inclusivePredicates != null) {
                boolean matchedFilter = false;
                for (CodePointPredicate predicate : inclusivePredicates) {
                    if (predicate.test(codePoint)) {
                        matchedFilter = true;
                        break;
                    }
                }
                if (!matchedFilter) {
                    continue;
                }
            }

            builder.appendCodePoint(codePoint);
            remaining--;

        } while (remaining != 0);

        return builder.toString();
    }

    /**
     * A predicate for selecting code points.
     * 
     * @since 1.0
     */
    public static interface CodePointPredicate {
        /**
         * Tests the code point with this predicate.
         * 
         * @param codePoint
         *            the code point to test
         * @return {@code true} if the code point matches the predicate,
         *         {@code false} otherwise
         * @since 1.0
         */
        boolean test(int codePoint);
    }

    /**
     * Tests code points against {@link Character#isLetter(int)}.
     * 
     * @since 1.0
     */
    public static final CodePointPredicate LETTERS = new LetterPredicate();

    /**
     * Tests code points against {@link Character#isDigit(int)}.
     * 
     * @since 1.0
     */
    public static final CodePointPredicate DIGITS = new DigitPredicate();

    /**
     * Tests whether code points are letters.
     */
    private static final class LetterPredicate implements CodePointPredicate {
        @Override
        public boolean test(int codePoint) {
            return Character.isLetter(codePoint);
        }
    }

    /**
     * Tests whether code points are digits.
     */
    private static final class DigitPredicate implements CodePointPredicate {
        @Override
        public boolean test(int codePoint) {
            return Character.isDigit(codePoint);
        }
    };
}
