package de.coiaf.footballprediction.sharedkernal.domain.model.matches;

import java.io.Serializable;
import java.time.LocalDate;

public class Fixture extends AbstractFixture<LocalDate> implements Serializable {

    /**
     * Creates a fixture instance
     * @param kickoffTime the kickoff day
     * @param homeTeamId the identifier for the home team
     * @param awayTeamId the identifier for away home team
     * @return the created fixture instance
     * @throws NullPointerException if any of the parameters is null
     * @throws IllegalArgumentException if homeTeamId equals awawyTeamId
     */
    public static Fixture valueOf(LocalDate kickoffTime, TeamId homeTeamId, TeamId awayTeamId) {
        return new Fixture(kickoffTime, homeTeamId, awayTeamId);
    }

    private Fixture(LocalDate kickoffTime, TeamId homeTeamId, TeamId awayTeamId) {
        super(kickoffTime, homeTeamId, awayTeamId);
    }
}
