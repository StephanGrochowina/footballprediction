package de.coiaf.random.weight;

import java.math.BigDecimal;

public interface WeightFunction<T> {

    /**
     *
     * @param value the value which should be weighted.
     * @return the weight for {@code value}
     */
    BigDecimal applyWeight(T value);

    /**
     *
     * @param index the index value which should be converted
     * @return the corresponding value of the weight function for {@code index}
     * @throws IndexOutOfBoundsException if {@code index} represents a value below or above the bounds of possible
     * values for T
     */
    T convertIndex(int index);
}
