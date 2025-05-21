/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */

package org.apache.commons.text.lookup.external;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.lookup.FileStringLookupTest;
import org.apache.commons.text.lookup.PropertiesStringLookupTest;
import org.apache.commons.text.lookup.StringLookupFactory;
import org.junit.jupiter.api.Test;

/**
 * Tests building a fenced {@link StringSubstitutor} to exclude the use of package-private elements.
 */
public class CustomStringSubstitutorTest {

    private StringSubstitutor createStringSubstitutor() {
        final StringLookupFactory factory = StringLookupFactory.builder().setFences(Paths.get("")).get();
        return new StringSubstitutor(factory.interpolatorStringLookup());
    }

    @Test
    public void testFencedFiles() throws IOException {
        FileStringLookupTest.testFence(createStringSubstitutor());
    }

    @Test
    public void testFencedProperties() {
        PropertiesStringLookupTest.testFence(createStringSubstitutor());
    }

}
