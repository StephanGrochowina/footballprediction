package de.coiaf.footballprediction.common.cache;

import de.coiaf.footballprediction.common.time.ConverterToZonedDateTime;

import java.time.ZonedDateTime;
import java.util.function.Supplier;

class CacheValueExpirySupplier implements Supplier<ZonedDateTime> {

    private static final long NANOSECONDS_OF_MIN_EXPIRY_AFTER_NOW = 1L;

    private final Supplier<ZonedDateTime> expirySupplier;

    CacheValueExpirySupplier(Supplier<ZonedDateTime> expirySupplier) {
        this.expirySupplier = expirySupplier;
    }

    @Override
    public ZonedDateTime get() {
        ZonedDateTime expiry = this.expirySupplier == null ? null : expirySupplier.get();
        ZonedDateTime minExpiry = ZonedDateTime.now().plusNanos(NANOSECONDS_OF_MIN_EXPIRY_AFTER_NOW);

        return ConverterToZonedDateTime.convertToTimeZoneUTC(expiry == null || minExpiry.isBefore(expiry) ? expiry : minExpiry);
    }
}
