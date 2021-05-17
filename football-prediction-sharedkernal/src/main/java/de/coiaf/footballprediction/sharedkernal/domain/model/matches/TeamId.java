package de.coiaf.footballprediction.sharedkernal.domain.model.matches;

import de.coiaf.footballprediction.sharedkernal.domain.model.buildingblocks.AbstractUuidIdentifier;

import java.util.UUID;

public class TeamId extends AbstractUuidIdentifier {

    /**
     * Creates a new TeamId instance with a random UUID.
     * @return a new TeamId instance
     */
    public static TeamId newInstance() {
        return new TeamId();
    }

    /**
     * Creates a new TeamId instance with idValue
     * @param idValue the id to create the instance with
     * @return a new TeamId instance with the given id
     * @throws NullPointerException if idValue is null
     */
    public static TeamId valueOf(final UUID idValue) {
        return new TeamId(idValue);
    }

    private TeamId() {
        super();
    }

    private TeamId(UUID idValue) {
        super(idValue);
    }
}
