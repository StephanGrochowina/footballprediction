package de.coiaf.random.betevaluation;

import de.coiaf.random.odds.OddsTest;
import de.coiaf.random.probability.Probability;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BudgetRateCalculatorProbabilityOnlyTest {

    private BudgetRateCalculatorProbabilityOnly budgetRateCalculator = null;

    @Before
    public void setUp() {
        this.budgetRateCalculator = new BudgetRateCalculatorProbabilityOnly();
    }

    @After
    public void tearDown() {
        this.budgetRateCalculator = null;
    }

    @Test
    public void calculateBudgetRate_nullOdd_givenAssumedProbability() {
        Probability budgetRate = this.budgetRateCalculator.calculateBudgetRate(null, Probability.UNCERTAIN);

        assertNotNull(budgetRate);
        assertFalse(Probability.isImpossible(budgetRate));
        assertFalse(Probability.isCertain(budgetRate));
        assertEquals(Probability.UNCERTAIN, budgetRate);
    }

    @Test
    public void calculateBudgetRate_givenOdd_nullAssumedProbability() {
        Probability budgetRate = this.budgetRateCalculator.calculateBudgetRate(OddsTest.MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY, null);

        assertNotNull(budgetRate);
        assertTrue(Probability.isImpossible(budgetRate));
    }
    @Test
    public void calculateBudgetRate_impossibleOdd_impossibleAssumedProbability() {
        Probability budgetRate = this.budgetRateCalculator.calculateBudgetRate(OddsTest.MOCK_ODD_IMPOSSIBLE_IMPLIED_PROBABILITY, Probability.IMPOSSIBLE);

        assertNotNull(budgetRate);
        assertTrue(Probability.isImpossible(budgetRate));
    }
    @Test
    public void calculateBudgetRate_impossibleOdd_uncertainAssumedProbability() {
        Probability budgetRate = this.budgetRateCalculator.calculateBudgetRate(OddsTest.MOCK_ODD_IMPOSSIBLE_IMPLIED_PROBABILITY, Probability.UNCERTAIN);

        assertNotNull(budgetRate);
        assertFalse(Probability.isImpossible(budgetRate));
        assertFalse(Probability.isCertain(budgetRate));
        assertEquals(Probability.UNCERTAIN, budgetRate);
    }
    @Test
    public void calculateBudgetRate_impossibleOdd_certainAssumedProbability() {
        Probability budgetRate = this.budgetRateCalculator.calculateBudgetRate(OddsTest.MOCK_ODD_IMPOSSIBLE_IMPLIED_PROBABILITY, Probability.CERTAIN);

        assertNotNull(budgetRate);
        assertTrue(Probability.isCertain(budgetRate));
    }
    @Test
    public void calculateBudgetRate_uncertainOdd_impossibleAssumedProbability() {
        Probability budgetRate = this.budgetRateCalculator.calculateBudgetRate(OddsTest.MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY, Probability.IMPOSSIBLE);

        assertNotNull(budgetRate);
        assertTrue(Probability.isImpossible(budgetRate));
    }
    @Test
    public void calculateBudgetRate_uncertainOdd_uncertainAssumedProbability() {
        Probability budgetRate = this.budgetRateCalculator.calculateBudgetRate(OddsTest.MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY, Probability.UNCERTAIN);

        assertNotNull(budgetRate);
        assertFalse(Probability.isImpossible(budgetRate));
        assertFalse(Probability.isCertain(budgetRate));
        assertEquals(Probability.UNCERTAIN, budgetRate);
    }
    @Test
    public void calculateBudgetRate_uncertainOdd_certainAssumedProbability() {
        Probability budgetRate = this.budgetRateCalculator.calculateBudgetRate(OddsTest.MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY, Probability.CERTAIN);

        assertNotNull(budgetRate);
        assertTrue(Probability.isCertain(budgetRate));
    }
    @Test
    public void calculateBudgetRate_certainOdd_impossibleAssumedProbability() {
        Probability budgetRate = this.budgetRateCalculator.calculateBudgetRate(OddsTest.MOCK_ODD_CERTAIN_IMPLIED_PROBABILITY, Probability.IMPOSSIBLE);

        assertNotNull(budgetRate);
        assertTrue(Probability.isImpossible(budgetRate));
    }
    @Test
    public void calculateBudgetRate_certainOdd_uncertainAssumedProbability() {
        Probability budgetRate = this.budgetRateCalculator.calculateBudgetRate(OddsTest.MOCK_ODD_CERTAIN_IMPLIED_PROBABILITY, Probability.UNCERTAIN);

        assertNotNull(budgetRate);
        assertFalse(Probability.isImpossible(budgetRate));
        assertFalse(Probability.isCertain(budgetRate));
        assertEquals(Probability.UNCERTAIN, budgetRate);
    }
    @Test
    public void calculateBudgetRate_certainOdd_certainAssumedProbability() {
        Probability budgetRate = this.budgetRateCalculator.calculateBudgetRate(OddsTest.MOCK_ODD_CERTAIN_IMPLIED_PROBABILITY, Probability.CERTAIN);

        assertNotNull(budgetRate);
        assertFalse(Probability.isImpossible(budgetRate));
        assertTrue(Probability.isCertain(budgetRate));
    }
}