package de.coiaf.footballprediction.backend.importresults.entity;

import javax.persistence.Embeddable;
import javax.validation.constraints.DecimalMin;

@Embeddable
public class EmbeddableMatchStat {

    private int matchStatHome;
    private int matchStatAway;

    protected EmbeddableMatchStat() {
    }

    public EmbeddableMatchStat(int matchStatHome, int matchStatAway) {
        this.matchStatHome = matchStatHome;
        this.matchStatAway = matchStatAway;
    }

    @DecimalMin("0")
    public int getMatchStatHome() {
        return this.matchStatHome;
    }

    protected void setMatchStatHome(int matchStatHome) {
        this.matchStatHome = matchStatHome;
    }

    @DecimalMin("0")
    public int getMatchStatAway() {
        return this.matchStatAway;
    }

    protected void setMatchStatAway(int matchStatAway) {
        this.matchStatAway = matchStatAway;
    }
}
