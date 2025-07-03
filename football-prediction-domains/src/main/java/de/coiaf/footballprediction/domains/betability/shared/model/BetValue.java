package de.coiaf.footballprediction.domains.betability.shared.model;

import de.coiaf.random.odds.DecimalOdd;
import de.coiaf.random.odds.Odd;
import de.coiaf.random.probability.Probability;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Represents the bet value of a betting occasion.
 */
public class BetValue {

    private final BettingOccasion occasion;
    private final Threshold threshold;
    private final BigDecimal value;

    /**
     * Calculates the bet value for a given betting occasion.
     * @param occasion the betting occasion which bet value should be calculated
     * @return the bet value
     */
    private static BigDecimal calculateBetValue(BettingOccasion occasion) {
        Objects.requireNonNull(occasion);

        BigDecimal modelProbabilityValue = occasion.getModelProbability().toBigDecimal();
        BigDecimal bookmakerOddValue = occasion.getBookmakerOdd().getDecimalOddValue();

        return modelProbabilityValue.multiply(bookmakerOddValue).subtract(BigDecimal.ONE);
    }

    /**
     * constructor for the minimum threshold {@code Threshold.MINIMUM_THRESHOLD}
     * @param occasion the betting occasion which bet value should be created
     * @throws NullPointerException if occasion is null.
     */
    public BetValue(BettingOccasion occasion) {
        this(occasion, Threshold.MINIMUM_THRESHOLD);
    }

    /**
     * constructor
     * @param occasion the betting occasion which bet value should be created
     * @param thresholdValue the threshold which is a lower exclusive bound for an acceptable bet value
     * @throws NullPointerException if any of the parameters is null.
     * @throws IllegalArgumentException if {@code thresholdValue} is less than 0
     */
    public BetValue(BettingOccasion occasion, BigDecimal thresholdValue) {
        this(occasion, new Threshold(thresholdValue));
    }

    /**
     * constructor
     * @param occasion the betting occasion which bet value should be created
     * @param threshold the threshold which is a lower exclusive bound for an acceptable bet value
     * @throws NullPointerException if any of the parameters is null.
     */
    public BetValue(BettingOccasion occasion, Threshold threshold) {
        Objects.requireNonNull(occasion);
        Objects.requireNonNull(threshold);

        this.occasion = occasion;
        this.threshold = threshold;
        this.value = BetValue.calculateBetValue(occasion);
    }

    public BettingOccasion getOccasion() {
        return this.occasion;
    }

    public Threshold getThreshold() {
        return this.threshold;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    /**
     * Determines whether this instance returns a bet value, ie. threshold < value is true.
     * @return true if the value is greater than the threshold.
     */
    public boolean hasBetValue() {
        return this.threshold.getValue().compareTo(this.value) < 0;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof BetValue)) return false;

        BetValue betValue = (BetValue) other;
        return this.occasion.equals(betValue.occasion) && this.threshold.equals(betValue.threshold);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.occasion, this.threshold);
    }

    public static class Threshold {

        static final BigDecimal MINIMUM_THRESHOLD_VALUE = BigDecimal.ZERO;
        static final BigDecimal DEFAULT_THRESHOLD_VALUE = new BigDecimal("0.1");
        public static final Threshold MINIMUM_THRESHOLD = new Threshold(MINIMUM_THRESHOLD_VALUE);
        public static final Threshold DEFAULT_THRESHOLD = new Threshold(DEFAULT_THRESHOLD_VALUE);

        private final BigDecimal value;

        /**
         * constructor
         * @param value the threshold value which must be greater than 0
         * @throws NullPointerException if {@code value} is null
         * @throws IllegalArgumentException if {@code value} is less than 0
         */
        public Threshold(BigDecimal value) {
            Objects.requireNonNull(value);

            if (MINIMUM_THRESHOLD_VALUE.compareTo(value) > 0) {
                throw new IllegalArgumentException("bet value threshold must not be less than 0. ");
            }

            this.value = value.setScale(4, RoundingMode.HALF_UP);
        }

        public BigDecimal getValue() {
            return this.value;
        }

        public boolean isDefault() {
            return Threshold.DEFAULT_THRESHOLD.value.equals(this.value);
        }

        /**
         * Calculates the minimum bookmaker odd required for this threshold and the given model probability.
         * @param modelProbability the model probability to determine the minimum bookmaker odd for
         * @return the required minimum bookmaker odd
         * @throws NullPointerException if modelProbability is null
         * @throws IllegalArgumentException if modelProbability equals the impossible probability
         */
        Odd<?> calculateMinimumBookmakerOdd(Probability modelProbability) {
            Objects.requireNonNull(modelProbability);

            if (Probability.isImpossible(modelProbability)) {
                throw new IllegalArgumentException("Parameter modelProbability must represent a probability greater than 0.");
            }

            DecimalOdd oddCorrespondingToModelProbability = DecimalOdd.from(modelProbability);
            BigDecimal decimalOddValue = this.calculateThresholdFactor()
                    .multiply(oddCorrespondingToModelProbability.getDecimalOddValue());

            return DecimalOdd.from(decimalOddValue);
        }

        /**
         * Calculates the minimum model probability required for this threshold and the given bookmaker odd.
         * @param bookmakerOdd the bookmaker odd to determine the minimum model probability for
         * @return the required minimum model probability
         * @throws NullPointerException if bookmakerOdd is null
         * @throws IllegalArgumentException if the bookmaker odd is less than the minimum odd required by this threshold
         */
        Probability calculateMinimumModelProbability(Odd<?> bookmakerOdd) {
            Objects.requireNonNull(bookmakerOdd);

            BigDecimal minimumDecimalOddValue = this.calculateThresholdFactor();
            if (minimumDecimalOddValue.compareTo(bookmakerOdd.getDecimalOddValue()) > 0) {
                throw new IllegalArgumentException("Parameter bookmakerOdd must represent a decimal odd greater than " + minimumDecimalOddValue + ".");
            }

            BigDecimal probabilityValue = minimumDecimalOddValue.multiply(bookmakerOdd.getImpliedProbability().toBigDecimal());

            return Probability.valueOf(probabilityValue);
        }

        private BigDecimal calculateThresholdFactor() {
            return this.value.add(BigDecimal.ONE);
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (!(other instanceof Threshold)) return false;

            Threshold threshold = (Threshold) other;
            return Objects.equals(this.value, threshold.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.value);
        }
    }
}
