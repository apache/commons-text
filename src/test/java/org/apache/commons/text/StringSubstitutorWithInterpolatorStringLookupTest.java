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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.text.lookup.StringLookup;
import org.apache.commons.text.lookup.StringLookupFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringSubstitutorWithInterpolatorStringLookupTest {

    @Test
    public void testCustomMapWithDefaults() {
        testCustomMapWithDefaults(true);
    }

    private void testCustomMapWithDefaults(final boolean addDefaultLookups) {
        final String key = "key";
        final String value = "value";
        final Map<String, String> map = new HashMap<>();
        map.put(key, value);
        final StringLookup mapStringLookup = StringLookupFactory.INSTANCE.mapStringLookup(map);
        final Map<String, StringLookup> stringLookupMap = new HashMap<>();
        stringLookupMap.put("customLookup", mapStringLookup);
        final StringSubstitutor strSubst = new StringSubstitutor(
                StringLookupFactory.INSTANCE.interpolatorStringLookup(stringLookupMap, null, addDefaultLookups));
        if (addDefaultLookups) {
            final String spKey = "user.name";
            Assertions.assertEquals(System.getProperty(spKey), strSubst.replace("${sys:" + spKey + "}"));
        }
        Assertions.assertEquals("value", strSubst.replace("${customLookup:key}"));
    }

    @Test
    public void testCustomMapWithoutDefaults() {
        testCustomMapWithDefaults(false);
    }

    @Test
    public void testDefaultValueForMissingKeyInResourceBundle() {
        final StringLookup interpolatorStringLookup = StringLookupFactory.INSTANCE.interpolatorStringLookup(
                StringLookupFactory.INSTANCE.resourceBundleStringLookup("testResourceBundleLookup"));
        assertEquals("${missingKey:-defaultValue}", interpolatorStringLookup.lookup("keyWithMissingKey"));
        final StringSubstitutor stringSubstitutor = new StringSubstitutor(interpolatorStringLookup);
        // The following would throw a MissingResourceException before TEXT-165.
        assertEquals("defaultValue", stringSubstitutor.replace("${keyWithMissingKey}"));
    }

    @Test
    void testAll() {
        // @formatter:off
        StringSubstitutor.createInterpolator().replace(
          "OS name: ${sys:os.name}, " + 
          "3 + 4 = ${script:javascript:3 + 4}");
        // @formatter:on
    }

    @Test
    public void testJavaScript() {
        Assertions.assertEquals("Hello World!",
                StringSubstitutor.createInterpolator().replace("${script:javascript:\"Hello World!\"}"));
        Assertions.assertEquals("7", StringSubstitutor.createInterpolator().replace("${script:javascript:3 + 4}"));
    }

    @Test
    public void testLocalHostLookup_Address() throws UnknownHostException {
        final StringSubstitutor strSubst = StringSubstitutor.createInterpolator();
        Assertions.assertEquals(InetAddress.getLocalHost().getHostAddress(), strSubst.replace("${localhost:address}"));
    }

    @Test
    public void testLocalHostLookup_CanonicalName() throws UnknownHostException {
        final StringSubstitutor strSubst = StringSubstitutor.createInterpolator();
        Assertions.assertEquals(InetAddress.getLocalHost().getCanonicalHostName(),
                strSubst.replace("${localhost:canonical-name}"));
    }

    @Test
    public void testLocalHostLookup_Name() throws UnknownHostException {
        final StringSubstitutor strSubst = StringSubstitutor.createInterpolator();
        Assertions.assertEquals(InetAddress.getLocalHost().getHostName(), strSubst.replace("${localhost:name}"));
    }

    @Test
    public void testMapAndSystemProperty() {
        final String key = "key";
        final String value = "value";
        final Map<String, String> map = new HashMap<>();
        map.put(key, value);
        final StringSubstitutor strSubst = new StringSubstitutor(
                StringLookupFactory.INSTANCE.interpolatorStringLookup(map));
        final String spKey = "user.name";
        Assertions.assertEquals(System.getProperty(spKey), strSubst.replace("${sys:" + spKey + "}"));
        Assertions.assertEquals(value, strSubst.replace("${" + key + "}"));
    }

    @Test
    public void testSystemProperty() {
        final StringSubstitutor strSubst = StringSubstitutor.createInterpolator();
        final String spKey = "user.name";
        Assertions.assertEquals(System.getProperty(spKey), strSubst.replace("${sys:" + spKey + "}"));
    }

}
