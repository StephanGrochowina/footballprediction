package de.coiaf.random.betevaluation;

import de.coiaf.random.odds.OddsTest;
import de.coiaf.random.probability.Probability;
import org.junit.Test;

import static org.junit.Assert.*;

public class BudgetRateCalculatorFractionalKellyTest {

    @Test(expected = NullPointerException.class)
    public void calculateBudgetRate_nullOdd_givenAssumedProbability_givenFraction() {
        BudgetRateCalculatorFractionalKelly budgetRateCalculator = new BudgetRateCalculatorFractionalKelly(Probability.UNCERTAIN);

        budgetRateCalculator.calculateBudgetRate(null, Probability.UNCERTAIN);
    }
    @Test(expected = NullPointerException.class)
    public void calculateBudgetRate_givenOdd_nullAssumedProbability_givenFraction() {
        BudgetRateCalculatorFractionalKelly budgetRateCalculator = new BudgetRateCalculatorFractionalKelly(Probability.UNCERTAIN);

        budgetRateCalculator.calculateBudgetRate(OddsTest.MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY, null);
    }
    @Test(expected = NullPointerException.class)
    public void calculateBudgetRate_givenOdd_nullAssumedProbability_nullFraction() {
        BudgetRateCalculatorFractionalKelly budgetRateCalculator = new BudgetRateCalculatorFractionalKelly((Probability) null);
    }
    @Test(expected = IllegalArgumentException.class)
    public void calculateBudgetRate_givenOdd_nullAssumedProbability_zeroFraction() {
        BudgetRateCalculatorFractionalKelly budgetRateCalculator = new BudgetRateCalculatorFractionalKelly(Probability.IMPOSSIBLE);
    }
    @Test
    public void calculateBudgetRate_impossibleOdd_impossibleAssumedProbability_givenFraction() {
        BudgetRateCalculatorFractionalKelly budgetRateCalculator = new BudgetRateCalculatorFractionalKelly(Probability.UNCERTAIN);

        Probability budgetRate = budgetRateCalculator.calculateBudgetRate(OddsTest.MOCK_ODD_IMPOSSIBLE_IMPLIED_PROBABILITY, Probability.IMPOSSIBLE);

        assertNotNull(budgetRate);
        assertTrue(Probability.isImpossible(budgetRate));
    }
    @Test
    public void calculateBudgetRate_impossibleOdd_uncertainAssumedProbability_givenFraction() {
        BudgetRateCalculatorFractionalKelly budgetRateCalculator = new BudgetRateCalculatorFractionalKelly(Probability.UNCERTAIN);

        Probability budgetRate = budgetRateCalculator.calculateBudgetRate(OddsTest.MOCK_ODD_IMPOSSIBLE_IMPLIED_PROBABILITY, Probability.UNCERTAIN);

        assertNotNull(budgetRate);
        assertFalse(Probability.isImpossible(budgetRate));
        assertFalse(Probability.isCertain(budgetRate));
    }
    @Test
    public void calculateBudgetRate_impossibleOdd_certainAssumedProbability_givenFraction() {
        BudgetRateCalculatorFractionalKelly budgetRateCalculator = new BudgetRateCalculatorFractionalKelly(Probability.UNCERTAIN);

        Probability budgetRate = budgetRateCalculator.calculateBudgetRate(OddsTest.MOCK_ODD_IMPOSSIBLE_IMPLIED_PROBABILITY, Probability.CERTAIN);

        assertNotNull(budgetRate);
        assertFalse(Probability.isImpossible(budgetRate));
        assertFalse(Probability.isCertain(budgetRate));
    }
    @Test
    public void calculateBudgetRate_uncertainOdd_impossibleAssumedProbability_givenFraction() {
        BudgetRateCalculatorFractionalKelly budgetRateCalculator = new BudgetRateCalculatorFractionalKelly(Probability.UNCERTAIN);

        Probability budgetRate = budgetRateCalculator.calculateBudgetRate(OddsTest.MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY, Probability.IMPOSSIBLE);

        assertNotNull(budgetRate);
        assertTrue(Probability.isImpossible(budgetRate));
    }
    @Test
    public void calculateBudgetRate_uncertainOdd_uncertainAssumedProbability_givenFraction() {
        BudgetRateCalculatorFractionalKelly budgetRateCalculator = new BudgetRateCalculatorFractionalKelly(Probability.UNCERTAIN);

        Probability budgetRate = budgetRateCalculator.calculateBudgetRate(OddsTest.MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY, Probability.UNCERTAIN);

        assertNotNull(budgetRate);
        assertTrue(Probability.isImpossible(budgetRate));
    }
    @Test
    public void calculateBudgetRate_uncertainOdd_certainAssumedProbability_givenFraction() {
        BudgetRateCalculatorFractionalKelly budgetRateCalculator = new BudgetRateCalculatorFractionalKelly(Probability.UNCERTAIN);

        Probability budgetRate = budgetRateCalculator.calculateBudgetRate(OddsTest.MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY, Probability.CERTAIN);

        assertNotNull(budgetRate);
        assertFalse(Probability.isImpossible(budgetRate));
        assertFalse(Probability.isCertain(budgetRate));
    }
    @Test
    public void calculateBudgetRate_certainOdd_impossibleAssumedProbability_givenFraction() {
        BudgetRateCalculatorFractionalKelly budgetRateCalculator = new BudgetRateCalculatorFractionalKelly(Probability.UNCERTAIN);

        Probability budgetRate = budgetRateCalculator.calculateBudgetRate(OddsTest.MOCK_ODD_CERTAIN_IMPLIED_PROBABILITY, Probability.IMPOSSIBLE);

        assertNotNull(budgetRate);
        assertTrue(Probability.isImpossible(budgetRate));
    }
    @Test
    public void calculateBudgetRate_certainOdd_uncertainAssumedProbability_givenFraction() {
        BudgetRateCalculatorFractionalKelly budgetRateCalculator = new BudgetRateCalculatorFractionalKelly(Probability.UNCERTAIN);

        Probability budgetRate = budgetRateCalculator.calculateBudgetRate(OddsTest.MOCK_ODD_CERTAIN_IMPLIED_PROBABILITY, Probability.UNCERTAIN);

        assertNotNull(budgetRate);
        assertTrue(Probability.isImpossible(budgetRate));
    }
    @Test
    public void calculateBudgetRate_certainOdd_certainAssumedProbability_givenFraction() {
        BudgetRateCalculatorFractionalKelly budgetRateCalculator = new BudgetRateCalculatorFractionalKelly(Probability.UNCERTAIN);

        Probability budgetRate = budgetRateCalculator.calculateBudgetRate(OddsTest.MOCK_ODD_CERTAIN_IMPLIED_PROBABILITY, Probability.CERTAIN);

        assertNotNull(budgetRate);
        assertTrue(Probability.isImpossible(budgetRate));
    }
}