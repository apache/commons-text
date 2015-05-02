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
package org.apache.commons.text.names;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;

/**
 * Options for the {@link HumanNameParser} parser.
 */
public final class ParserOptions {

    private static final Set<String> DEFAULT_PREFIXES = new HashSet<String>(Arrays.asList(
            "bar", "ben", "bin", "da", "dal",
            "de la", "de", "del", "der", "di", "ibn", "la", "le",
            "san", "st", "ste", "van", "van der", "van den", "vel",
            "von"));

    private static final Set<String> DEFAULT_SUFFIXES = new HashSet<String>(Arrays.asList(
            "esq", "esquire", "jr",
            "sr", "2", "ii", "iii", "iv"));

    private final Set<String> suffixes;

    private final Set<String> prefixes;

    public static final ParserOptions DEFAULT_OPTIONS = new ParserOptions();

    /**
     * Creates options for the human names parser.
     */
    public ParserOptions() {
        this(DEFAULT_PREFIXES, DEFAULT_SUFFIXES);
    }

    /**
     * Creates options for the human names parser.
     *
     * @param prefixes name prefixes, must not be null
     * @param suffixes name suffixes, must not be null
     */
    public ParserOptions(Set<String> prefixes, Set<String> suffixes) {
        Validate.notNull(prefixes, "Prefixes must not be null");
        Validate.notNull(prefixes, "Suffixes must not be null");
        this.prefixes = prefixes;
        this.suffixes = suffixes;
    }

    /**
     * @return the suffixes
     */
    public Set<String> getSuffixes() {
        return Collections.unmodifiableSet(suffixes);
    }

    /**
     * @return the prefixes
     */
    public Set<String> getPrefixes() {
        return Collections.unmodifiableSet(prefixes);
    }

}
