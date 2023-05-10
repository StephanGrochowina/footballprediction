package de.coiaf.random.betevaluation;

import de.coiaf.random.odds.Odd;
import de.coiaf.random.probability.Probability;

import java.util.Objects;

class UpperLimitBudgetRateCalculatorDecorator implements BudgetRateCalculator {

    private final Probability upperLimit;
    private final BudgetRateCalculator delegate;

    UpperLimitBudgetRateCalculatorDecorator(BudgetRateCalculator delegate, Probability upperLimit) {
        Objects.requireNonNull(delegate);
        Objects.requireNonNull(upperLimit);

        this.upperLimit = upperLimit;
        this.delegate = new NullAndImpossibleBudgetRateHandlingDecorator(delegate, probability -> this.upperLimit.compareTo(probability) < 0 ? this.upperLimit : probability);
    }

    @Override
    public Probability calculateBudgetRate(Odd<?> bookmakersOdd, Probability modelProbability) {
        return this.delegate.calculateBudgetRate(bookmakersOdd, modelProbability);
    }
}
