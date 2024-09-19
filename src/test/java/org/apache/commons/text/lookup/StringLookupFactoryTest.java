/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package org.apache.commons.text.lookup;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.XMLConstants;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link StringLookupFactory}.
 */
public class StringLookupFactoryTest {

    public static void assertDefaultKeys(final Map<String, StringLookup> stringLookupMap) {
        // included
        assertMappedLookups(stringLookupMap,
                "base64",
                StringLookupFactory.KEY_BASE64_DECODER,
                StringLookupFactory.KEY_BASE64_ENCODER,
                StringLookupFactory.KEY_CONST,
                StringLookupFactory.KEY_DATE,
                StringLookupFactory.KEY_ENV,
                StringLookupFactory.KEY_FILE,
                StringLookupFactory.KEY_JAVA,
                StringLookupFactory.KEY_LOCALHOST,
                StringLookupFactory.KEY_LOOPBACK_ADDRESS,
                StringLookupFactory.KEY_PROPERTIES,
                StringLookupFactory.KEY_RESOURCE_BUNDLE,
                StringLookupFactory.KEY_SYS,
                StringLookupFactory.KEY_URL_DECODER,
                StringLookupFactory.KEY_URL_ENCODER,
                StringLookupFactory.KEY_XML,
                StringLookupFactory.KEY_XML_DECODER,
                StringLookupFactory.KEY_XML_ENCODER);
    }

    private static void assertMappedLookups(final Map<String, StringLookup> lookupMap, final String... keys) {
        final Set<String> remainingKeys = new HashSet<>(lookupMap.keySet());

        for (final String key : keys) {
            final String normalizedKey = StringLookupFactory.toKey(key);
            Assertions.assertNotNull(normalizedKey, () -> "Expected map to contain string lookup for key " + key);

            remainingKeys.remove(normalizedKey);
        }

        Assertions.assertTrue(remainingKeys.isEmpty(), () -> "Unexpected keys in lookup map: " + remainingKeys);
    }

    private static void checkDefaultStringLookupsHolder(final Properties props, final String... keys) {
        final StringLookupFactory.DefaultStringLookupsHolder holder =
                new StringLookupFactory.DefaultStringLookupsHolder(props);

        final Map<String, StringLookup> lookupMap = holder.getDefaultStringLookups();

        assertMappedLookups(lookupMap, keys);
    }

    /**
     * Main method used to verify the default string lookups resolved during JVM execution.
     * @param args
     */
    public static void main(final String[] args) {
        final Map<String, StringLookup> lookupMap = new HashMap<>();
        StringLookupFactory.INSTANCE.addDefaultStringLookups(lookupMap);

        System.out.println("Default string lookups");
        for (final String key : lookupMap.keySet()) {
            System.out.println("- " + key);
        }
    }

    @Test
    public void testAddDefaultStringLookupsMap() {
        final Map<String, StringLookup> stringLookupMap = new HashMap<>();
        StringLookupFactory.INSTANCE.addDefaultStringLookups(stringLookupMap);
        assertDefaultKeys(stringLookupMap);
    }

    @Test
    public void testAddDefaultStringLookupsNull() {
        StringLookupFactory.INSTANCE.addDefaultStringLookups(null);
    }

