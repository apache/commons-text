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
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemProperties;
import org.w3c.dom.Document;

/**
 * Looks up values in an XML document in the format {@code "DocumentPath:XPath"}.
 * <p>
 * For example:
 * </p>
 * <ul>
 * <li>{@code "com/domain/document.xml:/path/to/node"}</li>
 * </ul>
 * <p>
 * Secure processing is enabled by default and can be overridden with the system property {@code "XmlStringLookup.secure"} set to {@code false}. The secure
 * boolean String parsing follows the syntax defined by {@link Boolean#parseBoolean(String)}.
 * </p>
 *
 * @since 1.5
 */
final class XmlStringLookup extends AbstractPathFencedLookup {

    /**
     * The number of key parts.
     */
    private static final int KEY_PARTS_LEN = 2;

    /**
     * Defines default XPath factory features.
     */
    static final Map<String, Boolean> DEFAULT_XPATH_FEATURES;

    /**
     * Defines default XML factory features.
     */
    static final Map<String, Boolean> DEFAULT_XML_FEATURES;
    static {
        DEFAULT_XPATH_FEATURES = new HashMap<>(1);
        DEFAULT_XPATH_FEATURES.put(XMLConstants.FEATURE_SECURE_PROCESSING, Boolean.TRUE);
        DEFAULT_XML_FEATURES = new HashMap<>(1);
        DEFAULT_XML_FEATURES.put(XMLConstants.FEATURE_SECURE_PROCESSING, Boolean.TRUE);
    }

    /**
     * Defines the singleton for this class with secure processing enabled.
     */
    static final XmlStringLookup INSTANCE = new XmlStringLookup(DEFAULT_XML_FEATURES, DEFAULT_XPATH_FEATURES, (Path[]) null);

    private static boolean isSecure() {
        return SystemProperties.getBoolean(XmlStringLookup.class, "secure", () -> true);
    }

    /**
     * Defines XPath factory features.
     */
    private final Map<String, Boolean> xPathFactoryFeatures;

    /**
     * Defines XML factory features.
     */
    private final Map<String, Boolean> xmlFactoryFeatures;

    /**
     * Constructs a new instance.
     *
     * @param xmlFactoryFeatures   The {@link DocumentBuilderFactory} features to set.
     * @param xPathFactoryFeatures The {@link XPathFactory} features to set.
     * @see DocumentBuilderFactory#setFeature(String, boolean)
     * @see XPathFactory#setFeature(String, boolean)
     */
    XmlStringLookup(final Map<String, Boolean> xmlFactoryFeatures, final Map<String, Boolean> xPathFactoryFeatures, final Path... fences) {
        super(fences);
        this.xmlFactoryFeatures = Objects.requireNonNull(xmlFactoryFeatures, "xmlFactoryFeatures");
        this.xPathFactoryFeatures = Objects.requireNonNull(xPathFactoryFeatures, "xPathFfactoryFeatures");
    }

    /**
     * Looks up a value for the key in the format {@code "DocumentPath:XPath"}.
     * <p>
     * For example:
     * </p>
     * <ul>
     * <li>{@code "com/domain/document.xml:/path/to/node"}</li>
     * </ul>
     * <p>
     * Secure processing is enabled by default. The secure boolean String parsing follows the syntax defined by {@link Boolean#parseBoolean(String)}. The secure
     * value in the key overrides instance settings given in the constructor.
     * </p>
     *
     * @param key the key to be looked up, may be null.
     * @return The value associated with the key.
     */
    @Override
    public String lookup(final String key) {
        if (key == null) {
            return null;
        }
        final String[] keys = key.split(SPLIT_STR);
        final int keyLen = keys.length;
        if (keyLen != KEY_PARTS_LEN) {
            throw IllegalArgumentExceptions.format("Bad XML key format '%s'; the expected format is 'DocumentPath:XPath'.", key);
        }
        final Boolean secure = isSecure();
        final String documentPath = keys[0];
        final String xpath = StringUtils.substringAfterLast(key, SPLIT_CH);
        final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            for (final Entry<String, Boolean> p : xmlFactoryFeatures.entrySet()) {
                dbFactory.setFeature(p.getKey(), p.getValue());
            }
            if (secure != null) {
                dbFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, secure.booleanValue());
            }
            try (InputStream inputStream = Files.newInputStream(getPath(documentPath))) {
                final Document doc = dbFactory.newDocumentBuilder().parse(inputStream);
                final XPathFactory factory = XPathFactory.newInstance();
                for (final Entry<String, Boolean> p : xPathFactoryFeatures.entrySet()) {
                    factory.setFeature(p.getKey(), p.getValue());
                }
                if (secure != null) {
                    factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, secure.booleanValue());
                }
                return factory.newXPath().evaluate(xpath, doc);
            }
        } catch (final Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
