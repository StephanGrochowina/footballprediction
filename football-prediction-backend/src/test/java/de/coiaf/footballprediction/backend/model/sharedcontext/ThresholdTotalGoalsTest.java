package de.coiaf.footballprediction.backend.model.sharedcontext;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@Deprecated
public class ThresholdTotalGoalsTest {

    private static final BigDecimal NEGATIVE_THRESHOLD_VALUE = new BigDecimal("-0.5");
    private static final BigDecimal ZERO_THRESHOLD_VALUE = BigDecimal.ZERO;
    private static final BigDecimal MIN_THRESHOLD_VALUE = BigDecimal.valueOf(Double.MIN_VALUE);
    private static final BigDecimal THRESHOLD_VALUE_0_5 = new BigDecimal("0.50");
    private static final BigDecimal ONE_THRESHOLD_VALUE = BigDecimal.ONE;
    private static final BigDecimal MIN_THRESHOLD_ABOVE_1 = BigDecimal.ONE.add(MIN_THRESHOLD_VALUE);
    private static final BigDecimal THRESHOLD_VALUE_1_5 = new BigDecimal("1.50");
    private static final BigDecimal TWO_THRESHOLD_VALUE = new BigDecimal("2");
    private static final BigDecimal MIN_THRESHOLD_ABOVE_2 = TWO_THRESHOLD_VALUE.add(MIN_THRESHOLD_VALUE);
    private static final BigDecimal THRESHOLD_VALUE_2_5 = new BigDecimal("2.50");
    private static final BigDecimal THREE_THRESHOLD_VALUE = new BigDecimal("3");

    @Test
    public void getDefaultInstance() {
        ThresholdTotalGoals result = ThresholdTotalGoals.getDefaultInstance();

        assertNotNull(result);
        assertSame(ThresholdTotalGoals.DEFAULT_THRESHOLD, result);
    }

    @Test
    public void getMinThreshold() {
        ThresholdTotalGoals result = ThresholdTotalGoals.getMinThreshold();

        assertNotNull(result);
        assertSame(ThresholdTotalGoals.MIN_THRESHOLD, result);
    }

    @Test(expected = NullPointerException.class)
    public void valueOf_nullN() {
        ThresholdTotalGoals.valueOf(null);
    }
    @Test(expected = IllegalArgumentException.class)
    public void valueOf_negativeN() {
        ThresholdTotalGoals.valueOf(NEGATIVE_THRESHOLD_VALUE);
    }
    @Test(expected = IllegalArgumentException.class)
    public void valueOf_zeroN() {
        ThresholdTotalGoals.valueOf(ZERO_THRESHOLD_VALUE);
    }
    @Test
    public void valueOf_positiveNUpToOne() {
        ThresholdTotalGoals expected = ThresholdTotalGoals.MIN_THRESHOLD;

        ThresholdTotalGoals thresholdMin = ThresholdTotalGoals.valueOf(MIN_THRESHOLD_VALUE);
        ThresholdTotalGoals threshold0_5 = ThresholdTotalGoals.valueOf(THRESHOLD_VALUE_0_5);
        ThresholdTotalGoals threshold1 = ThresholdTotalGoals.valueOf(ONE_THRESHOLD_VALUE);

        assertNotNull(thresholdMin);
        assertSame(expected, thresholdMin);

        assertNotNull(threshold0_5);
        assertSame(expected, threshold0_5);

        assertNotNull(threshold1);
        assertSame(expected, threshold1);
    }
    @Test
    public void valueOf_positiveNAboveOneUpToTwo() {
        ThresholdTotalGoals thresholdMinAbove1 = ThresholdTotalGoals.valueOf(MIN_THRESHOLD_ABOVE_1);
        ThresholdTotalGoals threshold1_5 = ThresholdTotalGoals.valueOf(THRESHOLD_VALUE_1_5);
        ThresholdTotalGoals threshold2 = ThresholdTotalGoals.valueOf(TWO_THRESHOLD_VALUE);

        assertNotNull(thresholdMinAbove1);
        assertNotEquals(ThresholdTotalGoals.MIN_THRESHOLD, thresholdMinAbove1);
        assertNotNull(threshold1_5);
        assertNotEquals(ThresholdTotalGoals.MIN_THRESHOLD, threshold1_5);
        assertNotNull(threshold2);
        assertNotEquals(ThresholdTotalGoals.MIN_THRESHOLD, threshold2);

        assertEquals(threshold1_5, thresholdMinAbove1);
        assertNotSame(threshold1_5, thresholdMinAbove1);
        assertEquals(threshold1_5, threshold2);
        assertNotSame(threshold1_5, threshold2);
    }
    @Test
    public void valueOf_positiveNAboveTwoUpToThree() {
        ThresholdTotalGoals expected = ThresholdTotalGoals.DEFAULT_THRESHOLD;

        ThresholdTotalGoals thresholdMinAbove2 = ThresholdTotalGoals.valueOf(MIN_THRESHOLD_ABOVE_2);
        ThresholdTotalGoals threshold2_5 = ThresholdTotalGoals.valueOf(THRESHOLD_VALUE_2_5);
        ThresholdTotalGoals threshold3 = ThresholdTotalGoals.valueOf(THREE_THRESHOLD_VALUE);

        assertNotNull(thresholdMinAbove2);
        assertSame(expected, thresholdMinAbove2);

        assertNotNull(threshold2_5);
        assertSame(expected, threshold2_5);

        assertNotNull(threshold3);
        assertSame(expected, threshold3);
    }

