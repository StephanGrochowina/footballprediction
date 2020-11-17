package de.coiaf.footballprediction.common.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

public class ConverterToLocalDate {

    public static LocalDate convertTo(LocalDateTime source) {
        return source == null ? null : source.toLocalDate();
    }

    public static LocalDate convert(ZonedDateTime source) {
        return source == null ? null : source.toLocalDate();
    }

    public static LocalDate convert(OffsetDateTime source) {
        return source == null ? null : source.toLocalDate();
    }
}
