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

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.nio.charset.Charset;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@code HumanNameParser} class.
 */
public class HumanNameParserTest {

    private CSVParser inputParser;
    private HumanNameParser nameParser;

    @Before
    public void setUp() throws Exception {
        inputParser = CSVParser.parse(
                HumanNameParserTest.class.getResource("testNames.txt"), 
                Charset.forName("UTF-8"), 
                CSVFormat.DEFAULT.withDelimiter('|').withHeader());
        nameParser = new HumanNameParser();
    }

    @After
    public void tearDown() throws Exception {
        if (inputParser != null) {
            inputParser.close();
        }
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException_WhenNullIsParsed() throws Exception {
        nameParser.parse(null);
    }

    @Test
    public void testInputs() {
        for (CSVRecord record : inputParser) {
            validateRecord(record);
        }
    }

    
    /**
     * Validates a line in the testNames.txt file.
     *
     * @param record a CSVRecord representing one record in the input file.
     */
    private void validateRecord(CSVRecord record) {
        Name result = nameParser.parse(record.get(Colums.Name));

        long recordNum = record.getRecordNumber();
        assertThat("Wrong LeadingInit in record " + recordNum,
                result.getLeadingInitial(), equalTo(record.get(Colums.LeadingInit)));

        assertThat("Wrong FirstName in record " + recordNum,
                result.getFirstName(), equalTo(record.get(Colums.FirstName)));

        assertThat("Wrong NickName in record " + recordNum,
                result.getNickName(), equalTo(record.get(Colums.NickName)));

        assertThat("Wrong MiddleName in record " + recordNum,
                result.getMiddleName(), equalTo(record.get(Colums.MiddleName)));

        assertThat("Wrong LastName in record " + recordNum,
                result.getLastName(), equalTo(record.get(Colums.LastName)));

        assertThat("Wrong Suffix in record " + recordNum,
                result.getSuffix(), equalTo(record.get(Colums.Suffix)));
    }

    private enum Colums {
        Name,LeadingInit,FirstName,NickName,MiddleName,LastName,Suffix
    }
}
