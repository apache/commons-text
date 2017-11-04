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

import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>
 * Generates random Unicode strings containing the specified number of code points.
 * Instances are created using a builder class, which allows the
 * callers to define the properties of the generator. See the documentation for the
 * {@link Builder} class to see available properties.
 * </p>
 * <pre>
 * // Generates a 20 code point string, using only the letters a-z
 * RandomStringGenerator generator = new RandomStringGenerator.Builder()
 *     .withinRange('a', 'z').build();
 * String randomLetters = generator.generate(20);
 * </pre>
 * <pre>
 * // Using Apache Commons RNG for randomness
 * UniformRandomProvider rng = RandomSource.create(...);
 * // Generates a 20 code point string, using only the letters a-z
 * RandomStringGenerator generator = new RandomStringGenerator.Builder()
 *     .withinRange('a', 'z')
 *     .usingRandom(rng::nextInt) // uses Java 8 syntax
 *     .build();
 * String randomLetters = generator.generate(20);
 * </pre>
 * <p>
 * {@code RandomStringBuilder} instances are thread-safe when using the
 * default random number generator (RNG). If a custom RNG is set by calling the method
 * {@link Builder#usingRandom(TextRandomProvider) Builder.usingRandom(TextRandomProvider)}, thread-safety
 * must be ensured externally.
 * </p>
 * @since 1.1
 */
public final class RandomStringGenerator {

    /**
     * The smallest allowed code point (inclusive).
     */
    private final int minimumCodePoint;

    /**
     * The largest allowed code point (inclusive).
     */
    private final int maximumCodePoint;

    /**
     * Filters for code points.
     */
    private final Set<CharacterPredicate> inclusivePredicates;

    /**
     * The source of randomness for this generator.
     */
    private final TextRandomProvider random;

    /**
     * The source of provided characters.
     */
    private final List<Character> characterList;

    /**
     * Constructs the generator.
     *
     * @param minimumCodePoint
     *            smallest allowed code point (inclusive)
     * @param maximumCodePoint
     *            largest allowed code point (inclusive)
     * @param inclusivePredicates
     *            filters for code points
     * @param random
     *            source of randomness
     * @param characterList list of predefined set of characters.
     */
    private RandomStringGenerator(final int minimumCodePoint, final int maximumCodePoint,
                                  final Set<CharacterPredicate> inclusivePredicates, final TextRandomProvider random,
                                  final List<Character> characterList) {
        this.minimumCodePoint = minimumCodePoint;
        this.maximumCodePoint = maximumCodePoint;
        this.inclusivePredicates = inclusivePredicates;
        this.random = random;
        this.characterList = characterList;
    }

    /**
     * Generates a random number within a range, using a {@link ThreadLocalRandom} instance
     * or the user-supplied source of randomness.
     *
     * @param minInclusive
     *            the minimum value allowed
     * @param maxInclusive
     *            the maximum value allowed
     * @return the random number.
     */
    private int generateRandomNumber(final int minInclusive, final int maxInclusive) {
        if (random != null) {
            return random.nextInt(maxInclusive - minInclusive + 1) + minInclusive;
        }
        return ThreadLocalRandom.current().nextInt(minInclusive, maxInclusive + 1);
    }

    /**
     * Generates a random number within a range, using a {@link ThreadLocalRandom} instance
     * or the user-supplied source of randomness.
     *
     * @param characterList predefined char list.
     * @return the random number.
     */
    private int generateRandomNumber(final List<Character> characterList) {
        final int listSize = characterList.size();
        if (random != null) {
            return String.valueOf(characterList.get(random.nextInt(listSize))).codePointAt(0);
        }
        return String.valueOf(characterList.get(ThreadLocalRandom.current().nextInt(0, listSize))).codePointAt(0);
    }

    /**
     * <p>
     * Generates a random string, containing the specified number of code points.
     * </p>
     * <p>Code points are randomly selected between the minimum and maximum values defined
     * in the generator.
     * Surrogate and private use characters are not returned, although the
     * resulting string may contain pairs of surrogates that together encode a
     * supplementary character.
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
     * @return the generated string
     * @throws IllegalArgumentException
     *             if {@code length < 0}
     */
    public String generate(final int length) {
        if (length == 0) {
            return "";
        }
        Validate.isTrue(length > 0, "Length %d is smaller than zero.", length);

        final StringBuilder builder = new StringBuilder(length);
        long remaining = length;

        do {
            int codePoint;
            if (characterList != null && !characterList.isEmpty()) {
                codePoint = generateRandomNumber(characterList);
            } else {
                codePoint = generateRandomNumber(minimumCodePoint, maximumCodePoint);
            }
            switch (Character.getType(codePoint)) {
            case Character.UNASSIGNED:
            case Character.PRIVATE_USE:
            case Character.SURROGATE:
                continue;
            default:
            }

            if (inclusivePredicates != null) {
                boolean matchedFilter = false;
                for (final CharacterPredicate predicate : inclusivePredicates) {
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
     * Generates a random string, containing between the minimum (inclusive) and the maximum (inclusive)
     * number of code points.
     *
     * @param minLengthInclusive
     *            the minimum (inclusive) number of code points to generate
     * @param maxLengthInclusive
     *            the maximum (inclusive) number of code points to generate
     * @return the generated string
     * @throws IllegalArgumentException
     *             if {@code minLengthInclusive < 0}, or {@code maxLengthInclusive < minLengthInclusive}
     * @see RandomStringGenerator#generate(int)
     * @since 1.2
     */
    public String generate(final int minLengthInclusive, final int maxLengthInclusive) {
        Validate.isTrue(minLengthInclusive >= 0, "Minimum length %d is smaller than zero.", minLengthInclusive);
        Validate.isTrue(minLengthInclusive <= maxLengthInclusive,
                "Maximum length %d is smaller than minimum length %d.", maxLengthInclusive, minLengthInclusive);
        return generate(generateRandomNumber(minLengthInclusive, maxLengthInclusive));
    }

    /**
     * <p>A builder for generating {@code RandomStringGenerator} instances.</p>
     * <p>The behaviour of a generator is controlled by properties set by this
     * builder. Each property has a default value, which can be overridden by
     * calling the methods defined in this class, prior to calling {@link #build()}.</p>
     *
     * <p>All the property setting methods return the {@code Builder} instance to allow for method chaining.</p>
     *
     * <p>The minimum and maximum code point values are defined using {@link #withinRange(int, int)}. The
     * default values are {@code 0} and {@link Character#MAX_CODE_POINT} respectively.</p>
     *
     * <p>The source of randomness can be set using {@link #usingRandom(TextRandomProvider)},
     * otherwise {@link ThreadLocalRandom} is used.</p>
     *
     * <p>The type of code points returned can be filtered using {@link #filteredBy(CharacterPredicate...)},
     * which defines a collection of tests that are applied to the randomly generated code points.
     * The code points will only be included in the result if they pass at least one of the tests.
     * Some commonly used predicates are provided by the {@link CharacterPredicates} enum.</p>
     *
     * <p>This class is not thread safe.</p>
     * @since 1.1
     */
    public static class Builder implements org.apache.commons.text.Builder<RandomStringGenerator> {

        /**
         * The default maximum code point allowed: {@link Character#MAX_CODE_POINT}
         * ({@value}).
         */
        public static final int DEFAULT_MAXIMUM_CODE_POINT = Character.MAX_CODE_POINT;

        /**
         * The default string length produced by this builder: {@value}.
         */
        public static final int DEFAULT_LENGTH = 0;

        /**
         * The default minimum code point allowed: {@value}.
         */
        public static final int DEFAULT_MINIMUM_CODE_POINT = 0;

        /**
         * The minimum code point allowed.
         */
        private int minimumCodePoint = DEFAULT_MINIMUM_CODE_POINT;

        /**
         * The maximum code point allowed.
         */
        private int maximumCodePoint = DEFAULT_MAXIMUM_CODE_POINT;

        /**
         * Filters for code points.
         */
        private Set<CharacterPredicate> inclusivePredicates;

        /**
         * The source of randomness.
         */
        private TextRandomProvider random;

        /**
         * The source of provided characters.
         */
        private List<Character> characterList;

        /**
         * <p>
         * Specifies the minimum and maximum code points allowed in the
         * generated string.
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
         */
        public Builder withinRange(final int minimumCodePoint, final int maximumCodePoint) {
            Validate.isTrue(minimumCodePoint <= maximumCodePoint,
                    "Minimum code point %d is larger than maximum code point %d", minimumCodePoint, maximumCodePoint);
            Validate.isTrue(minimumCodePoint >= 0, "Minimum code point %d is negative", minimumCodePoint);
            Validate.isTrue(maximumCodePoint <= Character.MAX_CODE_POINT,
                    "Value %d is larger than Character.MAX_CODE_POINT.", maximumCodePoint);

            this.minimumCodePoint = minimumCodePoint;
            this.maximumCodePoint = maximumCodePoint;
            return this;
        }

        /**
         * <p>
         * Specifies the array of minimum and maximum char allowed in the
         * generated string.
         * </p>
         *
         * For example:
         * <pre>
         * {@code
         *     char [][] pairs = {{'0','9'}};
         *     char [][] pairs = {{'a','z'}};
         *     char [][] pairs = {{'a','z'},{'0','9'}};
         * }
         * </pre>
         *
         * @param pairs array of characters array, expected is to pass min, max pairs through this arg.
         * @return {@code this}, to allow method chaining.
         */
        public Builder withinRange(final char[] ... pairs) {
            characterList = new ArrayList<>();
            for (final char[] pair :  pairs) {
                Validate.isTrue(pair.length == 2,
                      "Each pair must contain minimum and maximum code point");
                final int minimumCodePoint = pair[0];
                final int maximumCodePoint = pair[1];
                Validate.isTrue(minimumCodePoint <= maximumCodePoint,
                    "Minimum code point %d is larger than maximum code point %d", minimumCodePoint, maximumCodePoint);

                for (int index = minimumCodePoint; index <= maximumCodePoint; index++) {
                    characterList.add((char) index);
                }
            }
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
         */
        public Builder filteredBy(final CharacterPredicate... predicates) {
            if (predicates == null || predicates.length == 0) {
                inclusivePredicates = null;
                return this;
            }

            if (inclusivePredicates == null) {
                inclusivePredicates = new HashSet<>();
            } else {
                inclusivePredicates.clear();
            }

            Collections.addAll(inclusivePredicates, predicates);

            return this;
        }

        /**
         * <p>
         * Overrides the default source of randomness.  It is highly
         * recommended that a random number generator library like
         * <a href="http://commons.apache.org/proper/commons-rng/">Apache Commons RNG</a>
         * be used to provide the random number generation.
         * </p>
         *
         * <p>
         * When using Java 8 or later, {@link TextRandomProvider} is a
         * functional interface and need not be explicitly implemented:
         * </p>
         * <pre>
         * {@code
         *     UniformRandomProvider rng = RandomSource.create(...);
         *     RandomStringGenerator gen = new RandomStringGenerator.Builder()
         *         .usingRandom(rng::nextInt)
         *         // additional builder calls as needed
         *         .build();
         * }
         * </pre>
         *
         * <p>
         * Passing {@code null} to this method will revert to the default source of
         * randomness.
         * </p>
         *
         * @param random
         *            the source of randomness, may be {@code null}
         * @return {@code this}, to allow method chaining
         */
        public Builder usingRandom(final TextRandomProvider random) {
            this.random = random;
            return this;
        }

        /**
         * <p>
         * Limits the characters in the generated string to those who match at
         * supplied list of Character.
         * </p>
         *
         * <p>
         * Passing {@code null} or an empty array to this method will revert to the
         * default behaviour of allowing any character. Multiple calls to this
         * method will replace the previously stored Character.
         * </p>
         *
         * @param chars set of predefined Characters for random string generation
         *            the Character can be, may be {@code null} or empty
         * @return {@code this}, to allow method chaining
         * @since 1.2
         */
        public Builder selectFrom(final char ... chars) {
            characterList = new ArrayList<>();
            for (final char c : chars) {
                characterList.add(c);
            }
            return this;
        }

        /**
         * <p>Builds the {@code RandomStringGenerator} using the properties specified.</p>
         * @return the configured {@code RandomStringGenerator}
         */
        @Override
        public RandomStringGenerator build() {
            return new RandomStringGenerator(minimumCodePoint, maximumCodePoint, inclusivePredicates,
                    random, characterList);
        }
    }
}
