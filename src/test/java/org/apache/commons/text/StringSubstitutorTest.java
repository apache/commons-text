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

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.commons.text.lookup.StringLookup;
import org.apache.commons.text.lookup.StringLookupFactory;
import org.apache.commons.text.matcher.StringMatcher;
import org.apache.commons.text.matcher.StringMatcherFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Test class for {@link StringSubstitutor}.
 */
@TestMethodOrder(MethodOrderer.Alphanumeric.class) // temp, for my sanity during dev
public class StringSubstitutorTest {

    private static final String EMPTY_EXPR = "${}";
    protected Map<String, String> values;

    protected void doTestNoReplace(final String replaceTemplate) throws IOException {
        doTestNoReplace(new StringSubstitutor(values), replaceTemplate);
    }

    protected void doTestNoReplace(final StringSubstitutor substitutor, final String replaceTemplate)
        throws IOException {
        if (replaceTemplate == null) {
            assertNull(replace(substitutor, (String) null));
            assertNull(substitutor.replace((String) null, 0, 100));
            assertNull(substitutor.replace((char[]) null));
            assertNull(substitutor.replace((char[]) null, 0, 100));
            assertNull(substitutor.replace((StringBuffer) null));
            assertNull(substitutor.replace((StringBuffer) null, 0, 100));
            assertNull(substitutor.replace((TextStringBuilder) null));
            assertNull(substitutor.replace((TextStringBuilder) null, 0, 100));
            assertNull(substitutor.replace((Object) null));
            assertFalse(substitutor.replaceIn((StringBuffer) null));
            assertFalse(substitutor.replaceIn((StringBuffer) null, 0, 100));
            assertFalse(substitutor.replaceIn((TextStringBuilder) null));
            assertFalse(substitutor.replaceIn((TextStringBuilder) null, 0, 100));
        } else {
            assertEquals(replaceTemplate, substitutor.replace(replaceTemplate));
            final TextStringBuilder bld = new TextStringBuilder(replaceTemplate);
            assertFalse(substitutor.replaceIn(bld));
            assertEquals(replaceTemplate, bld.toString());
        }
    }

    protected void doTestReplace(final String expectedResult, final String replaceTemplate, final boolean substring)
        throws IOException {
        doTestReplace(new StringSubstitutor(values), expectedResult, replaceTemplate, substring);
    }

    protected void doTestReplace(final StringSubstitutor sub, final String expectedResult, final String replaceTemplate,
        final boolean substring) throws IOException {
        final String expectedShortResult = expectedResult.substring(1, expectedResult.length() - 1);

        // replace using String
        assertEquals(expectedResult, replace(sub, replaceTemplate));
        if (substring) {
            assertEquals(expectedShortResult, sub.replace(replaceTemplate, 1, replaceTemplate.length() - 2));
        }

        // replace using char[]
        final char[] chars = replaceTemplate.toCharArray();
        assertEquals(expectedResult, sub.replace(chars));
        if (substring) {
            assertEquals(expectedShortResult, sub.replace(chars, 1, chars.length - 2));
        }

        // replace using StringBuffer
        StringBuffer buf = new StringBuffer(replaceTemplate);
        assertEquals(expectedResult, sub.replace(buf));
        if (substring) {
            assertEquals(expectedShortResult, sub.replace(buf, 1, buf.length() - 2));
        }

        // replace using StringBuilder
        StringBuilder builder = new StringBuilder(replaceTemplate);
        assertEquals(expectedResult, sub.replace(builder));
        if (substring) {
            assertEquals(expectedShortResult, sub.replace(builder, 1, builder.length() - 2));
        }

        // replace using TextStringBuilder
        TextStringBuilder bld = new TextStringBuilder(replaceTemplate);
        assertEquals(expectedResult, sub.replace(bld));
        if (substring) {
            assertEquals(expectedShortResult, sub.replace(bld, 1, bld.length() - 2));
        }

        // replace using object
        final MutableObject<String> obj = new MutableObject<>(replaceTemplate); // toString returns template
        assertEquals(expectedResult, sub.replace(obj));

        // replace in StringBuffer
        buf = new StringBuffer(replaceTemplate);
        assertTrue(sub.replaceIn(buf));
        assertEquals(expectedResult, buf.toString());
        if (substring) {
            buf = new StringBuffer(replaceTemplate);
            assertTrue(sub.replaceIn(buf, 1, buf.length() - 2));
            assertEquals(expectedResult, buf.toString()); // expect full result as remainder is untouched
        }

        // replace in StringBuilder
        builder = new StringBuilder(replaceTemplate);
        assertTrue(sub.replaceIn(builder));
        assertEquals(expectedResult, builder.toString());
        if (substring) {
            builder = new StringBuilder(replaceTemplate);
            assertTrue(sub.replaceIn(builder, 1, builder.length() - 2));
            assertEquals(expectedResult, builder.toString()); // expect full result as remainder is untouched
        }

        // replace in TextStringBuilder
        bld = new TextStringBuilder(replaceTemplate);
        assertTrue(sub.replaceIn(bld));
        assertEquals(expectedResult, bld.toString());
        if (substring) {
            bld = new TextStringBuilder(replaceTemplate);
            assertTrue(sub.replaceIn(bld, 1, bld.length() - 2));
            assertEquals(expectedResult, bld.toString()); // expect full result as remainder is untouched
        }
    }

