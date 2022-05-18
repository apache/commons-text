/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.text;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.lang3.Validate;
import org.apache.commons.text.lookup.StringLookup;
import org.apache.commons.text.lookup.StringLookupFactory;
import org.apache.commons.text.matcher.StringMatcher;
import org.apache.commons.text.matcher.StringMatcherFactory;

/**
 * Substitutes variables within a string by values.
 * <p>
 * This class takes a piece of text and substitutes all the variables within it. The default definition of a variable is
 * {@code ${variableName}}. The prefix and suffix can be changed via constructors and set methods.
 * </p>
 * <p>
 * Variable values are typically resolved from a map, but could also be resolved from system properties, or by supplying
 * a custom variable resolver.
 * </p>
 * <h2>Using System Properties</h2>
 * <p>
 * The simplest example is to use this class to replace Java System properties. For example:
 * </p>
 *
 * <pre>
 * StringSubstitutor
 *     .replaceSystemProperties("You are running with java.version = ${java.version} and os.name = ${os.name}.");
 * </pre>
 *
 * <h2>Using a Custom Map</h2>
 * <p>
 * Typical usage of this class follows the following pattern:
 * </p>
 * <ul>
 * <li>Create and initialize a StringSubstitutor with the map that contains the values for the variables you want to
 * make available.</li>
 * <li>Optionally set attributes like variable prefix, variable suffix, default value delimiter, and so on.</li>
 * <li>Call the {@code replace()} method with in the source text for interpolation.</li>
 * <li>The returned text contains all variable references (as long as their values are known) as resolved.</li>
 * </ul>
 * <p>
 * For example:
 * </p>
 *
 * <pre>
 * // Build map
 * Map&lt;String, String&gt; valuesMap = new HashMap&lt;&gt;();
 * valuesMap.put(&quot;animal&quot;, &quot;quick brown fox&quot;);
 * valuesMap.put(&quot;target&quot;, &quot;lazy dog&quot;);
 * String templateString = &quot;The ${animal} jumped over the ${target}.&quot;;
 *
 * // Build StringSubstitutor
 * StringSubstitutor sub = new StringSubstitutor(valuesMap);
 *
 * // Replace
 * String resolvedString = sub.replace(templateString);
 * </pre>
 *
 * <p>
 * yielding:
 * </p>
 *
 * <pre>
 * "The quick brown fox jumped over the lazy dog."
 * </pre>
 *
 * <h2>Providing Default Values</h2>
 * <p>
 * You can set a default value for unresolved variables. The default value for a variable can be appended to the
 * variable name after the variable default value delimiter. The default value of the variable default value delimiter
 * is ":-", as in bash and other *nix shells.
 * </p>
 * <p>
 * You can set the variable value delimiter with {@link #setValueDelimiterMatcher(StringMatcher)},
 * {@link #setValueDelimiter(char)} or {@link #setValueDelimiter(String)}.
 * </p>
 * <p>
 * For example:
 * </p>
 *
 * <pre>
 * // Build map
 * Map&lt;String, String&gt; valuesMap = new HashMap&lt;&gt;();
 * valuesMap.put(&quot;animal&quot;, &quot;quick brown fox&quot;);
 * valuesMap.put(&quot;target&quot;, &quot;lazy dog&quot;);
 * String templateString = &quot;The ${animal} jumped over the ${target} ${undefined.number:-1234567890} times.&quot;;
 *
 * // Build StringSubstitutor
 * StringSubstitutor sub = new StringSubstitutor(valuesMap);
 *
 * // Replace
 * String resolvedString = sub.replace(templateString);
 * </pre>
 *
 * <p>
 * yielding:
 * </p>
 *
 * <pre>
 * "The quick brown fox jumped over the lazy dog 1234567890 times."
 * </pre>
 *
 * <p>
 * {@code StringSubstitutor} supports throwing exceptions for unresolved variables, you enable this by setting calling
 * {@link #setEnableUndefinedVariableException(boolean)} with {@code true}.
 * </p>
 *
 * <h2>Reusing Instances</h2>
 * <p>
 * Static shortcut methods cover the most common use cases. If multiple replace operations are to be performed, creating
 * and reusing an instance of this class will be more efficient.
 * </p>
 *
 * <h2>Using Interpolation</h2>
 * <p>
 * The default interpolator lets you use string lookups like:
 * </p>
 *
 * <pre>
 * final StringSubstitutor interpolator = StringSubstitutor.createInterpolator();
 * interpolator.setEnableSubstitutionInVariables(true); // Allows for nested $'s.
 * final String text = interpolator.replace("Base64 Decoder:        ${base64Decoder:SGVsbG9Xb3JsZCE=}\n"
 *     + "Base64 Encoder:        ${base64Encoder:HelloWorld!}\n"
 *     + "Java Constant:         ${const:java.awt.event.KeyEvent.VK_ESCAPE}\n"
 *     + "Date:                  ${date:yyyy-MM-dd}\n" + "DNS:                   ${dns:address|apache.org}\n"
 *     + "Environment Variable:  ${env:USERNAME}\n"
 *     + "File Content:          ${file:UTF-8:src/test/resources/document.properties}\n"
 *     + "Java:                  ${java:version}\n" + "Localhost:             ${localhost:canonical-name}\n"
 *     + "Properties File:       ${properties:src/test/resources/document.properties::mykey}\n"
 *     + "Resource Bundle:       ${resourceBundle:org.example.testResourceBundleLookup:mykey}\n"
 *     + "Script:                ${script:javascript:3 + 4}\n" + "System Property:       ${sys:user.dir}\n"
 *     + "URL Decoder:           ${urlDecoder:Hello%20World%21}\n"
 *     + "URL Encoder:           ${urlEncoder:Hello World!}\n"
 *     + "URL Content (HTTP):    ${url:UTF-8:http://www.apache.org}\n"
 *     + "URL Content (HTTPS):   ${url:UTF-8:https://www.apache.org}\n"
 *     + "URL Content (File):    ${url:UTF-8:file:///${sys:user.dir}/src/test/resources/document.properties}\n"
 *     + "XML XPath:             ${xml:src/test/resources/document.xml:/root/path/to/node}\n");
 * </pre>
 * <p>
 * For documentation of each lookup, see {@link StringLookupFactory}.
 * </p>
 *
 * <h2>Using Recursive Variable Replacement</h2>
 * <p>
 * Variable replacement can work recursively by calling {@link #setEnableSubstitutionInVariables(boolean)} with
 * {@code true}. If a variable value contains a variable then that variable will also be replaced. Cyclic replacements
 * are detected and will throw an exception.
 * </p>
 * <p>
 * You can get the replace result to contain a variable prefix. For example:
 * </p>
 *
 * <pre>
 * "The variable ${${name}} must be used."
 * </pre>
 *
 * <p>
 * If the value of the "name" variable is "x", then only the variable "name" is replaced resulting in:
 * </p>
 *
 * <pre>
 * "The variable ${x} must be used."
 * </pre>
 *
 * <p>
 * To achieve this effect there are two possibilities: Either set a different prefix and suffix for variables which do
 * not conflict with the result text you want to produce. The other possibility is to use the escape character, by
 * default '$'. If this character is placed before a variable reference, this reference is ignored and won't be
 * replaced. For example:
 * </p>
 *
 * <pre>
 * "The variable $${${name}} must be used."
 * </pre>
 * <p>
 * In some complex scenarios you might even want to perform substitution in the names of variables, for instance
 * </p>
 *
 * <pre>
 * ${jre-${java.specification.version}}
 * </pre>
 *
 * <p>
 * {@code StringSubstitutor} supports this recursive substitution in variable names, but it has to be enabled explicitly
 * by calling {@link #setEnableSubstitutionInVariables(boolean)} with {@code true}.
 * </p>
 *
 * <h2>Thread Safety</h2>
 * <p>
 * This class is <b>not</b> thread safe.
 * </p>
 *
 * @since 1.3
 */
