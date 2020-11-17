package de.coiaf.random.distributions.enumerated.goalEstimation;

import org.junit.Test;

import static org.junit.Assert.*;

public class KappaMeanEstimationBuilderTest {
    private static final Double GOALS_NEGATIVE = -1.0;
    private static final Double GOALS_ZERO = 0.0;
    private static final Double GOALS_POSITIVE = 1.0;
    private static final Double GOALS_LARGE_POSITIVE = 10.0;

    @Test(expected = NullPointerException.class)
    public void applyHomeScore_nullGoalsScored_positveGoalsConceded() {
        KappaMeanEstimationBuilder.createBuilder()
                .applyHomeScore(null, GOALS_POSITIVE);
    }
    @Test
    public void applyHomeScore_negativeGoalsScored_positveGoalsConceded() {
        KappaMeanEstimationBuilder builder = KappaMeanEstimationBuilder.createBuilder()
                .applyHomeScore(GOALS_NEGATIVE, GOALS_POSITIVE);

        assertNotNull(builder);
    }
    @Test
    public void applyHomeScore_zeroGoalsScored_positveGoalsConceded() {
        KappaMeanEstimationBuilder builder = KappaMeanEstimationBuilder.createBuilder()
                .applyHomeScore(GOALS_ZERO, GOALS_POSITIVE);

        assertNotNull(builder);
    }
    @Test
    public void applyHomeScore_positiveGoalsScored_positveGoalsConceded() {
        KappaMeanEstimationBuilder builder = KappaMeanEstimationBuilder.createBuilder()
                .applyHomeScore(GOALS_POSITIVE, GOALS_POSITIVE);

        assertNotNull(builder);
    }
    @Test(expected = NullPointerException.class)
    public void applyHomeScore_positiveGoalsScored_nullGoalsConceded() {
        KappaMeanEstimationBuilder.createBuilder()
                .applyHomeScore(GOALS_POSITIVE, null);
    }
    @Test
    public void applyHomeScore_positiveGoalsScored_negativeGoalsConceded() {
        KappaMeanEstimationBuilder builder = KappaMeanEstimationBuilder.createBuilder()
                .applyHomeScore(GOALS_POSITIVE, GOALS_NEGATIVE);

        assertNotNull(builder);
    }
    @Test
    public void applyHomeScore_positiveGoalsScored_zeroGoalsConceded() {
        KappaMeanEstimationBuilder builder = KappaMeanEstimationBuilder.createBuilder()
                .applyHomeScore(GOALS_POSITIVE, GOALS_ZERO);

        assertNotNull(builder);
    }

    @Test(expected = NullPointerException.class)
    public void applyAwayScore_nullGoalsScored_positveGoalsConceded() {
        KappaMeanEstimationBuilder.createBuilder()
                .applyAwayScore(null, GOALS_POSITIVE);
    }
    @Test
    public void applyAwayScore_negativeGoalsScored_positveGoalsConceded() {
        KappaMeanEstimationBuilder builder = KappaMeanEstimationBuilder.createBuilder()
                .applyAwayScore(GOALS_NEGATIVE, GOALS_POSITIVE);

        assertNotNull(builder);
    }
    @Test
    public void applyAwayScore_zeroGoalsScored_positveGoalsConceded() {
        KappaMeanEstimationBuilder builder = KappaMeanEstimationBuilder.createBuilder()
                .applyAwayScore(GOALS_ZERO, GOALS_POSITIVE);

        assertNotNull(builder);
    }
    @Test
    public void applyAwayScore_positiveGoalsScored_positveGoalsConceded() {
        KappaMeanEstimationBuilder builder = KappaMeanEstimationBuilder.createBuilder()
                .applyAwayScore(GOALS_POSITIVE, GOALS_POSITIVE);

        assertNotNull(builder);
    }
    @Test(expected = NullPointerException.class)
    public void applyAwayScore_positiveGoalsScored_nullGoalsConceded() {
        KappaMeanEstimationBuilder.createBuilder()
                .applyAwayScore(GOALS_POSITIVE, null);
    }
    @Test
    public void applyAwayScore_positiveGoalsScored_negativeGoalsConceded() {
        KappaMeanEstimationBuilder builder = KappaMeanEstimationBuilder.createBuilder()
                .applyAwayScore(GOALS_POSITIVE, GOALS_NEGATIVE);

        assertNotNull(builder);
    }
    @Test
    public void applyAwayScore_positiveGoalsScored_zeroGoalsConceded() {
        KappaMeanEstimationBuilder builder = KappaMeanEstimationBuilder.createBuilder()
                .applyAwayScore(GOALS_POSITIVE, GOALS_ZERO);

        assertNotNull(builder);
    }

