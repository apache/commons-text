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

package org.apache.commons.text.translate;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link EntityArrays}.
 */
public class EntityArraysTest  {

    @Test
    public void testAposMap() {
        testEscapeVsUnescapeMaps(EntityArrays.APOS_ESCAPE, EntityArrays.APOS_UNESCAPE);
    }

    @Test
    public void testBasicMap() {
        testEscapeVsUnescapeMaps(EntityArrays.BASIC_ESCAPE, EntityArrays.BASIC_UNESCAPE);
    }

    @Test
    public void testConstructorExists() {
        new EntityArrays();
    }

    private void testEscapeVsUnescapeMaps(final Map<CharSequence, CharSequence> escapeMap,
                                          final Map<CharSequence, CharSequence> unescapeMap) {
        for (final CharSequence escapeKey : escapeMap.keySet()) {
            for (final CharSequence unescapeKey : unescapeMap.keySet()) {
                if (escapeKey == unescapeMap.get(unescapeKey)) {
                    assertThat(unescapeKey).isEqualTo(escapeMap.get(escapeKey));
                }
            }
        }
    }

    // LANG-659, LANG-658 - avoid duplicate entries
    @Test
    public void testForDuplicatedDeclaredMapKeys() throws Exception {
        final String packageDirectory = EntityArraysTest.class.getPackage().getName().replace(".", "/");
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/" + packageDirectory
                + "/EntityArrays.java"))) {
            String line;
            int mapDeclarationCounter = 0;
            while ((line = br.readLine()) != null) {
                //Start with map declaration and count put lines
                if (line.contains("new HashMap<>();")) {
                    mapDeclarationCounter = 0;
                } else if (line.contains(".put(")) {
                    mapDeclarationCounter++;
                } else if (line.contains("Collections.unmodifiableMap(initialMap);")) {
                    final String mapVariableName = line.split("=")[0].trim();
                    @SuppressWarnings("unchecked") // This is test code
                    final
                    Map<String, String> mapValue = (Map<String, String>)
                        EntityArrays.class.getDeclaredField(mapVariableName).get(EntityArrays.class);
                    // Validate that we are not inserting into the same key twice in the map declaration. If this,
                    // indeed was the case the keySet().size() would be smaller than the number of put() statements
                    assertThat(mapValue.size()).isEqualTo(mapDeclarationCounter);
                }
            }
        }
    }

    @Test
    public void testForDuplicateDeclaredMapValuesAposMap() {
        assertThat(EntityArrays.APOS_ESCAPE.keySet()).hasSameSizeAs(
                EntityArrays.APOS_UNESCAPE.keySet());
    }

    @Test
    public void testForDuplicateDeclaredMapValuesBasicMap() {
        assertThat(EntityArrays.BASIC_ESCAPE.keySet()).hasSameSizeAs(
                EntityArrays.BASIC_UNESCAPE.keySet());
    }

    @Test
    public void testForDuplicateDeclaredMapValuesHtml40ExtendedMap() {
        assertThat(EntityArrays.HTML40_EXTENDED_ESCAPE.keySet()).hasSameSizeAs(
                EntityArrays.HTML40_EXTENDED_UNESCAPE.keySet());
    }

    @Test
    public void testForDuplicateDeclaredMapValuesISO8859Map() {
        assertThat(EntityArrays.ISO8859_1_ESCAPE.keySet()).hasSameSizeAs(
                EntityArrays.ISO8859_1_UNESCAPE.keySet());
    }

    @Test
    public void testForDuplicateDeclaredMapValuesJavaCtrlCharsMap() {
        assertThat(EntityArrays.JAVA_CTRL_CHARS_ESCAPE.keySet()).hasSameSizeAs(
                EntityArrays.JAVA_CTRL_CHARS_UNESCAPE.keySet());
    }

    @Test
    public void testHtml40ExtendedMap() {
        testEscapeVsUnescapeMaps(EntityArrays.HTML40_EXTENDED_ESCAPE, EntityArrays.HTML40_EXTENDED_UNESCAPE);
    }

    @Test
    public void testISO8859Map() {
        testEscapeVsUnescapeMaps(EntityArrays.ISO8859_1_ESCAPE, EntityArrays.ISO8859_1_UNESCAPE);
    }

    @Test
    public void testJavaCtrlCharsMap() {
        testEscapeVsUnescapeMaps(EntityArrays.JAVA_CTRL_CHARS_ESCAPE, EntityArrays.JAVA_CTRL_CHARS_UNESCAPE);
    }

}