public class StringSubstitutor {

    /**
     * The low-level result of a substitution.
     *
     * @since 1.9
     */
    private static final class Result {

        /** Whether the buffer is altered. */
        public final boolean altered;

        /** The length of change. */
        public final int lengthChange;

        private Result(final boolean altered, final int lengthChange) {
            this.altered = altered;
            this.lengthChange = lengthChange;
        }

        @Override
        public String toString() {
            return "Result [altered=" + altered + ", lengthChange=" + lengthChange + "]";
        }
    }

    /**
     * Constant for the default escape character.
     */
    public static final char DEFAULT_ESCAPE = '$';

    /**
     * The default variable default separator.
     *
     * @since 1.5.
     */
    public static final String DEFAULT_VAR_DEFAULT = ":-";

    /**
     * The default variable end separator.
     *
     * @since 1.5.
     */
    public static final String DEFAULT_VAR_END = "}";

    /**
     * The default variable start separator.
     *
     * @since 1.5.
     */
    public static final String DEFAULT_VAR_START = "${";

    /**
     * Constant for the default variable prefix.
     */
    public static final StringMatcher DEFAULT_PREFIX = StringMatcherFactory.INSTANCE.stringMatcher(DEFAULT_VAR_START);

    /**
     * Constant for the default variable suffix.
     */
    public static final StringMatcher DEFAULT_SUFFIX = StringMatcherFactory.INSTANCE.stringMatcher(DEFAULT_VAR_END);

    /**
     * Constant for the default value delimiter of a variable.
     */
    public static final StringMatcher DEFAULT_VALUE_DELIMITER = StringMatcherFactory.INSTANCE
        .stringMatcher(DEFAULT_VAR_DEFAULT);

    /**
     * Creates a new instance using the interpolator string lookup
     * {@link StringLookupFactory#interpolatorStringLookup()}.
     * <p>
     * This StringSubstitutor lets you perform substitutions like:
     * </p>
     *
     * <pre>
     * StringSubstitutor.createInterpolator().replace(
     *   "OS name: ${sys:os.name}, " + "3 + 4 = ${script:javascript:3 + 4}");
     * </pre>
     *
     * @return a new instance using the interpolator string lookup.
     * @see StringLookupFactory#interpolatorStringLookup()
     * @since 1.8
     */
    public static StringSubstitutor createInterpolator() {
        return new StringSubstitutor(StringLookupFactory.INSTANCE.interpolatorStringLookup());
    }

    /**
     * Replaces all the occurrences of variables in the given source object with their matching values from the map.
     *
     * @param <V> the type of the values in the map
     * @param source the source text containing the variables to substitute, null returns null
     * @param valueMap the map with the values, may be null
     * @return The result of the replace operation
     * @throws IllegalArgumentException if a variable is not found and enableUndefinedVariableException is true
     */
    public static <V> String replace(final Object source, final Map<String, V> valueMap) {
        return new StringSubstitutor(valueMap).replace(source);
    }

    /**
     * Replaces all the occurrences of variables in the given source object with their matching values from the map.
     * This method allows to specify a custom variable prefix and suffix
     *
     * @param <V> the type of the values in the map
     * @param source the source text containing the variables to substitute, null returns null
     * @param valueMap the map with the values, may be null
     * @param prefix the prefix of variables, not null
     * @param suffix the suffix of variables, not null
     * @return The result of the replace operation
     * @throws IllegalArgumentException if the prefix or suffix is null
     * @throws IllegalArgumentException if a variable is not found and enableUndefinedVariableException is true
     */
    public static <V> String replace(final Object source, final Map<String, V> valueMap, final String prefix,
        final String suffix) {
        return new StringSubstitutor(valueMap, prefix, suffix).replace(source);
    }

    /**
     * Replaces all the occurrences of variables in the given source object with their matching values from the
     * properties.
     *
     * @param source the source text containing the variables to substitute, null returns null
     * @param valueProperties the properties with values, may be null
     * @return The result of the replace operation
     * @throws IllegalArgumentException if a variable is not found and enableUndefinedVariableException is true
     */
    public static String replace(final Object source, final Properties valueProperties) {
        if (valueProperties == null) {
            return source.toString();
        }
        final Map<String, String> valueMap = new HashMap<>();
        final Enumeration<?> propNames = valueProperties.propertyNames();
        while (propNames.hasMoreElements()) {
            final String propName = (String) propNames.nextElement();
            final String propValue = valueProperties.getProperty(propName);
            valueMap.put(propName, propValue);
        }
        return StringSubstitutor.replace(source, valueMap);
    }

    /**
     * Replaces all the occurrences of variables in the given source object with their matching values from the system
     * properties.
     *
     * @param source the source text containing the variables to substitute, null returns null
     * @return The result of the replace operation
     * @throws IllegalArgumentException if a variable is not found and enableUndefinedVariableException is true
     */
    public static String replaceSystemProperties(final Object source) {
        return new StringSubstitutor(StringLookupFactory.INSTANCE.systemPropertyStringLookup()).replace(source);
    }

