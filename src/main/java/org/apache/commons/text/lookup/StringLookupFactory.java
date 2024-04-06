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

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.xml.xpath.XPathFactory;

import org.apache.commons.text.StringSubstitutor;

/**
 * Create instances of string lookups or access singleton string lookups implemented in this package.
 * <p>
 * The "classic" look up is {@link #mapStringLookup(Map)}.
 * </p>
 * <p>
 * The methods for variable interpolation (A.K.A. variable substitution) are:
 * </p>
 * <ul>
 * <li>{@link #interpolatorStringLookup()}.</li>
 * <li>{@link #interpolatorStringLookup(Map)}.</li>
 * <li>{@link #interpolatorStringLookup(StringLookup)}.</li>
 * <li>{@link #interpolatorStringLookup(Map, StringLookup, boolean)}.</li>
 * </ul>
 * <p>
 * Unless explicitly requested otherwise, a set of default lookups are included for convenience with these variable interpolation methods. These defaults are
 * listed in the table below. However, the exact lookups included can be configured through the use of the {@value #DEFAULT_STRING_LOOKUPS_PROPERTY} system
 * property. If present, this system property will be parsed as a comma-separated list of lookup names, with the names being those defined by the
 * {@link DefaultStringLookup} enum. For example, setting this system property to {@code "BASE64_ENCODER,ENVIRONMENT"} will only include the
 * {@link DefaultStringLookup#BASE64_ENCODER BASE64_ENCODER} and {@link DefaultStringLookup#ENVIRONMENT ENVIRONMENT} lookups. Setting the property to the empty
 * string will cause no defaults to be configured. Note that not all lookups defined here and in {@link DefaultStringLookup} are included by default.
 * Specifically, lookups that can execute code (e.g., {@link DefaultStringLookup#SCRIPT SCRIPT}) and those that can result in contact with remote servers (e.g.,
 * {@link DefaultStringLookup#URL URL} and {@link DefaultStringLookup#DNS DNS}) are not included by default. The current set of default lookups can be accessed
 * directly with {@link #addDefaultStringLookups(Map)}.
 * </p>
 * <table>
 * <caption>Default String Lookups</caption>
 * <tr>
 * <th>Key</th>
 * <th>Interface</th>
 * <th>Factory Method</th>
 * <th>Since</th>
 * </tr>
 * <tr>
 * <td>{@value #KEY_BASE64_DECODER}</td>
 * <td>{@link StringLookup}</td>
 * <td>{@link #base64DecoderStringLookup()}</td>
 * <td>1.6</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_BASE64_ENCODER}</td>
 * <td>{@link StringLookup}</td>
 * <td>{@link #base64EncoderStringLookup()}</td>
 * <td>1.6</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_CONST}</td>
 * <td>{@link StringLookup}</td>
 * <td>{@link #constantStringLookup()}</td>
 * <td>1.5</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_DATE}</td>
 * <td>{@link StringLookup}</td>
 * <td>{@link #dateStringLookup()}</td>
 * <td>1.5</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_ENV}</td>
 * <td>{@link StringLookup}</td>
 * <td>{@link #environmentVariableStringLookup()}</td>
 * <td>1.3</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_FILE}</td>
 * <td>{@link StringLookup}</td>
 * <td>{@link #fileStringLookup(Path...)}</td>
 * <td>1.5</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_JAVA}</td>
 * <td>{@link StringLookup}</td>
 * <td>{@link #javaPlatformStringLookup()}</td>
 * <td>1.5</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_LOCALHOST}</td>
 * <td>{@link StringLookup}</td>
 * <td>{@link #localHostStringLookup()}</td>
 * <td>1.3</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_PROPERTIES}</td>
 * <td>{@link StringLookup}</td>
 * <td>{@link #propertiesStringLookup(Path...)}</td>
 * <td>1.5</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_RESOURCE_BUNDLE}</td>
 * <td>{@link StringLookup}</td>
 * <td>{@link #resourceBundleStringLookup()}</td>
 * <td>1.6</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_SYS}</td>
 * <td>{@link StringLookup}</td>
 * <td>{@link #systemPropertyStringLookup()}</td>
 * <td>1.3</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_URL_DECODER}</td>
 * <td>{@link StringLookup}</td>
 * <td>{@link #urlDecoderStringLookup()}</td>
 * <td>1.5</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_URL_ENCODER}</td>
 * <td>{@link StringLookup}</td>
 * <td>{@link #urlEncoderStringLookup()}</td>
 * <td>1.5</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_XML}</td>
 * <td>{@link StringLookup}</td>
 * <td>{@link #xmlStringLookup(Map, Path...)}</td>
 * <td>1.5</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_XML_DECODER}</td>
 * <td>{@link StringLookup}</td>
 * <td>{@link #xmlDecoderStringLookup()}</td>
 * <td>1.11.0</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_XML_ENCODER}</td>
 * <td>{@link StringLookup}</td>
 * <td>{@link #xmlEncoderStringLookup()}</td>
 * <td>1.11.0</td>
 * </tr>
 * </table>
 *
 * <table>
 * <caption>Additional String Lookups (not included by default)</caption>
 * <tr>
 * <th>Key</th>
 * <th>Interface</th>
 * <th>Factory Method</th>
 * <th>Since</th>
 * </tr>
 * <tr>
 * <td>{@value #KEY_DNS}</td>
 * <td>{@link StringLookup}</td>
 * <td>{@link #dnsStringLookup()}</td>
 * <td>1.8</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_URL}</td>
 * <td>{@link StringLookup}</td>
 * <td>{@link #urlStringLookup()}</td>
 * <td>1.5</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_SCRIPT}</td>
 * <td>{@link StringLookup}</td>
 * <td>{@link #scriptStringLookup()}</td>
 * <td>1.5</td>
 * </tr>
 * </table>
 *
 * <p>
 * This class also provides functional lookups used as building blocks for other lookups.
 * <table>
 * <caption>Functional String Lookups</caption>
 * <tr>
 * <th>Interface</th>
 * <th>Factory Method</th>
 * <th>Since</th>
 * </tr>
 * <tr>
 * <td>{@link BiStringLookup}</td>
 * <td>{@link #biFunctionStringLookup(BiFunction)}</td>
 * <td>1.9</td>
 * </tr>
 * <tr>
 * <td>{@link StringLookup}</td>
 * <td>{@link #functionStringLookup(Function)}</td>
 * <td>1.9</td>
 * </tr>
 * </table>
 *
 * @since 1.3
 */
public final class StringLookupFactory {

    /**
     * Builds instance of {@link StringLookupFactory}.
     *
     * @since 1.12.0
     */
    public static final class Builder implements Supplier<StringLookupFactory> {

        /**
         * Fences.
         */
        private Path[] fences;

        @Override
        public StringLookupFactory get() {
            return new StringLookupFactory(fences);
        }

        /**
         * Sets Path resolution fences.
         * <p>
         * Path Fences apply to the file, property, and XML string lookups.
         * </p>
         *
         * @param fences Path resolution fences.
         * @return this.
         */
        public Builder setFences(final Path... fences) {
            this.fences = fences;
            return this;
        }

    }

    /**
     * Constructs a new {@link Builder}.
     *
     * @return a new {@link Builder}
     * @since 1.12.0
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Internal class used to construct the default {@link StringLookup} map used by {@link StringLookupFactory#addDefaultStringLookups(Map)}.
     */
    static final class DefaultStringLookupsHolder {

        /** Singleton instance, initialized with the system properties. */
        static final DefaultStringLookupsHolder INSTANCE = new DefaultStringLookupsHolder(System.getProperties());

