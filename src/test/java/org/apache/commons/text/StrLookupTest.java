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
package org.apache.commons.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

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

    //-----------------------------------------------------------------------
    @Test
    public void testNoneLookup() {
        assertNull(StrLookup.noneLookup().lookup(null));
        assertNull(StrLookup.noneLookup().lookup(""));
        assertNull(StrLookup.noneLookup().lookup("any"));
    }

    @Test
    public void testSystemProperiesLookup() {
        assertEquals(System.getProperty("os.name"), StrLookup.systemPropertiesLookup().lookup("os.name"));
        assertNull(StrLookup.systemPropertiesLookup().lookup(""));
        assertNull(StrLookup.systemPropertiesLookup().lookup("other"));
        try {
            StrLookup.systemPropertiesLookup().lookup(null);
            fail("Exception expected!");
        } catch (final NullPointerException ex) {
            // expected
        }
    }

    /**
     * Tests that a lookup object for system properties can deal with a full
     * replacement of the system properties object. This test is related to
     * LANG-1055.
     */
    @Test
    public void testSystemPropertiesLookupReplacedProperties() {
        final Properties oldProperties = System.getProperties();
        final String osName = "os.name";
        final String newOsName = oldProperties.getProperty(osName) + "_changed";

        final StrLookup<String> sysLookup = StrLookup.systemPropertiesLookup();
        final Properties newProps = new Properties();
        newProps.setProperty(osName, newOsName);
        System.setProperties(newProps);
        try {
            assertEquals(newOsName, sysLookup.lookup(osName), "Changed properties not detected");
        } finally {
            System.setProperties(oldProperties);
        }
    }

    /**
     * Tests that a lookup object for system properties sees changes on system
     * properties. This test is related to LANG-1141.
     */
    @Test
    public void testSystemPropertiesLookupUpdatedProperty() {
        final String osName = "os.name";
        final String oldOs = System.getProperty(osName);
        final String newOsName = oldOs + "_changed";

        final StrLookup<String> sysLookup = StrLookup.systemPropertiesLookup();
        System.setProperty(osName, newOsName);
        try {
            assertEquals(newOsName, sysLookup.lookup(osName), "Changed properties not detected");
        } finally {
            System.setProperty(osName, oldOs);
        }
    }

    @Test
    public void testMapLookup() {
        final Map<String, Object> map = new HashMap<>();
        map.put("key", "value");
        map.put("number", 2);
        assertEquals("value", StrLookup.mapLookup(map).lookup("key"));
        assertEquals("2", StrLookup.mapLookup(map).lookup("number"));
        assertNull(StrLookup.mapLookup(map).lookup(null));
        assertNull(StrLookup.mapLookup(map).lookup(""));
        assertNull(StrLookup.mapLookup(map).lookup("other"));
    }

    @Test
    public void testResourceBundleLookup() {
        final ResourceBundle map = ResourceBundle.getBundle("testResourceBundleLookup");
        assertEquals("value", StrLookup.resourceBundleLookup(map).lookup("key"));
        assertEquals("2", StrLookup.resourceBundleLookup(map).lookup("number"));
        assertNull(StrLookup.resourceBundleLookup(map).lookup(null));
        assertNull(StrLookup.resourceBundleLookup(map).lookup(""));
        assertNull(StrLookup.resourceBundleLookup(map).lookup("other"));
    }

    @Test
    public void testResourceBundleLookup_nullMap() {
        final ResourceBundle resourceBundle = null;
        assertNull(StrLookup.resourceBundleLookup(resourceBundle).lookup(null));
        assertNull(StrLookup.resourceBundleLookup(resourceBundle).lookup(""));
        assertNull(StrLookup.resourceBundleLookup(resourceBundle).lookup("any"));
    }

    @Test
    public void testMapLookup_nullMap() {
        final Map<String, ?> map = null;
        assertNull(StrLookup.mapLookup(map).lookup(null));
        assertNull(StrLookup.mapLookup(map).lookup(""));
        assertNull(StrLookup.mapLookup(map).lookup("any"));
    }


}
