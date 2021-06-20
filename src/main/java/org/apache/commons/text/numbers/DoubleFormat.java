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
package org.apache.commons.text.numbers;

import java.io.IOException;
import java.util.function.DoubleFunction;

/** Interface for constructing string representations of double values.
 */
@FunctionalInterface
public interface DoubleFormat extends DoubleFunction<String> {

    /** {@link DoubleFormat} instance that simply calls {@link Double#toString(double)}. */
    static DoubleFormat DOUBLE_TO_STRING = Double::toString;

    /** {@link DoubleFormat} instance that simply converts the double argumen to a float
     * and then calls {@link Float#toString(float)}.
     */
    static DoubleFormat FLOAT_TO_STRING = d -> Float.toString((float) d);

    /** Append the string representation of {@code d} to the given appendable.
     * @param appendable object to append to
     * @param d double value to append a string representation of
     * @throws IOException if an I/O error occurs
     */
    default void appendTo(final Appendable appendable, final double d)
            throws IOException {
        appendable.append(apply(d));
    }

    /** Append the string representation of {@code d} to the given string builder.
     * @param strBuilder string builder to append to
     * @param d double value to append a string representation of
     */
    default void appendTo(final StringBuilder strBuilder, final double d) {
        strBuilder.append(apply(d));
    }
}
