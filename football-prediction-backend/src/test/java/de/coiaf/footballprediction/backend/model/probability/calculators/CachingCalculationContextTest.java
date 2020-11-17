package de.coiaf.footballprediction.backend.model.probability.calculators;

import de.coiaf.footballprediction.backend.model.sharedcontext.EstimatedGoals;
import de.coiaf.random.distributions.discrete.DiscreteDistribution;
import de.coiaf.random.distributions.discrete.DiscreteDistributions;
import de.coiaf.random.probability.Probability;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.*;

public class CachingCalculationContextTest {

    private static final double THRESHOLD = 2.0;
    private static final BigDecimal POSSIBLE_PROBABILITY_VALUE_BELOW_THRESHOLD = new BigDecimal("0.00000000000009");
    private static final Probability POSSIBLE_PROBABILITY_BELOW_THRESHOLD = Probability.valueOf(POSSIBLE_PROBABILITY_VALUE_BELOW_THRESHOLD);
    private static final Probability ALMOST_UNCERTAIN_PROBABILITY = Probability.UNCERTAIN.subtract(POSSIBLE_PROBABILITY_BELOW_THRESHOLD);
    private static final EstimatedGoals EXPECTED_TOTAL_GOALS_LOW = EstimatedGoals.valueOf(0.5);
    private static final EstimatedGoals EXPECTED_TOTAL_GOALS_MEDIUM = EstimatedGoals.valueOf(1.5);
    private static final EstimatedGoals EXPECTED_TOTAL_GOALS_HIGH = EstimatedGoals.valueOf(3.5);
    private static final Function<Integer, Probability> TOTAL_GOALS_FUNCTION_SYMMETRIC = totalGoals -> {
        if (totalGoals == null || totalGoals < 0)  {
            return null;
        }
        else if (totalGoals == 0 || totalGoals == 3) {
            return POSSIBLE_PROBABILITY_BELOW_THRESHOLD;
        }
        else if (totalGoals == 1 || totalGoals == 2) {
            return ALMOST_UNCERTAIN_PROBABILITY;
        }
        return Probability.IMPOSSIBLE;
    };

    @Test(expected = NullPointerException.class)
    public void createCache_nullTotalGoalsProbabilityFunction_mediumEstimatedGoals() {
        CachingCalculationContext.createCache(null, EXPECTED_TOTAL_GOALS_MEDIUM);
    }
    @Test
    public void createCache_givenTotalGoalsProbabilityFunction_nullEstimatedGoals() {
        List<Probability> result = CachingCalculationContext.createCache(TOTAL_GOALS_FUNCTION_SYMMETRIC, null);

        assertNotNull(result);
        assertEquals(3, result.size());
    }
    @Test
    public void createCache_givenTotalGoalsProbabilityFunction_lowEstimatedGoals() {
        List<Probability> result = CachingCalculationContext.createCache(TOTAL_GOALS_FUNCTION_SYMMETRIC, EXPECTED_TOTAL_GOALS_LOW);

        assertNotNull(result);
        assertEquals(3, result.size());
    }
    @Test
    public void createCache_givenTotalGoalsProbabilityFunction_mediumEstimatedGoals() {
        List<Probability> result = CachingCalculationContext.createCache(TOTAL_GOALS_FUNCTION_SYMMETRIC, EXPECTED_TOTAL_GOALS_MEDIUM);

        assertNotNull(result);
        assertEquals(3, result.size());
    }
    @Test
    public void createCache_givenTotalGoalsProbabilityFunction_highEstimatedGoals() {
        List<Probability> result = CachingCalculationContext.createCache(TOTAL_GOALS_FUNCTION_SYMMETRIC, EXPECTED_TOTAL_GOALS_HIGH);

        assertNotNull(result);
        assertEquals(4, result.size());
    }
    @Test
    @Ignore
    public void createCache_finityCheck() {
        double expectedTotalGoalsValue = 40;
        EstimatedGoals expectedTotalGoals = EstimatedGoals.valueOf(expectedTotalGoalsValue);
        DiscreteDistribution totalGoalsDistribution = DiscreteDistributions.createPoissonDistribution(expectedTotalGoalsValue);
        Function<Integer, Probability> totalGoalsFunction = totalGoalsDistribution::getDensity;

        List<Probability> result = CachingCalculationContext.createCache(totalGoalsFunction, expectedTotalGoals);

        assertNotNull(result);
        assertTrue(100 >= result.size());
    }

