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

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * Looks up keys from a properties file.
 * <p>
 * Looks up the value for a given key in the format "Document::Key".
 * </p>
 * <p>
 * Note the use of "::" instead of ":" to allow for "C:" drive letters in paths.
 * </p>
 * <p>
 * For example: "com/domain/document.properties:key".
 * </p>
 *
 * @see Properties
 * @since 1.5
 */
final class PropertiesStringLookup extends AbstractPathFencedLookup {

    /**
     * Defines the singleton for this class.
     */
    static final PropertiesStringLookup INSTANCE = new PropertiesStringLookup((Path[]) null);

    /** Separates file and key. */
    static final String SEPARATOR = "::";

    /**
     * Creates a lookup key for a given file and key.
     */
    static String toPropertyKey(final String file, final String key) {
        return AbstractStringLookup.toLookupKey(file, SEPARATOR, key);
    }

    /**
     * Constructs a new instance.
     *
     * @param fences The fences guarding Path resolution.
     */
    PropertiesStringLookup(final Path... fences) {
        super(fences);
    }

    /**
     * Looks up the value for the key in the format "DocumentPath:XPath".
     * <p>
     * For example: "com/domain/document.xml::/path/to/node".
     * </p>
     * <p>
     * Note the use of "::" instead of ":" to allow for "C:" drive letters in paths.
     * </p>
     *
     * @param key the key to be looked up, may be null
     * @return The value associated with the key.
     */
    @Override
    public String lookup(final String key) {
        if (key == null) {
            return null;
        }
        final String[] keys = key.split(SEPARATOR);
        final int keyLen = keys.length;
        if (keyLen < 2) {
            throw IllegalArgumentExceptions.format("Bad properties key format [%s]; expected format is %s.", key,
                toPropertyKey("DocumentPath", "Key"));
        }
        final String documentPath = keys[0];
        final String propertyKey = StringUtils.substringAfter(key, SEPARATOR);
        try {
            final Properties properties = new Properties();
            try (InputStream inputStream = Files.newInputStream(getPath(documentPath))) {
                properties.load(inputStream);
            }
            return properties.getProperty(propertyKey);
        } catch (final Exception e) {
            throw IllegalArgumentExceptions.format(e, "Error looking up properties [%s] and key [%s].", documentPath,
                propertyKey);
        }
    }

}
