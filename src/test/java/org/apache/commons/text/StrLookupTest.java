/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.junit.jupiter.api.Test;

/**
 * Test class for {@link StrLookup}.
 *
 * @deprecated This class will be removed in 2.0.
 */
@Deprecated
public class StrLookupTest  {

    @Test
    void testMapLookup() {
        final Map<String, Object> map = new HashMap<>();
        map.put("key", "value");
        map.put("number", 2);
        assertEquals("value", StrLookup.mapLookup(map).apply("key"));
        assertEquals("2", StrLookup.mapLookup(map).apply("number"));
        assertNull(StrLookup.mapLookup(map).apply(null));
        assertNull(StrLookup.mapLookup(map).apply(""));
        assertNull(StrLookup.mapLookup(map).apply("other"));
    }

    @Test
    void testMapLookup_nullMap() {
        final Map<String, ?> map = null;
        assertNull(StrLookup.mapLookup(map).apply(null));
        assertNull(StrLookup.mapLookup(map).apply(""));
        assertNull(StrLookup.mapLookup(map).apply("any"));
    }

    @Test
    void testNoneLookup() {
        assertNull(StrLookup.noneLookup().apply(null));
        assertNull(StrLookup.noneLookup().apply(""));
        assertNull(StrLookup.noneLookup().apply("any"));
    }

    @Test
    void testResourceBundleLookup() {
        final ResourceBundle map = ResourceBundle.getBundle("org.apache.commons.text.example.testResourceBundleLookup");
        assertEquals("value", StrLookup.resourceBundleLookup(map).apply("key"));
        assertEquals("2", StrLookup.resourceBundleLookup(map).lookup("number"));
        assertNull(StrLookup.resourceBundleLookup(map).apply(null));
        assertNull(StrLookup.resourceBundleLookup(map).apply(""));
        assertNull(StrLookup.resourceBundleLookup(map).apply("other"));
    }

    @Test
    void testResourceBundleLookup_nullMap() {
        final ResourceBundle resourceBundle = null;
        assertNull(StrLookup.resourceBundleLookup(resourceBundle).apply(null));
        assertNull(StrLookup.resourceBundleLookup(resourceBundle).apply(""));
        assertNull(StrLookup.resourceBundleLookup(resourceBundle).apply("any"));
    }

    @Test
    void testSystemPropertiesLookup() {
        assertEquals(System.getProperty("os.name"), StrLookup.systemPropertiesLookup().apply("os.name"));
        assertNull(StrLookup.systemPropertiesLookup().apply(""));
        assertNull(StrLookup.systemPropertiesLookup().apply("other"));
        assertThrows(NullPointerException.class, () -> StrLookup.systemPropertiesLookup().apply(null));
    }

    /**
     * Tests that a lookup object for system properties can deal with a full
     * replacement of the system properties object. This test is related to
     * LANG-1055.
     */
    @Test
    void testSystemPropertiesLookupReplacedProperties() {
        final Properties oldProperties = System.getProperties();
        final String osName = "os.name";
        final String newOsName = oldProperties.getProperty(osName) + "_changed";

        final StrLookup<String> sysLookup = StrLookup.systemPropertiesLookup();
        final Properties newProps = new Properties();
        newProps.setProperty(osName, newOsName);
        System.setProperties(newProps);
        try {
            assertEquals(newOsName, sysLookup.apply(osName), "Changed properties not detected");
        } finally {
            System.setProperties(oldProperties);
        }
    }

    /**
     * Tests that a lookup object for system properties sees changes on system
     * properties. This test is related to LANG-1141.
     */
    @Test
    void testSystemPropertiesLookupUpdatedProperty() {
        final String osName = "os.name";
        final String oldOs = System.getProperty(osName);
        final String newOsName = oldOs + "_changed";

        final StrLookup<String> sysLookup = StrLookup.systemPropertiesLookup();
        System.setProperty(osName, newOsName);
        try {
            assertEquals(newOsName, sysLookup.apply(osName), "Changed properties not detected");
        } finally {
            System.setProperty(osName, oldOs);
        }
    }

}
