package de.coiaf.random.distributions.continuous;

import de.coiaf.random.distributions.ProbabilityDensityFunction;
import de.coiaf.random.distributions.common.DistributionElementFactory;
import de.coiaf.random.distributions.common.StandardDeviation;
import de.coiaf.random.distributions.common.Variance;
import de.coiaf.random.probability.Probability;

import java.util.Objects;

public class ContinuousDistributions {

    public static ProbabilityDensityFunction<Double> createErlangDensity(double lambda, int n) {
        return new ErlangDistribution(lambda, n);
    }

    public static ContinuousDistribution createExponentialDistribution(double mean) {
        return new ExponentialDistribution(mean);
    }
    public static ContinuousDistribution createExponentialDistribution(double value, Probability distribution) {
        return new ExponentialDistribution(value, distribution);
    }

    public static ContinuousDistribution createGaussianDistribution() {
        return new GaussianDistribution();
    }
    public static ContinuousDistribution createGaussianDistribution(double mean, Variance variance) {
        Objects.requireNonNull(variance, "Parameter variance must not be null.");

        if (variance.isLowerBound()) {
            throw new IllegalArgumentException("Parameter variance must be greater than 0.");
        }

        return createGaussianDistribution(mean, DistributionElementFactory.createStandardDeviation(variance));
    }
    public static ContinuousDistribution createGaussianDistribution(double mean, StandardDeviation standardDeviation) {
        Objects.requireNonNull(standardDeviation, "Parameter standardDeviation must not be null.");

        return createGaussianDistribution(mean, standardDeviation.doubleValue());
    }
    public static ContinuousDistribution createGaussianDistribution(double mean, double standardDeviation) {
        return new GaussianDistribution(mean, standardDeviation);
    }

    public static double createZTransformedValue(double value, double mean, double standardDeviation) {
        if (standardDeviation <= 0.0) {
            throw new IllegalArgumentException("Parameter standardDeviation must be greater than 0.");
        }

        return (value - mean) / standardDeviation;
    }

    public static ContinuousDistribution createLogisticDistribution(double mean, Variance variance) {
        double scale = LogisticDistribution.calculateScale(variance);

        return new LogisticDistribution(mean, scale);
    }
    public static ContinuousDistribution createLogisticDistribution(double mean, StandardDeviation standardDeviation) {
        double scale = LogisticDistribution.calculateScale(standardDeviation);

        return new LogisticDistribution(mean, scale);
    }
    public static ContinuousDistribution createLogisticDistribution(double mean, double scale) {
        return new LogisticDistribution(mean, scale);
    }
}
