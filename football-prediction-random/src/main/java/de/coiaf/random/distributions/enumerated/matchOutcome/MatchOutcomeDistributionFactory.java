package de.coiaf.random.distributions.enumerated.matchOutcome;

import de.coiaf.random.distributions.Distribution;
import de.coiaf.random.distributions.continuous.ContinuousDistribution;
import de.coiaf.random.distributions.discrete.DiscreteDistribution;
import de.coiaf.random.distributions.discrete.DiscreteDistributions;
import de.coiaf.random.distributions.enumerated.EnumDistribution;
import de.coiaf.random.distributions.enumerated.elo.Elo1x2Context;
import de.coiaf.random.distributions.enumerated.elo.Elo1x2ContextFactory;
import de.coiaf.random.probability.NormalizerDistributionElementWeights;
import de.coiaf.random.probability.Probability;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Stephan Grochowina <stephan.grochowina@web.de>
 */
public class MatchOutcomeDistributionFactory {

    private static final Elo1x2ContextFactory ELO_1_X_2_CONTEXT_FACTORY = new Elo1x2ContextFactory();
    private static final ItemsSupplierByAverageGoalsFactory ITEMS_SUPPLIER_BY_AVERAGE_GOALS_FACTORY = new ItemsSupplierByAverageGoalsFactory();

    public static EnumDistribution<Outcomes, Probability> createDistributionByAverageGoals(
            double averageGoalsForHomeTeam, double averageGoalsAgainstHomeTeam,
            double averageGoalsForAwayTeam, double averageGoalsAgainstAwayTeam) {
        ItemsSupplierByAverageGoals itemsSupplier = ITEMS_SUPPLIER_BY_AVERAGE_GOALS_FACTORY.createItemsSupplierByAverageGoals();

        return createDistributionByAverageGoals(
                averageGoalsForHomeTeam, averageGoalsAgainstHomeTeam,
                averageGoalsForAwayTeam, averageGoalsAgainstAwayTeam,
                itemsSupplier);
    }

    public static EnumDistribution<Outcomes, Probability> createDistributionByAverageGoals(
            double averageGoalsForHomeTeam, double averageGoalsAgainstHomeTeam,
            double averageGoalsForAwayTeam, double averageGoalsAgainstAwayTeam,
            double minimumAverageGoals) {
        ItemsSupplierByAverageGoals itemsSupplier = ITEMS_SUPPLIER_BY_AVERAGE_GOALS_FACTORY
                .createItemsSupplierByAverageGoals(minimumAverageGoals);

        return createDistributionByAverageGoals(
                averageGoalsForHomeTeam, averageGoalsAgainstHomeTeam,
                averageGoalsForAwayTeam, averageGoalsAgainstAwayTeam,
                itemsSupplier);
    }

    public static EnumDistribution<Outcomes, Probability> createDistributionByAverageGoals(
            double averageGoalsForHomeTeam, double averageGoalsAgainstHomeTeam,
            double averageGoalsForAwayTeam, double averageGoalsAgainstAwayTeam,
            Probability accuracy) {
        ItemsSupplierByAverageGoals itemsSupplier = ITEMS_SUPPLIER_BY_AVERAGE_GOALS_FACTORY
                .createItemsSupplierByAverageGoals(accuracy);

        return createDistributionByAverageGoals(
                averageGoalsForHomeTeam, averageGoalsAgainstHomeTeam,
                averageGoalsForAwayTeam, averageGoalsAgainstAwayTeam,
                itemsSupplier);
    }

    public static EnumDistribution<Outcomes, Probability> createDistributionByAverageGoals(
            double averageGoalsForHomeTeam, double averageGoalsAgainstHomeTeam,
            double averageGoalsForAwayTeam, double averageGoalsAgainstAwayTeam,
            double minimumAverageGoals, Probability accuracy) {
        ItemsSupplierByAverageGoals itemsSupplier = ITEMS_SUPPLIER_BY_AVERAGE_GOALS_FACTORY
                .createItemsSupplierByAverageGoals(minimumAverageGoals, accuracy);

        return createDistributionByAverageGoals(
                averageGoalsForHomeTeam, averageGoalsAgainstHomeTeam,
                averageGoalsForAwayTeam, averageGoalsAgainstAwayTeam,
                itemsSupplier);
    }