        /**
         * Adds the key and string lookup from {@code lookup} to {@code map}, also adding any additional key aliases if needed. Keys are normalized using the
         * {@link #toKey(String)} method.
         *
         * @param lookup lookup to add
         * @param map    map to add to
         */
        private static void addLookup(final DefaultStringLookup lookup, final Map<String, StringLookup> map) {
            map.put(toKey(lookup.getKey()), lookup.getStringLookup());
            if (DefaultStringLookup.BASE64_DECODER.equals(lookup)) {
                // "base64" is deprecated in favor of KEY_BASE64_DECODER.
                map.put(toKey("base64"), lookup.getStringLookup());
            }
        }

        /**
         * Creates the lookup map used when the user has requested no customization.
         *
         * @return default lookup map
         */
        private static Map<String, StringLookup> createDefaultStringLookups() {
            final Map<String, StringLookup> lookupMap = new HashMap<>();

            addLookup(DefaultStringLookup.BASE64_DECODER, lookupMap);
            addLookup(DefaultStringLookup.BASE64_ENCODER, lookupMap);
            addLookup(DefaultStringLookup.CONST, lookupMap);
            addLookup(DefaultStringLookup.DATE, lookupMap);
            addLookup(DefaultStringLookup.ENVIRONMENT, lookupMap);
            addLookup(DefaultStringLookup.FILE, lookupMap);
            addLookup(DefaultStringLookup.JAVA, lookupMap);
            addLookup(DefaultStringLookup.LOCAL_HOST, lookupMap);
            addLookup(DefaultStringLookup.PROPERTIES, lookupMap);
            addLookup(DefaultStringLookup.RESOURCE_BUNDLE, lookupMap);
            addLookup(DefaultStringLookup.SYSTEM_PROPERTIES, lookupMap);
            addLookup(DefaultStringLookup.URL_DECODER, lookupMap);
            addLookup(DefaultStringLookup.URL_ENCODER, lookupMap);
            addLookup(DefaultStringLookup.XML, lookupMap);
            addLookup(DefaultStringLookup.XML_DECODER, lookupMap);
            addLookup(DefaultStringLookup.XML_ENCODER, lookupMap);

            return lookupMap;
        }

        /**
         * Constructs a lookup map by parsing the given string. The string is expected to contain comma or space-separated names of values from the
         * {@link DefaultStringLookup} enum. If the given string is null or empty, an empty map is returned.
         *
         * @param str string to parse; may be null or empty
         * @return lookup map parsed from the given string
         */
        private static Map<String, StringLookup> parseStringLookups(final String str) {
            final Map<String, StringLookup> lookupMap = new HashMap<>();
            try {
                for (final String lookupName : str.split("[\\s,]+")) {
                    if (!lookupName.isEmpty()) {
                        addLookup(DefaultStringLookup.valueOf(lookupName.toUpperCase()), lookupMap);
                    }
                }
            } catch (final IllegalArgumentException exc) {
                throw new IllegalArgumentException("Invalid default string lookups definition: " + str, exc);
            }
            return lookupMap;
        }

        /** Default string lookup map. */
        private final Map<String, StringLookup> defaultStringLookups;

        /**
         * Constructs a new instance initialized with the given properties.
         *
         * @param props initialization properties
         */
        DefaultStringLookupsHolder(final Properties props) {
            final Map<String, StringLookup> lookups = props.containsKey(StringLookupFactory.DEFAULT_STRING_LOOKUPS_PROPERTY)
                    ? parseStringLookups(props.getProperty(StringLookupFactory.DEFAULT_STRING_LOOKUPS_PROPERTY))
                    : createDefaultStringLookups();
            defaultStringLookups = Collections.unmodifiableMap(lookups);
        }

        /**
         * Gets the default string lookups map.
         *
         * @return default string lookups map
         */
        Map<String, StringLookup> getDefaultStringLookups() {
            return defaultStringLookups;
        }
    }

    /**
     * Name of the system property used to determine the string lookups added by the {@link #addDefaultStringLookups(Map)} method. Use of this property is only
     * required in cases where the set of default lookups must be modified. (See the class documentation for details.)
     *
     * @since 1.10.0
     */
    public static final String DEFAULT_STRING_LOOKUPS_PROPERTY = "org.apache.commons.text.lookup.StringLookupFactory.defaultStringLookups";

    /**
     * Defines the singleton for this class.
     */
    public static final StringLookupFactory INSTANCE = new StringLookupFactory();

    /**
     * Decodes Base64 Strings.
     * <p>
     * Using a {@link StringLookup} from the {@link StringLookupFactory}:
     * </p>
     *
     * <pre>
     * StringLookupFactory.INSTANCE.base64DecoderStringLookup().lookup("SGVsbG9Xb3JsZCE=");
     * </pre>
     * <p>
     * Using a {@link StringSubstitutor}:
     * </p>
     *
     * <pre>
     * StringSubstitutor.createInterpolator().replace("... ${base64Decoder:SGVsbG9Xb3JsZCE=} ..."));
     * </pre>
     * <p>
     * The above examples convert {@code "SGVsbG9Xb3JsZCE="} to {@code "HelloWorld!"}.
     * </p>
     */
    static final FunctionStringLookup<String> INSTANCE_BASE64_DECODER = FunctionStringLookup
            .on(key -> new String(Base64.getDecoder().decode(key), StandardCharsets.ISO_8859_1));

    /**
     * Encodes Base64 Strings.
     * <p>
     * Using a {@link StringLookup} from the {@link StringLookupFactory}:
     * </p>
     *
     * <pre>
     * StringLookupFactory.INSTANCE.base64EncoderStringLookup().lookup("HelloWorld!");
     * </pre>
     * <p>
     * Using a {@link StringSubstitutor}:
     * </p>
     *
     * <pre>
     * StringSubstitutor.createInterpolator().replace("... ${base64Encoder:HelloWorld!} ..."));
     * </pre>
     * <p>
     * The above examples convert {@code "HelloWorld!"} to {@code "SGVsbG9Xb3JsZCE="}.
     * </p>
     * Defines the singleton for this class.
     */
    static final FunctionStringLookup<String> INSTANCE_BASE64_ENCODER = FunctionStringLookup
            .on(key -> Base64.getEncoder().encodeToString(key.getBytes(StandardCharsets.ISO_8859_1)));

    /**
     * Looks up keys from environment variables.
     * <p>
     * Using a {@link StringLookup} from the {@link StringLookupFactory}:
     * </p>
     *
     * <pre>
     * StringLookupFactory.INSTANCE.environmentVariableStringLookup().lookup("USER");
     * </pre>
     * <p>
     * Using a {@link StringSubstitutor}:
     * </p>
     *
     * <pre>
     * StringSubstitutor.createInterpolator().replace("... ${env:USER} ..."));
     * </pre>
     * <p>
     * The above examples convert (on Linux) {@code "USER"} to the current user name. On Windows 10, you would use {@code "USERNAME"} to the same effect.
     * </p>
     */
    static final FunctionStringLookup<String> INSTANCE_ENVIRONMENT_VARIABLES = FunctionStringLookup.on(System::getenv);

    /**
     * Defines the FunctionStringLookup singleton that always returns null.
     */
    static final FunctionStringLookup<String> INSTANCE_NULL = FunctionStringLookup.on(key -> null);

    /**
     * Defines the FunctionStringLookup singleton for looking up system properties.
     */
    static final FunctionStringLookup<String> INSTANCE_SYSTEM_PROPERTIES = FunctionStringLookup.on(System::getProperty);

