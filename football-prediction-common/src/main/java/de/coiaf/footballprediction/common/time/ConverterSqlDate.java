package de.coiaf.footballprediction.common.time;

import java.sql.Date;
import java.time.LocalDate;

public class ConverterSqlDate {

    public static Date convertToSqlDate(LocalDate source) {
        return source == null ? null : Date.valueOf(source);
    }

    public static LocalDate convertToLocalDate(Date source) {
        return source == null ? null : source.toLocalDate();
    }

}
