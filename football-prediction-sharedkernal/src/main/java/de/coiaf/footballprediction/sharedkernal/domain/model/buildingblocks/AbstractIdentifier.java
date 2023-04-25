package de.coiaf.footballprediction.sharedkernal.domain.model.buildingblocks;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Base class for ddd buildingblock identifier
 * @param <ID>
 */
abstract class AbstractIdentifier<ID> implements Identifier<ID> {

    private final ID idValue;

    /**
     * Creates an instance of the identifier
     * @param idValueSupplier the supplier which creates an id to initialize the instance with.
     * @throws NullPointerException if idValueSupplier is null or <code>idValueSupplier.get()</code>
     *                              returns null
     */
    AbstractIdentifier(Supplier<ID> idValueSupplier) {
        this(idValueSupplier == null ? null : idValueSupplier.get());
    }

    /**
     * Creates an instance of the identifier
     * @param idValue the id value to initialize the instance with
     * @throws NullPointerException if idValue is null
     */
    AbstractIdentifier(final ID idValue) {
        Objects.requireNonNull(idValue, "Parameter idValue must not be null.");

        this.idValue = idValue;
    }

    @Override
    public ID getIdValue() {
        return this.idValue;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        boolean result = false;
        Class<?> currentClass = this.getClass();

        if (other != null && currentClass.isAssignableFrom(other.getClass())) {
            AbstractIdentifier<?> that = (AbstractIdentifier<?>) other;

            result = Objects.equals(this.getIdValue(), that.getIdValue());
        }

        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getIdValue());
    }
}
