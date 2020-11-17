package de.coiaf.random.distributions.continuous;

import de.coiaf.random.probability.Probability;
import org.apache.commons.math3.distribution.RealDistribution;

/**
 * @author Stephan Grochowina <stephan.grochowina@web.de>
 */
public class ExponentialDistribution extends AbstractContinuousDistributionAdapter {

    private static RealDistribution createDelegate(double mean) {
        return new org.apache.commons.math3.distribution.ExponentialDistribution(mean);
    }

    private static RealDistribution createDelegate(double value, Probability distribution) {
        double distributionValue = distribution.doubleValue();

        return createDelegate(-(value/Math.log(1.0 - distributionValue)));
    }

    ExponentialDistribution(double mean) {
        super(createDelegate(mean));
    }

    ExponentialDistribution(double value, Probability distribution) {
        super(createDelegate(value, distribution));
    }
}
