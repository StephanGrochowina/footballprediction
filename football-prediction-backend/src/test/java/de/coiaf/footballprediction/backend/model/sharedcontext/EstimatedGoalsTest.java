package de.coiaf.footballprediction.backend.model.sharedcontext;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class EstimatedGoalsTest {

    private static final BigDecimal NEGATIVE_STATISTICAL_GOALS_VALUE = new BigDecimal("-0.5");
    private static final BigDecimal ZERO_STATISTICAL_GOALS_VALUE = BigDecimal.ZERO;
    private static final BigDecimal MIN_STATISTICAL_GOALS_VALUE = BigDecimal.valueOf(Double.MIN_VALUE);
    private static final BigDecimal STATISTICAL_GOALS_ABOVE_MIN_VALUE_DOWN_ROUNDED = new BigDecimal("1.234");
    private static final BigDecimal STATISTICAL_GOALS_ABOVE_MIN_VALUE_UP_ROUNDED = new BigDecimal("1.225");
    private static final BigDecimal STATISTICAL_GOALS_ABOVE_MIN_VALUE = new BigDecimal("1.23");
    private static final EstimatedGoals STATISTICAL_GOALS_ABOVE_MIN = EstimatedGoals.valueOf(STATISTICAL_GOALS_ABOVE_MIN_VALUE);
    private static final int[] VALUES_FOR_AVERAGE_CALCULATION = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private static final EstimatedGoals EXPECTED_AVERAGE_GOALS = EstimatedGoals.valueOf(new BigDecimal("5.50"));
    private static final EstimatedGoals GOALS_ADD_TEST_SUMMAND = EstimatedGoals.valueOf(new BigDecimal("1.23"));
    private static final EstimatedGoals GOALS_ADD_TEST_AUGEND = EstimatedGoals.valueOf(new BigDecimal("1.45"));
    private static final EstimatedGoals GOALS_ADD_TEST_SUM = EstimatedGoals.valueOf(new BigDecimal("2.68"));
    private static final EstimatedGoals GOALS_SUBTRACT_TEST_SMALLER_SUBTRAHEND = EstimatedGoals.valueOf(new BigDecimal("1.03"));
    private static final EstimatedGoals GOALS_SUBTRACT_TEST_GREATER_SUBTRAHEND = EstimatedGoals.valueOf(new BigDecimal("1.43"));

    @Test(expected = NullPointerException.class)
    public void valueOf_nullN() {
        EstimatedGoals.valueOf(null);
    }
    @Test(expected = IllegalArgumentException.class)
    public void valueOf_negativeN() {
        EstimatedGoals.valueOf(NEGATIVE_STATISTICAL_GOALS_VALUE);
    }
    @Test(expected = IllegalArgumentException.class)
    public void valueOf_zeroN() {
        EstimatedGoals.valueOf(ZERO_STATISTICAL_GOALS_VALUE);
    }
    @Test
    public void valueOf_positiveNBelowMinValue() {
        EstimatedGoals expected = EstimatedGoals.MIN_ESTIMATED_GOALS;

        EstimatedGoals result = EstimatedGoals.valueOf(MIN_STATISTICAL_GOALS_VALUE);

        assertNotNull(result);
        assertEquals(expected, result);
    }
    @Test
    public void valueOf_positiveNEqualToMinValue() {
        EstimatedGoals expected = EstimatedGoals.MIN_ESTIMATED_GOALS;

        EstimatedGoals result = EstimatedGoals.valueOf(EstimatedGoals.MIN_ESTIMATED_GOALS.toBigDecimal());

        assertNotNull(result);
        assertEquals(expected, result);
    }
    @Test
    public void valueOf_positiveNAboveMinValue_DownRounded() {
        EstimatedGoals result = EstimatedGoals.valueOf(STATISTICAL_GOALS_ABOVE_MIN_VALUE_DOWN_ROUNDED);

        assertNotNull(result);
        assertEquals(STATISTICAL_GOALS_ABOVE_MIN, result);
    }
    @Test
    public void valueOf_positiveNAboveMinValue_Unrounded() {
        EstimatedGoals result = EstimatedGoals.valueOf(STATISTICAL_GOALS_ABOVE_MIN_VALUE);

        assertNotNull(result);
        assertEquals(STATISTICAL_GOALS_ABOVE_MIN, result);
    }
    @Test
    public void valueOf_positiveNAboveMinValue_UpRounded() {
        EstimatedGoals result = EstimatedGoals.valueOf(STATISTICAL_GOALS_ABOVE_MIN_VALUE_UP_ROUNDED);

        assertNotNull(result);
        assertEquals(STATISTICAL_GOALS_ABOVE_MIN, result);
    }

    @Test(expected = NullPointerException.class)
    public void valueOf_nullN_illegalGoalsNotAllowed() {
        EstimatedGoals.valueOf(null, false);
    }
    @Test(expected = IllegalArgumentException.class)
    public void valueOf_negativeN_illegalGoalsNotAllowed() {
        EstimatedGoals.valueOf(NEGATIVE_STATISTICAL_GOALS_VALUE, false);
    }
    @Test(expected = IllegalArgumentException.class)
    public void valueOf_zeroN_illegalGoalsNotAllowed() {
        EstimatedGoals.valueOf(ZERO_STATISTICAL_GOALS_VALUE, false);
    }
    @Test
    public void valueOf_positiveNBelowMinValue_illegalGoalsNotAllowed() {
        EstimatedGoals expected = EstimatedGoals.MIN_ESTIMATED_GOALS;

        EstimatedGoals result = EstimatedGoals.valueOf(MIN_STATISTICAL_GOALS_VALUE, false);

        assertNotNull(result);
        assertEquals(expected, result);
    }
    @Test
    public void valueOf_positiveNEqualToMinValue_illegalGoalsNotAllowed() {
        EstimatedGoals expected = EstimatedGoals.MIN_ESTIMATED_GOALS;

        EstimatedGoals result = EstimatedGoals.valueOf(EstimatedGoals.MIN_ESTIMATED_GOALS.toBigDecimal(), false);

        assertNotNull(result);
        assertEquals(expected, result);
    }
    @Test
    public void valueOf_positiveNAboveMinValue_DownRounded_illegalGoalsNotAllowed() {
        EstimatedGoals result = EstimatedGoals.valueOf(STATISTICAL_GOALS_ABOVE_MIN_VALUE_DOWN_ROUNDED, false);

        assertNotNull(result);
        assertEquals(STATISTICAL_GOALS_ABOVE_MIN, result);
    }
    @Test
    public void valueOf_positiveNAboveMinValue_Unrounded_illegalGoalsNotAllowed() {
        EstimatedGoals result = EstimatedGoals.valueOf(STATISTICAL_GOALS_ABOVE_MIN_VALUE, false);

        assertNotNull(result);
        assertEquals(STATISTICAL_GOALS_ABOVE_MIN, result);
    }
    @Test
    public void valueOf_positiveNAboveMinValue_UpRounded_illegalGoalsNotAllowed() {
        EstimatedGoals result = EstimatedGoals.valueOf(STATISTICAL_GOALS_ABOVE_MIN_VALUE_UP_ROUNDED, false);

        assertNotNull(result);
        assertEquals(STATISTICAL_GOALS_ABOVE_MIN, result);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void valueOf_nullN_illegalGoalsAllowed() {
        EstimatedGoals result = EstimatedGoals.valueOf(null, true);

        assertNull(result);
    }
    @Test
    public void valueOf_negativeN_illegalGoalsAllowed() {
        EstimatedGoals expected = EstimatedGoals.MIN_ESTIMATED_GOALS;

        EstimatedGoals result = EstimatedGoals.valueOf(NEGATIVE_STATISTICAL_GOALS_VALUE, true);

        assertNotNull(result);
        assertEquals(expected, result);
    }
    @Test
    public void valueOf_zeroN_illegalGoalsAllowed() {
        EstimatedGoals expected = EstimatedGoals.MIN_ESTIMATED_GOALS;

        EstimatedGoals result = EstimatedGoals.valueOf(ZERO_STATISTICAL_GOALS_VALUE, true);

        assertNotNull(result);
        assertEquals(expected, result);
    }
    @Test
    public void valueOf_positiveNBelowMinValue_illegalGoalsAllowed() {
        EstimatedGoals expected = EstimatedGoals.MIN_ESTIMATED_GOALS;

        EstimatedGoals result = EstimatedGoals.valueOf(MIN_STATISTICAL_GOALS_VALUE, true);

        assertNotNull(result);
        assertEquals(expected, result);
    }
    @Test
    public void valueOf_positiveNEqualToMinValue_illegalGoalsAllowed() {
        EstimatedGoals expected = EstimatedGoals.MIN_ESTIMATED_GOALS;

        EstimatedGoals result = EstimatedGoals.valueOf(EstimatedGoals.MIN_ESTIMATED_GOALS.toBigDecimal(), true);

        assertNotNull(result);
        assertEquals(expected, result);
    }
    @Test
    public void valueOf_positiveNAboveMinValue_DownRounded_illegalGoalsAllowed() {
        EstimatedGoals result = EstimatedGoals.valueOf(STATISTICAL_GOALS_ABOVE_MIN_VALUE_DOWN_ROUNDED, true);

        assertNotNull(result);
        assertEquals(STATISTICAL_GOALS_ABOVE_MIN, result);
    }
    @Test
    public void valueOf_positiveNAboveMinValue_Unrounded_illegalGoalsAllowed() {
        EstimatedGoals result = EstimatedGoals.valueOf(STATISTICAL_GOALS_ABOVE_MIN_VALUE, true);

        assertNotNull(result);
        assertEquals(STATISTICAL_GOALS_ABOVE_MIN, result);
    }
    @Test
    public void valueOf_positiveNAboveMinValue_UpRounded_illegalGoalsAllowed() {
        EstimatedGoals result = EstimatedGoals.valueOf(STATISTICAL_GOALS_ABOVE_MIN_VALUE_UP_ROUNDED, true);

        assertNotNull(result);
        assertEquals(STATISTICAL_GOALS_ABOVE_MIN, result);
    }

    @Test
    public void calculateAverageFromEstimatedGoals_nullStream() {
        EstimatedGoals result = EstimatedGoals.calculateAverageFromEstimatedGoals(null);

        assertNull(result);
    }
    @Test
    public void calculateAverageFromEstimatedGoals_emptyStream() {
        List<EstimatedGoals> emptySource = Collections.emptyList();
        EstimatedGoals result = EstimatedGoals.calculateAverageFromEstimatedGoals(emptySource.stream());

        assertNull(result);
    }
    @Test
    public void calculateAverageFromEstimatedGoals_givenStream() {
        Stream<EstimatedGoals> source = Arrays.stream(VALUES_FOR_AVERAGE_CALCULATION)
                .mapToObj(EstimatedGoals::valueOf);

        EstimatedGoals result = EstimatedGoals.calculateAverageFromEstimatedGoals(source);

        assertNotNull(result);
        assertEquals(EXPECTED_AVERAGE_GOALS, result);
    }

    @Test
    public void calculateAverageFromNumbers_nullStream() {
        EstimatedGoals result = EstimatedGoals.calculateAverageFromNumbers(null);

        assertNull(result);
    }
    @Test
    public void calculateAverageFromNumbers_emptyStream() {
        List<Number> emptySource = Collections.emptyList();
        EstimatedGoals result = EstimatedGoals.calculateAverageFromNumbers(emptySource.stream());

        assertNull(result);
    }
    @Test
    public void calculateAverageFromNumbers_givenStream() {
        Stream<Number> source = Arrays.stream(VALUES_FOR_AVERAGE_CALCULATION)
                .mapToObj(value -> value);

        EstimatedGoals result = EstimatedGoals.calculateAverageFromNumbers(source);

        assertNotNull(result);
        assertEquals(EXPECTED_AVERAGE_GOALS, result);
    }

    @Test
    public void calculateAverageFromBigDecimals_nullStream() {
        EstimatedGoals result = EstimatedGoals.calculateAverageFromBigDecimals(null);

        assertNull(result);
    }
    @Test
    public void calculateAverageFromBigDecimals_emptyStream() {
        List<BigDecimal> emptySource = Collections.emptyList();
        EstimatedGoals result = EstimatedGoals.calculateAverageFromBigDecimals(emptySource.stream());

        assertNull(result);
    }
    @Test
    public void calculateAverageFromBigDecimals_givenStream() {
        Stream<BigDecimal> source = Arrays.stream(VALUES_FOR_AVERAGE_CALCULATION)
                .mapToObj(value -> BigDecimal.valueOf((long) value));

        EstimatedGoals result = EstimatedGoals.calculateAverageFromBigDecimals(source);

        assertNotNull(result);
        assertEquals(EXPECTED_AVERAGE_GOALS, result);
    }

    @Test(expected = NullPointerException.class)
    public void add_nullAugend() {
        GOALS_ADD_TEST_SUMMAND.add(null);
    }
    @Test
    public void add_givenAugend() {
        EstimatedGoals result = GOALS_ADD_TEST_SUMMAND.add(GOALS_ADD_TEST_AUGEND);

        assertNotNull(result);
        assertEquals(GOALS_ADD_TEST_SUM, result);
        assertNotSame(GOALS_ADD_TEST_SUM, result);
        assertNotSame(GOALS_ADD_TEST_SUMMAND, result);
    }

    @Test(expected = NullPointerException.class)
    public void subtract_nullSubtrahend() {
        GOALS_ADD_TEST_SUMMAND.subtract(null);
    }
    @Test
    public void subtract_smallerSubtrahend() {
        EstimatedGoals result = GOALS_ADD_TEST_SUMMAND.subtract(GOALS_SUBTRACT_TEST_SMALLER_SUBTRAHEND);

        assertNotNull(result);
        assertTrue(GOALS_ADD_TEST_SUMMAND.compareTo(result) > 0);
        assertTrue(EstimatedGoals.MIN_ESTIMATED_GOALS.compareTo(result) < 0);
    }
    @Test
    public void subtract_equalSubtrahend() {
        EstimatedGoals result = GOALS_ADD_TEST_SUMMAND.subtract(GOALS_ADD_TEST_SUMMAND);

        assertNotNull(result);
        assertTrue(GOALS_ADD_TEST_SUMMAND.compareTo(result) > 0);
        assertSame(EstimatedGoals.MIN_ESTIMATED_GOALS, result);
    }
    @Test
    public void subtract_greaterSubtrahend() {
        EstimatedGoals result = GOALS_ADD_TEST_SUMMAND.subtract(GOALS_SUBTRACT_TEST_GREATER_SUBTRAHEND);

        assertNotNull(result);
        assertTrue(GOALS_ADD_TEST_SUMMAND.compareTo(result) > 0);
        assertSame(EstimatedGoals.MIN_ESTIMATED_GOALS, result);
    }

    @SuppressWarnings({"SimplifiableJUnitAssertion", "ConstantConditions"})
    @Test
    public void equals_comparedWithNull() {
        assertFalse(STATISTICAL_GOALS_ABOVE_MIN.equals(null));
    }
    @SuppressWarnings({"SimplifiableJUnitAssertion", "EqualsBetweenInconvertibleTypes"})
    @Test
    public void equals_comparedWithDifferentClass() {
        assertFalse(STATISTICAL_GOALS_ABOVE_MIN.equals(STATISTICAL_GOALS_ABOVE_MIN_VALUE));
    }
    @SuppressWarnings({"SimplifiableJUnitAssertion", "EqualsWithItself"})
    @Test
    public void equals_comparedWithSameInstance() {
        assertTrue(STATISTICAL_GOALS_ABOVE_MIN.equals(STATISTICAL_GOALS_ABOVE_MIN));
    }
    @SuppressWarnings("SimplifiableJUnitAssertion")
    @Test
    public void equals_comparedWithInstanceOfDifferentValue() {
        assertFalse(STATISTICAL_GOALS_ABOVE_MIN.equals(EstimatedGoals.MIN_ESTIMATED_GOALS));
        assertFalse(EstimatedGoals.MIN_ESTIMATED_GOALS.equals(STATISTICAL_GOALS_ABOVE_MIN));
    }
    @SuppressWarnings("SimplifiableJUnitAssertion")
    @Test
    public void equals_comparedWithInstanceOfSameValue() {
        EstimatedGoals estimatedGoalsDownRounded = EstimatedGoals.valueOf(STATISTICAL_GOALS_ABOVE_MIN_VALUE_DOWN_ROUNDED);
        EstimatedGoals estimatedGoalsUpRounded = EstimatedGoals.valueOf(STATISTICAL_GOALS_ABOVE_MIN_VALUE_UP_ROUNDED);

        assertTrue(estimatedGoalsDownRounded.equals(estimatedGoalsUpRounded));
        assertTrue(estimatedGoalsUpRounded.equals(estimatedGoalsDownRounded));
    }

    @Test(expected = NullPointerException.class)
    public void compareTo_comparedWithNull() {
        STATISTICAL_GOALS_ABOVE_MIN.compareTo(null);
    }
    @SuppressWarnings("EqualsWithItself")
    @Test
    public void compareTo_comparedWithSameInstance() {
        assertEquals(0, STATISTICAL_GOALS_ABOVE_MIN.compareTo(STATISTICAL_GOALS_ABOVE_MIN));
    }
    @Test
    public void compareTo_comparedWithInstanceOfDifferentValue() {
        assertTrue(EstimatedGoals.MIN_ESTIMATED_GOALS.compareTo(STATISTICAL_GOALS_ABOVE_MIN) < 0);
        assertTrue(STATISTICAL_GOALS_ABOVE_MIN.compareTo(EstimatedGoals.MIN_ESTIMATED_GOALS) > 0);
    }
    @Test
    public void compareTo_comparedWithInstanceOfSameValue() {
        EstimatedGoals estimatedGoalsDownRounded = EstimatedGoals.valueOf(STATISTICAL_GOALS_ABOVE_MIN_VALUE_DOWN_ROUNDED);
        EstimatedGoals estimatedGoalsUpRounded = EstimatedGoals.valueOf(STATISTICAL_GOALS_ABOVE_MIN_VALUE_UP_ROUNDED);

        assertEquals(0, estimatedGoalsDownRounded.compareTo(estimatedGoalsUpRounded));
        assertEquals(0, estimatedGoalsUpRounded.compareTo(estimatedGoalsDownRounded));
    }
}