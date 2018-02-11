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

/**
 * Always returns null.
 *
 * @since 1.3
 */
final class NullStringLookup extends AbstractStringLookup {

    /**
     * Defines the singleton for this class.
     */
    public static final StringLookup INSTANCE = new NullStringLookup();

    /**
     * No need to build instances for now.
     */
    private NullStringLookup() {
        // empty
    }

    /**
     * Always returns null.
     */
    @Override
    public String lookup(final String key) {
        return null;
    }

}
