package de.coiaf.random.distributions.enumerated.totalGoals;

import de.coiaf.random.distributions.discrete.DiscreteDistribution;
import de.coiaf.random.distributions.discrete.DiscreteDistributions;
import de.coiaf.random.distributions.enumerated.goalEstimation.KappaMeanEstimationBuilder;
import de.coiaf.random.probability.NormalizerDistributionElementWeights;
import de.coiaf.random.probability.Probability;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class ItemsSupplierTotalGoals implements Supplier<Collection<NormalizerDistributionElementWeights.Pair<TotalGoals, Probability>>> {
    private static final double DEFAULT_MINIMUM_AVERAGE_GOALS = 0.05;

    private final double minimumAverageGoals;
    private Collection<NormalizerDistributionElementWeights.Pair<TotalGoals, Probability>> generatedItems = new ArrayList<>();

    ItemsSupplierTotalGoals() {
        this(DEFAULT_MINIMUM_AVERAGE_GOALS);
    }
    ItemsSupplierTotalGoals(double minimumAverageGoals) {
        this.minimumAverageGoals = minimumAverageGoals;
    }

    @Override
    public Collection<NormalizerDistributionElementWeights.Pair<TotalGoals, Probability>> get() {
        return this.generatedItems;
    }

    void createItems(double averageGoalsForHomeTeam, double averageGoalsAgainstHomeTeam, double averageGoalsForAwayTeam, double averageGoalsAgainstAwayTeam) {
        KappaMeanEstimationBuilder meanBuilder = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(this.minimumAverageGoals)
                .applyHomeScore(averageGoalsForHomeTeam, averageGoalsAgainstHomeTeam)
                .applyAwayScore(averageGoalsForAwayTeam, averageGoalsAgainstAwayTeam);

        this.createItems(
                meanBuilder.buildHomeMeanEstimation(),
                meanBuilder.buildAwayMeanEstimation()
        );
    }
    void createItems(double expectedHomeGoals, double expectedAwayGoals) {
        this.createItems(expectedHomeGoals + expectedAwayGoals);
    }
    void createItems(double expectedTotalGoals) {
        DiscreteDistribution poissonGoalsInMatch = DiscreteDistributions.createPoissonDistribution(expectedTotalGoals);
        Collection<NormalizerDistributionElementWeights.Pair<TotalGoals, Probability>> items = new ArrayList<>();
        Probability pWithinThreshold = poissonGoalsInMatch.getDistribution((int) Math.floor(expectedTotalGoals));

        items.add(
                NormalizerDistributionElementWeights.createPair(TotalGoals.BELOW_OR_EQUAL_TO_THRESHOLD, pWithinThreshold)
        );
        items.add(
                NormalizerDistributionElementWeights.createPair(TotalGoals.ABOVE_THRESHOLD, pWithinThreshold.negate())
        );

        this.generatedItems = items;
    }
}
