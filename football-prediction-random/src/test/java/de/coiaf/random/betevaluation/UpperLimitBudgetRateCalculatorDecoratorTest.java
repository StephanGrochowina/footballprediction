package de.coiaf.random.betevaluation;

import de.coiaf.random.odds.Odd;
import de.coiaf.random.probability.Probability;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UpperLimitBudgetRateCalculatorDecoratorTest {

    private static final Odd<?> BOOKMAKERS_ODD_MOCK = mock(Odd.class);
    private static final Probability MODEL_PROBABILITY_MOCK = mock(Probability.class);
    private static final Probability PROBABILITY_LIKELY = Probability.valueOf(0.75);
    private static final Probability PROBABILITY_UNLIKELY = Probability.valueOf(0.25);

    private BudgetRateCalculator delegateMock = null;

    @Before
    public void setUp() throws Exception {
        this.delegateMock = mock(BudgetRateCalculator.class);
    }

    @After
    public void tearDown() throws Exception {
        this.delegateMock = null;
    }

    @Test(expected = NullPointerException.class)
    public void constructor_nullDelegate_givenUpperLimit() {
        new UpperLimitBudgetRateCalculatorDecorator(null, Probability.UNCERTAIN);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_givenDelegate_nullUpperLimit() {
        new UpperLimitBudgetRateCalculatorDecorator(this.delegateMock, null);
    }

    @Test
    public void constructor_givenDelegate_impossibleUpperLimit() {
        UpperLimitBudgetRateCalculatorDecorator calculator = new UpperLimitBudgetRateCalculatorDecorator(this.delegateMock, Probability.IMPOSSIBLE);

        assertNotNull(calculator);
    }

    @Test
    public void constructor_givenDelegate_someUpperLimit() {
        UpperLimitBudgetRateCalculatorDecorator calculator = new UpperLimitBudgetRateCalculatorDecorator(this.delegateMock, Probability.UNCERTAIN);

        assertNotNull(calculator);
    }

    @Test
    public void constructor_givenDelegate_certainUpperLimit() {
        UpperLimitBudgetRateCalculatorDecorator calculator = new UpperLimitBudgetRateCalculatorDecorator(this.delegateMock, Probability.CERTAIN);

        assertNotNull(calculator);
    }

    @Test
    public void calculateBudgetRate_impossibleUpperLimit_delegateReturnsNull() {
        UpperLimitBudgetRateCalculatorDecorator calculator = new UpperLimitBudgetRateCalculatorDecorator(this.delegateMock, Probability.IMPOSSIBLE);
        Probability result;

        when(this.delegateMock.calculateBudgetRate(any(Odd.class), any(Probability.class))).thenReturn(null);

        result = calculator.calculateBudgetRate(BOOKMAKERS_ODD_MOCK, MODEL_PROBABILITY_MOCK);

        assertNotNull(result);
        assertEquals(Probability.IMPOSSIBLE, result);
    }

    @Test
    public void calculateBudgetRate_impossibleUpperLimit_delegateReturnsImpossible() {
        UpperLimitBudgetRateCalculatorDecorator calculator = new UpperLimitBudgetRateCalculatorDecorator(this.delegateMock, Probability.IMPOSSIBLE);
        Probability result;

        when(this.delegateMock.calculateBudgetRate(any(Odd.class), any(Probability.class))).thenReturn(Probability.IMPOSSIBLE);

        result = calculator.calculateBudgetRate(BOOKMAKERS_ODD_MOCK, MODEL_PROBABILITY_MOCK);

        assertNotNull(result);
        assertEquals(Probability.IMPOSSIBLE, result);
    }

    @Test
    public void calculateBudgetRate_impossibleUpperLimit_delegateReturnsSomeProbability() {
        UpperLimitBudgetRateCalculatorDecorator calculator = new UpperLimitBudgetRateCalculatorDecorator(this.delegateMock, Probability.IMPOSSIBLE);
        Probability result;

        when(this.delegateMock.calculateBudgetRate(any(Odd.class), any(Probability.class))).thenReturn(Probability.UNCERTAIN);

        result = calculator.calculateBudgetRate(BOOKMAKERS_ODD_MOCK, MODEL_PROBABILITY_MOCK);

        assertNotNull(result);
        assertEquals(Probability.IMPOSSIBLE, result);
    }

    @Test
    public void calculateBudgetRate_impossibleUpperLimit_delegateReturnsCertain() {
        UpperLimitBudgetRateCalculatorDecorator calculator = new UpperLimitBudgetRateCalculatorDecorator(this.delegateMock, Probability.IMPOSSIBLE);
        Probability result;

        when(this.delegateMock.calculateBudgetRate(any(Odd.class), any(Probability.class))).thenReturn(Probability.CERTAIN);

        result = calculator.calculateBudgetRate(BOOKMAKERS_ODD_MOCK, MODEL_PROBABILITY_MOCK);

        assertNotNull(result);
        assertEquals(Probability.IMPOSSIBLE, result);
    }

    @Test
    public void calculateBudgetRate_unlikelyUpperLimit_delegateReturnsNull() {
        UpperLimitBudgetRateCalculatorDecorator calculator = new UpperLimitBudgetRateCalculatorDecorator(this.delegateMock, PROBABILITY_UNLIKELY);
        Probability result;

        when(this.delegateMock.calculateBudgetRate(any(Odd.class), any(Probability.class))).thenReturn(null);

        result = calculator.calculateBudgetRate(BOOKMAKERS_ODD_MOCK, MODEL_PROBABILITY_MOCK);

        assertNotNull(result);
        assertEquals(Probability.IMPOSSIBLE, result);
    }

    @Test
    public void calculateBudgetRate_unlikelyUpperLimit_delegateReturnsImpossible() {
        UpperLimitBudgetRateCalculatorDecorator calculator = new UpperLimitBudgetRateCalculatorDecorator(this.delegateMock, PROBABILITY_UNLIKELY);
        Probability result;

        when(this.delegateMock.calculateBudgetRate(any(Odd.class), any(Probability.class))).thenReturn(Probability.IMPOSSIBLE);

        result = calculator.calculateBudgetRate(BOOKMAKERS_ODD_MOCK, MODEL_PROBABILITY_MOCK);

        assertNotNull(result);
        assertEquals(Probability.IMPOSSIBLE, result);
    }

    @Test
    public void calculateBudgetRate_unlikelyUpperLimit_delegateReturnsSomeProbability() {
        UpperLimitBudgetRateCalculatorDecorator calculator = new UpperLimitBudgetRateCalculatorDecorator(this.delegateMock, PROBABILITY_UNLIKELY);
        Probability result;

        when(this.delegateMock.calculateBudgetRate(any(Odd.class), any(Probability.class))).thenReturn(Probability.UNCERTAIN);

        result = calculator.calculateBudgetRate(BOOKMAKERS_ODD_MOCK, MODEL_PROBABILITY_MOCK);

        assertNotNull(result);
        assertEquals(PROBABILITY_UNLIKELY, result);
    }

    @Test
    public void calculateBudgetRate_unlikelyUpperLimit_delegateReturnsCertain() {
        UpperLimitBudgetRateCalculatorDecorator calculator = new UpperLimitBudgetRateCalculatorDecorator(this.delegateMock, PROBABILITY_UNLIKELY);
        Probability result;

        when(this.delegateMock.calculateBudgetRate(any(Odd.class), any(Probability.class))).thenReturn(Probability.CERTAIN);

        result = calculator.calculateBudgetRate(BOOKMAKERS_ODD_MOCK, MODEL_PROBABILITY_MOCK);

        assertNotNull(result);
        assertEquals(PROBABILITY_UNLIKELY, result);
    }

    @Test
    public void calculateBudgetRate_likelyUpperLimit_delegateReturnsNull() {
        UpperLimitBudgetRateCalculatorDecorator calculator = new UpperLimitBudgetRateCalculatorDecorator(this.delegateMock, PROBABILITY_LIKELY);
        Probability result;

        when(this.delegateMock.calculateBudgetRate(any(Odd.class), any(Probability.class))).thenReturn(null);

        result = calculator.calculateBudgetRate(BOOKMAKERS_ODD_MOCK, MODEL_PROBABILITY_MOCK);

        assertNotNull(result);
        assertEquals(Probability.IMPOSSIBLE, result);
    }

    @Test
    public void calculateBudgetRate_likelyUpperLimit_delegateReturnsImpossible() {
        UpperLimitBudgetRateCalculatorDecorator calculator = new UpperLimitBudgetRateCalculatorDecorator(this.delegateMock, PROBABILITY_LIKELY);
        Probability result;

        when(this.delegateMock.calculateBudgetRate(any(Odd.class), any(Probability.class))).thenReturn(Probability.IMPOSSIBLE);

        result = calculator.calculateBudgetRate(BOOKMAKERS_ODD_MOCK, MODEL_PROBABILITY_MOCK);

        assertNotNull(result);
        assertEquals(Probability.IMPOSSIBLE, result);
    }

    @Test
    public void calculateBudgetRate_likelyUpperLimit_delegateReturnsSomeProbability() {
        UpperLimitBudgetRateCalculatorDecorator calculator = new UpperLimitBudgetRateCalculatorDecorator(this.delegateMock, PROBABILITY_LIKELY);
        Probability result;

        when(this.delegateMock.calculateBudgetRate(any(Odd.class), any(Probability.class))).thenReturn(Probability.UNCERTAIN);

        result = calculator.calculateBudgetRate(BOOKMAKERS_ODD_MOCK, MODEL_PROBABILITY_MOCK);

        assertNotNull(result);
        assertEquals(Probability.UNCERTAIN, result);
    }

    @Test
    public void calculateBudgetRate_likelyUpperLimit_delegateReturnsCertain() {
        UpperLimitBudgetRateCalculatorDecorator calculator = new UpperLimitBudgetRateCalculatorDecorator(this.delegateMock, PROBABILITY_LIKELY);
        Probability result;

        when(this.delegateMock.calculateBudgetRate(any(Odd.class), any(Probability.class))).thenReturn(Probability.CERTAIN);

        result = calculator.calculateBudgetRate(BOOKMAKERS_ODD_MOCK, MODEL_PROBABILITY_MOCK);

        assertNotNull(result);
        assertEquals(PROBABILITY_LIKELY, result);
    }

    @Test
    public void calculateBudgetRate_certainUpperLimit_delegateReturnsNull() {
        UpperLimitBudgetRateCalculatorDecorator calculator = new UpperLimitBudgetRateCalculatorDecorator(this.delegateMock, Probability.CERTAIN);
        Probability result;

        when(this.delegateMock.calculateBudgetRate(any(Odd.class), any(Probability.class))).thenReturn(null);

        result = calculator.calculateBudgetRate(BOOKMAKERS_ODD_MOCK, MODEL_PROBABILITY_MOCK);

        assertNotNull(result);
        assertEquals(Probability.IMPOSSIBLE, result);
    }

    @Test
    public void calculateBudgetRate_certainUpperLimit_delegateReturnsImpossible() {
        UpperLimitBudgetRateCalculatorDecorator calculator = new UpperLimitBudgetRateCalculatorDecorator(this.delegateMock, Probability.CERTAIN);
        Probability result;

        when(this.delegateMock.calculateBudgetRate(any(Odd.class), any(Probability.class))).thenReturn(Probability.IMPOSSIBLE);

        result = calculator.calculateBudgetRate(BOOKMAKERS_ODD_MOCK, MODEL_PROBABILITY_MOCK);

        assertNotNull(result);
        assertEquals(Probability.IMPOSSIBLE, result);
    }

    @Test
    public void calculateBudgetRate_certainUpperLimit_delegateReturnsSomeProbability() {
        UpperLimitBudgetRateCalculatorDecorator calculator = new UpperLimitBudgetRateCalculatorDecorator(this.delegateMock, Probability.CERTAIN);
        Probability result;

        when(this.delegateMock.calculateBudgetRate(any(Odd.class), any(Probability.class))).thenReturn(Probability.UNCERTAIN);

        result = calculator.calculateBudgetRate(BOOKMAKERS_ODD_MOCK, MODEL_PROBABILITY_MOCK);

        assertNotNull(result);
        assertEquals(Probability.UNCERTAIN, result);
    }

    @Test
    public void calculateBudgetRate_certainUpperLimit_delegateReturnsCertain() {
        UpperLimitBudgetRateCalculatorDecorator calculator = new UpperLimitBudgetRateCalculatorDecorator(this.delegateMock, Probability.CERTAIN);
        Probability result;

        when(this.delegateMock.calculateBudgetRate(any(Odd.class), any(Probability.class))).thenReturn(Probability.CERTAIN);

        result = calculator.calculateBudgetRate(BOOKMAKERS_ODD_MOCK, MODEL_PROBABILITY_MOCK);

        assertNotNull(result);
        assertEquals(Probability.CERTAIN, result);
    }
}