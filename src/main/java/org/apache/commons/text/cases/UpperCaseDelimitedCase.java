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
import org.apache.commons.text.matcher.StringMatcherFactory;


/**
 * Case implementation which parses and formats strings where tokens are delimited by upper case characters.
 */
public class UpperCaseDelimitedCase implements Case {

    /**
     * The tokenizer.
     */
    private StringTokenizer tokenizer;

    /**
     * The stringifier.
     */
    private TokenStringifier stringifier;

    /**
     * Constructs a new UpperCaseDelimitedCase instance.
     */
    UpperCaseDelimitedCase(boolean lowerCaseFirstCharacter) {
        tokenizer = new StringTokenizer((String) null, StringMatcherFactory.INSTANCE.uppercaseMatcher());
        tokenizer.setOmitDelimiterMatches(false);
        stringifier = new TokenStringifier(TokenFormatterFactory.emptyFormatter(), new PascalTokenFormatter(lowerCaseFirstCharacter));
    }

    /**
     * Parses a string into tokens.
     * <p>
     * String characters are iterated over and when an upper case Unicode character is
     * encountered, that character starts a new token, with the character
     * itself included in the token. This method never returns empty tokens.
     * </p>
     *
     * @param string the string to parse
     * @return the list of tokens found in the string
     */
    @Override
    public List<String> parse(String string) {
        tokenizer.reset(string);
        return tokenizer.getTokenList();
    }

    /**
     * Formats string tokens into a single string where each token begins with an upper case
     * character, followed by lower case or non cased characters.
     * <p>
     * Iterates the tokens and formats each one into a token where the first character of the token
     * is forced upper case in the output. The remaining characters of the token will be lower case
     * or non cased. Conversions to lower case are attempted and any conversion that is not possible
     * throws an exception. Any other characters in the token are returned as-is. Empty tokens are
     * not supported and will cause an exception to be thrown.
     * </p>
     *
     * @param tokens the string tokens to be formatted
     * @return the formatted string
     * @throws IllegalArgumentException if 1) any token is empty 2) any token begins with a
     * character that cannot be mapped to upper case, or 3) any token contains an upper or title case
     * character that cannot be mapped to lower case.
     */
    @Override
    public String format(Iterable<String> tokens) {
        stringifier.reset(tokens);
        return stringifier.getString();
    }


}
