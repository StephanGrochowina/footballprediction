package de.coiaf.random.distributions.enumerated.goalEstimation;

import de.coiaf.random.distributions.common.DistributionElementFactory;
import de.coiaf.random.distributions.common.Variance;
import org.junit.Test;

import static org.junit.Assert.*;

public class KappaVarianceEstimationFunctionTest {

    private static final double LARGE_VALUE = Double.MAX_VALUE;

    private static final double ILLEGAL_DISTRIBUTION_ELEMENT_VALUE = -1.0d;
    private static final double POSITIVE_DISTRIBUTION_ELEMENT_VALUE = 1.0d;

    private static final Variance NO_VARIANCE = Variance.NO_VARIANCE;
    private static final Variance MINIMUM_NON_ZERO_VARIANCE = Variance.MINIMUM_NON_ZERO_VARIANCE;
    private static final Variance POSITIVE_VARIANCE = DistributionElementFactory.createVarianceZeroExclusive(POSITIVE_DISTRIBUTION_ELEMENT_VALUE);
    private static final Variance LARGE_VARIANCE = DistributionElementFactory.createVarianceZeroExclusive(LARGE_VALUE);
    private static final Variance INFINITE_VARIANCE = Variance.INFINITE_VARIANCE;

    private static final int ILLEGAL_MATCHES_VALUE = -1;
    private static final int POSITIVE_MATCHES_VALUE = 10;

    private static final PlayedMatches NO_MATCHES = PlayedMatches.NO_PLAYED_MATCHES;
    private static final PlayedMatches POSITIVE_MATCHES = PlayedMatches.valueOf(POSITIVE_MATCHES_VALUE);
    private static final PlayedMatches LARGE_MATCHES = PlayedMatches.valueOf(LARGE_VALUE);
    private static final PlayedMatches INFINITE_MATCHES = PlayedMatches.valueOf(Double.POSITIVE_INFINITY);

