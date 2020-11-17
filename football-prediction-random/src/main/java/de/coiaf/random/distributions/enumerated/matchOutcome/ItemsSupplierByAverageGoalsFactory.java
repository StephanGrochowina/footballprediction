package de.coiaf.random.distributions.enumerated.matchOutcome;

import de.coiaf.footballprediction.common.cache.ElementCachingFactory;
import de.coiaf.random.probability.Probability;

import java.util.Objects;
import java.util.function.Supplier;

class ItemsSupplierByAverageGoalsFactory {
    private final ElementCachingFactory<CacheKey, ItemsSupplierByAverageGoals> contextFactory = ElementCachingFactory
            .createFactory(CacheKey.class, ItemsSupplierByAverageGoals.class)
            .initCache(CacheKey.createCacheKey(), new ItemsSupplierByAverageGoals());

    ItemsSupplierByAverageGoals createItemsSupplierByAverageGoals() {
        return this.createItemsSupplierByAverageGoals(CacheKey::createCacheKey);
    }

    ItemsSupplierByAverageGoals createItemsSupplierByAverageGoals(double minimumAverageGoals) {
        return this.createItemsSupplierByAverageGoals(() -> CacheKey.createCacheKey(minimumAverageGoals));
    }

    ItemsSupplierByAverageGoals createItemsSupplierByAverageGoals(Probability accuracy) {
        return this.createItemsSupplierByAverageGoals(() -> CacheKey.createCacheKey(accuracy));
    }

    ItemsSupplierByAverageGoals createItemsSupplierByAverageGoals(double minimumAverageGoals, Probability accuracy) {
        return this.createItemsSupplierByAverageGoals(() -> CacheKey.createCacheKey(minimumAverageGoals, accuracy));
    }

    private ItemsSupplierByAverageGoals createItemsSupplierByAverageGoals(Supplier<CacheKey> keySupplier) {
        return this.contextFactory.createValue(keySupplier, CacheKey::getItemsSupplierByAverageGoalsSupplier);
    }

    private static class CacheKey {
        private final Double minimumAverageGoals;
        private final Probability accuracy;
        private final Supplier<ItemsSupplierByAverageGoals> itemsSupplierByAverageGoalsSupplier;

        static CacheKey createCacheKey() {
            return new CacheKey(null, null, ItemsSupplierByAverageGoals::new);
        }

        static CacheKey createCacheKey(Double minimumAverageGoals) {
            return new CacheKey(minimumAverageGoals, null, () -> new ItemsSupplierByAverageGoals(minimumAverageGoals));
        }

        static CacheKey createCacheKey(Probability accuracy) {
            return new CacheKey(null, accuracy, () -> new ItemsSupplierByAverageGoals(accuracy));
        }

        static CacheKey createCacheKey(Double minimumAverageGoals, Probability accuracy) {
            return new CacheKey(minimumAverageGoals, accuracy, () -> new ItemsSupplierByAverageGoals(minimumAverageGoals, accuracy));
        }

        private CacheKey(Double minimumAverageGoals, Probability accuracy, Supplier<ItemsSupplierByAverageGoals> itemsSupplierByAverageGoalsSupplier) {
            Objects.requireNonNull(itemsSupplierByAverageGoalsSupplier, "Parameter itemsSupplierByAverageGoalsSupplier must not be null.");

            this.minimumAverageGoals = minimumAverageGoals;
            this.accuracy = accuracy;
            this.itemsSupplierByAverageGoalsSupplier = itemsSupplierByAverageGoalsSupplier;
        }

        Double getMinimumAverageGoals() {
            return this.minimumAverageGoals;
        }

        Probability getAccuracy() {
            return this.accuracy;
        }

        Supplier<ItemsSupplierByAverageGoals> getItemsSupplierByAverageGoalsSupplier() {
            return this.itemsSupplierByAverageGoalsSupplier;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CacheKey cacheKey = (CacheKey) o;
            return Objects.equals(getMinimumAverageGoals(), cacheKey.getMinimumAverageGoals()) &&
                    Objects.equals(getAccuracy(), cacheKey.getAccuracy());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getMinimumAverageGoals(), getAccuracy());
        }
    }
}
