package de.coiaf.random.distributions;

import de.coiaf.random.probability.Probability;

/**
 * @author Stephan Grochowina <stephan.grochowina@web.de>
 */
public abstract class AbstractDistributionAdapter<T> implements Distribution<T> {

    @Override
    public final Probability getDensity(T value) {
        double density;

        this.validateValue(value);
        density = this.calculateDensity(value);

        return this.createProbability(density);
    }

    @Override
    public final Probability getDistribution(T value) {
        double cumulativeProbability;

        this.validateValue(value);
        cumulativeProbability = this.calculateDistribution(value);

        return this.createProbability(cumulativeProbability);
    }

    @Override
    public final Probability getDistribution(T value0, T value1) {
        double cumulativeProbability;

        this.validateValue(value0);
        this.validateValue(value1);
        cumulativeProbability = this.calculateDistribution(value0, value1);

        return this.createProbability(cumulativeProbability);
    }

    @Override
    public final T selectValue(Probability distribution) {
        this.validateProbability(distribution);

        return this.determineValue(distribution.doubleValue());
    }

    protected abstract double calculateDensity(T value);

    protected abstract double calculateDistribution(T value);

    protected abstract double calculateDistribution(T value0, T value1);

    protected abstract T determineValue(double p);

    protected void validateValue(T value) {
        if (value == null) {
            throw new IllegalArgumentException("The provided value should not be null");
        }
    }

    private void validateProbability(Probability p) {
        if (p == null) {
            throw new IllegalArgumentException("Parameter p should not be null.");
        }
    }

    private Probability createProbability(double value) {
        Probability result;

        try {
            result = new Probability(value);
        }
        catch (IllegalArgumentException e) {
            result = null;
        }

        return result;
    }

    @Override
    public T determineQuantile(Probability distribution) {
        double p = distribution == null ? 0.0 : distribution.doubleValue();

        return this.determineValue(p);
    }

}
