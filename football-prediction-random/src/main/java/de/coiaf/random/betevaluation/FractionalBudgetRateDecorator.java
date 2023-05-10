package de.coiaf.random.betevaluation;

import de.coiaf.random.odds.Odd;
import de.coiaf.random.probability.Probability;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;

/**
 * Decorator which only returns a dined fraction of the calculated budget rate.
 */
class FractionalBudgetRateDecorator implements BudgetRateCalculator {

    static final Probability ADVISED_KELLY_FRACTION = new Probability(new BigDecimal("0.1", MathContext.UNLIMITED));

    private final BudgetRateCalculator delegate;

    FractionalBudgetRateDecorator(BudgetRateCalculator delegate) {
        this(delegate, ADVISED_KELLY_FRACTION);
    }

    FractionalBudgetRateDecorator(BudgetRateCalculator delegate, Probability fraction) {
        Objects.requireNonNull(delegate);
        Objects.requireNonNull(fraction);
        if (Probability.isImpossible(fraction)) {
            throw new IllegalArgumentException("Parameter fraction should not represent an impossible value.");
        }

        this.delegate = new NullAndImpossibleBudgetRateHandlingDecorator(delegate, fraction::multiply);
    }

    @Override
    public Probability calculateBudgetRate(Odd<?> bookmakersOdd, Probability modelProbability) {
        return this.delegate.calculateBudgetRate(bookmakersOdd, modelProbability);
    }
}
