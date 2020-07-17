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

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link FileStringLookup}.
 */
public class FileStringLookupTest {

    @Test
    public void testBadCharsetName() {
        assertThrows(IllegalArgumentException.class, () -> {
            FileStringLookup.INSTANCE.lookup("BAD_CHARSET_NAME:src/test/resources/document.properties");
        });
    }

    @Test
    public void testBadDocumentPath() {
        assertThrows(IllegalArgumentException.class, () -> {
            FileStringLookup.INSTANCE.lookup("BAD_CHARSET_NAME:src/test/resources/DOCUMENT_NOT_FOUND.TXT");
        });
    }

    @Test
    public void testMissingFilePart() {
        assertThrows(IllegalArgumentException.class, () -> {
            FileStringLookup.INSTANCE.lookup(StandardCharsets.UTF_8.name());
        });
    }

    @Test
    public void testNull() {
        Assertions.assertNull(FileStringLookup.INSTANCE.lookup(null));
    }

    @Test
    public void testOne() throws Exception {
        final byte[] expectedBytes = Files.readAllBytes(Paths.get("src/test/resources/document.properties"));
        final String expectedString = new String(expectedBytes, StandardCharsets.UTF_8);
        Assertions.assertEquals(expectedString,
            FileStringLookup.INSTANCE.lookup("UTF-8:src/test/resources/document.properties"));
    }

    @Test
    public void testToString() {
        // does not blow up and gives some kind of string.
        Assertions.assertFalse(FileStringLookup.INSTANCE.toString().isEmpty());
    }
}
