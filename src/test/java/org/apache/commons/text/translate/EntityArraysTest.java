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

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link org.apache.commons.text.translate.EntityArrays}.
 */
public class EntityArraysTest  {

    @Test
    public void testConstructorExists() {
        new org.apache.commons.text.translate.EntityArrays();
    }

    // LANG-659 - check arrays for duplicate entries
    @Test
    public void testHTML40_EXTENDED_ESCAPE(){
        final Set<String> col0 = new HashSet<>();
        final Set<String> col1 = new HashSet<>();
        final String [][] sa = org.apache.commons.text.translate.EntityArrays.HTML40_EXTENDED_ESCAPE();
        for(int i =0; i <sa.length; i++){
            assertTrue("Already added entry 0: "+i+" "+sa[i][0],col0.add(sa[i][0]));
            assertTrue("Already added entry 1: "+i+" "+sa[i][1],col1.add(sa[i][1]));
        }
    }

   // LANG-658 - check arrays for duplicate entries
    @Test
    public void testISO8859_1_ESCAPE(){
        final Set<String> col0 = new HashSet<>();
        final Set<String> col1 = new HashSet<>();
        final String [][] sa = EntityArrays.ISO8859_1_ESCAPE();
        boolean success = true;
        for(int i =0; i <sa.length; i++){
            final boolean add0 = col0.add(sa[i][0]);
            final boolean add1 = col1.add(sa[i][1]);
            if (!add0) { 
                success = false;
                System.out.println("Already added entry 0: "+i+" "+sa[i][0]+" "+sa[i][1]);
            }
            if (!add1) {
                success = false;
                System.out.println("Already added entry 1: "+i+" "+sa[i][0]+" "+sa[i][1]);
            }
        }
        assertTrue("One or more errors detected",success);
    }

    @Test
    public void testISO8859Arrays() {
        testEscapeVsUnescapeArrays(EntityArrays.ISO8859_1_ESCAPE(), EntityArrays.ISO8859_1_UNESCAPE());
    }

    @Test
    public void testHtml40ExtendedArrays() {
        testEscapeVsUnescapeArrays(EntityArrays.HTML40_EXTENDED_ESCAPE(), EntityArrays.HTML40_EXTENDED_UNESCAPE());
    }

    @Test
    public void testAposArrays() {
        testEscapeVsUnescapeArrays(EntityArrays.APOS_ESCAPE(), EntityArrays.APOS_UNESCAPE());
    }

    @Test
    public void testBasicArrays() {
        testEscapeVsUnescapeArrays(EntityArrays.BASIC_ESCAPE(), EntityArrays.BASIC_UNESCAPE());
    }

    @Test
    public void testJavaCntrlCharsArrays() {
        testEscapeVsUnescapeArrays(EntityArrays.JAVA_CTRL_CHARS_ESCAPE(), EntityArrays.JAVA_CTRL_CHARS_ESCAPE());
    }

    private void testEscapeVsUnescapeArrays(final String[][] escapeArray, final String[][] unescapeArray) {
        for (final String[] escapeElement : escapeArray) {
            for (final String[] unescapeElement : unescapeArray) {
                if (escapeElement[0] == unescapeElement[1]) {
                    assertEquals(escapeElement[1], unescapeElement[0]);
                }
            }
        }
    }
    
}
