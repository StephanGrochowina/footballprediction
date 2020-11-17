package de.coiaf.random.distributions.enumerated.matchOutcome;

import de.coiaf.random.distributions.discrete.DiscreteDistribution;
import de.coiaf.random.probability.NormalizerDistributionElementWeights;
import de.coiaf.random.probability.Probability;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Supplier;

public class ItemsSupplierByScoredTotalGoalsDistributionModel implements Supplier<Collection<NormalizerDistributionElementWeights.Pair<Outcomes, Probability>>> {

    private Collection<NormalizerDistributionElementWeights.Pair<Outcomes, Probability>> generatedItems = new ArrayList<>();

    void createItems(
            DiscreteDistribution homeGoalsDiscreteDistribution,
            DiscreteDistribution awayGoalsDiscreteDistribution,
            int scoredTotalGoals) {
        Objects.requireNonNull(homeGoalsDiscreteDistribution);
        Objects.requireNonNull(awayGoalsDiscreteDistribution);

        if (scoredTotalGoals < 0) {
            throw new IllegalArgumentException("Parameter scoredTotalGoals must be positive.");
        }

        Collection<NormalizerDistributionElementWeights.Pair<Outcomes, Probability>> items = new ArrayList<>();

        for (int scoredHomeGoals = 0; scoredHomeGoals <= scoredTotalGoals; scoredHomeGoals++) {
            int scoredAwayGoals = scoredTotalGoals - scoredHomeGoals;
            Probability scoreProbability = homeGoalsDiscreteDistribution.getDensity(scoredHomeGoals)
                    .multiply(awayGoalsDiscreteDistribution.getDensity(scoredAwayGoals));

            if (scoredHomeGoals < scoredAwayGoals) {
                this.addItem(Outcomes.AWAY_WIN, scoreProbability, items);
            }
            else if (scoredHomeGoals > scoredAwayGoals) {
                this.addItem(Outcomes.HOME_WIN, scoreProbability, items);
            }
            else  {
                this.addItem(Outcomes.DRAW, scoreProbability, items);
            }
        }

        this.generatedItems = items;
    }

    private void addItem(Outcomes outcome, Probability probability,
                                Collection<NormalizerDistributionElementWeights.Pair<Outcomes, Probability>> items) {
        NormalizerDistributionElementWeights.Pair<Outcomes, Probability> pair =
                NormalizerDistributionElementWeights.createPair(outcome, probability);

        items.add(pair);
    }

    @Override
    public Collection<NormalizerDistributionElementWeights.Pair<Outcomes, Probability>> get() {
        return this.generatedItems;
    }
}
