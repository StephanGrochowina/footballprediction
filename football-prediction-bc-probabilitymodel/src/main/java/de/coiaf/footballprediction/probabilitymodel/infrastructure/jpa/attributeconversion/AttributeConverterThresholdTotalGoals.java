package de.coiaf.footballprediction.probabilitymodel.infrastructure.jpa.attributeconversion;

import de.coiaf.footballprediction.probabilitymodel.domain.model.ThresholdTotalGoals;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.math.BigDecimal;

@Converter(autoApply = true)
class AttributeConverterThresholdTotalGoals implements AttributeConverter<ThresholdTotalGoals, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(ThresholdTotalGoals propertyValue) {
        return propertyValue == null ? null : propertyValue.toBigDecimal();
    }

    @Override
    public ThresholdTotalGoals convertToEntityAttribute(BigDecimal databaseColumnValue) {
        return ThresholdTotalGoals.valueOf(databaseColumnValue, true);
    }
}
