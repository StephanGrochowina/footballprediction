package de.coiaf.footballprediction.common.cache;

import java.time.ZonedDateTime;
import java.util.function.Supplier;

public class OrderedCacheTest extends AbstractCacheTest<OrderedCache<String, String>> {

    @Override
    protected OrderedCache<String, String> createCache(Supplier<ZonedDateTime> nextExpirySupplier) {
        return new OrderedCache<>(nextExpirySupplier);
    }
}