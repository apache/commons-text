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

/**
 * A DurationParser is a {@link GenericUnitParser} for durations, from seconds
 * to years.
 *
 * @since 1.1
 *
 */
public class DurationParser extends GenericUnitParser {

    public DurationParser() {
        super(Unit.UnitsBuilder.baseUnit(   "s", "sec", "second")
                .addUnit(60               , "m(?!s)", "min", "minute")
                .addUnit(60 * 60          , "h", "hr", "hour")
                .addUnit(60 * 60 * 24     , "d", "dy", "day")
                .addUnit(60 * 60 * 24 * 7 , "w", "wk", "week")
                .addUnit(2628000          , "mth", "mo", "mon", "month")
                .addUnit(31536000         , "y", "yr", "year")
            .build()
        );
    }
}
