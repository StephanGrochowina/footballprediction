package de.coiaf.random.distributions.enumerated.goalEstimation;

import de.coiaf.footballprediction.common.vo.numerical.AbstractDoubleBasedValueObject;

import java.util.Objects;

class KappaGoals extends AbstractDoubleBasedValueObject<KappaGoals> {
    final static double MINIMUM_LOWER_BOUND_GOALS = Double.MIN_VALUE;

    static <G extends Number> KappaGoals valueOf(G goals) {
        return valueOf(goals, MINIMUM_LOWER_BOUND_GOALS);
    }

    static <G extends Number, LB extends Number> KappaGoals valueOf(G goals, LB lowerBoundInclusiveGoals) {
        Objects.requireNonNull(goals, "Parameter goals must not be null.");
        Objects.requireNonNull(lowerBoundInclusiveGoals, "Parameter lowerBoundInclusiveGoals must not be null.");

        return new KappaGoals(goals.doubleValue(), lowerBoundInclusiveGoals.doubleValue());
    }

    private static Double createInternalValue(double goals, double lowerBoundInclusiveGoals) {
        validateLowerBoundInclusiveGoals(lowerBoundInclusiveGoals);

        return Math.max(goals, lowerBoundInclusiveGoals);
    }

    static void validateLowerBoundInclusiveGoals(double lowerBoundInclusiveGoals) {
        if (lowerBoundInclusiveGoals < MINIMUM_LOWER_BOUND_GOALS) {
            throw new IllegalArgumentException("Parameter lowerBoundInclusiveGoals must be positive.");
        }
    }

    private KappaGoals(double goals, double lowerBoundInclusiveGoals) {
        super(() ->  createInternalValue(goals, lowerBoundInclusiveGoals));
    }


}
