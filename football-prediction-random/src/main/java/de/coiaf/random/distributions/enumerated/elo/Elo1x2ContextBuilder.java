package de.coiaf.random.distributions.enumerated.elo;

import de.coiaf.random.probability.Probability;

import java.util.Objects;

class Elo1x2ContextBuilder {

    private final WinningExpectationFunction expectationFunction;
    private double homeAdvantage = WinningExpectationFunction.DEFAULT_HOME_ADVANTAGE;

    static Elo1x2ContextBuilder create() {
        WinningExpectationFunction expectationFunction = new WinningExpectationFunction();

        return new Elo1x2ContextBuilder(expectationFunction);
    }

    static Elo1x2ContextBuilder create(double c, double d) {
        WinningExpectationFunction expectationFunction = new WinningExpectationFunction(c, d);

        return new Elo1x2ContextBuilder(expectationFunction);
    }

    private Elo1x2ContextBuilder(WinningExpectationFunction expectationFunction) {
        Objects.requireNonNull(expectationFunction, "Parameter expectationFunction must not be null.");

        this.expectationFunction = expectationFunction;
    }

    Elo1x2ContextBuilder applyHomeAdvantage(double homeAdvantage) {
        this.homeAdvantage = homeAdvantage;

        return this;
    }

    Elo1x2Context buildContext(double eloHome, double eloAway) {
        Probability homeWinAndHalfDraw = this.expectationFunction.calculateProbabilityHomeWinAndHalfDraw(eloHome, eloAway, this.homeAdvantage);
        Probability awayWinAndHalfDraw = homeWinAndHalfDraw.negate();
        Probability draw = this.expectationFunction.calculateProbabilityDraw(eloHome, eloAway, this.homeAdvantage);
        Probability halfDraw = Probability.UNCERTAIN.multiply(draw);
        Probability homeWin = homeWinAndHalfDraw.subtract(halfDraw);
        Probability awayWin = awayWinAndHalfDraw.subtract(halfDraw);

        return new Elo1x2Context(homeWin, draw, awayWin);
    }
}
