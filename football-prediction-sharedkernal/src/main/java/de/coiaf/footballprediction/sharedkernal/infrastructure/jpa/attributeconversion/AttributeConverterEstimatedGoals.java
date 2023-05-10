package de.coiaf.footballprediction.sharedkernal.infrastructure.jpa.attributeconversion;

import de.coiaf.footballprediction.sharedkernal.domain.model.prediction.EstimatedGoals;

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
