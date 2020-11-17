package de.coiaf.random.betevaluation;

import de.coiaf.random.probability.Probability;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class BudgetRateCalculatorFactoryTest {

    private BudgetRateCalculatorFactory factory = null;

    @Before
    public void setUp() {
        this.factory = new BudgetRateCalculatorFactory();
    }

    @After
    public void tearDown() {
        this.factory = null;
    }

    @Test
    public void createBudgetRateCalculatorKellyComplete() {
        BudgetRateCalculator result = this.factory.createBudgetRateCalculatorKellyComplete();

        assertNotNull(result);
        assertTrue(result instanceof BudgetRateCalculatorKellyComplete);
    }

    @Test
    public void createBudgetRateCalculatorKellyComplete_nullValueThreshold() {
        this.validateCreateBudgetRateCalculatorKellyComplete( null);
    }
    @Test
    public void createBudgetRateCalculatorKellyComplete_negativeValueThreshold() {
        this.validateCreateBudgetRateCalculatorKellyComplete(BigDecimal.ONE.negate());
    }
    @Test
    public void createBudgetRateCalculatorKellyComplete_zeroValueThreshold() {
        this.validateCreateBudgetRateCalculatorKellyComplete(new BigDecimal("0.00"));
    }
    @Test
    public void createBudgetRateCalculatorKellyComplete_positiveValueThreshold() {
        this.validateCreateBudgetRateCalculatorKellyComplete(BigDecimal.ONE);
    }
    private void validateCreateBudgetRateCalculatorKellyComplete(BigDecimal givenValueThreshold) {
        BudgetRateCalculator result = this.factory.createBudgetRateCalculatorKellyComplete(givenValueThreshold);

        assertNotNull(result);
        assertTrue(result instanceof BudgetRateCalculatorKellyComplete);
    }

    @Test
    public void createBudgetRateCalculatorFractionalKelly() {
        BudgetRateCalculator result = this.factory.createBudgetRateCalculatorFractionalKelly();

        assertNotNull(result);
        assertTrue(result instanceof BudgetRateCalculatorFractionalKelly);
    }

    @Test
    public void createBudgetRateCalculatorFractionalKelly_nullValueThreshold() {
        this.validateCreateBudgetRateCalculatorFractionalKelly((BigDecimal) null);
    }
    @Test
    public void createBudgetRateCalculatorFractionalKelly_negativeValueThreshold() {
        this.validateCreateBudgetRateCalculatorFractionalKelly(BigDecimal.ONE.negate());
    }
    @Test
    public void createBudgetRateCalculatorFractionalKelly_zeroValueThreshold() {
        this.validateCreateBudgetRateCalculatorFractionalKelly(new BigDecimal("0.00"));
    }
    @Test
    public void createBudgetRateCalculatorFractionalKelly_positiveValueThreshold() {
        this.validateCreateBudgetRateCalculatorFractionalKelly(BigDecimal.ONE);
    }

    @Test(expected = NullPointerException.class)
    public void createBudgetRateCalculatorFractionalKelly_nullFraction() {
        this.validateCreateBudgetRateCalculatorFractionalKelly((Probability) null);
    }
    @Test(expected = IllegalArgumentException.class)
    public void createBudgetRateCalculatorFractionalKelly_impossibleFraction() {
        this.validateCreateBudgetRateCalculatorFractionalKelly(Probability.IMPOSSIBLE);
    }
    @Test
    public void createBudgetRateCalculatorFractionalKelly_uncertainFraction() {
        this.validateCreateBudgetRateCalculatorFractionalKelly(Probability.UNCERTAIN);
    }
    @Test
    public void createBudgetRateCalculatorFractionalKelly_certainFraction() {
        this.validateCreateBudgetRateCalculatorFractionalKelly(Probability.CERTAIN);
    }

    @Test(expected = NullPointerException.class)
    public void createBudgetRateCalculatorFractionalKelly_nullFraction_positiveValueThreshold() {
        this.validateCreateBudgetRateCalculatorFractionalKelly(null, BigDecimal.ONE);
    }
    @Test(expected = IllegalArgumentException.class)
    public void createBudgetRateCalculatorFractionalKelly_impossibleFraction_positiveValueThreshold() {
        this.validateCreateBudgetRateCalculatorFractionalKelly(Probability.IMPOSSIBLE, BigDecimal.ONE);
    }
    @Test
    public void createBudgetRateCalculatorFractionalKelly_uncertainFraction_positiveValueThreshold() {
        this.validateCreateBudgetRateCalculatorFractionalKelly(Probability.UNCERTAIN, BigDecimal.ONE);
    }
    @Test
    public void createBudgetRateCalculatorFractionalKelly_certainFraction_positiveValueThreshold() {
        this.validateCreateBudgetRateCalculatorFractionalKelly(Probability.CERTAIN, BigDecimal.ONE);
    }
    @Test
    public void createBudgetRateCalculatorFractionalKelly_certainFraction_zeroValueThreshold() {
        this.validateCreateBudgetRateCalculatorFractionalKelly(Probability.CERTAIN, BigDecimal.ZERO);
    }
    @Test
    public void createBudgetRateCalculatorFractionalKelly_certainFraction_negativeValueThreshold() {
        this.validateCreateBudgetRateCalculatorFractionalKelly(Probability.CERTAIN, BigDecimal.ONE.negate());
    }
    @Test
    public void createBudgetRateCalculatorFractionalKelly_certainFraction_nullValueThreshold() {
        this.validateCreateBudgetRateCalculatorFractionalKelly(Probability.CERTAIN, null);
    }

    private void validateCreateBudgetRateCalculatorFractionalKelly(BigDecimal givenValueThreshold) {
        BudgetRateCalculator result = this.factory.createBudgetRateCalculatorFractionalKelly(givenValueThreshold);

        this.validateCreateBudgetRateCalculatorFractionalKelly(result, BudgetRateCalculatorFractionalKelly.ADVISED_KELLY_FRACTION);
    }
    private void validateCreateBudgetRateCalculatorFractionalKelly(Probability fraction) {
        BudgetRateCalculator result = this.factory.createBudgetRateCalculatorFractionalKelly(fraction);

        this.validateCreateBudgetRateCalculatorFractionalKelly(result, fraction);
    }
    private void validateCreateBudgetRateCalculatorFractionalKelly(Probability fraction, BigDecimal givenValueThreshold) {
        BudgetRateCalculator result = this.factory.createBudgetRateCalculatorFractionalKelly(fraction, givenValueThreshold);

        this.validateCreateBudgetRateCalculatorFractionalKelly(result, fraction);
    }
    private void validateCreateBudgetRateCalculatorFractionalKelly(BudgetRateCalculator calculator, Probability expectedFraction) {
        assertTrue(calculator instanceof BudgetRateCalculatorFractionalKelly);

        Probability fraction = ((BudgetRateCalculatorFractionalKelly) calculator).getFraction();
        assertNotNull(fraction);
        assertEquals(expectedFraction, fraction);
    }

    @Test
    public void createSimpleBudgetRateCalculator() {
        BudgetRateCalculator result = this.factory.createSimpleBudgetRateCalculator();

        assertNotNull(result);
        assertTrue(result instanceof SimpleBudgetRateCalculator);
    }

    @Test
    public void createSimpleBudgetRateCalculator_nullValueThreshold() {
        this.validateCreateSimpleBudgetRateCalculator( null);
    }
    @Test
    public void createSimpleBudgetRateCalculator_negativeValueThreshold() {
        this.validateCreateSimpleBudgetRateCalculator(BigDecimal.ONE.negate());
    }
    @Test
    public void createSimpleBudgetRateCalculator_zeroValueThreshold() {
        this.validateCreateSimpleBudgetRateCalculator(new BigDecimal("0.00"));
    }
    @Test
    public void createSimpleBudgetRateCalculator_positiveValueThreshold() {
        this.validateCreateSimpleBudgetRateCalculator(BigDecimal.ONE);
    }
    private void validateCreateSimpleBudgetRateCalculator(BigDecimal givenValueThreshold) {
        BudgetRateCalculator result = this.factory.createSimpleBudgetRateCalculator(givenValueThreshold);

        assertNotNull(result);
        assertTrue(result instanceof SimpleBudgetRateCalculator);
    }

    @Test(expected = NullPointerException.class)
    public void createBudgetRateCalculator_nullSupplier() {
        this.factory.createBudgetRateCalculator(null);
    }
    @Test(expected = IllegalArgumentException.class)
    public void createBudgetRateCalculator_supplierSupplyingNull() {
        this.factory.createBudgetRateCalculator(() -> null);
    }
    @Test
    public void createBudgetRateCalculator_supplierSupplyingInstance() {
        BudgetRateCalculator calculater = Mockito.mock(BudgetRateCalculator.class);

        BudgetRateCalculator result = this.factory.createBudgetRateCalculator(() -> calculater);

        assertNotNull(result);
        assertSame(calculater, result);
    }
}