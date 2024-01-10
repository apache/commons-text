/*
 * This file was automatically generated by EvoSuite
 * Wed Jan 10 17:40:03 GMT 2024
 */

package org.apache.commons.text.diff;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.text.diff.CommandVisitor;
import org.apache.commons.text.diff.DeleteCommand;
import org.apache.commons.text.diff.EditScript;
import org.apache.commons.text.diff.InsertCommand;
import org.apache.commons.text.diff.KeepCommand;
import org.apache.commons.text.diff.ReplacementsFinder;
import org.apache.commons.text.diff.ReplacementsHandler;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class EditScript_ESTest extends EditScript_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void test0()  throws Throwable  {
      EditScript<Integer> editScript0 = new EditScript<Integer>();
      InsertCommand<Integer> insertCommand0 = new InsertCommand<Integer>((Integer) null);
      editScript0.append(insertCommand0);
      // Undeclared exception!
      try { 
        editScript0.visit((CommandVisitor<Integer>) null);
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("org.apache.commons.text.diff.InsertCommand", e);
      }
  }

  @Test(timeout = 4000)
  public void test1()  throws Throwable  {
      EditScript<Object> editScript0 = new EditScript<Object>();
      Object object0 = new Object();
      KeepCommand<Object> keepCommand0 = new KeepCommand<Object>(object0);
      editScript0.append(keepCommand0);
      int int0 = editScript0.getLCSLength();
      assertEquals(1, int0);
  }

  @Test(timeout = 4000)
  public void test2()  throws Throwable  {
      EditScript<Object> editScript0 = new EditScript<Object>();
      InsertCommand<Object> insertCommand0 = new InsertCommand<Object>(editScript0);
      editScript0.append(insertCommand0);
      int int0 = editScript0.getModifications();
      assertEquals(1, int0);
  }

  @Test(timeout = 4000)
  public void test3()  throws Throwable  {
      EditScript<Object> editScript0 = new EditScript<Object>();
      ReplacementsHandler<Object> replacementsHandler0 = (ReplacementsHandler<Object>) mock(ReplacementsHandler.class, new ViolatedAssumptionAnswer());
      ReplacementsFinder<Object> replacementsFinder0 = new ReplacementsFinder<Object>(replacementsHandler0);
      DeleteCommand<Object> deleteCommand0 = new DeleteCommand<Object>(replacementsFinder0);
      editScript0.append(deleteCommand0);
      assertEquals(1, editScript0.getModifications());
  }

  @Test(timeout = 4000)
  public void test4()  throws Throwable  {
      EditScript<Integer> editScript0 = new EditScript<Integer>();
      int int0 = editScript0.getLCSLength();
      assertEquals(0, int0);
      assertEquals(0, editScript0.getModifications());
  }

  @Test(timeout = 4000)
  public void test5()  throws Throwable  {
      EditScript<Object> editScript0 = new EditScript<Object>();
      ReplacementsHandler<Object> replacementsHandler0 = (ReplacementsHandler<Object>) mock(ReplacementsHandler.class, new ViolatedAssumptionAnswer());
      ReplacementsFinder<Object> replacementsFinder0 = new ReplacementsFinder<Object>(replacementsHandler0);
      editScript0.visit(replacementsFinder0);
      assertEquals(0, editScript0.getLCSLength());
      assertEquals(0, editScript0.getModifications());
  }

  @Test(timeout = 4000)
  public void test6()  throws Throwable  {
      EditScript<Object> editScript0 = new EditScript<Object>();
      int int0 = editScript0.getModifications();
      assertEquals(0, int0);
      assertEquals(0, editScript0.getLCSLength());
  }
}
