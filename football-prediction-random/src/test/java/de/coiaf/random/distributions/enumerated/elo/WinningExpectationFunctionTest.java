package de.coiaf.random.distributions.enumerated.elo;

import de.coiaf.random.probability.Probability;
import org.junit.Test;

import static org.junit.Assert.*;

public class WinningExpectationFunctionTest {

    @Test
    public void Constructor() {
        WinningExpectationFunction result = new WinningExpectationFunction();

        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void Constructor_negativeC_positiveDAboveOne() {
        new WinningExpectationFunction(-1.0, 2.0);
    }
    @Test(expected = IllegalArgumentException.class)
    public void Constructor_zeroC_positiveDAboveOne() {
        new WinningExpectationFunction(0.0, 2.0);
    }
    @Test(expected = IllegalArgumentException.class)
    public void Constructor_positiveCBelowOne_positiveDAboveOne() {
        new WinningExpectationFunction(0.5, 2.0);
    }
    @Test(expected = IllegalArgumentException.class)
    public void Constructor_positiveCEqualsOne_positiveDAboveOne() {
        new WinningExpectationFunction(1.0, 2.0);
    }
    @Test
    public void Constructor_positiveCAboveOne_positiveDAboveOne() {
        WinningExpectationFunction result = new WinningExpectationFunction(2.0, 2.0);

        assertNotNull(result);
    }
    @Test(expected = IllegalArgumentException.class)
    public void Constructor_positiveCAboveOne_negativeD() {
        WinningExpectationFunction result = new WinningExpectationFunction(2.0, -1.0);

        assertNotNull(result);
    }
    @Test(expected = IllegalArgumentException.class)
    public void Constructor_positiveCAboveOne_zeroD() {
        WinningExpectationFunction result = new WinningExpectationFunction(2.0, 0.0);

        assertNotNull(result);
    }
    @Test
    public void Constructor_positiveCAboveOne_positiveDBelowOne() {
        WinningExpectationFunction result = new WinningExpectationFunction(2.0, 0.5);

        assertNotNull(result);
    }
    @Test
    public void Constructor_positiveCAboveOne_positiveDEqualsOne() {
        WinningExpectationFunction result = new WinningExpectationFunction(2.0, 1.0);

        assertNotNull(result);
    }

    @Test
    public void calculateProbabilityHomeWinAndHalfDraw_eloHomeAndHomeAdvantageEqualEloAway() {
        WinningExpectationFunction winningExpectationFunction = new WinningExpectationFunction();

        Probability result = winningExpectationFunction.calculateProbabilityHomeWinAndHalfDraw(0, 0, 0);

        assertNotNull(result);
        assertTrue(Probability.UNCERTAIN.compareTo(result) == 0);
    }
    @Test
    public void calculateProbabilityHomeWinAndHalfDraw_eloHomeAndHomeAdvantageAboveEloAway() {
        WinningExpectationFunction winningExpectationFunction = new WinningExpectationFunction();

        Probability result = winningExpectationFunction.calculateProbabilityHomeWinAndHalfDraw(200, 0, 100);

        assertNotNull(result);
        assertTrue(Probability.UNCERTAIN.compareTo(result) < 0);
        assertTrue(Probability.CERTAIN.compareTo(result) > 0);
    }
    @Test
    public void calculateProbabilityHomeWinAndHalfDraw_eloHomeAndHomeAdvantageBelowEloAway() {
        WinningExpectationFunction winningExpectationFunction = new WinningExpectationFunction();

        Probability result = winningExpectationFunction.calculateProbabilityHomeWinAndHalfDraw(0, 200, 100);

        assertNotNull(result);
        assertTrue(Probability.IMPOSSIBLE.compareTo(result) < 0);
        assertTrue(Probability.UNCERTAIN.compareTo(result) > 0);
    }

    @Test
    public void calculateProbabilityDraw_eloHomeAndHomeAdvantageEqualEloAway() {
        WinningExpectationFunction winningExpectationFunction = new WinningExpectationFunction();

        Probability result = winningExpectationFunction.calculateProbabilityHomeWinAndHalfDraw(0, 0, 0);

        assertNotNull(result);
        assertTrue(Probability.IMPOSSIBLE.compareTo(result) < 0);
        assertTrue(Probability.CERTAIN.compareTo(result) > 0);
    }

    @Test
    public void calculateProbabilityDraw_eloHomeAndHomeAdvantageAboveEloAway() {
        WinningExpectationFunction winningExpectationFunction = new WinningExpectationFunction();

        Probability result = winningExpectationFunction.calculateProbabilityHomeWinAndHalfDraw(200, 0, 100);

        assertNotNull(result);
        assertTrue(Probability.IMPOSSIBLE.compareTo(result) < 0);
        assertTrue(Probability.CERTAIN.compareTo(result) > 0);
    }

    @Test
    public void calculateProbabilityDraw_eloHomeAndHomeAdvantageBelowEloAway() {
        WinningExpectationFunction winningExpectationFunction = new WinningExpectationFunction();

        Probability result = winningExpectationFunction.calculateProbabilityHomeWinAndHalfDraw(0, 200, 100);

        assertNotNull(result);
        assertTrue(Probability.IMPOSSIBLE.compareTo(result) < 0);
        assertTrue(Probability.CERTAIN.compareTo(result) > 0);
    }
}