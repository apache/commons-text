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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link StringLookupFactory}.
 */
public class StringLookupFactoryTest {

    /**
     * Tests that we return the singleton.
     */
    @Test
    public void testSingletons() {
        final StringLookupFactory stringLookupFactory = StringLookupFactory.INSTANCE;
        Assertions.assertEquals(Base64DecoderStringLookup.INSTANCE, stringLookupFactory.base64StringLookup());
        Assertions.assertEquals(Base64EncoderStringLookup.INSTANCE, stringLookupFactory.base64StringLookup());
        Assertions.assertEquals(ConstantStringLookup.INSTANCE, stringLookupFactory.constantStringLookup());
        Assertions.assertEquals(DateStringLookup.INSTANCE, stringLookupFactory.dateStringLookup());
        Assertions.assertEquals(EnvironmentVariableStringLookup.INSTANCE,
                stringLookupFactory.environmentVariableStringLookup());
        Assertions.assertEquals(InterpolatorStringLookup.INSTANCE, stringLookupFactory.interpolatorStringLookup());
        Assertions.assertEquals(JavaPlatformStringLookup.INSTANCE, stringLookupFactory.javaPlatformStringLookup());
        Assertions.assertEquals(LocalHostStringLookup.INSTANCE, stringLookupFactory.localHostStringLookup());
        Assertions.assertEquals(NullStringLookup.INSTANCE, stringLookupFactory.nullStringLookup());
        Assertions.assertEquals(ResourceBundleStringLookup.INSTANCE, stringLookupFactory.resourceBundleStringLookup());
        Assertions.assertEquals(ScriptStringLookup.INSTANCE, stringLookupFactory.scriptStringLookup());
        Assertions.assertEquals(SystemPropertyStringLookup.INSTANCE, stringLookupFactory.systemPropertyStringLookup());
        Assertions.assertEquals(UrlDecoderStringLookup.INSTANCE, stringLookupFactory.urlDecoderStringLookup());
        Assertions.assertEquals(UrlEncoderStringLookup.INSTANCE, stringLookupFactory.urlEncoderStringLookup());
        Assertions.assertEquals(UrlStringLookup.INSTANCE, stringLookupFactory.urlStringLookup());
        Assertions.assertEquals(XmlStringLookup.INSTANCE, stringLookupFactory.xmlStringLookup());
    }
}
