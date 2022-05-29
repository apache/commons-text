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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link LongestCommonSubsequence}.
 */
public class LongestCommonSubsequenceTest {

    private static LongestCommonSubsequence subject;

    @BeforeAll
    public static void setup() {
        subject = new LongestCommonSubsequence();
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testGettingLogestCommonSubsequenceNullNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> subject.logestCommonSubsequence(null, null));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testGettingLogestCommonSubsequenceNullString() {
        assertThatIllegalArgumentException().isThrownBy(() -> subject.logestCommonSubsequence(null, "right"));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testGettingLogestCommonSubsequenceStringNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> subject.logestCommonSubsequence(" ", null));
    }

    @Test
    public void testGettingLongestCommonSubsequenceApplyNullNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> subject.apply(null, null));
    }

    @Test
    public void testGettingLongestCommonSubsequenceApplyNullString() {
        assertThatIllegalArgumentException().isThrownBy(() -> subject.apply(null, "right"));
    }

    @Test
    public void testGettingLongestCommonSubsequenceApplyStringNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> subject.apply(" ", null));
    }

    @Test
    public void testGettingLongestCommonSubsequenceNullNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> subject.longestCommonSubsequence(null, null));
    }

    @Test
    public void testGettingLongestCommonSubsequenceNullString() {
        assertThatIllegalArgumentException().isThrownBy(() -> subject.longestCommonSubsequence(null, "right"));
    }

    @Test
    public void testGettingLongestCommonSubsequenceStringNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> subject.longestCommonSubsequence(" ", null));
    }

    @Test
    @Deprecated
    public void testLogestCommonSubsequence() {
        assertThat(subject.logestCommonSubsequence("", "")).isEqualTo("");
        assertThat(subject.logestCommonSubsequence("left", "")).isEqualTo("");
        assertThat(subject.logestCommonSubsequence("", "right")).isEqualTo("");
        assertThat(subject.logestCommonSubsequence("frog", "fog")).isEqualTo("fog");
        assertThat(subject.logestCommonSubsequence("fly", "ant")).isEqualTo("");
        assertThat(subject.logestCommonSubsequence("elephant", "hippo")).isEqualTo("h");
        assertThat(subject.logestCommonSubsequence("ABC Corporation", "ABC Corp")).isEqualTo("ABC Corp");
        assertThat(subject.logestCommonSubsequence("D N H Enterprises Inc", "D & H Enterprises, Inc."))
            .isEqualTo("D  H Enterprises Inc");
        assertThat(subject.logestCommonSubsequence("My Gym Children's Fitness Center", "My Gym. Childrens Fitness"))
            .isEqualTo("My Gym Childrens Fitness");
        assertThat(subject.logestCommonSubsequence("PENNSYLVANIA", "PENNCISYLVNIA")).isEqualTo("PENNSYLVNIA");
        assertThat(subject.logestCommonSubsequence("left", "right")).isEqualTo("t");
        assertThat(subject.logestCommonSubsequence("leettteft", "ritttght")).isEqualTo("tttt");
        assertThat(subject.logestCommonSubsequence("the same string", "the same string")).isEqualTo("the same string");
    }

    @Test
    public void testLongestCommonSubsequence() {
        assertThat(subject.longestCommonSubsequence("", "")).isEqualTo("");
        assertThat(subject.longestCommonSubsequence("left", "")).isEqualTo("");
        assertThat(subject.longestCommonSubsequence("", "right")).isEqualTo("");
        assertThat(subject.longestCommonSubsequence("frog", "fog")).isEqualTo("fog");
        assertThat(subject.longestCommonSubsequence("fly", "ant")).isEqualTo("");
        assertThat(subject.longestCommonSubsequence("elephant", "hippo")).isEqualTo("h");
        assertThat(subject.longestCommonSubsequence("ABC Corporation", "ABC Corp")).isEqualTo("ABC Corp");
        assertThat(subject.longestCommonSubsequence("D N H Enterprises Inc", "D & H Enterprises, Inc."))
            .isEqualTo("D  H Enterprises Inc");
        assertThat(subject.longestCommonSubsequence("My Gym Children's Fitness Center", "My Gym. Childrens Fitness"))
            .isEqualTo("My Gym Childrens Fitness");
        assertThat(subject.longestCommonSubsequence("PENNSYLVANIA", "PENNCISYLVNIA")).isEqualTo("PENNSYLVNIA");
        assertThat(subject.longestCommonSubsequence("left", "right")).isEqualTo("t");
        assertThat(subject.longestCommonSubsequence("leettteft", "ritttght")).isEqualTo("tttt");
        assertThat(subject.longestCommonSubsequence("the same string", "the same string")).isEqualTo("the same string");
    }

    @Test
    public void testLongestCommonSubsequenceApply() {
        assertThat(subject.apply("", "")).isEqualTo(0);
        assertThat(subject.apply("left", "")).isEqualTo(0);
        assertThat(subject.apply("", "right")).isEqualTo(0);
        assertThat(subject.apply("frog", "fog")).isEqualTo(3);
        assertThat(subject.apply("fly", "ant")).isEqualTo(0);
        assertThat(subject.apply("elephant", "hippo")).isEqualTo(1);
        assertThat(subject.apply("ABC Corporation", "ABC Corp")).isEqualTo(8);
        assertThat(subject.apply("D N H Enterprises Inc", "D & H Enterprises, Inc.")).isEqualTo(20);
        assertThat(subject.apply("My Gym Children's Fitness Center", "My Gym. Childrens Fitness")).isEqualTo(24);
        assertThat(subject.apply("PENNSYLVANIA", "PENNCISYLVNIA")).isEqualTo(11);
        assertThat(subject.apply("left", "right")).isEqualTo(1);
        assertThat(subject.apply("leettteft", "ritttght")).isEqualTo(4);
        assertThat(subject.apply("the same string", "the same string")).isEqualTo(15);
    }
}
