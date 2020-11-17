package de.coiaf.random.distributions.discrete;

import de.coiaf.random.distributions.continuous.ContinuousDistribution;
import de.coiaf.random.probability.Probability;

public class DiscreteDistributions {

    public static DiscreteDistribution createBinomialDistribution(int trial, Probability p) {
        return new BinomialDistribution(trial, p);
    }
    public static DiscreteDistribution createBinomialDistribution(int trial, double p) {
        return new BinomialDistribution(trial, p);
    }

    public static DiscreteDistribution createPoissonDistribution(double lambda) {
        return new PoissonDistribution(lambda);
    }

    public static DiscreteDistribution createDiscreteDistribution(ContinuousDistribution continuousDistribution, boolean zeroBased) {
        return new DiscretifiedContinuousDistribution(continuousDistribution, zeroBased);
    }
}
