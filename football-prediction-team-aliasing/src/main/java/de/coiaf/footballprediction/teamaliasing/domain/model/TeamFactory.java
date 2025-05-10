package de.coiaf.footballprediction.teamaliasing.domain.model;

import java.util.UUID;

/**
 * Factory to create team entities
 */
public class TeamFactory {

    /**
     * Creates a {@link Team} entity with a random identifier and the given
     * team name.
     * @param teamName the name of the team
     * @return the {@link Team} instance to be created
     * @throws NullPointerException if team name is null
     */
    public static Team createInstance(String teamName) {
        TeamIdentifier identifierValue = TeamIdentifier.createRandomIdentifier();
        ClubName teamNameValue = ClubName.valueOf(teamName);

        return createInstance(identifierValue, teamNameValue);
    }

    /**
     * Creates a {@link Team} entity with the given identifier and team name.
     * @param identifier the entity identifier, if null a randomly created
     *                   identifier will be used internally.
     * @param teamName the name of the team
     * @return the {@link Team} instance to be created
     * @throws NullPointerException if team name is null
     */
    public static Team createInstance(UUID identifier, String teamName) {
        TeamIdentifier identifierValue = TeamIdentifier.of(identifier);
        ClubName teamNameValue = ClubName.valueOf(teamName);

        return createInstance(identifierValue, teamNameValue);
    }

    /**
     * Creates a {@link Team} entity with a random identifier and the given
     * team name.
     * @param teamName the name of the team
     * @return the {@link Team} instance to be created
     * @throws NullPointerException if team name is null
     */
    public static Team createInstance(ClubName teamName) {
        TeamIdentifier identifier = TeamIdentifier.createRandomIdentifier();

        return createInstance(identifier, teamName);
    }

    /**
     * Creates a {@link Team} entity with the given identifier and team name.
     * @param identifier the entity identifier
     * @param teamName the name of the team
     * @return the {@link Team} instance to be created
     * @throws NullPointerException if any of the parameters is null
     */
    public static Team createInstance(TeamIdentifier identifier, ClubName teamName) {
        return new Team(identifier, teamName);
    }
}
