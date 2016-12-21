package org.apache.commons.text.similarity;

import org.junit.Test;

/**
 * Created by tompkicr on 12/20/16.
 */
public class LongestCommonSubsequenceTest {

    @Test
    public void testLongestCommonSubsequence() {
        LongestCommonSubsequence lcs = new LongestCommonSubsequence();
        CharSequence retVal = lcs.logestCommonSubsequence("abab","abba");
        System.out.println("testLongestCommonSubstring:");
        System.out.println("retVal: " + retVal);
        for (int n=0; n < retVal.length(); n++){
            System.out.print("isISOControl: " + Character.isISOControl(retVal.charAt(n)) + " ");
            System.out.println(retVal.charAt(n));
        }
    }

    @Test
    public void testLcsLengthArray() {
        LongestCommonSubsequence lcs = new LongestCommonSubsequence();
        String left = "abab";
        String right = "abba";
        int[][] retVal = lcs.longestCommonSubstringLengthArray(left, right);
        System.out.println("testLcsLengthArray:");
        for (int i=0; i <= left.length(); i++){
            for (int j=0; j <= right.length(); j++){
                System.out.print(retVal[i][j] + " ");
            }
            System.out.print("\n");
        }
    }
}
