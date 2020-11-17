package de.coiaf.random.probability;

import de.coiaf.footballprediction.common.vo.numerical.AbstractBigDecimalBasedValueObject;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;

/**
 *
 * @author Stephan Grochowina <stephan.grochowina@web.de>
 */
public class Probability extends AbstractBigDecimalBasedValueObject<Probability> {

    static final int DIVISION_SCALE = 13;

    private static final BigDecimal VALUE_IMPOSSIBLE = BigDecimal.ZERO;
    private static final BigDecimal VALUE_CERTAIN = BigDecimal.ONE;
    private static final BigDecimal VALUE_UNCERTAIN = VALUE_IMPOSSIBLE.add(VALUE_CERTAIN).divide(BigDecimal.valueOf(2), 1, BigDecimal.ROUND_HALF_UP);

    public static final Probability IMPOSSIBLE = new Probability(VALUE_IMPOSSIBLE);
    public static final Probability UNCERTAIN = new Probability(VALUE_UNCERTAIN);
    public static final Probability CERTAIN = new Probability(VALUE_CERTAIN);

    public static boolean isCertain(Probability probability) {
        return probability != null && Probability.CERTAIN.compareTo(probability) == 0;
    }

    public static boolean isImpossible(Probability probability) {
        return isImpossible(probability, false);
    }

    public static boolean isImpossible(Probability probability, boolean interpretNullAsImpossible) {
        Probability handledProbability = probability == null && interpretNullAsImpossible ? Probability.IMPOSSIBLE : probability;

        return handledProbability != null && Probability.IMPOSSIBLE.compareTo(handledProbability) == 0;
    }

    /**
     * Returns a {@code Probability} instance representing the specified
     * {@code double} value.
     *
     * @param  d a double value.
     * @return a {@code Probability} instance representing {@code d}.
     * @throws IllegalArgumentException if d is outside [0.0, 1.0]
     */
    public static Probability valueOf(double d) {
        return new Probability(d);
    }

    /**
     *
     * @param n not null, representing a value between 0.0 and 1.0
     * @return a {@code Probability} instance representing {@code n}.
     * @throws IllegalArgumentException if n is outside [0.0, 1.0]
     * @throws NullPointerException if n is null
     */
    public static Probability valueOf(Number n) {
        return new Probability(n);
    }

    public static Probability createProbabilityByDivision(BigDecimal dividend, BigDecimal divisor) {
        return new Probability(createProbabilityValueByDivision(dividend, divisor));
    }

    public static BigDecimal createProbabilityValueByDivision(BigDecimal dividend, BigDecimal divisor) {
        Objects.requireNonNull(dividend);
        Objects.requireNonNull(divisor);

        return dividend.divide(divisor, DIVISION_SCALE, BigDecimal.ROUND_HALF_UP);
    }

    private static BigDecimal createInternalValue(Number probability) {
        BigDecimal probabilityValue = new BigDecimal(probability.toString(), MathContext.UNLIMITED);

        validateLowerBound(probabilityValue);
        validateUpperBound(probabilityValue);

        return probabilityValue;
    }

    private static BigDecimal createInternalValue(double probability) {
        validateLowerBound(probability);
        validateUpperBound(probability);

        return BigDecimal.valueOf(probability);
    }

    private static void validateLowerBound(double probability) {
        if (VALUE_IMPOSSIBLE.doubleValue() > probability) {
            throw new IllegalArgumentException("Parameter probability shouldn´t be less than 0.0");
        }
    }
    private static void validateLowerBound(BigDecimal probability) {
        if (VALUE_IMPOSSIBLE.compareTo(probability) > 0) {
            throw new IllegalArgumentException("Parameter probability shouldn´t be less than 0.0");
        }
    }

    private static void validateUpperBound(double probability) {
        if (VALUE_CERTAIN.doubleValue() < probability) {
            throw new IllegalArgumentException("Parameter probability shouldn´t be greater than 1.0");
        }
    }

    private static void validateUpperBound(BigDecimal probability) {
        if (VALUE_CERTAIN.compareTo(probability) < 0) {
            throw new IllegalArgumentException("Parameter probability shouldn´t be greater than 1.0");
        }
    }