    /**
     * Default lookup key for interpolation {@value #KEY_BASE64_DECODER}.
     *
     * @since 1.6
     */
    public static final String KEY_BASE64_DECODER = "base64Decoder";

    /**
     * Default lookup key for interpolation {@value #KEY_BASE64_ENCODER}.
     *
     * @since 1.6
     */
    public static final String KEY_BASE64_ENCODER = "base64Encoder";

    /**
     * Default lookup key for interpolation {@value #KEY_CONST}.
     *
     * @since 1.6
     */
    public static final String KEY_CONST = "const";

    /**
     * Default lookup key for interpolation {@value #KEY_DATE}.
     *
     * @since 1.6
     */
    public static final String KEY_DATE = "date";

    /**
     * Default lookup key for interpolation {@value #KEY_DNS}.
     *
     * @since 1.8
     */
    public static final String KEY_DNS = "dns";

    /**
     * Default lookup key for interpolation {@value #KEY_ENV}.
     *
     * @since 1.6
     */
    public static final String KEY_ENV = "env";

    /**
     * Default lookup key for interpolation {@value #KEY_FILE}.
     *
     * @since 1.6
     */
    public static final String KEY_FILE = "file";

    /**
     * Default lookup key for interpolation {@value #KEY_JAVA}.
     *
     * @since 1.6
     */
    public static final String KEY_JAVA = "java";

    /**
     * Default lookup key for interpolation {@value #KEY_LOCALHOST}.
     *
     * @since 1.6
     */
    public static final String KEY_LOCALHOST = "localhost";

    /**
     * Default lookup key for interpolation {@value #KEY_PROPERTIES}.
     *
     * @since 1.6
     */
    public static final String KEY_PROPERTIES = "properties";

    /**
     * Default lookup key for interpolation {@value #KEY_RESOURCE_BUNDLE}.
     *
     * @since 1.6
     */
    public static final String KEY_RESOURCE_BUNDLE = "resourceBundle";

    /**
     * Default lookup key for interpolation {@value #KEY_SCRIPT}.
     *
     * @since 1.6
     */
    public static final String KEY_SCRIPT = "script";

    /**
     * Default lookup key for interpolation {@value #KEY_SYS}.
     *
     * @since 1.6
     */
    public static final String KEY_SYS = "sys";

    /**
     * Default lookup key for interpolation {@value #KEY_URL}.
     *
     * @since 1.6
     */
    public static final String KEY_URL = "url";

    /**
     * Default lookup key for interpolation {@value #KEY_URL_DECODER}.
     *
     * @since 1.6
     */
    public static final String KEY_URL_DECODER = "urlDecoder";

    /**
     * Default lookup key for interpolation {@value #KEY_URL_ENCODER}.
     *
     * @since 1.6
     */
    public static final String KEY_URL_ENCODER = "urlEncoder";

    /**
     * Default lookup key for interpolation {@value #KEY_XML}.
     *
     * @since 1.6
     */
    public static final String KEY_XML = "xml";

    /**
     * Default lookup key for interpolation {@value #KEY_XML_DECODER}.
     *
     * @since 1.11.0
     */
    public static final String KEY_XML_DECODER = "xmlDecoder";

    /**
     * Default lookup key for interpolation {@value #KEY_XML_ENCODER}.
     *
     * @since 1.11.0
     */
    public static final String KEY_XML_ENCODER = "xmlEncoder";

    /**
     * Clears any static resources.
     *
     * @since 1.5
     */
    public static void clear() {
        ConstantStringLookup.clear();
    }

    /**
     * Gets a string suitable for use as a key in the string lookup map.
     *
     * @param key string to convert to a string lookup map key
     * @return string lookup map key
     */
    static String toKey(final String key) {
        return key.toLowerCase(Locale.ROOT);
    }

    /**
     * Returns the given map if the input is non-null or an empty immutable map if the input is null.
     *
     * @param <K> the class of the map keys
     * @param <V> the class of the map values
     * @param map The map to test
     * @return the given map if the input is non-null or an empty immutable map if the input is null.
     */
    static <K, V> Map<K, V> toMap(final Map<K, V> map) {
        return map == null ? Collections.emptyMap() : map;
    }

    /**
     * Fences.
     */
    private final Path[] fences;

    /**
     * Constructs a new instance.
     */
    private StringLookupFactory() {
        this(null);
    }

    /**
     * Constructs a new instance.
     */
    private StringLookupFactory(final Path[] fences) {
        this.fences = fences;
    }

    /**
     * Adds the default string lookups for this class to {@code stringLookupMap}. The default string lookups are a set of built-in lookups added for convenience
     * during string interpolation. The defaults may be configured using the {@value #DEFAULT_STRING_LOOKUPS_PROPERTY} system property. See the class
     * documentation for details and a list of lookups.
     *
     * @param stringLookupMap the map of string lookups to edit.
     * @since 1.5
     */
    public void addDefaultStringLookups(final Map<String, StringLookup> stringLookupMap) {
        if (stringLookupMap != null) {
            stringLookupMap.putAll(DefaultStringLookupsHolder.INSTANCE.getDefaultStringLookups());
        }
    }

    /**
     * Returns the Base64DecoderStringLookup singleton instance to decode Base64 strings.
     * <p>
     * Using a {@link StringLookup} from the {@link StringLookupFactory}:
     * </p>
     *
     * <pre>
     * StringLookupFactory.INSTANCE.base64DecoderStringLookup().lookup("SGVsbG9Xb3JsZCE=");
     * </pre>
     * <p>
     * Using a {@link StringSubstitutor}:
     * </p>
     *
     * <pre>
     * StringSubstitutor.createInterpolator().replace("... ${base64Decoder:SGVsbG9Xb3JsZCE=} ..."));
     * </pre>
     * <p>
     * The above examples convert {@code "SGVsbG9Xb3JsZCE="} to {@code "HelloWorld!"}.
     * </p>
     *
     * @return The Base64DecoderStringLookup singleton instance.
     * @since 1.5
     */
    public StringLookup base64DecoderStringLookup() {
        return StringLookupFactory.INSTANCE_BASE64_DECODER;
    }

    /**
     * Returns the Base64EncoderStringLookup singleton instance to encode strings to Base64.
     * <p>
     * Using a {@link StringLookup} from the {@link StringLookupFactory}:
     * </p>
     *
     * <pre>
     * StringLookupFactory.INSTANCE.base64EncoderStringLookup().lookup("HelloWorld!");
     * </pre>
     * <p>
     * Using a {@link StringSubstitutor}:
     * </p>
     *
     * <pre>
     * StringSubstitutor.createInterpolator().replace("... ${base64Encoder:HelloWorld!} ..."));
     * </pre>
     * <p>
     * The above examples convert {@code } to {@code "SGVsbG9Xb3JsZCE="}.
     * </p>
     *
     * @return The Base64EncoderStringLookup singleton instance.
     * @since 1.6
     */
    public StringLookup base64EncoderStringLookup() {
        return StringLookupFactory.INSTANCE_BASE64_ENCODER;
    }

