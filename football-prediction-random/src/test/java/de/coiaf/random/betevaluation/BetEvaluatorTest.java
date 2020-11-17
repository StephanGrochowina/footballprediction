package de.coiaf.random.betevaluation;

import de.coiaf.random.odds.Odd;
import de.coiaf.random.odds.OddsTest;
import de.coiaf.random.probability.Probability;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BetEvaluatorTest {

    private static final Odd<?> MOCK_ODD_IMPOSSIBLE_IMPLIED_PROBABILITY = OddsTest.MOCK_ODD_IMPOSSIBLE_IMPLIED_PROBABILITY;
    private static final Odd<?> MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY = OddsTest.MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY;
    private static final BigDecimal INVESTMENT = Probability.CERTAIN.toBigDecimal();
    private static final BigDecimal BALANCE = MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY.getDecimalOddValue();

    private BudgetRateCalculator calculatorMock = null;
    private BetEvaluator evaluator;

    @Before
    public void setUp() {
        this.calculatorMock = mock(BudgetRateCalculator.class);
        when(this.calculatorMock.calculateBet(any(Odd.class), any(Probability.class), any(BigDecimal.class))).thenCallRealMethod();

        this.evaluator = spy(new BetEvaluator(this.calculatorMock));
    }

    @After
    public void tearDown() {
        this.calculatorMock = null;
        this.evaluator = null;
    }

    @Test(expected = NullPointerException.class)
    public void constructor_nullCalculator() {
        BetEvaluator evaluator = new BetEvaluator(null);
    }
    @Test
    public void constructor_givenCalculator() {
        BetEvaluator evaluator = new BetEvaluator(this.calculatorMock);

        assertNotNull(evaluator);
    }

    @Test(expected = NullPointerException.class)
    public void evaluate_nullContexts() {
        this.evaluator.evaluate(null);
    }
    @Test
    public void evaluate_emptyContexts() {
        List<BetEvaluator.EvaluationContext> contexts = Collections.emptyList();

        BetEvaluator.AggregatedEvaluationResult result = this.evaluator.evaluate(contexts.stream());

        assertNotNull(result);
        assertEquals(BetEvaluator.EMPTY_EVALUATION_RESULT, result);
        assertFalse(result.isCompetitiveModel());
        assertEquals(0, result.getEvaluatedBets());
    }
    @Test
    public void evaluate_singleContextWithoutValue() {
        List<BetEvaluator.EvaluationContext> contexts = Collections.singletonList(this.createDefaultEvaluationContextWithWonBet());

        this.initializeBudgetRatecalculatorNoValueBets();

        BetEvaluator.AggregatedEvaluationResult result = this.evaluator.evaluate(contexts.stream());

        assertNotNull(result);
        assertEquals(BetEvaluator.EMPTY_EVALUATION_RESULT, result);
        assertFalse(result.isCompetitiveModel());
        assertEquals(0, result.getEvaluatedBets());
    }
    @Test
    public void evaluate_singleContextWithValue_betWon() {
        List<BetEvaluator.EvaluationContext> contexts = Collections.singletonList(this.createDefaultEvaluationContextWithWonBet());

        this.initializeBudgetRatecalculatorOnlyValueBets();

        BetEvaluator.AggregatedEvaluationResult result = this.evaluator.evaluate(contexts.stream());

        assertNotNull(result);
        assertNotEquals(BetEvaluator.EMPTY_EVALUATION_RESULT, result);
        assertTrue(result.isCompetitiveModel());
        assertEquals(1, result.getEvaluatedBets());
    }
    @Test
    public void evaluate_singleContextWithValue_betLost() {
        List<BetEvaluator.EvaluationContext> contexts = Collections.singletonList(this.createDefaultEvaluationContextWithLostBet());

        this.initializeBudgetRatecalculatorOnlyValueBets();

        BetEvaluator.AggregatedEvaluationResult result = this.evaluator.evaluate(contexts.stream());

        assertNotNull(result);
        assertNotEquals(BetEvaluator.EMPTY_EVALUATION_RESULT, result);
        assertFalse(result.isCompetitiveModel());
        assertEquals(1, result.getEvaluatedBets());
        assertTrue(BigDecimal.ZERO.compareTo(result.getAverageBalance()) > 0);
    }
    @Test
    public void evaluate_multipleContextsWithValue_betWon() {
        List<BetEvaluator.EvaluationContext> contexts = Arrays.asList(
                this.createDefaultEvaluationContextWithWonBet(),
                this.createDefaultEvaluationContextWithWonBet(),
                this.createDefaultEvaluationContextWithWonBet(),
                this.createDefaultEvaluationContextWithWonBet()
        );

        this.initializeBudgetRatecalculatorOnlyValueBets();

        BetEvaluator.AggregatedEvaluationResult result = this.evaluator.evaluate(contexts.stream());

        assertNotNull(result);
        assertNotEquals(BetEvaluator.EMPTY_EVALUATION_RESULT, result);
        assertTrue(result.isCompetitiveModel());
        assertEquals(contexts.size(), result.getEvaluatedBets());
    }

    @Test(expected = NullPointerException.class)
    public void createEvaluationContext_nullBookmakersOdd_givenModelProbability_hasWon() {
        this.evaluator.createEvaluationContext(null, Probability.CERTAIN, true);
    }
    @Test(expected = NullPointerException.class)
    public void createEvaluationContext_givenBookmakersOdd_nullModelProbability_hasWon() {
        this.evaluator.createEvaluationContext(MOCK_ODD_IMPOSSIBLE_IMPLIED_PROBABILITY, null, true);
    }
    @Test
    public void createEvaluationContext_givenBookmakersOdd_givenModelProbability_hasWon() {
        this.verifyCreateEvaluationContext(true);
    }
    @Test
    public void createEvaluationContext_givenBookmakersOdd_givenModelProbability_hasLost() {
        this.verifyCreateEvaluationContext(false);
    }
    private void verifyCreateEvaluationContext(boolean hasWon) {
        Odd<?> bookmakersOdd = MOCK_ODD_IMPOSSIBLE_IMPLIED_PROBABILITY;
        Probability modelProbability = Probability.CERTAIN;

        BetEvaluator.EvaluationContext context = this.evaluator.createEvaluationContext(bookmakersOdd, modelProbability, hasWon);

        assertNotNull(context);
        assertNotNull(context.getBookmakersOdd());
        assertEquals(bookmakersOdd, context.getBookmakersOdd());
        assertNotNull(context.getModelProbability());
        assertEquals(modelProbability, context.getModelProbability());
        assertEquals(hasWon, context.isHasWon());
    }

    @Test(expected = NullPointerException.class)
    public void createEvaluationResult_nullContext() {
        this.evaluator.createEvaluationResult(null);
    }
    @Test
    public void createEvaluationResult_noValueBet() {
        BetEvaluator.EvaluationContext context = this.createEvaluationContext(MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY, Probability.CERTAIN, true);
        BetEvaluator.AggregatedEvaluationResult result;

        this.initializeBudgetRatecalculatorNoValueBets();

        result = this.evaluator.createEvaluationResult(context);

        assertNotNull(result);
        assertEquals(1, result.getEvaluatedBets());
        assertNotNull(result.getAggregatedBookmakersOdds());
        assertEquals(MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY.getDecimalOddValue(), result.getAggregatedBookmakersOdds());
        assertNotNull(result.getAggregatedBalance());
        assertEquals(0, BigDecimal.ZERO.compareTo(result.getAggregatedBalance()));
        assertNotNull(result.getAggregatedInvestment());
        assertEquals(0, BigDecimal.ZERO.compareTo(result.getAggregatedInvestment()));
        assertNotNull(result.getAverageBalance());
        assertEquals(0, BigDecimal.ZERO.compareTo(result.getAverageBalance()));
        assertFalse(result.isCompetitiveModel());
    }
    @Test
    public void createEvaluationResult_wonValueBet() {
        BetEvaluator.EvaluationContext context = this.createEvaluationContext(MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY, Probability.CERTAIN, true);
        BetEvaluator.AggregatedEvaluationResult result;
        BigDecimal expectedWinAmount = MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY.getDecimalOddValue().subtract(BigDecimal.ONE);
        BigDecimal expectedInvestmentAmount = Probability.CERTAIN.toBigDecimal();

        this.initializeBudgetRatecalculatorOnlyValueBets();

        result = this.evaluator.createEvaluationResult(context);

        assertNotNull(result);
        assertEquals(1, result.getEvaluatedBets());
        assertNotNull(result.getAggregatedBookmakersOdds());
        assertEquals(MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY.getDecimalOddValue(), result.getAggregatedBookmakersOdds());
        assertNotNull(result.getAggregatedBalance());
        assertEquals(0, expectedWinAmount.compareTo(result.getAggregatedBalance()));
        assertNotNull(result.getAggregatedInvestment());
        assertEquals(0, expectedInvestmentAmount.compareTo(result.getAggregatedInvestment()));
        assertNotNull(result.getAverageBalance());
        assertEquals(0, expectedWinAmount.compareTo(result.getAverageBalance()));
        assertTrue(result.isCompetitiveModel());
    }
    @Test
    public void createEvaluationResult_lostValueBet() {
        BetEvaluator.EvaluationContext context = this.createEvaluationContext(MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY, Probability.CERTAIN, false);
        BetEvaluator.AggregatedEvaluationResult result;
        BigDecimal expectedInvestmentAmount = Probability.CERTAIN.toBigDecimal();
        BigDecimal expectedWinAmount = expectedInvestmentAmount.negate();

        this.initializeBudgetRatecalculatorOnlyValueBets();

        result = this.evaluator.createEvaluationResult(context);

        assertNotNull(result);
        assertEquals(1, result.getEvaluatedBets());
        assertNotNull(result.getAggregatedBookmakersOdds());
        assertEquals(MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY.getDecimalOddValue(), result.getAggregatedBookmakersOdds());
        assertNotNull(result.getAggregatedBalance());
        assertEquals(0, expectedWinAmount.compareTo(result.getAggregatedBalance()));
        assertNotNull(result.getAggregatedInvestment());
        assertEquals(0, expectedInvestmentAmount.compareTo(result.getAggregatedInvestment()));
        assertNotNull(result.getAverageBalance());
        assertEquals(0, expectedWinAmount.compareTo(result.getAverageBalance()));
        assertFalse(result.isCompetitiveModel());
    }

    @Test(expected = NullPointerException.class)
    public void addToAggregatedEvaluationResult_nullBookmakersOdd_givenBalance_givenInvestment() {
        BetEvaluator.EMPTY_EVALUATION_RESULT.add(null, BALANCE, INVESTMENT);
    }
    @Test(expected = NullPointerException.class)
    public void addToAggregatedEvaluationResult_givenBookmakersOdd_nullBalance_givenInvestment() {
        BetEvaluator.EMPTY_EVALUATION_RESULT.add(MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY.getDecimalOddValue(), null, INVESTMENT);
    }
    @Test(expected = NullPointerException.class)
    public void addToAggregatedEvaluationResult_givenBookmakersOdd_givenBalance_nullInvestment() {
        BetEvaluator.EMPTY_EVALUATION_RESULT.add(MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY.getDecimalOddValue(), BALANCE, null);
    }
    @Test
    public void addToAggregatedEvaluationResult_givenBookmakersOdd_givenBalance_givenInvestment() {
        BetEvaluator.AggregatedEvaluationResult result = BetEvaluator.EMPTY_EVALUATION_RESULT.add(MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY.getDecimalOddValue(), BALANCE, INVESTMENT);

        assertNotNull(result);
        assertEquals(1, result.getEvaluatedBets());
        assertNotNull(result.getAggregatedBookmakersOdds());
        assertEquals(MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY.getDecimalOddValue(), result.getAggregatedBookmakersOdds());
        assertNotNull(result.getAggregatedBalance());
        assertEquals(0, BALANCE.compareTo(result.getAggregatedBalance()));
        assertNotNull(result.getAggregatedInvestment());
        assertEquals(0, INVESTMENT.compareTo(result.getAggregatedInvestment()));
        assertNotNull(result.getAverageBalance());
        assertEquals(0, BALANCE.compareTo(result.getAverageBalance()));
    }

    @Test(expected = NullPointerException.class)
    public void addToAggregatedEvaluationResult_nullAggregatedEvaluationResult() {
        BetEvaluator.EMPTY_EVALUATION_RESULT.add(null);
    }
    @Test
    public void addToAggregatedEvaluationResult_givenAggregatedEvaluationResult() {
        BetEvaluator.AggregatedEvaluationResult addible = BetEvaluator.EMPTY_EVALUATION_RESULT.add(MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY.getDecimalOddValue(), BALANCE, INVESTMENT);
        BetEvaluator.AggregatedEvaluationResult result = BetEvaluator.EMPTY_EVALUATION_RESULT.add(addible);

        assertNotNull(result);
        assertEquals(1, result.getEvaluatedBets());
        assertNotNull(result.getAggregatedBookmakersOdds());
        assertEquals(MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY.getDecimalOddValue(), result.getAggregatedBookmakersOdds());
        assertNotNull(result.getAggregatedBalance());
        assertEquals(0, BALANCE.compareTo(result.getAggregatedBalance()));
        assertNotNull(result.getAggregatedInvestment());
        assertEquals(0, INVESTMENT.compareTo(result.getAggregatedInvestment()));
        assertNotNull(result.getAverageBalance());
        assertEquals(0, BALANCE.compareTo(result.getAverageBalance()));
    }

    private void initializeBudgetRatecalculatorNoValueBets() {
        this.initializeBudgetRatecalculator(Probability.IMPOSSIBLE);
    }
    private void initializeBudgetRatecalculatorOnlyValueBets() {
        this.initializeBudgetRatecalculator(Probability.CERTAIN);
    }
    private void initializeBudgetRatecalculator(Probability budgetRate, Probability... budgetRates) {
        Probability usedBudgetRate = budgetRate == null ? Probability.IMPOSSIBLE : budgetRate;

        if (budgetRates.length == 0) {
            when(this.calculatorMock.calculateBudgetRate(any(Odd.class), any(Probability.class))).thenReturn(usedBudgetRate);
        }
        else {
            when(this.calculatorMock.calculateBudgetRate(any(Odd.class), any(Probability.class))).thenReturn(usedBudgetRate, budgetRates);
        }
    }

    private BetEvaluator.EvaluationContext createDefaultEvaluationContextWithWonBet() {
        return this.createEvaluationContext(MOCK_ODD_IMPOSSIBLE_IMPLIED_PROBABILITY, Probability.CERTAIN, true);
    }
    private BetEvaluator.EvaluationContext createDefaultEvaluationContextWithLostBet() {
        return this.createEvaluationContext(MOCK_ODD_IMPOSSIBLE_IMPLIED_PROBABILITY, Probability.CERTAIN, false);
    }
    @SuppressWarnings("unchecked")
    private BetEvaluator.EvaluationContext createEvaluationContext(Odd bookmakersOdd, Probability modelProbability, boolean hasWon) {
        BetEvaluator.EvaluationContext result = mock(BetEvaluator.EvaluationContext.class);

        when(result.getBookmakersOdd()).thenReturn(bookmakersOdd);
        when(result.getModelProbability()).thenReturn(modelProbability);
        when(result.isHasWon()).thenReturn(hasWon);

        return result;
    }
}