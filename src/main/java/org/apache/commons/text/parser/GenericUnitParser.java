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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * A Generic Unit Parser is a natural language parser to parse strings
 * containing a sequence of tuples (value, unit), and convert it into a single
 * value in a base unit.
 * <p>
 * For example, if you define the following units:
 * <p>
 * <ul>
 * <li>Base unit: mm, millimeter</li>
 * <li>Centimeter (cm): 10mm</li>
 * <li>Meter (mtr, m): 1000mm</li>
 * <li>Kilometer (kmtr, km): 1000000mm</li>
 * </ul>
 * <p>
 * Parsing the following string:
 * <p>
 * <pre>
 * 		1 kmtr, 10m and 28cm, 12millimeter
 * </pre>
 * <p>
 * will give you the following result:
 * <p>
 * <pre>
 * 		1 * 1000000 + 10 * 1000 + 28 * 10 + 12 = 1010292mm
 * </pre>
 * <p>
 * A Generic Unit Parser is flexible enough to understand several symbols for a
 * unit (second(s), sec, s) and ignore punctuation and stop words.
 * <p>
 * Pre-built unit parsers are available. {@link DurationParser}, {@link MetricDistanceParser} and {@link ImperialDistanceParser}
 *
 * @since 1.1
 */
public class GenericUnitParser {
    private List<Unit> units;

    private Map<String, Pattern> patternCache = new ConcurrentHashMap<>();

    /**
     * Creates a new Generic Unit Parser with the specified units table.
     * <p>
     * The first unit in the list (index 0) is considered to be the base unit.
     * <p>
     * For an example of a units table, please see
     * {@link DurationParser#DurationParser()}
     *
     * @param units The units table
     */
    public GenericUnitParser(List<Unit> units) {
        this.units = new ArrayList<Unit>(units);
    }

    /**
     * Parse a string containing a sequence of tuples (value, unit) and convert
     * it into a single value in a base unit.
     *
     * @param str the string to parse
     * @return the value of the string in the base unit
     */
    public double parse(String str) {
        double result = 0;
        for (Unit unit : units) {
            for (String pattern : unit.getPatterns()) {
                Pattern p = patternCache.get(unit.makeKey(pattern));
                if (p == null) {
                    try {
                        p = Pattern.compile("((?:\\d+\\.\\d+)|\\d+)\\s?(" + pattern + "s?(?=\\s|\\d|\\b))",
                                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
                    } catch (PatternSyntaxException e) {
                        throw new RuntimeException("Syntax error in the pattern " + pattern, e);
                    }
                    patternCache.put(unit.makeKey(pattern), p);
                }
                Matcher m = p.matcher(str);
                while (m.find()) {
                    result += Double.parseDouble(m.group(1)) * unit.getValue();
                }
            }
        }
        return result;
    }
}