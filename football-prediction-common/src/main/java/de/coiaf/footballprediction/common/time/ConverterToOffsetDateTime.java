package de.coiaf.footballprediction.common.time;

import java.time.*;
import java.util.Objects;

public class ConverterToOffsetDateTime {

    public static OffsetDateTime convertToTimeZoneUTC(OffsetDateTime source) {
        return convertToTimeZone(source, ZoneOffset.UTC);
    }

    public static OffsetDateTime convertToTimeZone(OffsetDateTime source, ZoneOffset offset) {
        OffsetDateTime result = source;

        if (source != null && offset != null) {
            result = source.withOffsetSameInstant(offset);
        }

        return result;
    }

    public static OffsetDateTime convertAtTimeZoneUTC(LocalDate source) {
        return convertAtTimeZoneUTC(ConverterLocalDateTime.convert(source));
    }

    public static OffsetDateTime convertAtTimeZoneUTC(LocalDateTime source) {
        return convert(source, ZoneOffset.UTC);
    }

    public static OffsetDateTime convert(LocalDate source, ZoneOffset offset) {
        return convert(ConverterLocalDateTime.convert(source), offset);
    }

    public static OffsetDateTime convert(LocalDateTime source, ZoneOffset offset) {
        Objects.requireNonNull(offset, "Parameter offset should not be null.");

        return source == null ? null : source.atOffset(offset);
    }

    public static OffsetDateTime convert(ZonedDateTime source) {
        return source == null ? null : source.toOffsetDateTime();
    }
}
