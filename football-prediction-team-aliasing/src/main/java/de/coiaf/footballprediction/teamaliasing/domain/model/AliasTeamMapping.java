package de.coiaf.footballprediction.teamaliasing.domain.model;

import de.coiaf.footballprediction.sharedkernal.domain.model.common.buildingblocks.AbstractAggregateRoot;

import java.util.Objects;
import java.util.UUID;

/**
 * the representation of a mapping between an alias and a team.
 */
public class AliasTeamMapping extends AbstractAggregateRoot<UUID, AliasTeamMappingIdentifier, AliasTeamMapping> {

    private final Object valueChangeLock = new Object();
    private Alias alias;
    private Team team;

    /**
     * constructor
     * @param identifier the identifier for this mapping
     * @param alias the alias for this mapping
     * @throws NullPointerException if any of the parameters is null
     */
    AliasTeamMapping(AliasTeamMappingIdentifier identifier, Alias alias) {
        super(identifier);
        this.setAlias(alias);
    }

    /**
     * constructor
     * @param identifier the identifier for this mapping
     * @param team the team for the mapping
     * @throws NullPointerException if any of the parameters is null
     */
    AliasTeamMapping(AliasTeamMappingIdentifier identifier, Team team) {
        super(identifier);
        this.setTeam(team);
    }

    public Alias getAlias() {
        Alias currentAlias;

        synchronized (this.valueChangeLock) {
            currentAlias = this.alias;
        }

        return currentAlias;
    }

    private void setAlias(Alias alias) {
        synchronized (this.valueChangeLock) {
            if (!this.isMappedToTeam()) {
                Objects.requireNonNull(alias);
            }

            this.alias = alias;
        }
    }

    public Team getTeam() {
        Team currentTeam;

        synchronized (this.valueChangeLock) {
            currentTeam = this.team;
        }

        return currentTeam;
    }

    private void setTeam(Team team) {
        synchronized (this.valueChangeLock) {
            if (!this.isMappedToAlias()) {
                Objects.requireNonNull(team);
            }

            this.team = team;
        }
    }

    /**
     *
     * @return if this mapping is complete, i.e. an alias and a team
     * are assigned to this mapping.
     */
    public boolean isMapped() {
        return this.isMappedToAlias() && this.isMappedToTeam();
    }

    /**
     *
     * @return if an alias is assigned to this mapping
     */
    public boolean isMappedToAlias() {
        return this.getAlias() != null;
    }

    /**
     * Assigns an alias to this mapping.
     * @param alias the alias to be assigned.
     * @throws NullPointerException if the alias is null.
     */
    public void assignAlias(Alias alias) {
        Objects.requireNonNull(alias, "Parameter alias must not be null");

        this.setAlias(alias);
    }

    /**
     * Deletes the alias assignment.
     * @throws IllegalStateException if the alias cannot be removed.
     */
    public void removeAlias() {
        try {
            this.setAlias(null);
        }
        catch (NullPointerException ex) {
            throw new IllegalStateException("Cannot remove alias if team is not set.");
        }
    }

    /**
     * Renames the alias.
     * @param aliasName the new name for the alias
     * @throws NullPointerException if aliasName is null
     * @throws IllegalStateException if no alias is assigned to this instance
     */
    public void renameAlias(ClubName aliasName) {
        synchronized (this.valueChangeLock) {
            if (!this.isMappedToAlias()) {
                throw new IllegalStateException("No alias present to be renamed.");
            }

            this.getAlias().setAliasName(aliasName);
        }
    }

    /**
     *
     * @return if a team is assigned to this mapping
     */
    public boolean isMappedToTeam() {
        return this.getTeam() != null;
    }

    /**
     * Assigns a team to this mapping.
     * @param team the team to be assigned
     * @throws NullPointerException if the team is null.
     */
    public void assignTeam(Team team) {
        Objects.requireNonNull(team, "Parameter team must not be null");

        this.setTeam(team);
    }

    /**
     * Deletes the team assignment.
     * @throws IllegalStateException if the team cannot be removed.
     */
    public void removeTeam() {
        try {
            this.setTeam(null);
        }
        catch (NullPointerException ex) {
            throw new IllegalStateException("Cannot remove team if alias is not set.");
        }
    }

    /**
     * Renames the team.
     * @param teamName the new name for the team
     * @throws NullPointerException if teamName is null
     * @throws IllegalStateException if no team is assigned to this instance
     */
    public void renameTeam(ClubName teamName) {
        synchronized (this.valueChangeLock) {
            if (!this.isMappedToTeam()) {
                throw new IllegalStateException("No team present to be renamed.");
            }

            this.getTeam().setTeamName(teamName);
        }
    }
}
