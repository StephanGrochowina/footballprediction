package de.coiaf.footballprediction.backend.persistence.attributeconversion;

import de.coiaf.footballprediction.backend.model.sharedcontext.EstimatedGoals;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.math.BigDecimal;

@Converter(autoApply = true)
public class AttributeConverterEstimatedGoals implements AttributeConverter<EstimatedGoals, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(EstimatedGoals propertyValue) {
        return propertyValue == null ? null : propertyValue.toBigDecimal();
    }

    @Override
    public EstimatedGoals convertToEntityAttribute(BigDecimal databaseColumnValue) {
        return EstimatedGoals.valueOf(databaseColumnValue, true);
    }
}
