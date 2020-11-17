package de.coiaf.footballprediction.backend.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class ImporterCsv {

    private static final CSVFormat DEFAULT_SOURCE_IMPORT_FORMAT = CSVFormat.RFC4180.withFirstRecordAsHeader()
            .withIgnoreHeaderCase()
            .withTrim()
            .withIgnoreEmptyLines()
            .withAllowMissingColumnNames();


    public void importCsv(String sourceUrl, Charset charset, CSVFormat format, Consumer<CSVRecord> consumerRecord) throws IOException {
        Objects.requireNonNull(sourceUrl);
        Objects.requireNonNull(charset);
        Objects.requireNonNull(consumerRecord);

        CSVFormat usedFormat = format == null ? DEFAULT_SOURCE_IMPORT_FORMAT : format;
        URL source = new URL(sourceUrl);
        CSVParser parser = CSVParser.parse(source, charset, usedFormat);

        for (CSVRecord record : parser) {
            consumerRecord.accept(record);
        }
    }

    public Integer getInteger(CSVRecord record, String column) {
        return this.getValue(record, column, rawValue -> Integer.valueOf(rawValue));
    }

    public LocalDate getLocalDate(CSVRecord record, String column, DateTimeFormatter formatter) {
        return this.getValue(record, column, rawValue -> LocalDate.parse(rawValue, formatter));
    }
    public LocalDate getLocalDate(CSVRecord record, String column, Function<String, LocalDate> converter) {
        return this.getValue(record, column, converter);
    }

    public LocalTime getLocalTime(CSVRecord record, String column, DateTimeFormatter formatter) {
        return this.getValue(record, column, rawValue -> LocalTime.parse(rawValue, formatter));
    }

    public LocalDateTime getLocalDateTime(CSVRecord record, String column, DateTimeFormatter formatter) {
        return this.getValue(record, column, rawValue -> LocalDateTime.parse(rawValue, formatter));
    }

    public BigDecimal getBigDecimal(CSVRecord record, String column) {
        return this.getValue(record, column, rawValue -> new BigDecimal(rawValue));
    }

    public String getString(CSVRecord record, String column) {
        return this.getValue(record, column, rawValue -> rawValue.trim());
    }

    private <T> T getValue(CSVRecord record, String column, Function<String, T> converter) {
        Objects.requireNonNull(record);
        Objects.requireNonNull(column);
        Objects.requireNonNull(converter);

        String rawValue;
        T result = null;

        try {
            rawValue = record.isMapped(column) ? record.get(column) : null;
        }
        catch (IllegalArgumentException e) {
            rawValue = null;
        }

        if (rawValue != null && !rawValue.trim().isEmpty()) {
            try {
                result = converter.apply(rawValue.trim());
            }
            catch (RuntimeException e) {
                throw new IllegalArgumentException("Illegal value '" + rawValue + "' in column '" + column + "' (record: " + record.toString() + ")", e);
            }
        }

        return result;
    }
}