    /**
     * For subclasses to override.
     *
     * @throws IOException Thrown by subclasses.
     */
    protected String replace(final StringSubstitutor stringSubstitutor, final String source) throws IOException {
        return stringSubstitutor.replace(source);
    }

    @BeforeEach
    public void setUp() throws Exception {
        values = new HashMap<>();
        values.put("animal", "quick brown fox");
        values.put("target", "lazy dog");
    }

    @AfterEach
    public void tearDown() throws Exception {
        values = null;
    }

    /**
     * Tests get set.
     */
    @Test
    public void testGetSetEscape() {
        final StringSubstitutor sub = new StringSubstitutor();
        assertEquals('$', sub.getEscapeChar());
        sub.setEscapeChar('<');
        assertEquals('<', sub.getEscapeChar());
    }

    /**
     * Test for LANG-1055: StringSubstitutor.replaceSystemProperties does not work consistently
     */
    @Test
    public void testLANG1055() {
        System.setProperty("test_key", "test_value");

        final String expected = StringSubstitutor.replace("test_key=${test_key}", System.getProperties());
        final String actual = StringSubstitutor.replaceSystemProperties("test_key=${test_key}");
        assertEquals(expected, actual);
    }

    /**
     * Tests adjacent keys.
     */
    @Test
    public void testReplaceAdjacentAtEnd() throws IOException {
        values.put("code", "GBP");
        values.put("amount", "12.50");
        final StringSubstitutor sub = new StringSubstitutor(values);
        assertEquals("Amount is GBP12.50", replace(sub, "Amount is ${code}${amount}"));
    }

    /**
     * Tests adjacent keys.
     */
    @Test
    public void testReplaceAdjacentAtStart() throws IOException {
        values.put("code", "GBP");
        values.put("amount", "12.50");
        final StringSubstitutor sub = new StringSubstitutor(values);
        assertEquals("GBP12.50 charged", replace(sub, "${code}${amount} charged"));
    }

    /**
     * Tests key replace changing map after initialization (not recommended).
     */
    @Test
    public void testReplaceChangedMap() throws IOException {
        final StringSubstitutor sub = new StringSubstitutor(values);
        values.put("target", "moon");
        assertEquals("The quick brown fox jumps over the moon.",
            replace(sub, "The ${animal} jumps over the ${target}."));
    }

    /**
     * Tests complex escaping.
     */
    @Test
    public void testReplaceComplexEscaping() throws IOException {
        doTestReplace("The ${quick brown fox} jumps over the lazy dog.", "The $${${animal}} jumps over the ${target}.",
            true);
        doTestReplace("The ${quick brown fox} jumps over the lazy dog. ${1234567890}.",
            "The $${${animal}} jumps over the ${target}. $${${undefined.number:-1234567890}}.", true);
    }

