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
import java.util.HashSet;
import java.util.Set;

/**
 * Options for the {@link HumanNameParser} parser.
 */
public final class ParserOptions {

    public static final ParserOptions DEFAULT_OPTIONS = new ParserOptions();

    private final Set<String> suffixes;

    private final Set<String> prefixes;

    public ParserOptions() {
        this.suffixes = new HashSet<String>(Arrays.asList(
                "esq", "esquire", "jr",
                "sr", "2", "ii", "iii", "iv"));
        this.prefixes = new HashSet<String>(Arrays.asList(
                "bar", "ben", "bin", "da", "dal",
                "de la", "de", "del", "der", "di", "ibn", "la", "le",
                "san", "st", "ste", "van", "van der", "van den", "vel",
                "von"));
    }

    /**
     * @return the suffixes
     */
    public Set<String> getSuffixes() {
        return suffixes;
    }

    /**
     * @return the prefixes
     */
    public Set<String> getPrefixes() {
        return prefixes;
    }

}