    /**
     * Returns the Base64DecoderStringLookup singleton instance to decode Base64 strings.
     * <p>
     * Using a {@link StringLookup} from the {@link StringLookupFactory}:
     * </p>
     *
     * <pre>
     * StringLookupFactory.INSTANCE.base64DecoderStringLookup().lookup("SGVsbG9Xb3JsZCE=");
     * </pre>
     * <p>
     * Using a {@link StringSubstitutor}:
     * </p>
     *
     * <pre>
     * StringSubstitutor.createInterpolator().replace("... ${base64Decoder:SGVsbG9Xb3JsZCE=} ..."));
     * </pre>
     * <p>
     * The above examples convert {@code "SGVsbG9Xb3JsZCE="} to {@code "HelloWorld!"}.
     * </p>
     *
     * @return The Base64DecoderStringLookup singleton instance.
     * @since 1.5
     * @deprecated Use {@link #base64DecoderStringLookup()}.
     */
    @Deprecated
    public StringLookup base64StringLookup() {
        return StringLookupFactory.INSTANCE_BASE64_DECODER;
    }

    /**
     * Returns a new function-based lookup where the request for a lookup is answered by applying the function with a lookup key.
     *
     * @param <R>        the function return type.
     * @param <U>        the function's second parameter type.
     * @param biFunction the function.
     * @return a new MapStringLookup.
     * @since 1.9
     */
    public <R, U> BiStringLookup<U> biFunctionStringLookup(final BiFunction<String, U, R> biFunction) {
        return BiFunctionStringLookup.on(biFunction);
    }

    /**
     * Returns the ConstantStringLookup singleton instance to look up the value of a fully-qualified static final value.
     * <p>
     * Sometimes it is necessary in a configuration file to refer to a constant defined in a class. This can be done with this lookup implementation. Variable
     * names must be in the format {@code apackage.AClass.AFIELD}. The {@code lookup(String)} method will split the passed in string at the last dot, separating
     * the fully qualified class name and the name of the constant (i.e. <b>static final</b>) member field. Then the class is loaded and the field's value is
     * obtained using reflection.
     * </p>
     * <p>
     * Once retrieved values are cached for fast access. This class is thread-safe. It can be used as a standard (i.e. global) lookup object and serve multiple
     * clients concurrently.
     * </p>
     * <p>
     * Using a {@link StringLookup} from the {@link StringLookupFactory}:
     * </p>
     *
     * <pre>
     * StringLookupFactory.INSTANCE.constantStringLookup().lookup("java.awt.event.KeyEvent.VK_ESCAPE");
     * </pre>
     * <p>
     * Using a {@link StringSubstitutor}:
     * </p>
     *
     * <pre>
     * StringSubstitutor.createInterpolator().replace("... ${const:java.awt.event.KeyEvent.VK_ESCAPE} ..."));
     * </pre>
     * <p>
     * The above examples convert {@code java.awt.event.KeyEvent.VK_ESCAPE} to {@code "27"}.
     * </p>
     *
     * @return The ConstantStringLookup singleton instance.
     * @since 1.5
     */
    public StringLookup constantStringLookup() {
        return ConstantStringLookup.INSTANCE;
    }

    /**
     * Returns the DateStringLookup singleton instance to format the current date with the format given in the key in a format compatible with
     * {@link java.text.SimpleDateFormat}.
     * <p>
     * Using a {@link StringLookup} from the {@link StringLookupFactory}:
     * </p>
     *
     * <pre>
     * StringLookupFactory.INSTANCE.dateStringLookup().lookup("yyyy-MM-dd");
     * </pre>
     * <p>
     * Using a {@link StringSubstitutor}:
     * </p>
     *
     * <pre>
     * StringSubstitutor.createInterpolator().replace("... ${date:yyyy-MM-dd} ..."));
     * </pre>
     * <p>
     * The above examples convert {@code "yyyy-MM-dd"} to todays's date, for example, {@code "2019-08-04"}.
     * </p>
     *
     * @return The DateStringLookup singleton instance.
     */
    public StringLookup dateStringLookup() {
        return DateStringLookup.INSTANCE;
    }

    /**
     * Returns the DnsStringLookup singleton instance where the lookup key is one of:
     * <ul>
     * <li><b>name</b>: for the local host name, for example {@code EXAMPLE} but also {@code EXAMPLE.apache.org}.</li>
     * <li><b>canonical-name</b>: for the local canonical host name, for example {@code EXAMPLE.apache.org}.</li>
     * <li><b>address</b>: for the local host address, for example {@code 192.168.56.1}.</li>
     * </ul>
     *
     * <p>
     * Using a {@link StringLookup} from the {@link StringLookupFactory}:
     * </p>
     *
     * <pre>
     * StringLookupFactory.INSTANCE.dnsStringLookup().lookup("address|apache.org");
     * </pre>
     * <p>
     * When used through a {@link StringSubstitutor}, this lookup must either be added programmatically (as below) or enabled as a default lookup using the
     * {@value #DEFAULT_STRING_LOOKUPS_PROPERTY} system property (see class documentation).
     * </p>
     *
     * <pre>
     * Map&lt;String, StringLookup&gt; lookupMap = new HashMap&lt;&gt;();
     * lookupMap.put("dns", StringLookupFactory.INSTANCE.dnsStringLookup());
     *
     * StringLookup variableResolver = StringLookupFactory.INSTANCE.interpolatorStringLookup(lookupMap, null, false);
     *
     * new StringSubstitutor(variableResolver).replace("... ${dns:address|apache.org} ...");
     * </pre>
     * <p>
     * The above examples convert {@code "address|apache.org"} to the IP address of {@code apache.org}.
     * </p>
     *
     * @return the DnsStringLookup singleton instance.
     * @since 1.8
     */
    public StringLookup dnsStringLookup() {
        return DnsStringLookup.INSTANCE;
    }

    /**
     * Returns the EnvironmentVariableStringLookup singleton instance where the lookup key is an environment variable name.
     * <p>
     * Using a {@link StringLookup} from the {@link StringLookupFactory}:
     * </p>
     *
     * <pre>
     * StringLookupFactory.INSTANCE.environmentVariableStringLookup().lookup("USER");
     * </pre>
     * <p>
     * Using a {@link StringSubstitutor}:
     * </p>
     *
     * <pre>
     * StringSubstitutor.createInterpolator().replace("... ${env:USER} ..."));
     * </pre>
     * <p>
     * The above examples convert (on Linux) {@code "USER"} to the current user name. On Windows 10, you would use {@code "USERNAME"} to the same effect.
     * </p>
     *
     * @return The EnvironmentVariableStringLookup singleton instance.
     */
    public StringLookup environmentVariableStringLookup() {
        return StringLookupFactory.INSTANCE_ENVIRONMENT_VARIABLES;
    }