    @Test(expected = NullPointerException.class)
    public void valueOf_nullN_illegalThresholdNotAllowed() {
        ThresholdTotalGoals.valueOf(null, false);
    }
    @Test(expected = IllegalArgumentException.class)
    public void valueOf_negativeN_illegalThresholdNotAllowed() {
        ThresholdTotalGoals.valueOf(NEGATIVE_THRESHOLD_VALUE, false);
    }
    @Test(expected = IllegalArgumentException.class)
    public void valueOf_zeroN_illegalThresholdNotAllowed() {
        ThresholdTotalGoals.valueOf(ZERO_THRESHOLD_VALUE, false);
    }
    @Test
    public void valueOf_positiveNUpToOne_illegalThresholdNotAllowed() {
        ThresholdTotalGoals expected = ThresholdTotalGoals.MIN_THRESHOLD;

        ThresholdTotalGoals thresholdMin = ThresholdTotalGoals.valueOf(MIN_THRESHOLD_VALUE, false);
        ThresholdTotalGoals threshold0_5 = ThresholdTotalGoals.valueOf(THRESHOLD_VALUE_0_5, false);
        ThresholdTotalGoals threshold1 = ThresholdTotalGoals.valueOf(ONE_THRESHOLD_VALUE, false);

        assertNotNull(thresholdMin);
        assertSame(expected, thresholdMin);

        assertNotNull(threshold0_5);
        assertSame(expected, threshold0_5);

        assertNotNull(threshold1);
        assertSame(expected, threshold1);
    }
    @Test
    public void valueOf_positiveNAboveOneUpToTwo_illegalThresholdNotAllowed() {
        ThresholdTotalGoals thresholdMinAbove1 = ThresholdTotalGoals.valueOf(MIN_THRESHOLD_ABOVE_1, false);
        ThresholdTotalGoals threshold1_5 = ThresholdTotalGoals.valueOf(THRESHOLD_VALUE_1_5, false);
        ThresholdTotalGoals threshold2 = ThresholdTotalGoals.valueOf(TWO_THRESHOLD_VALUE, false);

        assertNotNull(thresholdMinAbove1);
        assertNotEquals(ThresholdTotalGoals.MIN_THRESHOLD, thresholdMinAbove1);
        assertNotNull(threshold1_5);
        assertNotEquals(ThresholdTotalGoals.MIN_THRESHOLD, threshold1_5);
        assertNotNull(threshold2);
        assertNotEquals(ThresholdTotalGoals.MIN_THRESHOLD, threshold2);

        assertEquals(threshold1_5, thresholdMinAbove1);
        assertNotSame(threshold1_5, thresholdMinAbove1);
        assertEquals(threshold1_5, threshold2);
        assertNotSame(threshold1_5, threshold2);
    }
    @Test
    public void valueOf_positiveNAboveTwoUpToThree_illegalThresholdNotAllowed() {
        ThresholdTotalGoals expected = ThresholdTotalGoals.DEFAULT_THRESHOLD;

        ThresholdTotalGoals thresholdMinAbove2 = ThresholdTotalGoals.valueOf(MIN_THRESHOLD_ABOVE_2, false);
        ThresholdTotalGoals threshold2_5 = ThresholdTotalGoals.valueOf(THRESHOLD_VALUE_2_5, false);
        ThresholdTotalGoals threshold3 = ThresholdTotalGoals.valueOf(THREE_THRESHOLD_VALUE, false);

        assertNotNull(thresholdMinAbove2);
        assertSame(expected, thresholdMinAbove2);

        assertNotNull(threshold2_5);
        assertSame(expected, threshold2_5);

        assertNotNull(threshold3);
        assertSame(expected, threshold3);
    }

