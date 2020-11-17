package de.coiaf.random.odds;

import de.coiaf.random.probability.Probability;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AmericanOddNegativeTest extends AmericanOddTest {

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void from_nullOdd() {
        AmericanOddNegative.from((Odd) null);
    }
    @Test(expected = NullPointerException.class)
    public void from_oddWithoutImpliedProbability() {
        AmericanOddNegative.from(OddsTest.MOCK_ODD_NULL_IMPLIED_PROBABILITY);
    }
    @Test
    public void from_oddWithImpossibleImpliedProbability() {
        this.verifyFrom(OddsTest.MOCK_ODD_IMPOSSIBLE_IMPLIED_PROBABILITY);
    }
    @Test
    public void from_oddWithUncertainImpliedProbability() {
        this.verifyFrom(OddsTest.MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY);
    }
    @Test(expected = IllegalArgumentException.class)
    public void from_oddWithCertainImpliedProbability() {
        this.verifyFrom(OddsTest.MOCK_ODD_CERTAIN_IMPLIED_PROBABILITY);
    }

    private void verifyFrom(Odd<?> odd) {
        AmericanOdd result = AmericanOddNegative.from(odd);

        assertNotNull(result);
        assertNotNull(result.getDecimalOddValue());
        assertEquals(0, odd.getDecimalOddValue().compareTo(result.getDecimalOddValue()));
        assertNotNull(result.getImpliedProbability());
        assertEquals(odd.getImpliedProbability(), result.getImpliedProbability());
    }

    @Test(expected = NullPointerException.class)
    public void convertToAmericanOddValue_nullDecimalOddValue() {
        AmericanOddNegative.convertToNegativeAmericanOddValue(null);
    }
    @Test(expected = IllegalArgumentException.class)
    public void convertToAmericanOddValue_negativeDecimalOddValue() {
        AmericanOddNegative.convertToNegativeAmericanOddValue(new BigDecimal("-1"));
    }
    @Test(expected = IllegalArgumentException.class)
    public void convertToAmericanOddValue_zeroDecimalOddValue() {
        AmericanOddNegative.convertToNegativeAmericanOddValue(BigDecimal.ZERO);
    }
    @Test(expected = IllegalArgumentException.class)
    public void convertToAmericanOddValue_PositiveDecimalOddValueBelow1() {
        AmericanOddNegative.convertToNegativeAmericanOddValue(new BigDecimal("0.5"));
    }
    @Test(expected = IllegalArgumentException.class)
    public void convertToAmericanOddValue_PositiveDecimalOddValueEquals1() {
        AmericanOddNegative.convertToNegativeAmericanOddValue(BigDecimal.ONE);
    }
    @Test
    public void convertToAmericanOddValue_PositiveDecimalOddValueAbove1() {
        int expectedAmericanOddValue = -100;

        this.verifyConvertToAmericanOddValue(EXPECTED_DECIMAL_ODD_FOR_ABS_AMERICAN_ODD_100, expectedAmericanOddValue);
    }
    @Test
    public void convertToAmericanOddValue_InfinitePositiveDecimalOddValue() {
        BigDecimal decimalOddValue = Odds.MAX_DECIMAL_ODD_VALUE;
        int expectedAmericanOddValue = -Integer.MAX_VALUE;

        this.verifyConvertToAmericanOddValue(decimalOddValue, expectedAmericanOddValue);
    }

    private void verifyConvertToAmericanOddValue(BigDecimal decimalOddValue, int expectedAmericanOddValue) {
        int result = AmericanOddNegative.convertToNegativeAmericanOddValue(decimalOddValue);

        assertEquals(expectedAmericanOddValue, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void convertToDecimalOddValue_positiveInfiniteAmericanOddValue() {
        super.convertToDecimalOddValue_positiveInfiniteAmericanOddValue();
    }
    @Test(expected = IllegalArgumentException.class)
    public void convertToDecimalOddValue_positiveAmericanOddValue() {
        super.convertToDecimalOddValue_positiveAmericanOddValue();
    }
    @Test(expected = IllegalArgumentException.class)
    public void convertToDecimalOddValue_zeroAmericanOddValue() {
        super.convertToDecimalOddValue_zeroAmericanOddValue();
    }

    @Override
    protected BigDecimal determineDecimalOddValue(int americanOddValue) {
        return AmericanOddNegative.convertToDecimalOddValue(americanOddValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_zeroOddValue() {
        super.testConstructor_zeroOddValue();
    }
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_positiveOddValue() {
        super.testConstructor_positiveOddValue();
    }
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_maxPositiveOddValue() {
        super.testConstructor_maxPositiveOddValue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_byOddValueEqualsByImpliedProbability_zeroOddValue() {
        super.testConstructor_byOddValueEqualsByImpliedProbability_zeroOddValue();
    }
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_byOddValueEqualsByImpliedProbability_positiveOddValue() {
        super.testConstructor_byOddValueEqualsByImpliedProbability_positiveOddValue();
    }
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_byOddValueEqualsByImpliedProbability_maxPositiveOddValue() {
        super.testConstructor_byOddValueEqualsByImpliedProbability_maxPositiveOddValue();
    }
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_byImpliedProbabilityEqualsByOddValue_certainImpliedProbability() {
        super.testConstructor_byImpliedProbabilityEqualsByOddValue_certainImpliedProbability();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_certainProbability() {
        super.testConstructor_certainProbability();
    }

    @Override
    protected AmericanOddNegative createInstance(Probability probability) {
        return new AmericanOddNegative(probability);
    }

    @Override
    protected AmericanOddNegative createInstance(Integer oddValue) {
        return new AmericanOddNegative(oddValue);
    }

    @Override
    protected Integer getOddValueImpossible() {
        return -Integer.MAX_VALUE;
    }

    @Override
    protected Integer getOddValueUncertain() {
        return -100;
    }

    @Override
    protected Integer getOddValueCertain() {
        return 0;
    }
}