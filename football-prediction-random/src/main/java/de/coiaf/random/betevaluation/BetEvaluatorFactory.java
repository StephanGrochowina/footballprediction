package de.coiaf.random.betevaluation;

import java.util.function.Supplier;

public class BetEvaluatorFactory {

    public BetEvaluator createBetEvaluatorForKellyComplete() {
        return this.createBetEvaluator(BudgetRateCalculatorBuilder.createKellyCompleteCalculatorBuilder().build());
    }

    public BetEvaluator createBetEvaluatorForSimpleBudgetRateCalculator() {
        return this.createBetEvaluator(BudgetRateCalculatorBuilder.createSimpleCalculatorBuilder().build());
    }

    public BetEvaluator createBetEvaluator(Supplier<BudgetRateCalculator> supplier) {
        return this.createBetEvaluator(BudgetRateCalculatorBuilder.createCalculatorBuilder(supplier).build());
    }

    public BetEvaluator createBetEvaluator(BudgetRateCalculator calculator) {
        return new BetEvaluator(calculator);
    }
}
