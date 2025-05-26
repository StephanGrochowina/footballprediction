package de.coiaf.random.odds;

import de.coiaf.random.probability.Probability;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @param <OV> the type of the odd value
 */
public interface Odd<OV> extends Comparable<Odd<?>> {

    /**
     * Returns the implied probability for this odd. This value is related to the decimal odd value by the formula
     * {@code <implied probability> = 1 / <decimal odd>}.
     *
     * There is always a {@link Probability Probability instance} returned.
     *
     * @return the probability for this odd that the
     * event takes place
     */
    Probability getImpliedProbability();

    /**
     * Returns the decimal odd value for this odd. The decimal odd value is related to the odd´s implied probability
     * by the formula {@code <decimal odd> = 1 / <implied probability>}.
     *
     * Therefore, it can have a value between 1 (for {@code Probability.CERTAIN.equals(this.getImpliedProbability()})
     * and {@code Double.POSITIVE_INFINITY} (for {@code Probability.IMPOSSIBLE.equals(this.getImpliedProbability()}).
     *
     * @return the decimal odd value for this odd
     *
     */
    BigDecimal getDecimalOddValue();

    /**
     *
     * @return a {@link BigDecimal BigDecimal instance} of the decimal odd value rounded to scale 2
     */
    default BigDecimal getNormalizedDecimalOddValue() {
        return this.getDecimalOddValue().setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    default int compareTo(Odd<?> other) {
        return this.getDecimalOddValue().compareTo(other.getDecimalOddValue());
    }

    /**
     *
     * @return the odd value of the implementation of this instance of {@link Odd the class Odd}.
     */
    OV getOddValue();

}
