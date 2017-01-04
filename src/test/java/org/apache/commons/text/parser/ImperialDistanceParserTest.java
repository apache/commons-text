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
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * Unit tests for {@link ImperialDistanceParser}
 */
public class ImperialDistanceParserTest extends AbstractGenericUnitParserTest {

    private static final long INCH = 1;
    private static final long FOOT = 12 * INCH;
    private static final long YARD = 3 * FOOT;
    private static final long MILE = 1760 * YARD;

    @Before
    public void init() {
        sut = new ImperialDistanceParser();
    }

    @Test
    public void parsingTest() {
        runTestCase("1 mi 20 feet", 1 * MILE + 20 * FOOT);
        runTestCase("2miles20inch", 2 * MILE + 20 * INCH);
        runTestCase("6 yd 1 foot", 6 * YARD + FOOT);
        runTestCase("1 mile 6 yards 1 inches", 1 * MILE + 6 * YARD + INCH);
        runTestCase("2.5 foot", 2.5 * FOOT);
        runTestCase("2.5ft", 2.5 * FOOT);
        runTestCase("47 mi 6 inch and 4.5foots", 47 * MILE + 6 * INCH + 4.5 * FOOT);
        runTestCase("3 miles and, 2 inch", 3 * MILE + 2 * INCH);
    }

    @Test
    public void parsingInvalidStrings() {
        runTestCase("2 km 20 mile", 20 * MILE);
        runTestCase("2minths5dhs", 0);
        runTestCase("15 milezz", 0);
        runTestCase("", 0);
    }
}
