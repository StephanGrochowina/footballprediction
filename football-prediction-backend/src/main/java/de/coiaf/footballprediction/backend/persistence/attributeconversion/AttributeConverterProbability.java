package de.coiaf.footballprediction.backend.persistence.attributeconversion;

import de.coiaf.random.probability.Probabilities;
import de.coiaf.random.probability.Probability;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.math.BigDecimal;

@Converter(autoApply = true)
public class AttributeConverterProbability implements AttributeConverter<Probability, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(Probability propertyValue) {
        return Probabilities.toFullyScaledBigDecimal(propertyValue);
    }

    @Override
    public Probability convertToEntityAttribute(BigDecimal databaseColumnValue) {
        return databaseColumnValue == null ? null : Probability.valueOf(databaseColumnValue);
    }
}
