package de.coiaf.footballprediction.common.cache;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This class is a factory which produces instances of type <V>. These instances are stored in an internal
 * cache under the key <K>. If an instance should be produced for a key already existing in the cache the
 * cached instance will be returned instead of creating a new one.
 * @param <K> the type of the key instances
 * @param <V> the type of the instances to be cached.
 */
public class ElementCachingFactory<K, V> {
    private static final CacheFactory CACHE_FACTORY = new CacheFactory();

    private final Cache<K, V> builderCache;

    public static <K, V> ElementCachingFactory<K, V> createFactory(Class<K> keyClass, Class<V> valueClass) {
        Objects.requireNonNull(keyClass, "Parameter keyClass must not be null.");
        Objects.requireNonNull(valueClass, "Parameter valueClass must not be null.");

        return new ElementCachingFactory<>();
    }

    private ElementCachingFactory() {
        this.builderCache = CACHE_FACTORY.createEmptyCache();
    }

    /**
     * Initializes the factory with the key supplied by {@code keySupplier}  and the
     * value [@code value}. If there already exists a value for that key no value will
     * be added to the cache.
     * @param keySupplier the supplier for a key by which the value is stored
     * @param value the value to be internally stored under the key supplied by
     *              {@code keySupplier}
     * @return the current instance
     * @throws NullPointerException if {@code keySupplier} is null.
     * @throws IllegalArgumentException if {@code keySupplier.get()} returns null
     */
    public ElementCachingFactory<K, V> initCache(Supplier<K> keySupplier, V value) {
        return this.initCache(keySupplier, () -> value);
    }

    /**
     * Initializes the factory with the key supplied by {@code keySupplier} and the
     * value supplied by {@code valueSupplier}. If there already exists a value for
     * that key no value will be added to the cache.
     * @param keySupplier the supplier for a key by which the value is stored
     * @param valueSupplier the supplier for a value to be created and to be internally
     *                      stored under the key supplied by {@code keySupplier}
     * @return the current instance
     * @throws NullPointerException if {@code keySupplier} or {@code valueSupplier} is null.
     * @throws IllegalArgumentException if {@code keySupplier.get()} returns null
     */
    public ElementCachingFactory<K, V> initCache(Supplier<K> keySupplier, Supplier<V> valueSupplier) {
        this.createValue(keySupplier, valueSupplier);

        return this;
    }

    /**
     * Initializes the factory with the key {@code key} and the value [@code value}.
     * If there already exists a value for {@code key} no value will be added to the
     * cache.
     * @param key the key by which the value is stored
     * @param value the value to be internally stored under {@code key}
     * @return the current instance
     * @throws NullPointerException if {@code key} is null.
     */
    public ElementCachingFactory<K, V> initCache(K key, V value) {
        return this.initCache(key, () -> value);
    }

    /**
     * Initializes the factory with the key {@code key} and the value supplied by
     * {@code valueSupplier}. If there already exists a value for {@code key} no
     * value will be added to the cache.
     * @param key the key by which the value is stored
     * @param valueSupplier the supplier for a value to be created and to be internally
     *                      stored under {@code key}
     * @return the current instance
     * @throws NullPointerException if {@code key} or {@code valueSupplier} is null.
     */
    public ElementCachingFactory<K, V> initCache(K key, Supplier<V> valueSupplier) {
        this.createValue(key, valueSupplier);

        return this;
    }

    /**
     * Reads the value from the cache or creates a new one and caches it if the cache of
     * this instance does not contain any entry for the key supplied by {@code keySupplier}.
     * @param keySupplier the the supplier for the key by which the value is stored
     * @param valueSupplierFunction a function transforming the key supplied by {@code keySupplier}
     *                              to a Supplier<V> instance. That supplier will be used
     *                              to create a value instance if no value has been
     *                              stored internally for the key supplied by {@code keySupplier}.
     * @return the cached or created value
     * @throws NullPointerException if {@code keySupplier} or {@code valueSupplierFunction} is null.
     * @throws IllegalArgumentException if {@code keySupplier.get()} or
     *                                  {@code valueSupplierFunction.apply(key)} returns null
     */
    public V createValue(Supplier<K> keySupplier, Function<K, Supplier<V>> valueSupplierFunction) {
        K key = this.determineValidKey(keySupplier);

        return this.createValue(key, valueSupplierFunction);
    }

    /**
     * Reads the value from the cache or creates a new one and caches it if the cache of
     * this instance does not contain any entry for the key {@code key}.
     * @param key the key by which the value is stored
     * @param valueSupplierFunction a function transforming the key {@code key} to a
     *                              Supplier<V> instance. That supplier will be used
     *                              to create a value instance if no value has been
     *                              stored internally for the key {@code key}.
     * @return the cached or created value
     * @throws NullPointerException if {@code key} or {@code valueSupplierFunction} is null.
     * @throws IllegalArgumentException if {@code valueSupplierFunction.apply(key)} returns null
     */
    public V createValue(K key, Function<K, Supplier<V>> valueSupplierFunction) {
        Objects.requireNonNull(key, "Parameter key must not be null.");
        Objects.requireNonNull(valueSupplierFunction, "Parameter valueSupplierFunction must not be null.");

        Supplier<V> valueSupplier = valueSupplierFunction.apply(key);

        if (valueSupplier == null) {
            throw new IllegalArgumentException("valueSupplierFunction.apply(key) must not return null.");
        }

        return this.createValue(key, valueSupplier);
    }

    /**
     * Reads the value from the cache or creates a new one and caches it if the cache of
     * this instance does not contain any entry for the key supplied by {@code keySupplier}.
     * @param keySupplier the supplier for the key
     * @param valueSupplier the supplier which will be used to create a value if no entry
     *      *               exists for the key supplied by {@code keySupplier}
     * @return the cached or created value
     * @throws NullPointerException if {@code keySupplier} or {@code valueSupplier} is null.
     * @throws IllegalArgumentException if {@code keySupplier.get()} returns null
     */
    public V createValue(Supplier<K> keySupplier, Supplier<V> valueSupplier) {
        K key = this.determineValidKey(keySupplier);

        return this.builderCache.get(key, valueSupplier);
    }

    private K determineValidKey(Supplier<K> keySupplier) {
        Objects.requireNonNull(keySupplier, "Parameter keySupplier must not be null.");

        K key = keySupplier.get();

        if (key == null) {
            throw new IllegalArgumentException("keySupplier.get() must not return null.");
        }

        return key;
    }

    /**
     * Reads the value from the cache or creates a new one and caches it if the cache of
     * this instance does not contain any entry for {@code key}.
     * @param key the key by which the value is stored
     * @param valueSupplier the supplier which will be used to create a value if no entry
     *                      exists for {@code key}
     * @return the cached or created value
     * @throws NullPointerException if {@code key} or {@code valueSupplier} is null.
     */
    public V createValue(K key, Supplier<V> valueSupplier) {
        return this.builderCache.get(key, valueSupplier);
    }
}
