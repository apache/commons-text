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

/**
 * Provides access to lookups defined in this package.
 * <ul>
 * <li>"base64" for the {@link Base64StringLookup} since 1.5.</li>
 * <li>"const" for the {@link ConstantStringLookup} since 1.5.</li>
 * <li>"date" for the {@link DateStringLookup}.</li>
 * <li>"env" for the {@link EnvironmentVariableStringLookup}.</li>
 * <li>"file" for the {@link FileStringLookup} since 1.5.</li>
 * <li>"java" for the {@link JavaPlatformStringLookup}.</li>
 * <li>"localhost" for the {@link LocalHostStringLookup}, see {@link #localHostStringLookup()} for key names; since
 * 1.3.</li>
 * <li>"properties" for the {@link PropertiesStringLookup} since 1.5.</li>
 * <li>"resourceBundle" for the {@link ResourceBundleStringLookup} since 1.6.</li>
 * <li>"script" for the {@link ScriptStringLookup} since 1.5.</li>
 * <li>"sys" for the {@link SystemPropertyStringLookup}.</li>
 * <li>"url" for the {@link UrlStringLookup} since 1.5.</li>
 * <li>"urlDecode" for the {@link UrlDecoderStringLookup} since 1.5.</li>
 * <li>"urlEncode" for the {@link UrlEncoderStringLookup} since 1.5.</li>
 * <li>"xml" for the {@link XmlStringLookup} since 1.5.</li>
 * </ul>
 *
 * @since 1.3
 */
public final class StringLookupFactory {

