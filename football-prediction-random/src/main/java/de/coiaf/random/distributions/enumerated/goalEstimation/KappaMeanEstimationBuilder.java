package de.coiaf.random.distributions.enumerated.goalEstimation;

import java.util.Objects;
import java.util.function.Supplier;

public class KappaMeanEstimationBuilder {

    private Number homeGoalsScored = 0.0;
    private Number homeGoalsConceded = 0.0;
    private Number awayGoalsScored = 0.0;
    private Number awayGoalsConceded = 0.0;
    private Double lowerBoundInclusiveGoals = null;

    public static KappaMeanEstimationBuilder createBuilder() {
        return new KappaMeanEstimationBuilder();
    }

    private static double buildMeanEstimation(Supplier<Number> scored, Supplier<Number> conceded, Double lowerBoundInclusiveGoals) {
        Objects.requireNonNull(scored, "Parameter scored must not be null.");
        Objects.requireNonNull(conceded, "Parameter conceded must not be null.");

        double result;

        if (lowerBoundInclusiveGoals == null) {
            result = KappaMeanEstimationFunction.calculateMean(scored.get(), conceded.get());
        }
        else {
            result = KappaMeanEstimationFunction.calculateMean(scored.get(), conceded.get(), lowerBoundInclusiveGoals);
        }

        return result;
    }

    private KappaMeanEstimationBuilder() {}

    public KappaMeanEstimationBuilder applyHomeScore(Number scored, Number conceded) {
        Objects.requireNonNull(scored, "Parameter scored must not be null.");
        Objects.requireNonNull(conceded, "Parameter conceded must not be null.");

        this.homeGoalsScored = scored;
        this.homeGoalsConceded = conceded;

        return this;
    }

    public KappaMeanEstimationBuilder applyAwayScore(Number scored, Number conceded) {
        Objects.requireNonNull(scored, "Parameter scored must not be null.");
        Objects.requireNonNull(conceded, "Parameter conceded must not be null.");

        this.awayGoalsScored = scored;
        this.awayGoalsConceded = conceded;

        return this;
    }

    public KappaMeanEstimationBuilder applyLowerBoundGoals(Double lowerBoundInclusiveGoals) {
        if (lowerBoundInclusiveGoals != null) {
            KappaGoals.validateLowerBoundInclusiveGoals(lowerBoundInclusiveGoals);
        }

        this.lowerBoundInclusiveGoals = lowerBoundInclusiveGoals;

        return this;
    }

    public double buildHomeMeanEstimation() {
        return buildMeanEstimation(() -> this.homeGoalsScored, () -> this.awayGoalsConceded, this.lowerBoundInclusiveGoals);
    }

    public double buildAwayMeanEstimation() {
        return buildMeanEstimation(() -> this.awayGoalsScored, () -> this.homeGoalsConceded, this.lowerBoundInclusiveGoals);
    }
}
