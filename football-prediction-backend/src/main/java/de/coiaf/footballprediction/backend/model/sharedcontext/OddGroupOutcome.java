package de.coiaf.footballprediction.backend.model.sharedcontext;

import de.coiaf.random.distributions.enumerated.EnumDistribution;
import de.coiaf.random.distributions.enumerated.elo.Elo1x2Context;
import de.coiaf.random.distributions.enumerated.matchOutcome.MatchOutcomeDistributionFactory;
import de.coiaf.random.distributions.enumerated.matchOutcome.Outcomes;
import de.coiaf.random.odds.DecimalOdd;
import de.coiaf.random.probability.Probability;

import java.util.Objects;

public class OddGroupOutcome {

    private final DecimalOdd oddHomeWin;
    private final DecimalOdd oddDraw;
    private final DecimalOdd oddAwayWin;

    /**
     * Creates an odd group for the given enum distribution
     * @param distribution the enum distribution of the outcomes
     * @return an {@link OddGroupOutcome} instance
     * @throws NullPointerException if {@code OddGroupOutcome} is null.
     */
    public static OddGroupOutcome createInstance(EnumDistribution<Outcomes, Probability> distribution) {
        Objects.requireNonNull(distribution, "Parameter distribution must not be null.");

        return createInstance(
                distribution.getDensity(Outcomes.HOME_WIN),
                distribution.getDensity(Outcomes.DRAW),
                distribution.getDensity(Outcomes.AWAY_WIN));
    }

    /**
     * Creates an odd group for the given home win, draw and away win probabilities
     * @param probabilityHomeWin the probability of an home win
     * @param probabilityDraw the probability of a draw
     * @param probabilityAwayWin the probability of an away win
     * @return an {@link OddGroupOutcome} instance
     * @throws NullPointerException if {@code probabilityHomeWin}, {@code probabilityDraw} or
     *         {@code probabilityAwayWin} is null.
     */
    public static OddGroupOutcome createInstance(Probability probabilityHomeWin, Probability probabilityDraw, Probability probabilityAwayWin) {
        Objects.requireNonNull(probabilityHomeWin, "Parameter probabilityHomeWin must not be null.");
        Objects.requireNonNull(probabilityDraw, "Parameter probabilityDraw must not be null.");
        Objects.requireNonNull(probabilityAwayWin, "Parameter probabilityAwayWin must not be null.");

        return createInstance(DecimalOdd.from(probabilityHomeWin), DecimalOdd.from(probabilityDraw), DecimalOdd.from(probabilityAwayWin));
    }

    /**
     * Creates an odd group for the given home win, draw and away win odds
     * @param oddHomeWin the home win odds
     * @param oddDraw the draw odds
     * @param oddAwayWin the away win odds
     * @return an {@link OddGroupOutcome} instance
     * @throws NullPointerException if {@code oddHomeWin}, {@code oddDraw} or {@code oddAwayWin} is null.
     */
    public static OddGroupOutcome createInstance(DecimalOdd oddHomeWin, DecimalOdd oddDraw, DecimalOdd oddAwayWin) {
        return new OddGroupOutcome(oddHomeWin, oddDraw, oddAwayWin);
    }

    private OddGroupOutcome(DecimalOdd oddHomeWin, DecimalOdd oddDraw, DecimalOdd oddAwayWin) {
        Objects.requireNonNull(oddHomeWin, "Parameter oddHomeWin must not be null.");
        Objects.requireNonNull(oddDraw, "Parameter oddDraw must not be null.");
        Objects.requireNonNull(oddAwayWin, "Parameter oddAwayWin must not be null.");

        this.oddHomeWin = oddHomeWin;
        this.oddDraw = oddDraw;
        this.oddAwayWin = oddAwayWin;
    }

    /**
     * Creates an enum distribution for the current instance
     * @return the created distribution
     */
    public EnumDistribution<Outcomes, Probability> toEnumDistribution() {
        return MatchOutcomeDistributionFactory.createDistribution(
                this.oddHomeWin.getImpliedProbability(),
                this.oddDraw.getImpliedProbability(),
                this.oddAwayWin.getImpliedProbability()
        );
    }

    public DecimalOdd getOddHomeWin() {
        return this.oddHomeWin;
    }

    public DecimalOdd getOddDraw() {
        return this.oddDraw;
    }

    public DecimalOdd getOddAwayWin() {
        return this.oddAwayWin;
    }
}