    @Test
    public void valueOf_nullN_illegalThresholdAllowed() {
        ThresholdTotalGoals result = ThresholdTotalGoals.valueOf(null, true);

        assertNull(result);
    }
    @Test
    public void valueOf_negativeN_illegalThresholdAllowed() {
        ThresholdTotalGoals result = ThresholdTotalGoals.valueOf(NEGATIVE_THRESHOLD_VALUE, true);

        assertNotNull(result);
        assertSame(ThresholdTotalGoals.MIN_THRESHOLD, result);
    }
    @Test
    public void valueOf_zeroN_illegalThresholdAllowed() {
        ThresholdTotalGoals result = ThresholdTotalGoals.valueOf(ZERO_THRESHOLD_VALUE, true);

        assertNotNull(result);
        assertSame(ThresholdTotalGoals.MIN_THRESHOLD, result);
    }
    @Test
    public void valueOf_positiveNUpToOne_illegalThresholdAllowed() {
        ThresholdTotalGoals expected = ThresholdTotalGoals.MIN_THRESHOLD;

        ThresholdTotalGoals thresholdMin = ThresholdTotalGoals.valueOf(MIN_THRESHOLD_VALUE, true);
        ThresholdTotalGoals threshold0_5 = ThresholdTotalGoals.valueOf(THRESHOLD_VALUE_0_5, true);
        ThresholdTotalGoals threshold1 = ThresholdTotalGoals.valueOf(ONE_THRESHOLD_VALUE, true);

        assertNotNull(thresholdMin);
        assertSame(expected, thresholdMin);

        assertNotNull(threshold0_5);
        assertSame(expected, threshold0_5);

        assertNotNull(threshold1);
        assertSame(expected, threshold1);
    }
    @Test
    public void valueOf_positiveNAboveOneUpToTwo_illegalThresholdAllowed() {
        ThresholdTotalGoals thresholdMinAbove1 = ThresholdTotalGoals.valueOf(MIN_THRESHOLD_ABOVE_1, true);
        ThresholdTotalGoals threshold1_5 = ThresholdTotalGoals.valueOf(THRESHOLD_VALUE_1_5, true);
        ThresholdTotalGoals threshold2 = ThresholdTotalGoals.valueOf(TWO_THRESHOLD_VALUE, true);

        assertNotNull(thresholdMinAbove1);
        assertNotEquals(ThresholdTotalGoals.MIN_THRESHOLD, thresholdMinAbove1);
        assertNotNull(threshold1_5);
        assertNotEquals(ThresholdTotalGoals.MIN_THRESHOLD, threshold1_5);
        assertNotNull(threshold2);
        assertNotEquals(ThresholdTotalGoals.MIN_THRESHOLD, threshold2);

        assertEquals(threshold1_5, thresholdMinAbove1);
        assertNotSame(threshold1_5, thresholdMinAbove1);
        assertEquals(threshold1_5, threshold2);
        assertNotSame(threshold1_5, threshold2);
    }
    @Test
    public void valueOf_positiveNAboveTwoUpToThree_illegalThresholdAllowed() {
        ThresholdTotalGoals expected = ThresholdTotalGoals.DEFAULT_THRESHOLD;

        ThresholdTotalGoals thresholdMinAbove2 = ThresholdTotalGoals.valueOf(MIN_THRESHOLD_ABOVE_2, true);
        ThresholdTotalGoals threshold2_5 = ThresholdTotalGoals.valueOf(THRESHOLD_VALUE_2_5, true);
        ThresholdTotalGoals threshold3 = ThresholdTotalGoals.valueOf(THREE_THRESHOLD_VALUE, true);

        assertNotNull(thresholdMinAbove2);
        assertSame(expected, thresholdMinAbove2);

        assertNotNull(threshold2_5);
        assertSame(expected, threshold2_5);

        assertNotNull(threshold3);
        assertSame(expected, threshold3);
    }

