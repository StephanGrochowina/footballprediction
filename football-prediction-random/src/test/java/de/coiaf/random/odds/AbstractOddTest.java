package de.coiaf.random.odds;

import de.coiaf.random.probability.Probability;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.Assert.*;

public abstract class AbstractOddTest<OV, O extends Odd<OV>> {

    protected static final BigDecimal EXPECTED_DECIMAL_ODD_VALUE_UNCERTAIN = new BigDecimal("2");

    @Test(expected = NullPointerException.class)
    public void testConstructor_nullProbability() {
        this.createInstance((Probability) null);
    }
    @Test
    public void testConstructor_impossibleProbability() {
        this.verifyConstructorByProbability(Probability.IMPOSSIBLE, Odds.MAX_DECIMAL_ODD_VALUE, this.getOddValueImpossible());
    }
    @Test
    public void testConstructor_uncertainProbability() {
        this.verifyConstructorByProbability(Probability.UNCERTAIN, EXPECTED_DECIMAL_ODD_VALUE_UNCERTAIN, this.getOddValueUncertain());
    }
    @Test
    public void testConstructor_certainProbability() {
        this.verifyConstructorByProbability(Probability.CERTAIN, BigDecimal.ONE, this.getOddValueCertain());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_nullOddValue() {
        this.createInstance((OV) null);
    }

    private void verifyConstructorByProbability(Probability expectedProbability, BigDecimal expectedDecimalOddValue, OV expectedOddValue) {
        O odd = this.createInstance(expectedProbability);

        this.verifyConstructor(odd, expectedProbability, expectedDecimalOddValue, expectedOddValue);
    }

    protected final void verifyConstructorByOddValue(Probability expectedProbability, BigDecimal expectedDecimalOddValue, OV expectedOddValue) {
        O odd = this.createInstance(expectedOddValue);

        this.verifyConstructor(odd, expectedProbability, expectedDecimalOddValue, expectedOddValue);
    }

    private void verifyConstructor(O odd, Probability expectedProbability, BigDecimal expectedDecimalOddValue, OV expectedOddValue) {
        assertNotNull(odd);
        Probability impliedProbability = odd.getImpliedProbability();
        BigDecimal decimalOddValue = odd.getDecimalOddValue();
        OV oddValue = odd.getOddValue();

        assertNotNull(impliedProbability);
        assertEquals(expectedProbability, impliedProbability);

        assertNotNull(decimalOddValue);
        assertTrue(expectedDecimalOddValue.compareTo(decimalOddValue) == 0);

        assertNotNull(oddValue);
        assertTrue(this.equals(expectedOddValue, oddValue));
    }

    @Test
    public void testConstructor_byImpliedProbabilityEqualsByOddValue_impossibleImpliedProbability() {
        this.verifyConstructorComparision(Probability.IMPOSSIBLE);
    }
    @Test
    public void testConstructor_byImpliedProbabilityEqualsByOddValue_uncertainImpliedProbability() {
        this.verifyConstructorComparision(Probability.UNCERTAIN);
    }
    @Test
    public void testConstructor_byImpliedProbabilityEqualsByOddValue_certainImpliedProbability() {
        this.verifyConstructorComparision(Probability.CERTAIN);
    }
    private void verifyConstructorComparision(Probability probability) {
        O oddProbability = this.createInstance(probability);
        O oddOddValue = this.createInstance(oddProbability.getOddValue());

        this.compareOddInstances(oddProbability, oddOddValue);
    }
    protected final void verifyConstructorComparision(OV oddValue) {
        O oddOddValue = this.createInstance(oddValue);
        O oddProbability = this.createInstance(oddOddValue.getImpliedProbability());

        this.compareOddInstances(oddOddValue, oddProbability);
    }

    private void compareOddInstances(O expected, O result) {
        Objects.requireNonNull(expected);
        Objects.requireNonNull(result);

        assertEquals(expected, result);
        assertEquals(expected.getImpliedProbability(), result.getImpliedProbability());
        assertTrue(expected.getDecimalOddValue().compareTo(result.getDecimalOddValue()) == 0);
        assertTrue(this.equals(expected.getOddValue(), result.getOddValue()));

    }

    protected abstract O createInstance(Probability probability);
    protected abstract O createInstance(OV oddValue);

    protected abstract OV getOddValueImpossible();
    protected abstract OV getOddValueUncertain();
    protected abstract OV getOddValueCertain();

    protected abstract boolean equals(OV expected, OV result);

}