    /**
     * Tests replace with null.
     */
    @Test
    public void testReplaceEmpty() throws IOException {
        doTestNoReplace(StringUtils.EMPTY);
    }

    /**
     * Tests when no variable name.
     */
    @Test
    public void testReplaceEmptyKey() throws IOException {
        doTestReplace("The ${} jumps over the lazy dog.", "The ${} jumps over the ${target}.", true);
    }

    /**
     * Tests when no variable name.
     */
    @Test
    public void testReplaceEmptyKeyExtraFirst() throws IOException {
        final String expected = "." + EMPTY_EXPR;
        assertEquals(expected, replace(new StringSubstitutor(values), expected));
    }

    /**
     * Tests when no variable name.
     */
    @Test
    public void testReplaceEmptyKeyExtraLast() throws IOException {
        final String expected = EMPTY_EXPR + ".";
        assertEquals(expected, replace(new StringSubstitutor(values), expected));
    }

    /**
     * Tests when no variable name.
     */
    @Test
    public void testReplaceEmptyKeyOnly() throws IOException {
        assertEquals(EMPTY_EXPR, replace(new StringSubstitutor(values), EMPTY_EXPR));
    }

    /**
     * Tests when no variable name.
     */
    @Test
    public void testReplaceEmptyKeyWithDefault() throws IOException {
        doTestReplace("The animal jumps over the lazy dog.", "The ${:-animal} jumps over the ${target}.", true);
    }

    /**
     * Tests escaping.
     */
    @Test
    public void testReplaceEscaping() throws IOException {
        doTestReplace("The ${animal} jumps over the lazy dog.", "The $${animal} jumps over the ${target}.", true);
    }

    /**
     * Tests replace with fail on undefined variable.
     */
    @Test
    public void testReplaceFailOnUndefinedVariable() throws IOException {
        values.put("animal.1", "fox");
        values.put("animal.2", "mouse");
        values.put("species", "2");
        final StringSubstitutor sub = new StringSubstitutor(values);
        sub.setEnableUndefinedVariableException(true);

        assertThatIllegalArgumentException()
            .isThrownBy(() -> replace(sub, "The ${animal.${species}} jumps over the ${target}."))
            .withMessage("Cannot resolve variable 'animal.${species' (enableSubstitutionInVariables=false).");

        assertThatIllegalArgumentException()
            .isThrownBy(() -> replace(sub, "The ${animal.${species:-1}} jumps over the ${target}."))
            .withMessage("Cannot resolve variable 'animal.${species:-1' (enableSubstitutionInVariables=false).");

        assertThatIllegalArgumentException()
            .isThrownBy(() -> replace(sub, "The ${test:-statement} is a sample for missing ${unknown}."))
            .withMessage("Cannot resolve variable 'unknown' (enableSubstitutionInVariables=false).");

        // if default value is available, exception will not be thrown
        assertEquals("The statement is a sample for missing variable.",
            replace(sub, "The ${test:-statement} is a sample for missing ${unknown:-variable}."));

        assertEquals("The fox jumps over the lazy dog.", replace(sub, "The ${animal.1} jumps over the ${target}."));
    }

