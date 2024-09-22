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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

public class StringSubstitutorOssFuzzTest {

    /**
     * Tests OSS-Fuzz issue 42522985.
     *
     * apache-commons-text:StringSubstitutorInterpolatorFuzzer: Security exception in java.base/java.util.Arrays.copyOf
     *
     * https://issues.oss-fuzz.com/issues/42522985
     */
    @Test
    public void test() throws IOException {
        final byte[] allBytes = Files.readAllBytes(
                Paths.get("src/test/resources/org/apache/commons/text/oss-fuzz/clusterfuzz-testcase-StringSubstitutorInterpolatorFuzzer-5149898315268096"));
        StringSubstitutor.createInterpolator().replace(new String(allBytes, StandardCharsets.UTF_8));
    }

    /**
     * Tests OSS-Fuzz issue 42527553.
     *
     * apache-commons-text:StringSubstitutorInterpolatorFuzzer: Security exception in java.base/java.util.Arrays.copyOf
     *
     * https://issues.oss-fuzz.com/issues/42527553
     */
    @Test
    public void test42527553() {
        StringSubstitutor.createInterpolator().replace("${date:swswswswsws\177sw\001\000swswswswswwswsswswswsws\177sw\001\000swswswsswswswswswswswswswswsws}");
    }
}
