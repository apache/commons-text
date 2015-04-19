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
package org.apache.commons.text.names;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@code Name} and {@code HumanNameParser}. Utilizes the same
 * input file as the PHP library 0.2 version.
 */
public class NameStringTest {

    private NameString nameString;

    @Before
    public void setUp() {
        nameString = new NameString("Björn O'Malley");
    }

    @Test
    public void testSetStrRemovesWhitespaceAtEnds() {
        nameString.setStr("    Björn O'Malley \r\n");
        assertEquals(
            "Björn O'Malley",
            nameString.getStr()
        );
    }

    @Test
    public void testSetStrRemovesRedudentantWhitespace(){
        nameString.setStr(" Björn    O'Malley");
        assertEquals(
            "Björn O'Malley",
            nameString.getStr()
        );
    }

    @Test
    public void testChopWithRegexReturnsChoppedSubstring(){
        nameString.setStr("Björn O'Malley");
        assertEquals(
            "Björn",
            nameString.chopWithRegex("(^([^ ]+))(.+)", 1)
        );
    }

    @Test
    public void testChopWithRegexChopsStartOffNameStr(){
        nameString.setStr("Björn O'Malley");
        nameString.chopWithRegex("(^[^ ]+)", 0);
        assertEquals(
                "O'Malley",
            nameString.getStr()
        );
    }

    @Test
    public void testChopWithRegexChopsEndOffNameStr(){
        nameString.setStr("Björn O'Malley");
        nameString.chopWithRegex("( (.+)$)", 1);
        assertEquals(
            "Björn",
            nameString.getStr()
        );
    }

    @Test
    public void testChopWithRegexChopsMiddleFromNameStr(){
        nameString.setStr("Björn 'Bill' O'Malley");
        nameString.chopWithRegex("( '[^']+' )", 0);
        assertEquals(
            "Björn O'Malley",
            nameString.getStr()
        );
    }

    @Test
    public void testFlip() {
        nameString.setStr("O'Malley, Björn");
        nameString.flip(",");
        assertEquals(
            "Björn O'Malley",
            nameString.getStr()
        );
    }

}
