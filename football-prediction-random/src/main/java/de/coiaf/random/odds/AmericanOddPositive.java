package de.coiaf.random.odds;

import de.coiaf.random.probability.Probabilities;
import de.coiaf.random.probability.Probability;

import java.math.BigDecimal;
import java.util.Objects;

class AmericanOddPositive extends AmericanOdd {

    static AmericanOddPositive from(Odd<?> odd) {
        return from(odd.getImpliedProbability());
    }
    static AmericanOddPositive from(Probability probability) {
        return new AmericanOddPositive(probability);
    }

    static int convertToPositiveAmericanOddValue(BigDecimal decimalOddValue) {
        Objects.requireNonNull(decimalOddValue);
        Odds.validateDecimalOddValue(decimalOddValue);

        return Probabilities.isMaxInvertibleValue(decimalOddValue) ? Integer.MAX_VALUE :
                decimalOddValue.subtract(BigDecimal.ONE).multiply(ONE_HUNDRED).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }

    static BigDecimal convertToDecimalOddValue(int oddValue) {
        BigDecimal workingOddValue = BigDecimal.valueOf(oddValue);
        BigDecimal result;

        if (BigDecimal.ZERO.compareTo(workingOddValue) > 0) {
            throw new IllegalArgumentException("Parameter oddValue must be positive.");
        }
        else if (oddValue == Integer.MAX_VALUE) {
            result = Odds.MAX_DECIMAL_ODD_VALUE;
        }
        else {
            result = workingOddValue.divide(ONE_HUNDRED, 2, BigDecimal.ROUND_HALF_UP).add(BigDecimal.ONE);
        }

        return result;
    }

    AmericanOddPositive(Probability impliedProbability) {
        super(
                convertToPositiveAmericanOddValue(Odds.convertProbabilityToDecimalOddValue(impliedProbability)),
                impliedProbability
        );
    }

    AmericanOddPositive(int oddValue) {
        super(oddValue, convertToDecimalOddValue(oddValue));
    }

    AmericanOddPositive(BigDecimal decimalOddValue) {
        super(
                convertToPositiveAmericanOddValue(decimalOddValue),
                decimalOddValue
        );
    }
}
