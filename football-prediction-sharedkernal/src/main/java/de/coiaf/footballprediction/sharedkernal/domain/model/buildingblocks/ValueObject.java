package de.coiaf.footballprediction.sharedkernal.domain.model.buildingblocks;

/**
 * An instance representing a DDD value object building block
 * @param <T> the value object type
 */
public interface ValueObject<T extends ValueObject<T>> {

    /**
     *
     * @param otherValueObject the value object to be compared
     * @return <code>true</code> if <code>otherValueObject</code> is not null and represents the same value as this instance.
     */
    default boolean isSameValue(T otherValueObject) {
        return this.equals(otherValueObject);
    }
}
