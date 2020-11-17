package de.coiaf.random.distributions.enumerated;

import de.coiaf.random.probability.NormalizerDistributionElementWeights;

import java.util.Arrays;
import java.util.Collection;

public class EnumeratedDistributions {

    public static <T, N extends Number> Collection<T> selectByCertainty(NormalizerDistributionElementWeights.Pair<T, N>... values) {
        return selectByCertainty(Arrays.asList(values));
    }

    public static <T, N extends Number> Collection<T> selectByCertainty(Collection<NormalizerDistributionElementWeights.Pair<T, N>> values) {
        EnumeratedDistribution<T, N> distribution = new EnumeratedDistribution<>(values);

        return distribution.determineCertainElements();
    }
}
