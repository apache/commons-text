package org.apache.commons.text.similarity;

import org.apache.commons.lang3.Validate;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for class {@link SimilarityScoreFrom}.
 *
 * @date 2017-07-26
 * @see SimilarityScoreFrom
 *
 **/
public class SimilarityScoreFromTest{

  @Test
  public void testFailsToCreateSimilarityScoreFromThrowsIllegalArgumentException() {
      try {
        new SimilarityScoreFrom<Object>(null, "");
        fail("Expecting exception: IllegalArgumentException");
      } catch(IllegalArgumentException e) {
         assertEquals(Validate.class.getName(), e.getStackTrace()[0].getClassName());
      }
  }

  @Test
  public void testApply() {
      LongestCommonSubsequence longestCommonSubsequence = new LongestCommonSubsequence();
      SimilarityScoreFrom<Integer> similarityScoreFrom = new SimilarityScoreFrom(longestCommonSubsequence, "asdf");

      assertEquals(1, (int) similarityScoreFrom.apply("s"));
  }

}