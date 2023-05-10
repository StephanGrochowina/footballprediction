package de.coiaf.random.probability;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Stephan Grochowina <stephan.grochowina@web.de>
 */
public class ProbabilityTest {
    
    
    @Test(expected = NullPointerException.class)
    public void testProbability_nullDoubleInstance() {
        new Probability(null);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testProbability_doubleInstanceBelow0() {
        Double value = -1.0;
        
        new Probability(value);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testProbability_doubleInstanceAbove1() {
        Double value = 2.0;
        
        new Probability(value);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testProbability_doubleInstanceNaN() {
        Double value = Double.NaN;

        new Probability(value);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testProbability_doubleInstancePositiveInfinity() {
        Double value = Double.POSITIVE_INFINITY;

        new Probability(value);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testProbability_doubleInstanceNegativeInfinity() {
        Double value = Double.NEGATIVE_INFINITY;

        new Probability(value);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testProbability_doubleValueBelow0() {
        double value = -1.0;
        
        new Probability(value);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testProbability_doubleValueAbove1() {
        double value = 2.0;
        
        new Probability(value);
    }
    
    @Test(expected = NullPointerException.class)
    public void testAdd_nullAugend() {
        Probability value = new Probability(0.5);
        
        value.add(null);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_sumExceeds1() {
        Probability value = new Probability(0.5);
        
        value.add(Probability.CERTAIN);
    }
    @Test
    public void testAdd_validAugend() {
        Probability value = new Probability(0.5);
        Probability augend = new Probability(0.25);
        
        Probability result = value.add(augend);
        
        assertNotNull(result);
        assertTrue(result.doubleValue() == value.doubleValue() + augend.doubleValue());
    }

    @Test(expected = NullPointerException.class)
    public void testSubtract_nullSubtrahend() {
        Probability value = new Probability(0.5);
        
        value.subtract(null);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testSubtract_diffBelow0() {
        Probability value = new Probability(0.5);
        
        value.subtract(Probability.CERTAIN);
    }
    @Test
    public void testSubtract_validSubtrahend() {
        Probability value = new Probability(0.5);
        Probability subtrahend = new Probability(0.25);
        
        Probability result = value.subtract(subtrahend);
        
        assertNotNull(result);
        assertTrue(result.doubleValue() == subtrahend.doubleValue());
    }

    @Test(expected = NullPointerException.class)
    public void testMultiply_nullMultiplicand() {
        Probability value = new Probability(0.5);
        
        value.multiply(null);
    }
    @Test
    public void testMultiply_validMultiplicand() {
        Probability value = new Probability(0.5);
        Probability multiplicand = new Probability(0.25);
        
        Probability result = value.multiply(multiplicand);
        
        assertNotNull(result);
        assertTrue(result.doubleValue() == multiplicand.doubleValue() * value.doubleValue());
    }

    @Test(expected = NullPointerException.class)
    public void testDivide_nullDivisor() {
        Probability value = new Probability(0.5);
        
        value.divide(null);
    }
    @Test(expected = ArithmeticException.class)
    public void testDivide_zeroDivisor() {
        Probability value = new Probability(0.5);
        
        value.divide(Probability.IMPOSSIBLE);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testDivide_divisionExceeds1() {
        Probability value = new Probability(0.5);
        Probability divisor = new Probability(0.1);
        
        value.divide(divisor);
    }
    @Test
    public void testDivide_validDivisor() {
        Probability value = new Probability(0.25);
        Probability divisor = new Probability(0.5);
        
        Probability result = value.divide(divisor);
        
        assertNotNull(result);
        assertTrue(result.doubleValue() == value.doubleValue() / divisor.doubleValue());
    }
    
    @Test
    public void testAnd_false_false_false() {
        this.validateAnd(Probability.IMPOSSIBLE, Probability.IMPOSSIBLE, Probability.IMPOSSIBLE, Probability.IMPOSSIBLE);
    }
    @Test
    public void testAnd_false_false_true() {
        this.validateAnd(Probability.IMPOSSIBLE, Probability.IMPOSSIBLE, Probability.IMPOSSIBLE, Probability.CERTAIN);
    }
    @Test
    public void testAnd_false_true_false() {
        this.validateAnd(Probability.IMPOSSIBLE, Probability.IMPOSSIBLE, Probability.CERTAIN, Probability.IMPOSSIBLE);
    }
    @Test
    public void testAnd_false_true_true() {
        this.validateAnd(Probability.IMPOSSIBLE, Probability.IMPOSSIBLE, Probability.CERTAIN, Probability.CERTAIN);
    }
    @Test
    public void testAnd_true_false_false() {
        this.validateAnd(Probability.IMPOSSIBLE, Probability.CERTAIN, Probability.IMPOSSIBLE, Probability.IMPOSSIBLE);
    }
    @Test
    public void testAnd_true_false_true() {
        this.validateAnd(Probability.IMPOSSIBLE, Probability.CERTAIN, Probability.IMPOSSIBLE, Probability.CERTAIN);
    }
    @Test
    public void testAnd_true_true_false() {
        this.validateAnd(Probability.IMPOSSIBLE, Probability.CERTAIN, Probability.CERTAIN, Probability.IMPOSSIBLE);
    }
    @Test
    public void testAnd_true_true_true() {
        this.validateAnd(Probability.CERTAIN, Probability.CERTAIN, Probability.CERTAIN, Probability.CERTAIN);
    }
    private void validateAnd(Probability expected, Probability operand1, Probability... operands) {
        Probability result = operand1.and(operands);
        
        assertEquals(expected, result);
    }

    @Test
    public void testOr_false_false_false() {
        this.validateOr(Probability.IMPOSSIBLE, Probability.IMPOSSIBLE, Probability.IMPOSSIBLE, Probability.IMPOSSIBLE);
    }
    @Test
    public void testOr_false_false_true() {
        this.validateOr(Probability.CERTAIN, Probability.IMPOSSIBLE, Probability.IMPOSSIBLE, Probability.CERTAIN);
    }
    @Test
    public void testOr_false_true_false() {
        this.validateOr(Probability.CERTAIN, Probability.IMPOSSIBLE, Probability.CERTAIN, Probability.IMPOSSIBLE);
    }
    @Test
    public void testOr_false_true_true() {
        this.validateOr(Probability.CERTAIN, Probability.IMPOSSIBLE, Probability.CERTAIN, Probability.CERTAIN);
    }
    @Test
    public void testOr_true_false_false() {
        this.validateOr(Probability.CERTAIN, Probability.CERTAIN, Probability.IMPOSSIBLE, Probability.IMPOSSIBLE);
    }
    @Test
    public void testOr_true_false_true() {
        this.validateOr(Probability.CERTAIN, Probability.CERTAIN, Probability.IMPOSSIBLE, Probability.CERTAIN);
    }
    @Test
    public void testOr_true_true_false() {
        this.validateOr(Probability.CERTAIN, Probability.CERTAIN, Probability.CERTAIN, Probability.IMPOSSIBLE);
    }
    @Test
    public void testOr_true_true_true() {
        this.validateOr(Probability.CERTAIN, Probability.CERTAIN, Probability.CERTAIN, Probability.CERTAIN);
    }
    private void validateOr(Probability expected, Probability operand1, Probability... operands) {
        Probability result = operand1.or(operands);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testNegate_false() {
        Probability result = Probability.IMPOSSIBLE.negate();
        
        assertEquals(Probability.CERTAIN, result);
    }
    @Test
    public void testNegate_true() {
        Probability result = Probability.CERTAIN.negate();
        
        assertEquals(Probability.IMPOSSIBLE, result);
    }
}
