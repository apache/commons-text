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
    public void testCustomFunctionWithDefaults() {
        testCustomFunctionWithDefaults(true);
    }

    private void testCustomFunctionWithDefaults(final boolean addDefaultLookups) {
        final String key = "key";
        final String value = "value";
        final Map<String, String> map = new HashMap<>();
        map.put(key, value);
        final StringLookup mapStringLookup = StringLookupFactory.INSTANCE.functionStringLookup(k -> map.get(k));
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
    public void testCustomFunctionWithoutDefaults() {
        testCustomFunctionWithDefaults(false);
    }

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
        Assertions.assertEquals("${UnknownLookup:key}", strSubst.replace("${UnknownLookup:key}"));
    }

    @Test
    public void testCustomMapWithoutDefaults() {
        testCustomMapWithDefaults(false);
    }

    @Test
    public void testDefaultInterpolator() {
        // Used to cut and paste into the docs.
        // @formatter:off
        final StringSubstitutor interpolator = StringSubstitutor.createInterpolator();
        interpolator.setEnableSubstitutionInVariables(true); // Allows for nested $'s.
        final String text = interpolator.replace(
                "Base64 Decoder:        ${base64Decoder:SGVsbG9Xb3JsZCE=}\n"
              + "Base64 Encoder:        ${base64Encoder:HelloWorld!}\n"
              + "Java Constant:         ${const:java.awt.event.KeyEvent.VK_ESCAPE}\n"
              + "Date:                  ${date:yyyy-MM-dd}\n"
              + "DNS:                   ${dns:address|apache.org}\n"
              + "Environment Variable:  ${env:USERNAME}\n"
              + "File Content:          ${file:UTF-8:src/test/resources/document.properties}\n"
              + "Java:                  ${java:version}\n"
              + "Localhost:             ${localhost:canonical-name}\n"
              + "Properties File:       ${properties:src/test/resources/document.properties::mykey}\n"
              + "Resource Bundle:       ${resourceBundle:org.example.testResourceBundleLookup:mykey}\n"
              + "Script:                ${script:javascript:3 + 4}\n"
              + "System Property:       ${sys:user.dir}\n"
              + "URL Decoder:           ${urlDecoder:Hello%20World%21}\n"
              + "URL Encoder:           ${urlEncoder:Hello World!}\n"
              + "URL Content (HTTP):    ${url:UTF-8:http://www.apache.org}\n"
              + "URL Content (HTTPS):   ${url:UTF-8:https://www.apache.org}\n"
              + "URL Content (File):    ${url:UTF-8:file:///${sys:user.dir}/src/test/resources/document.properties}\n"
              + "XML XPath:             ${xml:src/test/resources/document.xml:/root/path/to/node}\n"
        );
        // @formatter:on
        Assertions.assertNotNull(text);
        // TEXT-171:
        Assertions.assertFalse(text.contains("${base64Decoder:SGVsbG9Xb3JsZCE=}"));
        Assertions.assertFalse(text.contains("${base64Encoder:HelloWorld!}"));
        Assertions.assertFalse(text.contains("${urlDecoder:Hello%20World%21}"));
        Assertions.assertFalse(text.contains("${urlEncoder:Hello World!}"));
        Assertions.assertFalse(text.contains("${resourceBundle:org.example.testResourceBundleLookup:mykey}"));
        // System.out.println(text);
    }
    @Test
    public void testDefaultValueForMissingKeyInResourceBundle() {
        final StringLookup interpolatorStringLookup = StringLookupFactory.INSTANCE.interpolatorStringLookup(
            StringLookupFactory.INSTANCE.resourceBundleStringLookup("org.example.testResourceBundleLookup"));
        assertEquals("${missingKey:-defaultValue}", interpolatorStringLookup.lookup("keyWithMissingKey"));
        final StringSubstitutor stringSubstitutor = new StringSubstitutor(interpolatorStringLookup);
        // The following would throw a MissingResourceException before TEXT-165.
        assertEquals("defaultValue", stringSubstitutor.replace("${keyWithMissingKey}"));
    }

    @Test
    public void testDnsLookup() throws UnknownHostException {
        final StringSubstitutor strSubst = StringSubstitutor.createInterpolator();
        final String hostName = InetAddress.getLocalHost().getHostName();
        Assertions.assertEquals(InetAddress.getByName(hostName).getHostAddress(),
            strSubst.replace("${dns:" + hostName + "}"));
    }

    @Test
    public void testDnsLookupAddress() throws UnknownHostException {
        final StringSubstitutor strSubst = StringSubstitutor.createInterpolator();
        Assertions.assertEquals(InetAddress.getByName("apache.org").getHostAddress(),
            strSubst.replace("${dns:address|apache.org}"));
    }

    @Test
    public void testDnsLookupCanonicalName() throws UnknownHostException {
        final StringSubstitutor strSubst = StringSubstitutor.createInterpolator();
        final String address = InetAddress.getLocalHost().getHostAddress();
        final InetAddress inetAddress = InetAddress.getByName(address);
        Assertions.assertEquals(inetAddress.getCanonicalHostName(),
            strSubst.replace("${dns:canonical-name|" + address + "}"));
    }

    @Test
    public void testDnsLookupName() throws UnknownHostException {
        final StringSubstitutor strSubst = StringSubstitutor.createInterpolator();
        final String address = InetAddress.getLocalHost().getHostAddress();
        final InetAddress inetAddress = InetAddress.getByName(address);
        Assertions.assertEquals(inetAddress.getHostName(), strSubst.replace("${dns:name|" + address + "}"));
    }

    @Test
    public void testDnsLookupNameUntrimmed() throws UnknownHostException {
        final StringSubstitutor strSubst = StringSubstitutor.createInterpolator();
        final String address = InetAddress.getLocalHost().getHostAddress();
        final InetAddress inetAddress = InetAddress.getByName(address);
        Assertions.assertEquals(inetAddress.getHostName(), strSubst.replace("${dns:name| " + address + " }"));
    }

    @Test
    public void testDnsLookupUnknown() {
        final StringSubstitutor strSubst = StringSubstitutor.createInterpolator();
        final String unknown = "${dns: u n k n o w n}";
        Assertions.assertEquals(unknown, strSubst.replace(unknown));
    }

    @Test
    public void testJavaScript() {
        Assertions.assertEquals("Hello World!",
                StringSubstitutor.createInterpolator().replace("${script:javascript:\"Hello World!\"}"));
        Assertions.assertEquals("7",
                StringSubstitutor.createInterpolator().replace("${script:javascript:3 + 4}"));
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
