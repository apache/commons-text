/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.text.translate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link EntityArrays}.
 */
class EntityArraysTest {

    @Test
    void testAposMap() {
        testEscapeVsUnescapeMaps(EntityArrays.APOS_ESCAPE, EntityArrays.APOS_UNESCAPE);
    }

    @Test
    void testBasicMap() {
        testEscapeVsUnescapeMaps(EntityArrays.BASIC_ESCAPE, EntityArrays.BASIC_UNESCAPE);
    }

    @Test
    void testConstructorExists() {
        new EntityArrays();
    }

    private void testEscapeVsUnescapeMaps(final Map<CharSequence, CharSequence> escapeMap, final Map<CharSequence, CharSequence> unescapeMap) {
        for (final CharSequence escapeKey : escapeMap.keySet()) {
            for (final CharSequence unescapeKey : unescapeMap.keySet()) {
                if (escapeKey == unescapeMap.get(unescapeKey)) {
                    assertEquals(escapeMap.get(escapeKey), unescapeKey);
                }
            }
        }
    }

    // LANG-659, LANG-658 - avoid duplicate entries
    @Test
    void testForDuplicatedDeclaredMapKeys() throws Exception {
        final String packageDirectory = EntityArraysTest.class.getPackage().getName().replace(".", "/");
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/" + packageDirectory + "/EntityArrays.java"))) {
            String line;
            int mapDeclarationCounter = 0;
            while ((line = br.readLine()) != null) {
                // Start with map declaration and count put lines
                if (line.contains("new HashMap<>();")) {
                    mapDeclarationCounter = 0;
                } else if (line.contains(".put(")) {
                    mapDeclarationCounter++;
                } else if (line.contains("Collections.unmodifiableMap(initialMap);")) {
                    final String mapVariableName = line.split("=")[0].trim();
                    @SuppressWarnings("unchecked") // This is test code
                    final Map<String, String> mapValue = (Map<String, String>) EntityArrays.class.getDeclaredField(mapVariableName).get(EntityArrays.class);
                    // Validate that we are not inserting into the same key twice in the map declaration. If this,
                    // indeed was the case the keySet().size() would be smaller than the number of put() statements
                    assertEquals(mapDeclarationCounter, mapValue.size());
                }
            }
        }
    }

    @Test
    void testForDuplicateDeclaredMapValuesAposMap() {
        assertEquals(EntityArrays.APOS_UNESCAPE.size(), EntityArrays.APOS_ESCAPE.size());
    }

    @Test
    void testForDuplicateDeclaredMapValuesBasicMap() {
        assertEquals(EntityArrays.BASIC_ESCAPE.size(), EntityArrays.BASIC_UNESCAPE.size());
    }

    @Test
    void testForDuplicateDeclaredMapValuesHtml40ExtendedMap() {
        assertEquals(EntityArrays.HTML40_EXTENDED_ESCAPE.size(), EntityArrays.HTML40_EXTENDED_UNESCAPE.size());
    }

    @Test
    void testForDuplicateDeclaredMapValuesISO8859Map() {
        assertEquals(EntityArrays.ISO8859_1_ESCAPE.size(), EntityArrays.ISO8859_1_UNESCAPE.size());
    }

    @Test
    void testForDuplicateDeclaredMapValuesJavaCtrlCharsMap() {
        assertEquals(EntityArrays.JAVA_CTRL_CHARS_ESCAPE.size(), EntityArrays.JAVA_CTRL_CHARS_UNESCAPE.size());
    }

    @Test
    void testHtml40ExtendedMap() {
        testEscapeVsUnescapeMaps(EntityArrays.HTML40_EXTENDED_ESCAPE, EntityArrays.HTML40_EXTENDED_UNESCAPE);
    }

    @Test
    void testISO8859Map() {
        testEscapeVsUnescapeMaps(EntityArrays.ISO8859_1_ESCAPE, EntityArrays.ISO8859_1_UNESCAPE);
    }

    @Test
    void testJavaCtrlCharsMap() {
        testEscapeVsUnescapeMaps(EntityArrays.JAVA_CTRL_CHARS_ESCAPE, EntityArrays.JAVA_CTRL_CHARS_UNESCAPE);
    }

}
