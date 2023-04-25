package de.coiaf.footballprediction.sharedkernal.domain.model.matches;

import de.coiaf.footballprediction.sharedkernal.domain.model.buildingblocks.ValueObject;

import java.time.temporal.Temporal;
import java.util.Objects;

public class AbstractFixture<T extends Temporal> implements ValueObject<AbstractFixture<T>> {

    private final T kickoffTime;
    private final TeamId homeTeamId;
    private final TeamId awayTeamId;

    /**
     * Creates a fixture instance.
     * @param kickoffTime the kickoff time
     * @param homeTeamId the identifier for the home team
     * @param awayTeamId the identifier for away home team
     * @throws NullPointerException if any of the parameters is null
     * @throws IllegalArgumentException if homeTeamId equals awawyTeamId
     */
    protected AbstractFixture(T kickoffTime, TeamId homeTeamId, TeamId awayTeamId) {
        Objects.requireNonNull(kickoffTime, "Kickoff time must not be null.");
        Objects.requireNonNull(homeTeamId, "The home team identifier must not be null.");
        Objects.requireNonNull(awayTeamId, "The away team identifier must not be null.");

        if (homeTeamId.isSameValue(awayTeamId)) {
            throw new IllegalArgumentException("Home and away team identifiers must not be equal.");
        }

        this.kickoffTime = kickoffTime;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
    }

    public T getKickoffTime() {
        return this.kickoffTime;
    }

    public TeamId getHomeTeamId() {
        return this.homeTeamId;
    }

    public TeamId getAwayTeamId() {
        return this.awayTeamId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        boolean result = false;
        Class<?> currentClass = this.getClass();

        if (other != null && currentClass.isAssignableFrom(other.getClass())) {
            AbstractFixture<?> that = (AbstractFixture<?>) other;

            result = Objects.equals(getKickoffTime(), that.getKickoffTime()) &&
                    Objects.equals(getHomeTeamId(), that.getHomeTeamId()) &&
                    Objects.equals(getAwayTeamId(), that.getAwayTeamId());
        }

        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKickoffTime(), getHomeTeamId(), getAwayTeamId());
    }
}
