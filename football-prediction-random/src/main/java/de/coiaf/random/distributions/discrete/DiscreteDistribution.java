package de.coiaf.random.distributions.discrete;

import de.coiaf.random.distributions.Distribution;

import java.util.Objects;

public interface DiscreteDistribution extends Distribution<Integer> {

    /**
     * Casts {@code integerDistribution} to a DiscreteDistribution instance.
     * @param integerDistribution distribution to be converted
     * @return a DiscreteDistribution instance
     * @throws NullPointerException if {@code integerDistribution} is null
     * @throws ClassCastException if {@code integerDistribution} cannot be casted to a DiscreteDistribution instance.
     */
    static DiscreteDistribution convert(Distribution<Integer> integerDistribution) {
        Objects.requireNonNull(integerDistribution, "Parameter integerDistribution must not be null.");

       return DiscreteDistribution.class.cast(integerDistribution);
    }
}
