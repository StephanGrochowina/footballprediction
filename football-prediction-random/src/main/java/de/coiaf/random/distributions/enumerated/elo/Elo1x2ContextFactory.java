package de.coiaf.random.distributions.enumerated.elo;

import de.coiaf.footballprediction.common.cache.ElementCachingFactory;

import java.util.Objects;
import java.util.function.Supplier;

public class Elo1x2ContextFactory {
    private static final Supplier<Elo1x2ContextBuilder> DEFAULT_CONTEXT_BUILDER_SUPPLIER = Elo1x2ContextBuilder::create;

    private final ElementCachingFactory<CacheKey, Elo1x2ContextBuilder> contextFactory = ElementCachingFactory
            .createFactory(CacheKey.class, Elo1x2ContextBuilder.class)
            .initCache(CacheKey.createCacheKey(), DEFAULT_CONTEXT_BUILDER_SUPPLIER)
            .initCache(
                    CacheKey.createCacheKey(
                            WinningExpectationFunction.DEFAULT_C,
                            WinningExpectationFunction.DEFAULT_D),
                    DEFAULT_CONTEXT_BUILDER_SUPPLIER)
            .initCache(
                    CacheKey.createCacheKey(
                            WinningExpectationFunction.DEFAULT_C,
                            WinningExpectationFunction.DEFAULT_D,
                            WinningExpectationFunction.DEFAULT_HOME_ADVANTAGE),
                    DEFAULT_CONTEXT_BUILDER_SUPPLIER);

    public Elo1x2Context createContext(double eloHome, double eloAway) {
        return this.createContext(CacheKey::createCacheKey, eloHome, eloAway);
    }

    public Elo1x2Context createContext(double eloHome, double eloAway, double c, double d) {
        return this.createContext(() -> CacheKey.createCacheKey(c, d), eloHome, eloAway);
    }

    public Elo1x2Context createContext(double eloHome, double eloAway, double c, double d, double homeAdvantage) {
        return this.createContext(() -> CacheKey.createCacheKey(c, d, homeAdvantage), eloHome, eloAway);
    }

    private Elo1x2Context createContext(Supplier<CacheKey> keySupplier, double eloHome, double eloAway) {
        Elo1x2ContextBuilder contextBuilder = this.contextFactory.createValue(keySupplier, cacheKey -> cacheKey::createContextBuilder);

        return contextBuilder.buildContext(eloHome, eloAway);
    }

    static class CacheKey {
        private final Double c;
        private final Double d;
        private final Double homeAdvantage;
        private final Supplier<Elo1x2ContextBuilder> contextBuilderSupplier;

        private static CacheKey createCacheKey() {
            return new CacheKey(null, null, null, DEFAULT_CONTEXT_BUILDER_SUPPLIER);
        }

        static CacheKey createCacheKey(double c, double d) {
            return new CacheKey(c, d, null, () -> Elo1x2ContextBuilder.create(c, d));
        }

        static CacheKey createCacheKey(double c, double d, double homeAdvantage) {
            return new CacheKey(c, d, homeAdvantage, () -> Elo1x2ContextBuilder.create(c, d).applyHomeAdvantage(homeAdvantage));
        }

        private CacheKey(Double c, Double d, Double homeAdvantage, Supplier<Elo1x2ContextBuilder> contextBuilderSupplier) {
            Objects.requireNonNull(contextBuilderSupplier, "Parameter contextBuilderSupplier must not be null.");

            this.c = c;
            this.d = d;
            this.homeAdvantage = homeAdvantage;
            this.contextBuilderSupplier = contextBuilderSupplier;
        }

        Double getC() {
            return this.c;
        }

        Double getD() {
            return this.d;
        }

        Double getHomeAdvantage() {
            return this.homeAdvantage;
        }

        Elo1x2ContextBuilder createContextBuilder() {
            return this.contextBuilderSupplier.get();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CacheKey cacheKey = (CacheKey) o;
            return Objects.equals(getC(), cacheKey.getC()) &&
                    Objects.equals(getD(), cacheKey.getD()) &&
                    Objects.equals(getHomeAdvantage(), cacheKey.getHomeAdvantage());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getC(), getD(), getHomeAdvantage());
        }
    }
}
