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
 * Util methods for the Cases API.
 */
public class Cases {

    /** Constant reusable instance of KebabCase. */
    public static final KebabCase KEBAB = KebabCase.INSTANCE;
    /** Constant reusable instance of SnakeCase. */
    public static final SnakeCase SNAKE = SnakeCase.INSTANCE;
    /** Constant reusable instance of CamelCase. */
    public static final CamelCase CAMEL = CamelCase.INSTANCE;
    /** Constant reusable instance of PascalCase. */
    public static final PascalCase PASCAL = PascalCase.INSTANCE;

    /**
     * Utility method for converting between cases.
     *
     * @param string the cased string to parse
     * @param from the case of the existing string
     * @param to the case to convert to
     * @return the converted string
     */
    public static String convert(String string, Case from, Case to) {
        return to.format(from.parse(string));
    }

}
