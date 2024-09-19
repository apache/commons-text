/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.text.lookup;

/**
 * An enumeration defining {@link StringLookup} objects available through {@link StringLookupFactory}.
 * <p>
 * This enum was adapted and expanded from Apache Commons Configuration 2.4.
 * </p>
 * <p><strong>NOTE:</strong> Starting in version 1.10.0, not all lookups defined in this class are
 * included by default in the
 * {@link StringLookupFactory#addDefaultStringLookups(java.util.Map) StringLookupFactory.addDefaultStringLookups}
 * method. See the {@link StringLookupFactory} class documentation for details.
 * </p>
 *
 * @see StringLookupFactory
 * @see StringLookup
 * @since 1.7
 */
public enum DefaultStringLookup {

    /**
     * The lookup for Base64 decoding using the key {@code "base64Decoder"}.
     *
     * @see StringLookupFactory#KEY_BASE64_DECODER
     * @see StringLookupFactory#base64DecoderStringLookup()
     */
    BASE64_DECODER(StringLookupFactory.KEY_BASE64_DECODER, StringLookupFactory.INSTANCE.base64DecoderStringLookup()),

    /**
     * The lookup for Base64 encoding using the key {@code "base64Encoder"}.
     *
     * @see StringLookupFactory#KEY_BASE64_ENCODER
     * @see StringLookupFactory#base64EncoderStringLookup()
     */
    BASE64_ENCODER(StringLookupFactory.KEY_BASE64_ENCODER, StringLookupFactory.INSTANCE.base64EncoderStringLookup()),

    /**
     * The lookup for Java static class member constants using the key {@code "const"}.
     *
     * @see StringLookupFactory#KEY_CONST
     * @see StringLookupFactory#constantStringLookup()
     */
    CONST(StringLookupFactory.KEY_CONST, StringLookupFactory.INSTANCE.constantStringLookup()),

    /**
     * The lookup for formatting the current date using the key {@code "date"}.
     *
     * @see StringLookupFactory#KEY_DATE
     * @see StringLookupFactory#dateStringLookup()
     */
    DATE(StringLookupFactory.KEY_DATE, StringLookupFactory.INSTANCE.dateStringLookup()),

    /**
     * The lookup for DNS using the key {@code "dns"}.
     *
     * @see StringLookupFactory#KEY_DNS
     * @see StringLookupFactory#dnsStringLookup()
     * @since 1.8
     */
    DNS(StringLookupFactory.KEY_DNS, StringLookupFactory.INSTANCE.dnsStringLookup()),

    /**
     * The lookup for environment properties using the key {@code "env"}.
     *
     * @see StringLookupFactory#KEY_ENV
     * @see StringLookupFactory#environmentVariableStringLookup()
     */
    ENVIRONMENT(StringLookupFactory.KEY_ENV, StringLookupFactory.INSTANCE.environmentVariableStringLookup()),

    /**
     * The lookup for files using the key {@code "file"}.
     *
     * @see StringLookupFactory#KEY_FILE
     * @see StringLookupFactory#fileStringLookup()
     */
    FILE(StringLookupFactory.KEY_FILE, StringLookupFactory.INSTANCE.fileStringLookup()),

    /**
     * The lookup for Java platform information using the key {@code "java"}.
     *
     * @see StringLookupFactory#KEY_JAVA
     * @see StringLookupFactory#javaPlatformStringLookup()
     */
    JAVA(StringLookupFactory.KEY_JAVA, StringLookupFactory.INSTANCE.javaPlatformStringLookup()),

    /**
     * The lookup for local host information using the key {@code "localhost"}.
     *
     * @see StringLookupFactory#KEY_LOCALHOST
     * @see StringLookupFactory#localHostStringLookup()
     */
    LOCAL_HOST(StringLookupFactory.KEY_LOCALHOST, StringLookupFactory.INSTANCE.localHostStringLookup()),

    /**
     * The lookup for local host information using the key {@code "loopbackAddress"}.
     *
     * @see StringLookupFactory#KEY_LOOPBACK_ADDRESS
     * @see StringLookupFactory#loopbackAddressStringLookup()
     */
    LOOPBACK_ADDRESS(StringLookupFactory.KEY_LOOPBACK_ADDRESS, StringLookupFactory.INSTANCE.loopbackAddressStringLookup()),