    /**
     * Creates a {@code Probability} with value probability.
     *
     * @param probability the probability value. {@code probability} must be not null representing a value
     * between 0.0 and 1.0.
     * @throws NullPointerException if probability is null.
     * @throws IllegalArgumentException if probability is outside [0.0, 1.0]
     */
    public Probability(Number probability) {
        super(() -> createInternalValue(probability));
    }

    /**
     * Creates a {@code Probability} with value probability.
     *
     * @param probability the probability value. {@code probability} must be a value
     * between 0.0 and 1.0.
     * @throws IllegalArgumentException if probability is outside [0.0, 1.0]
     */
    public Probability(double probability) {
        super(() -> createInternalValue(probability));
    }

    /**
     * Returns a {@code Probability} whose value is {@code (this +
     * augend).
     * @param augend
     * @return augend value to be added to this {@code Probability}.
     *
     * @throws NullPointerException if augend is null.
     * @throws IllegalArgumentException if {@code this + augend > Probability.CERTAIN}
     */
    public Probability add(Probability augend) {
        return new Probability(this.getInternalValue().add(augend.getInternalValue()));
    }

    /**
     * Returns a {@code Probability} whose value is {@code (this -
     * subtrahend)}.
     *
     * @param subtrahend value to be subtracted from this {@code Probability}.
     * @return {@code this - subtrahend}
     * @throws NullPointerException if subtrahend is null.
     * @throws IllegalArgumentException if
     * {@code this - subtrahend < Probability.IMPOSSIBLE}
     */
    public Probability subtract(Probability subtrahend) {
        return new Probability(this.getInternalValue().subtract(subtrahend.getInternalValue()));
    }

    /**
     * Returns a {@code Probability} whose value is <tt>(this &times;
     * multiplicand)</tt>.
     *
     * @param multiplicand value to be multiplied by this {@code Probability}.
     * @return {@code this * multiplicand}
     * @throws NullPointerException if multiplicand is null.
     */
    public Probability multiply(Probability multiplicand) {
        return new Probability(this.getInternalValue().multiply(multiplicand.getInternalValue()));
    }

    /**
     * Returns a {@code Probability} whose value is {@code (this /
     * divisor)}.
     *
     * @param divisor value by which this {@code Probability} is to be divided.
     * @return {@code this / divisor}
     * @throws ArithmeticException if {@code divisor} is zero.
     * @throws NullPointerException if divisor is null.
     * @throws IllegalArgumentException if
     * {@code this / divisor > Probability.CERTAIN}
     */
    public Probability divide(Probability divisor) {
        if (isImpossible(divisor)) {
            throw new ArithmeticException("Division by zero");
        }

        return new Probability(createProbabilityValueByDivision(this.getInternalValue(), divisor.getInternalValue()));
    }
    /**
     * Returns a {@code Probability} whose value is {@code (Probability.CERTAIN - this)}.
     * 
     * @return {@code Probability.CERTAIN - this}
     */
    public Probability negate() {
        return CERTAIN.subtract(this);
    }
    
    /**
     * Returns a {@code Probability} whose value is {@code (this or operand)}.
     *
     * @param operand operand to apply or on
     * @return {@code this or operand}
     * @throws NullPointerException if operand is null.
     */
    public Probability binaryOr(Probability operand) {
        return new Probability(this.getInternalValue()
                .add(operand.getInternalValue())
                .subtract(this.getInternalValue().multiply(operand.getInternalValue())));
    }
    
    /**
     * Returns a {@code Probability} whose value is an or operation of this instance and
     * all {@code (operands)}.
     * @param operands all other operands for the or operation
     * @return result of or operation of this instance and all {@code operands}
     * @throws NullPointerException if at least one of the operands is null.
     */
    public Probability or(Probability... operands) {
        Probability result = null;
        
        for (Probability operand : operands) {
            if (result == null) {
                result = this.binaryOr(operand);
            }
            else {
                result = result.binaryOr(operand);
            }
        }

        return result;
    }
    
    /**
     * Returns a {@code Probability} whose value is an and operation of this instance and
     * all {@code (operands)}.
     * @param operands all other operands for the and operation
     * @return result of and operation of this instance and all {@code operands}
     * @throws NullPointerException if at least one of the operands is null.
     */
    public Probability and(Probability... operands) {
        BigDecimal product = this.getInternalValue();
        
        for (Probability operand : operands) {
            product = product.multiply(operand.getInternalValue());
        }
        
        return new Probability(product);
    }

}
