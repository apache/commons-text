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
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.xml.secure.SecureDocumentBuilderFactory;
import org.apache.commons.xml.secure.SecureXPathFactory;
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
 * DOM parser and XPath factory features can be set with {@link StringLookupFactory#xmlStringLookup(Map)}.
 * </p>
 * <p>
 * Documents are parsed through Apache Commons Secure XML, which secures two separate aspects:
 * </p>
 * <ul>
 * <li>Processing limits, such as the number of entity expansions, come from {@link XMLConstants#FEATURE_SECURE_PROCESSING}. That feature is enabled by
 * default, and the feature maps above can turn it off.</li>
 * <li>External resource fetching, that is external DTD subsets and external entities, is ignored by default and can <strong>not</strong> be enabled via the
 * feature map. To enable external resource fetching, provide a non-empty list of <strong>fences</strong>.</li>
 * </ul>
 * <p>
 * Every resource inside the fences is considered trusted, so fence only directories whose contents you control.
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
     * Defines the singleton for this class, which sets no parser or XPath factory feature and has no fence.
     * <p>
     * Use {@link StringLookupFactory#xmlStringLookup(Map, Path...)} to set features and fences; without any fence, external resources are ignored.
     * </p>
     */
    static final XmlStringLookup INSTANCE = new XmlStringLookup(Collections.emptyMap(), Collections.emptyMap(), (Path[]) null);

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
        this.xPathFactoryFeatures = Objects.requireNonNull(xPathFactoryFeatures, "xPathFactoryFeatures");
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
     * The document is parsed through Apache Commons Secure XML:
     * </p>
     * <ul>
     * <li>Processing limits are governed by {@link XMLConstants#FEATURE_SECURE_PROCESSING}, which is enabled by default.</li>
     * <li>External DTD subsets and external entities are resolved only from the fences guarding this lookup if these are not empty.</li>
     * </ul>
     * <p>
     * Every resource inside the fences is considered trusted, so fence only directories whose contents you control.
     * </p>
     *
     * @param key The key to be looked up, may be null.
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
        final String documentPath = keys[0];
        final String xpath = StringUtils.substringAfterLast(key, SPLIT_CH);
        final DocumentBuilderFactory dbFactory = SecureDocumentBuilderFactory.newInstance();
        try {
            for (final Entry<String, Boolean> p : xmlFactoryFeatures.entrySet()) {
                dbFactory.setFeature(p.getKey(), p.getValue());
            }
            final Path documentFile = getPath(documentPath);
            try (InputStream inputStream = Files.newInputStream(documentFile)) {
                final DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
                // If the fence is not empty, opt-in follow-up resources fetched from the fence.
                if (!fence.isEmpty()) {
                    documentBuilder.setEntityResolver(new PathFenceResolver(fence));
                }
                // Parsing with the document's own URI gives relative system identifiers a base URI to resolve against, as XML requires.
                final Document doc = documentBuilder.parse(inputStream, documentFile.toUri().toString());
                final XPathFactory xpFactory = SecureXPathFactory.newInstance();
                for (final Entry<String, Boolean> p : xPathFactoryFeatures.entrySet()) {
                    xpFactory.setFeature(p.getKey(), p.getValue());
                }
                return xpFactory.newXPath().evaluate(xpath, doc);
            }
        } catch (final Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
