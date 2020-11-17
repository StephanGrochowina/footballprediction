package de.coiaf.random.distributions.discrete;

import org.apache.commons.math3.distribution.IntegerDistribution;

/**
 * @author Stephan Grochowina <stephan.grochowina@web.de>
 */
public class PoissonDistribution extends AbstractDiscreteDistributionAdapter {

    private static IntegerDistribution createDelegate(double lambda) {
        return new org.apache.commons.math3.distribution.PoissonDistribution(lambda);
    }

    PoissonDistribution(double lambda) {
        super(createDelegate(lambda));
    }
}
