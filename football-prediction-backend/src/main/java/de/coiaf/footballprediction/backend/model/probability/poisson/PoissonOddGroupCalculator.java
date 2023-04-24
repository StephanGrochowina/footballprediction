package de.coiaf.footballprediction.backend.model.probability.poisson;

import de.coiaf.footballprediction.backend.model.sharedcontext.*;
import de.coiaf.footballprediction.sharedkernal.domain.model.prediction.EstimatedGoals;
import de.coiaf.footballprediction.sharedkernal.domain.model.prediction.EstimatedScore;
import de.coiaf.random.distributions.continuous.ContinuousDistribution;
import de.coiaf.random.distributions.discrete.DiscreteDistribution;
import de.coiaf.random.distributions.discrete.DiscreteDistributions;
import de.coiaf.random.distributions.enumerated.EnumDistribution;
import de.coiaf.random.distributions.enumerated.matchOutcome.MatchOutcomeDistributionFactory;
import de.coiaf.random.distributions.enumerated.matchOutcome.Outcomes;
import de.coiaf.random.probability.Probability;

import java.util.Objects;

public class PoissonOddGroupCalculator {

    /**
     * Creates an {@link OddGroupTotalGoals} instance for over/under bets depending on the {@code prediction}
     * to be expected in a match and the default threshold {@link ThresholdTotalGoals#getDefaultInstance()}
     * defining what number of goals are considered to be part of the under and the over bet.
     * @param score the estimated prediction
     * @return an {@link OddGroupTotalGoals} instance
     * @throws NullPointerException if {@code prediction} is null
     */
    public OddGroupTotalGoals createTotalGoalsOdds(EstimatedScore score) {
        return this.createTotalGoalsOdds(score.getTotalGoals());
    }

    /**
     * Creates an {@link OddGroupTotalGoals} instance for over/under bets depending on the {@code prediction}
     * to be expected in a match and the {@code threshold} defining what number of goals are considered to be
     * part of the under and the over bet.
     * @param score the estimated prediction
     * @param threshold the threshold for over/under probabilities
     * @return an {@link OddGroupTotalGoals} instance
     * @throws NullPointerException if {@code prediction} or {@code threshold} is null
     */
    public OddGroupTotalGoals createTotalGoalsOdds(EstimatedScore score, ThresholdTotalGoals threshold) {
        return this.createTotalGoalsOdds(score.getTotalGoals(), threshold);
    }

    /**
     * Creates an {@link OddGroupTotalGoals} instance for over/under bets depending on the {@code totalGoals}
     * to be expected in a match and the default threshold {@link ThresholdTotalGoals#getDefaultInstance()}
     * defining what number of goals are considered to be part of the under and the over bet.
     * @param totalGoals the total goals to be expected in a match
     * @return an {@link OddGroupTotalGoals} instance
     * @throws NullPointerException if {@code goals} is null
     */
    public OddGroupTotalGoals createTotalGoalsOdds(EstimatedGoals totalGoals) {
        return this.createTotalGoalsOdds(totalGoals, ThresholdTotalGoals.getDefaultInstance());
    }

    /**
     * Creates an {@link OddGroupTotalGoals} instance for over/under bets depending on the {@code totalGoals}
     * to be expected in a match and the {@code threshold} defining what number of goals are considered to be
     * part of the under and the over bet.
     * @param totalGoals the total goals to be expected in a match
     * @param threshold the threshold for over/under probabilities
     * @return an {@link OddGroupTotalGoals} instance
     * @throws NullPointerException if {@code goals} or {@code threshold} is null
     */
    public OddGroupTotalGoals createTotalGoalsOdds(EstimatedGoals totalGoals, ThresholdTotalGoals threshold) {
        Objects.requireNonNull(totalGoals, "Parameter totalGoals must not be null.");
        Objects.requireNonNull(threshold, "Parameter threshold must not be null.");

        DiscreteDistribution distribution = this.createTotalGoalsDistribution(totalGoals);
        int maxGoalsBelowThreshold = threshold.intValue();
        Probability probabilityBelowThreshold = distribution.getDistribution(maxGoalsBelowThreshold);
        Probability probabilityAboveThreshold = probabilityBelowThreshold.negate();

        return new OddGroupTotalGoals(probabilityBelowThreshold, probabilityAboveThreshold);
    }

    /**
     * Creates an {@link OddGroupOutcome} instance for outcome bets depending on the {@code prediction}.
     * @param score the estimated prediction
     * @return an {@link OddGroupOutcome} instance
     * @throws NullPointerException if {@code prediction} is null
     */
    public OddGroupOutcome createOutcomeOdds(EstimatedScore score) {
        Objects.requireNonNull(score, "Parameter prediction must not be null.");

        EnumDistribution<Outcomes, Probability> distribution = this.createOutcomeDistribution(score);
        Probability probabilityHomeWin = distribution.getDensity(Outcomes.HOME_WIN);
        Probability probabilityDraw = distribution.getDensity(Outcomes.DRAW);
        Probability probabilityAwayWin = distribution.getDensity(Outcomes.AWAY_WIN);

        return OddGroupOutcome.createInstance(probabilityHomeWin, probabilityDraw, probabilityAwayWin);
    }

    /**
     * Creates an {@link OddGroupOutcome} instance for outcome bets depending on the provided distributions.
     * @param homeGoalsDistribution the distribution of the goals scored by the home team
     * @param awayGoalsDistribution the distribution of the goals scored by the away team
     * @return an {@link OddGroupOutcome} instance
     * @throws NullPointerException if {@code homeGoalsDistribution} or {@code awayGoalsDistribution} is null
     */
    public OddGroupOutcome createOutcomeOdds(
            ContinuousDistribution homeGoalsDistribution, ContinuousDistribution awayGoalsDistribution) {
        Objects.requireNonNull(homeGoalsDistribution, "Parameter homeGoalsDistribution must not be null.");
        Objects.requireNonNull(awayGoalsDistribution, "Parameter awayGoalsDistribution must not be null.");

        EnumDistribution<Outcomes, Probability> distribution = this.createOutcomeDistribution(homeGoalsDistribution, awayGoalsDistribution);
        Probability probabilityHomeWin = distribution.getDensity(Outcomes.HOME_WIN);
        Probability probabilityDraw = distribution.getDensity(Outcomes.DRAW);
        Probability probabilityAwayWin = distribution.getDensity(Outcomes.AWAY_WIN);

        return OddGroupOutcome.createInstance(probabilityHomeWin, probabilityDraw, probabilityAwayWin);
    }

    private DiscreteDistribution createTotalGoalsDistribution(EstimatedGoals totalGoals) {
        return DiscreteDistributions.createPoissonDistribution(totalGoals.doubleValue());
    }

    private EnumDistribution<Outcomes, Probability> createOutcomeDistribution(EstimatedScore score) {
        return MatchOutcomeDistributionFactory.createDistribution(score.getHomeGoals().doubleValue(), score.getAwayGoals().doubleValue());
    }

    private EnumDistribution<Outcomes, Probability> createOutcomeDistribution(
            ContinuousDistribution homeGoalsDistribution, ContinuousDistribution awayGoalsDistribution) {
        return MatchOutcomeDistributionFactory.createDistributionWithScoredTotalGoalsSubDistribution(
                homeGoalsDistribution, awayGoalsDistribution);
    }
}
