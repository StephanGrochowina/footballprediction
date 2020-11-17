package de.coiaf.random.distributions.enumerated.matchOutcome;

import de.coiaf.random.distributions.enumerated.EnumDistribution;
import de.coiaf.random.probability.NormalizerDistributionElementWeights;
import de.coiaf.random.probability.Probability;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * @author Stephan Grochowina <stephan.grochowina@web.de>
 */
public class MatchOutcomeByItemsSupplierDistribution extends EnumDistribution<Outcomes, Probability> {

    MatchOutcomeByItemsSupplierDistribution(Supplier<Collection<NormalizerDistributionElementWeights.Pair<Outcomes, Probability>>> itemsSupplier) {
        super(itemsSupplier.get(), Outcomes.class);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("MatchOutcomeByItemsSupplierDistribution{");

        sb.append("<null>=[");
        sb.append(this.getDensity(null));
        sb.append(", ");
        sb.append(this.getDistribution(null));
        sb.append(']');

        for (Outcomes outcome : Outcomes.values()) {
            sb.append(", ");
            sb.append(outcome.name());
            sb.append("=[");
            sb.append(this.getDensity(outcome));
            sb.append(", ");
            sb.append(this.getDistribution(outcome));
            sb.append(']');
        }

        sb.append(", mean=");
        sb.append(this.getMean());

        sb.append(", expectationValue=");
        sb.append(this.getExpectationValue().name());

        sb.append(", variance=");
        sb.append(this.getVariance());

        sb.append(", standardDeviation=");
        sb.append(this.getStandardDeviation());

        sb.append('}');

        return sb.toString();
    }
}