    /**
     * The lookup for properties using the key {@code "properties"}.
     *
     * @see StringLookupFactory#KEY_PROPERTIES
     * @see StringLookupFactory#propertiesStringLookup()
     */
    PROPERTIES(StringLookupFactory.KEY_PROPERTIES, StringLookupFactory.INSTANCE.propertiesStringLookup()),

    /**
     * The lookup for resource bundles using the key {@code "resourceBundle"}.
     *
     * @see StringLookupFactory#KEY_RESOURCE_BUNDLE
     * @see StringLookupFactory#resourceBundleStringLookup()
     */
    RESOURCE_BUNDLE(StringLookupFactory.KEY_RESOURCE_BUNDLE, StringLookupFactory.INSTANCE.resourceBundleStringLookup()),

    /**
     * The lookup for scripts using the key {@code "script"}.
     *
     * @see StringLookupFactory#KEY_SCRIPT
     * @see StringLookupFactory#scriptStringLookup()
     */
    SCRIPT(StringLookupFactory.KEY_SCRIPT, StringLookupFactory.INSTANCE.scriptStringLookup()),

    /**
     * The lookup for system properties using the key {@code "sys"}.
     *
     * @see StringLookupFactory#KEY_SYS
     * @see StringLookupFactory#systemPropertyStringLookup()
     */
    SYSTEM_PROPERTIES(StringLookupFactory.KEY_SYS, StringLookupFactory.INSTANCE.systemPropertyStringLookup()),

    /**
     * The lookup for URLs using the key {@code "url"}.
     *
     * @see StringLookupFactory#KEY_URL
     * @see StringLookupFactory#urlStringLookup()
     */
    URL(StringLookupFactory.KEY_URL, StringLookupFactory.INSTANCE.urlStringLookup()),

    /**
     * The lookup for URL decoding using the key {@code "urlDecoder"}.
     *
     * @see StringLookupFactory#KEY_URL_DECODER
     * @see StringLookupFactory#urlDecoderStringLookup()
     */
    URL_DECODER(StringLookupFactory.KEY_URL_DECODER, StringLookupFactory.INSTANCE.urlDecoderStringLookup()),

    /**
     * The lookup for URL encoding using the key {@code "urlEncoder"}.
     *
     * @see StringLookupFactory#KEY_URL_ENCODER
     * @see StringLookupFactory#urlEncoderStringLookup()
     */
    URL_ENCODER(StringLookupFactory.KEY_URL_ENCODER, StringLookupFactory.INSTANCE.urlEncoderStringLookup()),

    /**
     * The lookup for XML decoding using the key {@code "xml"}.
     *
     * @see StringLookupFactory#KEY_XML
     * @see StringLookupFactory#xmlStringLookup()
     */
    XML(StringLookupFactory.KEY_XML, StringLookupFactory.INSTANCE.xmlStringLookup()),

    /**
     * The lookup for XML decoding using the key {@code "xmlDecoder"}.
     *
     * @see StringLookupFactory#KEY_XML_DECODER
     * @see StringLookupFactory#xmlDecoderStringLookup()
     * @since 1.11.0
     */
    XML_DECODER(StringLookupFactory.KEY_XML_DECODER, StringLookupFactory.INSTANCE.xmlDecoderStringLookup()),

    /**
     * The lookup for XML encoding using the key {@code "xmlEncoder"}.
     *
     * @see StringLookupFactory#KEY_XML_ENCODER
     * @see StringLookupFactory#xmlEncoderStringLookup()
     * @since 1.11.0
     */
    XML_ENCODER(StringLookupFactory.KEY_XML_ENCODER, StringLookupFactory.INSTANCE.xmlEncoderStringLookup());

    /** The prefix under which the associated lookup object is registered. */
    private final String key;

    /** The associated lookup instance. */
    private final StringLookup lookup;

    /**
     * Constructs a new instance of {@link DefaultStringLookup} and sets the key and the associated lookup instance.
     *
     * @param prefix the prefix
     * @param lookup the {@link StringLookup} instance
     */
    DefaultStringLookup(final String prefix, final StringLookup lookup) {
        this.key = prefix;
        this.lookup = lookup;
    }

    /**
     * Gets the standard prefix for the lookup object of this kind.
     *
     * @return the prefix
     */
    public String getKey() {
        return key;
    }

    /**
     * Gets the standard {@link StringLookup} instance of this kind.
     *
     * @return the associated {@link StringLookup} object
     */
    public StringLookup getStringLookup() {
        return lookup;
    }
}
