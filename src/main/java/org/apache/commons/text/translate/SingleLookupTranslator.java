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
package org.apache.commons.text.translate;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * Translates a value using a lookup table.
 * But doesn't translate if that value is already translated.
 *
 * @since 1.0
 */
public class SingleLookupTranslator extends CharSequenceTranslator {

    private final Map<String, String> lookupMap;
    private final HashSet<Character> prefixSet;
    private final int shortest;
    private final int longest;
    private final int shortestValue;
    private final int longestValue;

    /**
     * Define the look tables to be used in translation.
     * <p>
     * Note that, as of Lang 3.1, the key to the lookup table is converted to a
     * java.lang.String. This is because we need the key to support hashCode and
     * equals(Object), allowing it to be the key for a HashMap. See LANG-882.
     * <p>
     * Also note that, multiple lookup tables should be passed to this translator
     * instead of passing multiple instances of this translator to the
     * AggregateTranslator. Because, this translator only checks the values of the
     * lookup table passed to this instance while deciding whether a value is
     * already translated or not.
     *
     * @param inputMaps, an array of Map&lt;CharSequence, CharSequence&gt;.
     */
    public SingleLookupTranslator(Map<CharSequence, CharSequence>... inputMaps) {
        Map<CharSequence, CharSequence> lookup = new HashMap<>();
        for (Map<CharSequence, CharSequence> input : inputMaps) {
            Iterator<Map.Entry<CharSequence, CharSequence>> it = input.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<CharSequence, CharSequence> pair = it.next();
                lookup.put(pair.getKey(), pair.getValue());
            }
        }
        lookupMap = new HashMap<String, String>();
        prefixSet = new HashSet<Character>();
        int _shortest = Integer.MAX_VALUE;
        int _longest = 0;
        int _shortestValue = Integer.MAX_VALUE;
        int _longestValue = 0;
        if (lookup != null) {
            Iterator<Map.Entry<CharSequence, CharSequence>> it = lookup.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<CharSequence, CharSequence> pair = it.next();
                this.lookupMap.put(pair.getKey().toString(), pair.getValue().toString());
                this.prefixSet.add(pair.getKey().charAt(0));
                final int sz = pair.getKey().length();
                if (sz < _shortest) {
                    _shortest = sz;
                }
                if (sz > _longest) {
                    _longest = sz;
                }
                final int sizeOfValue = lookup.get(pair.getKey()).length();
                if (sizeOfValue < _shortestValue) {
                    _shortestValue = sizeOfValue;
                }
                if (sizeOfValue > _longestValue) {
                    _longestValue = sizeOfValue;
                }
            }
        }
        shortest = _shortest;
        longest = _longest;
        shortestValue = _shortestValue;
        longestValue = _longestValue;
    }

    /**
     * Translate a set of codepoints, represented by an int index into a CharSequence,
     * into another set of codepoints. The number of codepoints consumed must be returned,
     * and the only IOExceptions thrown must be from interacting with the Writer so that
     * the top level API may reliably ignore StringWriter IOExceptions.
     *
     * @param input CharSequence that is being translated
     * @param index int representing the current point of translation
     * @param out   Writer to translate the text to
     * @return int count of codepoints consumed
     * @throws IOException if and only if the Writer produces an IOException
     */
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        // check if already translated
        int maxValue = longestValue;
        if (index + maxValue > input.length()) {
            maxValue = input.length() - index;
        }
        // implement greedy algorithm to check all the possible 'value' matches
        // for which we need to skip translation.
        for (int i = maxValue; i >= shortestValue; i--) {
            final CharSequence subSeq = input.subSequence(index, index + i);
            // If the sub-string is already translated, return without translating.
            if (lookupMap.containsValue(subSeq.toString())) {
                return 0;
            }
        }

        // check if translation exists for the input at position index
        if (prefixSet.contains(input.charAt(index))) {
            int max = longest;
            if (index + longest > input.length()) {
                max = input.length() - index;
            }
            // implement greedy algorithm by trying maximum match first
            for (int i = max; i >= shortest; i--) {
                final CharSequence subSeq = input.subSequence(index, index + i);
                final String result = lookupMap.get(subSeq.toString());

                if (result != null) {
                    out.write(result);
                    return i;
                }
            }
        }
        return 0;
    }
}
