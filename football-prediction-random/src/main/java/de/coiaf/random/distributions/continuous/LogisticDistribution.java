package de.coiaf.random.distributions.continuous;

import de.coiaf.random.distributions.common.DistributionElementFactory;
import de.coiaf.random.distributions.common.StandardDeviation;
import de.coiaf.random.distributions.common.Variance;
import org.apache.commons.math3.distribution.RealDistribution;

import java.util.Objects;

class LogisticDistribution extends AbstractContinuousDistributionAdapter {

    static double calculateScale(Variance variance) {
        Objects.requireNonNull(variance, "Parameter variance must not be null");

        StandardDeviation standardDeviation = DistributionElementFactory.createStandardDeviation(variance);

        return calculateScale(standardDeviation);
    }

    static double calculateScale(StandardDeviation standardDeviation) {
        Objects.requireNonNull(standardDeviation, "Parameter standardDeviation must not be null");

        return standardDeviation.doubleValue() * (Math.sqrt(3) / Math.PI);
    }

    private static RealDistribution createDelegate(double mean, double scale) {
        return new org.apache.commons.math3.distribution.LogisticDistribution(mean, scale);
    }

    LogisticDistribution(double mean, double scale) {
        super(createDelegate(mean, scale));
    }
}
