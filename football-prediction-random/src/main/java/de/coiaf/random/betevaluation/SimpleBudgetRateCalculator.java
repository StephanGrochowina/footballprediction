package de.coiaf.random.betevaluation;

import de.coiaf.random.odds.Odd;
import de.coiaf.random.probability.Probability;

import java.math.BigDecimal;

public class SimpleBudgetRateCalculator extends AbstractBudgetRateCalculatorRespectingBetValue {

    public SimpleBudgetRateCalculator() {
        super();
    }

    public SimpleBudgetRateCalculator(BigDecimal valueThreshold) {
        super(valueThreshold);
    }

    @Override
    public Probability calculateBudgetRate(Odd<?> bookmakersOdd, Probability modelProbability) {
        BetValueCalculator.BetValue betValue = this.calculateBetValue(bookmakersOdd, modelProbability);

        return betValue.isValueBet() ? Probability.CERTAIN : Probability.IMPOSSIBLE;
    }
}
