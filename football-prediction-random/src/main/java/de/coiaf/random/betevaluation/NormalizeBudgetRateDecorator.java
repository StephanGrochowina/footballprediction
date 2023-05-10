package de.coiaf.random.betevaluation;

import de.coiaf.random.odds.Odd;
import de.coiaf.random.probability.Probability;

import java.math.RoundingMode;
import java.util.Objects;
import java.util.function.Function;

/**
 * Decorator which rounds the calculated budget rate to a value which is equivalent to a corresponding value of a scale of 2.
 */
class NormalizeBudgetRateDecorator implements BudgetRateCalculator {

    private static final Function<Probability, Probability> Calculation = probability -> Probability.valueOf(probability.toBigDecimal().setScale(2, RoundingMode.HALF_UP));

    private final BudgetRateCalculator delegate;

    NormalizeBudgetRateDecorator(BudgetRateCalculator delegate) {
        Objects.requireNonNull(delegate);
        this.delegate = new NullAndImpossibleBudgetRateHandlingDecorator(delegate, NormalizeBudgetRateDecorator.Calculation);
    }

    @Override
    public Probability calculateBudgetRate(Odd<?> bookmakersOdd, Probability modelProbability) {
        return this.delegate.calculateBudgetRate(bookmakersOdd, modelProbability);
    }
}
