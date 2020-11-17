package de.coiaf.random.distributions;

import de.coiaf.random.probability.Probability;

/**
 * @author Stephan Grochowina <stephan.grochowina@web.de>
 */
public interface ProbabilityDensityFunction<T> {

    /**
     *
     * @param value
     * @return the density at {@code value}, i.e. its probability
     */
    Probability getDensity(T value);

    /**
     *
     * @return the variance of the distribution
     * @throws UnsupportedOperationException if the {@link ProbabilityDensityFunction} instance and its type {@code T} do
     * not support this method
     */
    default Double getVariance() {
        throw new UnsupportedOperationException("This density does not support this method.");
    }

    /**
     *
     * @return the standard deviation of the distribution
     * @throws UnsupportedOperationException if the {@link ProbabilityDensityFunction} instance and its type {@code T} do
     * not support this method
     */
    default Double getStandardDeviation() {
        throw new UnsupportedOperationException("This density does not support this method.");
    }

    /**
     *
     * @return the mean of the density. The value returned does not necessarily represent a value of {@code T}.
     */
    Double getMean();

    /**
     *
     * @return the expectation value of the distribution, i.e. the value of {@code T} closest to the mean
     */
    T getExpectationValue();
}
