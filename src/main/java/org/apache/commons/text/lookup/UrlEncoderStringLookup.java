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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Encodes URL Strings using the UTF-8 encoding.
 *
 * @see URLEncoder
 * @since 1.5
 */
final class UrlEncoderStringLookup extends AbstractStringLookup {

    /**
     * Defines the singleton for this class.
     */
    static final UrlEncoderStringLookup INSTANCE = new UrlEncoderStringLookup();

    /**
     * Constructs a new instance.
     */
    private UrlEncoderStringLookup() {
        // empty
    }

    String encode(final String key, final String enc) throws UnsupportedEncodingException {
        return URLEncoder.encode(key, enc);
    }

    @Override
    public String lookup(final String key) {
        if (key == null) {
            return null;
        }
        final String enc = StandardCharsets.UTF_8.name();
        try {
            return encode(key, enc);
        } catch (final UnsupportedEncodingException e) {
            // Can't happen since UTF-8 is required by the Java specification.
            throw IllegalArgumentExceptions.format(e, "%s: source=%s, encoding=%s", e, key, enc);
        }
    }

}
