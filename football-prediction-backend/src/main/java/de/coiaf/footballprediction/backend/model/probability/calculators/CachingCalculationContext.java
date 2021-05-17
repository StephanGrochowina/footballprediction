package de.coiaf.footballprediction.backend.model.probability.calculators;

import de.coiaf.footballprediction.sharedkernal.domain.model.score.EstimatedGoals;
import de.coiaf.random.probability.Probability;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

class CachingCalculationContext implements Function<Integer, Probability> {

    private static final BigDecimal LOOP_TERMINATION_THRESHOLD_VALUE = new BigDecimal("0.0000000000001");
    static final Probability LOOP_TERMINATION_THRESHOLD = new Probability(LOOP_TERMINATION_THRESHOLD_VALUE);

    private final List<Probability> totalGoalsProbabilitesCache;

    static List<Probability> createCache(Function<Integer, Probability> totalGoalsProbabilityFunction, EstimatedGoals expectedTotalGoals) {
        Objects.requireNonNull(totalGoalsProbabilityFunction, "Parameter totalGoalsProbabilityFunction must not be null.");

        List<Probability> cache = new ArrayList<>();
        double threshold = determineThreshold(expectedTotalGoals);
        int index = 0;
        boolean continueLoop = true;

        while (continueLoop) {
            Probability currentIndexProbability = totalGoalsProbabilityFunction.apply(index);
            continueLoop = isIndexWithinThreshold(index, threshold) || isBigEnoughProbability(currentIndexProbability);

            if (continueLoop) {
//                System.out.print("Adding cache entry #");
//                System.out.print(index);
//                System.out.print(": ");
//                System.out.println(currentIndexProbability);
//
                cache.add(currentIndexProbability);
                index++;
            }
        }

        return cache;
    }

    static double determineThreshold(EstimatedGoals expectedTotalGoals) {
        return expectedTotalGoals == null ? 0.0 : expectedTotalGoals.doubleValue();
    }

    static boolean isIndexWithinThreshold(int index, double threshold) {
        return (double) index <= threshold;
    }

    static boolean isBigEnoughProbability(Probability probability) {
        return probability != null && LOOP_TERMINATION_THRESHOLD.compareTo(probability) <= 0;
    }

    CachingCalculationContext(Function<Integer, Probability> totalGoalsProbabilityFunction, EstimatedGoals expectedTotalGoals) {
        this.totalGoalsProbabilitesCache = createCache(totalGoalsProbabilityFunction, expectedTotalGoals);
    }

    @Override
    public Probability apply(Integer totalGoals) {
        if (totalGoals == null || totalGoals < 0 || totalGoals >= this.totalGoalsProbabilitesCache.size()) {
            return Probability.IMPOSSIBLE;
        }

        return this.totalGoalsProbabilitesCache.get(totalGoals);
    }
}
