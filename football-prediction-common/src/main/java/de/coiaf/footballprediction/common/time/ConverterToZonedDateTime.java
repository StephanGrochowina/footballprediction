package de.coiaf.footballprediction.common.time;

import java.time.*;
import java.util.Objects;

public class ConverterToZonedDateTime {

    public static ZonedDateTime convertToTimeZoneUTC(ZonedDateTime source) {
        return convertToTimeZone(source, ZoneOffset.UTC);
    }

    public static ZonedDateTime convertToTimeZone(ZonedDateTime source, ZoneId zoneId) {
        ZonedDateTime result = source;

        if (source != null && zoneId != null) {
            result = source.withZoneSameInstant(zoneId);
        }

        return result;
    }

    public static ZonedDateTime convertAtTimeZoneUTC(LocalDate source) {
        return convertAtTimeZoneUTC(ConverterLocalDateTime.convert(source));
    }

    public static ZonedDateTime convertAtTimeZoneUTC(LocalDateTime source) {
        return convert(source, ZoneOffset.UTC);
    }

    public static ZonedDateTime convert(LocalDate source, ZoneId zoneId) {
        return convert(ConverterLocalDateTime.convert(source), zoneId);
    }

    public static ZonedDateTime convert(LocalDateTime source, ZoneId zoneId) {
        Objects.requireNonNull(zoneId, "Parameter zoneId should not be null.");

        return source == null ? null : source.atZone(zoneId);
    }

    public static ZonedDateTime convert(OffsetDateTime source) {
        return source == null ? null : source.toZonedDateTime();
    }
}
