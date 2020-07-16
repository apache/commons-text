/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */

package org.apache.commons.text.lookup;

import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;

/**
 * Looks up keys from an XML document.
 * <p>
 * Looks up the value for a given key in the format "Charset:URL".
 * </p>
 * <p>
 * For example: "UTF-8:com/domain/document.properties".
 * </p>
 *
 * @since 1.5
 */
final class UrlStringLookup extends AbstractStringLookup {

    /**
     * Defines the singleton for this class.
     */
    static final UrlStringLookup INSTANCE = new UrlStringLookup();

    /**
     * No need to build instances for now.
     */
    private UrlStringLookup() {
        // empty
    }

    /**
     * Looks up the value for the key in the format "DocumentPath:XPath".
     * <p>
     * For example: "com/domain/document.xml:/path/to/node".
     * </p>
     *
     * @param key
     *            the key to be looked up, may be null
     * @return The value associated with the key.
     */
    @Override
    public String lookup(final String key) {
        if (key == null) {
            return null;
        }
        final String[] keys = key.split(SPLIT_STR);
        final int keyLen = keys.length;
        if (keyLen < 2) {
            throw IllegalArgumentExceptions.format("Bad URL key format [%s]; expected format is DocumentPath:Key.",
                    key);
        }
        final String charsetName = keys[0];
        final String urlStr = StringUtils.substringAfter(key, SPLIT_CH);
        try {
            final URL url = new URL(urlStr);
            final int size = 8192;
            final StringWriter writer = new StringWriter(size);
            final char[] buffer = new char[size];
            try (BufferedInputStream bis = new BufferedInputStream(url.openStream());
                    InputStreamReader reader = new InputStreamReader(bis, charsetName)) {
                int n;
                while (-1 != (n = reader.read(buffer))) {
                    writer.write(buffer, 0, n);
                }
            }
            return writer.toString();
        } catch (final Exception e) {
            throw IllegalArgumentExceptions.format(e, "Error looking up URL [%s] with Charset [%s].", urlStr,
                    charsetName);
        }
    }

}
