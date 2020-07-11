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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Test class for {@link StringSubstitutor}.
 */
@TestMethodOrder(MethodOrderer.Alphanumeric.class) // temp, for my sanity during dev
public class StringSubstitutorTest {

    private static final String ACTUAL_ANIMAL = "quick brown fox";
    private static final String ACTUAL_TARGET = "lazy dog";
    private static final String CLASSIC_RESULT = "The quick brown fox jumps over the lazy dog.";
    private static final String CLASSIC_TEMPLATE = "The ${animal} jumps over the ${target}.";
    private static final String EMPTY_EXPR = "${}";

    protected Map<String, String> values;

    private void assertEqualsCharSeq(final CharSequence expected, final CharSequence actual) {
        assertEquals(expected, actual, () -> String.format("expected.length()=%,d, actual.length()=%,d",
            StringUtils.length(expected), StringUtils.length(actual)));
    }

    protected void doNotReplace(final String replaceTemplate) throws IOException {
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
            assertEquals(replaceTemplate, replace(substitutor, replaceTemplate));
            final TextStringBuilder builder = new TextStringBuilder(replaceTemplate);
            assertFalse(substitutor.replaceIn(builder));
            assertEquals(replaceTemplate, builder.toString());
        }
    }

    protected void doReplace(final String expectedResult, final String replaceTemplate, final boolean substring)
        throws IOException {
        doTestReplace(new StringSubstitutor(values), expectedResult, replaceTemplate, substring);
    }

    protected void doTestReplace(final StringSubstitutor sub, final String expectedResult, final String replaceTemplate,
        final boolean substring) throws IOException {
        final String expectedShortResult = substring ? expectedResult.substring(1, expectedResult.length() - 1)
            : expectedResult;

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
        assertTrue(sub.replaceIn(buf), replaceTemplate);
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
    protected String replace(final StringSubstitutor stringSubstitutor, final String template) throws IOException {
        return stringSubstitutor.replace(template);
    }

    @BeforeEach
    public void setUp() throws Exception {
        values = new HashMap<>();
        // shortest key and value.
        values.put("a", "1");
        values.put("aa", "11");
        values.put("aaa", "111");
        values.put("b", "2");
        values.put("bb", "22");
        values.put("bbb", "222");
        values.put("a2b", "b");
        // normal key and value.
        values.put("animal", ACTUAL_ANIMAL);
        values.put("target", ACTUAL_TARGET);
    }

    @AfterEach
    public void tearDown() throws Exception {
        values = null;
    }

    @Test
    public void testConstructorStringSubstitutor() {
        final StringSubstitutor source = new StringSubstitutor();
        source.setDisableSubstitutionInValues(true);
        source.setEnableSubstitutionInVariables(true);
        source.setEnableUndefinedVariableException(true);
        source.setEscapeChar('e');
        source.setValueDelimiter('d');
        source.setVariablePrefix('p');
        source.setVariableResolver(StringLookupFactory.INSTANCE.nullStringLookup());
        source.setVariableSuffix('s');
        //
        final StringSubstitutor target = new StringSubstitutor(source);
        //
        assertTrue(target.isDisableSubstitutionInValues());
        assertTrue(target.isEnableSubstitutionInVariables());
        assertTrue(target.isEnableUndefinedVariableException());
        assertEquals('e', target.getEscapeChar());
        assertTrue(target.getValueDelimiterMatcher().toString().endsWith("['d']"),
            target.getValueDelimiterMatcher().toString());
        assertTrue(target.getVariablePrefixMatcher().toString().endsWith("['p']"),
            target.getValueDelimiterMatcher().toString());
        assertTrue(target.getVariableSuffixMatcher().toString().endsWith("['s']"),
            target.getValueDelimiterMatcher().toString());
    }

    @Test
    public void testGetMinExpressionLength() throws IOException {
        final StringSubstitutor sub = new StringSubstitutor();
        assertEquals(4, sub.getMinExpressionLength());
        sub.setVariablePrefix('a');
        assertEquals(3, sub.getMinExpressionLength());
        sub.setVariablePrefix("abc");
        assertEquals(5, sub.getMinExpressionLength());
        sub.setVariableSuffix("xyz");
        assertEquals(7, sub.getMinExpressionLength());
        sub.setVariablePrefix(StringUtils.EMPTY);
        sub.setVariableSuffix(StringUtils.EMPTY);
        assertEquals(1, sub.getMinExpressionLength());
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
        assertEqualsCharSeq("Amount is GBP12.50", replace(sub, "Amount is ${code}${amount}"));
    }

    /**
     * Tests adjacent keys.
     */
    @Test
    public void testReplaceAdjacentAtStart() throws IOException {
        values.put("code", "GBP");
        values.put("amount", "12.50");
        final StringSubstitutor sub = new StringSubstitutor(values);
        assertEqualsCharSeq("GBP12.50 charged", replace(sub, "${code}${amount} charged"));
    }

    /**
     * Tests key replace changing map after initialization (not recommended).
     */
    @Test
    public void testReplaceChangedMap() throws IOException {
        final StringSubstitutor sub = new StringSubstitutor(values);
        // no map change
        final String template = CLASSIC_TEMPLATE;
        assertEqualsCharSeq(CLASSIC_RESULT, replace(sub, template));
        // map change
        values.put("target", "moon");
        assertEqualsCharSeq("The quick brown fox jumps over the moon.", replace(sub, template));
    }

    /**
     * Tests complex escaping.
     */
    @Test
    public void testReplaceComplexEscaping() throws IOException {
        doReplace("${1}", "$${${a}}", false);
        doReplace("${11}", "$${${aa}}", false);
        doReplace("${111}", "$${${aaa}}", false);
        doReplace("${quick brown fox}", "$${${animal}}", false);
        doReplace("The ${quick brown fox} jumps over the lazy dog.", "The $${${animal}} jumps over the ${target}.",
            true);
        doReplace("${${a}}", "$${$${a}}", false);
        doReplace("${${aa}}", "$${$${aa}}", false);
        doReplace("${${aaa}}", "$${$${aaa}}", false);
        doReplace("${${animal}}", "$${$${animal}}", false);
        doReplace(".${${animal}}", ".$${$${animal}}", false);
        doReplace("${${animal}}.", "$${$${animal}}.", false);
        doReplace(".${${animal}}.", ".$${$${animal}}.", false);
        doReplace("The ${${animal}} jumps over the lazy dog.", "The $${$${animal}} jumps over the ${target}.", true);
        doReplace("The ${quick brown fox} jumps over the lazy dog. ${1234567890}.",
            "The $${${animal}} jumps over the ${target}. $${${undefined.number:-1234567890}}.", true);
    }

    /**
     * Tests when no variable name.
     */
    @Test
    public void testReplaceEmptyKey() throws IOException {
        doReplace("The ${} jumps over the lazy dog.", "The ${} jumps over the ${target}.", true);
    }

    /**
     * Tests when no variable name.
     */
    @Test
    public void testReplaceEmptyKeyExtraFirst() throws IOException {
        assertEqualsCharSeq("." + EMPTY_EXPR, replace(new StringSubstitutor(values), "." + EMPTY_EXPR));
    }

    /**
     * Tests when no variable name.
     */
    @Test
    public void testReplaceEmptyKeyExtraLast() throws IOException {
        assertEqualsCharSeq(EMPTY_EXPR + ".", replace(new StringSubstitutor(values), EMPTY_EXPR + "."));
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
    public void testReplaceEmptyKeyShortest() throws IOException {
        doNotReplace(EMPTY_EXPR);
    }

    /**
     * Tests when no variable name.
     */
    @Test
    public void testReplaceEmptyKeyWithDefault() throws IOException {
        doReplace("The animal jumps over the lazy dog.", "The ${:-animal} jumps over the ${target}.", true);
    }

    /**
     * Tests when no variable name.
     */
    @Test
    public void testReplaceEmptyKeyWithDefaultOnly() throws IOException {
        doReplace("animal", "${:-animal}", false);
    }

    /**
     * Tests when no variable name.
     */
    @Test
    public void testReplaceEmptyKeyWithDefaultOnlyEmpty() throws IOException {
        doReplace("", "${:-}", false);
    }

    /**
     * Tests when no variable name.
     */
    @Test
    public void testReplaceEmptyKeyWithDefaultOnlyShortest() throws IOException {
        doReplace("a", "${:-a}", false);
    }

    /**
     * Tests replace with null.
     */
    @Test
    public void testReplaceEmptyString() throws IOException {
        doNotReplace(StringUtils.EMPTY);
    }

    /**
     * Tests escaping.
     */
    @Test
    public void testReplaceEscaping() throws IOException {
        doReplace("The ${animal} jumps over the lazy dog.", "The $${animal} jumps over the ${target}.", true);
        doReplace("${a}", "$${a}", false);
        doReplace("${a${a}}", "$${a$${a}}", false);
        doReplace("${a${a${a}}}", "$${a$${a$${a}}}", false);
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
        assertEqualsCharSeq("The statement is a sample for missing variable.",
            replace(sub, "The ${test:-statement} is a sample for missing ${unknown:-variable}."));

        assertEqualsCharSeq("The fox jumps over the lazy dog.",
            replace(sub, "The ${animal.1} jumps over the ${target}."));
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

        assertEqualsCharSeq("The mouse jumps over the lazy dog.",
            replace(sub, "The ${animal.${species}} jumps over the ${target}."));
        values.put("species", "1");
        assertEqualsCharSeq("The fox jumps over the lazy dog.",
            replace(sub, "The ${animal.${species}} jumps over the ${target}."));

        // exception is thrown here because variable with name test.1 is missing
        assertThatIllegalArgumentException()
            .isThrownBy(() -> replace(sub, "The ${test.${statement}} is a sample for missing ${word}."))
            .withMessage("Cannot resolve variable 'statement' (enableSubstitutionInVariables=true).");

        // exception is thrown here because variable with name test.2 is missing
        assertThatIllegalArgumentException()
            .isThrownBy(() -> replace(sub, "The ${test.${statement.${recursive}}} is a sample for missing ${word}."))
            .withMessage("Cannot resolve variable 'test.2' (enableSubstitutionInVariables=true).");

        assertEqualsCharSeq("statement",
            replace(sub, "${testok.${statement.${recursive}}}"));

        assertEqualsCharSeq("${testok.2}",
            replace(sub, "$${testok.${statement.${recursive}}}"));

        assertEqualsCharSeq("The statement is a sample for missing variable.",
            replace(sub, "The ${testok.${statement.${recursive}}} is a sample for missing ${word}."));
    }

    /**
     * Tests when no incomplete prefix.
     */
    @Test
    public void testReplaceIncompletePrefix() throws IOException {
        doReplace("The {animal} jumps over the lazy dog.", "The {animal} jumps over the ${target}.", true);
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
        assertEqualsCharSeq("The mouse jumps over the lazy dog.",
            replace(sub, "The ${animal.${species}} jumps over the ${target}."));
        values.put("species", "1");
        assertEqualsCharSeq("The fox jumps over the lazy dog.",
            replace(sub, "The ${animal.${species}} jumps over the ${target}."));
        assertEqualsCharSeq("The fox jumps over the lazy dog.", replace(sub,
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
        assertEqualsCharSeq("The ${animal.${species}} jumps over the lazy dog.",
            replace(sub, "The ${animal.${species}} jumps over the ${target}."));
        assertEqualsCharSeq("The ${animal.${species:-1}} jumps over the lazy dog.",
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
        assertEqualsCharSeq("white mouse", replace(sub, "${animal.${species.${color}}}"));
        assertEqualsCharSeq("The white mouse jumps over the lazy dog.",
            replace(sub, "The ${animal.${species.${color}}} jumps over the ${target}."));
        assertEqualsCharSeq("The brown fox jumps over the lazy dog.",
            replace(sub, "The ${animal.${species.${unknownColor:-brown}}} jumps over the ${target}."));
    }

    /**
     * Tests when no variable name.
     */
    @Test
    public void testReplaceKeyStartChars() throws IOException {
        final String substring = StringSubstitutor.DEFAULT_VAR_START + "a";
        assertEqualsCharSeq(substring, replace(new StringSubstitutor(values), substring));
    }

    /**
     * Tests when no variable name.
     */
    @Test
    public void testReplaceKeyStartChars1Only() throws IOException {
        final String substring = StringSubstitutor.DEFAULT_VAR_START.substring(0, 1);
        assertEqualsCharSeq(substring, replace(new StringSubstitutor(values), substring));
    }

    /**
     * Tests when no variable name.
     */
    @Test
    public void testReplaceKeyStartChars2Only() throws IOException {
        final String substring = StringSubstitutor.DEFAULT_VAR_START.substring(0, 2);
        assertEqualsCharSeq(substring, replace(new StringSubstitutor(values), substring));
    }

    /**
     * Tests when no prefix or suffix.
     */
    @Test
    public void testReplaceNoPrefixNoSuffix() throws IOException {
        doReplace("The animal jumps over the lazy dog.", "The animal jumps over the ${target}.", true);
    }

    /**
     * Tests when suffix but no prefix.
     */
    @Test
    public void testReplaceNoPrefixSuffix() throws IOException {
        doReplace("The animal} jumps over the lazy dog.", "The animal} jumps over the ${target}.", true);
    }

    /**
     * Tests replace with no variables.
     */
    @Test
    public void testReplaceNoVariables() throws IOException {
        doNotReplace("The balloon arrived.");
    }

    /**
     * Tests replace with null.
     */
    @Test
    public void testReplaceNull() throws IOException {
        doNotReplace(null);
    }

    /**
     * Tests simple key replace.
     */
    @Test
    public void testReplacePartialString_noReplace() {
        final StringSubstitutor sub = new StringSubstitutor();
        assertEqualsCharSeq("${animal} jumps", sub.replace(CLASSIC_TEMPLATE, 4, 15));
    }

    /**
     * Tests when prefix but no suffix.
     */
    @Test
    public void testReplacePrefixNoSuffix() throws IOException {
        doReplace("The ${animal jumps over the ${target} lazy dog.", "The ${animal jumps over the ${target} ${target}.",
            true);
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
        doReplace(CLASSIC_RESULT, CLASSIC_TEMPLATE, true);

        values.put("pet", "${petCharacteristicUnknown:-lazy} dog");
        doReplace(CLASSIC_RESULT, CLASSIC_TEMPLATE, true);
    }

    /**
     * Tests simple key replace.
     */
    @Test
    public void testReplaceSimple() throws IOException {
        doReplace(CLASSIC_RESULT, CLASSIC_TEMPLATE, true);
    }

    /**
     * Tests simple key replace.
     */
    @Test
    public void testReplaceSimpleKeySize1() throws IOException {
        doReplace("1", "${a}", false);
    }

    /**
     * Tests simple key replace.
     */
    @Test
    public void testReplaceSimpleKeySize2() throws IOException {
        doReplace("11", "${aa}", false);
    }

    /**
     * Tests simple key replace.
     */
    @Test
    public void testReplaceSimpleKeySize3() throws IOException {
        doReplace("111", "${aaa}", false);
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
        doReplace("The ${animal} jumps.", "The ${animal} jumps.", true);
    }

    /**
     * Tests unknown key replace.
     */
    @Test
    public void testReplaceUnknownKey() throws IOException {
        doReplace("The ${person} jumps over the lazy dog.", "The ${person} jumps over the ${target}.", true);
    }

    /**
     * Tests unknown key replace.
     */
    @Test
    public void testReplaceUnknownKeyDefaultValue() throws IOException {
        doReplace("The ${person} jumps over the lazy dog. 1234567890.",
            "The ${person} jumps over the ${target}. ${undefined.number:-1234567890}.", true);
    }

    /**
     * Tests unknown key replace.
     */
    @Test
    public void testReplaceUnknownKeyOnly() throws IOException {
        final String expected = "${person}";
        assertEqualsCharSeq(expected, replace(new StringSubstitutor(values), expected));
    }

    /**
     * Tests unknown key replace.
     */
    @Test
    public void testReplaceUnknownKeyOnlyExtraFirst() throws IOException {
        final String expected = ".${person}";
        assertEqualsCharSeq(expected, replace(new StringSubstitutor(values), expected));
    }

    /**
     * Tests unknown key replace.
     */
    @Test
    public void testReplaceUnknownKeyOnlyExtraLast() throws IOException {
        final String expected = "${person}.";
        assertEqualsCharSeq(expected, replace(new StringSubstitutor(values), expected));
    }

    /**
     * Tests unknown key replace.
     */
    @Test
    public void testReplaceUnknownShortestKeyOnly() throws IOException {
        final String expected = "${U}";
        assertEqualsCharSeq(expected, replace(new StringSubstitutor(values), expected));
    }

    /**
     * Tests unknown key replace.
     */
    @Test
    public void testReplaceUnknownShortestKeyOnlyExtraFirst() throws IOException {
        final String expected = ".${U}";
        assertEqualsCharSeq(expected, replace(new StringSubstitutor(values), expected));
    }

    /**
     * Tests unknown key replace.
     */
    @Test
    public void testReplaceUnknownShortestKeyOnlyExtraLast() throws IOException {
        final String expected = "${U}.";
        assertEqualsCharSeq(expected, replace(new StringSubstitutor(values), expected));
    }

    /**
     * Tests simple key replace.
     */
    @Test
    public void testReplaceVariablesCount1() throws IOException {
        doReplace(ACTUAL_ANIMAL, "${animal}", false);
    }

    /**
     * Tests escaping.
     */
    @Test
    public void testReplaceVariablesCount1Escaping2To1() throws IOException {
        doReplace("${a}", "$${a}", false);
        doReplace("${animal}", "$${animal}", false);
    }

    /**
     * Tests escaping.
     */
    @Test
    public void testReplaceVariablesCount1Escaping3To2() throws IOException {
        doReplace("$${a}", "$$${a}", false);
        doReplace("$${animal}", "$$${animal}", false);
    }

    /**
     * Tests escaping.
     */
    @Test
    public void testReplaceVariablesCount1Escaping4To3() throws IOException {
        doReplace("$$${a}", "$$$${a}", false);
        doReplace("$$${animal}", "$$$${animal}", false);
    }

    /**
     * Tests escaping.
     */
    @Test
    public void testReplaceVariablesCount1Escaping5To4() throws IOException {
        doReplace("$$$${a}", "$$$$${a}", false);
        doReplace("$$$${animal}", "$$$$${animal}", false);
    }

    /**
     * Tests simple key replace.
     */
    @Test
    public void testReplaceVariablesCount2() throws IOException {
        // doTestReplace("12", "${a}${b}", false);
        doReplace("1122", "${aa}${bb}", false);
        doReplace(ACTUAL_ANIMAL + ACTUAL_ANIMAL, "${animal}${animal}", false);
        doReplace(ACTUAL_TARGET + ACTUAL_TARGET, "${target}${target}", false);
        doReplace(ACTUAL_ANIMAL + ACTUAL_TARGET, "${animal}${target}", false);
    }

    /**
     * Tests simple key replace.
     */
    @Test
    public void testReplaceVariablesCount2NonAdjacent() throws IOException {
        doReplace("1 2", "${a} ${b}", false);
        doReplace("11 22", "${aa} ${bb}", false);
        doReplace(ACTUAL_ANIMAL + " " + ACTUAL_ANIMAL, "${animal} ${animal}", false);
        doReplace(ACTUAL_ANIMAL + " " + ACTUAL_ANIMAL, "${animal} ${animal}", false);
        doReplace(ACTUAL_ANIMAL + " " + ACTUAL_ANIMAL, "${animal} ${animal}", false);
    }

    /**
     * Tests simple key replace.
     */
    @Test
    public void testReplaceVariablesCount3() throws IOException {
        doReplace("121", "${a}${b}${a}", false);
        doReplace("112211", "${aa}${bb}${aa}", false);
        doReplace(ACTUAL_ANIMAL + ACTUAL_ANIMAL + ACTUAL_ANIMAL, "${animal}${animal}${animal}", false);
        doReplace(ACTUAL_TARGET + ACTUAL_TARGET + ACTUAL_TARGET, "${target}${target}${target}", false);
    }

    /**
     * Tests simple key replace.
     */
    @Test
    public void testReplaceVariablesCount3NonAdjacent() throws IOException {
        doReplace("1 2 1", "${a} ${b} ${a}", false);
        doReplace("11 22 11", "${aa} ${bb} ${aa}", false);
        doReplace(ACTUAL_ANIMAL + " " + ACTUAL_ANIMAL + " " + ACTUAL_ANIMAL, "${animal} ${animal} ${animal}", false);
        doReplace(ACTUAL_TARGET + " " + ACTUAL_TARGET + " " + ACTUAL_TARGET, "${target} ${target} ${target}", false);
    }

    /**
     * Tests interpolation with weird boundary patterns.
     */
    @Test
    public void testReplaceWeirdPattens() throws IOException {
        doNotReplace(StringUtils.EMPTY);
        doNotReplace(EMPTY_EXPR);
        doNotReplace("${ }");
        doNotReplace("${\t}");
        doNotReplace("${\n}");
        doNotReplace("${\b}");
        doNotReplace("${");
        doNotReplace("$}");
        doNotReplace("$$}");
        doNotReplace("}");
        doNotReplace("${}$");
        doNotReplace("${}$$");
        doNotReplace("${${");
        doNotReplace("${${}}");
        doNotReplace("${$${}}");
        doNotReplace("${$$${}}");
        doNotReplace("${$$${$}}");
        doNotReplace("${${}}");
        doNotReplace("${${ }}");
        //
        doNotReplace("${${a}}");
        doNotReplace("${$${a}}");
        doNotReplace("${$$${a}}");
        doNotReplace("${$$${a}}");
        doNotReplace("${${${a}");
    }

    /**
     * Tests interpolation with weird boundary patterns.
     */
    @Test
    @Disabled
    public void testReplaceWeirdPattensNo_JiraText178() throws IOException {
        doNotReplace("$${");
        doNotReplace("$${a");
        doNotReplace("$$${");
        doNotReplace("$$${a");
        doNotReplace("$${${a");
    }

    /**
     * Tests interpolation with weird boundary patterns.
     */
    @Test
    @Disabled
    public void testReplaceWeirdPattens_Partial_JiraText178() throws IOException {
        doReplace("${1}", "$${${a}}", false);
        doReplace("${12}", "$${${a}${b}}", false);
        doReplace("${${${a}2", "${${${a}${b}", false);
        doReplace("$${1", "$${${a}", false);
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
        assertEqualsCharSeq("Hi jakarta!", builder.toString());
    }

    @Test
    public void testSamePrefixAndSuffix() {
        final Map<String, String> map = new HashMap<>();
        map.put("greeting", "Hello");
        map.put(" there ", "XXX");
        map.put("name", "commons");
        assertEqualsCharSeq("Hi commons!", StringSubstitutor.replace("Hi @name@!", map, "@", "@"));
        assertEqualsCharSeq("Hello there commons!",
            StringSubstitutor.replace("@greeting@ there @name@!", map, "@", "@"));
    }

    /**
     * Tests static.
     */
    @Test
    public void testStaticReplace() {
        final Map<String, String> map = new HashMap<>();
        map.put("name", "commons");
        assertEqualsCharSeq("Hi commons!", StringSubstitutor.replace("Hi ${name}!", map));
    }

    /**
     * Tests static.
     */
    @Test
    public void testStaticReplacePrefixSuffix() {
        final Map<String, String> map = new HashMap<>();
        map.put("name", "commons");
        assertEqualsCharSeq("Hi commons!", StringSubstitutor.replace("Hi <name>!", map, "<", ">"));
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
        assertEqualsCharSeq(buf.toString(), StringSubstitutor.replaceSystemProperties(
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

        assertEqualsCharSeq("It works!", StringSubstitutor.replace(org, props));
    }

    @Test
    public void testSubstitutePreserveEscape() throws IOException {
        final String org = "${not-escaped} $${escaped}";
        final Map<String, String> map = new HashMap<>();
        map.put("not-escaped", "value");

        final StringSubstitutor sub = new StringSubstitutor(map, "${", "}", '$');
        assertFalse(sub.isPreserveEscapes());
        assertEqualsCharSeq("value ${escaped}", replace(sub, org));

        sub.setPreserveEscapes(true);
        assertTrue(sub.isPreserveEscapes());
        assertEqualsCharSeq("value $${escaped}", replace(sub, org));
    }

}
