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

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests {@link PathFence}.
 */
class PathFenceTest {

    @Test
    void testApplyReturnsCheckedPath(@TempDir final Path tempDir) {
        // The caller must open the path the fence checked, not the original one, since the OS may resolve ".." differently.
        final PathFence fence = PathFence.builder().setRoots(tempDir).get();
        assertEquals(tempDir.toAbsolutePath().normalize().resolve("b.txt"), fence.apply(tempDir.resolve("a/../b.txt").toString()));
    }
}
