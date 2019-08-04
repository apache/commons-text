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

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.text.StringSubstitutor;

/**
 * Looks up keys from an XML document.
 * <p>
 * Looks up the value for a given key in the format "Document:Key".
 * </p>
 * <p>
 * For example: {@code "javascript:3 + 4"}.
 * </p>
 * <p>
 * Using a {@link StringSubstitutor}:
 * </p>
 * <pre>
 * StringSubstitutor.createInterpolator().replace("${script:javascript:3 + 4}"));
 * </pre>
 * @since 1.5
 */
final class ScriptStringLookup extends AbstractStringLookup {

    /**
     * Defines the singleton for this class.
     */
    static final ScriptStringLookup INSTANCE = new ScriptStringLookup();

    /**
     * No need to build instances for now.
     */
    private ScriptStringLookup() {
        // empty
    }

    /**
     * Looks up the value for the key in the format "DocumentPath:XPath".
     * <p>
     * For example: "com/domain/document.xml:/path/to/node".
     * </p>
     *
     * @param key
     *            the key to be looked up, may be null
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
            throw IllegalArgumentExceptions.format("Bad script key format [%s]; expected format is DocumentPath:Key.",
                    key);
        }
        final String engineName = keys[0];
        final String script = keys[1];
        try {
            final ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName(engineName);
            if (scriptEngine == null) {
                throw new IllegalArgumentException("No script engine named " + engineName);
            }
            return Objects.toString(scriptEngine.eval(script), null);
        } catch (final Exception e) {
            throw IllegalArgumentExceptions.format(e, "Error in script engine [%s] evaluating script [%s].", engineName,
                    script);
        }
    }

}