    /**
     * Returns a file StringLookup instance.
     * <p>
     * If this factory was built using {@link Builder#setFences(Path...)}, then the string lookup is fenced and will throw an {@link IllegalArgumentException}
     * if a lookup causes causes a path to resolve outside of these fences. Otherwise, the result is unfenced to preserved behavior from previous versions.
     * </p>
     * <em>Using a fenced StringLookup</em>
     * <p>
     * To use a fenced {@link StringLookup}, use {@link StringLookupFactory#builder()}:
     * </p>
     *
     * <pre>
     * // Make the fence the current directory
     * StringLookupFactory factory = StringLookupFactory.builder().setFences(Paths.get("")).get();
     * factory.fileStringLookup().lookup("UTF-8:com/domain/document.txt");
     *
     * // throws IllegalArgumentException
     * factory.fileStringLookup().lookup("UTF-8:/rootdir/foo/document.txt");
     *
     * // throws IllegalArgumentException
     * factory.fileStringLookup().lookup("UTF-8:../document.txt");
     * </pre>
     *
     * <em>Using an unfenced StringLookup</em>
     * <p>
     * To use an unfenced {@link StringLookup}, use {@link StringLookupFactory#INSTANCE}:
     * </p>
     *
     * <pre>
     * StringLookupFactory.INSTANCE.fileStringLookup().lookup("UTF-8:com/domain/document.properties");
     * </pre>
     *
     * <em>Using a StringLookup with StringSubstitutor</em>
     * <p>
     * To build a fenced StringSubstitutor, use:
     * </p>
     *
     * <pre>
     * // Make the fence the current directory
     * final StringLookupFactory factory = StringLookupFactory.builder().setFences(Paths.get("")).get();
     * final StringSubstitutor stringSubstitutor = new StringSubstitutor(factory.interpolatorStringLookup());
     * stringSubstitutor.replace("... ${file:UTF-8:com/domain/document.txt} ..."));
     *
     * // throws IllegalArgumentException
     * stringSubstitutor.replace("... ${file:UTF-8:/rootdir/foo/document.txt} ..."));
     * </pre>
     * <p>
     * Using an unfenced {@link StringSubstitutor}:
     * </p>
     *
     * <pre>
     * StringSubstitutor.createInterpolator().replace("... ${file:UTF-8:com/domain/document.txt} ..."));
     * </pre>
     * <p>
     * The above examples convert {@code "UTF-8:com/domain/document.txt"} to the contents of the file.
     * </p>
     *
     * @return a file StringLookup instance.
     * @since 1.5
     */
    public StringLookup fileStringLookup() {
        return fences != null ? fileStringLookup(fences) : FileStringLookup.INSTANCE;
    }

    /**
     * Returns a fenced file StringLookup instance.
     * <p>
     * To use a {@link StringLookup} fenced by the current directory, use:
     * </p>
     *
     * <pre>
     * StringLookupFactory.INSTANCE.fileStringLookup(Paths.get("")).lookup("UTF-8:com/domain/document.txt");
     *
     * // throws IllegalArgumentException
     * StringLookupFactory.INSTANCE.fileStringLookup(Paths.get("")).lookup("UTF-8:/rootdir/foo/document.txt");
     *
     * // throws IllegalArgumentException
     * StringLookupFactory.INSTANCE.fileStringLookup(Paths.get("")).lookup("UTF-8:../com/domain/document.txt");
     * </pre>
     * <p>
     * The above example converts {@code "UTF-8:com/domain/document.txt"} to the contents of the file.
     * </p>
     * <p>
     * {@link StringSubstitutor} methods like {@link StringSubstitutor#replace(String)} will throw a {@link IllegalArgumentException} when a file doesn't
     * resolves in a fence.
     * </p>
     *
     * @param fences The fences guarding Path resolution.
     * @return a file StringLookup instance.
     * @since 1.12.0
     */
    public StringLookup fileStringLookup(final Path... fences) {
        return new FileStringLookup(fences);
    }

    /**
     * Returns a new function-based lookup where the request for a lookup is answered by applying the function with a lookup key.
     *
     * @param <R>      the function return type.
     * @param function the function.
     * @return a new MapStringLookup.
     * @since 1.9
     */
    public <R> StringLookup functionStringLookup(final Function<String, R> function) {
        return FunctionStringLookup.on(function);
    }

    /**
     * Returns a {@link InterpolatorStringLookup} containing the configured {@link #addDefaultStringLookups(Map) default lookups}. See the class documentation
     * for details on how these defaults are configured.
     * <p>
     * Using a {@link StringLookup} from the {@link StringLookupFactory}:
     * </p>
     *
     * <pre>
     * StringLookupFactory.INSTANCE.interpolatorStringLookup().lookup("${sys:os.name}, ${env:USER}");
     * </pre>
     * <p>
     * Using a {@link StringSubstitutor}:
     * </p>
     *
     * <pre>
     * StringSubstitutor.createInterpolator().replace("... ${sys:os.name}, ${env:USER} ..."));
     * </pre>
     * <p>
     * The above examples convert {@code "${sys:os.name}, ${env:USER}"} to the OS name and Linux user name.
     * </p>
     *
     * @return the default {@link InterpolatorStringLookup}.
     */
    public StringLookup interpolatorStringLookup() {
        return InterpolatorStringLookup.INSTANCE;
    }

    /**
     * Returns a new InterpolatorStringLookup. If {@code addDefaultLookups} is {@code true}, the configured {@link #addDefaultStringLookups(Map) default
     * lookups} are included in addition to the ones provided in {@code stringLookupMap}. (See the class documentation for details on how default lookups are
     * configured.)
     *
     * @param stringLookupMap     the map of string lookups.
     * @param defaultStringLookup the default string lookup; this lookup is used when a variable cannot be resolved using the lookups in {@code stringLookupMap}
     *                            or the configured default lookups (if enabled)
     * @param addDefaultLookups   whether to use default lookups as described above.
     * @return a new InterpolatorStringLookup.
     * @since 1.4
     */
    public StringLookup interpolatorStringLookup(final Map<String, StringLookup> stringLookupMap, final StringLookup defaultStringLookup,
            final boolean addDefaultLookups) {
        return new InterpolatorStringLookup(stringLookupMap, defaultStringLookup, addDefaultLookups);
    }

    /**
     * Returns a new InterpolatorStringLookup using the given key-value pairs and the configured {@link #addDefaultStringLookups(Map) default lookups} to
     * resolve variables. (See the class documentation for details on how default lookups are configured.)
     *
     * @param <V> the value type the default string lookup's map.
     * @param map the default map for string lookups.
     * @return a new InterpolatorStringLookup.
     */
    public <V> StringLookup interpolatorStringLookup(final Map<String, V> map) {
        return new InterpolatorStringLookup(map);
    }

    /**
     * Returns a new InterpolatorStringLookup using the given lookup and the configured {@link #addDefaultStringLookups(Map) default lookups} to resolve
     * variables. (See the class documentation for details on how default lookups are configured.)
     *
     * @param defaultStringLookup the default string lookup.
     * @return a new InterpolatorStringLookup.
     */
    public StringLookup interpolatorStringLookup(final StringLookup defaultStringLookup) {
        return new InterpolatorStringLookup(defaultStringLookup);
    }

    /**
     * Returns the JavaPlatformStringLookup singleton instance. Looks up keys related to Java: Java version, JRE version, VM version, and so on.
     * <p>
     * The lookup keys with examples are:
     * </p>
     * <ul>
     * <li><b>version</b>: "Java version 1.8.0_181"</li>
     * <li><b>runtime</b>: "Java(TM) SE Runtime Environment (build 1.8.0_181-b13) from Oracle Corporation"</li>
     * <li><b>vm</b>: "Java HotSpot(TM) 64-Bit Server VM (build 25.181-b13, mixed mode)"</li>
     * <li><b>os</b>: "Windows 10 10.0, architecture: amd64-64"</li>
     * <li><b>hardware</b>: "processors: 4, architecture: amd64-64, instruction sets: amd64"</li>
     * <li><b>locale</b>: "default locale: en_US, platform encoding: iso-8859-1"</li>
     * </ul>
     *
     * <p>
     * Using a {@link StringLookup} from the {@link StringLookupFactory}:
     * </p>
     *
     * <pre>
     * StringLookupFactory.INSTANCE.javaPlatformStringLookup().lookup("version");
     * </pre>
     * <p>
     * Using a {@link StringSubstitutor}:
     * </p>
     *
     * <pre>
     * StringSubstitutor.createInterpolator().replace("... ${java:version} ..."));
     * </pre>
     * <p>
     * The above examples convert {@code "version"} to the current VM version, for example, {@code "Java version 1.8.0_181"}.
     * </p>
     *
     * @return The JavaPlatformStringLookup singleton instance.
     */
    public StringLookup javaPlatformStringLookup() {
        return JavaPlatformStringLookup.INSTANCE;
    }

