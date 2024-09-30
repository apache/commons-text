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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.commons.lang3.CharUtils;
import org.junit.jupiter.api.Test;

public class CsvTranslatorsTest {

    @Test
    public void testCsvEscaperCommaTest() throws IOException {
        final CsvTranslators.CsvEscaper escaper = new CsvTranslators.CsvEscaper();
        final Writer writer = new StringWriter();
        final String input = "hi,this,is,a,test";
        escaper.translateWhole(input, writer);
        final String data = writer.toString();
        assertEquals("\"hi,this,is,a,test\"", data);
    }

    @Test
    public void testCsvEscaperCRTest() throws IOException {
        final CsvTranslators.CsvEscaper escaper = new CsvTranslators.CsvEscaper();
        final Writer writer = new StringWriter();
        final String input = "hi,this,is,a,CR,test" + String.valueOf(CharUtils.CR);
        escaper.translateWhole(input, writer);
        final String data = writer.toString();
        assertEquals("\"hi,this,is,a,CR,test" + String.valueOf(CharUtils.CR) + "\"", data);
    }

    @Test
    public void testCsvEscaperLFTest() throws IOException {
        final CsvTranslators.CsvEscaper escaper = new CsvTranslators.CsvEscaper();
        final Writer writer = new StringWriter();
        final String input = "hi,this,is,a,LF,test" + String.valueOf(CharUtils.LF);
        escaper.translateWhole(input, writer);
        final String data = writer.toString();
        assertEquals("\"hi,this,is,a,LF,test" + String.valueOf(CharUtils.LF) + "\"", data);
    }

    @Test
    public void testCsvEscaperPlaneTextTest() throws IOException {
        final CsvTranslators.CsvEscaper escaper = new CsvTranslators.CsvEscaper();
        final Writer writer = new StringWriter();
        final String input = "hi this is just a plane text nothing to do with csv!";
        escaper.translateWhole(input, writer);
        final String data = writer.toString();
        assertEquals(data, input);
    }

    @Test
    public void testCsvEscaperQuoteTest() throws IOException {
        final CsvTranslators.CsvEscaper escaper = new CsvTranslators.CsvEscaper();
        final Writer writer = new StringWriter();
        final String input = "hi,this,is,a,\"quote,test";
        escaper.translateWhole(input, writer);
        final String data = writer.toString();
        assertEquals("\"hi,this,is,a,\"\"quote,test\"", data);
    }

    @Test
    public void testCsvUnEscaperPlaneTextTest() throws IOException {
        final CsvTranslators.CsvUnescaper escaper = new CsvTranslators.CsvUnescaper();
        final Writer writer = new StringWriter();
        final String input = "hi,this,is,unescape,test";
        escaper.translateWhole(input, writer);
        final String data = writer.toString();
        assertEquals(input, data);
    }

    @Test
    public void testCsvUnEscaperTest1() throws IOException {
        final CsvTranslators.CsvUnescaper escaper = new CsvTranslators.CsvUnescaper();
        final Writer writer = new StringWriter();
        final String input = "\"hi,this,is,unescape,test\"";
        escaper.translateWhole(input, writer);
        final String data = writer.toString();
        assertEquals("hi,this,is,unescape,test", data);
    }

    @Test
    public void testCsvUnEscaperTest2() throws IOException {
        final CsvTranslators.CsvUnescaper escaper = new CsvTranslators.CsvUnescaper();
        final Writer writer = new StringWriter();
        final String input = "\"hi,this,is,unescape,test";
        escaper.translateWhole(input, writer);
        final String data = writer.toString();
        assertEquals(input, data);
    }

    @Test
    public void testCsvUnEscaperTest3() throws IOException {
        final CsvTranslators.CsvUnescaper escaper = new CsvTranslators.CsvUnescaper();
        final Writer writer = new StringWriter();
        final String input = "hi,this,is,unescape,test\"";
        escaper.translateWhole(input, writer);
        final String data = writer.toString();
        assertEquals(input, data);
    }

    @Test
    public void testCsvUnEscaperTest4() throws IOException {
        final CsvTranslators.CsvUnescaper escaper = new CsvTranslators.CsvUnescaper();
        final Writer writer = new StringWriter();
        final String input = "\"hi,this,is,\"unescape,test\"";
        escaper.translateWhole(input, writer);
        final String data = writer.toString();
        assertEquals("hi,this,is,\"unescape,test", data);
    }

}
