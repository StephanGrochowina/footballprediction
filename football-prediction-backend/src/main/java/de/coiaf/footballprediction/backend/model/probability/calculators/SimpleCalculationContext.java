package de.coiaf.footballprediction.backend.model.probability.calculators;

import de.coiaf.random.probability.Probability;

import java.util.Objects;
import java.util.function.Function;

class SimpleCalculationContext implements Function<Integer, Probability> {

    private final Function<Integer, Probability> totalGoalsProbabilityFunction;

    SimpleCalculationContext(Function<Integer, Probability> totalGoalsProbabilityFunction) {
        Objects.requireNonNull(totalGoalsProbabilityFunction, "Parameter totalGoalsProbabilityFunction must not be null.");

        this.totalGoalsProbabilityFunction = totalGoalsProbabilityFunction;
    }

    @Override
    public Probability apply(Integer totalGoals) {

        if (totalGoals == null || totalGoals < 0) {
            return Probability.IMPOSSIBLE;
        }

        return this.totalGoalsProbabilityFunction.apply(totalGoals);
    }
}
