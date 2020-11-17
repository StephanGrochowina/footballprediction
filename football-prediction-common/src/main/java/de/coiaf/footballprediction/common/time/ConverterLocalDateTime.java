package de.coiaf.footballprediction.common.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

public class ConverterLocalDateTime {

    public static LocalDateTime convert(LocalDate source) {
        return source == null ? null : source.atStartOfDay();
    }

    public static LocalDateTime convert(ZonedDateTime source) {
        return source == null ? null : source.toLocalDateTime();
    }

    public static LocalDateTime convert(OffsetDateTime source) {
        return source == null ? null : source.toLocalDateTime();
    }
}
