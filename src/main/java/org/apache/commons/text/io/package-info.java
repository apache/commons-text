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
 * <p>
 * {@link org.apache.commons.text.io.StringSubstitutorReader} is a {@link java.io.Reader} that performs string
 * substitution on a source {@code Reader} using a {@link org.apache.commons.text.StringSubstitutor}.
 * </p>
 * 
 * <p>
 * Using this Reader avoids reading a whole file into memory as a {@code String} to perform string substitution, for
 * example, when a Servlet filters a file to a client.
 * </p>
 * 
 * @since 1.9
 */
package org.apache.commons.text.io;
