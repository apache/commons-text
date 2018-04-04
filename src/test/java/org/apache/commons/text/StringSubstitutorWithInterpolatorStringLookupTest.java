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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.text.lookup.StringLookupFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringSubstitutorWithInterpolatorStringLookupTest {

    @Test
    public void testLocalHostLookup_Address() throws UnknownHostException {
        final StringSubstitutor strSubst = new StringSubstitutor(
                StringLookupFactory.INSTANCE.interpolatorStringLookup());
        Assertions.assertEquals(InetAddress.getLocalHost().getHostAddress(), strSubst.replace("${localhost:address}"));
    }

    @Test
    public void testLocalHostLookup_CanonicalName() throws UnknownHostException {
        final StringSubstitutor strSubst = new StringSubstitutor(
                StringLookupFactory.INSTANCE.interpolatorStringLookup());
        Assertions.assertEquals(InetAddress.getLocalHost().getCanonicalHostName(),
                strSubst.replace("${localhost:canonical-name}"));
    }

    @Test
    public void testLocalHostLookup_Name() throws UnknownHostException {
        final StringSubstitutor strSubst = new StringSubstitutor(
                StringLookupFactory.INSTANCE.interpolatorStringLookup());
        Assertions.assertEquals(InetAddress.getLocalHost().getHostName(), strSubst.replace("${localhost:name}"));
    }

    @Test
    public void testMapAndSystemProperty() {
        final String key = "key";
        final String value = "value";
        final Map<String, String> map = new HashMap<>();
        map.put(key, value);
        final StringSubstitutor strSubst = new StringSubstitutor(
                StringLookupFactory.INSTANCE.interpolatorStringLookup(map));
        final String spKey = "user.name";
        Assertions.assertEquals(System.getProperty(spKey), strSubst.replace("${sys:" + spKey + "}"));
        Assertions.assertEquals(value, strSubst.replace("${" + key + "}"));
    }

    @Test
    public void testSystemProperty() {
        final StringSubstitutor strSubst = new StringSubstitutor(
                StringLookupFactory.INSTANCE.interpolatorStringLookup());
        final String spKey = "user.name";
        Assertions.assertEquals(System.getProperty(spKey), strSubst.replace("${sys:" + spKey + "}"));
    }
}
