/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.text.lookup;

import java.awt.event.KeyEvent;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link ConstantStringLookup}.
 * <p>
 * This class was adapted from Apache Commons Configuration.
 * </p>
 */
public class ConstantStringLookupTest {

    /** A public field that can be read by the lookup. */
    public static final String FIELD = "Field that can be read";

    /** A private field that cannot be read by the lookup. */
    @SuppressWarnings("unused")
    private static final String PRIVATE_FIELD = "PRIVATE";

    /** The lookup object to be tested. */
    private ConstantStringLookup stringLookup;

    /**
     * Clears the test environment. Here the static cache of the constant lookup class is wiped out.
     */
    @AfterEach
    public void afterEach() {
        ConstantStringLookup.clear();
    }

    /**
     * Clears the test environment. Here the static cache of the constant lookup class is wiped out.
     */
    @BeforeEach
    public void beforeEach() {
        stringLookup = ConstantStringLookup.INSTANCE;
    }

    /**
     * Tests accessing the cache by querying a variable twice.
     */
    @Test
    void testLookupCache() {
        testLookupConstant();
        testLookupConstant();
    }

    /**
     * Tests resolving a valid constant.
     */
    @Test
    void testLookupConstant() {
        Assertions.assertEquals(FIELD, stringLookup.apply(variable("FIELD")), "Wrong value of constant");
    }

    /**
     * Tries to resolve a variable with an invalid syntax: The name does not contain a dot as a field separator.
     */
    @Test
    void testLookupInvalidSyntax() {
        Assertions.assertNull(stringLookup.apply("InvalidVariableName"),
            "Non null return value for invalid variable name");
    }

    /**
     * Tests resolving a non existing constant. Result should be null.
     */
    @Test
    void testLookupNonExisting() {
        Assertions.assertNull(stringLookup.apply(variable("NO_FIELD")),
            "Non null return value for non existing constant");
    }

    /**
     * Tests resolving a non string constant. Then looks the same variable up from the cache.
     */
    @Test
    void testLookupNonString() {
        final String ref = KeyEvent.class.getName() + ".VK_ESCAPE";
        final String expected = Integer.toString(KeyEvent.VK_ESCAPE);
        Assertions.assertEquals(expected, stringLookup.apply(ref), "Wrong result of first lookup");
        Assertions.assertEquals(expected, stringLookup.apply(ref), "Wrong result of 2nd lookup");
    }

    /**
     * Tests looking up a null variable.
     */
    @Test
    void testLookupNull() {
        Assertions.assertNull(stringLookup.apply(null), "Non null return value for null variable");
    }

    /**
     * Tests resolving a private constant. Because a private field cannot be accessed this should again yield null.
     */
    @Test
    void testLookupPrivate() {
        Assertions.assertNull(stringLookup.apply(variable("PRIVATE_FIELD")),
            "Non null return value for non accessible field");
    }

    /**
     * Tests resolving a field from an unknown class.
     */
    @Test
    void testLookupUnknownClass() {
        Assertions.assertNull(stringLookup.apply("org.apache.commons.configuration.NonExistingConfig." + FIELD),
            "Non null return value for unknown class");
    }

    /**
     * Generates the name of a variable for a lookup operation based on the given field name of this class.
     *
     * @param field the field name
     * @return the variable for looking up this field
     */
    private String variable(final String field) {
        return getClass().getName() + '.' + field;
    }
}
