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

/**
 * Case implementation which parses and formats strings of the form 'my_snake_string'
 * <p>
 * SnakeCase is a delimited case where the delimiter is the underscore character '_'.
 * </p>
 */
public class SnakeCase extends DelimitedCase {

    /** constant for delimiter. */
    public static final char DELIMITER = '_';

    /** constant reuseable instance of this case. */
    public static final SnakeCase INSTANCE = new SnakeCase();

    /**
     * Constructs a new SnakeCase instance.
     */
    public SnakeCase() {
        super(DELIMITER);
    }

}