    /**
     * Tests whether replace with fail on undefined variable with substitution in variable names enabled.
     */
    @Test
    public void testReplaceFailOnUndefinedVariableWithReplaceInVariable() throws IOException {
        values.put("animal.1", "fox");
        values.put("animal.2", "mouse");
        values.put("species", "2");
        values.put("statement.1", "2");
        values.put("recursive", "1");
        values.put("word", "variable");
        values.put("testok.2", "statement");
        final StringSubstitutor sub = new StringSubstitutor(values);
        sub.setEnableUndefinedVariableException(true);
        sub.setEnableSubstitutionInVariables(true);

        assertEquals("The mouse jumps over the lazy dog.",
            replace(sub, "The ${animal.${species}} jumps over the ${target}."));
        values.put("species", "1");
        assertEquals("The fox jumps over the lazy dog.",
            replace(sub, "The ${animal.${species}} jumps over the ${target}."));

        // exception is thrown here because variable with name test.1 is missing
        assertThatIllegalArgumentException()
            .isThrownBy(() -> replace(sub, "The ${test.${statement}} is a sample for missing ${word}."))
            .withMessage("Cannot resolve variable 'statement' (enableSubstitutionInVariables=true).");

        // exception is thrown here because variable with name test.2 is missing
        assertThatIllegalArgumentException()
            .isThrownBy(() -> replace(sub, "The ${test.${statement.${recursive}}} is a sample for missing ${word}."))
            .withMessage("Cannot resolve variable 'test.2' (enableSubstitutionInVariables=true).");

        assertEquals("The statement is a sample for missing variable.",
            replace(sub, "The ${testok.${statement.${recursive}}} is a sample for missing ${word}."));
    }

    /**
     * Tests when no incomplete prefix.
     */
    @Test
    public void testReplaceIncompletePrefix() throws IOException {
        doTestReplace("The {animal} jumps over the lazy dog.", "The {animal} jumps over the ${target}.", true);
    }

    @Test
    public void testReplaceInTakingStringBufferWithNonNull() {
        final StringSubstitutor strSubstitutor = new StringSubstitutor(new HashMap<String, String>(), "WV@i#y?N*[",
            "WV@i#y?N*[", '*');

        assertFalse(strSubstitutor.isPreserveEscapes());
        assertFalse(strSubstitutor.replaceIn(new StringBuffer("WV@i#y?N*[")));
        assertEquals('*', strSubstitutor.getEscapeChar());
    }

    @Test
    public void testReplaceInTakingStringBuilderWithNonNull() {
        final StringLookup strLookup = StringLookupFactory.INSTANCE.systemPropertyStringLookup();
        final StringSubstitutor strSubstitutor = new StringSubstitutor(strLookup, "b<H", "b<H", '\'');
        final StringBuilder stringBuilder = new StringBuilder((CharSequence) "b<H");

        assertEquals('\'', strSubstitutor.getEscapeChar());
        assertFalse(strSubstitutor.replaceIn(stringBuilder));
    }

    @Test
    public void testReplaceInTakingStringBuilderWithNull() {
        final Map<String, Object> map = new HashMap<>();
        final StringSubstitutor strSubstitutor = new StringSubstitutor(map, StringUtils.EMPTY, StringUtils.EMPTY, 'T',
            "K+<'f");

        assertFalse(strSubstitutor.replaceIn((StringBuilder) null));
    }

    @Test
    public void testReplaceInTakingTwoAndThreeIntsReturningFalse() {
        final Map<String, Object> hashMap = new HashMap<>();
        final StringLookup mapStringLookup = StringLookupFactory.INSTANCE.mapStringLookup(hashMap);
        final StringMatcher strMatcher = StringMatcherFactory.INSTANCE.tabMatcher();
        final StringSubstitutor strSubstitutor = new StringSubstitutor(mapStringLookup, strMatcher, strMatcher, 'b',
            strMatcher);

        assertFalse(strSubstitutor.replaceIn((StringBuilder) null, 1315, -1369));
        assertEquals('b', strSubstitutor.getEscapeChar());
        assertFalse(strSubstitutor.isPreserveEscapes());
    }

    /**
     * Tests whether a variable can be replaced in a variable name.
     */
    @Test
    public void testReplaceInVariable() throws IOException {
        values.put("animal.1", "fox");
        values.put("animal.2", "mouse");
        values.put("species", "2");
        final StringSubstitutor sub = new StringSubstitutor(values);
        sub.setEnableSubstitutionInVariables(true);
        assertEquals("The mouse jumps over the lazy dog.",
            replace(sub, "The ${animal.${species}} jumps over the ${target}."));
        values.put("species", "1");
        assertEquals("The fox jumps over the lazy dog.",
            replace(sub, "The ${animal.${species}} jumps over the ${target}."));
        assertEquals("The fox jumps over the lazy dog.", replace(sub,
            "The ${unknown.animal.${unknown.species:-1}:-fox} " + "jumps over the ${unknow.target:-lazy dog}."));
    }

