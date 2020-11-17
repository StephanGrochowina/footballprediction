package de.coiaf.random.distributions.enumerated.goalEstimation;

import org.junit.Test;

import static org.junit.Assert.*;

public class PlayedMatchesTest {

    @Test(expected = NullPointerException.class)
    public void valueOf_nullMatchesPlayed() {
        PlayedMatches.valueOf(null);
    }
    @Test(expected = IllegalArgumentException.class)
    public void valueOf_negativeMatchesPlayed() {
        PlayedMatches.valueOf(-1);
    }
    @Test
    public void valueOf_zeroMatchesPlayed() {
        double matchesValue = 0;
        PlayedMatches matches = PlayedMatches.valueOf(matchesValue);

        assertNotNull(matches);
        assertFalse(matches.hasPlayedMatches());
        assertEquals(matchesValue, matches.doubleValue(), 0.0);
    }
    @Test
    public void valueOf_positiveMatchesPlayed() {
        double matchesValue = 1;
        PlayedMatches matches = PlayedMatches.valueOf(matchesValue);

        assertNotNull(matches);
        assertTrue(matches.hasPlayedMatches());
        assertEquals(matchesValue, matches.doubleValue(), 0.0);
    }
}