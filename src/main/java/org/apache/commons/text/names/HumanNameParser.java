/*
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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

/**
 * A parser capable of parsing name parts out of a single string.
 *
 * <h3>Parsing examples</h3>
 * 
 * <p>The code works by basically applying several Regexes in a certain order
 * and removing (chopping) tokens off the original string. The parser creates
 * a {@link Name} object representing the parse result. Note that passing null
 * to the {@link #parse(String)} method will result in an exception.</p>
 *
 * <table summary="Examples">
 *  <tr>
 *   <th>input</th>
 *   <th>Leading initial</th>
 *   <th>First name</th>
 *   <th>Nick name</th>
 *   <th>Middle name</th>
 *   <th>Last Name</th>
 *   <th>Suffix</th>
 *  </tr>
 *  <tr>
 *   <td>J. Walter Weatherman</td>
 *   <td>J.</td>
 *   <td>Walter</td>
 *   <td></td>
 *   <td></td>
 *   <td>Weatherman</td>
 *   <td></td>
 *  </tr>
 *  <tr>
 *   <td>de la Cruz, Ana M.</td>
 *   <td></td>
 *   <td>Ana</td>
 *   <td></td>
 *   <td>M.</td>
 *   <td>de la Cruz</td>
 *   <td></td>
 *  </tr>
 *  <tr>
 *   <td>James C. ('Jimmy') O'Dell, Jr.</td>
 *   <td></td>
 *   <td>James</td>
 *   <td>Jimmy</td>
 *   <td>C.</td>
 *   <td>O'Dell</td>
 *   <td>Jr.</td>
 *  </tr>
 * </table>
 *
 * <h3>Sample usage</h3>
 * 
 * <p>HumanNameParser instances are immutable and can be reused for parsing multiple names:</p>
 * 
 * <pre>
 * HumanNameParser parser = new HumanNameParser();
 * Name parsedName = parser.parse("Sérgio Vieira de Mello")
 * String firstName = parsedName.getFirstName();
 * String nickname = parsedName.getNickName();
 * // ...
 * 
 * Name nextName = parser.parse("James C. ('Jimmy') O'Dell, Jr.")
 * String firstName = nextName.getFirstName();
 * String nickname = nextName.getNickName();
 * </pre>
 *
 * <h3>Further notes</h3>
 * 
 * <p>The original code was written in <a href="http://jasonpriem.com/human-name-parse">PHP</a>
 * and ported to <a href="http://tupilabs.github.io/HumanNameParser.java/">Java</a>. This 
 * implementation is based on the Java implementation, with additions
 * suggested in <a href="https://issues.apache.org/jira/browse/TEXT-15">TEXT-15</a>
 * and <a href="https://issues.apache.org/jira/browse/TEXT-16">TEXT-16</a>.</p>
 *
 * <p>This class is immutable.</p>
 */
public final class HumanNameParser {

    private final List<String> suffixes;
    private final List<String> prefixes;

    /**
     * Creates a new parser.
     */
    public HumanNameParser() {
        // TODO make this configurable
        this.suffixes = Arrays.asList(
                "esq", "esquire", "jr",
                "sr", "2", "ii", "iii", "iv");
        this.prefixes = Arrays.asList(
                    "bar", "ben", "bin", "da", "dal",
                    "de la", "de", "del", "der", "di", "ibn", "la", "le",
                    "san", "st", "ste", "van", "van der", "van den", "vel",
                    "von" );
    }

    /**
     * Parses a name from the given string.
     *
     * @param name the name to parse. Must not be null.
     * @throws NameParseException if the parser fails to retrieve the name parts.
     * @throws NullPointerException if name is null.
     * @return The name object
     */
    public Name parse(String name) {
        Objects.requireNonNull(name, "Parameter 'name' must not be null.");

        NameString nameString = new NameString(name);
        // TODO compile regexes only once when the parser is created
        String suffixes = StringUtils.join(this.suffixes, "\\.*|") + "\\.*";
        String prefixes = StringUtils.join(this.prefixes, " |") + " ";

        // The regex use is a bit tricky.  *Everything* matched by the regex will be replaced,
        // but you can select a particular parenthesized submatch to be returned.
        // Also, note that each regex requres that the preceding ones have been run, and matches chopped out.
        // names that starts or end w/ an apostrophe break this
        String nicknamesRegex = "(?i) ('|\\\"|\\(\\\"*'*)(.+?)('|\\\"|\\\"*'*\\)) ";
        String suffixRegex = "(?i),* *((" + suffixes + ")$)";
        String lastRegex = "(?i)(?!^)\\b([^ ]+ y |" + prefixes + ")*[^ ]+$";
        // note the lookahead, which isn't returned or replaced
        String leadingInitRegex = "(?i)(^(.\\.*)(?= \\p{L}{2}))";
        String firstRegex = "(?i)^([^ ]+)";

        // get nickname, if there is one
        String nickname = nameString.chopWithRegex(nicknamesRegex, 2);

        // get suffix, if there is one
        String suffix = nameString.chopWithRegex(suffixRegex, 1);

        // flip the before-comma and after-comma parts of the name
        nameString.flip(",");

        // get the last name
        String last = nameString.chopWithRegex(lastRegex, 0);

        // get the first initial, if there is one
        String leadingInit = nameString.chopWithRegex(leadingInitRegex, 1);

        // get the first name
        String first = nameString.chopWithRegex(firstRegex, 0);
        if (StringUtils.isBlank(first)) {
            throw new NameParseException("Couldn't find a first name in '{" + nameString.getWrappedString() + "}'");
        }

        // if anything's left, that's the middle name
        String middle = nameString.getWrappedString();
        
        return new Name(leadingInit, first, nickname, middle, last, suffix);
    }

}
