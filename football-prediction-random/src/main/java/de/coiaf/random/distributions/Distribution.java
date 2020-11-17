package de.coiaf.random.distributions;

public interface Distribution<T> extends ProbabilityDensityFunction<T>, CumulativeDistributionFunction<T> {

    default ProbabilityDensityFunction<T> getProbabilityDensityFunction() {
        return this;
    }

    default CumulativeDistributionFunction<T> getCumulativeDistributionFunction() {
        return this;
    }
}
