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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests the {@code HumanNameParser} class.
 */
public class ParserTest {

    private static final Logger LOGGER = Logger.getLogger(ParserTest.class.getName());

    private static File testNames = null;

    @BeforeClass
    public static void setUp() {
        testNames = new File(ParserTest.class.getResource("/org/apache/commons/text/names/testNames.txt").getFile());
    }

    @Test
    public void testAll() throws IOException {
        BufferedReader buffer = null;
        FileReader reader = null;

        try {
            reader = new FileReader(testNames);
            buffer = new BufferedReader(reader);

            String line = null;
            while ((line = buffer.readLine()) != null) {
                if (StringUtils.isBlank(line)) {
                    LOGGER.warning("Empty line in testNames.txt");
                    continue;
                }

                String[] tokens = line.split("\\|");
                if (tokens.length != 7) {
                    LOGGER.warning(String.format("Invalid line in testNames.txt: %s", line));
                    continue;
                }

                validateLine(tokens);
            }
        } finally {
            if (reader != null)
                reader.close();
            if (buffer != null)
                buffer.close();
        }
    }

    /**
     * Validates a line in the testNames.txt file.
     *
     * @param tokens the tokens with leading spaces
     */
    private void validateLine(String[] tokens) {
        String name = tokens[0].trim();

        String leadingInit = tokens[1].trim();
        String first = tokens[2].trim();
        String nickname = tokens[3].trim();
        String middle = tokens[4].trim();
        String last = tokens[5].trim();
        String suffix = tokens[6].trim();

        HumanNameParser parser = new HumanNameParser(name);

        assertEquals(leadingInit, parser.getLeadingInit());
        assertEquals(first, parser.getFirst());
        assertEquals(nickname, parser.getNickname());
        assertEquals(middle, parser.getMiddle());
        assertEquals(last, parser.getLast());
        assertEquals(suffix, parser.getSuffix());
    }

}
