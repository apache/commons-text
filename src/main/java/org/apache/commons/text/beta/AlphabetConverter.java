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
package org.apache.commons.text.beta;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

/**
 * <p>
 * Convert from one alphabet to another, with the possibility of leaving certain characters unencoded.
 * </p>
 *
 * <p>
 * The target and do not encode languages must be in the Unicode BMP, but the source language does not.
 * </p>
 *
 * <p>
 * The encoding will all be of a fixed length, except for the 'do not encode' chars, which will be of length 1
 * </p>
 *
 * <h3>Sample usage</h3>
 *
 * <pre>
 * Character[] originals; // a, b, c, d
 * Character[] encoding; // 0, 1, d
 * Character[] doNotEncode; // d
 *
 * AlphabetConverter ac = AlphabetConverter.createConverterFromChars(originals, encoding, doNotEncode);
 *
 * ac.encode("a"); // 00
 * ac.encode("b"); // 01
 * ac.encode("c"); // 0d
 * ac.encode("d"); // d
 * ac.encode("abcd"); // 00010dd
 * </pre>
 *
 * <p>
 * #ThreadSafe# AlphabetConverter class methods are threadsafe as they do not change internal state.
 * </p>
 *
 * @since 1.0
 *
 */
public final class AlphabetConverter {

    /**
     * Original string to be encoded.
     */
    private final Map<Integer, String> originalToEncoded;
    /**
     * Encoding alphabet.
     */
    private final Map<String, String> encodedToOriginal;
    /**
     * Length of the encoded letter.
     */
    private final int encodedLetterLength;
    /**
     * Arrow constant, used for converting the object into a string.
     */
    private static final String ARROW = " -> ";
    /**
     * Line separator, used for converting the object into a string.
     */
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * Hidden constructor for alphabet converter. Used by static helper methods.
     *
     * @param originalToEncoded original string to be encoded
     * @param encodedToOriginal encoding alphabet
     * @param encodedLetterLength length of the encoded letter
     */
    private AlphabetConverter(final Map<Integer, String> originalToEncoded, final Map<String, String> encodedToOriginal,
            final int encodedLetterLength) {

        this.originalToEncoded = originalToEncoded;
        this.encodedToOriginal = encodedToOriginal;
        this.encodedLetterLength = encodedLetterLength;
    }

    /**
     * Encode a given string.
     *
     * @param original the string to be encoded
     * @return the encoded string, {@code null} if the given string is null
     * @throws UnsupportedEncodingException if chars that are not supported are encountered
     */
    public String encode(final String original) throws UnsupportedEncodingException {
        if (original == null) {
            return null;
        }

        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < original.length();) {
            final int codepoint = original.codePointAt(i);

            final String nextLetter = originalToEncoded.get(codepoint);

            if (nextLetter == null) {
                throw new UnsupportedEncodingException(
                        "Couldn't find encoding for '" + codePointToString(codepoint) + "' in " + original);
            }

            sb.append(nextLetter);

            i += Character.charCount(codepoint);
        }