    /**
     * The flag whether substitution in variable values is disabled.
     */
    private boolean disableSubstitutionInValues;

    /**
     * The flag whether substitution in variable names is enabled.
     */
    private boolean enableSubstitutionInVariables;

    /**
     * The flag whether exception should be thrown on undefined variable.
     */
    private boolean enableUndefinedVariableException;

    /**
     * Stores the escape character.
     */
    private char escapeChar;

    /**
     * Stores the variable prefix.
     */
    private StringMatcher prefixMatcher;

    /**
     * Whether escapes should be preserved. Default is false;
     */
    private boolean preserveEscapes;

    /**
     * Stores the variable suffix.
     */
    private StringMatcher suffixMatcher;

    /**
     * Stores the default variable value delimiter.
     */
    private StringMatcher valueDelimiterMatcher;

    /**
     * Variable resolution is delegated to an implementor of {@link StringLookup}.
     */
    private StringLookup variableResolver;

    /**
     * Creates a new instance with defaults for variable prefix and suffix and the escaping character.
     */
    public StringSubstitutor() {
        this((StringLookup) null, DEFAULT_PREFIX, DEFAULT_SUFFIX, DEFAULT_ESCAPE);
    }

    /**
     * Creates a new instance and initializes it. Uses defaults for variable prefix and suffix and the escaping
     * character.
     *
     * @param <V> the type of the values in the map
     * @param valueMap the map with the variables' values, may be null
     */
    public <V> StringSubstitutor(final Map<String, V> valueMap) {
        this(StringLookupFactory.INSTANCE.mapStringLookup(valueMap), DEFAULT_PREFIX, DEFAULT_SUFFIX, DEFAULT_ESCAPE);
    }

    /**
     * Creates a new instance and initializes it. Uses a default escaping character.
     *
     * @param <V> the type of the values in the map
     * @param valueMap the map with the variables' values, may be null
     * @param prefix the prefix for variables, not null
     * @param suffix the suffix for variables, not null
     * @throws IllegalArgumentException if the prefix or suffix is null
     */
    public <V> StringSubstitutor(final Map<String, V> valueMap, final String prefix, final String suffix) {
        this(StringLookupFactory.INSTANCE.mapStringLookup(valueMap), prefix, suffix, DEFAULT_ESCAPE);
    }

    /**
     * Creates a new instance and initializes it.
     *
     * @param <V> the type of the values in the map
     * @param valueMap the map with the variables' values, may be null
     * @param prefix the prefix for variables, not null
     * @param suffix the suffix for variables, not null
     * @param escape the escape character
     * @throws IllegalArgumentException if the prefix or suffix is null
     */
    public <V> StringSubstitutor(final Map<String, V> valueMap, final String prefix, final String suffix,
        final char escape) {
        this(StringLookupFactory.INSTANCE.mapStringLookup(valueMap), prefix, suffix, escape);
    }

    /**
     * Creates a new instance and initializes it.
     *
     * @param <V> the type of the values in the map
     * @param valueMap the map with the variables' values, may be null
     * @param prefix the prefix for variables, not null
     * @param suffix the suffix for variables, not null
     * @param escape the escape character
     * @param valueDelimiter the variable default value delimiter, may be null
     * @throws IllegalArgumentException if the prefix or suffix is null
     */
    public <V> StringSubstitutor(final Map<String, V> valueMap, final String prefix, final String suffix,
        final char escape, final String valueDelimiter) {
        this(StringLookupFactory.INSTANCE.mapStringLookup(valueMap), prefix, suffix, escape, valueDelimiter);
    }

    /**
     * Creates a new instance and initializes it.
     *
     * @param variableResolver the variable resolver, may be null
     */
    public StringSubstitutor(final StringLookup variableResolver) {
        this(variableResolver, DEFAULT_PREFIX, DEFAULT_SUFFIX, DEFAULT_ESCAPE);
    }

    /**
     * Creates a new instance and initializes it.
     *
     * @param variableResolver the variable resolver, may be null
     * @param prefix the prefix for variables, not null
     * @param suffix the suffix for variables, not null
     * @param escape the escape character
     * @throws IllegalArgumentException if the prefix or suffix is null
     */
    public StringSubstitutor(final StringLookup variableResolver, final String prefix, final String suffix,
        final char escape) {
        this.setVariableResolver(variableResolver);
        this.setVariablePrefix(prefix);
        this.setVariableSuffix(suffix);
        this.setEscapeChar(escape);
        this.setValueDelimiterMatcher(DEFAULT_VALUE_DELIMITER);
    }

    /**
     * Creates a new instance and initializes it.
     *
     * @param variableResolver the variable resolver, may be null
     * @param prefix the prefix for variables, not null
     * @param suffix the suffix for variables, not null
     * @param escape the escape character
     * @param valueDelimiter the variable default value delimiter string, may be null
     * @throws IllegalArgumentException if the prefix or suffix is null
     */
    public StringSubstitutor(final StringLookup variableResolver, final String prefix, final String suffix,
        final char escape, final String valueDelimiter) {
        this.setVariableResolver(variableResolver);
        this.setVariablePrefix(prefix);
        this.setVariableSuffix(suffix);
        this.setEscapeChar(escape);
        this.setValueDelimiter(valueDelimiter);
    }

    /**
     * Creates a new instance and initializes it.
     *
     * @param variableResolver the variable resolver, may be null
     * @param prefixMatcher the prefix for variables, not null
     * @param suffixMatcher the suffix for variables, not null
     * @param escape the escape character
     * @throws IllegalArgumentException if the prefix or suffix is null
     */
    public StringSubstitutor(final StringLookup variableResolver, final StringMatcher prefixMatcher,
        final StringMatcher suffixMatcher, final char escape) {
        this(variableResolver, prefixMatcher, suffixMatcher, escape, DEFAULT_VALUE_DELIMITER);
    }

