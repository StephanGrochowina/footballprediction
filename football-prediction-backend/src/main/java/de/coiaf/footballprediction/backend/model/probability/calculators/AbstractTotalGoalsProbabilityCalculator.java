package de.coiaf.footballprediction.backend.model.probability.calculators;

import de.coiaf.footballprediction.sharedkernal.domain.model.prediction.EstimatedGoals;
import de.coiaf.footballprediction.sharedkernal.domain.model.prediction.EstimatedScore;
import de.coiaf.random.probability.Probability;

import javax.inject.Inject;
import java.util.function.Function;

public abstract class AbstractTotalGoalsProbabilityCalculator implements TotalGoalsProbabilityCalculator {

    @Inject
    private TotalGoalsProbabilityCalculationContextFactory contextFactory;
    private final Function<Integer, Probability> context;

    protected AbstractTotalGoalsProbabilityCalculator() {
        this.context = this.contextFactory.createSimpleContext(this::calculateProbability);
    }

    protected AbstractTotalGoalsProbabilityCalculator(EstimatedGoals expectedTotalGoals) {
        this.context = this.contextFactory.createCachingContext(this::calculateProbability, expectedTotalGoals);
    }

    protected AbstractTotalGoalsProbabilityCalculator(EstimatedScore expectedScore) {
        this.context = this.contextFactory.createCachingContext(this::calculateProbability, expectedScore);
    }

    @Override
    public final Probability determineProbability(int totalGoals) {
        return this.context.apply(totalGoals);
    }

    protected abstract Probability calculateProbability(Integer totalGoals);
}
