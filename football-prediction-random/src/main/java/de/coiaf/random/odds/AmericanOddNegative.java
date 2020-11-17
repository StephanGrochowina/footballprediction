package de.coiaf.random.odds;

import de.coiaf.random.probability.Probabilities;
import de.coiaf.random.probability.Probability;

import java.math.BigDecimal;
import java.util.Objects;

class AmericanOddNegative extends AmericanOdd {

    static AmericanOddNegative from(Odd<?> odd) {
        return from(odd.getImpliedProbability());
    }
    static AmericanOddNegative from(Probability probability) {
        return new AmericanOddNegative(probability);
    }

    static int convertToNegativeAmericanOddValue(BigDecimal decimalOddValue) {
        Objects.requireNonNull(decimalOddValue);
        Odds.validateDecimalOddValue(decimalOddValue);

        if (BigDecimal.ONE.compareTo(decimalOddValue) == 0) {
            throw new IllegalArgumentException("This class only supports decimalOddValues greater than 1.");
        }

        return Probabilities.isMaxInvertibleValue(decimalOddValue) ? -Integer.MAX_VALUE :
                ONE_HUNDRED.divide(decimalOddValue.subtract(BigDecimal.ONE), 0, BigDecimal.ROUND_HALF_UP).negate().intValue();
    }

    static BigDecimal convertToDecimalOddValue(int oddValue) {
        BigDecimal workingOddValue = BigDecimal.valueOf(oddValue);
        BigDecimal result;

        if (BigDecimal.ZERO.compareTo(workingOddValue) <= 0) {
            throw new IllegalArgumentException("Parameter oddValue must be negative.");
        }
        else if (oddValue <= -Integer.MAX_VALUE) {
            result = Odds.MAX_DECIMAL_ODD_VALUE;
        }
        else {
            result = Probability.createProbabilityValueByDivision(ONE_HUNDRED, workingOddValue.abs()).add(BigDecimal.ONE);
        }

        return result;
    }

    AmericanOddNegative(Probability impliedProbability) {
        super(
                convertToNegativeAmericanOddValue(Odds.convertProbabilityToDecimalOddValue(impliedProbability)),
                impliedProbability
        );
    }

    AmericanOddNegative(int oddValue) {
        super(oddValue, convertToDecimalOddValue(oddValue));
    }

    AmericanOddNegative(BigDecimal decimalOddValue) {
        super(
                convertToNegativeAmericanOddValue(decimalOddValue),
                decimalOddValue
        );
    }
}
