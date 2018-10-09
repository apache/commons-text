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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link StringLookupFactory}.
 */
public class StringLookupFactoryTest {

    public static void assertDefaultKeys(final Map<String, StringLookup> stringLookupMap) {
        assertTrue(stringLookupMap.containsKey(StringLookupFactory.KEY_BASE64_DECODER));
        assertTrue(stringLookupMap.containsKey(StringLookupFactory.KEY_BASE64_ENCODER));
        assertTrue(stringLookupMap.containsKey(StringLookupFactory.KEY_CONST));
        assertTrue(stringLookupMap.containsKey(StringLookupFactory.KEY_DATE));
        assertTrue(stringLookupMap.containsKey(StringLookupFactory.KEY_ENV));
        assertTrue(stringLookupMap.containsKey(StringLookupFactory.KEY_FILE));
        assertTrue(stringLookupMap.containsKey(StringLookupFactory.KEY_JAVA));
        assertTrue(stringLookupMap.containsKey(StringLookupFactory.KEY_LOCALHOST));
        assertTrue(stringLookupMap.containsKey(StringLookupFactory.KEY_PROPERTIES));
        assertTrue(stringLookupMap.containsKey(StringLookupFactory.KEY_RESOURCE_BUNDLE));
        assertTrue(stringLookupMap.containsKey(StringLookupFactory.KEY_SCRIPT));
        assertTrue(stringLookupMap.containsKey(StringLookupFactory.KEY_SYS));
        assertTrue(stringLookupMap.containsKey(StringLookupFactory.KEY_URL));
        assertTrue(stringLookupMap.containsKey(StringLookupFactory.KEY_URL_DECODER));
        assertTrue(stringLookupMap.containsKey(StringLookupFactory.KEY_URL_ENCODER));
        assertTrue(stringLookupMap.containsKey(StringLookupFactory.KEY_XML));
    }

    @Test
    public void addDefaultStringLookupsMap() {
        final Map<String, StringLookup> stringLookupMap = new HashMap<>();
        StringLookupFactory.INSTANCE.addDefaultStringLookups(stringLookupMap);
        assertDefaultKeys(stringLookupMap);
    }

    @Test
    public void addDefaultStringLookupsNull() {
        StringLookupFactory.INSTANCE.addDefaultStringLookups(null);
    }

    /**
     * Tests that we return the singleton.
     */
    @Test
    public void testSingletons() {
        final StringLookupFactory stringLookupFactory = StringLookupFactory.INSTANCE;
        Assertions.assertSame(Base64DecoderStringLookup.INSTANCE, stringLookupFactory.base64DecoderStringLookup());
        Assertions.assertSame(Base64EncoderStringLookup.INSTANCE, stringLookupFactory.base64EncoderStringLookup());
        Assertions.assertSame(ConstantStringLookup.INSTANCE, stringLookupFactory.constantStringLookup());
        Assertions.assertSame(DateStringLookup.INSTANCE, stringLookupFactory.dateStringLookup());
        Assertions.assertSame(EnvironmentVariableStringLookup.INSTANCE,
                stringLookupFactory.environmentVariableStringLookup());
        Assertions.assertSame(InterpolatorStringLookup.INSTANCE, stringLookupFactory.interpolatorStringLookup());
        Assertions.assertSame(JavaPlatformStringLookup.INSTANCE, stringLookupFactory.javaPlatformStringLookup());
        Assertions.assertSame(LocalHostStringLookup.INSTANCE, stringLookupFactory.localHostStringLookup());
        Assertions.assertSame(NullStringLookup.INSTANCE, stringLookupFactory.nullStringLookup());
        Assertions.assertSame(ResourceBundleStringLookup.INSTANCE, stringLookupFactory.resourceBundleStringLookup());
        Assertions.assertSame(ScriptStringLookup.INSTANCE, stringLookupFactory.scriptStringLookup());
        Assertions.assertSame(SystemPropertyStringLookup.INSTANCE, stringLookupFactory.systemPropertyStringLookup());
        Assertions.assertSame(UrlDecoderStringLookup.INSTANCE, stringLookupFactory.urlDecoderStringLookup());
        Assertions.assertSame(UrlEncoderStringLookup.INSTANCE, stringLookupFactory.urlEncoderStringLookup());
        Assertions.assertSame(UrlStringLookup.INSTANCE, stringLookupFactory.urlStringLookup());
        Assertions.assertSame(XmlStringLookup.INSTANCE, stringLookupFactory.xmlStringLookup());
    }
}
