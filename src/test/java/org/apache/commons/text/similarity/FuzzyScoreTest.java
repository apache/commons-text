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
package org.apache.commons.text.similarity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.Locale;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link FuzzyScore}.
 */
public class FuzzyScoreTest {

    private static final FuzzyScore ENGLISH_SCORE = new FuzzyScore(Locale.ENGLISH);

    @Test
    public void testGetFuzzyScore() {
        assertThat(ENGLISH_SCORE.fuzzyScore("", "")).isEqualTo(0);
        assertThat(ENGLISH_SCORE.fuzzyScore("Workshop", "b")).isEqualTo(0);
        assertThat(ENGLISH_SCORE.fuzzyScore("Room", "o")).isEqualTo(1);
        assertThat(ENGLISH_SCORE.fuzzyScore("Workshop", "w")).isEqualTo(1);
        assertThat(ENGLISH_SCORE.fuzzyScore("Workshop", "ws")).isEqualTo(2);
        assertThat(ENGLISH_SCORE.fuzzyScore("Workshop", "wo")).isEqualTo(4);
        assertThat(ENGLISH_SCORE.fuzzyScore("Apache Software Foundation", "asf")).isEqualTo(3);
    }

    @Test
    public void testGetFuzzyScore_NullNullLocale() {
        assertThatIllegalArgumentException().isThrownBy(() -> ENGLISH_SCORE.fuzzyScore(null, null));
    }

    @Test
    public void testGetFuzzyScore_NullStringLocale() {
        assertThatIllegalArgumentException().isThrownBy(() -> ENGLISH_SCORE.fuzzyScore(null, "not null"));
    }

    @Test
    public void testGetFuzzyScore_StringNullLocale() {
        assertThatIllegalArgumentException().isThrownBy(() -> ENGLISH_SCORE.fuzzyScore("not null", null));
    }

    @Test
    public void testGetLocale() {
        final Locale locale = Locale.CANADA_FRENCH;
        final FuzzyScore fuzzyScore = new FuzzyScore(locale);
        final Locale localeTwo = fuzzyScore.getLocale();

        assertThat(localeTwo).isSameAs(locale);
    }

    @Test
    public void testMissingLocale() {
        assertThatIllegalArgumentException().isThrownBy(() -> new FuzzyScore((Locale) null));
    }

}
