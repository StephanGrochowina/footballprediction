package de.coiaf.random.distributions.enumerated.goalEstimation;

import org.junit.Test;

import static org.junit.Assert.*;

public class KappaMeanEstimationFunctionTest {

    private static final KappaGoals GOALS_LOW = KappaGoals.valueOf(1);
    private static final KappaGoals GOALS_HIGH = KappaGoals.valueOf(5);
    private static final KappaGoals GOALS_ZERO = KappaGoals.valueOf(0);

    @Test(expected = NullPointerException.class)
    public void calculateMean_nullScoredGoals_givenConcededGoals() {
        KappaMeanEstimationFunction.calculateMean(null, GOALS_LOW);
    }

    @Test(expected = NullPointerException.class)
    public void calculateMean_givenScoredGoals_nullConcededGoals() {
        KappaMeanEstimationFunction.calculateMean(GOALS_LOW, null);
    }

    @Test
    public void calculateMean_scoredGoalsEqualConcededGoals() {
        double expected = GOALS_LOW.doubleValue();

        double result = KappaMeanEstimationFunction.calculateMean(GOALS_LOW, GOALS_LOW);

        assertEquals(expected, result, 0.01);
    }

    @Test
    public void calculateMean_scoredGoalsBelowConcededGoals() {
        double result = KappaMeanEstimationFunction.calculateMean(GOALS_LOW, GOALS_HIGH);

        assertTrue(result > GOALS_LOW.doubleValue());
        assertTrue(result < GOALS_HIGH.doubleValue());
    }

    @Test
    public void calculateMean_scoredGoalsAboveConcededGoals() {
        double result = KappaMeanEstimationFunction.calculateMean(GOALS_HIGH, GOALS_LOW);

        assertTrue(result > GOALS_LOW.doubleValue());
        assertTrue(result < GOALS_HIGH.doubleValue());
    }

    @Test
    public void calculateMean_zeroScoredGoals_givenConcededGoals() {
        double result = KappaMeanEstimationFunction.calculateMean(GOALS_ZERO, GOALS_LOW);

        assertTrue(result > GOALS_ZERO.doubleValue());
        assertTrue(result > KappaGoals.MINIMUM_LOWER_BOUND_GOALS);
        assertTrue(result > GOALS_LOW.doubleValue());
        assertEquals(Double.POSITIVE_INFINITY, result, 0.0);
    }

    @Test
    public void calculateMean_givenScoredGoals_zeroConcededGoals() {
        double result = KappaMeanEstimationFunction.calculateMean(GOALS_LOW, GOALS_ZERO);

        assertTrue(result > GOALS_ZERO.doubleValue());
        assertTrue(result > KappaGoals.MINIMUM_LOWER_BOUND_GOALS);
        assertTrue(result < GOALS_LOW.doubleValue());
    }
}