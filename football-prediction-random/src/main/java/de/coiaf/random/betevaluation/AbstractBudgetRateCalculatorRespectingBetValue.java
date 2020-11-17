package de.coiaf.random.betevaluation;

import de.coiaf.random.odds.Odd;
import de.coiaf.random.probability.Probability;

import java.math.BigDecimal;

public abstract class AbstractBudgetRateCalculatorRespectingBetValue implements BudgetRateCalculator {

    private final BigDecimal valueThreshold;
    private final BetValueCalculator betValueCalculator = new BetValueCalculator();

    protected AbstractBudgetRateCalculatorRespectingBetValue() {
        this(BetValueCalculator.DEFAULT_VALUE_BET_THRESHOLD);
    }

    protected AbstractBudgetRateCalculatorRespectingBetValue(BigDecimal valueThreshold) {
        this.valueThreshold = valueThreshold;
    }

    protected final BetValueCalculator.BetValue calculateBetValue(Odd<?> bookmakersOdd, Probability modelProbability) {
        return this.betValueCalculator.createBetValue(bookmakersOdd, modelProbability, this.valueThreshold);
    }

    protected BigDecimal getValueThreshold() {
        return this.valueThreshold;
    }

    protected BetValueCalculator getBetValueCalculator() {
        return this.betValueCalculator;
    }
}
