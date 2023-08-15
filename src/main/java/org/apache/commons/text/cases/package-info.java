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
 * <p>Provides algorithms for parsing and formatting various programming "Cases".</p>
 * <p>The provided implementations are for the four most common cases:<br>
 * CamelCase - delimited by ascii uppercase alpha characters and always beginning with a lowercase ascii alpha<br>
 * PascalCase - Similar to CamelCase but always begins with an uppercase ascii alpha<br>
 * DelimitedCase - delimited by a constant character, which is omitted from parsed tokens<br>
 * SnakeCase - implementation of DelimitedCase in which the delimiter is an underscore '_'<br>
 * KebabCase - implementation of DelimitedCase in which the delimiter is a hyphen '-'<br>
 * </p>
 *
 * @since 1.0
 */
package org.apache.commons.text.cases;
