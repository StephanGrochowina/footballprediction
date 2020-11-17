package de.coiaf.random.distributions.continuous;

import de.coiaf.random.distributions.ProbabilityDensityFunction;
import de.coiaf.random.probability.Probability;

/**
 * @author Stephan Grochowina <stephan.grochowina@web.de>
 */
public class ErlangDistribution implements ProbabilityDensityFunction<Double> {
    private final double lambda;
    private final double n;
    private final double mean;
    private final double variance;
    private final double standardDeviation;
    private final double densityCoefficient;

    ErlangDistribution(double lambda, int n) {

        this.validateParameterLambda(lambda);
        this.validateParameterN(n);

        this.lambda = lambda;
        this.n = (double) n;
        this.mean = this.n/this.lambda;
        this.variance = this.n / Math.pow(this.lambda, 2.0);
        this.standardDeviation = Math.sqrt(this.variance);
        this.densityCoefficient = this.calculateDensityCoefficent(lambda, n);
    }

    private void validateParameterLambda(double lambda) {
        if (lambda <= 0) {
            throw new IllegalArgumentException("Parameter lambda must be greater than 0.0.");
        }
    }

    private void validateParameterN(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("Parameter n must be greater than 0.");
        }
    }

    private double calculateDensityCoefficent(double lambda, int n) {
        double densityCoefficent = Math.pow(lambda, n);

        for (int i = 1; i < n; i++) {
            densityCoefficent /= (double) i;
        }

        return densityCoefficent;
    }

    @Override
    public Probability getDensity(Double value) {
        Probability result;

        if (value < 0) {
            result = Probability.IMPOSSIBLE;
        }
        else {
            double resultValue = this.densityCoefficient * Math.pow(value, this.n - 1.0) * Math.exp(- this.lambda * value);
            result = new Probability(resultValue);
        }

        return result;
    }

    @Override
    public Double getMean() {
        return this.mean;
    }

    @Override
    public Double getExpectationValue() {
        return this.getMean();
    }

    @Override
    public Double getVariance() {
        return this.variance;
    }

    @Override
    public Double getStandardDeviation() {
        return this.standardDeviation;
    }
}
