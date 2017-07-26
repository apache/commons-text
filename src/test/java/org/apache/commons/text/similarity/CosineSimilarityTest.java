package org.apache.commons.text.similarity;

import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for class {@link CosineSimilarity}.
 *
 * @date 2017-07-26
 * @see CosineSimilarity
 *
 **/
public class CosineSimilarityTest{

  @Test
  public void testCosineSimilarityWithNonEmptyMap() {
      CosineSimilarity cosineSimilarity = new CosineSimilarity();
      Map<String, Integer> hashMap = new HashMap();
      Integer integer = new Integer((-397));
      hashMap.put("3J/$3.L", integer);
      Map<CharSequence, Integer> hashMapTwo = new HashMap(hashMap);
      Map<CharSequence, Integer> hashMapThree = new HashMap();

      assertEquals(0.0, (double) cosineSimilarity.cosineSimilarity(hashMapTwo, hashMapThree), 0.01);
  }

  @Test
  public void testCosineSimilarityThrowsIllegalArgumentException() {
      CosineSimilarity cosineSimilarity = new CosineSimilarity();
      Map<CharSequence, Integer> map = new HashMap();

      try { 
        cosineSimilarity.cosineSimilarity(map, null);
        fail("Expecting exception: IllegalArgumentException");
      } catch(IllegalArgumentException e) {
         assertEquals(CosineSimilarity.class.getName(), e.getStackTrace()[0].getClassName());
      }
  }

  @Test
  public void testCosineSimilarityWithNull() {
      CosineSimilarity cosineSimilarity = new CosineSimilarity();

      try { 
        cosineSimilarity.cosineSimilarity(null,null);
        fail("Expecting exception: IllegalArgumentException");
      } catch(IllegalArgumentException e) {
         assertEquals(CosineSimilarity.class.getName(), e.getStackTrace()[0].getClassName());
      }
  }

  @Test
  public void testCosineSimilarityReturningDoubleWhereByteValueIsZero() {
      CosineSimilarity cosineSimilarity = new CosineSimilarity();
      Map<String, Integer> hashMap = new HashMap();
      Map<CharSequence, Integer> hashMapTwo = new HashMap(hashMap);

      assertEquals(0.0, (double) cosineSimilarity.cosineSimilarity(hashMapTwo, hashMapTwo), 0.01);
  }

}