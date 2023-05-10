package de.coiaf.random.betevaluation;

import de.coiaf.random.odds.Odd;
import de.coiaf.random.probability.Probability;

class BudgetRateCalculatorProbabilityOnly implements BudgetRateCalculator {

    @Override
    public Probability calculateBudgetRate(Odd<?> bookmakersOdd, Probability modelProbability) {
        return Probability.isImpossible(modelProbability, true) ? Probability.IMPOSSIBLE : modelProbability;
    }
}