    @Test
    public void applyLowerBoundGoals_nullLowerBound() {
        KappaMeanEstimationBuilder builder = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(null);

        assertNotNull(builder);
    }
    @Test(expected = IllegalArgumentException.class)
    public void applyLowerBoundGoals_negativeLowerBound() {
        KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(GOALS_NEGATIVE);
    }
    @Test(expected = IllegalArgumentException.class)
    public void applyLowerBoundGoals_zeroLowerBound() {
        KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(GOALS_ZERO);
    }
    @Test
    public void applyLowerBoundGoals_positiveLowerBound() {
        KappaMeanEstimationBuilder builder = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(GOALS_POSITIVE);

        assertNotNull(builder);
    }

    @Test
    public void buildHomeMeanEstimation_nullLowerBound_negativeHomeGoalsScored_positiveAwayGoalsConceded() {
        double homeGoalsScored = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(null)
                .applyHomeScore(GOALS_NEGATIVE, GOALS_LARGE_POSITIVE)
                .applyAwayScore(GOALS_LARGE_POSITIVE, GOALS_POSITIVE)
                .buildHomeMeanEstimation();

        assertTrue(GOALS_NEGATIVE < homeGoalsScored);
        assertTrue(KappaGoals.MINIMUM_LOWER_BOUND_GOALS < homeGoalsScored);
        assertTrue(GOALS_POSITIVE < homeGoalsScored);
        assertEquals(Double.POSITIVE_INFINITY, homeGoalsScored, 0.0);
    }
    @Test
    public void buildHomeMeanEstimation_nullLowerBound_zeroHomeGoalsScored_positiveAwayGoalsConceded() {
        double homeGoalsScored = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(null)
                .applyHomeScore(GOALS_ZERO, GOALS_LARGE_POSITIVE)
                .applyAwayScore(GOALS_LARGE_POSITIVE, GOALS_POSITIVE)
                .buildHomeMeanEstimation();

        assertTrue(GOALS_ZERO < homeGoalsScored);
        assertTrue(KappaGoals.MINIMUM_LOWER_BOUND_GOALS < homeGoalsScored);
        assertTrue(GOALS_POSITIVE < homeGoalsScored);
        assertEquals(Double.POSITIVE_INFINITY, homeGoalsScored, 0.0);
    }
    @Test
    public void buildHomeMeanEstimation_nullLowerBound_positiveHomeGoalsScored_positiveAwayGoalsConceded() {
        double homeGoalsScored = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(null)
                .applyHomeScore(GOALS_POSITIVE, GOALS_LARGE_POSITIVE)
                .applyAwayScore(GOALS_LARGE_POSITIVE, GOALS_POSITIVE)
                .buildHomeMeanEstimation();

        assertTrue(KappaGoals.MINIMUM_LOWER_BOUND_GOALS < homeGoalsScored);
        assertEquals(GOALS_POSITIVE, homeGoalsScored, 0.0);
    }
    @Test
    public void buildHomeMeanEstimation_nullLowerBound_largePositiveHomeGoalsScored_positiveAwayGoalsConceded() {
        double homeGoalsScored = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(null)
                .applyHomeScore(GOALS_LARGE_POSITIVE, GOALS_LARGE_POSITIVE)
                .applyAwayScore(GOALS_LARGE_POSITIVE, GOALS_POSITIVE)
                .buildHomeMeanEstimation();

        assertTrue(KappaGoals.MINIMUM_LOWER_BOUND_GOALS < homeGoalsScored);
        assertTrue(GOALS_POSITIVE < homeGoalsScored);
        assertTrue(GOALS_LARGE_POSITIVE > homeGoalsScored);
    }
    @Test
    public void buildHomeMeanEstimation_nullLowerBound_positiveHomeGoalsScored_negativeAwayGoalsConceded() {
        double homeGoalsScored = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(null)
                .applyHomeScore(GOALS_POSITIVE, GOALS_LARGE_POSITIVE)
                .applyAwayScore(GOALS_LARGE_POSITIVE, GOALS_NEGATIVE)
                .buildHomeMeanEstimation();

        assertTrue(KappaGoals.MINIMUM_LOWER_BOUND_GOALS < homeGoalsScored);
        assertTrue(GOALS_POSITIVE > homeGoalsScored);
    }
    @Test
    public void buildHomeMeanEstimation_nullLowerBound_positiveHomeGoalsScored_zeroAwayGoalsConceded() {
        double homeGoalsScored = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(null)
                .applyHomeScore(GOALS_POSITIVE, GOALS_LARGE_POSITIVE)
                .applyAwayScore(GOALS_LARGE_POSITIVE, GOALS_ZERO)
                .buildHomeMeanEstimation();

        assertTrue(KappaGoals.MINIMUM_LOWER_BOUND_GOALS < homeGoalsScored);
        assertTrue(GOALS_POSITIVE > homeGoalsScored);
    }
    @Test
    public void buildHomeMeanEstimation_nullLowerBound_positiveHomeGoalsScored_largePositiveAwayGoalsConceded() {
        double homeGoalsScored = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(null)
                .applyHomeScore(GOALS_POSITIVE, GOALS_LARGE_POSITIVE)
                .applyAwayScore(GOALS_LARGE_POSITIVE, GOALS_LARGE_POSITIVE)
                .buildHomeMeanEstimation();

        assertTrue(KappaGoals.MINIMUM_LOWER_BOUND_GOALS < homeGoalsScored);
        assertTrue(GOALS_POSITIVE < homeGoalsScored);
        assertTrue(GOALS_LARGE_POSITIVE > homeGoalsScored);
    }
    @Test
    public void buildHomeMeanEstimation_givenLowerBound_negativeHomeGoalsScored_positiveAwayGoalsConceded() {
        double homeGoalsScored = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(GOALS_POSITIVE)
                .applyHomeScore(GOALS_NEGATIVE, GOALS_LARGE_POSITIVE)
                .applyAwayScore(GOALS_LARGE_POSITIVE, GOALS_POSITIVE)
                .buildHomeMeanEstimation();

        assertTrue(GOALS_NEGATIVE < homeGoalsScored);
        assertTrue(KappaGoals.MINIMUM_LOWER_BOUND_GOALS < homeGoalsScored);
        assertEquals(GOALS_POSITIVE, homeGoalsScored, 0.0);
    }
    @Test
    public void buildHomeMeanEstimation_givenLowerBound_zeroHomeGoalsScored_positiveAwayGoalsConceded() {
        double homeGoalsScored = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(GOALS_POSITIVE)
                .applyHomeScore(GOALS_ZERO, GOALS_LARGE_POSITIVE)
                .applyAwayScore(GOALS_LARGE_POSITIVE, GOALS_POSITIVE)
                .buildHomeMeanEstimation();

        assertTrue(GOALS_ZERO < homeGoalsScored);
        assertTrue(KappaGoals.MINIMUM_LOWER_BOUND_GOALS < homeGoalsScored);
        assertEquals(GOALS_POSITIVE, homeGoalsScored, 0.0);
    }
    @Test
    public void buildHomeMeanEstimation_givenLowerBound_positiveHomeGoalsScored_positiveAwayGoalsConceded() {
        double homeGoalsScored = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(GOALS_POSITIVE)
                .applyHomeScore(GOALS_POSITIVE, GOALS_LARGE_POSITIVE)
                .applyAwayScore(GOALS_LARGE_POSITIVE, GOALS_POSITIVE)
                .buildHomeMeanEstimation();

        assertTrue(KappaGoals.MINIMUM_LOWER_BOUND_GOALS < homeGoalsScored);
        assertEquals(GOALS_POSITIVE, homeGoalsScored, 0.0);
    }
    @Test
    public void buildHomeMeanEstimation_givenLowerBound_largePositiveHomeGoalsScored_positiveAwayGoalsConceded() {
        double homeGoalsScored = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(GOALS_POSITIVE)
                .applyHomeScore(GOALS_LARGE_POSITIVE, GOALS_LARGE_POSITIVE)
                .applyAwayScore(GOALS_LARGE_POSITIVE, GOALS_POSITIVE)
                .buildHomeMeanEstimation();

        assertTrue(KappaGoals.MINIMUM_LOWER_BOUND_GOALS < homeGoalsScored);
        assertTrue(GOALS_POSITIVE < homeGoalsScored);
        assertTrue(GOALS_LARGE_POSITIVE > homeGoalsScored);
    }
    @Test
    public void buildHomeMeanEstimation_givenLowerBound_positiveHomeGoalsScored_negativeAwayGoalsConceded() {
        double homeGoalsScored = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(GOALS_POSITIVE)
                .applyHomeScore(GOALS_POSITIVE, GOALS_LARGE_POSITIVE)
                .applyAwayScore(GOALS_LARGE_POSITIVE, GOALS_NEGATIVE)
                .buildHomeMeanEstimation();

        assertTrue(KappaGoals.MINIMUM_LOWER_BOUND_GOALS < homeGoalsScored);
        assertEquals(GOALS_POSITIVE, homeGoalsScored, 0.0);
    }
    @Test
    public void buildHomeMeanEstimation_givenLowerBound_positiveHomeGoalsScored_zeroAwayGoalsConceded() {
        double homeGoalsScored = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(GOALS_POSITIVE)
                .applyHomeScore(GOALS_POSITIVE, GOALS_LARGE_POSITIVE)
                .applyAwayScore(GOALS_LARGE_POSITIVE, GOALS_ZERO)
                .buildHomeMeanEstimation();

        assertTrue(KappaGoals.MINIMUM_LOWER_BOUND_GOALS < homeGoalsScored);
        assertEquals(GOALS_POSITIVE, homeGoalsScored, 0.0);
    }
    @Test
    public void buildHomeMeanEstimation_givenLowerBound_positiveHomeGoalsScored_largePositiveAwayGoalsConceded() {
        double homeGoalsScored = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(GOALS_POSITIVE)
                .applyHomeScore(GOALS_POSITIVE, GOALS_LARGE_POSITIVE)
                .applyAwayScore(GOALS_LARGE_POSITIVE, GOALS_LARGE_POSITIVE)
                .buildHomeMeanEstimation();

        assertTrue(KappaGoals.MINIMUM_LOWER_BOUND_GOALS < homeGoalsScored);
        assertTrue(GOALS_POSITIVE < homeGoalsScored);
        assertTrue(GOALS_LARGE_POSITIVE > homeGoalsScored);
    }

