package de.coiaf.footballprediction.backend.model.probability.poisson;

import de.coiaf.footballprediction.backend.model.sharedcontext.*;
import de.coiaf.footballprediction.backend.persistence.entity.NumericSizes;
import de.coiaf.footballprediction.sharedkernal.domain.model.prediction.EstimatedGoals;
import de.coiaf.footballprediction.sharedkernal.domain.model.prediction.EstimatedScore;
import de.coiaf.random.probability.Probability;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class PoissonOddGroupCalculatorTest {

    private static final ThresholdTotalGoals THRESHOLD_DEFAULT = ThresholdTotalGoals.getDefaultInstance();
    private static final EstimatedGoals GOALS_HOME = EstimatedGoals.valueOf(new BigDecimal("1.59"));
    private static final EstimatedGoals GOALS_AWAY = EstimatedGoals.valueOf(new BigDecimal("1.00"));
    private static final EstimatedGoals GOALS_TOTAL = GOALS_HOME.add(GOALS_AWAY);
    private static final EstimatedScore SCORE = EstimatedScore.valueOf(GOALS_HOME, GOALS_AWAY);
    private static final Probability PROBABILITY_TOTAL_GOALS_BELOW_THRESHOLD = Probability.valueOf(new BigDecimal("0.5209429093545"));
    private static final Probability PROBABILITY_TOTAL_GOALS_ABOVE_THRESHOLD = Probability.valueOf(new BigDecimal("0.4790570906455"));
    private static final Probability PROBABILITY_HOME_WIN = Probability.valueOf(new BigDecimal("0.5109326216938"));
    private static final Probability PROBABILITY_DRAW = Probability.valueOf(new BigDecimal("0.2509807931601"));
    private static final Probability PROBABILITY_AWAY_WIN = Probability.valueOf(new BigDecimal("0.2380865851461"));

    private static BigDecimal normalizeProbability(Probability probability) {
        return probability == null ? null : probability.toBigDecimal().setScale(NumericSizes.PROBABILITY_SCALE, BigDecimal.ROUND_HALF_UP);
    }

    private PoissonOddGroupCalculator calculator;

    @Before
    public void setUp() {
        this.calculator = spy(new PoissonOddGroupCalculator());
    }

    @After
    public void tearDown() {
        this.calculator = null;
    }

    @Test(expected = NullPointerException.class)
    public void createTotalGoalsOdds_nullScore() {
        this.calculator.createTotalGoalsOdds((EstimatedScore) null);
    }
    @Test
    public void createTotalGoalsOdds_givenScore() {
        OddGroupTotalGoals result = this.calculator.createTotalGoalsOdds(SCORE);

        assertNotNull(result);
        verify(this.calculator, times(1)).createTotalGoalsOdds(SCORE.getTotalGoals(), THRESHOLD_DEFAULT);
    }
    @Test(expected = NullPointerException.class)
    public void createTotalGoalsOdds_nullScore_givenThreshold() {
        this.calculator.createTotalGoalsOdds((EstimatedScore) null, THRESHOLD_DEFAULT);
    }
    @Test(expected = NullPointerException.class)
    public void createTotalGoalsOdds_givenScore_nullThreshold() {
        this.calculator.createTotalGoalsOdds(SCORE, null);
    }
    @Test
    public void createTotalGoalsOdds_givenScore_givenThreshold() {
        OddGroupTotalGoals result = this.calculator.createTotalGoalsOdds(SCORE, THRESHOLD_DEFAULT);

        assertNotNull(result);
        verify(this.calculator, times(1)).createTotalGoalsOdds(SCORE.getTotalGoals(), THRESHOLD_DEFAULT);
    }

    @Test(expected = NullPointerException.class)
    public void createTotalGoalsOdds_nullGoals() {
        this.calculator.createTotalGoalsOdds((EstimatedGoals) null);
    }
    @Test
    public void createTotalGoalsOdds_givenGoals() {
        OddGroupTotalGoals result = this.calculator.createTotalGoalsOdds(GOALS_TOTAL);

        assertNotNull(result);
        verify(this.calculator, times(1)).createTotalGoalsOdds(GOALS_TOTAL, THRESHOLD_DEFAULT);
    }
    @Test(expected = NullPointerException.class)
    public void createTotalGoalsOdds_nullGoals_givenThreshold() {
        this.calculator.createTotalGoalsOdds((EstimatedGoals) null, THRESHOLD_DEFAULT);
    }
    @Test(expected = NullPointerException.class)
    public void createTotalGoalsOdds_givenGoals_nullThreshold() {
        this.calculator.createTotalGoalsOdds(GOALS_TOTAL, null);
    }
    @Test
    public void createTotalGoalsOdds_givenGoals_givenThreshold() {
        OddGroupTotalGoals result = this.calculator.createTotalGoalsOdds(GOALS_TOTAL, THRESHOLD_DEFAULT);

        assertNotNull(result);
        assertEquals(
                normalizeProbability(PROBABILITY_TOTAL_GOALS_BELOW_THRESHOLD),
                normalizeProbability(result.getOddBelowThreshold().getImpliedProbability()));
        assertEquals(
                normalizeProbability(PROBABILITY_TOTAL_GOALS_ABOVE_THRESHOLD),
                normalizeProbability(result.getOddAboveThreshold().getImpliedProbability()));
    }

    @Test(expected = NullPointerException.class)
    public void createOutcomeOdds_nullScore() {
        this.calculator.createOutcomeOdds(null);
    }
    @Test
    public void createOutcomeOdds_givenScore() {
        OddGroupOutcome result = this.calculator.createOutcomeOdds(SCORE);

        assertNotNull(result);
        assertEquals(
                normalizeProbability(PROBABILITY_HOME_WIN),
                normalizeProbability(result.getOddHomeWin().getImpliedProbability())
        );
        assertEquals(
                normalizeProbability(PROBABILITY_DRAW),
                normalizeProbability(result.getOddDraw().getImpliedProbability())
        );
        assertEquals(
                normalizeProbability(PROBABILITY_AWAY_WIN),
                normalizeProbability(result.getOddAwayWin().getImpliedProbability())
        );
    }
}