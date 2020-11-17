package de.coiaf.random.odds;

import de.coiaf.random.probability.Probability;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class DecimalOddTest extends AbstractOddTest<BigDecimal, DecimalOdd> {

    private static final BigDecimal ODD_VALUE_ILLEGAL_NEGATIVE = OddsTest.ODD_VALUE_ILLEGAL_NEGATIVE;
    private static final BigDecimal ODD_VALUE_ILLEGAL_ZERO = OddsTest.ODD_VALUE_ILLEGAL_ZERO;
    private static final BigDecimal ODD_VALUE_ILLEGAL_POSITIVE_BELOW_ONE = OddsTest.ODD_VALUE_ILLEGAL_POSITIVE_BELOW_ONE;

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_negativeOddValue() {
        this.createInstance(ODD_VALUE_ILLEGAL_NEGATIVE);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_zeroOddValue() {
        this.createInstance(ODD_VALUE_ILLEGAL_ZERO);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_positiveOddValueBelowOne() {
        this.createInstance(ODD_VALUE_ILLEGAL_POSITIVE_BELOW_ONE);
    }
    @Test
    public void testConstructor_positiveOddValueEqualsOne() {
        this.verifyConstructorByOddValue(Probability.CERTAIN, BigDecimal.ONE, BigDecimal.ONE);
    }
    @Test
    public void testConstructor_positiveOddValueAboveOne() {
        this.verifyConstructorByOddValue(Probability.UNCERTAIN, EXPECTED_DECIMAL_ODD_VALUE_UNCERTAIN, EXPECTED_DECIMAL_ODD_VALUE_UNCERTAIN);
    }
    @Test
    public void testConstructor_maxOddValue() {
        this.verifyConstructorByOddValue(Probability.IMPOSSIBLE, Odds.MAX_DECIMAL_ODD_VALUE, Odds.MAX_DECIMAL_ODD_VALUE);
    }

    @Test
    public void testConstructor_byOddValueEqualsByImpliedProbability_oddValueEqualsOne() {
        this.verifyConstructorComparision(BigDecimal.ONE);
    }
    @Test
    public void testConstructor_byOddValueEqualsByImpliedProbability_oddValueAboveOne() {
        this.verifyConstructorComparision(EXPECTED_DECIMAL_ODD_VALUE_UNCERTAIN);
    }
    @Test
    public void testConstructor_byOddValueEqualsByImpliedProbability_maxOddValue() {
        this.verifyConstructorComparision(Odds.MAX_DECIMAL_ODD_VALUE);
    }

    @Override
    protected DecimalOdd createInstance(Probability probability) {
        return new DecimalOdd(probability);
    }

    @Override
    protected DecimalOdd createInstance(BigDecimal oddValue) {
        return new DecimalOdd(oddValue);
    }

    @Override
    protected BigDecimal getOddValueImpossible() {
        return Odds.MAX_DECIMAL_ODD_VALUE;
    }

    @Override
    protected BigDecimal getOddValueUncertain() {
        return DecimalOddTest.EXPECTED_DECIMAL_ODD_VALUE_UNCERTAIN;
    }

    @Override
    protected BigDecimal getOddValueCertain() {
        return BigDecimal.ONE;
    }

    @Override
    protected boolean equals(BigDecimal expected, BigDecimal result) {
        return expected != null && result != null && expected.compareTo(result) == 0;
    }

    @Test(expected = NullPointerException.class)
    public void from_nullOdd() {
        DecimalOdd.from((Odd) null);
    }
    @Test(expected = NullPointerException.class)
    public void from_oddWithoutImpliedProbability() {
        DecimalOdd.from(OddsTest.MOCK_ODD_NULL_IMPLIED_PROBABILITY);
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
        DecimalOdd result = DecimalOdd.from(odd);

        assertNotNull(result);
        assertNotNull(result.getDecimalOddValue());
        assertTrue(odd.getDecimalOddValue().compareTo(result.getDecimalOddValue()) == 0);
        assertNotNull(result.getImpliedProbability());
        assertEquals(odd.getImpliedProbability(), result.getImpliedProbability());
    }

}