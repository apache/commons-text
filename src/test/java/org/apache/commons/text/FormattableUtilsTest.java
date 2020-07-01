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
package org.apache.commons.text;

import static java.util.FormattableFlags.LEFT_JUSTIFY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.util.Formattable;
import java.util.Formatter;

import org.junit.jupiter.api.Test;

/**
 * Unit tests {@link FormattableUtils}.
 */
public class FormattableUtilsTest {

    static class SimplestFormattable implements Formattable {
        private final String text;

        SimplestFormattable(final String text) {
            this.text = text;
        }

        @Override
        public void formatTo(final Formatter formatter, final int flags, final int width, final int precision) {
            formatter.format(text);
        }
    }

    private Formatter createFormatter() {
        return new Formatter();
    }

    @Test
    public void testAlternatePadCharacter() {
        final char pad = '_';
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, -1, -1, pad).toString()).isEqualTo("foo");
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, -1, 2, pad).toString()).isEqualTo("fo");
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, 4, -1, pad).toString()).isEqualTo("_foo");
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, 6, -1, pad).toString()).isEqualTo("___foo");
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, 3, 2, pad).toString()).isEqualTo("_fo");
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, 5, 2, pad).toString()).isEqualTo("___fo");
        assertThat(FormattableUtils.append("foo", createFormatter(), LEFT_JUSTIFY, 4, -1, pad).toString())
            .isEqualTo("foo_");
        assertThat(FormattableUtils.append("foo", createFormatter(), LEFT_JUSTIFY, 6, -1, pad).toString())
            .isEqualTo("foo___");
        assertThat(FormattableUtils.append("foo", createFormatter(), LEFT_JUSTIFY, 3, 2, pad).toString())
            .isEqualTo("fo_");
        assertThat(FormattableUtils.append("foo", createFormatter(), LEFT_JUSTIFY, 5, 2, pad).toString())
            .isEqualTo("fo___");
    }

    @Test
    public void testAlternatePadCharAndEllipsis() {
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, -1, -1, '_', "*").toString()).isEqualTo("foo");
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, -1, 2, '_', "*").toString()).isEqualTo("f*");
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, 4, -1, '_', "*").toString()).isEqualTo("_foo");
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, 6, -1, '_', "*").toString())
            .isEqualTo("___foo");
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, 3, 2, '_', "*").toString()).isEqualTo("_f*");
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, 5, 2, '_', "*").toString()).isEqualTo("___f*");
        assertThat(FormattableUtils.append("foo", createFormatter(), LEFT_JUSTIFY, 4, -1, '_', "*").toString())
            .isEqualTo("foo_");
        assertThat(FormattableUtils.append("foo", createFormatter(), LEFT_JUSTIFY, 6, -1, '_', "*").toString())
            .isEqualTo("foo___");
        assertThat(FormattableUtils.append("foo", createFormatter(), LEFT_JUSTIFY, 3, 2, '_', "*").toString())
            .isEqualTo("f*_");
        assertThat(FormattableUtils.append("foo", createFormatter(), LEFT_JUSTIFY, 5, 2, '_', "*").toString())
            .isEqualTo("f*___");

        assertThat(FormattableUtils.append("foo", createFormatter(), 0, -1, -1, '_', "+*").toString()).isEqualTo("foo");
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, -1, 2, '_', "+*").toString()).isEqualTo("+*");
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, 4, -1, '_', "+*").toString()).isEqualTo("_foo");
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, 6, -1, '_', "+*").toString())
            .isEqualTo("___foo");
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, 3, 2, '_', "+*").toString()).isEqualTo("_+*");
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, 5, 2, '_', "+*").toString()).isEqualTo("___+*");
        assertThat(FormattableUtils.append("foo", createFormatter(), LEFT_JUSTIFY, 4, -1, '_', "+*").toString())
            .isEqualTo("foo_");
        assertThat(FormattableUtils.append("foo", createFormatter(), LEFT_JUSTIFY, 6, -1, '_', "+*").toString())
            .isEqualTo("foo___");
        assertThat(FormattableUtils.append("foo", createFormatter(), LEFT_JUSTIFY, 3, 2, '_', "+*").toString())
            .isEqualTo("+*_");
        assertThat(FormattableUtils.append("foo", createFormatter(), LEFT_JUSTIFY, 5, 2, '_', "+*").toString())
            .isEqualTo("+*___");
    }

    @Test
    public void testAppendWithNullFormatterAndIntsThrowsNullPointerException() {
        assertThatNullPointerException().isThrownBy(() -> FormattableUtils.append("", null, 0, 0, 0, '}'));
    }

    @Test
    public void testDefaultAppend() {
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, -1, -1).toString()).isEqualTo("foo");
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, -1, 2).toString()).isEqualTo("fo");
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, 4, -1).toString()).isEqualTo(" foo");
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, 6, -1).toString()).isEqualTo("   foo");
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, 3, 2).toString()).isEqualTo(" fo");
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, 5, 2).toString()).isEqualTo("   fo");
        assertThat(FormattableUtils.append("foo", createFormatter(), LEFT_JUSTIFY, 4, -1).toString()).isEqualTo("foo ");
        assertThat(FormattableUtils.append("foo", createFormatter(), LEFT_JUSTIFY, 6, -1).toString())
            .isEqualTo("foo   ");
        assertThat(FormattableUtils.append("foo", createFormatter(), LEFT_JUSTIFY, 3, 2).toString()).isEqualTo("fo ");
        assertThat(FormattableUtils.append("foo", createFormatter(), LEFT_JUSTIFY, 5, 2).toString()).isEqualTo("fo   ");
    }

    @Test
    public void testEllipsis() {
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, -1, -1, "*").toString()).isEqualTo("foo");
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, -1, 2, "*").toString()).isEqualTo("f*");
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, 4, -1, "*").toString()).isEqualTo(" foo");
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, 6, -1, "*").toString()).isEqualTo("   foo");
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, 3, 2, "*").toString()).isEqualTo(" f*");
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, 5, 2, "*").toString()).isEqualTo("   f*");
        assertThat(FormattableUtils.append("foo", createFormatter(), LEFT_JUSTIFY, 4, -1, "*").toString())
            .isEqualTo("foo ");
        assertThat(FormattableUtils.append("foo", createFormatter(), LEFT_JUSTIFY, 6, -1, "*").toString())
            .isEqualTo("foo   ");
        assertThat(FormattableUtils.append("foo", createFormatter(), LEFT_JUSTIFY, 3, 2, "*").toString())
            .isEqualTo("f* ");
        assertThat(FormattableUtils.append("foo", createFormatter(), LEFT_JUSTIFY, 5, 2, "*").toString())
            .isEqualTo("f*   ");

        assertThat(FormattableUtils.append("foo", createFormatter(), 0, -1, -1, "+*").toString()).isEqualTo("foo");
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, -1, 2, "+*").toString()).isEqualTo("+*");
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, 4, -1, "+*").toString()).isEqualTo(" foo");
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, 6, -1, "+*").toString()).isEqualTo("   foo");
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, 3, 2, "+*").toString()).isEqualTo(" +*");
        assertThat(FormattableUtils.append("foo", createFormatter(), 0, 5, 2, "+*").toString()).isEqualTo("   +*");
        assertThat(FormattableUtils.append("foo", createFormatter(), LEFT_JUSTIFY, 4, -1, "+*").toString())
            .isEqualTo("foo ");
        assertThat(FormattableUtils.append("foo", createFormatter(), LEFT_JUSTIFY, 6, -1, "+*").toString())
            .isEqualTo("foo   ");
        assertThat(FormattableUtils.append("foo", createFormatter(), LEFT_JUSTIFY, 3, 2, "+*").toString())
            .isEqualTo("+* ");
        assertThat(FormattableUtils.append("foo", createFormatter(), LEFT_JUSTIFY, 5, 2, "+*").toString())
            .isEqualTo("+*   ");
    }

    @Test
    public void testIllegalEllipsis() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            FormattableUtils.append("foo", createFormatter(), 0, -1, 1, "xx");
        });
    }

    @Test
    public void testPublicConstructorExists() {
        new FormattableUtils();
    }

    @Test
    public void testSimplestFormat() {
        final Formattable formattable = new SimplestFormattable("foo");

        assertThat(FormattableUtils.toString(formattable)).isEqualTo("foo");
    }

}
