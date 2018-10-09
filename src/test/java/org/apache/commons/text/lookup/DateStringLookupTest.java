/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */

package org.apache.commons.text.lookup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link DateStringLookup}.
 */
public class DateStringLookupTest {

    @Test
    public void testDefault() throws ParseException {
        final String formatted = DateStringLookup.INSTANCE.lookup(null);
        DateFormat.getInstance().parse(formatted); // throws ParseException

    }

    @Test
    public void testFormat() {
        final String fomat = "yyyy-MM-dd";
        final String value = DateStringLookup.INSTANCE.lookup(fomat);
        assertNotNull(value, "No Date");
        final SimpleDateFormat format = new SimpleDateFormat(fomat);
        final String today = format.format(new Date());
        assertEquals(value, today);

    }

}
