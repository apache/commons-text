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
package org.apache.commons.text.parser;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link GenericUnitParser}
 */
public class GenericUnitParserTest {

    private GenericUnitParser sut;

    @Before
    public void init() {
        List<Unit> units = new ArrayList<>();
        units.add(new Unit(new String[] { "s", "sec", "second" }, 1));
        // The regexp below is incorrect on purpose
        units.add(new Unit(new String[] { "m(?!s[)]]]??!!\\", "min", "minute" }, 60));
        units.add(new Unit(new String[] { "h", "hr", "hour" }, 60 * 60));
        units.add(new Unit(new String[] { "d", "dy", "day" }, 60 * 60 * 24));
        units.add(new Unit(new String[] { "w", "wk", "week" }, 60 * 60 * 24 * 7));
        units.add(new Unit(new String[] { "mth", "mo", "mon", "month" }, 2628000));
        units.add(new Unit(new String[] { "y", "yr", "year" }, 31536000));
        sut = new GenericUnitParser(units);
    }

    @Test(expected = RuntimeException.class)
    public void test_incorrectRegexp() {
        sut.parse("");
    }
}
