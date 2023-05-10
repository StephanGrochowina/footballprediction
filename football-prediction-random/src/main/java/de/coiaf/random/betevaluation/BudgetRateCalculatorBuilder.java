package de.coiaf.random.betevaluation;

import de.coiaf.random.probability.Probability;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Builder for budget rate calculators
 */
public class BudgetRateCalculatorBuilder {

    private BudgetRateCalculator calculator;

    /**
     * Creates a builder which creates a budget rate calculator using the Kelly complete algorithm.
     * @return a builder instance
     */
    public static BudgetRateCalculatorBuilder createKellyCompleteCalculatorBuilder() {
        return createCalculatorBuilder(BudgetRateCalculatorKellyComplete::new);
    }

    /**
     * Creates a builder which creates a budget rate calculator using the Kelly complete algorithm.
     * @param valueThreshold a threshold for determining the bet value. If {@param valueThreshold} is null
     *                       or negative, {@code BigDecimal.ZERO} will be used instead.
     * @return a builder instance
     */
    public static BudgetRateCalculatorBuilder createKellyCompleteCalculatorBuilder(BigDecimal valueThreshold) {
        return createCalculatorBuilder(() -> new BudgetRateCalculatorKellyComplete(valueThreshold));
    }

    /**
     * Creates a builder which creates a budget rate calculator which either returns {@code Probability.CERTAIN}
     * if a value bet is detected or {@code Probability.CERTAIN} if not.
     * @return a builder instance
     */
    public static BudgetRateCalculatorBuilder createSimpleCalculatorBuilder() {
        return createCalculatorBuilder(SimpleBudgetRateCalculator::new);
    }

    /**
     * Creates a builder which creates a budget rate calculator which either returns {@code Probability.CERTAIN}
     * if a value bet is detected or {@code Probability.CERTAIN} if not.
     * @param valueThreshold a threshold for determining the bet value. If {@param valueThreshold} is null
     *                       or negative, {@code BigDecimal.ZERO} will be used instead.
     * @return a builder instance
     */
    public static BudgetRateCalculatorBuilder createSimpleCalculatorBuilder(BigDecimal valueThreshold) {
        return createCalculatorBuilder(() -> new SimpleBudgetRateCalculator(valueThreshold));
    }

    /**
     * Creates a builder which creates a budget rate calculator which either returns the model probability
     * if a value bet is detected or {@code Probability.CERTAIN} if not.
     * @return a builder instance
     */
    public static BudgetRateCalculatorBuilder createProbabilityByValueBetCalculatorBuilder() {
        return createCalculatorBuilder(BudgetRateCalculatorProbabilityOnValueBet::new);
    }

    /**
     * Creates a builder which creates a budget rate calculator which either returns the model probability
     * if a value bet is detected or {@code Probability.CERTAIN} if not.
     * @param valueThreshold a threshold for determining the bet value. If {@param valueThreshold} is null
     *                       or negative, {@code BigDecimal.ZERO} will be used instead.
     * @return a builder instance
     */
    public static BudgetRateCalculatorBuilder createProbabilityByValueBetCalculatorBuilder(BigDecimal valueThreshold) {
        return createCalculatorBuilder(() -> new BudgetRateCalculatorProbabilityOnValueBet(valueThreshold));
    }

    /**
     * Creates a builder which creates a budget rate calculator which either returns the model probability
     * if it is more likely than {@code Probability.IMPOSSIBLE}.
     * @return a builder instance
     */
    public static BudgetRateCalculatorBuilder createProbabilityOnlyBetCalculatorBuilder() {
        return createCalculatorBuilder(BudgetRateCalculatorProbabilityOnly::new);
    }

    static BudgetRateCalculatorBuilder createCalculatorBuilder(Supplier<BudgetRateCalculator> calculatorProvider) {
        Objects.requireNonNull(calculatorProvider);

        return new BudgetRateCalculatorBuilder(calculatorProvider.get());
    }

    private BudgetRateCalculatorBuilder(BudgetRateCalculator calculator) {
        Objects.requireNonNull(calculator);

        this.calculator = calculator;
    }

    /**
     * Adds fractional budget rate calculation with default fraction to calculator.
     * @return this instance
     */
    public BudgetRateCalculatorBuilder applyFractionalBudgetRateCalculation() {
        this.calculator = new FractionalBudgetRateDecorator(this.calculator);

        return this;
    }

    /**
     * Adds fractional budget rate calculation with fraction {@param fraction} to calculator.
     * @param fraction the fraction to be applied
     * @return this instance
     * @throws NullPointerException if {@param fraction} is null.
     * @throws IllegalArgumentException if {@param fraction} equals {@code Probability.Impossible}
     */
    public BudgetRateCalculatorBuilder applyFractionalBudgetRateCalculation(Probability fraction) {
        this.calculator = new FractionalBudgetRateDecorator(this.calculator, fraction);

        return this;
    }

    /**
     * Adds budget rate normalization to the calculator. The budget rate will be rounded to a value
     * which is equivalent to a corresponding value of a scale of 2.
     * @return this instance
     */
    public BudgetRateCalculatorBuilder applyBudgetRateNormalization() {
        this.calculator = new NormalizeBudgetRateDecorator(this.calculator);

        return this;
    }

    /**
     * Adds an upper limit of the budget rate to the calculator.
     * @param upperLimit the maximum possible budget rate
     * @return this instance
     */
    public BudgetRateCalculatorBuilder applyUpperLimit(Probability upperLimit) {
        this.calculator = new UpperLimitBudgetRateCalculatorDecorator(this.calculator, upperLimit);

        return this;
    }

    /**
     * Returns the currently created budget rate calculator.
     * @return the calculator
     */
    public BudgetRateCalculator build() {
        return this.calculator;
    }
}
