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
package org.apache.commons.text.similarity.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple word tokenizer that utilizes regex to find words. It applies a regex
 * {@code}(\w)+{@code} over the input text to extract words from a given character
 * sequence.
 *
 * @since 0.1
 */
public class RegexTokenizer implements Tokenizer<CharSequence> {

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if the input text is blank
     */
    @Override
    public CharSequence[] tokenize(CharSequence text) {
        if (text == null || text.toString().trim().equals("")) {
            throw new IllegalArgumentException("Invalid text");
        }
        Pattern pattern = Pattern.compile("(\\w)+");
        Matcher matcher = pattern.matcher(text.toString());
        List<String> tokens = new ArrayList<String>();
        while (matcher.find()) {
            tokens.add(matcher.group(0));
        }
        return tokens.toArray(new String[0]);
    }

}
