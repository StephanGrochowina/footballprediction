package de.coiaf.random.betevaluation;

import de.coiaf.random.odds.Odd;
import de.coiaf.random.probability.Probability;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class NullAndImpossibleBudgetRateHandlingDecoratorTest {

    private static final Odd<?> BOOKMAKERS_ODD_MOCK = mock(Odd.class);
    private static final Probability MODEL_PROBABILITY_MOCK = mock(Probability.class);

    private BudgetRateCalculator delegateMock = null;
    private Function<Probability, Probability> transformationMock = null;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        this.delegateMock = mock(BudgetRateCalculator.class);
        this.transformationMock = (Function<Probability, Probability>) mock(Function.class);
        when(this.transformationMock.apply(any(Probability.class))).thenReturn(Probability.UNCERTAIN);
    }

    @After
    public void tearDown() {
        this.delegateMock = null;
        this.transformationMock = null;
    }

    @Test(expected = NullPointerException.class)
    public void constructor_nullDelegate_givenTransformation() {
        new NullAndImpossibleBudgetRateHandlingDecorator(null, this.transformationMock);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_givenDelegate_nullTransformation() {
        new NullAndImpossibleBudgetRateHandlingDecorator(this.delegateMock, null);
    }

    @Test
    public void constructor_givenDelegate_givenTransformation() {
        NullAndImpossibleBudgetRateHandlingDecorator calculator = new NullAndImpossibleBudgetRateHandlingDecorator(this.delegateMock, this.transformationMock);

        assertNotNull(calculator);
    }

    @Test
    public void calculateBudgetRate_delegateReturnsNull() {
        NullAndImpossibleBudgetRateHandlingDecorator calculator = new NullAndImpossibleBudgetRateHandlingDecorator(this.delegateMock, this.transformationMock);
        Probability result;

        when(this.delegateMock.calculateBudgetRate(any(Odd.class), any(Probability.class))).thenReturn(null);

        result = calculator.calculateBudgetRate(BOOKMAKERS_ODD_MOCK, MODEL_PROBABILITY_MOCK);

        assertNotNull(result);
        assertEquals(Probability.IMPOSSIBLE, result);
        verify(this.transformationMock, never()).apply(any(Probability.class));
    }

    @Test
    public void calculateBudgetRate_delegateReturnsImpossible() {
        NullAndImpossibleBudgetRateHandlingDecorator calculator = new NullAndImpossibleBudgetRateHandlingDecorator(this.delegateMock, this.transformationMock);
        Probability result;

        when(this.delegateMock.calculateBudgetRate(any(Odd.class), any(Probability.class))).thenReturn(Probability.IMPOSSIBLE);

        result = calculator.calculateBudgetRate(BOOKMAKERS_ODD_MOCK, MODEL_PROBABILITY_MOCK);

        assertNotNull(result);
        assertEquals(Probability.IMPOSSIBLE, result);
        verify(this.transformationMock, never()).apply(any(Probability.class));
    }

    @Test
    public void calculateBudgetRate_delegateReturnsSomeProbability() {
        NullAndImpossibleBudgetRateHandlingDecorator calculator = new NullAndImpossibleBudgetRateHandlingDecorator(this.delegateMock, this.transformationMock);
        Probability result;
        Probability someProbability = Probability.valueOf(0.25);

        when(this.delegateMock.calculateBudgetRate(any(Odd.class), any(Probability.class))).thenReturn(someProbability);

        result = calculator.calculateBudgetRate(BOOKMAKERS_ODD_MOCK, MODEL_PROBABILITY_MOCK);

        assertNotNull(result);
        assertEquals(Probability.UNCERTAIN, result);
        verify(this.transformationMock, times(1)).apply(any(Probability.class));
    }

    @Test
    public void calculateBudgetRate_delegateReturnsCertain() {
        NullAndImpossibleBudgetRateHandlingDecorator calculator = new NullAndImpossibleBudgetRateHandlingDecorator(this.delegateMock, this.transformationMock);
        Probability result;

        when(this.delegateMock.calculateBudgetRate(any(Odd.class), any(Probability.class))).thenReturn(Probability.CERTAIN);

        result = calculator.calculateBudgetRate(BOOKMAKERS_ODD_MOCK, MODEL_PROBABILITY_MOCK);

        assertNotNull(result);
        assertEquals(Probability.UNCERTAIN, result);
        verify(this.transformationMock, times(1)).apply(any(Probability.class));
    }
}