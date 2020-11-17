package de.coiaf.random.distributions.enumerated.matchOutcome;

import de.coiaf.random.distributions.continuous.ContinuousDistributionFunctions;
import de.coiaf.random.distributions.continuous.MeanBasedContinuousDistributionFunction;
import de.coiaf.random.distributions.common.Variance;
import de.coiaf.random.distributions.enumerated.goalEstimation.KappaDistributionBuilder;
import de.coiaf.random.distributions.enumerated.goalEstimation.KappaMeanEstimationBuilder;
import de.coiaf.random.probability.Probability;

/**
 * @author Stephan Grochowina <stephan.grochowina@web.de>
 */
class ItemsSupplierByAverageGoals extends AbstractItemsSupplierDependingOnDistributionModel {
    private static final double DEFAULT_MINIMUM_AVERAGE_GOALS = 0.05;
    private static final MeanBasedContinuousDistributionFunction DISTRIBUTION_FUNCTION = ContinuousDistributionFunctions.getGaussianDistributionFunction();

    private final double minimumAverageGoals;

    ItemsSupplierByAverageGoals() {
        this(DEFAULT_MINIMUM_AVERAGE_GOALS, ItemsSupplierByAverageGoals.DEFAULT_ACCURACY);
    }
    ItemsSupplierByAverageGoals(double minimumAverageGoals) {
        this(minimumAverageGoals, ItemsSupplierByAverageGoals.DEFAULT_ACCURACY);
    }
    ItemsSupplierByAverageGoals(Probability accuracy) {
        this(DEFAULT_MINIMUM_AVERAGE_GOALS, accuracy);
    }
    ItemsSupplierByAverageGoals(double minimumAverageGoals, Probability accuracy) {
        super(accuracy);

        this.minimumAverageGoals = minimumAverageGoals;
    }

    void createItems(double averageGoalsForHomeTeam, double averageGoalsAgainstHomeTeam, double averageGoalsForAwayTeam, double averageGoalsAgainstAwayTeam) {
        KappaMeanEstimationBuilder meanBuilder = KappaMeanEstimationBuilder.createBuilder()
                .applyLowerBoundGoals(this.minimumAverageGoals)
                .applyHomeScore(averageGoalsForHomeTeam, averageGoalsAgainstHomeTeam)
                .applyAwayScore(averageGoalsForAwayTeam, averageGoalsAgainstAwayTeam);

        this.generateItems(
                meanBuilder.buildHomeMeanEstimation(),
                meanBuilder.buildAwayMeanEstimation()
        );
    }

    void createItems(
            double averageGoalsForHomeTeam, double averageGoalsAgainstHomeTeam, int homeMatchesPlayedByHomeTeam,
            double averageGoalsForAwayTeam, double averageGoalsAgainstAwayTeam, int awayMatchesPlayedByAwayTeam) {
        KappaDistributionBuilder distributionBuilder = KappaDistributionBuilder.createBuilder()
                .applyLowerBoundGoals(this.minimumAverageGoals)
                .applyHomeScore(averageGoalsForHomeTeam, averageGoalsAgainstHomeTeam)
                .applyAwayScore(averageGoalsForAwayTeam, averageGoalsAgainstAwayTeam)
                .applyHomeVariance(Variance.NO_VARIANCE, homeMatchesPlayedByHomeTeam)
                .applyAwayVariance(Variance.NO_VARIANCE, awayMatchesPlayedByAwayTeam);

        this.generateItems(
                distributionBuilder.buildHomeDistribution(DISTRIBUTION_FUNCTION),
                distributionBuilder.buildAwayDistribution(DISTRIBUTION_FUNCTION)
        );
    }

}
