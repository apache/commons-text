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
 * This enum was adapted from Apache Commons Configuration 2.4-SNAPSHOT.
 * </p>
 *
 * @see StringLookupFactory
 * @see StringLookup
 * @since 1.7
 */
public enum DefaultStringLookup {

    /**
     * The lookup for Base64 decoding.
     */
    BASE64_DECODER(StringLookupFactory.KEY_BASE64_DECODER, StringLookupFactory.INSTANCE.base64DecoderStringLookup()),

    /**
     * The lookup for Base64 decoding.
     */
    BASE64_ENCODER(StringLookupFactory.KEY_BASE64_ENCODER, StringLookupFactory.INSTANCE.base64EncoderStringLookup()),

    /**
     * The lookup for constants.
     */
    CONST(StringLookupFactory.KEY_CONST, StringLookupFactory.INSTANCE.constantStringLookup()),

    /**
     * The lookup for dates.
     */
    DATE(StringLookupFactory.KEY_DATE, StringLookupFactory.INSTANCE.dateStringLookup()),

    /**
     * The lookup for DNS.
     * 
     * @since 1.8
     */
    DNS(StringLookupFactory.KEY_DNS, StringLookupFactory.INSTANCE.dnsStringLookup()),

    /**
     * The lookup for environment properties.
     */
    ENVIRONMENT(StringLookupFactory.KEY_ENV, StringLookupFactory.INSTANCE.environmentVariableStringLookup()),

    /**
     * The lookup for files.
     */
    FILE(StringLookupFactory.KEY_FILE, StringLookupFactory.INSTANCE.fileStringLookup()),

    /**
     * The lookup for Java platform information.
     */
    JAVA(StringLookupFactory.KEY_JAVA, StringLookupFactory.INSTANCE.javaPlatformStringLookup()),

    /**
     * The lookup for localhost information.
     */
    LOCAL_HOST(StringLookupFactory.KEY_LOCALHOST, StringLookupFactory.INSTANCE.localHostStringLookup()),

    /**
     * The lookup for properties.
     */
    PROPERTIES(StringLookupFactory.KEY_PROPERTIES, StringLookupFactory.INSTANCE.propertiesStringLookup()),

    /**
     * The lookup for resource bundles.
     */
    RESOURCE_BUNDLE(StringLookupFactory.KEY_RESOURCE_BUNDLE, StringLookupFactory.INSTANCE.resourceBundleStringLookup()),

    /**
     * The lookup for scripts.
     */
    SCRIPT(StringLookupFactory.KEY_SCRIPT, StringLookupFactory.INSTANCE.scriptStringLookup()),

    /**
     * The lookup for system properties.
     */
    SYSTEM_PROPERTIES(StringLookupFactory.KEY_SYS, StringLookupFactory.INSTANCE.systemPropertyStringLookup()),

    /**
     * The lookup for URLs.
     */
    URL(StringLookupFactory.KEY_URL, StringLookupFactory.INSTANCE.urlStringLookup()),

    /**
     * The lookup for URL decoding.
     */
    URL_DECODER(StringLookupFactory.KEY_URL_DECODER, StringLookupFactory.INSTANCE.urlDecoderStringLookup()),

    /**
     * The lookup for URL decoding.
     */
    URL_ENCODER(StringLookupFactory.KEY_URL_ENCODER, StringLookupFactory.INSTANCE.urlEncoderStringLookup()),

    /**
     * The lookup for URL decoding.
     */
    XML(StringLookupFactory.KEY_XML, StringLookupFactory.INSTANCE.xmlStringLookup());

    /** The associated lookup instance. */
    private final StringLookup lookup;

    /** The prefix under which the associated lookup object is registered. */
    private final String key;

    /**
     * Creates a new instance of {@link DefaultStringLookup} and sets the key and the associated lookup instance.
     *
     * @param prefix
     *            the prefix
     * @param lookup
     *            the {@link StringLookup} instance
     */
    DefaultStringLookup(final String prefix, final StringLookup lookup) {
        this.key = prefix;
        this.lookup = lookup;
    }

    /**
     * Returns the standard prefix for the lookup object of this kind.
     *
     * @return the prefix
     */
    public String getKey() {
        return key;
    }

    /**
     * Returns the standard {@link StringLookup} instance of this kind.
     *
     * @return the associated {@link StringLookup} object
     */
    public StringLookup getStringLookup() {
        return lookup;
    }
}
