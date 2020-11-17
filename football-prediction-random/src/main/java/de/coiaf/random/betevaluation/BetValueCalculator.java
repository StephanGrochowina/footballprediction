package de.coiaf.random.betevaluation;

import de.coiaf.random.odds.Odd;
import de.coiaf.random.probability.Probability;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Supplier;

public class BetValueCalculator {

    static final BigDecimal DEFAULT_VALUE_BET_THRESHOLD = BigDecimal.ZERO;

    /**
     * Calculates the value of a bet given by {@code bookmakersOdd} and {@code modelProbability}.
     * If the bet has value true is returned, otherwise false.
     * This method is equivalent to calling {@link BetValueCalculator#isValueBet(Odd, Probability, BigDecimal)}}
     * with a thresold of BigDecimal.ZERO.
     * @param bookmakersOdd the odd provided by a bookmaker
     * @param modelProbability the probability of a personal model
     * @return if {@code bookmakersOdd} is value related to {@code modelProbability}
     * @throws NullPointerException if {@code bookmakersOdd} or {@code modelProbability} is null
     */
    public boolean isValueBet(Odd<?> bookmakersOdd, Probability modelProbability) {
        return this.isValueBet(() -> this.createBetValue(bookmakersOdd, modelProbability));
    }

    /**
     * Calculates the value of a bet given by {@code bookmakersOdd} and {@code modelProbability}.
     * If the bet value exceeds {@code valueThreshold} true is returned, otherwise false.
     * @param bookmakersOdd the odd provided by a bookmaker
     * @param modelProbability the probability of a personal model
     * @param valueThreshold the threshold which has to be exceeded for a bet to have value. If {@code valueThreshold}
     * is null or negative the threshold is set to BigDecimal.ZERO.
     * @return if the value of the bet represented by {@code bookmakersOdd} related to {@code modelProbability} exceeds
     * {@code valueThreshold}
     * @throws NullPointerException if {@code bookmakersOdd} or {@code modelProbability} is null
     */
    public boolean isValueBet(Odd<?> bookmakersOdd, Probability modelProbability, BigDecimal valueThreshold) {
        return this.isValueBet(() -> this.createBetValue(bookmakersOdd, modelProbability, valueThreshold));
    }

    private boolean isValueBet(Supplier<BetValue> betValueSupplier) {
        Objects.requireNonNull(betValueSupplier);

        BetValue betValue = betValueSupplier.get();

        return betValue.isValueBet();
    }

    /**
     * Calculates the value of a bet represented by {@code bookmakersOdd} related to {@code modelProbability}. If the
     * odd {@code bookmakersOdd} is no value for the probability {@code modelProbability} BigDecimal.ZERO is returned
     * instead.
     * This method is equivalent to calling {@link BetValueCalculator#determineBetValue(Odd, Probability, BigDecimal)}}
     * with a thresold of BigDecimal.ZERO.
     * @param bookmakersOdd the odd provided by a bookmaker
     * @param modelProbability the probability of a personal model
     * @return the value of the bet represented by {@code bookmakersOdd} related to {@code modelProbability} or
     * BigDecimal.ZERO if the bet is of no value
     * @throws NullPointerException if {@code bookmakersOdd} or {@code modelProbability} is null
     */
    public BigDecimal determineBetValue(Odd<?> bookmakersOdd, Probability modelProbability) {
        return this.determineBetValue(() -> this.createBetValue(bookmakersOdd, modelProbability));
    }

    /**
     * Calculates the value of a bet represented by {@code bookmakersOdd} related to {@code modelProbability}. If the
     * odd {@code bookmakersOdd} is no value for the probability {@code modelProbability} or the value does not exceed
     * {@code valueThreshold} BigDecimal.ZERO is returned instead.
     * @param bookmakersOdd the odd provided by a bookmaker
     * @param modelProbability the probability of a personal model
     * @param valueThreshold the threshold which has to be exceeded for a bet to have value. If {@code valueThreshold}
     * is null or negative the threshold is set to BigDecimal.ZERO.
     * @return the value of the bet represented by {@code bookmakersOdd} related to {@code modelProbability} or
     * BigDecimal.ZERO if the bet is of no value or the value does not exceed {@code valueThreshold}.
     * @throws NullPointerException if {@code bookmakersOdd} or {@code modelProbability} is null
     */
    public BigDecimal determineBetValue(Odd<?> bookmakersOdd, Probability modelProbability, BigDecimal valueThreshold) {
        return this.determineBetValue(() -> this.createBetValue(bookmakersOdd, modelProbability, valueThreshold));
    }

