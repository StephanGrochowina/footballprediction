package de.coiaf.random.odds;

import de.coiaf.random.probability.Probabilities;
import de.coiaf.random.probability.Probability;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * @author Stephan Grochowina <stephan.grochowina@web.de>
 */
public class Odds {
    static final BigDecimal MAX_DECIMAL_ODD_VALUE = Probabilities.MAX_INVERTIBLE_VALUE;

    static BigDecimal convertProbabilityToDecimalOddValue(Probability probability) {
        return Probability.isImpossible(probability) ? MAX_DECIMAL_ODD_VALUE: Probabilities.invertProbabilityValue(probability);
    }

    static Probability convertDecimalOddValueToProbability(BigDecimal decimalOddValue) {
        validateDecimalOddValue(decimalOddValue);

        return Probabilities.invertValue(decimalOddValue);
    }

    @SuppressWarnings("WeakerAccess")
    public static boolean equalsOdd(Odd<?> odd1, Odd<?> odd2) {
        BigDecimal odd1DecimalOddValue = odd1 == null ? BigDecimal.ZERO : odd1.getNormalizedDecimalOddValue();
        BigDecimal odd2DecimalOddValue = odd2 == null ? BigDecimal.ZERO : odd2.getNormalizedDecimalOddValue();

        return equals(odd1DecimalOddValue, odd2DecimalOddValue);
    }

    @SuppressWarnings("unchecked")
    public static <T, O extends Odd<T>> boolean equals(O odd, Object o) {
        Objects.requireNonNull(odd);

        if (odd == o) {
            return true;
        }
        if (o == null || ! odd.getClass().isAssignableFrom(o.getClass())) {
            return false;
        }

        O other = (O) o;

        return Odds.equals(odd.getDecimalOddValue(), other.getDecimalOddValue());
    }

    private static boolean equals(BigDecimal decimalOddValue1, BigDecimal decimalOddValue2) {
        Objects.requireNonNull(decimalOddValue1);
        Objects.requireNonNull(decimalOddValue2);

        return decimalOddValue1.compareTo(decimalOddValue2) == 0;
    }

    public static int hashCode(Odd<?> odd) {
        BigDecimal decimalOddValue = odd == null ? null : odd.getDecimalOddValue();

        return Objects.hash(decimalOddValue);
    }

    @SuppressWarnings("WeakerAccess")
    public static <OV> void requireNotEmpty(Collection<Odd<OV>> odds) {
        Objects.requireNonNull(odds);

        if (odds.isEmpty()) {
            throw new IllegalArgumentException("Collection must contain elements.");
        }
    }

    @SuppressWarnings("WeakerAccess")
    public static <K, OV> void requireNotEmpty(Map<K, Odd<OV>> odds) {
        Objects.requireNonNull(odds);

        if (odds.isEmpty()) {
            throw new IllegalArgumentException("Collection must contain elements.");
        }
    }

    static void validateDecimalOddValue(BigDecimal decimalOddValue) {
        if (BigDecimal.ONE.compareTo(decimalOddValue) > 0) {
            throw new IllegalArgumentException("Illegal decimal odd value");
        }
    }
}
