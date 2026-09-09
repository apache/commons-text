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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Resolves the external resources of an XML document from within a {@link PathFence}.
 * <p>
 * A document fetched from a fence may legitimately reference a follow-up resource, an external DTD subset or an external entity, living in the same roots.
 * </p>
 *
 * Keep package-private.
 */
final class PathFenceResolver implements EntityResolver {

    /**
     * Converts a system identifier to a local Path.
     *
     * @param systemId An absolute 'file:' URI, may be null.
     * @return A Path, or null if the system identifier is empty.
     * @throws SAXException If the system identifier is not a valid `file:` URI.
     */
    private static Path toPath(final String systemId) throws SAXException {
        if (StringUtils.isEmpty(systemId)) {
            return null;
        }
        try {
            final URI uri = new URI(systemId);
            if (!"file".equals(uri.getScheme())) {
                throw new SAXParseException("Failed to read external document '" + systemId + "', because only 'file' access is allowed.", null, systemId, -1,
                        -1);
            }
            return Paths.get(uri);
        } catch (final URISyntaxException | IllegalArgumentException e) {
            throw new SAXParseException("Failed to read external document '" + systemId + "'.", null, systemId, -1, -1, e);
        }
    }

    /**
     * A fence is made of Paths guarding Path resolution.
     */
    private final PathFence fence;

    /**
     * Constructs a new instance.
     *
     * @param fence The fence guarding Path resolution.
     */
    PathFenceResolver(final PathFence fence) {
        this.fence = Objects.requireNonNull(fence, "fence");
    }

    /**
     * Resolves an external resource, opting it in when it resolves within our fence.
     *
     * @param publicId The public identifier, may be null.
     * @param systemId The system identifier, already absolutized by the caller, may be null.
     * @return An InputSource on the resource.
     * @throws SAXException if the system identifier names a file outside our fence.
     * @throws IOException  if the resource cannot be read.
     */
    @Override
    public InputSource resolveEntity(final String publicId, final String systemId) throws SAXException, IOException {
        final Path path = toPath(systemId);
        if (path == null) {
            return null;
        }
        final Path fenced;
        try {
            fenced = fence.apply(path.toString());
        } catch (final IllegalArgumentException e) {
            throw new SAXException(e);
        }
        final InputSource inputSource = new InputSource(Files.newInputStream(fenced));
        inputSource.setPublicId(publicId);
        inputSource.setSystemId(systemId);
        return inputSource;
    }
}
