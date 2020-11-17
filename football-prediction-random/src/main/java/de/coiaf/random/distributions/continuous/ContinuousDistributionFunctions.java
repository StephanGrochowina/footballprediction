package de.coiaf.random.distributions.continuous;

public class ContinuousDistributionFunctions {

    private static final MeanAndStandardDeviationBasedContinuousDistributionFunction GAUSSIAN_DISTRIBUTION_FUNCTION =
            (mean, deviation) -> ContinuousDistributions.createGaussianDistribution(mean.doubleValue(), deviation);

    private static final MeanAndStandardDeviationBasedContinuousDistributionFunction LOGISTIC_DISTRIBUTION_FUNCTION =
            (mean, deviation) -> ContinuousDistributions.createLogisticDistribution(mean.doubleValue(), deviation);

    public static MeanBasedContinuousDistributionFunction getGaussianDistributionFunction() {
        return GAUSSIAN_DISTRIBUTION_FUNCTION;
    }

    public static MeanBasedContinuousDistributionFunction getLogisticDistributionFunction() {
        return LOGISTIC_DISTRIBUTION_FUNCTION;
    }
}
