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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link UrlStringLookup}.
 */
class UrlStringLookupTest {

    @Test
    void testBadCharsetName() {
        assertThrows(IllegalArgumentException.class, () -> UrlStringLookup.INSTANCE.apply("BAD_CHARSET_NAME:BAD_URL"));
    }

    @Test
    void testBadEncoding() {
        assertThrows(IllegalArgumentException.class, () -> UrlStringLookup.INSTANCE.apply("FOO:https://www.google.com"));
    }

    @Test
    void testBadUrl() {
        assertThrows(IllegalArgumentException.class, () -> UrlStringLookup.INSTANCE.apply("UTF-8:BAD_URL"));
    }

    @Test
    void testFileScheme() throws Exception {
        final Path path = Paths.get("src/test/resources/org/apache/commons/text/document.properties");
        final URI uri = path.toUri();
        // System.out.println(uri);
        final byte[] expectedBytes = Files.readAllBytes(path);
        final String expectedString = new String(expectedBytes, StandardCharsets.UTF_8);
        assertEquals(expectedString, UrlStringLookup.INSTANCE.apply("UTF-8:" + uri.toString()));
    }

    @Test
    void testHttpScheme() {
        assertNotNull(UrlStringLookup.INSTANCE.apply("UTF-8:https://www.apache.org"));
        assertNotNull(UrlStringLookup.INSTANCE.apply("UTF-8:https://www.google.com"));
    }

    @Test
    void testMissingUrl() {
        assertThrows(IllegalArgumentException.class, () -> UrlStringLookup.INSTANCE.apply("UTF-8"));
    }

    @Test
    void testNull() {
        assertNull(UrlStringLookup.INSTANCE.apply(null));
    }

    @Test
    void testToString() {
        // does not blow up and gives some kind of string.
        assertFalse(UrlStringLookup.INSTANCE.toString().isEmpty());
    }

}
