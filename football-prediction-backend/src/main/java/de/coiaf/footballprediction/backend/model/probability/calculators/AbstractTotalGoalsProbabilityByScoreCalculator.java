package de.coiaf.footballprediction.backend.model.probability.calculators;

import de.coiaf.footballprediction.sharedkernal.domain.model.score.EstimatedGoals;
import de.coiaf.footballprediction.sharedkernal.domain.model.score.EstimatedScore;
import de.coiaf.random.probability.Probability;

public abstract class AbstractTotalGoalsProbabilityByScoreCalculator extends AbstractTotalGoalsProbabilityCalculator {

    protected AbstractTotalGoalsProbabilityByScoreCalculator() {
        super();
    }

    protected AbstractTotalGoalsProbabilityByScoreCalculator(EstimatedGoals expectedTotalGoals) {
        super(expectedTotalGoals);
    }

    protected AbstractTotalGoalsProbabilityByScoreCalculator(EstimatedScore expectedScore) {
        super(expectedScore);
    }

    protected final Probability calculateProbability(Integer totalGoals) {
        Probability result = Probability.IMPOSSIBLE;

        if (totalGoals == null) {
            return null;
        }

        for (int homeGoals = 0; homeGoals <= totalGoals; homeGoals++) {
            int awayGoals = totalGoals - homeGoals;

            result = result.add(this.calculateProbability(homeGoals, awayGoals));
        }

        return result;
    }

    protected abstract Probability calculateProbability(int goalsHome, int goalsAway);
}
