package de.coiaf.random.distance;

import de.coiaf.random.distributions.CumulativeDistributionFunction;
import de.coiaf.random.probability.Probability;

import java.util.Objects;
import java.util.function.Function;

/**
 * Normalizes a value of type V showing the following behaviour:
 *
 * Given: a range of valid values [a, b] of type V. The interval may be bounded or unbounded,
 * i.e it is possible that a -> -oo or b -> +oo
 * For all x, y being element of [a, b]: x < y => this.apply(x) < this.apply(y)
 *
 * If a is a finite lower bound then Probability.IMPOSSIBLE.equals(this.apply(a)) is true.
 * If there is no lower bound then for lim x-> -oo Probability.IMPOSSIBLE.equals(this.apply(x)) is true.
 *
 * If b is a finite upper bound then Probability.CERTAIN.equals(this.apply(b)) is true.
 * If there is no upper bound then for lim x-> +oo Probability.CERTAIN.equals(this.apply(x)) is true.
 * @param <V> the type of the value to be normalized
 */
@FunctionalInterface
public interface ValueNormalizer<V> extends Function<V, Probability> {

    /**
     * Provides the function to normalize values.
     * @return the function to be used to normalize a value of type V
     */
    CumulativeDistributionFunction<V> getNormalizingFunction();

    default Probability apply(V value) {
        Objects.requireNonNull(value, "Parameter value should not be null");

        return this.getNormalizingFunction().getDistribution(value);
    }

}
