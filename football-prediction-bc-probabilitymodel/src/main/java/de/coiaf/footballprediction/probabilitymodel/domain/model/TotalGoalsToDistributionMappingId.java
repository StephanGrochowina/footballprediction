package de.coiaf.footballprediction.probabilitymodel.domain.model;

import de.coiaf.footballprediction.sharedkernal.domain.model.buildingblocks.AbstractUuidIdentifier;

import java.util.UUID;

public class TotalGoalsToDistributionMappingId extends AbstractUuidIdentifier {

    /**
     * Creates a random identifier.
     * @return an instance of the identifier
     */
    public static TotalGoalsToDistributionMappingId createRandomIdentifier() {
        return new TotalGoalsToDistributionMappingId();
    }

    /**
     * Creates an identifier for the given id
     * @param id the internal id
     * @return an instance of the identifier
     */
    public static TotalGoalsToDistributionMappingId of(UUID id) {
        return id == null ? null : new TotalGoalsToDistributionMappingId(id);
    }

    private TotalGoalsToDistributionMappingId() {
        super();
    }

    private TotalGoalsToDistributionMappingId(UUID idValue) {
        super(idValue);
    }
}
