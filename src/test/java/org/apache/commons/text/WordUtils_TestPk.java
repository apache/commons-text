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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.lang3.StringUtils;

import org.junit.jupiter.api.Test;

public class WordUtils_TestPk {

 


   //Testing empty string and no deliminitor
    @Test
    public void testCapitalizePartitionEmptyString() {
        //setup

       String str="";
       char[] delimiters ={};
       
       //test
       String result= WordUtils.capitalize(str, delimiters);

       //verify

        assertEquals("", result);
    }
  
    // testing single lower case string
    @Test 
    public void testCapitalizePartitionLowerCaseString(){
        //set up
        String str= "hello";
        char[] delimiters ={};

        //test
        String result= WordUtils.capitalize(str, delimiters);

        //verify

        assertEquals("Hello", result);
    }
      //testing multiple lower case string with no deliminator
    @Test 
    public void testCapitalizePartitionMultipleLowerCaseString(){
        //set up
        String str= "hello world";
        char[] delimiters ={};

        //test
        String result= WordUtils.capitalize(str, delimiters);

        //verify

        assertEquals("Hello world", result);
    }
     // multiple lower case string with a space deliminiter
    @Test 
    public void testCapitalizePartitionMultipleLowerCaseStringWithDeli(){
        //set up
        String str= "hello world";
        char[] delimiters ={' '};

        //test
        String result= WordUtils.capitalize(str, delimiters);

        //verify

        assertEquals("Hello World", result);
    }
       // words already capitalized with a space deliminiter
       @Test 
       public void testCapitalizePartitionUpperCaseStringWithDeli(){
           //set up
           String str= "Hello World";
           char[] delimiters ={' '};
   
           //test
           String result= WordUtils.capitalize(str, delimiters);
   
           //verify
   
           assertEquals("Hello World", result);
       }
       // words all capitalized with a multiple deliminiter
       @Test 
       public void testCapitalizePartitionStringWithMultipleDeli(){
           //set up
           String str= "hello-world";
           char[] delimiters ={' ','-'};
   
           //test
           String result= WordUtils.capitalize(str, delimiters);
   
           //verify
   
           assertEquals("Hello-World", result);
       }
       // words with special character as deliminiter
       @Test 
       public void testCapitalizePartitionWithSpecialDeli(){
           //set up
           String str= "hello@world";
           char[] delimiters ={' '};
   
           //test
           String result= WordUtils.capitalize(str, delimiters);
   
           //verify
   
           assertEquals("Hello@world", result);
       }
       // words already capitalized with a space deliminiter
       @Test 
       public void testCapitalizePartitionAllUpperCaseWithDeli(){
           //set up
           String str= "HELLO WORLD";
           char[] delimiters ={' '};
   
           //test
           String result= WordUtils.capitalize(str, delimiters);
   
           //verify
   
           assertEquals("HELLO WORLD", result);
       }
       // words already capitalized with a space deliminiter
       @Test 
       public void testCapitalizePartitionStringWithNumbersWithDeli(){
           //set up
           String str= "Hello World";
           char[] delimiters ={' '};
   
           //test
           String result= WordUtils.capitalize(str, delimiters);
   
           //verify
   
           assertEquals("Hello World", result);
       }
       // words with a space deliminiter at the start
       @Test 
       public void testCapitalizePartitionWithDeliAtStart(){
           //set up
           String str= " hello";
           char[] delimiters ={' '};
   
           //test
           String result= WordUtils.capitalize(str, delimiters);
   
           //verify
   
           assertEquals(" Hello", result);
       }
       // words with a space deliminiter at the end
       @Test 
       public void testCapitalizePartitionWithDeliAtEnd(){
           //set up
           String str= "hello ";
           char[] delimiters ={' '};
   
           //test
           String result= WordUtils.capitalize(str, delimiters);
   
           //verify
   
           assertEquals("Hello ", result);
       }
       // lower case word only first letter is capital with no deliminiter
       @Test 
       public void testCapitalizePartitionNoDeliminiters(){
           //set up
           String str= "hello world";
           char[] delimiters ={};
   
           //test
           String result= WordUtils.capitalize(str, delimiters);
   
           //verify
   
           assertEquals("Hello world", result);
       }
}
