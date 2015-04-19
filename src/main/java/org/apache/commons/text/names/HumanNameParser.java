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

import org.apache.commons.lang3.StringUtils;

/**
 * <p>A parser capable of parsing name parts out of a single string.</p>
 *
 * <p>The code works by basically applying several Regexes in a certain order
 * and removing (chopping) tokens off the original string. The parser consumes
 * the tokens during its creation.</p>
 *
 * <ul>
 * <li>J. Walter Weatherman </li>
 * <li>de la Cruz, Ana M.</li>
 * <li>James C. ('Jimmy') O'Dell, Jr.</li>
 * </ul>
 *
 * <p>and parses out the:</p>
 *
 * <ul>
 * <li>leading initial (Like "J." in "J. Walter Weatherman")</li>
 * <li>first name (or first initial in a name like 'R. Crumb')</li>
 * <li>nicknames (like "Jimmy" in "James C. ('Jimmy') O'Dell, Jr.")</li>
 * <li>middle names</li>
 * <li>last name (including compound ones like "van der Sar' and "Ortega y Gasset"), and</li>
 * <li>suffix (like 'Jr.', 'III')</li>
 * </ul>
 *
 * <pre>
 * Name name = new Name("Sérgio Vieira de Mello");
 * HumanNameParser parser = new HumanNameParser(name);
 * String firstName = parser.getFirst();
 * String nickname = parser.getNickname();
 * // ...
 * </pre>
 *
 * <p>The original code was written in <a href="http://jasonpriem.com/human-name-parse">PHP</a>
 * and ported to <a href="http://tupilabs.github.io/HumanNameParser.java/">Java</a>.</p>
 *
 * <p>This implementation is based on the Java implementation, with additions
 * suggested in <a href="https://issues.apache.org/jira/browse/SANDBOX-487">SANDBOX-487</a>.</p>
 *
 * <p>This class is not thread-safe.</p>
 */
public class HumanNameParser {

    /**
     * Name parsed.
     */
    private Name name;
    /**
     * Leading init part.
     */
    private String leadingInit;
    /**
     * First name.
     */
    private String first;
    /**
     * Single nickname found in the name input.
     */
    private String nickname;
    /**
     * Middle name.
     */
    private String middle;
    /**
     * Last name.
     */
    private String last;
    /**
     * Name suffix.
     */
    private String suffix;
    /**
     * Suffixes found.
     */
    private List<String> suffixes;
    /**
     * Prefixes found.
     */
    private List<String> prefixes;

    /**
     * Creates a parser given a string name.
     *
     * @param name string name
     */
    public HumanNameParser(String name) {
        this(new Name(name));
    }

    /**
     * Creates a parser given a {@code Name} object.
     *
     * @param name {@code Name}
     */
    public HumanNameParser(Name name) {
        this.name = name;

        this.leadingInit = "";
        this.first = "";
        this.nickname = "";
        this.middle = "";
        this.last = "";
        this.suffix = "";

        this.suffixes = Arrays.asList(new String[] {
                "esq", "esquire", "jr",
                "sr", "2", "ii", "iii", "iv" });
        this.prefixes = Arrays
            .asList(new String[] {
                    "bar", "ben", "bin", "da", "dal",
                    "de la", "de", "del", "der", "di", "ibn", "la", "le",
                    "san", "st", "ste", "van", "van der", "van den", "vel",
                    "von" });

        this.parse();
    }

    /**
     * Gets the {@code Name} object.
     *
     * @return the {@code Name} object
     */
    public Name getName() {
        return name;
    }

    /**
     * Gets the leading init part of the name.
     *
     * @return the leading init part of the name
     */
    public String getLeadingInit() {
        return leadingInit;
    }

    /**
     * Gets the first name.
     *
     * @return first name
     */
    public String getFirst() {
        return first;
    }

    /**
     * Gets the nickname.
     *
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Gets the middle name.
     *
     * @return the middle name
     */
    public String getMiddle() {
        return middle;
    }

    /**
     * Gets the last name.
     *
     * @return the last name
     */
    public String getLast() {
        return last;
    }

    /**
     * Gets the suffix part of the name.
     *
     * @return the name suffix
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * Gets the name suffixes.
     *
     * @return the name suffixes
     */
    public List<String> getSuffixes() {
        return suffixes;
    }

    /**
     * Gets the name prefixes.
     *
     * @return the name prefixes
     */
    public List<String> getPrefixes() {
        return prefixes;
    }

    /**
     * Consumes the string and creates the name parts.
     *
     * @throws NameParseException if the parser fails to retrieve the name parts
     */
    private void parse() {
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
        this.nickname = this.name.chopWithRegex(nicknamesRegex, 2);

        // get suffix, if there is one
        this.suffix = this.name.chopWithRegex(suffixRegex, 1);

        // flip the before-comma and after-comma parts of the name
        this.name.flip(",");

        // get the last name
        this.last = this.name.chopWithRegex(lastRegex, 0);

        // get the first initial, if there is one
        this.leadingInit = this.name.chopWithRegex(leadingInitRegex, 1);

        // get the first name
        this.first = this.name.chopWithRegex(firstRegex, 0);
        if (StringUtils.isBlank(this.first)) {
            throw new NameParseException("Couldn't find a first name in '{" + this.name.getStr() + "}'");
        }

        // if anything's left, that's the middle name
        this.middle = this.name.getStr();
    }

}
