/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */

package org.apache.commons.text.lookup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link PropertiesStringLookup}.
 */
public class PropertiesStringLookupTest {

    private static final Path CURRENT_PATH = Paths.get(StringUtils.EMPTY); // NOT "."!
    private static final String DOC_RELATIVE = "src/test/resources/org/apache/commons/text/document.properties";
    private static final String DOC_ROOT = "/foo.txt";
    private static final String KEY = "mykey";
    private static final String KEY_RELATIVE = PropertiesStringLookup.toPropertyKey(DOC_RELATIVE, KEY);
    private static final String KEY_ROOT = PropertiesStringLookup.toPropertyKey(DOC_ROOT, KEY);
    private static final Path[] NULL_PATH_ARRAY = null;

    public static void testFence(final StringSubstitutor stringSubstitutor) {
        assertEquals("Hello World!", stringSubstitutor.replace("${properties:" + KEY_RELATIVE + "}"));
        assertThrows(IllegalArgumentException.class, () -> stringSubstitutor.replace("${file:UTF-8:/foo.txt}"));
        assertThrows(IllegalArgumentException.class, () -> stringSubstitutor.replace("${file:UTF-8:../foo.txt}"));
    }

    @Test
    public void testFenceOne() {
        assertThrows(IllegalArgumentException.class, () -> new PropertiesStringLookup(CURRENT_PATH).apply(KEY_ROOT));
        assertThrows(IllegalArgumentException.class, () -> new PropertiesStringLookup(Paths.get("not a dir at all"), CURRENT_PATH).apply(KEY_ROOT));
    }

    @Test
    public void testInterpolator() {
        final StringSubstitutor stringSubstitutor = StringSubstitutor.createInterpolator();
        assertEquals("Hello World!", stringSubstitutor.replace("${properties:" + KEY_RELATIVE + "}"));
    }

    @Test
    public void testInterpolatorNestedColon() {
        final StringSubstitutor stringSubstitutor = StringSubstitutor.createInterpolator();
        // Need to handle "C:" in the sys prop user.dir.
        final String replaced = stringSubstitutor.replace("$${properties:${sys:user.dir}/" + KEY_RELATIVE + "}");
        assertEquals("${properties:" + System.getProperty("user.dir") + "/src/test/resources/org/apache/commons/text/document.properties::mykey}", replaced);
        assertEquals("Hello World!", stringSubstitutor.replace(replaced));
    }

    @Test
    public void testInterpolatorReplace() {
        final StringSubstitutor stringSubstitutor = StringSubstitutor.createInterpolator();
        assertEquals("Hello World!", stringSubstitutor.replace("${properties:" + KEY_RELATIVE + "}"));
        final InterpolatorStringLookup stringLookup = (InterpolatorStringLookup) stringSubstitutor.getStringLookup();
        stringLookup.getStringLookupMap().replace(StringLookupFactory.KEY_FILE, StringLookupFactory.INSTANCE.fileStringLookup(CURRENT_PATH));
        testFence(stringSubstitutor);
    }

    @Test
    public void testInterpolatorReplaceProperties() {
        final StringSubstitutor stringSubstitutor = StringSubstitutor.createInterpolator();
        assertEquals("Hello World!", stringSubstitutor.replace("${properties:" + KEY_RELATIVE + "}"));
        final InterpolatorStringLookup stringLookup = (InterpolatorStringLookup) stringSubstitutor.getStringLookup();
        stringLookup.getStringLookupMap().replace(StringLookupFactory.KEY_PROPERTIES, StringLookupFactory.INSTANCE.propertiesStringLookup(CURRENT_PATH));
        assertEquals("Hello World!", stringSubstitutor.replace("${properties:" + KEY_RELATIVE + "}"));
        assertThrows(IllegalArgumentException.class, () -> stringSubstitutor.replace("${properties:UTF-8:/foo.txt}"));
    }