    /**
     * Returns the LocalHostStringLookup singleton instance where the lookup key is one of:
     * <ul>
     * <li><b>name</b>: for the local host name, for example {@code EXAMPLE}.</li>
     * <li><b>canonical-name</b>: for the local canonical host name, for example {@code EXAMPLE.apache.org}.</li>
     * <li><b>address</b>: for the local host address, for example {@code 192.168.56.1}.</li>
     * </ul>
     *
     * <p>
     * Using a {@link StringLookup} from the {@link StringLookupFactory}:
     * </p>
     *
     * <pre>
     * StringLookupFactory.INSTANCE.localHostStringLookup().lookup("canonical-name");
     * </pre>
     * <p>
     * Using a {@link StringSubstitutor}:
     * </p>
     *
     * <pre>
     * StringSubstitutor.createInterpolator().replace("... ${localhost:canonical-name} ..."));
     * </pre>
     * <p>
     * The above examples convert {@code "canonical-name"} to the current host name, for example, {@code "EXAMPLE.apache.org"}.
     * </p>
     *
     * @return The DateStringLookup singleton instance.
     */
    public StringLookup localHostStringLookup() {
        return LocalHostStringLookup.INSTANCE;
    }

    /**
     * Returns a new map-based lookup where the request for a lookup is answered with the value for that key.
     *
     * @param <V> the map value type.
     * @param map the map.
     * @return a new MapStringLookup.
     */
    public <V> StringLookup mapStringLookup(final Map<String, V> map) {
        return FunctionStringLookup.on(map);
    }

    /**
     * Returns the NullStringLookup singleton instance which always returns null.
     *
     * @return The NullStringLookup singleton instance.
     */
    public StringLookup nullStringLookup() {
        return StringLookupFactory.INSTANCE_NULL;
    }

    /**
     * Returns a Properties StringLookup instance.
     * <p>
     * If this factory was built using {@link Builder#setFences(Path...)}, then the string lookup is fenced and will throw an {@link IllegalArgumentException}
     * if a lookup causes causes a path to resolve outside of these fences. Otherwise, the result is unfenced to preserved behavior from previous versions.
     * </p>
     * <p>
     * We looks up a value for the key in the format "DocumentPath::MyKey".
     * </p>
     * <p>
     * Note the use of "::" instead of ":" to allow for "C:" drive letters in paths.
     * </p>
     * <p>
     * For example: "com/domain/document.properties::MyKey".
     * </p>
     * <em>Using a fenced StringLookup</em>
     * <p>
     * To use a fenced {@link StringLookup}, use {@link StringLookupFactory#builder()}:
     * </p>
     *
     * <pre>
     * // Make the fence the current directory
     * StringLookupFactory factory = StringLookupFactory.builder().setFences(Paths.get("")).get();
     * factory.propertiesStringLookup().lookup("com/domain/document.properties::MyKey");
     *
     * // throws IllegalArgumentException
     * factory.propertiesStringLookup().lookup("/com/domain/document.properties::MyKey");
     *
     * // throws IllegalArgumentException
     * factory.propertiesStringLookup().lookup("../com/domain/document.properties::MyKey");
     * </pre>
     *
     * <em>Using an unfenced StringLookup</em>
     * <p>
     * To use an unfenced {@link StringLookup}, use {@link StringLookupFactory#INSTANCE}:
     * </p>
     *
     * <pre>
     * StringLookupFactory.INSTANCE.propertiesStringLookup().lookup("com/domain/document.properties::MyKey");
     * </pre>
     *
     * <em>Using a StringLookup with StringSubstitutor</em>
     * <p>
     * To build a fenced StringSubstitutor, use:
     * </p>
     *
     * <pre>
     * // Make the fence the current directory
     * final StringLookupFactory factory = StringLookupFactory.builder().setFences(Paths.get("")).get();
     * final StringSubstitutor stringSubstitutor = new StringSubstitutor(factory.interpolatorStringLookup());
     * stringSubstitutor.replace("... ${properties:com/domain/document.properties::MyKey} ..."));
     *
     * // throws IllegalArgumentException
     * stringSubstitutor.replace("... ${properties:/rootdir/foo/document.properties::MyKey} ..."));
     * </pre>
     * <p>
     * Using an unfenced {@link StringSubstitutor}:
     * </p>
     *
     * <pre>
     * StringSubstitutor.createInterpolator().replace("... ${properties:com/domain/document.properties::MyKey} ..."));
     * </pre>
     * <p>
     * The above examples convert {@code "com/domain/document.properties::MyKey"} to the key value in the properties file at the path
     * "com/domain/document.properties".
     * </p>
     *
     * @return a Properties StringLookup instance.
     * @since 1.5
     */
    public StringLookup propertiesStringLookup() {
        return fences != null ? propertiesStringLookup(fences) : PropertiesStringLookup.INSTANCE;
    }

    /**
     * Returns a fenced Properties StringLookup instance.
     * <p>
     * Looks up the value for the key in the format "DocumentPath::MyKey":.
     * </p>
     * <p>
     * Note the use of "::" instead of ":" to allow for "C:" drive letters in paths.
     * </p>
     * <p>
     * For example: "com/domain/document.properties::MyKey".
     * </p>
     * <p>
     * To use a {@link StringLookup} fenced by the current directory, use:
     * </p>
     *
     * <pre>
     * StringLookupFactory.INSTANCE.fileStringLookup(Paths.get("")).lookup("com/domain/document.properties::MyKey");
     *
     * // throws IllegalArgumentException
     * StringLookupFactory.INSTANCE.fileStringLookup(Paths.get("")).lookup("com/domain/document.properties::MyKey");
     *
     * // throws IllegalArgumentException
     * StringLookupFactory.INSTANCE.fileStringLookup(Paths.get("")).lookup("com/domain/document.properties::MyKey");
     * </pre>
     * <p>
     * The above example converts {@code "com/domain/document.properties::MyKey"} to the key value in the properties file at the path
     * "com/domain/document.properties".
     * </p>
     * <p>
     * {@link StringSubstitutor} methods like {@link StringSubstitutor#replace(String)} will throw a {@link IllegalArgumentException} when a file doesn't
     * resolves in a fence.
     * </p>
     *
     * @param fences The fences guarding Path resolution.
     * @return a Properties StringLookup instance.
     * @since 1.12.0
     */
    public StringLookup propertiesStringLookup(final Path... fences) {
        return new PropertiesStringLookup(fences);
    }

    /**
     * Returns the ResourceBundleStringLookup singleton instance.
     * <p>
     * Looks up the value for a given key in the format "BundleName:BundleKey".
     * </p>
     * <p>
     * For example: "com.domain.messages:MyKey".
     * </p>
     * <p>
     * Using a {@link StringLookup} from the {@link StringLookupFactory}:
     * </p>
     *
     * <pre>
     * StringLookupFactory.INSTANCE.resourceBundleStringLookup().lookup("com.domain.messages:MyKey");
     * </pre>
     * <p>
     * Using a {@link StringSubstitutor}:
     * </p>
     *
     * <pre>
     * StringSubstitutor.createInterpolator().replace("... ${resourceBundle:com.domain.messages:MyKey} ..."));
     * </pre>
     * <p>
     * The above examples convert {@code "com.domain.messages:MyKey"} to the key value in the resource bundle at {@code "com.domain.messages"}.
     * </p>
     *
     * @return The ResourceBundleStringLookup singleton instance.
     */
    public StringLookup resourceBundleStringLookup() {
        return ResourceBundleStringLookup.INSTANCE;
    }

