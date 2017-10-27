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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.junit.Test;

/**
 * Unit tests for {@link StrBuilder}.
 */
public class StrBuilderAppendInsertTest {

    /** The system line separator. */
    private static final String SEP = System.lineSeparator();

    /** Test subclass of Object, with a toString method. */
    private static final Object FOO = new Object() {
        @Override
        public String toString() {
            return "foo";
        }
    };

    //-----------------------------------------------------------------------
    @Test
    public void testAppendNewLine() {
        StrBuilder sb = new StrBuilder("---");
        sb.appendNewLine().append("+++");
        assertThat(sb.toString()).isEqualTo("---" + SEP + "+++");

        sb = new StrBuilder("---");
        sb.setNewLineText("#").appendNewLine().setNewLineText(null).appendNewLine();
        assertThat(sb.toString()).isEqualTo("---#" + SEP);
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendWithNullText() {
        final StrBuilder sb = new StrBuilder();
        sb.setNullText("NULL");
        assertThat(sb.toString()).isEqualTo("");

        sb.appendNull();
        assertThat(sb.toString()).isEqualTo("NULL");

        sb.append((Object) null);
        assertThat(sb.toString()).isEqualTo("NULLNULL");

        sb.append(FOO);
        assertThat(sb.toString()).isEqualTo("NULLNULLfoo");

        sb.append((String) null);
        assertThat(sb.toString()).isEqualTo("NULLNULLfooNULL");

        sb.append("");
        assertThat(sb.toString()).isEqualTo("NULLNULLfooNULL");

        sb.append("bar");
        assertThat(sb.toString()).isEqualTo("NULLNULLfooNULLbar");

        sb.append((StringBuffer) null);
        assertThat(sb.toString()).isEqualTo("NULLNULLfooNULLbarNULL");

        sb.append(new StringBuffer("baz"));
        assertThat(sb.toString()).isEqualTo("NULLNULLfooNULLbarNULLbaz");
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppend_Object() {
        final StrBuilder sb = new StrBuilder();
        sb.appendNull();
        assertThat(sb.toString()).isEqualTo("");

        sb.append((Object) null);
        assertThat(sb.toString()).isEqualTo("");

        sb.append(FOO);
        assertThat(sb.toString()).isEqualTo("foo");

        sb.append((StringBuffer) null);
        assertThat(sb.toString()).isEqualTo("foo");

        sb.append(new StringBuffer("baz"));
        assertThat(sb.toString()).isEqualTo("foobaz");

        sb.append(new StrBuilder("yes"));
        assertThat(sb.toString()).isEqualTo("foobazyes");

        sb.append((CharSequence) "Seq");
        assertThat(sb.toString()).isEqualTo("foobazyesSeq");

        sb.append(new StringBuilder("bld")); // Check it supports StringBuilder
        assertThat(sb.toString()).isEqualTo("foobazyesSeqbld");
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppend_StringBuilder() {
        StrBuilder sb = new StrBuilder();
        sb.setNullText("NULL").append((String) null);
        assertThat(sb.toString()).isEqualTo("NULL");

        sb = new StrBuilder();
        sb.append(new StringBuilder("foo"));
        assertThat(sb.toString()).isEqualTo("foo");

        sb.append(new StringBuilder(""));
        assertThat(sb.toString()).isEqualTo("foo");

        sb.append(new StringBuilder("bar"));
        assertThat(sb.toString()).isEqualTo("foobar");
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppend_String() {
        StrBuilder sb = new StrBuilder();
        sb.setNullText("NULL").append((String) null);
        assertThat(sb.toString()).isEqualTo("NULL");

        sb = new StrBuilder();
        sb.append("foo");
        assertThat(sb.toString()).isEqualTo("foo");

        sb.append("");
        assertThat(sb.toString()).isEqualTo("foo");

        sb.append("bar");
        assertThat(sb.toString()).isEqualTo("foobar");
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppend_String_int_int() {
        StrBuilder sb = new StrBuilder();
        sb.setNullText("NULL").append((String) null, 0, 1);
        assertThat(sb.toString()).isEqualTo("NULL");

        sb = new StrBuilder();
        sb.append("foo", 0, 3);
        assertThat(sb.toString()).isEqualTo("foo");

        try {
            sb.append("bar", -1, 1);
            fail("append(char[], -1,) expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append("bar", 3, 1);
            fail("append(char[], 3,) expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append("bar", 1, -1);
            fail("append(char[],, -1) expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append("bar", 1, 3);
            fail("append(char[], 1, 3) expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append("bar", -1, 3);
            fail("append(char[], -1, 3) expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append("bar", 4, 0);
            fail("append(char[], 4, 0) expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        sb.append("bar", 3, 0);
        assertThat(sb.toString()).isEqualTo("foo");

        sb.append("abcbardef", 3, 3);
        assertThat(sb.toString()).isEqualTo("foobar");

        sb.append((CharSequence) "abcbardef", 4, 3);
        assertThat(sb.toString()).isEqualTo("foobarard");
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppend_StringBuilder_int_int() {
        StrBuilder sb = new StrBuilder();
        sb.setNullText("NULL").append((String) null, 0, 1);
        assertThat(sb.toString()).isEqualTo("NULL");

        sb = new StrBuilder();
        sb.append(new StringBuilder("foo"), 0, 3);
        assertThat(sb.toString()).isEqualTo("foo");

        try {
            sb.append(new StringBuilder("bar"), -1, 1);
            fail("append(StringBuilder, -1,) expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new StringBuilder("bar"), 3, 1);
            fail("append(StringBuilder, 3,) expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new StringBuilder("bar"), 1, -1);
            fail("append(StringBuilder,, -1) expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new StringBuilder("bar"), 1, 3);
            fail("append(StringBuilder, 1, 3) expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new StringBuilder("bar"), -1, 3);
            fail("append(StringBuilder, -1, 3) expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new StringBuilder("bar"), 4, 0);
            fail("append(StringBuilder, 4, 0) expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        sb.append(new StringBuilder("bar"), 3, 0);
        assertThat(sb.toString()).isEqualTo("foo");

        sb.append(new StringBuilder("abcbardef"), 3, 3);
        assertThat(sb.toString()).isEqualTo("foobar");

        sb.append(new StringBuilder("abcbardef"), 4, 3);
        assertThat(sb.toString()).isEqualTo("foobarard");
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppend_StringBuffer() {
        StrBuilder sb = new StrBuilder();
        sb.setNullText("NULL").append((StringBuffer) null);
        assertThat(sb.toString()).isEqualTo("NULL");

        sb = new StrBuilder();
        sb.append(new StringBuffer("foo"));
        assertThat(sb.toString()).isEqualTo("foo");

        sb.append(new StringBuffer(""));
        assertThat(sb.toString()).isEqualTo("foo");

        sb.append(new StringBuffer("bar"));
        assertThat(sb.toString()).isEqualTo("foobar");
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppend_StringBuffer_int_int() {
        StrBuilder sb = new StrBuilder();
        sb.setNullText("NULL").append((StringBuffer) null, 0, 1);
        assertThat(sb.toString()).isEqualTo("NULL");

        sb = new StrBuilder();
        sb.append(new StringBuffer("foo"), 0, 3);
        assertThat(sb.toString()).isEqualTo("foo");

        try {
            sb.append(new StringBuffer("bar"), -1, 1);
            fail("append(char[], -1,) expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new StringBuffer("bar"), 3, 1);
            fail("append(char[], 3,) expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new StringBuffer("bar"), 1, -1);
            fail("append(char[],, -1) expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new StringBuffer("bar"), 1, 3);
            fail("append(char[], 1, 3) expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new StringBuffer("bar"), -1, 3);
            fail("append(char[], -1, 3) expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new StringBuffer("bar"), 4, 0);
            fail("append(char[], 4, 0) expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        sb.append(new StringBuffer("bar"), 3, 0);
        assertThat(sb.toString()).isEqualTo("foo");

        sb.append(new StringBuffer("abcbardef"), 3, 3);
        assertThat(sb.toString()).isEqualTo("foobar");
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppend_StrBuilder() {
        StrBuilder sb = new StrBuilder();
        sb.setNullText("NULL").append((StrBuilder) null);
        assertThat(sb.toString()).isEqualTo("NULL");

        sb = new StrBuilder();
        sb.append(new StrBuilder("foo"));
        assertThat(sb.toString()).isEqualTo("foo");

        sb.append(new StrBuilder(""));
        assertThat(sb.toString()).isEqualTo("foo");

        sb.append(new StrBuilder("bar"));
        assertThat(sb.toString()).isEqualTo("foobar");
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppend_StrBuilder_int_int() {
        StrBuilder sb = new StrBuilder();
        sb.setNullText("NULL").append((StrBuilder) null, 0, 1);
        assertThat(sb.toString()).isEqualTo("NULL");

        sb = new StrBuilder();
        sb.append(new StrBuilder("foo"), 0, 3);
        assertThat(sb.toString()).isEqualTo("foo");

        try {
            sb.append(new StrBuilder("bar"), -1, 1);
            fail("append(char[], -1,) expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new StrBuilder("bar"), 3, 1);
            fail("append(char[], 3,) expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new StrBuilder("bar"), 1, -1);
            fail("append(char[],, -1) expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new StrBuilder("bar"), 1, 3);
            fail("append(char[], 1, 3) expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new StrBuilder("bar"), -1, 3);
            fail("append(char[], -1, 3) expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new StrBuilder("bar"), 4, 0);
            fail("append(char[], 4, 0) expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        sb.append(new StrBuilder("bar"), 3, 0);
        assertThat(sb.toString()).isEqualTo("foo");

        sb.append(new StrBuilder("abcbardef"), 3, 3);
        assertThat(sb.toString()).isEqualTo("foobar");
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppend_CharArray() {
        StrBuilder sb = new StrBuilder();
        sb.setNullText("NULL").append((char[]) null);
        assertThat(sb.toString()).isEqualTo("NULL");

        sb = new StrBuilder();
        sb.append(new char[0]);
        assertThat(sb.toString()).isEqualTo("");

        sb.append(new char[]{'f', 'o', 'o'});
        assertThat(sb.toString()).isEqualTo("foo");
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppend_CharArray_int_int() {
        StrBuilder sb = new StrBuilder();
        sb.setNullText("NULL").append((char[]) null, 0, 1);
        assertThat(sb.toString()).isEqualTo("NULL");

        sb = new StrBuilder();
        sb.append(new char[]{'f', 'o', 'o'}, 0, 3);
        assertThat(sb.toString()).isEqualTo("foo");

        try {
            sb.append(new char[]{'b', 'a', 'r'}, -1, 1);
            fail("append(char[], -1,) expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new char[]{'b', 'a', 'r'}, 3, 1);
            fail("append(char[], 3,) expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new char[]{'b', 'a', 'r'}, 1, -1);
            fail("append(char[],, -1) expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new char[]{'b', 'a', 'r'}, 1, 3);
            fail("append(char[], 1, 3) expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new char[]{'b', 'a', 'r'}, -1, 3);
            fail("append(char[], -1, 3) expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.append(new char[]{'b', 'a', 'r'}, 4, 0);
            fail("append(char[], 4, 0) expected IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        sb.append(new char[]{'b', 'a', 'r'}, 3, 0);
        assertThat(sb.toString()).isEqualTo("foo");

        sb.append(new char[]{'a', 'b', 'c', 'b', 'a', 'r', 'd', 'e', 'f'}, 3, 3);
        assertThat(sb.toString()).isEqualTo("foobar");
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppend_Boolean() {
        final StrBuilder sb = new StrBuilder();
        sb.append(true);
        assertThat(sb.toString()).isEqualTo("true");

        sb.append(false);
        assertThat(sb.toString()).isEqualTo("truefalse");

        sb.append('!');
        assertThat(sb.toString()).isEqualTo("truefalse!");
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppend_PrimitiveNumber() {
        final StrBuilder sb = new StrBuilder();
        sb.append(0);
        assertThat(sb.toString()).isEqualTo("0");

        sb.append(1L);
        assertThat(sb.toString()).isEqualTo("01");

        sb.append(2.3f);
        assertThat(sb.toString()).isEqualTo("012.3");

        sb.append(4.5d);
        assertThat(sb.toString()).isEqualTo("012.34.5");
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendln_FormattedString() {
        final int[] count = new int[2];
        final StrBuilder sb = new StrBuilder() {
            private static final long serialVersionUID = 1L;

            @Override
            public StrBuilder append(final String str) {
                count[0]++;
                return super.append(str);
            }
            @Override
            public StrBuilder appendNewLine() {
                count[1]++;
                return super.appendNewLine();
            }
        };
        sb.appendln("Hello %s", "Alice");
        assertThat(sb.toString()).isEqualTo("Hello Alice" + SEP);
        assertThat(count[0]);  // appendNewLine() calls append(String).isEqualTo(2)
        assertThat(count[1]).isEqualTo(1);
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendln_Object() {
        final StrBuilder sb = new StrBuilder();
        sb.appendln((Object) null);
        assertThat(sb.toString()).isEqualTo("" + SEP);

        sb.appendln(FOO);
        assertThat(sb.toString()).isEqualTo(SEP + "foo" + SEP);

        sb.appendln(Integer.valueOf(6));
        assertThat(sb.toString()).isEqualTo(SEP + "foo" + SEP + "6" + SEP);
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendln_String() {
        final int[] count = new int[2];
        final StrBuilder sb = new StrBuilder() {
            private static final long serialVersionUID = 1L;

            @Override
            public StrBuilder append(final String str) {
                count[0]++;
                return super.append(str);
            }
            @Override
            public StrBuilder appendNewLine() {
                count[1]++;
                return super.appendNewLine();
            }
        };
        sb.appendln("foo");
        assertThat(sb.toString()).isEqualTo("foo" + SEP);
        assertThat(count[0]);  // appendNewLine() calls append(String).isEqualTo(2)
        assertThat(count[1]).isEqualTo(1);
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendln_String_int_int() {
        final int[] count = new int[2];
        final StrBuilder sb = new StrBuilder() {
            private static final long serialVersionUID = 1L;

            @Override
            public StrBuilder append(final String str, final int startIndex, final int length) {
                count[0]++;
                return super.append(str, startIndex, length);
            }
            @Override
            public StrBuilder appendNewLine() {
                count[1]++;
                return super.appendNewLine();
            }
        };
        sb.appendln("foo", 0, 3);
        assertThat(sb.toString()).isEqualTo("foo" + SEP);
        assertThat(count[0]).isEqualTo(1);
        assertThat(count[1]).isEqualTo(1);
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendln_StringBuffer() {
        final int[] count = new int[2];
        final StrBuilder sb = new StrBuilder() {
            private static final long serialVersionUID = 1L;

            @Override
            public StrBuilder append(final StringBuffer str) {
                count[0]++;
                return super.append(str);
            }
            @Override
            public StrBuilder appendNewLine() {
                count[1]++;
                return super.appendNewLine();
            }
        };
        sb.appendln(new StringBuffer("foo"));
        assertThat(sb.toString()).isEqualTo("foo" + SEP);
        assertThat(count[0]).isEqualTo(1);
        assertThat(count[1]).isEqualTo(1);
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendln_StringBuilder() {
        final int[] count = new int[2];
        final StrBuilder sb = new StrBuilder() {
            private static final long serialVersionUID = 1L;

            @Override
            public StrBuilder append(final StringBuilder str) {
                count[0]++;
                return super.append(str);
            }
            @Override
            public StrBuilder appendNewLine() {
                count[1]++;
                return super.appendNewLine();
            }
        };
        sb.appendln(new StringBuilder("foo"));
        assertThat(sb.toString()).isEqualTo("foo" + SEP);
        assertThat(count[0]).isEqualTo(1);
        assertThat(count[1]).isEqualTo(1);
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendln_StringBuffer_int_int() {
        final int[] count = new int[2];
        final StrBuilder sb = new StrBuilder() {
            private static final long serialVersionUID = 1L;

            @Override
            public StrBuilder append(final StringBuffer str, final int startIndex, final int length) {
                count[0]++;
                return super.append(str, startIndex, length);
            }
            @Override
            public StrBuilder appendNewLine() {
                count[1]++;
                return super.appendNewLine();
            }
        };
        sb.appendln(new StringBuffer("foo"), 0, 3);
        assertThat(sb.toString()).isEqualTo("foo" + SEP);
        assertThat(count[0]).isEqualTo(1);
        assertThat(count[1]).isEqualTo(1);
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendln_StringBuilder_int_int() {
        final int[] count = new int[2];
        final StrBuilder sb = new StrBuilder() {
            private static final long serialVersionUID = 1L;

            @Override
            public StrBuilder append(final StringBuilder str, final int startIndex, final int length) {
                count[0]++;
                return super.append(str, startIndex, length);
            }
            @Override
            public StrBuilder appendNewLine() {
                count[1]++;
                return super.appendNewLine();
            }
        };
        sb.appendln(new StringBuilder("foo"), 0, 3);
        assertThat(sb.toString()).isEqualTo("foo" + SEP);
        assertThat(count[0]).isEqualTo(1);
        assertThat(count[1]).isEqualTo(1);
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendln_StrBuilder() {
        final int[] count = new int[2];
        final StrBuilder sb = new StrBuilder() {
            private static final long serialVersionUID = 1L;

            @Override
            public StrBuilder append(final StrBuilder str) {
                count[0]++;
                return super.append(str);
            }
            @Override
            public StrBuilder appendNewLine() {
                count[1]++;
                return super.appendNewLine();
            }
        };
        sb.appendln(new StrBuilder("foo"));
        assertThat(sb.toString()).isEqualTo("foo" + SEP);
        assertThat(count[0]).isEqualTo(1);
        assertThat(count[1]).isEqualTo(1);
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendln_StrBuilder_int_int() {
        final int[] count = new int[2];
        final StrBuilder sb = new StrBuilder() {
            private static final long serialVersionUID = 1L;

            @Override
            public StrBuilder append(final StrBuilder str, final int startIndex, final int length) {
                count[0]++;
                return super.append(str, startIndex, length);
            }
            @Override
            public StrBuilder appendNewLine() {
                count[1]++;
                return super.appendNewLine();
            }
        };
        sb.appendln(new StrBuilder("foo"), 0, 3);
        assertThat(sb.toString()).isEqualTo("foo" + SEP);
        assertThat(count[0]).isEqualTo(1);
        assertThat(count[1]).isEqualTo(1);
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendln_CharArray() {
        final int[] count = new int[2];
        final StrBuilder sb = new StrBuilder() {
            private static final long serialVersionUID = 1L;

            @Override
            public StrBuilder append(final char[] str) {
                count[0]++;
                return super.append(str);
            }
            @Override
            public StrBuilder appendNewLine() {
                count[1]++;
                return super.appendNewLine();
            }
        };
        sb.appendln("foo".toCharArray());
        assertThat(sb.toString()).isEqualTo("foo" + SEP);
        assertThat(count[0]).isEqualTo(1);
        assertThat(count[1]).isEqualTo(1);
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendln_CharArray_int_int() {
        final int[] count = new int[2];
        final StrBuilder sb = new StrBuilder() {
            private static final long serialVersionUID = 1L;

            @Override
            public StrBuilder append(final char[] str, final int startIndex, final int length) {
                count[0]++;
                return super.append(str, startIndex, length);
            }
            @Override
            public StrBuilder appendNewLine() {
                count[1]++;
                return super.appendNewLine();
            }
        };
        sb.appendln("foo".toCharArray(), 0, 3);
        assertThat(sb.toString()).isEqualTo("foo" + SEP);
        assertThat(count[0]).isEqualTo(1);
        assertThat(count[1]).isEqualTo(1);
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendln_Boolean() {
        final StrBuilder sb = new StrBuilder();
        sb.appendln(true);
        assertThat(sb.toString()).isEqualTo("true" + SEP);

        sb.clear();
        sb.appendln(false);
        assertThat(sb.toString()).isEqualTo("false" + SEP);
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendln_PrimitiveNumber() {
        final StrBuilder sb = new StrBuilder();
        sb.appendln(0);
        assertThat(sb.toString()).isEqualTo("0" + SEP);

        sb.clear();
        sb.appendln(1L);
        assertThat(sb.toString()).isEqualTo("1" + SEP);

        sb.clear();
        sb.appendln(2.3f);
        assertThat(sb.toString()).isEqualTo("2.3" + SEP);

        sb.clear();
        sb.appendln(4.5d);
        assertThat(sb.toString()).isEqualTo("4.5" + SEP);
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendPadding() {
        final StrBuilder sb = new StrBuilder();
        sb.append("foo");
        assertThat(sb.toString()).isEqualTo("foo");

        sb.appendPadding(-1, '-');
        assertThat(sb.toString()).isEqualTo("foo");

        sb.appendPadding(0, '-');
        assertThat(sb.toString()).isEqualTo("foo");

        sb.appendPadding(1, '-');
        assertThat(sb.toString()).isEqualTo("foo-");

        sb.appendPadding(16, '-');
        assertThat(sb.length()).isEqualTo(20);
        //            12345678901234567890
        assertThat(sb.toString()).isEqualTo("foo-----------------");
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendFixedWidthPadLeft() {
        final StrBuilder sb = new StrBuilder();
        sb.appendFixedWidthPadLeft("foo", -1, '-');
        assertThat(sb.toString()).isEqualTo("");

        sb.clear();
        sb.appendFixedWidthPadLeft("foo", 0, '-');
        assertThat(sb.toString()).isEqualTo("");

        sb.clear();
        sb.appendFixedWidthPadLeft("foo", 1, '-');
        assertThat(sb.toString()).isEqualTo("o");

        sb.clear();
        sb.appendFixedWidthPadLeft("foo", 2, '-');
        assertThat(sb.toString()).isEqualTo("oo");

        sb.clear();
        sb.appendFixedWidthPadLeft("foo", 3, '-');
        assertThat(sb.toString()).isEqualTo("foo");

        sb.clear();
        sb.appendFixedWidthPadLeft("foo", 4, '-');
        assertThat(sb.toString()).isEqualTo("-foo");

        sb.clear();
        sb.appendFixedWidthPadLeft("foo", 10, '-');
        assertThat(sb.length()).isEqualTo(10);
        //            1234567890
        assertThat(sb.toString()).isEqualTo("-------foo");

        sb.clear();
        sb.setNullText("null");
        sb.appendFixedWidthPadLeft(null, 5, '-');
        assertThat(sb.toString()).isEqualTo("-null");
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendFixedWidthPadLeft_int() {
        final StrBuilder sb = new StrBuilder();
        sb.appendFixedWidthPadLeft(123, -1, '-');
        assertThat(sb.toString()).isEqualTo("");

        sb.clear();
        sb.appendFixedWidthPadLeft(123, 0, '-');
        assertThat(sb.toString()).isEqualTo("");

        sb.clear();
        sb.appendFixedWidthPadLeft(123, 1, '-');
        assertThat(sb.toString()).isEqualTo("3");

        sb.clear();
        sb.appendFixedWidthPadLeft(123, 2, '-');
        assertThat(sb.toString()).isEqualTo("23");

        sb.clear();
        sb.appendFixedWidthPadLeft(123, 3, '-');
        assertThat(sb.toString()).isEqualTo("123");

        sb.clear();
        sb.appendFixedWidthPadLeft(123, 4, '-');
        assertThat(sb.toString()).isEqualTo("-123");

        sb.clear();
        sb.appendFixedWidthPadLeft(123, 10, '-');
        assertThat(sb.length()).isEqualTo(10);
        //            1234567890
        assertThat(sb.toString()).isEqualTo("-------123");
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendFixedWidthPadRight() {
        final StrBuilder sb = new StrBuilder();
        sb.appendFixedWidthPadRight("foo", -1, '-');
        assertThat(sb.toString()).isEqualTo("");

        sb.clear();
        sb.appendFixedWidthPadRight("foo", 0, '-');
        assertThat(sb.toString()).isEqualTo("");

        sb.clear();
        sb.appendFixedWidthPadRight("foo", 1, '-');
        assertThat(sb.toString()).isEqualTo("f");

        sb.clear();
        sb.appendFixedWidthPadRight("foo", 2, '-');
        assertThat(sb.toString()).isEqualTo("fo");

        sb.clear();
        sb.appendFixedWidthPadRight("foo", 3, '-');
        assertThat(sb.toString()).isEqualTo("foo");

        sb.clear();
        sb.appendFixedWidthPadRight("foo", 4, '-');
        assertThat(sb.toString()).isEqualTo("foo-");

        sb.clear();
        sb.appendFixedWidthPadRight("foo", 10, '-');
        assertThat(sb.length()).isEqualTo(10);
        //            1234567890
        assertThat(sb.toString()).isEqualTo("foo-------");

        sb.clear();
        sb.setNullText("null");
        sb.appendFixedWidthPadRight(null, 5, '-');
        assertThat(sb.toString()).isEqualTo("null-");
    }

    // See: http://issues.apache.org/jira/browse/LANG-299
    @Test
    public void testLang299() {
        final StrBuilder sb = new StrBuilder(1);
        sb.appendFixedWidthPadRight("foo", 1, '-');
        assertThat(sb.toString()).isEqualTo("f");
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendFixedWidthPadRight_int() {
        final StrBuilder sb = new StrBuilder();
        sb.appendFixedWidthPadRight(123, -1, '-');
        assertThat(sb.toString()).isEqualTo("");

        sb.clear();
        sb.appendFixedWidthPadRight(123, 0, '-');
        assertThat(sb.toString()).isEqualTo("");

        sb.clear();
        sb.appendFixedWidthPadRight(123, 1, '-');
        assertThat(sb.toString()).isEqualTo("1");

        sb.clear();
        sb.appendFixedWidthPadRight(123, 2, '-');
        assertThat(sb.toString()).isEqualTo("12");

        sb.clear();
        sb.appendFixedWidthPadRight(123, 3, '-');
        assertThat(sb.toString()).isEqualTo("123");

        sb.clear();
        sb.appendFixedWidthPadRight(123, 4, '-');
        assertThat(sb.toString()).isEqualTo("123-");

        sb.clear();
        sb.appendFixedWidthPadRight(123, 10, '-');
        assertThat(sb.length()).isEqualTo(10);
        //            1234567890
        assertThat(sb.toString()).isEqualTo("123-------");
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppend_FormattedString() {
        StrBuilder sb;

        sb = new StrBuilder();
        sb.append("Hi", (Object[]) null);
        assertThat(sb.toString()).isEqualTo("Hi");

        sb = new StrBuilder();
        sb.append("Hi", "Alice");
        assertThat(sb.toString()).isEqualTo("Hi");

        sb = new StrBuilder();
        sb.append("Hi %s", "Alice");
        assertThat(sb.toString()).isEqualTo("Hi Alice");

        sb = new StrBuilder();
        sb.append("Hi %s %,d", "Alice", 5000);
        // group separator depends on system locale
        final char groupingSeparator = DecimalFormatSymbols.getInstance().getGroupingSeparator();
        final String expected = "Hi Alice 5" + groupingSeparator + "000";
        assertThat(sb.toString()).isEqualTo(expected);
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendAll_Array() {
        final StrBuilder sb = new StrBuilder();
        sb.appendAll((Object[]) null);
        assertThat(sb.toString()).isEqualTo("");

        sb.clear();
        sb.appendAll(new Object[0]);
        assertThat(sb.toString()).isEqualTo("");

        sb.clear();
        sb.appendAll(new Object[]{"foo", "bar", "baz"});
        assertThat(sb.toString()).isEqualTo("foobarbaz");

        sb.clear();
        sb.appendAll("foo", "bar", "baz");
        assertThat(sb.toString()).isEqualTo("foobarbaz");
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendAll_Collection() {
        final StrBuilder sb = new StrBuilder();
        sb.appendAll((Collection<?>) null);
        assertThat(sb.toString()).isEqualTo("");

        sb.clear();
        sb.appendAll(Collections.EMPTY_LIST);
        assertThat(sb.toString()).isEqualTo("");

        sb.clear();
        sb.appendAll(Arrays.asList(new Object[]{"foo", "bar", "baz"}));
        assertThat(sb.toString()).isEqualTo("foobarbaz");
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendAll_Iterator() {
        final StrBuilder sb = new StrBuilder();
        sb.appendAll((Iterator<?>) null);
        assertThat(sb.toString()).isEqualTo("");

        sb.clear();
        sb.appendAll(Collections.EMPTY_LIST.iterator());
        assertThat(sb.toString()).isEqualTo("");

        sb.clear();
        sb.appendAll(Arrays.asList(new Object[]{"foo", "bar", "baz"}).iterator());
        assertThat(sb.toString()).isEqualTo("foobarbaz");
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendWithSeparators_Array() {
        final StrBuilder sb = new StrBuilder();
        sb.appendWithSeparators((Object[]) null, ",");
        assertThat(sb.toString()).isEqualTo("");

        sb.clear();
        sb.appendWithSeparators(new Object[0], ",");
        assertThat(sb.toString()).isEqualTo("");

        sb.clear();
        sb.appendWithSeparators(new Object[]{"foo", "bar", "baz"}, ",");
        assertThat(sb.toString()).isEqualTo("foo,bar,baz");

        sb.clear();
        sb.appendWithSeparators(new Object[]{"foo", "bar", "baz"}, null);
        assertThat(sb.toString()).isEqualTo("foobarbaz");

        sb.clear();
        sb.appendWithSeparators(new Object[]{"foo", null, "baz"}, ",");
        assertThat(sb.toString()).isEqualTo("foo,,baz");
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendWithSeparators_Collection() {
        final StrBuilder sb = new StrBuilder();
        sb.appendWithSeparators((Collection<?>) null, ",");
        assertThat(sb.toString()).isEqualTo("");

        sb.clear();
        sb.appendWithSeparators(Collections.EMPTY_LIST, ",");
        assertThat(sb.toString()).isEqualTo("");

        sb.clear();
        sb.appendWithSeparators(Arrays.asList(new Object[]{"foo", "bar", "baz"}), ",");
        assertThat(sb.toString()).isEqualTo("foo,bar,baz");

        sb.clear();
        sb.appendWithSeparators(Arrays.asList(new Object[]{"foo", "bar", "baz"}), null);
        assertThat(sb.toString()).isEqualTo("foobarbaz");

        sb.clear();
        sb.appendWithSeparators(Arrays.asList(new Object[]{"foo", null, "baz"}), ",");
        assertThat(sb.toString()).isEqualTo("foo,,baz");
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendWithSeparators_Iterator() {
        final StrBuilder sb = new StrBuilder();
        sb.appendWithSeparators((Iterator<?>) null, ",");
        assertThat(sb.toString()).isEqualTo("");

        sb.clear();
        sb.appendWithSeparators(Collections.EMPTY_LIST.iterator(), ",");
        assertThat(sb.toString()).isEqualTo("");

        sb.clear();
        sb.appendWithSeparators(Arrays.asList(new Object[]{"foo", "bar", "baz"}).iterator(), ",");
        assertThat(sb.toString()).isEqualTo("foo,bar,baz");

        sb.clear();
        sb.appendWithSeparators(Arrays.asList(new Object[]{"foo", "bar", "baz"}).iterator(), null);
        assertThat(sb.toString()).isEqualTo("foobarbaz");

        sb.clear();
        sb.appendWithSeparators(Arrays.asList(new Object[]{"foo", null, "baz"}).iterator(), ",");
        assertThat(sb.toString()).isEqualTo("foo,,baz");
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendWithSeparatorsWithNullText() {
        final StrBuilder sb = new StrBuilder();
        sb.setNullText("null");
        sb.appendWithSeparators(new Object[]{"foo", null, "baz"}, ",");
        assertThat(sb.toString()).isEqualTo("foo,null,baz");

        sb.clear();
        sb.appendWithSeparators(Arrays.asList(new Object[]{"foo", null, "baz"}), ",");
        assertThat(sb.toString()).isEqualTo("foo,null,baz");
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendSeparator_String() {
        final StrBuilder sb = new StrBuilder();
        sb.appendSeparator(",");  // no effect
        assertThat(sb.toString()).isEqualTo("");
        sb.append("foo");
        assertThat(sb.toString()).isEqualTo("foo");
        sb.appendSeparator(",");
        assertThat(sb.toString()).isEqualTo("foo,");
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendSeparator_String_String() {
        final StrBuilder sb = new StrBuilder();
        final String startSeparator = "order by ";
        final String standardSeparator = ",";
        final String foo = "foo";
        sb.appendSeparator(null, null);
        assertThat(sb.toString()).isEqualTo("");
        sb.appendSeparator(standardSeparator, null);
        assertThat(sb.toString()).isEqualTo("");
        sb.appendSeparator(standardSeparator, startSeparator);
        assertThat(sb.toString()).isEqualTo(startSeparator);
        sb.appendSeparator(null, null);
        assertThat(sb.toString()).isEqualTo(startSeparator);
        sb.appendSeparator(null, startSeparator);
        assertThat(sb.toString()).isEqualTo(startSeparator);
        sb.append(foo);
        assertThat(sb.toString()).isEqualTo(startSeparator + foo);
        sb.appendSeparator(standardSeparator, startSeparator);
        assertThat(sb.toString()).isEqualTo(startSeparator + foo + standardSeparator);
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendSeparator_char() {
        final StrBuilder sb = new StrBuilder();
        sb.appendSeparator(',');  // no effect
        assertThat(sb.toString()).isEqualTo("");
        sb.append("foo");
        assertThat(sb.toString()).isEqualTo("foo");
        sb.appendSeparator(',');
        assertThat(sb.toString()).isEqualTo("foo,");
    }
    @Test
    public void testAppendSeparator_char_char() {
        final StrBuilder sb = new StrBuilder();
        final char startSeparator = ':';
        final char standardSeparator = ',';
        final String foo = "foo";
        sb.appendSeparator(standardSeparator, startSeparator);  // no effect
        assertThat(sb.toString()).isEqualTo(String.valueOf(startSeparator));
        sb.append(foo);
        assertThat(sb.toString()).isEqualTo(String.valueOf(startSeparator) + foo);
        sb.appendSeparator(standardSeparator, startSeparator);
        assertThat(sb.toString()).isEqualTo(String.valueOf(startSeparator) + foo + standardSeparator);
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendSeparator_String_int() {
        final StrBuilder sb = new StrBuilder();
        sb.appendSeparator(",", 0);  // no effect
        assertThat(sb.toString()).isEqualTo("");
        sb.append("foo");
        assertThat(sb.toString()).isEqualTo("foo");
        sb.appendSeparator(",", 1);
        assertThat(sb.toString()).isEqualTo("foo,");

        sb.appendSeparator(",", -1);  // no effect
        assertThat(sb.toString()).isEqualTo("foo,");
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAppendSeparator_char_int() {
        final StrBuilder sb = new StrBuilder();
        sb.appendSeparator(',', 0);  // no effect
        assertThat(sb.toString()).isEqualTo("");
        sb.append("foo");
        assertThat(sb.toString()).isEqualTo("foo");
        sb.appendSeparator(',', 1);
        assertThat(sb.toString()).isEqualTo("foo,");

        sb.appendSeparator(',', -1);  // no effect
        assertThat(sb.toString()).isEqualTo("foo,");
    }

    //-----------------------------------------------------------------------
    @Test
    public void testInsert() {

        final StrBuilder sb = new StrBuilder();
        sb.append("barbaz");
        assertThat(sb.toString()).isEqualTo("barbaz");

        try {
            sb.insert(-1, FOO);
            fail("insert(-1, Object) expected StringIndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.insert(7, FOO);
            fail("insert(7, Object) expected StringIndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        sb.insert(0, (Object) null);
        assertThat(sb.toString()).isEqualTo("barbaz");

        sb.insert(0, FOO);
        assertThat(sb.toString()).isEqualTo("foobarbaz");

        sb.clear();
        sb.append("barbaz");
        assertThat(sb.toString()).isEqualTo("barbaz");

        try {
            sb.insert(-1, "foo");
            fail("insert(-1, String) expected StringIndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.insert(7, "foo");
            fail("insert(7, String) expected StringIndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        sb.insert(0, (String) null);
        assertThat(sb.toString()).isEqualTo("barbaz");

        sb.insert(0, "foo");
        assertThat(sb.toString()).isEqualTo("foobarbaz");

        sb.clear();
        sb.append("barbaz");
        assertThat(sb.toString()).isEqualTo("barbaz");

        try {
            sb.insert(-1, new char[]{'f', 'o', 'o'});
            fail("insert(-1, char[]) expected StringIndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.insert(7, new char[]{'f', 'o', 'o'});
            fail("insert(7, char[]) expected StringIndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        sb.insert(0, (char[]) null);
        assertThat(sb.toString()).isEqualTo("barbaz");

        sb.insert(0, new char[0]);
        assertThat(sb.toString()).isEqualTo("barbaz");

        sb.insert(0, new char[]{'f', 'o', 'o'});
        assertThat(sb.toString()).isEqualTo("foobarbaz");

        sb.clear();
        sb.append("barbaz");
        assertThat(sb.toString()).isEqualTo("barbaz");

        try {
            sb.insert(-1, new char[]{'a', 'b', 'c', 'f', 'o', 'o', 'd', 'e', 'f'}, 3, 3);
            fail("insert(-1, char[], 3, 3) expected StringIndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.insert(7, new char[]{'a', 'b', 'c', 'f', 'o', 'o', 'd', 'e', 'f'}, 3, 3);
            fail("insert(7, char[], 3, 3) expected StringIndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        sb.insert(0, (char[]) null, 0, 0);
        assertThat(sb.toString()).isEqualTo("barbaz");

        sb.insert(0, new char[0], 0, 0);
        assertThat(sb.toString()).isEqualTo("barbaz");

        try {
            sb.insert(0, new char[]{'a', 'b', 'c', 'f', 'o', 'o', 'd', 'e', 'f'}, -1, 3);
            fail("insert(0, char[], -1, 3) expected StringIndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.insert(0, new char[]{'a', 'b', 'c', 'f', 'o', 'o', 'd', 'e', 'f'}, 10, 3);
            fail("insert(0, char[], 10, 3) expected StringIndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.insert(0, new char[]{'a', 'b', 'c', 'f', 'o', 'o', 'd', 'e', 'f'}, 0, -1);
            fail("insert(0, char[], 0, -1) expected StringIndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.insert(0, new char[]{'a', 'b', 'c', 'f', 'o', 'o', 'd', 'e', 'f'}, 0, 10);
            fail("insert(0, char[], 0, 10) expected StringIndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        sb.insert(0, new char[]{'a', 'b', 'c', 'f', 'o', 'o', 'd', 'e', 'f'}, 0, 0);
        assertThat(sb.toString()).isEqualTo("barbaz");

        sb.insert(0, new char[]{'a', 'b', 'c', 'f', 'o', 'o', 'd', 'e', 'f'}, 3, 3);
        assertThat(sb.toString()).isEqualTo("foobarbaz");

        sb.clear();
        sb.append("barbaz");
        assertThat(sb.toString()).isEqualTo("barbaz");

        try {
            sb.insert(-1, true);
            fail("insert(-1, boolean) expected StringIndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.insert(7, true);
            fail("insert(7, boolean) expected StringIndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        sb.insert(0, true);
        assertThat(sb.toString()).isEqualTo("truebarbaz");

        sb.insert(0, false);
        assertThat(sb.toString()).isEqualTo("falsetruebarbaz");

        sb.clear();
        sb.append("barbaz");
        assertThat(sb.toString()).isEqualTo("barbaz");

        try {
            sb.insert(-1, '!');
            fail("insert(-1, char) expected StringIndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.insert(7, '!');
            fail("insert(7, char) expected StringIndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        sb.insert(0, '!');
        assertThat(sb.toString()).isEqualTo("!barbaz");

        sb.clear();
        sb.append("barbaz");
        assertThat(sb.toString()).isEqualTo("barbaz");

        try {
            sb.insert(-1, 0);
            fail("insert(-1, int) expected StringIndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.insert(7, 0);
            fail("insert(7, int) expected StringIndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        sb.insert(0, '0');
        assertThat(sb.toString()).isEqualTo("0barbaz");

        sb.clear();
        sb.append("barbaz");
        assertThat(sb.toString()).isEqualTo("barbaz");

        try {
            sb.insert(-1, 1L);
            fail("insert(-1, long) expected StringIndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.insert(7, 1L);
            fail("insert(7, long) expected StringIndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        sb.insert(0, 1L);
        assertThat(sb.toString()).isEqualTo("1barbaz");

        sb.clear();
        sb.append("barbaz");
        assertThat(sb.toString()).isEqualTo("barbaz");

        try {
            sb.insert(-1, 2.3F);
            fail("insert(-1, float) expected StringIndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.insert(7, 2.3F);
            fail("insert(7, float) expected StringIndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        sb.insert(0, 2.3F);
        assertThat(sb.toString()).isEqualTo("2.3barbaz");

        sb.clear();
        sb.append("barbaz");
        assertThat(sb.toString()).isEqualTo("barbaz");

        try {
            sb.insert(-1, 4.5D);
            fail("insert(-1, double) expected StringIndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.insert(7, 4.5D);
            fail("insert(7, double) expected StringIndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        sb.insert(0, 4.5D);
        assertThat(sb.toString()).isEqualTo("4.5barbaz");
    }

    //-----------------------------------------------------------------------
    @Test
    public void testInsertWithNullText() {
        final StrBuilder sb = new StrBuilder();
        sb.setNullText("null");
        sb.append("barbaz");
        assertThat(sb.toString()).isEqualTo("barbaz");

        try {
            sb.insert(-1, FOO);
            fail("insert(-1, Object) expected StringIndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.insert(7, FOO);
            fail("insert(7, Object) expected StringIndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        sb.insert(0, (Object) null);
        assertThat(sb.toString()).isEqualTo("nullbarbaz");

        sb.insert(0, FOO);
        assertThat(sb.toString()).isEqualTo("foonullbarbaz");

        sb.clear();
        sb.append("barbaz");
        assertThat(sb.toString()).isEqualTo("barbaz");

        try {
            sb.insert(-1, "foo");
            fail("insert(-1, String) expected StringIndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        try {
            sb.insert(7, "foo");
            fail("insert(7, String) expected StringIndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException e) {
            // expected
        }

        sb.insert(0, (String) null);
        assertThat(sb.toString()).isEqualTo("nullbarbaz");

        sb.insert(0, "foo");
        assertThat(sb.toString()).isEqualTo("foonullbarbaz");

        sb.insert(0, (char[]) null);
        assertThat(sb.toString()).isEqualTo("nullfoonullbarbaz");

        sb.insert(0, (char[]) null, 0, 0);
        assertThat(sb.toString()).isEqualTo("nullnullfoonullbarbaz");
    }
}
