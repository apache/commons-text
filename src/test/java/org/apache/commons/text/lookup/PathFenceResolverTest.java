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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Tests {@link PathFenceResolver}.
 */
class PathFenceResolverTest {

    private static final String DATA = "Hello World!";
    private static final Path CURRENT_PATH = Paths.get(StringUtils.EMPTY); // NOT "."

    /**
     * Reads an InputSource byte stream as UTF-8.
     */
    private static String read(final InputSource inputSource) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (InputStream in = inputSource.getByteStream()) {
            final byte[] buffer = new byte[1024];
            for (int len = in.read(buffer); len != -1; len = in.read(buffer)) {
                out.write(buffer, 0, len);
            }
        }
        return new String(out.toByteArray(), StandardCharsets.UTF_8);
    }

    private static PathFenceResolver resolver(final Path... roots) {
        return new PathFenceResolver(PathFence.builder().setRoots(roots).get());
    }

    /**
     * Writes a file and gives back its path relative to the working directory.
     */
    private static Path write(final Path path, final String content) throws IOException {
        Files.createDirectories(path.getParent());
        Files.write(path, content.getBytes(StandardCharsets.UTF_8));
        return CURRENT_PATH.toAbsolutePath().relativize(path.toAbsolutePath());
    }

    @Test
    void testAbsentFileInFence(@TempDir final Path tempDir) {
        assertThrows(IOException.class, () -> resolver(tempDir).resolveEntity(null, tempDir.resolve("absent.txt").toUri().toString()));
    }

    @Test
    void testDrivePathIsRefused(@TempDir final Path tempDir) {
        // A bare Windows drive path is not a 'file:' URI, so it is refused like any other non-file system identifier.
        assertThrows(SAXException.class, () -> resolver(tempDir).resolveEntity(null, "C:/does-not-matter.txt"));
    }

    @Test
    void testEmptySystemId(@TempDir final Path tempDir) throws Exception {
        assertNull(resolver(tempDir).resolveEntity(null, StringUtils.EMPTY));
    }

    @Test
    void testFileUrlInFence(@TempDir final Path tempDir) throws Exception {
        final Path target = write(tempDir.resolve("entity.txt"), DATA);
        final InputSource inputSource = resolver(tempDir).resolveEntity("publicId", target.toUri().toString());
        assertNotNull(inputSource);
        assertEquals("publicId", inputSource.getPublicId());
        assertEquals(DATA, read(inputSource));
    }

    @Test
    void testFileUrlOutsideFence(@TempDir final Path tempDir) throws Exception {
        final Path target = write(tempDir.resolve("out/entity.txt"), DATA);
        final SAXException e = assertThrows(SAXException.class, () -> resolver(tempDir.resolve("in")).resolveEntity(null, target.toUri().toString()));
        assertInstanceOf(IllegalArgumentException.class, e.getCause());
    }

    @Test
    void testNullFence() {
        assertThrows(NullPointerException.class, () -> new PathFenceResolver(null));
    }

    @Test
    void testNullSystemId(@TempDir final Path tempDir) throws Exception {
        assertNull(resolver(tempDir).resolveEntity(null, null));
    }

    @Test
    void testPercentEncodedFileUrlInFence(@TempDir final Path tempDir) throws Exception {
        final Path target = write(tempDir.resolve("na me.txt"), DATA);
        final InputSource inputSource = resolver(tempDir).resolveEntity(null, target.toUri().toString());
        assertNotNull(inputSource);
        assertEquals(DATA, read(inputSource));
    }

    @Test
    void testRelativeSystemIdIsRefused(@TempDir final Path tempDir) throws Exception {
        // XmlStringLookup parses with the document's own URI, so a relative system identifier always reaches us absolutized.
        final Path target = write(tempDir.resolve("entity.txt"), DATA);
        assertThrows(SAXException.class, () -> resolver(tempDir).resolveEntity(null, target.toString().replace('\\', '/')));
    }

    @Test
    void testRemoteSystemIdIsRefused(@TempDir final Path tempDir) {
        // A remote reference names no path, so the fence can never opt it in.
        assertThrows(SAXException.class, () -> resolver(tempDir).resolveEntity(null, "http://localhost:1/entity.txt"));
        assertThrows(SAXException.class, () -> resolver(tempDir).resolveEntity(null, "https://localhost:1/entity.txt"));
        assertThrows(SAXException.class, () -> resolver(tempDir).resolveEntity(null, "ftp://localhost:1/entity.txt"));
        assertThrows(SAXException.class, () -> resolver(tempDir).resolveEntity(null, "jar:file:/lib.jar!/entity.txt"));
    }

    @Test
    void testSystemIdIsEchoed(@TempDir final Path tempDir) throws Exception {
        final Path target = write(tempDir.resolve("entity.txt"), DATA);
        final String systemId = target.toUri().toString();
        assertEquals(systemId, resolver(tempDir).resolveEntity(null, systemId).getSystemId());
    }
}
