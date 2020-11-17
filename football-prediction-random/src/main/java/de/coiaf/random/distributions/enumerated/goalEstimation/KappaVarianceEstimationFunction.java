package de.coiaf.random.distributions.enumerated.goalEstimation;

import de.coiaf.random.distributions.common.DistributionElementFactory;
import de.coiaf.random.distributions.common.StandardDeviation;
import de.coiaf.random.distributions.common.Variance;

import java.util.Objects;

/**
 * This function calculates a modified variance depending on a given variance and the number of matches played.
 * The number of matches played determines how reliable the given variance is. The greater the number of played
 * matches is the closer the modified variance is to the given variance.
 *
 * The modified variance is always greater than zero.
 *
 * Behaviour of the function for a given variance v and a given number of matches played m:
 * lim m -> 0: f(v, m) -> +oo
 * lim m -> +00, lim v -> 0: f(v, m) -> Double.MIN_VALUE
 * lim m -> +00, v > 0: f(v, m) -> v
 * finite m, lim v -> 0: f(v, m) = (1/(m+1))^2
 * lim v -> +oo: f(v, m) -> +oo
 * else: f(v, m) = (1+1/(m+1)^2) * (v+1) * e^(v/m) - 1
 */
class KappaVarianceEstimationFunction {

    static Variance calculateVarianceByObservedStandardDeviation(double observedStandardDeviationeValue, int matches) {
        return calculateVariance(
                DistributionElementFactory.createStandardDeviationZeroInclusive(observedStandardDeviationeValue),
                matches);
    }

    static Variance calculateVarianceByObservedVariance(double observedVarianceValue, int matches) {
        return calculateVariance(
                DistributionElementFactory.createVarianceZeroInclusive(observedVarianceValue),
                matches);
    }

    static Variance calculateVariance(StandardDeviation observedStandardDeviation, int matches) {
        return calculateVariance(
                DistributionElementFactory.createVariance(observedStandardDeviation),
                PlayedMatches.valueOf(matches));
    }

    static Variance calculateVariance(Variance observedVariance, int matches) {
        return calculateVariance(observedVariance, PlayedMatches.valueOf(matches));
    }

    static Variance calculateVariance(Variance observedVariance, PlayedMatches matches) {
        Objects.requireNonNull(observedVariance, "Parameter observedVariance must not be null.");
        Objects.requireNonNull(matches, "Parameter matches must not be null.");

        Variance result;
        double matchesValue = matches.doubleValue();
        double observedVarianceValue = observedVariance.doubleValue();

        if (matchesValue == Double.POSITIVE_INFINITY && observedVariance.isLowerBound()) {
            result = Variance.MINIMUM_NON_ZERO_VARIANCE;
        }
        else if (matchesValue == Double.POSITIVE_INFINITY) {
            result = observedVariance;
        }
        else if (!matches.hasPlayedMatches() || observedVariance.isUpperBound()) {
            result = Variance.INFINITE_VARIANCE;
        }
        else if (observedVariance.isLowerBound()) {
            double resultVarianceValue = Math.pow((1.0d/(matchesValue + 1.0d)), 2);
            result = resultVarianceValue > 0.0d
                    ? DistributionElementFactory.createVarianceZeroExclusive(resultVarianceValue)
                    : Variance.MINIMUM_NON_ZERO_VARIANCE;
        }
        else {
            double factorMatches = 1.0d + (1.0d/Math.pow(matchesValue + 1.0d,2));
            double factorObservedVariance = observedVarianceValue + 1.0d;
            double factorExponential = Math.exp(observedVarianceValue/matchesValue);

            result = DistributionElementFactory.createVarianceZeroExclusive(factorMatches * factorObservedVariance * factorExponential -1.0d);
        }

        return result;
    }
}
