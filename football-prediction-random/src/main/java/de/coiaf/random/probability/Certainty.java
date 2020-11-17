package de.coiaf.random.probability;

import java.util.Arrays;
import java.util.Collection;

public class Certainty {

    /**
     * Determines a value indicating the certainty of the values, i.e. the certainty to be able to choose the right value
     * out of them (e.g. if they are probability values). If all values represent the same value Probability.IMPOSSIBLE is
     * returned to indicate that the likelihood to select the wrong value is maximum high. If one value is above zero and
     * the others are zero Possibility.CERTAIN is returned. Otherwise the result will represent a value between those two
     * results.
     *
     * The certainty relates to the uncertainty by {@code certainty = 1 - uncertainty}
     * @param values the values to be checked
     * @param <T>
     * @param <N> the type of each value
     * @return a Probability instance indicating the certainty
     */
    public static <T, N extends Number> Probability determineCertainty(NormalizerDistributionElementWeights.Pair<T, N>... values) {
        return determineCertainty(Arrays.asList(values));
    }

    /**
     * Determines a value indicating the certainty of the values, i.e. the certainty to be able to choose the right value
     * out of them (e.g. if they are probability values). If all values represent the same value Probability.IMPOSSIBLE is
     * returned to indicate that the likelihood to select the wrong value is maximum high. If one value is above zero and
     * the others are zero Possibility.CERTAIN is returned. Otherwise the result will represent a value between those two
     * results.
     *
     * The certainty relates to the uncertainty by {@code certainty = 1 - uncertainty}
     * @param values the values to be checked
     * @param <T>
     * @param <N> the type of each value
     * @return a Probability instance indicating the certainty
     */
    public static <T, N extends Number> Probability determineCertainty(Collection<NormalizerDistributionElementWeights.Pair<T, N>> values) {
        return determineUncertainty(values).negate();
    }

    /**
     * Determines a value indicating the uncertainty of the values, i.e. the certainty to be able to choose the right value
     * out of them (e.g. if they are probability values). If all values represent the same value Probability.CERTAIN is
     * returned to indicate that the likelihood to select the wrong value is maximum high. If one value is above zero and
     * the others are zero Possibility.IMPOSSIBLE is returned. Otherwise the result will represent a value between those two
     * results.
     *
     * The uncertainty relates to the certainty by {@code uncertainty = 1 - certainty}
     * @param values the values to be checked
     * @param <T>
     * @param <N> the type of each value
     * @return a Probability instance indicating the uncertainty
     */
    public static <T, N extends Number> Probability determineUncertainty(NormalizerDistributionElementWeights.Pair<T, N>... values) {
        return determineUncertainty(Arrays.asList(values));
    }

    /**
     * Determines a value indicating the uncertainty of the values, i.e. the certainty to be able to choose the right value
     * out of them (e.g. if they are probability values). If all values represent the same value Probability.CERTAIN is
     * returned to indicate that the likelihood to select the wrong value is maximum high. If one value is above zero and
     * the others are zero Possibility.IMPOSSIBLE is returned. Otherwise the result will represent a value between those two
     * results.
     *
     * The uncertainty relates to the certainty by {@code uncertainty = 1 - certainty}
     * @param values the values to be checked
     * @param <T>
     * @param <N> the type of each value
     * @return a Probability instance indicating the uncertainty
     */
    public static <T, N extends Number> Probability determineUncertainty(Collection<NormalizerDistributionElementWeights.Pair<T, N>> values) {
        UncertaintyContext<T, N> context = new UncertaintyContext<>(values);

        return context.process();
    }

    static double calculateUncertaintyElementValue(Probability probability) {
        double result = 0;

        if (!Probability.isImpossible(probability, true) && !Probability.isCertain(probability)) {
            double densityValue = probability.doubleValue();
            result = - densityValue * Math.log10(densityValue);
        }

        return result;
    }

    static class UncertaintyContext<T, N extends Number> {

        private final NormalizerDistributionElementWeights<T, N> normalizer;
        private final Collection<NormalizerDistributionElementWeights.Pair<T, N>> values;
        private double sumLogValues = 0.0;
        private int groupedElementsSize = 0;

        UncertaintyContext(Collection<NormalizerDistributionElementWeights.Pair<T, N>> values) {
            this.normalizer = this.createNormalizer();
            this.values = values;
        }

        private NormalizerDistributionElementWeights<T, N> createNormalizer() {
            return new NormalizerDistributionElementWeights<>();
        }

        Probability process() {
            double maxLogvalue;

            if (values == null || values.isEmpty()) {
                return Probability.CERTAIN;
            }

            this.normalizer.normalize(this.values, (element -> {this.handleProcessedElement(element);}));

            if (this.groupedElementsSize == 0) {
                return Probability.CERTAIN;
            }
            else if (this.sumLogValues == 0.0) {
                return Probability.IMPOSSIBLE;
            }

            maxLogvalue = - Math.log10(1.0/((double) this.groupedElementsSize));

            return this.sumLogValues > maxLogvalue ? Probability.CERTAIN : new Probability(this.sumLogValues / maxLogvalue);
        }

        private void handleProcessedElement(NormalizerDistributionElementWeights.NormalizedEntry<T, N> element) {
            Probability elementDensity = element == null ? Probability.IMPOSSIBLE : element.getDensity();
            this.groupedElementsSize++;

            this.sumLogValues += calculateUncertaintyElementValue(elementDensity);
        }
    }
}