    /**
     * Creates a new instance and initializes it.
     *
     * @param variableResolver the variable resolver, may be null
     * @param prefixMatcher the prefix for variables, not null
     * @param suffixMatcher the suffix for variables, not null
     * @param escape the escape character
     * @param valueDelimiterMatcher the variable default value delimiter matcher, may be null
     * @throws IllegalArgumentException if the prefix or suffix is null
     */
    public StringSubstitutor(final StringLookup variableResolver, final StringMatcher prefixMatcher,
        final StringMatcher suffixMatcher, final char escape, final StringMatcher valueDelimiterMatcher) {
        this.setVariableResolver(variableResolver);
        this.setVariablePrefixMatcher(prefixMatcher);
        this.setVariableSuffixMatcher(suffixMatcher);
        this.setEscapeChar(escape);
        this.setValueDelimiterMatcher(valueDelimiterMatcher);
    }

    /**
     * Creates a new instance based on the given StringSubstitutor.
     *
     * @param other The StringSubstitutor used as the source.
     * @since 1.9
     */
    public StringSubstitutor(final StringSubstitutor other) {
        disableSubstitutionInValues = other.isDisableSubstitutionInValues();
        enableSubstitutionInVariables = other.isEnableSubstitutionInVariables();
        enableUndefinedVariableException = other.isEnableUndefinedVariableException();
        escapeChar = other.getEscapeChar();
        prefixMatcher = other.getVariablePrefixMatcher();
        preserveEscapes = other.isPreserveEscapes();
        suffixMatcher = other.getVariableSuffixMatcher();
        valueDelimiterMatcher = other.getValueDelimiterMatcher();
        variableResolver = other.getStringLookup();
    }

    /**
     * Checks if the specified variable is already in the stack (list) of variables.
     *
     * @param varName the variable name to check
     * @param priorVariables the list of prior variables
     */
    private void checkCyclicSubstitution(final String varName, final List<String> priorVariables) {
        if (!priorVariables.contains(varName)) {
            return;
        }
        final TextStringBuilder buf = new TextStringBuilder(256);
        buf.append("Infinite loop in property interpolation of ");
        buf.append(priorVariables.remove(0));
        buf.append(": ");
        buf.appendWithSeparators(priorVariables, "->");
        throw new IllegalStateException(buf.toString());
    }

    // Escape
    /**
     * Returns the escape character.
     *
     * @return The character used for escaping variable references
     */
    public char getEscapeChar() {
        return this.escapeChar;
    }

    /**
     * Gets the StringLookup that is used to lookup variables.
     *
     * @return The StringLookup
     */
    public StringLookup getStringLookup() {
        return this.variableResolver;
    }

    /**
     * Gets the variable default value delimiter matcher currently in use.
     * <p>
     * The variable default value delimiter is the character or characters that delimit the variable name and the
     * variable default value. This delimiter is expressed in terms of a matcher allowing advanced variable default
     * value delimiter matches.
     * </p>
     * <p>
     * If it returns null, then the variable default value resolution is disabled.
     *
     * @return The variable default value delimiter matcher in use, may be null
     */
    public StringMatcher getValueDelimiterMatcher() {
        return valueDelimiterMatcher;
    }

    /**
     * Gets the variable prefix matcher currently in use.
     * <p>
     * The variable prefix is the character or characters that identify the start of a variable. This prefix is
     * expressed in terms of a matcher allowing advanced prefix matches.
     * </p>
     *
     * @return The prefix matcher in use
     */
    public StringMatcher getVariablePrefixMatcher() {
        return prefixMatcher;
    }

    /**
     * Gets the variable suffix matcher currently in use.
     * <p>
     * The variable suffix is the character or characters that identify the end of a variable. This suffix is expressed
     * in terms of a matcher allowing advanced suffix matches.
     * </p>
     *
     * @return The suffix matcher in use
     */
    public StringMatcher getVariableSuffixMatcher() {
        return suffixMatcher;
    }

    /**
     * Returns a flag whether substitution is disabled in variable values.If set to <b>true</b>, the values of variables
     * can contain other variables will not be processed and substituted original variable is evaluated, e.g.
     *
     * <pre>
     * Map&lt;String, String&gt; valuesMap = new HashMap&lt;&gt;();
     * valuesMap.put(&quot;name&quot;, &quot;Douglas ${surname}&quot;);
     * valuesMap.put(&quot;surname&quot;, &quot;Crockford&quot;);
     * String templateString = &quot;Hi ${name}&quot;;
     * StrSubstitutor sub = new StrSubstitutor(valuesMap);
     * String resolvedString = sub.replace(templateString);
     * </pre>
     *
     * yielding:
     *
     * <pre>
     *      Hi Douglas ${surname}
     * </pre>
     *
     * @return The substitution in variable values flag
     */
    public boolean isDisableSubstitutionInValues() {
        return disableSubstitutionInValues;
    }

    /**
     * Returns a flag whether substitution is done in variable names.
     *
     * @return The substitution in variable names flag
     */
    public boolean isEnableSubstitutionInVariables() {
        return enableSubstitutionInVariables;
    }

    /**
     * Returns a flag whether exception can be thrown upon undefined variable.
     *
     * @return The fail on undefined variable flag
     */
    public boolean isEnableUndefinedVariableException() {
        return enableUndefinedVariableException;
    }

    /**
     * Returns the flag controlling whether escapes are preserved during substitution.
     *
     * @return The preserve escape flag
     */
    public boolean isPreserveEscapes() {
        return preserveEscapes;
    }

    /**
     * Replaces all the occurrences of variables with their matching values from the resolver using the given source
     * array as a template. The array is not altered by this method.
     *
     * @param source the character array to replace in, not altered, null returns null
     * @return The result of the replace operation
     * @throws IllegalArgumentException if variable is not found when its allowed to throw exception
     */
    public String replace(final char[] source) {
        if (source == null) {
            return null;
        }
        final TextStringBuilder buf = new TextStringBuilder(source.length).append(source);
        substitute(buf, 0, source.length);
        return buf.toString();
    }

    /**
     * Replaces all the occurrences of variables with their matching values from the resolver using the given source
     * array as a template. The array is not altered by this method.
     * <p>
     * Only the specified portion of the array will be processed. The rest of the array is not processed, and is not
     * returned.
     * </p>
     *
     * @param source the character array to replace in, not altered, null returns null
     * @param offset the start offset within the array, must be valid
     * @param length the length within the array to be processed, must be valid
     * @return The result of the replace operation
     * @throws IllegalArgumentException if variable is not found when its allowed to throw exception
     * @throws StringIndexOutOfBoundsException if {@code offset} is not in the
     *  range {@code 0 <= offset <= chars.length}
     * @throws StringIndexOutOfBoundsException if {@code length < 0}
     * @throws StringIndexOutOfBoundsException if {@code offset + length > chars.length}
     */
    public String replace(final char[] source, final int offset, final int length) {
        if (source == null) {
            return null;
        }
        final TextStringBuilder buf = new TextStringBuilder(length).append(source, offset, length);
        substitute(buf, 0, length);
        return buf.toString();
    }

