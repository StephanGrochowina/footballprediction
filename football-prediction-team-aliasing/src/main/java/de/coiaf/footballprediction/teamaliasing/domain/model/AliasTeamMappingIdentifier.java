package de.coiaf.footballprediction.teamaliasing.domain.model;

import de.coiaf.footballprediction.sharedkernal.domain.model.common.buildingblocks.AbstractUuidIdentifier;

import java.util.UUID;

public class AliasTeamMappingIdentifier extends AbstractUuidIdentifier {

    /**
     * Creates a random identifier.
     * @return an instance of the identifier
     */
    public static AliasTeamMappingIdentifier createRandomIdentifier() {
        return new AliasTeamMappingIdentifier();
    }

    /**
     * Creates an identifier for the given id
     * @param id the internal id
     * @return an instance of the identifier
     */
    public static AliasTeamMappingIdentifier of(UUID id) {
        return id == null ? new AliasTeamMappingIdentifier() : new AliasTeamMappingIdentifier(id);
    }

    private AliasTeamMappingIdentifier() {
        super();
    }

    private AliasTeamMappingIdentifier(UUID idValue) {
        super(idValue);
    }
}
