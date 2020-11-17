package de.coiaf.random.odds;

import de.coiaf.random.probability.Probability;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class AmericanOddPositiveTest extends AmericanOddTest {

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void from_nullOdd() {
        AmericanOddPositive.from((Odd) null);
    }
    @Test(expected = NullPointerException.class)
    public void from_oddWithoutImpliedProbability() {
        AmericanOddPositive.from(OddsTest.MOCK_ODD_NULL_IMPLIED_PROBABILITY);
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
        AmericanOdd result = AmericanOddPositive.from(odd);

        assertNotNull(result);
        assertNotNull(result.getDecimalOddValue());
        assertEquals(0, odd.getDecimalOddValue().compareTo(result.getDecimalOddValue()));
        assertNotNull(result.getImpliedProbability());
        assertEquals(odd.getImpliedProbability(), result.getImpliedProbability());
    }

    @Test(expected = NullPointerException.class)
    public void convertToAmericanOddValue_nullDecimalOddValue() {
        AmericanOddPositive.convertToPositiveAmericanOddValue(null);
    }
    @Test(expected = IllegalArgumentException.class)
    public void convertToAmericanOddValue_negativeDecimalOddValue() {
        AmericanOddPositive.convertToPositiveAmericanOddValue(new BigDecimal("-1"));
    }
    @Test(expected = IllegalArgumentException.class)
    public void convertToAmericanOddValue_zeroDecimalOddValue() {
        AmericanOddPositive.convertToPositiveAmericanOddValue(BigDecimal.ZERO);
    }
    @Test(expected = IllegalArgumentException.class)
    public void convertToAmericanOddValue_PositiveDecimalOddValueBelow1() {
        AmericanOddPositive.convertToPositiveAmericanOddValue(new BigDecimal("0.5"));
    }
    @Test
    public void convertToAmericanOddValue_PositiveDecimalOddValueEquals1() {
        BigDecimal decimalOddValue = BigDecimal.ONE;
        int expectedAmericanOddValue = 0;

        this.verifyConvertToAmericanOddValue(decimalOddValue, expectedAmericanOddValue);
    }
    @Test
    public void convertToAmericanOddValue_PositiveDecimalOddValueAbove1() {
        int expectedAmericanOddValue = 100;

        this.verifyConvertToAmericanOddValue(EXPECTED_DECIMAL_ODD_FOR_ABS_AMERICAN_ODD_100, expectedAmericanOddValue);
    }
    @Test
    public void convertToAmericanOddValue_InfinitePositiveDecimalOddValue() {
        BigDecimal decimalOddValue = Odds.MAX_DECIMAL_ODD_VALUE;
        int expectedAmericanOddValue = Integer.MAX_VALUE;

        this.verifyConvertToAmericanOddValue(decimalOddValue, expectedAmericanOddValue);
    }

    private void verifyConvertToAmericanOddValue(BigDecimal decimalOddValue, int expectedAmericanOddValue) {
        int result = AmericanOddPositive.convertToPositiveAmericanOddValue(decimalOddValue);

        assertEquals(expectedAmericanOddValue, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void convertToDecimalOddValue_negativeAmericanOddValue() {
        super.convertToDecimalOddValue_negativeAmericanOddValue();
    }
    @Test(expected = IllegalArgumentException.class)
    public void convertToDecimalOddValue_negativeInfiniteAmericanOddValue() {
        super.convertToDecimalOddValue_negativeInfiniteAmericanOddValue();
    }

    @Override
    protected BigDecimal determineDecimalOddValue(int americanOddValue) {
        return AmericanOddPositive.convertToDecimalOddValue(americanOddValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_negativeOddValue() {
        super.testConstructor_negativeOddValue();
    }
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_minNegativeOddValue() {
        super.testConstructor_minNegativeOddValue();
    }
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_maxNegativeOddValue() {
        super.testConstructor_maxNegativeOddValue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_byOddValueEqualsByImpliedProbability_negativeOddValue() {
        super.testConstructor_byOddValueEqualsByImpliedProbability_negativeOddValue();
    }
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_byOddValueEqualsByImpliedProbability_maxNegativeOddValue() {
        super.testConstructor_byOddValueEqualsByImpliedProbability_maxNegativeOddValue();
    }

    @Override
    protected AmericanOddPositive createInstance(Probability probability) {
        return new AmericanOddPositive(probability);
    }

    @Override
    protected AmericanOddPositive createInstance(Integer oddValue) {
        return new AmericanOddPositive(oddValue);
    }

    @Override
    protected Integer getOddValueImpossible() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected Integer getOddValueUncertain() {
        return 100;
    }

    @Override
    protected Integer getOddValueCertain() {
        return 0;
    }
}