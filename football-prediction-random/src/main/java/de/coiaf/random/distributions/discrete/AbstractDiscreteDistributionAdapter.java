package de.coiaf.random.distributions.discrete;

import de.coiaf.random.distributions.AbstractDistributionAdapter;
import org.apache.commons.math3.distribution.IntegerDistribution;

/**
 * @author Stephan Grochowina <stephan.grochowina@web.de>
 */
public class AbstractDiscreteDistributionAdapter extends AbstractDistributionAdapter<Integer> implements DiscreteDistribution {

    private final IntegerDistribution delegate;

    protected AbstractDiscreteDistributionAdapter(IntegerDistribution delegate) {
        if (delegate == null) {
            throw new IllegalArgumentException("Parameter delegate should not be null.");
        }

        this.delegate = delegate;
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
        double mean = this.delegate.getNumericalMean();

        return (Double.NaN == mean) ? null : mean;
    }

    @Override
    public Integer getExpectationValue() {
        double mean = this.getMean();

        return this.createDiscreteValue(mean);
    }

    @Override
    protected double calculateDensity(Integer value) {
        return this.delegate.probability(value);
    }

    @Override
    protected double calculateDistribution(Integer value) {
        return this.delegate.cumulativeProbability(value);
    }

    @Override
    protected double calculateDistribution(Integer value0, Integer value1) {
        return this.delegate.cumulativeProbability(value0, value1);
    }

    @Override
    protected Integer determineValue(double p) {
        return this.delegate.inverseCumulativeProbability(p);
    }

    private Integer createDiscreteValue(Double value) {
        return value == null || Double.NaN == value || Double.POSITIVE_INFINITY == value ? null : (int) Math.round(value);
    }

    @Override
    public Integer convertIndex(int index) {
        return index;
    }
}