    /**
     * Replaces all the occurrences of variables with their matching values from the resolver using the given source as
     * a template. The source is not altered by this method.
     *
     * @param source the buffer to use as a template, not changed, null returns null
     * @return The result of the replace operation
     * @throws IllegalArgumentException if variable is not found when its allowed to throw exception
     */
    public String replace(final CharSequence source) {
        if (source == null) {
            return null;
        }
        return replace(source, 0, source.length());
    }

    /**
     * Replaces all the occurrences of variables with their matching values from the resolver using the given source as
     * a template. The source is not altered by this method.
     * <p>
     * Only the specified portion of the buffer will be processed. The rest of the buffer is not processed, and is not
     * returned.
     * </p>
     *
     * @param source the buffer to use as a template, not changed, null returns null
     * @param offset the start offset within the array, must be valid
     * @param length the length within the array to be processed, must be valid
     * @return The result of the replace operation
     * @throws IllegalArgumentException if variable is not found when its allowed to throw exception
     */
    public String replace(final CharSequence source, final int offset, final int length) {
        if (source == null) {
            return null;
        }
        final TextStringBuilder buf = new TextStringBuilder(length).append(source.toString(), offset, length);
        substitute(buf, 0, length);
        return buf.toString();
    }

    /**
     * Replaces all the occurrences of variables in the given source object with their matching values from the
     * resolver. The input source object is converted to a string using {@code toString} and is not altered.
     *
     * @param source the source to replace in, null returns null
     * @return The result of the replace operation
     * @throws IllegalArgumentException if a variable is not found and enableUndefinedVariableException is true
     */
    public String replace(final Object source) {
        if (source == null) {
            return null;
        }
        final TextStringBuilder buf = new TextStringBuilder().append(source);
        substitute(buf, 0, buf.length());
        return buf.toString();
    }

    /**
     * Replaces all the occurrences of variables with their matching values from the resolver using the given source
     * string as a template.
     *
     * @param source the string to replace in, null returns null
     * @return The result of the replace operation
     * @throws IllegalArgumentException if variable is not found when its allowed to throw exception
     */
    public String replace(final String source) {
        if (source == null) {
            return null;
        }
        final TextStringBuilder buf = new TextStringBuilder(source);
        if (!substitute(buf, 0, source.length())) {
            return source;
        }
        return buf.toString();
    }

    /**
     * Replaces all the occurrences of variables with their matching values from the resolver using the given source
     * string as a template.
     * <p>
     * Only the specified portion of the string will be processed. The rest of the string is not processed, and is not
     * returned.
     * </p>
     *
     * @param source the string to replace in, null returns null
     * @param offset the start offset within the source, must be valid
     * @param length the length within the source to be processed, must be valid
     * @return The result of the replace operation
     * @throws IllegalArgumentException if variable is not found when its allowed to throw exception
     * @throws StringIndexOutOfBoundsException if {@code offset} is not in the
     *  range {@code 0 <= offset <= source.length()}
     * @throws StringIndexOutOfBoundsException if {@code length < 0}
     * @throws StringIndexOutOfBoundsException if {@code offset + length > source.length()}
     */
    public String replace(final String source, final int offset, final int length) {
        if (source == null) {
            return null;
        }
        final TextStringBuilder buf = new TextStringBuilder(length).append(source, offset, length);
        if (!substitute(buf, 0, length)) {
            return source.substring(offset, offset + length);
        }
        return buf.toString();
    }

    /**
     * Replaces all the occurrences of variables with their matching values from the resolver using the given source
     * buffer as a template. The buffer is not altered by this method.
     *
     * @param source the buffer to use as a template, not changed, null returns null
     * @return The result of the replace operation
     * @throws IllegalArgumentException if variable is not found when its allowed to throw exception
     */
    public String replace(final StringBuffer source) {
        if (source == null) {
            return null;
        }
        final TextStringBuilder buf = new TextStringBuilder(source.length()).append(source);
        substitute(buf, 0, buf.length());
        return buf.toString();
    }

    /**
     * Replaces all the occurrences of variables with their matching values from the resolver using the given source
     * buffer as a template. The buffer is not altered by this method.
     * <p>
     * Only the specified portion of the buffer will be processed. The rest of the buffer is not processed, and is not
     * returned.
     * </p>
     *
     * @param source the buffer to use as a template, not changed, null returns null
     * @param offset the start offset within the source, must be valid
     * @param length the length within the source to be processed, must be valid
     * @return The result of the replace operation
     * @throws IllegalArgumentException if variable is not found when its allowed to throw exception
     */
    public String replace(final StringBuffer source, final int offset, final int length) {
        if (source == null) {
            return null;
        }
        final TextStringBuilder buf = new TextStringBuilder(length).append(source, offset, length);
        substitute(buf, 0, length);
        return buf.toString();
    }

    /**
     * Replaces all the occurrences of variables with their matching values from the resolver using the given source
     * builder as a template. The builder is not altered by this method.
     *
     * @param source the builder to use as a template, not changed, null returns null
     * @return The result of the replace operation
     * @throws IllegalArgumentException if variable is not found when its allowed to throw exception
     */
    public String replace(final TextStringBuilder source) {
        if (source == null) {
            return null;
        }
        final TextStringBuilder builder = new TextStringBuilder(source.length()).append(source);
        substitute(builder, 0, builder.length());
        return builder.toString();
    }

    /**
     * Replaces all the occurrences of variables with their matching values from the resolver using the given source
     * builder as a template. The builder is not altered by this method.
     * <p>
     * Only the specified portion of the builder will be processed. The rest of the builder is not processed, and is not
     * returned.
     * </p>
     *
     * @param source the builder to use as a template, not changed, null returns null
     * @param offset the start offset within the source, must be valid
     * @param length the length within the source to be processed, must be valid
     * @return The result of the replace operation
     * @throws IllegalArgumentException if variable is not found when its allowed to throw exception
     */
    public String replace(final TextStringBuilder source, final int offset, final int length) {
        if (source == null) {
            return null;
        }
        final TextStringBuilder buf = new TextStringBuilder(length).append(source, offset, length);
        substitute(buf, 0, length);
        return buf.toString();
    }

