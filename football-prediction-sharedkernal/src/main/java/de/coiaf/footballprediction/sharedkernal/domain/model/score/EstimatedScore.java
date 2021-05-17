package de.coiaf.footballprediction.sharedkernal.domain.model.score;

import de.coiaf.footballprediction.sharedkernal.domain.model.buildingblocks.ValueObject;

import java.io.Serializable;
import java.util.Objects;

public class EstimatedScore implements ValueObject<EstimatedScore>, Serializable {

    private final EstimatedGoals homeGoals;
    private final EstimatedGoals awayGoals;
    private final EstimatedGoals totalGoals;

    public static EstimatedScore valueOf(Double homeGoals, Double awayGoals) {
        return new EstimatedScore(EstimatedGoals.valueOf(homeGoals, true), EstimatedGoals.valueOf(awayGoals, true));
    }

    public static EstimatedScore valueOf(EstimatedGoals homeGoals, EstimatedGoals awayGoals) {
        return new EstimatedScore(homeGoals, awayGoals);
    }

    private EstimatedScore(EstimatedGoals homeGoals, EstimatedGoals awayGoals) {
        Objects.requireNonNull(homeGoals, "Parameter homeGoals must not be null.");
        Objects.requireNonNull(awayGoals, "Parameter awayGoals must not be null.");

        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
        this.totalGoals = homeGoals.add(awayGoals);
    }

    public EstimatedGoals getHomeGoals() {
        return this.homeGoals;
    }

    public EstimatedGoals getAwayGoals() {
        return this.awayGoals;
    }

    public EstimatedGoals getTotalGoals() {
        return this.totalGoals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EstimatedScore that = (EstimatedScore) o;
        return Objects.equals(getHomeGoals(), that.getHomeGoals()) &&
                Objects.equals(getAwayGoals(), that.getAwayGoals());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHomeGoals(), getAwayGoals());
    }
}
