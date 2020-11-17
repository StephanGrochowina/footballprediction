package de.coiaf.random.distributions.enumerated.goalEstimation;

import de.coiaf.random.distributions.common.StandardDeviation;
import de.coiaf.random.distributions.common.Variance;
import de.coiaf.random.distributions.continuous.ContinuousDistribution;
import de.coiaf.random.distributions.continuous.MeanBasedContinuousDistributionFunction;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class KappaDistributionBuilderTest {

    private static final Variance VARIANCE_MOCK = mock(Variance.class);
    private static final StandardDeviation STANDARD_DEVIATION_MOCK = mock(StandardDeviation.class);
    private static final int GIVEN_MATCHES = 42;
    private static final ContinuousDistribution DISTRIBUTION_MOCK = mock(ContinuousDistribution.class);

    @Test(expected = NullPointerException.class)
    public void applyHomeVariance_nullVariance_givenMatches() {
        KappaDistributionBuilder.createBuilder()
                .applyHomeVariance(null, GIVEN_MATCHES);
    }
    @Test
    public void applyHomeVariance_givenVariance_givenMatches() {
        KappaDistributionBuilder builder = KappaDistributionBuilder.createBuilder()
                .applyHomeVariance(VARIANCE_MOCK, GIVEN_MATCHES);

        assertNotNull(builder);
    }

    @Test(expected = NullPointerException.class)
    public void applyHomeStandardDeviation_nullStandardDeviation_givenMatches() {
        KappaDistributionBuilder.createBuilder()
                .applyHomeStandardDeviation(null, GIVEN_MATCHES);
    }
    @Test
    public void applyHomeStandardDeviation_givenStandardDeviation_givenMatches() {
        KappaDistributionBuilder.createBuilder()
                .applyHomeStandardDeviation(STANDARD_DEVIATION_MOCK, GIVEN_MATCHES);
    }

    @Test(expected = NullPointerException.class)
    public void applyAwayVariance_nullVariance_givenMatches() {
        KappaDistributionBuilder.createBuilder()
                .applyAwayVariance(null, GIVEN_MATCHES);
    }
    @Test
    public void applyAwayVariance_givenVariance_givenMatches() {
        KappaDistributionBuilder builder = KappaDistributionBuilder.createBuilder()
                .applyAwayVariance(VARIANCE_MOCK, GIVEN_MATCHES);

        assertNotNull(builder);
    }

    @Test(expected = NullPointerException.class)
    public void applyAwayStandardDeviation_nullStandardDeviation_givenMatches() {
        KappaDistributionBuilder.createBuilder()
                .applyAwayStandardDeviation(null, GIVEN_MATCHES);
    }
    @Test
    public void applyAwayStandardDeviation_givenStandardDeviation_givenMatches() {
        KappaDistributionBuilder.createBuilder()
                .applyAwayStandardDeviation(STANDARD_DEVIATION_MOCK, GIVEN_MATCHES);
    }

    @Test(expected = NullPointerException.class)
    public void buildHomeDistribution_nullDistributionFunction() {
        KappaDistributionBuilder.createBuilder()
                .buildHomeDistribution(null);
    }
    @Test(expected = NullPointerException.class)
    public void buildHomeDistribution_givenDistributionFunctionReturningNullDistribution() {
        MeanBasedContinuousDistributionFunction distributionFunctionMock = mock(MeanBasedContinuousDistributionFunction.class);

        KappaDistributionBuilder.createBuilder()
                .buildHomeDistribution(distributionFunctionMock);
    }
    @Test
    public void buildHomeDistribution_givenDistributionFunctionReturningGivenDistribution() {
        MeanBasedContinuousDistributionFunction distributionFunctionMock = mock(MeanBasedContinuousDistributionFunction.class);

        when(distributionFunctionMock.createDistribution(any(Number.class), any(Variance.class))).thenReturn(DISTRIBUTION_MOCK);

        ContinuousDistribution result = KappaDistributionBuilder.createBuilder()
                .buildHomeDistribution(distributionFunctionMock);

        assertNotNull(result);
        assertEquals(DISTRIBUTION_MOCK, result);

        verify(distributionFunctionMock, times(1)).createDistribution(any(Number.class), any(Variance.class));
    }

    @Test(expected = NullPointerException.class)
    public void buildAwayDistribution_nullDistributionFunction() {
        KappaDistributionBuilder.createBuilder()
                .buildAwayDistribution(null);
    }
    @Test(expected = NullPointerException.class)
    public void buildAwayDistribution_givenDistributionFunctionReturningNullDistribution() {
        MeanBasedContinuousDistributionFunction distributionFunctionMock = mock(MeanBasedContinuousDistributionFunction.class);

        KappaDistributionBuilder.createBuilder()
                .buildAwayDistribution(distributionFunctionMock);
    }
    @Test
    public void buildAwayDistribution_givenDistributionFunctionReturningGivenDistribution() {
        MeanBasedContinuousDistributionFunction distributionFunctionMock = mock(MeanBasedContinuousDistributionFunction.class);

        when(distributionFunctionMock.createDistribution(any(Number.class), any(Variance.class))).thenReturn(DISTRIBUTION_MOCK);

        ContinuousDistribution result = KappaDistributionBuilder.createBuilder()
                .buildAwayDistribution(distributionFunctionMock);

        assertNotNull(result);
        assertEquals(DISTRIBUTION_MOCK, result);

        verify(distributionFunctionMock, times(1)).createDistribution(any(Number.class), any(Variance.class));
    }
}