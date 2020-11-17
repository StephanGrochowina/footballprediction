package de.coiaf.footballprediction.common.cache;

import de.coiaf.footballprediction.common.time.ConverterToZonedDateTime;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.function.Supplier;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CacheValueExpirySupplierTest {

    @Test
    public void get_nullSupplier() {
        CacheValueExpirySupplier expirySupplierWrapper = new CacheValueExpirySupplier(null);

        assertNull(expirySupplierWrapper.get());
    }

    @Test
    public void get_supplierReturningNull() {
        @SuppressWarnings("unchecked")
        Supplier<ZonedDateTime> expirySupplierMock = mock(Supplier.class);
        when(expirySupplierMock.get()).thenReturn(null);

        CacheValueExpirySupplier expirySupplierWrapper = new CacheValueExpirySupplier(expirySupplierMock);

        assertNull(expirySupplierWrapper.get());
    }

    @Test
    public void get_supplierReturningExpiredExpiryValue() {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime expiry = now.minusHours(1L);
        @SuppressWarnings("unchecked")
        Supplier<ZonedDateTime> expirySupplierMock = mock(Supplier.class);
        when(expirySupplierMock.get()).thenReturn(expiry);

        CacheValueExpirySupplier expirySupplierWrapper = new CacheValueExpirySupplier(expirySupplierMock);

        ZonedDateTime determinedExpiryThreshold = expirySupplierWrapper.get();

        assertNotNull(determinedExpiryThreshold);
        assertTrue(expiry.isBefore(determinedExpiryThreshold));
        assertTrue(now.isBefore(determinedExpiryThreshold));
    }

    @Test
    public void get_supplierReturningExpiryValueIsNow() {
        ZonedDateTime now = ZonedDateTime.now();
        @SuppressWarnings("unchecked")
        Supplier<ZonedDateTime> expirySupplierMock = mock(Supplier.class);
        when(expirySupplierMock.get()).thenReturn(now);

        CacheValueExpirySupplier expirySupplierWrapper = new CacheValueExpirySupplier(expirySupplierMock);

        ZonedDateTime determinedExpiryThreshold = expirySupplierWrapper.get();

        assertNotNull(determinedExpiryThreshold);
        assertTrue(now.isBefore(determinedExpiryThreshold));
    }

    @Test
    public void get_supplierReturningFutureExpiryValue() {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime expiry = now.plusHours(1L);
        ZonedDateTime expectedExpiry = ConverterToZonedDateTime.convertToTimeZoneUTC(expiry);
        @SuppressWarnings("unchecked")
        Supplier<ZonedDateTime> expirySupplierMock = mock(Supplier.class);
        when(expirySupplierMock.get()).thenReturn(expiry);

        CacheValueExpirySupplier expirySupplierWrapper = new CacheValueExpirySupplier(expirySupplierMock);

        ZonedDateTime determinedExpiryThreshold = expirySupplierWrapper.get();

        assertNotNull(determinedExpiryThreshold);
        assertEquals(expectedExpiry, determinedExpiryThreshold);
        assertTrue(now.isBefore(determinedExpiryThreshold));
    }
}