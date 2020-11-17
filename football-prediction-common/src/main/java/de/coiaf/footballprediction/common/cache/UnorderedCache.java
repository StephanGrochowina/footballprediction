package de.coiaf.footballprediction.common.cache;

import java.lang.ref.SoftReference;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

class UnorderedCache<K, V> extends AbstractCache<K, V> {

    private final Map<K, SoftReference<CachedValue<V>>> internalCache = new HashMap<>();

    UnorderedCache(Supplier<ZonedDateTime> expirySupplier) {
        super(expirySupplier);
    }

    @Override
    protected Map<K, SoftReference<CachedValue<V>>> getInternalCache() {
        return this.internalCache;
    }
}
