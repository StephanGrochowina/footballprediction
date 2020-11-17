package de.coiaf.random.distributions.common;

import org.junit.Test;

import static org.junit.Assert.*;

public class DistributionElementFactoryTest {

    private static final double NEGATIVE_DISTRIBUTION_ELEMENT_VALUE = -1d;
    private static final double ZERO_DISTRIBUTION_ELEMENT_VALUE = 0d;
    private static final double POSITIVE_DISTRIBUTION_ELEMENT_VALUE = 1d;

    @Test(expected = IllegalArgumentException.class)
    public void createStandardDeviationZeroInclusive_negativeValue() {
        DistributionElementFactory.createStandardDeviationZeroInclusive(NEGATIVE_DISTRIBUTION_ELEMENT_VALUE);
    }
    @Test
    public void createStandardDeviationZeroInclusive_zeroValue() {
        StandardDeviation result = DistributionElementFactory.createStandardDeviationZeroInclusive(ZERO_DISTRIBUTION_ELEMENT_VALUE);

        assertNotNull(result);
        assertEquals(0d, result.doubleValue(), 0.0);
        assertTrue(result.isLowerBound());
        assertFalse(result.isUpperBound());
    }
    @Test
    public void createStandardDeviationZeroInclusive_positiveValue() {
        StandardDeviation result = DistributionElementFactory.createStandardDeviationZeroInclusive(POSITIVE_DISTRIBUTION_ELEMENT_VALUE);

        assertNotNull(result);
        assertTrue(0d < result.doubleValue());
        assertFalse(result.isLowerBound());
        assertFalse(result.isUpperBound());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createStandardDeviationZeroExclusive_negativeValue() {
        DistributionElementFactory.createStandardDeviationZeroExclusive(NEGATIVE_DISTRIBUTION_ELEMENT_VALUE);
    }
    @Test(expected = IllegalArgumentException.class)
    public void createStandardDeviationZeroExclusive_zeroValue() {
        DistributionElementFactory.createStandardDeviationZeroExclusive(ZERO_DISTRIBUTION_ELEMENT_VALUE);
    }
    @Test
    public void createStandardDeviationZeroExclusive_positiveValue() {
        StandardDeviation result = DistributionElementFactory.createStandardDeviationZeroExclusive(POSITIVE_DISTRIBUTION_ELEMENT_VALUE);

        assertNotNull(result);
        assertTrue(0d < result.doubleValue());
        assertFalse(result.isLowerBound());
        assertFalse(result.isUpperBound());
    }

    @Test(expected = NullPointerException.class)
    public void createStandardDeviation_nullVariance() {
        DistributionElementFactory.createStandardDeviation(null);
    }
    @Test
    public void createStandardDeviation_zeroVariance() {
        Variance variance = Variance.NO_VARIANCE;
        StandardDeviation result = DistributionElementFactory.createStandardDeviation(variance);

        assertNotNull(result);
        assertEquals(0d, result.doubleValue(), 0.0);
        assertTrue(result.isLowerBound());
        assertFalse(result.isUpperBound());
        assertSame(StandardDeviation.NO_DEVIATION, result);
    }
    @Test
    public void createStandardDeviation_positiveVariance() {
        Variance variance = new Variance(POSITIVE_DISTRIBUTION_ELEMENT_VALUE, true);
        StandardDeviation result = DistributionElementFactory.createStandardDeviation(variance);

        assertNotNull(result);
        assertTrue(0d < result.doubleValue());
        assertFalse(result.isLowerBound());
        assertFalse(result.isUpperBound());
    }
    @Test
    public void createStandardDeviation_infiniteVariance() {
        Variance variance = Variance.INFINITE_VARIANCE;
        StandardDeviation result = DistributionElementFactory.createStandardDeviation(variance);

        assertNotNull(result);
        assertTrue(0d < result.doubleValue());
        assertFalse(result.isLowerBound());
        assertTrue(result.isUpperBound());
        assertSame(StandardDeviation.INFINITE_DEVIATION, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createVarianceZeroInclusive_negativeValue() {
        DistributionElementFactory.createVarianceZeroInclusive(NEGATIVE_DISTRIBUTION_ELEMENT_VALUE);
    }
    @Test
    public void createVarianceZeroInclusive_zeroValue() {
        Variance result = DistributionElementFactory.createVarianceZeroInclusive(ZERO_DISTRIBUTION_ELEMENT_VALUE);

        assertNotNull(result);
        assertEquals(0d, result.doubleValue(), 0.0);
        assertTrue(result.isLowerBound());
        assertFalse(result.isUpperBound());
    }
    @Test
    public void createVarianceZeroInclusive_positiveValue() {
        Variance result = DistributionElementFactory.createVarianceZeroInclusive(POSITIVE_DISTRIBUTION_ELEMENT_VALUE);

        assertNotNull(result);
        assertTrue(0d < result.doubleValue());
        assertFalse(result.isLowerBound());
        assertFalse(result.isUpperBound());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createVarianceZeroExclusive_negativeValue() {
        DistributionElementFactory.createVarianceZeroExclusive(NEGATIVE_DISTRIBUTION_ELEMENT_VALUE);
    }
    @Test(expected = IllegalArgumentException.class)
    public void createVarianceZeroExclusive_zeroValue() {
        DistributionElementFactory.createVarianceZeroExclusive(ZERO_DISTRIBUTION_ELEMENT_VALUE);
    }
    @Test
    public void createVarianceZeroExclusive_positiveValue() {
        Variance result = DistributionElementFactory.createVarianceZeroExclusive(POSITIVE_DISTRIBUTION_ELEMENT_VALUE);

        assertNotNull(result);
        assertTrue(0d < result.doubleValue());
        assertFalse(result.isLowerBound());
        assertFalse(result.isUpperBound());
    }

    @Test(expected = NullPointerException.class)
    public void createVariance_nullStandardDeviation() {
        DistributionElementFactory.createVariance(null);
    }
    @Test
    public void createVariance_zeroStandardDeviation() {
        StandardDeviation standardDeviation = StandardDeviation.NO_DEVIATION;
        Variance result = DistributionElementFactory.createVariance(standardDeviation);

        assertNotNull(result);
        assertEquals(0d, result.doubleValue(), 0.0);
        assertTrue(result.isLowerBound());
        assertFalse(result.isUpperBound());
        assertSame(Variance.NO_VARIANCE, result);
    }
    @Test
    public void createVariance_positiveStandardDeviation() {
        StandardDeviation standardDeviation = new StandardDeviation(POSITIVE_DISTRIBUTION_ELEMENT_VALUE, true);
        Variance result = DistributionElementFactory.createVariance(standardDeviation);

        assertNotNull(result);
        assertTrue(0d < result.doubleValue());
        assertFalse(result.isLowerBound());
        assertFalse(result.isUpperBound());
    }
    @Test
    public void createVariance_infiniteStandardDeviation() {
        StandardDeviation standardDeviation = StandardDeviation.INFINITE_DEVIATION;
        Variance result = DistributionElementFactory.createVariance(standardDeviation);

        assertNotNull(result);
        assertTrue(0d < result.doubleValue());
        assertFalse(result.isLowerBound());
        assertTrue(result.isUpperBound());
        assertSame(Variance.INFINITE_VARIANCE, result);
    }
}