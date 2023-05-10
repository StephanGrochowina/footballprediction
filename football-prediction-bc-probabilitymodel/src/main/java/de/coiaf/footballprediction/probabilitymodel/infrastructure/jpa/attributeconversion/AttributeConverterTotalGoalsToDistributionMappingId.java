package de.coiaf.footballprediction.probabilitymodel.infrastructure.jpa.attributeconversion;

import de.coiaf.footballprediction.probabilitymodel.domain.model.TotalGoalsToDistributionMappingId;
import de.coiaf.footballprediction.sharedkernal.infrastructure.jpa.attributeconversion.AbstractAttributeConverterUuidIdentifier;

import javax.persistence.Converter;
import java.util.UUID;

@Converter(autoApply = true)
public class AttributeConverterTotalGoalsToDistributionMappingId extends AbstractAttributeConverterUuidIdentifier<TotalGoalsToDistributionMappingId> {

    @Override
    protected TotalGoalsToDistributionMappingId createIdentifier(UUID idValue) {
        return TotalGoalsToDistributionMappingId.of(idValue);
    }
}
