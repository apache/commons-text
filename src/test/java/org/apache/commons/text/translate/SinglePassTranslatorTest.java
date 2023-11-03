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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for {@link SinglePassTranslator}.
 */
public class SinglePassTranslatorTest {

    private final SinglePassTranslator dummyTranslator = new SinglePassTranslator() {

        @Override
        void translateWhole(final CharSequence input, final Writer writer) throws IOException {
            // noop
        }
    };

    private StringWriter out;

    @BeforeEach
    public void before() {
         out = new StringWriter();
    }

    @Test
    public void testCodePointsAreReturned() throws Exception {
        assertThat(dummyTranslator.translate("", 0, out)).isEqualTo(0);
        assertThat(dummyTranslator.translate("abc", 0, out)).isEqualTo(3);
        assertThat(dummyTranslator.translate("abcdefg", 0, out)).isEqualTo(7);
    }

    @Test
    public void testIndexIsValidated() {
        assertThatIllegalArgumentException().isThrownBy(() -> dummyTranslator.translate("abc", 1, out));
    }

    @Test
    public void testTranslateThrowsIllegalArgumentException() {
        assertThatIllegalArgumentException().isThrownBy(() -> dummyTranslator.translate("(,Fk", 647, null));
    }

}
