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

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests {@link PathFence}.
 */
class PathFenceTest {

    private static final String SECRET = "TOP-SECRET";

    @TempDir
    private Path tempDir;

    /** Aborts the test where the platform cannot create symbolic links, for example Windows without developer mode. */
    private void createSymbolicLink(final Path link, final Path target) {
        try {
            Files.createSymbolicLink(link, target);
        } catch (final IOException | UnsupportedOperationException e) {
            Assumptions.abort("This platform cannot create symbolic links: " + e);
        }
    }

    @Test
    void testPathInsideFenceIsAccepted() throws IOException {
        final Path inside = Files.createDirectories(tempDir.resolve("in"));
        final Path file = Files.write(inside.resolve("ok.txt"), "INSIDE".getBytes(StandardCharsets.UTF_8));
        final StringLookup lookup = StringLookupFactory.builder().setFences(inside).get().fileStringLookup();
        assertEquals("INSIDE", lookup.lookup("UTF-8:" + file));
    }

    @Test
    void testRelativeTraversalOutOfFenceIsRejected() throws IOException {
        final Path inside = Files.createDirectories(tempDir.resolve("in"));
        final Path outside = Files.createDirectories(tempDir.resolve("out"));
        Files.write(outside.resolve("secret.txt"), SECRET.getBytes(StandardCharsets.UTF_8));
        final StringLookup lookup = StringLookupFactory.builder().setFences(inside).get().fileStringLookup();
        assertThrows(IllegalArgumentException.class, () -> lookup.lookup("UTF-8:" + inside.resolve("../out/secret.txt")));
    }

    @Test
    void testSymbolicLinkInsideFenceIsRejected() throws IOException {
        final Path inside = Files.createDirectories(tempDir.resolve("in"));
        final Path outside = Files.createDirectories(tempDir.resolve("out"));
        final Path secret = Files.write(outside.resolve("secret.txt"), SECRET.getBytes(StandardCharsets.UTF_8));
        final Path link = inside.resolve("link.txt");
        createSymbolicLink(link, secret);
        final StringLookup lookup = StringLookupFactory.builder().setFences(inside).get().fileStringLookup();
        assertThrows(IllegalArgumentException.class, () -> lookup.lookup("UTF-8:" + link));
    }

    @Test
    void testSymbolicLinkFenceRootResolvesToItsTarget() throws IOException {
        final Path real = Files.createDirectories(tempDir.resolve("real"));
        final Path file = Files.write(real.resolve("ok.txt"), "INSIDE".getBytes(StandardCharsets.UTF_8));
        final Path linkedRoot = tempDir.resolve("linked");
        createSymbolicLink(linkedRoot, real);
        final StringLookup lookup = StringLookupFactory.builder().setFences(linkedRoot).get().fileStringLookup();
        assertEquals("INSIDE", lookup.lookup("UTF-8:" + file));
    }
}
