package de.coiaf.footballprediction.common.cache;

import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.function.Supplier;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public abstract class AbstractCacheTest<C extends AbstractCache<String, String>> {

    private static final String DEFAULT_KEY = "key";
    private static final String DEFAULT_VALUE = "value";
    private static final long SLEEPING_TIME_IN_MILLISECONDS = 1000L;

    private static Supplier<String> createValueSupplierMock(String value) {
        @SuppressWarnings("unchecked")
        Supplier<String> valueSupplierMock = (Supplier<String>) mock(Supplier.class);

        when(valueSupplierMock.get()).thenReturn(value);

        return valueSupplierMock;
    }

    @SuppressWarnings("SameParameterValue")
    private static Supplier<String> createValueSupplierSpy(String value, long sleepingTimeInMilliseconds) {
        Supplier<String> valueSupplierSpy = new AbstractCacheTest.ValueSupplierImpl(value, sleepingTimeInMilliseconds);

        return spy(valueSupplierSpy);
    }

    @Test(expected = NullPointerException.class)
    public void get_nullKey_supplierReturningValue() {
        C cache = this.createCache();
        Supplier<String> valueSupplierMock = createValueSupplierMock(DEFAULT_VALUE);

        cache.get(null, valueSupplierMock);
    }

    @Test(expected = NullPointerException.class)
    public void get_givenKey_nullSupplier() {
        C cache = this.createCache();

        cache.get(DEFAULT_KEY, null);
    }

    @Test
    public void get_givenKey_supplierReturningNull_singleCacheCall() {
        C cache = this.createCache();
        Supplier<String> valueSupplierMock = createValueSupplierMock(null);

        String result = cache.get(DEFAULT_KEY, valueSupplierMock);

        assertNull(result);

        verify(valueSupplierMock, times(1)).get();
    }

    @Test
    public void get_givenKey_supplierReturningNull_multipleCacheCalls() {
        C cache = this.createCache();
        Supplier<String> valueSupplierMock = createValueSupplierMock(null);

        cache.get(DEFAULT_KEY, valueSupplierMock);

        String result = cache.get(DEFAULT_KEY, valueSupplierMock);

        assertNull(result);

        verify(valueSupplierMock, times(1)).get();
    }

    @Test
    public void get_givenKey_supplierReturningValue_singleCacheCall() {
        C cache = this.createCache();
        Supplier<String> valueSupplierMock = createValueSupplierMock(DEFAULT_VALUE);

        String result = cache.get(DEFAULT_KEY, valueSupplierMock);

        assertNotNull(result);
        assertEquals(DEFAULT_VALUE, result);

        verify(valueSupplierMock, times(1)).get();
    }

    @Test
    public void get_givenKey_supplierReturningValue_multipleCacheCalls() {
        C cache = this.createCache();
        Supplier<String> valueSupplierMock = createValueSupplierMock(DEFAULT_VALUE);

        cache.get(DEFAULT_KEY, valueSupplierMock);

        String result = cache.get(DEFAULT_KEY, valueSupplierMock);

        assertNotNull(result);
        assertEquals(DEFAULT_VALUE, result);

        verify(valueSupplierMock, times(1)).get();
    }

    @Test
    public void get_givenKey_supplierReturningValue_multipleConcurrentCacheCalls() {
        C cacheSpy = spy(this.createCache());
        Supplier<String> valueSupplierSpy = createValueSupplierSpy(DEFAULT_VALUE, SLEEPING_TIME_IN_MILLISECONDS);

        Thread t1 = this.createCacheCallingThread(cacheSpy, DEFAULT_KEY, valueSupplierSpy);
        Thread t2 = this.createCacheCallingThread(cacheSpy, DEFAULT_KEY, valueSupplierSpy);
        Thread t3 = this.createCacheCallingThread(cacheSpy, DEFAULT_KEY, valueSupplierSpy);

        boolean t1Alive = true;
        boolean t2Alive = true;
        boolean t3Alive = true;
        int maxThreadsAlive = 0;

        t1.start();
        t2.start();
        t3.start();

        while (t1Alive || t2Alive || t3Alive) {
            t1Alive = t1.isAlive();
            t2Alive = t2.isAlive();
            t3Alive = t3.isAlive();

            int threadsAlive = (t1Alive ? 1 : 0) + (t2Alive ? 1 : 0) + (t3Alive ? 1 : 0);

            if (threadsAlive > maxThreadsAlive) {
                maxThreadsAlive = threadsAlive;
            }
        }

        assertEquals(3, maxThreadsAlive);

        verify(valueSupplierSpy, times(1)).get();
        verify(cacheSpy, times(3)).get(DEFAULT_KEY, valueSupplierSpy);
    }

    @Test
    public void get_givenKey_supplierReturningValue_expirySupplierReturningNull() {
        C cache = this.createCache(() -> null);
        Supplier<String> valueSupplierMock = createValueSupplierMock(DEFAULT_VALUE);

        cache.get(DEFAULT_KEY, valueSupplierMock);

        String result = cache.get(DEFAULT_KEY, valueSupplierMock);

        assertNotNull(result);
        assertEquals(DEFAULT_VALUE, result);

        verify(valueSupplierMock, times(1)).get();
    }

    @Test
    public void get_givenKey_supplierReturningValue_expirySupplierReturningPast() {
        C cache = this.createCache(() -> ZonedDateTime.now().minusHours(1L));
        Supplier<String> valueSupplierMock = createValueSupplierMock(DEFAULT_VALUE);

        cache.get(DEFAULT_KEY, valueSupplierMock);

        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String result = cache.get(DEFAULT_KEY, valueSupplierMock);

        assertNotNull(result);
        assertEquals(DEFAULT_VALUE, result);

        verify(valueSupplierMock, times(2)).get();
    }

    @Test
    public void get_givenKey_supplierReturningValue_expirySupplierReturningPresent() {
        C cache = this.createCache(ZonedDateTime::now);
        Supplier<String> valueSupplierMock = createValueSupplierMock(DEFAULT_VALUE);

        cache.get(DEFAULT_KEY, valueSupplierMock);

        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String result = cache.get(DEFAULT_KEY, valueSupplierMock);

        assertNotNull(result);
        assertEquals(DEFAULT_VALUE, result);

        verify(valueSupplierMock, times(2)).get();
    }

    @Test
    public void get_givenKey_supplierReturningValue_expirySupplierReturningFuture() {
        C cache = this.createCache(() -> ZonedDateTime.now().plusHours(1L));
        Supplier<String> valueSupplierMock = createValueSupplierMock(DEFAULT_VALUE);

        cache.get(DEFAULT_KEY, valueSupplierMock);

        String result = cache.get(DEFAULT_KEY, valueSupplierMock);

        assertNotNull(result);
        assertEquals(DEFAULT_VALUE, result);

        verify(valueSupplierMock, times(1)).get();
    }

    private C createCache() {
        return this.createCache(null);
    }

    protected abstract C createCache(Supplier<ZonedDateTime> nextExpirySupplier);

    @SuppressWarnings("SameParameterValue")
    private Thread createCacheCallingThread(C cache, String key, Supplier<String> valueSupplierSpy) {
        return new Thread(() -> cache.get(key, valueSupplierSpy));
    }

    protected static class ValueSupplierImpl implements Supplier<String> {

        private final String value;
        private final long sleepingTimeInMilliseconds;

        private ValueSupplierImpl(String value, long sleepingTimeInMilliseconds) {
            this.value = value;
            this.sleepingTimeInMilliseconds = sleepingTimeInMilliseconds;
        }

        @Override
        public String get() {
            try {
                Thread.sleep(this.sleepingTimeInMilliseconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return this.value;
        }
    }

}