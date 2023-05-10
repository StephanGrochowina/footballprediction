package de.coiaf.random.betevaluation;

import de.coiaf.random.odds.Odd;
import de.coiaf.random.probability.Probability;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NormalizeBudgetRateDecoratorTest {

    private static final Odd<?> BOOKMAKERS_ODD_MOCK = mock(Odd.class);
    private static final Probability MODEL_PROBABILITY_MOCK = mock(Probability.class);

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
    public void constructor_nullDelegate() {
        new NormalizeBudgetRateDecorator(null);
    }

    @Test
    public void constructor_givenDelegate() {
        NormalizeBudgetRateDecorator calculator = new NormalizeBudgetRateDecorator(this.delegateMock);

        assertNotNull(calculator);
    }

    @Test
    public void calculateBudgetRate_delegateReturnsNull() {
        NormalizeBudgetRateDecorator calculator = new NormalizeBudgetRateDecorator(this.delegateMock);
        Probability result;

        when(this.delegateMock.calculateBudgetRate(any(Odd.class), any(Probability.class))).thenReturn(null);

        result = calculator.calculateBudgetRate(BOOKMAKERS_ODD_MOCK, MODEL_PROBABILITY_MOCK);

        assertNotNull(result);
        assertEquals(Probability.IMPOSSIBLE, result);
    }

    @Test
    public void calculateBudgetRate_delegateReturnsImpossible() {
        NormalizeBudgetRateDecorator calculator = new NormalizeBudgetRateDecorator(this.delegateMock);
        Probability result;

        when(this.delegateMock.calculateBudgetRate(any(Odd.class), any(Probability.class))).thenReturn(Probability.IMPOSSIBLE);

        result = calculator.calculateBudgetRate(BOOKMAKERS_ODD_MOCK, MODEL_PROBABILITY_MOCK);

        assertNotNull(result);
        assertEquals(Probability.IMPOSSIBLE, result);
    }

    @Test
    public void calculateBudgetRate_delegateReturnsCertain() {
        NormalizeBudgetRateDecorator calculator = new NormalizeBudgetRateDecorator(this.delegateMock);
        Probability result;

        when(this.delegateMock.calculateBudgetRate(any(Odd.class), any(Probability.class))).thenReturn(Probability.CERTAIN);

        result = calculator.calculateBudgetRate(BOOKMAKERS_ODD_MOCK, MODEL_PROBABILITY_MOCK);

        assertNotNull(result);
        assertEquals(Probability.CERTAIN, result);
    }

    @Test
    public void calculateBudgetRate_delegateResultUnchanged() {
        NormalizeBudgetRateDecorator calculator = new NormalizeBudgetRateDecorator(this.delegateMock);
        Probability delegateResult = Probability.valueOf(new BigDecimal("0.67"));
        Probability expected = Probability.valueOf(new BigDecimal("0.67"));
        Probability result;

        when(this.delegateMock.calculateBudgetRate(any(Odd.class), any(Probability.class))).thenReturn(delegateResult);

        result = calculator.calculateBudgetRate(BOOKMAKERS_ODD_MOCK, MODEL_PROBABILITY_MOCK);

        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void calculateBudgetRate_delegateResultRoundedUp() {
        NormalizeBudgetRateDecorator calculator = new NormalizeBudgetRateDecorator(this.delegateMock);
        Probability delegateResult = Probability.valueOf(new BigDecimal("0.665"));
        Probability expected = Probability.valueOf(new BigDecimal("0.67"));
        Probability result;

        when(this.delegateMock.calculateBudgetRate(any(Odd.class), any(Probability.class))).thenReturn(delegateResult);

        result = calculator.calculateBudgetRate(BOOKMAKERS_ODD_MOCK, MODEL_PROBABILITY_MOCK);

        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void calculateBudgetRate_delegateResultRoundedDown() {
        NormalizeBudgetRateDecorator calculator = new NormalizeBudgetRateDecorator(this.delegateMock);
        Probability delegateResult = Probability.valueOf(new BigDecimal("0.674"));
        Probability expected = Probability.valueOf(new BigDecimal("0.67"));
        Probability result;

        when(this.delegateMock.calculateBudgetRate(any(Odd.class), any(Probability.class))).thenReturn(delegateResult);

        result = calculator.calculateBudgetRate(BOOKMAKERS_ODD_MOCK, MODEL_PROBABILITY_MOCK);

        assertNotNull(result);
        assertEquals(expected, result);
    }
}