    private BigDecimal determineBetValue(Supplier<BetValue> betValueSupplier) {
        Objects.requireNonNull(betValueSupplier);

        BetValue betValue = betValueSupplier.get();

        return betValue.toBigDecimal();
    }

    /**
     * Creates a BetValue instance for a given {@code bookmakersOdd} and {@code modelProbability}.
     * If that instance represents a BigDecimal value greater than BigDecimal.ZERO the bet is considered to
     * have value.
     * @param bookmakersOdd the odd provided by a bookmaker
     * @param modelProbability the probability of a personal model
     * @return a BetVaule instance
     * @throws NullPointerException if {@code bookmakersOdd} or {@code modelProbability} is null
     */
    BetValue createBetValue(Odd<?> bookmakersOdd, Probability modelProbability) {
        return new BetValue(bookmakersOdd, modelProbability);
    }

    /**
     * Creates a BetValue instance for a given {@code bookmakersOdd} and {@code modelProbability}.
     * If that instance represents a BigDecimal value greater than {@code valueThreshold} the bet is considered to
     * have value.
     * @param bookmakersOdd the odd provided by a bookmaker
     * @param modelProbability the probability of a personal model
     * @param valueThreshold the threshold which has to be exceeded for a bet to have value. If {@code valueThreshold}
     * is null or negative the threshold is set to BigDecimal.ZERO.
     * @return a BetVaule instance
     * @throws NullPointerException if {@code bookmakersOdd} or {@code modelProbability} is null
     */
    BetValue createBetValue(Odd<?> bookmakersOdd, Probability modelProbability, BigDecimal valueThreshold) {
        return new BetValue(bookmakersOdd, modelProbability, valueThreshold);
    }

    static class BetValue {

        private static BigDecimal calculateBetValue(Odd<?> bookmakersOdd, Probability modelProbability) {
            BigDecimal bookmakersOddValue = bookmakersOdd.getDecimalOddValue();
            BigDecimal betValue = modelProbability.toBigDecimal().multiply(bookmakersOddValue).subtract(BigDecimal.ONE);

            return BigDecimal.ZERO.compareTo(betValue) >= 0 ? BigDecimal.ZERO : betValue;
        }

        private final Odd<?> bookmakersOdd;
        private final Probability modelProbability;
        private final BigDecimal betValue;
        private final BigDecimal valueThreshold;

        private BetValue(Odd<?> bookmakersOdd, Probability modelProbability) {
            this(bookmakersOdd, modelProbability, DEFAULT_VALUE_BET_THRESHOLD);
        }

        private BetValue(Odd<?> bookmakersOdd, Probability modelProbability, BigDecimal valueThreshold) {
            Objects.requireNonNull(bookmakersOdd);
            Objects.requireNonNull(modelProbability);

            this.bookmakersOdd = bookmakersOdd;
            this.modelProbability = modelProbability;
            this.valueThreshold = valueThreshold == null || DEFAULT_VALUE_BET_THRESHOLD.compareTo(valueThreshold) > 0
                    ? DEFAULT_VALUE_BET_THRESHOLD : valueThreshold;
            this.betValue = calculateBetValue(bookmakersOdd, modelProbability);
        }

        Odd<?> getBookmakersOdd() {
            return this.bookmakersOdd;
        }

        Probability getModelProbability() {
            return this.modelProbability;
        }

        BigDecimal toBigDecimal() {
            return this.betValue;
        }

        BigDecimal getValueThreshold() {
            return this.valueThreshold;
        }

        boolean isValueBet() {
            return this.valueThreshold.compareTo(this.betValue) < 0;
        }
    }
}
