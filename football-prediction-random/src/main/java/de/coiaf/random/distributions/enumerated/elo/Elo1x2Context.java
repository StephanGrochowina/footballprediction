package de.coiaf.random.distributions.enumerated.elo;

import de.coiaf.random.probability.Probability;

import java.util.Objects;

public class Elo1x2Context {

    private final Probability homeWin;
    private final Probability draw;
    private final Probability awayWin;

    Elo1x2Context(Probability homeWin, Probability draw, Probability awayWin) {
        Objects.requireNonNull(homeWin, "Parameter homeWin must not be null.");
        Objects.requireNonNull(draw, "Parameter draw must not be null.");
        Objects.requireNonNull(awayWin, "Parameter awayWin must not be null.");

        Probability total = homeWin.add(draw).add(awayWin);
        if (Probability.CERTAIN.compareTo(total) != 0) {
            throw new IllegalArgumentException("The sum of the parameters is not 1.");
        }

        this.homeWin = homeWin;
        this.draw = draw;
        this.awayWin = awayWin;
    }

    public Probability getHomeWin() {
        return this.homeWin;
    }

    public Probability getDraw() {
        return this.draw;
    }

    public Probability getAwayWin() {
        return this.awayWin;
    }
}
