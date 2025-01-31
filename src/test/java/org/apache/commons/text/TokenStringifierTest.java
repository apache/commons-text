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

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class TokenStringifierTest {

    @Test
    public void testTokenStringifier() {
        TokenStringifier stringifier = new TokenStringifier(TokenFormatterFactory.constantFormatter(',', true), TokenFormatterFactory.noOpFormatter());
        List<String> tokens = Arrays.asList(new String[]{"my", "csv", "tokens"});
        stringifier.reset(tokens);
        String csv = stringifier.getString();
        assertEquals("my,csv,tokens", csv);
        //double check that csv tokenizer can read the csv string
        StringTokenizer csvTokenizer = StringTokenizer.getCSVInstance(csv);
        csvTokenizer.reset(csv);
        List<String> tokenizerTokens = csvTokenizer.getTokenList();
        assertEquals(tokens, tokenizerTokens);
    }
}
