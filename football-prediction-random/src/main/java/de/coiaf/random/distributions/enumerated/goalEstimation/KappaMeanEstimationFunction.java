package de.coiaf.random.distributions.enumerated.goalEstimation;

import java.util.Objects;

/**
 * This function calculates the result of the Kappa function for given scored and conceded goals.
 * the minimum value for these parameters can be passed to this function, too. If one of the goals parameter
 * is less than the minimum value the minimum value will be used instead. The default minimum value is
 * {@link KappaGoals#MINIMUM_LOWER_BOUND_GOALS}.
 *
 * If a minimum value is set it must be greater or equal to {@link KappaGoals#MINIMUM_LOWER_BOUND_GOALS}.
 * Otherwise, an IllegalArgumentException is thrown.
 *
 * Behaviour of the function for given scored goals s, conceded goals c and a given minimum value m:
 * Kappa(s, c, m) = max(s, m) * ln(e + max(c, m) / max(s, m) - 1)
 */
class KappaMeanEstimationFunction {

    static <G1 extends Number, G2 extends Number> double calculateMean(
            G1 scoredGoalsValue, G2 concededGoalsValue) {
        KappaGoals scoredGoals = KappaGoals.valueOf(scoredGoalsValue);
        KappaGoals concededGoals = KappaGoals.valueOf(concededGoalsValue);

        return calculateMean(scoredGoals, concededGoals);
    }

    static <G1 extends Number, G2 extends Number, LB extends Number> double calculateMean(
            G1 scoredGoalsValue, G2 concededGoalsValue, LB lowerBoundInclusiveGoals) {
        KappaGoals scoredGoals = KappaGoals.valueOf(scoredGoalsValue, lowerBoundInclusiveGoals);
        KappaGoals concededGoals = KappaGoals.valueOf(concededGoalsValue, lowerBoundInclusiveGoals);

        return calculateMean(scoredGoals, concededGoals);
    }

    static double calculateMean(KappaGoals scoredGoals, KappaGoals concededGoals) {
        Objects.requireNonNull(scoredGoals, "Parameter scoredGoals must not be null.");
        Objects.requireNonNull(concededGoals, "Parameter concededGoals must not be null.");

        return scoredGoals.doubleValue()
                * Math.log(Math.E + (concededGoals.doubleValue() / scoredGoals.doubleValue()) - 1.0);
    }
}