    @Test
    public void determineThreshold_nullExpectedTotalGoals() {
        double threshold = CachingCalculationContext.determineThreshold(null);

        assertEquals(0.0,threshold, 0.0);
    }
    @Test
    public void determineThreshold_givenExpectedTotalGoals() {
        EstimatedGoals expectedTotalGoals = EstimatedGoals.valueOf(0.5);
        double threshold = CachingCalculationContext.determineThreshold(expectedTotalGoals);

        assertEquals(expectedTotalGoals.doubleValue(),threshold, 0.0);
    }

    @Test
    public void isIndexWithinThreshold_indexBelowThreshold() {
        boolean result = CachingCalculationContext.isIndexWithinThreshold(1, THRESHOLD);

        assertTrue(result);
    }
    @Test
    public void isIndexWithinThreshold_indexEqualsThreshold() {
        boolean result = CachingCalculationContext.isIndexWithinThreshold(2, THRESHOLD);

        assertTrue(result);
    }
    @Test
    public void isIndexWithinThreshold_indexAboveThreshold() {
        boolean result = CachingCalculationContext.isIndexWithinThreshold(3, THRESHOLD);

        assertFalse(result);
    }

    @Test
    public void isBigEnoughProbability_nullProbability() {
        boolean result = CachingCalculationContext.isBigEnoughProbability(null);

        //noinspection ConstantConditions
        assertFalse(result);
    }
    @Test
    public void isBigEnoughProbability_probabilityImpossible() {
        boolean result = CachingCalculationContext.isBigEnoughProbability(Probability.IMPOSSIBLE);

        assertFalse(result);
    }
    @Test
    public void isBigEnoughProbability_possibleProbabilityBelowThreshold() {
        boolean result = CachingCalculationContext.isBigEnoughProbability(POSSIBLE_PROBABILITY_BELOW_THRESHOLD);

        assertFalse(result);
    }
    @Test
    public void isBigEnoughProbability_possibleProbabilityEqualsThreshold() {
        boolean result = CachingCalculationContext.isBigEnoughProbability(CachingCalculationContext.LOOP_TERMINATION_THRESHOLD);

        assertTrue(result);
    }
    @Test
    public void isBigEnoughProbability_possibleProbabilityAboveThreshold() {
        boolean result = CachingCalculationContext.isBigEnoughProbability(Probability.UNCERTAIN);

        assertTrue(result);
    }

    @Test
    public void apply_nullTotalGoals() {
        this.verifyApply(null, Probability.IMPOSSIBLE);
    }
    @Test
    public void apply_negativeTotalGoals() {
        this.verifyApply(-1, Probability.IMPOSSIBLE);
    }
    @Test
    public void apply_zeroTotalGoals() {
        this.verifyApply(0, POSSIBLE_PROBABILITY_BELOW_THRESHOLD);
    }
    @Test
    public void apply_totalGoalsBelowThreshold() {
        this.verifyApply(1, ALMOST_UNCERTAIN_PROBABILITY);
    }
    @Test
    public void apply_totalGoalsAboveThresholdWithinCacheRange() {
        this.verifyApply(2, ALMOST_UNCERTAIN_PROBABILITY);
    }
    @Test
    public void apply_totalGoalsAboveThresholdExceedingCacheRange() {
        this.verifyApply(3, Probability.IMPOSSIBLE);
    }

    private void verifyApply(Integer totalGoals, Probability expectedProbability) {
        CachingCalculationContext context = new CachingCalculationContext(TOTAL_GOALS_FUNCTION_SYMMETRIC, EXPECTED_TOTAL_GOALS_MEDIUM);
        Probability result = context.apply(totalGoals);

        assertNotNull(result);
        assertEquals(expectedProbability, result);
    }
}