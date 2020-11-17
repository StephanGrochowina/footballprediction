package de.coiaf.footballprediction.common.cache;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class CacheFactory {

    /**
     * Creates a cache initializing it with {@code initializationData}. If {@code initializationData} is empty
     * this method will behave like {@link CacheFactory#createEmptyCache()}.
     * The entries added to the cache will not expire.
     * @param initializationData the data to pre-populate the cache with
     * @param <K> the type of the key of the cache
     * @param <V> the type of the value of the cache
     * @return a Cache<K, V> instance pre-populated by {@code initializationData}
     * @throws NullPointerException if {@code initializationData} is null
     */
    public <K, V> Cache<K, V> createPrePopulatedCache(Map<K, V> initializationData) {
        return this.createPrePopulatedCache(initializationData, null);
    }

    /**
     * Creates a cache initializing it with {@code initializationData}. If {@code initializationData} is empty
     * this method will behave like {@link CacheFactory#createEmptyCache(Supplier)}.
     * The entries added to the cache will expire depending on {@code expirySupplier}.
     * @param initializationData the data to pre-populate the cache with
     * @param expirySupplier the functionality for creating the expiry of newly created cache entries. Expiration is disabled if
     *                       {@code expirySupplier} is null
     * @param <K> the type of the key of the cache
     * @param <V> the type of the value of the cache
     * @return a Cache<K, V> instance pre-populated by {@code initializationData}
     * @throws NullPointerException if {@code initializationData} is null
     */
    public <K, V> Cache<K, V> createPrePopulatedCache(Map<K, V> initializationData, Supplier<ZonedDateTime> expirySupplier) {
        Cache<K, V> cache = this.createEmptyCache(initializationData, expirySupplier);

        this.prePopulateCache(cache, initializationData);

        return cache;
    }

    private <K, V> Cache<K, V> createEmptyCache(Map<K, V> initializationData, Supplier<ZonedDateTime> expirySupplier) {
        Objects.requireNonNull(initializationData, "Parameter initializationData should not be null.");

        return new UnorderedCache<>(expirySupplier);
    }

    /**
     * Creates an empty cache. The entries added to the cache will not expire.
     * @param <K> the type of the key of the cache
     * @param <V> the type of the value of the cache
     * @return an empty Cache<K, V> instance
     * @throws NullPointerException if {@code keyClass} or {@code valueClass} is null
     */
    public <K, V> Cache<K, V> createEmptyCache() {
        return this.createEmptyCache(null);
    }

    /**
     * Creates an empty cache. The entries added to the cache will expire depending on {@code expirySupplier}.
     * @param expirySupplier the functionality for creating the expiry of newly created cache entries. Expiration is disabled if
     *                       {@code expirySupplier} is null
     * @param <K> the type of the key of the cache
     * @param <V> the type of the value of the cache
     * @return an empty Cache<K, V> instance
     * @throws NullPointerException if {@code keyClass} or {@code valueClass} is null
     */
    public <K, V> Cache<K, V> createEmptyCache(Supplier<ZonedDateTime> expirySupplier) {
        return new UnorderedCache<>(expirySupplier);
    }

    /**
     * Creates an ordered cache initializing it with {@code initializationData}. If {@code initializationData} is empty
     * this method will behave like {@link CacheFactory#createEmptyOrderedCache()}.
     * The entries added to the cache will not expire.
     * @param initializationData the data to pre-populate the cache with
     * @param <K> the type of the key of the cache
     * @param <V> the type of the value of the cache
     * @return a Cache<K, V> instance pre-populated by {@code initializationData}
     * @throws NullPointerException if {@code initializationData} is null
     */
    public <K, V> Cache<K, V> createPrePopulatedOrderedCache(Map<K, V> initializationData) {
        return this.createPrePopulatedOrderedCache(initializationData, null);
    }

    /**
     * Creates an ordered cache initializing it with {@code initializationData}. If {@code initializationData} is empty
     * this method will behave like {@link CacheFactory#createEmptyOrderedCache(Supplier)}.
     * The entries added to the cache will expire depending on {@code expirySupplier}.
     * @param initializationData the data to pre-populate the cache with
     * @param expirySupplier the functionality for creating the expiry of newly created cache entries. Expiration is disabled if
     *                       {@code expirySupplier} is null
     * @param <K> the type of the key of the cache
     * @param <V> the type of the value of the cache
     * @return a Cache<K, V> instance pre-populated by {@code initializationData}
     * @throws NullPointerException if {@code initializationData} is null
     */
    public <K, V> Cache<K, V> createPrePopulatedOrderedCache(Map<K, V> initializationData, Supplier<ZonedDateTime> expirySupplier) {
        Cache<K, V> cache = this.createEmptyOrderedCache(initializationData, expirySupplier);

        this.prePopulateCache(cache, initializationData);

        return cache;
    }

    private <K, V> void prePopulateCache(Cache<K, V> cache, Map<K, V> initializationData) {
        Objects.requireNonNull(cache, "Parameter cache should not be null.");

        if (initializationData != null && !initializationData.isEmpty()) {
            initializationData.entrySet().stream()
                    .filter(Objects::nonNull)
                    .filter(kvEntry -> kvEntry.getKey() != null)
                    .forEach(kvEntry -> cache.get(kvEntry.getKey(), kvEntry::getValue));
        }

    }

    private <K, V> Cache<K, V> createEmptyOrderedCache(Map<K, V> initializationData, Supplier<ZonedDateTime> expirySupplier) {
        Objects.requireNonNull(initializationData, "Parameter initializationData should not be null.");

        return new OrderedCache<>(expirySupplier);
    }

    /**
     * Creates an empty ordered cache. The entries added to the cache will not expire.
     * @param <K> the type of the key of the cache
     * @param <V> the type of the value of the cache
     * @return an empty Cache<K, V> instance
     * @throws NullPointerException if {@code keyClass} or {@code valueClass} is null
     */
    public <K, V> Cache<K, V> createEmptyOrderedCache() {
        return this.createEmptyOrderedCache((Supplier<ZonedDateTime>) null);
    }

    /**
     * Creates an empty ordered cache. The entries added to the cache will expire depending on {@code expirySupplier}.
     * @param expirySupplier the functionality for creating the expiry of newly created cache entries. Expiration is disabled if
     *                       {@code expirySupplier} is null
     * @param <K> the type of the key of the cache
     * @param <V> the type of the value of the cache
     * @return an empty Cache<K, V> instance
     * @throws NullPointerException if {@code keyClass} or {@code valueClass} is null
     */
    public <K, V> Cache<K, V> createEmptyOrderedCache(Supplier<ZonedDateTime> expirySupplier) {
        return new OrderedCache<>(expirySupplier);
    }
}
