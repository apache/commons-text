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

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Unit tests for {@link org.apache.commons.text.similarity.StringMetricFrom}.
 */
@RunWith(Parameterized.class)
public class ParameterizedStringMetricFromTest {

    private final StringMetric<Number> metric;
    private final CharSequence left;
    private final CharSequence right;
    private final Number distance;

    public ParameterizedStringMetricFromTest(
        final StringMetric<Number> metric,
        final CharSequence left, final CharSequence right,
        final Number distance) {

        this.metric = metric;
        this.left = left;
        this.right = right;
        this.distance = distance;
    }

    @Parameters
    public static Iterable<Object[]> parameters() {
        return Arrays.asList( new Object[][] {

            { new LevenshteinDistance(), "Apache", "a patchy", 4 }
            /* TODO - add lots more! */

        } );
    }

    @Test
    public void test() {
        StringMetricFrom<Number> metricFrom = new StringMetricFrom<Number>(metric, left);
        assertThat(metricFrom.apply(right), equalTo(distance));
    }

}
