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

package org.apache.commons.text.jmh;

import org.apache.commons.text.TextStringBuilder;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Fork(value = 1)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
public class TextStringBuilderBenchmark {

    @Param({"10000"})
    private int size;

    @Param("0")
    private int minExp;

    @Param("100")
    private int maxExp;

    private TextStringBuilder textStringBuilder;

    @Setup(Level.Iteration)
    public void setup() {

        textStringBuilder = new TextStringBuilder("Performance tests are implemented using JMH to stress the most cumbersome components of the project:");
    }

    @Benchmark
    public void append() {
        for (int i = minExp; i < maxExp; i++) {
            textStringBuilder.append(i);
        }
    }

    @Benchmark
    public void contains() {
        for (int i = minExp; i < maxExp; i++) {
            textStringBuilder.contains("Hello World");
        }
    }

    @Benchmark
    public void lastIndexOf() {
        textStringBuilder.lastIndexOf("Java Microbenchmark Harness");
    }

    @Benchmark
    public void startsWith() {
        textStringBuilder.startsWith("JMH");
    }

    @Benchmark
    public void substring() {
        textStringBuilder.substring(minExp, maxExp);
    }

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }
}