    /**
     * Tests whether substitution in variable names is disabled per default.
     */
    @Test
    public void testReplaceInVariableDisabled() throws IOException {
        values.put("animal.1", "fox");
        values.put("animal.2", "mouse");
        values.put("species", "2");
        final StringSubstitutor sub = new StringSubstitutor(values);
        assertEquals("The ${animal.${species}} jumps over the lazy dog.",
            replace(sub, "The ${animal.${species}} jumps over the ${target}."));
        assertEquals("The ${animal.${species:-1}} jumps over the lazy dog.",
            replace(sub, "The ${animal.${species:-1}} jumps over the ${target}."));
    }

    /**
     * Tests complex and recursive substitution in variable names.
     */
    @Test
    public void testReplaceInVariableRecursive() throws IOException {
        values.put("animal.2", "brown fox");
        values.put("animal.1", "white mouse");
        values.put("color", "white");
        values.put("species.white", "1");
        values.put("species.brown", "2");
        final StringSubstitutor sub = new StringSubstitutor(values);
        sub.setEnableSubstitutionInVariables(true);
        assertEquals("The white mouse jumps over the lazy dog.",
            replace(sub, "The ${animal.${species.${color}}} jumps over the ${target}."));
        assertEquals("The brown fox jumps over the lazy dog.",
            replace(sub, "The ${animal.${species.${unknownColor:-brown}}} jumps over the ${target}."));
    }

    /**
     * Tests when no prefix or suffix.
     */
    @Test
    public void testReplaceNoPrefixNoSuffix() throws IOException {
        doTestReplace("The animal jumps over the lazy dog.", "The animal jumps over the ${target}.", true);
    }

    /**
     * Tests when suffix but no prefix.
     */
    @Test
    public void testReplaceNoPrefixSuffix() throws IOException {
        doTestReplace("The animal} jumps over the lazy dog.", "The animal} jumps over the ${target}.", true);
    }

    /**
     * Tests replace with no variables.
     */
    @Test
    public void testReplaceNoVariables() throws IOException {
        doTestNoReplace("The balloon arrived.");
    }

    /**
     * Tests replace with null.
     */
    @Test
    public void testReplaceNull() throws IOException {
        doTestNoReplace(null);
    }

    /**
     * Tests simple key replace.
     */
    @Test
    public void testReplacePartialString_noReplace() {
        final StringSubstitutor sub = new StringSubstitutor();
        assertEquals("${animal} jumps", sub.replace("The ${animal} jumps over the ${target}.", 4, 15));
    }

    /**
     * Tests when prefix but no suffix.
     */
    @Test
    public void testReplacePrefixNoSuffix() throws IOException {
        doTestReplace("The ${animal jumps over the ${target} lazy dog.",
            "The ${animal jumps over the ${target} ${target}.", true);
    }

    /**
     * Tests simple recursive replace.
     */
    @Test
    public void testReplaceRecursive() throws IOException {
        values.put("animal", "${critter}");
        values.put("target", "${pet}");
        values.put("pet", "${petCharacteristic} dog");
        values.put("petCharacteristic", "lazy");
        values.put("critter", "${critterSpeed} ${critterColor} ${critterType}");
        values.put("critterSpeed", "quick");
        values.put("critterColor", "brown");
        values.put("critterType", "fox");
        doTestReplace("The quick brown fox jumps over the lazy dog.", "The ${animal} jumps over the ${target}.", true);

        values.put("pet", "${petCharacteristicUnknown:-lazy} dog");
        doTestReplace("The quick brown fox jumps over the lazy dog.", "The ${animal} jumps over the ${target}.", true);
    }

    /**
     * Tests simple key replace.
     */
    @Test
    public void testReplaceSimple() throws IOException {
        doTestReplace("The quick brown fox jumps over the lazy dog.", "The ${animal} jumps over the ${target}.", true);
    }

