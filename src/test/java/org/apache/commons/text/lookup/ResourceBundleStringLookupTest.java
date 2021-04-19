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

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ResourceBundle;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link ResourceBundleStringLookup}.
 */
public class ResourceBundleStringLookupTest {

    private static final String KEY = "key";
    private static final String TEST_RESOURCE_BUNDLE = "org.example.testResourceBundleLookup";

    @Test
    public void testAny() {
        final String bundleName = TEST_RESOURCE_BUNDLE;
        final String bundleKey = KEY;
        Assertions.assertEquals(ResourceBundle.getBundle(bundleName).getString(bundleKey),
            ResourceBundleStringLookup.INSTANCE.lookup(AbstractStringLookup.toLookupKey(bundleName, bundleKey)));
    }

    @Test
    public void testBadKey() {
        final String bundleName = TEST_RESOURCE_BUNDLE;
        final String bundleKey = "bad_key";
        assertNull(new ResourceBundleStringLookup(bundleName).lookup(bundleKey));
        assertNull(ResourceBundleStringLookup.INSTANCE.lookup(AbstractStringLookup.toLookupKey(bundleName, bundleKey)));
    }

    @Test
    public void testBadNames() {
        assertNull(ResourceBundleStringLookup.INSTANCE
            .lookup(AbstractStringLookup.toLookupKey("BAD_RESOURCE_BUNDLE_NAME", "KEY")));
    }

    @Test
    public void testDoubleBundle() {
        assertThrows(IllegalArgumentException.class, () -> new ResourceBundleStringLookup(TEST_RESOURCE_BUNDLE)
            .lookup(AbstractStringLookup.toLookupKey("OtherBundle", KEY)));
    }

    @Test
    public void testExceptionGettingString() {
        final ResourceBundleStringLookup mockLookup = spy(ResourceBundleStringLookup.class);
        when(mockLookup.getString(TEST_RESOURCE_BUNDLE, KEY)).thenThrow(ClassCastException.class);
        assertThrows(IllegalArgumentException.class, () -> mockLookup.lookup(AbstractStringLookup.toLookupKey(TEST_RESOURCE_BUNDLE, KEY)));
    }

    @Test
    public void testMissingKeyInSpec() {
        assertThrows(IllegalArgumentException.class, () -> ResourceBundleStringLookup.INSTANCE.lookup(TEST_RESOURCE_BUNDLE + ":"));
    }

    @Test
    public void testNull() {
        Assertions.assertNull(ResourceBundleStringLookup.INSTANCE.lookup(null));
    }

    @Test
    public void testOne() {
        Assertions.assertEquals(ResourceBundle.getBundle(TEST_RESOURCE_BUNDLE).getString(KEY),
            new ResourceBundleStringLookup(TEST_RESOURCE_BUNDLE).lookup(KEY));
    }

    @Test
    public void testToString() {
        // does not blow up and gives some kind of string.
        Assertions.assertFalse(ResourceBundleStringLookup.INSTANCE.toString().isEmpty());
    }

}
