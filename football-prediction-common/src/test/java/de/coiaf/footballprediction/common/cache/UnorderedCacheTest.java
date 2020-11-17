package de.coiaf.footballprediction.common.cache;

import java.time.ZonedDateTime;
import java.util.function.Supplier;

public class UnorderedCacheTest extends AbstractCacheTest<UnorderedCache<String, String>> {

    @Override
    protected UnorderedCache<String, String> createCache(Supplier<ZonedDateTime> nextExpirySupplier) {
        return new UnorderedCache<>(nextExpirySupplier);
    }
}