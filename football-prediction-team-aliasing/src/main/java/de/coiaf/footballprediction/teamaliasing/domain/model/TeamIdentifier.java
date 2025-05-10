package de.coiaf.footballprediction.teamaliasing.domain.model;

import de.coiaf.footballprediction.sharedkernal.domain.model.common.buildingblocks.AbstractUuidIdentifier;

import java.util.UUID;

public class TeamIdentifier extends AbstractUuidIdentifier {

    /**
     * Creates a random identifier.
     * @return an instance of the identifier
     */
    static TeamIdentifier createRandomIdentifier() {
        return new TeamIdentifier();
    }

    /**
     * Creates an identifier for the given id
     * @param id the internal id
     * @return an instance of the identifier
     */
    static TeamIdentifier of(UUID id) {
        return id == null ? new TeamIdentifier() : new TeamIdentifier(id);
    }

    private TeamIdentifier() {
        super();
    }

    private TeamIdentifier(UUID idValue) {
        super(idValue);
    }
}
