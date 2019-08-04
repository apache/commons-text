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

import java.util.Map;

import org.apache.commons.text.StringSubstitutor;

/**
 * Provides access to lookups defined in this package.
 * <p>
 * The default lookups are:
 * </p>
 * <table>
 * <caption>Default String Lookups</caption>
 * <tr>
 * <th>Key</th>
 * <th>Implementation</th>
 * <th>Factory Method</th>
 * <th>Since</th>
 * </tr>
 * <tr>
 * <td>{@value #KEY_BASE64_DECODER}</td>
 * <td>{@link Base64DecoderStringLookup}</td>
 * <td>{@link #base64DecoderStringLookup()}</td>
 * <td>1.6</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_BASE64_ENCODER}</td>
 * <td>{@link Base64EncoderStringLookup}</td>
 * <td>{@link #base64EncoderStringLookup()}</td>
 * <td>1.6</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_CONST}</td>
 * <td>{@link ConstantStringLookup}</td>
 * <td>{@link #constantStringLookup()}</td>
 * <td>1.5</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_DATE}</td>
 * <td>{@link DateStringLookup}</td>
 * <td>{@link #dateStringLookup()}</td>
 * <td>1.5</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_ENV}</td>
 * <td>{@link EnvironmentVariableStringLookup}</td>
 * <td>{@link #environmentVariableStringLookup()}</td>
 * <td>1.3</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_FILE}</td>
 * <td>{@link FileStringLookup}</td>
 * <td>{@link #fileStringLookup()}</td>
 * <td>1.5</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_JAVA}</td>
 * <td>{@link JavaPlatformStringLookup}</td>
 * <td>{@link #javaPlatformStringLookup()}</td>
 * <td>1.5</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_LOCALHOST}</td>
 * <td>{@link LocalHostStringLookup}</td>
 * <td>{@link #localHostStringLookup()}</td>
 * <td>1.3</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_PROPERTIES}</td>
 * <td>{@link PropertiesStringLookup}</td>
 * <td>{@link #propertiesStringLookup()}</td>
 * <td>1.5</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_RESOURCE_BUNDLE}</td>
 * <td>{@link ResourceBundleStringLookup}</td>
 * <td>{@link #resourceBundleStringLookup()}</td>
 * <td>1.6</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_SCRIPT}</td>
 * <td>{@link ScriptStringLookup}</td>
 * <td>{@link #scriptStringLookup()}</td>
 * <td>1.5</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_SYS}</td>
 * <td>{@link SystemPropertyStringLookup}</td>
 * <td>{@link #systemPropertyStringLookup()}</td>
 * <td>1.3</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_URL}</td>
 * <td>{@link UrlStringLookup}</td>
 * <td>{@link #urlStringLookup()}</td>
 * <td>1.5</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_URL_DECODER}</td>
 * <td>{@link UrlDecoderStringLookup}</td>
 * <td>{@link #urlDecoderStringLookup()}</td>
 * <td>1.5</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_URL_ENCODER}</td>
 * <td>{@link UrlEncoderStringLookup}</td>
 * <td>{@link #urlEncoderStringLookup()}</td>
 * <td>1.5</td>
 * </tr>
 * <tr>
 * <td>{@value #KEY_XML}</td>
 * <td>{@link XmlStringLookup}</td>
 * <td>{@link #xmlStringLookup()}</td>
 * <td>1.5</td>
 * </tr>
 * </table>
 *
 * @since 1.3
 */
public final class StringLookupFactory {

    /**
     * Defines the singleton for this class.
     */
    public static final StringLookupFactory INSTANCE = new StringLookupFactory();

    /**
     * Default lookup key for interpolation.
     *
     * @since 1.6
     */
    public static final String KEY_BASE64_DECODER = "base64Decoder";

    /**
     * Default lookup key for interpolation.
     *
     * @since 1.6
     */
    public static final String KEY_BASE64_ENCODER = "base64Encoder";

    /**
     * Default lookup key for interpolation.
     *
     * @since 1.6
     */
    public static final String KEY_CONST = "const";

    /**
     * Default lookup key for interpolation.
     *
     * @since 1.6
     */
    public static final String KEY_DATE = "date";

    /**
     * Default lookup key for interpolation.
     *
     * @since 1.6
     */
    public static final String KEY_ENV = "env";

    /**
     * Default lookup key for interpolation.
     *
     * @since 1.6
     */
    public static final String KEY_FILE = "file";

    /**
     * Default lookup key for interpolation.
     *
     * @since 1.6
     */
    public static final String KEY_JAVA = "java";

    /**
     * Default lookup key for interpolation.
     *
     * @since 1.6
     */
    public static final String KEY_LOCALHOST = "localhost";

    /**
     * Default lookup key for interpolation.
     *
     * @since 1.6
     */
    public static final String KEY_PROPERTIES = "properties";

    /**
     * Default lookup key for interpolation.
     *
     * @since 1.6
     */
    public static final String KEY_RESOURCE_BUNDLE = "resourceBundle";

    /**
     * Default lookup key for interpolation.
     *
     * @since 1.6
     */
    public static final String KEY_SCRIPT = "script";

