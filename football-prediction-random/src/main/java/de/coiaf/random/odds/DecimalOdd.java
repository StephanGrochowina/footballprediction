package de.coiaf.random.odds;

import de.coiaf.random.probability.Probability;

import java.math.BigDecimal;

/**
 * @author Stephan Grochowina <stephan.grochowina@web.de>
 */
public class DecimalOdd extends AbstractOdd<BigDecimal> {

    public static DecimalOdd from(Odd<?> odd) {
        return from(odd.getImpliedProbability());
    }
    public static DecimalOdd from(Probability probability) {
        return new DecimalOdd(probability);
    }
    public static DecimalOdd from(BigDecimal decimalOddValue) {
        return new DecimalOdd(decimalOddValue);
    }

    DecimalOdd(Probability impliedProbability) {
        super(impliedProbability);
    }

    DecimalOdd(BigDecimal decimalOddValue) {
        super(decimalOddValue);
    }

    @Override
    public BigDecimal getOddValue() {
        return this.getDecimalOddValue();
    }
}
