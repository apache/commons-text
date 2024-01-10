/*
 * This file was automatically generated by EvoSuite
 * Wed Jan 10 18:05:56 GMT 2024
 */

package org.apache.commons.text.lookup;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.MissingResourceException;
import org.apache.commons.text.lookup.ResourceBundleStringLookup;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class ResourceBundleStringLookup_ESTest extends ResourceBundleStringLookup_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void test00()  throws Throwable  {
      ResourceBundleStringLookup resourceBundleStringLookup0 = ResourceBundleStringLookup.INSTANCE;
      // Undeclared exception!
      try { 
        resourceBundleStringLookup0.lookup("]");
        fail("Expecting exception: IllegalArgumentException");
      
      } catch(IllegalArgumentException e) {
         //
         // Bad resource bundle key format []]; expected format is BundleName:KeyName.
         //
         verifyException("org.apache.commons.text.lookup.IllegalArgumentExceptions", e);
      }
  }

  @Test(timeout = 4000)
  public void test01()  throws Throwable  {
      ResourceBundleStringLookup resourceBundleStringLookup0 = new ResourceBundleStringLookup("");
      // Undeclared exception!
      try { 
        resourceBundleStringLookup0.getString("", "");
        fail("Expecting exception: MissingResourceException");
      
      } catch(MissingResourceException e) {
         //
         // Can't find bundle for base name , locale en
         //
         verifyException("java.util.ResourceBundle", e);
      }
  }

  @Test(timeout = 4000)
  public void test02()  throws Throwable  {
      ResourceBundleStringLookup resourceBundleStringLookup0 = new ResourceBundleStringLookup((String) null);
      // Undeclared exception!
      try { 
        resourceBundleStringLookup0.getString((String) null, (String) null);
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
         //
         // no message in exception (getMessage() returned null)
         //
      }
  }

  @Test(timeout = 4000)
  public void test03()  throws Throwable  {
      ResourceBundleStringLookup resourceBundleStringLookup0 = new ResourceBundleStringLookup("h^3+HQjOK64&");
      // Undeclared exception!
      try { 
        resourceBundleStringLookup0.getBundle("h^3+HQjOK64&");
        fail("Expecting exception: MissingResourceException");
      
      } catch(MissingResourceException e) {
         //
         // Can't find bundle for base name h^3+HQjOK64&, locale en
         //
         verifyException("java.util.ResourceBundle", e);
      }
  }

  @Test(timeout = 4000)
  public void test04()  throws Throwable  {
      ResourceBundleStringLookup resourceBundleStringLookup0 = ResourceBundleStringLookup.INSTANCE;
      // Undeclared exception!
      try { 
        resourceBundleStringLookup0.getBundle((String) null);
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
         //
         // no message in exception (getMessage() returned null)
         //
      }
  }

  @Test(timeout = 4000)
  public void test05()  throws Throwable  {
      ResourceBundleStringLookup resourceBundleStringLookup0 = new ResourceBundleStringLookup("Bad resource bundle key format [%s]; expected format is BundleName:KeyName.");
      // Undeclared exception!
      try { 
        resourceBundleStringLookup0.lookup("Bad resource bundle key format [%s]; expected format is BundleName:KeyName.");
        fail("Expecting exception: IllegalArgumentException");
      
      } catch(IllegalArgumentException e) {
         //
         // Bad resource bundle key format [Bad resource bundle key format [%s]; expected format is BundleName:KeyName.]; expected format is KeyName.
         //
         verifyException("org.apache.commons.text.lookup.IllegalArgumentExceptions", e);
      }
  }

  @Test(timeout = 4000)
  public void test06()  throws Throwable  {
      ResourceBundleStringLookup resourceBundleStringLookup0 = ResourceBundleStringLookup.INSTANCE;
      // Undeclared exception!
      try { 
        resourceBundleStringLookup0.lookup("Bad]resource bundle key format [%s]; expected format is BundleName:KeyNa:e.");
        fail("Expecting exception: IllegalArgumentException");
      
      } catch(IllegalArgumentException e) {
         //
         // Bad resource bundle key format [Bad]resource bundle key format [%s]; expected format is BundleName:KeyNa:e.]; expected format is BundleName:KeyName.
         //
         verifyException("org.apache.commons.text.lookup.IllegalArgumentExceptions", e);
      }
  }

  @Test(timeout = 4000)
  public void test07()  throws Throwable  {
      ResourceBundleStringLookup resourceBundleStringLookup0 = new ResourceBundleStringLookup("<JvVdf\u0000$");
      String string0 = resourceBundleStringLookup0.lookup("<JvVdf\u0000$");
      assertNull(string0);
  }

  @Test(timeout = 4000)
  public void test08()  throws Throwable  {
      ResourceBundleStringLookup resourceBundleStringLookup0 = new ResourceBundleStringLookup((String) null);
      String string0 = resourceBundleStringLookup0.lookup((String) null);
      assertNull(string0);
  }

  @Test(timeout = 4000)
  public void test09()  throws Throwable  {
      ResourceBundleStringLookup resourceBundleStringLookup0 = ResourceBundleStringLookup.INSTANCE;
      String string0 = resourceBundleStringLookup0.lookup("Bad resource bundle key format [%s]; expected format is BundleName:KeyName.");
      assertNull(string0);
  }

  @Test(timeout = 4000)
  public void test10()  throws Throwable  {
      ResourceBundleStringLookup resourceBundleStringLookup0 = new ResourceBundleStringLookup("h^3+HQjOK64&");
      String string0 = resourceBundleStringLookup0.toString();
      assertNotNull(string0);
  }
}
