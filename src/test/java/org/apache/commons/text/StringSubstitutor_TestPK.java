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
package org.apache.commons.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class StringSubstitutor_TestPK {

    //test for edge case Null value

    @Test
    void testReplace_NullInput() {
        StringSubstitutor substitutor = new StringSubstitutor();
        assertNull(substitutor.replace((String) null), "Expected null when input is null");
    }
    
    // Edge case testing by testing empty string
    @Test
    void testReplace_EmptyString() {
        StringSubstitutor substitutor = new StringSubstitutor();
        assertEquals("", substitutor.replace(""), "Expected empty string to remain empty");
    }
    // No placeholder test
     @Test
    void testReplace_NoPlaceholders() {
        StringSubstitutor substitutor = new StringSubstitutor();
        String input = "Hello, World!";
        assertEquals(input, substitutor.replace(input), "Expected the same string when no placeholders exist");
    }
    //test valid substitution
    @Test
    void testReplace_ValidSubstitution() {
        Map<String, String> values = new HashMap<>();
        values.put("name", "Jacob");
        StringSubstitutor substitutor = new StringSubstitutor(values);

        String input = "Hello, ${name}!";
        String expected = "Hello, Jacob!";
        assertEquals(expected, substitutor.replace(input), "Expected placeholder to be replaced");
    }
    @Test
    void testReplace_UnresolvedPlaceholder() {
        Map<String, String> values = new HashMap<>();
        values.put("name", "Alice");
        StringSubstitutor substitutor = new StringSubstitutor(values);

        String input = "Hello, ${unknown}!";
        String expected = "Hello, ${unknown}!";
        assertEquals(expected, substitutor.replace(input), "Expected unresolved placeholder to remain unchanged");
    }
}
