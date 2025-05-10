package de.coiaf.footballprediction.teamaliasing.domain.model;

import de.coiaf.footballprediction.sharedkernal.domain.model.common.buildingblocks.AbstractEntity;

import java.util.Objects;
import java.util.UUID;

/**
 * the team representation of a source for which an alias is required.
 */
public class Alias extends AbstractEntity<UUID, AliasIdentifier, Alias> {

    private final AliasTypes aliasType;
    private ClubName aliasName;

    /**
     * constructor
     *
     * @param identifier the entity identifier
     * @param aliasType the type of the alias
     * @param aliasName the alias name of the team
     * @throws NullPointerException if any of the parameters is null
     */
    Alias(AliasIdentifier identifier, AliasTypes aliasType, ClubName aliasName) {
        super(identifier);

        Objects.requireNonNull(aliasType, "Parameter aliasType must not be null");

        this.aliasType = aliasType;
        this.setAliasName(aliasName);
    }

    public AliasTypes getAliasType() {
        return this.aliasType;
    }

    public ClubName getAliasName() {
        return this.aliasName;
    }

    void setAliasName(ClubName aliasName) {
        Objects.requireNonNull(aliasName, "Parameter aliasName must not be null.");

        this.aliasName = aliasName;
    }


}
