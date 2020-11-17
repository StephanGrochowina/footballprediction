package de.coiaf.random.distributions.common;

import java.util.Objects;
import java.util.function.Function;

public class DistributionElementFactory {

    /**
     * Creates a {@link Variance instance} for values greater or equal to zero.
     * @param variance the variance value
     * @return a {@link Variance instance}
     * @throws IllegalArgumentException if {@code variance} is less than zero.
     */
    public static Variance createVarianceZeroInclusive(double variance) {
        return new Variance(variance, true);
    }

    /**
     * Creates a {@link Variance instance} for values greater than zero.
     * @param variance the variance value
     * @return a {@link Variance instance}
     * @throws IllegalArgumentException if {@code variance} is less or equals zero.
     */
    public static Variance createVarianceZeroExclusive(double variance) {
        return new Variance(variance, false);
    }

    /**
     * Creates a {@link Variance instance} from a {@link StandardDeviation instance}.
     * @param standardDeviation a {@link StandardDeviation instance}
     * @return a {@link Variance instance}
     * @throws NullPointerException if {@code standardDeviation} is null
     */
    public static Variance createVariance(StandardDeviation standardDeviation) {
        return calculateDistributionElement(
                standardDeviation, Variance.NO_VARIANCE, Variance.INFINITE_VARIANCE,
                DistributionElementFactory::calculateVariance);
    }

    private static Variance calculateVariance(StandardDeviation standardDeviation) {
        Objects.requireNonNull(standardDeviation);

        double varianceValue = Math.pow(standardDeviation.doubleValue(), 2);

        return createVarianceZeroInclusive(varianceValue);
    }

    /**
     * Creates a {@link StandardDeviation instance} for values greater or equal to zero.
     * @param standardDeviation the standard deviation value
     * @return a {@link StandardDeviation instance}
     * @throws IllegalArgumentException if {@code standardDeviation} is less than zero.
     */
    public static StandardDeviation createStandardDeviationZeroInclusive(double standardDeviation) {
        return new StandardDeviation(standardDeviation, true);
    }

    /**
     * Creates a {@link StandardDeviation instance} for values greater than zero.
     * @param standardDeviation the standard deviation value
     * @return a {@link StandardDeviation instance}
     * @throws IllegalArgumentException if {@code standardDeviation} is less or equals zero.
     */
    public static StandardDeviation createStandardDeviationZeroExclusive(double standardDeviation) {
        return new StandardDeviation(standardDeviation, false);
    }

    /**
     * Creates a {@link StandardDeviation instance} from a {@link Variance instance}.
     * @param variance a {@link Variance instance}
     * @return a {@link StandardDeviation instance}
     * @throws NullPointerException if {@code variance} is null
     */
    public static StandardDeviation createStandardDeviation(Variance variance) {
        return calculateDistributionElement(
                variance, StandardDeviation.NO_DEVIATION, StandardDeviation.INFINITE_DEVIATION,
                DistributionElementFactory::calculateStandardDeviation);
    }

    private static StandardDeviation calculateStandardDeviation(Variance variance) {
        Objects.requireNonNull(variance);

        double standardDeviationValue = Math.sqrt(variance.doubleValue());

        return createStandardDeviationZeroInclusive(standardDeviationValue);
    }

    private static final <DES extends AbstractDistributionElement<DES>, DET extends AbstractDistributionElement<DET>>
            DET calculateDistributionElement(DES source, DET lowerBound, DET upperBound, Function<DES, DET> calculator) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(lowerBound);
        Objects.requireNonNull(upperBound);
        Objects.requireNonNull(calculator);

        DET result;

        if (source.isLowerBound()) {
            result = lowerBound;
        }
        else if (source.isUpperBound()) {
            result = upperBound;
        }
        else {
            result = calculator.apply(source);
        }

        return result;
    }
}
