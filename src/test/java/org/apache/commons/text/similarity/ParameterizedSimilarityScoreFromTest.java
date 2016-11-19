package org.apache.commons.text.similarity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Unit tests for {@link org.apache.commons.text.similarity.SimilarityScoreFrom}.
 *
 * @param <R> The {@link SimilarityScore} return type.
 */
@RunWith(Parameterized.class)
public class ParameterizedSimilarityScoreFromTest<R> {

    private final SimilarityScore<R> similarityScore;
    private final CharSequence left;
    private final CharSequence right;
    private final R distance;

    public ParameterizedSimilarityScoreFromTest(
            final SimilarityScore<R> similarityScore,
            final CharSequence left, final CharSequence right,
            final R distance) {

        this.similarityScore = similarityScore;
        this.left = left;
        this.right = right;
        this.distance = distance;
    }

    @Parameters
    public static Iterable<Object[]> parameters() {
        return Arrays.asList( new Object[][] {

                { new JaroWinklerDistance(), "elephant", "hippo", 0.44 },
                { new JaroWinklerDistance(), "hippo", "elephant",  0.44 },
                { new JaroWinklerDistance(), "hippo", "zzzzzzzz", 0.0 },

                {
                        new SimilarityScore<Boolean>() {
                            @Override
                            public Boolean apply(final CharSequence left, final CharSequence right) {
                                return left == right || (left != null && left.equals(right));
                            }
                        },
                        "Bob's your uncle.",
                        "Every good boy does fine.",
                        false
                }

        } );
    }

    @Test
    public void test() {
        final SimilarityScoreFrom<R> similarityScoreFrom = new SimilarityScoreFrom<R>(similarityScore, left);
        assertThat(similarityScoreFrom.apply(right), equalTo(distance));
    }
}
