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
public class NameTest {

    protected Name object;

    @Before
    public void setUp() {
        object = new Name("Björn O'Malley");
    }

    @Test
    public void testSetStrRemovesWhitespaceAtEnds() {
        object.setStr("    Björn O'Malley \r\n");
        assertEquals(
            "Björn O'Malley",
            object.getStr()
        );
    }

    @Test
    public void testSetStrRemovesRedudentantWhitespace(){
        object.setStr(" Björn    O'Malley");
        assertEquals(
            "Björn O'Malley",
            object.getStr()
        );
    }

    @Test
    public void testChopWithRegexReturnsChoppedSubstring(){
        object.setStr("Björn O'Malley");
        assertEquals(
            "Björn",
            object.chopWithRegex("(^([^ ]+))(.+)", 1)
        );
    }

    @Test
    public void testChopWithRegexChopsStartOffNameStr(){
        object.setStr("Björn O'Malley");
        object.chopWithRegex("(^[^ ]+)", 0);
        assertEquals(
                "O'Malley",
            object.getStr()
        );
    }

    @Test
    public void testChopWithRegexChopsEndOffNameStr(){
        object.setStr("Björn O'Malley");
        object.chopWithRegex("( (.+)$)", 1);
        assertEquals(
            "Björn",
            object.getStr()
        );
    }

    @Test
    public void testChopWithRegexChopsMiddleFromNameStr(){
        object.setStr("Björn 'Bill' O'Malley");
        object.chopWithRegex("( '[^']+' )", 0);
        assertEquals(
            "Björn O'Malley",
            object.getStr()
        );
    }

    @Test
    public void testFlip() {
        object.setStr("O'Malley, Björn");
        object.flip(",");
        assertEquals(
            "Björn O'Malley",
            object.getStr()
        );
    }

}
