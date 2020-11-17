package de.coiaf.random.probability;

import de.coiaf.footballprediction.common.cache.Cache;
import de.coiaf.footballprediction.common.cache.CacheFactory;
import de.coiaf.random.distributions.enumerated.EnumeratedDistribution;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author Stephan Grochowina <stephan.grochowina@web.de>
 */
public class NormalizerDistributionElementWeights<T, N extends Number> {

    /**
     * Creates a pair for {@code representationValue} which is only used for initializing a normalization
     * with that value. The created {@link Pair} instance can be used to take influence on the order of the
     * elements in an enumerated distribution. If no further {@link Pair} instance for {@code representationValue}
     * with a given weight is supplied to the {@code NormalizerDistributionElementWeights} instance {@code representationValue}
     * will be mapped to a probability of 0
     * @param representationValue the value for which a density value should be provided
     * @param <T> type of the {@code representationValue}
     * @param <N> type of the weight
     * @return the pair representing {@code representationValue} and its empty weight.
     */
    public static <T, N extends Number> Pair<T, N> createInitializingPair(T representationValue) {
        return createPair(representationValue, null, false);
    }

    /**
     * Creates a pair for {@code representationValue} and its corresponding weight {@code weight}.  {@code weight} will be used to
     * determine the density value for {@code representationValue} in a distribution. It may represent any value between
     * {@link Double#NEGATIVE_INFINITY Double.NEGATIVE_INFINITY} and {@link Double#POSITIVE_INFINITY Double.POSITIVE_INFINITY}.
     *
     * @param representationValue the value for which a density value should be provided
     * @param weight the value influencing the density value for {@code representationValue}
     * @param <T> type of the {@code representationValue}
     * @param <N> <N> type of the {@code weight}, a sub class of {@link Number}
     * @return the pair representing {@code representationValue} and its {@code weight}.
     * @throws NullPointerException if {@code weight} is null
     */
    public static <T, N extends Number> Pair<T, N> createPair(T representationValue, N weight) {
        if (weight == null) {
            throw new NullPointerException();
        }

        return createPair(representationValue, weight, true);
    }
    private static <T, N extends Number> Pair<T, N> createPair(T representationValue, N weight, boolean incrementQuantity) {
        return new Pair<>(representationValue, weight, incrementQuantity);
    }

    /**
     * Creates a collection of {@link NormalizedEntry} instances. An instance of {@link NormalizedEntry}
     * represents all instances of {@link Pair} sharing an equal representation value and the sum of their weights.
     * it provides a density value. I.e. there exists exactly one instance of {@link NormalizedEntry} for each
     * representation value.
     *
     * The sum of the density values of all {@link NormalizedEntry} instances within the result collection is 1.
     *
     * if {@code resultHandler} is not null, each {@link NormalizedEntry} instance of the result collection will be
     * passed to {@code resultHandler} for further processing (e.g. for initializing an {@link EnumeratedDistribution}).
     * @param items the {@link Pair} instances provided for normalization
     * @param resultHandler the Handler for processing each result entry
     * @throws NullPointerException if {@code items} is null
     * @throws IllegalArgumentException if {@code items} is empty
     */
    public void normalize(Collection<Pair<T, N>> items, Consumer<NormalizedEntry<T, N>> resultHandler) {
        ContextNormalizing<T, N> context;

        this.validateItems(items);

        context = this.createContext();
        items.forEach(context::addToContext);
        context.normalize(resultHandler);
    }

