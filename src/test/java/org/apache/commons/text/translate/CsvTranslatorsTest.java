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

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.commons.lang3.CharUtils;
import org.junit.jupiter.api.Test;

public class CsvTranslatorsTest {

    @Test
    public void csvEscaperPlaneTextTest() throws IOException {
        final CsvTranslators.CsvEscaper escaper = new CsvTranslators.CsvEscaper();
        final Writer writer = new StringWriter();
        final String input = "hi this is just a plane text nothing to do with csv!";
        escaper.translateWhole(input, writer);
        final String data = writer.toString();
        assertThat(input).isEqualTo(data);
    }

    @Test
    public void csvEscaperCommaTest() throws IOException {
        final CsvTranslators.CsvEscaper escaper = new CsvTranslators.CsvEscaper();
        final Writer writer = new StringWriter();
        final String input = "hi,this,is,a,test";
        escaper.translateWhole(input, writer);
        final String data = writer.toString();
        assertThat(data).isEqualTo("\"hi,this,is,a,test\"");
    }

    @Test
    public void csvEscaperQuoteTest() throws IOException {
        final CsvTranslators.CsvEscaper escaper = new CsvTranslators.CsvEscaper();
        final Writer writer = new StringWriter();
        final String input = "hi,this,is,a,\"quote,test";
        escaper.translateWhole(input, writer);
        final String data = writer.toString();
        assertThat(data).isEqualTo("\"hi,this,is,a,\"\"quote,test\"");
    }

    @Test
    public void csvEscaperCRTest() throws IOException {
        final CsvTranslators.CsvEscaper escaper = new CsvTranslators.CsvEscaper();
        final Writer writer = new StringWriter();
        final String input = "hi,this,is,a,CR,test" + String.valueOf(CharUtils.CR);
        escaper.translateWhole(input, writer);
        final String data = writer.toString();
        assertThat(data).isEqualTo("\"hi,this,is,a,CR,test" + String.valueOf(CharUtils.CR) + "\"");
    }

    @Test
    public void csvEscaperLFTest() throws IOException {
        final CsvTranslators.CsvEscaper escaper = new CsvTranslators.CsvEscaper();
        final Writer writer = new StringWriter();
        final String input = "hi,this,is,a,LF,test" + String.valueOf(CharUtils.LF);
        escaper.translateWhole(input, writer);
        final String data = writer.toString();
        assertThat(data).isEqualTo("\"hi,this,is,a,LF,test" + String.valueOf(CharUtils.LF) + "\"");
    }

    @Test
    public void csvUnEscaperPlaneTextTest() throws IOException {
        final CsvTranslators.CsvUnescaper escaper = new CsvTranslators.CsvUnescaper();
        final Writer writer = new StringWriter();
        final String input = "hi,this,is,unescape,test";
        escaper.translateWhole(input, writer);
        final String data = writer.toString();
        assertThat(data).isEqualTo("hi,this,is,unescape,test");
    }

    @Test
    public void csvUnEscaperTest1() throws IOException {
        final CsvTranslators.CsvUnescaper escaper = new CsvTranslators.CsvUnescaper();
        final Writer writer = new StringWriter();
        final String input = "\"hi,this,is,unescape,test\"";
        escaper.translateWhole(input, writer);
        final String data = writer.toString();
        assertThat(data).isEqualTo("hi,this,is,unescape,test");
    }

    @Test
    public void csvUnEscaperTest2() throws IOException {
        final CsvTranslators.CsvUnescaper escaper = new CsvTranslators.CsvUnescaper();
        final Writer writer = new StringWriter();
        final String input = "\"hi,this,is,unescape,test";
        escaper.translateWhole(input, writer);
        final String data = writer.toString();
        assertThat(input).isEqualTo(data);
    }

    @Test
    public void csvUnEscaperTest3() throws IOException {
        final CsvTranslators.CsvUnescaper escaper = new CsvTranslators.CsvUnescaper();
        final Writer writer = new StringWriter();
        final String input = "hi,this,is,unescape,test\"";
        escaper.translateWhole(input, writer);
        final String data = writer.toString();
        assertThat(input).isEqualTo(data);
    }

    @Test
    public void csvUnEscaperTest4() throws IOException {
        final CsvTranslators.CsvUnescaper escaper = new CsvTranslators.CsvUnescaper();
        final Writer writer = new StringWriter();
        final String input = "\"hi,this,is,\"unescape,test\"";
        escaper.translateWhole(input, writer);
        final String data = writer.toString();
        assertThat(data).isEqualTo("hi,this,is,\"unescape,test");
    }

}
