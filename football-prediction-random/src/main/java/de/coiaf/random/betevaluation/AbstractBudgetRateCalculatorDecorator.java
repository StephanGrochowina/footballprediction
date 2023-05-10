package de.coiaf.random.betevaluation;

import de.coiaf.random.odds.Odd;
import de.coiaf.random.probability.Probability;

import java.util.Objects;

abstract class AbstractBudgetRateCalculatorDecorator implements BudgetRateCalculator {

    private final BudgetRateCalculator delegate;

    /**
     * Instantiates a new decorator for calculating budget rates
     * @param delegate the delegate to be internally called
     */
    protected AbstractBudgetRateCalculatorDecorator(BudgetRateCalculator delegate) {
        Objects.requireNonNull(delegate);

        this.delegate = delegate;
    }

    /**
     * Calculates the budget rate of the delegate.
     * @param bookmakersOdd the odd provided by a bookmaker
     * @param modelProbability the probability of a personal model
     * @return the budget rate of the delegate
     */
    protected Probability calculateDelegateBudgetRate(Odd<?> bookmakersOdd, Probability modelProbability) {
        return this.delegate.calculateBudgetRate(bookmakersOdd, modelProbability);
    }
}
