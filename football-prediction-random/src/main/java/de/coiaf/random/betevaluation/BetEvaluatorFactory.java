package de.coiaf.random.betevaluation;

import java.util.function.Supplier;

public class BetEvaluatorFactory {

    private final BudgetRateCalculatorFactory budgetRateCalculatorFactory = new BudgetRateCalculatorFactory();

    public BetEvaluator createBetEvaluatorForKellyComplete() {
        return this.createBetEvaluator(this.budgetRateCalculatorFactory.createBudgetRateCalculatorKellyComplete());
    }

    public BetEvaluator createBetEvaluatorForSimpleBudgetRateCalculator() {
        return this.createBetEvaluator(this.budgetRateCalculatorFactory.createSimpleBudgetRateCalculator());
    }

    public BetEvaluator createBetEvaluator(Supplier<BudgetRateCalculator> supplier) {
        return this.createBetEvaluator(this.budgetRateCalculatorFactory.createBudgetRateCalculator(supplier));
    }

    public BetEvaluator createBetEvaluator(BudgetRateCalculator calculator) {
        return new BetEvaluator(calculator);
    }
}
