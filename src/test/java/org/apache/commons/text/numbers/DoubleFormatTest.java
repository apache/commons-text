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
import java.io.StringWriter;
import java.io.UncheckedIOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DoubleFormatTest {
    @Test
    void testDoubleToString() {
        // arrange
        final DoubleFormat fmt = DoubleFormat.DOUBLE_TO_STRING;

        // act/assert
        checkDefaultFormatSpecial(fmt);

        checkFormat(fmt, 0.00001, "1.0E-5");
        checkFormat(fmt, -0.0001, "-1.0E-4");
        checkFormat(fmt, 0.001, "0.001");
        checkFormat(fmt, -0.01, "-0.01");
        checkFormat(fmt, 0.1, "0.1");
        checkFormat(fmt, -0.0, "-0.0");
        checkFormat(fmt, 0.0, "0.0");
        checkFormat(fmt, -1.0, "-1.0");
        checkFormat(fmt, 10.0, "10.0");
        checkFormat(fmt, -100.0, "-100.0");
        checkFormat(fmt, 1000.0, "1000.0");
        checkFormat(fmt, -10000.0, "-10000.0");
        checkFormat(fmt, 100000.0, "100000.0");
        checkFormat(fmt, -1000000.0, "-1000000.0");
        checkFormat(fmt, 10000000.0, "1.0E7");
        checkFormat(fmt, -100000000.0, "-1.0E8");

        checkFormat(fmt, 0.5 * Float.MAX_VALUE, "1.7014117331926443E38");
        checkFormat(fmt, -1.0 / 1.9175e20, "-5.2151238591916555E-21");

        checkFormat(fmt, Double.MAX_VALUE, "1.7976931348623157E308");
        checkFormat(fmt, Double.MIN_VALUE, "4.9E-324");
        checkFormat(fmt, Double.MIN_NORMAL, "2.2250738585072014E-308");
        checkFormat(fmt, Math.PI, "3.141592653589793");
        checkFormat(fmt, Math.E, "2.718281828459045");
    }

    @Test
    void testFloatToString() {
        // arrange
        final DoubleFormat fmt = DoubleFormat.FLOAT_TO_STRING;

        // act/assert
        checkDefaultFormatSpecial(fmt);

        checkFormat(fmt, 0.00001, "1.0E-5");
        checkFormat(fmt, -0.0001, "-1.0E-4");
        checkFormat(fmt, 0.001, "0.001");
        checkFormat(fmt, -0.01, "-0.01");
        checkFormat(fmt, 0.1, "0.1");
        checkFormat(fmt, -0.0, "-0.0");
        checkFormat(fmt, 0.0, "0.0");
        checkFormat(fmt, -1.0, "-1.0");
        checkFormat(fmt, 10.0, "10.0");
        checkFormat(fmt, -100.0, "-100.0");
        checkFormat(fmt, 1000.0, "1000.0");
        checkFormat(fmt, -10000.0, "-10000.0");
        checkFormat(fmt, 100000.0, "100000.0");
        checkFormat(fmt, -1000000.0, "-1000000.0");
        checkFormat(fmt, 10000000.0, "1.0E7");
        checkFormat(fmt, -100000000.0, "-1.0E8");

        checkFormat(fmt, 0.5 * Float.MAX_VALUE, "1.7014117E38");
        checkFormat(fmt, -1.0 / 1.9175e20, "-5.2151238E-21");

        checkFormat(fmt, Double.MAX_VALUE, "Infinity");
        checkFormat(fmt, Double.MIN_VALUE, "0.0");
        checkFormat(fmt, Double.MIN_NORMAL, "0.0");
        checkFormat(fmt, Math.PI, "3.1415927");
        checkFormat(fmt, Math.E, "2.7182817");
    }

    private static void checkDefaultFormatSpecial(final DoubleFormat fmt) {
        checkFormat(fmt, 0.0, "0.0");
        checkFormat(fmt, -0.0, "-0.0");
        checkFormat(fmt, Double.NaN, "NaN");
        checkFormat(fmt, Double.POSITIVE_INFINITY, "Infinity");
        checkFormat(fmt, Double.NEGATIVE_INFINITY, "-Infinity");
    }

    private static void checkFormat(final DoubleFormat fmt, final double d, final String str) {
        Assertions.assertEquals(str, fmt.apply(d));

        final StringBuilder sb = new StringBuilder();
        fmt.appendTo(sb, d);
        Assertions.assertEquals(str, sb.toString());

        final StringWriter appendable = new StringWriter();
        try {
            fmt.appendTo(appendable, d);
        } catch (IOException exc) {
            throw new UncheckedIOException(exc);
        }
        Assertions.assertEquals(str, appendable.toString());
    }
}
