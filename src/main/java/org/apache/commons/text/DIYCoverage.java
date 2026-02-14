/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.text;

import java.util.Map;
import java.util.TreeMap;

/**
 * DIY tool for tracking branch coverage during the lab assignment.
 * This class provides a simple mechanism to mark which code branches
 * are executed during test runs.
 */
public class DIYCoverage {

    /** * The map used to store coverage data where the key is the branch ID 
     * and the value indicates if it was hit.
     */
    private static final Map<String, Boolean> COVERAGE_MAP = new TreeMap<>();

    /**
     * Default constructor required for Javadoc completeness.
     */
    public DIYCoverage() {
        // No-op
    }

    /**
     * Marks a specific branch as having been executed.
     * * @param branchId A unique string identifier for the branch, e.g., "read_if_block_1".
     */
    public static void mark(final String branchId) {
        COVERAGE_MAP.put(branchId, true);
    }

    /**
     * Prints a formatted report of all branches that were hit to the standard output.
     * This should be called at the end of a test suite.
     */
    public static void printReport() {
        System.out.println("\n========== DIY COVERAGE REPORT ==========");
        System.out.println("Total Branches Hit: " + COVERAGE_MAP.size());
        System.out.println("-----------------------------------------");
        for (final String branch : COVERAGE_MAP.keySet()) {
            System.out.println("[HIT] " + branch);
        }
        System.out.println("=========================================\n");
    }

    /**
     * Clears all recorded coverage data from the map.
     * Useful for resetting state between independent tests.
     */
    public static void clear() {
        COVERAGE_MAP.clear();
    }
}