package de.coiaf.footballprediction.domains.betability.shared.model;

import de.coiaf.random.odds.DecimalOdd;
import de.coiaf.random.odds.Odd;
import de.coiaf.random.probability.Probability;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class BetValueTest {

    private static final DecimalOdd ODD_2_0 = DecimalOdd.from(new BigDecimal("2.00"));
    private static final DecimalOdd ODD_3_0 = DecimalOdd.from(new BigDecimal("3.00"));
    private static final BettingOccasion OCCASION_MODEL_PROBABILITY_CORRESPONDS_TO_BOOKMAKER_ODD = new BettingOccasion(ODD_2_0.getImpliedProbability(), ODD_2_0);
    private static final BettingOccasion OCCASION_GOOD_BOOKMAKER_ODD = new BettingOccasion(ODD_2_0.getImpliedProbability(), ODD_3_0);
    private static final BettingOccasion OCCASION_BAD_BOOKMAKER_ODD = new BettingOccasion(ODD_3_0.getImpliedProbability(), ODD_2_0);
    private static final BigDecimal GIVEN_THRESHOLD_VALUE = new BigDecimal("0.2");
    private static final BetValue.Threshold GIVEN_THRESHOLD = new BetValue.Threshold(GIVEN_THRESHOLD_VALUE);

    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void constructor_nullOccasion() {
        BetValue betValue = new BetValue(null);
    }

    @Test
    public void constructor_givenOccasion() {
        BetValue betValue = new BetValue(OCCASION_GOOD_BOOKMAKER_ODD);

        assertNotNull(betValue);
        assertEquals(OCCASION_GOOD_BOOKMAKER_ODD, betValue.getOccasion());
        assertEquals(BetValue.Threshold.MINIMUM_THRESHOLD, betValue.getThreshold());
    }

    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void constructor_nullOccasion_validThresholdValue() {
        BetValue betValue = new BetValue(null, BetValue.Threshold.DEFAULT_THRESHOLD.getValue());
    }

    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void constructor_givenOccasion_nullThresholdValue() {
        BetValue betValue = new BetValue(OCCASION_GOOD_BOOKMAKER_ODD, (BigDecimal) null);
    }

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void constructor_givenOccasion_invalidThresholdValue() {
        BetValue betValue = new BetValue(OCCASION_GOOD_BOOKMAKER_ODD, new BigDecimal("-1"));
    }

    @Test
    public void constructor_goodOccasion_validThresholdValue() {
        BetValue betValue = new BetValue(OCCASION_GOOD_BOOKMAKER_ODD, BetValue.Threshold.DEFAULT_THRESHOLD.getValue());

        assertNotNull(betValue);
        assertEquals(OCCASION_GOOD_BOOKMAKER_ODD, betValue.getOccasion());
        assertEquals(BetValue.Threshold.DEFAULT_THRESHOLD, betValue.getThreshold());
        assertNotNull(betValue.getValue());
        assertTrue(BigDecimal.ZERO.compareTo(betValue.getValue()) < 0);
    }

    @Test
    public void constructor_badOccasion_validThresholdValue() {
        BetValue betValue = new BetValue(OCCASION_BAD_BOOKMAKER_ODD, BetValue.Threshold.DEFAULT_THRESHOLD.getValue());

        assertNotNull(betValue);
        assertEquals(OCCASION_BAD_BOOKMAKER_ODD, betValue.getOccasion());
        assertEquals(BetValue.Threshold.DEFAULT_THRESHOLD, betValue.getThreshold());
        assertNotNull(betValue.getValue());
        assertTrue(BigDecimal.ZERO.compareTo(betValue.getValue()) > 0);
    }

    @SuppressWarnings("SimplifiableAssertion")
    @Test
    public void constructor_zeroOccasion_validThresholdValue() {
        BetValue betValue = new BetValue(OCCASION_MODEL_PROBABILITY_CORRESPONDS_TO_BOOKMAKER_ODD, BetValue.Threshold.DEFAULT_THRESHOLD.getValue());

        assertNotNull(betValue);
        assertEquals(OCCASION_MODEL_PROBABILITY_CORRESPONDS_TO_BOOKMAKER_ODD, betValue.getOccasion());
        assertEquals(BetValue.Threshold.DEFAULT_THRESHOLD, betValue.getThreshold());
        assertNotNull(betValue.getValue());
        assertTrue(BigDecimal.ZERO.compareTo(betValue.getValue()) == 0);
    }

    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void constructor_nullOccasion_validThreshold() {
        BetValue betValue = new BetValue(null, BetValue.Threshold.DEFAULT_THRESHOLD);
    }

    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void constructor_givenOccasion_nullThreshold() {
        BetValue betValue = new BetValue(OCCASION_GOOD_BOOKMAKER_ODD, (BetValue.Threshold) null);
    }

    @Test
    public void constructor_givenOccasion_givenThreshold() {
        BetValue betValue = new BetValue(OCCASION_GOOD_BOOKMAKER_ODD, BetValue.Threshold.DEFAULT_THRESHOLD);

        assertNotNull(betValue);
        assertEquals(OCCASION_GOOD_BOOKMAKER_ODD, betValue.getOccasion());
        assertEquals(BetValue.Threshold.DEFAULT_THRESHOLD, betValue.getThreshold());
    }

    @Test
    public void hasBetValue_goodOccasion() {
        BetValue betValue = new BetValue(OCCASION_GOOD_BOOKMAKER_ODD);

        assertTrue(betValue.hasBetValue());
    }

    @Test
    public void hasBetValue_badOccasion() {
        BetValue betValue = new BetValue(OCCASION_BAD_BOOKMAKER_ODD);

        assertFalse(betValue.hasBetValue());
    }

    @Test
    public void hasBetValue_zeroOccasion() {
        BetValue betValue = new BetValue(OCCASION_MODEL_PROBABILITY_CORRESPONDS_TO_BOOKMAKER_ODD);

        assertFalse(betValue.hasBetValue());
    }

    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void threshold_constructor_nullValue() {
        BetValue.Threshold threshold = new BetValue.Threshold(null);
    }

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void threshold_constructor_illegalValue() {
        BetValue.Threshold threshold = new BetValue.Threshold(new BigDecimal("-0.1"));
    }

    @Test
    public void threshold_constructor_zeroValue() {
        BetValue.Threshold threshold = new BetValue.Threshold(new BigDecimal("0.00000"));

        assertNotNull(threshold);
        assertEquals(BetValue.Threshold.MINIMUM_THRESHOLD, threshold);
        assertFalse(threshold.isDefault());
    }

    @Test
    public void threshold_constructor_defaultValue() {
        BetValue.Threshold threshold = new BetValue.Threshold(new BigDecimal("0.10000"));

        assertNotNull(threshold);
        assertEquals(BetValue.Threshold.DEFAULT_THRESHOLD, threshold);
        assertTrue(threshold.isDefault());
    }

    @Test
    public void threshold_constructor_largeValue() {
        BetValue.Threshold threshold = new BetValue.Threshold(new BigDecimal("1000.00000"));

        assertNotNull(threshold);
        assertNotEquals(BetValue.Threshold.MINIMUM_THRESHOLD, threshold);
        assertNotEquals(BetValue.Threshold.DEFAULT_THRESHOLD, threshold);
        assertFalse(threshold.isDefault());
    }

    @Test(expected = NullPointerException.class)
    public void threshold_calculateMinimumBookmakerOdd_nullModelProbability() {
        GIVEN_THRESHOLD.calculateMinimumBookmakerOdd(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void threshold_calculateMinimumBookmakerOdd_impossibleModelProbability() {
        GIVEN_THRESHOLD.calculateMinimumBookmakerOdd(Probability.IMPOSSIBLE);
    }

    @Test
    public void threshold_calculateMinimumBookmakerOdd_uncertainModelProbability() {
        DecimalOdd correspondingBookmakerOdd = DecimalOdd.from(Probability.UNCERTAIN);

        Odd<?> bookmakerOdd = GIVEN_THRESHOLD.calculateMinimumBookmakerOdd(Probability.UNCERTAIN);

        assertNotNull(bookmakerOdd);
        assertTrue(correspondingBookmakerOdd.compareTo(bookmakerOdd) < 0);
    }

    @Test
    public void threshold_calculateMinimumBookmakerOdd_certainModelProbability() {
        DecimalOdd correspondingBookmakerOdd = DecimalOdd.from(Probability.CERTAIN);

        Odd<?> bookmakerOdd = GIVEN_THRESHOLD.calculateMinimumBookmakerOdd(Probability.CERTAIN);

        assertNotNull(bookmakerOdd);
        assertTrue(correspondingBookmakerOdd.compareTo(bookmakerOdd) < 0);
    }

    @Test(expected = NullPointerException.class)
    public void threshold_calculateMinimumModelProbability_nullBookmakerOdd() {
        GIVEN_THRESHOLD.calculateMinimumModelProbability(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void threshold_calculateMinimumModelProbability_minimumBookmakerOdd() {
        DecimalOdd bookmakerOdd = DecimalOdd.from(BigDecimal.ONE);

        GIVEN_THRESHOLD.calculateMinimumModelProbability(bookmakerOdd);
    }

    @Test
    public void threshold_calculateMinimumModelProbability_minimumRequiredBookmakerOdd() {
        DecimalOdd bookmakerOdd = DecimalOdd.from(GIVEN_THRESHOLD.getValue().add(BigDecimal.ONE));

        Probability modelProbability = GIVEN_THRESHOLD.calculateMinimumModelProbability(bookmakerOdd);

        assertNotNull(modelProbability);
        assertTrue(bookmakerOdd.getImpliedProbability().compareTo(modelProbability) < 0);
    }

    @Test
    public void threshold_calculateMinimumModelProbability_aboveMinimumRequiredBookmakerOdd() {
        DecimalOdd bookmakerOdd = DecimalOdd.from(GIVEN_THRESHOLD.getValue().add(BigDecimal.TEN));

        Probability modelProbability = GIVEN_THRESHOLD.calculateMinimumModelProbability(bookmakerOdd);

        assertNotNull(modelProbability);
        assertTrue(bookmakerOdd.getImpliedProbability().compareTo(modelProbability) < 0);
    }
}