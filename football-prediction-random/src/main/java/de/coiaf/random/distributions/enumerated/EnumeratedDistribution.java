package de.coiaf.random.distributions.enumerated;

import de.coiaf.random.distributions.AbstractDistributionAdapter;
import de.coiaf.random.probability.Certainty;
import de.coiaf.random.probability.NormalizerDistributionElementWeights;
import de.coiaf.random.probability.Probability;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

import java.util.*;

/**
 * @author Stephan Grochowina <stephan.grochowina@web.de>
 */
public class EnumeratedDistribution<T, N extends Number> extends AbstractDistributionAdapter<T> {

    private final Map<T, DistributionEntry<T>> entries = new LinkedHashMap<>();
    private final Collection<NormalizerDistributionElementWeights.Pair<T, N>> items;
    private final Double mean;
    private final T expectationValue;
    private final Double variance;
    private final Double standardDeviation;
    private final Class<T> type;
    private final Probability certainty;

    public EnumeratedDistribution(Collection<NormalizerDistributionElementWeights.Pair<T, N>> items) {
        this(items, null);
    }
    protected EnumeratedDistribution(Collection<NormalizerDistributionElementWeights.Pair<T, N>> items, Class<T> type) {
        this.items = items;
        this.type = type;

        List<DistributionEntry<T>> indexedEntries = this.createDistribution();
        double calculatedMean = this.calculateMean(indexedEntries);
        double calculatedVariance = this.calculateVariance(calculatedMean, indexedEntries);

        this.mean = calculatedMean;
        this.expectationValue = this.determineExpectationValue(calculatedMean, indexedEntries);
        this.variance = calculatedVariance;
        this.standardDeviation = Math.sqrt(calculatedVariance);
        this.certainty = Certainty.determineUncertainty(items);
    }

    public Collection<T> determineCertainElements() {
        return this.determineCertainElements(this.certainty);
    }
    public Collection<T> determineCertainElements(Probability certainty) {
        Probability appliedCertainty = certainty == null ? Probability.IMPOSSIBLE : certainty;
        Collection<T> result = new ArrayList<>();

        this.entries.values().forEach(
                distributionEntry ->  {
                    if (appliedCertainty.compareTo(distributionEntry.getDensity()) <= 0) {
                        result.add(distributionEntry.getRepresentationValue());
                    }
                }
        );

        return result;
    }

    @Override
    public T convertIndex(int index) {
        if (index < 0 || index >= this.entries.size()) {
            throw new IndexOutOfBoundsException("index must be a non negative value below " + this.entries.size());
        }

        int currentIndex = 0;
        T foundValue = null;
        Iterator<Map.Entry<T, DistributionEntry<T>>> iter = this.entries.entrySet().iterator();

        while (foundValue == null && iter.hasNext()) {
            Map.Entry<T, DistributionEntry<T>> currentEntry = iter.next();

            if (currentIndex == index) {
                foundValue = currentEntry.getKey();
            }

            currentIndex++;
        }

        return foundValue;
    }

// TODO add method to create EnumeratedDistribution<T, N extends Number> from this distribution applying a MatrixPredictionCost instance.

    private List<DistributionEntry<T>> createDistribution() {
        NormalizerDistributionElementWeights<T, N> normalizer = this.createNormalizer();
        Collection<NormalizerDistributionElementWeights.Pair<T, N>> initializedItems = this.initializeItems();
        List<DistributionEntry<T>> indexedEntries = new ArrayList<>();

        initializedItems.addAll(this.items);
        normalizer.normalize(initializedItems, (normalizedEntry -> {
            DistributionEntry<T> entry = new DistributionEntry<>(
                    normalizedEntry.getRepresentationValue(), normalizedEntry.getWeight().doubleValue(), normalizedEntry.getDensity(), normalizedEntry.getDistribution());

            this.entries.put(entry.getRepresentationValue(), entry);
            indexedEntries.add(entry);
        }));

        return indexedEntries;
    }