    /**
     * Replaces all the occurrences of variables within the given source buffer with their matching values from the
     * resolver. The buffer is updated with the result.
     *
     * @param source the buffer to replace in, updated, null returns zero
     * @return true if altered
     */
    public boolean replaceIn(final StringBuffer source) {
        if (source == null) {
            return false;
        }
        return replaceIn(source, 0, source.length());
    }

    /**
     * Replaces all the occurrences of variables within the given source buffer with their matching values from the
     * resolver. The buffer is updated with the result.
     * <p>
     * Only the specified portion of the buffer will be processed. The rest of the buffer is not processed, but it is
     * not deleted.
     * </p>
     *
     * @param source the buffer to replace in, updated, null returns zero
     * @param offset the start offset within the source, must be valid
     * @param length the length within the source to be processed, must be valid
     * @return true if altered
     * @throws IllegalArgumentException if variable is not found when its allowed to throw exception
     */
    public boolean replaceIn(final StringBuffer source, final int offset, final int length) {
        if (source == null) {
            return false;
        }
        final TextStringBuilder buf = new TextStringBuilder(length).append(source, offset, length);
        if (!substitute(buf, 0, length)) {
            return false;
        }
        source.replace(offset, offset + length, buf.toString());
        return true;
    }

    /**
     * Replaces all the occurrences of variables within the given source buffer with their matching values from the
     * resolver. The buffer is updated with the result.
     *
     * @param source the buffer to replace in, updated, null returns zero
     * @return true if altered
     */
    public boolean replaceIn(final StringBuilder source) {
        if (source == null) {
            return false;
        }
        return replaceIn(source, 0, source.length());
    }

    /**
     * Replaces all the occurrences of variables within the given source builder with their matching values from the
     * resolver. The builder is updated with the result.
     * <p>
     * Only the specified portion of the buffer will be processed. The rest of the buffer is not processed, but it is
     * not deleted.
     * </p>
     *
     * @param source the buffer to replace in, updated, null returns zero
     * @param offset the start offset within the source, must be valid
     * @param length the length within the source to be processed, must be valid
     * @return true if altered
     * @throws IllegalArgumentException if variable is not found when its allowed to throw exception
     */
    public boolean replaceIn(final StringBuilder source, final int offset, final int length) {
        if (source == null) {
            return false;
        }
        final TextStringBuilder buf = new TextStringBuilder(length).append(source, offset, length);
        if (!substitute(buf, 0, length)) {
            return false;
        }
        source.replace(offset, offset + length, buf.toString());
        return true;
    }

    /**
     * Replaces all the occurrences of variables within the given source builder with their matching values from the
     * resolver.
     *
     * @param source the builder to replace in, updated, null returns zero
     * @return true if altered
     * @throws IllegalArgumentException if variable is not found when its allowed to throw exception
     */
    public boolean replaceIn(final TextStringBuilder source) {
        if (source == null) {
            return false;
        }
        return substitute(source, 0, source.length());
    }

    /**
     * Replaces all the occurrences of variables within the given source builder with their matching values from the
     * resolver.
     * <p>
     * Only the specified portion of the builder will be processed. The rest of the builder is not processed, but it is
     * not deleted.
     * </p>
     *
     * @param source the builder to replace in, null returns zero
     * @param offset the start offset within the source, must be valid
     * @param length the length within the source to be processed, must be valid
     * @return true if altered
     * @throws IllegalArgumentException if variable is not found when its allowed to throw exception
     */
    public boolean replaceIn(final TextStringBuilder source, final int offset, final int length) {
        if (source == null) {
            return false;
        }
        return substitute(source, offset, length);
    }

    /**
     * Internal method that resolves the value of a variable.
     * <p>
     * Most users of this class do not need to call this method. This method is called automatically by the substitution
     * process.
     * </p>
     * <p>
     * Writers of subclasses can override this method if they need to alter how each substitution occurs. The method is
     * passed the variable's name and must return the corresponding value. This implementation uses the
     * {@link #getStringLookup()} with the variable's name as the key.
     * </p>
     *
     * @param variableName the name of the variable, not null
     * @param buf the buffer where the substitution is occurring, not null
     * @param startPos the start position of the variable including the prefix, valid
     * @param endPos the end position of the variable including the suffix, valid
     * @return The variable's value or <b>null</b> if the variable is unknown
     */
    protected String resolveVariable(final String variableName, final TextStringBuilder buf, final int startPos,
        final int endPos) {
        final StringLookup resolver = getStringLookup();
        if (resolver == null) {
            return null;
        }
        return resolver.lookup(variableName);
    }

    /**
     * Sets a flag whether substitution is done in variable values (recursive).
     *
     * @param disableSubstitutionInValues true if substitution in variable value are disabled
     * @return this, to enable chaining
     */
    public StringSubstitutor setDisableSubstitutionInValues(final boolean disableSubstitutionInValues) {
        this.disableSubstitutionInValues = disableSubstitutionInValues;
        return this;
    }

    /**
     * Sets a flag whether substitution is done in variable names. If set to <b>true</b>, the names of variables can
     * contain other variables which are processed first before the original variable is evaluated, e.g.
     * {@code ${jre-${java.version}}}. The default value is <b>false</b>.
     *
     * @param enableSubstitutionInVariables the new value of the flag
     * @return this, to enable chaining
     */
    public StringSubstitutor setEnableSubstitutionInVariables(final boolean enableSubstitutionInVariables) {
        this.enableSubstitutionInVariables = enableSubstitutionInVariables;
        return this;
    }

    /**
     * Sets a flag whether exception should be thrown if any variable is undefined.
     *
     * @param failOnUndefinedVariable true if exception should be thrown on undefined variable
     * @return this, to enable chaining
     */
    public StringSubstitutor setEnableUndefinedVariableException(final boolean failOnUndefinedVariable) {
        this.enableUndefinedVariableException = failOnUndefinedVariable;
        return this;
    }

    /**
     * Sets the escape character. If this character is placed before a variable reference in the source text, this
     * variable will be ignored.
     *
     * @param escapeCharacter the escape character (0 for disabling escaping)
     * @return this, to enable chaining
     */
    public StringSubstitutor setEscapeChar(final char escapeCharacter) {
        this.escapeChar = escapeCharacter;
        return this;
    }

