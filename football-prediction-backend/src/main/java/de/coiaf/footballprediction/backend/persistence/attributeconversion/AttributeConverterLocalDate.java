package de.coiaf.footballprediction.backend.persistence.attributeconversion;

import de.coiaf.footballprediction.common.time.ConverterSqlDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;

@Converter(autoApply = true)
public class AttributeConverterLocalDate implements AttributeConverter<LocalDate, Date> {

    @Override
    public Date convertToDatabaseColumn(LocalDate propertyValue) {
        return ConverterSqlDate.convertToSqlDate(propertyValue);
    }

    @Override
    public LocalDate convertToEntityAttribute(Date databaseColumnValue) {
        return ConverterSqlDate.convertToLocalDate(databaseColumnValue);
    }
}