    /**
     * Default lookup key for interpolation.
     *
     * @since 1.6
     */
    public static final String KEY_SYS = "sys";

    /**
     * Default lookup key for interpolation.
     *
     * @since 1.6
     */
    public static final String KEY_URL = "url";

    /**
     * Default lookup key for interpolation.
     *
     * @since 1.6
     */
    public static final String KEY_URL_DECODER = "urlDecoder";

    /**
     * Default lookup key for interpolation.
     *
     * @since 1.6
     */
    public static final String KEY_URL_ENCODER = "urlEncoder";

    /**
     * Default lookup key for interpolation.
     *
     * @since 1.6
     */
    public static final String KEY_XML = "xml";

    /**
     * Clears any static resources.
     *
     * @since 1.5
     */
    public static void clear() {
        ConstantStringLookup.clear();
    }

    /**
     * No need to build instances for now.
     */
    private StringLookupFactory() {
        // empty
    }

    /**
     * Adds the {@link StringLookupFactory default lookups}.
     *
     * @param stringLookupMap the map of string lookups.
     * @since 1.5
     */
    public void addDefaultStringLookups(final Map<String, StringLookup> stringLookupMap) {
        if (stringLookupMap != null) {
            // "base64" is deprecated in favor of KEY_BASE64_DECODER.
            stringLookupMap.put("base64", Base64DecoderStringLookup.INSTANCE);
            for (final DefaultStringLookup stringLookup : DefaultStringLookup.values()) {
                stringLookupMap.put(stringLookup.getKey(), stringLookup.getStringLookup());
            }
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
     * @return The DateStringLookup singleton instance.
     * @since 1.5
     */
    public StringLookup base64DecoderStringLookup() {
        return Base64DecoderStringLookup.INSTANCE;
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
     * @return The DateStringLookup singleton instance.
     * @since 1.6
     */
    public StringLookup base64EncoderStringLookup() {
        return Base64EncoderStringLookup.INSTANCE;
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
     * @return The DateStringLookup singleton instance.
     * @since 1.5
     * @deprecated Use {@link #base64DecoderStringLookup()}.
     */
    @Deprecated
    public StringLookup base64StringLookup() {
        return Base64DecoderStringLookup.INSTANCE;
    }

    /**
     * Returns the ConstantStringLookup singleton instance to look up the value of a fully-qualified static final value.
     * <p>
     * Sometimes it is necessary in a configuration file to refer to a constant defined in a class. This can be done
     * with this lookup implementation. Variable names must be in the format {@code apackage.AClass.AFIELD}. The
     * {@code lookup(String)} method will split the passed in string at the last dot, separating the fully qualified
     * class name and the name of the constant (i.e. <b>static final</b>) member field. Then the class is loaded and the
     * field's value is obtained using reflection.
     * </p>
     * <p>
     * Once retrieved values are cached for fast access. This class is thread-safe. It can be used as a standard (i.e.
     * global) lookup object and serve multiple clients concurrently.
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
     * @return The DateStringLookup singleton instance.
     * @since 1.5
     */
    public StringLookup constantStringLookup() {
        return ConstantStringLookup.INSTANCE;
    }

    /**
     * Returns the DateStringLookup singleton instance to format the current date with the format given in the key in a
     * format compatible with {@link java.text.SimpleDateFormat}.
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
     * Returns the EnvironmentVariableStringLookup singleton instance where the lookup key is an environment variable
     * name.
     * <p>
     * Using a {@link StringLookup} from the {@link StringLookupFactory}:
     * </p>
     * 
     * <pre>
     * StringLookupFactory.INSTANCE.dateStringLookup().lookup("USER");
     * </pre>
     * <p>
     * Using a {@link StringSubstitutor}:
     * </p>
     * 
     * <pre>
     * StringSubstitutor.createInterpolator().replace("... ${env:USER} ..."));
     * </pre>
     * <p>
     * The above examples convert (on Linux) {@code "USER"} to the current user name. On Windows 10, you would use
     * {@code "USERNAME"} to the same effect.
     * </p>
     * 
     * @return The EnvironmentVariableStringLookup singleton instance.
     */
    public StringLookup environmentVariableStringLookup() {
        return EnvironmentVariableStringLookup.INSTANCE;
    }

    /**
     * Returns the FileStringLookup singleton instance.
     * <p>
     * Using a {@link StringLookup} from the {@link StringLookupFactory}:
     * </p>
     * 
     * <pre>
     * StringLookupFactory.INSTANCE.fileStringLookup().lookup("UTF-8:com/domain/document.properties");
     * </pre>
     * <p>
     * Using a {@link StringSubstitutor}:
     * </p>
     * 
     * <pre>
     * StringSubstitutor.createInterpolator().replace("... ${file:UTF-8:com/domain/document.properties} ..."));
     * </pre>
     * <p>
     * The above examples convert {@code "UTF-8:com/domain/document.properties"} to the contents of the file.
     * </p>
     *
     * @return The FileStringLookup singleton instance.
     * @since 1.5
     */
    public StringLookup fileStringLookup() {
        return FileStringLookup.INSTANCE;
    }

    /**
     * Returns a new InterpolatorStringLookup using the {@link StringLookupFactory default lookups}.
     * <p>
     * The lookups available to an interpolator are defined in 
     * </p>
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
     * @return a new InterpolatorStringLookup.
     */
    public StringLookup interpolatorStringLookup() {
        return InterpolatorStringLookup.INSTANCE;
    }

    /**
     * Returns a new InterpolatorStringLookup using the {@link StringLookupFactory default lookups}.
     * <p>
     * If {@code addDefaultLookups} is true, the following lookups are used in addition to the ones provided in
     * {@code stringLookupMap}:
     * </p>
     *
     * @param stringLookupMap     the map of string lookups.
     * @param defaultStringLookup the default string lookup.
     * @param addDefaultLookups   whether to use lookups as described above.
     * @return a new InterpolatorStringLookup.
     * @since 1.4
     */
    public StringLookup interpolatorStringLookup(final Map<String, StringLookup> stringLookupMap,
            final StringLookup defaultStringLookup, final boolean addDefaultLookups) {
        return new InterpolatorStringLookup(stringLookupMap, defaultStringLookup, addDefaultLookups);
    }

    /**
     * Returns a new InterpolatorStringLookup using the {@link StringLookupFactory default lookups}.
     *
     * @param <V> the value type the default string lookup's map.
     * @param map the default map for string lookups.
     * @return a new InterpolatorStringLookup.
     */
    public <V> StringLookup interpolatorStringLookup(final Map<String, V> map) {
        return new InterpolatorStringLookup(map);
    }

    /**
     * Returns a new InterpolatorStringLookup using the {@link StringLookupFactory default lookups}.
     *
     * @param defaultStringLookup the default string lookup.
     * @return a new InterpolatorStringLookup.
     */
    public StringLookup interpolatorStringLookup(final StringLookup defaultStringLookup) {
        return new InterpolatorStringLookup(defaultStringLookup);
    }

    /**
     * Returns the JavaPlatformStringLookup singleton instance. Looks up keys related to Java: Java version, JRE
     * version, VM version, and so on.
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
        return MapStringLookup.on(map);
    }

    /**
     * Returns the NullStringLookup singleton instance which always returns null.
     *
     * @return The NullStringLookup singleton instance.
     */
    public StringLookup nullStringLookup() {
        return NullStringLookup.INSTANCE;
    }

    /**
     * Returns the PropertiesStringLookup singleton instance.
     * <p>
     * Looks up the value for the key in the format "DocumentPath:Key".
     * </p>
     * <p>
     * For example: "com/domain/document.properties:Key".
     * </p>
     *
     * @return The PropertiesStringLookup singleton instance.
     * @since 1.5
     */
    public StringLookup propertiesStringLookup() {
        return PropertiesStringLookup.INSTANCE;
    }

    /**
     * Returns the ResourceBundleStringLookup singleton instance.
     * <p>
     * Looks up the value for a given key in the format "BundleName:BundleKey".
     * </p>
     * <p>
     * For example: "com.domain.messages:MyKey".
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
     * Looks up the value for a given key in the format "BundleKey".
     * </p>
     * <p>
     * For example: "MyKey".
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
     * Returns the ScriptStringLookup singleton instance.
     * <p>
     * Looks up the value for the key in the format "ScriptEngineName:Script".
     * </p>
     * <p>
     * For example: "javascript:3+4".
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
     * @return The SystemPropertyStringLookup singleton instance.
     */
    public StringLookup systemPropertyStringLookup() {
        return SystemPropertyStringLookup.INSTANCE;
    }

    /**
     * Returns the UrlDecoderStringLookup singleton instance.
     * <p>
     * Decodes URL Strings using the UTF-8 encoding.
     * </p>
     * <p>
     * For example: "Hello%20World%21" becomes "Hello World!".
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
     *
     * @return The UrlStringLookup singleton instance.
     * @since 1.6
     */
    public StringLookup urlEncoderStringLookup() {
        return UrlEncoderStringLookup.INSTANCE;
    }

    /**
     * Returns the UrlStringLookup singleton instance.
     * <p>
     * Looks up the value for the key in the format "CharsetName:URL".
     * </p>
     * <p>
     * For example, using the HTTP scheme: "UTF-8:http://www.google.com"
     * </p>
     * <p>
     * For example, using the file scheme:
     * "UTF-8:file:///C:/somehome/commons/commons-text/src/test/resources/document.properties"
     * </p>
     *
     * @return The UrlStringLookup singleton instance.
     * @since 1.5
     */
    public StringLookup urlStringLookup() {
        return UrlStringLookup.INSTANCE;
    }

    /**
     * Returns the XmlStringLookup singleton instance.
     * <p>
     * Looks up the value for the key in the format "DocumentPath:XPath".
     * </p>
     * <p>
     * For example: "com/domain/document.xml:/path/to/node".
     * </p>
     *
     * @return The XmlStringLookup singleton instance.
     * @since 1.5
     */
    public StringLookup xmlStringLookup() {
        return XmlStringLookup.INSTANCE;
    }

}
