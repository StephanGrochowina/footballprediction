package de.coiaf.footballprediction.backend.model.probability.calculators;

import de.coiaf.random.probability.Probability;

@FunctionalInterface
public interface TotalGoalsProbabilityCalculator {

    Probability determineProbability(int totalGoals);
}
