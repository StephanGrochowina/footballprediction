package de.coiaf.random.odds;

import de.coiaf.random.probability.Probability;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author Stephan Grochowina <stephan.grochowina@web.de>
 */
public class FractionalOdd extends AbstractOdd<FractionalOdd.OddValue> {


    public static FractionalOdd from(Odd<?> odd) {
        return from(odd.getImpliedProbability());
    }
    public static FractionalOdd from(Probability probability) {
        return new FractionalOdd(probability);
    }

    private static void validateOddValue(OddValue fractionalOddValue) {
        validateOddValue(fractionalOddValue.getDividend(), fractionalOddValue.getDivisor());
    }
    static void validateOddValue(BigDecimal dividend, BigDecimal divisor) {
        if (divisor.compareTo(BigDecimal.ONE) < 0) {
            throw new IllegalArgumentException("Divisor must be greater than or equal 1.");
        }
        if (dividend.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Dividend must be greater than or equal 0.");
        }
    }
    static BigDecimal convertToDecimalOddValue(OddValue fractionalOddValue) {
        Objects.requireNonNull(fractionalOddValue);

        validateOddValue(fractionalOddValue);

        return fractionalOddValue.getQuotient().add(BigDecimal.ONE);
    }
    static FractionalOdd.OddValue convertToFractionalOddValue(BigDecimal decimalOddValue) {
        Objects.requireNonNull(decimalOddValue);

        return new FractionalOdd.OddValue(decimalOddValue.subtract(BigDecimal.ONE), BigDecimal.ONE);
    }

    private final OddValue oddValue;

    FractionalOdd(Probability impliedProbability) {
        super(impliedProbability);
        this.oddValue = convertToFractionalOddValue(this.getDecimalOddValue());
    }

    FractionalOdd(OddValue oddValue) {
        this(oddValue, convertToDecimalOddValue(oddValue));
    }
    private FractionalOdd(OddValue oddValue, BigDecimal decimalOddValue) {
        super(decimalOddValue);
        this.oddValue = oddValue;
    }

    @Override
    public OddValue getOddValue() {
        return this.oddValue;
    }

    public static class OddValue implements Serializable {
        private final BigDecimal dividend;
        private final BigDecimal divisor;
        private final BigDecimal quotient;

        OddValue(BigDecimal dividend, BigDecimal divisor) {
            validateOddValue(dividend, divisor);

            this.dividend = dividend;
            this.divisor = divisor;
            this.quotient = Probability.createProbabilityValueByDivision(dividend, divisor);
        }

        BigDecimal getDivisor() {
            return this.divisor;
        }

        BigDecimal getDividend() {
            return this.dividend;
        }

        BigDecimal getQuotient() {
            return this.quotient;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OddValue oddValue = (OddValue) o;
            return oddValue.quotient.compareTo(this.quotient) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.quotient);
        }
    }

}