    @Test
    public void testDefaultStringLookupsHolder_allLookups() {
        final Properties props = new Properties();
        props.setProperty(StringLookupFactory.DEFAULT_STRING_LOOKUPS_PROPERTY,
                "BASE64_DECODER BASE64_ENCODER const, date, dns, environment "
                + "file ,java, local_host properties, resource_bundle,script,system_properties "
                + "url url_decoder  , url_encoder, xml");

        checkDefaultStringLookupsHolder(props,
                "base64",
                StringLookupFactory.KEY_BASE64_DECODER,
                StringLookupFactory.KEY_BASE64_ENCODER,
                StringLookupFactory.KEY_CONST,
                StringLookupFactory.KEY_DATE,
                StringLookupFactory.KEY_ENV,
                StringLookupFactory.KEY_FILE,
                StringLookupFactory.KEY_JAVA,
                StringLookupFactory.KEY_LOCALHOST,
                StringLookupFactory.KEY_LOOPBACK_ADDRESS,
                StringLookupFactory.KEY_PROPERTIES,
                StringLookupFactory.KEY_RESOURCE_BUNDLE,
                StringLookupFactory.KEY_SYS,
                StringLookupFactory.KEY_URL_DECODER,
                StringLookupFactory.KEY_URL_ENCODER,
                StringLookupFactory.KEY_XML,

                StringLookupFactory.KEY_DNS,
                StringLookupFactory.KEY_URL,
                StringLookupFactory.KEY_SCRIPT);
    }

    @Test
    public void testDefaultStringLookupsHolder_givenSingleLookup() {
        final Properties props = new Properties();
        props.setProperty(StringLookupFactory.DEFAULT_STRING_LOOKUPS_PROPERTY, "base64_encoder");

        checkDefaultStringLookupsHolder(props,
                "base64",
                StringLookupFactory.KEY_BASE64_ENCODER);
    }

    @Test
    public void testDefaultStringLookupsHolder_givenSingleLookup_weirdString() {
        final Properties props = new Properties();
        props.setProperty(StringLookupFactory.DEFAULT_STRING_LOOKUPS_PROPERTY, " \n \t  ,, DnS , , ");

        checkDefaultStringLookupsHolder(props, StringLookupFactory.KEY_DNS);
    }

    @Test
    public void testDefaultStringLookupsHolder_invalidLookupsDefinition() {
        final Properties props = new Properties();
        props.setProperty(StringLookupFactory.DEFAULT_STRING_LOOKUPS_PROPERTY, "base64_encoder nope");

        final Exception exc = Assertions.assertThrows(IllegalArgumentException.class,
                () -> new StringLookupFactory.DefaultStringLookupsHolder(props));
        Assertions.assertEquals("Invalid default string lookups definition: base64_encoder nope", exc.getMessage());
    }

    @Test
    public void testDefaultStringLookupsHolder_lookupsPropertyEmptyAndBlank() {
        final Properties propsWithNull = new Properties();
        propsWithNull.setProperty(StringLookupFactory.DEFAULT_STRING_LOOKUPS_PROPERTY, "");

        checkDefaultStringLookupsHolder(propsWithNull);

        final Properties propsWithBlank = new Properties();
        propsWithBlank.setProperty(StringLookupFactory.DEFAULT_STRING_LOOKUPS_PROPERTY, " ");

        checkDefaultStringLookupsHolder(propsWithBlank);
    }

    @Test
    public void testDefaultStringLookupsHolder_lookupsPropertyNotPresent() {
        checkDefaultStringLookupsHolder(new Properties(),
                "base64",
                StringLookupFactory.KEY_BASE64_DECODER,
                StringLookupFactory.KEY_BASE64_ENCODER,
                StringLookupFactory.KEY_CONST,
                StringLookupFactory.KEY_DATE,
                StringLookupFactory.KEY_ENV,
                StringLookupFactory.KEY_FILE,
                StringLookupFactory.KEY_JAVA,
                StringLookupFactory.KEY_LOCALHOST,
                StringLookupFactory.KEY_LOOPBACK_ADDRESS,
                StringLookupFactory.KEY_PROPERTIES,
                StringLookupFactory.KEY_RESOURCE_BUNDLE,
                StringLookupFactory.KEY_SYS,
                StringLookupFactory.KEY_URL_DECODER,
                StringLookupFactory.KEY_URL_ENCODER,
                StringLookupFactory.KEY_XML,
                StringLookupFactory.KEY_XML_DECODER,
                StringLookupFactory.KEY_XML_ENCODER);
    }

