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
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * A Path fence guards against using paths outside of a "fence" of made of root paths.
 *
 * Keep package-private.
 */
final class PathFence {

    /**
     * Builds {@link PathFence} instances.
     */
    static final class Builder implements Supplier<PathFence> {

        /** The empty Path array. */
        private static final Path[] EMPTY = {};

        /**
         * A fence is made of root Paths.
         */
        private Path[] roots = EMPTY;

        @Override
        public PathFence get() {
            return new PathFence(this);
        }

        /**
         * Sets the paths that delineate this fence.
         *
         * @param paths the paths that delineate this fence.
         * @return {@code this} instance.
         */
        Builder setRoots(final Path... paths) {
            this.roots = paths != null ? paths.clone() : EMPTY;
            return this;
        }
    }

    /**
     * Creates a new builder.
     *
     * @return a new builder.
     */
    static Builder builder() {
        return new Builder();
    }

    /**
     * A fence is made of Paths guarding Path resolution.
     */
    private final List<Path> roots;

    /**
     * Constructs a new instance.
     *
     * @param builder A builder.
     */
    private PathFence(final Builder builder) {
        this.roots = Arrays.stream(builder.roots).map(Path::toAbsolutePath).collect(Collectors.toList());
    }

    /**
     * Gets a Path for the given file name checking that it resolves within our fence.
     *
     * @param fileName the file name to resolve.
     * @return a fenced Path.
     * @throws IllegalArgumentException if the file name is not without our fence.
     */
    Path apply(final String fileName) {
        final Path path = Paths.get(fileName);
        if (roots.isEmpty()) {
            return path;
        }
        final Path pathAbs = path.normalize().toAbsolutePath();
        final Optional<Path> first = roots.stream().filter(pathAbs::startsWith).findFirst();
        if (first.isPresent()) {
            return path;
        }
        throw new IllegalArgumentException(String.format("[%s] -> [%s] not in the fence %s", fileName, pathAbs, roots));
    }

}
