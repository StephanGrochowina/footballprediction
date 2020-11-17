package de.coiaf.random.distributions.enumerated;

import de.coiaf.random.probability.NormalizerDistributionElementWeights;

import java.util.Collection;

/**
 * @author Stephan Grochowina <stephan.grochowina@web.de>
 */
public class EnumDistribution<T extends Enum<T>, N extends Number> extends EnumeratedDistribution<T, N> {

    public EnumDistribution(Collection<NormalizerDistributionElementWeights.Pair<T, N>> items, Class<T> enumType) {
        super(items, enumType);
    }

    @Override
    protected Collection<NormalizerDistributionElementWeights.Pair<T, N>> initializeItems() {
        Collection<NormalizerDistributionElementWeights.Pair<T, N>> initializedItems = super.initializeItems();

        for (T enumValue : this.getType().getEnumConstants()) {
            initializedItems.add(NormalizerDistributionElementWeights.createInitializingPair(enumValue));
        }

        return initializedItems;
    }

    @Override
    protected void validateValue(T value) {
    }
}
