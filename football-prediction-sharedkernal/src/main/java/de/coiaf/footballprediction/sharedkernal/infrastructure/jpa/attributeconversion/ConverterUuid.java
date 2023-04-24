package de.coiaf.footballprediction.sharedkernal.infrastructure.jpa.attributeconversion;

import java.util.UUID;

class ConverterUuid {

    static String convert(UUID uuid) {
        return uuid == null ? null : uuid.toString();
    }

    static UUID convert(String idValue) {
        if (idValue == null) {
            return null;
        }

        UUID result;

        try {
            result = UUID.fromString(idValue);
        }
        catch (IllegalArgumentException e) {
            result = null;
        }

        return result;
    }
}
