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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Abstracts guarding Path lookups with fences.
 */
abstract class AbstractPathFencedLookup extends AbstractStringLookup {

    /**
     * A fence is made of Paths guarding Path resolution.
     */
    protected final List<Path> fences;

    /**
     * Constructs a new instance.
     *
     * @param fences The fences guarding Path resolution.
     */
    AbstractPathFencedLookup(final Path... fences) {
        this.fences = fences != null ? Arrays.stream(fences).map(Path::toAbsolutePath).collect(Collectors.toList()) : Collections.emptyList();
    }

    /**
     * Gets a Path for the given file name checking that it resolves within our fence.
     *
     * @param fileName the file name to resolve.
     * @return a fenced Path.
     * @throws IllegalArgumentException if the file name is not without our fence.
     */
    protected Path getPath(final String fileName) {
        final Path path = Paths.get(fileName);
        if (fences.isEmpty()) {
            return path;
        }
        final Path pathAbs = path.normalize().toAbsolutePath();
        final Optional<Path> first = fences.stream().filter(pathAbs::startsWith).findFirst();
        if (first.isPresent()) {
            return path;
        }
        throw IllegalArgumentExceptions.format("[%s] -> [%s] not in %s", fileName, pathAbs, fences);
    }

}
