package de.coiaf.random.odds;

import de.coiaf.random.probability.Probability;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public abstract class AmericanOddTest extends AbstractOddTest<Integer, AmericanOdd> {

    protected static final BigDecimal EXPECTED_DECIMAL_ODD_FOR_ABS_AMERICAN_ODD_100 = EXPECTED_DECIMAL_ODD_VALUE_UNCERTAIN;

    @Test
    public void convertToDecimalOddValue_zeroAmericanOddValue() {
        BigDecimal expectedDecimalOddValue = BigDecimal.ONE;
        int americanOddValue = 0;

        verifyConvertToDecimalOddValue(americanOddValue, expectedDecimalOddValue);
    }
    @Test
    public void convertToDecimalOddValue_positiveAmericanOddValue() {
        int americanOddValue = 100;

        verifyConvertToDecimalOddValue(americanOddValue, EXPECTED_DECIMAL_ODD_FOR_ABS_AMERICAN_ODD_100);
    }
    @Test
    public void convertToDecimalOddValue_positiveInfiniteAmericanOddValue() {
        int americanOddValue = Integer.MAX_VALUE;
        BigDecimal expectedDecimalOddValue = Odds.MAX_DECIMAL_ODD_VALUE;

        verifyConvertToDecimalOddValue(americanOddValue, expectedDecimalOddValue);
    }
    @Test
    public void convertToDecimalOddValue_negativeAmericanOddValue() {
        int americanOddValue = -100;

        verifyConvertToDecimalOddValue(americanOddValue, EXPECTED_DECIMAL_ODD_FOR_ABS_AMERICAN_ODD_100);
    }
    @Test
    public void convertToDecimalOddValue_negativeInfiniteAmericanOddValue() {
        int americanOddValue = -Integer.MAX_VALUE;
        BigDecimal expectedDecimalOddValue = Odds.MAX_DECIMAL_ODD_VALUE;

        verifyConvertToDecimalOddValue(americanOddValue, expectedDecimalOddValue);
    }

    private void verifyConvertToDecimalOddValue(int americanOddValue, BigDecimal expectedDecimalOddValue) {
        BigDecimal result;
        result = this.determineDecimalOddValue(americanOddValue);

        assertNotNull(result);
        assertEquals(0, expectedDecimalOddValue.compareTo(result));
    }

    protected abstract BigDecimal determineDecimalOddValue(int americanOddValue);

    @Test(expected = NullPointerException.class)
    public void testConstructor_nullOddValue() {
        this.createInstance((Integer) null);
    }
    @Test
    public void testConstructor_zeroOddValue() {
        this.verifyConstructorByOddValue(Probability.CERTAIN, BigDecimal.ONE, 0);
    }
    @Test
    public void testConstructor_positiveOddValue() {
        this.verifyConstructorByOddValue(Probability.UNCERTAIN, EXPECTED_DECIMAL_ODD_VALUE_UNCERTAIN, 100);
    }
    @Test
    public void testConstructor_maxPositiveOddValue() {
        this.verifyConstructorByOddValue(Probability.IMPOSSIBLE, Odds.MAX_DECIMAL_ODD_VALUE, Integer.MAX_VALUE);
    }
    @Test
    public void testConstructor_negativeOddValue() {
        this.verifyConstructorByOddValue(Probability.UNCERTAIN, EXPECTED_DECIMAL_ODD_VALUE_UNCERTAIN, -100);
    }
    @Test
    public void testConstructor_maxNegativeOddValue() {
        this.verifyConstructorByOddValue(Probability.IMPOSSIBLE, Odds.MAX_DECIMAL_ODD_VALUE, -Integer.MAX_VALUE);
    }
    @Test
    public void testConstructor_minNegativeOddValue() {
        this.verifyConstructorByOddValue(Probability.IMPOSSIBLE, Odds.MAX_DECIMAL_ODD_VALUE, Integer.MIN_VALUE);
    }

    @Test
    public void testConstructor_byOddValueEqualsByImpliedProbability_zeroOddValue() {
        this.verifyConstructorComparision(0);
    }
    @Test
    public void testConstructor_byOddValueEqualsByImpliedProbability_positiveOddValue() {
        this.verifyConstructorComparision(100);
    }
    @Test
    public void testConstructor_byOddValueEqualsByImpliedProbability_maxPositiveOddValue() {
        this.verifyConstructorComparision(Integer.MAX_VALUE);
    }
    @Test
    public void testConstructor_byOddValueEqualsByImpliedProbability_negativeOddValue() {
        this.verifyConstructorComparision(-100);
    }
    @Test
    public void testConstructor_byOddValueEqualsByImpliedProbability_maxNegativeOddValue() {
        this.verifyConstructorComparision(-Integer.MAX_VALUE);
    }

    @Override
    protected boolean equals(Integer expected, Integer result) {
        return expected != null && expected.equals(result);
    }
}