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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PropertiesStringLookupTest {

    private static final String DOC_PATH = "src/test/resources/document.properties";
    private static final String KEY = "mykey";
    private static final String KEY_PATH = DOC_PATH + ":" + KEY;

    @Test
    public void testOne() {
        Assertions.assertEquals("Hello World!", PropertiesStringLookup.INSTANCE.lookup(KEY_PATH));
    }

    @Test
    public void testInterpolator() {
        final StringSubstitutor stringSubstitutor = new StringSubstitutor(
                StringLookupFactory.INSTANCE.interpolatorStringLookup());
        Assertions.assertEquals("Hello World!", stringSubstitutor.replace("${properties:" + KEY_PATH + "}"));
    }

    @Test
    public void testInterpolatorWithParameterizedKey() {
        final Map<String, String> map = new HashMap<>();
        map.put("KeyIsHere", KEY);
        final StringSubstitutor stringSubstitutor = new StringSubstitutor(
                StringLookupFactory.INSTANCE.interpolatorStringLookup(map));
        final String replaced = stringSubstitutor.replace("$${properties:" + DOC_PATH + ":${KeyIsHere}}");
        Assertions.assertEquals("${properties:" + DOC_PATH + ":mykey}", replaced);
        Assertions.assertEquals("Hello World!", stringSubstitutor.replace(replaced));
    }

}
