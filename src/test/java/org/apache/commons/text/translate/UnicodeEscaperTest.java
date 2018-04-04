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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link UnicodeEscaper}.
 */
public class UnicodeEscaperTest  {

    @Test
    public void testBelow() {
        final UnicodeEscaper ue = UnicodeEscaper.below('F');

        final String input = "ADFGZ";
        final String result = ue.translate(input);
        assertThat(result).as("Failed to escape Unicode characters via the below method")
            .isEqualTo("\\u0041\\u0044FGZ");
    }

    @Test
    public void testBetween() {
        final UnicodeEscaper ue = UnicodeEscaper.between('F', 'L');

        final String input = "ADFGZ";
        final String result = ue.translate(input);
        assertThat(result).as("Failed to escape Unicode characters via the between method")
            .isEqualTo("AD\\u0046\\u0047Z");
    }

    @Test
    public void testAbove() {
        final UnicodeEscaper ue = UnicodeEscaper.above('F');

        final String input = "ADFGZ";
        final String result = ue.translate(input);
        assertThat(result).as("Failed to escape Unicode characters via the above method")
            .isEqualTo("ADF\\u0047\\u005A");
    }
}
