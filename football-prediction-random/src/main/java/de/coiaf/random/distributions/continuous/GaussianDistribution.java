package de.coiaf.random.distributions.continuous;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.RealDistribution;

/**
 * @author Stephan Grochowina <stephan.grochowina@web.de>
 */
class GaussianDistribution extends AbstractContinuousDistributionAdapter {

    private static RealDistribution createDelegate() {
        return new NormalDistribution();
    }

    private static RealDistribution createDelegate(double mean, double standardDeviation) {
        return new NormalDistribution(mean, standardDeviation);
    }

    GaussianDistribution() {
        super(createDelegate());
    }

    GaussianDistribution (double mean, double standardDeviation) {
        super(createDelegate(mean, standardDeviation));
    }
}
