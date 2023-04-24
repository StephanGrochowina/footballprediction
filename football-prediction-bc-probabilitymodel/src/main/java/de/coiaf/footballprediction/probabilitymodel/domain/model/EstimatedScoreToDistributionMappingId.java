package de.coiaf.footballprediction.probabilitymodel.domain.model;

import de.coiaf.footballprediction.sharedkernal.domain.model.buildingblocks.AbstractUuidIdentifier;

import java.util.UUID;

public class EstimatedScoreToDistributionMappingId extends AbstractUuidIdentifier {

    /**
     * Creates a random identifier.
     * @return an instance of the identifier
     */
    public static EstimatedScoreToDistributionMappingId createRandomIdentifier() {
        return new EstimatedScoreToDistributionMappingId();
    }

    /**
     * Creates an identifier for the given id
     * @param id the internal id
     * @return an instance of the identifier
     */
    public static EstimatedScoreToDistributionMappingId of(UUID id) {
        return id == null ? null : new EstimatedScoreToDistributionMappingId(id);
    }

    private EstimatedScoreToDistributionMappingId() {
        super();
    }

    private EstimatedScoreToDistributionMappingId(UUID idValue) {
        super(idValue);
    }
}