    @Test
    public void testInterpolatorWithParameterizedKey() {
        final Map<String, String> map = new HashMap<>();
        map.put("KeyIsHere", KEY);
        final StringSubstitutor stringSubstitutor = new StringSubstitutor(StringLookupFactory.INSTANCE.interpolatorStringLookup(map));
        final String replaced = stringSubstitutor.replace("$${properties:" + PropertiesStringLookup.toPropertyKey(DOC_RELATIVE, "${KeyIsHere}}"));
        assertEquals("${properties:" + PropertiesStringLookup.toPropertyKey(DOC_RELATIVE, "mykey}"), replaced);
        assertEquals("Hello World!", stringSubstitutor.replace(replaced));
    }

    @Test
    public void testInterpolatorWithParameterizedKey2() {
        final Map<String, String> map = new HashMap<>();
        map.put("KeyIsHere", KEY);
        final StringSubstitutor stringSubstitutor = new StringSubstitutor(StringLookupFactory.INSTANCE.interpolatorStringLookup(map));
        final String replaced = stringSubstitutor
                .replace("$${properties:${sys:user.dir}/" + PropertiesStringLookup.toPropertyKey(DOC_RELATIVE, "${KeyIsHere}}"));
        assertEquals("${properties:" + System.getProperty("user.dir") + "/" + PropertiesStringLookup.toPropertyKey(DOC_RELATIVE, "mykey}"), replaced);
        assertEquals("Hello World!", stringSubstitutor.replace(replaced));
    }

    @Test
    public void testMissingFile() {
        assertThrows(IllegalArgumentException.class, () -> PropertiesStringLookup.INSTANCE.apply("MissingFile"));
    }

    @Test
    public void testMissingFileWithKey() {
        assertThrows(IllegalArgumentException.class,
                () -> PropertiesStringLookup.INSTANCE.apply(PropertiesStringLookup.toPropertyKey("MissingFile", "AnyKey")));
    }

    @Test
    public void testMissingKey() {
        assertThrows(IllegalArgumentException.class, () -> PropertiesStringLookup.INSTANCE.apply(DOC_RELATIVE));
        assertThrows(IllegalArgumentException.class, () -> new PropertiesStringLookup().apply(DOC_RELATIVE));
        assertThrows(IllegalArgumentException.class, () -> new PropertiesStringLookup(NULL_PATH_ARRAY).apply(DOC_RELATIVE));
        assertThrows(IllegalArgumentException.class, () -> new PropertiesStringLookup(CURRENT_PATH).apply(DOC_RELATIVE));
    }

    @Test
    public void testNull() {
        Assertions.assertNull(PropertiesStringLookup.INSTANCE.apply(null));
        Assertions.assertNull(new PropertiesStringLookup().apply(null));
        Assertions.assertNull(new PropertiesStringLookup(NULL_PATH_ARRAY).apply(null));
        Assertions.assertNull(new PropertiesStringLookup(CURRENT_PATH).apply(null));
    }

    @Test
    public void testOne() {
        assertEquals("Hello World!", PropertiesStringLookup.INSTANCE.apply(KEY_RELATIVE));
        assertEquals("Hello World!", new PropertiesStringLookup().apply(KEY_RELATIVE));
        assertEquals("Hello World!", new PropertiesStringLookup(NULL_PATH_ARRAY).apply(KEY_RELATIVE));
        assertEquals("Hello World!", new PropertiesStringLookup(CURRENT_PATH).apply(KEY_RELATIVE));
        assertThrows(IllegalArgumentException.class, () -> new PropertiesStringLookup(CURRENT_PATH).apply(KEY_ROOT));
    }

    @Test
    public void testToString() {
        // does not blow up and gives some kind of string.
        Assertions.assertFalse(PropertiesStringLookup.INSTANCE.toString().isEmpty());
        Assertions.assertFalse(new PropertiesStringLookup().toString().isEmpty());
        Assertions.assertFalse(new PropertiesStringLookup(NULL_PATH_ARRAY).toString().isEmpty());
        Assertions.assertFalse(new PropertiesStringLookup(CURRENT_PATH).toString().isEmpty());
    }

}
