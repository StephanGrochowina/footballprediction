package de.coiaf.random.odds;

import de.coiaf.random.probability.Probabilities;
import de.coiaf.random.probability.Probability;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OddsTest {

    static final Odd<?> MOCK_ODD_NULL_IMPLIED_PROBABILITY = createOddMock(null);
    public static final Odd<?> MOCK_ODD_IMPOSSIBLE_IMPLIED_PROBABILITY = createOddMock(Probability.IMPOSSIBLE);
    public static final Odd<?> MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY = createOddMock(Probability.UNCERTAIN);
    public static final Odd<?> MOCK_ODD_CERTAIN_IMPLIED_PROBABILITY = createOddMock(Probability.CERTAIN);
    private static final BigDecimal DECIMAL_ODD_VALUE_UNCERTAIN = new BigDecimal("2");

    static final BigDecimal ODD_VALUE_ILLEGAL_NEGATIVE = new BigDecimal("-1");
    static final BigDecimal ODD_VALUE_ILLEGAL_ZERO = BigDecimal.ZERO;
    static final BigDecimal ODD_VALUE_ILLEGAL_POSITIVE_BELOW_ONE = new BigDecimal("0.5");

    private static final Object OBJECT_NO_ODD = new Object();
    private static final Probability PROBABILITY_REFERENCE = new Probability(0.2);
    private static final Probability PROBABILITY_COMPARED = new Probability(0.4);
    private static final DecimalOdd ODD_REFERENCE_DECIMAL1 = new DecimalOdd(PROBABILITY_REFERENCE);
    private static final DecimalOdd ODD_REFERENCE_DECIMAL2 = new DecimalOdd(PROBABILITY_REFERENCE);
    private static final DecimalOdd ODD_COMPARED_DECIMAL = new DecimalOdd(PROBABILITY_COMPARED);
    private static final FractionalOdd ODD_REFERENCE_FRACTIONAL = new FractionalOdd(PROBABILITY_REFERENCE);
    private static final FractionalOdd ODD_COMPARED_FRACTIONAL = new FractionalOdd(PROBABILITY_COMPARED);
    private static final AmericanOdd ODD_REFERENCE_AMERICAN = new AmericanOddPositive(PROBABILITY_REFERENCE);
    private static final AmericanOdd ODD_COMPARED_AMERICAN = new AmericanOddPositive(PROBABILITY_COMPARED);
    private static final AmericanOdd ODD_AMERICAN_PLUS_100 = new AmericanOddPositive(100);
    private static final AmericanOdd ODD_AMERICAN_NEGATIVE_100 = new AmericanOddNegative(-100);

    private static Odd<?> createOddMock(Probability impliedProbability) {
        Odd<?> oddMock = mock(Odd.class);

        when(oddMock.getOddValue()).thenReturn(null);

        if (impliedProbability == null) {
            when(oddMock.getDecimalOddValue()).thenReturn(null);
            when(oddMock.getImpliedProbability()).thenReturn(null);
            when(oddMock.getNormalizedDecimalOddValue()).thenReturn(null);
        }
        else {
            when(oddMock.getDecimalOddValue()).thenReturn(Odds.convertProbabilityToDecimalOddValue(impliedProbability));
            when(oddMock.getImpliedProbability()).thenReturn(impliedProbability);
            when(oddMock.getNormalizedDecimalOddValue()).thenCallRealMethod();
        }

        return oddMock;
    }

    @Test(expected = NullPointerException.class)
    public void convertProbabilityToDecimalOddValue_nullProbability() {
        Odds.convertProbabilityToDecimalOddValue(null);
    }
    @Test
    public void convertProbabilityToDecimalOddValue_impossibleProbability() {
        this.verifyConvertProbabilityToDecimalOddValue(Probability.IMPOSSIBLE, Odds.MAX_DECIMAL_ODD_VALUE);
    }
    @Test
    public void convertProbabilityToDecimalOddValue_uncertainProbability() {
        this.verifyConvertProbabilityToDecimalOddValue(Probability.UNCERTAIN, DECIMAL_ODD_VALUE_UNCERTAIN);
    }
    @Test
    public void convertProbabilityToDecimalOddValue_certainProbability() {
        this.verifyConvertProbabilityToDecimalOddValue(Probability.CERTAIN, BigDecimal.ONE);
    }

    private void verifyConvertProbabilityToDecimalOddValue(Probability probability, BigDecimal expectedDecimalOddValue) {
        BigDecimal result = Odds.convertProbabilityToDecimalOddValue(probability);

        assertNotNull(result);
        assertEquals(0, expectedDecimalOddValue.compareTo(result));
    }

    @Test(expected = NullPointerException.class)
    public void convertDecimalOddValueToProbability_nullDecimalOddValue() {
        Odds.convertDecimalOddValueToProbability(null);
    }
    @Test(expected = IllegalArgumentException.class)
    public void convertDecimalOddValueToProbability_negativeDecimalOddValue() {
        Odds.convertDecimalOddValueToProbability(ODD_VALUE_ILLEGAL_NEGATIVE);
    }
    @Test(expected = IllegalArgumentException.class)
    public void convertDecimalOddValueToProbability_zeroDecimalOddValue() {
        Odds.convertDecimalOddValueToProbability(ODD_VALUE_ILLEGAL_ZERO);
    }
    @Test(expected = IllegalArgumentException.class)
    public void convertDecimalOddValueToProbability_positiveDecimalOddValueBelowOne() {
        Odds.convertDecimalOddValueToProbability(ODD_VALUE_ILLEGAL_POSITIVE_BELOW_ONE);
    }
    @Test
    public void convertDecimalOddValueToProbability_positiveDecimalOddValueEqualsOne() {
        this.verifyConvertDecimalOddValueToProbability(BigDecimal.ONE, Probability.CERTAIN);
    }
    @Test
    public void convertDecimalOddValueToProbability_positiveDecimalOddValueAboveOne() {
        this.verifyConvertDecimalOddValueToProbability(DECIMAL_ODD_VALUE_UNCERTAIN, Probability.UNCERTAIN);
    }
    @Test
    public void convertDecimalOddValueToProbability_maxPositiveDecimalOddValue() {
        this.verifyConvertDecimalOddValueToProbability(Odds.MAX_DECIMAL_ODD_VALUE, Probability.IMPOSSIBLE);
    }

    private void verifyConvertDecimalOddValueToProbability(BigDecimal decimalOddValue, Probability expectedProbability) {
        Probability result = Odds.convertDecimalOddValueToProbability(decimalOddValue);

        assertNotNull(result);
        assertEquals(expectedProbability, result);
    }

    @Test
    public void isMaxDecimalOddValue_nullOddValue() {
        this.verifyIsMaxDecimalOddValue(null, false);
    }
    @Test
    public void isMaxDecimalOddValue_negativeOddValue() {
        this.verifyIsMaxDecimalOddValue(ODD_VALUE_ILLEGAL_NEGATIVE, false);
    }
    @Test
    public void isMaxDecimalOddValue_zeroOddValue() {
        this.verifyIsMaxDecimalOddValue(ODD_VALUE_ILLEGAL_ZERO, false);
    }
    @Test
    public void isMaxDecimalOddValue_positiveOddValueBelowOne() {
        this.verifyIsMaxDecimalOddValue(ODD_VALUE_ILLEGAL_POSITIVE_BELOW_ONE, false);
    }
    @Test
    public void isMaxDecimalOddValue_positiveOddValueEqualsOne() {
        this.verifyIsMaxDecimalOddValue(BigDecimal.ONE, false);
    }
    @Test
    public void isMaxDecimalOddValue_positiveOddValueAboveOne() {
        this.verifyIsMaxDecimalOddValue(DECIMAL_ODD_VALUE_UNCERTAIN, false);
    }
    @Test
    public void isMaxDecimalOddValue_maxPositiveDecimalOddValue() {
        this.verifyIsMaxDecimalOddValue(Odds.MAX_DECIMAL_ODD_VALUE, true);
    }

    private void verifyIsMaxDecimalOddValue(BigDecimal decimalOddValue, boolean expected) {
        boolean result = Probabilities.isMaxInvertibleValue(decimalOddValue);

        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void validateDecimalOddValue_nullDecimalOddValue() {
        Odds.validateDecimalOddValue(null);
    }
    @Test(expected = IllegalArgumentException.class)
    public void validateDecimalOddValue_negativeDecimalOddValue() {
        Odds.validateDecimalOddValue(ODD_VALUE_ILLEGAL_NEGATIVE);
    }
    @Test(expected = IllegalArgumentException.class)
    public void validateDecimalOddValue_zeroDecimalOddValue() {
        Odds.validateDecimalOddValue(ODD_VALUE_ILLEGAL_ZERO);
    }
    @Test(expected = IllegalArgumentException.class)
    public void validateDecimalOddValue_positiveDecimalOddValueBelowOne() {
        Odds.validateDecimalOddValue(ODD_VALUE_ILLEGAL_POSITIVE_BELOW_ONE);
    }
    @Test
    public void validateDecimalOddValue_positiveDecimalOddValueEqualsOne() {
        Odds.validateDecimalOddValue(BigDecimal.ONE);
    }
    @Test
    public void validateDecimalOddValue_positiveDecimalOddValueAboveOne() {
        Odds.validateDecimalOddValue(DECIMAL_ODD_VALUE_UNCERTAIN);
    }
    @Test
    public void validateDecimalOddValue_maxPositiveDecimalOddValue() {
        Odds.validateDecimalOddValue(Odds.MAX_DECIMAL_ODD_VALUE);
    }

    @Test
    public void hashCode_nullOdd() {
        this.verifyHashCode(null);
    }
    @Test
    public void hashCode_impossibleOdd() {
        this.verifyHashCode(MOCK_ODD_IMPOSSIBLE_IMPLIED_PROBABILITY);
    }
    @Test
    public void hashCode_uncertainOdd() {
        this.verifyHashCode(MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY);
    }
    @Test
    public void hashCode_certainOdd() {
        this.verifyHashCode(MOCK_ODD_CERTAIN_IMPLIED_PROBABILITY);
    }

    private void verifyHashCode(Odd<?> odd) {
        Integer hashCode = Odds.hashCode(odd);

        assertNotNull(hashCode);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test(expected = NullPointerException.class)
    public void equals_nullOdd_givenObject() {
        Odds.equals(null, OBJECT_NO_ODD);
    }
    @Test
    public void equals_givenOdd_nullObject() {
        boolean result;

        result = Odds.equals(ODD_REFERENCE_DECIMAL1, null);
        assertFalse(result);

        result = Odds.equals(ODD_REFERENCE_FRACTIONAL, null);
        assertFalse(result);

        result = Odds.equals(ODD_REFERENCE_AMERICAN, null);
        assertFalse(result);
    }
    @Test
    public void equals_givenOdd_givenObjectNoOdd() {
        boolean result;

        result = Odds.equals(ODD_REFERENCE_DECIMAL1, OBJECT_NO_ODD);
        assertFalse(result);

        result = Odds.equals(ODD_REFERENCE_FRACTIONAL, OBJECT_NO_ODD);
        assertFalse(result);

        result = Odds.equals(ODD_REFERENCE_AMERICAN, OBJECT_NO_ODD);
        assertFalse(result);
    }
    @Test
    public void equals_givenOdd_givenObjectDifferentOddClassWithSameImpliedProbability() {
        boolean result;

        result = Odds.equals(ODD_REFERENCE_DECIMAL1, ODD_REFERENCE_FRACTIONAL);
        assertFalse(result);

        result = Odds.equals(ODD_REFERENCE_DECIMAL1, ODD_REFERENCE_AMERICAN);
        assertFalse(result);

        result = Odds.equals(ODD_REFERENCE_FRACTIONAL, ODD_REFERENCE_DECIMAL1);
        assertFalse(result);

        result = Odds.equals(ODD_REFERENCE_FRACTIONAL, ODD_REFERENCE_AMERICAN);
        assertFalse(result);

        result = Odds.equals(ODD_REFERENCE_AMERICAN, ODD_REFERENCE_DECIMAL1);
        assertFalse(result);

        result = Odds.equals(ODD_REFERENCE_AMERICAN, ODD_REFERENCE_FRACTIONAL);
        assertFalse(result);
    }
    @Test
    public void equals_givenOdd_givenObjectDifferentOddClassWithDifferentImpliedProbability() {
        boolean result;

        result = Odds.equals(ODD_REFERENCE_DECIMAL1, ODD_COMPARED_FRACTIONAL);
        assertFalse(result);

        result = Odds.equals(ODD_REFERENCE_DECIMAL1, ODD_COMPARED_AMERICAN);
        assertFalse(result);

        result = Odds.equals(ODD_REFERENCE_FRACTIONAL, ODD_COMPARED_DECIMAL);
        assertFalse(result);

        result = Odds.equals(ODD_REFERENCE_FRACTIONAL, ODD_COMPARED_AMERICAN);
        assertFalse(result);

        result = Odds.equals(ODD_REFERENCE_AMERICAN, ODD_COMPARED_DECIMAL);
        assertFalse(result);

        result = Odds.equals(ODD_REFERENCE_AMERICAN, ODD_COMPARED_FRACTIONAL);
        assertFalse(result);
    }
    @Test
    public void equals_givenOdd_givenObjectSameOddClassWithSameImpliedProbability() {
        boolean result;

        result = Odds.equals(ODD_REFERENCE_DECIMAL1, ODD_REFERENCE_DECIMAL2);
        assertTrue(result);

        result = Odds.equals(ODD_AMERICAN_PLUS_100, ODD_AMERICAN_NEGATIVE_100);
        assertFalse(result);
    }
    @Test
    public void equals_givenOdd_givenObjectSameOddInstance() {
        boolean result;

        result = Odds.equals(ODD_REFERENCE_DECIMAL1, ODD_REFERENCE_DECIMAL1);
        assertTrue(result);

        result = Odds.equals(ODD_REFERENCE_FRACTIONAL, ODD_REFERENCE_FRACTIONAL);
        assertTrue(result);

        result = Odds.equals(ODD_REFERENCE_AMERICAN, ODD_REFERENCE_AMERICAN);
        assertTrue(result);
    }

    @Test
    public void equalsOdd_nullOdd_nullOdd() {
        boolean result = Odds.equalsOdd(null, null);

        assertTrue(result);
    }
    @Test
    public void equalsOdd_nullOdd_givenOdd() {
        boolean result = Odds.equalsOdd(null, ODD_COMPARED_DECIMAL);

        assertFalse(result);
    }
    @Test
    public void equalsOdd_givenOdd_nullOdd() {
        boolean result = Odds.equalsOdd(ODD_REFERENCE_DECIMAL1, null);

        assertFalse(result);
    }
    @Test
    public void equalsOdd_givenOdd_givenOddSameImpliedProbability() {
        boolean result;

        result = Odds.equalsOdd(ODD_REFERENCE_DECIMAL1, ODD_REFERENCE_DECIMAL2);
        assertTrue(result);

        result = Odds.equalsOdd(ODD_REFERENCE_DECIMAL1, ODD_REFERENCE_FRACTIONAL);
        assertTrue(result);

        result = Odds.equalsOdd(ODD_REFERENCE_DECIMAL1, ODD_REFERENCE_AMERICAN);
        assertTrue(result);
    }
    @Test
    public void equalsOdd_givenOdd_givenOddDifferentImpliedProbability() {
        boolean result;

        result = Odds.equalsOdd(ODD_REFERENCE_DECIMAL1, ODD_COMPARED_DECIMAL);
        assertFalse(result);

        result = Odds.equalsOdd(ODD_REFERENCE_DECIMAL1, ODD_COMPARED_FRACTIONAL);
        assertFalse(result);

        result = Odds.equalsOdd(ODD_REFERENCE_DECIMAL1, ODD_COMPARED_AMERICAN);
        assertFalse(result);

        result = Odds.equalsOdd(ODD_REFERENCE_FRACTIONAL, ODD_COMPARED_DECIMAL);
        assertFalse(result);

        result = Odds.equalsOdd(ODD_REFERENCE_FRACTIONAL, ODD_COMPARED_FRACTIONAL);
        assertFalse(result);

        result = Odds.equalsOdd(ODD_REFERENCE_FRACTIONAL, ODD_COMPARED_AMERICAN);
        assertFalse(result);

        result = Odds.equalsOdd(ODD_REFERENCE_AMERICAN, ODD_COMPARED_DECIMAL);
        assertFalse(result);

        result = Odds.equalsOdd(ODD_REFERENCE_AMERICAN, ODD_COMPARED_FRACTIONAL);
        assertFalse(result);

        result = Odds.equalsOdd(ODD_REFERENCE_AMERICAN, ODD_COMPARED_AMERICAN);
        assertFalse(result);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NullPointerException.class)
    public void requireNotEmpty_nullCollection() {
        Odds.requireNotEmpty((Collection) null);
    }
    @Test(expected = IllegalArgumentException.class)
    public void requireNotEmpty_emptyCollection() {
        Odds.requireNotEmpty(Collections.emptyList());
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NullPointerException.class)
    public void requireNotEmpty_nullMap() {
        Odds.requireNotEmpty((Map) null);
    }
    @Test(expected = IllegalArgumentException.class)
    public void requireNotEmpty_emptyMap() {
        Odds.requireNotEmpty(Collections.emptyMap());
    }
}