    /**
     * Returns a ResourceBundleStringLookup instance for the given bundle name.
     * <p>
     * Looks up the value for a given key in the format "MyKey".
     * </p>
     * <p>
     * For example: "MyKey".
     * </p>
     * <p>
     * Using a {@link StringLookup} from the {@link StringLookupFactory}:
     * </p>
     *
     * <pre>
     * StringLookupFactory.INSTANCE.resourceBundleStringLookup("com.domain.messages").lookup("MyKey");
     * </pre>
     * <p>
     * The above example converts {@code "MyKey"} to the key value in the resource bundle at {@code "com.domain.messages"}.
     * </p>
     *
     * @param bundleName Only lookup in this bundle.
     * @return a ResourceBundleStringLookup instance for the given bundle name.
     * @since 1.5
     */
    public StringLookup resourceBundleStringLookup(final String bundleName) {
        return new ResourceBundleStringLookup(bundleName);
    }

    /**
     * Returns the ScriptStringLookup singleton instance. NOTE: This lookup is not included as a {@link #addDefaultStringLookups(Map) default lookup} unless
     * explicitly enabled. See the class level documentation for details.
     * <p>
     * Looks up the value for the key in the format "ScriptEngineName:Script".
     * </p>
     * <p>
     * For example: "javascript:3 + 4".
     * </p>
     * <p>
     * Using a {@link StringLookup} from the {@link StringLookupFactory}:
     * </p>
     *
     * <pre>
     * StringLookupFactory.INSTANCE.scriptStringLookup().lookup("javascript:3 + 4");
     * </pre>
     * <p>
     * When used through a {@link StringSubstitutor}, this lookup must either be added programmatically (as below) or enabled as a default lookup using the
     * {@value #DEFAULT_STRING_LOOKUPS_PROPERTY} system property (see class documentation).
     * </p>
     *
     * <pre>
     * Map&lt;String, StringLookup&gt; lookupMap = new HashMap&lt;&gt;();
     * lookupMap.put("script", StringLookupFactory.INSTANCE.scriptStringLookup());
     *
     * StringLookup variableResolver = StringLookupFactory.INSTANCE.interpolatorStringLookup(lookupMap, null, false);
     *
     * String value = new StringSubstitutor(variableResolver).replace("${script:javascript:3 + 4}");
     * </pre>
     * <p>
     * The above examples convert {@code "javascript:3 + 4"} to {@code "7"}.
     * </p>
     *
     * @return The ScriptStringLookup singleton instance.
     * @since 1.5
     */
    public StringLookup scriptStringLookup() {
        return ScriptStringLookup.INSTANCE;
    }

    /**
     * Returns the SystemPropertyStringLookup singleton instance where the lookup key is a system property name.
     *
     * <p>
     * Using a {@link StringLookup} from the {@link StringLookupFactory}:
     * </p>
     *
     * <pre>
     * StringLookupFactory.INSTANCE.systemPropertyStringLookup().lookup("os.name");
     * </pre>
     * <p>
     * Using a {@link StringSubstitutor}:
     * </p>
     *
     * <pre>
     * StringSubstitutor.createInterpolator().replace("... ${sys:os.name} ..."));
     * </pre>
     * <p>
     * The above examples convert {@code "os.name"} to the operating system name.
     * </p>
     *
     * @return The SystemPropertyStringLookup singleton instance.
     */
    public StringLookup systemPropertyStringLookup() {
        return StringLookupFactory.INSTANCE_SYSTEM_PROPERTIES;
    }

    /**
     * Returns the UrlDecoderStringLookup singleton instance.
     * <p>
     * Decodes URL Strings using the UTF-8 encoding.
     * </p>
     * <p>
     * For example: "Hello%20World%21" becomes "Hello World!".
     * </p>
     * <p>
     * Using a {@link StringLookup} from the {@link StringLookupFactory}:
     * </p>
     *
     * <pre>
     * StringLookupFactory.INSTANCE.urlDecoderStringLookup().lookup("Hello%20World%21");
     * </pre>
     * <p>
     * Using a {@link StringSubstitutor}:
     * </p>
     *
     * <pre>
     * StringSubstitutor.createInterpolator().replace("... ${urlDecoder:Hello%20World%21} ..."));
     * </pre>
     * <p>
     * The above examples convert {@code "Hello%20World%21"} to {@code "Hello World!"}.
     * </p>
     *
     * @return The UrlStringLookup singleton instance.
     * @since 1.6
     */
    public StringLookup urlDecoderStringLookup() {
        return UrlDecoderStringLookup.INSTANCE;
    }

    /**
     * Returns the UrlDecoderStringLookup singleton instance.
     * <p>
     * Decodes URL Strings using the UTF-8 encoding.
     * </p>
     * <p>
     * For example: "Hello World!" becomes "Hello+World%21".
     * </p>
     * <p>
     * Using a {@link StringLookup} from the {@link StringLookupFactory}:
     * </p>
     *
     * <pre>
     * StringLookupFactory.INSTANCE.urlEncoderStringLookup().lookup("Hello World!");
     * </pre>
     * <p>
     * Using a {@link StringSubstitutor}:
     * </p>
     *
     * <pre>
     * StringSubstitutor.createInterpolator().replace("... ${urlEncoder:Hello World!} ..."));
     * </pre>
     * <p>
     * The above examples convert {@code "Hello World!"} to {@code "Hello%20World%21"}.
     * </p>
     *
     * @return The UrlStringLookup singleton instance.
     * @since 1.6
     */
    public StringLookup urlEncoderStringLookup() {
        return UrlEncoderStringLookup.INSTANCE;
    }

    /**
     * Returns the UrlStringLookup singleton instance. This lookup is not included as a {@link #addDefaultStringLookups(Map) default lookup} unless explicitly
     * enabled. See the class level documentation for details.
     * <p>
     * Looks up the value for the key in the format "CharsetName:URL".
     * </p>
     * <p>
     * For example, using the HTTP scheme: "UTF-8:http://www.google.com"
     * </p>
     * <p>
     * For example, using the file scheme: "UTF-8:file:///C:/somehome/commons/commons-text/src/test/resources/document.properties"
     * </p>
     * <p>
     * Using a {@link StringLookup} from the {@link StringLookupFactory}:
     * </p>
     *
     * <pre>
     * StringLookupFactory.INSTANCE.urlStringLookup().lookup("UTF-8:https://www.apache.org");
     * </pre>
     * <p>
     * When used through a {@link StringSubstitutor}, this lookup must either be added programmatically (as below) or enabled as a default lookup using the
     * {@value #DEFAULT_STRING_LOOKUPS_PROPERTY} system property (see class documentation).
     * </p>
     *
     * <pre>
     * Map&lt;String, StringLookup&gt; lookupMap = new HashMap&lt;&gt;();
     * lookupMap.put("url", StringLookupFactory.INSTANCE.urlStringLookup());
     *
     * StringLookup variableResolver = StringLookupFactory.INSTANCE.interpolatorStringLookup(lookupMap, null, false);
     *
     * String value = new StringSubstitutor(variableResolver).replace("${url:UTF-8:https://www.apache.org}");
     * </pre>
     * <p>
     * The above examples convert {@code "UTF-8:https://www.apache.org"} to the contents of that page.
     * </p>
     *
     * @return The UrlStringLookup singleton instance.
     * @since 1.5
     */
    public StringLookup urlStringLookup() {
        return UrlStringLookup.INSTANCE;
    }

