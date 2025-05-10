package de.coiaf.footballprediction.teamaliasing.domain.model;

import java.util.Objects;

/**
 * Builder to create an {@link AliasTeamMapping} aggregate root
 */
public class AliasTeamMappingBuilder {

    private AliasTeamMappingIdentifier identifier = AliasTeamMappingIdentifier.createRandomIdentifier();
    private Alias alias = null;
    private Team team = null;

    /**
     * Creates a new builder instance with the given alias and/or team.
     * @param alias the alias to be part of the mapping
     * @param team the team to be part of the mapping
     * @return an {@link AliasTeamMappingBuilder} instance
     * @throws NullPointerException if alias and team are null
     */
    public static AliasTeamMappingBuilder createInstance(Alias alias, Team team) {
        if (alias == null && team == null) {
            throw new NullPointerException("At least one of the parameters alias and team must not be null.");
        }

        if (alias != null) {
            return createInstance(alias)
                    .assignTeamIfNotNull(team);
        }

        return createInstance(team);
    }

    /**
     * Creates a new builder instance with the given alias.
     * @param alias the alias to be part of the mapping
     * @return an {@link AliasTeamMappingBuilder} instance
     * @throws NullPointerException if alias is null
     */
    public static AliasTeamMappingBuilder createInstance(Alias alias) {
        return new AliasTeamMappingBuilder(alias);
    }

    /**
     * Creates a new builder instance with the given team.
     * @param team the team to be part of the mapping
     * @return an {@link AliasTeamMappingBuilder} instance
     * @throws NullPointerException if team is null
     */
    public static AliasTeamMappingBuilder createInstance(Team team) {
        return new AliasTeamMappingBuilder(team);
    }

    private AliasTeamMappingBuilder(Alias alias) {
        this.setAlias(alias);
    }

    private AliasTeamMappingBuilder(Team team) {
        this.setTeam(team);
    }

    private void setIdentifier(AliasTeamMappingIdentifier identifier) {
        Objects.requireNonNull(identifier, "Parameter identifier must not be null.");

        this.identifier = identifier;
    }

    private void setAlias(Alias alias) {
        Objects.requireNonNull(alias, "Parameter alias must not be null.");

        this.alias = alias;
    }

    private void setTeam(Team team) {
        Objects.requireNonNull(team, "Parameter team must not be null.");

        this.team = team;
    }

    /**
     * Assigns an alias to this instance.
     * @param alias the alias to be assigned
     * @return this instance
     * @throws NullPointerException if alias is null
     */
    public AliasTeamMappingBuilder assignAlias(Alias alias) {
        this.setAlias(alias);

        return this;
    }

    private AliasTeamMappingBuilder assignTeamIfNotNull(Team team) {
        if (team != null) {
            this.assignTeam(team);
        }

        return this;
    }

    /**
     * Assigns a team to this instance.
     * @param team the team to be assigned
     * @return this instance
     * @throws NullPointerException if team is null
     */
    public AliasTeamMappingBuilder assignTeam(Team team) {
        this.setTeam(team);

        return this;
    }

    /**
     * Assigns an identifier to this instance.
     * @param identifier the identifier to be assigned
     * @return this instance
     * @throws NullPointerException if identifier is null
     */
    public AliasTeamMappingBuilder assignIdentifier(AliasTeamMappingIdentifier identifier) {
        this.setIdentifier(identifier);

        return this;
    }

    /**
     * Creates a mapping.
     * @return an {@link AliasTeamMapping} instance
     */
    public AliasTeamMapping build() {
        AliasTeamMapping mapping;

        if (this.alias != null) {
            mapping = new AliasTeamMapping(this.identifier, this.alias);

            if (this.team != null) {
                mapping.assignTeam(this.team);
            }
        }
        else {
            mapping = new AliasTeamMapping(this.identifier, this.team);
        }

        return mapping;
    }
}
