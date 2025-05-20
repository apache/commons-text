/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.text.diff;

/**
 * <p>
 * It is guaranteed that the comparisons will always be done as
 * {@code o1.equals(o2)} where {@code o1} belongs to the first
 * sequence and {@code o2} belongs to the second sequence. This can
 * be important if subclassing is used for some elements in the first
 * sequence and the {@code equals} method is specialized.
 * </p>
 * <p>
 * Comparison can be seen from two points of view: either as giving the smallest
 * modification allowing to transform the first sequence into the second one, or
 * as giving the longest sequence which is a subsequence of both initial
 * sequences. The {@code equals} method is used to compare objects, so any
 * object can be put into sequences. Modifications include deleting, inserting
 * or keeping one object, starting from the beginning of the first sequence.
 * </p>
 * <p>
 * This class implements the comparison algorithm, which is the very efficient
 * algorithm from Eugene W. Myers
 * <a href="http://www.cis.upenn.edu/~bcpierce/courses/dd/papers/diff.ps">
 * An O(ND) Difference Algorithm and Its Variations</a>. This algorithm produces
 * the shortest possible {@link EditScript edit script} containing all the
 * {@link EditCommand commands} needed to transform the first sequence into
 * the second one.
 *
 * <p>
 * This code has been adapted from Apache Commons Collections 4.0.
 * </p>
 *
 * @see EditScript
 * @see EditCommand
 * @see CommandVisitor
 * @since 1.0
 */
public class StringsComparator {

    /**
     * This class is a simple placeholder to hold the end part of a path
     * under construction in a {@link StringsComparator StringsComparator}.
     */
    private static final class Snake {

        /** Start index. */
        private final int start;

        /** End index. */
        private final int end;

        /** Diagonal number. */
        private final int diag;

        /**
         * Constructs a new instance of Snake with specified indices.
         *
         * @param start  start index of the snake
         * @param end  end index of the snake
         * @param diag  diagonal number
         */
        Snake(final int start, final int end, final int diag) {
            this.start = start;
            this.end   = end;
            this.diag  = diag;
        }

        /**
         * Gets the diagonal number of the snake.
         *
         * @return diagonal number of the snake
         */
        public int getDiag() {
            return diag;
        }

        /**
         * Gets the end index of the snake.
         *
         * @return end index of the snake
         */
        public int getEnd() {
            return end;
        }

        /**
         * Gets the start index of the snake.
         *
         * @return start index of the snake
         */
        public int getStart() {
            return start;
        }
    }
    /**
     * First character sequence.
     */
    private final String left;
    /**
     * Second character sequence.
     */
    private final String right;
    /**
     * Temporary array.
     */
    private final int[] vDown;

    /**
     * Temporary array.
     */
    private final int[] vUp;

    /**
     * Constructs a new instance of StringsComparator.
     * <p>
     * It is <em>guaranteed</em> that the comparisons will always be done as
     * {@code o1.equals(o2)} where {@code o1} belongs to the first
     * sequence and {@code o2} belongs to the second sequence. This can be
     * important if subclassing is used for some elements in the first sequence
     * and the {@code equals} method is specialized.
     * </p>
     *
     * @param left first character sequence to be compared
     * @param right second character sequence to be compared
     */
    public StringsComparator(final String left, final String right) {
        this.left = left;
        this.right = right;

        final int size = left.length() + right.length() + 2;
        vDown = new int[size];
        vUp   = new int[size];
    }

    /**
     * Builds an edit script.
     *
     * @param start1  the begin of the first sequence to be compared
     * @param end1  the end of the first sequence to be compared
     * @param start2  the begin of the second sequence to be compared
     * @param end2  the end of the second sequence to be compared
     * @param script the edited script
     */
    private void buildScript(final int start1, final int end1, final int start2, final int end2,
            final EditScript<Character> script) {
        final Snake middle = getMiddleSnake(start1, end1, start2, end2);

        if (middle == null
                || middle.getStart() == end1 && middle.getDiag() == end1 - end2
                || middle.getEnd() == start1 && middle.getDiag() == start1 - start2) {

            int i = start1;
            int j = start2;
            while (i < end1 || j < end2) {
                if (i < end1 && j < end2 && left.charAt(i) == right.charAt(j)) {
                    script.append(new KeepCommand<>(left.charAt(i)));
                    ++i;
                    ++j;
                } else if (end1 - start1 > end2 - start2) {
                    script.append(new DeleteCommand<>(left.charAt(i)));
                    ++i;
                } else {
                    script.append(new InsertCommand<>(right.charAt(j)));
                    ++j;
                }
            }

        } else {

            buildScript(start1, middle.getStart(),
                        start2, middle.getStart() - middle.getDiag(),
                        script);
            for (int i = middle.getStart(); i < middle.getEnd(); ++i) {
                script.append(new KeepCommand<>(left.charAt(i)));
            }
            buildScript(middle.getEnd(), end1,
                        middle.getEnd() - middle.getDiag(), end2,
                        script);
        }
    }