    /**
     * Returns the XmlDecoderStringLookup singleton instance.
     * <p>
     * Decodes strings according to the XML 1.0 specification.
     * </p>
     * <p>
     * For example: "&amp;lt;element&amp;gt;" becomes "&lt;element&gt;".
     * </p>
     * <p>
     * Using a {@link StringLookup} from the {@link StringLookupFactory}:
     * </p>
     *
     * <pre>
     * StringLookupFactory.INSTANCE.xmlDecoderStringLookup().lookup("&amp;lt;element&amp;gt;");
     * </pre>
     * <p>
     * Using a {@link StringSubstitutor}:
     * </p>
     *
     * <pre>
     * StringSubstitutor.createInterpolator().replace("... ${xmlDecoder:&amp;lt;element&amp;gt;} ..."));
     * </pre>
     * <p>
     * The above examples convert {@code "&lt;element&gt;"} to {@code "<element>"}.
     * </p>
     *
     * @return The XmlDecoderStringLookup singleton instance.
     * @since 1.11.0
     */
    public StringLookup xmlDecoderStringLookup() {
        return XmlDecoderStringLookup.INSTANCE;
    }

    /**
     * Returns the XmlEncoderStringLookup singleton instance.
     * <p>
     * Encodes strings according to the XML 1.0 specification.
     * </p>
     * <p>
     * For example: "&lt;element&gt;" becomes "&amp;lt;element&amp;gt;".
     * </p>
     * <p>
     * Using a {@link StringLookup} from the {@link StringLookupFactory}:
     * </p>
     *
     * <pre>
     * StringLookupFactory.INSTANCE.xmlEncoderStringLookup().lookup("&lt;element&gt;");
     * </pre>
     * <p>
     * Using a {@link StringSubstitutor}:
     * </p>
     *
     * <pre>
     * StringSubstitutor.createInterpolator().replace("... ${xmlEncoder:&lt;element&gt;} ..."));
     * </pre>
     * <p>
     * The above examples convert {@code "<element>"} to {@code "&lt;element&gt;"}.
     * </p>
     *
     * @return The XmlEncoderStringLookup singleton instance.
     * @since 1.11.0
     */
    public StringLookup xmlEncoderStringLookup() {
        return XmlEncoderStringLookup.INSTANCE;
    }

    /**
     * Returns an XML StringLookup instance.
     * <p>
     * If this factory was built using {@link Builder#setFences(Path...)}, then the string lookup is fenced and will throw an {@link IllegalArgumentException}
     * if a lookup causes causes a path to resolve outside of these fences. Otherwise, the result is unfenced to preserved behavior from previous versions.
     * </p>
     * <p>
     * We look up the value for the key in the format "DocumentPath:XPath".
     * </p>
     * <p>
     * For example: "com/domain/document.xml:/path/to/node".
     * </p>
     * <p>
     * Using a {@link StringLookup} from the {@link StringLookupFactory}:
     * </p>
     *
     * <pre>
     * StringLookupFactory.INSTANCE.xmlStringLookup().lookup("com/domain/document.xml:/path/to/node");
     * </pre>
     * <p>
     * Using a {@link StringSubstitutor}:
     * </p>
     *
     * <pre>
     * StringSubstitutor.createInterpolator().replace("... ${xml:com/domain/document.xml:/path/to/node} ..."));
     * </pre>
     * <p>
     * The above examples convert {@code "com/domain/document.xml:/path/to/node"} to the value of the XPath in the XML document.
     * </p>
     *
     * @return An XML StringLookup instance.
     * @since 1.5
     */
    public StringLookup xmlStringLookup() {
        return fences != null ? xmlStringLookup(XmlStringLookup.DEFAULT_FEATURES, fences) : XmlStringLookup.INSTANCE;
    }

    /**
     * Returns an XML StringLookup instance.
     * <p>
     * If this factory was built using {@link Builder#setFences(Path...)}, then the string lookup is fenced and will throw an {@link IllegalArgumentException}
     * if a lookup causes causes a path to resolve outside of these fences. Otherwise, the result is unfenced to preserved behavior from previous versions.
     * </p>
     * <p>
     * We look up the value for the key in the format "DocumentPath:XPath".
     * </p>
     * <p>
     * For example: "com/domain/document.xml:/path/to/node".
     * </p>
     * <p>
     * Using a {@link StringLookup} from the {@link StringLookupFactory}:
     * </p>
     *
     * <pre>
     * StringLookupFactory.INSTANCE.xmlStringLookup().lookup("com/domain/document.xml:/path/to/node");
     * </pre>
     * <p>
     * Using a {@link StringSubstitutor}:
     * </p>
     *
     * <pre>
     * StringSubstitutor.createInterpolator().replace("... ${xml:com/domain/document.xml:/path/to/node} ..."));
     * </pre>
     * <p>
     * The above examples convert {@code "com/domain/document.xml:/path/to/node"} to the value of the XPath in the XML document.
     * </p>
     *
     * @param xPathFactoryFeatures XPathFactory features to set.
     * @return An XML StringLookup instance.
     * @see XPathFactory#setFeature(String, boolean)
     * @since 1.11.0
     */
    public StringLookup xmlStringLookup(final Map<String, Boolean> xPathFactoryFeatures) {
        return xmlStringLookup(xPathFactoryFeatures, fences);
    }

    /**
     * Returns a fenced XML StringLookup instance.
     * <p>
     * If this factory was built using {@link Builder#setFences(Path...)}, then the string lookup is fenced and will throw an {@link IllegalArgumentException}
     * if a lookup causes causes a path to resolve outside of these fences. Otherwise, the result is unfenced to preserved behavior from previous versions.
     * </p>
     * <p>
     * We look up the value for the key in the format "DocumentPath:XPath".
     * </p>
     * <p>
     * For example: "com/domain/document.xml:/path/to/node".
     * </p>
     * <p>
     * Using a {@link StringLookup} from the {@link StringLookupFactory} fenced by the current directory ({@code Paths.get("")}):
     * </p>
     *
     * <pre>
     * StringLookupFactory.INSTANCE.xmlStringLookup(map, Pathe.get("")).lookup("com/domain/document.xml:/path/to/node");
     * </pre>
     * <p>
     * To use a {@link StringLookup} fenced by the current directory, use:
     * </p>
     *
     * <pre>
     * StringLookupFactory.INSTANCE.xmlStringLookup(Paths.get("")).lookup("com/domain/document.xml:/path/to/node");
     *
     * // throws IllegalArgumentException
     * StringLookupFactory.INSTANCE.xmlStringLookup(Paths.get("")).lookup("/rootdir/foo/document.xml:/path/to/node");
     *
     * // throws IllegalArgumentException
     * StringLookupFactory.INSTANCE.xmlStringLookup(Paths.get("")).lookup("../com/domain/document.xml:/path/to/node");
     * </pre>
     * <p>
     * The above examples convert {@code "com/domain/document.xml:/path/to/node"} to the value of the XPath in the XML document.
     * </p>
     * <p>
     * {@link StringSubstitutor} methods like {@link StringSubstitutor#replace(String)} will throw a {@link IllegalArgumentException} when a file doesn't
     * resolves in a fence.
     * </p>
     *
     * @param xPathFactoryFeatures XPathFactory features to set.
     * @param fences               The fences guarding Path resolution.
     * @return An XML StringLookup instance.
     * @since 1.12.0
     */
    public StringLookup xmlStringLookup(final Map<String, Boolean> xPathFactoryFeatures, final Path... fences) {
        return new XmlStringLookup(xPathFactoryFeatures, fences);
    }
}