    /**
     * Sets a flag controlling whether escapes are preserved during substitution. If set to <b>true</b>, the escape
     * character is retained during substitution (e.g. {@code $${this-is-escaped}} remains {@code $${this-is-escaped}}).
     * If set to <b>false</b>, the escape character is removed during substitution (e.g. {@code $${this-is-escaped}}
     * becomes {@code ${this-is-escaped}}). The default value is <b>false</b>
     *
     * @param preserveEscapes true if escapes are to be preserved
     * @return this, to enable chaining
     */
    public StringSubstitutor setPreserveEscapes(final boolean preserveEscapes) {
        this.preserveEscapes = preserveEscapes;
        return this;
    }

    /**
     * Sets the variable default value delimiter to use.
     * <p>
     * The variable default value delimiter is the character or characters that delimit the variable name and the
     * variable default value. This method allows a single character variable default value delimiter to be easily set.
     * </p>
     *
     * @param valueDelimiter the variable default value delimiter character to use
     * @return this, to enable chaining
     */
    public StringSubstitutor setValueDelimiter(final char valueDelimiter) {
        return setValueDelimiterMatcher(StringMatcherFactory.INSTANCE.charMatcher(valueDelimiter));
    }

    /**
     * Sets the variable default value delimiter to use.
     * <p>
     * The variable default value delimiter is the character or characters that delimit the variable name and the
     * variable default value. This method allows a string variable default value delimiter to be easily set.
     * </p>
     * <p>
     * If the {@code valueDelimiter} is null or empty string, then the variable default value resolution becomes
     * disabled.
     * </p>
     *
     * @param valueDelimiter the variable default value delimiter string to use, may be null or empty
     * @return this, to enable chaining
     */
    public StringSubstitutor setValueDelimiter(final String valueDelimiter) {
        if (valueDelimiter == null || valueDelimiter.isEmpty()) {
            setValueDelimiterMatcher(null);
            return this;
        }
        return setValueDelimiterMatcher(StringMatcherFactory.INSTANCE.stringMatcher(valueDelimiter));
    }

    /**
     * Sets the variable default value delimiter matcher to use.
     * <p>
     * The variable default value delimiter is the character or characters that delimit the variable name and the
     * variable default value. This delimiter is expressed in terms of a matcher allowing advanced variable default
     * value delimiter matches.
     * </p>
     * <p>
     * If the {@code valueDelimiterMatcher} is null, then the variable default value resolution becomes disabled.
     * </p>
     *
     * @param valueDelimiterMatcher variable default value delimiter matcher to use, may be null
     * @return this, to enable chaining
     */
    public StringSubstitutor setValueDelimiterMatcher(final StringMatcher valueDelimiterMatcher) {
        this.valueDelimiterMatcher = valueDelimiterMatcher;
        return this;
    }

    /**
     * Sets the variable prefix to use.
     * <p>
     * The variable prefix is the character or characters that identify the start of a variable. This method allows a
     * single character prefix to be easily set.
     * </p>
     *
     * @param prefix the prefix character to use
     * @return this, to enable chaining
     */
    public StringSubstitutor setVariablePrefix(final char prefix) {
        return setVariablePrefixMatcher(StringMatcherFactory.INSTANCE.charMatcher(prefix));
    }

    /**
     * Sets the variable prefix to use.
     * <p>
     * The variable prefix is the character or characters that identify the start of a variable. This method allows a
     * string prefix to be easily set.
     * </p>
     *
     * @param prefix the prefix for variables, not null
     * @return this, to enable chaining
     * @throws IllegalArgumentException if the prefix is null
     */
    public StringSubstitutor setVariablePrefix(final String prefix) {
        Validate.isTrue(prefix != null, "Variable prefix must not be null!");
        return setVariablePrefixMatcher(StringMatcherFactory.INSTANCE.stringMatcher(prefix));
    }

    /**
     * Sets the variable prefix matcher currently in use.
     * <p>
     * The variable prefix is the character or characters that identify the start of a variable. This prefix is
     * expressed in terms of a matcher allowing advanced prefix matches.
     * </p>
     *
     * @param prefixMatcher the prefix matcher to use, null ignored
     * @return this, to enable chaining
     * @throws IllegalArgumentException if the prefix matcher is null
     */
    public StringSubstitutor setVariablePrefixMatcher(final StringMatcher prefixMatcher) {
        Validate.isTrue(prefixMatcher != null, "Variable prefix matcher must not be null!");
        this.prefixMatcher = prefixMatcher;
        return this;
    }

    /**
     * Sets the VariableResolver that is used to lookup variables.
     *
     * @param variableResolver the VariableResolver
     * @return this, to enable chaining
     */
    public StringSubstitutor setVariableResolver(final StringLookup variableResolver) {
        this.variableResolver = variableResolver;
        return this;
    }

    /**
     * Sets the variable suffix to use.
     * <p>
     * The variable suffix is the character or characters that identify the end of a variable. This method allows a
     * single character suffix to be easily set.
     * </p>
     *
     * @param suffix the suffix character to use
     * @return this, to enable chaining
     */
    public StringSubstitutor setVariableSuffix(final char suffix) {
        return setVariableSuffixMatcher(StringMatcherFactory.INSTANCE.charMatcher(suffix));
    }

    /**
     * Sets the variable suffix to use.
     * <p>
     * The variable suffix is the character or characters that identify the end of a variable. This method allows a
     * string suffix to be easily set.
     * </p>
     *
     * @param suffix the suffix for variables, not null
     * @return this, to enable chaining
     * @throws IllegalArgumentException if the suffix is null
     */
    public StringSubstitutor setVariableSuffix(final String suffix) {
        Validate.isTrue(suffix != null, "Variable suffix must not be null!");
        return setVariableSuffixMatcher(StringMatcherFactory.INSTANCE.stringMatcher(suffix));
    }

    /**
     * Sets the variable suffix matcher currently in use.
     * <p>
     * The variable suffix is the character or characters that identify the end of a variable. This suffix is expressed
     * in terms of a matcher allowing advanced suffix matches.
     * </p>
     *
     * @param suffixMatcher the suffix matcher to use, null ignored
     * @return this, to enable chaining
     * @throws IllegalArgumentException if the suffix matcher is null
     */
    public StringSubstitutor setVariableSuffixMatcher(final StringMatcher suffixMatcher) {
        Validate.isTrue(suffixMatcher != null, "Variable suffix matcher must not be null!");
        this.suffixMatcher = suffixMatcher;
        return this;
    }