        return sb.toString();
    }

    /**
     * Decode a given string.
     *
     * @param encoded a string that has been encoded using this AlphabetConverter
     * @return the decoded string, {@code null} if the given string is null
     * @throws UnsupportedEncodingException if unexpected characters that cannot be handled are encountered
     */
    public String decode(final String encoded) throws UnsupportedEncodingException {
        if (encoded == null) {
            return null;
        }

        final StringBuilder result = new StringBuilder();

        for (int j = 0; j < encoded.length();) {
            final Integer i = encoded.codePointAt(j);
            final String s = codePointToString(i);

            if (s.equals(originalToEncoded.get(i))) {
                result.append(s);
                j++; // because we do not encode in Unicode extended the length of each encoded char is 1
            } else {
                if (j + encodedLetterLength > encoded.length()) {
                    throw new UnsupportedEncodingException("Unexpected end of string while decoding " + encoded);
                }
                final String nextGroup = encoded.substring(j, j + encodedLetterLength);
                final String next = encodedToOriginal.get(nextGroup);
                if (next == null) {
                    throw new UnsupportedEncodingException(
                            "Unexpected string without decoding (" + nextGroup + ") in " + encoded);
                }
                result.append(next);
                j += encodedLetterLength;
            }
        }

        return result.toString();
    }

    /**
     * Get the length of characters in the encoded alphabet that are necessary for each character in the original
     * alphabet.
     *
     * @return the length of the encoded char
     */
    public int getEncodedCharLength() {
        return encodedLetterLength;
    }

    /**
     * Get the mapping from integer code point of source language to encoded string. Use to reconstruct converter from
     * serialized map.
     *
     * @return the original map
     */
    public Map<Integer, String> getOriginalToEncoded() {
        return Collections.unmodifiableMap(originalToEncoded);
    }

    /**
     * Recursive method used when creating encoder/decoder.
     *
     * @param level at which point it should add a single encoding
     * @param currentEncoding current encoding
     * @param encoding letters encoding
     * @param originals original values
     * @param doNotEncodeMap map of values that should not be encoded
     */
    @SuppressWarnings("PMD")
    private void addSingleEncoding(final int level, final String currentEncoding, final Collection<Integer> encoding,
            final Iterator<Integer> originals, final Map<Integer, String> doNotEncodeMap) {

        if (level > 0) {
            for (final int encodingLetter : encoding) {
                if (originals.hasNext()) {

                    // this skips the doNotEncode chars if they are in the
                    // leftmost place
                    if (level != encodedLetterLength || !doNotEncodeMap.containsKey(encodingLetter)) {
                        addSingleEncoding(level - 1, currentEncoding + codePointToString(encodingLetter), encoding,
                                originals, doNotEncodeMap);
                    }
                } else {
                    return; // done encoding all the original alphabet
                }
            }
        } else {
            Integer next = originals.next();

            while (doNotEncodeMap.containsKey(next)) {
                final String originalLetterAsString = codePointToString(next);

                originalToEncoded.put(next, originalLetterAsString);
                encodedToOriginal.put(originalLetterAsString, originalLetterAsString);

                if (!originals.hasNext()) {
                    return;
                }

                next = originals.next();
            }

            final String originalLetterAsString = codePointToString(next);

            originalToEncoded.put(next, currentEncoding);
            encodedToOriginal.put(currentEncoding, originalLetterAsString);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        for (final Entry<Integer, String> entry : originalToEncoded.entrySet()) {
            sb.append(codePointToString(entry.getKey())).append(ARROW).append(entry.getValue()).append(LINE_SEPARATOR);
        }

        return sb.toString();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AlphabetConverter)) {
            return false;
        }
        final AlphabetConverter other = (AlphabetConverter) obj;
        return originalToEncoded.equals(other.originalToEncoded) && encodedToOriginal.equals(other.encodedToOriginal)
                && encodedLetterLength == other.encodedLetterLength;
    }

    @Override
    public int hashCode() {
        return Objects.hash(originalToEncoded, encodedToOriginal, encodedLetterLength);
    }

    // -- static methods

    /**
     * Create a new converter from a map.
     *
     * @param originalToEncoded a map returned from getOriginalToEncoded()
     * @return the reconstructed AlphabetConverter
     * @see AlphabetConverter#getOriginalToEncoded()
     */
    public static AlphabetConverter createConverterFromMap(final Map<Integer, String> originalToEncoded) {
        final Map<Integer, String> unmodifiableOriginalToEncoded = Collections.unmodifiableMap(originalToEncoded);
        final Map<String, String> encodedToOriginal = new LinkedHashMap<>();
        final Map<Integer, String> doNotEncodeMap = new HashMap<>();

        int encodedLetterLength = 1;

        for (final Entry<Integer, String> e : unmodifiableOriginalToEncoded.entrySet()) {
            final String originalAsString = codePointToString(e.getKey());
            encodedToOriginal.put(e.getValue(), originalAsString);

            if (e.getValue().equals(originalAsString)) {
                doNotEncodeMap.put(e.getKey(), e.getValue());
            }

            if (e.getValue().length() > encodedLetterLength) {
                encodedLetterLength = e.getValue().length();
            }
        }

        return new AlphabetConverter(unmodifiableOriginalToEncoded, encodedToOriginal, encodedLetterLength);
    }

    /**
     * Create an alphabet converter, for converting from the original alphabet, to the encoded alphabet, while leaving
     * the characters in <em>doNotEncode</em> as they are (if possible).
     *
     * <p>Duplicate letters in either original or encoding will be ignored.</p>
     *
     * @param original an array of chars representing the original alphabet
     * @param encoding an array of chars representing the alphabet to be used for encoding
     * @param doNotEncode an array of chars to be encoded using the original alphabet - every char here must appear in
     *            both the previous params
     * @return the AlphabetConverter
     * @throws IllegalArgumentException if an AlphabetConverter cannot be constructed
     */
    public static AlphabetConverter createConverterFromChars(final Character[] original, final Character[] encoding,
            final Character[] doNotEncode) {
        return AlphabetConverter.createConverter(convertCharsToIntegers(original), convertCharsToIntegers(encoding),
                convertCharsToIntegers(doNotEncode));
    }

    /**
     * Convert characters to integers.
     *
     * @param chars array of characters
     * @return an equivalent array of integers
     */
    private static Integer[] convertCharsToIntegers(final Character[] chars) {
        if (chars == null || chars.length == 0) {
            return new Integer[0];
        }
        final Integer[] integers = new Integer[chars.length];
        for (int i = 0; i < chars.length; i++) {
            integers[i] = (int) chars[i];
        }
        return integers;
    }

    /**
     * Create an alphabet converter, for converting from the original alphabet, to the encoded alphabet, while leaving
     * the characters in <em>doNotEncode</em> as they are (if possible).
     *
     * <p>Duplicate letters in either original or encoding will be ignored.</p>
     *
     * @param original an array of ints representing the original alphabet in codepoints
     * @param encoding an array of ints representing the alphabet to be used for encoding, in codepoints
     * @param doNotEncode an array of ints representing the chars to be encoded using the original alphabet - every char
     *            here must appear in both the previous params
     * @return the AlphabetConverter
     * @throws IllegalArgumentException if an AlphabetConverter cannot be constructed
     */
    public static AlphabetConverter createConverter(final Integer[] original, final Integer[] encoding, final Integer[] doNotEncode) {

        final Set<Integer> originalCopy = new LinkedHashSet<>(Arrays.<Integer> asList(original));
        final Set<Integer> encodingCopy = new LinkedHashSet<>(Arrays.<Integer> asList(encoding));
        final Set<Integer> doNotEncodeCopy = new LinkedHashSet<>(Arrays.<Integer> asList(doNotEncode));

        final Map<Integer, String> originalToEncoded = new LinkedHashMap<>();
        final Map<String, String> encodedToOriginal = new LinkedHashMap<>();
        final Map<Integer, String> doNotEncodeMap = new HashMap<>();

        int encodedLetterLength;

        for (final int i : doNotEncodeCopy) {
            if (!originalCopy.contains(i)) {
                throw new IllegalArgumentException(
                        "Can not use 'do not encode' list because original alphabet does not contain '"
                                + codePointToString(i) + "'");
            }

            if (!encodingCopy.contains(i)) {
                throw new IllegalArgumentException(
                        "Can not use 'do not encode' list because encoding alphabet does not contain '"
                                + codePointToString(i) + "'");
            }

            doNotEncodeMap.put(i, codePointToString(i));
        }

        if (encodingCopy.size() >= originalCopy.size()) {
            encodedLetterLength = 1;

            final Iterator<Integer> it = encodingCopy.iterator();

            for (final int originalLetter : originalCopy) {
                final String originalLetterAsString = codePointToString(originalLetter);

                if (doNotEncodeMap.containsKey(originalLetter)) {
                    originalToEncoded.put(originalLetter, originalLetterAsString);
                    encodedToOriginal.put(originalLetterAsString, originalLetterAsString);
                } else {
                    Integer next = it.next();

                    while (doNotEncodeCopy.contains(next)) {
                        next = it.next();
                    }

                    final String encodedLetter = codePointToString(next);

                    originalToEncoded.put(originalLetter, encodedLetter);
                    encodedToOriginal.put(encodedLetter, originalLetterAsString);
                }
            }

            return new AlphabetConverter(originalToEncoded, encodedToOriginal, encodedLetterLength);

        } else if (encodingCopy.size() - doNotEncodeCopy.size() < 2) {
            throw new IllegalArgumentException(
                    "Must have at least two encoding characters (excluding those in the 'do not encode' list), but has "
                            + (encodingCopy.size() - doNotEncodeCopy.size()));
        } else {
            // we start with one which is our minimum, and because we do the
            // first division outside the loop
            int lettersSoFar = 1;

            // the first division takes into account that the doNotEncode
            // letters can't be in the leftmost place
            int lettersLeft = (originalCopy.size() - doNotEncodeCopy.size())
                    / (encodingCopy.size() - doNotEncodeCopy.size());

            while (lettersLeft / encodingCopy.size() >= 1) {
                lettersLeft = lettersLeft / encodingCopy.size();
                lettersSoFar++;
            }

            encodedLetterLength = lettersSoFar + 1;

            final AlphabetConverter ac = new AlphabetConverter(originalToEncoded, encodedToOriginal, encodedLetterLength);

            ac.addSingleEncoding(encodedLetterLength, "", encodingCopy, originalCopy.iterator(), doNotEncodeMap);

            return ac;
        }
    }

    /**
     * Create new String that contains just the given code point.
     *
     * @param i code point
     * @return a new string with the new code point
     * @see "http://www.oracle.com/us/technologies/java/supplementary-142654.html"
     */
    private static String codePointToString(final int i) {
        if (Character.charCount(i) == 1) {
            return String.valueOf((char) i);
        }
        return new String(Character.toChars(i));
    }
}
