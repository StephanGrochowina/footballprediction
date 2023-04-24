package de.coiaf.footballprediction.probabilitymodel.domain.model.results;

import java.util.HashSet;
import java.util.Set;

public class ResultsBuilder {

    private enum BetCategories {
        OUTCOME, TOTAL_GOALS_25
    }

    private final ResultsImpl results = new ResultsImpl();
    private final Set<BetCategories> processedCategories = new HashSet<>();

    public static ResultsBuilder CreateInstance() {
        return new ResultsBuilder();
    }

    private ResultsBuilder() {
    }

    public ProbabilityModelResults build() {
        return this.results;
    }

    // TODO methods to set Outcome and total goals probabilities
    // TODO processedCategories in private static class auslagern
}
