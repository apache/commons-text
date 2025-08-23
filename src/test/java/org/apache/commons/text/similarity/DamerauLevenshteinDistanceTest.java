/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.text.similarity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DamerauLevenshteinDistanceTest {

    @Test
    void testGetThresholdDirectlyAfterObjectInstantiation() {
        assertNull(LevenshteinDistance.getDefaultInstance().getThreshold());
    }

    @Test
    void testGetThresholdIsCorrect() {
        LevenshteinDistance distance = new LevenshteinDistance(10);

        assertEquals(10, distance.getThreshold());
    }

    @Test
    void testNullInputsThrowUnlimited() {
        DamerauLevenshteinDistance instance = DamerauLevenshteinDistance.getDefaultInstance();

        assertThrows(IllegalArgumentException.class, () -> instance.apply(null, "test"));
        assertThrows(IllegalArgumentException.class, () -> instance.apply("test", null));
        assertThrows(IllegalArgumentException.class, () -> instance.apply(null, SimilarityInput.input("test")));
        assertThrows(IllegalArgumentException.class, () -> instance.apply(SimilarityInput.input("test"), null));
    }

    @Test
    void testNullInputsThrowLimited() {
        DamerauLevenshteinDistance instance = new DamerauLevenshteinDistance(10);

        assertThrows(IllegalArgumentException.class, () -> instance.apply(null, "test"));
        assertThrows(IllegalArgumentException.class, () -> instance.apply("test", null));
        assertThrows(IllegalArgumentException.class, () -> instance.apply(null, SimilarityInput.input("test")));
        assertThrows(IllegalArgumentException.class, () -> instance.apply(SimilarityInput.input("test"), null));
    }

    @Test
    void testInvalidThresholdThrows() {
        assertThrows(IllegalArgumentException.class, () -> new DamerauLevenshteinDistance(-1));
    }

    @ParameterizedTest(name = "DamerauLevenshteinDistance.unlimitedCompare(\"{0}\", \"{1}\") should return {2}")
    @MethodSource("unlimitedDamerauLevenshteinDistanceTestCases")
    void testCalculateDamerauLevenshteinDistance(String left, String right, int expectedDistance) {
        DamerauLevenshteinDistance instance = DamerauLevenshteinDistance.getDefaultInstance();

        int leftRightDistance = instance.apply(left, right);
        int rightLeftDistance = instance.apply(right, left);

        assertEquals(expectedDistance, leftRightDistance);
        assertEquals(expectedDistance, rightLeftDistance);
    }

    @ParameterizedTest(name = "DamerauLevenshteinDistance.unlimitedCompare(\"{0}\", \"{1}\") should return {2} ({3})")
    @MethodSource("unlimitedDamerauLevenshteinDistanceTestCases_SimilarityInput")
    void testCalculateDamerauLevenshteinDistance_SimilarityInput(String left, String right, int expectedDistance, final Class<?> cls) {
        DamerauLevenshteinDistance instance = DamerauLevenshteinDistance.getDefaultInstance();

        SimilarityInput<Object> leftInput = SimilarityInputTest.build(cls, left);
        SimilarityInput<Object> rightInput = SimilarityInputTest.build(cls, right);

        int leftRightDistance = instance.apply(leftInput, rightInput);
        int rightLeftDistance = instance.apply(rightInput, leftInput);

        assertEquals(expectedDistance, leftRightDistance);
        assertEquals(expectedDistance, rightLeftDistance);
    }

    @ParameterizedTest(name = "DamerauLevenshteinDistance.limitedCompare(\"{0}\", \"{1}\") should return {2}")
    @MethodSource("limitedDamerauLevenshteinDistanceTestCases")
    void testCalculateDamerauLevenshteinDistance(String left, String right, int threshold, int expectedDistance) {
        DamerauLevenshteinDistance instance = new DamerauLevenshteinDistance(threshold);

        int leftRightDistance = instance.apply(left, right);
        int rightLeftDistance = instance.apply(right, left);

        assertEquals(expectedDistance, leftRightDistance);
        assertEquals(expectedDistance, rightLeftDistance);
    }

    @ParameterizedTest(name = "DamerauLevenshteinDistance.limitedCompare(\"{0}\", \"{1}\") should return {2}")
    @MethodSource("limitedDamerauLevenshteinDistanceTestCases_SimilarityInput")
    void testCalculateDamerauLevenshteinDistance_SimilarityInput(String left, String right, int threshold, int expectedDistance, final Class<?> cls) {
        DamerauLevenshteinDistance instance = new DamerauLevenshteinDistance(threshold);

        SimilarityInput<Object> leftInput = SimilarityInputTest.build(cls, left);
        SimilarityInput<Object> rightInput = SimilarityInputTest.build(cls, right);

        int leftRightDistance = instance.apply(leftInput, rightInput);
        int rightLeftDistance = instance.apply(rightInput, leftInput);

        assertEquals(expectedDistance, leftRightDistance);
        assertEquals(expectedDistance, rightLeftDistance);
    }

    static Stream<Arguments> unlimitedDamerauLevenshteinDistanceTestCases_SimilarityInput() {
        return SimilarityInputTest.similarityInputs()
                .flatMap(cls -> unlimitedDamerauLevenshteinDistanceTestCases().map(arguments -> {
                    Object[] values = Arrays.copyOf(arguments.get(), arguments.get().length + 1);
                    values[values.length - 1] = cls;
                    return Arguments.of(values);
                }));
    }

    static Stream<Arguments> unlimitedDamerauLevenshteinDistanceTestCases() {
        return Stream.of(
                Arguments.of("", "test", 4),
                Arguments.of("test", "", 4),
                Arguments.of("kitten", "sitting", 3),
                Arguments.of("saturday", "sunday", 3),
                Arguments.of("hello", "world", 4),
                Arguments.of("algorithm", "logarithm", 3),
                Arguments.of("computer", "comptuer", 1),
                Arguments.of("receive", "recieve", 1),
                Arguments.of("programming", "porgramming", 1),
                Arguments.of("test", "tset", 1),
                Arguments.of("example", "exmaple", 1),
                Arguments.of("transform", "transfrom", 1),
                Arguments.of("information", "infromation", 1),
                Arguments.of("development", "developemnt", 1),
                Arguments.of("password", "passwrod", 1),
                Arguments.of("separate", "seperate", 1),
                Arguments.of("definitely", "definately", 1),
                Arguments.of("occurrence", "occurence", 1),
                Arguments.of("necessary", "neccessary", 1),
                Arguments.of("restaurant", "restaraunt", 2),
                Arguments.of("beginning", "begining", 1),
                Arguments.of("government", "goverment", 1),
                Arguments.of("abcdefghijklmnop", "ponmlkjihgfedcba", 15),
                Arguments.of("AAAAAAAAAA", "BBBBBBBBBB", 10),
                Arguments.of("abababababab", "babababababa", 2),
                Arguments.of("supercalifragilisticexpialidocious", "supercalifragilisticexpialidocous", 1),
                Arguments.of("pneumonoultramicroscopicsilicovolcanoconiosiss", "pneumonoultramicroscopicsilicovolcanoconiosis", 1),
                Arguments.of("abcdefg", "gfedcba", 6),
                Arguments.of("xyxyxyxyxy", "yxyxyxyxyx", 2),
                Arguments.of("aaaaabbbbbccccc", "cccccbbbbbaaaaa", 10),
                Arguments.of("thequickbrownfoxjumpsoverthelazydog", "thequickbrownfoxjumpsovrethelazydog", 1),
                Arguments.of("antidisestablishmentarianism", "antidisestablishmentarianisn", 1)
        );
    }

    static Stream<Arguments> limitedDamerauLevenshteinDistanceTestCases_SimilarityInput() {
        return SimilarityInputTest.similarityInputs()
                .flatMap(cls -> limitedDamerauLevenshteinDistanceTestCases().map(arguments -> {
                    Object[] values = Arrays.copyOf(arguments.get(), arguments.get().length + 1);
                    values[values.length - 1] = cls;
                    return Arguments.of(values);
                }));
    }

    static Stream<Arguments> limitedDamerauLevenshteinDistanceTestCases() {
        return Stream.of(
                Arguments.of("", "test", 10, 4),
                Arguments.of("test", "", 10, 4),
                Arguments.of("", "test", 2, -1),
                Arguments.of("test", "", 2, -1),
                Arguments.of("testing long string", "testing", 2, -1),
                Arguments.of("kitten", "sitting", 1, -1),
                Arguments.of("saturday", "sunday", 3, 3),
                Arguments.of("hello", "world", 6, 4),
                Arguments.of("algorithm", "logarithm", 1, -1),
                Arguments.of("computer", "comptuer", 1, 1),
                Arguments.of("receive", "recieve", 3, 1),
                Arguments.of("programming", "porgramming", 0, -1),
                Arguments.of("test", "tset", 1, 1),
                Arguments.of("example", "exmaple", 3, 1),
                Arguments.of("transform", "transfrom", 0, -1),
                Arguments.of("information", "infromation", 1, 1),
                Arguments.of("development", "developemnt", 3, 1),
                Arguments.of("password", "passwrod", 0, -1),
                Arguments.of("separate", "seperate", 1, 1),
                Arguments.of("definitely", "definately", 3, 1),
                Arguments.of("occurrence", "occurence", 0, -1),
                Arguments.of("necessary", "neccessary", 1, 1),
                Arguments.of("restaurant", "restaraunt", 4, 2),
                Arguments.of("beginning", "begining", 0, -1),
                Arguments.of("government", "goverment", 1, 1),
                Arguments.of("abcdefghijklmnop", "ponmlkjihgfedcba", 17, 15),
                Arguments.of("AAAAAAAAAA", "BBBBBBBBBB", 5, -1),
                Arguments.of("abababababab", "babababababa", 2, 2),
                Arguments.of("supercalifragilisticexpialidocious", "supercalifragilisticexpialidocous", 3, 1),
                Arguments.of("pneumonoultramicroscopicsilicovolcanoconiosiss", "pneumonoultramicroscopicsilicovolcanoconiosis", 0, -1),
                Arguments.of("abcdefg", "gfedcba", 6, 6),
                Arguments.of("xyxyxyxyxy", "yxyxyxyxyx", 4, 2),
                Arguments.of("aaaaabbbbbccccc", "cccccbbbbbaaaaa", 5, -1),
                Arguments.of("thequickbrownfoxjumpsoverthelazydog", "thequickbrownfoxjumpsovrethelazydog", 1, 1),
                Arguments.of("antidisestablishmentarianism", "antidisestablishmentarianisn", 3, 1)
        );
    }
}
