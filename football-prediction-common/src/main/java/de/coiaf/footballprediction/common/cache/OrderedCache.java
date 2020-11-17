package de.coiaf.footballprediction.common.cache;

import java.lang.ref.SoftReference;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

class OrderedCache<K, V> extends AbstractCache<K, V> {

    private final Map<K, SoftReference<CachedValue<V>>> internalCache = new LinkedHashMap<>();

    OrderedCache(Supplier<ZonedDateTime> expirySupplier) {
        super(expirySupplier);
    }

    @Override
    protected Map<K, SoftReference<CachedValue<V>>> getInternalCache() {
        return this.internalCache;
    }
}
