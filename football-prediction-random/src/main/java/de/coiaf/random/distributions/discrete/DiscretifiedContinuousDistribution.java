package de.coiaf.random.distributions.discrete;

import de.coiaf.random.distributions.AbstractDistributionAdapter;
import de.coiaf.random.distributions.continuous.ContinuousDistribution;
import de.coiaf.random.probability.Probability;

import java.util.Objects;

public class DiscretifiedContinuousDistribution extends AbstractDistributionAdapter<Integer> implements DiscreteDistribution {

    private final DistributionContext context;

    DiscretifiedContinuousDistribution(ContinuousDistribution continuousDistribution, boolean zeroBased) {
        this.context = zeroBased ? new ZeroBasedDistributionContext(continuousDistribution)
                : new UnbasedDistributionContext(continuousDistribution);
    }

    @Override
    protected double calculateDensity(Integer value) {
        return this.context.calculateDensity(value);
    }

    @Override
    protected double calculateDistribution(Integer value) {
        return this.context.calculateDistribution(value);
    }

    @Override
    protected double calculateDistribution(Integer value0, Integer value1) {
        return this.context.calculateDistribution(value0, value1);
    }

    @Override
    protected Integer determineValue(double p) {
        return this.context.determineValue(p);
    }

    @Override
    public Double getVariance() {
        return this.context.getVariance();
    }

    @Override
    public Double getStandardDeviation() {
        return this.context.getStandardDeviation();
    }

    @Override
    public Double getMean() {
        return this.context.getMean();
    }

    @Override
    public Integer getExpectationValue() {
        return this.context.getExpectationValue();
    }

    @Override
    public Integer convertIndex(int index) {
        return index;
    }

    private static abstract class DistributionContext {
        private final ContinuousDistribution continuousDistribution;

        DistributionContext(ContinuousDistribution continuousDistribution) {
            Objects.requireNonNull(continuousDistribution, "Parameter continuousDistribution must not be null.");

            this.continuousDistribution = continuousDistribution;
        }

        Double getVariance() {
            return this.continuousDistribution.getVariance();
        }

        Double getStandardDeviation() {
            return this.continuousDistribution.getStandardDeviation();
        }

        Double getMean() {
            return this.continuousDistribution.getMean();
        }

        Integer getExpectationValue() {
            double mean = this.getMean();

            return this.createDiscreteValue(mean);
        }

        protected abstract Integer determineValue(double p);

        Integer determineValueStandard(double p) {
            Probability probability = Probability.valueOf(p);

            Double result = this.continuousDistribution.selectValue(probability);

            return (int) Math.ceil(result - 0.5);
        }

        protected abstract double calculateDensity(Integer value);

        double calculateDensityStandard(Integer value) {
            Double lowerBound = this.determineLowerBound(value);
            Double upperBound = this.determineUpperBound(value);

            return this.continuousDistribution.getDistribution(lowerBound, upperBound).doubleValue();
        }

        protected abstract double calculateDistribution(Integer value);

        double calculateDistributionStandard(Integer value) {
            Double upperBound = this.determineUpperBound(value);

            return this.continuousDistribution.getDistribution(upperBound).doubleValue();
        }

        protected abstract double calculateDistribution(Integer value0, Integer value1);

        double calculateDistributionStandard(Integer value0, Integer value1) {
            Objects.requireNonNull(value0);
            Objects.requireNonNull(value1);

            Integer minValue = value0 <= value1 ? value0 : value1;
            Integer maxValue = value0 <= value1 ? value1 : value0;

            Double lowerBound = this.determineLowerBound(minValue);
            Double upperBound = this.determineUpperBound(maxValue);

            return this.continuousDistribution.getDistribution(lowerBound, upperBound).doubleValue();
        }

        Double determineLowerBound(Integer value) {
            return value == null ? null : value.doubleValue() - 0.5;
        }

        Double determineUpperBound(Integer value) {
            return value == null ? null : value.doubleValue() + 0.5;
        }

        private Integer createDiscreteValue(Double value) {
            return value == null || Double.POSITIVE_INFINITY == value ? null : (int) Math.round(value);
        }


        ContinuousDistribution getContinuousDistribution() {
            return this.continuousDistribution;
        }
    }

    private static class ZeroBasedDistributionContext extends DistributionContext {

        ZeroBasedDistributionContext(ContinuousDistribution continuousDistribution) {
            super(continuousDistribution);
        }

        @Override
        protected Integer determineValue(double p) {
            Integer result = this.determineValueStandard(p);

            return result > 0 ? result : 0;
        }

        @Override
        protected double calculateDensity(Integer value) {
            double result;

            if (value < 0) {
                result = 0.0;
            }
            else if (value == 0) {
                result = this.getContinuousDistribution().getDistribution(this.determineUpperBound(value)).doubleValue();
            }
            else {
                result = this.calculateDensityStandard(value);
            }

            return result;
        }

        @Override
        protected double calculateDistribution(Integer value) {
            double result;

            if (value < 0) {
                result = 0.0;
            }
            else {
                result = this.calculateDistributionStandard(value);
            }

            return result;
        }

        @Override
        protected double calculateDistribution(Integer value0, Integer value1) {
            Objects.requireNonNull(value0);
            Objects.requireNonNull(value1);

            Integer minValue = value0 <= value1 ? value0 : value1;
            Integer maxValue = value0 <= value1 ? value1 : value0;
            double result;

            if (minValue < 0) {
                result = this.calculateDistribution(maxValue);
            }
            else {
                result = this.calculateDistribution(maxValue) - this.calculateDistribution(minValue);
            }

            return result;
        }
    }

    private static class UnbasedDistributionContext extends DistributionContext {

        UnbasedDistributionContext(ContinuousDistribution continuousDistribution) {
            super(continuousDistribution);
        }

        @Override
        protected Integer determineValue(double p) {
            return this.determineValueStandard(p);
        }

        @Override
        protected double calculateDensity(Integer value) {
            return this.calculateDensityStandard(value);
        }

        @Override
        protected double calculateDistribution(Integer value) {
            return this.calculateDistributionStandard(value);
        }

        @Override
        protected double calculateDistribution(Integer value0, Integer value1) {
            return this.calculateDistributionStandard(value0, value1);
        }
    }
}
