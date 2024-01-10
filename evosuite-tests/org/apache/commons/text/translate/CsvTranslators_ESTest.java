/*
 * This file was automatically generated by EvoSuite
 * Wed Jan 10 18:53:51 GMT 2024
 */

package org.apache.commons.text.translate;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.CharBuffer;
import org.apache.commons.text.translate.CsvTranslators;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class CsvTranslators_ESTest extends CsvTranslators_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void test0()  throws Throwable  {
      CsvTranslators.CsvUnescaper csvTranslators_CsvUnescaper0 = new CsvTranslators.CsvUnescaper();
      StringWriter stringWriter0 = new StringWriter();
      StringWriter stringWriter1 = stringWriter0.append('\"');
      StringBuffer stringBuffer0 = stringWriter0.getBuffer();
      char[] charArray0 = new char[9];
      stringWriter1.write(charArray0);
      csvTranslators_CsvUnescaper0.translateWhole(stringBuffer0, stringWriter0);
      assertEquals("\"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000", stringWriter0.toString());
  }

  @Test(timeout = 4000)
  public void test1()  throws Throwable  {
      CsvTranslators.CsvUnescaper csvTranslators_CsvUnescaper0 = new CsvTranslators.CsvUnescaper();
      char[] charArray0 = new char[3];
      charArray0[0] = '9';
      CharBuffer charBuffer0 = CharBuffer.wrap(charArray0);
      // Undeclared exception!
      try { 
        csvTranslators_CsvUnescaper0.translateWhole(charBuffer0, (Writer) null);
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("org.apache.commons.text.translate.CsvTranslators$CsvUnescaper", e);
      }
  }

  @Test(timeout = 4000)
  public void test2()  throws Throwable  {
      CsvTranslators.CsvUnescaper csvTranslators_CsvUnescaper0 = new CsvTranslators.CsvUnescaper();
      StringWriter stringWriter0 = new StringWriter(0);
      StringWriter stringWriter1 = stringWriter0.append('\"');
      StringWriter stringWriter2 = stringWriter1.append('\"');
      StringBuffer stringBuffer0 = stringWriter2.getBuffer();
      // Undeclared exception!
      try { 
        csvTranslators_CsvUnescaper0.translateWhole(stringBuffer0, stringWriter0);
        fail("Expecting exception: NoClassDefFoundError");
      
      } catch(NoClassDefFoundError e) {
         //
         // org/apache/commons/lang3/StringUtils
         //
         verifyException("org.apache.commons.text.translate.CsvTranslators$CsvUnescaper", e);
      }
  }

  @Test(timeout = 4000)
  public void test3()  throws Throwable  {
      CsvTranslators.CsvUnescaper csvTranslators_CsvUnescaper0 = new CsvTranslators.CsvUnescaper();
      StringWriter stringWriter0 = new StringWriter();
      StringWriter stringWriter1 = stringWriter0.append('\"');
      StringWriter stringWriter2 = stringWriter1.append((CharSequence) "FFFFFAC4");
      StringBuffer stringBuffer0 = stringWriter2.getBuffer();
      csvTranslators_CsvUnescaper0.translateWhole(stringBuffer0, stringWriter0);
      assertSame(stringWriter0, stringWriter1);
  }

  @Test(timeout = 4000)
  public void test4()  throws Throwable  {
      CsvTranslators.CsvUnescaper csvTranslators_CsvUnescaper0 = new CsvTranslators.CsvUnescaper();
      CharBuffer charBuffer0 = CharBuffer.allocate(3477);
      StringWriter stringWriter0 = new StringWriter(3477);
      csvTranslators_CsvUnescaper0.translateWhole(charBuffer0, stringWriter0);
      assertTrue(charBuffer0.hasArray());
  }

  @Test(timeout = 4000)
  public void test5()  throws Throwable  {
      CsvTranslators.CsvEscaper csvTranslators_CsvEscaper0 = new CsvTranslators.CsvEscaper();
      StringWriter stringWriter0 = new StringWriter(56);
      // Undeclared exception!
      try { 
        csvTranslators_CsvEscaper0.translateWhole("22", stringWriter0);
        fail("Expecting exception: NoClassDefFoundError");
      
      } catch(NoClassDefFoundError e) {
         //
         // org/apache/commons/lang3/StringUtils
         //
         verifyException("org.apache.commons.text.translate.CsvTranslators$CsvEscaper", e);
      }
  }
}
