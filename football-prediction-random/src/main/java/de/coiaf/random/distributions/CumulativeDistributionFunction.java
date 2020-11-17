package de.coiaf.random.distributions;

import de.coiaf.random.probability.Probability;
import de.coiaf.random.weight.WeightFunction;

import java.math.BigDecimal;

/**
 * @author Stephan Grochowina <stephan.grochowina@web.de>
 */
public interface CumulativeDistributionFunction<T> extends WeightFunction<T> {

    /**
     *
     * @param value
     * @return the distribution at value (i.e. the probability for x <= value)
     */
    Probability getDistribution(T value);

    /**
     *
     * @param value0
     * @param value1
     * @return the distribution between value0 and value1 (i.e. the probability for value0 < x <= value1)
     */
    Probability getDistribution(T value0, T value1);

    /**
     *
     * @param distribution
     * @return the instance of T with the lowest distribution equal or greater than distribution
     */
    T selectValue(Probability distribution);

    /**
     *
     * @param distribution
     * @return the smallest instance of T according to the order of this distribution with P(T) >= distribution
     */
    T determineQuantile(Probability distribution);

    @Override
    default BigDecimal applyWeight(T value) {
        Probability distribution = this.getDistribution(value);

        return distribution == null ? null : distribution.toBigDecimal();
    }
}
