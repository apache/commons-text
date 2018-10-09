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

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ResourceBundle;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ResourceBundleStringLookupTest {

    private static final String TEST_RESOURCE_BUNDLE = "testResourceBundleLookup";

    @Test
    public void testAny() {
        final String bundleName = TEST_RESOURCE_BUNDLE;
        final String bundleKey = "key";
        Assertions.assertEquals(ResourceBundle.getBundle(bundleName).getString(bundleKey),
                ResourceBundleStringLookup.INSTANCE.lookup(bundleName + ":" + bundleKey));
    }

    @Test
    public void testBadKey() {
        final String bundleName = TEST_RESOURCE_BUNDLE;
        final String bundleKey = "bad_key";
        assertThrows(IllegalArgumentException.class, () -> {
            new ResourceBundleStringLookup(bundleName).lookup(bundleKey);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            ResourceBundleStringLookup.INSTANCE.lookup(bundleName + ":" + bundleKey);
        });
    }

    @Test
    public void testMissingKeyInSpec() {
        final String bundleName = TEST_RESOURCE_BUNDLE;
        assertThrows(IllegalArgumentException.class, () -> {
            ResourceBundleStringLookup.INSTANCE.lookup(bundleName + ":");
        });
    }

    @Test
    public void testName() {
        assertThrows(IllegalArgumentException.class, () -> {
            ResourceBundleStringLookup.INSTANCE.lookup("BAD_RESOURCE_BUNDLE_NAME:KEY");
        });
    }

    @Test
    public void testNull() {
        Assertions.assertNull(ResourceBundleStringLookup.INSTANCE.lookup(null));
    }

    @Test
    public void testOne() {
        final String bundleName = TEST_RESOURCE_BUNDLE;
        final String bundleKey = "key";
        Assertions.assertEquals(ResourceBundle.getBundle(bundleName).getString(bundleKey),
                new ResourceBundleStringLookup(bundleName).lookup(bundleKey));
    }

}
