package de.coiaf.random.distributions.continuous;

import de.coiaf.random.distributions.AbstractDistributionAdapter;
import org.apache.commons.math3.distribution.RealDistribution;

/**
 * @author Stephan Grochowina <stephan.grochowina@web.de>
 */
public abstract class AbstractContinuousDistributionAdapter extends AbstractDistributionAdapter<Double> implements ContinuousDistribution {

    private final RealDistribution delegate;

    protected AbstractContinuousDistributionAdapter(RealDistribution delegate) {
        if (delegate == null) {
            throw new IllegalArgumentException("Parameter delegate should not be null.");
        }

        this.delegate = delegate;
    }

    @Override
    protected double calculateDensity(Double value) {
        return this.delegate.density(value);
    }

    @Override
    public Double getVariance() {
        double variance = this.delegate.getNumericalVariance();

        return Double.NaN == variance || Double.POSITIVE_INFINITY == variance ? null : variance;
    }

    @Override
    public Double getStandardDeviation() {
        Double variance = this.getVariance();

        return variance == null || variance < 0.0 ? null : Math.sqrt(variance);
    }

    @Override
    public Double getMean() {
        return this.getExpectationValue();
    }

    @Override
    public Double getExpectationValue() {
        double mean = this.delegate.getNumericalMean();

        return (Double.NaN == mean) ? null : mean;
    }

    @Override
    protected double calculateDistribution(Double value) {
        return this.delegate.cumulativeProbability(value);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected double calculateDistribution(Double value0, Double value1) {
        return this.delegate.cumulativeProbability(value0, value1);
    }

    @Override
    protected Double determineValue(double p) {
        return this.delegate.inverseCumulativeProbability(p);
    }

    @Override
    public Double convertIndex(int index) {
        return (double) index;
    }
}