    @Test
    public void testDefaultStringLookupsHolder_multipleLookups() {
        final Properties props = new Properties();
        props.setProperty(StringLookupFactory.DEFAULT_STRING_LOOKUPS_PROPERTY, "dns, url script ");

        checkDefaultStringLookupsHolder(props,
                StringLookupFactory.KEY_DNS,
                StringLookupFactory.KEY_URL,
                StringLookupFactory.KEY_SCRIPT);
    }

    /**
     * Tests that we return the singleton.
     */
    @Test
    public void testSingletons() {
        final StringLookupFactory stringLookupFactory = StringLookupFactory.INSTANCE;
        Assertions.assertSame(StringLookupFactory.INSTANCE_BASE64_DECODER,
            stringLookupFactory.base64DecoderStringLookup());
        Assertions.assertSame(StringLookupFactory.INSTANCE_BASE64_ENCODER,
            stringLookupFactory.base64EncoderStringLookup());
        Assertions.assertSame(ConstantStringLookup.INSTANCE, stringLookupFactory.constantStringLookup());
        Assertions.assertSame(DateStringLookup.INSTANCE, stringLookupFactory.dateStringLookup());
        Assertions.assertSame(DnsStringLookup.INSTANCE, stringLookupFactory.dnsStringLookup());
        Assertions.assertSame(StringLookupFactory.INSTANCE_ENVIRONMENT_VARIABLES,
            stringLookupFactory.environmentVariableStringLookup());
        Assertions.assertSame(InterpolatorStringLookup.INSTANCE, stringLookupFactory.interpolatorStringLookup());
        Assertions.assertSame(JavaPlatformStringLookup.INSTANCE, stringLookupFactory.javaPlatformStringLookup());
        Assertions.assertSame(InetAddressStringLookup.LOCAL_HOST, stringLookupFactory.localHostStringLookup());
        Assertions.assertSame(InetAddressStringLookup.LOOPACK_ADDRESS, stringLookupFactory.loopbackAddressStringLookup());
        Assertions.assertSame(StringLookupFactory.INSTANCE_NULL, stringLookupFactory.nullStringLookup());
        Assertions.assertSame(ResourceBundleStringLookup.INSTANCE, stringLookupFactory.resourceBundleStringLookup());
        Assertions.assertSame(ScriptStringLookup.INSTANCE, stringLookupFactory.scriptStringLookup());
        Assertions.assertSame(StringLookupFactory.INSTANCE_SYSTEM_PROPERTIES,
            stringLookupFactory.systemPropertyStringLookup());
        Assertions.assertSame(UrlDecoderStringLookup.INSTANCE, stringLookupFactory.urlDecoderStringLookup());
        Assertions.assertSame(UrlEncoderStringLookup.INSTANCE, stringLookupFactory.urlEncoderStringLookup());
        Assertions.assertSame(UrlStringLookup.INSTANCE, stringLookupFactory.urlStringLookup());
        Assertions.assertSame(XmlStringLookup.INSTANCE, stringLookupFactory.xmlStringLookup());
        Assertions.assertSame(XmlDecoderStringLookup.INSTANCE, stringLookupFactory.xmlDecoderStringLookup());
        Assertions.assertSame(XmlEncoderStringLookup.INSTANCE, stringLookupFactory.xmlEncoderStringLookup());
    }

    @Test
    public void testXmlStringLookup() {
        final StringLookupFactory stringLookupFactory = StringLookupFactory.INSTANCE;
        final HashMap<String, Boolean> features = new HashMap<>(1);
        features.put(XMLConstants.FEATURE_SECURE_PROCESSING, Boolean.TRUE);
        XmlStringLookupTest.assertLookup(stringLookupFactory.xmlStringLookup(features));
        XmlStringLookupTest.assertLookup(stringLookupFactory.xmlStringLookup(new HashMap<>()));
    }
}
