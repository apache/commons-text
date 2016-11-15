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
 * Unit tests for {@link org.apache.commons.text.translate.NumericEntityUnescaper}.
 */
public class NumericEntityUnescaperTest  {

    @Test
    public void testSupplementaryUnescaping() {
        final org.apache.commons.text.translate.NumericEntityUnescaper neu = new org.apache.commons.text.translate.NumericEntityUnescaper();
        final String input = "&#68642;";
        final String expected = "\uD803\uDC22";

        final String result = neu.translate(input);
        assertEquals("Failed to unescape numeric entities supplementary characters", expected, result);
    }

    @Test
    public void testOutOfBounds() {
        final org.apache.commons.text.translate.NumericEntityUnescaper neu = new org.apache.commons.text.translate.NumericEntityUnescaper();

        assertEquals("Failed to ignore when last character is &", "Test &", neu.translate("Test &"));
        assertEquals("Failed to ignore when last character is &", "Test &#", neu.translate("Test &#"));
        assertEquals("Failed to ignore when last character is &", "Test &#x", neu.translate("Test &#x"));
        assertEquals("Failed to ignore when last character is &", "Test &#X", neu.translate("Test &#X"));
    }

    @Test
    public void testUnfinishedEntity() {
        // parse it
        org.apache.commons.text.translate.NumericEntityUnescaper neu = new org.apache.commons.text.translate.NumericEntityUnescaper(org.apache.commons.text.translate.NumericEntityUnescaper.OPTION.semiColonOptional);
        String input = "Test &#x30 not test";
        String expected = "Test \u0030 not test";

        String result = neu.translate(input);
        assertEquals("Failed to support unfinished entities (i.e. missing semi-colon)", expected, result);

        // ignore it
        neu = new org.apache.commons.text.translate.NumericEntityUnescaper();
        input = "Test &#x30 not test";
        expected = input;

        result = neu.translate(input);
        assertEquals("Failed to ignore unfinished entities (i.e. missing semi-colon)", expected, result);

        // fail it
        neu = new org.apache.commons.text.translate.NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        input = "Test &#x30 not test";

        try {
            result = neu.translate(input);
            fail("IllegalArgumentException expected");
        } catch(final IllegalArgumentException iae) {
            // expected
        }
    }

}
