package de.coiaf.footballprediction.probabilitymodel.infrastructure.jpa.attributeconversion;

import de.coiaf.footballprediction.probabilitymodel.domain.model.EstimatedScoreToDistributionMappingId;
import de.coiaf.footballprediction.sharedkernal.infrastructure.jpa.attributeconversion.AbstractAttributeConverterUuidIdentifier;

import javax.persistence.Converter;
import java.util.UUID;

@Converter(autoApply = true)
public class AttributeConverterEstimatedScoreToDistributionMappingId extends AbstractAttributeConverterUuidIdentifier<EstimatedScoreToDistributionMappingId> {

    @Override
    protected EstimatedScoreToDistributionMappingId createIdentifier(UUID idValue) {
        return EstimatedScoreToDistributionMappingId.of(idValue);
    }
}
