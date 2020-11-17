package de.coiaf.footballprediction.common.cache;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Cache<K, V> {

    /**
     * non-blocking access to the cache instance
     * @param key the key for accessing the cache
     * @param valueSupplier the logic to retrieve a value for the {@code key} if it doesn´t
     *                      exist in this cache instance or if the entry has expired.
     * @param callback the callback the value is passed to after it has been retrieved from the cache
     * @throws NullPointerException if {@code key}, {@code valueSupplier} or {@code callback} is null.
     */
    void getAsync(K key, Supplier<V> valueSupplier, Consumer<V> callback);

    /**
     * Retrieves a value vor {@code key from this instance}
     * @param key the key for accessing the cache
     * @param valueSupplier the logic to retrieve a value for the {@code key} if it doesn´t
     *                      exist in this cache instance or if the entry has expired.
     * @return the value for the key
     * @throws NullPointerException if {@code key} or {@code valueSupplier} is null.
     */
    V get(K key, Supplier<V> valueSupplier);

    /**
     * Determines the size of this cache.
     * @return number of unexpired cache entries
     */
    default long size() {
        return this.streamValues().count();
    }

    /**
     * Creates a collection of all values stored in this cache and returns it.
     * Modifying this collection does not have any effects on the cache itself.
     * This method never returns null. If the cache is empty an empty collection
     * will be returned instead.
     * @return a collection of all values of this cache
     */
    default Collection<V> values() {
        return this.streamValues().collect(Collectors.toList());
    }

    /**
     * Creates a stream of all values stored in this cache and returns it.
     * This method never returns null. If the cache is empty an empty stream will
     * be returned instead.
     * @return the stream of all values of this cache
     */
    Stream<V> streamValues();
}
