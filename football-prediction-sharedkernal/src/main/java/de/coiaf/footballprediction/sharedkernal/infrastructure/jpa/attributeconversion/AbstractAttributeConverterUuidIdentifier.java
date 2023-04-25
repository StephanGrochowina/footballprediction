package de.coiaf.footballprediction.sharedkernal.infrastructure.jpa.attributeconversion;

import de.coiaf.footballprediction.sharedkernal.domain.model.buildingblocks.AbstractUuidIdentifier;

import javax.persistence.AttributeConverter;
import java.util.UUID;

/**
 * Superclass for all attribute converters to and from ddd identifiers which represent a UUID.
 * @param <ID>
 */
public abstract class AbstractAttributeConverterUuidIdentifier<ID extends AbstractUuidIdentifier> implements AttributeConverter<ID, String> {

    @Override
    public String convertToDatabaseColumn(ID propertyValue) {
        return propertyValue == null ? null : propertyValue.toString();
    }

    @Override
    public ID convertToEntityAttribute(String databaseColumnValue) {
        UUID idValue = ConverterUuid.convert(databaseColumnValue);

        return idValue == null ? null : this.createIdentifier(idValue);
    }

    /**
     * Creates an identifier instance for the given idValue
     * @param idValue the UUID of the identifier
     * @return the identifier instance
     */
    protected abstract ID createIdentifier(UUID idValue);
}
