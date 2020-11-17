package de.coiaf.random.distributions.enumerated.matchOutcome;

import de.coiaf.random.probability.NormalizerDistributionElementWeights;
import de.coiaf.random.probability.Probability;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class ItemsSupplierByDistributionModelTest {

    private static final ItemsSupplierByDistributionModel ITEMS_SUPPLIER = new ItemsSupplierByDistributionModel();

    @Test(expected = NullPointerException.class)
    public void testConstructor_nullAccuracy() {
        new ItemsSupplierByDistributionModel(null);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_impossibleAccuracy() {
        new ItemsSupplierByDistributionModel(Probability.IMPOSSIBLE);
    }
    @Test
    public void testConstructor_accuracyBelowCertain() {
        ItemsSupplierByDistributionModel result = new ItemsSupplierByDistributionModel(Probability.UNCERTAIN);

        assertNotNull(result);
    }
    @Test
    public void testConstructor_accuracyEqualsCertain() {
        ItemsSupplierByDistributionModel result = new ItemsSupplierByDistributionModel(Probability.CERTAIN);

        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createItems_negativeExpectedHomeGoals_positiveExpectedAwayGoals() {
        ITEMS_SUPPLIER.createItems(-1.0, 1.0);
    }
    @Test(expected = IllegalArgumentException.class)
    public void createItems_zeroExpectedHomeGoals_positiveExpectedAwayGoals() {
        ITEMS_SUPPLIER.createItems(0.0, 1.0);
    }
    @Test(expected = IllegalArgumentException.class)
    public void createItems_positiveExpectedHomeGoals_negativeExpectedAwayGoals() {
        ITEMS_SUPPLIER.createItems(1.0, -1.0);
    }
    @Test(expected = IllegalArgumentException.class)
    public void createItems_positiveExpectedHomeGoals_zeroExpectedAwayGoals() {
        ITEMS_SUPPLIER.createItems(1.0, 0.0);
    }
    @Test
    public void createItems_positiveExpectedHomeGoalsLessThanPositiveExpectedAwayGoals() {
        this.verifyCreateItems(1.0, 5.0);
    }
    @Test
    public void createItems_positiveExpectedHomeGoalsEqualsPositiveExpectedAwayGoals() {
        this.verifyCreateItems(5.0, 5.0);
    }
    @Test
    public void createItems_positiveExpectedHomeGoalsGreaterThanPositiveExpectedAwayGoals() {
        this.verifyCreateItems(5.0, 1.0);
    }

    private void verifyCreateItems(double expectedHomeGoals, double expectedAwayGoals) {
        Collection<NormalizerDistributionElementWeights.Pair<Outcomes, Probability>> generatedItems;

        ITEMS_SUPPLIER.createItems(expectedHomeGoals, expectedAwayGoals);

        generatedItems = ITEMS_SUPPLIER.get();

        assertNotNull(generatedItems);
        assertFalse(generatedItems.isEmpty());
    }
}