package de.coiaf.random.betevaluation;

import de.coiaf.random.odds.Odd;
import de.coiaf.random.probability.Probability;

import java.math.BigDecimal;

class SimpleBudgetRateCalculator extends AbstractBudgetRateCalculatorRespectingBetValue {

    SimpleBudgetRateCalculator() {
        super();
    }

    SimpleBudgetRateCalculator(BigDecimal valueThreshold) {
        super(valueThreshold);
    }

    @Override
    public Probability calculateBudgetRate(Odd<?> bookmakersOdd, Probability modelProbability) {
        BetValueCalculator.BetValue betValue = this.calculateBetValue(bookmakersOdd, modelProbability);

        return betValue.isValueBet() ? Probability.CERTAIN : Probability.IMPOSSIBLE;
    }
}
