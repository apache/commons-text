/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package org.apache.commons.text.lookup;

import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link DefaultStringLookup}.
 */
class DefaultStringLookupTest {

    @Test
    void testEnumValues() {
        final Map<String, StringLookup> stringLookupMap = new HashMap<>();
        StringLookupFactory.INSTANCE.addDefaultStringLookups(stringLookupMap);
        // Loop through all enums
        for (final DefaultStringLookup stringLookup : DefaultStringLookup.values()) {
            assertSame(stringLookupMap.get(stringLookup.getKey()), stringLookupMap.get(stringLookup.getKey()));
        }
    }

    @Test
    void testIndividualEnums() {
        assertSame(DefaultStringLookup.BASE64_DECODER.getStringLookup(),
            StringLookupFactory.INSTANCE.base64DecoderStringLookup());
        assertSame(DefaultStringLookup.BASE64_ENCODER.getStringLookup(),
            StringLookupFactory.INSTANCE.base64EncoderStringLookup());
        assertSame(DefaultStringLookup.CONST.getStringLookup(), StringLookupFactory.INSTANCE.constantStringLookup());
        assertSame(DefaultStringLookup.DATE.getStringLookup(), StringLookupFactory.INSTANCE.dateStringLookup());
        assertSame(DefaultStringLookup.DNS.getStringLookup(), StringLookupFactory.INSTANCE.dnsStringLookup());
        assertSame(DefaultStringLookup.ENVIRONMENT.getStringLookup(),
            StringLookupFactory.INSTANCE.environmentVariableStringLookup());
        assertSame(DefaultStringLookup.FILE.getStringLookup(), StringLookupFactory.INSTANCE.fileStringLookup());
        assertSame(DefaultStringLookup.JAVA.getStringLookup(), StringLookupFactory.INSTANCE.javaPlatformStringLookup());
        assertSame(DefaultStringLookup.LOCAL_HOST.getStringLookup(),
            StringLookupFactory.INSTANCE.localHostStringLookup());
        assertSame(DefaultStringLookup.PROPERTIES.getStringLookup(),
            StringLookupFactory.INSTANCE.propertiesStringLookup());
        assertSame(DefaultStringLookup.RESOURCE_BUNDLE.getStringLookup(),
            StringLookupFactory.INSTANCE.resourceBundleStringLookup());
        assertSame(DefaultStringLookup.SCRIPT.getStringLookup(), StringLookupFactory.INSTANCE.scriptStringLookup());
        assertSame(DefaultStringLookup.SYSTEM_PROPERTIES.getStringLookup(),
            StringLookupFactory.INSTANCE.systemPropertyStringLookup());
        assertSame(DefaultStringLookup.URL.getStringLookup(), StringLookupFactory.INSTANCE.urlStringLookup());
        assertSame(DefaultStringLookup.URL_DECODER.getStringLookup(),
            StringLookupFactory.INSTANCE.urlDecoderStringLookup());
        assertSame(DefaultStringLookup.URL_ENCODER.getStringLookup(),
            StringLookupFactory.INSTANCE.urlEncoderStringLookup());
        assertSame(DefaultStringLookup.XML.getStringLookup(), StringLookupFactory.INSTANCE.xmlStringLookup());
    }

}
