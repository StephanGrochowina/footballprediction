package de.coiaf.random.betevaluation;

import de.coiaf.random.probability.Probability;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Supplier;

public class BudgetRateCalculatorFactory {

    /**
     * Creates a BudgetRateCalculator instance which determines the budget rate by the
     * complete Kelly criterion.
     * This method is equivalent to calling {@link BudgetRateCalculatorFactory#createBudgetRateCalculatorKellyComplete(BigDecimal)}}
     * with a threshold of BigDecimal.ZERO.
     * @return a BudgetRateCalculator instance
     */
    public BudgetRateCalculator createBudgetRateCalculatorKellyComplete() {
        return this.createBudgetRateCalculator(BudgetRateCalculatorKellyComplete::new);
    }

    /**
     * Creates a BudgetRateCalculator instance which determines the budget rate by the
     * complete Kelly criterion.
     * @param valueThreshold a threshold for determining the bet value
     * @return a BudgetRateCalculator instance
     */
    public BudgetRateCalculator createBudgetRateCalculatorKellyComplete(BigDecimal valueThreshold) {
        return this.createBudgetRateCalculator(() -> new BudgetRateCalculatorKellyComplete(valueThreshold));
    }

    /**
     * Creates a BudgetRateCalculator instance which determines the budget rate by the
     * fractional Kelly criterion.
     * This method is equivalent to calling {@link BudgetRateCalculatorFactory#createBudgetRateCalculatorFractionalKelly(Probability, BigDecimal)}}
     * with a fraction of 0.1 and a threshold of BigDecimal.ZERO.
     * @return a BudgetRateCalculator instance
     */
    public BudgetRateCalculator createBudgetRateCalculatorFractionalKelly() {
        return this.createBudgetRateCalculator(BudgetRateCalculatorFractionalKelly::new);
    }

    /**
     * Creates a BudgetRateCalculator instance which determines the budget rate by the
     * fractional Kelly criterion.
     * This method is equivalent to calling {@link BudgetRateCalculatorFactory#createBudgetRateCalculatorFractionalKelly(Probability, BigDecimal)}}
     * with a fraction of 0.1.
     * @param valueThreshold a threshold for determining the bet value
     * @return a BudgetRateCalculator instance
     */
    public BudgetRateCalculator createBudgetRateCalculatorFractionalKelly(BigDecimal valueThreshold) {
        return this.createBudgetRateCalculator(() -> new BudgetRateCalculatorFractionalKelly(valueThreshold));
    }

    /**
     * Creates a BudgetRateCalculator instance which determines the budget rate by the
     * fractional Kelly criterion.
     * This method is equivalent to calling {@link BudgetRateCalculatorFactory#createBudgetRateCalculatorFractionalKelly(Probability, BigDecimal)}}
     * with a threshold of BigDecimal.ZERO.
     * @param fraction the fraction of a complete Kelly result
     * @return a BudgetRateCalculator instance
     */
    public BudgetRateCalculator createBudgetRateCalculatorFractionalKelly(Probability fraction) {
        return this.createBudgetRateCalculator(() -> new BudgetRateCalculatorFractionalKelly(fraction));
    }

    /**
     * Creates a BudgetRateCalculator instance which determines the budget rate by the
     * fractional Kelly criterion.
     * @param fraction the fraction of a complete Kelly result
     * @param valueThreshold a threshold for determining the bet value
     * @return a BudgetRateCalculator instance
     */
    public BudgetRateCalculator createBudgetRateCalculatorFractionalKelly(Probability fraction, BigDecimal valueThreshold) {
        return this.createBudgetRateCalculator(() -> new BudgetRateCalculatorFractionalKelly(fraction, valueThreshold));
    }

    /**
     * Creates a BudgetRateCalculator instance which returns {@link Probability#CERTAIN} if the bet has value or
     * otherwise {@link Probability#IMPOSSIBLE}.
     * This method is equivalent to calling {@link BudgetRateCalculatorFactory#createSimpleBudgetRateCalculator(BigDecimal)}}
     * with a threshold of BigDecimal.ZERO.
     * @return a BudgetRateCalculator instance
     */
    public BudgetRateCalculator createSimpleBudgetRateCalculator() {
        return this.createBudgetRateCalculator(SimpleBudgetRateCalculator::new);
    }

    /**
     * Creates a BudgetRateCalculator instance which returns {@link Probability#CERTAIN} if the bet has value or
     * otherwise {@link Probability#IMPOSSIBLE}.
     * @param valueThreshold a threshold for determining the bet value
     * @return a BudgetRateCalculator instance
     */
    public BudgetRateCalculator createSimpleBudgetRateCalculator(BigDecimal valueThreshold) {
        return this.createBudgetRateCalculator(() -> new SimpleBudgetRateCalculator(valueThreshold));
    }

    /**
     * Creates a BudgetRateCalculator instance.
     * @param supplier the supplier providing a BudgetRateCalculator instance
     * @return a BudgetRateCalculator instance
     * @throws NullPointerException if {@code supplier} is null.
     * @throws IllegalArgumentException if {@code supplier.get()} returns null.
     */
    public BudgetRateCalculator createBudgetRateCalculator(Supplier<BudgetRateCalculator> supplier) {
        Objects.requireNonNull(supplier);

        BudgetRateCalculator calculator =  supplier.get();

        if (calculator == null) {
            throw new IllegalArgumentException("Supplier must not return null in get method.");
        }

        return calculator;
    }
}
