package de.coiaf.random.odds;

import de.coiaf.random.probability.Probabilities;
import de.coiaf.random.probability.Probability;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OddGroup<K, OV> {
    private static final BigDecimal BOOKMAKER_MARGIN_MINIMUM = new BigDecimal("-1.000", MathContext.UNLIMITED);
    private static final BigDecimal BOOKMAKER_MARGIN_MINIMUM_PROBABILITY_SUM = new BigDecimal("0.5", MathContext.UNLIMITED);

    private final Map<K, OddGroupElement<OV>> groupElements;
    private final BigDecimal marginBookmaker;

    public OddGroup(Map<K, Odd<OV>> odds) {
        AdditionContext<K, OV> context = createAdditionContext(odds);

        this.validateGroupElements(context.elements);

        this.groupElements = context.elements;
        this.marginBookmaker = context.calculateMargin();
    }

    private AdditionContext<K, OV> createAdditionContext(Map<K, Odd<OV>> odds) {
        AdditionContext<K, OV> context;

        Odds.requireNotEmpty(odds);

        context = odds.entrySet().stream()
                .filter(oddEntry -> oddEntry != null && oddEntry.getKey() != null && oddEntry.getValue() != null)
                .map(oddEntry -> new AdditionContext<>(oddEntry.getKey(), oddEntry.getValue()))
                .reduce(new AdditionContext<>(), AdditionContext::add);

        context.normalizeEntries();

        return context;
    }

    @SuppressWarnings("WeakerAccess")
    public BigDecimal getMarginBookmaker() {
        return this.marginBookmaker;
    }

    /**
     * @param key the key referring to an odd
     * @return the implied probability of the odd referred to by key
     * @throws NullPointerException     if key is null
     * @throws IllegalArgumentException if there is no odd for key
     */
    public Probability getImpliedProbability(K key) {
        return this.determineElement(key).getImpliedProbability();
    }

    /**
     * @param key the key referring to an odd
     * @return the normalized probability of the odd referred to by key
     * @throws NullPointerException     if key is null
     * @throws IllegalArgumentException if there is no odd for key
     */
    @SuppressWarnings("WeakerAccess")
    public Probability getNormalizedProbability(K key) {
        return this.determineElement(key).getNormalizedProbability();
    }

    /**
     * @param key the key referring to an odd
     * @return the decimal odd value of the odd referred to by key
     * @throws NullPointerException     if key is null
     * @throws IllegalArgumentException if there is no odd for key
     */
    public BigDecimal getDecimalOddValue(K key) {
        return this.determineElement(key).getDecimalOddValue();
    }

    /**
     * @param key the key referring to an odd
     * @return the normalized decimal odd value of the odd referred to by key
     * @throws NullPointerException     if key is null
     * @throws IllegalArgumentException if there is no odd for key
     */
    @SuppressWarnings("WeakerAccess")
    public BigDecimal getNormalizedOddValue(K key) {
        return this.determineElement(key).getNormalizedOddValue();
    }

    /**
     * @param key the key referring to an odd
     * @return the odd value of the odd referred to by key
     * @throws NullPointerException     if key is null
     * @throws IllegalArgumentException if there is no odd for key
     */
    public OV getOddValue(K key) {
        return this.determineElement(key).getOddValue();
    }

    private OddGroupElement<OV> determineElement(K key) {
        OddGroupElement<OV> result;

        Objects.requireNonNull(key);

        result = this.groupElements.get(key);

        if (result == null) {
            throw new IllegalArgumentException();
        }

        return result;
    }

    private void validateGroupElements(Map<K, OddGroupElement<OV>> elements) {
        Objects.requireNonNull(elements);

        if (elements.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private static class AdditionContext<K, OV> {
        private final BigDecimal sumProbabilities;
        private final Map<K, OddGroupElement<OV>> elements = new HashMap<>();

        private AdditionContext() {
            this(BigDecimal.ZERO);
        }

        private AdditionContext(K key, Odd<OV> odd) {
            this(odd.getImpliedProbability().toBigDecimal());

            Objects.requireNonNull(key);

            this.elements.put(key, new OddGroupElement<>(odd));
        }

        private AdditionContext(BigDecimal sumProbabilities) {
            Objects.requireNonNull(sumProbabilities);

            this.sumProbabilities = sumProbabilities;
        }

        private AdditionContext<K, OV> add(AdditionContext<K, OV> augend) {
            AdditionContext<K, OV> sum;

            Objects.requireNonNull(augend);

            sum = new AdditionContext<>(this.sumProbabilities.add(augend.sumProbabilities));
            sum.elements.putAll(this.elements);
            sum.elements.putAll(augend.elements);

            return sum;
        }

        private void normalizeEntries() {
            Probability averageProbability = this.createAverageProbability();

            this.elements.values().forEach(oddGroupElement -> {
                if (BigDecimal.ZERO.compareTo(sumProbabilities) >= 0) {
                    oddGroupElement.normalizedProbability = averageProbability;
                }
                else {
                    oddGroupElement.normalizedProbability = oddGroupElement.normalizeImpliedProbability(this.sumProbabilities);
                }
                if (!Probability.isImpossible(oddGroupElement.normalizedProbability, true)) {
                    oddGroupElement.normalizedOddValue = Probabilities.invertProbabilityValue(oddGroupElement.normalizedProbability);
                }
            });

        }

        private Probability createAverageProbability() {
            if (this.elements.isEmpty()) {
                throw new IllegalArgumentException("An odd group must contain at least one element.");

            }

            return Probability.createProbabilityByDivision(BigDecimal.ONE, new BigDecimal(this.elements.size()));
        }

        private BigDecimal calculateMargin() {
            return BOOKMAKER_MARGIN_MINIMUM_PROBABILITY_SUM.compareTo(this.sumProbabilities) >= 0 ?
                    BOOKMAKER_MARGIN_MINIMUM : BigDecimal.ONE.subtract(BigDecimal.ONE.divide(sumProbabilities, 3, BigDecimal.ROUND_HALF_UP));
        }
    }

    private static class OddGroupElement<OV> implements Odd<OV> {

        private final Odd<OV> delegate;
        private Probability normalizedProbability;
        private BigDecimal normalizedOddValue;

        private OddGroupElement(Odd<OV> delegate) {
            this.delegate = delegate;
        }

        /**
         * @param normalizationDivisor the divisor for normalizing the probability
         * values within an odd group
         * @return impliedProbability divided by normalizationDivisor
         * @throws NullPointerException     if normalizationDivisor is null
         * @throws IllegalArgumentException if {@code normalizationDivisor} is not
         * greater than 0
         */
        private Probability normalizeImpliedProbability(BigDecimal normalizationDivisor) {
            BigDecimal occurenceProbabilityValue = this.getImpliedProbability().toBigDecimal();

            Objects.requireNonNull(normalizationDivisor);

            if (BigDecimal.ZERO.compareTo(normalizationDivisor) >= 0) {
                throw new IllegalArgumentException();
            }

            return Probability.createProbabilityByDivision(occurenceProbabilityValue, normalizationDivisor);
        }

        @SuppressWarnings("WeakerAccess")
        public Probability getNormalizedProbability() {
            return this.normalizedProbability;
        }

        @Override
        public Probability getImpliedProbability() {
            return this.delegate.getImpliedProbability();
        }

        @Override
        public BigDecimal getDecimalOddValue() {
            return this.delegate.getDecimalOddValue();
        }

        @Override
        public OV getOddValue() {
            return this.delegate.getOddValue();
        }

        @SuppressWarnings("WeakerAccess")
        public BigDecimal getNormalizedOddValue() {
            return this.normalizedOddValue;
        }
    }
}
