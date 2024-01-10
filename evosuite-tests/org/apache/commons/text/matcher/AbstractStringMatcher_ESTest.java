/*
 * This file was automatically generated by EvoSuite
 * Wed Jan 10 18:16:36 GMT 2024
 */

package org.apache.commons.text.matcher;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import org.apache.commons.text.matcher.AbstractStringMatcher;
import org.apache.commons.text.matcher.StringMatcher;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class AbstractStringMatcher_ESTest extends AbstractStringMatcher_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void test00()  throws Throwable  {
      AbstractStringMatcher.TrimMatcher abstractStringMatcher_TrimMatcher0 = new AbstractStringMatcher.TrimMatcher();
      char[] charArray0 = new char[4];
      charArray0[0] = ' ';
      int int0 = abstractStringMatcher_TrimMatcher0.isMatch(charArray0, 0, 0, (-1));
      assertEquals(1, int0);
  }

  @Test(timeout = 4000)
  public void test01()  throws Throwable  {
      char[] charArray0 = new char[4];
      charArray0[0] = '9';
      charArray0[1] = 'H';
      charArray0[3] = '<';
      AbstractStringMatcher.CharSetMatcher abstractStringMatcher_CharSetMatcher0 = new AbstractStringMatcher.CharSetMatcher(charArray0);
      CharBuffer charBuffer0 = CharBuffer.wrap(charArray0);
      StringMatcher[] stringMatcherArray0 = new StringMatcher[3];
      stringMatcherArray0[0] = (StringMatcher) abstractStringMatcher_CharSetMatcher0;
      stringMatcherArray0[1] = (StringMatcher) abstractStringMatcher_CharSetMatcher0;
      AbstractStringMatcher.AndStringMatcher abstractStringMatcher_AndStringMatcher0 = new AbstractStringMatcher.AndStringMatcher(stringMatcherArray0);
      int int0 = abstractStringMatcher_AndStringMatcher0.isMatch((CharSequence) charBuffer0, 1, (-655), 123);
      assertEquals(2, int0);
  }

  @Test(timeout = 4000)
  public void test02()  throws Throwable  {
      StringMatcher[] stringMatcherArray0 = new StringMatcher[1];
      AbstractStringMatcher.CharMatcher abstractStringMatcher_CharMatcher0 = new AbstractStringMatcher.CharMatcher('-');
      stringMatcherArray0[0] = (StringMatcher) abstractStringMatcher_CharMatcher0;
      AbstractStringMatcher.AndStringMatcher abstractStringMatcher_AndStringMatcher0 = new AbstractStringMatcher.AndStringMatcher(stringMatcherArray0);
      char[] charArray0 = new char[4];
      charArray0[0] = 'y';
      int int0 = abstractStringMatcher_AndStringMatcher0.isMatch(charArray0, 0, 1, 1);
      assertEquals(0, int0);
  }

  @Test(timeout = 4000)
  public void test03()  throws Throwable  {
      char[] charArray0 = new char[2];
      charArray0[0] = 'p';
      AbstractStringMatcher.CharArrayMatcher abstractStringMatcher_CharArrayMatcher0 = new AbstractStringMatcher.CharArrayMatcher(charArray0);
      int int0 = abstractStringMatcher_CharArrayMatcher0.isMatch((CharSequence) "org.apache.commons.text.matcher.AbstractStringMatcher$CharSetMatcher@2[W, p]", 8);
      assertEquals(0, int0);
  }

  @Test(timeout = 4000)
  public void test04()  throws Throwable  {
      char[] charArray0 = new char[1];
      AbstractStringMatcher.CharArrayMatcher abstractStringMatcher_CharArrayMatcher0 = new AbstractStringMatcher.CharArrayMatcher(charArray0);
      // Undeclared exception!
      try { 
        abstractStringMatcher_CharArrayMatcher0.isMatch((CharSequence) null, (-3398), (-3398), (-2838));
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("org.apache.commons.text.matcher.AbstractStringMatcher$CharArrayMatcher", e);
      }
  }

  @Test(timeout = 4000)
  public void test05()  throws Throwable  {
      char[] charArray0 = new char[0];
      AbstractStringMatcher.CharArrayMatcher abstractStringMatcher_CharArrayMatcher0 = new AbstractStringMatcher.CharArrayMatcher(charArray0);
      int int0 = abstractStringMatcher_CharArrayMatcher0.isMatch(charArray0, 1, 1, 1);
      assertEquals(0, int0);
  }

  @Test(timeout = 4000)
  public void test06()  throws Throwable  {
      char[] charArray0 = new char[5];
      charArray0[2] = 'E';
      AbstractStringMatcher.CharArrayMatcher abstractStringMatcher_CharArrayMatcher0 = new AbstractStringMatcher.CharArrayMatcher(charArray0);
      int int0 = abstractStringMatcher_CharArrayMatcher0.isMatch(charArray0, 2, 39, 39);
      assertEquals(0, int0);
  }

  @Test(timeout = 4000)
  public void test07()  throws Throwable  {
      char[] charArray0 = new char[4];
      AbstractStringMatcher.CharSetMatcher abstractStringMatcher_CharSetMatcher0 = new AbstractStringMatcher.CharSetMatcher(charArray0);
      StringMatcher[] stringMatcherArray0 = new StringMatcher[6];
      stringMatcherArray0[0] = (StringMatcher) abstractStringMatcher_CharSetMatcher0;
      stringMatcherArray0[1] = (StringMatcher) abstractStringMatcher_CharSetMatcher0;
      AbstractStringMatcher.AndStringMatcher abstractStringMatcher_AndStringMatcher0 = new AbstractStringMatcher.AndStringMatcher(stringMatcherArray0);
      int int0 = abstractStringMatcher_AndStringMatcher0.isMatch(charArray0, 0, 0, 1);
      assertEquals(2, int0);
  }

  @Test(timeout = 4000)
  public void test08()  throws Throwable  {
      StringMatcher[] stringMatcherArray0 = new StringMatcher[1];
      AbstractStringMatcher.CharMatcher abstractStringMatcher_CharMatcher0 = new AbstractStringMatcher.CharMatcher('-');
      stringMatcherArray0[0] = (StringMatcher) abstractStringMatcher_CharMatcher0;
      AbstractStringMatcher.AndStringMatcher abstractStringMatcher_AndStringMatcher0 = new AbstractStringMatcher.AndStringMatcher(stringMatcherArray0);
      char[] charArray0 = new char[2];
      charArray0[1] = '-';
      int int0 = abstractStringMatcher_AndStringMatcher0.isMatch(charArray0, 1, 1, 32);
      assertEquals(1, int0);
  }

  @Test(timeout = 4000)
  public void test09()  throws Throwable  {
      char[] charArray0 = new char[2];
      AbstractStringMatcher.CharArrayMatcher abstractStringMatcher_CharArrayMatcher0 = new AbstractStringMatcher.CharArrayMatcher(charArray0);
      AbstractStringMatcher.CharSetMatcher abstractStringMatcher_CharSetMatcher0 = new AbstractStringMatcher.CharSetMatcher(charArray0);
      StringMatcher[] stringMatcherArray0 = new StringMatcher[2];
      stringMatcherArray0[0] = (StringMatcher) abstractStringMatcher_CharSetMatcher0;
      stringMatcherArray0[1] = (StringMatcher) abstractStringMatcher_CharArrayMatcher0;
      AbstractStringMatcher.AndStringMatcher abstractStringMatcher_AndStringMatcher0 = new AbstractStringMatcher.AndStringMatcher(stringMatcherArray0);
      int int0 = abstractStringMatcher_AndStringMatcher0.isMatch(charArray0, 0, 2, (-2413));
      assertEquals(0, int0);
  }

  @Test(timeout = 4000)
  public void test10()  throws Throwable  {
      AbstractStringMatcher.TrimMatcher abstractStringMatcher_TrimMatcher0 = new AbstractStringMatcher.TrimMatcher();
      int int0 = abstractStringMatcher_TrimMatcher0.size();
      assertEquals(1, int0);
  }

  @Test(timeout = 4000)
  public void test11()  throws Throwable  {
      char[] charArray0 = new char[9];
      AbstractStringMatcher.NoneMatcher abstractStringMatcher_NoneMatcher0 = new AbstractStringMatcher.NoneMatcher();
      int int0 = abstractStringMatcher_NoneMatcher0.isMatch(charArray0, 1, 483, (-2371));
      assertEquals(0, int0);
  }

  @Test(timeout = 4000)
  public void test12()  throws Throwable  {
      AbstractStringMatcher.CharMatcher abstractStringMatcher_CharMatcher0 = new AbstractStringMatcher.CharMatcher('g');
      int int0 = abstractStringMatcher_CharMatcher0.size();
      assertEquals(1, int0);
  }

  @Test(timeout = 4000)
  public void test13()  throws Throwable  {
      char[] charArray0 = new char[2];
      AbstractStringMatcher.CharMatcher abstractStringMatcher_CharMatcher0 = new AbstractStringMatcher.CharMatcher('W');
      int int0 = abstractStringMatcher_CharMatcher0.isMatch(charArray0, 0, 756, 2);
      assertEquals(0, int0);
  }

  @Test(timeout = 4000)
  public void test14()  throws Throwable  {
      char[] charArray0 = new char[9];
      AbstractStringMatcher.CharMatcher abstractStringMatcher_CharMatcher0 = new AbstractStringMatcher.CharMatcher('c');
      CharBuffer charBuffer0 = CharBuffer.wrap(charArray0);
      int int0 = abstractStringMatcher_CharMatcher0.isMatch((CharSequence) charBuffer0, 1, 0, 0);
      assertEquals(0, int0);
  }

  @Test(timeout = 4000)
  public void test15()  throws Throwable  {
      AbstractStringMatcher.TrimMatcher abstractStringMatcher_TrimMatcher0 = new AbstractStringMatcher.TrimMatcher();
      char[] charArray0 = new char[2];
      charArray0[0] = '3';
      int int0 = abstractStringMatcher_TrimMatcher0.isMatch(charArray0, 0, (-1252), 0);
      assertEquals(0, int0);
  }

  @Test(timeout = 4000)
  public void test16()  throws Throwable  {
      AbstractStringMatcher.TrimMatcher abstractStringMatcher_TrimMatcher0 = new AbstractStringMatcher.TrimMatcher();
      CharBuffer charBuffer0 = CharBuffer.allocate(3648);
      int int0 = abstractStringMatcher_TrimMatcher0.isMatch((CharSequence) charBuffer0, 0, 0, 4097);
      assertEquals(1, int0);
  }

  @Test(timeout = 4000)
  public void test17()  throws Throwable  {
      char[] charArray0 = new char[4];
      charArray0[0] = '9';
      CharBuffer charBuffer0 = CharBuffer.wrap(charArray0);
      AbstractStringMatcher.TrimMatcher abstractStringMatcher_TrimMatcher0 = new AbstractStringMatcher.TrimMatcher();
      int int0 = abstractStringMatcher_TrimMatcher0.isMatch((CharSequence) charBuffer0, 0, (-655), (-1));
      assertEquals(0, int0);
  }

  @Test(timeout = 4000)
  public void test18()  throws Throwable  {
      char[] charArray0 = new char[4];
      AbstractStringMatcher.CharSetMatcher abstractStringMatcher_CharSetMatcher0 = new AbstractStringMatcher.CharSetMatcher(charArray0);
      CharBuffer charBuffer0 = CharBuffer.wrap(charArray0);
      int int0 = abstractStringMatcher_CharSetMatcher0.isMatch((CharSequence) charBuffer0, 0, 0, (-655));
      assertEquals(1, int0);
  }

  @Test(timeout = 4000)
  public void test19()  throws Throwable  {
      char[] charArray0 = new char[0];
      AbstractStringMatcher.CharSetMatcher abstractStringMatcher_CharSetMatcher0 = new AbstractStringMatcher.CharSetMatcher(charArray0);
      int int0 = abstractStringMatcher_CharSetMatcher0.isMatch((CharSequence) "org.apache.commons.text.matcher.AbstractStringMatcher$CharSetMatcher@2[]", 9);
      assertEquals(0, int0);
  }

  @Test(timeout = 4000)
  public void test20()  throws Throwable  {
      char[] charArray0 = new char[6];
      charArray0[0] = 'h';
      charArray0[1] = 'h';
      charArray0[2] = '5';
      charArray0[3] = 'P';
      charArray0[4] = '4';
      charArray0[5] = 'l';
      AbstractStringMatcher.CharSetMatcher abstractStringMatcher_CharSetMatcher0 = new AbstractStringMatcher.CharSetMatcher(charArray0);
      char[] charArray1 = new char[8];
      int int0 = abstractStringMatcher_CharSetMatcher0.isMatch(charArray1, 0, 34, 0);
      assertEquals(0, int0);
  }

  @Test(timeout = 4000)
  public void test21()  throws Throwable  {
      StringMatcher[] stringMatcherArray0 = new StringMatcher[13];
      AbstractStringMatcher.CharMatcher abstractStringMatcher_CharMatcher0 = new AbstractStringMatcher.CharMatcher('-');
      stringMatcherArray0[0] = (StringMatcher) abstractStringMatcher_CharMatcher0;
      AbstractStringMatcher.AndStringMatcher abstractStringMatcher_AndStringMatcher0 = new AbstractStringMatcher.AndStringMatcher(stringMatcherArray0);
      int int0 = abstractStringMatcher_AndStringMatcher0.isMatch((CharSequence) "org.apache.commons.text.matcher.AbstractStringMatcher$CharMatcher@1['-']", 1, 1, 1);
      assertEquals(0, int0);
  }

  @Test(timeout = 4000)
  public void test22()  throws Throwable  {
      char[] charArray0 = new char[1];
      AbstractStringMatcher.CharArrayMatcher abstractStringMatcher_CharArrayMatcher0 = new AbstractStringMatcher.CharArrayMatcher(charArray0);
      int int0 = abstractStringMatcher_CharArrayMatcher0.isMatch((CharSequence) "org.apache.commons.text.matcher.AbstractStringMatcher$CharArrayMatcher@2[\"\u0000\"]", 0);
      assertEquals(0, int0);
  }

  @Test(timeout = 4000)
  public void test23()  throws Throwable  {
      char[] charArray0 = new char[4];
      CharBuffer charBuffer0 = CharBuffer.wrap(charArray0);
      AbstractStringMatcher.CharArrayMatcher abstractStringMatcher_CharArrayMatcher0 = new AbstractStringMatcher.CharArrayMatcher(charArray0);
      int int0 = abstractStringMatcher_CharArrayMatcher0.isMatch((CharSequence) charBuffer0, 0, 4047, 111);
      assertEquals(4, int0);
  }

  @Test(timeout = 4000)
  public void test24()  throws Throwable  {
      char[] charArray0 = new char[3];
      AbstractStringMatcher.CharArrayMatcher abstractStringMatcher_CharArrayMatcher0 = new AbstractStringMatcher.CharArrayMatcher(charArray0);
      int int0 = abstractStringMatcher_CharArrayMatcher0.isMatch((CharSequence) null, 1393, 1393, 1393);
      assertEquals(0, int0);
  }

  @Test(timeout = 4000)
  public void test25()  throws Throwable  {
      char[] charArray0 = new char[3];
      charArray0[0] = '%';
      AbstractStringMatcher.CharArrayMatcher abstractStringMatcher_CharArrayMatcher0 = new AbstractStringMatcher.CharArrayMatcher(charArray0);
      StringMatcher[] stringMatcherArray0 = new StringMatcher[1];
      stringMatcherArray0[0] = (StringMatcher) abstractStringMatcher_CharArrayMatcher0;
      AbstractStringMatcher.AndStringMatcher abstractStringMatcher_AndStringMatcher0 = new AbstractStringMatcher.AndStringMatcher(stringMatcherArray0);
      int int0 = abstractStringMatcher_AndStringMatcher0.isMatch(charArray0, 1, 758, 875);
      assertEquals(0, int0);
  }

  @Test(timeout = 4000)
  public void test26()  throws Throwable  {
      char[] charArray0 = new char[3];
      AbstractStringMatcher.CharArrayMatcher abstractStringMatcher_CharArrayMatcher0 = new AbstractStringMatcher.CharArrayMatcher(charArray0);
      StringMatcher[] stringMatcherArray0 = new StringMatcher[1];
      stringMatcherArray0[0] = (StringMatcher) abstractStringMatcher_CharArrayMatcher0;
      AbstractStringMatcher.AndStringMatcher abstractStringMatcher_AndStringMatcher0 = new AbstractStringMatcher.AndStringMatcher(stringMatcherArray0);
      int int0 = abstractStringMatcher_AndStringMatcher0.isMatch(charArray0, 0, 1393, 1393);
      assertEquals(3, int0);
  }

  @Test(timeout = 4000)
  public void test27()  throws Throwable  {
      StringMatcher[] stringMatcherArray0 = new StringMatcher[1];
      AbstractStringMatcher.CharMatcher abstractStringMatcher_CharMatcher0 = new AbstractStringMatcher.CharMatcher('-');
      stringMatcherArray0[0] = (StringMatcher) abstractStringMatcher_CharMatcher0;
      AbstractStringMatcher.AndStringMatcher abstractStringMatcher_AndStringMatcher0 = new AbstractStringMatcher.AndStringMatcher(stringMatcherArray0);
      int int0 = abstractStringMatcher_AndStringMatcher0.isMatch((CharSequence) "org.apache.commons.text.matcher.AbstractStringMatcher$CharArrayMatcher@3[\"----\"]", 76);
      assertEquals(1, int0);
  }

  @Test(timeout = 4000)
  public void test28()  throws Throwable  {
      AbstractStringMatcher.NoneMatcher abstractStringMatcher_NoneMatcher0 = new AbstractStringMatcher.NoneMatcher();
      StringMatcher[] stringMatcherArray0 = new StringMatcher[1];
      stringMatcherArray0[0] = (StringMatcher) abstractStringMatcher_NoneMatcher0;
      AbstractStringMatcher.AndStringMatcher abstractStringMatcher_AndStringMatcher0 = new AbstractStringMatcher.AndStringMatcher(stringMatcherArray0);
      int int0 = abstractStringMatcher_AndStringMatcher0.size();
      assertEquals(0, int0);
  }

  @Test(timeout = 4000)
  public void test29()  throws Throwable  {
      StringMatcher[] stringMatcherArray0 = new StringMatcher[1];
      AbstractStringMatcher.NoneMatcher abstractStringMatcher_NoneMatcher0 = new AbstractStringMatcher.NoneMatcher();
      stringMatcherArray0[0] = (StringMatcher) abstractStringMatcher_NoneMatcher0;
      AbstractStringMatcher.AndStringMatcher abstractStringMatcher_AndStringMatcher0 = new AbstractStringMatcher.AndStringMatcher(stringMatcherArray0);
      char[] charArray0 = new char[0];
      CharBuffer charBuffer0 = CharBuffer.wrap(charArray0);
      int int0 = abstractStringMatcher_AndStringMatcher0.isMatch((CharSequence) charBuffer0, 0, 219, 0);
      assertEquals(0, int0);
  }

  @Test(timeout = 4000)
  public void test30()  throws Throwable  {
      StringMatcher[] stringMatcherArray0 = new StringMatcher[3];
      AbstractStringMatcher.TrimMatcher abstractStringMatcher_TrimMatcher0 = new AbstractStringMatcher.TrimMatcher();
      stringMatcherArray0[1] = (StringMatcher) abstractStringMatcher_TrimMatcher0;
      AbstractStringMatcher.AndStringMatcher abstractStringMatcher_AndStringMatcher0 = new AbstractStringMatcher.AndStringMatcher(stringMatcherArray0);
      int int0 = abstractStringMatcher_AndStringMatcher0.size();
      assertEquals(1, int0);
  }

  @Test(timeout = 4000)
  public void test31()  throws Throwable  {
      AbstractStringMatcher.TrimMatcher abstractStringMatcher_TrimMatcher0 = new AbstractStringMatcher.TrimMatcher();
      char[] charArray0 = new char[4];
      int int0 = abstractStringMatcher_TrimMatcher0.isMatch(charArray0, 0, 0, (-1));
      assertEquals(1, int0);
  }

  @Test(timeout = 4000)
  public void test32()  throws Throwable  {
      AbstractStringMatcher.CharMatcher abstractStringMatcher_CharMatcher0 = new AbstractStringMatcher.CharMatcher('-');
      String string0 = abstractStringMatcher_CharMatcher0.toString();
      assertNotNull(string0);
  }

  @Test(timeout = 4000)
  public void test33()  throws Throwable  {
      char[] charArray0 = new char[0];
      AbstractStringMatcher.CharArrayMatcher abstractStringMatcher_CharArrayMatcher0 = new AbstractStringMatcher.CharArrayMatcher(charArray0);
      StringMatcher[] stringMatcherArray0 = new StringMatcher[2];
      stringMatcherArray0[0] = (StringMatcher) abstractStringMatcher_CharArrayMatcher0;
      AbstractStringMatcher.AndStringMatcher abstractStringMatcher_AndStringMatcher0 = new AbstractStringMatcher.AndStringMatcher(stringMatcherArray0);
      int int0 = abstractStringMatcher_AndStringMatcher0.isMatch((CharSequence) null, 46, 46, 46);
      assertEquals(0, int0);
  }

  @Test(timeout = 4000)
  public void test34()  throws Throwable  {
      char[] charArray0 = new char[0];
      AbstractStringMatcher.CharArrayMatcher abstractStringMatcher_CharArrayMatcher0 = new AbstractStringMatcher.CharArrayMatcher(charArray0);
      String string0 = abstractStringMatcher_CharArrayMatcher0.toString();
      assertNotNull(string0);
  }

  @Test(timeout = 4000)
  public void test35()  throws Throwable  {
      char[] charArray0 = new char[3];
      AbstractStringMatcher.CharSetMatcher abstractStringMatcher_CharSetMatcher0 = new AbstractStringMatcher.CharSetMatcher(charArray0);
      String string0 = abstractStringMatcher_CharSetMatcher0.toString();
      assertNotNull(string0);
  }

  @Test(timeout = 4000)
  public void test36()  throws Throwable  {
      char[] charArray0 = new char[0];
      StringMatcher[] stringMatcherArray0 = new StringMatcher[3];
      AbstractStringMatcher.CharSetMatcher abstractStringMatcher_CharSetMatcher0 = new AbstractStringMatcher.CharSetMatcher(charArray0);
      stringMatcherArray0[0] = (StringMatcher) abstractStringMatcher_CharSetMatcher0;
      AbstractStringMatcher.AndStringMatcher abstractStringMatcher_AndStringMatcher0 = new AbstractStringMatcher.AndStringMatcher(stringMatcherArray0);
      int int0 = abstractStringMatcher_AndStringMatcher0.size();
      assertEquals(1, int0);
  }
}
