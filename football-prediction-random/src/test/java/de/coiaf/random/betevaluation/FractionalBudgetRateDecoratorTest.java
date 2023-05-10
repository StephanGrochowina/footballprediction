package de.coiaf.random.betevaluation;

import de.coiaf.random.odds.Odd;
import de.coiaf.random.probability.Probability;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FractionalBudgetRateDecoratorTest {

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
        new FractionalBudgetRateDecorator(null);
    }

    @Test
    public void constructor_givenDelegate() {
        FractionalBudgetRateDecorator calculator = new FractionalBudgetRateDecorator(this.delegateMock);

        assertNotNull(calculator);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_nullDelegate_givenFraction() {
        new FractionalBudgetRateDecorator(null, Probability.UNCERTAIN);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_givenDelegate_nullFraction() {
        new FractionalBudgetRateDecorator(this.delegateMock, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_givenDelegate_impossibleFraction() {
        new FractionalBudgetRateDecorator(this.delegateMock, Probability.IMPOSSIBLE);
    }

    @Test
    public void constructor_givenDelegate_someFraction() {
        FractionalBudgetRateDecorator calculator = new FractionalBudgetRateDecorator(this.delegateMock, Probability.UNCERTAIN);

        assertNotNull(calculator);
    }

    @Test
    public void constructor_givenDelegate_certainFraction() {
        FractionalBudgetRateDecorator calculator = new FractionalBudgetRateDecorator(this.delegateMock, Probability.CERTAIN);

        assertNotNull(calculator);
    }

    @Test
    public void calculateBudgetRate_delegateReturnsNull() {
        FractionalBudgetRateDecorator calculator = new FractionalBudgetRateDecorator(this.delegateMock, Probability.UNCERTAIN);
        Probability result;

        when(this.delegateMock.calculateBudgetRate(any(Odd.class), any(Probability.class))).thenReturn(null);

        result = calculator.calculateBudgetRate(BOOKMAKERS_ODD_MOCK, MODEL_PROBABILITY_MOCK);

        assertNotNull(result);
        assertEquals(Probability.IMPOSSIBLE, result);
    }

    @Test
    public void calculateBudgetRate_delegateReturnsImpossible() {
        FractionalBudgetRateDecorator calculator = new FractionalBudgetRateDecorator(this.delegateMock, Probability.UNCERTAIN);
        Probability result;

        when(this.delegateMock.calculateBudgetRate(any(Odd.class), any(Probability.class))).thenReturn(Probability.IMPOSSIBLE);

        result = calculator.calculateBudgetRate(BOOKMAKERS_ODD_MOCK, MODEL_PROBABILITY_MOCK);

        assertNotNull(result);
        assertEquals(Probability.IMPOSSIBLE, result);
    }

    @Test
    public void calculateBudgetRate_delegateReturnsSomeProbability() {
        FractionalBudgetRateDecorator calculator = new FractionalBudgetRateDecorator(this.delegateMock, Probability.UNCERTAIN);
        Probability result;
        Probability someProbability = Probability.valueOf(0.25);
        Probability expected = someProbability.multiply(Probability.UNCERTAIN);

        when(this.delegateMock.calculateBudgetRate(any(Odd.class), any(Probability.class))).thenReturn(someProbability);

        result = calculator.calculateBudgetRate(BOOKMAKERS_ODD_MOCK, MODEL_PROBABILITY_MOCK);

        assertNotNull(result);
        assertEquals(expected, result);
    }
}