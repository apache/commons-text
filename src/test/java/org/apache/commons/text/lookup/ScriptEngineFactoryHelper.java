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

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

/** Helper to see what ScriptEngineFactory are the classpath. */
public class ScriptEngineFactoryHelper {

    public static void main(final String[] args) {
        final String indent = "  ";
        for (final ScriptEngineFactory factory : new ScriptEngineManager().getEngineFactories()) {
            System.out.println(factory);
            System.out.println(indent + factory.getEngineName());
            System.out.println(indent + factory.getEngineVersion());
            System.out.println(indent + factory.getLanguageName());
            System.out.println(indent + factory.getLanguageVersion());
            System.out.println(indent + factory.getClass());
            System.out.println(indent + factory.getExtensions());
            System.out.println(indent + factory.getMimeTypes());
            System.out.println(indent + factory.getNames());
        }
    }

}
