package de.coiaf.footballprediction.backend.persistence.attributeconversion;

import de.coiaf.footballprediction.backend.model.sharedcontext.ThresholdTotalGoals;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.math.BigDecimal;

@Converter(autoApply = true)
public class AttributeConverterThresholdTotalGoals implements AttributeConverter<ThresholdTotalGoals, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(ThresholdTotalGoals propertyValue) {
        return propertyValue == null ? null : propertyValue.toBigDecimal();
    }

    @Override
    public ThresholdTotalGoals convertToEntityAttribute(BigDecimal databaseColumnValue) {
        return ThresholdTotalGoals.valueOf(databaseColumnValue, true);
    }
}
