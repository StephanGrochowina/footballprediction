package de.coiaf.random.distributions.enumerated.elo;

import de.coiaf.random.distributions.continuous.ContinuousDistribution;
import de.coiaf.random.distributions.continuous.ContinuousDistributions;
import de.coiaf.random.probability.Probability;

/**
 * This class calculates the elo winning expectancy for a home team depending on the old Elo values
 * of an home and an away team according to this formula:
 *
 * Wea = 1/(1+c^(-(eloH + HomeAdvantage - eloA)/d))
 *      = pHomeWin(eloH, eloA, HomeAdvantage, c, d) + 0.5 * pDraw(eloH, eloA, HomeAdvantage, c, d).
 *
 * The draw probability is inferred by using the normal distribution p = N(0, e^2):
 * pDraw(eloH, eloA, HomeAdvantage, c, d) = p(((eloH + HomeAdvantage - eloA) * ln(c)) / d)
 *
 * By default, c = 10 and d = 400. However, these values may be set in the constructor.
 */
class WinningExpectationFunction {
    static final double DEFAULT_D = 400;
    static final double DEFAULT_C = 10;
    private static final double DEFAULT_SCALE = calculateEBasedScale(DEFAULT_C, DEFAULT_D);
    static final double DEFAULT_HOME_ADVANTAGE = 80;

    private final ContinuousDistribution distributionHomeWinAwayWin;
    private final ContinuousDistribution distributionDraw;
    private final double scale;

    private static double calculateEBasedScale(double c, double d) {
        if (c <= 1.0) {
            throw new IllegalArgumentException("Parameter c must be greater than one.");
        }
        if (d <= 0.0) {
            throw new IllegalArgumentException("Parameter d must be greater than zero.");
        }

        double scaleDivisor = Math.log(c);

        return d/scaleDivisor;
    }

    private static double calculateDr(double eloHome, double eloAway, double homeAdvantage) {
        return eloHome + homeAdvantage - eloAway;
    }

    /**
     * Creates an instance initializing it with the default base c = 10 and default scale d = 400
     */
    WinningExpectationFunction() {
        this(DEFAULT_SCALE);
    }

    /**
     * Creates an instance initializing it with the given base c = 10 and the given scale {@code d}
     * @param c the base
     * @param d the scale
     * @throws IllegalArgumentException if c is not greater than 1 or d is not greater than 0
     */
    WinningExpectationFunction(double c, double d) {
        this(calculateEBasedScale(c, d));
    }

    private WinningExpectationFunction(double scale) {
        this.scale = scale;
        this.distributionHomeWinAwayWin = ContinuousDistributions.createLogisticDistribution(0.0, scale);
        this.distributionDraw = ContinuousDistributions.createGaussianDistribution(0.0, Math.E);
    }

    Probability calculateProbabilityHomeWinAndHalfDraw(double eloHome, double eloAway) {
        return this.calculateProbabilityHomeWinAndHalfDraw(eloHome, eloAway, DEFAULT_HOME_ADVANTAGE);
    }

    Probability calculateProbabilityHomeWinAndHalfDraw(double eloHome, double eloAway, double homeAdvantage) {
        double dr = calculateDr(eloHome, eloAway, homeAdvantage);

        return this.distributionHomeWinAwayWin.getDistribution(dr);
    }

    Probability calculateProbabilityDraw(double eloHome, double eloAway) {
        return this.calculateProbabilityDraw(eloHome, eloAway, DEFAULT_HOME_ADVANTAGE);
    }

    Probability calculateProbabilityDraw(double eloHome, double eloAway, double homeAdvantage) {
        double dr = calculateDr(eloHome, eloAway, homeAdvantage);
        double value = dr/this.scale;

        return this.distributionDraw.getDensity(value);
    }
}