    @SuppressWarnings({"SimplifiableJUnitAssertion", "ConstantConditions"})
    @Test
    public void equals_comparedWithNull() {
        ThresholdTotalGoals threshold1_5 = ThresholdTotalGoals.valueOf(THRESHOLD_VALUE_1_5, true);

        assertFalse(threshold1_5.equals(null));
    }
    @SuppressWarnings({"SimplifiableJUnitAssertion", "EqualsBetweenInconvertibleTypes"})
    @Test
    public void equals_comparedWithDifferentClass() {
        ThresholdTotalGoals threshold1_5 = ThresholdTotalGoals.valueOf(THRESHOLD_VALUE_1_5, true);

        assertFalse(threshold1_5.equals(THRESHOLD_VALUE_1_5));
    }
    @SuppressWarnings({"SimplifiableJUnitAssertion", "EqualsWithItself"})
    @Test
    public void equals_comparedWithSameInstance() {
        ThresholdTotalGoals threshold1_5 = ThresholdTotalGoals.valueOf(THRESHOLD_VALUE_1_5, true);

        assertTrue(threshold1_5.equals(threshold1_5));
    }
    @SuppressWarnings("SimplifiableJUnitAssertion")
    @Test
    public void equals_comparedWithInstanceOfDifferentValue() {
        ThresholdTotalGoals threshold1_5 = ThresholdTotalGoals.valueOf(THRESHOLD_VALUE_1_5, true);
        ThresholdTotalGoals defaultThreshold = ThresholdTotalGoals.getDefaultInstance();

        assertFalse(threshold1_5.equals(defaultThreshold));
        assertFalse(defaultThreshold.equals(threshold1_5));
    }
    @SuppressWarnings("SimplifiableJUnitAssertion")
    @Test
    public void equals_comparedWithInstanceOfSameValue() {
        ThresholdTotalGoals threshold1_5 = ThresholdTotalGoals.valueOf(THRESHOLD_VALUE_1_5, true);
        ThresholdTotalGoals thresholdMinAbove1 = ThresholdTotalGoals.valueOf(MIN_THRESHOLD_ABOVE_1, true);

        assertTrue(threshold1_5.equals(thresholdMinAbove1));
        assertTrue(thresholdMinAbove1.equals(threshold1_5));
    }

    @Test(expected = NullPointerException.class)
    public void compareTo_comparedWithNull() {
        ThresholdTotalGoals threshold1_5 = ThresholdTotalGoals.valueOf(THRESHOLD_VALUE_1_5, true);

        threshold1_5.compareTo(null);
    }
    @SuppressWarnings("EqualsWithItself")
    @Test
    public void compareTo_comparedWithSameInstance() {
        ThresholdTotalGoals threshold1_5 = ThresholdTotalGoals.valueOf(THRESHOLD_VALUE_1_5, true);

        assertEquals(0, threshold1_5.compareTo(threshold1_5));
    }
    @Test
    public void compareTo_comparedWithInstanceOfDifferentValue() {
        ThresholdTotalGoals threshold1_5 = ThresholdTotalGoals.valueOf(THRESHOLD_VALUE_1_5, true);
        ThresholdTotalGoals defaultThreshold = ThresholdTotalGoals.getDefaultInstance();

        assertTrue(threshold1_5.compareTo(defaultThreshold) < 0);
        assertTrue(defaultThreshold.compareTo(threshold1_5) > 0);
    }
    @Test
    public void compareTo_comparedWithInstanceOfSameValue() {
        ThresholdTotalGoals threshold1_5 = ThresholdTotalGoals.valueOf(THRESHOLD_VALUE_1_5, true);
        ThresholdTotalGoals thresholdMinAbove1 = ThresholdTotalGoals.valueOf(MIN_THRESHOLD_ABOVE_1, true);

        assertEquals(0, threshold1_5.compareTo(thresholdMinAbove1));
        assertEquals(0, thresholdMinAbove1.compareTo(threshold1_5));
    }
}