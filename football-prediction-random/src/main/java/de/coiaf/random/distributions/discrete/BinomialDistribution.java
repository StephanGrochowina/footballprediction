package de.coiaf.random.distributions.discrete;

import de.coiaf.random.probability.Probability;
import org.apache.commons.math3.distribution.IntegerDistribution;

import java.util.Objects;

/**
 * @author Stephan Grochowina <stephan.grochowina@web.de>
 */
public class BinomialDistribution extends AbstractDiscreteDistributionAdapter {

    private static IntegerDistribution createDelegate(int trial, Probability p) {
        Objects.requireNonNull(p);

        return createDelegate(trial, p.doubleValue());
    }
    private static IntegerDistribution createDelegate(int trial, double p) {
        return new org.apache.commons.math3.distribution.BinomialDistribution(trial, p);
    }

    BinomialDistribution(int trial, Probability p) {
        super(createDelegate(trial, p));
    }

    BinomialDistribution(int trial, double p) {
        super(createDelegate(trial, p));
    }
}
