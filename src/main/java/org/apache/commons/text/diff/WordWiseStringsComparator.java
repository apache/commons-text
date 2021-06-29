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
package org.apache.commons.text.diff;

/**
 * <p>
 * This class is a specialization of {@link Comparator class} adapted for sequences of {@code String} objects.
 * </p>
 *
 * @since 1.x
 */
public class WordWiseStringsComparator extends Comparator<String> {

    /**
     * Simple constructor.
     * <p>
     * Creates a new instance of WordWiseStringsComparator.
     * </p>
     *
     * @param left  first sequence of {@code String} objects to be compared
     * @param right second sequence of {@code String} objects to be compared
     */
    public WordWiseStringsComparator(String[] left, String[] right) {
        super(left, right);
    }

}
