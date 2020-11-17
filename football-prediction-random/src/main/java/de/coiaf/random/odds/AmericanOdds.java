package de.coiaf.random.odds;

import de.coiaf.random.probability.Probability;

import java.math.BigDecimal;

public class AmericanOdds {

    /**
     * Creates a new {@link AmericanOdd} instance from {@code odd}.
     * @param odd the odd to convertToLocalDate into a positive American odd
     * @return a positive AmericanOdd instance
     * @throws NullPointerException if {@code odd} is null
     */
    public static AmericanOdd from(Odd<?> odd) {
        return from(odd.getImpliedProbability());
    }

    /**
     * Creates a new {@link AmericanOdd} instance from {@code odd}.
     * If {@code negativeAmericanOddIfPossible} is set to true and the
     * implied probability of {@code odd} is not {@link Probability#CERTAIN}
     * an instance representing a negative American odd is created,
     * otherwise one representing a positive American odd.
     * @param odd the odd to convertToLocalDate into a positive American odd
     * @param negativeAmericanOddIfPossible flag indicating that
     * the result should be a negative American odd
     * @return either a positive or negative {@link AmericanOdd} instance
     * @throws NullPointerException if {@code odd} is null
     */
    public static AmericanOdd from(Odd<?> odd, boolean negativeAmericanOddIfPossible) {
        return from(odd.getImpliedProbability(), negativeAmericanOddIfPossible);
    }

    /**
     * Creates a new {@link AmericanOdd} instance from {@code probability}.
     * @param probability the probability value corresponding to the positive {@link AmericanOdd}
     * instance to be created
     * @return a positive AmericanOdd instance
     * @throws NullPointerException if {@code probability} is null
     */
    public static AmericanOdd from(Probability probability) {
        return from(probability, false);
    }

    /**
     * Creates a new {@link AmericanOdd} instance from {@code probability}.
     * If {@code negativeAmericanOddIfPossible} is set to true and the
     * probability {@code probability} is not {@link Probability#CERTAIN}
     * an instance representing a negative American odd is created,
     * otherwise one representing a positive American odd.
     * @param probability the probability value corresponding to the positive {@link AmericanOdd}
     * @param negativeAmericanOddIfPossible flag indicating that
     * the result should be a negative American odd
     * @return either a positive or negative {@link AmericanOdd} instance
     * @throws NullPointerException if {@code probability} is null
     */
    public static AmericanOdd from(Probability probability, boolean negativeAmericanOddIfPossible) {
        return negativeAmericanOddIfPossible && !Probability.isCertain(probability) ?
                AmericanOddNegative.from(probability) : AmericanOddPositive.from(probability);
    }

    /**
     * Creates a new {@link AmericanOdd} instance from {@code oddValue}.
     * If {@code oddValue &lt; 0} an instance representing a negative American odd is created,
     * otherwise one representing a positive American odd.
     * @param oddValue the odd value representing the American odd
     * @return either a positive or negative {@link AmericanOdd} instance
     */
    public static AmericanOdd from(int oddValue) {
        return oddValue < 0 ? new AmericanOddNegative(oddValue) : new AmericanOddPositive(oddValue);
    }

    /**
     * Creates a new {@link AmericanOdd} instance from {@code decimalOddValue}.
     * @param decimalOddValue the value of e decimal odd to be used for creating
     * an {@link AmericanOdd} instance
     * @return a positive AmericanOdd instance
     * @throws NullPointerException if {@code decimalOddValue} is null
     */
    public static AmericanOdd from(BigDecimal decimalOddValue) {
        return from(decimalOddValue, false);
    }

    /**
     * Creates a new {@link AmericanOdd} instance from {@code decimalOddValue}.
     * If {@code negativeAmericanOddIfPossible} is set to true and the
     * decimal odd value {@code {@link BigDecimal#ONE}.compareTo(decimalOddValue) != 0}
     * an instance representing a negative American odd is created,
     * otherwise one representing a positive American odd.
     * @param decimalOddValue the value of e decimal odd to be used for creating
     * an {@link AmericanOdd} instance
     * @param negativeAmericanOddIfPossible flag indicating that
     * the result should be a negative American odd
     * @return either a positive or negative {@link AmericanOdd} instance
     * @throws NullPointerException if {@code decimalOddValue} is null
     */
    public static AmericanOdd from(BigDecimal decimalOddValue, boolean negativeAmericanOddIfPossible) {
        return negativeAmericanOddIfPossible && BigDecimal.ONE.compareTo(decimalOddValue) != 0 ?
                new AmericanOddNegative(decimalOddValue) : new AmericanOddPositive(decimalOddValue);
    }
}
