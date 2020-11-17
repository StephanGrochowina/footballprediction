package de.coiaf.footballprediction.backend.model.sharedcontext;

import de.coiaf.random.odds.DecimalOdd;
import de.coiaf.random.probability.Probability;

import java.util.Objects;

public class OddGroupTotalGoals {

    private final DecimalOdd oddBelowThreshold;
    private final DecimalOdd oddAboveThreshold;

    public OddGroupTotalGoals(DecimalOdd oddBelowThreshold, DecimalOdd oddAboveThreshold) {
        Objects.requireNonNull(oddBelowThreshold, "Parameter oddBelowThreshold must not be null.");
        Objects.requireNonNull(oddAboveThreshold, "Parameter oddAboveThreshold must not be null.");

        this.oddBelowThreshold = oddBelowThreshold;
        this.oddAboveThreshold = oddAboveThreshold;
    }

    public OddGroupTotalGoals(Probability probabilityBelowThreshold, Probability probabilityAboveThreshold) {
        Objects.requireNonNull(probabilityBelowThreshold, "Parameter probabilityBelowThreshold must not be null.");
        Objects.requireNonNull(probabilityAboveThreshold, "Parameter probabilityAboveThreshold must not be null.");

        this.oddBelowThreshold = DecimalOdd.from(probabilityBelowThreshold);
        this.oddAboveThreshold = DecimalOdd.from(probabilityAboveThreshold);
    }

    public DecimalOdd getOddBelowThreshold() {
        return this.oddBelowThreshold;
    }

    public DecimalOdd getOddAboveThreshold() {
        return this.oddAboveThreshold;
    }
}
