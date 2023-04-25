package de.coiaf.footballprediction.probabilitymodel.domain.model;

import de.coiaf.footballprediction.common.vo.numerical.AbstractBigDecimalBasedValueObject;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.function.Supplier;

public class ThresholdTotalGoals extends AbstractBigDecimalBasedValueObject<ThresholdTotalGoals> {

    private static final int THRESHOLD_SCALE = 0;
    private static final BigDecimal DEFAULT_THRESHOLD_VALUE = new BigDecimal("2.5");
    private static final BigDecimal MIN_VALUE_ALLOWED = BigDecimal.valueOf(Double.MIN_VALUE);
    private static final BigDecimal SUBTRAHEND = new BigDecimal("0.5");
    private static final BigDecimal ILLEGAL_THRESHOLD_MARKING_VALUE = new BigDecimal("-0.5");

    static final ThresholdTotalGoals DEFAULT_THRESHOLD = new ThresholdTotalGoals(DEFAULT_THRESHOLD_VALUE);
    static final ThresholdTotalGoals MIN_THRESHOLD = new ThresholdTotalGoals(MIN_VALUE_ALLOWED);
    private static final BigDecimal MIN_THRESHOLD_VALUE = MIN_THRESHOLD.toBigDecimal();

    private static final Supplier<ThresholdTotalGoals> THRESHOLD_SUPPLIER_ALLOWING_NULL_VALUE = () -> null;
    private static final Supplier<ThresholdTotalGoals> THRESHOLD_SUPPLIER_ALLOWING_ILLEGAL_VALUE = () -> MIN_THRESHOLD;

    /**
     *
     * @return the default threshold for total goals
     */
    public static ThresholdTotalGoals getDefaultInstance() {
        return DEFAULT_THRESHOLD;
    }

    /**
     *
     * @return the minimum threshold possible
     */
    public static ThresholdTotalGoals getMinThreshold() {
        return MIN_THRESHOLD;
    }

    /**
     * Creates a {@code ThresholdTotalGoals} instance from {@code n}.
     * @param n value representing threshold
     * @return a {@code ThresholdTotalGoals} instance for {@code n}
     * @throws NullPointerException if {@code n} is null
     * @throws IllegalArgumentException if {@code n} is not greater than BigDecimal.ZERO
     */
    public static ThresholdTotalGoals valueOf(Number n) {
        return valueOf(n, false);
    }

    /**
     * Creates a {@code ThresholdTotalGoals} instance from {@code n}.
     * @param n value representing threshold
     * @param allowIllegalValue  flag indicating whether null or illegal values for {@code n} should return a
     * result instead of throwing an exception. If set to true this method will behave this way:
     * If {@code n} is null null will be returned.
     * If {@code n} is negative or zero the minimum threshold will be returned.
     * @return a {@code ThresholdTotalGoals} instance for {@code n}
     * @throws NullPointerException if {@code n} is null and {@code n == false}
     * @throws IllegalArgumentException if {@code n} is not greater than BigDecimal.ZERO and {@code n == false}
     */
    public static ThresholdTotalGoals valueOf(Number n, boolean allowIllegalValue) {
        Supplier<ThresholdTotalGoals> thresholdSupplierOnNullValue = allowIllegalValue ? THRESHOLD_SUPPLIER_ALLOWING_NULL_VALUE : null;
        Supplier<ThresholdTotalGoals> thresholdSupplierOnIllegalValue = allowIllegalValue ? THRESHOLD_SUPPLIER_ALLOWING_ILLEGAL_VALUE : null;

        return valueOf(n, thresholdSupplierOnNullValue, thresholdSupplierOnIllegalValue);
    }

    /**
     * Creates a {@code ThresholdTotalGoals} instance from {@code n}.
     * @param n value representing threshold
     * @param thresholdSupplierOnNullValue provides a {@code ThresholdTotalGoals} instance if {@code n} is null
     * @param thresholdSupplierOnIllegalValue provides a {@code ThresholdTotalGoals} instance if {@code n} is not greater than BigDecimal.ZERO
     * @return a {@code ThresholdTotalGoals} instance for {@code n}
     * @throws NullPointerException if {@code n} is null and {@code thresholdSupplierOnNullValue} is null
     * @throws IllegalArgumentException if {@code n} is not greater than BigDecimal.ZERO and {@code thresholdSupplierOnIllegalValue} is null
     */
    private static ThresholdTotalGoals valueOf(Number n, Supplier<ThresholdTotalGoals> thresholdSupplierOnNullValue, Supplier<ThresholdTotalGoals> thresholdSupplierOnIllegalValue) {
        ThresholdTotalGoals threshold;

        try {
            BigDecimal thresholdValue =  new BigDecimal(n.toString(), MathContext.UNLIMITED);
            BigDecimal thresholdValueNormalized = normalize(thresholdValue, true);

            if (MIN_THRESHOLD_VALUE.compareTo(thresholdValueNormalized) == 0) {
                threshold = MIN_THRESHOLD;
            }
            else if (DEFAULT_THRESHOLD_VALUE.compareTo(thresholdValueNormalized) == 0) {
                threshold = DEFAULT_THRESHOLD;
            }
            else {
                threshold = new ThresholdTotalGoals(thresholdValue);
            }
        }
        catch (NullPointerException e) {
            if (thresholdSupplierOnNullValue == null) {
                throw e;
            }

            threshold = thresholdSupplierOnNullValue.get();
        }
        catch (IllegalArgumentException e) {
            if (thresholdSupplierOnIllegalValue == null) {
                throw e;
            }

            threshold = thresholdSupplierOnIllegalValue.get();
        }

        return threshold;
    }

    private static  BigDecimal createInternalValue(BigDecimal threshold) {
        validateLowerBound(threshold);

        return normalize(threshold, false);
    }

    private static BigDecimal normalize(BigDecimal threshold, boolean separateIllegalValues) {
        BigDecimal thresholdIllegalValue = separateIllegalValues ? ILLEGAL_THRESHOLD_MARKING_VALUE : MIN_VALUE_ALLOWED;
        BigDecimal usedThreshold = threshold == null || MIN_VALUE_ALLOWED.compareTo(threshold) > 0 ? thresholdIllegalValue : threshold;

        return usedThreshold.setScale(THRESHOLD_SCALE, RoundingMode.UP).subtract(SUBTRAHEND);
    }

    private static void validateLowerBound(BigDecimal threshold) {
        if (MIN_VALUE_ALLOWED.compareTo(threshold) > 0) {
            throw new IllegalArgumentException("Parameter threshold shouldn´t be less than 0.1");
        }
    }

    /**
     *
     * @param threshold total goals threshold
     * @throws NullPointerException if {@code threshold} is null
     * @throws IllegalArgumentException if {@code threshold} is not greater than BigDecimal.ZERO
     */
    private ThresholdTotalGoals(BigDecimal threshold) {
        super(() -> createInternalValue(threshold));
    }

}
