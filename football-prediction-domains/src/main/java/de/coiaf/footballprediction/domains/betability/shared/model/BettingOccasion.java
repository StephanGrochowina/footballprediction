package de.coiaf.footballprediction.domains.betability.shared.model;

import de.coiaf.random.odds.Odd;
import de.coiaf.random.probability.Probability;

import java.util.Objects;

/**
 * Value object representing a betting occasion. A betting occasion is a pair consisting of
 * the model probability and the odd offered by a bookmaker.
 */
public class BettingOccasion {
    private final Probability modelProbability;
    private final Odd<?> bookmakerOdd;

    /**
     * constructor
     * @param modelProbability the probability of the model for a certain betting occasion
     * @param bookmakerOdd the odd offered by the bookmaker
     * @throws NullPointerException if either of the parameters is null
     */
    public BettingOccasion(Probability modelProbability, Odd<?> bookmakerOdd) {
        Objects.requireNonNull(modelProbability);
        Objects.requireNonNull(bookmakerOdd);

        this.modelProbability = modelProbability;
        this.bookmakerOdd = bookmakerOdd;
    }

    public Probability getModelProbability() {
        return this.modelProbability;
    }

    public Odd<?> getBookmakerOdd() {
        return this.bookmakerOdd;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof BettingOccasion)) return false;

        BettingOccasion that = (BettingOccasion) other;
        return this.modelProbability.equals(that.modelProbability) && this.bookmakerOdd.equals(that.bookmakerOdd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.modelProbability, this.bookmakerOdd);
    }
}
