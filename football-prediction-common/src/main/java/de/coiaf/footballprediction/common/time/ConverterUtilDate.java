package de.coiaf.footballprediction.common.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class ConverterUtilDate {

    public static LocalDate convertToLocalDate(Date source) {
        return convertToLocalDate(source, null);
    }

    public static LocalDate convertToLocalDate(Date source, ZoneId zoneId) {
        ZoneId usedZoneId = determineZoneId(zoneId);

        return source == null ? null : source.toInstant().atZone(usedZoneId).toLocalDate();
    }

    public static LocalDateTime convertToLocalDateTime(Date source) {
        return convertToLocalDateTime(source, null);
    }

    public static LocalDateTime convertToLocalDateTime(Date source, ZoneId zoneId) {
        ZoneId usedZoneId = determineZoneId(zoneId);

        return source == null ? null : source.toInstant().atZone(usedZoneId).toLocalDateTime();
    }

    public static Date convertToDate(LocalDate source) {
        return convertToDate(source, null);
    }

    public static Date convertToDate(LocalDate source, ZoneId zoneId) {
        return convertToDate(ConverterLocalDateTime.convert(source), zoneId);
    }

    public static Date convertToDate(LocalDateTime source) {
        return convertToDate(source, null);
    }

    public static Date convertToDate(LocalDateTime source, ZoneId zoneId) {
        ZoneId usedZoneId = determineZoneId(zoneId);

        return source == null ? null : Date.from(source.atZone(usedZoneId).toInstant());
    }

    private static ZoneId determineZoneId(ZoneId zoneId) {
        return zoneId == null ? ZoneId.systemDefault() : zoneId;
    }
}
