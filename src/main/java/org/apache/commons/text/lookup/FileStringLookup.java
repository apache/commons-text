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

import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;

/**
 * Looks up file contents.
 * <p>
 * Using a {@link StringLookup} from the {@link StringLookupFactory}:
 * </p>
 *
 * <pre>
 * StringLookupFactory.INSTANCE.fileStringLookup().lookup("UTF-8:com/domain/document.properties");
 * </pre>
 * <p>
 * The above example converts {@code "UTF-8:com/domain/document.properties"} to the UTF-8 contents of the file at {@code com/domain/document.properties}.
 * </p>
 * <p>
 * Using a {@link StringSubstitutor}:
 * </p>
 *
 * <pre>
 * StringSubstitutor.createInterpolator().replace("... ${file:UTF-8:com/domain/document.properties} ..."));
 * </pre>
 * <p>
 * The above example converts {@code "UTF-8:SomePath"} to the UTF-8 contents of the file at {@code SomePath}.
 * </p>
 * <p>
 * Public access is through {@link StringLookupFactory}.
 * </p>
 *
 * @see StringLookupFactory
 * @since 1.5
 */
final class FileStringLookup extends AbstractPathFencedLookup {

    /**
     * Defines the singleton for this class.
     */
    static final AbstractStringLookup INSTANCE = new FileStringLookup((Path[]) null);

    /**
     * Constructs a new instance.
     *
     * @param fences The fences guarding Path resolution.
     */
    FileStringLookup(final Path... fences) {
        super(fences);
    }

    /**
     * Looks up the value for the key in the format "charsetName:DocumentPath".
     * <p>
     * For example: "UTF-8:com/domain/document.properties".
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
        final String[] keys = key.split(String.valueOf(SPLIT_CH));
        final int keyLen = keys.length;
        if (keyLen < 2) {
            throw IllegalArgumentExceptions.format("Bad file key format [%s], expected format is CharsetName:DocumentPath.", key);
        }
        final String charsetName = keys[0];
        final String fileName = StringUtils.substringAfter(key, SPLIT_CH);
        try {
            return new String(Files.readAllBytes(getPath(fileName)), charsetName);
        } catch (final Exception e) {
            throw IllegalArgumentExceptions.format(e, "Error looking up file [%s] with charset [%s].", fileName, charsetName);
        }
    }

}
