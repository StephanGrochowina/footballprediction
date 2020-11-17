package de.coiaf.random.distributions.enumerated.matchOutcome;

import de.coiaf.random.distributions.continuous.ContinuousDistribution;
import de.coiaf.random.probability.NormalizerDistributionElementWeights;
import de.coiaf.random.probability.Probability;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

/**
 * @author Stephan Grochowina <stephan.grochowina@web.de>
 */
abstract class AbstractItemsSupplierDependingOnDistributionModel implements Supplier<Collection<NormalizerDistributionElementWeights.Pair<Outcomes, Probability>>> {

    protected final static Probability DEFAULT_ACCURACY = null;

    private Collection<NormalizerDistributionElementWeights.Pair<Outcomes, Probability>> generatedItems = new ArrayList<>();
    private final Probability accuracy;

    protected AbstractItemsSupplierDependingOnDistributionModel() {
        this(DEFAULT_ACCURACY);
    }
    protected AbstractItemsSupplierDependingOnDistributionModel(Probability accuracy) {
        this.accuracy = accuracy;
    }

    protected void generateItems(double expectedHomeGoals, double expectedAwayGoals) {
        ItemsSupplierByDistributionModel distributionModel = this.createDistributionModel(this.accuracy);

        distributionModel.createItems(expectedHomeGoals, expectedAwayGoals);
        this.generatedItems = distributionModel.get();
    }

    protected void generateItems(ContinuousDistribution homeGoalsDistribution, ContinuousDistribution awayGoalsDistribution) {
        ItemsSupplierByDistributionModel distributionModel = this.createDistributionModel(this.accuracy);

        distributionModel.createItems(homeGoalsDistribution, awayGoalsDistribution);
        this.generatedItems = distributionModel.get();
    }

    ItemsSupplierByDistributionModel createDistributionModel(Probability accuracy) {
        return accuracy == null ? new ItemsSupplierByDistributionModel() : new ItemsSupplierByDistributionModel(accuracy);
    }

    @Override
    public Collection<NormalizerDistributionElementWeights.Pair<Outcomes, Probability>> get() {
        return this.generatedItems;
    }
}
