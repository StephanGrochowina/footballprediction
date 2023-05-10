package de.coiaf.random.betevaluation;

import de.coiaf.random.odds.Odd;
import de.coiaf.random.probability.Probability;

import java.util.Objects;
import java.util.function.Function;

class NullAndImpossibleBudgetRateHandlingDecorator extends AbstractBudgetRateCalculatorDecorator {

    private final Function<Probability, Probability> transformation;

    NullAndImpossibleBudgetRateHandlingDecorator(BudgetRateCalculator delegate, Function<Probability, Probability> transformation) {
        super(delegate);

        Objects.requireNonNull(transformation);

        this.transformation = transformation;
    }

    @Override
    public Probability calculateBudgetRate(Odd<?> bookmakersOdd, Probability modelProbability) {
        Probability delegateResult = this.calculateDelegateBudgetRate(bookmakersOdd, modelProbability);

        return Probability.isImpossible(delegateResult, true) ? Probability.IMPOSSIBLE : this.transformation.apply(delegateResult);
    }
}
