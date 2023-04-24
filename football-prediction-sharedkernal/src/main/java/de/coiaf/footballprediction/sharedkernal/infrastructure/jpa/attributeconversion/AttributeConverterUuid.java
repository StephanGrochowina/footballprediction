package de.coiaf.footballprediction.sharedkernal.infrastructure.jpa.attributeconversion;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.UUID;

@Converter(autoApply = true)
public class AttributeConverterUuid implements AttributeConverter<UUID, String> {

    @Override
    public String convertToDatabaseColumn(UUID propertyValue) {
        return ConverterUuid.convert(propertyValue);
    }

    @Override
    public UUID convertToEntityAttribute(String databaseColumnValue) {
        return ConverterUuid.convert(databaseColumnValue);
    }
}