    /**
     * Builds a snake.
     *
     * @param start  the value of the start of the snake
     * @param diag  the value of the diagonal of the snake
     * @param end1  the value of the end of the first sequence to be compared
     * @param end2  the value of the end of the second sequence to be compared
     * @return The snake built
     */
    private Snake buildSnake(final int start, final int diag, final int end1, final int end2) {
        int end = start;
        while (end - diag < end2
                && end < end1
                && left.charAt(end) == right.charAt(end - diag)) {
            ++end;
        }
        return new Snake(start, end, diag);
    }

    /**
     * Gets the middle snake corresponding to two subsequences of the
     * main sequences.
     * <p>
     * The snake is found using the MYERS Algorithm (this algorithms has
     * also been implemented in the GNU diff program). This algorithm is
     * explained in Eugene Myers article:
     * <a href="http://www.cs.arizona.edu/people/gene/PAPERS/diff.ps">
     * An O(ND) Difference Algorithm and Its Variations</a>.
     * </p>
     *
     * @param start1  the begin of the first sequence to be compared
     * @param end1  the end of the first sequence to be compared
     * @param start2  the begin of the second sequence to be compared
     * @param end2  the end of the second sequence to be compared
     * @return The middle snake
     */
    private Snake getMiddleSnake(final int start1, final int end1, final int start2, final int end2) {
        // Myers Algorithm
        // Initializations
        final int m = end1 - start1;
        final int n = end2 - start2;
        if (m == 0 || n == 0) {
            return null;
        }

        final int delta  = m - n;
        final int sum    = n + m;
        final int offset = (sum % 2 == 0 ? sum : sum + 1) / 2;
        vDown[1 + offset] = start1;
        vUp[1 + offset]   = end1 + 1;

        for (int d = 0; d <= offset; ++d) {
            // Down
            for (int k = -d; k <= d; k += 2) {
                // First step

                final int i = k + offset;
                if (k == -d || k != d && vDown[i - 1] < vDown[i + 1]) {
                    vDown[i] = vDown[i + 1];
                } else {
                    vDown[i] = vDown[i - 1] + 1;
                }

                int x = vDown[i];
                int y = x - start1 + start2 - k;

                while (x < end1 && y < end2 && left.charAt(x) == right.charAt(y)) {
                    vDown[i] = ++x;
                    ++y;
                }
                // Second step
                if (delta % 2 != 0 && delta - d <= k && k <= delta + d && vUp[i - delta] <= vDown[i]) { // NOPMD
                    return buildSnake(vUp[i - delta], k + start1 - start2, end1, end2);
                }
            }

            // Up
            for (int k = delta - d; k <= delta + d; k += 2) {
                // First step
                final int i = k + offset - delta;
                if (k == delta - d
                        || k != delta + d && vUp[i + 1] <= vUp[i - 1]) {
                    vUp[i] = vUp[i + 1] - 1;
                } else {
                    vUp[i] = vUp[i - 1];
                }

                int x = vUp[i] - 1;
                int y = x - start1 + start2 - k;
                while (x >= start1 && y >= start2
                        && left.charAt(x) == right.charAt(y)) {
                    vUp[i] = x--;
                    y--;
                }
                // Second step
                if (delta % 2 == 0 && -d <= k && k <= d && vUp[i] <= vDown[i + delta]) { // NOPMD
                    return buildSnake(vUp[i], k + start1 - start2, end1, end2);
                }
            }
        }

        // this should not happen
        throw new IllegalStateException("Internal Error");
    }

    /**
     * Gets the {@link EditScript} object.
     * <p>
     * It is guaranteed that the objects embedded in the {@link InsertCommand
     * insert commands} come from the second sequence and that the objects
     * embedded in either the {@link DeleteCommand delete commands} or
     * {@link KeepCommand keep commands} come from the first sequence. This can
     * be important if subclassing is used for some elements in the first
     * sequence and the {@code equals} method is specialized.
     * </p>
     *
     * @return The edit script resulting from the comparison of the two
     *         sequences
     */
    public EditScript<Character> getScript() {
        final EditScript<Character> script = new EditScript<>();
        buildScript(0, left.length(), 0, right.length(), script);
        return script;
    }

}
