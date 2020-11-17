package de.coiaf.footballprediction.common.cache;

import java.lang.ref.SoftReference;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

abstract class AbstractCache<K, V> implements Cache<K, V> {
    private final ReentrantLock lock = new ReentrantLock();
    private final Supplier<ZonedDateTime> expirySupplier;

    AbstractCache(Supplier<ZonedDateTime> expirySupplier) {
        this.expirySupplier = new CacheValueExpirySupplier(expirySupplier);
    }

    @Override
    public Stream<V> streamValues() {
        Map<K, SoftReference<CachedValue<V>>> cache = this.getInternalCache();

        if (cache == null) {
            return Stream.empty();
        }

        return cache.values()
                .stream()
                .filter(Objects::nonNull)
                .map(SoftReference::get)
                .filter(cachedValue -> cachedValue != null && !cachedValue.hasExpired())
                .map(CachedValue::getValue);
    }

    protected abstract Map<K, SoftReference<CachedValue<V>>> getInternalCache();

    /**
     * non-blocking access to the cache instance
     * @param key the key for accessing the cache
     * @param valueSupplier the logic to retrieve a value for the {@code key} if it doesn´t
     *                      exist in this cache instance or if the entry has expired.
     * @param callback the callback the value is passed to after it has been retrieved from the cache
     * @throws NullPointerException if {@code key}, {@code valueSupplier} or {@code callback} is null.
     */
    @Override
    public void getAsync(K key, Supplier<V> valueSupplier, Consumer<V> callback) {
        Objects.requireNonNull(key, "Parameter key should not be null.");
        Objects.requireNonNull(valueSupplier, "Parameter valueSupplier should not be null.");
        Objects.requireNonNull(callback, "Parameter callback should not be null.");

        CompletableFuture.supplyAsync(() -> this.get(key, valueSupplier))
                .thenAccept(callback);
    }

    /**
     * Retrieves a value vor {@code key from this instance}
     * @param key the key for accessing the cache
     * @param valueSupplier the logic to retrieve a value for the {@code key} if it doesn´t
     *                      exist in this cache instance or if the entry has expired.
     * @return the value for the key
     * @throws NullPointerException if {@code key} or {@code valueSupplier} is null.
     */
    @Override
    public V get(K key, Supplier<V> valueSupplier) {
        Objects.requireNonNull(key, "Parameter key should not be null.");
        Objects.requireNonNull(valueSupplier, "Parameter valueSupplier should not be null.");

        V result;

        this.lock.lock();
        try {
            CachedValue<V> cachedValue = this.getCacheValue(key);

            if (cachedValue != null && !cachedValue.hasExpired()) {
                result = cachedValue.getValue();
            }
            else {
                result = valueSupplier.get();
                this.putCacheValue(key, new CachedValue<>(result, this.expirySupplier.get()));
            }
        }
        finally {
            this.lock.unlock();
        }

        return result;
    }

    private CachedValue<V> getCacheValue(K key) {
        SoftReference<CachedValue<V>> reference = this.getInternalCache().get(key);

        return reference == null ? null : reference.get();
    }

    private void putCacheValue(K key, CachedValue<V> cachedValue) {
        SoftReference<CachedValue<V>> reference = new SoftReference<>(cachedValue);

        this.getInternalCache().put(key, reference);
    }

    protected static class CachedValue<V> {

        private final V value;
        private final ZonedDateTime expiry;

        private CachedValue(V value, ZonedDateTime expiry) {
            this.value = value;
            this.expiry = expiry;
        }

        public V getValue() {
            return this.value;
        }

        private boolean hasExpired() {
            return this.hasExpired(ZonedDateTime.now());
        }

        private boolean hasExpired(ZonedDateTime referenceDateTime) {
            return this.expiry != null && referenceDateTime != null && !this.expiry.isAfter(referenceDateTime);
        }
    }
}
