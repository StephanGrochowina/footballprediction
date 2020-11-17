package de.coiaf.random.probability;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;

/**
 * @author Stephan Grochowina <stephan.grochowina@web.de>
 */
public class Probabilities {
    public static final BigDecimal MAX_INVERTIBLE_VALUE = BigDecimal.valueOf(Double.MAX_VALUE);

    public static BigDecimal invertProbabilityValue(Probability probability) {
        Objects.requireNonNull(probability);

        if (Probability.isImpossible(probability)) {
            throw new IllegalArgumentException("Cannot invert probability 0");
        }

        return BigDecimal.ONE.divide(probability.toBigDecimal(), Probability.DIVISION_SCALE, BigDecimal.ROUND_HALF_UP);
    }

    public static Probability invertValue(BigDecimal value) {
        Objects.requireNonNull(value);

        if (BigDecimal.ONE.compareTo(value) > 0) {
            throw new IllegalArgumentException("Values below 1 cannot be inverted");
        }

        return isMaxInvertibleValue(value) ? Probability.IMPOSSIBLE : new Probability(BigDecimal.ONE.divide(value, Probability.DIVISION_SCALE, BigDecimal.ROUND_HALF_UP));
    }

    public static boolean isMaxInvertibleValue(BigDecimal value) {
        return value != null && MAX_INVERTIBLE_VALUE.compareTo(value) == 0;
    }

    public static BigDecimal toFullyScaledBigDecimal(Probability probability) {
        return probability == null ? null : probability.toBigDecimal().setScale(Probability.DIVISION_SCALE, BigDecimal.ROUND_HALF_UP);
    }
}