    private static EnumDistribution<Outcomes, Probability> createDistributionByAverageGoals(
            double averageGoalsForHomeTeam, double averageGoalsAgainstHomeTeam,
            double averageGoalsForAwayTeam, double averageGoalsAgainstAwayTeam,
            ItemsSupplierByAverageGoals itemsSupplier) {
        itemsSupplier.createItems(
                averageGoalsForHomeTeam, averageGoalsAgainstHomeTeam,
                averageGoalsForAwayTeam, averageGoalsAgainstAwayTeam);

        return new MatchOutcomeByItemsSupplierDistribution(itemsSupplier);
    }

    public static EnumDistribution<Outcomes, Probability> createDistributionByAverageGoals(
            double averageGoalsForHomeTeam, double averageGoalsAgainstHomeTeam, int homeMatchesPlayedByHomeTeam,
            double averageGoalsForAwayTeam, double averageGoalsAgainstAwayTeam, int awayMatchesPlayedByAwayTeam) {
        ItemsSupplierByAverageGoals itemsSupplier = ITEMS_SUPPLIER_BY_AVERAGE_GOALS_FACTORY.createItemsSupplierByAverageGoals();

        return createDistributionByAverageGoals(averageGoalsForHomeTeam, averageGoalsAgainstHomeTeam, homeMatchesPlayedByHomeTeam,
                averageGoalsForAwayTeam, averageGoalsAgainstAwayTeam, awayMatchesPlayedByAwayTeam,
                itemsSupplier);
    }

    public static EnumDistribution<Outcomes, Probability> createDistributionByAverageGoals(
            double averageGoalsForHomeTeam, double averageGoalsAgainstHomeTeam, int homeMatchesPlayedByHomeTeam,
            double averageGoalsForAwayTeam, double averageGoalsAgainstAwayTeam, int awayMatchesPlayedByAwayTeam,
            double minimumAverageGoals) {
        ItemsSupplierByAverageGoals itemsSupplier = ITEMS_SUPPLIER_BY_AVERAGE_GOALS_FACTORY
                .createItemsSupplierByAverageGoals(minimumAverageGoals);

        return createDistributionByAverageGoals(averageGoalsForHomeTeam, averageGoalsAgainstHomeTeam, homeMatchesPlayedByHomeTeam,
                averageGoalsForAwayTeam, averageGoalsAgainstAwayTeam, awayMatchesPlayedByAwayTeam,
                itemsSupplier);
    }

    public static EnumDistribution<Outcomes, Probability> createDistributionByAverageGoals(
            double averageGoalsForHomeTeam, double averageGoalsAgainstHomeTeam, int homeMatchesPlayedByHomeTeam,
            double averageGoalsForAwayTeam, double averageGoalsAgainstAwayTeam, int awayMatchesPlayedByAwayTeam,
            Probability accuracy) {
        ItemsSupplierByAverageGoals itemsSupplier = ITEMS_SUPPLIER_BY_AVERAGE_GOALS_FACTORY
                .createItemsSupplierByAverageGoals(accuracy);

        return createDistributionByAverageGoals(averageGoalsForHomeTeam, averageGoalsAgainstHomeTeam, homeMatchesPlayedByHomeTeam,
                averageGoalsForAwayTeam, averageGoalsAgainstAwayTeam, awayMatchesPlayedByAwayTeam,
                itemsSupplier);
    }

