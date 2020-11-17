package de.coiaf.random.distributions.continuous;

import de.coiaf.random.distributions.Distribution;

import java.util.Objects;

public interface ContinuousDistribution extends Distribution<Double> {

    /**
     * Casts {@code doubleDistribution} to a ContinuousDistribution instance.
     * @param doubleDistribution distribution to be converted
     * @return a ContinuousDistribution instance
     * @throws NullPointerException if {@code doubleDistribution} is null
     * @throws ClassCastException if {@code doubleDistribution} cannot be casted to a ContinuousDistribution instance.
     */
    static ContinuousDistribution convert(Distribution<Double> doubleDistribution) {
        Objects.requireNonNull(doubleDistribution, "Parameter doubleDistribution must not be null.");

        return ContinuousDistribution.class.cast(doubleDistribution);
    }

}
