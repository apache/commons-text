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

import java.nio.file.Path;

/**
 * Abstracts string lookup that guards Path lookups with a fence.
 */
abstract class AbstractPathFencedLookup extends AbstractStringLookup {

    /**
     * A fence is made of Paths guarding Path resolution.
     */
    protected final PathFence fence;

    /**
     * Constructs a new instance.
     *
     * @param paths The fences guarding Path resolution.
     */
    AbstractPathFencedLookup(final Path... paths) {
        this.fence = PathFence.builder().setRoots(paths).get();
    }

    /**
     * Gets a Path for the given file name checking that it resolves within our fence.
     *
     * @param fileName the file name to resolve.
     * @return a fenced Path.
     * @throws IllegalArgumentException if the file name is not without our fence.
     */
    protected Path getPath(final String fileName) {
        return fence.apply(fileName);
    }
}
