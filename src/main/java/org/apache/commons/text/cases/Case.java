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
package org.apache.commons.text.cases;

import java.util.List;

/**
 * Formats and parses tokens to/from a String. In most implementations tokens returned
 * by the parse method abide by any restrictions present in the format method. That is, calling
 * format() with the results of a call to parse() on the same Case instance should return a
 * matching String.
 *
 * @since 1.11
 */
public interface Case {

    /**
     * Formats a set of tokens into a string. The tokens do not necessarily have to meet the syntax
     * requirements of the Case. The documentation for each implementation should specify what input
     * is supported.
     *
     * @param tokens string tokens to be formatted by this Case
     * @return the formatted string
     * @throws IllegalArgumentException if tokens cannot be formatted
     */
    String format(Iterable<String> tokens);

    /**
     * Parses a string into a series of tokens. The string must abide by certain restrictions,
     * dependent on each Case implementation.
     *
     * @param string the string to be parsed by this Case into a list of tokens
     * @return the list of parsed tokens
     * @throws IllegalArgumentException if the string cannot be parsed by this implementation
     */
    List<String> parse(String string);

}
