package de.coiaf.footballprediction.probabilitymodel.domain.model.results;

import de.coiaf.footballprediction.sharedkernal.domain.model.bets.BetTypes;
import de.coiaf.random.probability.Probability;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class ResultsImpl implements ProbabilityModelResults {

    private final Map<BetTypes, Probability> results = new HashMap<>();

    ResultsImpl() {
    }

    @Override
    public Probability getProbability(BetTypes betType) {
        Objects.requireNonNull(betType, "Parameter betType must not be null.");
        if (!this.results.containsKey(betType)) {
            throw new IllegalArgumentException("Unsupported bet type '" + betType + "'");
        }

        return this.results.get(betType);
    }

    void setProperty(BetTypes betType, Probability probability) {
        Objects.requireNonNull(betType, "Parameter betType must not be null.");
        Objects.requireNonNull(probability, "Parameter probability must not be null.");
        if (this.results.containsKey(betType)) {
            throw new IllegalArgumentException("Bet type '" + betType + "' has already been set.");
        }

        this.results.put(betType, probability);
    }

    @Override
    public Map<BetTypes, Probability> All() {
        return new HashMap<>(this.results);
    }
}
