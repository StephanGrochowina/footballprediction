package de.coiaf.random.odds;

import de.coiaf.random.probability.Probability;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author Stephan Grochowina <stephan.grochowina@web.de>
 */
public abstract class AmericanOdd extends AbstractOdd<Integer> {
    static final BigDecimal ONE_HUNDRED = new BigDecimal("100", MathContext.UNLIMITED);

    private final int oddValue;

    AmericanOdd(int oddValue, Probability impliedProbability) {
        super(impliedProbability);
        this.oddValue = oddValue;
    }

    AmericanOdd(int oddValue, BigDecimal decimalOddValue) {
        super(decimalOddValue);
        this.oddValue = oddValue;
    }

    @Override
    public Integer getOddValue() {
        return this.oddValue;
    }
}