    public static EnumDistribution<Outcomes, Probability> createDistributionByAverageGoals(
            double averageGoalsForHomeTeam, double averageGoalsAgainstHomeTeam, int homeMatchesPlayedByHomeTeam,
            double averageGoalsForAwayTeam, double averageGoalsAgainstAwayTeam, int awayMatchesPlayedByAwayTeam,
            double minimumAverageGoals, Probability accuracy) {
        ItemsSupplierByAverageGoals itemsSupplier = ITEMS_SUPPLIER_BY_AVERAGE_GOALS_FACTORY
                .createItemsSupplierByAverageGoals(minimumAverageGoals, accuracy);

        return createDistributionByAverageGoals(averageGoalsForHomeTeam, averageGoalsAgainstHomeTeam, homeMatchesPlayedByHomeTeam,
                averageGoalsForAwayTeam, averageGoalsAgainstAwayTeam, awayMatchesPlayedByAwayTeam,
                itemsSupplier);
    }

    private static EnumDistribution<Outcomes, Probability> createDistributionByAverageGoals(
            double averageGoalsForHomeTeam, double averageGoalsAgainstHomeTeam, int homeMatchesPlayedByHomeTeam,
            double averageGoalsForAwayTeam, double averageGoalsAgainstAwayTeam, int awayMatchesPlayedByAwayTeam,
            ItemsSupplierByAverageGoals itemsSupplier) {

        itemsSupplier.createItems(
                averageGoalsForHomeTeam, averageGoalsAgainstHomeTeam, homeMatchesPlayedByHomeTeam,
                averageGoalsForAwayTeam, averageGoalsAgainstAwayTeam, awayMatchesPlayedByAwayTeam);

        return new MatchOutcomeByItemsSupplierDistribution(itemsSupplier);
    }

    public static EnumDistribution<Outcomes, Probability> createDistribution(
            double expectedHomeGoals, double expectedAwayGoals) {
        ItemsSupplierByDistributionModel itemsSupplier = new ItemsSupplierByDistributionModel();

        itemsSupplier.createItems(expectedHomeGoals, expectedAwayGoals);

        return new MatchOutcomeByItemsSupplierDistribution(itemsSupplier);
    }

    public static EnumDistribution<Outcomes, Probability> createDistribution(
            ContinuousDistribution homeGoalsDistribution, ContinuousDistribution awayGoalsDistribution) {
        Objects.requireNonNull(homeGoalsDistribution);
        Objects.requireNonNull(awayGoalsDistribution);

        ItemsSupplierByDistributionModel itemsSupplier = new ItemsSupplierByDistributionModel();

        itemsSupplier.createItems(homeGoalsDistribution, awayGoalsDistribution);

        return new MatchOutcomeByItemsSupplierDistribution(itemsSupplier);
    }

    public static EnumDistribution<Outcomes, Probability> createDistributionWithScoredTotalGoalsSubDistribution(
            DiscreteDistribution homeGoalsDistribution, DiscreteDistribution awayGoalsDistribution) {
        double expectedTotalGoals = determineExpectedTotalGoals(homeGoalsDistribution, awayGoalsDistribution);

        return createDistributionWithTotalGoalsSubDistribution(homeGoalsDistribution, awayGoalsDistribution, expectedTotalGoals);
    }

    public static EnumDistribution<Outcomes, Probability> createDistributionWithScoredTotalGoalsSubDistribution(
            ContinuousDistribution homeGoalsDistribution, ContinuousDistribution awayGoalsDistribution) {
        double expectedTotalGoals = determineExpectedTotalGoals(homeGoalsDistribution, awayGoalsDistribution);

        DiscreteDistribution homeGoalsDiscreteDistribution = DiscreteDistributions.createDiscreteDistribution(homeGoalsDistribution, false);
        DiscreteDistribution awayGoalsDiscreteDistribution = DiscreteDistributions.createDiscreteDistribution(awayGoalsDistribution, false);

        return createDistributionWithTotalGoalsSubDistribution(homeGoalsDiscreteDistribution, awayGoalsDiscreteDistribution, expectedTotalGoals);
    }

