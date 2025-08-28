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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.xml.XMLConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.InputSource;

/**
 * Looks up keys from an XML document.
 * <p>
 * Looks up the value for a given key in the format "Document:XPath".
 * </p>
 * <p>
 * For example: "com/domain/document.xml:/path/to/node".
 * </p>
 *
 * @since 1.5
 */
final class XmlStringLookup extends AbstractPathFencedLookup {

    /**
     * Defines default XPath factory features.
     */
    static final Map<String, Boolean> DEFAULT_FEATURES;

    static {
        DEFAULT_FEATURES = new HashMap<>(1);
        DEFAULT_FEATURES.put(XMLConstants.FEATURE_SECURE_PROCESSING, Boolean.TRUE);
    }

    /**
     * Defines the singleton for this class.
     */
    static final XmlStringLookup INSTANCE = new XmlStringLookup(DEFAULT_FEATURES, (Path[]) null);

    /**
     * Defines XPath factory features.
     */
    private final Map<String, Boolean> xPathFactoryFeatures;

    /**
     * No need to build instances for now.
     *
     * @param xPathFactoryFeatures XPathFactory features to set.
     * @see XPathFactory#setFeature(String, boolean)
     */
    XmlStringLookup(final Map<String, Boolean> xPathFactoryFeatures, final Path... fences) {
        super(fences);
        this.xPathFactoryFeatures = Objects.requireNonNull(xPathFactoryFeatures, "xPathFactoryFeatures");
    }

    /**
     * Looks up the value for the key in the format "DocumentPath:XPath".
     * <p>
     * For example: "com/domain/document.xml:/path/to/node".
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
        final String[] keys = key.split(SPLIT_STR);
        final int keyLen = keys.length;
        if (keyLen != 2) {
            throw IllegalArgumentExceptions.format("Bad XML key format [%s]; expected format is DocumentPath:XPath.",
                    key);
        }
        final String documentPath = keys[0];
        final String xpath = StringUtils.substringAfter(key, SPLIT_CH);
        try (InputStream inputStream = Files.newInputStream(getPath(documentPath))) {
            final XPathFactory factory = XPathFactory.newInstance();
            for (final Entry<String, Boolean> p : xPathFactoryFeatures.entrySet()) {
                factory.setFeature(p.getKey(), p.getValue());
            }
            return factory.newXPath().evaluate(xpath, new InputSource(inputStream));
        } catch (final Exception e) {
            throw IllegalArgumentExceptions.format(e, "Error looking up XML document [%s] and XPath [%s].",
                    documentPath, xpath);
        }
    }

}
