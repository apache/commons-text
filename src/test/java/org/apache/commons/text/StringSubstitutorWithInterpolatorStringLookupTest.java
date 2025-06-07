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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.text.lookup.DefaultStringLookup;
import org.apache.commons.text.lookup.StringLookup;
import org.apache.commons.text.lookup.StringLookupFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringSubstitutorWithInterpolatorStringLookupTest {

    private static StringLookup createInterpolatorWithLookups(final DefaultStringLookup... lookups) {
        final Map<String, StringLookup> lookupMap = new HashMap<>();
        for (final DefaultStringLookup lookup : lookups) {
            lookupMap.put(lookup.getKey().toLowerCase(), lookup.getStringLookup());
        }

        return StringLookupFactory.INSTANCE.interpolatorStringLookup(lookupMap, null, false);
    }

    @Test
    void testCustomFunctionWithDefaults() {
        testCustomFunctionWithDefaults(true);
    }

    private void testCustomFunctionWithDefaults(final boolean addDefaultLookups) {
        final String key = "key";
        final String value = "value";
        final Map<String, String> map = new HashMap<>();
        map.put(key, value);
        final StringLookup mapStringLookup = StringLookupFactory.INSTANCE.functionStringLookup(map::get);
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
    void testCustomFunctionWithoutDefaults() {
        testCustomFunctionWithDefaults(false);
    }

    @Test
    void testCustomMapWithDefaults() {
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
        Assertions.assertEquals("${UnknownLookup:key}", strSubst.replace("${UnknownLookup:key}"));
    }

    @Test
    void testCustomMapWithoutDefaults() {
        testCustomMapWithDefaults(false);
    }
    @Test
    void testDefaultInterpolator() {
        // Used to cut and paste into the docs.
        // @formatter:off
        final StringSubstitutor interpolator = StringSubstitutor.createInterpolator();
        final String text = interpolator.replace(
                "Base64 Decoder:        ${base64Decoder:SGVsbG9Xb3JsZCE=}\n"
              + "Base64 Encoder:        ${base64Encoder:HelloWorld!}\n"
              + "Java Constant:         ${const:java.awt.event.KeyEvent.VK_ESCAPE}\n"
              + "Date:                  ${date:yyyy-MM-dd}\n"
              + "Environment Variable:  ${env:USERNAME}\n"
              + "File Content:          ${file:UTF-8:src/test/resources/org/apache/commons/text/document.properties}\n"
              + "Java:                  ${java:version}\n"
              + "Localhost:             ${localhost:canonical-name}\n"
              + "Properties File:       ${properties:src/test/resources/org/apache/commons/text/document.properties::mykey}\n"
              + "Resource Bundle:       ${resourceBundle:org.apache.commons.text.example.testResourceBundleLookup:mykey}\n"
              + "System Property:       ${sys:user.dir}\n"
              + "URL Decoder:           ${urlDecoder:Hello%20World%21}\n"
              + "URL Encoder:           ${urlEncoder:Hello World!}\n"
              + "XML XPath:             ${xml:src/test/resources/org/apache/commons/text/document.xml:/root/path/to/node}\n"
        );
        // @formatter:on
        Assertions.assertNotNull(text);
        // TEXT-171:
        Assertions.assertFalse(text.contains("${base64Decoder:SGVsbG9Xb3JsZCE=}"));
        Assertions.assertFalse(text.contains("${base64Encoder:HelloWorld!}"));
        Assertions.assertFalse(text.contains("${urlDecoder:Hello%20World%21}"));
        Assertions.assertFalse(text.contains("${urlEncoder:Hello World!}"));
        Assertions.assertFalse(text.contains("${resourceBundle:org.apache.commons.text.example.testResourceBundleLookup:mykey}"));
    }

    @Test
    void testDefaultValueForMissingKeyInResourceBundle() {
        final StringLookup interpolatorStringLookup = StringLookupFactory.INSTANCE.interpolatorStringLookup(
            StringLookupFactory.INSTANCE.resourceBundleStringLookup("org.apache.commons.text.example.testResourceBundleLookup"));
        assertEquals("${missingKey:-defaultValue}", interpolatorStringLookup.apply("keyWithMissingKey"));
        assertEquals("${missingKey:-defaultValue}", interpolatorStringLookup.lookup("keyWithMissingKey"));
        final StringSubstitutor stringSubstitutor = new StringSubstitutor(interpolatorStringLookup);
        // The following would throw a MissingResourceException before TEXT-165.
        assertEquals("defaultValue", stringSubstitutor.replace("${keyWithMissingKey}"));
    }

    @Test
    void testDnsLookup() throws UnknownHostException {
        final StringSubstitutor strSubst =
                new StringSubstitutor(createInterpolatorWithLookups(DefaultStringLookup.DNS));
        final String hostName = InetAddress.getLocalHost().getHostName();
        Assertions.assertEquals(InetAddress.getByName(hostName).getHostAddress(),
            strSubst.replace("${dns:" + hostName + "}"));
    }

    @Test
    void testDnsLookup_disabledByDefault() throws UnknownHostException {
        final StringSubstitutor strSubst = StringSubstitutor.createInterpolator();
        final String hostName = InetAddress.getLocalHost().getHostName();
        final String input = "${dns:" + hostName + "}";
        Assertions.assertEquals(input, strSubst.replace(input));
    }

    @Test
    void testDnsLookupAddress() throws UnknownHostException {
        final StringSubstitutor strSubst =
                new StringSubstitutor(createInterpolatorWithLookups(DefaultStringLookup.DNS));
        Assertions.assertEquals(InetAddress.getByName("apache.org").getHostAddress(),
            strSubst.replace("${dns:address|apache.org}"));
    }

    @Test
    void testDnsLookupCanonicalName() throws UnknownHostException {
        final StringSubstitutor strSubst =
                new StringSubstitutor(createInterpolatorWithLookups(DefaultStringLookup.DNS));
        final String address = InetAddress.getLocalHost().getHostAddress();
        final InetAddress inetAddress = InetAddress.getByName(address);
        Assertions.assertEquals(inetAddress.getCanonicalHostName(),
            strSubst.replace("${dns:canonical-name|" + address + "}"));
    }

    @Test
    void testDnsLookupName() throws UnknownHostException {
        final StringSubstitutor strSubst =
                new StringSubstitutor(createInterpolatorWithLookups(DefaultStringLookup.DNS));
        final String address = InetAddress.getLocalHost().getHostAddress();
        final InetAddress inetAddress = InetAddress.getByName(address);
        Assertions.assertEquals(inetAddress.getHostName(), strSubst.replace("${dns:name|" + address + "}"));
    }

    @Test
    void testDnsLookupNameUntrimmed() throws UnknownHostException {
        final StringSubstitutor strSubst =
                new StringSubstitutor(createInterpolatorWithLookups(DefaultStringLookup.DNS));
        final String address = InetAddress.getLocalHost().getHostAddress();
        final InetAddress inetAddress = InetAddress.getByName(address);
        Assertions.assertEquals(inetAddress.getHostName(), strSubst.replace("${dns:name| " + address + " }"));
    }

    @Test
    void testDnsLookupUnknown() {
        final StringSubstitutor strSubst =
                new StringSubstitutor(createInterpolatorWithLookups(DefaultStringLookup.DNS));
        final String unknown = "${dns: u n k n o w n}";
        Assertions.assertEquals(unknown, strSubst.replace(unknown));
    }

    @Test
    void testJavaScript() {
        final StringSubstitutor strSubst =
                new StringSubstitutor(createInterpolatorWithLookups(DefaultStringLookup.SCRIPT));

        Assertions.assertEquals("Hello World!", strSubst.replace("${script:javascript:\"Hello World!\"}"));
        Assertions.assertEquals("7", strSubst.replace("${script:javascript:3 + 4}"));
    }

    @Test
    void testJavaScript_disabledByDefault() {
        final StringSubstitutor strSubst = StringSubstitutor.createInterpolator();

        Assertions.assertEquals("${script:javascript:3 + 4}", strSubst.replace("${script:javascript:3 + 4}"));
    }

    @Test
    void testLocalHostLookup_Address() throws UnknownHostException {
        final StringSubstitutor strSubst = StringSubstitutor.createInterpolator();
        Assertions.assertEquals(InetAddress.getLocalHost().getHostAddress(), strSubst.replace("${localhost:address}"));
    }

    @Test
    void testLocalHostLookup_CanonicalName() throws UnknownHostException {
        final StringSubstitutor strSubst = StringSubstitutor.createInterpolator();
        Assertions.assertEquals(InetAddress.getLocalHost().getCanonicalHostName(),
            strSubst.replace("${localhost:canonical-name}"));
    }

    @Test
    void testLocalHostLookup_Name() throws UnknownHostException {
        final StringSubstitutor strSubst = StringSubstitutor.createInterpolator();
        Assertions.assertEquals(InetAddress.getLocalHost().getHostName(), strSubst.replace("${localhost:name}"));
    }

    @Test
    void testMapAndSystemProperty() {
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
    void testSystemProperty() {
        final StringSubstitutor strSubst = StringSubstitutor.createInterpolator();
        final String spKey = "user.name";
        Assertions.assertEquals(System.getProperty(spKey), strSubst.replace("${sys:" + spKey + "}"));
    }

    @Test
    void testSystemPropertyDefaultStringLookup() {
        final StringSubstitutor strSubst = new StringSubstitutor(
            StringLookupFactory.INSTANCE.interpolatorStringLookup(StringLookupFactory.INSTANCE.systemPropertyStringLookup()));
        final String spKey = "user.name";
        Assertions.assertEquals(System.getProperty(spKey), strSubst.replace("${" + spKey + "}"));
        Assertions.assertEquals(System.getProperty(spKey), strSubst.replace("${sys:" + spKey + "}"));
    }
}
