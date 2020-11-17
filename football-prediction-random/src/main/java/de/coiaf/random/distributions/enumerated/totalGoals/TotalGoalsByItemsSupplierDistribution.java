package de.coiaf.random.distributions.enumerated.totalGoals;

import de.coiaf.random.distributions.enumerated.EnumDistribution;
import de.coiaf.random.probability.NormalizerDistributionElementWeights;
import de.coiaf.random.probability.Probability;

import java.util.Collection;
import java.util.function.Supplier;

public class TotalGoalsByItemsSupplierDistribution extends EnumDistribution<TotalGoals, Probability> {

    TotalGoalsByItemsSupplierDistribution(Supplier<Collection<NormalizerDistributionElementWeights.Pair<TotalGoals, Probability>>> itemsSupplier) {
        super(itemsSupplier.get(), TotalGoals.class);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TotalGoalsByItemsSupplierDistribution{");

        sb.append("<null>=[");
        sb.append(this.getDensity(null));
        sb.append(", ");
        sb.append(this.getDistribution(null));
        sb.append(']');

        for (TotalGoals threshold : TotalGoals.values()) {
            sb.append(", ");
            sb.append(threshold.name());
            sb.append("=[");
            sb.append(this.getDensity(threshold));
            sb.append(", ");
            sb.append(this.getDistribution(threshold));
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
