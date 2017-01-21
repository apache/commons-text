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
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Translates a value using a lookup table.
 *
 * @since 1.0
 */
public class LookupTranslator extends CharSequenceTranslator {

    private final Map<String, String> lookupMap;
    private final HashSet<Character> prefixSet;
    private final int shortest;
    private final int longest;

    /**
     * Define the lookup table to be used in translation
     *
     * Note that, as of Lang 3.1, the key to the lookup table is converted to a
     * java.lang.String. This is because we need the key to support hashCode and
     * equals(Object), allowing it to be the key for a HashMap. See LANG-882.
     *
     * @param lookup CharSequence[][] table of size [*][2]
     */
    @Deprecated
    public LookupTranslator(final CharSequence[]... lookup) {
        lookupMap = new HashMap<>();
        prefixSet = new HashSet<>();
        int _shortest = Integer.MAX_VALUE;
        int _longest = 0;
        if (lookup != null) {
            for (final CharSequence[] seq : lookup) {
                this.lookupMap.put(seq[0].toString(), seq[1].toString());
                this.prefixSet.add(seq[0].charAt(0));
                final int sz = seq[0].length();
                if (sz < _shortest) {
                    _shortest = sz;
                }
                if (sz > _longest) {
                    _longest = sz;
                }
            }
        }
        shortest = _shortest;
        longest = _longest;
    }

    /**
     * Define the lookup table to be used in translation
     *
     * Note that, as of Lang 3.1 (the orgin of this code), the key to the lookup
     * table is converted to a java.lang.String. This is because we need the key
     * to support hashCode and equals(Object), allowing it to be the key for a
     * HashMap. See LANG-882.
     *
     * @param lookupMap Map&lt;CharSequence, CharSequence&gt; table of translator
     *                  mappings
     */
    public LookupTranslator(final Map<CharSequence, CharSequence> lookupMap) {
        if (lookupMap == null) {
            throw new InvalidParameterException("lookupMap cannot be null");
        }
        this.lookupMap = new HashMap<>();
        prefixSet = new HashSet<>();
        int _shortest = Integer.MAX_VALUE;
        int _longest = 0;
        for (final CharSequence key : lookupMap.keySet()) {
            this.lookupMap.put(key.toString(),
                    lookupMap.get(key).toString());
            this.prefixSet.add(key.charAt(0));
            final int sz = key.length();
            if (sz < _shortest) {
                _shortest = sz;
            }
            if (sz > _longest) {
                _longest = sz;
            }
        }
        shortest = _shortest;
        longest = _longest;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int translate(final CharSequence input, final int index, final Writer out) throws IOException {
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
