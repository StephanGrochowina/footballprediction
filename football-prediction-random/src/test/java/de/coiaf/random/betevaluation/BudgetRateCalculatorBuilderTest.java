package de.coiaf.random.betevaluation;

import de.coiaf.random.probability.Probability;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class BudgetRateCalculatorBuilderTest {

    private BudgetRateCalculator delegateMock = null;

    @Before
    public void setUp() throws Exception {
        this.delegateMock = mock(BudgetRateCalculator.class);
    }

    @After
    public void tearDown() throws Exception {
        this.delegateMock = null;
    }

    @Test
    public void createKellyCompleteCalculatorBuilder() {
        BudgetRateCalculatorBuilder builder;
        BudgetRateCalculator calculator;

        builder = BudgetRateCalculatorBuilder.createKellyCompleteCalculatorBuilder();

        assertNotNull(builder);
        calculator = builder.build();
        assertTrue(calculator instanceof BudgetRateCalculatorKellyComplete);
    }

    @Test
    public void createKellyCompleteCalculatorBuilder_nullValueThreshold() {
        BudgetRateCalculatorBuilder builder;
        BudgetRateCalculator calculator;

        builder = BudgetRateCalculatorBuilder.createKellyCompleteCalculatorBuilder(null);

        assertNotNull(builder);
        calculator = builder.build();
        assertTrue(calculator instanceof BudgetRateCalculatorKellyComplete);
    }

    @Test
    public void createKellyCompleteCalculatorBuilder_negativeValueThreshold() {
        BudgetRateCalculatorBuilder builder;
        BudgetRateCalculator calculator;

        builder = BudgetRateCalculatorBuilder.createKellyCompleteCalculatorBuilder(new BigDecimal("-1.23"));

        assertNotNull(builder);
        calculator = builder.build();
        assertTrue(calculator instanceof BudgetRateCalculatorKellyComplete);
    }

    @Test
    public void createKellyCompleteCalculatorBuilder_zeroValueThreshold() {
        BudgetRateCalculatorBuilder builder;
        BudgetRateCalculator calculator;

        builder = BudgetRateCalculatorBuilder.createKellyCompleteCalculatorBuilder(BigDecimal.ZERO);

        assertNotNull(builder);
        calculator = builder.build();
        assertTrue(calculator instanceof BudgetRateCalculatorKellyComplete);
    }

    @Test
    public void createKellyCompleteCalculatorBuilder_betweenZeroAndOneValueThreshold() {
        BudgetRateCalculatorBuilder builder;
        BudgetRateCalculator calculator;

        builder = BudgetRateCalculatorBuilder.createKellyCompleteCalculatorBuilder(new BigDecimal("0.23"));

        assertNotNull(builder);
        calculator = builder.build();
        assertTrue(calculator instanceof BudgetRateCalculatorKellyComplete);
    }

    @Test
    public void createKellyCompleteCalculatorBuilder_oneValueThreshold() {
        BudgetRateCalculatorBuilder builder;
        BudgetRateCalculator calculator;

        builder = BudgetRateCalculatorBuilder.createKellyCompleteCalculatorBuilder(BigDecimal.ONE);

        assertNotNull(builder);
        calculator = builder.build();
        assertTrue(calculator instanceof BudgetRateCalculatorKellyComplete);
    }

    @Test
    public void createKellyCompleteCalculatorBuilder_aboveOneValueThreshold() {
        BudgetRateCalculatorBuilder builder;
        BudgetRateCalculator calculator;

        builder = BudgetRateCalculatorBuilder.createKellyCompleteCalculatorBuilder(new BigDecimal("1.23"));

        assertNotNull(builder);
        calculator = builder.build();
        assertTrue(calculator instanceof BudgetRateCalculatorKellyComplete);
    }

    @Test
    public void createSimpleCalculatorBuilder() {
        BudgetRateCalculatorBuilder builder;
        BudgetRateCalculator calculator;

        builder = BudgetRateCalculatorBuilder.createSimpleCalculatorBuilder();

        assertNotNull(builder);
        calculator = builder.build();
        assertTrue(calculator instanceof SimpleBudgetRateCalculator);
    }

    @Test
    public void createSimpleCalculatorBuilder_nullValueThreshold() {
        BudgetRateCalculatorBuilder builder;
        BudgetRateCalculator calculator;

        builder = BudgetRateCalculatorBuilder.createSimpleCalculatorBuilder(null);

        assertNotNull(builder);
        calculator = builder.build();
        assertTrue(calculator instanceof SimpleBudgetRateCalculator);
    }

    @Test
    public void createSimpleCalculatorBuilder_negativeValueThreshold() {
        BudgetRateCalculatorBuilder builder;
        BudgetRateCalculator calculator;

        builder = BudgetRateCalculatorBuilder.createSimpleCalculatorBuilder(new BigDecimal("-1.23"));

        assertNotNull(builder);
        calculator = builder.build();
        assertTrue(calculator instanceof SimpleBudgetRateCalculator);
    }

    @Test
    public void createSimpleCalculatorBuilder_zeroValueThreshold() {
        BudgetRateCalculatorBuilder builder;
        BudgetRateCalculator calculator;

        builder = BudgetRateCalculatorBuilder.createSimpleCalculatorBuilder(BigDecimal.ZERO);

        assertNotNull(builder);
        calculator = builder.build();
        assertTrue(calculator instanceof SimpleBudgetRateCalculator);
    }

    @Test
    public void createSimpleCalculatorBuilder_betweenZeroAndOneValueThreshold() {
        BudgetRateCalculatorBuilder builder;
        BudgetRateCalculator calculator;

        builder = BudgetRateCalculatorBuilder.createSimpleCalculatorBuilder(new BigDecimal("0.23"));

        assertNotNull(builder);
        calculator = builder.build();
        assertTrue(calculator instanceof SimpleBudgetRateCalculator);
    }

    @Test
    public void createSimpleCalculatorBuilder_oneValueThreshold() {
        BudgetRateCalculatorBuilder builder;
        BudgetRateCalculator calculator;

        builder = BudgetRateCalculatorBuilder.createSimpleCalculatorBuilder(BigDecimal.ONE);

        assertNotNull(builder);
        calculator = builder.build();
        assertTrue(calculator instanceof SimpleBudgetRateCalculator);
    }

    @Test
    public void createSimpleCalculatorBuilder_aboveOneValueThreshold() {
        BudgetRateCalculatorBuilder builder;
        BudgetRateCalculator calculator;

        builder = BudgetRateCalculatorBuilder.createSimpleCalculatorBuilder(new BigDecimal("1.23"));

        assertNotNull(builder);
        calculator = builder.build();
        assertTrue(calculator instanceof SimpleBudgetRateCalculator);
    }

    @Test
    public void createProbabilityByValueBetCalculatorBuilder() {
        BudgetRateCalculatorBuilder builder;
        BudgetRateCalculator calculator;

        builder = BudgetRateCalculatorBuilder.createProbabilityByValueBetCalculatorBuilder();

        assertNotNull(builder);
        calculator = builder.build();
        assertTrue(calculator instanceof BudgetRateCalculatorProbabilityOnValueBet);
    }

    @Test
    public void createProbabilityByValueBetCalculatorBuilder_nullValueThreshold() {
        BudgetRateCalculatorBuilder builder;
        BudgetRateCalculator calculator;

        builder = BudgetRateCalculatorBuilder.createProbabilityByValueBetCalculatorBuilder(null);

        assertNotNull(builder);
        calculator = builder.build();
        assertTrue(calculator instanceof BudgetRateCalculatorProbabilityOnValueBet);
    }

    @Test
    public void createProbabilityByValueBetCalculatorBuilder_negativeValueThreshold() {
        BudgetRateCalculatorBuilder builder;
        BudgetRateCalculator calculator;

        builder = BudgetRateCalculatorBuilder.createProbabilityByValueBetCalculatorBuilder(new BigDecimal("-1.23"));

        assertNotNull(builder);
        calculator = builder.build();
        assertTrue(calculator instanceof BudgetRateCalculatorProbabilityOnValueBet);
    }

    @Test
    public void createProbabilityByValueBetCalculatorBuilder_zeroValueThreshold() {
        BudgetRateCalculatorBuilder builder;
        BudgetRateCalculator calculator;

        builder = BudgetRateCalculatorBuilder.createProbabilityByValueBetCalculatorBuilder(BigDecimal.ZERO);

        assertNotNull(builder);
        calculator = builder.build();
        assertTrue(calculator instanceof BudgetRateCalculatorProbabilityOnValueBet);
    }

    @Test
    public void createProbabilityByValueBetCalculatorBuilder_betweenZeroAndOneValueThreshold() {
        BudgetRateCalculatorBuilder builder;
        BudgetRateCalculator calculator;

        builder = BudgetRateCalculatorBuilder.createProbabilityByValueBetCalculatorBuilder(new BigDecimal("0.23"));

        assertNotNull(builder);
        calculator = builder.build();
        assertTrue(calculator instanceof BudgetRateCalculatorProbabilityOnValueBet);
    }

    @Test
    public void createProbabilityByValueBetCalculatorBuilder_oneValueThreshold() {
        BudgetRateCalculatorBuilder builder;
        BudgetRateCalculator calculator;

        builder = BudgetRateCalculatorBuilder.createProbabilityByValueBetCalculatorBuilder(BigDecimal.ONE);

        assertNotNull(builder);
        calculator = builder.build();
        assertTrue(calculator instanceof BudgetRateCalculatorProbabilityOnValueBet);
    }

    @Test
    public void createProbabilityByValueBetCalculatorBuilder_aboveOneValueThreshold() {
        BudgetRateCalculatorBuilder builder;
        BudgetRateCalculator calculator;

        builder = BudgetRateCalculatorBuilder.createProbabilityByValueBetCalculatorBuilder(new BigDecimal("1.23"));

        assertNotNull(builder);
        calculator = builder.build();
        assertTrue(calculator instanceof BudgetRateCalculatorProbabilityOnValueBet);
    }

    @Test
    public void createProbabilityOnlyBetCalculatorBuilder() {
        BudgetRateCalculatorBuilder builder;
        BudgetRateCalculator calculator;

        builder = BudgetRateCalculatorBuilder.createProbabilityOnlyBetCalculatorBuilder();

        assertNotNull(builder);
        calculator = builder.build();
        assertTrue(calculator instanceof BudgetRateCalculatorProbabilityOnly);
    }

    @Test(expected = NullPointerException.class)
    public void createCalculatorBuilder_nullCalculatorProvider() {
        BudgetRateCalculatorBuilder.createCalculatorBuilder(null);
    }

    @Test(expected = NullPointerException.class)
    public void createCalculatorBuilder_calculatorProviderSupplyingNull() {
        BudgetRateCalculatorBuilder.createCalculatorBuilder(() -> null);
    }

    @Test
    public void createCalculatorBuilder_calculatorProviderSupplyingCalculator() {
        BudgetRateCalculatorBuilder builder;
        BudgetRateCalculator calculator;

        builder = BudgetRateCalculatorBuilder.createCalculatorBuilder(() -> this.delegateMock);

        assertNotNull(builder);
        calculator = builder.build();
        assertNotNull(calculator);
        assertSame(this.delegateMock, calculator);
    }

    @Test
    public void applyFractionalBudgetRateCalculation() {
        BudgetRateCalculatorBuilder builder = BudgetRateCalculatorBuilder.createCalculatorBuilder(() -> this.delegateMock);
        BudgetRateCalculatorBuilder result;
        BudgetRateCalculator calculator;

        result = builder.applyFractionalBudgetRateCalculation();

        assertNotNull(result);
        assertSame(builder, result);
        calculator = result.build();
        assertTrue(calculator instanceof FractionalBudgetRateDecorator);
    }

    @Test(expected = NullPointerException.class)
    public void applyFractionalBudgetRateCalculation_nullFraction() {
        BudgetRateCalculatorBuilder builder = BudgetRateCalculatorBuilder.createCalculatorBuilder(() -> this.delegateMock);

        builder.applyFractionalBudgetRateCalculation(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void applyFractionalBudgetRateCalculation_impossibleFraction() {
        BudgetRateCalculatorBuilder builder = BudgetRateCalculatorBuilder.createCalculatorBuilder(() -> this.delegateMock);

        builder.applyFractionalBudgetRateCalculation(Probability.IMPOSSIBLE);
    }

    @Test
    public void applyFractionalBudgetRateCalculation_fractionBetweenZeroAndOne() {
        BudgetRateCalculatorBuilder builder = BudgetRateCalculatorBuilder.createCalculatorBuilder(() -> this.delegateMock);
        BudgetRateCalculatorBuilder result;
        BudgetRateCalculator calculator;

        result = builder.applyFractionalBudgetRateCalculation(Probability.UNCERTAIN);

        assertNotNull(result);
        assertSame(builder, result);
        calculator = result.build();
        assertTrue(calculator instanceof FractionalBudgetRateDecorator);
    }

    @Test
    public void applyFractionalBudgetRateCalculation_certainFraction() {
        BudgetRateCalculatorBuilder builder = BudgetRateCalculatorBuilder.createCalculatorBuilder(() -> this.delegateMock);
        BudgetRateCalculatorBuilder result;
        BudgetRateCalculator calculator;

        result = builder.applyFractionalBudgetRateCalculation(Probability.CERTAIN);

        assertNotNull(result);
        assertSame(builder, result);
        calculator = result.build();
        assertTrue(calculator instanceof FractionalBudgetRateDecorator);
    }

    @Test
    public void applyBudgetRateNormalization() {
        BudgetRateCalculatorBuilder builder = BudgetRateCalculatorBuilder.createCalculatorBuilder(() -> this.delegateMock);
        BudgetRateCalculatorBuilder result;
        BudgetRateCalculator calculator;

        result = builder.applyBudgetRateNormalization();

        assertNotNull(result);
        assertSame(builder, result);
        calculator = result.build();
        assertTrue(calculator instanceof NormalizeBudgetRateDecorator);
    }

    @Test(expected = NullPointerException.class)
    public void applyUpperLimit_nullFraction() {
        BudgetRateCalculatorBuilder builder = BudgetRateCalculatorBuilder.createCalculatorBuilder(() -> this.delegateMock);

        builder.applyUpperLimit(null);
    }

    @Test
    public void applyUpperLimit_impossibleFraction() {
        BudgetRateCalculatorBuilder builder = BudgetRateCalculatorBuilder.createCalculatorBuilder(() -> this.delegateMock);
        BudgetRateCalculatorBuilder result;
        BudgetRateCalculator calculator;

        result = builder.applyUpperLimit(Probability.IMPOSSIBLE);

        assertNotNull(result);
        assertSame(builder, result);
        calculator = result.build();
        assertTrue(calculator instanceof UpperLimitBudgetRateCalculatorDecorator);
    }

    @Test
    public void applyUpperLimit_fractionBetweenZeroAndOne() {
        BudgetRateCalculatorBuilder builder = BudgetRateCalculatorBuilder.createCalculatorBuilder(() -> this.delegateMock);
        BudgetRateCalculatorBuilder result;
        BudgetRateCalculator calculator;

        result = builder.applyUpperLimit(Probability.UNCERTAIN);

        assertNotNull(result);
        assertSame(builder, result);
        calculator = result.build();
        assertTrue(calculator instanceof UpperLimitBudgetRateCalculatorDecorator);
    }

    @Test
    public void applyUpperLimit_certainFraction() {
        BudgetRateCalculatorBuilder builder = BudgetRateCalculatorBuilder.createCalculatorBuilder(() -> this.delegateMock);
        BudgetRateCalculatorBuilder result;
        BudgetRateCalculator calculator;

        result = builder.applyUpperLimit(Probability.CERTAIN);

        assertNotNull(result);
        assertSame(builder, result);
        calculator = result.build();
        assertTrue(calculator instanceof UpperLimitBudgetRateCalculatorDecorator);
    }
}