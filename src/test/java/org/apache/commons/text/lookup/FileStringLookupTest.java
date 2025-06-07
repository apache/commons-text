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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link FileStringLookup}.
 */
public class FileStringLookupTest {

    private static final Path DOCUEMENT_PATH = Paths.get("src/test/resources/org/apache/commons/text/document.properties");
    private static final Path CURRENT_PATH = Paths.get(StringUtils.EMPTY);

    public static String readDocumentFixtureString() throws IOException {
        return new String(Files.readAllBytes(DOCUEMENT_PATH), StandardCharsets.UTF_8);
    }

    public static void testFence(final String expectedString, final FileStringLookup fileStringLookup) {
        Assertions.assertEquals(expectedString, fileStringLookup.apply("UTF-8:src/test/resources/org/apache/commons/text/document.properties"));
        assertThrows(IllegalArgumentException.class, () -> fileStringLookup.apply("UTF-8:/src/test/resources/org/apache/commons/text/document.properties"));
        assertThrows(IllegalArgumentException.class, () -> fileStringLookup.apply("UTF-8:../src/test/resources/org/apache/commons/text/document.properties"));
    }

    public static void testFence(final StringSubstitutor stringSubstitutor) throws IOException {
        assertEquals(readDocumentFixtureString(), stringSubstitutor.replace("${file:UTF-8:" + DOCUEMENT_PATH + "}"));
        assertThrows(IllegalArgumentException.class, () -> stringSubstitutor.replace("${file:UTF-8:/foo.txt}"));
        assertThrows(IllegalArgumentException.class, () -> stringSubstitutor.replace("${file:UTF-8:../foo.txt}"));
    }

    @Test
    void testDefaultInstanceBadCharsetName() {
        assertThrows(IllegalArgumentException.class,
                () -> FileStringLookup.INSTANCE.apply("BAD_CHARSET_NAME:src/test/resources/org/apache/commons/text/document.properties"));
    }

    @Test
    void testDefaultInstanceBadDocumentPath() {
        assertThrows(IllegalArgumentException.class, () -> FileStringLookup.INSTANCE.apply("BAD_CHARSET_NAME:src/test/resources/DOCUMENT_NOT_FOUND.TXT"));
    }

    @Test
    void testDefaultInstanceMissingFilePart() {
        assertThrows(IllegalArgumentException.class, () -> FileStringLookup.INSTANCE.apply(StandardCharsets.UTF_8.name()));
    }

    @Test
    void testDefaultInstanceNull() {
        Assertions.assertNull(FileStringLookup.INSTANCE.apply(null));
    }

    @Test
    void testDefaultInstanceOne() throws Exception {
        final String expectedString = readDocumentFixtureString();
        Assertions.assertEquals(expectedString, FileStringLookup.INSTANCE.apply("UTF-8:src/test/resources/org/apache/commons/text/document.properties"));
    }

    @Test
    void testDefaultInstanceToString() {
        // does not blow up and gives some kind of string.
        Assertions.assertFalse(FileStringLookup.INSTANCE.toString().isEmpty());
    }

    @Test
    void testFenceBadDirOne() throws Exception {
        final FileStringLookup fileStringLookup = new FileStringLookup(Paths.get("dir does not exist at all"));
        assertThrows(IllegalArgumentException.class, () -> fileStringLookup.apply("UTF-8:src/test/resources/org/apache/commons/text/document.properties"));
        assertThrows(IllegalArgumentException.class, () -> fileStringLookup.apply("UTF-8:/src/test/resources/org/apache/commons/text/document.properties"));
        assertThrows(IllegalArgumentException.class, () -> fileStringLookup.apply("UTF-8:../src/test/resources/org/apache/commons/text/document.properties"));
    }

    @Test
    void testFenceBadDirPlusGoodOne() throws Exception {
        final String expectedString = readDocumentFixtureString();
        final FileStringLookup fileStringLookup = new FileStringLookup(Paths.get("dir does not exist at all"), CURRENT_PATH);
        testFence(expectedString, fileStringLookup);
    }

    @Test
    void testFenceCurrentDirOne() throws Exception {
        final String expectedString = readDocumentFixtureString();
        final FileStringLookup fileStringLookup = new FileStringLookup(CURRENT_PATH);
        testFence(expectedString, fileStringLookup);
    }

    @Test
    void testFenceCurrentDirPlusOne() throws Exception {
        final String expectedString = readDocumentFixtureString();
        final FileStringLookup fileStringLookup = new FileStringLookup(Paths.get("target"), CURRENT_PATH);
        testFence(expectedString, fileStringLookup);
    }

    @Test
    void testFenceEmptyOne() throws Exception {
        final String expectedString = readDocumentFixtureString();
        Assertions.assertEquals(expectedString, new FileStringLookup().apply("UTF-8:src/test/resources/org/apache/commons/text/document.properties"));
    }

    @Test
    void testFenceNullOne() throws Exception {
        final String expectedString = readDocumentFixtureString();
        Assertions.assertEquals(expectedString,
                new FileStringLookup((Path[]) null).apply("UTF-8:src/test/resources/org/apache/commons/text/document.properties"));
    }

    @Test
    void testInterpolatorReplace() throws IOException {
        final StringSubstitutor stringSubstitutor = StringSubstitutor.createInterpolator();
        assertEquals(readDocumentFixtureString(), stringSubstitutor.replace("${file:UTF-8:" + DOCUEMENT_PATH + "}"));
        final InterpolatorStringLookup stringLookup = (InterpolatorStringLookup) stringSubstitutor.getStringLookup();
        stringLookup.getStringLookupMap().replace(StringLookupFactory.KEY_FILE, StringLookupFactory.INSTANCE.fileStringLookup(CURRENT_PATH));
        testFence(stringSubstitutor);
    }
}
