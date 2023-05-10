package de.coiaf.random.betevaluation;

import de.coiaf.random.odds.Odd;
import de.coiaf.random.probability.Probability;

import java.math.BigDecimal;

class BudgetRateCalculatorKellyComplete extends AbstractBudgetRateCalculatorRespectingBetValue {

    BudgetRateCalculatorKellyComplete() {
        super();
    }

    BudgetRateCalculatorKellyComplete(BigDecimal valueThreshold) {
        super(valueThreshold);
    }

    @Override
    public Probability calculateBudgetRate(Odd<?> bookmakersOdd, Probability modelProbability) {
        BetValueCalculator.BetValue betValue = this.calculateBetValue(bookmakersOdd, modelProbability);
        Probability budgetRate = Probability.IMPOSSIBLE;

        if (betValue.isValueBet() && Probability.isCertain(modelProbability)) {
            budgetRate = Probability.CERTAIN;
        }
        else if (betValue.isValueBet()) {
            budgetRate = Probability.createProbabilityByDivision(
                    betValue.toBigDecimal(), bookmakersOdd.getDecimalOddValue().subtract(BigDecimal.ONE));
        }

        return budgetRate;
    }
}
