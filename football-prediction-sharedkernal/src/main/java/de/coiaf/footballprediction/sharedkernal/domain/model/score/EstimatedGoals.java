package de.coiaf.footballprediction.sharedkernal.domain.model.score;

import de.coiaf.footballprediction.common.vo.numerical.AbstractBigDecimalBasedValueObject;
import de.coiaf.footballprediction.sharedkernal.domain.model.buildingblocks.ValueObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class EstimatedGoals extends AbstractBigDecimalBasedValueObject<EstimatedGoals> implements ValueObject<EstimatedGoals> {

    private static final int ESTIMATED_GOALS_SCALE = 2;
    private static final BigDecimal MIN_VALUE_ALLOWED = BigDecimal.valueOf(Double.MIN_VALUE);
    private static final BigDecimal MIN_VALUE = new BigDecimal("0.01");
    public static final EstimatedGoals MIN_ESTIMATED_GOALS = new EstimatedGoals(MIN_VALUE);

    /**
     * Creates a {@code EstimatedGoals} instance from {@code n}.
     * @param n value representing the instance to be created
     * @return a {@code EstimatedGoals} instance for {@code n}
     * @throws NullPointerException if {@code n} is null
     * @throws IllegalArgumentException if {@code n} is zero or negative
     */
    public static EstimatedGoals valueOf(Number n) {
        return valueOf(n, false);
    }

    /**
     * Creates a {@code EstimatedGoals} instance from {@code n}.
     * @param n value representing the instance to be created
     * @param allowIllegalValue  flag indicating whether null or illegal values for {@code n} should return a
     * result instead of throwing an exception. If set to true this method will behave this way:
     * If {@code n} is null null will be returned.
     * If {@code n} is negative or zero an instance being initialized with the smallest positive non zero value
     * will be returned.
     * @return a {@code EstimatedGoals} instance for {@code n}
     * @throws NullPointerException if {@code allowIllegalValues == false} and {@code n} is null
     * @throws IllegalArgumentException if {@code allowIllegalValues == false} and
     * {@code n} is zero or negative
     */
    public static EstimatedGoals valueOf(Number n, boolean allowIllegalValue) {
        if (allowIllegalValue && n == null) {
            return null;
        }

        BigDecimal estimatedGoals = new BigDecimal(n.toString());

        return new EstimatedGoals(estimatedGoals, allowIllegalValue);
    }

    /**
     * Calculates the average of all elements of {@code estimatedGoalsValues}. If {@code estimatedGoalsValues}
     * is null or does not provide any elements null is returned.
     * @param estimatedGoalsValues the stream of values to calculate the average of
     * @return a {@code EstimatedGoals} instance representing the average value of all stream elements
     */
    public static EstimatedGoals calculateAverageFromEstimatedGoals(Stream<EstimatedGoals> estimatedGoalsValues) {
        return calculateAverage(estimatedGoalsValues, EstimatedGoals::toBigDecimal);
    }

    /**
     * Calculates the average of all elements of {@code estimatedGoalsValues}. If {@code estimatedGoalsValues}
     * is null or does not provide any elements null is returned.
     * @param estimatedGoalsValues the stream of values to calculate the average of
     * @return a {@code EstimatedGoals} instance representing the average value of all stream elements
     */
    public static EstimatedGoals calculateAverageFromNumbers(Stream<Number> estimatedGoalsValues) {
        return calculateAverage(estimatedGoalsValues, estimatedGoalsValue -> new BigDecimal(estimatedGoalsValue.toString()));
    }

    /**
     * Calculates the average of all elements of {@code estimatedGoalsValues}. If {@code estimatedGoalsValues}
     * is null or does not provide any elements null is returned.
     * @param estimatedGoalsValues the stream of values to calculate the average of
     * @return a {@code EstimatedGoals} instance representing the average value of all stream elements
     */
    public static EstimatedGoals calculateAverageFromBigDecimals(Stream<BigDecimal> estimatedGoalsValues) {
        return calculateAverage(estimatedGoalsValues, estimatedGoalsValue -> estimatedGoalsValue);
    }

    private static <T> EstimatedGoals calculateAverage(Stream<T> estimatedGoalsValues, Function<T, BigDecimal> converter) {
        Objects.requireNonNull(converter, "Parameter converter must not be null");

        if (estimatedGoalsValues == null) {
            return null;
        }

        Optional<BigDecimal[]> sumCountOptional = estimatedGoalsValues.filter(Objects::nonNull)
                .map(converter)
                .map(estimatedGoalsValue -> new BigDecimal[]{estimatedGoalsValue, BigDecimal.ONE})
                .reduce((a, b) -> new BigDecimal[]{a[0].add(b[0]), a[1].add(BigDecimal.ONE)});

        if (!sumCountOptional.isPresent()) {
            return null;
        }

        BigDecimal[] sumCount = sumCountOptional.get();
        BigDecimal average = sumCount[0].divide(sumCount[1], ESTIMATED_GOALS_SCALE, BigDecimal.ROUND_HALF_UP);

        return new EstimatedGoals(average);
    }

    private static BigDecimal createInternalValue(BigDecimal estimatedGoals, boolean allowIllegalValues) {
        BigDecimal internalValue;

        validateLowerBound(estimatedGoals, allowIllegalValues);

        if (MIN_VALUE.compareTo(estimatedGoals) > 0) {
            internalValue = MIN_VALUE;
        }
        else {
            internalValue = estimatedGoals.setScale(ESTIMATED_GOALS_SCALE, RoundingMode.HALF_UP);
        }

        return internalValue;
    }

    private static void validateLowerBound(BigDecimal estimatedGoals, boolean allowIllegalValues) {
        Objects.requireNonNull(estimatedGoals, "Parameter estimatedGoals must not be null.");

        if (!allowIllegalValues && MIN_VALUE_ALLOWED.compareTo(estimatedGoals) > 0) {
            throw new IllegalArgumentException("Parameter estimatedGoals must be a positive non zero value.");
        }
    }

    /**
     * Creates an instance of {@code EstimatedGoals}.
     * @param estimatedGoals value representing the statistical goals value
     * @throws NullPointerException if {@code estimatedGoals} is null
     * @throws IllegalArgumentException if {@code estimatedGoals} is zero or negative
     */
    private EstimatedGoals(BigDecimal estimatedGoals) {
        this(estimatedGoals, false);
    }

    /**
     * Creates an instance of {@code EstimatedGoals}. If {@code allowIllegalValues == true} and
     * {@code estimatedGoals} is zero or negative the instance will be initialized with the smallest,
     * positive non zero value.
     * @param estimatedGoals value representing the statistical goals value
     * @param allowIllegalValues whether illegal values should be allowed
     * @throws NullPointerException if {@code estimatedGoals} is null
     * @throws IllegalArgumentException if {@code allowIllegalValues == false} and
     * {@code estimatedGoals} is zero or negative
     */
    private EstimatedGoals(BigDecimal estimatedGoals, boolean allowIllegalValues) {
        super(() -> createInternalValue(estimatedGoals, allowIllegalValues));
    }

    /**
     * Creates an EstimatedGoals instance which represents the sum of this instance and {@code augend}.
     * @param augend the EstimatedGoals instance to be added
     * @return an EstimatedGoals instance
     * @throws NullPointerException if {@code augend} is null
     */
    public EstimatedGoals add(EstimatedGoals augend) {
        Objects.requireNonNull(augend, "Parameter augend must not be null.");

        BigDecimal sum = this.getInternalValue().add(augend.getInternalValue());

        return new EstimatedGoals(sum);
    }

    /**
     * Creates an EstimatedGoals instance which represents the difference between this instance and {@code subtrahend}.
     * If {@code subtrahend} equals or is greater than this instance {@link EstimatedGoals#MIN_ESTIMATED_GOALS} is
     * returned.
     * @param subtrahend the EstimatedGoals instance to be subtracted
     * @return an EstimatedGoals instance
     * @throws NullPointerException if {@code subtrahend} is null
     */
    public EstimatedGoals subtract(EstimatedGoals subtrahend) {
        Objects.requireNonNull(subtrahend, "Parameter subtrahend must not be null.");

        BigDecimal difference = this.getInternalValue().subtract(subtrahend.getInternalValue());

        return MIN_VALUE.compareTo(difference) < 0 ? new EstimatedGoals(difference) : MIN_ESTIMATED_GOALS;
    }

}
