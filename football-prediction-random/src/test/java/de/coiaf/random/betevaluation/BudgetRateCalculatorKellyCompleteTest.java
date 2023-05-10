package de.coiaf.random.betevaluation;

import de.coiaf.random.odds.OddsTest;
import de.coiaf.random.probability.Probability;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BudgetRateCalculatorKellyCompleteTest {

    private BudgetRateCalculatorKellyComplete budgetRateCalculator = null;

    @Before
    public void setUp() {
        this.budgetRateCalculator = new BudgetRateCalculatorKellyComplete();
    }

    @After
    public void tearDown() {
        this.budgetRateCalculator = null;
    }

    @Test(expected = NullPointerException.class)
    public void calculateBudgetRate_nullOdd_givenAssumedProbability() {
        this.budgetRateCalculator.calculateBudgetRate(null, Probability.UNCERTAIN);
    }
    @Test(expected = NullPointerException.class)
    public void calculateBudgetRate_givenOdd_nullAssumedProbability() {
        this.budgetRateCalculator.calculateBudgetRate(OddsTest.MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY, null);
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
        assertTrue(Probability.isImpossible(budgetRate));
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
        assertTrue(Probability.isImpossible(budgetRate));
    }
    @Test
    public void calculateBudgetRate_certainOdd_certainAssumedProbability() {
        Probability budgetRate = this.budgetRateCalculator.calculateBudgetRate(OddsTest.MOCK_ODD_CERTAIN_IMPLIED_PROBABILITY, Probability.CERTAIN);

        assertNotNull(budgetRate);
        assertTrue(Probability.isImpossible(budgetRate));
    }
}