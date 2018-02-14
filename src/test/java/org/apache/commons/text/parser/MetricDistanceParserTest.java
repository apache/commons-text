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
 * Unit tests for {@link MetricDistanceParser}
 */
public class MetricDistanceParserTest extends AbstractGenericUnitParserTest {

    private static final long MILLIMETER = 1;
    private static final long CENTIMETER = 10 * MILLIMETER;
    private static final long DECIMETER = 10 * CENTIMETER;
    private static final long METER = 10 * DECIMETER;
    private static final long DECAMETER = 10 * METER;
    private static final long HECTOMETER = 10 * DECAMETER;
    private static final long KILOMETER = 10 * HECTOMETER;

    @Before
    public void init() {
        sut = new MetricDistanceParser();
    }

    @Test
    public void parsingTest() {
        runTestCase("1 km 20 meters", 1 * KILOMETER + 20 * METER);
        runTestCase("2hms20decametres", 2 * HECTOMETER + 20 * DECAMETER);
        runTestCase("6 mms 1 dm", 6 * MILLIMETER + DECIMETER);
        runTestCase("1 km 6 ms 1 centimeters", 1 * KILOMETER + 6 * METER + CENTIMETER);
        runTestCase("2.5 km", 2.5 * KILOMETER);
        runTestCase("2.5km", 2.5 * KILOMETER);
        runTestCase("47 km 6 meters and 4.5mm", 47 * KILOMETER + 6 * METER + 4.5 * MILLIMETER);
        runTestCase("3 meters and, 2 kilometers", 3 * METER + 2 * KILOMETER);
    }

    @Test
    public void parsingInvalidStrings() {
        runTestCase("2 sausages 20 km", 20 * KILOMETER);
        runTestCase("2minths5dhs", 0);
        runTestCase("15 kilometerzz", 0);
        runTestCase("", 0);
    }
}