    private static EnumDistribution<Outcomes, Probability> createDistributionWithTotalGoalsSubDistribution(
            DiscreteDistribution homeGoalsDiscreteDistribution, DiscreteDistribution awayGoalsDiscreteDistribution, double expectedTotalGoals) {
        Function<Integer, EnumDistribution<Outcomes, Probability>> subdistributionCreator =
                scoredTotalGoals -> createSubDistribution(homeGoalsDiscreteDistribution, awayGoalsDiscreteDistribution, scoredTotalGoals);
        ItemsSupplierByDistributionModel itemsSupplier = new ItemsSupplierByDistributionModel();

        itemsSupplier.createItems(expectedTotalGoals, subdistributionCreator);

        return new MatchOutcomeByItemsSupplierDistribution(itemsSupplier);
    }

    private static double determineExpectedTotalGoals(Distribution<?> homeGoalsDistribution, Distribution<?> awayGoalsDistribution) {
        Objects.requireNonNull(homeGoalsDistribution);
        Objects.requireNonNull(awayGoalsDistribution);

        return homeGoalsDistribution.getMean() + awayGoalsDistribution.getMean();
    }

    private static EnumDistribution<Outcomes, Probability> createSubDistribution(
            DiscreteDistribution homeGoalsDiscreteDistribution,
            DiscreteDistribution awayGoalsDiscreteDistribution, int scoredTotalGoals) {
        Objects.requireNonNull(homeGoalsDiscreteDistribution);
        Objects.requireNonNull(awayGoalsDiscreteDistribution);
        if (scoredTotalGoals < 0) {
            throw new IllegalArgumentException("Parameter scoredTotalGoals must be positive.");
        }

        ItemsSupplierByScoredTotalGoalsDistributionModel itemsSupplier = new ItemsSupplierByScoredTotalGoalsDistributionModel();

        itemsSupplier.createItems(homeGoalsDiscreteDistribution, awayGoalsDiscreteDistribution, scoredTotalGoals);

        return new MatchOutcomeByItemsSupplierDistribution(itemsSupplier);
    }

    public static EnumDistribution<Outcomes, Probability> createDistributionFromElo(double eloHome, double eloAway) {
        return createDistributionFromElo(() -> ELO_1_X_2_CONTEXT_FACTORY.createContext(eloHome, eloAway));
    }

    public static EnumDistribution<Outcomes, Probability> createDistributionFromElo(double eloHome, double eloAway, double c, double d) {
        return createDistributionFromElo(() -> ELO_1_X_2_CONTEXT_FACTORY.createContext(eloHome, eloAway, c, d));
    }

    public static EnumDistribution<Outcomes, Probability> createDistributionFromElo(double eloHome, double eloAway, double c, double d, double homeAdvantage) {
        return createDistributionFromElo(() -> ELO_1_X_2_CONTEXT_FACTORY.createContext(eloHome, eloAway, c, d, homeAdvantage));
    }

    private static EnumDistribution<Outcomes, Probability> createDistributionFromElo(Supplier<Elo1x2Context> contextSupplier) {
        Objects.requireNonNull(contextSupplier, "Parameter keySupplier must not be null.");

        Elo1x2Context context = contextSupplier.get();

        return createDistribution(
                context == null ? null : context.getHomeWin(),
                context == null ? null : context.getDraw(),
                context == null ? null : context.getAwayWin()
        );
    }

    public static EnumDistribution<Outcomes, Probability> createDistribution(Probability homeWin, Probability draw, Probability awayWin) {
        Collection<NormalizerDistributionElementWeights.Pair<Outcomes, Probability>> items = new ArrayList<>();

        if (homeWin != null) {
            items.add(NormalizerDistributionElementWeights.createPair(Outcomes.HOME_WIN, homeWin));
        }
        if (draw != null) {
            items.add(NormalizerDistributionElementWeights.createPair(Outcomes.DRAW, draw));
        }
        if (awayWin != null) {
            items.add(NormalizerDistributionElementWeights.createPair(Outcomes.AWAY_WIN, awayWin));
        }

        return new MatchOutcomeByItemsSupplierDistribution(() -> items);
    }
}