    @Test(expected = IllegalArgumentException.class)
    public void calculateVarianceByObservedStandardDeviation_illegalStandardDeviation_legalMatches() {
        KappaVarianceEstimationFunction.calculateVarianceByObservedStandardDeviation(ILLEGAL_DISTRIBUTION_ELEMENT_VALUE, POSITIVE_MATCHES_VALUE);
    }
    @Test(expected = IllegalArgumentException.class)
    public void calculateVarianceByObservedStandardDeviation_legalStandardDeviation_illegalMatches() {
        KappaVarianceEstimationFunction.calculateVarianceByObservedStandardDeviation(POSITIVE_DISTRIBUTION_ELEMENT_VALUE, ILLEGAL_MATCHES_VALUE);
    }
    @Test
    public void calculateVarianceByObservedStandardDeviation_legalStandardDeviation_legalMatches() {
        Variance result = KappaVarianceEstimationFunction.calculateVarianceByObservedStandardDeviation(POSITIVE_DISTRIBUTION_ELEMENT_VALUE, POSITIVE_MATCHES_VALUE);

        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void calculateVarianceByObservedVariance_illegalVariance_legalMatches() {
        KappaVarianceEstimationFunction.calculateVarianceByObservedVariance(ILLEGAL_DISTRIBUTION_ELEMENT_VALUE, POSITIVE_MATCHES_VALUE);
    }
    @Test(expected = IllegalArgumentException.class)
    public void calculateVarianceByObservedVariance_legalVariance_illegalMatches() {
        KappaVarianceEstimationFunction.calculateVarianceByObservedVariance(POSITIVE_DISTRIBUTION_ELEMENT_VALUE, ILLEGAL_MATCHES_VALUE);
    }
    @Test
    public void calculateVarianceByObservedVariance_legalVariance_legalMatches() {
        Variance result = KappaVarianceEstimationFunction.calculateVarianceByObservedVariance(POSITIVE_DISTRIBUTION_ELEMENT_VALUE, POSITIVE_MATCHES_VALUE);

        assertNotNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void calculateVariance_givenVariance_nullMatches() {
        KappaVarianceEstimationFunction.calculateVariance(POSITIVE_VARIANCE, null);
    }
    @Test(expected = NullPointerException.class)
    public void calculateVariance_nullVariance_givenMatches() {
        KappaVarianceEstimationFunction.calculateVariance(null, POSITIVE_MATCHES);
    }

    @Test
    public void calculateVariance_noVariance_noMatches() {
        Variance result = KappaVarianceEstimationFunction.calculateVariance(NO_VARIANCE, NO_MATCHES);

        assertNotNull(result);
        assertFalse(result.isLowerBound());
        assertTrue(result.isUpperBound());
    }
    @Test
    public void calculateVariance_noVariance_positiveMatches() {
        Variance expected = DistributionElementFactory.createVarianceZeroExclusive(
                Math.pow(1.0d/(1.0d + POSITIVE_MATCHES.doubleValue()), 2));
        Variance result = KappaVarianceEstimationFunction.calculateVariance(NO_VARIANCE, POSITIVE_MATCHES);

        assertNotNull(result);
        assertFalse(result.isLowerBound());
        assertFalse(result.isUpperBound());
        assertEquals(expected, result);
    }
    @Test
    public void calculateVariance_noVariance_largeMatches() {
        Variance result = KappaVarianceEstimationFunction.calculateVariance(NO_VARIANCE, LARGE_MATCHES);

        assertNotNull(result);
        assertFalse(result.isLowerBound());
        assertFalse(result.isUpperBound());
        assertEquals(MINIMUM_NON_ZERO_VARIANCE, result);
    }
    @Test
    public void calculateVariance_noVariance_infiniteMatches() {
        Variance result = KappaVarianceEstimationFunction.calculateVariance(NO_VARIANCE, INFINITE_MATCHES);

        assertNotNull(result);
        assertFalse(result.isLowerBound());
        assertFalse(result.isUpperBound());
        assertEquals(MINIMUM_NON_ZERO_VARIANCE, result);
    }

    @Test
    public void calculateVariance_positiveVariance_noMatches() {
        Variance result = KappaVarianceEstimationFunction.calculateVariance(POSITIVE_VARIANCE, NO_MATCHES);

        assertNotNull(result);
        assertFalse(result.isLowerBound());
        assertTrue(result.isUpperBound());
    }
    @Test
    public void calculateVariance_positiveVariance_positiveMatches() {
        Variance result = KappaVarianceEstimationFunction.calculateVariance(POSITIVE_VARIANCE, POSITIVE_MATCHES);

        assertNotNull(result);
        assertFalse(result.isLowerBound());
        assertFalse(result.isUpperBound());
        assertTrue(MINIMUM_NON_ZERO_VARIANCE.doubleValue() < result.doubleValue());
    }
    @Test
    public void calculateVariance_positiveVariance_largeMatches() {
        Variance result = KappaVarianceEstimationFunction.calculateVariance(POSITIVE_VARIANCE, LARGE_MATCHES);

        assertNotNull(result);
        assertFalse(result.isLowerBound());
        assertFalse(result.isUpperBound());
        assertTrue(MINIMUM_NON_ZERO_VARIANCE.doubleValue() < result.doubleValue());
    }
    @Test
    public void calculateVariance_positiveVariance_infiniteMatches() {
        Variance result = KappaVarianceEstimationFunction.calculateVariance(POSITIVE_VARIANCE, INFINITE_MATCHES);

        assertNotNull(result);
        assertFalse(result.isLowerBound());
        assertFalse(result.isUpperBound());
        assertTrue(MINIMUM_NON_ZERO_VARIANCE.doubleValue() < result.doubleValue());
        assertEquals(POSITIVE_VARIANCE, result);
    }

    @Test
    public void calculateVariance_largeVariance_noMatches() {
        Variance result = KappaVarianceEstimationFunction.calculateVariance(LARGE_VARIANCE, NO_MATCHES);

        assertNotNull(result);
        assertFalse(result.isLowerBound());
        assertTrue(result.isUpperBound());
    }
    @Test
    public void calculateVariance_largeVariance_positiveMatches() {
        Variance result = KappaVarianceEstimationFunction.calculateVariance(LARGE_VARIANCE, POSITIVE_MATCHES);

        assertNotNull(result);
        assertFalse(result.isLowerBound());
        assertTrue(result.isUpperBound());
    }
    @Test
    public void calculateVariance_largeVariance_largeMatches() {
        Variance result = KappaVarianceEstimationFunction.calculateVariance(LARGE_VARIANCE, LARGE_MATCHES);

        assertNotNull(result);
        assertFalse(result.isLowerBound());
        assertTrue(result.isUpperBound());
    }
    @Test
    public void calculateVariance_largeVariance_infiniteMatches() {
        Variance result = KappaVarianceEstimationFunction.calculateVariance(LARGE_VARIANCE, INFINITE_MATCHES);

        assertNotNull(result);
        assertFalse(result.isLowerBound());
        assertFalse(result.isUpperBound());
        assertTrue(MINIMUM_NON_ZERO_VARIANCE.doubleValue() < result.doubleValue());
        assertEquals(LARGE_VARIANCE, result);
    }

    @Test
    public void calculateVariance_infiniteVariance_noMatches() {
        Variance result = KappaVarianceEstimationFunction.calculateVariance(INFINITE_VARIANCE, NO_MATCHES);

        assertNotNull(result);
        assertFalse(result.isLowerBound());
        assertTrue(result.isUpperBound());
    }
    @Test
    public void calculateVariance_infiniteVariance_positiveMatches() {
        Variance result = KappaVarianceEstimationFunction.calculateVariance(INFINITE_VARIANCE, POSITIVE_MATCHES);

        assertNotNull(result);
        assertFalse(result.isLowerBound());
        assertTrue(result.isUpperBound());
    }
    @Test
    public void calculateVariance_infiniteVariance_largeMatches() {
        Variance result = KappaVarianceEstimationFunction.calculateVariance(INFINITE_VARIANCE, LARGE_MATCHES);

        assertNotNull(result);
        assertFalse(result.isLowerBound());
        assertTrue(result.isUpperBound());
    }
    @Test
    public void calculateVariance_infiniteVariance_infiniteMatches() {
        Variance result = KappaVarianceEstimationFunction.calculateVariance(INFINITE_VARIANCE, INFINITE_MATCHES);

        assertNotNull(result);
        assertFalse(result.isLowerBound());
        assertTrue(result.isUpperBound());
    }
}