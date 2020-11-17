package de.coiaf.random.distributions.continuous;

import de.coiaf.random.distributions.common.DistributionElementFactory;
import de.coiaf.random.distributions.common.StandardDeviation;
import de.coiaf.random.distributions.common.Variance;

import java.util.Objects;

@FunctionalInterface
public interface MeanAndStandardDeviationBasedContinuousDistributionFunction extends MeanBasedContinuousDistributionFunction {

    @Override
    default ContinuousDistribution createDistribution(Number mean, Variance variance) {
        Objects.requireNonNull(variance, "Parameter variance must not be null.");

        StandardDeviation deviation = DistributionElementFactory.createStandardDeviation(variance);

        return this.createDistribution(mean, deviation);
    }
}
