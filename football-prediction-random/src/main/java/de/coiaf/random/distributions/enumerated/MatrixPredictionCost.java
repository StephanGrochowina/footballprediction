package de.coiaf.random.distributions.enumerated;

import de.coiaf.random.probability.Probability;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Stephan Grochowina <stephan.grochowina@web.de>
 */
public class MatrixPredictionCost<T> {

    private final Map<T, CostDistribution<T>> costMatrix = new HashMap<>();
    private boolean evaluated = false;
    private final Function<Probability, BigDecimal> weightingFunction;

    public MatrixPredictionCost(Function<Probability, BigDecimal> weightingFunction) {
        this.weightingFunction = weightingFunction;
    }

    public void verify(T prediction, T outcome) {
        CostDistribution<T> costDistribution = this.determineCostDistribution(prediction, outcome);
        if (!costDistribution.isEvaluated()) {
            this.evaluated = false;
        }

        costDistribution.apply(outcome);
    }

    public Probability getDensity(T prediction, T outcome) {
        if (!this.evaluated) {
            this.evaluate(this.weightingFunction);
        }

        CostDistribution<T> costDistribution = this.costMatrix.get(prediction);
        OutcomeCost<T> cost = costDistribution == null ? null : costDistribution.costDistribution.get(outcome);

        return cost == null ? null : cost.density;
    }

    // TODO add Method to add probability density as weight for each pair (T prediction, T outcome)

    private CostDistribution<T> determineCostDistribution(T prediction, T outcome) {
        CostDistribution<T> costDistribution;

        if (this.costMatrix.containsKey(prediction)) {
            costDistribution = this.costMatrix.get(prediction);
        }
        else {
            costDistribution = new CostDistribution<>(prediction);
            this.costMatrix.put(prediction, costDistribution);
        }
        costDistribution.initializeOutcomeCost(outcome);

        return costDistribution;
    }

    private void evaluate(Function<Probability, BigDecimal> weightingFunction) {
        this.costMatrix.values().forEach(costDistribution -> {costDistribution.evaluate(weightingFunction);});
        this.evaluated = true;
    }

    private static class CostDistribution<T> {
        private final T prediction;
        private final Map<T, OutcomeCost<T>> costDistribution = new HashMap<>();
        private int sumOccurrences = 0;
        private boolean evaluated = false;

        private CostDistribution(T prediction) {
            this.prediction = prediction;
        }

        private void initializeOutcomeCost(T outcome) {
            if (!this.costDistribution.containsKey(outcome)) {
                OutcomeCost<T> cost = new OutcomeCost<>(outcome);

                this.costDistribution.put(outcome, cost);
                this.evaluated = false;
            }
        }

        private void apply(T outcome) {
            OutcomeCost<T> cost = this.costDistribution.get(outcome);

            assert cost != null;
            this.sumOccurrences++;
            cost.addOccurrence();
        }

        private void evaluate(Function<Probability, BigDecimal> weightingFunction) {
            Map<T, OutcomeCostForEvaluation<T>> evaluationContext = new HashMap<>();
            BigDecimal sumWeights = BigDecimal.ZERO;

            if (this.sumOccurrences > 0) {
                for (OutcomeCost<T> outcomeCost : this.costDistribution.values()) {
                    OutcomeCostForEvaluation<T> evaluatedOutcome = new OutcomeCostForEvaluation<>(outcomeCost.outcome, outcomeCost.occurrences, this.sumOccurrences);
                    evaluationContext.put(outcomeCost.outcome, evaluatedOutcome);

                    if (weightingFunction != null) {
                        BigDecimal weight = weightingFunction.apply(evaluatedOutcome.initialDensity);

                        if (BigDecimal.ZERO.compareTo(weight) > 0) {
                            throw new IllegalArgumentException("Weighting function must create non negative values.");
                        }

                        sumWeights = sumWeights.add(weight);
                        evaluatedOutcome.weight = weight;
                    }
                }

                for (OutcomeCostForEvaluation<T> evaluatedOutcome : evaluationContext.values()) {
                    OutcomeCost<T> outcomeCost = this.costDistribution.get(evaluatedOutcome.outcome);

                    if (sumWeights.compareTo(BigDecimal.ZERO) > 0) {
                        outcomeCost.density = Probability.createProbabilityByDivision(evaluatedOutcome.weight, sumWeights);
                    } else {
                        outcomeCost.density = evaluatedOutcome.initialDensity;
                    }
                }
            }
            this.evaluated = true;
        }

        private boolean isEvaluated() {
            return evaluated;
        }
    }

    private static class OutcomeCostForEvaluation<T> {
        private final T outcome;
        private BigDecimal weight = null;
        private final Probability initialDensity;

        public OutcomeCostForEvaluation(T outcome, int outcomeOccurrences, int totalOccurrences) {
            this.outcome = outcome;
            this.initialDensity = Probability.createProbabilityByDivision(BigDecimal.valueOf(outcomeOccurrences), BigDecimal.valueOf(totalOccurrences));
        }
    }

    private static class OutcomeCost<T> {
        private final T outcome;
        private int occurrences = 0;
        private Probability density = null;

        private OutcomeCost(T outcome) {
            this.outcome = outcome;
        }

        private void addOccurrence() {
            this.occurrences++;
        }
    }
}
