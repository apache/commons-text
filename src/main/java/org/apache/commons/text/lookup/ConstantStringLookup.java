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

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.text.StringSubstitutor;

/**
 * <p>
 * Looks up the value of a fully-qualified static final value.
 * </p>
 * <p>
 * Sometimes it is necessary in a configuration file to refer to a constant defined in a class. This can be done with
 * this lookup implementation. Variable names must be in the format {@code apackage.AClass.AFIELD}. The
 * {@code lookup(String)} method will split the passed in string at the last dot, separating the fully qualified class
 * name and the name of the constant (i.e. <strong>static final</strong>) member field. Then the class is loaded and the field's
 * value is obtained using reflection.
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
 * <p>
 * This class was adapted from Apache Commons Configuration.
 * </p>
 *
 * @since 1.5
 */
class ConstantStringLookup extends AbstractStringLookup {

    /** An internally used cache for already retrieved values. */
    private static final ConcurrentHashMap<String, String> CONSTANT_CACHE = new ConcurrentHashMap<>();

    /** Constant for the field separator. */
    private static final char FIELD_SEPARATOR = '.';

    /**
     * Defines the singleton for this class.
     */
    static final ConstantStringLookup INSTANCE = new ConstantStringLookup();

    /**
     * Clears the shared cache with the so far resolved constants.
     */
    static void clear() {
        CONSTANT_CACHE.clear();
    }

    /**
     * Loads the class with the specified name. If an application has special needs regarding the class loaders to be
     * used, it can hook in here. This implementation delegates to the {@code getClass()} method of Commons Lang's
     * <code><a href="https://commons.apache.org/lang/api-release/org/apache/commons/lang/ClassUtils.html">
     * ClassUtils</a></code>.
     *
     * @param className the name of the class to be loaded
     * @return The corresponding class object
     * @throws ClassNotFoundException if the class cannot be loaded
     */
    protected Class<?> fetchClass(final String className) throws ClassNotFoundException {
        return ClassUtils.getClass(className);
    }

    /**
     * Tries to resolve the specified variable. The passed in variable name is interpreted as the name of a <b>static
     * final</b> member field of a class. If the value has already been obtained, it can be retrieved from an internal
     * cache. Otherwise this method will invoke the {@code resolveField()} method and pass in the name of the class and
     * the field.
     *
     * @param key the name of the variable to be resolved
     * @return The value of this variable or <strong>null</strong> if it cannot be resolved
     */
    @Override
    public synchronized String lookup(final String key) {
        if (key == null) {
            return null;
        }
        String result;
        result = CONSTANT_CACHE.get(key);
        if (result != null) {
            return result;
        }
        final int fieldPos = key.lastIndexOf(FIELD_SEPARATOR);
        if (fieldPos < 0) {
            return null;
        }
        try {
            final Object value = resolveField(key.substring(0, fieldPos), key.substring(fieldPos + 1));
            if (value != null) {
                final String string = Objects.toString(value, null);
                CONSTANT_CACHE.put(key, string);
                result = string;
            }
        } catch (final Exception ex) {
            // TODO it would be nice to log
            return null;
        }
        return result;
    }

    /**
     * Determines the value of the specified constant member field of a class. This implementation will call
     * {@code fetchClass()} to obtain the {@link Class} object for the target class. Then it will use
     * reflection to obtain the field's value. For this to work the field must be accessible.
     *
     * @param className the name of the class
     * @param fieldName the name of the member field of that class to read
     * @return The field's value
     * @throws ReflectiveOperationException if an error occurs
     */
    protected Object resolveField(final String className, final String fieldName) throws ReflectiveOperationException {
        final Class<?> clazz = fetchClass(className);
        if (clazz == null) {
            return null;
        }
        return clazz.getField(fieldName).get(null);
    }
}
