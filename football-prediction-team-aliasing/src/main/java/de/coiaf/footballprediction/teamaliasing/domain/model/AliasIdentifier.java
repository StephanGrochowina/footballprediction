package de.coiaf.footballprediction.teamaliasing.domain.model;

import de.coiaf.footballprediction.sharedkernal.domain.model.common.buildingblocks.AbstractUuidIdentifier;

import java.util.UUID;

public class AliasIdentifier extends AbstractUuidIdentifier {

    /**
     * Creates a random identifier.
     * @return an instance of the identifier
     */
    public static AliasIdentifier createRandomIdentifier() {
        return new AliasIdentifier();
    }

    /**
     * Creates an identifier for the given id
     * @param id the internal id
     * @return an instance of the identifier
     */
    public static AliasIdentifier of(UUID id) {
        return id == null ? new AliasIdentifier() : new AliasIdentifier(id);
    }

    private AliasIdentifier() {
        super();
    }

    private AliasIdentifier(UUID idValue) {
        super(idValue);
    }
}