    private double calculateMean(List<DistributionEntry<T>> indexedEntries) {
        int index = 0;
        int limit = indexedEntries.size();
        double calculatedMean = 0.0;

        while (index < limit) {
            DistributionEntry<T> entry = indexedEntries.get(index);

            if (entry != null && !Probability.isImpossible(entry.getDensity())) {
                calculatedMean += ((double) index) * entry.getDensity().doubleValue();
            }

            index++;
        }

        return calculatedMean;
    }

    private T determineExpectationValue(double mean, List<DistributionEntry<T>> indexedEntries) {
        int index = (int) Math.round(mean);

        return index < 0 || index >= indexedEntries.size() ? null : indexedEntries.get(index).getRepresentationValue();
    }

    private double calculateVariance(double mean, List<DistributionEntry<T>> indexedEntries) {
        int index = 0;
        int limit = indexedEntries.size();
        double calculatedVariance = 0.0;

        while (index < limit) {
            DistributionEntry<T> entry = indexedEntries.get(index);

            if (entry != null && !Probability.isImpossible(entry.getDensity())) {
                calculatedVariance += Math.pow(((double) index) - mean, 2.0) * entry.getDensity().doubleValue();
            }

            index++;
        }

        return calculatedVariance;
    }

    private NormalizerDistributionElementWeights<T, N> createNormalizer() {
        return new NormalizerDistributionElementWeights<>();
    }

    protected Collection<NormalizerDistributionElementWeights.Pair<T, N>> initializeItems() {
        return new ArrayList<>();
    }

    @Override
    protected double calculateDensity(T value) {
        DistributionEntry<T> entry = this.entries.get(value);
        Probability density = entry == null ? Probability.IMPOSSIBLE : entry.getDensity();

        return density.doubleValue();
    }

    @Override
    protected double calculateDistribution(T value) {
        DistributionEntry<T> entry = this.entries.get(value);
        Probability distribution = entry == null ? Probability.IMPOSSIBLE : entry.getDistribution();

        return distribution.doubleValue();
    }

    @Override
    protected double calculateDistribution(T value0, T value1) {
        DistributionEntry<T> entry0 = this.entries.get(value0);
        Probability distribution0 = entry0 == null ? Probability.IMPOSSIBLE : entry0.getDistribution();
        DistributionEntry<T> entry1 = this.entries.get(value1);
        Probability distribution1 = entry1 == null ? Probability.IMPOSSIBLE : entry1.getDistribution();

        if (distribution0.compareTo(distribution1) > 0) {
            throw new NumberIsTooLargeException(distribution0, distribution1, true);
        }

        return distribution1.subtract(distribution0).doubleValue();
    }

    @Override
    protected T determineValue(double p) {
        Optional<DistributionEntry<T>> result = this.entries.values().stream()
                .filter(entry -> entry != null && entry.getDistribution().doubleValue() >= p)
                .findFirst();
        return result.isPresent() ? result.get().getRepresentationValue() : null;
    }

    @Override
    public Double getMean() {
        return this.mean;
    }

    @Override
    public T getExpectationValue() {
        return this.expectationValue;
    }

    @Override
    public Double getVariance() {
        return this.variance;
    }

    @Override
    public Double getStandardDeviation() {
        return this.standardDeviation;
    }

    protected Class<T> getType() {
        return this.type;
    }

    public Probability getCertainty() {
        return this.certainty;
    }

    public Collection<NormalizerDistributionElementWeights.Pair<T, N>> getItems() {
        return this.items;
    }

    private static class DistributionEntry<T> {
        private final T representationValue;
        private final double weight;
        private final Probability density;
        private final Probability distribution;

        private DistributionEntry(T representationValue, double weight, Probability density, Probability distribution) {
            this.representationValue = representationValue;
            this.weight = weight;
            this.density = density == null ? Probability.IMPOSSIBLE : density;
            this.distribution = distribution == null ? Probability.IMPOSSIBLE : distribution;
        }

        public T getRepresentationValue() {
            return this.representationValue;
        }

        public double getWeight() {
            return this.weight;
        }

        public Probability getDensity() {
            return this.density;
        }

        public Probability getDistribution() {
            return this.distribution;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DistributionEntry<?> that = (DistributionEntry<?>) o;
            return Objects.equals(this.representationValue, that.representationValue);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.representationValue);
        }
    }
}
