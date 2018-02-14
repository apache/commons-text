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
/**
 * <p>Provides several natural language parsers and related utilities</p>
 *
 * <p>Based on {@link org.apache.commons.text.parser.GenericUnitParser}, the following parsers are available at the moment:</p>
 *
 * <ul>
 * <li>{@link org.apache.commons.text.parser.DurationParser Duration parser}</li>
 * <li>{@link org.apache.commons.text.parser.MetricDistanceParser Distance parser (using metric system)}</li>
 * <li>{@link org.apache.commons.text.parser.ImperialDistanceParser Distance parser (using imperial system)}</li>
 * </ul>
 *
 * You can write your own parser for any set of units using @link {@link org.apache.commons.text.parser.GenericUnitParser}
 * and using any of the aforementioned implementation source code as example.
 *
 * @since 1.1
 */
package org.apache.commons.text.parser;
