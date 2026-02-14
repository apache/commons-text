package org.apache.commons.text;

import java.util.Map;
import java.util.TreeMap;

public class DIYCoverage {
    // We use a Map to store which branches were hit.
    // Key = "MethodName_BranchID", Value = true (hit)
    private static final Map<String, Boolean> coverageMap = new TreeMap<>();

    /**
     * Call this method inside every 'if', 'else' or 'case' block.
     * @param branchId A unique name for the branch, e.g., "read_branch_1"
     */
    public static void mark(String branchId) {
        coverageMap.put(branchId, true);
    }

    /**
     * Call this at the end of your test to see the results.
     */
    public static void printReport() {
        System.out.println("\n-------- DIY COVERAGE REPORT ----------");
        System.out.println("Total Branches Hit: " + coverageMap.size());
        System.out.println("-----------------------------------------");
        for (String branch : coverageMap.keySet()) {
            System.out.println("[HIT] " + branch);
        }
    }

    /**
     * Clear the map between tests if needed.
     */
    public static void clear() {
        coverageMap.clear();
    }
}