    @Test
    public void testReplaceSimplest() throws IOException {
        doTestReplace("quick brown fox", "${animal}", false);
    }

    /**
     * Tests simple key replace.
     */
    @Test
    public void testReplaceSolo() throws IOException {
        doTestReplace("quick brown fox", "${animal}", false);
    }

    /**
     * Tests escaping.
     */
    @Test
    public void testReplaceSoloEscaping2To1() throws IOException {
        doTestReplace("${animal}", "$${animal}", false);
    }

    /**
     * Tests escaping.
     */
    @Test
    public void testReplaceSoloEscaping3To2() throws IOException {
        doTestReplace("$${animal}", "$$${animal}", false);
    }

    /**
     * Tests escaping.
     */
    @Test
    public void testReplaceSoloEscaping4To3() throws IOException {
        doTestReplace("$$${animal}", "$$$${animal}", false);
    }

    @Test
    public void testReplaceTakingCharSequenceReturningNull() {
        final StringSubstitutor strSubstitutor = new StringSubstitutor((StringLookup) null);

        assertNull(strSubstitutor.replace((CharSequence) null));
        assertFalse(strSubstitutor.isPreserveEscapes());
        assertEquals('$', strSubstitutor.getEscapeChar());
    }

    @Test
    public void testReplaceTakingThreeArgumentsThrowsNullPointerException() {
        assertThatNullPointerException().isThrownBy(() -> StringSubstitutor.replace(null, (Properties) null));
    }

    /**
     * Tests replace creates output same as input.
     */
    @Test
    public void testReplaceToIdentical() throws IOException {
        values.put("animal", "$${${thing}}");
        values.put("thing", "animal");
        doTestReplace("The ${animal} jumps.", "The ${animal} jumps.", true);
    }

    /**
     * Tests unknown key replace.
     */
    @Test
    public void testReplaceUnknownKey() throws IOException {
        doTestReplace("The ${person} jumps over the lazy dog.", "The ${person} jumps over the ${target}.", true);
    }

    /**
     * Tests unknown key replace.
     */
    @Test
    public void testReplaceUnknownKeyDefaultValue() throws IOException {
        doTestReplace("The ${person} jumps over the lazy dog. 1234567890.",
            "The ${person} jumps over the ${target}. ${undefined.number:-1234567890}.", true);
    }

    /**
     * Tests unknown key replace.
     */
    @Test
    public void testReplaceUnknownKeyOnly() throws IOException {
        final String expected = "${person}";
        assertEquals(expected, replace(new StringSubstitutor(values), expected));
    }

    /**
     * Tests unknown key replace.
     */
    @Test
    public void testReplaceUnknownKeyOnlyExtraFirst() throws IOException {
        final String expected = ".${person}";
        assertEquals(expected, replace(new StringSubstitutor(values), expected));
    }

    /**
     * Tests unknown key replace.
     */
    @Test
    public void testReplaceUnknownKeyOnlyExtraLast() throws IOException {
        final String expected = "${person}.";
        assertEquals(expected, replace(new StringSubstitutor(values), expected));
    }

    /**
     * Tests unknown key replace.
     */
    @Test
    public void testReplaceUnknownShortestKeyOnly() throws IOException {
        final String expected = "${U}";
        assertEquals(expected, replace(new StringSubstitutor(values), expected));
    }

    /**
     * Tests unknown key replace.
     */
    @Test
    public void testReplaceUnknownShortestKeyOnlyExtraFirst() throws IOException {
        final String expected = ".${U}";
        assertEquals(expected, replace(new StringSubstitutor(values), expected));
    }

    /**
     * Tests unknown key replace.
     */
    @Test
    public void testReplaceUnknownShortestKeyOnlyExtraLast() throws IOException {
        final String expected = "${U}.";
        assertEquals(expected, replace(new StringSubstitutor(values), expected));
    }

