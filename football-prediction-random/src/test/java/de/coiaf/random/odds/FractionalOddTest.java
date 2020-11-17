package de.coiaf.random.odds;

import de.coiaf.random.probability.Probability;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class FractionalOddTest extends AbstractOddTest<FractionalOdd.OddValue, FractionalOdd> {

    private static final BigDecimal NEGATIVE_VALUE = new BigDecimal("-1");
    private static final BigDecimal POSITIVE_VALUE_BELOW_ONE = new BigDecimal("0.5");
    private static final BigDecimal POSITIVE_VALUE_ABOVE_ONE = new BigDecimal("2");
    private static final FractionalOdd.OddValue ODD_VALUE_NO_GAIN = new FractionalOdd.OddValue(BigDecimal.ZERO, BigDecimal.ONE);
    private static final FractionalOdd.OddValue ODD_VALUE_GAIN = new FractionalOdd.OddValue(BigDecimal.ONE, BigDecimal.ONE);
    private static final FractionalOdd.OddValue ODD_VALUE_MAXIMUM_GAIN = new FractionalOdd.OddValue(Odds.MAX_DECIMAL_ODD_VALUE.subtract(BigDecimal.ONE), BigDecimal.ONE);
    private static final BigDecimal DECIMAL_ODD_VALUE_NO_GAIN = BigDecimal.ONE;
    private static final BigDecimal DECIMAL_ODD_VALUE_GAIN = new BigDecimal("2");
    private static final BigDecimal DECIMAL_ODD_VALUE_NEGATIVE = new BigDecimal("-2");
    private static final BigDecimal DECIMAL_ODD_VALUE_POSITIVE_BELOW_ONE = new BigDecimal("0.5");

    @Test(expected = NullPointerException.class)
    public void from_nullOdd() {
        FractionalOdd.from((Odd) null);
    }
    @Test(expected = NullPointerException.class)
    public void from_oddWithoutImpliedProbability() {
        FractionalOdd.from(OddsTest.MOCK_ODD_NULL_IMPLIED_PROBABILITY);
    }
    @Test
    public void from_oddWithImpossibleImpliedProbability() {
        this.verifyFrom(OddsTest.MOCK_ODD_IMPOSSIBLE_IMPLIED_PROBABILITY);
    }
    @Test
    public void from_oddWithUncertainImpliedProbability() {
        this.verifyFrom(OddsTest.MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY);
    }
    @Test
    public void from_oddWithCertainImpliedProbability() {
        this.verifyFrom(OddsTest.MOCK_ODD_CERTAIN_IMPLIED_PROBABILITY);
    }

    private void verifyFrom(Odd<?> odd) {
        FractionalOdd result = FractionalOdd.from(odd);

        assertNotNull(result);
        assertNotNull(result.getDecimalOddValue());
        assertTrue(odd.getDecimalOddValue().compareTo(result.getDecimalOddValue()) == 0);
        assertNotNull(result.getImpliedProbability());
        assertEquals(odd.getImpliedProbability(), result.getImpliedProbability());
    }

    @Test(expected = NullPointerException.class)
    public void validateOddValue_nullDividend_divisorAboveOne() {
        FractionalOdd.validateOddValue(null, POSITIVE_VALUE_ABOVE_ONE);
    }
    @Test(expected = IllegalArgumentException.class)
    public void validateOddValue_negativeDividend_divisorAboveOne() {
        FractionalOdd.validateOddValue(NEGATIVE_VALUE, POSITIVE_VALUE_ABOVE_ONE);
    }
    @Test
    public void validateOddValue_zeroDividend_divisorAboveOne() {
        FractionalOdd.validateOddValue(BigDecimal.ZERO, POSITIVE_VALUE_ABOVE_ONE);
    }
    @Test
    public void validateOddValue_positiveDividendBelowOne_divisorAboveOne() {
        FractionalOdd.validateOddValue(POSITIVE_VALUE_BELOW_ONE, POSITIVE_VALUE_ABOVE_ONE);
    }
    @Test
    public void validateOddValue_positiveDividendEqualsOne_divisorAboveOne() {
        FractionalOdd.validateOddValue(BigDecimal.ONE, POSITIVE_VALUE_ABOVE_ONE);
    }
    @Test
    public void validateOddValue_positiveDividendAboveOne_divisorAboveOne() {
        FractionalOdd.validateOddValue(POSITIVE_VALUE_ABOVE_ONE, POSITIVE_VALUE_ABOVE_ONE);
    }
    @Test(expected = NullPointerException.class)
    public void validateOddValue_positiveDividendAboveOne_nullDivisor() {
        FractionalOdd.validateOddValue(POSITIVE_VALUE_ABOVE_ONE, null);
    }
    @Test(expected = IllegalArgumentException.class)
    public void validateOddValue_positiveDividendAboveOne_negativeDivisor() {
        FractionalOdd.validateOddValue(POSITIVE_VALUE_ABOVE_ONE, NEGATIVE_VALUE);
    }
    @Test(expected = IllegalArgumentException.class)
    public void validateOddValue_positiveDividendAboveOne_zeroDivisor() {
        FractionalOdd.validateOddValue(POSITIVE_VALUE_ABOVE_ONE, BigDecimal.ZERO);
    }
    @Test(expected = IllegalArgumentException.class)
    public void validateOddValue_positiveDividendAboveOne_positiveDivisorBelowOne() {
        FractionalOdd.validateOddValue(POSITIVE_VALUE_ABOVE_ONE, POSITIVE_VALUE_BELOW_ONE);
    }
    @Test
    public void validateOddValue_positiveDividendAboveOne_positiveDivisorEqualsOne() {
        FractionalOdd.validateOddValue(POSITIVE_VALUE_ABOVE_ONE, BigDecimal.ONE);
    }

    @Test(expected = NullPointerException.class)
    public void convertToDecimalOddValue_nullOddValue() {
        FractionalOdd.convertToDecimalOddValue(null);
    }
    @Test
    public void convertToDecimalOddValue_oddValueNoGain() {
        this.verifyConvertToDecimalOddValue(ODD_VALUE_NO_GAIN, DECIMAL_ODD_VALUE_NO_GAIN);
    }
    @Test
    public void convertToDecimalOddValue_oddValueGain() {
        this.verifyConvertToDecimalOddValue(ODD_VALUE_GAIN, DECIMAL_ODD_VALUE_GAIN);
    }

    private void verifyConvertToDecimalOddValue(FractionalOdd.OddValue oddValue, BigDecimal expectedDecimalValue) {
        BigDecimal result = FractionalOdd.convertToDecimalOddValue(oddValue);

        assertNotNull(result);
        assertTrue(expectedDecimalValue.compareTo(result) == 0);
    }

    @Test(expected = NullPointerException.class)
    public void convertToFractionalOddValue_nullDecimalOddValue() {
        FractionalOdd.convertToFractionalOddValue(null);
    }
    @Test(expected = IllegalArgumentException.class)
    public void convertToFractionalOddValue_negativeDecimalOddValue() {
        FractionalOdd.convertToFractionalOddValue(DECIMAL_ODD_VALUE_NEGATIVE);
    }
    @Test(expected = IllegalArgumentException.class)
    public void convertToFractionalOddValue_zeroDecimalOddValue() {
        FractionalOdd.convertToFractionalOddValue(BigDecimal.ZERO);
    }
    @Test(expected = IllegalArgumentException.class)
    public void convertToFractionalOddValue_positiveDecimalOddValueBelowOne() {
        FractionalOdd.convertToFractionalOddValue(DECIMAL_ODD_VALUE_POSITIVE_BELOW_ONE);
    }
    @Test
    public void convertToFractionalOddValue_positiveDecimalOddValueEqualsOne() {
        this.verifyConvertToFractionalOddValue(BigDecimal.ONE, ODD_VALUE_NO_GAIN);
    }
    @Test
    public void convertToFractionalOddValue_positiveDecimalOddValueAboveOne() {
        this.verifyConvertToFractionalOddValue(DECIMAL_ODD_VALUE_GAIN, ODD_VALUE_GAIN);
    }

    private void verifyConvertToFractionalOddValue(BigDecimal decimalOddValue, FractionalOdd.OddValue expectedOddValue) {
        FractionalOdd.OddValue result = FractionalOdd.convertToFractionalOddValue(decimalOddValue);

        assertNotNull(result);
        assertEquals(expectedOddValue, result);
    }

    @Test
    public void testConstructor_oddValueNoGain() {
        this.verifyConstructorByOddValue(Probability.CERTAIN, BigDecimal.ONE, ODD_VALUE_NO_GAIN);
    }
    @Test
    public void testConstructor_oddValueGain() {
        this.verifyConstructorByOddValue(Probability.UNCERTAIN, EXPECTED_DECIMAL_ODD_VALUE_UNCERTAIN, ODD_VALUE_GAIN);
    }
    @Test
    public void testConstructor_oddValueMaximumGain() {
        this.verifyConstructorByOddValue(Probability.IMPOSSIBLE, Odds.MAX_DECIMAL_ODD_VALUE, ODD_VALUE_MAXIMUM_GAIN);
    }

    @Test
    public void testConstructor_byOddValueEqualsByImpliedProbability_oddValueNoGain() {
        this.verifyConstructorComparision(ODD_VALUE_NO_GAIN);
    }
    @Test
    public void testConstructor_byOddValueEqualsByImpliedProbability_oddValueGain() {
        this.verifyConstructorComparision(ODD_VALUE_GAIN);
    }
    @Test
    public void testConstructor_byOddValueEqualsByImpliedProbability_oddValueMaxGain() {
        this.verifyConstructorComparision(ODD_VALUE_MAXIMUM_GAIN);
    }

    @Override
    protected FractionalOdd createInstance(Probability probability) {
        return new FractionalOdd(probability);
    }

    @Override
    protected FractionalOdd createInstance(FractionalOdd.OddValue oddValue) {
        return new FractionalOdd(oddValue);
    }

    @Override
    protected FractionalOdd.OddValue getOddValueImpossible() {
        return ODD_VALUE_MAXIMUM_GAIN;
    }

    @Override
    protected FractionalOdd.OddValue getOddValueUncertain() {
        return ODD_VALUE_GAIN;
    }

    @Override
    protected FractionalOdd.OddValue getOddValueCertain() {
        return ODD_VALUE_NO_GAIN;
    }

    @Override
    protected boolean equals(FractionalOdd.OddValue expected, FractionalOdd.OddValue result) {
        return expected != null && expected.equals(result);
    }
}