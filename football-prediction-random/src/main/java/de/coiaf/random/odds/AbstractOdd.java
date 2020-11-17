package de.coiaf.random.odds;

import de.coiaf.random.probability.Probability;

import java.math.BigDecimal;

public abstract class AbstractOdd<OV> implements Odd<OV> {

    private final Probability impliedProbability;
    private final BigDecimal decimalOddValue;

    @SuppressWarnings("WeakerAccess")
    protected AbstractOdd(Probability impliedProbability) {
        this.impliedProbability = impliedProbability;
        this.decimalOddValue = Odds.convertProbabilityToDecimalOddValue(impliedProbability);
    }

    @SuppressWarnings("WeakerAccess")
    protected AbstractOdd(BigDecimal decimalOddValue) {
        Odds.validateDecimalOddValue(decimalOddValue);

        this.decimalOddValue = decimalOddValue;
        this.impliedProbability = Odds.convertDecimalOddValueToProbability(decimalOddValue);
    }

    @Override
    public Probability getImpliedProbability() {
        return this.impliedProbability;
    }

    @Override
    public BigDecimal getDecimalOddValue() {
        return this.decimalOddValue;
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object o) {
        return Odds.equals(this, o);
    }

    @Override
    public int hashCode() {
        return Odds.hashCode(this);
    }

}
