package de.coiaf.random.distributions.enumerated.matchOutcome;

import de.coiaf.random.distributions.continuous.ContinuousDistribution;
import de.coiaf.random.distributions.discrete.DiscreteDistribution;
import de.coiaf.random.distributions.discrete.DiscreteDistributions;
import de.coiaf.random.distributions.enumerated.EnumDistribution;
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
public class ItemsSupplierByDistributionModel implements Supplier<Collection<NormalizerDistributionElementWeights.Pair<Outcomes, Probability>>> {
    private static final Probability DEFAULT_ACCURACY = new Probability(0.9999999999);

    private final Probability accuracy;
    private Collection<NormalizerDistributionElementWeights.Pair<Outcomes, Probability>> generatedItems = new ArrayList<>();

    ItemsSupplierByDistributionModel() {
        this(DEFAULT_ACCURACY);
    }

    ItemsSupplierByDistributionModel(Probability accuracy) {

        this.validateAccuracy(accuracy);

        this.accuracy = accuracy;
    }

    void createItems(double expectedHomeGoals, double expectedAwayGoals) {
        double expectedMatchGoals = this.createExpectedMatchGoals(expectedHomeGoals, expectedAwayGoals);
        Probability pHomeTeamScores = new Probability(expectedHomeGoals / expectedMatchGoals);

        this.createItems(expectedMatchGoals, pHomeTeamScores);
    }
    void createItems(ContinuousDistribution homeGoalsDistribution, ContinuousDistribution awayGoalsDistribution) {
        Objects.requireNonNull(homeGoalsDistribution);
        Objects.requireNonNull(awayGoalsDistribution);

        double expectedMatchGoals = this.createExpectedMatchGoals(homeGoalsDistribution.getExpectationValue(), awayGoalsDistribution.getExpectationValue());
        double cumulativeProbabilityHomeGoals = homeGoalsDistribution.getDistribution(expectedMatchGoals).doubleValue();
        double cumulativeProbabilityAwayGoals = awayGoalsDistribution.getDistribution(expectedMatchGoals).doubleValue();
        Probability pHomeTeamScores = new Probability(cumulativeProbabilityHomeGoals / (cumulativeProbabilityHomeGoals + cumulativeProbabilityAwayGoals));

        this.createItems(expectedMatchGoals, pHomeTeamScores);
    }
    private void createItems(double expectedMatchGoals, Probability pHomeTeamScores) {
        Objects.requireNonNull(pHomeTeamScores);

        DiscreteDistribution poissonGoalsInMatch = DiscreteDistributions.createPoissonDistribution(expectedMatchGoals);
        Collection<NormalizerDistributionElementWeights.Pair<Outcomes, Probability>> items = new ArrayList<>();
        int k = 0;
        Probability currentDistribution = Probability.IMPOSSIBLE;

        while (((double) k) <= expectedMatchGoals || accuracy.compareTo(currentDistribution) > 0) {
            int indexEven = 2 * k;
            int indexOdd = 2 * k - 1;
            DiscreteDistribution binomialEvenGoals = DiscreteDistributions.createBinomialDistribution(indexEven, pHomeTeamScores);
            Probability pPoissonEvenGoals = poissonGoalsInMatch.getDensity(indexEven);

            currentDistribution = currentDistribution.add(this.addItem(Outcomes.DRAW,
                    binomialEvenGoals.getDensity(k).multiply(pPoissonEvenGoals),
                    items));

            if (k > 0) {
                DiscreteDistribution binomialOddGoals = DiscreteDistributions.createBinomialDistribution(indexOdd, pHomeTeamScores);
                Probability pPoissonOddGoals = poissonGoalsInMatch.getDensity(indexOdd);

                currentDistribution = currentDistribution.add(this.addItem(Outcomes.HOME_WIN,
                        binomialEvenGoals.getDistribution(k).negate().multiply(pPoissonEvenGoals),
                        items));

                currentDistribution = currentDistribution.add(this.addItem(Outcomes.AWAY_WIN,
                        binomialEvenGoals.getDistribution(k - 1).multiply(pPoissonEvenGoals),
                        items));

                currentDistribution = currentDistribution.add(this.addItem(Outcomes.HOME_WIN,
                        binomialOddGoals.getDistribution(k - 1).negate().multiply(pPoissonOddGoals),
                        items));

                currentDistribution = currentDistribution.add(this.addItem(Outcomes.AWAY_WIN,
                        binomialOddGoals.getDistribution(k - 1).multiply(pPoissonOddGoals),
                        items));

            }

            k++;
        }

        this.generatedItems = items;
    }

    void createItems(double expectedMatchGoals, Function<Integer, EnumDistribution<Outcomes, Probability>> subdistributionCreator) {
        Objects.requireNonNull(subdistributionCreator, "Parameter subdistributionCreator must not be null.");
        this.validateExpectedGoals(expectedMatchGoals);

        DiscreteDistribution poissonGoalsInMatch = DiscreteDistributions.createPoissonDistribution(expectedMatchGoals);
        Collection<NormalizerDistributionElementWeights.Pair<Outcomes, Probability>> items = new ArrayList<>();
        int scoredTotalGoals = 0;
        Probability currentDistribution = Probability.IMPOSSIBLE;

        while (((double) scoredTotalGoals) <= expectedMatchGoals || accuracy.compareTo(currentDistribution) > 0) {
            EnumDistribution<Outcomes, Probability> subdistribution = subdistributionCreator.apply(scoredTotalGoals);
            Probability pPoisson = poissonGoalsInMatch.getDensity(scoredTotalGoals);
            Probability pHomeWinForScoredTotalGoals = subdistribution.getDensity(Outcomes.HOME_WIN);
            Probability pDrawForScoredTotalGoals = subdistribution.getDensity(Outcomes.DRAW);
            Probability pAwayWinForScoredTotalGoals = subdistribution.getDensity(Outcomes.AWAY_WIN);

            currentDistribution = currentDistribution.add(this.addItem(Outcomes.HOME_WIN,
                    pPoisson.multiply(pHomeWinForScoredTotalGoals),
                    items));

            currentDistribution = currentDistribution.add(this.addItem(Outcomes.DRAW,
                    pPoisson.multiply(pDrawForScoredTotalGoals),
                    items));

            currentDistribution = currentDistribution.add(this.addItem(Outcomes.AWAY_WIN,
                    pPoisson.multiply(pAwayWinForScoredTotalGoals),
                    items));

            scoredTotalGoals++;
        }

    }

    private Probability addItem(Outcomes outcome, Probability probability,
                         Collection<NormalizerDistributionElementWeights.Pair<Outcomes, Probability>> items) {
        NormalizerDistributionElementWeights.Pair<Outcomes, Probability> pair =
                NormalizerDistributionElementWeights.createPair(outcome, probability);

        items.add(pair);

        return probability;
    }

    private double createExpectedMatchGoals(double expectedHomeGoals, double expectedAwayGoals) {
        this.validateExpectedGoals(expectedHomeGoals);
        this.validateExpectedGoals(expectedAwayGoals);

        return expectedHomeGoals + expectedAwayGoals;
    }

    private void validateAccuracy(Probability accuracy) {

        if (accuracy == null) {
            throw new NullPointerException();
        }
        if (Probability.isImpossible(accuracy)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateExpectedGoals(double expectedGoals) {
        if (expectedGoals <= 0.0) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Collection<NormalizerDistributionElementWeights.Pair<Outcomes, Probability>> get() {
        return this.generatedItems;
    }
}
