package de.coiaf.footballprediction.domains.betability.shared.model;

import de.coiaf.random.odds.DecimalOdd;
import de.coiaf.random.odds.Odd;
import de.coiaf.random.probability.Probability;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class BettingOccasionTest {

    private static final Probability GIVEN_MODEL_PROBABILITY = Probability.UNCERTAIN;
    private static final Odd<?> GIVEN_BOOKMAKER_ODD = DecimalOdd.from(new BigDecimal("1.4"));

    @Test
    public void constructor_givenModelProbability_givenBookmakerOdd() {
        BettingOccasion occasion = new BettingOccasion(GIVEN_MODEL_PROBABILITY, GIVEN_BOOKMAKER_ODD);

        assertNotNull(occasion);
        assertNotNull(occasion.getModelProbability());
        assertSame(GIVEN_MODEL_PROBABILITY, occasion.getModelProbability());
        assertNotNull(occasion.getBookmakerOdd());
        assertSame(GIVEN_BOOKMAKER_ODD, occasion.getBookmakerOdd());
    }

    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void constructor_nullModelProbability_givenBookmakerOdd() {
        BettingOccasion occasion = new BettingOccasion(null, GIVEN_BOOKMAKER_ODD);
    }

    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void constructor_givenModelProbability_nullBookmakerOdd() {
        BettingOccasion occasion = new BettingOccasion(GIVEN_MODEL_PROBABILITY, null);
    }

    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void constructor_nullModelProbability_nullBookmakerOdd() {
        BettingOccasion occasion = new BettingOccasion(null, null);
    }
}