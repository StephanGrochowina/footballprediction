package de.coiaf.random.distributions.continuous;

import de.coiaf.random.distributions.common.StandardDeviation;
import de.coiaf.random.distributions.common.Variance;

public interface MeanBasedContinuousDistributionFunction {

    ContinuousDistribution createDistribution(Number mean, Variance variance);

    ContinuousDistribution createDistribution(Number mean, StandardDeviation deviation);
}
