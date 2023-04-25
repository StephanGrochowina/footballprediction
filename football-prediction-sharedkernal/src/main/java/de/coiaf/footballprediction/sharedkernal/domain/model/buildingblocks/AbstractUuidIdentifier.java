package de.coiaf.footballprediction.sharedkernal.domain.model.buildingblocks;

import java.io.Serializable;
import java.util.UUID;

/**
 * Base class for identifiers using UUIDs
 */
public abstract class AbstractUuidIdentifier extends AbstractIdentifier<UUID> implements Serializable {

    protected AbstractUuidIdentifier() {
        super(UUID::randomUUID);
    }

    protected AbstractUuidIdentifier(UUID idValue) {
        super(idValue);
    }

    @Override
    public String toString() {
        return this.getIdValue().toString();
    }
}