    /**
     * Defines the singleton for this class.
     */
    public static final StringLookupFactory INSTANCE = new StringLookupFactory();

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
     * The following lookups are installed:
     * <ul>
     * <li>"base64" for the {@link Base64StringLookup} since 1.5.</li>
     * <li>"const" for the {@link ConstantStringLookup} since 1.5.</li>
     * <li>"date" for the {@link DateStringLookup}.</li>
     * <li>"env" for the {@link EnvironmentVariableStringLookup}.</li>
     * <li>"file" for the {@link FileStringLookup} since 1.5.</li>
     * <li>"java" for the {@link JavaPlatformStringLookup}.</li>
     * <li>"localhost" for the {@link LocalHostStringLookup}, see {@link #localHostStringLookup()} for key names; since
     * 1.3.</li>
     * <li>"properties" for the {@link PropertiesStringLookup} since 1.5.</li>
     * <li>"resourceBundle" for the {@link ResourceBundleStringLookup} since 1.6.</li>
     * <li>"script" for the {@link ScriptStringLookup} since 1.5.</li>
     * <li>"sys" for the {@link SystemPropertyStringLookup}.</li>
     * <li>"url" for the {@link UrlStringLookup} since 1.5.</li>
     * <li>"urlDecode" for the {@link UrlDecoderStringLookup} since 1.5.</li>
     * <li>"urlEncode" for the {@link UrlEncoderStringLookup} since 1.5.</li>
     * <li>"xml" for the {@link XmlStringLookup} since 1.5.</li>
     * </ul>
     *
     * @param stringLookupMap
     *            the map of string lookups.
     * @since 1.5
     */
    public void addDefaultStringLookups(final Map<String, StringLookup> stringLookupMap) {
        if (stringLookupMap != null) {
            stringLookupMap.put("sys", SystemPropertyStringLookup.INSTANCE);
            stringLookupMap.put("env", EnvironmentVariableStringLookup.INSTANCE);
            stringLookupMap.put("java", JavaPlatformStringLookup.INSTANCE);
            stringLookupMap.put("date", DateStringLookup.INSTANCE);
            stringLookupMap.put("localhost", LocalHostStringLookup.INSTANCE);
            stringLookupMap.put("xml", XmlStringLookup.INSTANCE);
            stringLookupMap.put("properties", PropertiesStringLookup.INSTANCE);
            stringLookupMap.put("resourceBundle", ResourceBundleStringLookup.INSTANCE);
            stringLookupMap.put("script", ScriptStringLookup.INSTANCE);
            stringLookupMap.put("file", FileStringLookup.INSTANCE);
            stringLookupMap.put("url", UrlStringLookup.INSTANCE);
            stringLookupMap.put("base64", Base64StringLookup.INSTANCE);
            stringLookupMap.put("urlEncode", UrlEncoderStringLookup.INSTANCE);
            stringLookupMap.put("urlDecode", UrlDecoderStringLookup.INSTANCE);
            stringLookupMap.put("const", ConstantStringLookup.INSTANCE);
        }
    }

    /**
     * Returns the DateStringLookup singleton instance to format the current date with the format given in the key in a
     * format compatible with {@link java.text.SimpleDateFormat}.
     *
     * @return the DateStringLookup singleton instance.
     */
    public StringLookup base64StringLookup() {
        return Base64StringLookup.INSTANCE;
    }

    /**
     * Returns the ConstantStringLookup singleton instance to get the value of a fully-qualified static final value.
     *
     * @return the DateStringLookup singleton instance.
     * @since 1.5
     */
    public StringLookup constantStringLookup() {
        return ConstantStringLookup.INSTANCE;
    }

    /**
     * Returns the DateStringLookup singleton instance to format the current date with the format given in the key in a
     * format compatible with {@link java.text.SimpleDateFormat}.
     *
     * @return the DateStringLookup singleton instance.
     */
    public StringLookup dateStringLookup() {
        return DateStringLookup.INSTANCE;
    }

    /**
     * Returns the EnvironmentVariableStringLookup singleton instance where the lookup key is an environment variable
     * name.
     *
     * @return the EnvironmentVariableStringLookup singleton instance.
     */
    public StringLookup environmentVariableStringLookup() {
        return EnvironmentVariableStringLookup.INSTANCE;
    }

    /**
     * Returns the FileStringLookup singleton instance.
     * <p>
     * Looks up the value for the key in the format "CharsetName:Path".
     * </p>
     * <p>
     * For example: "UTF-8:com/domain/document.properties".
     * </p>
     *
     * @return the FileStringLookup singleton instance.
     * @since 1.5
     */
    public StringLookup fileStringLookup() {
        return FileStringLookup.INSTANCE;
    }

    /**
     * Returns a new InterpolatorStringLookup.
     * <p>
     * The following lookups are used by default:
     * </p>
     * <ul>
     * <li>"base64" for the {@link Base64StringLookup} since 1.5.</li>
     * <li>"const" for the {@link ConstantStringLookup} since 1.5.</li>
     * <li>"date" for the {@link DateStringLookup}.</li>
     * <li>"env" for the {@link EnvironmentVariableStringLookup}.</li>
     * <li>"file" for the {@link FileStringLookup} since 1.5.</li>
     * <li>"java" for the {@link JavaPlatformStringLookup}.</li>
     * <li>"localhost" for the {@link LocalHostStringLookup}, see {@link #localHostStringLookup()} for key names; since
     * 1.3.</li>
     * <li>"properties" for the {@link PropertiesStringLookup} since 1.5.</li>
     * <li>"resourceBundle" for the {@link ResourceBundleStringLookup} since 1.6.</li>
     * <li>"script" for the {@link ScriptStringLookup} since 1.5.</li>
     * <li>"sys" for the {@link SystemPropertyStringLookup}.</li>
     * <li>"url" for the {@link UrlStringLookup} since 1.5.</li>
     * <li>"urlDecode" for the {@link UrlDecoderStringLookup} since 1.5.</li>
     * <li>"urlEncode" for the {@link UrlEncoderStringLookup} since 1.5.</li>
     * <li>"xml" for the {@link XmlStringLookup} since 1.5.</li>
     * </ul>
     *
     * @return a new InterpolatorStringLookup.
     */
    public StringLookup interpolatorStringLookup() {
        return new InterpolatorStringLookup();
    }

    /**
     * Returns a new InterpolatorStringLookup.
     * <p>
     * If {@code addDefaultLookups} is true, the following lookups are used in addition to the ones provided in
     * {@code stringLookupMap}:
     * </p>
     * <ul>
     * <li>"base64" for the {@link Base64StringLookup} since 1.5.</li>
     * <li>"const" for the {@link ConstantStringLookup} since 1.5.</li>
     * <li>"date" for the {@link DateStringLookup}.</li>
     * <li>"env" for the {@link EnvironmentVariableStringLookup}.</li>
     * <li>"file" for the {@link FileStringLookup} since 1.5.</li>
     * <li>"java" for the {@link JavaPlatformStringLookup}.</li>
     * <li>"localhost" for the {@link LocalHostStringLookup}, see {@link #localHostStringLookup()} for key names; since
     * 1.3.</li>
     * <li>"properties" for the {@link PropertiesStringLookup} since 1.5.</li>
     * <li>"resourceBundle" for the {@link ResourceBundleStringLookup} since 1.6.</li>
     * <li>"script" for the {@link ScriptStringLookup} since 1.5.</li>
     * <li>"sys" for the {@link SystemPropertyStringLookup}.</li>
     * <li>"url" for the {@link UrlStringLookup} since 1.5.</li>
     * <li>"urlDecode" for the {@link UrlDecoderStringLookup} since 1.5.</li>
     * <li>"urlEncode" for the {@link UrlEncoderStringLookup} since 1.5.</li>
     * <li>"xml" for the {@link XmlStringLookup} since 1.5.</li>
     * </ul>
     *
     * @param stringLookupMap
     *            the map of string lookups.
     * @param defaultStringLookup
     *            the default string lookup.
     * @param addDefaultLookups
     *            whether to use lookups as described above.
     * @return a new InterpolatorStringLookup.
     * @since 1.4
     */
    public StringLookup interpolatorStringLookup(final Map<String, StringLookup> stringLookupMap,
            final StringLookup defaultStringLookup, final boolean addDefaultLookups) {
        return new InterpolatorStringLookup(stringLookupMap, defaultStringLookup, addDefaultLookups);
    }

    /**
     * Returns a new InterpolatorStringLookup.
     * <p>
     * The following lookups are used by default:
     * </p>
     * <ul>
     * <li>"base64" for the {@link Base64StringLookup} since 1.5.</li>
     * <li>"const" for the {@link ConstantStringLookup} since 1.5.</li>
     * <li>"date" for the {@link DateStringLookup}.</li>
     * <li>"env" for the {@link EnvironmentVariableStringLookup}.</li>
     * <li>"file" for the {@link FileStringLookup} since 1.5.</li>
     * <li>"java" for the {@link JavaPlatformStringLookup}.</li>
     * <li>"localhost" for the {@link LocalHostStringLookup}, see {@link #localHostStringLookup()} for key names; since
     * 1.3.</li>
     * <li>"properties" for the {@link PropertiesStringLookup} since 1.5.</li>
     * <li>"resourceBundle" for the {@link ResourceBundleStringLookup} since 1.6.</li>
     * <li>"script" for the {@link ScriptStringLookup} since 1.5.</li>
     * <li>"sys" for the {@link SystemPropertyStringLookup}.</li>
     * <li>"url" for the {@link UrlStringLookup} since 1.5.</li>
     * <li>"urlDecode" for the {@link UrlDecoderStringLookup} since 1.5.</li>
     * <li>"urlEncode" for the {@link UrlEncoderStringLookup} since 1.5.</li>
     * <li>"xml" for the {@link XmlStringLookup} since 1.5.</li>
     * </ul>
     *
     * @param <V>
     *            the value type the default string lookup's map.
     * @param map
     *            the default map for string lookups.
     * @return a new InterpolatorStringLookup.
     */
    public <V> StringLookup interpolatorStringLookup(final Map<String, V> map) {
        return new InterpolatorStringLookup(map);
    }

    /**
     * Returns a new InterpolatorStringLookup.
     * <p>
     * The following lookups are used by default:
     * </p>
     * <ul>
     * <li>"base64" for the {@link Base64StringLookup} since 1.5.</li>
     * <li>"const" for the {@link ConstantStringLookup} since 1.5.</li>
     * <li>"date" for the {@link DateStringLookup}.</li>
     * <li>"env" for the {@link EnvironmentVariableStringLookup}.</li>
     * <li>"file" for the {@link FileStringLookup} since 1.5.</li>
     * <li>"java" for the {@link JavaPlatformStringLookup}.</li>
     * <li>"localhost" for the {@link LocalHostStringLookup}, see {@link #localHostStringLookup()} for key names; since
     * 1.3.</li>
     * <li>"properties" for the {@link PropertiesStringLookup} since 1.5.</li>
     * <li>"resourceBundle" for the {@link ResourceBundleStringLookup} since 1.6.</li>
     * <li>"script" for the {@link ScriptStringLookup} since 1.5.</li>
     * <li>"sys" for the {@link SystemPropertyStringLookup}.</li>
     * <li>"url" for the {@link UrlStringLookup} since 1.5.</li>
     * <li>"urlDecode" for the {@link UrlDecoderStringLookup} since 1.5.</li>
     * <li>"urlEncode" for the {@link UrlEncoderStringLookup} since 1.5.</li>
     * <li>"xml" for the {@link XmlStringLookup} since 1.5.</li>
     * </ul>
     *
     * @param defaultStringLookup
     *            the default string lookup.
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
     * @return the JavaPlatformStringLookup singleton instance.
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
     * @return the DateStringLookup singleton instance.
     */
    public StringLookup localHostStringLookup() {
        return LocalHostStringLookup.INSTANCE;
    }

    /**
     * Returns a new map-based lookup where the request for a lookup is answered with the value for that key.
     *
     * @param <V>
     *            the map value type.
     * @param map
     *            the map.
     * @return a new MapStringLookup.
     */
    public <V> StringLookup mapStringLookup(final Map<String, V> map) {
        return MapStringLookup.on(map);
    }

    /**
     * Returns the NullStringLookup singleton instance which always returns null.
     *
     * @return the NullStringLookup singleton instance.
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
     * @return the PropertiesStringLookup singleton instance.
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
     * @return the ResourceBundleStringLookup singleton instance.
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
     * @param bundleName
     *            Only lookup in this bundle.
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
     * For example: "javascript:\"HelloWorld\"".
     * </p>
     *
     * @return the ScriptStringLookup singleton instance.
     * @since 1.5
     */
    public StringLookup scriptStringLookup() {
        return ScriptStringLookup.INSTANCE;
    }

    /**
     * Returns the SystemPropertyStringLookup singleton instance where the lookup key is a system property name.
     *
     * @return the SystemPropertyStringLookup singleton instance.
     */
    public StringLookup systemPropertyStringLookup() {
        return SystemPropertyStringLookup.INSTANCE;
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
     * @return the UrlStringLookup singleton instance.
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
     * @return the XmlStringLookup singleton instance.
     * @since 1.5
     */
    public StringLookup xmlStringLookup() {
        return XmlStringLookup.INSTANCE;
    }

}
