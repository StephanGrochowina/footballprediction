package de.coiaf.random.distributions.enumerated.goalEstimation;

import org.junit.Test;

import static org.junit.Assert.*;

public class KappaGoalsTest {

    private static final Integer LOWER_BOUND_ZERO = 0;
    private static final Integer LOWER_BOUND_NEGATIVE = -1;
    private static final Integer LOWER_BOUND_POSITIVE = 1;

    private static final Double KAPPA_GOALS_ZERO = 0.0;
    private static final Double KAPPA_GOALS_NEGATIVE = -1.0;
    private static final Double KAPPA_GOALS_POSITIVE_BELOW_LOWER_BOUND = 0.5;
    private static final Double KAPPA_GOALS_POSITIVE_ABOVE_LOWER_BOUND = 1.5;

    private static final double COMPARISON_DELTA = 0.1;

    @Test(expected = NullPointerException.class)
    public void valueOf_nullKappaGoals() {
        KappaGoals.valueOf(null);
    }
    @Test
    public void valueOf_negativeKappaGoals() {
        double expected = KappaGoals.MINIMUM_LOWER_BOUND_GOALS;

        this.verifyValueOf(expected, KAPPA_GOALS_NEGATIVE);
    }
    @Test
    public void valueOf_zeroKappaGoals() {
        double expected = KappaGoals.MINIMUM_LOWER_BOUND_GOALS;

        this.verifyValueOf(expected, KAPPA_GOALS_ZERO);
    }
    @Test
    public void valueOf_positiveKappaGoals() {
        double expected = KAPPA_GOALS_POSITIVE_BELOW_LOWER_BOUND;

        this.verifyValueOf(expected, KAPPA_GOALS_POSITIVE_BELOW_LOWER_BOUND);
    }

    @Test(expected = NullPointerException.class)
    public void valueOf_nullKappaGoals_lowerBoundAboveMinimum() {
        KappaGoals.valueOf(null, LOWER_BOUND_POSITIVE);
    }
    @Test
    public void valueOf_negativeKappaGoals_lowerBoundAboveMinimum() {
        double expected = LOWER_BOUND_POSITIVE.doubleValue();

        this.verifyValueOf(expected, KAPPA_GOALS_NEGATIVE, LOWER_BOUND_POSITIVE);
    }
    @Test
    public void valueOf_zeroKappaGoals_lowerBoundAboveMinimum() {
        double expected = LOWER_BOUND_POSITIVE.doubleValue();

        this.verifyValueOf(expected, KAPPA_GOALS_ZERO, LOWER_BOUND_POSITIVE);
    }
    @Test
    public void valueOf_positiveKappaGoalsBelowLowerBounds_lowerBoundAboveMinimum() {
        double expected = LOWER_BOUND_POSITIVE.doubleValue();

        this.verifyValueOf(expected, KAPPA_GOALS_POSITIVE_BELOW_LOWER_BOUND, LOWER_BOUND_POSITIVE);
    }
    @Test
    public void valueOf_positiveKappaGoalsAboveLowerBounds_lowerBoundAboveMinimum() {
        double expected = KAPPA_GOALS_POSITIVE_ABOVE_LOWER_BOUND;

        this.verifyValueOf(expected, KAPPA_GOALS_POSITIVE_ABOVE_LOWER_BOUND, LOWER_BOUND_POSITIVE);
    }
    @Test(expected = NullPointerException.class)
    public void valueOf_positiveKappaGoalsAboveLowerBounds_nullLowerBound() {
        KappaGoals.valueOf(KAPPA_GOALS_POSITIVE_ABOVE_LOWER_BOUND, null);
    }
    @Test(expected = IllegalArgumentException.class)
    public void valueOf_positiveKappaGoalsAboveLowerBounds_zeroLowerBound() {
        KappaGoals.valueOf(KAPPA_GOALS_POSITIVE_ABOVE_LOWER_BOUND, LOWER_BOUND_ZERO);
    }
    @Test(expected = IllegalArgumentException.class)
    public void valueOf_positiveKappaGoalsAboveLowerBounds_negativeLowerBound() {
        KappaGoals.valueOf(KAPPA_GOALS_POSITIVE_ABOVE_LOWER_BOUND, LOWER_BOUND_NEGATIVE);
    }

    private void verifyValueOf(double expected, Double goals) {
        KappaGoals result = KappaGoals.valueOf(goals);

        assertNotNull(result);
        assertEquals(expected, result.doubleValue(), COMPARISON_DELTA);
    }
    private void verifyValueOf(double expected, Double goals, Integer lowerBound) {
        KappaGoals result = KappaGoals.valueOf(goals, lowerBound);

        assertNotNull(result);
        assertEquals(expected, result.doubleValue(), COMPARISON_DELTA);
    }
}