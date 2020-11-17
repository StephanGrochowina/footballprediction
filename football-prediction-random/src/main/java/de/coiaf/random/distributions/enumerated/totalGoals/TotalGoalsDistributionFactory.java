package de.coiaf.random.distributions.enumerated.totalGoals;

import de.coiaf.random.distributions.enumerated.EnumDistribution;
import de.coiaf.random.probability.Probability;

public class TotalGoalsDistributionFactory {

    public static EnumDistribution<TotalGoals, Probability> createDistribution(
            double averageGoalsForHomeTeam, double averageGoalsAgainstHomeTeam, double averageGoalsForAwayTeam, double averageGoalsAgainstAwayTeam) {
        ItemsSupplierTotalGoals itemsSupplier = new ItemsSupplierTotalGoals();

        itemsSupplier.createItems(averageGoalsForHomeTeam, averageGoalsAgainstHomeTeam, averageGoalsForAwayTeam, averageGoalsAgainstAwayTeam);

        return new TotalGoalsByItemsSupplierDistribution(itemsSupplier);
    }

    public static EnumDistribution<TotalGoals, Probability> createDistribution(
            double expectedHomeGoals, double expectedAwayGoals) {
        ItemsSupplierTotalGoals itemsSupplier = new ItemsSupplierTotalGoals();

        itemsSupplier.createItems(expectedHomeGoals, expectedAwayGoals);

        return new TotalGoalsByItemsSupplierDistribution(itemsSupplier);
    }

    public static EnumDistribution<TotalGoals, Probability> createDistribution(
            double expectedTotalGoals) {
        ItemsSupplierTotalGoals itemsSupplier = new ItemsSupplierTotalGoals();

        itemsSupplier.createItems(expectedTotalGoals);

        return new TotalGoalsByItemsSupplierDistribution(itemsSupplier);
    }
}
