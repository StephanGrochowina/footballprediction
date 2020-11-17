package de.coiaf.footballprediction.backend.model.probability.calculators;

import de.coiaf.footballprediction.backend.model.sharedcontext.EstimatedGoals;
import de.coiaf.footballprediction.backend.model.sharedcontext.EstimatedScore;
import de.coiaf.random.probability.Probability;

import java.util.function.Function;

public class TotalGoalsProbabilityCalculationContextFactory {

    public Function<Integer, Probability> createSimpleContext(Function<Integer, Probability> totalGoalsProbabilityFunction) {
        return new SimpleCalculationContext(totalGoalsProbabilityFunction);
    }

    public Function<Integer, Probability> createCachingContext(Function<Integer, Probability> totalGoalsProbabilityFunction, EstimatedScore expectedScore) {
        EstimatedGoals expectedTotalGoals = expectedScore == null ? null : expectedScore.getTotalGoals();

        return this.createCachingContext(totalGoalsProbabilityFunction, expectedTotalGoals);
    }

    public Function<Integer, Probability> createCachingContext(Function<Integer, Probability> totalGoalsProbabilityFunction, EstimatedGoals expectedTotalGoals) {
        return new CachingCalculationContext(totalGoalsProbabilityFunction, expectedTotalGoals);
    }
}
