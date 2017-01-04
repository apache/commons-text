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
 * Unit tests for {@link DurationParser}
 */
public class DurationParserTest extends AbstractGenericUnitParserTest {

    private static final long MINUTE = 60;
    private static final long HOUR = 60 * MINUTE;
    private static final long DAY = 24 * HOUR;
    private static final long WEEK = 7 * DAY;
    private static final long YEAR = 31536000;
    private static final long MONTH = 2628000;

    @Before
    public void init() {
        sut = new DurationParser();
    }

    @Test
    public void parsingTest() {
        runTestCase("2 hrs 20 min", 2 * HOUR + 20 * MINUTE);
        runTestCase("2h20min", 2 * HOUR + 20 * MINUTE);
        runTestCase("6 mos 1 day", 6 * MONTH + DAY);
        runTestCase("1 year 6 mos 1 day", 1 * YEAR + 6 * MONTH + DAY);
        runTestCase("2.5 hrs", Math.round(2.5 * HOUR));
        runTestCase("2.5h", Math.round(2.5 * HOUR));
        runTestCase("47 yrs 6 mos and 4.5d", 47 * YEAR + 6 * MONTH + Math.round(4.5 * DAY));
        runTestCase("3 weeks and, 2 days", HOUR * 24 * 7 * 3 + HOUR * 24 * 2);
        runTestCase("3 weeks, plus 2 days", HOUR * 24 * 7 * 3 + HOUR * 24 * 2);
        runTestCase("3 weeks with 2 days", HOUR * 24 * 7 * 3 + HOUR * 24 * 2);
        runTestCase("10 weeks and 1 day", WEEK * 10 + DAY);
        runTestCase("3 mins 4 sec", 3 * MINUTE + 4);
        runTestCase("3 Mins 4 sec", 3 * MINUTE + 4);
        runTestCase("3m 4s", 3 * MINUTE + 4);
        runTestCase("3m, 4s", 3 * MINUTE + 4);
        runTestCase("3m,4s", 3 * MINUTE + 4);
        runTestCase("3m4s", 3 * MINUTE + 4);
        runTestCase("3mon4sec", 3 * MONTH + 4);
        runTestCase("15s", 15);
        runTestCase("1 year", 1 * YEAR);
        runTestCase("12 mos", 1 * YEAR);
        runTestCase("1 mos", 1 * MONTH);
        runTestCase("18 months", 1 * YEAR + 6 * MONTH);
        runTestCase("24 months", 2 * YEAR);
    }

    @Test
    public void parsingInvalidStrings() {
        runTestCase("2 sausages 20 min", 20 * MINUTE);
        runTestCase("2minths5dhs", 0);
        runTestCase("15 dayz", 0);
        runTestCase("", 0);
    }
}
