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

/**
 * <p>
 * Provides algorithms for looking up strings for use with a {@link org.apache.commons.text.StringSubstitutor
 * StringSubstitutor}. The main class in this package is {@link org.apache.commons.text.lookup.StringLookupFactory
 * StringLookupFactory}.
 * </p>
 * <p>
 * Use {@link org.apache.commons.text.lookup.StringLookupFactory StringLookupFactory} to create instances of string
 * lookups or access singleton string lookups. The main interface is {@link org.apache.commons.text.lookup.StringLookup
 * StringLookup} which is implemented here in package private classes.
 * </p>
 * <p>
 * Like {@link java.util.function.BiFunction BiFunction} is a variant of {@link java.util.function.Function Function},
 * this {@link org.apache.commons.text.lookup.BiStringLookup BiStringLookup} is a variant of
 * {@link org.apache.commons.text.lookup.StringLookup StringLookup}.
 * </p>
 * <p>
 * The initial implementation was adapted from Apache Commons Log4j 2.11.0.
 * </p>
 *
 * @since 1.3
 */
package org.apache.commons.text.lookup;
