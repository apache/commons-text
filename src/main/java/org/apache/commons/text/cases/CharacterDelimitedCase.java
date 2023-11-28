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
package org.apache.commons.text.cases;

import java.util.List;

import org.apache.commons.text.StringTokenizer;
import org.apache.commons.text.TokenFormatterFactory;
import org.apache.commons.text.TokenStringifier;

public class CharacterDelimitedCase implements Case {

    /**
     * The tokenizer.
     */
    private StringTokenizer tokenizer;

    /**
     * The stringifier.
     */
    private TokenStringifier stringifier;

    /**
     * Constructs a new CharacterDelimitedCase instance.
     */
    protected CharacterDelimitedCase(char delimiter) {
        tokenizer = new StringTokenizer((String) null, delimiter);
        tokenizer.setIgnoreEmptyTokens(false);
        stringifier = new TokenStringifier(TokenFormatterFactory.constantFormatter(delimiter, true), TokenFormatterFactory.noOpFormatter());
    }

    @Override
    public String format(Iterable<String> tokens) {
        stringifier.reset(tokens);
        return stringifier.getString();
    }

    @Override
    public List<String> parse(String string) {
        tokenizer.reset(string);
        return tokenizer.getTokenList();
    }

}