    /**
     * Tests interpolation with weird boundary patterns.
     */
    @Test
    public void testReplaceWeirdPattens() throws IOException {
        doTestNoReplace(StringUtils.EMPTY);
        doTestNoReplace(EMPTY_EXPR);
        doTestNoReplace("${ }");
        doTestNoReplace("${\t}");
        doTestNoReplace("${\n}");
        doTestNoReplace("${\b}");
        doTestNoReplace("${");
        doTestNoReplace("$}");
        doTestNoReplace("}");
        doTestNoReplace("${}$");
        doTestNoReplace("${${");
        doTestNoReplace("${${}}");
        doTestNoReplace("${$${}}");
        doTestNoReplace("${$$${}}");
        doTestNoReplace("${$$${$}}");
        doTestNoReplace("${${}}");
        doTestNoReplace("${${ }}");
    }

    /**
     * Tests protected.
     */
    @Test
    public void testResolveVariable() {
        final TextStringBuilder builder = new TextStringBuilder("Hi ${name}!");
        final Map<String, String> map = new HashMap<>();
        map.put("name", "commons");
        final StringSubstitutor sub = new StringSubstitutor(map) {
            @Override
            protected String resolveVariable(final String variableName, final TextStringBuilder buf, final int startPos,
                final int endPos) {
                assertEquals("name", variableName);
                assertSame(builder, buf);
                assertEquals(3, startPos);
                assertEquals(10, endPos);
                return "jakarta";
            }
        };
        sub.replaceIn(builder);
        assertEquals("Hi jakarta!", builder.toString());
    }

    @Test
    public void testSamePrefixAndSuffix() {
        final Map<String, String> map = new HashMap<>();
        map.put("greeting", "Hello");
        map.put(" there ", "XXX");
        map.put("name", "commons");
        assertEquals("Hi commons!", StringSubstitutor.replace("Hi @name@!", map, "@", "@"));
        assertEquals("Hello there commons!", StringSubstitutor.replace("@greeting@ there @name@!", map, "@", "@"));
    }

    /**
     * Tests static.
     */
    @Test
    public void testStaticReplace() {
        final Map<String, String> map = new HashMap<>();
        map.put("name", "commons");
        assertEquals("Hi commons!", StringSubstitutor.replace("Hi ${name}!", map));
    }

    /**
     * Tests static.
     */
    @Test
    public void testStaticReplacePrefixSuffix() {
        final Map<String, String> map = new HashMap<>();
        map.put("name", "commons");
        assertEquals("Hi commons!", StringSubstitutor.replace("Hi <name>!", map, "<", ">"));
    }

    /**
     * Tests interpolation with system properties.
     */
    @Test
    public void testStaticReplaceSystemProperties() {
        final TextStringBuilder buf = new TextStringBuilder();
        buf.append("Hi ").append(System.getProperty("user.name"));
        buf.append(", you are working with ");
        buf.append(System.getProperty("os.name"));
        buf.append(", your home directory is ");
        buf.append(System.getProperty("user.home")).append('.');
        assertEquals(buf.toString(), StringSubstitutor.replaceSystemProperties(
            "Hi ${user.name}, you are " + "working with ${os.name}, your home " + "directory is ${user.home}."));
    }

    /**
     * Test the replace of a properties object
     */
    @Test
    public void testSubstituteDefaultProperties() {
        final String org = "${doesnotwork}";
        System.setProperty("doesnotwork", "It works!");

        // create a new Properties object with the System.getProperties as default
        final Properties props = new Properties(System.getProperties());

        assertEquals("It works!", StringSubstitutor.replace(org, props));
    }

    @Test
    public void testSubstitutePreserveEscape() throws IOException {
        final String org = "${not-escaped} $${escaped}";
        final Map<String, String> map = new HashMap<>();
        map.put("not-escaped", "value");

        final StringSubstitutor sub = new StringSubstitutor(map, "${", "}", '$');
        assertFalse(sub.isPreserveEscapes());
        assertEquals("value ${escaped}", replace(sub, org));

        sub.setPreserveEscapes(true);
        assertTrue(sub.isPreserveEscapes());
        assertEquals("value $${escaped}", replace(sub, org));
    }

}
