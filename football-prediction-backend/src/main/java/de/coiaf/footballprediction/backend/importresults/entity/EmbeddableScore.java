package de.coiaf.footballprediction.backend.importresults.entity;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Embeddable
public class EmbeddableScore implements Serializable {
    public enum Outcome {
        HOME_WIN, DRAW, AWAY_WIN;

        public static Outcome determineOutcome(int goalsHome, int goalsAway) {
            Outcome outcome;

            if (goalsHome < goalsAway) {
                outcome = AWAY_WIN;
            }
            else if (goalsHome == goalsAway) {
                outcome = DRAW;
            }
            else {
                outcome = HOME_WIN;
            }

            return outcome;
        }
    }

    private int goalsHome;
    private int goalsAway;
    private Outcome outcome;

    protected EmbeddableScore() {
    }

    public EmbeddableScore(int goalsHome, int goalsAway) {
        this.goalsHome = goalsHome;
        this.goalsAway = goalsAway;
        this.outcome = Outcome.determineOutcome(goalsHome, goalsAway);
    }

    @DecimalMin("0")
    @DecimalMax("999")
    public int getGoalsHome() {
        return this.goalsHome;
    }

    protected void setGoalsHome(int goalsHome) {
        this.goalsHome = goalsHome;
    }

    @DecimalMin("0")
    @DecimalMax("999")
    public int getGoalsAway() {
        return this.goalsAway;
    }

    protected void setGoalsAway(int goalsAway) {
        this.goalsAway = goalsAway;
    }

    @NotNull
    @Enumerated(EnumType.STRING)
    public Outcome getOutcome() {
        return this.outcome;
    }

    protected void setOutcome(Outcome outcome) {
        this.outcome = outcome;
    }
}
