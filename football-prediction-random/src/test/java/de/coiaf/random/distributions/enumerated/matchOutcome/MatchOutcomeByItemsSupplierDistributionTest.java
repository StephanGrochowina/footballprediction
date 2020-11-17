package de.coiaf.random.distributions.enumerated.matchOutcome;

import de.coiaf.random.distributions.enumerated.EnumDistribution;
import de.coiaf.random.probability.Probability;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Stephan Grochowina <stephan.grochowina@web.de>
 */
public class MatchOutcomeByItemsSupplierDistributionTest {

    @Ignore
    @Test
    public void testDistributionByExpectedGoals() {
        int range = 5;

        for (int i = 0; i <= range; i++) {
            for (int j = 0; j <= range; j++) {
                this.verifyDistributionByExpectedGoals(i == 0 ? 0.1 : i, j == 0 ? 0.1 : j);
            }
        }

    }
    private void verifyDistributionByExpectedGoals(double expectedHomeGoals, double expectedAwayGoals) {
        System.out.println("\nExpected: " + expectedHomeGoals + " : " + expectedAwayGoals);

        EnumDistribution<Outcomes, Probability> distribution =
                MatchOutcomeDistributionFactory.createDistribution(expectedHomeGoals, expectedAwayGoals);

        System.out.println(distribution.toString());
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    @Ignore
    @Test
    public void testDistributionByAverageGoals() {
        int range = 3;

        for (int homeFor = 0 ; homeFor <= range; homeFor++) {
            for (int homeAgainst = 0 ; homeAgainst <= range; homeAgainst++) {
                for (int awayFor = 0 ; awayFor <= range; awayFor++) {
                    for (int awayAgainst = 0 ; awayAgainst <= range; awayAgainst++) {
                        this.verifyDistributionByAverageGoals(homeFor, homeAgainst, awayFor, awayAgainst);
                    }
                }
            }
        }
    }
    private void verifyDistributionByAverageGoals(double averageHomeGoalsFor, double averageHomeGoalsAgainst, double averageAwayGoalsFor, double averageAwayGoalsAgainst) {
        System.out.println("\nAverage goals: " + averageHomeGoalsFor + " : " + averageHomeGoalsAgainst + " vs. " + averageAwayGoalsFor + " : " + averageAwayGoalsAgainst);

        EnumDistribution<Outcomes, Probability> distribution =
                MatchOutcomeDistributionFactory.createDistributionByAverageGoals(averageHomeGoalsFor, averageHomeGoalsAgainst, averageAwayGoalsFor, averageAwayGoalsAgainst);

        System.out.println(distribution.toString());
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------");
    }
}