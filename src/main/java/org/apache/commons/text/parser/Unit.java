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
import java.util.Arrays;
import java.util.List;

/**
 * A Unit is a list of patterns for that unit and its value in the base
 * unit.
 * <p>
 * For example, if seconds is the base unit, a unit for minutes may have
 * those patterns: <pre>m(?!s)</pre>, <pre>min</pre>, <pre>minute</pre>.
 * <p>
 * And as the base value is seconds and a minute is 60 seconds, the value
 * in our example would be 60.
 *
 * @since 1.1
 *
 */
public class Unit {
    private final List<String> patterns;

    private final double value;

    /**
     * Build a new unit with the specified unit symbols patterns and the value
     * in the base unit.
     *
     * @param symbolsPatterns an array of unit symbols patterns matching possible unit's symbols
     * @param valueInBaseUnit the value of this unit in the base unit
     */
    Unit(String[] symbolsPatterns, double valueInBaseUnit) {
        this.patterns = Arrays.asList(symbolsPatterns);
        this.value = valueInBaseUnit;
    }

    String makeKey(String pattern) {
        return value + pattern;
    }

    public List<String> getPatterns() {
        return patterns;
    }

    public double getValue() {
        return value;
    }

    /**
     * Helper class to build a units table for a {@link GenericUnitParser}
     */
    public static class UnitsBuilder {

        private List<Unit> units;

        /**
         * Static helper for API fluency
         *
         * @see #UnitsBuilder(String...)
         */
        public static UnitsBuilder baseUnit(String... baseUnitSymbolsPatterns) {
            return new UnitsBuilder(baseUnitSymbolsPatterns);
        }

        /**
         * Create a new units table with a base unit.
         *
         * @param baseUnitSymbolsPatterns patterns matching the symbols of the base unit
         */
        private UnitsBuilder(String... baseUnitSymbolsPatterns) {
            units = new ArrayList<>();
            units.add(new Unit(baseUnitSymbolsPatterns, 1.0));
        }

        /**
         * Add a new unit to the units table
         *
         * @param valueInBaseUnit     the value of this unit in the base unit
         * @param unitSymbolsPatterns patterns matching the symbols of this unit
         * @return the builder
         */
        public UnitsBuilder addUnit(double valueInBaseUnit, String... unitSymbolsPatterns) {
            units.add(new Unit(unitSymbolsPatterns, valueInBaseUnit));
            return this;
        }

        /**
         * Build the units table
         *
         * @return the units table
         */
        public List<Unit> build() {
            return units;
        }
    }
}