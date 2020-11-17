package de.coiaf.random.betevaluation;

import de.coiaf.random.odds.Odd;
import de.coiaf.random.odds.OddsTest;
import de.coiaf.random.probability.Probability;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class BetValueCalculatorTest {

    private static final Odd<?> MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY = OddsTest.MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY;
    private static final Odd<?> MOCK_ODD_CERTAIN_IMPLIED_PROBABILITY = OddsTest.MOCK_ODD_CERTAIN_IMPLIED_PROBABILITY;
    private static final BigDecimal GIVEN_THRESHOLD = new BigDecimal("0.5");
    private static final BigDecimal NEGATIVE_THRESHOLD = new BigDecimal("-0.5");

    private BetValueCalculator calculator;

    @Before
    public void setUp() {
        this.calculator = new BetValueCalculator();
    }

    @After
    public void tearDown() {
        this.calculator = null;
    }

    @Test(expected = NullPointerException.class)
    public void createBetValue_nullBookmakerOdd_givenModelProbability() {
        this.calculator.createBetValue(null, Probability.UNCERTAIN);
    }
    @Test(expected = NullPointerException.class)
    public void createBetValue_givenBookmakerOdd_nullModelProbability() {
        this.calculator.createBetValue(MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY, null);
    }
    @Test
    public void createBetValue_givenBookmakerOdd_givenModelProbability_bookmakerOddProbabilityBelowModelProbability() {
        BetValueCalculator.BetValue value = this.calculator.createBetValue(MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY, Probability.CERTAIN);

        assertNotNull(value);
        assertNotNull(value.toBigDecimal());
        assertTrue(BigDecimal.ZERO.compareTo(value.toBigDecimal()) < 0);
        assertTrue(value.isValueBet());
        assertEquals(BetValueCalculator.DEFAULT_VALUE_BET_THRESHOLD, value.getValueThreshold());
    }
    @Test
    public void createBetValue_givenBookmakerOdd_givenModelProbability_bookmakerOddProbabilityEqualsModelProbability() {
        BetValueCalculator.BetValue value = this.calculator.createBetValue(MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY, Probability.UNCERTAIN);

        assertNotNull(value);
        assertNotNull(value.toBigDecimal());
        assertEquals(0, BigDecimal.ZERO.compareTo(value.toBigDecimal()));
        assertFalse(value.isValueBet());
        assertEquals(BetValueCalculator.DEFAULT_VALUE_BET_THRESHOLD, value.getValueThreshold());
    }
    @Test
    public void createBetValue_givenBookmakerOdd_givenModelProbability_bookmakerOddProbabilityAboveModelProbability() {
        BetValueCalculator.BetValue value = this.calculator.createBetValue(MOCK_ODD_CERTAIN_IMPLIED_PROBABILITY, Probability.UNCERTAIN);

        assertNotNull(value);
        assertNotNull(value.toBigDecimal());
        assertEquals(0, BigDecimal.ZERO.compareTo(value.toBigDecimal()));
        assertFalse(value.isValueBet());
        assertEquals(BetValueCalculator.DEFAULT_VALUE_BET_THRESHOLD, value.getValueThreshold());
    }
    @Test
    public void createBetValue_givenBookmakerOdd_impossibleModelProbability() {
        BetValueCalculator.BetValue value = this.calculator.createBetValue(MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY, Probability.IMPOSSIBLE);

        assertNotNull(value);
        assertNotNull(value.toBigDecimal());
        assertEquals(BigDecimal.ZERO, value.toBigDecimal());
        assertFalse(value.isValueBet());
        assertEquals(BetValueCalculator.DEFAULT_VALUE_BET_THRESHOLD, value.getValueThreshold());
    }

    @Test(expected = NullPointerException.class)
    public void createBetValue_nullBookmakerOdd_givenModelProbability_givenThreshold() {
        this.calculator.createBetValue(null, Probability.UNCERTAIN, GIVEN_THRESHOLD);
    }
    @Test(expected = NullPointerException.class)
    public void createBetValue_givenBookmakerOdd_nullModelProbability_givenThreshold() {
        this.calculator.createBetValue(MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY, null, GIVEN_THRESHOLD);
    }
    @Test
    public void createBetValue_givenBookmakerOdd_givenModelProbability_nullThreshold() {
        BetValueCalculator.BetValue value = this.calculator.createBetValue(MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY, Probability.UNCERTAIN, null);

        assertNotNull(value);
        assertNotNull(value.toBigDecimal());
        assertEquals(BetValueCalculator.DEFAULT_VALUE_BET_THRESHOLD, value.getValueThreshold());
    }
    @Test
    public void createBetValue_givenBookmakerOdd_givenModelProbability_negativeThreshold() {
        BetValueCalculator.BetValue value = this.calculator.createBetValue(MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY, Probability.UNCERTAIN, NEGATIVE_THRESHOLD);

        assertNotNull(value);
        assertNotNull(value.toBigDecimal());
        assertEquals(BetValueCalculator.DEFAULT_VALUE_BET_THRESHOLD, value.getValueThreshold());
    }
    @Test
    public void createBetValue_givenBookmakerOdd_givenModelProbability_givenThreshold_bookmakerOddProbabilityBelowModelProbability() {
        BetValueCalculator.BetValue value = this.calculator.createBetValue(MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY, Probability.CERTAIN, GIVEN_THRESHOLD);

        assertNotNull(value);
        assertNotNull(value.toBigDecimal());
        assertTrue(BigDecimal.ZERO.compareTo(value.toBigDecimal()) < 0);
        assertTrue(value.isValueBet());
        assertEquals(GIVEN_THRESHOLD, value.getValueThreshold());
    }
    @Test
    public void createBetValue_givenBookmakerOdd_givenModelProbability_givenThreshold_bookmakerOddProbabilityEqualsModelProbability() {
        BetValueCalculator.BetValue value = this.calculator.createBetValue(MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY, Probability.UNCERTAIN, GIVEN_THRESHOLD);

        assertNotNull(value);
        assertNotNull(value.toBigDecimal());
        assertEquals(0, BigDecimal.ZERO.compareTo(value.toBigDecimal()));
        assertFalse(value.isValueBet());
        assertEquals(GIVEN_THRESHOLD, value.getValueThreshold());
    }
    @Test
    public void createBetValue_givenBookmakerOdd_givenModelProbability_givenThreshold_bookmakerOddProbabilityAboveModelProbability() {
        BetValueCalculator.BetValue value = this.calculator.createBetValue(MOCK_ODD_CERTAIN_IMPLIED_PROBABILITY, Probability.UNCERTAIN, GIVEN_THRESHOLD);

        assertNotNull(value);
        assertNotNull(value.toBigDecimal());
        assertEquals(0, BigDecimal.ZERO.compareTo(value.toBigDecimal()));
        assertFalse(value.isValueBet());
        assertEquals(GIVEN_THRESHOLD, value.getValueThreshold());
    }
    @Test
    public void createBetValue_givenBookmakerOdd_impossibleModelProbability_givenThreshold() {
        BetValueCalculator.BetValue value = this.calculator.createBetValue(MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY, Probability.IMPOSSIBLE, GIVEN_THRESHOLD);

        assertNotNull(value);
        assertNotNull(value.toBigDecimal());
        assertEquals(BigDecimal.ZERO, value.toBigDecimal());
        assertFalse(value.isValueBet());
        assertEquals(GIVEN_THRESHOLD, value.getValueThreshold());
    }
}