    private void validateItems(Collection<Pair<T, N>> items) {
        if (items == null) {
            throw new NullPointerException();
        }
        if (items.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private ContextNormalizing<T, N> createContext() {
        return new ContextNormalizing<>();
    }

    private static class ContextNormalizing<T, N extends Number> {
        private static final CacheFactory CACHE_FACTORY = new CacheFactory();

        private BigDecimal offset = BigDecimal.ZERO;
        private BigDecimal sum = BigDecimal.ZERO;
        private final Cache<T, NormalizedEntry<T, N>> entryCache = CACHE_FACTORY.createPrePopulatedOrderedCache(new LinkedHashMap<>());

        private void addToContext(Pair<T, N> item) {
            T representationValue = item == null ? null : item.getRepresentationValue();
            N weight = item == null ? null : item.getWeight();
            boolean incrementQuantity = item == null || item.isIncrementQuantity();
            NormalizedEntry<T, N> entry;

            entry = this.entryCache.get(representationValue, () -> new NormalizedEntry<>(representationValue));

            entry.addWeight(weight, incrementQuantity);
            if (weight != null) {
                BigDecimal weightValue = new BigDecimal(weight.toString(), MathContext.UNLIMITED);

                this.sum = this.sum.add(weightValue);
                if (this.offset.compareTo(weightValue) > 0) {
                    this.offset =  weightValue;
                }
            }
        }

        private void normalize(Consumer<NormalizedEntry<T, N>> resultHandler) {
            BigDecimal entries = BigDecimal.valueOf(this.entryCache.size());
            Probability defaultProbability = Probability.createProbabilityByDivision(BigDecimal.ONE, entries);
            BigDecimal normalizedSum = this.offset.compareTo(BigDecimal.ZERO) < 0 ?
                    this.sum.subtract(this.offset.multiply(entries)) : this.sum;
            Probability currentDistribution = Probability.IMPOSSIBLE;

            for (NormalizedEntry<T,N> entry : this.entryCache.values()) {
                Probability currentDensity;

                if (this.offset.compareTo(BigDecimal.ZERO) < 0) {
                    entry.normalizedWeight = entry.normalizedWeight.subtract(this.offset.multiply(BigDecimal.valueOf(entry.quantity)));
                }

                if (normalizedSum.compareTo(BigDecimal.ZERO) > 0) {
                    currentDensity = Probability.createProbabilityByDivision(entry.normalizedWeight, normalizedSum);
                }
                else {
                    currentDensity = defaultProbability;
                }

                if (!Probability.CERTAIN.equals(currentDistribution)) {
                    try {
                        currentDistribution = currentDistribution.add(currentDensity);
                    } catch (IllegalArgumentException e) {
                        currentDistribution = Probability.CERTAIN;
                    }
                }

                entry.setDensity(currentDensity);
                entry.setDistribution(currentDistribution);

                if (resultHandler != null) {
                    resultHandler.accept(entry);
                }
            }
        }
    }

    public static class NormalizedEntry<T, N extends Number> {
        private final T representationValue;
        private BigDecimal weight = BigDecimal.ZERO;
        private BigDecimal normalizedWeight = BigDecimal.ZERO;
        private int quantity = 0;
        private Probability density = Probability.IMPOSSIBLE;
        private Probability distribution = Probability.IMPOSSIBLE;

        private NormalizedEntry(T representationValue) {
            this.representationValue = representationValue;
        }

        private void addWeight(N additionalWeight, boolean incrementQuantity) {
            BigDecimal additionalWeightValue = additionalWeight == null ? BigDecimal.ZERO : new BigDecimal(additionalWeight.toString(), MathContext.UNLIMITED);

            if (incrementQuantity) {
                this.quantity++;
            }

            this.normalizedWeight = this.normalizedWeight.add(additionalWeightValue);
            this.weight = this.weight.add(additionalWeightValue);
        }

        public Probability getDensity() {
            return this.density;
        }

        private void setDensity(Probability density) {
            this.density = density;
        }

        public Probability getDistribution() {
            return this.distribution;
        }

        private void setDistribution(Probability distribution) {
            this.distribution = distribution;
        }

        public T getRepresentationValue() {
            return this.representationValue;
        }

        public BigDecimal getWeight() {
            return this.weight;
        }
    }

    public static class Pair<T, N extends Number> {
        private final T representationValue;
        private final N weight;
        private final boolean incrementQuantity;

        private Pair(T representationValue, N weight, boolean incrementQuantity) {
            this.representationValue = representationValue;
            this.weight = weight;
            this.incrementQuantity = incrementQuantity;
        }

        T getRepresentationValue() {
            return this.representationValue;
        }

        public N getWeight() {
            return this.weight;
        }

        private boolean isIncrementQuantity() {
            return incrementQuantity;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair<?, ?> pair = (Pair<?, ?>) o;
            return Objects.equals(this.representationValue, pair.representationValue);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.representationValue);
        }
    }
}
