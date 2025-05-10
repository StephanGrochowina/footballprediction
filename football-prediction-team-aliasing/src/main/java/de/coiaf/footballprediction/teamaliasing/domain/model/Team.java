package de.coiaf.footballprediction.teamaliasing.domain.model;

import de.coiaf.footballprediction.sharedkernal.domain.model.common.buildingblocks.AbstractEntity;

import java.util.Objects;
import java.util.UUID;

/**
 * the representation of a team to which aliases may be assigned
 */
public class Team extends AbstractEntity<UUID, TeamIdentifier, Team> {

    private ClubName teamName;

    /**
     * constructor
     *
     * @param identifier the entity identifier
     * @param teamName the name of the team
     * @throws NullPointerException if any of the parameters is null
     */
    Team(TeamIdentifier identifier, ClubName teamName) {
        super(identifier);
        this.setTeamName(teamName);
    }

    public ClubName getTeamName() {
        return this.teamName;
    }

    void setTeamName(ClubName teamName) {
        Objects.requireNonNull(teamName, "Parameter teamName must not be null.");

        this.teamName = teamName;
    }
}