    /**
     * Internal method that substitutes the variables.
     * <p>
     * Most users of this class do not need to call this method. This method will be called automatically by another
     * (public) method.
     * </p>
     * <p>
     * Writers of subclasses can override this method if they need access to the substitution process at the start or
     * end.
     * </p>
     *
     * @param builder the string builder to substitute into, not null
     * @param offset the start offset within the builder, must be valid
     * @param length the length within the builder to be processed, must be valid
     * @return true if altered
     */
    protected boolean substitute(final TextStringBuilder builder, final int offset, final int length) {
        return substitute(builder, offset, length, null).altered;
    }

    /**
     * Recursive handler for multiple levels of interpolation. This is the main interpolation method, which resolves the
     * values of all variable references contained in the passed in text.
     *
     * @param builder the string builder to substitute into, not null
     * @param offset the start offset within the builder, must be valid
     * @param length the length within the builder to be processed, must be valid
     * @param priorVariables the stack keeping track of the replaced variables, may be null
     * @return The result.
     * @throws IllegalArgumentException if variable is not found and <pre>isEnableUndefinedVariableException()==true</pre>
     * @since 1.9
     */
    private Result substitute(final TextStringBuilder builder, final int offset, final int length,
        List<String> priorVariables) {
        Objects.requireNonNull(builder, "builder");
        final StringMatcher prefixMatcher = getVariablePrefixMatcher();
        final StringMatcher suffixMatcher = getVariableSuffixMatcher();
        final char escapeCh = getEscapeChar();
        final StringMatcher valueDelimMatcher = getValueDelimiterMatcher();
        final boolean substitutionInVariablesEnabled = isEnableSubstitutionInVariables();
        final boolean substitutionInValuesDisabled = isDisableSubstitutionInValues();
        final boolean undefinedVariableException = isEnableUndefinedVariableException();
        final boolean preserveEscapes = isPreserveEscapes();

        boolean altered = false;
        int lengthChange = 0;
        int bufEnd = offset + length;
        int pos = offset;
        int escPos = -1;
        outer: while (pos < bufEnd) {
            final int startMatchLen = prefixMatcher.isMatch(builder, pos, offset, bufEnd);
            if (startMatchLen == 0) {
                pos++;
            } else {
                // found variable start marker
                if (pos > offset && builder.charAt(pos - 1) == escapeCh) {
                    // escape detected
                    if (preserveEscapes) {
                        // keep escape
                        pos++;
                        continue;
                    }
                    // mark esc ch for deletion if we find a complete variable
                    escPos = pos - 1;
                }
                // find suffix
                int startPos = pos;
                pos += startMatchLen;
                int endMatchLen = 0;
                int nestedVarCount = 0;
                while (pos < bufEnd) {
                    if (substitutionInVariablesEnabled && prefixMatcher.isMatch(builder, pos, offset, bufEnd) != 0) {
                        // found a nested variable start
                        endMatchLen = prefixMatcher.isMatch(builder, pos, offset, bufEnd);
                        nestedVarCount++;
                        pos += endMatchLen;
                        continue;
                    }

                    endMatchLen = suffixMatcher.isMatch(builder, pos, offset, bufEnd);
                    if (endMatchLen == 0) {
                        pos++;
                    } else {
                        // found variable end marker
                        if (nestedVarCount == 0) {
                            if (escPos >= 0) {
                                // delete escape
                                builder.deleteCharAt(escPos);
                                escPos = -1;
                                lengthChange--;
                                altered = true;
                                bufEnd--;
                                pos = startPos + 1;
                                startPos--;
                                continue outer;
                            }
                            // get var name
                            String varNameExpr = builder.midString(startPos + startMatchLen,
                                pos - startPos - startMatchLen);
                            if (substitutionInVariablesEnabled) {
                                final TextStringBuilder bufName = new TextStringBuilder(varNameExpr);
                                substitute(bufName, 0, bufName.length());
                                varNameExpr = bufName.toString();
                            }
                            pos += endMatchLen;
                            final int endPos = pos;

                            String varName = varNameExpr;
                            String varDefaultValue = null;

                            if (valueDelimMatcher != null) {
                                final char[] varNameExprChars = varNameExpr.toCharArray();
                                int valueDelimiterMatchLen = 0;
                                for (int i = 0; i < varNameExprChars.length; i++) {
                                    // if there's any nested variable when nested variable substitution disabled,
                                    // then stop resolving name and default value.
                                    if (!substitutionInVariablesEnabled && prefixMatcher.isMatch(varNameExprChars, i, i,
                                        varNameExprChars.length) != 0) {
                                        break;
                                    }
                                    if (valueDelimMatcher.isMatch(varNameExprChars, i, 0,
                                        varNameExprChars.length) != 0) {
                                        valueDelimiterMatchLen = valueDelimMatcher.isMatch(varNameExprChars, i, 0,
                                            varNameExprChars.length);
                                        varName = varNameExpr.substring(0, i);
                                        varDefaultValue = varNameExpr.substring(i + valueDelimiterMatchLen);
                                        break;
                                    }
                                }
                            }

                            // on the first call initialize priorVariables
                            if (priorVariables == null) {
                                priorVariables = new ArrayList<>();
                                priorVariables.add(builder.midString(offset, length));
                            }

                            // handle cyclic substitution
                            checkCyclicSubstitution(varName, priorVariables);
                            priorVariables.add(varName);

                            // resolve the variable
                            String varValue = resolveVariable(varName, builder, startPos, endPos);
                            if (varValue == null) {
                                varValue = varDefaultValue;
                            }
                            if (varValue != null) {
                                final int varLen = varValue.length();
                                builder.replace(startPos, endPos, varValue);
                                altered = true;
                                int change = 0;
                                if (!substitutionInValuesDisabled) { // recursive replace
                                    change = substitute(builder, startPos, varLen, priorVariables).lengthChange;
                                }
                                change = change + varLen - (endPos - startPos);
                                pos += change;
                                bufEnd += change;
                                lengthChange += change;
                            } else if (undefinedVariableException) {
                                throw new IllegalArgumentException(
                                    String.format("Cannot resolve variable '%s' (enableSubstitutionInVariables=%s).",
                                        varName, substitutionInVariablesEnabled));
                            }

                            // remove variable from the cyclic stack
                            priorVariables.remove(priorVariables.size() - 1);
                            break;
                        }
                        nestedVarCount--;
                        pos += endMatchLen;
                    }
                }
            }
        }
        return new Result(altered, lengthChange);
    }
}
