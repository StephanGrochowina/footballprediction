package de.coiaf.random.weight;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CalculatorWeightedMean {

    private static final int DIVISION_SCALE = 13;
    private static final WeightingContext EMPTY_WEIGHTING_CONTEXT = new WeightingContext();

    private static <T> BigDecimal calculateWeightByIndex(WeightFunction<T> weight, int index) {
        T convertedIndexValue = weight.convertIndex(index);

        return calculateWeightBySample(weight, convertedIndexValue);
    }

    private static <T> BigDecimal calculateWeightBySample(WeightFunction<T> weight, T sample) {
        Objects.requireNonNull(weight, "Parameter weight must not be null.");
        Objects.requireNonNull(sample, "Parameter sample must not be null.");

        return weight.applyWeight(sample);
    }

    private static BigDecimal calculateWeightedValueByIndex(BigDecimal weight, int index) {
        return calculateWeightedValue(weight, new BigDecimal(index));
    }

    private static <T extends Number> BigDecimal calculateWeightedValueBySample(BigDecimal weight, T sample) {
        Objects.requireNonNull(sample, "Parameter sample must not be null.");

        return calculateWeightedValue(weight, new BigDecimal(sample.toString()));
    }

    private static BigDecimal calculateWeightedValue(BigDecimal weight, BigDecimal sampleValue) {
        Objects.requireNonNull(weight, "Parameter weight must not be null.");
        Objects.requireNonNull(sampleValue, "Parameter sampleValue must not be null.");

        return weight.multiply(sampleValue);
    }

    public <T> double calculateWeightedMean(WeightFunction<T> weight, Collection<T> samples) {
        Objects.requireNonNull(weight);
        Objects.requireNonNull(samples);

        double result = EMPTY_WEIGHTING_CONTEXT.getAverageWeightedValue();

        if (!samples.isEmpty()) {
            List<T> samplesAccessableByIndex = new ArrayList<>(samples);

            Stream<WeightingContext> stream = IntStream.rangeClosed(0, samples.size() - 1)
                    .filter(index -> samplesAccessableByIndex.get(index) != null)
                    .mapToObj(index -> new WeightingContext(
                                    () -> calculateWeightBySample(weight, samplesAccessableByIndex.get(index)),
                                    weightValue -> calculateWeightedValueByIndex(weightValue, index)
                            ));

            result = this.calculateWeightedMean(stream);
        }

        return result;
    }

    public <T> double calculateWeightedMean(WeightFunction<T> weight, int startIndex, int endIndex) {
        Objects.requireNonNull(weight);

        int minIndex = startIndex <= endIndex ? startIndex : endIndex;
        int maxIndex = startIndex <= endIndex ? endIndex : startIndex;

        Stream<WeightingContext> stream = IntStream.rangeClosed(minIndex, maxIndex)
                .mapToObj(index -> new WeightingContext(
                        () -> calculateWeightByIndex(weight, index),
                        weightValue -> calculateWeightedValueByIndex(weightValue, index)
                ));

        return this.calculateWeightedMean(stream);
    }

    public <T extends Number> double calculateWeightedMean(WeightFunction<T> weight, Stream<T> sampleStream) {
        Objects.requireNonNull(weight);
        Objects.requireNonNull(sampleStream);

        Stream<WeightingContext> stream = sampleStream
                .filter(Objects::nonNull)
                .map(sample -> new WeightingContext(
                        () -> calculateWeightBySample(weight, sample),
                        weightValue -> calculateWeightedValueBySample(weightValue, sample)
                ));

        return this.calculateWeightedMean(stream);
    }

    private double calculateWeightedMean(Stream<WeightingContext> weightingContextStream) {
        Objects.requireNonNull(weightingContextStream);

        WeightingContext result = weightingContextStream.reduce(EMPTY_WEIGHTING_CONTEXT, WeightingContext::add, WeightingContext::add);

        return result.getAverageWeightedValue();
    }

    private static class WeightingContext {

        private final BigDecimal sumWeights;
        private final BigDecimal sumWeightedValues;
        private final double averageWeightedValue;

        private WeightingContext() {
            this(BigDecimal.ZERO, BigDecimal.ZERO);
        }

        private WeightingContext(Supplier<BigDecimal> weightSupplier, Function<BigDecimal, BigDecimal> weightedValueCalculator) {
            this(weightSupplier.get(), weightedValueCalculator.apply(weightSupplier.get()));
        }

        private WeightingContext(BigDecimal sumWeights, BigDecimal sumWeightedValues) {
            Objects.requireNonNull(sumWeights, "Parameter sumWeights must not be null.");
            Objects.requireNonNull(sumWeightedValues, "Parameter sumWeightedValues must not be null.");

            this.sumWeights = sumWeights;
            this.sumWeightedValues = sumWeightedValues;
            this.averageWeightedValue = BigDecimal.ZERO.compareTo(sumWeights) == 0 ? 0.0
                    : sumWeightedValues.divide(sumWeights, DIVISION_SCALE, RoundingMode.HALF_UP).doubleValue();
        }

        private WeightingContext add(WeightingContext augend) {
            Objects.requireNonNull(augend, "Parameter augend must not be null.");

            BigDecimal weights = this.sumWeights.add(augend.sumWeights);
            BigDecimal weightedValues = this.sumWeightedValues.add(augend.sumWeightedValues);

            return new WeightingContext(weights, weightedValues);
        }

        private double getAverageWeightedValue() {
            return this.averageWeightedValue;
        }
    }
}
