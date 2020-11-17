package de.coiaf.random.distributions.continuous;

import de.coiaf.random.distributions.common.DistributionElementFactory;
import de.coiaf.random.distributions.common.StandardDeviation;
import de.coiaf.random.distributions.common.Variance;

import java.util.Objects;

@FunctionalInterface
public interface MeanAndVarianceBasedContinuousDistributionFunction extends MeanBasedContinuousDistributionFunction {

    @Override
    default ContinuousDistribution createDistribution(Number mean, StandardDeviation deviation) {
        Objects.requireNonNull(deviation, "Parameter deviation must not be null.");

        Variance variance = DistributionElementFactory.createVariance(deviation);

        return this.createDistribution(mean, variance);
    }
}
