/*
 * This file was automatically generated by EvoSuite
 * Wed Jan 10 17:45:30 GMT 2024
 */

package org.apache.commons.text;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.text.DateFormat;
import java.text.Format;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.text.ExtendedMessageFormat;
import org.apache.commons.text.FormatFactory;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.text.MockDateFormat;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class ExtendedMessageFormat_ESTest extends ExtendedMessageFormat_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void test00()  throws Throwable  {
      HashMap<String, FormatFactory> hashMap0 = new HashMap<String, FormatFactory>();
      ExtendedMessageFormat extendedMessageFormat0 = null;
      try {
        extendedMessageFormat0 = new ExtendedMessageFormat("^n=JEl6{OxsG%t(EH#", hashMap0);
        fail("Expecting exception: IllegalArgumentException");
      
      } catch(IllegalArgumentException e) {
         //
         // Invalid format argument index at position 8: 
         //
         verifyException("org.apache.commons.text.ExtendedMessageFormat", e);
      }
  }

  @Test(timeout = 4000)
  public void test01()  throws Throwable  {
      HashMap<String, FormatFactory> hashMap0 = new HashMap<String, FormatFactory>();
      FormatFactory formatFactory0 = mock(FormatFactory.class, new ViolatedAssumptionAnswer());
      hashMap0.put("I$#IX y\" ", formatFactory0);
      ExtendedMessageFormat extendedMessageFormat0 = new ExtendedMessageFormat("", hashMap0);
      extendedMessageFormat0.hashCode();
  }

  @Test(timeout = 4000)
  public void test02()  throws Throwable  {
      HashMap<String, FormatFactory> hashMap0 = new HashMap<String, FormatFactory>();
      ExtendedMessageFormat extendedMessageFormat0 = new ExtendedMessageFormat("gC%", hashMap0);
      String string0 = extendedMessageFormat0.toPattern();
      assertEquals("gC%", string0);
  }

  @Test(timeout = 4000)
  public void test03()  throws Throwable  {
      ExtendedMessageFormat extendedMessageFormat0 = new ExtendedMessageFormat("");
      // Undeclared exception!
      try { 
        extendedMessageFormat0.applyPattern((String) null);
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
         //
         // no message in exception (getMessage() returned null)
         //
      }
  }

  @Test(timeout = 4000)
  public void test04()  throws Throwable  {
      ExtendedMessageFormat extendedMessageFormat0 = new ExtendedMessageFormat("");
      // Undeclared exception!
      try { 
        extendedMessageFormat0.applyPattern("rV{ n(Hbf^Sp;YT:WbH");
        fail("Expecting exception: IllegalArgumentException");
      
      } catch(IllegalArgumentException e) {
         //
         // Unmatched braces in the pattern.
         //
         verifyException("java.text.MessageFormat", e);
      }
  }

  @Test(timeout = 4000)
  public void test05()  throws Throwable  {
      HashMap<String, FormatFactory> hashMap0 = new HashMap<String, FormatFactory>();
      ExtendedMessageFormat extendedMessageFormat0 = new ExtendedMessageFormat(" _H_[", hashMap0);
      // Undeclared exception!
      try { 
        extendedMessageFormat0.applyPattern("-N8ek&b[TzL^Ad9{");
        fail("Expecting exception: ArrayIndexOutOfBoundsException");
      
      } catch(ArrayIndexOutOfBoundsException e) {
         //
         // no message in exception (getMessage() returned null)
         //
      }
  }

  @Test(timeout = 4000)
  public void test06()  throws Throwable  {
      HashMap<String, FormatFactory> hashMap0 = new HashMap<String, FormatFactory>();
      ExtendedMessageFormat extendedMessageFormat0 = null;
      try {
        extendedMessageFormat0 = new ExtendedMessageFormat((String) null, hashMap0);
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
         //
         // no message in exception (getMessage() returned null)
         //
      }
  }

  @Test(timeout = 4000)
  public void test07()  throws Throwable  {
      ExtendedMessageFormat extendedMessageFormat0 = null;
      try {
        extendedMessageFormat0 = new ExtendedMessageFormat("!4{ Gqxg9x8Cn", (Map<String, ? extends FormatFactory>) null);
        fail("Expecting exception: IllegalArgumentException");
      
      } catch(IllegalArgumentException e) {
         //
         // Unmatched braces in the pattern.
         //
         verifyException("java.text.MessageFormat", e);
      }
  }

  @Test(timeout = 4000)
  public void test08()  throws Throwable  {
      HashMap<String, FormatFactory> hashMap0 = new HashMap<String, FormatFactory>();
      ExtendedMessageFormat extendedMessageFormat0 = null;
      try {
        extendedMessageFormat0 = new ExtendedMessageFormat(",GG=A^Y0;q\"Pcu{", hashMap0);
        fail("Expecting exception: ArrayIndexOutOfBoundsException");
      
      } catch(ArrayIndexOutOfBoundsException e) {
         //
         // no message in exception (getMessage() returned null)
         //
      }
  }

  @Test(timeout = 4000)
  public void test09()  throws Throwable  {
      HashMap<String, FormatFactory> hashMap0 = new HashMap<String, FormatFactory>();
      Locale locale0 = Locale.ROOT;
      ExtendedMessageFormat extendedMessageFormat0 = null;
      try {
        extendedMessageFormat0 = new ExtendedMessageFormat((String) null, locale0, hashMap0);
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
         //
         // no message in exception (getMessage() returned null)
         //
      }
  }

  @Test(timeout = 4000)
  public void test10()  throws Throwable  {
      Locale locale0 = Locale.JAPANESE;
      ExtendedMessageFormat extendedMessageFormat0 = null;
      try {
        extendedMessageFormat0 = new ExtendedMessageFormat(",jaLB[OB?0U){2", locale0, (Map<String, ? extends FormatFactory>) null);
        fail("Expecting exception: IllegalArgumentException");
      
      } catch(IllegalArgumentException e) {
         //
         // Unmatched braces in the pattern.
         //
         verifyException("java.text.MessageFormat", e);
      }
  }

  @Test(timeout = 4000)
  public void test11()  throws Throwable  {
      HashMap<String, FormatFactory> hashMap0 = new HashMap<String, FormatFactory>();
      Locale locale0 = Locale.ROOT;
      ExtendedMessageFormat extendedMessageFormat0 = null;
      try {
        extendedMessageFormat0 = new ExtendedMessageFormat("CG9!D&{+DZ]1r_*<y", locale0, hashMap0);
        fail("Expecting exception: IllegalArgumentException");
      
      } catch(IllegalArgumentException e) {
         //
         // Invalid format argument index at position 7: +
         //
         verifyException("org.apache.commons.text.ExtendedMessageFormat", e);
      }
  }

  @Test(timeout = 4000)
  public void test12()  throws Throwable  {
      Locale locale0 = Locale.FRANCE;
      ExtendedMessageFormat extendedMessageFormat0 = null;
      try {
        extendedMessageFormat0 = new ExtendedMessageFormat((String) null, locale0);
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
         //
         // no message in exception (getMessage() returned null)
         //
      }
  }

  @Test(timeout = 4000)
  public void test13()  throws Throwable  {
      Locale locale0 = Locale.UK;
      ExtendedMessageFormat extendedMessageFormat0 = null;
      try {
        extendedMessageFormat0 = new ExtendedMessageFormat("!a9MOlHOBT0U){s", locale0);
        fail("Expecting exception: IllegalArgumentException");
      
      } catch(IllegalArgumentException e) {
         //
         // Unmatched braces in the pattern.
         //
         verifyException("java.text.MessageFormat", e);
      }
  }

  @Test(timeout = 4000)
  public void test14()  throws Throwable  {
      ExtendedMessageFormat extendedMessageFormat0 = null;
      try {
        extendedMessageFormat0 = new ExtendedMessageFormat((String) null);
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
         //
         // no message in exception (getMessage() returned null)
         //
      }
  }

  @Test(timeout = 4000)
  public void test15()  throws Throwable  {
      ExtendedMessageFormat extendedMessageFormat0 = null;
      try {
        extendedMessageFormat0 = new ExtendedMessageFormat("!4{ G qxg9jx8CEn");
        fail("Expecting exception: IllegalArgumentException");
      
      } catch(IllegalArgumentException e) {
         //
         // Unmatched braces in the pattern.
         //
         verifyException("java.text.MessageFormat", e);
      }
  }

  @Test(timeout = 4000)
  public void test16()  throws Throwable  {
      HashMap<String, FormatFactory> hashMap0 = new HashMap<String, FormatFactory>();
      ExtendedMessageFormat extendedMessageFormat0 = new ExtendedMessageFormat("/vo=Ma|P{8}|", hashMap0);
      extendedMessageFormat0.applyPattern("/vo=Ma|P{8}|");
      assertEquals("/vo=Ma|P{8}|", extendedMessageFormat0.toPattern());
  }

  @Test(timeout = 4000)
  public void test17()  throws Throwable  {
      HashMap<String, FormatFactory> hashMap0 = new HashMap<String, FormatFactory>();
      ExtendedMessageFormat extendedMessageFormat0 = new ExtendedMessageFormat("OgB", hashMap0);
      // Undeclared exception!
      try { 
        extendedMessageFormat0.applyPattern("']");
        fail("Expecting exception: IllegalArgumentException");
      
      } catch(IllegalArgumentException e) {
         //
         // Unterminated quoted string at position 1
         //
         verifyException("org.apache.commons.text.ExtendedMessageFormat", e);
      }
  }

  @Test(timeout = 4000)
  public void test18()  throws Throwable  {
      HashMap<String, FormatFactory> hashMap0 = new HashMap<String, FormatFactory>();
      ExtendedMessageFormat extendedMessageFormat0 = null;
      try {
        extendedMessageFormat0 = new ExtendedMessageFormat(",GI=A^Y0;q\"PcU{,", hashMap0);
        fail("Expecting exception: IllegalArgumentException");
      
      } catch(IllegalArgumentException e) {
         //
         // Invalid format argument index at position 15: ,
         //
         verifyException("org.apache.commons.text.ExtendedMessageFormat", e);
      }
  }

  @Test(timeout = 4000)
  public void test19()  throws Throwable  {
      HashMap<String, FormatFactory> hashMap0 = new HashMap<String, FormatFactory>();
      ExtendedMessageFormat extendedMessageFormat0 = null;
      try {
        extendedMessageFormat0 = new ExtendedMessageFormat("!4{5 Gqxg9x8Cn", hashMap0);
        fail("Expecting exception: IllegalArgumentException");
      
      } catch(IllegalArgumentException e) {
         //
         // Invalid format argument index at position 3: 5 G
         //
         verifyException("org.apache.commons.text.ExtendedMessageFormat", e);
      }
  }

  @Test(timeout = 4000)
  public void test20()  throws Throwable  {
      HashMap<String, FormatFactory> hashMap0 = new HashMap<String, FormatFactory>();
      ExtendedMessageFormat extendedMessageFormat0 = null;
      try {
        extendedMessageFormat0 = new ExtendedMessageFormat("!ja9OlHOB?0U){2", hashMap0);
        fail("Expecting exception: IllegalArgumentException");
      
      } catch(IllegalArgumentException e) {
         //
         // Unterminated format element at position 14
         //
         verifyException("org.apache.commons.text.ExtendedMessageFormat", e);
      }
  }

  @Test(timeout = 4000)
  public void test21()  throws Throwable  {
      Locale locale0 = Locale.UK;
      HashMap<String, FormatFactory> hashMap0 = new HashMap<String, FormatFactory>();
      ExtendedMessageFormat extendedMessageFormat0 = new ExtendedMessageFormat("0W[", locale0, hashMap0);
      // Undeclared exception!
      try { 
        extendedMessageFormat0.applyPattern("!a9MOlHOBT0U){s");
        fail("Expecting exception: IllegalArgumentException");
      
      } catch(IllegalArgumentException e) {
         //
         // Invalid format argument index at position 14: s
         //
         verifyException("org.apache.commons.text.ExtendedMessageFormat", e);
      }
  }

  @Test(timeout = 4000)
  public void test22()  throws Throwable  {
      Locale locale0 = Locale.CANADA_FRENCH;
      ExtendedMessageFormat extendedMessageFormat0 = new ExtendedMessageFormat("", locale0);
      HashMap<String, FormatFactory> hashMap0 = new HashMap<String, FormatFactory>();
      ExtendedMessageFormat extendedMessageFormat1 = new ExtendedMessageFormat("", hashMap0);
      boolean boolean0 = extendedMessageFormat0.equals(extendedMessageFormat1);
      assertFalse(boolean0);
  }

  @Test(timeout = 4000)
  public void test23()  throws Throwable  {
      ExtendedMessageFormat extendedMessageFormat0 = new ExtendedMessageFormat("y'^QAE)NE");
      Object object0 = extendedMessageFormat0.clone();
      boolean boolean0 = extendedMessageFormat0.equals(object0);
      assertTrue(boolean0);
  }

  @Test(timeout = 4000)
  public void test24()  throws Throwable  {
      HashMap<String, FormatFactory> hashMap0 = new HashMap<String, FormatFactory>();
      ExtendedMessageFormat extendedMessageFormat0 = new ExtendedMessageFormat("jwJ%-di", hashMap0);
      ExtendedMessageFormat extendedMessageFormat1 = new ExtendedMessageFormat("uG2E^N,][A[DTd'Q{rG");
      boolean boolean0 = extendedMessageFormat0.equals(extendedMessageFormat1);
      assertFalse(boolean0);
  }

  @Test(timeout = 4000)
  public void test25()  throws Throwable  {
      ExtendedMessageFormat extendedMessageFormat0 = new ExtendedMessageFormat("[\"");
      boolean boolean0 = extendedMessageFormat0.equals("[\"");
      assertFalse(boolean0);
  }

  @Test(timeout = 4000)
  public void test26()  throws Throwable  {
      ExtendedMessageFormat extendedMessageFormat0 = new ExtendedMessageFormat("Unreadable format element at position ");
      boolean boolean0 = extendedMessageFormat0.equals(extendedMessageFormat0);
      assertTrue(boolean0);
  }

  @Test(timeout = 4000)
  public void test27()  throws Throwable  {
      ExtendedMessageFormat extendedMessageFormat0 = new ExtendedMessageFormat("LN");
      boolean boolean0 = extendedMessageFormat0.equals((Object) null);
      assertFalse(boolean0);
  }

  @Test(timeout = 4000)
  public void test28()  throws Throwable  {
      HashMap<String, FormatFactory> hashMap0 = new HashMap<String, FormatFactory>();
      ExtendedMessageFormat extendedMessageFormat0 = new ExtendedMessageFormat("'!QN4'-5OQ[N~", hashMap0);
      assertEquals("!QN4-5OQ[N~", extendedMessageFormat0.toPattern());
  }

  @Test(timeout = 4000)
  public void test29()  throws Throwable  {
      ExtendedMessageFormat extendedMessageFormat0 = new ExtendedMessageFormat("R83");
      Format[] formatArray0 = new Format[1];
      // Undeclared exception!
      try { 
        extendedMessageFormat0.setFormats(formatArray0);
        fail("Expecting exception: UnsupportedOperationException");
      
      } catch(UnsupportedOperationException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("org.apache.commons.text.ExtendedMessageFormat", e);
      }
  }

  @Test(timeout = 4000)
  public void test30()  throws Throwable  {
      HashMap<String, FormatFactory> hashMap0 = new HashMap<String, FormatFactory>();
      ExtendedMessageFormat extendedMessageFormat0 = new ExtendedMessageFormat("/vo=MP{8}|", hashMap0);
      assertEquals("/vo=MP{8}|", extendedMessageFormat0.toPattern());
  }

  @Test(timeout = 4000)
  public void test31()  throws Throwable  {
      ExtendedMessageFormat extendedMessageFormat0 = new ExtendedMessageFormat("\"]");
      DateFormat dateFormat0 = MockDateFormat.getDateInstance(0);
      // Undeclared exception!
      try { 
        extendedMessageFormat0.setFormatByArgumentIndex(0, dateFormat0);
        fail("Expecting exception: UnsupportedOperationException");
      
      } catch(UnsupportedOperationException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("org.apache.commons.text.ExtendedMessageFormat", e);
      }
  }

  @Test(timeout = 4000)
  public void test32()  throws Throwable  {
      ExtendedMessageFormat extendedMessageFormat0 = new ExtendedMessageFormat("kZR");
      DateFormat dateFormat0 = MockDateFormat.getDateInstance();
      // Undeclared exception!
      try { 
        extendedMessageFormat0.setFormat((-982), dateFormat0);
        fail("Expecting exception: UnsupportedOperationException");
      
      } catch(UnsupportedOperationException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("org.apache.commons.text.ExtendedMessageFormat", e);
      }
  }

  @Test(timeout = 4000)
  public void test33()  throws Throwable  {
      Locale locale0 = Locale.UK;
      HashMap<String, FormatFactory> hashMap0 = new HashMap<String, FormatFactory>();
      ExtendedMessageFormat extendedMessageFormat0 = new ExtendedMessageFormat("0W[", locale0, hashMap0);
      extendedMessageFormat0.hashCode();
      assertEquals("0W[", extendedMessageFormat0.toPattern());
  }

  @Test(timeout = 4000)
  public void test34()  throws Throwable  {
      ExtendedMessageFormat extendedMessageFormat0 = new ExtendedMessageFormat("");
      String string0 = extendedMessageFormat0.toPattern();
      assertEquals("", string0);
  }

  @Test(timeout = 4000)
  public void test35()  throws Throwable  {
      ExtendedMessageFormat extendedMessageFormat0 = new ExtendedMessageFormat("j8t&1BNrWA3@,5");
      Format[] formatArray0 = new Format[0];
      // Undeclared exception!
      try { 
        extendedMessageFormat0.setFormatsByArgumentIndex(formatArray0);
        fail("Expecting exception: UnsupportedOperationException");
      
      } catch(UnsupportedOperationException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("org.apache.commons.text.ExtendedMessageFormat", e);
      }
  }
}