    @Test
    public void buildAwayMeanEstimation_nullLowerBound_negativeAwayGoalsScored_positiveHomeGoalsConceded() {
        double awayGoalsScored = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(null)
                .applyHomeScore(GOALS_LARGE_POSITIVE, GOALS_POSITIVE)
                .applyAwayScore(GOALS_NEGATIVE, GOALS_LARGE_POSITIVE)
                .buildAwayMeanEstimation();

        assertTrue(GOALS_NEGATIVE < awayGoalsScored);
        assertTrue(KappaGoals.MINIMUM_LOWER_BOUND_GOALS < awayGoalsScored);
        assertTrue(GOALS_POSITIVE < awayGoalsScored);
        assertEquals(Double.POSITIVE_INFINITY, awayGoalsScored, 0.0);
    }
    @Test
    public void buildAwayMeanEstimation_nullLowerBound_zeroAwayGoalsScored_positiveHomeGoalsConceded() {
        double awayGoalsScored = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(null)
                .applyHomeScore(GOALS_LARGE_POSITIVE, GOALS_POSITIVE)
                .applyAwayScore(GOALS_ZERO, GOALS_LARGE_POSITIVE)
                .buildAwayMeanEstimation();

        assertTrue(GOALS_ZERO < awayGoalsScored);
        assertTrue(KappaGoals.MINIMUM_LOWER_BOUND_GOALS < awayGoalsScored);
        assertTrue(GOALS_POSITIVE < awayGoalsScored);
        assertEquals(Double.POSITIVE_INFINITY, awayGoalsScored, 0.0);
    }
    @Test
    public void buildAwayMeanEstimation_nullLowerBound_positiveAwayGoalsScored_positiveHomeGoalsConceded() {
        double awayGoalsScored = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(null)
                .applyHomeScore(GOALS_LARGE_POSITIVE, GOALS_POSITIVE)
                .applyAwayScore(GOALS_POSITIVE, GOALS_LARGE_POSITIVE)
                .buildAwayMeanEstimation();

        assertTrue(KappaGoals.MINIMUM_LOWER_BOUND_GOALS < awayGoalsScored);
        assertEquals(GOALS_POSITIVE, awayGoalsScored, 0.0);
    }
    @Test
    public void buildAwayMeanEstimation_nullLowerBound_largePositiveAwayGoalsScored_positiveHomeGoalsConceded() {
        double awayGoalsScored = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(null)
                .applyHomeScore(GOALS_LARGE_POSITIVE, GOALS_POSITIVE)
                .applyAwayScore(GOALS_LARGE_POSITIVE, GOALS_LARGE_POSITIVE)
                .buildAwayMeanEstimation();

        assertTrue(KappaGoals.MINIMUM_LOWER_BOUND_GOALS < awayGoalsScored);
        assertTrue(GOALS_POSITIVE < awayGoalsScored);
        assertTrue(GOALS_LARGE_POSITIVE > awayGoalsScored);
    }
    @Test
    public void buildAwayMeanEstimation_nullLowerBound_positiveAwayGoalsScored_negativeHomeGoalsConceded() {
        double awayGoalsScored = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(null)
                .applyHomeScore(GOALS_LARGE_POSITIVE, GOALS_NEGATIVE)
                .applyAwayScore(GOALS_POSITIVE, GOALS_LARGE_POSITIVE)
                .buildAwayMeanEstimation();

        assertTrue(KappaGoals.MINIMUM_LOWER_BOUND_GOALS < awayGoalsScored);
        assertTrue(GOALS_POSITIVE > awayGoalsScored);
    }
    @Test
    public void buildAwayMeanEstimation_nullLowerBound_positiveAwayGoalsScored_zeroHomeGoalsConceded() {
        double awayGoalsScored = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(null)
                .applyHomeScore(GOALS_LARGE_POSITIVE, GOALS_ZERO)
                .applyAwayScore(GOALS_POSITIVE, GOALS_LARGE_POSITIVE)
                .buildAwayMeanEstimation();

        assertTrue(KappaGoals.MINIMUM_LOWER_BOUND_GOALS < awayGoalsScored);
        assertTrue(GOALS_POSITIVE > awayGoalsScored);
    }
    @Test
    public void buildAwayMeanEstimation_nullLowerBound_positiveAwayGoalsScored_largePositiveHomeGoalsConceded() {
        double awayGoalsScored = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(null)
                .applyHomeScore(GOALS_LARGE_POSITIVE, GOALS_LARGE_POSITIVE)
                .applyAwayScore(GOALS_POSITIVE, GOALS_LARGE_POSITIVE)
                .buildAwayMeanEstimation();

        assertTrue(KappaGoals.MINIMUM_LOWER_BOUND_GOALS < awayGoalsScored);
        assertTrue(GOALS_POSITIVE < awayGoalsScored);
        assertTrue(GOALS_LARGE_POSITIVE > awayGoalsScored);
    }
    @Test
    public void buildAwayMeanEstimation_givenLowerBound_negativeAwayGoalsScored_positiveHomeGoalsConceded() {
        double awayGoalsScored = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(GOALS_POSITIVE)
                .applyHomeScore(GOALS_LARGE_POSITIVE, GOALS_POSITIVE)
                .applyAwayScore(GOALS_NEGATIVE, GOALS_LARGE_POSITIVE)
                .buildAwayMeanEstimation();

        assertTrue(GOALS_NEGATIVE < awayGoalsScored);
        assertTrue(KappaGoals.MINIMUM_LOWER_BOUND_GOALS < awayGoalsScored);
        assertEquals(GOALS_POSITIVE, awayGoalsScored, 0.0);
    }
    @Test
    public void buildAwayMeanEstimation_givenLowerBound_zeroAwayGoalsScored_positiveHomeGoalsConceded() {
        double awayGoalsScored = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(GOALS_POSITIVE)
                .applyHomeScore(GOALS_LARGE_POSITIVE, GOALS_POSITIVE)
                .applyAwayScore(GOALS_ZERO, GOALS_LARGE_POSITIVE)
                .buildAwayMeanEstimation();

        assertTrue(GOALS_ZERO < awayGoalsScored);
        assertTrue(KappaGoals.MINIMUM_LOWER_BOUND_GOALS < awayGoalsScored);
        assertEquals(GOALS_POSITIVE, awayGoalsScored, 0.0);
    }
    @Test
    public void buildAwayMeanEstimation_givenLowerBound_positiveAwayGoalsScored_positiveHomeGoalsConceded() {
        double awayGoalsScored = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(GOALS_POSITIVE)
                .applyHomeScore(GOALS_LARGE_POSITIVE, GOALS_POSITIVE)
                .applyAwayScore(GOALS_POSITIVE, GOALS_LARGE_POSITIVE)
                .buildAwayMeanEstimation();

        assertTrue(KappaGoals.MINIMUM_LOWER_BOUND_GOALS < awayGoalsScored);
        assertEquals(GOALS_POSITIVE, awayGoalsScored, 0.0);
    }
    @Test
    public void buildAwayMeanEstimation_givenLowerBound_largePositiveAwayGoalsScored_positiveHomeGoalsConceded() {
        double awayGoalsScored = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(GOALS_POSITIVE)
                .applyHomeScore(GOALS_LARGE_POSITIVE, GOALS_POSITIVE)
                .applyAwayScore(GOALS_LARGE_POSITIVE, GOALS_LARGE_POSITIVE)
                .buildAwayMeanEstimation();

        assertTrue(KappaGoals.MINIMUM_LOWER_BOUND_GOALS < awayGoalsScored);
        assertTrue(GOALS_POSITIVE < awayGoalsScored);
        assertTrue(GOALS_LARGE_POSITIVE > awayGoalsScored);
    }
    @Test
    public void buildAwayMeanEstimation_givenLowerBound_positiveAwayGoalsScored_negativeHomeGoalsConceded() {
        double awayGoalsScored = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(GOALS_POSITIVE)
                .applyHomeScore(GOALS_LARGE_POSITIVE, GOALS_NEGATIVE)
                .applyAwayScore(GOALS_POSITIVE, GOALS_LARGE_POSITIVE)
                .buildAwayMeanEstimation();

        assertTrue(KappaGoals.MINIMUM_LOWER_BOUND_GOALS < awayGoalsScored);
        assertEquals(GOALS_POSITIVE, awayGoalsScored, 0.0);
    }
    @Test
    public void buildAwayMeanEstimation_givenLowerBound_positiveAwayGoalsScored_zeroHomeGoalsConceded() {
        double awayGoalsScored = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(GOALS_POSITIVE)
                .applyHomeScore(GOALS_LARGE_POSITIVE, GOALS_ZERO)
                .applyAwayScore(GOALS_POSITIVE, GOALS_LARGE_POSITIVE)
                .buildAwayMeanEstimation();

        assertTrue(KappaGoals.MINIMUM_LOWER_BOUND_GOALS < awayGoalsScored);
        assertEquals(GOALS_POSITIVE, awayGoalsScored, 0.0);
    }
    @Test
    public void buildAwayMeanEstimation_givenLowerBound_positiveAwayGoalsScored_largePositiveHomeGoalsConceded() {
        double awayGoalsScored = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(GOALS_POSITIVE)
                .applyHomeScore(GOALS_LARGE_POSITIVE, GOALS_LARGE_POSITIVE)
                .applyAwayScore(GOALS_POSITIVE, GOALS_LARGE_POSITIVE)
                .buildAwayMeanEstimation();

        assertTrue(KappaGoals.MINIMUM_LOWER_BOUND_GOALS < awayGoalsScored);
        assertTrue(GOALS_POSITIVE < awayGoalsScored);
        assertTrue(GOALS_LARGE_POSITIVE > awayGoalsScored);
    }
}