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

  @Test(expected = IllegalArgumentException.class)
  public void testCosineSimilarityThrowsIllegalArgumentException() {
    CosineSimilarity cosineSimilarity = new CosineSimilarity();
    Map<CharSequence, Integer> map = new HashMap();
    cosineSimilarity.cosineSimilarity(map, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCosineSimilarityWithNull() {
     CosineSimilarity cosineSimilarity = new CosineSimilarity();
     cosineSimilarity.cosineSimilarity(null,null);
  }

  @Test
  public void testCosineSimilarityReturningDoubleWhereByteValueIsZero() {
      CosineSimilarity cosineSimilarity = new CosineSimilarity();
      Map<String, Integer> hashMap = new HashMap();
      Map<CharSequence, Integer> hashMapTwo = new HashMap(hashMap);

      assertEquals(0.0, (double) cosineSimilarity.cosineSimilarity(hashMapTwo, hashMapTwo), 0.01);
  }

}