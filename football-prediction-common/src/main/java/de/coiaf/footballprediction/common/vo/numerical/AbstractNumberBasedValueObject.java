package de.coiaf.footballprediction.common.vo.numerical;

import java.util.Objects;
import java.util.function.Supplier;

public abstract class AbstractNumberBasedValueObject<V extends Number & Comparable<V>, VO extends AbstractNumberBasedValueObject> extends Number implements Comparable<VO> {

    private final V internalValue;

    protected AbstractNumberBasedValueObject(Supplier<V> internalValueSupplier) {
        Objects.requireNonNull(internalValueSupplier, "Parameter internalValueSupplier must not be null.");

        V value = internalValueSupplier.get();
        if (value == null) {
            throw new IllegalArgumentException("Parameter internalValueSupplier must not supply null.");
        }

        this.internalValue = value;
    }

    @Override
    public int intValue() {
        return this.internalValue.intValue();
    }

    @Override
    public long longValue() {
        return this.internalValue.longValue();
    }

    @Override
    public float floatValue() {
        return this.internalValue.floatValue();
    }

    @Override
    public double doubleValue() {
        return this.internalValue.doubleValue();
    }

    @Override
    public String toString() {
        return this.internalValue.toString();
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public boolean equals(Object other) {
        VO otherInstance = other == null || !this.getClass().isAssignableFrom(other.getClass())
                ? null : (VO) other;

        return otherInstance != null && this.compareTo(otherInstance) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getInternalValue());
    }

    protected V getInternalValue() {
        return this.internalValue;
    }
}
