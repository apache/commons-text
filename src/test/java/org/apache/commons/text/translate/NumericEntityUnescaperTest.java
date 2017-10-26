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

package org.apache.commons.text.translate;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link NumericEntityUnescaper}.
 */
public class NumericEntityUnescaperTest  {

    @Test
    public void testSupplementaryUnescaping() {
        final NumericEntityUnescaper neu = new NumericEntityUnescaper();
        final String input = "&#68642;";
        final String expected = "\uD803\uDC22";

        final String result = neu.translate(input);
        assertEquals("Failed to unescape numeric entities supplementary characters", expected, result);
    }

    @Test
    public void testOutOfBounds() {
        final NumericEntityUnescaper neu = new NumericEntityUnescaper();

        assertEquals("Failed to ignore when last character is &", "Test &", neu.translate("Test &"));
        assertEquals("Failed to ignore when last character is &", "Test &#", neu.translate("Test &#"));
        assertEquals("Failed to ignore when last character is &", "Test &#x", neu.translate("Test &#x"));
        assertEquals("Failed to ignore when last character is &", "Test &#X", neu.translate("Test &#X"));
    }

    @Test
    public void testUnfinishedEntity() {
        // parse it
        NumericEntityUnescaper neu = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        String input = "Test &#x30 not test";
        String expected = "Test \u0030 not test";

        String result = neu.translate(input);
        assertEquals("Failed to support unfinished entities (i.e. missing semi-colon)", expected, result);

        // ignore it
        neu = new NumericEntityUnescaper();
        input = "Test &#x30 not test";
        expected = input;

        result = neu.translate(input);
        assertEquals("Failed to ignore unfinished entities (i.e. missing semi-colon)", expected, result);

        // fail it
        neu = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        input = "Test &#x30 not test";

        try {
            result = neu.translate(input);
            fail("IllegalArgumentException expected");
        } catch (final IllegalArgumentException iae) {
            // expected
        }
    }

    @Test
    public void testCreatesNumericEntityUnescaperOne() {
        final NumericEntityUnescaper.OPTION[] numericEntityUnescaperOPTIONArray = new NumericEntityUnescaper.OPTION[0];
        final NumericEntityUnescaper numericEntityUnescaper =
                new NumericEntityUnescaper(numericEntityUnescaperOPTIONArray);

        assertEquals("2|y|O7y`&#uVWj", numericEntityUnescaper.translate("2|y|O7y`&#uVWj"));
    }

    @Test
    public void testCreatesNumericEntityUnescaperTwo() {
        final NumericEntityUnescaper.OPTION[] numericEntityUnescaperOPTIONArray = new NumericEntityUnescaper.OPTION[0];
        final NumericEntityUnescaper numericEntityUnescaper =
                new NumericEntityUnescaper(numericEntityUnescaperOPTIONArray);

        assertEquals("Ws2v8|O=7NR&#cB", numericEntityUnescaper.translate("Ws2v8|O=7NR&#cB"));
    }

}
